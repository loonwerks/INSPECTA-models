#!/bin/bash -e
# Install Sireum (kekinian) into ${SIREUM_HOME}.
#
# This installs Sireum itself and nothing else.  The IDEs are separate, optional
# installs: ive.sh (IntelliJ-based IVE), codeive.sh (VSCodium-based CodeIVE) and
# fmide.sh (Eclipse-based FMIDE).
#
# PROVERS_SIREUM_PROFILE selects how:
#
#   full     a complete, usable Sireum  (default).  Where SIREUM_V names a
#            release this unpacks that release's `cli` distribution, which takes
#            a couple of minutes; where it names a commit there is nothing to
#            unpack, so kekinian is cloned at that commit and built, which takes
#            the better part of an hour.  See "release or source" below.
#   minimal  fetch the prebuilt sireum.jar for that revision, which is enough
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
set -Eeuo pipefail

# Captured before env.sh runs, which defaults it to ${PROVERS_DIR}/Sireum.  A
# SIREUM_HOME set by the caller means "this machine already has Sireum" -- see
# below.
_given_sireum_home="${SIREUM_HOME-}"

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/env.sh"

# An existing Sireum is adopted, not replaced.  A Mac running this setup is
# typically a development machine with a Sireum checkout already on it, and
# rebuilding it -- or worse, checking it out to the pinned SIREUM_V and
# discarding whatever the user was working on -- is not what installing an
# environment should do.  So if the caller pointed SIREUM_HOME at a Sireum of
# their own, that is the Sireum this environment uses: env.sh keeps SIREUM_HOME
# pointing at it, so the IDE installers and PATH follow.
#
# Either of two files says one is there:
#
#   bin/sireum.jar   a working install -- an unpacked release distribution, or a
#                    source build that has been built
#   bin/build.cmd    a kekinian checkout, built or not (a release distribution
#                    ships no build.cmd, which is why it takes both tests)
#
# The jar on its own is too narrow.  It is a build product, and kekinian's
# .gitignore covers the whole of bin/ bar the tracked scripts, so a plain
# 'git clone' -- the most ordinary thing to find on a developer machine -- has
# no jar and fails that test.  The question worth asking is not "has this been
# built" but "did the caller name a Sireum of their own", and the cost of
# answering it wrong is not a wasted download: the fall-through path runs
# 'git checkout ${SIREUM_V}' in that very directory, which is the outcome this
# guard exists to prevent.  So where the two disagree, decline to touch it.
#
# To build the pinned revision anyway, run this with SIREUM_HOME unset (or
# pointing somewhere else) and it installs into ${PROVERS_DIR}/Sireum as usual.
if [ -n "${_given_sireum_home}" ] &&
   { [ -f "${_given_sireum_home}/bin/sireum.jar" ] ||
     [ -f "${_given_sireum_home}/bin/build.cmd" ]; }; then
  set +x
  echo ""
  echo "Found an existing Sireum at ${_given_sireum_home}"
  echo "(SIREUM_HOME was set and points at one, so it is left as it is;"
  echo " nothing is checked out, built or overwritten.)"
  echo ""
  if [ ! -f "${_given_sireum_home}/bin/sireum.jar" ]; then
    # A checkout that has never been bootstrapped.  Report that rather than
    # running bin/sireum to ask its version: that launcher bootstraps itself on
    # first use, downloading the jar and unpacking a JDK, Scala and 7zz into
    # bin/ -- a good deal more than reporting a version, and not this script's
    # call to make on a tree it was just told to leave alone.
    echo "  not bootstrapped yet: there is no bin/sireum.jar under it"
    echo "  build it with '${_given_sireum_home}/bin/build.cmd jar'"
  elif [ -x "${_given_sireum_home}/bin/sireum" ]; then
    # Captured whole and trimmed to its first line rather than piped into head:
    # 'sireum --version' goes on to print its full dependency version map, and a
    # pipe that closes early would hand it a SIGPIPE, which pipefail would then
    # turn into a failure of this script.
    _sireum_version="$("${_given_sireum_home}/bin/sireum" --version 2>&1 || true)"
    echo "  ${_sireum_version%%$'\n'*}"
  else
    echo "  version unavailable: ${_given_sireum_home}/bin/sireum is not executable"
  fi
  exit 0
