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
# SIREUM_V is the source revision that gets built.  SIREUM_INIT_V is the release
# whose prebuilt sireum.jar bootstraps that build; bin/init.sh derives it from
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
    ;;
  x86_64 | amd64)
    PROVERS_ARCH=x86_64
    : "${RUST_HOST_TRIPLE:=x86_64-unknown-linux-gnu}"
    : "${RUST_MUSL_TRIPLE:=x86_64-unknown-linux-musl}"
    : "${MICROKIT_SDK_ARCH:=x86-64}"
    # The x86 Verus release ships a matching Z3, and sdfgen has an x86 wheel.
    : "${VERUS_FROM_SOURCE:=false}"
    : "${SDFGEN_FROM_SOURCE:=false}"
    ;;
  *)
    echo "provers-env: unsupported architecture '${PROVERS_ARCH}'" >&2
    return 1 2>/dev/null || exit 1
    ;;
esac

# Sources for the from-source builds above.  Unused where the prebuilt assets
# exist, but pinned here so the two architectures stay on one set of versions.
# Verus requires this exact Z3; it is the version bundled in its x86 release, so
# it moves with VERUS_VER -- check `z3 --version` next to the verus binary in the
# release before bumping one without the other.
: "${Z3_VER:=z3-4.16.0}"
: "${Z3_REPO:=https://github.com/Z3Prover/z3}"
: "${ZIG_VER:=0.15.2}"
: "${VERUS_REPO:=https://github.com/verus-lang/verus}"
: "${SDFGEN_REPO:=https://github.com/au-ts/microkit_sdf_gen}"
: "${LIONSOS_REPO:=https://github.com/au-ts/lionsos}"

# Upstream Microkit, used to rebuild the SDK's `microkit` tool with the vCPU
# domain fix (see microkit-vcpu-domain.sh).  The tag is MICROKIT_SDK_VER, so the
# rebuilt tool is exactly the released one plus that fix.
: "${MICROKIT_REPO:=https://github.com/seL4/microkit}"

export MICROKIT_SDK_VER RUST_TOOLCHAIN_VER \
  SDFGEN_VER VERUS_VER LIONSOS_VER LIONSOS_REPO \
  SIREUM_V SIREUM_INIT_V SIREUM_REPO PROVERS_IMAGE PROVERS_BUILD_VER \
  PROVERS_ARCH RUST_HOST_TRIPLE RUST_MUSL_TRIPLE MICROKIT_SDK_ARCH \
  VERUS_FROM_SOURCE SDFGEN_FROM_SOURCE \
  Z3_VER Z3_REPO ZIG_VER VERUS_REPO SDFGEN_REPO MICROKIT_REPO
