#!/bin/bash -e
# Install the apt dependencies of the PROVERS environment.
#
# PROVERS_DEPS_PROFILE selects how much:
#
#   vm       everything needed to build the tools, plus the GUI libraries the
#            IDEs need and the en_US.UTF-8 locale  (default)
#   builder  the build set alone -- what a container's builder stage needs
#   runtime  only what the installed tools need to run, for a container's final
#            stage, where the build-only packages would just be dead weight
set -Eeuxo pipefail

_DEPS_BIN_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "${_DEPS_BIN_DIR}/env.sh"

export DEBIAN_FRONTEND=noninteractive

: "${PROVERS_DEPS_PROFILE:=vm}"
case "${PROVERS_DEPS_PROFILE}" in
  vm | builder | runtime) ;;
  *) echo "deps.sh: unknown PROVERS_DEPS_PROFILE '${PROVERS_DEPS_PROFILE}'" >&2; exit 1 ;;
esac

# honour PROVERS_APT_MIRROR before fetching anything (a no-op when unset)
bash "${_DEPS_BIN_DIR}/apt-mirror.sh"

# A dropped connection to the archive part-way through a download aborts the
# whole provision, which is expensive this early in a multi-hour build.  Retry
# instead: this covers a blipping mirror, though not one that is fully
# unreachable.
echo 'Acquire::Retries "3";' | sudo tee /etc/apt/apt.conf.d/80-provers-retries > /dev/null

# Needed to run what is installed, on every profile.
PKGS=(
  binutils build-essential
  clang llvm lld device-tree-compiler
  libxml2-utils
  curl wget git
  python3.12 python3-pip python3.12-venv
  qemu-system-arm qemu-system-misc
  # Sireum's own bootstrap (bin/init.sh) needs unzip or 7z to unpack what it
  # downloads, so this belongs in every profile: without it a container cannot
  # re-run 'sireum --init', let alone a full Sireum install.
  unzip
)
# Needed only while building the tools.
if [ "${PROVERS_DEPS_PROFILE}" != "runtime" ]; then
  PKGS+=(
    software-properties-common
    gcc-riscv64-unknown-elf
    cmake pandoc ninja-build
    # Sireum's Init.deps() -- which 'sireum setup ive' and 'setup vscode' both
    # run -- builds verilator from source for Anvil.  Without these it fails
    # with 'Cannot run program "autoconf"' from inside a shutdown hook, so the
    # install still exits 0 and the failure is easy to miss.
    autoconf flex bison help2man
    texlive-latex-base texlive-latex-recommended
    texlive-fonts-recommended texlive-fonts-extra
  )
fi

sudo apt-get update
sudo apt-get install -y "${PKGS[@]}"

if [ "${PROVERS_DEPS_PROFILE}" = "vm" ]; then
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


echo "Configuring the en_US.UTF-8 locale"
sudo locale-gen en_US.UTF-8
sudo update-locale LANG=en_US.UTF-8
fi

sudo rm -rf /var/lib/apt/lists/*
