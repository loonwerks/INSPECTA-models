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
    cprintln(F, st"${(proc.cmds, " ")}".render)
    cprintln(F, r.out)
    cprintln(T, r.err)
  }
  return r.exitCode
}

println(
  st"""**************************************************************************
      |*                          TEMP CONTROL                                  *
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

// user-land scheduling requires a 2.x.x microkit sdk; environments that default
// MICROKIT_SDK to the domain-scheduling sdk provide the 2.x.x one via MICROKIT_SDK_CURRENT
val envs: ISZ[(String, String)] =
  if (Os.env("MICROKIT_SDK_CURRENT").nonEmpty) ISZ(("MICROKIT_SDK", Os.env("MICROKIT_SDK_CURRENT").get))
  else ISZ()

val hasMicrokit: B = Os.env("MICROKIT_SDK").nonEmpty || Os.env("MICROKIT_SDK_CURRENT").nonEmpty

val microkitMcsDir = homeDir / "hamr" / "microkit_mcs"


//if (result == 0 && !disable_logika && Os.env("SYSML_AADL_LIBRARIES").nonEmpty) {
//  val libs = Os.env("SYSML_AADL_LIBRARIES").get
//  result = run("Checking integration constraints", F, proc"$sireum hamr sysml logika --sourcepath ${homeDir / "sysml"}:$libs")
//}


clean(microkitMcsDir)

println(st"""╔═════════════════════════════════════════════════════════════════════════════════════════╗
            |║  SysMLv2 + Microkit + user-land scheduler + runtime monitoring + verus attribute syntax ║
            |╚═════════════════════════════════════════════════════════════════════════════════════════╝""".render)
if (result == 0) {
  // The C temperature sensor embeds an R2U2 runtime monitor, so the generated
  // makefiles must compile and link the vendored R2U2 engine plus the sensor's
  // spec. These aux-code options are required for this project (see
  // sysml_readme.md) or the sensor will not build.
  val r2u2LibDir = microkitMcsDir / "r2u2" / "r2u2-lib"
  val sensorSpecDir = microkitMcsDir / "r2u2" / "sensor-spec"
  val auxCodeDirs = s"$r2u2LibDir${Os.pathSep}$sensorSpecDir"
  val args = s"--platform Microkit --runtime-monitoring --scheduling UserLand --verus-attribute-syntax --sel4-aux-code-dirs $auxCodeDirs --sel4-aux-code-symlink --sel4-output-dir $microkitMcsDir"

  result = run("Running codegen from SysMLv2 model targeting Microkit with user-land scheduler", F,
    proc"$sireum slang run ${homeDir / "sysml" / "bin" / "run-hamr.cmd"} $args".env(envs))
}

if (result == 0 && Os.env("AM_REPOS_ROOT").nonEmpty) {
  result = run("Running SysMLv2 attestation", F, proc"$sireum slang run ${microkitMcsDir / "attestation" / "sysml_attestation.cmd"} appraise".env(envs))
}

if (result == 0 && hasMicrokit) {
  result = run("Building the image", F, proc"make".at(microkitMcsDir).env(envs))

  if (result == 0) {
    result = run("Building with GUMBO runtime monitoring", F, proc"make CONFIG=gumbo_monitor.mk".at(microkitMcsDir).env(envs))
  }

  if (result == 0) {
    result = run("Building with System Verification runtime monitoring", F, proc"make CONFIG=sys_nominal_monitor.mk".at(microkitMcsDir).env(envs))
  }

  if (result == 0) {
    result = run("Building with user-land schedule runtime monitoring", F, proc"make CONFIG=userland_monitor.mk".at(microkitMcsDir).env(envs))
  }

  if (result == 0 && !disable_verus) {
    result = run("Building/Verifying component and system contracts", F, proc"make verus".at(microkitMcsDir).env(envs))
  }

  if (result == 0) {
    result = run("Running the microkit unit tests", F, proc"make test".at(microkitMcsDir).env(envs))
  }

  removeBuildArtifacts()
}

Os.exit(result)
