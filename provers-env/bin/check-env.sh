#!/bin/bash -e
# Verify that a pre-set environment agrees with what versions.sh pins.
#
# A container declares its environment as ENV, so that it is there for
# `docker run <image> <cmd>`, which sources no shell startup file.  Docker
# interpolates those ENV lines itself, from build args, which makes them the one
# place a version can silently disagree with bin/versions.sh -- and the result
# would be an image whose MICROKIT_SDK points at a directory that is not there.
# Running this after the environment is set turns that into a failed build.
#
# Only the version-bearing path is checked, and only when PROVERS_DIR
# already agrees, i.e. when this really is the environment env.sh describes.  On
# a developer's machine, where MICROKIT_SDK may well point at a local build, it
# is a no-op.
set -Eeuo pipefail

given_provers_dir="${PROVERS_DIR-}"
given_sdk="${MICROKIT_SDK-}"

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/env.sh"

if [ -z "${given_provers_dir}" ] || [ "${given_provers_dir}" != "${PROVERS_DIR}" ]; then
  echo "check-env.sh: PROVERS_DIR is not the one env.sh describes; nothing to check"
  exit 0
fi

status=0
if [ -n "${given_sdk}" ] && [ "${given_sdk}" != "${MICROKIT_SDK}" ]; then
  echo "check-env.sh: MICROKIT_SDK is '${given_sdk}' but versions.sh pins '${MICROKIT_SDK}'" >&2
  status=1
fi

if [ ${status} -ne 0 ]; then
  echo "check-env.sh: pass MICROKIT_SDK_VER as --build-arg (docker.sh does)," >&2
  echo "              or update the ENV to match versions.sh" >&2
  exit 1
fi

echo "check-env.sh: MICROKIT_SDK agrees with bin/versions.sh"
