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

// Boots a built image under QEMU and fails on a fault, panic or violation (see
// .github/workflows/hamr/simulate.cmd).  Run it right after the image's `make`, with the same
// environment, and before its build directory is removed.
def bootUnderQemu(dir: Os.Path, options: ISZ[String]): Z = {
  var root = homeDir
  while (!(root / ".github").exists) {
    root = root.up
  }
  val simulate = root / ".github" / "workflows" / "hamr" / "simulate.cmd"
  return run(s"Booting the image in ${dir.name} under QEMU", T,
    Os.proc(ISZ[String](sireum.string, "slang", "run", simulate.string, dir.string) ++ options))
}

// Boots a monitored image under QEMU and judges what the runtime monitor reported.
// QEMU never exits on its own -- the schedule runs forever -- so the run is cut off after
// simSeconds of wall time (enough for dozens of hyperperiods).  Any contract, system
// assertion or schedule-conformance violation, or a panic, fails it; so does a run in
// which the monitor never logged at all, since silence is also what a hang looks like.
val simSeconds: Z = 60

def simulate(title: String, dir: Os.Path, config: String, evidence: ISZ[String]): Z = {
  if (!proc"which qemu-system-aarch64".run().ok) {
    println(s"$title ... skipped: qemu-system-aarch64 not found")
    return 0
  }
  println(s"$title ...")
  val logFile = Os.temp()
  val driver: String =
    st"""set -u
        |"$$@" > "$$LOG" 2>&1 &
        |mpid=$$!
        |sleep $$SECS
        |pkill -P $$mpid 2>/dev/null
        |kill $$mpid 2>/dev/null
        |wait $$mpid 2>/dev/null
        |exit 0""".render
  Os.proc(ISZ[String]("sh", "-c", driver, "sh", "make", "-C", dir.string, s"CONFIG=$config", "qemu"))
    .env(ISZ(("LOG", logFile.string), ("SECS", simSeconds.string)))
    .timeout((simSeconds + 60) * 1000).run()
  // the QEMU serial console emits CRLF
  val out: String = if (logFile.exists) ops.StringOps(logFile.read).replaceAllLiterally("\r", "") else ""
  logFile.removeAll()

  val markers = ops.ISZOps(ISZ[String]("CONTRACT VIOLATION", "SYS ASSERT VIOLATION",
    "SCHEDULE CONFORMANCE VIOLATION", "Schedule conformance check failed", "panicked"))
  val lines = ops.StringOps(out).split((c: C) => c == '\n')
  val bad = lines.filter((l: String) => markers.exists((m: String) => ops.StringOps(l).contains(m)))
  val missing = evidence.filter((e: String) => !ops.StringOps(out).contains(e))
  if (bad.isEmpty && missing.isEmpty) {
    println(s"$title ... ok: no violations in ${simSeconds}s")
    return 0
  }
  println(s"$title failed!")
  for (e <- missing) {
    cprintln(F, s"  expected in the log but never seen: $e")
  }
  // each distinct offending line once, with how often it occurred
  var counts: HashSMap[String, Z] = HashSMap.empty
  for (l <- bad) {
    counts = counts + l ~> (counts.get(l).getOrElse(0) + 1)
  }
  for (e <- counts.entries) {
    cprintln(F, s"  ${e._2}x ${e._1}")
  }
  cprintln(F, "---- captured QEMU output ----")
  cprintln(F, out)
  return 1
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
val microkitDir = homeDir / "hamr" / "microkit"


clean(slangDir)

println(st"""╔══════════════════════════════════╗
            |║  AADL + JVM + runtime monitoring ║
            |╚══════════════════════════════════╝""".render)
if (result == 0) {
  val args = s"--platform JVM --runtime-monitoring --slang-output-dir $slangDir"

  result = run("Running codegen from AADL model targeting JVM", F, proc"$sireum slang run ${homeDir / "aadl" / "bin" / "run-hamr.cmd"} $args")
}

if (result == 0) {
  result = run("Running JVM unit tests from AADL model targeting JVM", F, proc"$sireum proyek test ${homeDir / "hamr" / "slang"}")
  removeBuildArtifacts()
}


println(st"""╔═════════════════════════════════════╗
            |║  SysMLv2 + JVM + runtime monitoring ║
            |╚═════════════════════════════════════╝""".render)
if (result == 0) {
  val args = s"--platform JVM --runtime-monitoring --slang-output-dir $slangDir"

  result = run("Running codegen from SysMLv2 model targeting JVM", F, proc"$sireum slang run ${homeDir / "sysml" / "bin" / "run-hamr.cmd"} $args")
}

if (result == 0 && !disable_logika) {
  result = run("Verifying via Logika", T, proc"$sireum slang run ${homeDir / "hamr" / "slang" / "bin" / "run-logika.cmd"}")
}

if (result == 0 && !disable_logika) {
  val libs = Os.env("SYSML_AADL_LIBRARIES").get
  result = run("Checking integration constraints", F, proc"$sireum hamr sysml logika --sourcepath ${homeDir / "sysml"}:$libs")
}

removeBuildArtifacts()


clean(microkitDir)


println(st"""╔═════════════════════════════════════════════════════════════╗
            |║  SysMLv2 + Microkit + domain scheduler + runtime monitoring ║
            |╚═════════════════════════════════════════════════════════════╝""".render)
if (result == 0) {
  val args = s"--platform Microkit --runtime-monitoring --sel4-output-dir $microkitDir"

  result = run("Running codegen from SysMLv2 model targeting Microkit", F, proc"$sireum slang run ${homeDir / "sysml" / "bin" / "run-hamr.cmd"} $args")
}

if (result == 0 && Os.env("AM_REPOS_ROOT").nonEmpty) {
  result = run("Running SysMLv2 attestation", F, proc"$sireum slang run ${homeDir / "hamr" / "microkit" / "attestation" / "sysml_attestation.cmd"} appraise")
}

if (result == 0 && hasMicrokit) {
  result = run("Building the image", F, proc"make".at(homeDir / "hamr" / "microkit"))

  if (result == 0) {
    result = bootUnderQemu(homeDir / "hamr" / "microkit", ISZ())
  }
  removeBuildArtifacts()
}


println(st"""╔══════════════════════════════════════════════════════════╗
            |║  AADL + Microkit + domain scheduler + runtime monitoring ║
            |╚══════════════════════════════════════════════════════════╝""".render)
if (result == 0) {
  val args = s"--platform Microkit --runtime-monitoring --sel4-output-dir $microkitDir"

  result = run("Running codegen from AADL model targeting Microkit", F, proc"$sireum slang run ${homeDir / "aadl" / "bin" / "run-hamr.cmd"} $args")
}

if (result == 0 && Os.env("AM_REPOS_ROOT").nonEmpty) {
  result = run("Running AADL attestation", F, proc"$sireum slang run ${homeDir / "hamr" / "microkit" / "attestation" / "aadl_attestation.cmd"} appraise")
}

if (result == 0 && hasMicrokit) {
  result = run("Building and verifying the image", F, proc"make".at(homeDir / "hamr" / "microkit"))

  if (result == 0) {
    result = bootUnderQemu(homeDir / "hamr" / "microkit", ISZ())
  }

  if (result == 0) {
    result = run("Running the microkit unit tests", F, proc"make test".at(homeDir / "hamr" / "microkit"))
  }

  removeBuildArtifacts()
}

println(st"""╔═════════════════════════════════════════════════════════════════════════════════════════╗
            |║  SysMLv2 + Microkit + user-land scheduler + runtime monitoring + verus attribute syntax ║
            |╚═════════════════════════════════════════════════════════════════════════════════════════╝""".render)
val microkitMcsDir = homeDir / "hamr" / "microkit_mcs"
clean(microkitMcsDir)

if (result == 0) {
  // ENABLE_TEST_SCHEDULER adds the test_scheduler.mk variant that the system tests in
  // crates/test_controller run under (bin/run-tests.cmd); the default image is unaffected
  val args = s"--platform Microkit --runtime-monitoring --scheduling UserLand --verus-attribute-syntax --experimental-options ENABLE_TEST_SCHEDULER --sel4-output-dir $microkitMcsDir"

  result = run("Running codegen from SysMLv2 model targeting Microkit with user-land scheduler", F,
    proc"$sireum slang run ${homeDir / "sysml" / "bin" / "run-hamr.cmd"} $args")
}

if (result == 0 && hasMicrokit) {
  result = run("Building and verifying the image", F, proc"make".at(microkitMcsDir))

  if (result == 0) {
    result = bootUnderQemu(microkitMcsDir, ISZ())
  }

  if (result == 0) {
    result = run("Building with GUMBO runtime monitoring", F, proc"make CONFIG=gumbo_monitor.mk".at(microkitMcsDir))
  }

  if (result == 0) {
    result = simulate("Simulating with GUMBO runtime monitoring under QEMU", microkitMcsDir, "gumbo_monitor.mk",
      ISZ("[gumbo_monitor::"))
  }

  if (result == 0) {
    result = run("Building with System Verification runtime monitoring", F, proc"make CONFIG=sys_nominal_monitor.mk".at(microkitMcsDir))
  }

  if (result == 0) {
    result = simulate("Simulating with System Verification runtime monitoring under QEMU", microkitMcsDir, "sys_nominal_monitor.mk",
      ISZ("[sys_nominal_monitor::", "Schedule conformance check passed"))
  }

  if (result == 0) {
    result = run("Building/Verifying component and system contracts", F, proc"make verus".at(microkitMcsDir))
  }

  if (result == 0) {
    result = run("Running the microkit unit tests", F, proc"make test".at(microkitMcsDir))
  }

  // The system tests ported from the JVM ones (crates/test_controller/src/system_tests),
  // run on seL4 under QEMU with the test scheduler.  run-tests.cmd builds the
  // test_scheduler.mk image itself and fails unless every selected test passes.
  if (result == 0) {
    if (proc"which qemu-system-aarch64".run().ok) {
      println(st"""╔═════════════════════════════════════════════════════════════╗
                  |║  SysMLv2 + Microkit + user-land scheduler + system testing  ║
                  |╚═════════════════════════════════════════════════════════════╝""".render)      
      result = run("Running the system tests under QEMU", F,
        proc"$sireum slang run ${microkitMcsDir / "bin" / "run-tests.cmd"}")
    } else {
      println("Running the system tests under QEMU ... skipped: qemu-system-aarch64 not found")
    }
  }

  removeBuildArtifacts()
}
Os.exit(result)
