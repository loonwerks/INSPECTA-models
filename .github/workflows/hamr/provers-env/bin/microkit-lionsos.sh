#!/bin/bash -e
# Install the Microkit SDK ${MICROKIT_SDK_VER}, the sdfgen ${SDFGEN_VER} python
# package (into its own venv) and LionsOS.
#
# Unlike the Dockerfile this keeps the LionsOS examples, its .git directory and
# the micropython/wasm-micro-runtime deps -- those are stripped from the image
# purely to keep it small, and they are handy in a development VM.
set -Eeuxo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/env.sh"

mkdir -p "${PROVERS_DIR}"
cd "${PROVERS_DIR}"

echo "Creating the sdfgen venv"
rm -rf "${SDFGEN_VENV}"
python3.12 -m venv "${SDFGEN_VENV}"
"${SDFGEN_VENV}/bin/pip" install --upgrade pip
"${SDFGEN_VENV}/bin/pip" install "sdfgen==${SDFGEN_VER}"

echo "Installing the Microkit SDK ${MICROKIT_SDK_VER}"
rm -rf "${MICROKIT_SDK_CURRENT}"
wget "https://github.com/seL4/microkit/releases/download/${MICROKIT_SDK_VER}/microkit-sdk-${MICROKIT_SDK_VER}-linux-x86-64.tar.gz"
tar xf "${PROVERS_DIR}/microkit-sdk-${MICROKIT_SDK_VER}-linux-x86-64.tar.gz"
rm -f "${PROVERS_DIR}/microkit-sdk-${MICROKIT_SDK_VER}-linux-x86-64.tar.gz"

echo "Cloning LionsOS"
rm -rf "${LIONSOS}"
git clone --rec --depth=1 --shallow-submodules https://github.com/au-ts/lionsos.git "${LIONSOS}"
