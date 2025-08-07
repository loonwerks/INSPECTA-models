::/*#! 2> /dev/null                                   #
@ 2>/dev/null # 2>nul & echo off & goto BOF           #
if [ -z "${SIREUM_HOME}" ]; then                      #
  echo "Please set SIREUM_HOME env var"               #
  exit -1                                             #
fi                                                    #
exec "${SIREUM_HOME}/bin/sireum" slang run "$0" "$@"  #
:BOF
setlocal
if not defined SIREUM_HOME (
  echo Please set SIREUM_HOME env var
  exit /B -1
)
"%SIREUM_HOME%\bin\sireum.bat" slang run %0 %*
exit /B %errorlevel%
::!#*/
// #Sireum

import org.sireum._

@pure def exists(p: Os.Path): Unit = {
  if (!p.exists) {
    println(s"$p doesn't exists")
    Os.exit(1)
    halt("")
  }
}

val attestation_dir: Os.Path = Os.slashDir.canon
exists(attestation_dir)

val workspace_dir: Os.Path = (attestation_dir / "../../../aadl").canon
exists(workspace_dir)

val codegen_dir: Os.Path = attestation_dir.up.canon
exists(codegen_dir)

val env_AM_REPOS_ROOT: String = "AM_REPOS_ROOT"

val provision: B = ops.ISZOps(Os.cliArgs).contains("provision")
val appraise: B = ops.ISZOps(Os.cliArgs).contains("appraise")
val verbose: B = ops.ISZOps(Os.cliArgs).contains("verbose")

if (!(provision |^ appraise)) {
  println("Usage: (provision | appraise) <verbose>")
  Os.exit(0)
  halt("")
}

val AM_REPOS_ROOT: Os.Path = Os.env(env_AM_REPOS_ROOT) match {
  case Some(r) =>
    val d = Os.path(r)
    exists(d)
    d
  case _ =>
    println(s"$env_AM_REPOS_ROOT environment variable not set")
    Os.exit(1)
    halt("")
}

val rust_am_clients: Os.Path = AM_REPOS_ROOT / "rust-am-clients"

val cvm: Os.Path = AM_REPOS_ROOT / "cvm"/ "_build" / "install"/ "default" / "bin" / "cvm"
val RODEO_ENVS_DIR: Os.Path = AM_REPOS_ROOT / "rust-am-clients" / "rodeo_configs" / "rodeo_envs"

val env_roedeo_micro: Os.Path = RODEO_ENVS_DIR / "env_rodeo_micro.json"
val env_rodeo_micro_provision: Os.Path = RODEO_ENVS_DIR / "env_rodeo_micro_provision.json"

exists(rust_am_clients)
exists(cvm)
exists(env_roedeo_micro)
exists(env_rodeo_micro_provision)

val provisionFile: Os.Path = attestation_dir / "aadl_provision.json"
val appraiseFile: Os.Path = attestation_dir / "aadl_appraise.json"

exists (provisionFile)
exists (appraiseFile)

val cargo: ST = st"cargo run --release --bin rust-rodeo-client -- -c $cvm"

@pure def replace(r: String): String = {
  var op = ops.StringOps(r)
  op = ops.StringOps(op.replaceAllLiterally("%%workspace_dir%%", workspace_dir.value))
  op = ops.StringOps(op.replaceAllLiterally("%%attestation_dir%%", attestation_dir.value))
  return op.replaceAllLiterally("%%codegen_dir%%", codegen_dir.value)
}

if (provision) {
  val ptemp = Os.temp()
  ptemp.writeOver(replace(provisionFile.read))

  val a = st"$cargo -r $ptemp -e $env_rodeo_micro_provision"
  var p = proc"${a.render}".at(rust_am_clients)
  if (verbose) {
    p = p.console.echo
  }
  val results = p.run()
  exists(attestation_dir / "aadl_model_golden.txt")
  exists(attestation_dir / "aadl_codegen_golden.txt")

  println("Provisioning successful!")

} else {
  val atemp = Os.temp()
  atemp.writeOver(replace(appraiseFile.read))

  exists(attestation_dir / "aadl_model_golden.txt")
  exists(attestation_dir / "aadl_codegen_golden.txt")

  val a = st"$cargo -r $atemp -e $env_roedeo_micro"
  var p = proc"${a.render}".at(rust_am_clients)

  if (verbose) {
    p = p.console.echo
  }
  val results = p.run()

  if (results.ok) {
    val o = ops.StringOps(results.out)
    if (o.contains("\"RodeoClientResponse_success\":true")) {
      println("Appraisal successful!")
    } else {
      println("Appraisal failed")
      Os.exit(1)
    }
  } else {
    println("Appraisal failed")
    cprintln(T, results.err)
    Os.exit(results.exitCode)
  }
}