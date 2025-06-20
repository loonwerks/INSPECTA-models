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

val rootDir = Os.slashDir.up.up.up

val sireumBin = Os.path(Os.env("SIREUM_HOME").get) / "bin"
val sireum = sireumBin / (if(Os.isWin) "sireum.bat" else "sireum")

val excludes: ops.ISZOps[Os.Path] = ops.ISZOps(ISZ(
  rootDir / "micro-examples" / "case-transition-models"
))

println(st"""Options: 
            |  disable-logika
            |  disable-verus""".render)

var result: Z = 0
def findCIs(p: Os.Path): Unit = {
  if (!excludes.contains(p)) {
    if(p.isFile && p.name == "ci.cmd") {
      val args = st"${(Os.cliArgs, " ")}".render
      val r = proc"$sireum slang run $p $args".console.echo.run()
      result = result + r.exitCode
    } else if(p.isDir) {
      p.list.foreach((m: Os.Path) => findCIs(m))
    }
  }
}
findCIs(rootDir)

Os.exit(result)
