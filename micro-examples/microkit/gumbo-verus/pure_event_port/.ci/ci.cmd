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

// Regression test for hamr-tutorials BR-02: Microkit codegen must not halt
// for Rust threads that have GUMBO integration constraints and also own
// pure (payload-less) event ports.  See ../readme.md.

import org.sireum._

val homeDir = Os.slashDir.up

val sireumBin = Os.path(Os.env("SIREUM_HOME").get) / "bin"
val sireum = sireumBin / (if (Os.isWin) "sireum.bat" else "sireum")
var result: Z = 0

@strictpure def verbose: B = ops.ISZOps(Os.cliArgs).contains("verbose")
@strictpure def disable_verus: B = ops.ISZOps(Os.cliArgs).contains("disable-verus")

def run(title: String, verboseArg: B, proc: OsProto.Proc): Z = {
  println(s"$title ...")
  val r = (if (verbose | verboseArg) proc.console.echo else proc).run()
  if (!r.ok) {
    println(s"$title failed!")
    cprintln(F, st"${(proc.cmds, " ")}".render)
    cprintln(F, r.out)
    cprintln(T, r.err)
  }
  return r.exitCode
}

println(
  st"""**************************************************************************
      |*                 GUMBO-VERUS Pure Event Port (BR-02)                    *
      |**************************************************************************""".render
)

def clean(d: Os.Path): Unit = {
  if (result == 0) {
    result = run(s"Cleaning $d", F, proc"$sireum slang run ${homeDir / "sysml" / "bin" / "clean.cmd"} $d")
  }
}

def removeBuildArtifacts(): Unit = {
  val removeNames = ops.ISZOps(ISZ("build", "out", "target"))
  val removeDirs = Os.Path.walk(homeDir, T, F, p => p.isDir && removeNames.contains(p.name))
  for (d <- removeDirs) {
    d.removeAll()
  }
}

val hasMicrokit: B = Os.env("MICROKIT_SDK").nonEmpty

val microkitMcsDir = homeDir / "hamr" / "microkit_mcs"

clean(microkitMcsDir)

if (result == 0) {
  val args = s"--platform Microkit --sel4-output-dir $microkitMcsDir"

  result = run("Running codegen from SysMLv2 targeting Microkit", F,
    proc"$sireum slang run ${homeDir / "sysml" / "bin" / "run-hamr.cmd"} $args")
}

if (result == 0 && hasMicrokit) {
  result = run("Building the image", F, proc"make".at(microkitMcsDir))
}

if (result == 0 && hasMicrokit && !disable_verus) {
  result = run("Building/Verifying component contracts", F, proc"make verus".at(microkitMcsDir))
}

removeBuildArtifacts()

Os.exit(result)