fi

# TODO: the guard above cannot tell a SIREUM_HOME the caller deliberately named
# from one that is merely in the ambient environment, and the second case has a
# use that it currently defeats.  docker/readme.md documents upgrading a running
# container with 'SIREUM_V=master bash bin/sireum.sh', but the final image stage
# sets SIREUM_HOME itself (docker/Dockerfile.provers, ENV SIREUM_HOME=...) and
# the image's Sireum has a bin/sireum.jar -- so inside the container the guard
# adopts and exits 0, and the upgrade never runs.  Image *builds* are unaffected:
# that ENV belongs to the final stage, and bin/sireum.sh runs in the builder,
# where SIREUM_HOME is unset.
#
# The fix wants an explicit way to say "install into SIREUM_HOME, do not adopt
# it" -- PROVERS_SIREUM_ADOPT=false, say, tested at the top of the guard, with
# docker/readme.md passing it.  Left undone deliberately: it adds a knob to a
# public surface this project keeps small (see the export list in env.sh), and
# that is a call worth making on purpose rather than in passing.

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

# --- release or source ---
#
# SIREUM_V may name a release (4.20260810.80aad0c2, or the rolling `dev`), or a
# commit or branch (e8f69b3d..., master).  They are told apart by asking whether
# a release of that name carries an install.cmd: every release publishes one,
# and a commit or branch has no release at all, so the URL 404s.  That is a
# cheap, exact test -- better than pattern-matching the string, which would have
# to guess at what a release tag looks like, and would get `dev` wrong.
#
# It classifies correctly at the edges: `dev` is a real release (200) and is
# unpacked, while `master` is a branch with no release (404) and is cloned and
# built, which is what you want of a branch.
#
# A release is installed by running its own install.cmd with DISTRO=cli: the
# command-line distribution, ~70MB, against ~1.9GB for the one bundling the IVE
# (which this environment installs separately, and only when asked -- see
# ive.sh).  DIR points it at ${SIREUM_HOME} rather than its default of
# ~/Applications/Sireum.
#
# PROVERS_SIREUM_FROM_SOURCE overrides the choice:
#   auto   decide by the test above  (default)
#   true   clone and build, even where SIREUM_V names a release
#   false  require a release, and fail rather than fall back to a source build
#
# Note that `dev` is a *moving* release: its assets are re-uploaded as kekinian
# advances, so two setups run weeks apart install different Sireums from it.
# That is the opposite of what everything else in versions.sh is pinned for, so
# use it to track the tip deliberately, not as a default.
: "${PROVERS_SIREUM_FROM_SOURCE:=auto}"

SIREUM_INSTALL_CMD_URL="${SIREUM_REPO}/releases/download/${SIREUM_V}/install.cmd"

# 'sireum setup ive' and 'sireum setup vscode' both run Sireum's Init.deps() --
# which is what installs Logika's solvers (Z3, CVC) among other extras -- so with
# either IDE selected the solvers arrive with it.  With both declined, which is
# the default off a VM, nothing else would install them, so do it here.
#
# PROVERS_SIREUM_SOLVERS=false skips it.  Sireum downloads a solver on first use
# when it is missing, so Logika still works given network access; what the
# install buys is working offline.  The container sets it false: it is a CI
# image where size is what costs, and the installers fetch every platform's
# build -- macOS, Windows and both Linux architectures, ~825MB -- of which only
# one is usable.
sireum_install_solvers() {
  if [ "${PROVERS_SIREUM_SOLVERS:-true}" != "true" ]; then
    return 0
  fi
  if [ "${PROVERS_IVE}" = "true" ] || [ "${PROVERS_CODEIVE}" = "true" ]; then
    return 0
  fi
  echo "No IDE selected; installing Logika's solvers on their own"
  if [ -f "${SIREUM_HOME}/bin/build.cmd" ]; then
    # a source build: these two targets fetch exactly the two solvers
    "${SIREUM_HOME}/bin/build.cmd" z3
    "${SIREUM_HOME}/bin/build.cmd" cvc
  else
    # A release distribution ships no build.cmd.  '--init' is its equivalent: it
    # runs Init.deps(), whose logikaDeps() is installZ3() + installCVC().  It
    # does fetch the rest of the dependency set with them, which is more than
    # asked for but is the only route a distribution offers.
    "${SIREUM_HOME}/bin/sireum" --init
  fi
}

