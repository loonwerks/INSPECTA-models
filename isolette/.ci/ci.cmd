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

@strictpure def verbose: B = ops.ISZOps(Os.cliArgs).contains("verbose")
@strictpure def disable_logika: B = ops.ISZOps(Os.cliArgs).contains("disable-logika")
@strictpure def disable_verus: B = ops.ISZOps(Os.cliArgs).contains("disable-verus")

def run(title: String, verboseArg: B, proc: OsProto.Proc): Z = {
  println(s"$title ...")
  val r = (if (verbose | verboseArg) proc.console.echo else proc).run()
  if (!r.ok) {
    println(s"$title failed!")
    cprintln(F, r.out)
    cprintln(T, r.err)
  }
  return r.exitCode
}

println(
  st"""**************************************************************************
      |*                            ISOLETTE                                    *
      |**************************************************************************""".render
)

def clean(d: Os.Path): Unit = {
  if (result == 0) {
    result = run(s"Cleaning $d", F, proc"$sireum slang run ${homeDir / "aadl" / "bin" / "clean.cmd"} $d")
  }
}

def removeBuildArtifacts(): Unit = {
  val removeNames = ops.ISZOps(ISZ("build", "out", "target"))
  val removeDirs = Os.Path.walk(homeDir, T, F, p => p.isDir && removeNames.contains(p.name))
  for (d <- removeDirs) {
    d.removeAll()
  }
}

val hasMicrokit: B =  Os.env("MICROKIT_SDK").nonEmpty

val slangDir = homeDir / "hamr" / "slang"
clean(slangDir)

println(st"""╔══════════════════════════════════╗
            |║  AADL + JVM + runtime monitoring ║
            |╚══════════════════════════════════╝""".render)
if (result == 0) {
  val args = s"--platform JVM --runtime-monitoring --slang-output-dir $slangDir"

  result = run("Running codegen from AADL model targeting JVM", F, proc"$sireum slang run ${homeDir / "aadl" / "bin" / "run-hamr.cmd"} $args")
  removeBuildArtifacts()
}

if (result == 0) {
  result = run("Running JVM unit tests from AADL model targeting JVM", F, proc"$sireum proyek test ${homeDir / "hamr" / "slang"}")
}


println(st"""╔═════════════════════════════════════╗
            |║  SysMLv2 + JVM + runtime monitoring ║
            |╚═════════════════════════════════════╝""".render)
if (result == 0) {
  val args = s"--platform JVM --runtime-monitoring --slang-output-dir $slangDir"

  result = run("Running codegen from SysMLv2 model targeting JVM", F, proc"$sireum slang run ${homeDir / "sysml" / "bin" / "run-hamr.cmd"} $args")
  removeBuildArtifacts()
}

if (result == 0 && !disable_logika) {
  result = run("Verifying via Logika", T, proc"$sireum slang run ${homeDir / "hamr" / "slang" / "bin" / "run-logika.cmd"}")
}

if (result == 0 && !disable_logika) {
  val libs = Os.env("SYSML_AADL_LIBRARIES").get
  result = run("Checking integration constraints", F, proc"$sireum hamr sysml logika --sourcepath ${homeDir / "sysml"}:$libs")
}


val microkitDir = homeDir / "hamr" / "microkit"
clean(microkitDir)


println(st"""╔═════════════════════════════════════════════════════════════╗
            |║  SysMLv2 + Microkit + domain scheduler + runtime monitoring ║
            |╚═════════════════════════════════════════════════════════════╝""".render)
if (result == 0) {
  val args = s"--platform Microkit --runtime-monitoring --sel4-output-dir $microkitDir"

  result = run("Running codegen from SysMLv2 model targeting Microkit", F, proc"$sireum slang run ${homeDir / "sysml" / "bin" / "run-hamr.cmd"} $args")
  removeBuildArtifacts()
}

if (result == 0 && Os.env("AM_REPOS_ROOT").nonEmpty) {
  result = run("Running SysMLv2 attestation", F, proc"$sireum slang run ${homeDir / "hamr" / "microkit" / "attestation" / "sysml_attestation.cmd"} appraise")
}

if (result == 0 && hasMicrokit) {
  result = run("Building the image", F, proc"make".at(homeDir / "hamr" / "microkit"))
  removeBuildArtifacts()
}


println(st"""╔══════════════════════════════════════════════════════════╗
            |║  AADL + Microkit + domain scheduler + runtime monitoring ║
            |╚══════════════════════════════════════════════════════════╝""".render)
if (result == 0) {
  val args = s"--platform Microkit --runtime-monitoring --sel4-output-dir $microkitDir"

  result = run("Running codegen from AADL model targeting Microkit", F, proc"$sireum slang run ${homeDir / "aadl" / "bin" / "run-hamr.cmd"} $args")
  removeBuildArtifacts()
}

if (result == 0 && Os.env("AM_REPOS_ROOT").nonEmpty) {
  result = run("Running AADL attestation", F, proc"$sireum slang run ${homeDir / "hamr" / "microkit" / "attestation" / "aadl_attestation.cmd"} appraise")
}

if (result == 0 && hasMicrokit) {
  result = run("Building and verifying the image", F, proc"make".at(homeDir / "hamr" / "microkit"))
  removeBuildArtifacts()
}

if (result == 0 && hasMicrokit) {
  result = run("Running the microkit unit tests", F, proc"make test".at(homeDir / "hamr" / "microkit"))
  removeBuildArtifacts()
}


if (Os.env("MICROKIT_SDK_CURRENT").nonEmpty) {
 
  // use 2.x.x microkit sdk for user-land scheduling
  val envs: ISZ[(String, String)] = ISZ(("MICROKIT_SDK", Os.env("MICROKIT_SDK_CURRENT").get))

  println(st"""╔═════════════════════════════════════════════════════════════════════════════════════════╗
              |║  SysMLv2 + Microkit + user-land scheduler + runtime monitoring + verus attribute syntax ║
              |╚═════════════════════════════════════════════════════════════════════════════════════════╝""".render)
  val microkitMcsDir = homeDir / "hamr" / "microkit_mcs"
  clean(microkitMcsDir)

  if (result == 0) {
    val args = s"--platform Microkit --runtime-monitoring --scheduling UserLand --verus-attribute-syntax --sel4-output-dir $microkitMcsDir"

    result = run("Running codegen from SysMLv2 model targeting Microkit with user-land scheduler", F,
      proc"$sireum slang run ${homeDir / "sysml" / "bin" / "run-hamr.cmd"} $args".env(envs))
    removeBuildArtifacts()
  }

  if (result == 0 && hasMicrokit) {
    result = run("Building and verifying the image", F, proc"make".at(microkitMcsDir).env(envs))
    removeBuildArtifacts()
  }

  if (result == 0 && hasMicrokit) {
    result = run("Building and verifying the image with runtime monitoring", F, proc"make CONFIG=gumbo_monitor.mk".at(microkitMcsDir).env(envs))
    removeBuildArtifacts()
  }

  if (result == 0 && hasMicrokit) {
    result = run("Running the microkit unit tests", F, proc"make test".at(microkitMcsDir).env(envs))
    removeBuildArtifacts()
  }

}
Os.exit(result)
