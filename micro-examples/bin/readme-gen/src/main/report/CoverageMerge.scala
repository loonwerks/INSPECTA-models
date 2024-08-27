// #Sireum
package report

import org.sireum._
import report.CoverageMerge.{genPaperVersions, regenMergedReports, remergeVectorFiles, reportRoot}
import report.MergeUtils._

object CoverageMerge extends App {
  val genPaperVersions: B = T

  val removeUnsatFromPassing: B = F // Need to do this at least once

  val removeDump: B = T
  val regenMergedReports: B = F
  val remergeVectorFiles: B = F
  val emptyTrash: B = F

  val dsc_prefix: String = "dsc_gumbox_journal_cust"

  val reportRoot: Os.Path = Os.path("/opt") / "santos" / "jenkins" / dsc_prefix / "tester"

  val filter: B = F
  val filters: ISZ[String] = ISZ("tc")

  def main(args: ISZ[String]): Z = {
    merge()
    return 0
  }

  def merge(): Unit = {

    var map: cmap = HashSMap.empty
    var results: ISZ[Results] = ISZ()
    var trashes: ISZ[Os.Path] = ISZ()
    def walk(p: Os.Path): Unit = {
      if (p.isFile && p.name == "sireum.version") {
        //println(p.up.toUri)
        val passingP = fetch(".passing", p.up.list).get
        val failingP = fetch(".failing", p.up.list).get
        val unsatP = fetch(".unsat", p.up.list).get
        val execP = fetch(".exec", p.up.list).get
        val csvP = fetch(".csv", p.up.list).get
        val coverageP = fetch(".coverage", p.up.list).get
        val dumpP = fetch(".dump", p.up.list)
        val project = p.up.up.up.up.name
        val dscTestName = p.up.up.up.name
        val componentName = ops.StringOps(dscTestName).replaceAllLiterally("_DSC_UnitTests", "")
        val configName = p.up.up.name
        val timeout = Z(p.up.name).get

        val hamrRoot = Projects.projects.get(project).get.hamrRoot
        val configJsonP: Os.Path = {
          val configFileName = s".$dscTestName.scala.json"
          val cached = reportRoot / project / dscTestName / configFileName
          if (cached.exists) {
            //println(s"Using cached config for ${dscTestName}")
            cached
          } else {
            val actual = Os.Path.walk(hamrRoot / "src" / "test" / "bridge", F, F, p => p.name == configFileName)(0)
            cached.writeOver(actual.read)
            actual
          }
        }

        if (removeUnsatFromPassing) {
          cleanupPassingFile(passingP, unsatP)
        }

        val behaviorP = Os.Path.walk(hamrRoot / "src" / "main" / "component", F, F, p => p.name == s"$componentName.scala")(0)
        val gumboxP: Option[Os.Path] = {
          val ll = Os.Path.walk(hamrRoot / "src" / "main" / "bridge", F, F, p => p.name == s"${componentName}_GumboX.scala")
          if (ll.nonEmpty) Some(ll(0))
          else None()
        }
        val componentPackage = s"${behaviorP.up.up.name}.${behaviorP.up.name}"
        val r = Results(
          project = project,
          component = componentName,
          componentNickName = getNickName(componentName),
          dscTestName = dscTestName,
          componentPackage = componentPackage,
          configName = configName,
          configDescription = getConfigDescription(configName, configJsonP),
          timeout = timeout,
          passing = numVectors(passingP),
          failing = numVectors(failingP),
          unsat = numVectors(unsatP),
          passingP = passingP,
          failingP = failingP,
          unsatP = unsatP,
          exec = execP,
          csv = csvP,
          coverageP = coverageP,
          dumpP = dumpP,
          configJsonP = configJsonP,
          gumboXP = gumboxP,
          behaviorP = behaviorP,
          actualRunDir = passingP.up
        )
        if (!filter || ops.ISZOps(filters).contains(project)) {
          results = results :+ r
          map = add2Map(r, map)
        } else {
          println(s"Not process ${project} due to filter ...")
        }
        if (removeDump && r.dumpP.nonEmpty) {
          if (dumpLoc(r.project) != r.dumpP.get) {
            r.dumpP.get.removeAll()
            //println(s"     Would delete ${r.dumpP.get}")
          } else {
            //println(s"Would keep ${r.dumpP.get}")
          }
        }

      } else if (p.isDir) {
        val x = ops.StringOps(p.name)
        if(p.name == "trash") {
          trashes = trashes :+ p
        }
        if (!x.endsWith(".coverage") && !x.endsWith(".dump")) {
          for (l <- p.list) {
            walk(l)
          }
        }
      }
    }

    walk(reportRoot)
    //println()
    //println(map)

    if(emptyTrash) {
      for (t <- trashes) {
        t.removeAll()
      }
    }
    var topLevelProjs: ISZ[(String, Os.Path)] = ISZ()

    for (projects <- map.entries) {
      var projectResults = ISZ[(String, ISZ[(ISZ[Os.Path], ISZ[Results])])]()
      for (component <- projects._2.entries) {
        var componentResults = ISZ[Results]()
        for (configs <- component._2.entries) {
          var configResults = ISZ[Results]()
          for (timeouts <- configs._2.entries) {
            val results = timeouts._2
            configResults = configResults :+ results
            componentResults = componentResults :+ results
            addTimeoutReport(results, reportRoot)
          }
          val configurationRoot = configResults(0).actualRunDir.up
          addConfigurationReport(configResults, configurationRoot, reportRoot)
        }
        val componentRoot = componentResults(0).actualRunDir.up.up

        val splits: ISZ[(ISZ[Os.Path], ISZ[Results])] = splitResults(componentResults, componentRoot)
        projectResults = projectResults :+ (component._1, splits)
        addComponentReport(splits, component._2.keys, componentRoot, reportRoot)
      }
      val projectRoot = projectResults(0)._2(0)._2(0).actualRunDir.up.up.up
      val components: ISZ[(String, String)] = for (p <- projects._2.keys) yield (p, getNickName(p))
      addProjectReport(projectResults, components, projectRoot, reportRoot)

      topLevelProjs = topLevelProjs :+ (projects._1, projectRoot)
    }

    addRootReport(reportRoot, topLevelProjs)
  } // end of merge
}

