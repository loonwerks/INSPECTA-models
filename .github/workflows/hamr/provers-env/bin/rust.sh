#!/bin/bash -e
# Install Rust.  Verus ${VERUS_VER} requires ${RUST_TOOLCHAIN_VER}.
set -Eeuxo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/env.sh"

if [ ! -f "${HOME}/.cargo/env" ]; then
  curl --proto '=https' --tlsv1.2 -sSf https://sh.rustup.rs | sh -s -- -y
fi

source "${HOME}/.cargo/env"

rustup toolchain install "${RUST_TOOLCHAIN_VER}-${RUST_HOST_TRIPLE}"
rustup component add rust-src --toolchain "${RUST_TOOLCHAIN_VER}-${RUST_HOST_TRIPLE}"
rustup toolchain install "${RUST_NIGHTLY_VER}" \
  --component rustfmt,rust-src,rustc-dev,llvm-tools-preview,rust-analyzer
