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

val excludes: ops.ISZOps[Os.Path] = ops.ISZOps(ISZ(
  rootDir / "micro-examples" / "case-transition-models"
))

println(st"""Options:
            |  disable-logika
            |  disable-verus
            |  list-models     print the models this invocation would run, then exit
            |
            |Environment:
            |  HAMR_CI_MODELS       run only these models (repo-relative dirs, comma and/or
            |                       newline separated); unset runs every model
            |  HAMR_CI_SKIP_MODELS  run every model except these; ignored when HAMR_CI_MODELS
            |                       is set""".render)

// Model directories named by one of the environment variables. Commas and newlines both
// separate, so a workflow can use a YAML block scalar and keep one model per line.
@pure def parseSelection(v: String): ISZ[String] = {
  val flat = ops.StringOps(v).replaceAllLiterally("\n", ",")
  return for (e <- ops.StringOps(flat).split((c: C) => c == ',') if ops.StringOps(e).trim != "")
    yield ops.StringOps(e).trim
}

// rootDir / "a/b/c", folded a segment at a time so the separator is handled the same everywhere
@pure def resolve(rel: String): Os.Path = {
  return ops.ISZOps(ops.StringOps(rel).split((c: C) => c == '/')).
    foldLeft((acc: Os.Path, seg: String) => acc / seg, rootDir)
}

// A model named in the environment that does not exist is an error rather than a silent
// omission: a typo in a workflow matrix would otherwise produce a green shard that ran nothing.
def resolveModels(v: String, envName: String): ISZ[Os.Path] = {
  var r = ISZ[Os.Path]()
  for (rel <- parseSelection(v)) {
    val script = resolve(rel) / ".ci" / "ci.cmd"
    if (!script.exists) {
      eprintln(s"$envName names '$rel', but ${script.toUri} does not exist")
      Os.exit(-1)
    }
    r = r :+ script
  }
  return r
}

// mm:ss, right-aligned so the summary column scans cleanly
@pure def fmtDuration(ms: Z): String = {
  val total = ms / 1000
  val m = total / 60
  val sec = total % 60
  return if (sec < 10) s"${m}m0${sec}s" else s"${m}m${sec}s"
}

def padLeft(s: String, w: Z): String = {
  var r = s
  while (r.size < w) {
    r = s" $r"
  }
  return r
}

// The model directory a ci.cmd belongs to. Every model in the repo uses <model>/.ci/ci.cmd, but
// the walk below matches any file named ci.cmd, so fall back to the parent rather than
// misreporting the grandparent for one that is not under a .ci directory.
@pure def modelDirOf(script: Os.Path): Os.Path = {
  return if (script.up.name == ".ci") script.up.up else script.up
}

var scripts = ISZ[Os.Path]()

def collectCIs(p: Os.Path): Unit = {
  if (!excludes.contains(p)) {
    if(p.isFile && p.name == "ci.cmd") {
      scripts = scripts :+ p
    } else if(p.isDir) {
      p.list.foreach((m: Os.Path) => collectCIs(m))
    }
  }
}

Os.env("HAMR_CI_MODELS") match {
  case Some(v) if ops.StringOps(v).trim != "" =>
    scripts = resolveModels(v, "HAMR_CI_MODELS")
  case _ =>
    collectCIs(rootDir)
    // The catch-all shard names what the other shards cover rather than listing its own models,
    // so a model added to the repo lands here automatically instead of going untested.
    Os.env("HAMR_CI_SKIP_MODELS") match {
      case Some(v) if ops.StringOps(v).trim != "" =>
        val skip = resolveModels(v, "HAMR_CI_SKIP_MODELS")
        scripts = for (s <- scripts if !ops.ISZOps(skip).contains(s)) yield s
      case _ =>
    }
}

println(s"Running ${scripts.size} model(s):")
for (script <- scripts) {
  println(s"  ${rootDir.relativize(modelDirOf(script))}")
}

if (ops.ISZOps(Os.cliArgs).contains("list-models")) {
  Os.exit(0)
}

var result: Z = 0
var timings = ISZ[(Z, String, Z)]()

for (script <- scripts) {
  val model = rootDir.relativize(modelDirOf(script)).string
  val start = extension.Time.currentMillis
  // Built as a sequence rather than interpolated into proc"...": the options reach each model
  // script as separate arguments, so one containing a space cannot split into two.
  val cmd = ISZ[String](sireum.string, "slang", "run", script.string) ++ Os.cliArgs
  val r = Os.proc(cmd).console.echo.run()
  val elapsed = extension.Time.currentMillis - start
  timings = timings :+ ((elapsed, model, r.exitCode))
  println(s"--- $model: ${fmtDuration(elapsed)} (exit ${r.exitCode})")
  // Any non-zero exit is a failure. Summing exit codes, as this did previously, can wrap to a
  // multiple of 256 and report success.
  if (r.exitCode != 0) {
    result = 1
  }
}

// Longest first: this is the input for rebalancing the CI shards, so it is worth having in the
// log rather than reconstructing it from timestamps after the fact.
var total: Z = 0
for (t <- timings) {
  total = total + t._1
}
println()
println(s"Summary -- ${timings.size} model(s), ${fmtDuration(total)} total:")
for (t <- ops.ISZOps(timings).sortWith((a: (Z, String, Z), b: (Z, String, Z)) => a._1 > b._1)) {
  val mark: String = if (t._3 == 0) "" else s"  <-- FAILED (exit ${t._3})"
  println(s"  ${padLeft(fmtDuration(t._1), 8)}  ${t._2}$mark")
}

Os.exit(result)
