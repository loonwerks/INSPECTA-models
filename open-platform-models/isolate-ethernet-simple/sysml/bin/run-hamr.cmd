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
val sireum = sireumBin / (if (Os.isWin) "sireum.bat" else "sireum")

val platform: String =
  if (Os.cliArgs.nonEmpty) Os.cliArgs(0)
  else "Microkit"

var codegenArgs = ISZ("hamr", "sysml", "codegen",
  "--platform", platform,
  "--output-dir", (sysmlDir.up / "hamr").string,
  "--package-name", "microkit",
  "--verbose"
  )

codegenArgs = codegenArgs :+ (sysmlDir / "SW.sysml").string

val results = Os.proc(ISZ(sireum.string) ++ codegenArgs).echo.console.run()

if (results.exitCode == 0) {
  Os.exit(0)
} else {
  println(results.err)
  Os.exit(results.exitCode)
}
