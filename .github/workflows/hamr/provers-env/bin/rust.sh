#!/bin/bash -e
# Install Rust.  Verus ${VERUS_VER} requires ${RUST_TOOLCHAIN_VER}.
set -Eeuxo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/env.sh"

if [ ! -f "${HOME}/.cargo/env" ]; then
  # --default-toolchain none because the pinned toolchain is installed below.
  # Left to itself rustup-init downloads whatever stable is current and makes
  # that the default, which is both a wasted toolchain and how the pin ended up
  # installed but unused.
  curl --proto '=https' --tlsv1.2 -sSf https://sh.rustup.rs | sh -s -- -y \
    --default-toolchain none
fi

source "${HOME}/.cargo/env"

rustup toolchain install "${RUST_TOOLCHAIN_VER}-${RUST_HOST_TRIPLE}"
rustup component add rust-src --toolchain "${RUST_TOOLCHAIN_VER}-${RUST_HOST_TRIPLE}"
rustup toolchain install "${RUST_NIGHTLY_VER}" \
  --component rustfmt,rust-src,rustc-dev,llvm-tools-preview,rust-analyzer

# Make the pin the toolchain that a bare 'cargo' or 'rustc' actually runs.
# Installing a toolchain only adds it alongside the others; nothing here ever
# selected one, so the default stayed whatever rustup-init picked and
# 'rustc --version' reported a newer stable than ${RUST_TOOLCHAIN_VER} -- the
# version Verus is pinned against.  Re-running this script repairs an existing
# install too.
rustup default "${RUST_TOOLCHAIN_VER}-${RUST_HOST_TRIPLE}"
