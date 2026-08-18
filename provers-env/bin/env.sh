# Environment for the DARPA PROVERS development environment.
#
# This mirrors the ENV settings of the final stage of
#   provers-env/docker/Dockerfile.provers
#
# It is sourced by each of the install scripts in this directory and, after
# provisioning, by the shell startup file.  It is meant to be sourced, not
# executed.
#
# PROVERS_BIN_DIR names this directory.  The install scripts leave it unset and
# it is derived from BASH_SOURCE, but the startup file sets it explicitly: on
# macOS the login shell is zsh, which sources this file happily enough but does
# not populate BASH_SOURCE.  Setting it is what keeps one env.sh correct under
# both shells.
if [ -n "${PROVERS_BIN_DIR:-}" ]; then
  _PROVERS_BIN_DIR="${PROVERS_BIN_DIR}"
else
  _PROVERS_BIN_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
fi
# shellcheck source=versions.sh
source "${_PROVERS_BIN_DIR}/versions.sh"

: "${PROVERS_DIR:=$HOME/provers}"

# Which IDEs the setup installs.  All three are optional, independent, and OFF
# unless asked for -- the command-line tools work without any of them, and each
# is a large download that a machine already carrying its own IDE has no use
# for.  Ask for one with PROVERS_IVE=true, e.g.
#
#   PROVERS_IVE=true PROVERS_CODEIVE=true bash provers-setup.sh
#
# vagrant/Vagrantfile turns all three on, since a desktop VM with none of them
# is not what that image is for; docker/Dockerfile.provers leaves them off.
: "${PROVERS_IVE:=false}"
: "${PROVERS_CODEIVE:=false}"
: "${PROVERS_FMIDE:=false}"

LIONSOS=${PROVERS_DIR}/lionsos
MICROKIT_BOARD=${MICROKIT_BOARD:-qemu_virt_aarch64}
# One SDK: the released Microkit ${MICROKIT_SDK_VER}, with its `microkit` tool
# rebuilt to carry the vCPU domain fix (see microkit-vcpu-domain.sh).
MICROKIT_SDK=${PROVERS_DIR}/microkit-sdk-${MICROKIT_SDK_VER}
# An existing Sireum is adopted rather than replaced: export SIREUM_HOME before
# running the setup and bin/sireum.sh leaves that install alone (it reports the
# version and stops), while everything else here -- PATH, the IDE installers,
# build-info -- refers to it.  This matters most on a Mac, which is likely to be
# a development machine with Sireum already on it rather than a dedicated box.
SIREUM_HOME=${SIREUM_HOME:-${PROVERS_DIR}/Sireum}
VERUS_DIR=${PROVERS_DIR}/verus
# Verus looks for z3 next to its own binary, which is where the released builds
# put it.  Where Z3 is built from source it lands in ${Z3_DIR} instead and
# verus.sh symlinks it into place, so this one path is correct on every host.
# Not exported: verus finds that z3 on its own, so nothing outside these scripts
# needs to be told where it is.
Z3_DIR=${PROVERS_DIR}/z3
VERUS_Z3_PATH=${VERUS_DIR}/z3
SDFGEN_VENV=${PROVERS_DIR}/sdfgen-venv

# Shell tracing.  The install scripts used to run under 'set -x' unconditionally,
# which buries the handful of lines that actually say what is happening under a
# '+ ...' echo of every test and assignment.  It is genuinely useful when a step
# fails in an unattended build, so it is kept -- behind a switch:
#
#   PROVERS_TRACE=true bash provers-setup.sh
#
# Set here rather than in each script so there is one answer.  'source' runs in
# the calling shell, so this applies to whichever script sourced this file.  The
# interactive guard matters because the shell startup file sources this too, and
# tracing an interactive shell would be unusable.
# (an explicit 'if' rather than '[ ... ] && set -x': a trailing && list that
# fails would make this compound command return non-zero, which the caller's
# 'set -e' takes as a reason to abort)
if [ "${PROVERS_TRACE:-false}" = "true" ]; then
  case "$-" in
    *i*) ;;
    *) set -x ;;
  esac
fi

# Release downloads go through this rather than a bare wget.  A multi-hour build
# that dies an hour in because GitHub answered one request with 503 is expensive,
# and unauthenticated release downloads do get throttled -- which is exactly what
# a 503 or 429 here means.  wget does not retry those by default.
# Verbosity is chosen from where the output is going: a person watching a
# terminal gets a progress bar, anything else gets nothing.  provers_fetch
# already names each artifact as it starts, which is the useful half; wget's own
# line is a redrawn progress bar on a terminal and, redirected, the full HTTP
# headers or -- with -nv -- a hundred-character signed release URL.  A failure
# still stops the script, since these run under 'set -e'.  Setting
# PROVERS_WGET_OPTS yourself replaces both this and the retry budget.
if [ -t 1 ]; then
  _PROVERS_WGET_V="-q --show-progress"
else
  _PROVERS_WGET_V="-q"
fi
PROVERS_WGET_OPTS="${PROVERS_WGET_OPTS:-${_PROVERS_WGET_V} --tries=8 --waitretry=15 --retry-connrefused --retry-on-http-error=408,429,500,502,503,504}"
unset _PROVERS_WGET_V

