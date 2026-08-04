# Environment for the DARPA PROVERS development environment.
#
# This mirrors the ENV settings of the final stage of
#   .github/workflows/hamr/docker/Dockerfile.provers_LinuxAMD64
#
# It is sourced by each of the install scripts in this directory and, after
# provisioning, by ~/.bashrc.  It is meant to be sourced, not executed.

_PROVERS_BIN_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
# shellcheck source=versions.sh
source "${_PROVERS_BIN_DIR}/versions.sh"

: "${PROVERS_DIR:=$HOME/provers}"

export PROVERS_DIR
export LIONSOS=${PROVERS_DIR}/lionsos
export VMM_DIR=${LIONSOS}/dep/libvmm
export MICROKIT_BOARD=${MICROKIT_BOARD:-qemu_virt_aarch64}
export MICROKIT_SDK=${PROVERS_DIR}/microkit-sdk-${MICROKIT_DOMAINS_SDK_VER}
export MICROKIT_SDK_CURRENT=${PROVERS_DIR}/microkit-sdk-${MICROKIT_SDK_VER}
export SIREUM_HOME=${PROVERS_DIR}/Sireum
export VERUS_DIR=${PROVERS_DIR}/verus
export VERUS_Z3_PATH=${VERUS_DIR}/z3
export SDFGEN_VENV=${PROVERS_DIR}/sdfgen-venv

# Prepend the tool directories only once so that repeated sourcing (e.g. nested
# bash shells) does not keep growing PATH.
case ":${PATH}:" in
  *":${VERUS_DIR}:"*) ;;
  *) export PATH="${VERUS_DIR}:${SIREUM_HOME}/bin:${SDFGEN_VENV}/bin:${HOME}/.cargo/bin:${PATH}" ;;
esac

unset _PROVERS_BIN_DIR
