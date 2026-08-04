#!/bin/bash -e
# Install Sireum (kekinian) into ${SIREUM_HOME}.
#
# Unlike the Dockerfile, which installs a stripped-down Sireum just big enough
# to run Slash (.cmd) scripts, this is a full install:
#
#   bin/build.cmd setup   builds Sireum and installs the IntelliJ-based IVE
#   sireum setup vscode   installs the VSCodium-based CodeIVE
#
# Nothing is removed afterwards.  FMIDE is installed separately by fmide.sh.
set -Eeuxo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/env.sh"

: "${SIREUM_REPO:=https://github.com/sireum/kekinian}"
# a branch, tag or commit SHA of the above; leave as master for the latest
: "${SIREUM_V:=master}"

if [ ! -d "${SIREUM_HOME}/.git" ]; then
  rm -rf "${SIREUM_HOME}"
  mkdir -p "$(dirname "${SIREUM_HOME}")"
  git clone --rec "${SIREUM_REPO}" "${SIREUM_HOME}"
fi

cd "${SIREUM_HOME}"
git fetch --all
git checkout "${SIREUM_V}"
# fast-forward when SIREUM_V names a branch (a tag/SHA leaves HEAD detached)
if git symbolic-ref -q HEAD > /dev/null; then
  git pull --ff-only
fi
git submodule update --init --recursive

echo "Building Sireum and installing the IVE (this takes a while)"
"${SIREUM_HOME}/bin/build.cmd" setup

echo "Installing CodeIVE (VSCodium)"
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
