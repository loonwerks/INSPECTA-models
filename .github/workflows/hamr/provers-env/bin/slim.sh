#!/bin/bash -e
# Drop everything under ${PROVERS_DIR} and ${HOME} that an installed environment
# does not need: build leftovers, download caches, and the parts of the Sireum
# distribution that only matter when rebuilding it.
#
# In a container this runs as the last builder-stage step.  Multi-stage keeps
# the builder's own layers out of the image, but the final stage copies ${HOME}
# wholesale, so anything left here does ship -- it just does not have to be
# deleted in the same layer that created it.
#
# In a VM this is the first half of prep-export.sh, which adds the free-space
# zero-fill that only makes sense before exporting a virtual disk.
#
# Safe to skip; nothing installed stops working without it.  It also leaves the
# Rust and JVM dependency caches alone unless asked -- see PROVERS_SLIM_CACHES.
set -Eeuxo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/env.sh"

# Sireum lays out aarch64 binaries under bin/linux/arm, x86_64 under bin/linux.
if [ "${PROVERS_ARCH}" = "aarch64" ]; then
  SIREUM_LINUX_BIN=${SIREUM_HOME}/bin/linux/arm
else
  SIREUM_LINUX_BIN=${SIREUM_HOME}/bin/linux
fi

echo "Removing build leftovers and download caches"
rm -rf \
  "${PROVERS_DIR}"/*.zip "${PROVERS_DIR}"/*.tar.gz "${PROVERS_DIR}"/*.tar.xz \
  "${PROVERS_DIR}/z3-build" "${PROVERS_DIR}/verus-build" \
  "${PROVERS_DIR}/microkit_sdf_gen" "${PROVERS_DIR}"/zig-* \
  "${PROVERS_DIR}/microkit-sdk-${MICROKIT_DOMAINS_SDK_VER}-build" \
  "${HOME}/Downloads/sireum"

# LionsOS is kept in full in a VM -- the examples and history are useful there
# -- but a shipped image only needs the sources the SDK builds against.
if [ "${PROVERS_SLIM_LIONSOS:-true}" = "true" ]; then
  echo "Trimming LionsOS"
  rm -rf \
    "${LIONSOS}/.git" "${LIONSOS}/examples" \
    "${LIONSOS}/dep/micropython" "${LIONSOS}/dep/wasm-micro-runtime"
fi

# The Sireum distribution ships a full JDK, including JavaFX, that only the IDEs
# use.  Where none is installed it is dead weight; where one is, this would break
# it, so it is skipped.
if [ "${PROVERS_IVE}" != "true" ] && [ "${PROVERS_CODEIVE}" != "true" ]; then
  echo "Trimming the Sireum distribution"
  # shellcheck disable=SC2086
  # Every platform's solvers go, not just the foreign ones.  Sireum's installers
  # fetch macOS, Windows and both Linux builds (~825MB) and only one is usable;
  # Sireum re-downloads what it needs on first use, which is how the image
  # behaved before these scripts existed.  PROVERS_SIREUM_SOLVERS=false stops
  # them being installed in the first place -- this is the belt-and-braces half,
  # for anything else that may have pulled them in.
  # bin/*/idea is the IntelliJ-based IVE, 3GB of it.  Nothing installs it now --
  # sireum.sh runs 'build.cmd jar' precisely to avoid the project() step that
  # used to drag it in -- so this is a backstop against a future path that does.
  rm -rf ${SIREUM_HOME}/bin/mac ${SIREUM_HOME}/bin/win \
    ${SIREUM_HOME}/bin/linux/idea ${SIREUM_HOME}/bin/linux/arm/idea \
    ${SIREUM_HOME}/bin/linux/{.,}z3* ${SIREUM_HOME}/bin/linux/{.,}cvc* \
    ${SIREUM_HOME}/bin/linux/arm/{.,}z3* ${SIREUM_HOME}/bin/linux/arm/{.,}cvc* \
    ${SIREUM_HOME}/bin/sbt ${SIREUM_LINUX_BIN}/{.,}cs* \
    ${SIREUM_LINUX_BIN}/{.,}checkstack* \
    ${SIREUM_HOME}/lib/jacoco* ${SIREUM_HOME}/lib/marytts_text2wav.jar \
    ${SIREUM_LINUX_BIN}/java/jmods \
    ${SIREUM_LINUX_BIN}/java/lib/src.zip \
    ${SIREUM_LINUX_BIN}/java/include \
    ${SIREUM_LINUX_BIN}/java/man \
    ${SIREUM_LINUX_BIN}/java/demo \
    ${SIREUM_LINUX_BIN}/java/lib/client \
    ${SIREUM_LINUX_BIN}/java/lib/libjfxwebkit.so \
    ${SIREUM_LINUX_BIN}/java/lib/libavplugin-* \
    ${SIREUM_LINUX_BIN}/java/lib/libjavafx_* \
    ${SIREUM_LINUX_BIN}/java/lib/libglass* \
    ${SIREUM_LINUX_BIN}/java/lib/libgstreamer-lite.so \
    ${SIREUM_LINUX_BIN}/java/lib/libprism_* \
    ${SIREUM_LINUX_BIN}/java/lib/libdecora_sse.so \
    ${SIREUM_LINUX_BIN}/java/lib/libfxplugins.so \
    ${SIREUM_LINUX_BIN}/java/lib/javafx-swt.jar \
    ${SIREUM_LINUX_BIN}/java/lib/javafx.properties \
    ${SIREUM_LINUX_BIN}/java/lib/libawt_xawt.so \
    ${SIREUM_LINUX_BIN}/java/lib/libsplashscreen.so
fi

# A source-built Sireum leaves its history and its build output behind.  Both are
# large, neither is needed to run anything, and re-obtaining them costs only time
# -- unlike the dependency caches below.
# Build output is always safe to drop: the next build recreates it, and unlike
# the dependency caches nothing has to be re-downloaded to do so.
rm -rf "${SIREUM_HOME}/out"

if [ -d "${SIREUM_HOME}/.git" ]; then
  echo "Trimming the Sireum checkout"
  # submodules keep a .git file pointing into .git/modules, so both go together
  find "${SIREUM_HOME}" -name .git -maxdepth 4 -exec rm -rf {} + 2>/dev/null || true
fi

# The dependency caches are deliberately NOT removed by default.  They are what
# lets an installed environment resolve Rust crates and JVM artifacts without
# reaching crates.io or Maven Central -- which matters behind a corporate proxy,
# where the bundled JDK's own trust store is the usual thing that breaks.  Drop
# them only where the network is known to be open and the size matters more.
if [ "${PROVERS_SLIM_CACHES:-false}" = "true" ]; then
  echo "Removing the dependency caches (crates.io and Maven artifacts will be refetched)"
  # Sireum's coursier cache is ${SIREUM_HOME}/lib/cache, not ~/.cache/coursier:
  # Coursier.defaultCacheDir falls back to it when COURSIER_CACHE is unset, which
  # it is here.  The others are listed because other tooling may create them.
  rm -rf \
    "${SIREUM_HOME}/lib/cache" \
    "${HOME}/.cargo/registry" \
    "${HOME}/.cache/coursier" "${HOME}/.ivy2" "${HOME}/.sbt"
fi

set +x
echo ""
echo "Slimmed.  ${PROVERS_DIR} is now:"
du -sh "${PROVERS_DIR}" 2>/dev/null || true
