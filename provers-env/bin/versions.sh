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
#   LIONSOS_VER=6855732 bash setup.sh
#
# This file is meant to be sourced, not executed.

# The version suffix a build carries: the container tags (<image>:<arch>_<ver>
# and the multi-arch <image>:<ver>), the VirtualBox machine name and the OVA name
# all come from this.  It is a date, and it is pinned rather than taken from the
# clock so that a rebuild can deliberately *replace* a published build instead of
# standing beside it -- which is what a fix to the pins above usually means.  Set
# it to today's date when publishing a genuinely new build:
#
#   PROVERS_BUILD_VER=$(date +%Y.%m.%d) bash docker/docker.sh
#
: "${PROVERS_BUILD_VER:=2026.08.18}"

: "${MICROKIT_SDK_VER:=2.3.0}"
# The toolchain Verus ${VERUS_VER} was built against, which is also the channel
# the Rust crates HAMR generates pin (see the rust-toolchain.toml codegen emits).
# It must also satisfy tool/microkit/Cargo.toml's rust-version = 1.94.0, which
# bin/microkit-vcpu-domain.sh builds against; cargo refuses outright if not.
: "${RUST_TOOLCHAIN_VER:=1.97.1}"
: "${SDFGEN_VER:=0.33.0}"
# Verus.  x86_64 unpacks the published release asset; aarch64 has none and
# builds from source.  bin/verus.sh checks out the commit named in the last
# field of this version rather than the release/<ver> tag, because that tag has
# been moved after its assets were published -- following it shipped a different
# Verus on each architecture.  Both paths assert the installed version matches
# this pin, so a re-cut release fails the build rather than passing silently.
: "${VERUS_VER:=0.2026.08.09.92f466f}"

# LionsOS, pinned to a commit rather than tracking main.  What is installed here
# is what the models build their VM examples against, and LionsOS carries sDDF
# and libvmm as submodules, so a moving checkout moves those APIs too -- later
# main broke the vms microexample outright.  Bump deliberately, after confirming
# the examples still build.
#
# This commit is the one kekinian pins in
# hamr/codegen/jvm/src/main/resources/microkit_versions.properties, and it is
# coupled to MICROKIT_SDK_VER: its libvmm uses seL4_VCPUReg_PAR, which exists
# only from Microkit 2.3.0 on.  The two move together.
: "${LIONSOS_VER:=3945dc5}"

# Sireum (kekinian), pinned for the same reason everything else here is: two
# setups run weeks apart should install the same Sireum.  Bump deliberately,
# after confirming the build still passes.
#
# SIREUM_V may name a release, or a commit or branch, and bin/sireum.sh tells
# them apart by asking whether a release of that name publishes an install.cmd.
# Both are quick; the difference is minutes rather than the hour a full toolchain
# build would suggest, since only Sireum itself is built:
#
#   4.20260810.80aad0c2  a numbered release: its `cli` distribution is
#                        unpacked, about a minute, and it is reproducible
#   dev                  also a release, so also unpacked and quick -- but a
#                        moving one, re-cut as kekinian advances, so two setups
#                        run weeks apart get different Sireums.  For tracking
#                        the tip deliberately, not for a pin
#   e8f69b3d... / master a commit or branch: no release exists, so kekinian is
#                        cloned and built from source -- around ten minutes on
#                        recent hardware, for the jar and the IVE distribution
#                        together, and more on a slower machine
#
# So prefer a numbered release unless the environment needs a revision newer
# than the last one -- which is the situation this pin is in today.
#
# SIREUM_INIT_V is only consulted on the source path: it is the release whose
# prebuilt sireum.jar bootstraps the build.  bin/init.sh derives it from
# SIREUM_V, but correctly only when SIREUM_V names a release tag (`4.*`) -- for a
# bare commit SHA it derives the nonexistent release `4.<sha>`.  So a SHA pin
# requires pinning both, and pinning SIREUM_INIT_V to a release rather than
# leaving it at the moving `dev` tag is what makes the bootstrap reproducible
# too.  Set SIREUM_V to a `4.*` tag and SIREUM_INIT_V can be left unset.
: "${SIREUM_V:=e8f69b3dadd477d83f7339be07038463819e9f27}"
: "${SIREUM_INIT_V:=4.20260810.80aad0c2}"
: "${SIREUM_REPO:=https://github.com/sireum/kekinian}"

# The image docker/docker.sh builds, tags and pushes.  Override to publish
# somewhere else, e.g. your own Docker Hub account or a private registry:
#
#   PROVERS_IMAGE=myorg/microkit_provers bash docker/docker.sh
#
: "${PROVERS_IMAGE:=jasonbelt/microkit_provers}"

# Host OS and architecture.  Everything that differs between the supported hosts
# -- Ubuntu on x86_64 and on aarch64, and macOS on Apple Silicon -- is derived
# from these two rather than pinned per host, so one file drives all of them and
# nothing above bin/ has to branch.
: "${PROVERS_OS:=$(uname -s)}"
case "${PROVERS_OS}" in
  Linux | linux)   PROVERS_OS=linux ;;
  Darwin | darwin) PROVERS_OS=darwin ;;
  *)
    echo "provers-env: unsupported OS '${PROVERS_OS}'" >&2
    return 1 2>/dev/null || exit 1
    ;;
esac

: "${PROVERS_ARCH:=$(uname -m)}"
case "${PROVERS_ARCH}" in
  # canonicalised to the Linux spelling: it also names download artifacts (zig's
  # tarball, for one), and macOS reports arm64 where Linux reports aarch64.
  aarch64 | arm64) PROVERS_ARCH=aarch64 ;;
  x86_64 | amd64)  PROVERS_ARCH=x86_64 ;;
  *)
    echo "provers-env: unsupported architecture '${PROVERS_ARCH}'" >&2
    return 1 2>/dev/null || exit 1
    ;;
