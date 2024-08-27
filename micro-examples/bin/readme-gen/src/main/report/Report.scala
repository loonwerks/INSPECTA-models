// #Sireum
package report

import org.sireum._
import org.sireum.hamr.act.periodic.PeriodicUtil
import org.sireum.hamr.arsit.HAMR.OS.CAmkES
import org.sireum.hamr.codegen.common.StringUtil
import org.sireum.hamr.codegen.common.symbols.{AadlComponent, AadlProcessor, AadlThread, SymbolTable}
import org.sireum.hamr.ir
import org.sireum.message.{Position, Reporter}
import report.ReadmeGen.{CAmkES, Microkit, Project, Sel4Config, projects, repoRootDir, vm}
import report.Report._

@sig trait Level {
  def tag: String
}

@datatype class ReportBlock(val tag: String,
                            val content: Option[ST]) extends Level

@datatype class ReportLevel(val tag: String,
                            val title: Option[ST],
                            val description: Option[ST],
                            val content: ISZ[ReportBlock],
                            val subLevels: ISZ[ReportLevel]) extends Level

object Report {
  val camkes: String = "CAmkES"

  val htmlDir: String = "https://people.cs.ksu.edu/~santos_jenkins/pub/hamr-system-testing-case-studies"

  def wrapWithTag(tag: String, isTitle: B, content: Option[ST]): ST = {
    assert (!seenTags.contains(tag), s"$tag already exists")
    seenTags = seenTags + tag
    val (start, end): (String, String) = (s"<!--start__${tag}-->", s"<!--end____${tag}-->")
    val ret: ST = {
      if (isTitle) st"${start}${content}${end}"
      else
        st"""${start}
            |${content}
            |${end}"""
    }
    return ret
  }

  @strictpure def getCookies(cookies: ISZ[Level]): ST = if(cookies.isEmpty) st"" else st"${(for (c <- cookies) yield c.tag, "_")}_"
  @strictpure def getBlockTag(block: ReportBlock, cookies: ISZ[Level]): String = st"${getCookies(cookies)}${block.tag}".render
  @strictpure def getTitleTag(reportLevel: ReportLevel, cookies: ISZ[Level]): String = st"${getCookies(cookies)}${reportLevel.tag}-title".render

  @strictpure def getDescTag(reportLevel: ReportLevel, cookies: ISZ[Level]): String = st"${getCookies(cookies)}${reportLevel.tag}-description".render

  def overwrite(readmeLoc: Os.Path, report: ReportLevel): Unit = {
    def renderLevel(l: ReportLevel, pos: Z, cookies: ISZ[Level]): ST = {
      val hashes = st"${(for (i <- 0 to pos) yield "#", "")}"

      def renderBlock(block: ReportBlock): ST = {
        wrapWithTag(getBlockTag(block, cookies :+ l), F, block.content)
      }

      return (
        st"""$hashes ${wrapWithTag(getTitleTag(l, cookies), T, l.title)}
            |${wrapWithTag(getDescTag(l, cookies), F, l.description)}
            |${(for (b <- l.content) yield renderBlock(b), "\n")}
            |${(for (sl <- l.subLevels) yield renderLevel(sl, pos + 1, cookies :+ l), "\n")}""")
    }

    val content = renderLevel(report, 0, ISZ())
    readmeLoc.writeOver(content.render)
    println(s"Wrote: ${readmeLoc}")
  }


  def weave(readmeLoc: Os.Path, readmeContent: ReportLevel): Unit = {
    var existing = ops.StringOps(readmeLoc.read)

    def replaceContent(tag: String, content: ST, addNewLines: B): Unit = {
      val (start, end): (String, String) = (s"<!--start__${tag}-->", s"<!--end____${tag}-->")
      var posStart = existing.stringIndexOf(start)
      if (posStart > 0) {
        posStart = posStart + start.size // include the tag
        val posEnd = existing.stringIndexOfFrom(end, posStart)
        if (posEnd > 0) {
          val prelude: String = existing.substring(0, posStart)
          val postlude: String = existing.substring(posEnd, existing.size)
          val newContent: String =
            if (addNewLines)
              st"""$prelude
                  |$content
                  |$postlude""".render
            else st"$prelude$content$postlude".render
          existing = ops.StringOps(newContent)
        } else {
          cprintln(T, s"didn't find end tag ${end}")
        }
      } else {
        cprintln(T, s"didn't find start tag ${start}")
      }
    }

    def walk(level: Level, cookies: ISZ[Level]): Unit = {
      level match {
        case r: ReportLevel =>
          if (r.title.nonEmpty) {
            replaceContent(getTitleTag(r, cookies), r.title.get, F)
          }
          if (r.description.nonEmpty) {
            replaceContent(getDescTag(r, cookies), r.description.get, T)
          }
          for (rb <- r.content) {
            replaceContent(getBlockTag(rb, cookies :+ level), rb.content.get, T)
          }
          for (sl <- r.subLevels) {
            walk(sl, cookies :+ level)
          }
        case x => halt("Infeasible")
      }
    }
    walk(readmeContent, ISZ())

    readmeLoc.writeOver(existing.s)
    println(s"Replaced: ${readmeLoc}")
  }

  var seenTags: Set[String] = Set.empty

  def genReport(project: Project, packageName: String, aadlRootDir: Os.Path, rootDir: Os.Path, reporter: Reporter, emitMarkdown: B): ReportLevel = {
    seenTags = Set.empty
    return Report(packageName, aadlRootDir, repoRootDir, emitMarkdown).genReport(project)
  }