object MergeUtils {
  def addTimeoutReport(r: Results, reportRoot: Os.Path): Unit = {
    val root1 = r.passingP.up

    val reportDir = root1

    val r0 = reportDir.relativize(reportRoot / "report.html")
    val r1 = reportDir.relativize(reportRoot / r.project / "report.html")
    val r2 = reportDir.relativize(reportRoot / r.project / r.dscTestName / "report.html")
    val r3 = reportDir.relativize(reportRoot / r.project / r.dscTestName / r.configName / "report.html")

    val cookieCrumb = cookies(ISZ(("Report", r0), (r.project, r1), (r.componentNickName, r2), (r.configName, r3)))

    val resultTable: ST = genResultTable(
      results = ISZ(r),
      parentDir = reportDir,
      reportDir = reportDir,
      isActualRun = T
    )

    val html = reportTemplate(cookieCrumbs = cookieCrumb,
      purpose =
        st"""This presents the combined coverage information along with the number of passing/failing/unsat test vectors
            |for the ${r.configName} configuration with a ${r.timeout} second timeout""",
      project = st"""<a href="$r1">${r.project}</a>""",
      system = Some(st"""<a href="$r2">${r.componentNickName}</a>"""),
      families = Some(st"""<a href="$r3">${r.configName}</a> : ${r.configDescription}"""),
      timeouts = Some(st"""${r.timeout}"""),
      resultTables = ISZ(resultTable),
      reportDir = reportDir,
    )
    val report = reportDir / "report.html"
    report.writeOver(html.render)
    println(s"Wrote: ${report}")
  }

  def getEntrypointMethodName(r: Results): String = {
    val o = ops.StringOps(r.configName)
    if (o.contains("Compute")) {
      return "timeTriggered"
    } else if (o.contains("Initialize")) {
      return "initialise"
    } else {
      halt(s"Couldn't determine entrypoint for ${r.configName}")
    }
  }

  @enum object EntrypointType {
    "Initialize"
    "Compute"
  }

  def splitResults(results: ISZ[Results], outputDir: Os.Path): ISZ[(ISZ[Os.Path], ISZ[Results])] = {
    var splits = HashSMap.empty[EntrypointType.Type, HashSMap[Z, ISZ[Results]]]
    for (r <- results) {
      val typ: EntrypointType.Type = if (ops.StringOps(r.configName).contains("Compute")) EntrypointType.Compute else EntrypointType.Initialize
      val e: HashSMap[Z, ISZ[Results]] = splits.get(typ) match {
        case Some(ee) => ee
        case _ => HashSMap.empty[Z, ISZ[Results]]
      }
      val e2: ISZ[Results] = e.get(r.timeout) match {
        case Some(ee2) => ee2
        case _ => ISZ()
      }
      splits = splits + typ ~> (e + (r.timeout ~> (e2 :+ r)))
    }

    var ret = ISZ[(ISZ[Os.Path], ISZ[Results])]()
    for (typ <- splits.entries) {
      var timeoutResults: ISZ[Results] = ISZ()
      var configPaths: Set[Os.Path] = Set.empty
      for (timeout <- typ._2.entries) {
        configPaths = configPaths ++ (for (e <- timeout._2) yield outputDir / e.configName)
        if (timeout._2.size == 1) {
          // no need to merge
          timeoutResults = timeoutResults :+ timeout._2(0)
        } else {
          timeoutResults = timeoutResults :+
            mergeResults(
              results = timeout._2, remergeVectorFiles = remergeVectorFiles, regenMergedReports = regenMergedReports,
              vectorFilePrefix = s"${typ._1.name}_${timeout._1.string}", jacocoDirName = s"jacocoCoverage_${typ._1}_${timeout._1}", relativeTo = outputDir)
        }
      }
      ret = ret :+ (configPaths.elements, timeoutResults)
    }
    return ret
  }

