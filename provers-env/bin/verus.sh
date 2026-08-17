#!/bin/bash -e
# Install Verus ${VERUS_VER} into ${VERUS_DIR}.
#
# x86_64 gets the published release, which bundles a matching Z3.  aarch64 has
# no published release, so Verus is built from source there against the Z3 that
# z3.sh built -- run that first.
set -Eeuxo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/env.sh"

# The two architectures install Verus by different routes, so the pin is only
# worth anything if both end up on the same build.  Verus derives its version
# string from the commit it was built from, so checking it here catches a moved
# tag or a re-cut release asset at install time rather than leaving it to be
# noticed in a published image.
verus_assert_version() {
  local got
  got="$("$1" --version 2>&1 | sed -n 's/^[[:space:]]*Version:[[:space:]]*//p' | head -1)"
  if [ "${got}" != "${VERUS_VER}" ]; then
    echo "verus.sh: installed Verus reports '${got}', expected '${VERUS_VER}'." >&2
    echo "verus.sh: on aarch64, set VERUS_REV to the commit release/${VERUS_VER}" >&2
    echo "verus.sh: was cut from; otherwise update VERUS_VER in bin/versions.sh." >&2
    exit 1
  fi
}

mkdir -p "${PROVERS_DIR}"
cd "${PROVERS_DIR}"

rm -rf "${VERUS_DIR}"

if [ "${VERUS_FROM_SOURCE}" != "true" ]; then
  rm -rf "${PROVERS_DIR}/verus-x86-linux"
  provers_fetch "${VERUS_REPO}/releases/download/release%2F${VERUS_VER}/verus-${VERUS_VER}-x86-linux.zip" \
    "${PROVERS_DIR}/verus-${VERUS_VER}-x86-linux.zip"
  unzip "${PROVERS_DIR}/verus-${VERUS_VER}-x86-linux.zip"
  mv "${PROVERS_DIR}/verus-x86-linux" "${VERUS_DIR}"
  rm -rf "${PROVERS_DIR}"/*.zip
  "${VERUS_Z3_PATH}" --version
  verus_assert_version "${VERUS_DIR}/verus"
  exit 0
fi

# --- built from source (aarch64) ---
#
# See https://verus-lang.zulipchat.com/#narrow/channel/399078-help/topic/
#     .E2.9C.94.20Verus.20support.20for.20arm64.20on.20linux.3F/near/561282380

if [ ! -x "${Z3_DIR}/bin/z3" ]; then
  echo "verus.sh: ${Z3_DIR}/bin/z3 is missing -- run z3.sh first" >&2
  exit 1
fi

source "${HOME}/.cargo/env"

VERUS_BUILD_DIR=${PROVERS_DIR}/verus-build
rm -rf "${VERUS_BUILD_DIR}"

git clone "${VERUS_REPO}" "${VERUS_BUILD_DIR}"
cd "${VERUS_BUILD_DIR}/source"

# Check out the commit, not the release tag.  A release tag is not a stable
# pointer: release/0.2026.01.23.1650a05 was moved after its assets were
# published and now resolves to 772e2256, two days newer than the tag's own
# name.  x86_64 unpacks the asset built from the original commit, so following
# the tag here shipped a different Verus on each architecture from one pin.
#
# A Verus version is 0.<year>.<month>.<day>.<short sha>, and that sha is the
# commit the release was cut from -- which is what the x86 asset contains.  Set
# VERUS_REV to override, for a pin whose last field is not the commit.
git checkout "${VERUS_REV:-${VERUS_VER##*.}}"

# vargo looks for z3 next to the sources rather than on PATH.
ln -s "${Z3_DIR}/bin/z3" ./z3

# tools/activate is written for an interactive shell and reads variables it has
# not set, which -u turns into a fatal error; vargo itself is run with -u off
# for the same reason.
# vargo resolves z3 through VERUS_Z3_PATH, which env.sh points at ${VERUS_DIR}/z3
# -- the symlink created below, in a directory this script has just deleted.  So
# for the build itself, point it at the Z3 that z3.sh actually installed;
# otherwise vargo stops with "could not execute z3".
export VERUS_Z3_PATH=${Z3_DIR}/bin/z3

set +u
source ../tools/activate
vargo build --release
set -u

cp -r ./target-verus/release "${VERUS_DIR}"
# Match the layout of the x86 release, where z3 sits next to the verus binary,
# which is what makes one VERUS_Z3_PATH correct on both architectures.  vargo may
# have placed a z3 there already.
if [ ! -e "${VERUS_DIR}/z3" ]; then
  ln -s "${Z3_DIR}/bin/z3" "${VERUS_DIR}/z3"
fi

cd "${PROVERS_DIR}"
rm -rf "${VERUS_BUILD_DIR}"

"${VERUS_DIR}/verus" --version
"${VERUS_DIR}/z3" --version
verus_assert_version "${VERUS_DIR}/verus"
