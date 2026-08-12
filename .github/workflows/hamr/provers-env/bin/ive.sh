#!/bin/bash -e
# Install the Sireum IVE, the IntelliJ-based IDE, into ${SIREUM_HOME}.
# Requires sireum.sh to have been run first.
#
# 'sireum setup ive' also runs Init.deps(), which installs the shared extras --
# Logika's solvers (Z3, CVC), jacoco, checkstack and the helper scripts.  It is
# therefore safe, if slow, to re-run, and it overlaps with what codeive.sh does.
#
# Launch it with the 'ive' shell function; see bin/functions.sh.
set -Eeuxo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/env.sh"

if [ ! -x "${SIREUM_HOME}/bin/sireum" ]; then
  echo "ERROR: ${SIREUM_HOME}/bin/sireum not found -- run sireum.sh first" >&2
  exit 1
fi

"${SIREUM_HOME}/bin/sireum" setup ive
