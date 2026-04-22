#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

if ! docker login --username jasonbelt 2>/dev/null; then
    echo "ERROR: Docker login failed. Please run 'docker login' first."
    exit 1
fi

VERSION=$(date +%Y.%m.%d)

IMAGE="jasonbelt/microkit_provers"

# Start ARM64 build in background
docker buildx build --platform linux/arm64 \
    -f ${SCRIPT_DIR}/Dockerfile.provers_LinuxARM64 \
    -t ${IMAGE}:arm64_$VERSION \
    --push . &
PID_ARM=$!

# Start X64 build in background
docker buildx build --platform linux/amd64 \
  -f ${SCRIPT_DIR}/Dockerfile.provers_LinuxAMD64 \
  -t ${IMAGE}:amd64_$VERSION \
  --push . &
PID_AMD=$!

# Wait for both builds and check exit codes
FAILED=0
wait $PID_ARM || { echo "ERROR: ARM64 build failed."; FAILED=1; }
wait $PID_AMD || { echo "ERROR: AMD64 build failed."; FAILED=1; }
if [ $FAILED -ne 0 ]; then
    exit 1
fi

echo "Both architecture-specific builds completed."

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