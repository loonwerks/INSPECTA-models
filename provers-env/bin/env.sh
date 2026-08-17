# Environment for the DARPA PROVERS development environment.
#
# This mirrors the ENV settings of the final stage of
#   provers-env/docker/Dockerfile.provers
#
# It is sourced by each of the install scripts in this directory and, after
# provisioning, by ~/.bashrc.  It is meant to be sourced, not executed.

_PROVERS_BIN_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
# shellcheck source=versions.sh
source "${_PROVERS_BIN_DIR}/versions.sh"

: "${PROVERS_DIR:=$HOME/provers}"

# Which IDEs the setup installs.  All three are optional and independent; the
# command-line tools work without any of them.  Set to anything other than
# 'true' to skip one -- e.g. PROVERS_IVE=false bash setup.sh
: "${PROVERS_IVE:=true}"
: "${PROVERS_CODEIVE:=true}"
: "${PROVERS_FMIDE:=true}"

export PROVERS_DIR PROVERS_IVE PROVERS_CODEIVE PROVERS_FMIDE
export LIONSOS=${PROVERS_DIR}/lionsos
export VMM_DIR=${LIONSOS}/dep/libvmm
export MICROKIT_BOARD=${MICROKIT_BOARD:-qemu_virt_aarch64}
export MICROKIT_SDK=${PROVERS_DIR}/microkit-sdk-${MICROKIT_DOMAINS_SDK_VER}
export MICROKIT_SDK_CURRENT=${PROVERS_DIR}/microkit-sdk-${MICROKIT_SDK_VER}
export SIREUM_HOME=${PROVERS_DIR}/Sireum
export VERUS_DIR=${PROVERS_DIR}/verus
# Verus looks for z3 next to its own binary, which is where the x86 release puts
# it.  Where Z3 is built from source it lands in ${Z3_DIR} instead and verus.sh
# symlinks it into place, so this one path is correct on every architecture --
# and the container can then declare it as a single ENV.
export Z3_DIR=${PROVERS_DIR}/z3
export VERUS_Z3_PATH=${VERUS_DIR}/z3
export SDFGEN_VENV=${PROVERS_DIR}/sdfgen-venv

# Release downloads go through this rather than a bare wget.  A multi-hour build
# that dies an hour in because GitHub answered one request with 503 is expensive,
# and unauthenticated release downloads do get throttled -- which is exactly what
# a 503 or 429 here means.  wget does not retry those by default.
export PROVERS_WGET_OPTS="${PROVERS_WGET_OPTS:---tries=8 --waitretry=15 --retry-connrefused --retry-on-http-error=408,429,500,502,503,504}"

# Where provers_fetch keeps downloaded archives.  Empty disables caching, which
# is the default: a one-off install has nothing to reuse.  A container build sets
# it to a BuildKit cache mount, so that rebuilding does not re-download hundreds
# of megabytes from GitHub -- which is what gets a build throttled.
: "${PROVERS_CACHE_DIR:=}"
export PROVERS_CACHE_DIR

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

# Prepend the tool directories only once so that repeated sourcing (e.g. nested
# bash shells) does not keep growing PATH.
case ":${PATH}:" in
  *":${VERUS_DIR}:"*) ;;
  *) export PATH="${VERUS_DIR}:${SIREUM_HOME}/bin:${SDFGEN_VENV}/bin:${HOME}/.cargo/bin:${PATH}" ;;
esac

unset _PROVERS_BIN_DIR
