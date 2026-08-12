# Tool versions for the DARPA PROVERS development environment.
#
# This is the single source of truth for what gets installed.  The Vagrant VM
# and the bare-metal setup source it directly; the container builds get the same
# values because docker/docker.sh sources this file and passes them as
# --build-arg.  Previously the Dockerfile ARGs and this file mirrored each other
# by hand, which only stays true until someone forgets.
#
# Every entry can be overridden by exporting the variable before running
# setup.sh / provers-setup.sh / docker.sh, e.g.
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

# Sireum (kekinian), pinned for the same reason everything else here is: two
# setups run weeks apart should install the same Sireum.  Bump deliberately,
# after confirming the build still passes.
#
# SIREUM_V is the source revision that gets built.  SIREUM_INIT_V is the release
# whose prebuilt sireum.jar bootstraps that build; bin/init.sh derives it from
# SIREUM_V, but correctly only when SIREUM_V names a release tag (`4.*`) -- for a
# bare commit SHA it derives the nonexistent release `4.<sha>`.  So a SHA pin
# requires pinning both, and pinning SIREUM_INIT_V to a release rather than
# leaving it at the moving `dev` tag is what makes the bootstrap reproducible
# too.  Set SIREUM_V to a `4.*` tag and SIREUM_INIT_V can be left unset.
: "${SIREUM_V:=80aad0c2d5d3053b0a287b4a0e81536c99d8908f}"
: "${SIREUM_INIT_V:=4.20260720.6a05e505}"
: "${SIREUM_REPO:=https://github.com/sireum/kekinian}"

# The image docker/docker.sh builds, tags and pushes.  Override to publish
# somewhere else, e.g. your own Docker Hub account or a private registry:
#
#   PROVERS_IMAGE=myorg/microkit_provers bash docker/docker.sh
#
: "${PROVERS_IMAGE:=jasonbelt/microkit_provers}"

# Host architecture.  Everything that differs between x86_64 and aarch64 is
# derived from this rather than pinned per-arch, so one file drives both the x86
# environment and the ARM one.
: "${PROVERS_ARCH:=$(uname -m)}"
case "${PROVERS_ARCH}" in
  aarch64 | arm64)
    # canonicalised to the Linux spelling: it also names download artifacts
    # (zig's tarball, for one), and macOS reports arm64 when docker.sh sources
    # this file on the host.
    PROVERS_ARCH=aarch64
    : "${RUST_HOST_TRIPLE:=aarch64-unknown-linux-gnu}"
    : "${RUST_MUSL_TRIPLE:=aarch64-unknown-linux-musl}"
    # microkit-sdk-<ver>-linux-<arch>.tar.gz
    : "${MICROKIT_SDK_ARCH:=aarch64}"
    # Verus publishes no aarch64 release asset, and PyPI carries no aarch64
    # sdfgen wheel, so both are built from source on this architecture.
    : "${VERUS_FROM_SOURCE:=true}"
    : "${SDFGEN_FROM_SOURCE:=true}"
    # ARM's own download, the only host-aarch64 build of the cross toolchain.
    : "${ARM_GNU_TOOLCHAIN_HOST:=aarch64}"
    : "${ARM_GNU_TOOLCHAIN_URL:=https://developer.arm.com/-/media/files/downloads/gnu/12.2.rel1/binrel/arm-gnu-toolchain-12.2.rel1-aarch64-aarch64-none-elf.tar.xz}"
    ;;
  x86_64 | amd64)
    PROVERS_ARCH=x86_64
    : "${RUST_HOST_TRIPLE:=x86_64-unknown-linux-gnu}"
    : "${RUST_MUSL_TRIPLE:=x86_64-unknown-linux-musl}"
    : "${MICROKIT_SDK_ARCH:=x86-64}"
    # The x86 Verus release ships a matching Z3, and sdfgen has an x86 wheel.
    : "${VERUS_FROM_SOURCE:=false}"
    : "${SDFGEN_FROM_SOURCE:=false}"
    # seL4's mirror of the same ARM release; the query string is part of the
    # object name here, not a parameter, so it stays percent-encoded.
    : "${ARM_GNU_TOOLCHAIN_HOST:=x86_64}"
    : "${ARM_GNU_TOOLCHAIN_URL:=https://sel4-toolchains.s3.us-east-2.amazonaws.com/arm-gnu-toolchain-12.2.rel1-x86_64-aarch64-none-elf.tar.xz%3Frev%3D28d5199f6db34e5980aae1062e5a6703%26hash%3DF6F5604BC1A2BBAAEAC4F6E98D8DC35B}"
    ;;
  *)
    echo "provers-env: unsupported architecture '${PROVERS_ARCH}'" >&2
    return 1 2>/dev/null || exit 1
    ;;
esac

# Cross toolchain used to build the domain-scheduling Microkit SDK.  Same
# release on both hosts, different host build of it.
: "${ARM_GNU_TOOLCHAIN_VER:=12.2.rel1}"
: "${ARM_GNU_TOOLCHAIN_DIR:=arm-gnu-toolchain-${ARM_GNU_TOOLCHAIN_VER}-${ARM_GNU_TOOLCHAIN_HOST}-aarch64-none-elf}"

# Sources for the from-source builds above.  Unused where the prebuilt assets
# exist, but pinned here so the two architectures stay on one set of versions.
# Verus requires this exact Z3; it is the version bundled in its x86 release.
: "${Z3_VER:=z3-4.12.5}"
: "${Z3_REPO:=https://github.com/Z3Prover/z3}"
: "${ZIG_VER:=0.15.2}"
: "${VERUS_REPO:=https://github.com/verus-lang/verus}"
: "${SDFGEN_REPO:=https://github.com/au-ts/microkit_sdf_gen}"

# Repositories used to build the Microkit SDK with domain scheduling support.
: "${SEL4_DOMAINS_REPO:=https://github.com/Ivan-Velickovic/seL4}"
: "${SEL4_DOMAINS_BRANCH:=microkit_domains}"
: "${MICROKIT_DOMAINS_REPO:=https://github.com/JE-Archer/microkit}"
: "${MICROKIT_DOMAINS_BRANCH:=domains}"

export MICROKIT_SDK_VER MICROKIT_DOMAINS_SDK_VER RUST_TOOLCHAIN_VER \
  RUST_NIGHTLY_VER SDFGEN_VER VERUS_VER \
  SIREUM_V SIREUM_INIT_V SIREUM_REPO PROVERS_IMAGE \
  PROVERS_ARCH RUST_HOST_TRIPLE RUST_MUSL_TRIPLE MICROKIT_SDK_ARCH \
  VERUS_FROM_SOURCE SDFGEN_FROM_SOURCE \
  ARM_GNU_TOOLCHAIN_VER ARM_GNU_TOOLCHAIN_HOST ARM_GNU_TOOLCHAIN_DIR \
  ARM_GNU_TOOLCHAIN_URL \
  Z3_VER Z3_REPO ZIG_VER VERUS_REPO SDFGEN_REPO \
  SEL4_DOMAINS_REPO SEL4_DOMAINS_BRANCH MICROKIT_DOMAINS_REPO \
  MICROKIT_DOMAINS_BRANCH