  def addConfigurationReport(configResults: ISZ[Results], configRoot: Os.Path, reportRoot: Os.Path): Unit = {
    val reportDir = configRoot

    val r = configResults(0)
    val r0 = reportDir.relativize(reportRoot / "report.html")
    val r1 = reportDir.relativize(reportRoot / r.project / "report.html")
    val r2 = reportDir.relativize(reportRoot / r.project / r.dscTestName / "report.html")

    val stimeouts = ops.ISZOps(for (r <- configResults) yield r.timeout).sortWith((a, b) => a <= b)
    val timeOuts: ISZ[String] = for (t <- stimeouts) yield s"<a href=\"${(reportDir.relativize(reportDir / t.string / "report.html")).string}\">${t.string}</a>"

    val cookieCrumb = cookies(ISZ(("Report", r0), (r.project, r1), (r.componentNickName, r2)))

    val resultTables = ISZ(
      genResultTable(
        results = configResults,
        parentDir = reportDir,
        reportDir = reportDir,
        isActualRun = T))

    val html = reportTemplate(cookieCrumbs = cookieCrumb,
      purpose =
        st"""This presents the combined coverage information along with the number of passing/failing/unsat test vectors
            |for the ${r.configName} configuration""",
      project = st"""<a href="$r1">${r.project}</a>""",
      system = Some(st"""<a href="$r2">${r.componentNickName}</a>"""),
      families = Some(st"""${r.configName} : ${r.configDescription}"""),
      timeouts = Some(st"""${(timeOuts, " ")}"""),
      resultTables = resultTables,
      reportDir = reportDir
    )
    val report = reportDir / "report.html"
    report.writeOver(html.render)
    println(s"Wrote: ${report}")
  }

  @datatype class CoverageInfo(val behavior: ST, val gumbox: Option[ST], val fullReport: ST)

  def addComponentReport(allConfigResults: ISZ[(ISZ[Os.Path], ISZ[Results])], configurations: ISZ[String], componentRoot: Os.Path, reportRoot: Os.Path): Unit = {
    val reportDir = componentRoot

    val r: Results = allConfigResults(0)._2(0)

    val r0 = reportDir.relativize(reportRoot / "report.html")
    val r1 = reportDir.relativize(reportRoot / r.project / "report.html")

    val configLinks: ISZ[String] = for (t <- configurations) yield
      s"""<a href="${(reportDir.relativize(reportDir / t / "report.html")).string}">${t}</a><br>"""

    val cookieCrumb = cookies(ISZ(("Report", r0), (r.project, r1)))

    assert (allConfigResults.size == 1 || allConfigResults.size == 2, allConfigResults.size)

    var resultTablesX: ISZ[ST] = ISZ()
    for (e <- allConfigResults) {
      val configLinks: ISZ[String] = for (tr <- e._1) yield s"""<a href="${(componentRoot.relativize(tr / "report.html")).string}">${tr.name}</a><br>"""

      val table =
        genResultTable(
          results = e._2,
          parentDir = componentRoot,
          reportDir = reportDir,
          isActualRun = F)

      resultTablesX = resultTablesX :+
        st"""<td valign="bottom" style='vertical-align: bottom'>
            |  ${(configLinks, "\n")}
            |  $table
            |</td>"""
    }

    val resultsTables: ISZ[ST] =
      if (!genPaperVersions) {
        ISZ(st"<table><tr>${(resultTablesX, "<td>&nbsp;&nbsp</td>\n")}</tr></table>")
      } else {
        if (resultTablesX.size == 2) {
          ISZ(
            st"""${resultTablesX(1)}
                |<br>
                |${resultTablesX(0)}""")
        } else {
          resultTablesX
        }
      }

    val html = reportTemplate(
      cookieCrumbs = cookieCrumb,
      purpose =
        st"""The presents the combined coverage information along with the number of passing/failing/unsat test vectors
            |for running all the configurations for ${r.componentNickName}""",
      project = st"""<a href="$r1">${r.project}</a>""",
      system = Some(st"${r.componentNickName}"),
      families = Some(st"${(configLinks, " ")}"),
      timeouts = None(),
      resultTables = resultsTables,
      reportDir = reportDir
    )

    val report = reportDir / "report.html"
    report.writeOver(html.render)
    println(s"Wrote: ${report}")
  }

