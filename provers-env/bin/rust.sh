#!/bin/bash -e
# Install Rust.  Verus ${VERUS_VER} requires ${RUST_TOOLCHAIN_VER}.
set -Eeuo pipefail

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

# The full component set goes on the pinned channel, because that is the channel
# the crates HAMR generates now pin: rustc-dev and rust-src are what Verus
# verification needs, llvm-tools-preview is needed for microkit's no_std linking,
# and rust-analyzer is for the IDEs.
rustup toolchain install "${RUST_TOOLCHAIN_VER}-${RUST_HOST_TRIPLE}" \
  --component rustfmt,rust-src,rustc-dev,llvm-tools-preview,rust-analyzer

# Make the pin the toolchain that a bare 'cargo' or 'rustc' actually runs.
# Installing a toolchain only adds it; nothing here ever selected one, so the
# default stayed whatever rustup-init picked and 'rustc --version' reported a
# newer stable than ${RUST_TOOLCHAIN_VER} -- the version Verus is pinned
# against.  Re-running this script repairs an existing install too.
#
# PROVERS_RUST_DEFAULT=false skips it, for a machine that is not dedicated to
# this environment and has its own reason for the default it already has.  It is
# safe to skip: nothing installed here relies on the default.  Verus comes from a
# release, bin/microkit-vcpu-domain.sh builds with an explicit
# 'cargo +${RUST_TOOLCHAIN_VER}-${RUST_HOST_TRIPLE}', and the crates HAMR
# generates select the channel themselves through rust-toolchain.toml.  What you
# give up is a bare 'cargo build' in a hand-written crate using the pin.
if [ "${PROVERS_RUST_DEFAULT:-true}" = "true" ]; then
  rustup default "${RUST_TOOLCHAIN_VER}-${RUST_HOST_TRIPLE}"
else
  echo "rust.sh: leaving the rustup default alone (PROVERS_RUST_DEFAULT=false);"
  echo "         ${RUST_TOOLCHAIN_VER}-${RUST_HOST_TRIPLE} is installed and selectable with +${RUST_TOOLCHAIN_VER}"
fi