  def createAadlComponentLink(linkToImplementation: B,
                              component: org.sireum.hamr.ir.Component,
                              isRoot: B,
                              aadlDir: Os.Path,
                              rootDir: Os.Path): ST = {
    val componentType: String = component.category match {
      case ir.ComponentCategory.System => "system"
      case ir.ComponentCategory.Process => "process"
      case ir.ComponentCategory.Processor => "processor"
      case ir.ComponentCategory.Thread => "thread"
      case _ => "??"
    }
    //println(component)
    val classifier = component.classifier.get
    val name = ops.StringOps(ops.StringOps(classifier.name).replaceAllLiterally("::", ":")).split(c => c == ':')
    val fileCands = Os.Path.walk(aadlDir, T, F,
      p =>
        p.isFile && p.ext == "aadl" &&
          ops.ISZOps(p.readLines).exists(l => ops.StringOps(l).trim == s"package ${name(0)}")
    )
    assert(fileCands.size == 1)

    val file = fileCands(0)
    val (searchStr, textToDisplay): (String, String) = {
      val nameSplit = ops.StringOps(name(1)).split(c => c == '.')
      if (nameSplit.size == 1) (s"$componentType ${name(1)}", name(1)) // no period
      else if (linkToImplementation) (s"$componentType implementation ${name(1)}", name(1))
      else (s"$componentType ${nameSplit(0)}", nameSplit(0))
    }

    val declPos = component.identifier.pos.get
    val lines = file.readLines
    for (i <- 0 until lines.size if ops.StringOps(lines(i)).contains(searchStr) && (isRoot || declPos.beginLine != i + 1)) {
      return st"[${name(0)}::${textToDisplay}](${rootDir.relativize(file).value}#L${i + 1})"
    }
    halt(s"Infeasible: couldn't find $searchStr in $file")
  }

  def sortThreads(threads: ISZ[AadlThread], symbolTable: SymbolTable): ISZ[AadlThread] = {
    return (
      ops.ISZOps(threads()).sortWith((a, b) => a.component.classifier.get.name < b.component.classifier.get.name))
  }

  def createHyperLink(title: String, target: String): ST = {
    return st"[${title}](${target})"
  }

  def createTag(s: String): String = {
    val lc = StringUtil.toLowerCase(s)
    val d = StringUtil.replaceAll(lc, " ", "-")
    val cis = conversions.String.toCis(d)

    // only keep numbers, lowercase letters, '-' and '_'
    val cis_ = cis.filter(c =>
      (c.value >= 48 && c.value <= 57) || (c.value >= 97 && c.value <= 122) ||
        (c == '-') || (c == '_'))
    val d_ = conversions.String.fromCis(cis_)
    return d_
  }

  def toGithubLink(s: String): String = {
    return s"#${createTag(s)}"
  }

  def splitClassifier(c: AadlComponent): (String, String) = {
    val o = ops.StringOps(ops.StringOps(c.component.classifier.get.name).replaceAllLiterally("::", ":")).split(c => c == ':')
    return (o(0), o(1))
  }

  def sanitize(s: String): String = {
    return ops.StringOps(s).replaceAllLiterally(".", "_")
  }

  def findMethod(key: String, f: Os.Path): Z = {
    assert(f.isFile, s"$f is not a file")
    assert(!ops.StringOps(f.read).contains("\r"), s"$f contains windows style new lines")

    var line = 1
    // add space before newline as split does not preserve empty lines (i.e. those that only contain newline char)
    for (l <- ops.StringOps(ops.StringOps(f.read).replaceAllLiterally("\n", " \n")).split(c => c == '\n')) {
      if (ops.StringOps(l).contains(s"def $key(api: ")) {
        return line
      }
      line = line + 1
    }
    halt(s"Infeasible, didn't find $key in $f")
  }
}

@datatype class Report(packageName: String, aadlRootDir: Os.Path, repoRoot: Os.Path, emitMarkDown: B) {

  def runCloc(dirs: ISZ[Os.Path]): ST = {

    // define a language processing filter for camkes
    val camkesClocDef =
      st"""$camkes
          |    filter rm_comments_in_strings " /* */
          |    filter rm_comments_in_strings " //
          |    filter call_regexp_common C++
          |    extension camkes
          |    3rd_gen_scale 2.00
          |    end_of_line_continuation \\$$"""
    val temp = Os.temp()
    temp.writeOver(camkesClocDef.render)

    val args: ISZ[String] = ISZ[String](
      "cloc",
      "--md",
      "--exclude-lang=make",
      s"--read-lang-def=${temp.value}"
    ) ++ dirs.map((m: Os.Path) => m.value)

    val results = Os.proc(args).runCheck()
    val s = ops.StringOps(results.out)

    val ret = st"${(ops.ISZOps(s.split(c => c == '\n')).drop(2), "\n")}"

    return ret
  }

  def dirsScanned(project: Project, dirs: ISZ[Os.Path]): ST = {
    val rdirs = dirs.map((m: Os.Path) => project.projectRootDir.relativize(m)).map((m: Os.Path) => st"- [${m}]($m)")

    val _dirs: ST =
      st"""Directories Scanned Using [https://github.com/AlDanial/cloc](https://github.com/AlDanial/cloc) v1.94:
          |${(rdirs, "\n")}"""
    return _dirs
  }