  def addProjectReport(allConfigResults: ISZ[(String, ISZ[(ISZ[Os.Path], ISZ[Results])])], components: ISZ[(String, String)], projectRoot: Os.Path, reportRoot: Os.Path): Unit = {
    val reportDir = projectRoot

    val r: Results = allConfigResults(0)._2(0)._2(0)

    val r0 = reportDir.relativize(reportRoot / "report.html")

    val componentLinks: ISZ[String] = for (component <- components) yield s"<a href=\"${(reportDir.relativize(reportDir / s"${component._1}_DSC_UnitTests" / "report.html")).string}\">${component._2}</a>"

    val cookieCrumb = cookies(ISZ(("Report", r0)))

    var resultTables: ISZ[ST] = ISZ()
    for (component <- allConfigResults) {
      var resultTablesX: ISZ[ST] = ISZ()
      for (e <- component._2) {
        val configLinks: ISZ[String] = for (tr <- e._1) yield s"""<a href="${(projectRoot.relativize(tr / "report.html")).string}">${tr.name}</a><br>"""

        val table =
          genResultTable(
            results = e._2,
            parentDir = projectRoot,
            reportDir = reportDir,
            isActualRun = F)

        resultTablesX = resultTablesX :+
          st"""<td valign="bottom" style='vertical-align: bottom'>
              |  ${(configLinks, "\n")}
              |  $table
              |</td>"""
      }

      if (!genPaperVersions) {
        resultTables = resultTables :+
          st"""<h3>${getNickName(component._1)} <a style="font-size: medium; font-weight: normal;" href="${(reportDir.relativize(reportDir / s"${component._1}_DSC_UnitTests" / "report.html")).string}">link</a></h3>
              |
              |<table><tr>${(resultTablesX, "<td>&nbsp;&nbsp</td>\n")}</tr></table>
              |"""
      } else {
        val paperFormatted: ST = if (resultTablesX.size == 2) {
          st"""${resultTablesX(1)}
              |<br>
              |${resultTablesX(0)}"""
        } else {
          resultTablesX(0)
        }

        resultTables = resultTables :+
          st"""<h3>${getNickName(component._1)} <a style="font-size: medium; font-weight: normal;" href="${(reportDir.relativize(reportDir / s"${component._1}_DSC_UnitTests" / "report.html")).string}">link</a></h3>
              |
              |$paperFormatted
              |"""
      }
    }

    val html = reportTemplate(
      cookieCrumbs = cookieCrumb,
      purpose =
        st"""This presents the combined coverage information along with the number of passing/failing/unsat test vectors
            |for each of the ${r.project}'s components under testing""",
      project = st"${r.project}",
      system = Some(st"${(componentLinks, "<br><br> ")}"),
      resultTables = resultTables,
      families = None(),
      timeouts = None(),
      reportDir = reportDir
    )
    val report = reportDir / "report.html"
    report.writeOver(html.render)
    println(s"Wrote: ${report}")
  }


  def addRootReport(root: Os.Path, topLevelProjs: ISZ[(String, Os.Path)]): Unit = {
    val projects: ISZ[ST] = for (p <- topLevelProjs) yield st"""<a href="${root.relativize(p._2 / "report.html")}">${p._1}</a>"""
    val html = reportTemplate(
      cookieCrumbs = st"",
      purpose = st"",
      project = st"${(projects, "<br><br>")}",
      resultTables = ISZ(),
      system = None(),
      families = None(),
      timeouts = None(),
      reportDir = root
    )
    val f = root / "report.html"
    f.writeOver(html.render)
    println(s"Wrote: $f")
  }

  @datatype class Results(val project: String,
                          val component: String,
                          val componentNickName: String,
                          val componentPackage: String,
                          val dscTestName: String,
                          val configName: String,
                          val timeout: Z,

                          val configDescription: String,

                          val passing: Z,
                          val failing: Z,
                          val unsat: Z,

                          val passingP: Os.Path,
                          val failingP: Os.Path,
                          val unsatP: Os.Path,

                          val exec: Os.Path,
                          val csv: Os.Path,
                          val coverageP: Os.Path,
                          val dumpP: Option[Os.Path],

                          val configJsonP: Os.Path,
                          val gumboXP: Option[Os.Path],
                          val behaviorP: Os.Path,

                          val actualRunDir: Os.Path)

