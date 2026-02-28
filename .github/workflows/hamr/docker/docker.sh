#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

VERSION=$(date +%Y.%m.%d)

# Start ARM64 build in background
docker buildx build --platform linux/arm64 \
    -f ${SCRIPT_DIR}/Dockerfile.lionsOS_LinuxARM64 \
    -t jasonbelt/microkit_lionsos:arm64_$VERSION . \
    &
    #--push &

# Start X64 build in background
#docker buildx build --platform linux/amd64 \
  #-f Dockerfile.lionsOS_LinuxX64 \
  #-t jasonbelt/microkit_lionsos:amd64_$VERSION . \
  #--push &

# Wait for both builds to finish
wait

exit

echo "Both architecture-specific builds completed."

# Create multi-arch manifest
docker manifest create jasonbelt/microkit_lionsos:$VERSION \
  jasonbelt/microkit_lionsos:arm64_$VERSION \
  jasonbelt/microkit_lionsos:amd64_$VERSION

# Annotate architectures
docker manifest annotate jasonbelt/microkit_lionsos:$VERSION \
  jasonbelt/microkit_lionsos:arm64_$VERSION --arch arm64
docker manifest annotate jasonbelt/microkit_lionsos:$VERSION \
  jasonbelt/microkit_lionsos:amd64_$VERSION --arch amd64

# Push the multi-arch manifest
docker manifest push jasonbelt/microkit_lionsos:$VERSION

# Update 'latest' tag to point to this manifest
docker tag jasonbelt/microkit_lionsos:$VERSION jasonbelt/microkit_lionsos:latest
docker push jasonbelt/microkit_lionsos:latest

echo "Multi-arch manifest created and 'latest' updated."