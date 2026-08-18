#!/bin/bash -e
# Point apt at an alternative Ubuntu mirror.
#
#   PROVERS_APT_MIRROR           base URL of the mirror, e.g.
#                                https://mirrors.kernel.org/ubuntu/
#   PROVERS_APT_SECURITY_MIRROR  mirror for the -security suite
#                                (defaults to PROVERS_APT_MIRROR; full Ubuntu
#                                mirrors carry -security as well)
#
# A no-op when PROVERS_APT_MIRROR is unset, so the default Canonical archives
# are used unless you ask for something else.  Useful when archive.ubuntu.com is
# unreachable or slow, or when a site mirror is simply closer.
#
# Ubuntu 24.04 uses the deb822 format in /etc/apt/sources.list.d/ubuntu.sources;
# the legacy one-line /etc/apt/sources.list is handled too, for older guests and
# for bare-metal installs that still use it.
#
# Safe to re-run: the original sources are backed up once, and each run rewrites
# the URIs from that pristine copy rather than compounding earlier edits.
set -Eeuo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/env.sh"

if [ "${PROVERS_OS}" != "linux" ]; then
  echo "apt-mirror.sh: there is no apt on ${PROVERS_OS}; nothing to do."
  exit 0
fi

MIRROR="${PROVERS_APT_MIRROR:-}"
if [ -z "${MIRROR}" ]; then
  echo "PROVERS_APT_MIRROR is not set; leaving the apt sources unchanged."
  exit 0
fi

# normalise to exactly one trailing slash
MIRROR="${MIRROR%/}/"
SECURITY="${PROVERS_APT_SECURITY_MIRROR:-${MIRROR}}"
SECURITY="${SECURITY%/}/"

CODENAME="$(. /etc/os-release && echo "${VERSION_CODENAME}")"

# Fail here with a clear message rather than 200 lines into an apt-get install.
# -L matters: mirrors.kernel.org and friends answer 301 and redirect to an edge
# host, and without following that this would pass without fetching anything.
for probe in "${MIRROR}dists/${CODENAME}/Release" \
             "${SECURITY}dists/${CODENAME}-security/Release"; do
  if ! curl -fsSL --max-time 20 -o /dev/null "${probe}"; then
    echo "ERROR: cannot fetch ${probe}" >&2
    echo "       Check PROVERS_APT_MIRROR / PROVERS_APT_SECURITY_MIRROR; the" >&2
    echo "       mirror must carry ${CODENAME} and ${CODENAME}-security." >&2
    exit 1
  fi
done

DEB822=/etc/apt/sources.list.d/ubuntu.sources
LEGACY=/etc/apt/sources.list

if [ -f "${DEB822}" ]; then
  [ -f "${DEB822}.provers-orig" ] || sudo cp -a "${DEB822}" "${DEB822}.provers-orig"

  # Rewrite each stanza's URIs, sending the one whose Suites name a -security
  # pocket to the security mirror and everything else to the main mirror.
  sudo awk -v main="${MIRROR}" -v sec="${SECURITY}" '
    BEGIN { RS = ""; ORS = "" }
    {
      is_sec = ($0 ~ /Suites:[^\n]*-security/)
      n = split($0, line, "\n")
      for (i = 1; i <= n; i++) {
        if (line[i] ~ /^[[:space:]]*URIs:/) printf "URIs: %s\n", (is_sec ? sec : main)
        else if (line[i] != "") printf "%s\n", line[i]
      }
      printf "\n"
    }
  ' "${DEB822}.provers-orig" | sudo tee "${DEB822}" > /dev/null
fi

if [ -f "${LEGACY}" ] && grep -qE '^\s*deb(-src)?\s' "${LEGACY}"; then
  [ -f "${LEGACY}.provers-orig" ] || sudo cp -a "${LEGACY}" "${LEGACY}.provers-orig"
  sudo sed -E \
    -e "s#https?://[a-z0-9.-]*security\.ubuntu\.com/ubuntu/?#${SECURITY}#g" \
    -e "s#https?://[a-z0-9.-]*archive\.ubuntu\.com/ubuntu/?#${MIRROR}#g" \
    -e "s#https?://[a-z0-9.-]*\.clouds\.archive\.ubuntu\.com/ubuntu/?#${MIRROR}#g" \
    "${LEGACY}.provers-orig" | sudo tee "${LEGACY}" > /dev/null
fi

echo "apt now points at:"
echo "  archive   ${MIRROR}"
echo "  security  ${SECURITY}"

sudo apt-get update
