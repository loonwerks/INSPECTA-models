#!/bin/bash -e
# Install the Microkit SDK ${MICROKIT_SDK_VER}, the sdfgen ${SDFGEN_VER} python
# package (into its own venv) and LionsOS.
#
# sdfgen comes from PyPI on x86_64; there is no aarch64 wheel, so on that
# architecture it is built from source, which needs zig ${ZIG_VER}.
#
# Unlike the Dockerfile this keeps the LionsOS examples, its .git directory and
# the micropython/wasm-micro-runtime deps -- those are stripped from the image
# purely to keep it small, and they are handy in a development VM.
set -Eeuxo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/env.sh"

mkdir -p "${PROVERS_DIR}"
cd "${PROVERS_DIR}"

echo "Creating the sdfgen venv"
rm -rf "${SDFGEN_VENV}"
python3.12 -m venv "${SDFGEN_VENV}"
"${SDFGEN_VENV}/bin/pip" install --upgrade pip

if [ "${SDFGEN_FROM_SOURCE}" != "true" ]; then
  "${SDFGEN_VENV}/bin/pip" install "sdfgen==${SDFGEN_VER}"
else
  echo "Building sdfgen ${SDFGEN_VER} from source"
  ZIG_DIR=zig-${PROVERS_ARCH}-linux-${ZIG_VER}
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

echo "Installing the Microkit SDK ${MICROKIT_SDK_VER}"
rm -rf "${MICROKIT_SDK_CURRENT}"
MICROKIT_SDK_TAR=microkit-sdk-${MICROKIT_SDK_VER}-linux-${MICROKIT_SDK_ARCH}.tar.gz
provers_fetch "https://github.com/seL4/microkit/releases/download/${MICROKIT_SDK_VER}/${MICROKIT_SDK_TAR}" \
  "${PROVERS_DIR}/${MICROKIT_SDK_TAR}"
tar xf "${PROVERS_DIR}/${MICROKIT_SDK_TAR}"
rm -f "${PROVERS_DIR}/${MICROKIT_SDK_TAR}"

echo "Cloning LionsOS"
rm -rf "${LIONSOS}"
git clone --rec --depth=1 --shallow-submodules https://github.com/au-ts/lionsos.git "${LIONSOS}"
