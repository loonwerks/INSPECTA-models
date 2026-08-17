#!/bin/bash
# Install the DARPA PROVERS development environment onto this machine, by running
# the scripts in bin/ in order.
#
# Requirement: Ubuntu 24.04, x86_64 or aarch64.
#
# Nothing here knows about Vagrant or Docker; it is run directly on a dedicated
# machine, and vagrant/Vagrantfile also copies it into the VM and runs it there.
# docker/Dockerfile.provers does not use it -- it runs the same bin/ scripts as
# separate steps, so that each is a cacheable layer.
#
# Everything is installed under ${PROVERS_DIR} (default ~/provers).  Individual
# steps can be re-run later from bin/ -- ~/bin in the VM (see vagrant/readme.md).

set -Eeuxo pipefail

# bin/ sits next to this script in the repository, but the VM's file provisioner
# lands the two separately, as ~/provers-setup.sh and ~/bin.
BIN_DIR="${BIN_DIR:-$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/bin}"
if [ ! -d "${BIN_DIR}" ]; then
  BIN_DIR="${HOME}/bin"
fi

# shellcheck source=bin/env.sh
source "${BIN_DIR}/env.sh"

mkdir -p "${PROVERS_DIR}"

# Reclaim any unallocated space in root's LVM volume group before installing into
# it.  This is aimed at the Vagrant base box, which formats only about half its
# disk, but it is guarded rather than conditional: on a machine without LVM, or
# with no free extents, it does nothing.
bash "${BIN_DIR}/disk.sh"

bash "${BIN_DIR}/deps.sh"
bash "${BIN_DIR}/rust.sh"
# no-op where the Verus release bundles a Z3; verus.sh links against it where not
bash "${BIN_DIR}/z3.sh"
bash "${BIN_DIR}/verus.sh"
bash "${BIN_DIR}/microkit-lionsos.sh"
bash "${BIN_DIR}/microkit-domains.sh"
bash "${BIN_DIR}/sireum.sh"

# The IDEs are independent of each other and of the command-line tools, so each
# is opt-out on its own.  ive.sh and codeive.sh both pull in Init.deps(); see
# sireum.sh for what happens when neither is selected.
if [ "${PROVERS_IVE}" = "true" ]; then
  bash "${BIN_DIR}/ive.sh"
fi
if [ "${PROVERS_CODEIVE}" = "true" ]; then
  bash "${BIN_DIR}/codeive.sh"
fi
if [ "${PROVERS_FMIDE}" = "true" ]; then
  bash "${BIN_DIR}/fmide.sh"
fi

# Last, so that it records what actually got installed
bash "${BIN_DIR}/build-info.sh"

echo "Wiring the PROVERS environment into ~/.bashrc"
if ! grep -q "provers-env" "${HOME}/.bashrc" 2>/dev/null; then
  cat >> "${HOME}/.bashrc" << EOF

# provers-env
export LANG=en_US.UTF-8
source ${BIN_DIR}/env.sh
source ${BIN_DIR}/functions.sh
EOF
fi

# aliases carried over from the Dockerfile's final stage
if [ ! -f "${HOME}/.bash_aliases" ] || ! grep -q "alias ddir=" "${HOME}/.bash_aliases"; then
  {
    echo "alias env='env | sort'"
    echo "alias ddir='ls -lFaG'"
    echo "alias dir='ls -lFG'"
    echo "alias ..='cd ..'"
  } >> "${HOME}/.bash_aliases"
fi

set +x
echo ""
echo "PROVERS environment setup complete.  Installed under ${PROVERS_DIR}:"
echo "  Verus                      ${VERUS_DIR}"
echo "  Microkit SDK (domains)     ${MICROKIT_SDK}"
echo "  Microkit SDK ${MICROKIT_SDK_VER}         ${MICROKIT_SDK_CURRENT}"
echo "  LionsOS ${LIONSOS_VER}            ${LIONSOS}"
echo "  Sireum                     ${SIREUM_HOME}"
if [ "${PROVERS_IVE}" = "true" ]; then
  echo "  Sireum IVE                 installed"
fi
if [ "${PROVERS_CODEIVE}" = "true" ]; then
  echo "  CodeIVE                    installed"
fi
if [ "${PROVERS_FMIDE}" = "true" ]; then
  echo "  FMIDE                      ${SIREUM_HOME}/bin/linux/fmide"
fi
echo ""
echo "Open a new shell (or 'source ${BIN_DIR}/env.sh') to pick up the environment."
echo "The installed IDEs are launched with the 'ive', 'codium' and 'fmide' shell functions."
