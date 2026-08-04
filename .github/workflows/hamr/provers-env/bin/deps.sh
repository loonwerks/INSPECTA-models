#!/bin/bash -e
# Install the build and runtime dependencies of the PROVERS environment.
# Union of the builder-stage and final-stage apt installs of
#   .github/workflows/hamr/docker/Dockerfile.provers_LinuxAMD64
# plus the GUI runtime libraries needed by IVE, CodeIVE and FMIDE.
set -Eeuxo pipefail

_DEPS_BIN_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "${_DEPS_BIN_DIR}/env.sh"

export DEBIAN_FRONTEND=noninteractive

# honour PROVERS_APT_MIRROR before fetching anything (a no-op when unset)
bash "${_DEPS_BIN_DIR}/apt-mirror.sh"

# A dropped connection to the archive part-way through a download aborts the
# whole provision, which is expensive this early in a multi-hour build.  Retry
# instead: this covers a blipping mirror, though not one that is fully
# unreachable.
echo 'Acquire::Retries "3";' | sudo tee /etc/apt/apt.conf.d/80-provers-retries > /dev/null

sudo apt-get update
sudo apt-get install -y \
  software-properties-common \
  binutils build-essential \
  clang llvm lld device-tree-compiler \
  libxml2-utils \
  curl wget git unzip \
  python3.12 python3-pip python3.12-venv \
  qemu-system-arm qemu-system-misc \
  gcc-riscv64-unknown-elf \
  cmake pandoc ninja-build \
  texlive-latex-base texlive-latex-recommended \
  texlive-fonts-recommended texlive-fonts-extra

# GTK/X11 libraries needed by the Eclipse-based FMIDE and the JetBrains- and
# VSCodium-based IDEs.  Ubuntu 24.04 renamed several of these as part of the
# 64-bit time_t transition, so only install the names this release actually has.
GUI_PKGS=(
  libgtk-3-0t64 libgtk-3-0
  libwebkit2gtk-4.1-0
  libasound2t64 libasound2
  libxtst6 libxi6 libxrender1 libxss1
  libfontconfig1 fonts-dejavu
  libgl1 libglu1-mesa
  at-spi2-core libsecret-1-0 libnss3
  xdg-utils
)
AVAILABLE_GUI_PKGS=()
for pkg in "${GUI_PKGS[@]}"; do
  # 'apt-cache show' is not a usable test here: it also succeeds for virtual
  # packages, and on noble the pre-t64 names (libasound2, libgtk-3-0) are
  # virtual, so installing one fails with "has no installation candidate".
  # 'Candidate:' distinguishes all three cases -- a version for an installable
  # package, "(none)" for a virtual one, and empty for a name that is unknown.
  candidate="$(apt-cache policy "${pkg}" 2>/dev/null | awk '/Candidate:/ { print $2; exit }' || true)"
  if [ -n "${candidate}" ] && [ "${candidate}" != "(none)" ]; then
    AVAILABLE_GUI_PKGS+=("${pkg}")
  fi
done
if [ ${#AVAILABLE_GUI_PKGS[@]} -gt 0 ]; then
  sudo apt-get install -y "${AVAILABLE_GUI_PKGS[@]}"
fi

sudo rm -rf /var/lib/apt/lists/*

echo "Configuring the en_US.UTF-8 locale"
sudo locale-gen en_US.UTF-8
sudo update-locale LANG=en_US.UTF-8