  def fetch(suffix: String, paths: ISZ[Os.Path]): Option[Os.Path] = {
    val ret: Option[Os.Path] = (ops.ISZOps(paths).filter(f => ops.StringOps(f.name).endsWith(suffix)) match {
      case ISZ(e) => Some(e)
      case _ => None()
    })
    return ret
  }

  //                     proj             component        config           timeout
  type cmap = HashSMap[String, HashSMap[String, HashSMap[String, HashSMap[Z, Results]]]]

  def add2Map(r: Results, projMap: HashSMap[String, HashSMap[String, HashSMap[String, HashSMap[Z, Results]]]]): cmap = {
    val compMap: HashSMap[String, HashSMap[String, HashSMap[Z, Results]]] = projMap.get(r.project) match {
      case Some(componentMap) => componentMap
      case _ => HashSMap.empty[String, HashSMap[String, HashSMap[Z, Results]]]()
    }
    val configMap: HashSMap[String, HashSMap[Z, Results]] = compMap.get(r.component) match {
      case Some(configMap) => configMap
      case _ => HashSMap.empty[String, HashSMap[Z, Results]]()
    }
    val timMap: HashSMap[Z, Results] = configMap.get(r.configName) match {
      case Some(timmap) => timmap
      case _ => HashSMap.empty[Z, Results]()
    }
    assert(!timMap.contains(r.timeout))

    return projMap + r.project ~> (compMap + (r.component ~> (configMap + r.configName ~> (timMap + (r.timeout ~> r)))))
  }

  def numVectors(p: Os.Path): Z = {
    return ops.StringOps(p.read).split(c => c == '\n').size
  }

  def getConfigDescription(configName: String, p: Os.Path): String = {
    var c = ops.StringOps(ops.StringOps(ops.StringOps(p.read).replaceAllLiterally("[ ", "")).replaceAllLiterally(" ]", ""))
    for (e <- c.split(c => c == ',')) {
      c = ops.StringOps(ops.StringOps(e).trim)
      val list = ops.StringOps(c.substring(1, c.size - 1)).split(c => c == '|')
      if (list(0) == configName) {
        return list(1)
      }
    }
    halt(s"Couldn't find description for ${configName}")
  }

  def cookies(ps: ISZ[(String, Os.Path)]): ST = {
    val x: ISZ[ST] = for (p <- ps) yield st"""<a href="${p._2}">${p._1}</a>"""
    return st"${(x, " > ")}"
  }

  def getLineCoverage(methodName: String, r: Results, reportDir: Os.Path): (Option[ST], Option[ST]) = {
    val p: Os.Path = r.coverageP / r.componentPackage
    val behavior = p / s"${r.component}$$.html"
    //val summary = reportDir.relativize(p / s"${r.component}$$.html")
    val gumbox = p / s"${r.component}_GumboX$$.html"

    def mine(pos: Z, contents: ops.StringOps): (Z, Z) = {
      val p2 = contents.stringIndexOfFrom("id=\"h", pos)
      val p2_1 = contents.stringIndexOfFrom("\"", p2 + 4)
      val missed = contents.substring(p2_1 + 2, contents.stringIndexOfFrom("</td>", p2_1))
      val p3 = contents.stringIndexOfFrom("id=\"i", p2)
      val p3_1 = contents.stringIndexOfFrom("\"", p3 + 4)
      val total = contents.substring(p3_1 + 2, contents.stringIndexOfFrom("</td>", p3_1))
      if (Z(total).isEmpty) {
        println(total)
        println()
      }
      if (Z(total).isEmpty) {
        println(total)
      }
      val totalz = Z(total).get
      val covered = totalz - Z(missed).get
      return (covered, totalz)
    }

    val b: Option[ST] = if (behavior.exists) {
      val contents = ops.StringOps(behavior.read)
      val info = mine(contents.stringIndexOf(methodName), contents)
      Some(st"${info._1}/${info._2}")
    } else {
      None()
    }
    val g: Option[ST] = if (gumbox.exists) {
      val suffix: String = if (methodName == "timeTriggered") "compute" else "initialize"
      val contents = ops.StringOps(gumbox.read)
      var pos = 0
      var covered: Z = 0
      var total: Z = 0
      while (pos >= 0 && pos < contents.size) {
        pos = contents.stringIndexOfFrom(suffix, pos)
        val pos2 = contents.stringIndexOfFrom("(", pos)
        if (pos >= 0 && pos2 >= 0) {
          val mname = contents.substring(pos, pos2)
          if (!ops.StringOps(mname).contains("Container")) {
            val (x, y) = mine(pos2, contents)
            //println(s"$x/$y - $mname")
            covered = covered + x
            total = total + y
          }
          pos = pos2 + 1
        }
      }
      Some(st"$covered/$total")
    } else {
      None()
    }
    //val gumboxR = reportDir.relativize(gumbox)
    //val optGumbox: Option[ST] =
    //  if (gumbox.exists) Some(st"<a href=$gumboxR>link</a>")
    // else None()
    //return (st"""<a href=$summary>link</a>""", optGumbox)
    return (b, g)
  }

