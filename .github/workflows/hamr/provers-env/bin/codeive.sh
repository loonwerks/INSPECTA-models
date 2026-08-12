#!/bin/bash -e
# Install CodeIVE, the VSCodium-based IDE, into ${SIREUM_HOME}.
# Requires sireum.sh to have been run first.
#
# 'sireum setup vscode' also runs Init.deps(), which installs the shared extras
# -- Logika's solvers (Z3, CVC), jacoco, checkstack and the helper scripts -- so
# it overlaps with what ive.sh does.
#
# Launch it with the 'codium' shell function; see bin/functions.sh.
set -Eeuxo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/env.sh"

if [ ! -x "${SIREUM_HOME}/bin/sireum" ]; then
  echo "ERROR: ${SIREUM_HOME}/bin/sireum not found -- run sireum.sh first" >&2
  exit 1
fi

"${SIREUM_HOME}/bin/sireum" setup vscode

# CodeIVE is Electron, and Electron refuses to start unless its SUID sandbox
# helper is owned by root with mode 4755:
#
#   FATAL: The SUID sandbox helper binary was found, but is not configured
#   correctly.  Rather than run without sandboxing I'm aborting now.
#
# A distro package would set this; unpacking a tarball leaves the helper owned
# by the installing user, so fix it here.  Note the failure is invisible in
# normal use -- the 'codium' launcher backgrounds the process and discards its
# output, so the app just never appears.
# bin/linux on x86_64, bin/linux/arm on aarch64 -- fix whichever is present.
for _sandbox in "${SIREUM_HOME}"/bin/linux/vscodium/chrome-sandbox \
                "${SIREUM_HOME}"/bin/linux/arm/vscodium/chrome-sandbox; do
  if [ -f "${_sandbox}" ]; then
    sudo chown root:root "${_sandbox}"
    sudo chmod 4755 "${_sandbox}"
    ls -l "${_sandbox}"
  fi
done
unset _sandbox
