::#! 2> /dev/null                                   #
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
::!#
// #Sireum

import org.sireum._

val hamrDir: Os.Path = Os.slashDir.up.up / "hamr"
val slangDir = hamrDir / "slang"
val cDir = hamrDir / "c"

val toKeep = ops.ISZOps(ISZ(
  (slangDir / ".idea"),

  (slangDir / "src" / "main" / "component"),
  (slangDir / "src" / "test" / "bridge"),

  (cDir / "ext-c/consumer_thread_i_consumer_consumer"),
  (cDir / "ext-c/producer_thread_i_producer_producer"),
))


def rec(p: Os.Path, onlyDelAutoGen: B): Unit = {
  if(p.isFile) {
    if ((!toKeep.contains(p) && !onlyDelAutoGen) || ops.StringOps(p.read).contains("Do not edit")) {
      p.remove()
      println(s"Removed file: $p")
    }
  } else {
    for (pp <- p.list) {
      rec(pp, toKeep.contains(p) || onlyDelAutoGen)
    }
    if (p.list.isEmpty) {
      p.removeAll()
      println(s"Removed empty directory: $p")
    }
  }
}

if (Os.cliArgs.nonEmpty) {
  for (a <- Os.cliArgs) {
    val d = Os.slashDir / a
    assert (d.exists, s"$d is not a valid directory")
    rec(d, F)
  }
} else {
  rec(hamrDir, F)
}