esac

# Per-host values.  VERUS_RELEASE_ID and the two _FROM_SOURCE flags carry most of
# the difference: where an upstream publishes an asset for this host we unpack
# it, and where it does not we build from source.  Only Linux/aarch64 has to
# build anything -- Verus, its Z3 and sdfgen all publish Apple Silicon assets,
# which is why a Mac setup is much the quickest of the three.
case "${PROVERS_OS}:${PROVERS_ARCH}" in
  linux:x86_64)
    : "${RUST_HOST_TRIPLE:=x86_64-unknown-linux-gnu}"
    : "${RUST_MUSL_TRIPLE:=x86_64-unknown-linux-musl}"
    # names the SDK tarball: microkit-sdk-<ver>-<os>-<arch>.tar.gz
    : "${MICROKIT_SDK_OS:=linux}"
    : "${MICROKIT_SDK_ARCH:=x86-64}"
    # names the Verus release asset: verus-<ver>-<id>.zip, unpacking to verus-<id>
    : "${VERUS_RELEASE_ID:=x86-linux}"
    # The x86 Verus release ships a matching Z3, and sdfgen has an x86 wheel.
    : "${VERUS_FROM_SOURCE:=false}"
    : "${SDFGEN_FROM_SOURCE:=false}"
    ;;
  linux:aarch64)
    : "${RUST_HOST_TRIPLE:=aarch64-unknown-linux-gnu}"
    : "${RUST_MUSL_TRIPLE:=aarch64-unknown-linux-musl}"
    : "${MICROKIT_SDK_OS:=linux}"
    : "${MICROKIT_SDK_ARCH:=aarch64}"
    # Verus publishes no aarch64 Linux release asset, and PyPI carries no
    # aarch64 Linux sdfgen wheel, so both are built from source here -- and Z3
    # with them, since a Verus release is where the matching Z3 comes from.
    : "${VERUS_RELEASE_ID:=}"
    : "${VERUS_FROM_SOURCE:=true}"
    : "${SDFGEN_FROM_SOURCE:=true}"
    ;;
  darwin:aarch64)
    : "${RUST_HOST_TRIPLE:=aarch64-apple-darwin}"
    # no musl target on macOS
    : "${RUST_MUSL_TRIPLE:=}"
    : "${MICROKIT_SDK_OS:=macos}"
    : "${MICROKIT_SDK_ARCH:=aarch64}"
    # verus-<ver>-arm64-macos.zip, which bundles a matching Z3 next to the
    # binary exactly as the x86 Linux release does; and PyPI carries an arm64
    # macOS sdfgen wheel.  So nothing is built from source here.
    : "${VERUS_RELEASE_ID:=arm64-macos}"
    : "${VERUS_FROM_SOURCE:=false}"
    : "${SDFGEN_FROM_SOURCE:=false}"
    ;;
  darwin:x86_64)
    # Intel Macs fall out of the same derivation -- upstream publishes for them
    # too -- but this environment is developed and tested on Apple Silicon.
    : "${RUST_HOST_TRIPLE:=x86_64-apple-darwin}"
    : "${RUST_MUSL_TRIPLE:=}"
    : "${MICROKIT_SDK_OS:=macos}"
    : "${MICROKIT_SDK_ARCH:=x86-64}"
    : "${VERUS_RELEASE_ID:=x86-macos}"
    : "${VERUS_FROM_SOURCE:=false}"
    : "${SDFGEN_FROM_SOURCE:=false}"
    ;;
esac

# The python the sdfgen venv is built with, and which runs the small helper
# scripts in bin/.  3.12 on every host: it is Ubuntu 24.04's default, and
# bin/deps.sh installs Homebrew's python@3.12 to match on macOS, so two setups
# do not quietly differ in the interpreter sdfgen is installed for.
: "${PROVERS_PYTHON:=python3.12}"

# Sources for the from-source builds above.  Unused where the prebuilt assets
# exist, but pinned here so every host stays on one set of versions.  Verus
# requires this exact Z3; it is the version bundled in its releases, so it moves
# with VERUS_VER -- check `z3 --version` next to the verus binary in the release
# before bumping one without the other.
: "${Z3_VER:=z3-4.16.0}"
: "${Z3_REPO:=https://github.com/Z3Prover/z3}"
: "${ZIG_VER:=0.15.2}"
# zig's tarball is zig-<arch>-<os>-<ver>.tar.xz
if [ "${PROVERS_OS}" = "darwin" ]; then
  : "${ZIG_PLATFORM:=${PROVERS_ARCH}-macos}"
else
  : "${ZIG_PLATFORM:=${PROVERS_ARCH}-linux}"
fi
: "${VERUS_REPO:=https://github.com/verus-lang/verus}"
: "${SDFGEN_REPO:=https://github.com/au-ts/microkit_sdf_gen}"
: "${LIONSOS_REPO:=https://github.com/au-ts/lionsos}"

# Upstream Microkit, used to rebuild the SDK's `microkit` tool with the vCPU
# domain fix (see microkit-vcpu-domain.sh).  The tag is MICROKIT_SDK_VER, so the
# rebuilt tool is exactly the released one plus that fix.
: "${MICROKIT_REPO:=https://github.com/seL4/microkit}"

# Nothing here is exported.  These are the install scripts' own values, and each
# script sources this file (through env.sh) rather than inheriting them, so they
# work just as well as ordinary shell variables -- and do not end up in the
# environment of every command the user afterwards runs.  bin/env.sh exports the
# short list a build actually needs; see the comment there.
#
# docker/docker.sh sources this file and reads these to build its --build-arg
# list, which works the same way: same shell, no export needed.
