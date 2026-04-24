#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

VERSION=$(date +%Y.%m.%d)

IMAGE="jasonbelt/microkit_provers"

# Build locally (no push) in parallel
docker buildx build --platform linux/arm64 \
    -f ${SCRIPT_DIR}/Dockerfile.provers_LinuxARM64 \
    -t ${IMAGE}:arm64_$VERSION \
    --load . &
PID_ARM=$!

docker buildx build --platform linux/amd64 \
  -f ${SCRIPT_DIR}/Dockerfile.provers_LinuxAMD64 \
  -t ${IMAGE}:amd64_$VERSION \
  --load . &
PID_AMD=$!

FAILED=0
wait $PID_ARM || { echo "ERROR: ARM64 build failed."; FAILED=1; }
wait $PID_AMD || { echo "ERROR: AMD64 build failed."; FAILED=1; }
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
