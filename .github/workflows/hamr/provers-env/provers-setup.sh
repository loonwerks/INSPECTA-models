#!/bin/bash
# Set up the DARPA PROVERS development environment.
#
# Requirement: Ubuntu 24.04 (amd64)
#
# This is the Vagrant/bare-metal counterpart of
#   .github/workflows/hamr/docker/Dockerfile.provers_LinuxAMD64
#
# Everything is installed under ${PROVERS_DIR} (default ~/provers).  Individual
# steps can be re-run later from ~/bin (see the readme).

set -Eeuxo pipefail

BIN_DIR="${BIN_DIR:-$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/bin}"
if [ ! -d "${BIN_DIR}" ]; then
  BIN_DIR="${HOME}/bin"
fi

# shellcheck source=bin/env.sh
source "${BIN_DIR}/env.sh"

mkdir -p "${PROVERS_DIR}"

# reclaim the unallocated half of the box's disk before installing into it
bash "${BIN_DIR}/disk.sh"

bash "${BIN_DIR}/deps.sh"
bash "${BIN_DIR}/rust.sh"
bash "${BIN_DIR}/verus.sh"
bash "${BIN_DIR}/microkit-lionsos.sh"
bash "${BIN_DIR}/microkit-domains.sh"
bash "${BIN_DIR}/sireum.sh"
bash "${BIN_DIR}/fmide.sh"

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
echo "  LionsOS                    ${LIONSOS}"
echo "  Sireum + IVE + CodeIVE     ${SIREUM_HOME}"
echo "  FMIDE                      ${SIREUM_HOME}/bin/linux/fmide"
echo ""
echo "Open a new shell (or 'source ${BIN_DIR}/env.sh') to pick up the environment."
echo "The IDEs are launched with the 'ive', 'codium' and 'fmide' shell functions."