  def genAadlMetrics(project: Project): ReportLevel = {

    assert(project.configs.size == 1)
    val model = AadlModelUtil.getModel(project.air, F)
    val symbolTable = AadlModelUtil.getSymbolTable(model, project.configs(0).packageName.get, project.configs(0))

    val threads = symbolTable.getThreads()

    val connections = symbolTable.connections

    var ports: Z = 0
    for (t <- threads) {
      ports = ports + t.getPorts().size
    }

    val ret: ST =
      st"""| | |
           ||:--|:--|
           ||Threads|${threads.size}|
           ||Ports|${ports}|
           ||Connections|${connections.size}|"""

    return ReportLevel(
      tag = createTag("AADL Metrics"),
      title = Some(st"AADL Metrics"),
      description = None(),
      content = ISZ(ReportBlock(
        tag = createTag("AADL Metrics content block"),
        content = Some(ret))),
      subLevels = ISZ()
    )
  }

  def processUserCloc(root: Os.Path): ST = {
    var code: Z = 0
    var log: Z = 0

    def process(d: Os.Path): Unit = {
      if (d.isDir) {
        for (f <- d.list) {
          process(f)
        }
      } else {
        if (d.ext == "loc") {
          val props = d.properties
          code = code + Z(props.getOrElse("code", "0")).get
          log = log + Z(props.getOrElse("log", "0")).get
        }
      }
    }

    process(root)

    val ret: ST =
      st"""|Type|code |
           ||:--|--:|
           ||Behavior|${(code - log)}|
           ||Log|${log}|
           ||--------|--------|
           ||SUM:|${code}|"""

    return ret
  }

  def genJVMMetrics(project: Project): ReportLevel = {

    assert(project.configs.size == 1)

    val mainDir = Os.path(project.configs(0).outputDir.get) / "src" / "main"
    assert(mainDir.exists && mainDir.isDir)

    val cloc: ST = runCloc(ISZ(mainDir))

    val _dirsScanned = dirsScanned(project, ISZ(mainDir))

    val userCloc: ST = processUserCloc(mainDir)

    val ret: ST =
      st"""${_dirsScanned}
          |
          |<u><b>Total LOC</b></u>
          |
          |Total number of HAMR-generated and developer-written lines of code
          |${cloc}
          |
          |<u><b>User LOC</b></u>
          |
          |The number of lines of code written by the developer.
          |"Log" are lines of code used for logging that
          |likely would be excluded in a release build
          | ${userCloc}"""

    val block = ReportBlock(
      tag = createTag(s"${project.title}_code_metrics"),
      content = Some(ret))

    return ReportLevel(
      tag = createTag("JVM Metrics"),
      title = Some(st"JVM Metrics"),
      description = Some(st"The following section provides statistics about the Slang source code."),
      content = ISZ(block),
      subLevels = ISZ()
    )
  }

  def genCodeMetrics(project: Project): ReportLevel = {

    return ReportLevel(
      tag = createTag("Metrics"),
      title = Some(st"Metrics"),
      description = None(),
      content = ISZ(),
      subLevels = ISZ(genAadlMetrics(project), genJVMMetrics(project))
    )
  }

  def processNickNames(names: String, table: SymbolTable): Map[String, AadlComponent] = {
    var ret: Map[String, AadlComponent] = Map.empty
    for(n <- ops.StringOps(names).split(c => c == ',')) {
      val split = ops.StringOps(n).split(c => c == ':')
      val compCand = table.componentMap.values.filter(p => p.pathAsString("_") == split(1))
      assert (compCand.size == 1, compCand.size)
      ret = ret + split(0) ~> compCand(0)
    }
    return ret
  }

  def findBehaviorCode(path: String, project: ReadmeGen.Project): (Option[Os.Path], Option[Os.Path]) = {
    val bridgesDir = project.projectRootDir / "hamr" / "slang" / "src" / "main" / "bridge"
    val componentsDir = project.projectRootDir / "hamr" / "slang" / "src" / "main" / "component"

    val behavior = Util.hackyFind(componentsDir, s"${path}.scala")
    val gumbox = Util.hackyFind(bridgesDir, s"${path}_GumboX.scala")

    return (behavior, gumbox)
  }