  def getCoverage(r: Results, reportDir: Os.Path): (ST, Option[ST]) = {
    val p: Os.Path = r.coverageP / r.componentPackage
    val summary = reportDir.relativize(p / s"${r.component}$$.html")
    val gumbox = p / s"${r.component}_GumboX$$.html"
    val gumboxR = reportDir.relativize(gumbox)
    val optGumbox: Option[ST] =
      if (gumbox.exists) Some(st"$gumboxR")
      else None()
    return (st"$summary", optGumbox)
  }

  def genResultTable(results: ISZ[Results], parentDir: Os.Path, reportDir: Os.Path, isActualRun: B): ST = {
    var tableEntries: ISZ[ST] = ISZ()
    for (r <- ops.ISZOps(results).sortWith((a, b) => a.timeout <= b.timeout)) {
      def optLink(num: Z, p: Os.Path): ST = {
        return (
          if (num > 0) st"""<a href="${parentDir.relativize(p)}">$num</a>"""
          //else if (genPaperVersions && num != 0) st"<a>$num</a>"
          else st"$num"
          )
      }

      val subs: (ST, Option[ST]) = getCoverage(r, parentDir)
      val codeCoverage = getLineCoverage(getEntrypointMethodName(r), r, parentDir)
      val entrypointCoverageOpt: ST = {
        if (codeCoverage._1.nonEmpty) {
          st"""<a href="${subs._1}">${codeCoverage._1.get}</a>"""
        } else {
          st"??"
        }
      }
      val behaviorCoverage: ST =
        if (codeCoverage._2.nonEmpty)
          st"""<a href="${subs._2}">${codeCoverage._2.get}</a>"""
        else
          st"NA"
      val timeoutSt: ST =
        if (isActualRun) st"""<td><a href="${reportDir.relativize(r.unsatP.up / "report.html")}">${r.timeout}</a></td>"""
        else st"""<td>${r.timeout}</td>"""

      val coverage = CoverageInfo(subs._1, subs._2, st"""<a href="${parentDir.relativize(r.coverageP)}/index.html">link</a>""")
      val cells = ISZ(
        timeoutSt,
        st"""<td id=col_a title="Number of test vectors that passed">${optLink(r.passing, r.passingP)}</td>""",
        st"""<td id=col_a title="Number of test vectors that failed">${optLink(r.failing, r.failingP)}</td>""",
        st"""<td id=col_a title="Number of test vectors that failed to satisfy filter">${optLink(r.unsat, r.unsatP)}</td>""",
        st"<td>${entrypointCoverageOpt}</td>",
        st"<td>$behaviorCoverage</td>",
        st"<td>${coverage.fullReport}</td>"
      )
      tableEntries = tableEntries :+ st"<tr>${(cells, "\n")}</tr>"
    }

    def colName(str: String): String = {
      return if (genPaperVersions) s"${ops.StringOps(str).first}" else str
    }

    val stats =
      st"""<table class=resultTable border=1>
          |<th>${colName("Timeouts")}</th>
          |<th>${colName("Passing<br>Tests")}</th>
          |<th>${colName("Failing<br>Tests")}</th>
          |<th>${colName("Unsat<br>Tests")}</th>
          |<th>Behavior<br>Coverage</th>
          |<th>GumboX<br>Coverage</th>
          |<th>Full<br>Coverage</th>
          |${(tableEntries, "\n")}
          |</table>"""
    return stats
  }

