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

val rootDir = Os.slashDir.up.up

val sireumBin = Os.path(Os.env("SIREUM_HOME").get) / "bin"
val sireum = sireumBin / (if(Os.isWin) "sireum.bat" else "sireum")

val excludes: ops.ISZOps[Os.Path] = ops.ISZOps(ISZ(
  rootDir / "micro-examples" / "case-transition-models"
))

var result: Z = 0
def findCIs(p: Os.Path): Unit = {
  if (!excludes.contains(p)) {
    if(p.isFile && p.name == "ci.cmd") {
      val r = proc"$sireum slang run $p".console.echo.run()
      result = result + r.exitCode
      result = result + Tasks.attestation(p.up.up)
    } else if(p.isDir) {
      p.list.foreach((m: Os.Path) => findCIs(m))
    }
  }
}
findCIs(rootDir)

Os.exit(result)



object Tasks {
  def run(title: String, verbose: B, proc: OsProto.Proc): Z = {
    println(s"$title ...")
    val r = (if (verbose) proc.console.echo else proc).run()

    if (r.exitCode != 1) { // TODO Test.sh script grep needs to be updated
      println(s"$title failed!")
      cprintln(F, r.out)
      cprintln(T, r.err)
    }
    return if (r.exitCode == 1) 0 else 1
  }

  def attestation(p: Os.Path): Z = {

    val attestationDir = p / "attestation"
    if (Os.env("MICROKIT_SDK").nonEmpty && Os.env("DEMO_ROOT").nonEmpty && attestationDir.exists) {
      println()
      val testsDir = Os.path(Os.env("DEMO_ROOT").get) / "am-cakeml" / "tests"
      
      val micro_composite = testsDir / "DemoFiles" / "goldenFiles"/ "micro_composite.txt"
      val cached_micro_composite = p / "attestation" / "micro_composite.txt"
      
      var aargs = ISZ[String]((testsDir / "CI" / "Test.sh").value, "-t", "micro", "-h",
        "-m", (attestationDir / "model_args.json").value, "-s", (attestationDir / "system_args.json").value)

      if (ops.ISZOps(Os.cliArgs).contains("provision")) {
        aargs = aargs :+ "-p"
        var result = run(s"Provisioning $p", F, Os.proc(aargs))
        if (result == 0) {
          micro_composite.copyOverTo(cached_micro_composite)
        }
        return result
      } else {
        var result = 1
        for(retry <- 0 until 5 if result != 0) {
          cached_micro_composite.copyOverTo(micro_composite)
          result = run(s"Appraising $p${if (retry > 0) s": Retry $retry" else ""}", F, Os.proc(aargs))
          assert (result == 0, result)
        
          val resp = testsDir / "DemoFiles" / "Generated" / "output_resp.json"        
          println(resp.read)
          result = if (resp.read == "Resolute Policy check:  SUCCESS") 0 else 1
        }
        println(s"Appraisal ${if (result == 0) "succeeded" else "failed"}!")
        return result
      }
    }
    return 0
  }
}