  def genHowToRunSection(project: Project, table: SymbolTable): ReportLevel = {

    val readmeRoot = project.projectRootDir
    val reportRoot = project.projectRootDir.up
    val stagReadme = readmeRoot.relativize(reportRoot / "report/util/SystemTestArtifactGen/readme.md")

    /*
    val exTestConfig = project.testConfigs(0)
    val exContainer = readmeRoot.relativize(exTestConfig.inputOutputContainers)
    val testOutputDirRel = readmeRoot.relativize(exTestConfig.systemTestOutputDir)
    val exManualRel = testOutputDirRel / s"${exTestConfig.exampleTestFrameworkPrefix}_Test_wSlangCheck.scala"
    val exDSCRel = testOutputDirRel / s"${exTestConfig.exampleTestFrameworkPrefix}_DSC_Test_Harness.scala"

    val framework: ReportLevel = ReportLevel(
      tag = createTag("framework-generation"),
      title = Some(st"Framework Generation"),
      description = Some(
        st"""1. Build the System Testing Artifact Generator following the instructions at
            |   [SystemTestArtifactGen/readme.md]($stagReadme)
            |1. Run the generator by passing it the paths to one or more files that contain
            |   input/output container definitions
            |
            |For example, running the generator on
            |[${exContainer.name}](${exContainer})
            |will generate the following artifacts:
            |
            |1. [${exManualRel.name}]($exManualRel)
            |
            |   System test suite containing an example test run configuration.  Developers can make a copy of this file and then define
            |   custom test run configurations where each configuration has the structure
            |   _(script schema, property, randomization profile, random vector filter)_
            |
            |1. [${exDSCRel.name}]($exDSCRel)
            |
            |      Example showing how a system test suite can be adapted for use with Distributed SlangCheck (DSC). It overrides/implements
            |   two DCS methods, ``next`` and ``test``.  The next method is called during DSC's test vector generation phase. The generated
            |   vectors are subsequently passed to the test method during DSC's testing phase. Both methods use the environment variable
            |   ``DSC_TEST_FAMILY_NAME`` to determine which test run configuration should be used.
            |
            |"""),
      content = ISZ(),
      subLevels = ISZ()
    )

    val subSystemsX: ISZ[String] = for (sub <- project.testConfigs) yield sub.systemName
    val subSystems: String = if (subSystemsX.size == 1) {
      subSystemsX(0)
    }
    else {
      val o = ops.ISZOps(subSystemsX)
      st"${(o.dropRight(1), ", ")} and ${o.last}".render
    }

    val manBlocks: ISZ[ReportBlock] = {
      var mbs: ISZ[ReportBlock] = ISZ()
      for (tconfig <- project.testConfigs) {
        val daconfig = tconfig.systemTestOutputDir / tconfig.manualTestingFilename
        val daconfigcontents = ops.StringOps(ops.StringOps(daconfig.read).replaceAllLiterally("\n", " \n")).split(c => c == '\n')
        val mtf = readmeRoot.relativize(daconfig)

        val testSuiteSuffix: String = ops.StringOps(tconfig.manualTestingFilename).replaceAllLiterally(".scala", "")
        val nest: ST =
          st"""System Test Suite Class: [${mtf.name}](${mtf})
              |
              |Test run configurations are specified via entries in the ${Util.locateText("testMatrix", daconfigcontents, mtf)}. For example,
              |the ${Util.locateText(tconfig.exampleTestConfig.name, daconfigcontents, mtf)} configuration uses the following:
              |
              || | |
              ||:--|--|
              || Script Schema: | ${Util.locateMethodDefinition(tconfig.exampleTestConfig.schema, daconfigcontents, mtf)}|
              || Property: | ${Util.locateMethodDefinition(tconfig.exampleTestConfig.property, daconfigcontents, mtf)}|
              || Randomization Profile: | ${Util.locateMethodDefinition(tconfig.exampleTestConfig.profile, daconfigcontents, mtf)}|
              || Random Vector Filter: | ${Util.locateMethodDefinition(tconfig.exampleTestConfig.filter, daconfigcontents, mtf)}|
              |
              |How to run:
              |
              |```
              |cd hamr-system-testing-case-studies
              |
              |sireum proyek test --suffixes ${testSuiteSuffix} ${project.projectRootDir.name}/hamr/slang
              |```
              |"""

        mbs = mbs :+ ReportBlock(
          tag = createTag(s"${tconfig.systemName}_block"),
          content = Some(
            st"""__${tconfig.systemName}__
                |
                |  $nest
                |""")
        )
      }
      mbs
    }

    val manualTesting: ReportLevel = ReportLevel(
      tag = createTag("manual-testing"),
      title = Some(st"Manual System Testing"),
      description = Some(
        st"""The example system test suites described previously were used to write
            |system tests for the ${subSystems} as follows:"""),
      content = manBlocks,
      subLevels = ISZ()
    )
    val bs = "\\"

    val title = project.title
    val pdir = project.projectRootDir.name
    val harness = project.testConfigs(0).simpleDscHarnessName
    val config = project.testConfigs(0).exampleTestConfig.name

    val phtmldir: String = {
      if (title == "Isolette") "isolette"
      else if (title == "RTS") "RTS"
      else "??"
    }

    val lt1 = s"$phtmldir/$harness/$config/1"
    val l1 = s"$htmlDir/$lt1"
    val lt2 = s"$phtmldir/$harness/$config"
    val l2 = s"$htmlDir/$lt2"
    val lt3 = s"$phtmldir/$harness"
    val l3 = s"$htmlDir/$lt3"
    val lt4 = s"$phtmldir"
    val l4 = s"$htmlDir/$lt4"

    val dscTesting: ReportLevel = ReportLevel(
      tag = createTag("dsc-testing"),
      title = Some(st"Distributed SlangCheck System Testing"),
      description = Some(
        st"""Background:
            |
            |System testing as put forth in this paper uses SlangCheck to generate input/injection test vectors.
            |SlangCheck is Sireum's randomized
            |test generator framework similar to ScalaCheck and Haskell's QuickCheck.
            |Distributed SlangCheck (DSC) adds a framework that allows test vector
            |generation to be run via a server cluster up to a user specified timeout. Increasing
            |the timeout allows more vectors to be produced which may lead to increased code
            |coverage during testing. DSC passes the vectors to user defined unit tests
            |and serializes the
            |passing and failing vectors to seperate files so that they can be replayed if needed.
            |DSC uses JaCoCo to produce code coverage information.
            |
            |Approach:
            |
            |The ${config} configuration of ${title}'s
            |${project.testConfigs(0).simpleManualTestSuiteName} test suite will be used to
            |illustrate how system testing can employ DSC.  The actual results reported in the next
            |section simply automated the following steps such that each configuration was run with timeouts
            |of 1, 5, and 10 seconds using a Jenkins cluster.
            |
            |Create a jar file for this project that includes the sources and tests suites
            |
            |```
            |cd hamr-system-testing-case-studies
            |
            |sireum proyek assemble --include-sources --include-tests ${pdir}/hamr/slang
            |```
            |
            |Set the environment variable ``DSC_TEST_FAMILY_NAME`` to indicate which configuration
            |should be used
            |
            |```
            |export DSC_TEST_FAMILY_NAME=${config}
            |```
            |
            |The following will repeatedly call ${harness}'s next method for 1 second to generate test vectors
            |and store them in a local file (DSC can be
            |configured to scp the results to a remote server where they can be combined with vectors
            |generated from other 'generating' servers)
            |```
            |sireum tools slangcheck runner$bs
            |  -t 1$bs
            |  -o $$(pwd)/${pdir}-dsc.jsons$bs
            |  -c ${pdir}/hamr/slang/out/slang/assemble/slang.jar$bs
            |  ${project.testConfigs(0).dscFQName}
            |```
            |
            |DSC is designed to only report passing and failing test vectors.  The generated DSC
            |test harness test methods extend this by invoking the configuration's random vector filter and
            |writing out unsat vectors to a file specified via the ``DSC_SAVE_LOC`` environment variable.
            |```
            |export DSC_SAVE_LOC=$$(pwd)/${pdir}-dsc-output.unsat
            |touch $$DSC_SAVE_LOC
            |```
            |
            |The following will pass each test vector to the ${harness}'s test method,
            |record the passing/failing/unsat test vectors in separate files and generate an HTML
            |report that combines the coverage information across all the runs.
            |```
            |sireum tools slangcheck tester$bs
            |  -i $$(pwd)/${pdir}-dsc.jsons.dsc.7z$bs
            |  -o $$(pwd)/${pdir}-dsc-output$bs
            |  --coverage $$(pwd)/${pdir}-jacoco$bs
            |  -c ${pdir}/hamr/slang/out/slang/assemble/slang.jar$bs
            |  --sourcepath ${pdir}/hamr/slang/out/slang/assemble/slang.jar$bs
            |  ${project.testConfigs(0).dscFQName}
            |```
            |
            |Results:
            |
            |The full experimental results for the ${title} are available at:<br>
            |[$l4]($l4/report.html)
            |
            |<br><br>
            |The following table explains the report directory structure,
            |starting with the results from a specific run of DSC and then walking
            |up the report directory hierarchy.
            |
            |||
            ||:--|
            ||[$lt1]($l1/report.html)<br><br>The combined coverage information along with the number of passing/failing/unsat test vectors for the ${config} configuration with a 1 second timeout<br><br>__NOTE__ this is what DSC was actually run on.  The following rows are simply aggregate information |
            ||[$lt2]($l2/report.html)<br><br>The combined coverage information along with the number of passing/failing/unsat test vectors for the MA__Failing__CT____Alarm_On configuration using timeouts of 1, 5, and 10 seconds |
            ||[$lt3]($l3/report.html)<br><br>The combined coverage information along with the number of passing/failing/unsat test vectors for running all the configurations through ${harness} using timeouts of 1, 5, and 10 seconds |
            ||[$lt4]($l4/report.html)<br><br>The combined coverage information along with the number of passing/failing/unsat test vectors for each of the ${title}'s subsystems under testing using timeouts of 1, 5, and 10 seconds |
            |"""),
      content = ISZ(),
      subLevels = ISZ()
    )
    */
    return ReportLevel(
      tag = createTag("how-to-run"),
      title = Some(st"How to Run"),
      description = Some(
        st"""GUMBOX unit testing requires a Sireum distribution. Instructions on how to obtain a
            |distribution are available at [https://sireum.org/getting-started/](https://sireum.org/getting-started/).
            |The rest of this documentation assumes the ``SIREUM_HOME`` environmental variable has been set and that
            |sireum's bin directory has been added to your path (e.g. for Linux/MacOS ``export PATH=$$SIREUM_HOME/bin:$$PATH``
            |or Windows ``set PATH=%PATH%\bin;%PATH%``
            |
            |"""),


      content = ISZ(),
      subLevels = ISZ()//framework, manualTesting, dscTesting)

    )
  }