# Where provers_fetch keeps downloaded archives.  Empty disables caching, which
# is the default: a one-off install has nothing to reuse.  A container build sets
# it to a BuildKit cache mount, so that rebuilding does not re-download hundreds
# of megabytes from GitHub -- which is what gets a build throttled.
: "${PROVERS_CACHE_DIR:=}"

# provers_fetch <url> <dest> [cache-key]
#
# Download to <dest>, reusing PROVERS_CACHE_DIR when one is configured.  The
# cache key defaults to <dest>'s basename, which is fine where the name carries
# the version; pass one explicitly where it does not (sireum.jar, say).
# Downloads land on a .part name first, so an interrupted transfer cannot be
# mistaken for a complete file on the next run.
provers_fetch() {
  local url=$1 dest=$2 key=${3:-}
  [ -n "${key}" ] || key=${dest##*/}
  local cached=""

  if [ -n "${PROVERS_CACHE_DIR}" ]; then
    mkdir -p "${PROVERS_CACHE_DIR}"
    cached="${PROVERS_CACHE_DIR}/${key}"
    if [ -s "${cached}" ]; then
      echo "provers_fetch: reusing cached ${key}"
      cp "${cached}" "${dest}"
      return 0
    fi
  fi

  echo "provers_fetch: downloading ${key}"
  # shellcheck disable=SC2086
  wget ${PROVERS_WGET_OPTS} -O "${dest}.part" "${url}"
  mv "${dest}.part" "${dest}"
  if [ -n "${cached}" ]; then
    cp "${dest}" "${cached}" || true
  fi
}

# Where Sireum puts this host's binaries and IDEs: bin/linux on x86_64 Linux,
# bin/linux/arm on aarch64 Linux, bin/mac on macOS.  Defined here so that
# functions.sh and slim.sh work from one answer rather than each deriving it.
if [ "${PROVERS_OS}" = "darwin" ]; then
  SIREUM_PLATFORM_BIN=${SIREUM_HOME}/bin/mac
elif [ "${PROVERS_ARCH}" = "aarch64" ]; then
  SIREUM_PLATFORM_BIN=${SIREUM_HOME}/bin/linux/arm
else
  SIREUM_PLATFORM_BIN=${SIREUM_HOME}/bin/linux
fi

# provers_nproc -- how many cores to build with, on either host.
provers_nproc() {
  if command -v nproc > /dev/null 2>&1; then
    nproc
  elif command -v sysctl > /dev/null 2>&1; then
    sysctl -n hw.ncpu
  else
    echo 4
  fi
}

# On macOS the compiler and binutils the generated Microkit makefiles name --
# clang, ld.lld, llvm-ar, llvm-ranlib, llvm-objcopy -- come from Homebrew, and
# its llvm formula is keg-only, so its bin directory has to be named explicitly.
# ${HOMEBREW_PREFIX}/opt/make/libexec/gnubin supplies GNU make 4.x as `make`;
# macOS itself ships 3.81, which the sDDF makefiles are not written for.  Both
# are Homebrew's own directories -- see bin/deps.sh for what it installs.
_PROVERS_OS_PATH=""
if [ "${PROVERS_OS}" = "darwin" ]; then
  if [ -z "${HOMEBREW_PREFIX:-}" ]; then
    if [ -x /opt/homebrew/bin/brew ]; then
      HOMEBREW_PREFIX=/opt/homebrew
    elif [ -x /usr/local/bin/brew ]; then
      HOMEBREW_PREFIX=/usr/local
    fi
  fi
  if [ -n "${HOMEBREW_PREFIX:-}" ]; then
    export HOMEBREW_PREFIX
    _PROVERS_OS_PATH="${HOMEBREW_PREFIX}/opt/make/libexec/gnubin:${HOMEBREW_PREFIX}/opt/llvm/bin:${HOMEBREW_PREFIX}/bin:"
  fi
fi

# ---------------------------------------------------------------------------
# The public environment.
#
# Everything above is internal: the install scripts each source this file, so
# they see those values as ordinary shell variables and nothing has to be pushed
# into the environment of every command run afterwards.  What is exported here is
# the contract a *build* relies on, and only that -- four variables and PATH,
# rather than the forty this file used to leave behind:
#
#   SIREUM_HOME     the Sireum install; its launcher and HAMR codegen read it
#   MICROKIT_SDK    generated Makefiles fail with an explicit error without it
#   MICROKIT_BOARD  likewise
#   LIONSOS         the models' system.mk errors out without it, and derives
#                   SDDF and LIBVMM from it
#
# Adding to this list means adding to what every user of this environment is
# entitled to depend on, so add deliberately.
# ---------------------------------------------------------------------------
export SIREUM_HOME MICROKIT_SDK MICROKIT_BOARD LIONSOS

# Prepend the tool directories only once so that repeated sourcing (e.g. nested
# shells) does not keep growing PATH.
case ":${PATH}:" in
  *":${VERUS_DIR}:"*) ;;
  *) export PATH="${_PROVERS_OS_PATH}${VERUS_DIR}:${SIREUM_HOME}/bin:${SDFGEN_VENV}/bin:${HOME}/.cargo/bin:${PATH}" ;;
esac

unset _PROVERS_BIN_DIR _PROVERS_OS_PATH
