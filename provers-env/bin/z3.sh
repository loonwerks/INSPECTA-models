#!/bin/bash -e
# Build Z3 ${Z3_VER} from source into ${PROVERS_DIR}/z3, for the architectures
# where Verus has to be built from source too.
#
# Verus is tied to one exact Z3 version, and every published Verus release ships
# that Z3 alongside the binary -- so wherever a release is used (x86_64 Linux and
# macOS) there is nothing to do here and this script is a no-op.  aarch64 Linux
# has no Verus release at all, and Z3 publishes no aarch64 Linux release either,
# so it is built here; verus.sh then links it in as ${VERUS_Z3_PATH} so every
# host ends up with the same layout.
set -Eeuo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/env.sh"

if [ "${VERUS_FROM_SOURCE}" != "true" ]; then
  set +x
  echo "z3.sh: ${PROVERS_OS}/${PROVERS_ARCH} uses the Z3 bundled with the Verus release; nothing to build"
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
