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

val sysmlDir = Os.slashDir.up

val sireumBin = Os.path(Os.env("SIREUM_HOME").get) / "bin"
val sireum = sireumBin / (if(Os.isWin) "sireum.bat" else "sireum")

if(Os.cliArgs.size > 1) {
  eprintln("Only expecting a single argument")
  Os.exit(1)
}

val platform: String =
  if(Os.cliArgs.nonEmpty) Os.cliArgs(0)
  else "Microkit"

val sel4_output_dir =
  if (platform == "Microkit") "microkit_mcs"
  else "sysml"

val hamrDir = sysmlDir.up / "hamr"

var codegenArgs = ISZ(
  sireum.value, "hamr", "sysml", "codegen",
  "--platform", platform,
  "--sel4-output-dir", (hamrDir / sel4_output_dir).string,
  "--verbose",
  "--workspace-root-dir", sysmlDir.string,
  "--sourcepath", sysmlDir.value,
  "--system-name", "vmR_data::top_impl",
)

if (platform == "JVM") {
  codegenArgs = codegenArgs :+ "--runtime-monitoring"
} else {
  println("***********************************************************************")
  println(s"Note: runtime-monitoring support is not yet available for ${platform} ")
  println("***********************************************************************")
}

codegenArgs = codegenArgs :+ "--no-proyek-ive"

codegenArgs = codegenArgs :+ (sysmlDir / "vmR_data.sysml").value

val results = Os.proc(codegenArgs).echo.console.run()

if(results.exitCode == 0 || results.exitCode == 23) {
  Os.exit(0)
} else {
  println(results.err)
  Os.exit(results.exitCode)
}
