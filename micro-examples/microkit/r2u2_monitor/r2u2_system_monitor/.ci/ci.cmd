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
      |*                          R2U2 SYSTEM MONITOR                           *
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

// Building a monitor crate installs r2u2_cli from crates.io and, for the C variant, clones
// the R2U2 repo, so the make steps need network access on top of the Microkit SDK.
val hasMicrokit: B = Os.env("MICROKIT_SDK").nonEmpty

val hamrDir = homeDir / "hamr"
val aadlDir = homeDir / "aadl"

// The Rust and C variants are separate AADL packages -- both declare a system implementation
// named Sys.i, so they need separate directories to keep the recorded AIR unambiguous. MCS has
// to be requested with --scheduling UserLand here: the HAMR_Microkit property set defines only
// SMC and Passive, so an AADL model cannot declare HAMR_Microkit::Scheduling the way its SysML
// sibling does.
def codegenFromAadl(title: String, variantDir: Os.Path, sel4OutputDir: Os.Path): Unit = {
  if (result == 0) {
    result = run(title, F,
      proc"$sireum hamr codegen --platform Microkit --scheduling UserLand --verbose --sel4-output-dir $sel4OutputDir --workspace-root-dir $variantDir ${variantDir / ".slang" / "AIR.json"}")
  }
}

def buildImage(dir: Os.Path, attestation: Os.Path): Unit = {
  if (result == 0 && Os.env("AM_REPOS_ROOT").nonEmpty) {
    result = run(s"Running attestation for ${dir.name}", F, proc"$sireum slang run $attestation appraise")
  }

  if (result == 0 && hasMicrokit) {
    result = run(s"Building the image in ${dir.name}", F, proc"make".at(dir))

    if (result == 0) {
      result = run(s"Running the microkit unit tests in ${dir.name}", F, proc"make test".at(dir))
    }

    if (result == 0 && !disable_verus) {
      result = run(s"Building/Verifying component and system contracts in ${dir.name}", F, proc"make verus".at(dir))
    }

    removeBuildArtifacts()
  }
}

println(st"""╔════════════════════════════════════════════════════════════╗
            |║  AADL + Microkit + user-land scheduler + Rust components   ║
            |╚════════════════════════════════════════════════════════════╝""".render)
val mcsDir = hamrDir / "microkit_mcs"
clean(mcsDir)
codegenFromAadl("Running codegen from the AADL model with Rust components", aadlDir / "rust", mcsDir)
buildImage(mcsDir, mcsDir / "attestation" / "aadl_attestation.cmd")

println(st"""╔════════════════════════════════════════════════════════════╗
            |║  AADL + Microkit + user-land scheduler + C components      ║
            |╚════════════════════════════════════════════════════════════╝""".render)
val mcsCDir = hamrDir / "microkit_mcs_c"
clean(mcsCDir)
codegenFromAadl("Running codegen from the AADL model with C components", aadlDir / "c", mcsCDir)
buildImage(mcsCDir, mcsCDir / "attestation" / "aadl_attestation.cmd")

println(st"""╔════════════════════════════════════════════════════════════╗
            |║  SysMLv2 + Microkit + user-land scheduler                  ║
            |╚════════════════════════════════════════════════════════════╝""".render)
val mcsSysmlDir = hamrDir / "microkit_mcs_sysml"
clean(mcsSysmlDir)
if (result == 0) {
  result = run("Running codegen from the SysMLv2 model", F,
    proc"$sireum slang run ${homeDir / "sysml" / "bin" / "run-hamr.cmd"}")
}
buildImage(mcsSysmlDir, mcsSysmlDir / "attestation" / "sysml_attestation.cmd")

Os.exit(result)
