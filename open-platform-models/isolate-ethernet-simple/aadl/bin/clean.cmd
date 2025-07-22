::#! 2> /dev/null                                   #
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
::!#
// #Sireum

import org.sireum._

val homeDir = Os.slashDir.up.up

val hamrDir = homeDir / "hamr"

@sig trait Keep {
  @pure def keep(f: Os.Path): B
}
@datatype class KeepPath (path: Os.Path) extends Keep {
  @pure def keep(f: Os.Path): B = {
    return f == path
  }
}
@datatype class KeepPattern (pattern: String) extends Keep {
  @pure def keep(f: Os.Path): B = {
    return ops.StringOps(f.value).contains(pattern)
  }
}

val toKeep = ISZ(
  KeepPattern("component/base"), // slang user implementation files
  KeepPattern("test/bridge"), // slang user test files
  KeepPattern("test/system"), // slang user test files

  KeepPattern("_user.c"), // microkit C user implementation file

  KeepPattern("_app.rs"), // microkit Rust user implementation files
  KeepPattern("tests.rs"),
)

@pure def keep(f: Os.Path): B = {
  for (p <- toKeep if p.keep(f)) {
    return T
  }
  return F
}

def rec(p: Os.Path, onlyDelAutoGen: B): Unit = {
  if(p.isFile) {
    if ((!keep(p) && !onlyDelAutoGen) || ops.StringOps(p.read).contains("Do not edit")) {
      p.remove()
      println(s"Removed file: $p")
    }
  } else {
    for (pp <- p.list) {
      rec(pp, keep(p) || onlyDelAutoGen)
    }
    if (p.list.isEmpty) {
      p.removeAll()
      println(s"Removed empty directory: $p")
    }
  }
}

if (Os.cliArgs.nonEmpty) {
  for (a <- Os.cliArgs) {
    val d = Os.slashDir / a
    assert (d.exists, s"$d is not a valid directory")
    rec(d, F)
  }
} else {
  rec(hamrDir, F)
}