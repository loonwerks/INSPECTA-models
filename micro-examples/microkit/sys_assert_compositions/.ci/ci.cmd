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

// Minimal CI driver for the multi-composition sys-assert micro-example.
// Present so the model is discovered by MicrokitTests (getAadlModels requires a
// sibling `.ci` directory). HAMR codegen + Verus checks would be wired here when
// this model is upstreamed to INSPECTA-models.
println("sys_assert_compositions: ci.cmd placeholder")
