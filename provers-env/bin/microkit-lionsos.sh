#!/bin/bash -e
# Install the Microkit SDK ${MICROKIT_SDK_VER}, the sdfgen ${SDFGEN_VER} python
# package (into its own venv) and LionsOS ${LIONSOS_VER}.
#
# sdfgen comes from PyPI wherever a wheel exists for the host (x86_64 Linux and
# arm64 macOS); aarch64 Linux has none, so there it is built from source, which
# needs zig ${ZIG_VER}.
#
# LionsOS is a full clone at the pinned commit, examples and history included;
# bin/slim.sh strips those from a shipped image purely to keep it small, and they
# are handy in a development VM.
set -Eeuo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/env.sh"

mkdir -p "${PROVERS_DIR}"
cd "${PROVERS_DIR}"

echo "Creating the sdfgen venv"
rm -rf "${SDFGEN_VENV}"
"${PROVERS_PYTHON}" -m venv "${SDFGEN_VENV}"
"${SDFGEN_VENV}/bin/pip" install --upgrade pip

if [ "${SDFGEN_FROM_SOURCE}" != "true" ]; then
  "${SDFGEN_VENV}/bin/pip" install "sdfgen==${SDFGEN_VER}"
else
  echo "Building sdfgen ${SDFGEN_VER} from source"
  ZIG_DIR=zig-${ZIG_PLATFORM}-${ZIG_VER}
  SDFGEN_BUILD_DIR=${PROVERS_DIR}/microkit_sdf_gen
  rm -rf "${PROVERS_DIR}/${ZIG_DIR}" "${SDFGEN_BUILD_DIR}"

  provers_fetch "https://ziglang.org/download/${ZIG_VER}/${ZIG_DIR}.tar.xz" \
    "${PROVERS_DIR}/${ZIG_DIR}.tar.xz"
  tar xf "${PROVERS_DIR}/${ZIG_DIR}.tar.xz"
  rm -f "${PROVERS_DIR}/${ZIG_DIR}.tar.xz"
  export PATH=${PROVERS_DIR}/${ZIG_DIR}:${PATH}

  git clone "${SDFGEN_REPO}" "${SDFGEN_BUILD_DIR}"
  cd "${SDFGEN_BUILD_DIR}"
  git checkout "${SDFGEN_VER}"
  "${SDFGEN_VENV}/bin/pip" install .

  cd "${PROVERS_DIR}"
  # zig and the checkout are build-time only; the wheel is installed in the venv
  rm -rf "${PROVERS_DIR}/${ZIG_DIR}" "${SDFGEN_BUILD_DIR}"
fi

# The released SDK, unmodified.  bin/microkit-vcpu-domain.sh then rebuilds its
# `microkit` tool in place with the seL4/microkit#586 fix; everything else stays
# as released.
echo "Installing the Microkit SDK ${MICROKIT_SDK_VER}"
rm -rf "${MICROKIT_SDK}"
MICROKIT_SDK_TAR=microkit-sdk-${MICROKIT_SDK_VER}-${MICROKIT_SDK_OS}-${MICROKIT_SDK_ARCH}.tar.gz
provers_fetch "https://github.com/seL4/microkit/releases/download/${MICROKIT_SDK_VER}/${MICROKIT_SDK_TAR}" \
  "${PROVERS_DIR}/${MICROKIT_SDK_TAR}"
tar xf "${PROVERS_DIR}/${MICROKIT_SDK_TAR}"
rm -f "${PROVERS_DIR}/${MICROKIT_SDK_TAR}"

echo "Cloning LionsOS ${LIONSOS_VER}"
rm -rf "${LIONSOS}"

# Only dep/sddf and dep/libvmm are initialised.  Generated Microkit makefiles use
# sDDF, and the VM examples use libvmm; the rest of what LionsOS vendors --
# micropython, musllibc, libnfs, microdot, libmicrokitco, wasm-micro-runtime --
# is part of LionsOS the operating system, which nothing here builds.
#
# It also avoids a real failure mode: 'git clone --recurse-submodules' treats any
# submodule failure as fatal and deletes the whole clone, and micropython's
# nested lib/fsp does fail.  Naming the two we need means an unrelated submodule
# cannot take a build down an hour in.
git clone "${LIONSOS_REPO}" "${LIONSOS}"
# -c advice.detachedHead=false: the pin is a commit, so this is always a
# detached checkout and git's 14 lines of advice about it are just noise.
git -C "${LIONSOS}" -c advice.detachedHead=false checkout "${LIONSOS_VER}"
git -C "${LIONSOS}" submodule update --init --recursive dep/sddf dep/libvmm
