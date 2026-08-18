#!/bin/bash -e
# Install Firefox from Mozilla's own apt repository.
#
# Ubuntu 24.04's 'firefox' package is a transitional stub whose version is
# 1:1snap1-... -- installing it pulls in a snap rather than a browser.  Snaps
# are a poor fit for an appliance image: a much larger download, a noticeably
# slow first launch inside a VM, snapd machinery in the image, and background
# refreshes over the network.  Mozilla publish real .debs, so use those.
#
# Safe to re-run.
set -Eeuo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/env.sh"

if [ "${PROVERS_OS}" != "linux" ]; then
  echo "firefox.sh: this installs a .deb from Mozilla's apt repository, which"
  echo "            ${PROVERS_OS} has no use for; nothing to do."
  exit 0
fi

export DEBIAN_FRONTEND=noninteractive

KEYRING=/etc/apt/keyrings/packages.mozilla.org.asc
# The repo is hosted on Google Artifact Registry, so the key's uid reads
# "Artifact Registry Repository Signer" rather than anything Mozilla-branded.
EXPECTED_FPR=35BAA0B33E9EB396F59CA838C0BA5CE6DC6315A3

sudo install -d -m 0755 /etc/apt/keyrings
# The published key is ASCII-armoured, hence the .asc name: apt requires the
# extension to match the encoding.
sudo wget -qO "${KEYRING}" https://packages.mozilla.org/apt/repo-signing-key.gpg

FPR="$(gpg --show-keys --with-colons "${KEYRING}" 2>/dev/null | awk -F: '/^fpr:/ { print $10; exit }')"
if [ "${FPR}" != "${EXPECTED_FPR}" ]; then
  echo "ERROR: Mozilla signing key fingerprint is ${FPR}," >&2
  echo "       expected ${EXPECTED_FPR}.  Refusing to add the repository." >&2
  exit 1
fi

echo "deb [signed-by=${KEYRING}] https://packages.mozilla.org/apt mozilla main" \
  | sudo tee /etc/apt/sources.list.d/mozilla.list > /dev/null

# Required, not optional: Ubuntu's snap stub carries a higher version than
# Mozilla's real package, so without a pin apt prefers the stub and installs the
# snap anyway.  'Pin: origin <host>' matches the hostname a package came from --
# note that it does NOT match the Release file's Origin field, which for this
# repo is "namespaces/moz-fx-productdelivery-pr-38b5/repositories/mozilla".
sudo tee /etc/apt/preferences.d/mozilla > /dev/null << 'EOF'
Package: *
Pin: origin packages.mozilla.org
Pin-Priority: 1000
EOF

sudo apt-get update
sudo apt-get install -y firefox

# Confirm we got the real thing rather than the snap stub.
installed="$(apt-cache policy firefox | awk '/Installed:/ { print $2 }')"
case "${installed}" in
  *snap*)
    echo "ERROR: apt installed the snap stub (${installed}); the pin did not take." >&2
    exit 1
    ;;
esac

firefox --version
