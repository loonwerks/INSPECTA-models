# Tool versions for the DARPA PROVERS development environment.
#
# These mirror the ARGs in
#   .github/workflows/hamr/docker/Dockerfile.provers_LinuxAMD64
#
# Every entry can be overridden by exporting the variable before running
# setup.sh / provers-setup.sh, e.g.
#
#   MICROKIT_SDK_VER=2.2.0 bash setup.sh
#
# This file is meant to be sourced, not executed.

: "${MICROKIT_SDK_VER:=2.2.0}"
: "${MICROKIT_DOMAINS_SDK_VER:=1.4.1}"
: "${RUST_TOOLCHAIN_VER:=1.92.0}"
: "${RUST_NIGHTLY_VER:=nightly-2026-01-25}"
: "${SDFGEN_VER:=0.32.0}"
: "${VERUS_VER:=0.2026.01.23.1650a05}"

# Rust host triple.  The VM created by the accompanying Vagrantfile is x86_64;
# see the readme if you want to target an aarch64 host instead.
: "${RUST_HOST_TRIPLE:=x86_64-unknown-linux-gnu}"

# Repositories used to build the Microkit SDK with domain scheduling support.
: "${SEL4_DOMAINS_REPO:=https://github.com/Ivan-Velickovic/seL4}"
: "${SEL4_DOMAINS_BRANCH:=microkit_domains}"
: "${MICROKIT_DOMAINS_REPO:=https://github.com/JE-Archer/microkit}"
: "${MICROKIT_DOMAINS_BRANCH:=domains}"

export MICROKIT_SDK_VER MICROKIT_DOMAINS_SDK_VER RUST_TOOLCHAIN_VER \
  RUST_NIGHTLY_VER SDFGEN_VER VERUS_VER RUST_HOST_TRIPLE \
  SEL4_DOMAINS_REPO SEL4_DOMAINS_BRANCH MICROKIT_DOMAINS_REPO \
  MICROKIT_DOMAINS_BRANCH
