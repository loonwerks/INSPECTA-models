#!/bin/bash -e
# Install the Verus ${VERUS_VER} x86 Linux release into ${VERUS_DIR}.
set -Eeuxo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/env.sh"

mkdir -p "${PROVERS_DIR}"
cd "${PROVERS_DIR}"

rm -rf "${VERUS_DIR}" "${PROVERS_DIR}/verus-x86-linux"

wget "https://github.com/verus-lang/verus/releases/download/release%2F${VERUS_VER}/verus-${VERUS_VER}-x86-linux.zip"
unzip "${PROVERS_DIR}/verus-${VERUS_VER}-x86-linux.zip"
mv "${PROVERS_DIR}/verus-x86-linux" "${VERUS_DIR}"
rm -rf "${PROVERS_DIR}"/*.zip
