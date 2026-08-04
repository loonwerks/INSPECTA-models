#!/bin/bash -e
# Build the Microkit SDK ${MICROKIT_DOMAINS_SDK_VER} with domain scheduling
# support, i.e. ${MICROKIT_SDK}.  This is the longest step of the setup.
set -Eeuxo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/env.sh"
source "${HOME}/.cargo/env"

MICROKIT_BUILD_DIR=${PROVERS_DIR}/microkit-sdk-${MICROKIT_DOMAINS_SDK_VER}-build
AARCH64_TOOLCHAIN_DIR=arm-gnu-toolchain-12.2.rel1-x86_64-aarch64-none-elf
AARCH64_TOOLCHAIN_URL='https://sel4-toolchains.s3.us-east-2.amazonaws.com/arm-gnu-toolchain-12.2.rel1-x86_64-aarch64-none-elf.tar.xz%3Frev%3D28d5199f6db34e5980aae1062e5a6703%26hash%3DF6F5604BC1A2BBAAEAC4F6E98D8DC35B'

rm -rf "${MICROKIT_BUILD_DIR}" "${MICROKIT_SDK}"
mkdir -p "${MICROKIT_BUILD_DIR}"
cd "${MICROKIT_BUILD_DIR}"

rustup target add x86_64-unknown-linux-musl
rustup target add aarch64-unknown-linux-musl

echo "Fetching the aarch64-none-elf toolchain"
wget -O aarch64-toolchain.tar.gz "${AARCH64_TOOLCHAIN_URL}"
tar xf "${MICROKIT_BUILD_DIR}/aarch64-toolchain.tar.gz"
rm "${MICROKIT_BUILD_DIR}/aarch64-toolchain.tar.gz"
export PATH=${MICROKIT_BUILD_DIR}/${AARCH64_TOOLCHAIN_DIR}/bin:${PATH}:.

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
  --tool-target-triple="x86_64-unknown-linux-musl"

microkit=$(find "${MICROKIT_BUILD_DIR}/microkit/release/" -type d -name 'microkit-sdk*')
mv "${microkit}" "${MICROKIT_SDK}"

cd "${PROVERS_DIR}"
rm -rf "${MICROKIT_BUILD_DIR}"
