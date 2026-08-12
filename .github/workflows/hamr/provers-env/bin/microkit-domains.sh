#!/bin/bash -e
# Build the Microkit SDK ${MICROKIT_DOMAINS_SDK_VER} with domain scheduling
# support, i.e. ${MICROKIT_SDK}.  This is the longest step of the setup.
set -Eeuxo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/env.sh"
source "${HOME}/.cargo/env"

MICROKIT_BUILD_DIR=${PROVERS_DIR}/microkit-sdk-${MICROKIT_DOMAINS_SDK_VER}-build

rm -rf "${MICROKIT_BUILD_DIR}" "${MICROKIT_SDK}"
mkdir -p "${MICROKIT_BUILD_DIR}"
cd "${MICROKIT_BUILD_DIR}"

rustup target add x86_64-unknown-linux-musl
rustup target add aarch64-unknown-linux-musl

# The target is aarch64-none-elf either way; what differs is the host build of
# the cross toolchain, and where it is published -- ARM_GNU_TOOLCHAIN_URL and
# _DIR are resolved per architecture in versions.sh.
echo "Fetching the ${ARM_GNU_TOOLCHAIN_HOST}-hosted aarch64-none-elf toolchain"
provers_fetch "${ARM_GNU_TOOLCHAIN_URL}" \
  "${MICROKIT_BUILD_DIR}/aarch64-toolchain.tar.gz" "${ARM_GNU_TOOLCHAIN_DIR}.tar.xz"
tar xf "${MICROKIT_BUILD_DIR}/aarch64-toolchain.tar.gz"
rm "${MICROKIT_BUILD_DIR}/aarch64-toolchain.tar.gz"
export PATH=${MICROKIT_BUILD_DIR}/${ARM_GNU_TOOLCHAIN_DIR}/bin:${PATH}:.

git clone "${SEL4_DOMAINS_REPO}" --branch "${SEL4_DOMAINS_BRANCH}"
git clone "${MICROKIT_DOMAINS_REPO}" --branch "${MICROKIT_DOMAINS_BRANCH}"

cd microkit
python3.12 -m venv pyenv
./pyenv/bin/pip install --upgrade pip setuptools wheel
./pyenv/bin/pip install -r requirements.txt
./pyenv/bin/python build_sdk.py \
  --experimental-domain-support \
  --sel4="${MICROKIT_BUILD_DIR}/seL4" \
  --configs debug \
  --tool-target-triple="${RUST_MUSL_TRIPLE}"

microkit=$(find "${MICROKIT_BUILD_DIR}/microkit/release/" -type d -name 'microkit-sdk*')
mv "${microkit}" "${MICROKIT_SDK}"

cd "${PROVERS_DIR}"
rm -rf "${MICROKIT_BUILD_DIR}"
