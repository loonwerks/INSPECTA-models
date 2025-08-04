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

val attestations = Os.Path.walk(rootDir, T, T, p => p.name == "attestation" && p.isDir && (p / "attestation.cmd").exists)

val verbose: B = ops.ISZOps(Os.cliArgs).contains("verbose")

@pure def run (s: String): Unit = {
  var p = proc"$s"
  if (verbose) {
    p = p.echo.console
  }
  val results = p.run()
  if (!results.ok) {
    println(results.out)
    println(results.err)
    Os.exit(results.exitCode)
  }
}

if (ops.ISZOps(Os.cliArgs).contains("provision")) {
  println("Provisioning ...")
  for (a <- attestations) {
    println(a.value)
    run(s"$sireum slang run $a/attestation.cmd provision ${if (verbose) "verbose" else "" }")
  }
} else if (ops.ISZOps(Os.cliArgs).contains("appraise")) {
  println("Appraising ...")
  for (a <- attestations) {
    println(a.value)
    run(s"$sireum slang run $a/attestation.cmd appraise")
  }
} else {
  println("Usage: [provision | appraise] <verbose>")
}

