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

val sysmlDir: Os.Path = Os.slashDir.up
val hamrDir: Os.Path = sysmlDir.up / "hamr"

val sireumBin: Os.Path = Os.path(Os.env("SIREUM_HOME").get) / "bin"
val sireum: Os.Path = sireumBin / (if (Os.isWin) "sireum.bat" else "sireum")

if (Os.cliArgs.size > 1) {
  eprintln("Only expecting an optional HAMR platform argument")
  Os.exit(1)
}

val platform: String =
  if (Os.cliArgs.nonEmpty) Os.cliArgs(0)
  else "Microkit"

var sourcePath: String = sysmlDir.string
if (!(sysmlDir / "sysml-aadl-libraries").exists && Os.envs.contains("SYSML_AADL_LIBRARIES")) {
  sourcePath = s"$sourcePath:${Os.env("SYSML_AADL_LIBRARIES").get}"
}

val slangOutputDir: Os.Path = hamrDir / (if (platform == "JVM") "slang_sysml" else "sysml")
val sel4OutputDir: Os.Path = hamrDir / (if (platform == "Microkit") "microkit_mcs_sysml" else "sysml")

val codegenArgs: ISZ[String] = ISZ(
  sireum.value, "hamr", "sysml", "codegen",
  "--platform", platform,
  "--package-name", "integer_toy_extended",
  "--slang-output-dir", slangOutputDir.string,
  "--output-c-dir", (hamrDir / "c").string,
  "--sel4-output-dir", sel4OutputDir.string,
  "--run-transpiler",
  "--bit-width", "32",
  "--max-string-size", "256",
  "--max-array-size", "32768",
  "--verbose",
  "--no-proyek-ive",
  "--workspace-root-dir", sysmlDir.string,
  "--sourcepath", sourcePath,
  "--system-name", "Integer_Toy_Extended::'above_top.Impl'",
  (sysmlDir / "Integer_Toy_Extended.sysml").value
)

val results = Os.proc(codegenArgs).echo.console.run()

if (results.exitCode == 0 || results.exitCode == 23) {
  Os.exit(0)
} else {
  println(results.err)
  Os.exit(results.exitCode)
}
