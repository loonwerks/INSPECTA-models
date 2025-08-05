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

val verbose: B = ops.ISZOps(Os.cliArgs).contains("verbose")

@pure def run (s: String): Unit = {
  var p = proc"$s".echo
  if (verbose) {
    p = p.console
  }
  val results = p.run()
  if (!results.ok) {
    println(results.out)
    println(results.err)
    Os.exit(results.exitCode)
  }
}

@pure def getLang(i: Z): String = {
  if (i < Os.cliArgs.size - 1) {
    val lang = Os.cliArgs(i + 1)
    if (lang == "aadl" || lang == "sysml") {
      return lang
    }
  }
  println("Must supply either 'aadl' or 'sysml'")
  Os.exit(1)
  halt("")
}

if (ops.ISZOps(Os.cliArgs).contains("provision")) {
  val lang = getLang(ops.ISZOps(Os.cliArgs).indexOf("provision"))
  println(s"Provisioning $lang ...")
  val attestations = Os.Path.walk(rootDir, T, T, p => p.name == s"${lang}_attestation.cmd")
  for (a <- attestations) {
    run(s"$sireum slang run $a provision ${if (verbose) "verbose" else "" }")
  }
} else if (ops.ISZOps(Os.cliArgs).contains("appraise")) {
  val lang = getLang(ops.ISZOps(Os.cliArgs).indexOf("appraise"))
  println(s"Appraising $lang ...")
  val attestations = Os.Path.walk(rootDir, T, T, p => p.name == s"${lang}_attestation.cmd")
  for (a <- attestations) {
    run(s"$sireum slang run $a appraise")
  }
} else {
  println("Usage: [verbose] (provision|appraise) (aadl|sysml)")
}

