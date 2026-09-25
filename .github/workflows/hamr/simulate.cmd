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

// Boots a built Microkit image under QEMU and judges the console, for the model CI scripts.
//
//   simulate.cmd <microkit-dir> [--config <file.mk>] [--env <NAME=VALUE>]* [--seconds <n>]
//                [--expect <text>]*
//
// Run it after the image has been built with plain `make` (or `make CONFIG=<file.mk>`):
// `make qemu` builds exactly the image `make` does, so booting it misses nothing, and
// building first keeps a build failure a build failure, with no time limit on it.  Pass the
// build's environment with --env (e.g. RUST_MAKE_TARGET=build-release): the image target is
// always remade, and under a different environment `make qemu` would rebuild the Rust crates
// a different way -- with Verus, say -- instead of booting.  The image is brought up to date
// with an untimed `make` first, so the timed window below is spent booting.
//
// QEMU never exits on its own -- the schedule runs forever -- so the run is cut off after
// --seconds (default 30) of wall time.  It fails if the system never booted, if any
// protection domain faulted, panicked or reported a contract, system assertion or schedule
// conformance violation, or if an --expect text never appeared.  It is skipped, and passes,
// when qemu-system-aarch64 is not installed.

val usage: String = "usage: simulate.cmd <microkit-dir> [--config <file.mk>] [--env <NAME=VALUE>]* [--seconds <n>] [--expect <text>]*"

if (Os.cliArgs.isEmpty) {
  eprintln(usage)
  Os.exit(1)
}

val dir: Os.Path = Os.path(Os.cliArgs(0)).canon
var config: Option[String] = None()
var seconds: Z = 30
var expects: ISZ[String] = ISZ()
var envs: ISZ[(String, String)] = ISZ()
var i: Z = 1
while (i < Os.cliArgs.size) {
  val a = Os.cliArgs(i)
  if (i + 1 >= Os.cliArgs.size) {
    eprintln(s"missing value for $a; $usage")
    Os.exit(1)
  }
  val v = Os.cliArgs(i + 1)
  a.native match {
    case "--config" => config = Some(v)
    case "--seconds" =>
      Z(v) match {
        case Some(n) => seconds = n
        case _ =>
          eprintln(s"--seconds needs a number, not $v")
          Os.exit(1)
      }
    case "--expect" => expects = expects :+ v
    case "--env" =>
      val eq = ops.StringOps(v).indexOf('=')
      if (eq <= 0) {
        eprintln(s"--env needs NAME=VALUE, not $v")
        Os.exit(1)
      }
      envs = envs :+ ((ops.StringOps(v).substring(0, eq), ops.StringOps(v).substring(eq + 1, v.size)))
    case _ =>
      eprintln(s"unknown option $a; $usage")
      Os.exit(1)
  }
  i = i + 2
}

val title: String = s"Simulating ${dir.name}${if (config.nonEmpty) s" (${config.get})" else ""} under QEMU"

if (!proc"which qemu-system-aarch64".run().ok) {
  println(s"$title ... skipped: qemu-system-aarch64 not found")
  Os.exit(0)
}

println(s"$title ...")

val configArgs: ISZ[String] = if (config.nonEmpty) ISZ[String](s"CONFIG=${config.get}") else ISZ[String]()

val build = Os.proc(ISZ[String]("make", "-C", dir.string) ++ configArgs).env(envs).run()
if (!build.ok) {
  println(s"$title failed: the image did not build")
  cprintln(F, build.out)
  cprintln(T, build.err)
  Os.exit(1)
}

val logFile = Os.temp()
// make qemu runs in its own process group, so that exactly this QEMU -- and not one another
// job on the machine is running -- is stopped at the end
val driver: String =
  st"""set -u
      |set -m
      |"$$@" > "$$LOG" 2>&1 &
      |mpid=$$!
      |set +m
      |sleep $$SECS
      |kill -TERM -- -$$mpid 2>/dev/null
      |wait $$mpid 2>/dev/null
      |exit 0""".render
val makeArgs: ISZ[String] = ISZ[String]("make", "-C", dir.string) ++ configArgs :+ "qemu"
Os.proc(ISZ[String]("sh", "-c", driver, "sh") ++ makeArgs)
  .env(envs ++ ISZ(("LOG", logFile.string), ("SECS", seconds.string)))
  .timeout((seconds + 60) * 1000).run()
// the QEMU serial console emits CRLF
val out: String = if (logFile.exists) ops.StringOps(logFile.read).replaceAllLiterally("\r", "") else ""
logFile.removeAll()

val bootMarker: String = "Microkit Monitor started"
val markers = ops.ISZOps(ISZ[String]("FAULT in user PD", "MON|ERROR", "panicked",
  "CONTRACT VIOLATION", "SYS ASSERT VIOLATION", "SCHEDULE CONFORMANCE VIOLATION",
  "Schedule conformance check failed"))
val lines = ops.StringOps(out).split((c: C) => c == '\n')
val bad = lines.filter((l: String) => markers.exists((m: String) => ops.StringOps(l).contains(m)))
val missing = (bootMarker +: expects).filter((e: String) => !ops.StringOps(out).contains(e))

if (bad.isEmpty && missing.isEmpty) {
  println(s"$title ... ok: booted, no faults or violations in ${seconds}s")
  Os.exit(0)
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
Os.exit(1)
