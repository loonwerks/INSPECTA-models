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

val slangDir = homeDir / "hamr" / "slang"

println(
  st"""**************************************************************************
      |*                            ISOLATE-ETHERNET_SIMPLE                     *
      |**************************************************************************""".render
)

if (result == 0) {
  result = run("Cleaning", F, proc"$sireum slang run ${homeDir / "aadl" / "bin" / "clean.cmd"}")
}

if (result == 0) {
  result = run("Running codegen targeting JVM", F, proc"$sireum slang run ${homeDir / "aadl" / "bin" / "run-hamr.cmd"} JVM")
}

if (result == 0) {
  println("!!!!!!!!!!!!!!! Disabling JVM unit tests -- need to add behavior code !!!!!!!!!!!!!!!!!!!!!")
  //result = run("Running JVM unit tests", F, proc"$sireum proyek test $slangDir")
}

if (result == 0) {
  println("!!!!!!!!!!!!!! Disabling Logika -- need to add behavior code !!!!!!!!!!!!!!!!!!!!!!!")
  //result = run("Verifying via Logika", F, proc"$sireum proyek logika --all $slangDir")
}

if (result == 0) {
  result = run("Running codegen targeting Microkit", F, proc"$sireum slang run ${homeDir / "aadl" / "bin" / "run-hamr.cmd"} Microkit")
}

if (result == 0 && Os.env("MICROKIT_SDK").nonEmpty) {
  result = run("Building the image", F, proc"make".at(homeDir / "hamr" / "microkit"))
  if ((homeDir / "hamr" / "microkit" / "build").exists) {
    (homeDir / "hamr" / "microkit" / "build").removeAll()
  }
}

if (result == 0 && Os.env("DEMO_ROOT").nonEmpty) {
  result = run("Running AADL attestation", F, proc"$sireum slang run ${homeDir / "attestation" / "run-attestation.cmd"} aadl")
}

Os.exit(result)