// #Sireum

package report

import org.sireum._

object Util {

  def hackyFind(dir: Os.Path, suffix: String): Option[Os.Path] = {
    assert(dir.isDir, s"${dir}")
    for(f <- dir.list if f.isFile) {
      if (ops.StringOps(f.name).endsWith(suffix)) {
        return Some(f)
      }
    }
    for(subdir <- dir.list if subdir.isDir) {
      hackyFind(subdir, suffix) match {
        case Some(matchy) => return Some(matchy)
        case _ =>
      }
    }
    //println(s"didn't find ${suffix} in ${dir}")
    return None()
  }

  def readLines(s: String, sep: C): ISZ[String] = {
    // sireum's split does not preserve blank lines so a space.
    // TODO drop the space
    return ops.StringOps(ops.StringOps(s).replaceAllLiterally(s"$sep", s" $sep")).split(c => c == sep)
  }

  def parseJson(str: String, keys: ISZ[String]): HashSMap[String, String] = {
    val p = org.sireum.Json.Parser.create(str)
    p.parseObjectBegin()
    var entries = HashSMap.empty[String, String]()
    do {
      val key = p.parseObjectKeys(keys)
      val value = p.parseString()
      entries = entries + key ~> value
    } while(p.parseObjectNext())
    return entries
  }

  @datatype class Dirs(val rootDir: Os.Path,
                       val aadlDir: Os.Path,
                       val slangDir: Os.Path,
                       val json: Os.Path)
  def getDefaultDirectories(projRootDir: Os.Path): Dirs = {
    def exists(path: Os.Path): Os.Path = {
      if (!path.exists) {
        halt(s"${path} does not exist")
      }
      return path
    }
    def getJson(path: Os.Path): Os.Path = {
      val cands = Os.Path.walk(path, T, F, p => p.ext == "json")
      if (cands.size != 1) {
        halt(s"Found ${cands.size} json files in ${path}")
      }
      return cands(0)
    }
    return Dirs(
      rootDir = projRootDir,
      aadlDir = exists(projRootDir / "aadl"),
      slangDir = exists((projRootDir / "hamr" / "slang")),
      json = getJson(projRootDir / "aadl")
    )
  }


  val baseOptions: Cli.SireumHamrCodegenOption = Cli.SireumHamrCodegenOption(
    help = "",
    args = ISZ(),
    msgpack = F,
    verbose = T,
    runtimeMonitoring = T,
    platform = Cli.SireumHamrCodegenHamrPlatform.JVM,
    packageName = None(),

    noProyekIve = T,
    noEmbedArt = F,
    devicesAsThreads = F,
    excludeComponentImpl = T,
    genSbtMill = F,

    outputDir = None(),
    slangAuxCodeDirs = ISZ(),
    slangOutputCDir = None(),
    camkesOutputDir = None(),
    camkesAuxCodeDirs = ISZ(),
    aadlRootDir = None(),

    bitWidth = 32,
    maxStringSize = 256,
    maxArraySize = 1,
    runTranspiler = F,

    experimentalOptions = ISZ()
  )

  def locateText(s: String, lines: ISZ[String], linkPrefix: Os.Path): Option[String] = {
    return locateTextD(F, F, s, lines, linkPrefix)
  }
  def locateTextD(rev: B, makeHtmlLinks: B, s: String, lines: ISZ[String], linkPrefix: Os.Path): Option[String] = {
    if (ops.StringOps(s).startsWith("Default ")) {
      return (if (makeHtmlLinks) Some("getDefaultProfile, <i>i.e. uses default configurations as provided by SlangCheck</i>")
       else Some("getDefaultProfile, _i.e. uses default configurations as provided by SlangCheck_"))
    }
    if (rev) {
      for (i <- lines.size - 1 to 0 by -1 if ops.StringOps(lines(i)).contains(s)) {
        return Some(mkLink(makeHtmlLinks, s, s"${linkPrefix}#L${i + 1})"))
      }
    } else {
      for (i <- 0 until lines.size if ops.StringOps(lines(i)).contains(s)) {
        return Some(mkLink(makeHtmlLinks, s, s"${linkPrefix}#L${i + 1}"))
      }
    }
    return None()
  }
  def locateMethodDefinition(methodName: String, lines: ISZ[String], linkPrefix: Os.Path): String = {
    return locateMethodDefinitionH(F, methodName, lines, linkPrefix)
  }

  def mkLink(makeHtmlLinks: B, name: String, anchor: String): String = {
    return (
      if (makeHtmlLinks) s"<a href=\"$anchor\">$name</a>"
      else s"[$name]($anchor)"
    )
  }
  def locateMethodDefinitionH(makeHtmlLinks: B, methodName: String, lines: ISZ[String], linkPrefix: Os.Path): String = {
    if (methodName == "Regulate_Subsystem_Inputs_Container_GumboX.system_Pre_Container") {
      return mkLink(makeHtmlLinks, "system_Pre_Container", "hamr/slang/src/test/system/isolette/system_tests/rst/Regulate_Subsystem_Inputs_Container_GumboX.scala#L46")
    }
    if (methodName == "getDefaultProfile") {
      return "getDefaultProfile, _i.e. uses default configurations as provided by SlangCheck_"
    }
    val deffy = s"def $methodName"
    for (i <- 0 until lines.size if ops.StringOps(lines(i)).contains(deffy)) {
      return mkLink(makeHtmlLinks, methodName, s"${linkPrefix}#L${i+1}")
    }
    return s"Didn't find $methodName in $linkPrefix"
  }
}
