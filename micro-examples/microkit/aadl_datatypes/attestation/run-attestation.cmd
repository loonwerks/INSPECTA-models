::/*#! 2> /dev/null                                 #
@ 2>/dev/null # 2>nul & echo off & goto BOF         #
if [ -z ${SIREUM_HOME} ]; then                      #
  echo "Please set SIREUM_HOME env var"             #
  exit -1                                           #
fi                                                  #
exec ${SIREUM_HOME}/bin/sireum slang run "$0" "$@"  #
:BOF
setlocal
if not defined SIREUM_HOME (
  echo Please set SIREUM_HOME env var
  exit /B -1
)
%SIREUM_HOME%\\bin\\sireum.bat slang run "%0" %*
exit /B %errorlevel%
::!#*/
// #Sireum

import org.sireum._

val home = Os.slashDir
val projectDir = home.up

if (!(ops.ISZOps(Os.cliArgs).contains("aadl")) &&
    !(ops.ISZOps(Os.cliArgs).contains("sysml")) &&
    !(ops.ISZOps(Os.cliArgs).contains("provision"))) {
  println(st"""Pass one of the following arguments:
              |  provision: Reruns provisioning
              |  aadl:      Performs an appraisal on the AADL artifacts
              |  sysml:     Performs an appraisal on the SysML artifacts""".render)
  Os.exit(0)
}

val repoRoot = {
  def f(p: Os.Path): Option[Os.Path] = {
    if ((p / ".git" / "config").exists) {
      assert((p / "micro-examples").exists, s"Expected ${p / "micro-examples"} to exist")
      return Some(p)
    }
    return f(p.up)
  }
  f(projectDir).get
}

val sireumBin = Os.path(Os.env("SIREUM_HOME").get) / "bin" 
val sireum = sireumBin / (if(Os.isWin) "sireum.bat" else "sireum")

val microkitFile = home.up / "hamr" / "microkit" / "microkit.system"
assert(microkitFile.exists, s"Didn't find MSD at $microkitFile")

val aadlDir = projectDir / "aadl"
val sysmlDir = projectDir / "sysml"

val model_args = "model_args.json"
val system_args = home / "system_args.json"

def makeArgs(extension: String, modelDir: Os.Path): Unit = {
  @strictpure def getDir(dir: Os.Path): String = s"/INSPECTA-models/${repoRoot.relativize(dir).value}"
  (home / s"${extension}_$model_args").writeOver(
    st"""{"dirpath":         "${getDir(modelDir)}/",
        | "suffix":          ".$extension",
        | "filepath-golden": "${getDir(home)}/${extension}_composite.txt"}""".render)
  system_args.writeOver(st"""{"dirpath":         "${getDir(microkitFile.up)}/",
                            | "suffix":          ".system",
                            | "filepath-golden": "${getDir(home)}/microkit_composite.txt"}""".render)
}

if (aadlDir.exists) {
  val cands = Os.Path.walk(aadlDir, T, T, p => p.ext == "aadl")
  assert (ops.ISZOps(cands).forall(p => p.up == cands(0).up), st"All AADL files must be in the same directory: ${(cands, "\n")}".render)
  makeArgs("aadl", cands(0).up)
}

if (sysmlDir.exists) {
  makeArgs("sysml", sysmlDir)
}



def run(title: String, proc: OsProto.Proc, verbose: B, checkExitCode: B): Z = {
  println(s"$title ...")
  val r = (if (verbose) proc.console.echo else proc).run()
  if (checkExitCode &&r.exitCode != 0) {
    println(s"$title failed!")
    cprintln(F, r.out)
    cprintln(T, r.err)
  }
  return if (r.exitCode == 0) 0 else 1
}

assert (Os.env("DEMO_ROOT").nonEmpty, "DEMO_ROOT environment variable not set")
val DEMO_ROOT = Os.path(Os.env("DEMO_ROOT").get)
assert ((DEMO_ROOT / "am-cakeml").exists, s"'am-cakeml' should be a subdirectory of $DEMO_ROOT")
assert ((DEMO_ROOT / "asp-libs").exists, s"'asp-libs' should be a subdirectory of $DEMO_ROOT")
assert ((DEMO_ROOT / "INSPECTA-models/LICENSE").exists, s"The INSPECTA-models repo should be cloned under $DEMO_ROOT")

val testsDir = DEMO_ROOT / "am-cakeml" / "tests"

val micro_composite = testsDir / "DemoFiles" / "goldenFiles" / "micro_composite.txt"

var aargs = ISZ[String]((testsDir / "CI" / "Test.sh").value, "-t", "micro", "-h",
  "-s", system_args.value)

if (ops.ISZOps(Os.cliArgs).contains("provision")) {
  def provision(extension: String): Unit = {
    val cached_micro_composite = home / s"${extension}_micro_composite.txt"
    if (cached_micro_composite.exists) {
      cached_micro_composite.remove()
    }
    val f = home / s"${extension}_$model_args"
    run(s"Provisioning $f", Os.proc(aargs :+ "-m" :+ f.value :+ "-p"), F, F)

    assert(micro_composite.exists)
    micro_composite.copyOverTo(cached_micro_composite)
  }
  if (aadlDir.exists) {
    provision("aadl")
  }
  if (sysmlDir.exists) {
    provision("sysml")
  }
  Os.exit(0)
} else {
  val extension =
    if (ops.ISZOps(Os.cliArgs).contains("aadl")) "aadl"
    else if (ops.ISZOps(Os.cliArgs).contains("sysml")) "sysml"
    else "aadl"
  val cached_micro_composite = home / s"${extension}_micro_composite.txt"
  cached_micro_composite.copyOverTo(micro_composite)
  val f = home / s"${extension}_$model_args"
  val result = run(s"Appraising $f", Os.proc(aargs :+ "-m" :+ f.value), F, T)
  println(s"Appraisal ${if (result == 0) "succeeded" else "failed"}!")
  Os.exit(result)
}