  def reportTemplate(cookieCrumbs: ST,
                     purpose: ST,
                     project: ST,
                     system: Option[ST],
                     families: Option[ST],
                     timeouts: Option[ST],
                     resultTables: ISZ[ST],
                     reportDir: Os.Path): ST = {

    @strictpure def wrap(n: String, title: String, o: Option[ST]): Option[ST] =
      if (o.nonEmpty) Some(st"""<tr><td id=col_a title="$title">${n}: </td><td>${o.get}<br><br></td></tr>""")
      else None()

    return (
      st"""<html>
          |  <head>
          |    <style>
          |      *{ font-family: monospace }
          |
          |      a, a:visited {
          |        color: blue;
          |        text-decoration: none;
          |      }
          |
          |      table.resultTable {
          |        border: collapse;
          |        table-layout: fixed;
          |        font-family: monospace;
          |      }
          |      table.resultTable th {
          |        background-color: #E5E4E2;
          |        padding-left: 5px;
          |        padding-right: 5px;
          |      }
          |      table.resultTable td {
          |        padding-left: 5px;
          |        padding-right: 5px;
          |      }
          |      table.resultTable tr:nth-child(odd) { background-color: #f2f2f2; }
          |      td { vertical-align: top; }
          |      #col_a { font-weight: bold; }
          |      </style>
          |    </head>
          |  </head>
          |  <body>
          |$cookieCrumbs
          |<br><br><br>
          |
          |$purpose
          |<br><br><br>
          |
          |<table>
          |  <tr><td id=col_a title="Project name">Projects:</td><td>$project<br><br></td></tr>
          |    ${wrap("Components", "The component being tested", system)}
          |    ${wrap("Configurations", "The test configuration(s) used during testing", families)}
          |    ${wrap("Timeouts", "The timeout(s) used while generating test vectors", timeouts)}
          |</table>
          |
          |  ${(resultTables, "<br>\n<br>\n")}
          |
          |  </body>
          |</html>
          |""")
  }

  def mergeResults(results: ISZ[Results], remergeVectorFiles: B, regenMergedReports: B, vectorFilePrefix: String, jacocoDirName: String, relativeTo: Os.Path): Results = {
    val passingP = relativeTo / s"${vectorFilePrefix}-combined.passing"
    val failingP = relativeTo / s"${vectorFilePrefix}-combined.failing"
    val unsatP = relativeTo / s"${vectorFilePrefix}-combined.unsat"

    if (remergeVectorFiles) {
      //for (old <- relativeTo.list if old.ext == "passing" || old.ext == "failing" || old.ext == "unsat") {
      //  old.removeAll()
      //}
      var passingV: ISZ[String] = ISZ()
      var failingV: ISZ[String] = ISZ()
      var unsatV: ISZ[String] = ISZ()
      for (r <- results) {
        passingV = passingV :+ ops.StringOps(r.passingP.read).trim
        failingV = failingV :+ ops.StringOps(r.failingP.read).trim
        unsatV = unsatV :+ ops.StringOps(r.unsatP.read).trim
      }
      passingP.writeOver(st"${(passingV, "\n")}".render)
      failingP.writeOver(st"${(failingV, "\n")}".render)
      unsatP.writeOver(st"${(unsatV, "\n")}".render)
      println(s"Wrote: ${passingP}")
      println(s"Wrote: ${failingP}")
      println(s"Wrote: ${unsatP}")
    }
    val passing = passingP.readLines.size
    val failing = failingP.readLines.size
    val unsat = unsatP.readLines.size

    val execsPaths: ISZ[Os.Path] = for(r <- results) yield r.exec

    //assert (dumpLoc.nonEmpty, "Wasn't able to find a dump")
    val dumpLocal = dumpLoc(results(0).project)

    val jacocoOutDir = relativeTo / jacocoDirName

    if (regenMergedReports) {
      val jacocoCli = Os.home / "devel" / "sireum" / "kekinian" / "lib" / "jacococli.jar"

      val csv = relativeTo / "jacoco.csv"

      val javaExe = Sireum.javaHomeOpt.get / "bin" / (if (Os.isWin) "java.exe" else "java")

      val execs: ISZ[String] = for (x <- execsPaths) yield x.string

      println(s"Working on ${relativeTo.string} ...")
      val commands = ISZ[String](javaExe.string, "-jar", jacocoCli.string, "report") ++ execs ++ ISZ[String]("--encoding",
        "UTF-8", "--classfiles", dumpLocal.string, "--csv", csv.string, "--html", jacocoOutDir.string, "--sourcefiles", dumpLocal.string)

      Os.proc(commands).console.echo.runCheck()
      println()
    }

    val x = results(0)(
      passing = passing, failing = failing, unsat = unsat,
      passingP = passingP, failingP = failingP, unsatP = unsatP,
      coverageP = jacocoOutDir)

    return x
  }

  def cleanupPassingFile(passingP: Os.Path, unsatP: Os.Path): Unit = {
    val unsat = unsatP.readLines
    if (unsat.isEmpty) {
      return
    }
    println(s"Processing ${passingP.name} vs ${unsatP.name}")
    val uops = ops.ISZOps(unsat)
    val passing = passingP.readLines
    var clean: ISZ[String] = ISZ()
    for (p <- passing) {
      if (!uops.contains(p)) {
        clean = clean :+ p
      }
    }
    println(s"  Removed ${passing.size - clean.size}")
    val out = st"${(clean, "\n")}".render

    passingP.writeOver(out)
    println(s"Wrote ${passingP}")
  }

