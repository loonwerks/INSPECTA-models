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

def run(title: String, verbose: B, proc: OsProto.Proc): Z = {
  println(s"$title ...")
  val r = (if (verbose) proc.console.echo else proc).run()
  if (!r.ok) {
    println(s"$title failed!")
    cprintln(F, r.out)
    cprintln(T, r.err)
  }
  return r.exitCode
}

@strictpure def disable_logika: B = ops.ISZOps(Os.cliArgs).contains("disable-logika")
@strictpure def disable_verus: B = ops.ISZOps(Os.cliArgs).contains("disable-verus")

println(
  st"""**************************************************************************
      |*                            Firewall-Simple-Byte-Array                  *
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

// AADL JVM
if (result == 0) {
  result = run("Running codegen from AADL model targeting JVM", F, proc"$sireum slang run ${homeDir / "aadl" / "bin" / "run-hamr.cmd"} JVM")
  removeBuildArtifacts()
}

if (result == 0 && !disable_logika) {
  result = run("Verifying via Logika", T, proc"$sireum slang run ${homeDir / "hamr" / "slang" / "bin" / "run-logika.cmd"}")
}

// AADL Microkit
if (result == 0) {
  result = run("Running codegen from AADL model targeting Microkit", F, proc"$sireum slang run ${homeDir / "aadl" / "bin" / "run-hamr.cmd"} Microkit")
  removeBuildArtifacts()
}

if (result == 0 && Os.env("AM_REPOS_ROOT").nonEmpty) {
  result = run("Running AADL attestation", F, proc"$sireum slang run ${homeDir / "hamr" / "microkit" / "attestation" / "aadl_attestation.cmd"} appraise")
}

val hasMicrokit: B =  Os.env("MICROKIT_SDK").nonEmpty

if (result == 0 && hasMicrokit) {
  result = run("Building the image", F, proc"make".at(homeDir / "hamr" / "microkit"))
  removeBuildArtifacts()
}

if (result == 0 && hasMicrokit) {
  result = run("Running microkit unit tests", F, proc"make test".at(homeDir / "hamr" / "microkit"))
  removeBuildArtifacts()
}

if (result == 0 && !disable_verus) {
  result = run("Verifying via Verus", F, proc"make verus".at(homeDir / "hamr" / "microkit"))
  removeBuildArtifacts()
}

Os.exit(result)
