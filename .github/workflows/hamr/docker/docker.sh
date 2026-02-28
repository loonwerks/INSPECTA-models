#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

VERSION=$(date +%Y.%m.%d)

IMAGE="jasonbelt/microkit_provers"

# Start ARM64 build in background
docker buildx build --platform linux/arm64 \
    -f ${SCRIPT_DIR}/Dockerfile.provers_LinuxARM64 \
    -t ${IMAGE}:arm64_$VERSION \
    --push . &

# Start X64 build in background
docker buildx build --platform linux/amd64 \
  -f ${SCRIPT_DIR}/Dockerfile.provers_LinuxAMD64 \
  -t ${IMAGE}:amd64_$VERSION \
  --push . &

# Wait for both builds to finish
wait

echo "Both architecture-specific builds completed."

# Create multi-arch manifest
#docker manifest create jasonbelt/microkit_provers:$VERSION \
 # jasonbelt/microkit_provers:arm64_$VERSION \
  #jasonbelt/microkit_provers:amd64_$VERSION

# Annotate architectures
#docker manifest annotate jasonbelt/microkit_provers:$VERSION \
 # jasonbelt/microkit_provers:arm64_$VERSION --arch arm64
#docker manifest annotate jasonbelt/microkit_provers:$VERSION \
  #jasonbelt/microkit_provers:amd64_$VERSION --arch amd64

# Push the multi-arch manifest
#docker manifest push jasonbelt/microkit_provers:$VERSION

# Update 'latest' tag to point to this manifest
#docker tag jasonbelt/microkit_provers:$VERSION jasonbelt/microkit_provers:latest
#docker push jasonbelt/microkit_provers:latest


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