  def dumpLoc(projectName: String): Os.Path = {
    val ret: Os.Path = projectName match {
      case "isolette" => reportRoot / projectName / "Manage_Alarm_impl_thermostat_monitor_temperature_manage_alarm_DSC_UnitTests/Default_Initialize_Config/1/isolette_isolette-ma__1_mac-mini-m1-jacoco.dump"
      case "rts" => reportRoot / projectName / "Actuator_i_actuationSubsystem_saturationActuatorUnit_saturationActuator_actuator_DSC_UnitTests/Default_Initialize_Config/1/rts_rts-saturationActuator__1_mac-mini-m1-jacoco.dump"
      case "tc" => reportRoot / projectName / "OperatorInterfacePeriodic_p_tcproc_operatorInterface_DSC_UnitTests/Default_Initialize_Config/1/tc_tc-operator-interface__1_mac-mini-m1-jacoco.dump"
    }
    assert(ret.exists, ret)
    return ret
  }

  def getNickName(s: String): String = {
    val nicknames = Map.empty[String, String] ++
      ISZ(
        "Manage_Alarm_impl_thermostat_monitor_temperature_manage_alarm" ~> "Manage Alarm (MA)",
        "Manage_Heat_Source_impl_thermostat_regulate_temperature_manage_heat_source" ~> "Manage Heat Source (MHS)",
        "Manage_Monitor_Interface_impl_thermostat_monitor_temperature_manage_monitor_interface" ~> "Manage Monitor Interface (MMI)",
        "Manage_Monitor_Mode_impl_thermostat_monitor_temperature_manage_monitor_mode" ~> "Manage Monitor Mode (MMM)",
        "Manage_Regulator_Interface_impl_thermostat_regulate_temperature_manage_regulator_interface" ~> "Manage Regulator Interface (MRI)",
        "Manage_Regulator_Mode_impl_thermostat_regulate_temperature_manage_regulator_mode" ~> "Manage Regulator Mode (MRM)",

        "FanPeriodic_p_tcproc_fan" ~> "Fan",
        "OperatorInterfacePeriodic_p_tcproc_operatorInterface" ~> "Operator Interface",
        "TempControlPeriodic_p_tcproc_tempControl" ~> "Temp Control",
        "TempSensorPeriodic_p_tcproc_tempSensor" ~> "Temp Sensor",


        "CoincidenceLogic_i_actuationSubsystem_actuationUnit1_pressureLogic_coincidenceLogic" ~> "au1_press_coincidenceLogic",
        "CoincidenceLogic_i_actuationSubsystem_actuationUnit1_saturationLogic_coincidenceLogic" ~> "au1_satLogic_coincidenceLogic",
        "CoincidenceLogic_i_actuationSubsystem_actuationUnit1_temperatureLogic_coincidenceLogic" ~> "au1_temp_coincidenceLogic",
        "OrLogic_i_actuationSubsystem_actuationUnit1_tempPressureTripOut_orLogic" ~> "au1_tempPressTripOut_orLogic",

        "CoincidenceLogic_i_actuationSubsystem_actuationUnit2_pressureLogic_coincidenceLogic" ~> "au2_press_coincidenceLogic",
        "CoincidenceLogic_i_actuationSubsystem_actuationUnit2_saturationLogic_coincidenceLogic" ~> "au2_sat_coincidenceLogic",
        "CoincidenceLogic_i_actuationSubsystem_actuationUnit2_temperatureLogic_coincidenceLogic" ~> "au2_temp_coincidenceLogic",
        "OrLogic_i_actuationSubsystem_actuationUnit2_tempPressureTripOut_orLogic" ~> "au2_tempPressTripOut_orLogic",

        "OrLogic_i_actuationSubsystem_tempPressureActuatorUnit_actuateTempPressureActuator_orLogic" ~> "TPAU_actTempPA_orLogic",
        "Actuator_i_actuationSubsystem_tempPressureActuatorUnit_tempPressureActuator_actuator" ~> "TPAU_tempPressA_actuator",

        "OrLogic_i_actuationSubsystem_saturationActuatorUnit_actuateSaturationActuator_orLogic" ~> "SAU_actSatActuator_orLogic",
        "Actuator_i_actuationSubsystem_saturationActuatorUnit_saturationActuator_actuator" ~> "SAU_satActuator_actuator"
      )

    val ret: String = nicknames.get(s) match {
      case Some(n) => n
      case _ => s
    }
    return ret
  }
}