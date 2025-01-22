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

val attestations = Os.Path.walk(rootDir, T, T, p => p.name == "attestation" && p.isDir && (p / "run-attestation.cmd").exists)

val canonical = rootDir / "isolette" / "attestation" / "run-attestation.cmd"

if (ops.ISZOps(Os.cliArgs).contains("update")) {
  for (a <- attestations if (a != canonical.up)) {
    a.removeAll()
    a.mkdir()
    canonical.copyOverTo(a / "run-attestation.cmd")
    println(s"Wrote: ${a / "run-attestation.cmd"}")
  }
} else if (ops.ISZOps(Os.cliArgs).contains("provision")) {
  for (a <- attestations if (a != canonical.up)) {
    proc"$sireum slang run ${a / "run-attestation.cmd"} provision".console.run()    
  }
} else if (ops.ISZOps(Os.cliArgs).contains("appraise")) {
  for (a <- attestations) {
    if ( (a.up / "aadl").exists) {
      proc"$sireum slang run ${a / "run-attestation.cmd"} aadl".console.run()
    }
    if ( (a.up / "sysml").exists) {
      proc"$sireum slang run ${a / "run-attestation.cmd"} sysml".console.run()
    }
  }
} else {
  println("Usage: update | provision | appraise")
}