sireum_release_exists() {
  # -f so a 404 is a failure, -I to fetch headers only, -L to follow GitHub's
  # redirect to the asset host.  A network problem also lands here, and the
  # answer is then "not a release", so say which was checked when falling back.
  curl -fsIL --max-time 30 -o /dev/null "${SIREUM_INSTALL_CMD_URL}"
}

case "${PROVERS_SIREUM_FROM_SOURCE}" in
  auto)
    if sireum_release_exists; then
      _sireum_source=false
    else
      echo "sireum.sh: no release install.cmd at ${SIREUM_INSTALL_CMD_URL};"
      echo "           treating SIREUM_V=${SIREUM_V} as a commit and building from source."
      _sireum_source=true
    fi
    ;;
  true)  _sireum_source=true ;;
  false)
    if ! sireum_release_exists; then
      echo "sireum.sh: PROVERS_SIREUM_FROM_SOURCE=false, but SIREUM_V=${SIREUM_V} has no" >&2
      echo "           release at ${SIREUM_INSTALL_CMD_URL}" >&2
      exit 1
    fi
    _sireum_source=false
    ;;
  *)
    echo "sireum.sh: PROVERS_SIREUM_FROM_SOURCE must be auto, true or false" >&2
    exit 1
    ;;
esac

if [ "${_sireum_source}" != "true" ]; then
  # install.cmd extracts the tarball -- whose top-level directory is Sireum --
  # into $(dirname "${DIR}"), and then reports DIR as the install.  The two only
  # agree when DIR itself ends in /Sireum, so refuse rather than scatter a
  # distribution next to where SIREUM_HOME says it should be.
  case "${SIREUM_HOME}" in
    */Sireum) ;;
    *)
      echo "sireum.sh: a release install needs SIREUM_HOME to end in /Sireum," >&2
      echo "           but it is '${SIREUM_HOME}'.  Unset SIREUM_HOME to use the" >&2
      echo "           default (\${PROVERS_DIR}/Sireum), or build from source with" >&2
      echo "           PROVERS_SIREUM_FROM_SOURCE=true." >&2
      exit 1
      ;;
  esac

  echo "Installing Sireum ${SIREUM_V} (cli distribution) into ${SIREUM_HOME}"
  mkdir -p "${PROVERS_DIR}"
  provers_fetch "${SIREUM_INSTALL_CMD_URL}" "${PROVERS_DIR}/sireum-install.cmd" \
    "sireum-install-${SIREUM_V}.cmd"

  # install.cmd removes DIR before unpacking, so a re-run replaces the install
  # rather than merging into it.  It also unpacks org.sireum.m2.zip (~253MB) into
  # ${HOME} -- the Maven repository the Sireum build resolves against -- which is
  # its own doing and lands outside ${PROVERS_DIR}.
  SIREUM_V="${SIREUM_V}" DISTRO=cli DIR="${SIREUM_HOME}" \
    bash "${PROVERS_DIR}/sireum-install.cmd"
  rm -f "${PROVERS_DIR}/sireum-install.cmd"

  "${SIREUM_HOME}/bin/sireum" --version > /dev/null

  sireum_install_solvers

  echo ""
  echo "Sireum ${SIREUM_V} is installed at ${SIREUM_HOME}"
  exit 0
fi

# --- built from source (SIREUM_V names a commit) ---

# A source build needs to unpack what Sireum's bootstrap downloads.  Checked here
# because the failure otherwise surfaces from inside init.sh, several steps in.
if ! command -v unzip > /dev/null 2>&1 && ! command -v 7z > /dev/null 2>&1; then
  echo "sireum.sh: a full install needs unzip (or 7z); install it with" >&2
  if [ "${PROVERS_OS}" = "darwin" ]; then
    # macOS ships unzip, so getting here means PATH is unusual rather than that
    # anything is missing -- but say something actionable either way.
    echo "           brew install unzip" >&2
  else
    echo "           sudo apt-get install -y unzip" >&2
  fi
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
git -c advice.detachedHead=false checkout "${SIREUM_V}"
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

sireum_install_solvers

set +x
echo ""
echo "Sireum ${SIREUM_V} is installed at ${SIREUM_HOME}"
