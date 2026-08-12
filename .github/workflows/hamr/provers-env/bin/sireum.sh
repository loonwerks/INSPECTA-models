#!/bin/bash -e
# Install Sireum (kekinian) into ${SIREUM_HOME}.
#
# This installs Sireum itself and nothing else.  The IDEs are separate, optional
# installs: ive.sh (IntelliJ-based IVE), codeive.sh (VSCodium-based CodeIVE) and
# fmide.sh (Eclipse-based FMIDE).
#
# PROVERS_SIREUM_PROFILE selects how:
#
#   full     clone kekinian at SIREUM_V and build it  (default)
#   minimal  fetch the prebuilt distribution for that revision, which is enough
#            to run Slang (.cmd) scripts and HAMR codegen but cannot rebuild
#            Sireum itself -- what the container wants
#
# The two are interchangeable after the fact: running this with 'full' over a
# minimal install replaces it with a source build, which is how a container gets
# a Sireum newer than the image it was published in.  See docker/readme.md.
#
# The full build invokes 'bin/build.cmd jar'.  Not 'setup', which always builds
# the IVE; and not the no-argument form, which ends in project() -- that runs
# 'sireum proyek ive --force', and generating an IntelliJ project for the
# kekinian checkout downloads and installs IDEA itself, 3GB of it.  Read against
# build.cmd, 'jar' is the no-argument path minus that step: neither form calls
# init.deps(), so nothing else is lost.  An IDE, when wanted, is installed by
# ive.sh / codeive.sh, which is what those scripts are for.
set -Eeuxo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/env.sh"

# SIREUM_REPO, SIREUM_V (a branch, tag or commit SHA) and SIREUM_INIT_V come
# from versions.sh, pinned there with the rest of the tool versions.  Override
# either on the command line to build a different revision -- see
# ../vagrant/readme.md.

: "${PROVERS_SIREUM_PROFILE:=full}"

if [ "${PROVERS_SIREUM_PROFILE}" = "minimal" ]; then
  # bin/init.sh downloads the launcher, versions.properties and the prebuilt
  # sireum.jar; SIREUM_NO_SETUP keeps it from going on to build a distro.
  mkdir -p "${SIREUM_HOME}/bin"
  # shellcheck disable=SC2086
  wget ${PROVERS_WGET_OPTS} -O "${SIREUM_HOME}/versions.properties" \
    "https://raw.githubusercontent.com/sireum/kekinian/${SIREUM_V}/versions.properties"
  # shellcheck disable=SC2086
  wget ${PROVERS_WGET_OPTS} -O "${SIREUM_HOME}/bin/init.sh" \
    "https://raw.githubusercontent.com/sireum/kekinian/${SIREUM_V}/bin/init.sh"
  chmod 700 "${SIREUM_HOME}/bin/init.sh"
  SIREUM_NO_SETUP=true "${SIREUM_HOME}/bin/init.sh"
  "${SIREUM_HOME}/bin/sireum" --init
  set +x
  echo ""
  echo "Sireum ${SIREUM_V} (minimal) is installed at ${SIREUM_HOME}"
  exit 0
fi

# A source build needs to unpack what Sireum's bootstrap downloads.  Checked here
# because the failure otherwise surfaces from inside init.sh, several steps in.
if ! command -v unzip > /dev/null 2>&1 && ! command -v 7z > /dev/null 2>&1; then
  echo "sireum.sh: a full install needs unzip (or 7z); install it with" >&2
  echo "           sudo apt-get install -y unzip" >&2
  exit 1
fi

if [ ! -d "${SIREUM_HOME}/.git" ]; then
  if [ -e "${SIREUM_HOME}" ]; then
    # e.g. upgrading a container's minimal install, which is not a git checkout
    echo "Replacing the existing non-git ${SIREUM_HOME} with a source build"
  fi
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

# Sireum's own bin/init.sh fetches the bootstrap jar with curl, and gives up
# after a handful of attempts -- a poor bet for a 100MB+ download, which is where
# two emulated amd64 container builds died with "curl: (56) Connection died".
# init.sh skips the download when the jar is already there, so fetch it here
# instead, with the retry budget the rest of our downloads use.  Written to a
# temporary name so an interrupted transfer cannot leave a truncated jar behind.
if [ -n "${SIREUM_INIT_V}" ] && [ ! -f "${SIREUM_HOME}/bin/sireum.jar" ]; then
  echo "Fetching the ${SIREUM_INIT_V} bootstrap jar"
  provers_fetch "${SIREUM_REPO}/releases/download/${SIREUM_INIT_V}/sireum.jar" \
    "${SIREUM_HOME}/bin/sireum.jar" "sireum-${SIREUM_INIT_V}.jar"
  chmod +x "${SIREUM_HOME}/bin/sireum.jar"
fi

echo "Building Sireum (this takes a while)"
"${SIREUM_HOME}/bin/build.cmd" jar

# 'sireum setup ive' and 'sireum setup vscode' both run Init.deps() -- which is
# what installs Logika's solvers (Z3, CVC) among other things -- so with either
# IDE selected the solvers arrive with it.  With both declined nothing installs
# them, so pull them in directly.
#
# PROVERS_SIREUM_SOLVERS=false skips that.  Sireum downloads a solver on first
# use when it is missing, so Logika still works given network access; what the
# install buys is working offline.  The container sets it false: it is a CI
# image where size is what costs, and the installers fetch every platform's
# build -- macOS, Windows and both Linux architectures, ~825MB -- of which only
# one is usable.
if [ "${PROVERS_SIREUM_SOLVERS:-true}" = "true" ] &&
   [ "${PROVERS_IVE}" != "true" ] && [ "${PROVERS_CODEIVE}" != "true" ]; then
  echo "No IDE selected; installing Logika's solvers on their own"
  "${SIREUM_HOME}/bin/build.cmd" z3
  "${SIREUM_HOME}/bin/build.cmd" cvc
fi

set +x
echo ""
echo "Sireum ${SIREUM_V} is installed at ${SIREUM_HOME}"