  def collect(index: Z, content: ISZ[ISZ[String]]): Z = {
    var sum: Z = 0
    for (c <- content) {
      sum = sum + Z(c(index)).get
    }
    return sum / content.size
  }

  def allSame(index: Z, content: ISZ[ISZ[String]]): Unit = {
    var last: Z = -1
    for (c <- content) {
      if (last == -1) {
        last = Z(c(index)).get
      } else {
        assert(Z(c(index)).get == last)
      }
    }
  }

  def formatTime(s: Z): String = {
    val cis = conversions.String.toCis(s.string)
    var ret: ISZ[org.sireum.C] = ISZ()
    for (ix <- cis.size - 1 to 0 by -1) {
      ret = cis(ix) +: ret
      if (ret.size == 3) {
        ret = '.' +: ret
      }
    }
    val r: String = conversions.String.fromCis(ret)
    return if (ret.size == 2) s"0.0$r"
    else if (ret.size == 3) s"0.$r"
    else if (ret.size == 4) s"0$r"
    else r
  }

  def genArchitectureSection(project: Project, table: SymbolTable): ReportLevel = {

    var blocks: ISZ[ReportBlock] = ISZ()

    var archDescription: ST = st""
    AadlModelUtil.getAadlArchDiagram(aadlRootDir) match {
      case Some(p) =>
        val rootDir = project.projectRootDir
        val rel = rootDir.relativize(p)
        val link = createHyperLink("AADL Arch", rel.value)
        //blocks = blocks :+ ReportBlock(
        //  tag = createTag("aadl-arch-diagram"),
        //  content = Some(st"!${link}"))
        archDescription =
          st"""!${link}"""
      case _ =>
        halt(s"Didn't find AADL arch diagram: ${project.title}")
    }

    val tagPrefix = "aadl-arch-component-info"

    val header: ST =
      createAadlComponentLink(T, table.rootSystem.component, T, aadlRootDir, project.projectRootDir)

    var systemProps =
      st"""|System: ${header}|
           ||:--|"""

    if (table.rootSystem.getUseRawConnection()) {
      systemProps =
        st"""${systemProps}
            ||Wire Protocol|"""
    }

    blocks = blocks :+ ReportBlock(
      tag = createTag(s"${tagPrefix}-${table.rootSystem.identifier}"),
      content = Some(systemProps))

    val threads: ISZ[AadlThread] = sortThreads(table.getThreads(), table)
    for (thread <- threads) {
      val name = thread.identifier
      val typ: String =
        if (thread.isPeriodic()) s"Periodic: ${thread.period.get} ms"
        else "Sporadic"

      val compType: String =
        if (thread.isCakeMLComponent()) "CakeML"
        else if (thread.getParent(table).toVirtualMachine(table)) "Virtual Machine"
        else "Native"

      val domain: Option[ST] = if (thread.getParent(table).getDomain(table).nonEmpty) {
        Some(st"""|Domain: ${thread.getParent(table).getDomain(table).get}|""")
      } else {
        None()
      }

      val header: ST = {
        val pos: Position = thread.component.identifier.pos.get
        AadlModelUtil.createLinkFromPos(name, pos, aadlRootDir, project.projectRootDir)
      }

      val componentType = createAadlComponentLink(F, thread.component, F, aadlRootDir, project.projectRootDir)

      val componentImpl: Option[ST] =
        if (AadlModelUtil.isImplementation(thread)) {
          val link = createAadlComponentLink(T, thread.component, F, aadlRootDir, project.projectRootDir)
          Some(st"<br>Implementation: ${link}")
        }
        else {
          None()
        }

      val gumbo: Option[ST] = AadlModelUtil.getGumboSubclause(thread, table) match {
        case Some(g) =>
          val pos = AadlModelUtil.getGumboSubclausePos(g, aadlRootDir)
          Some(st"<br>Behavior Specification: ${AadlModelUtil.createLinkFromPos("GUMBO", pos, aadlRootDir, project.projectRootDir)}")
        case _ => None()
      }

      val tpath = st"${(ops.ISZOps(thread.path).drop(1), "_")}".render

      blocks = blocks :+ ReportBlock(
        tag = createTag(s"${tagPrefix}-${thread.identifier}"),
        content = Some(
          st"""|Thread: ${thread.identifier} <!--${header}--> |
               ||:--|
               ||Type: ${componentType}${componentImpl}${gumbo}|
               ||${compType}|
               ||${typ}|
               |${domain}
               |"""))
    }

    val proc: AadlProcessor = PeriodicUtil.getBoundProcessor(table)
    proc.getScheduleSourceText() match {
      case Some(loc) =>
        val schedule = project.aadlRootDir / loc
        assert(schedule.exists, schedule.canon)
        blocks = blocks :+ ReportBlock(
          tag = createTag(s"${tagPrefix}-schedule"),
          content = Some(st"**Schedule:** [${schedule.name}](${project.projectRootDir.relativize(schedule).value})"))
      case _ =>
    }

    return ReportLevel(
      tag = createTag("arch-section"),
      title = Some(st"AADL Architecture"),
      description = Some(archDescription),
      content = blocks,
      subLevels = ISZ()
    )
  }

