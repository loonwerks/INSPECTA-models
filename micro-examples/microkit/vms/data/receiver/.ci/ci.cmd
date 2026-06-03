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

val homeDir = Os.slashDir.up

val sireumBin = Os.path(Os.env("SIREUM_HOME").get) / "bin"
val sireum = sireumBin / (if (Os.isWin) "sireum.bat" else "sireum")
var result: Z = 0

@strictpure def verbose: B = ops.ISZOps(Os.cliArgs).contains("verbose")
@strictpure def disable_logika: B = ops.ISZOps(Os.cliArgs).contains("disable-logika")
@strictpure def disable_verus: B = ops.ISZOps(Os.cliArgs).contains("disable-verus")

def run(title: String, verboseArg: B, proc: OsProto.Proc): Z = {
  println(s"$title ...")
  val r = (if (verbose | verboseArg) proc.console.echo else proc).run()
  if (!r.ok) {
    println(s"$title failed!")
    cprintln(F, r.out)
    cprintln(T, r.err)
  }
  return r.exitCode
}

println(
  st"""**************************************************************************
      |*                            VMS Data Receiver                           *
      |**************************************************************************""".render
)

def clean(d: Os.Path): Unit = {
  result = run(s"Cleaning $d", F, proc"$sireum slang run ${homeDir / "aadl" / "bin" / "clean.cmd"} $d")
}

def removeBuildArtifacts(): Unit = {
  val removeNames = ops.ISZOps(ISZ("build", "out", "target"))
  val removeDirs = Os.Path.walk(homeDir, T, F, p => p.isDir && removeNames.contains(p.name))
  for (d <- removeDirs) {
    d.removeAll()
  }
}

// domain scheduling

val microkitDir = homeDir / "hamr" / "microkit"
clean(microkitDir)

if (result == 0) {
  val args = s"--platform Microkit --sel4-output-dir $microkitDir"

  result = run("Running AADL codegen targeting Microkit with domain scheduling", F, proc"$sireum slang run ${homeDir / "aadl" / "bin" / "run-hamr.cmd"} $args")
  removeBuildArtifacts()
}

if (result == 0 && Os.env("MICROKIT_SDK").nonEmpty) {
  result = run(s"Building the image at $microkitMcsDir", F, proc"make".at(homeDir / "hamr" / "microkit"))
  removeBuildArtifacts()
}

if (result == 0 && Os.env("AM_REPOS_ROOT").nonEmpty) {
  result = run("Running AADL attestation", F, proc"$sireum slang run ${homeDir / "hamr" / "microkit" / "attestation" / "aadl_attestation.cmd"} appraise")
}



// user land scheduling

// use 2.x.x microkit sdk for user-land scheduling
val env: ISZ[(String, String)] = ISZ(("MICROKIT_SDK", Os.env("MICROKIT_SDK_CURRENT").get))

val microkitMcsDir = homeDir / "hamr" / "microkit_mcs"
clean(microkitMcsDir)

if (result == 0) {
  val args = s"--platform Microkit --scheduling UserLand --sel4-output-dir $microkitMcsDir"

  result = run("Running SysML codegen targeting Microkit with user land scheduling", F, proc"$sireum slang run ${homeDir / "sysml" / "bin" / "run-hamr.cmd"} $args")
  removeBuildArtifacts()
}

if (result == 0 && Os.env("MICROKIT_SDK").nonEmpty) {
  result = run(s"Building the image at $microkitMcsDir", F, proc"make".at(microkitMcsDir).env(env))
  removeBuildArtifacts()
}


Os.exit(result)