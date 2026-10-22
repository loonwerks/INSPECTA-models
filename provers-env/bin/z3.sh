#!/bin/bash -e
# Build Z3 ${Z3_VER} from source into ${PROVERS_DIR}/z3, for the architectures
# where Verus has to be built from source too.
#
# Verus is tied to one exact Z3 version.  Wherever a Verus release is used
# (x86_64 Linux and macOS) verus.sh installs that Z3 itself -- the one bundled in
# the release, or, for an asset that bundles none (as arm64-macos of
# 0.2026.09.06.8dea4a2 did), the official Z3 release build named by Z3_RELEASE_ID --
# so there is nothing to do here and this script is a no-op.  aarch64 Linux has
# no Verus release at all, so Verus and its Z3 are built from source there;
# verus.sh then links this Z3 in as ${VERUS_Z3_PATH} so every host ends up with
# the same layout.
set -Eeuo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/env.sh"

if [ "${VERUS_FROM_SOURCE}" != "true" ]; then
  set +x
  echo "z3.sh: ${PROVERS_OS}/${PROVERS_ARCH} installs Verus from a release, and verus.sh installs its Z3; nothing to build"
  exit 0
fi

Z3_BUILD_DIR=${PROVERS_DIR}/z3-build

mkdir -p "${PROVERS_DIR}"
rm -rf "${Z3_BUILD_DIR}" "${Z3_DIR}"

git clone "${Z3_REPO}" "${Z3_BUILD_DIR}"
cd "${Z3_BUILD_DIR}"
git checkout "${Z3_VER}"

"${PROVERS_PYTHON}" scripts/mk_make.py
cd "${Z3_BUILD_DIR}/build"
make "-j$(provers_nproc)"

# DESTDIR + PREFIX rather than --prefix: this lands the install at
# ${PROVERS_DIR}/z3/{bin,include,lib} without needing root.
make -C "${Z3_BUILD_DIR}/build" install DESTDIR="$(dirname "${Z3_DIR}")/" PREFIX="$(basename "${Z3_DIR}")"

cd "${PROVERS_DIR}"
rm -rf "${Z3_BUILD_DIR}"

"${Z3_DIR}/bin/z3" --version
