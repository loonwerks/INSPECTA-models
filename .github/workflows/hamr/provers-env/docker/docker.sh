#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

CACHE_DIR=${SCRIPT_DIR}/.buildx-cache

# The image name and the tool versions come from the same file the VM and the
# bare-metal setup use, so the image and the VM install the same things.
# Override PROVERS_IMAGE to build and push somewhere else.
# shellcheck source=../bin/versions.sh
source "${SCRIPT_DIR}/../bin/versions.sh"

IMAGE=${PROVERS_IMAGE}

# The tag suffix, pinned in versions.sh rather than taken from today's clock, so
# that the container and the VM built from the same pins carry the same version
# -- and so that a rebuild meant to replace a published build tags over it rather
# than beside it.  PROVERS_BUILD_VER=$(date +%Y.%m.%d) for a new one.
VERSION=${PROVERS_BUILD_VER}

# Only the architecture-independent pins are forwarded.  versions.sh also derives
# per-architecture values, but from the *host's* uname, which would be wrong for
# whichever of the two images is cross-built; inside the image the same file is
# sourced again and derives them from the target's uname instead.
BUILD_ARGS=()
for v in MICROKIT_SDK_VER MICROKIT_DOMAINS_SDK_VER RUST_TOOLCHAIN_VER \
         RUST_NIGHTLY_VER SDFGEN_VER VERUS_VER Z3_VER LIONSOS_VER \
         SIREUM_V SIREUM_INIT_V PROVERS_BUILD_VER; do
  BUILD_ARGS+=(--build-arg "${v}=${!v}")
done

# One Dockerfile for both architectures -- the install scripts it runs resolve
# every per-architecture difference themselves.  The build context is provers-env,
# the parent of this directory, so that bin/ is reachable from it.
DOCKERFILE=${SCRIPT_DIR}/Dockerfile.provers
CONTEXT=${SCRIPT_DIR}/..

# Exporting the layer cache to disk is opt-in.  'mode=max' writes every layer of
# every stage on every build and nothing prunes it -- 58GB after a day of
# iterating here, enough to fill a disk and take the Docker daemon down with it.
# BuildKit's own cache already makes local rebuilds fast, so the export earns its
# keep in CI, where nothing survives between runs, and not on a workstation.
# Reading an existing export costs nothing, so --cache-from stays unconditional.
CACHE_ARGS=()
if [ -d "${CACHE_DIR}" ]; then
  CACHE_ARGS+=(--cache-from "type=local,src=${CACHE_DIR}/ARCH")
fi
if [ "${PROVERS_EXPORT_BUILD_CACHE:-false}" = "true" ]; then
  echo "Exporting the layer cache to ${CACHE_DIR} (PROVERS_EXPORT_BUILD_CACHE=true)"
  CACHE_ARGS+=(--cache-to "type=local,dest=${CACHE_DIR}/ARCH,mode=max")
fi

build_one() {
  local arch=$1
  local args=()
  local a
  for a in "${CACHE_ARGS[@]}"; do
    args+=("${a//ARCH/${arch}}")
  done
  docker buildx build --platform "linux/${arch}" \
    -f "${DOCKERFILE}" \
    -t "${IMAGE}:${arch}_${VERSION}" \
    "${BUILD_ARGS[@]}" \
    "${args[@]}" \
    --load "${CONTEXT}"
}

# Building both at once halves the wall clock but doubles the concurrent load on
# the same hosts -- and the two builds pull hundreds of megabytes from GitHub at
# roughly the same moments, which is a good way to get throttled into a 503 an
# hour into a build.  Set PROVERS_BUILD_SEQUENTIAL=true to build one at a time.
FAILED=0
if [ "${PROVERS_BUILD_SEQUENTIAL:-false}" = "true" ]; then
  echo "Building sequentially (PROVERS_BUILD_SEQUENTIAL=true)"
  build_one arm64 || { echo "ERROR: ARM64 build failed."; FAILED=1; }
  build_one amd64 || { echo "ERROR: AMD64 build failed."; FAILED=1; }
else
  build_one arm64 & PID_ARM=$!
  build_one amd64 & PID_AMD=$!
  wait $PID_ARM || { echo "ERROR: ARM64 build failed."; FAILED=1; }
  wait $PID_AMD || { echo "ERROR: AMD64 build failed."; FAILED=1; }
fi
if [ $FAILED -ne 0 ]; then
    exit 1
fi

echo "Both architecture-specific builds completed successfully."
read -p "Push images to Docker Hub? [y/N] " answer
if [[ ! "$answer" =~ ^[Yy]$ ]]; then
    echo "Skipping push."
    exit 0
fi

if ! grep -qs "index.docker.io" "$HOME/.docker/config.json"; then
    echo "ERROR: Not logged in to Docker Hub. Please run 'docker login' first."
    exit 1
fi

docker push "${IMAGE}:arm64_${VERSION}"
docker push "${IMAGE}:amd64_${VERSION}"

# Create multi-arch manifest for VERSION
docker buildx imagetools create \
  -t "${IMAGE}:${VERSION}" \
  "${IMAGE}:arm64_${VERSION}" \
  "${IMAGE}:amd64_${VERSION}"

# Point latest at the same manifest
docker buildx imagetools create \
  -t "${IMAGE}:latest" \
  "${IMAGE}:${VERSION}"

echo "Multi-arch manifest created and 'latest' updated."