  def genSetup(): ReportLevel = {
    val setup = ReportBlock(
      tag = createTag("setup-block"),
      content = Some(
        st"""You only need to perform these steps once for all the micro-examples in this
            |repository.
            |
            |1. Install [Docker Desktop](https://www.docker.com/products/docker-desktop/)
            |
            |1. Clone this repo and cd into it
            |
            |   ```
            |   git clone https://github.com/santoslab/case-tool-assessment-4.git
            |   cd case-tool-assessment-4
            |   ```
            |
            |1. Download and run the CAmkES docker container
            |
            |   **NOTE**: Add the option `--platform linux/arm64` if on ARM
            |
            |   ```
            |   docker run -it -w /root -v $$(pwd):/root/case-tool-assessment-4 trustworthysystems/camkes
            |   ```
            |
            |   Copy and paste the following into the container in order to fetch CAmkES, CAmkES-VM, and the Microkit SDK
            |
            |   ```
            |   git config --global user.email ""
            |   git config --global user.name ""
            |   #
            |   (mkdir camkes && cd camkes && \
            |     repo init -u https://github.com/seL4/camkes-manifest.git && \
            |     repo sync)
            |   #
            |   (mkdir camkes-vm && cd camkes-vm && \
            |     repo init -u https://github.com/seL4/camkes-vm-examples-manifest.git && \
            |     repo sync)
            |   #
            |   (curl -L trustworthy.systems/Downloads/microkit_tutorial/sdk-linux-x64.tar.gz -o sdk.tar.gz && \
            |     tar xf sdk.tar.gz && mv sdk microkit_sdk && \
            |     echo "export MICROKIT_SDK=$$(pwd)/microkit_sdk" >> $$HOME/.bashrc)
            |   #
            |   source $$HOME/.bashrc
            |   ```
            |
            |1. *OPTIONAL*
            |
            |    If you want to rerun codegen then you will need to install Sireum
            |    and OSATE into the container.
            |
            |    Copy/paste the following into the container to install Sireum
            |    ```
            |    (DIR=$$HOME/Sireum && export SIREUM_V=4.20240517.2ba56a5 && rm -fR $$DIR && mkdir -p $$DIR/bin && cd $$DIR/bin && curl -JLso init.sh https://raw.githubusercontent.com/sireum/kekinian/$$SIREUM_V/bin/init.sh && bash init.sh)
            |    echo "export SIREUM_HOME=$$HOME/Sireum" >> $$HOME/.bashrc
            |    echo "export PATH=\$$SIREUM_HOME/bin:\$$PATH" >> $$HOME/.bashrc
            |    source $$HOME/.bashrc
            |    ```
            |
            |    Now copy/paste the following to install OSATE
            |
            |    ```
            |    sireum hamr phantom -u -v -o $$HOME/osate
            |    echo "export OSATE_HOME=$$HOME/osate" >> $$HOME/.bashrc
            |    source $$HOME/.bashrc
            |    ```
            |
            |    The following instructions related to rerunning HAMR Codegen assumes
            |    the ``SIREUM_HOME`` environmental variable has been set and that Sireum's
            |    ``bin`` directory has been added to the path. To verify a correct installation,
            |    run Sireum from the command line by typing ``sireum``. The output should indicate
            |    a build date of 4.20240508.f1c262c."""
      )
    )
    return ReportLevel(
      tag = createTag("setup"),
      title = Some(st"Setup the CAmkES Docker Container"),
      description = None(),
      content = ISZ(setup),
      subLevels = ISZ()
    )
  }

