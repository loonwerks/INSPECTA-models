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

val slangDir = homeDir / "hamr" / "slang"

println(
  st"""**************************************************************************
      |*                            ISOLATE-ETHERNET_SIMPLE                     *
      |**************************************************************************""".render
)

if (result == 0) {
  result = run("Cleaning", F, proc"$sireum slang run ${homeDir / "aadl" / "bin" / "clean.cmd"}")
}

def removeBuildArtifacts(): Unit = {
  val removeNames = ops.ISZOps(ISZ("build", "out", "target"))
  val removeDirs = Os.Path.walk(homeDir, T, F, p => p.isDir && removeNames.contains(p.name))
  for (d <- removeDirs) {
    d.removeAll()
  }
}

if (result == 0) {
  result = run("Running codegen targeting JVM", F, proc"$sireum slang run ${homeDir / "aadl" / "bin" / "run-hamr.cmd"} JVM")
  removeBuildArtifacts()
}

if (result == 0) {
  println("!!!!!!!!!!!!!!! Disabling JVM unit tests -- need to add behavior code !!!!!!!!!!!!!!!!!!!!!")
  //result = run("Running JVM unit tests", F, proc"$sireum proyek test $slangDir")
  //removeBuildArtifacts()
}

if (result == 0) {
  println("!!!!!!!!!!!!!! Disabling Logika -- need to add behavior code !!!!!!!!!!!!!!!!!!!!!!!")
  //result = run("Verifying via Logika", F, proc"$sireum proyek logika --all $slangDir")
}

if (result == 0) {
  result = run("Running codegen targeting Microkit", F, proc"$sireum slang run ${homeDir / "aadl" / "bin" / "run-hamr.cmd"} Microkit")
  removeBuildArtifacts()
}

if (result == 0 && Os.env("MICROKIT_SDK").nonEmpty) {
  // behavior code hasn't been added so verification via 'cargo-verus build' will fail.  Use
  // the RUST_MAKE_TARGET env var to bypass verification so that cargo is used instead
  result = run("Building the image", F, proc"make".at(homeDir / "hamr" / "microkit").env(ISZ(("RUST_MAKE_TARGET", "build-release"))))
  if ((homeDir / "hamr" / "microkit" / "build").exists) {
    (homeDir / "hamr" / "microkit" / "build").removeAll()
  }
}

if (result == 0 && Os.env("AM_REPOS_ROOT").nonEmpty) {
  result = run("Running AADL attestation", F, proc"$sireum slang run ${homeDir / "hamr" / "microkit" / "attestation" / "aadl_attestation.cmd"} appraise")
}

Os.exit(result)