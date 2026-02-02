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
      |*                            GUMBO-VERUS Structs_arrays                  *
      |**************************************************************************""".render
)

if (result == 0) {
  result = run("Cleaning", F, proc"$sireum slang run ${homeDir / "aadl" / "bin" / "clean.cmd"}")
}

if (result == 0) {
  if ((homeDir / "sysml" / "sysml-aadl-libraries").exists) {
    ((homeDir / "sysml" / "sysml-aadl-libraries")).removeAll()
  }
  result = run("Cloning https://github.com/santoslab/sysml-aadl-libraries.git", F, proc"git clone https://github.com/santoslab/sysml-aadl-libraries.git sysml-aadl-libraries".at(homeDir / "sysml"))
}

def removeBuildArtifacts(): Unit = {
  val removeNames = ops.ISZOps(ISZ("build", "out", "target"))
  val removeDirs = Os.Path.walk(homeDir, T, F, p => p.isDir && removeNames.contains(p.name))
  for (d <- removeDirs) {
    d.removeAll()
  }
}

///////////////////////////////////////////////////////////////////////////////////////
// AADL
///////////////////////////////////////////////////////////////////////////////////////

if (result == 0) {
  result = run("Running codegen from AADL targeting JVM", F, proc"$sireum slang run ${homeDir / "aadl" / "bin" / "run-hamr.cmd"} JVM")
}

if (result == 0) {
  result = run("Compiling JVM", F, proc"$sireum proyek compile ${homeDir / "hamr" / "slang"}")
}

if (result == 0) {
  result = run("Running codegen from AADL targeting Microkit", F, proc"$sireum slang run ${homeDir / "aadl" / "bin" / "run-hamr.cmd"} Microkit")
}

if (result == 0) {
  result = run("Building/Verifying with Verus", F, proc"make verus".at(homeDir / "hamr" / "microkit"))
  removeBuildArtifacts()
}

val hasMicrokit: B =  Os.env("MICROKIT_SDK").nonEmpty

/*
if (result == 0 && hasMicrokit) {
  // TODO: add behavior code
  result = run("Running microkit unit tests", F, proc"make test".at(homeDir / "hamr" / "microkit"))
  removeBuildArtifacts()
}
*/

if (result == 0 && hasMicrokit && Os.env("AM_REPOS_ROOT").nonEmpty) {
  result = run("Running AADL attestation", F, proc"$sireum slang run ${homeDir / "hamr" / "microkit" / "attestation" / "aadl_attestation.cmd"} appraise")
}


///////////////////////////////////////////////////////////////////////////////////////
// SysMLv2
///////////////////////////////////////////////////////////////////////////////////////

if (result == 0) {
  result = run("Running codegen from SysMLv2 targeting JVM", F, proc"$sireum slang run ${homeDir / "sysml" / "bin" / "run-hamr.cmd"} JVM")
}

if (result == 0) {
  result = run("Compiling JVM", F, proc"$sireum proyek compile ${homeDir / "hamr" / "slang_sysml"}")
}

if (result == 0) {
  result = run("Running codegen from SysMLv2 targeting Microkit", F, proc"$sireum slang run ${homeDir / "sysml" / "bin" / "run-hamr.cmd"} Microkit")
}

if (result == 0) {
  result = run("Building/Verifying with Verus", F, proc"make verus".at(homeDir / "hamr" / "microkit_sysml"))
  removeBuildArtifacts()
}

/*
if (result == 0 && hasMicrokit) {
  // TODO: add behavior code
  result = run("Running microkit unit tests", F, proc"make test".at(homeDir / "hamr" / "microkit_sysml"))
  removeBuildArtifacts()
}
*/

if (result == 0 && hasMicrokit && Os.env("AM_REPOS_ROOT").nonEmpty) {
  result = run("Running SysML attestation", F, proc"$sireum slang run ${homeDir / "hamr" / "microkit_sysml" / "attestation" / "sysml_attestation.cmd"} appraise")
}

Os.exit(result)