  def genReport(project: Project): ReportLevel = {

    val model = AadlModelUtil.getModel(project.air, F)
    val table = AadlModelUtil.getSymbolTable(model, packageName, project.configs(0))

    val arch: ReportLevel = genArchitectureSection(project, table)

    val setup: ReportLevel = genSetup()

    var targetBlocks: ISZ[ReportLevel] = ISZ()
    for(t <- project.targets) {
      println(t.name)
      targetBlocks = targetBlocks :+ genTargetBlock(t, project, table)
    }

    //val behaviorCode: ReportLevel = genBehaviorCodeSection(project, table)

    //val metrics: ReportLevel = genCodeMetrics(project)

    //val unitTesting: ReportLevel = genTestingSection(project, table)

    //val logika: ReportLevel = genLogikaSection(project, table)

    //val howToRun: ReportLevel = genHowToRunSection(project, table)

    return ReportLevel(
      tag = createTag(project.title),
      title = Some(st"${project.title}"),
      description = None(),
      content = ISZ(),
      subLevels = ISZ(arch, setup) ++ targetBlocks
    )
  }

  private def genTargetBlock(targetType: ReadmeGen.Target, project: Project, table: SymbolTable): ReportLevel = {
    targetType match {
      case t: CAmkES =>

        val target = if (t.name == "vm") "seL4" else t.name
        val camkesDir: Os.Path = project.hamrDir / s"camkes-${target}"

        val dot = camkesDir / "graph.dot"
        val dest = project.aadlRootDir / "diagrams" / s"CAmkES-HAMR-arch-${target}.svg"
        proc"dot -Tsvg -o $dest $dot".runCheck()

        val dir = project.hamrDir / "camkes"
        val camkesArch = ReportLevel(
          tag = createTag(s"${t.name}_camkesarch"),
          title = Some(st"CAmkES Architecture"),
          description = None(),
          content = ISZ(ReportBlock(
            tag = createTag(s"${t.name}_camkesarch_block"),
            content = Some(
              st"""![${dest.name}](${project.projectRootDir.relativize(dest)})"""))),
          subLevels = ISZ())

        val behaviorCode = ReportLevel(
          tag = createTag(s"${t.name}_behavior"),
          title = Some(st"Behavior Code"),
          description = None(),
          content = ISZ(ReportBlock(
            tag = createTag(s"${t.name}_behavior_block"),
            content = Some(
              st""" - [${t.producerBehaviorCode.name}](${project.projectRootDir.relativize(t.producerBehaviorCode)})
                  | - [${t.consumerBehaviorCode.name}](${project.projectRootDir.relativize(t.consumerBehaviorCode)})"""))),
          subLevels = ISZ())

        val camkesAssembly = ReportLevel(
          tag = createTag(s"${t.name}_assembly"),
          title = Some(st"$camkes Assembly"),
          description = None(),
          content = ISZ(ReportBlock(
            tag = createTag(s"${t.name}_assembly_block"),
            content = Some(st""" - [${t.camkesRoot.name}](${project.projectRootDir.relativize(t.camkesRoot)})"""))),
          subLevels = ISZ())

        val camkesProducerArtifacts = ReportLevel(
          tag = createTag(s"${t.name}_producer"),
          title = Some(st"$camkes Producer Artifacts"),
          description = None(),
          content = ISZ(ReportBlock(
            tag = createTag(s"${t.name}_producer_block"),
            content = Some(
              st""" - [$camkes component](${project.projectRootDir.relativize(t.producerCamkesComponent)})
                  | - [Infrastructure Code](${project.projectRootDir.relativize(t.producerCamkesInfrastructure)})"""))),
          subLevels = ISZ())

        val infracode = t match {
          case i: vm => st"*infrastructure and behavior code are combined for VMs*"
          case i: Sel4Config => st"[Infrastructure Code](${project.projectRootDir.relativize(i.consumerCamkesInfrastructure)})"
        }
        val camkesConsumerArtifacts = ReportLevel(
          tag = createTag(s"${t.name}_consumer"),
          title = Some(st"$camkes Consumer Artifacts"),
          description = None(),
          content = ISZ(ReportBlock(
            tag = createTag(s"${t.name}_consumer_block"),
            content =
              Some(
                st""" - [$camkes component](${project.projectRootDir.relativize(t.consumerCamkesComponent)})
                    | - $infracode"""))),
          subLevels = ISZ())

        var sublevels = ISZ(behaviorCode, camkesAssembly, camkesProducerArtifacts, camkesConsumerArtifacts)

        val shim = t match {
          case i: vm =>
          case i: Sel4Config =>
            val counter: Option[ST] = if (i.jimCounter.nonEmpty) Some(st" - [Event Counter](${project.projectRootDir.relativize(i.jimCounter.get)})") else None()
            val includes: Option[ST] = if (i.jimH.nonEmpty) Some(st" - [Header](${project.projectRootDir.relativize(i.jimH.get)})") else None()
            val impl: Option[ST] = if (i.jimC.nonEmpty) Some(st" - [Implementation](${project.projectRootDir.relativize(i.jimC.get)})") else None()

            sublevels = sublevels :+ ReportLevel(
              tag = createTag(s"${t.name}_shim"),
              title = Some(st"Message Shim Code"),
              description = None(),
              content = ISZ(ReportBlock(
                tag = createTag(s"${t.name}_shim_block"),
                content = Some(
                  st"""$impl
                      |$includes
                      |$counter""")
              )),
              subLevels = ISZ()
            )
        }

        val camkesRepoDir: ST =
          t match {
            case i: vm => st"/root/camkes-vm"
            case i: Sel4Config => st"/root/camkes"
          }
        val howToRerun = ReportLevel(
          tag = createTag(s"${t.name}-rerun"),
          title = Some(st"How to build and run"),
          description = None(),
          content = ISZ(
            ReportBlock(
              tag = createTag(s"${t.name}-rerun-codegen"),
              content = Some(
                st"""**OPTIONAL: Rerun Codegen**
                    |
                    |```
                    |/root/case-tool-assessment-4/${repoRoot.relativize(project.aadlRootDir)}/bin/run-hamr.cmd ${target}
                    |```""")),
            ReportBlock(
              tag = createTag(s"${t.name}-rerun-buildsim"),
              content = Some(
                st"""**Build and simulate the system**
                    |
                    |```
                    |/root/case-tool-assessment-4/${repoRoot.relativize(camkesDir / "bin" / s"run-camkes.sh")} -c $camkesRepoDir -s
                    |```
                    |
                    |Type ``CTRL`` + ``a`` then `x` to exit the QEMU simulation
                    |""")
            )
          ),
          subLevels = ISZ()
        )

        sublevels = sublevels ++ ISZ(howToRerun, camkesArch)

        return ReportLevel(
          tag = createTag(t.name),
          title = Some(st"${target}"),
          description = None(),
          content = ISZ(),
          subLevels = sublevels)

      case m: Microkit =>
        val system = ReportLevel(
          tag = createTag(s"${m.name}_system"),
          title = Some(st"Microkit System"),
          description = None(),
          content = ISZ(ReportBlock(
            tag = createTag(s"${m.name}_system_block"),
            content = Some(
              st""" - [${m.system.name}](${project.projectRootDir.relativize(m.system)})"""
            )
          )),
          subLevels = ISZ()
        )

        val behaviorCode = ReportLevel(
          tag = createTag(s"${m.name}_behavior"),
          title = Some(st"Behavior Code"),
          description = None(),
          content = ISZ(ReportBlock(
            tag = createTag(s"${m.name}_behavior_block"),
            content = Some(
              st""" - [${m.producerBehaviorCode.name}](${project.projectRootDir.relativize(m.producerBehaviorCode)})
                  | - [${m.consumerBehaviorCode.name}](${project.projectRootDir.relativize(m.consumerBehaviorCode)})
                """
            )
          )),
          subLevels = ISZ()
        )

        var sublevels = ISZ(system, behaviorCode)

        if (m.periodicDispatcher.nonEmpty) {
          sublevels = sublevels :+ ReportLevel(
            tag = createTag(s"${m.name}_periodic_hack"),
            title = Some(st"Periodic Dispatching Artifacts"),
            description = None(),
            content = ISZ(ReportBlock(
              tag = createTag(s"${m.name}_periodic_hack_block"),
              content = Some(
                st""" - [${m.periodicDispatcher.get.name}](${project.projectRootDir.relativize(m.periodicDispatcher.get)})
                    | - [${m.end_of_schedule_ping.get.name}](${project.projectRootDir.relativize(m.end_of_schedule_ping.get)})""")
            )),
            subLevels = ISZ()
          )
        }

        val howToRun = ReportLevel(
          tag = createTag(s"${m.name}_rerun"),
          title = Some(st"How to build and run"),
          description = None(),
          content = ISZ(ReportBlock(
            tag = createTag(s"${m.name}_rerun_blcok"),
            content = Some(
              st"""
                  |```
                  |cd /root/${repoRoot.name}/${repoRoot.relativize(project.projectRootDir / "microkit")}
                  |make run
                  |```"""
            )
          )),
          subLevels = ISZ()
        )
        return ReportLevel(
          tag = createTag(m.name),
          title = Some(st"Microkit"),
          description = None(),
          content = ISZ(),
          subLevels = sublevels)

    }
  }
}