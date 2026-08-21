#!/bin/bash -e
# Install the OS package dependencies of the PROVERS environment: apt packages on
# Ubuntu, Homebrew formulae on macOS.
#
# PROVERS_DEPS_PROFILE selects how much:
#
#   vm       everything needed to build the tools, plus (on Ubuntu) the GUI
#            libraries the IDEs need and the en_US.UTF-8 locale  (default)
#   builder  the build set alone -- what a container's builder stage needs
#   runtime  only what the installed tools need to run, for a container's final
#            stage, where the build-only packages would just be dead weight
#
# On macOS 'vm' and 'builder' are the same thing: the IDEs are .app bundles that
# carry their own frameworks, so there is no GUI package set to install, and the
# locale is already UTF-8.
set -Eeuo pipefail

_DEPS_BIN_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "${_DEPS_BIN_DIR}/env.sh"

: "${PROVERS_DEPS_PROFILE:=vm}"
case "${PROVERS_DEPS_PROFILE}" in
  vm | builder | runtime) ;;
  *) echo "deps.sh: unknown PROVERS_DEPS_PROFILE '${PROVERS_DEPS_PROFILE}'" >&2; exit 1 ;;
esac

# ---------------------------------------------------------------------------
# macOS -- everything comes from Homebrew
# ---------------------------------------------------------------------------
if [ "${PROVERS_OS}" = "darwin" ]; then
  # The Command Line Tools supply git, make, the system headers and the linker
  # that Homebrew itself builds against, so nothing works without them.
  if ! xcode-select -p > /dev/null 2>&1; then
    set +x
    echo "deps.sh: the Xcode Command Line Tools are not installed.  Run" >&2
    echo "           xcode-select --install" >&2
    echo "         and re-run this script once it finishes." >&2
    exit 1
  fi

  # Deliberately not installed for you: Homebrew's installer wants sudo and
  # makes lasting changes to the machine, which is the user's call, not ours.
  # HOMEBREW_PREFIX is resolved by env.sh, which probes /opt/homebrew (Apple
  # Silicon) and /usr/local (Intel) -- so an unset one here means no brew.
  if [ -z "${HOMEBREW_PREFIX:-}" ] || [ ! -x "${HOMEBREW_PREFIX}/bin/brew" ]; then
    set +x
    echo "deps.sh: Homebrew is not installed.  Install it from https://brew.sh" >&2
    echo "         and re-run this script." >&2
    exit 1
  fi
  # Puts brew's own bin on PATH and sets the variables it expects, so that this
  # works from a shell that has never sourced Homebrew's shellenv.
  eval "$("${HOMEBREW_PREFIX}/bin/brew" shellenv)"

  # Needed to run what is installed, on every profile.  The names here are the
  # ones the generated Microkit makefiles invoke:
  #   llvm  clang, llvm-ar, llvm-ranlib, llvm-objcopy
  #   lld   ld.lld
  #   dtc   dtc
  #   make  GNU make 4.x; macOS ships 3.81, which sDDF's makefiles are not
  #         written for.  Homebrew installs it as gmake and also as `make` in
  #         ${HOMEBREW_PREFIX}/opt/make/libexec/gnubin, which bin/env.sh puts on
  #         PATH ahead of /usr/bin -- along with llvm's keg-only bin directory.
  # macOS already provides unzip, xmllint and a C compiler, so unlike the apt
  # side there is nothing here standing in for those.
  BREW_PKGS=(
    llvm lld
    dtc
    make
    wget git
    python@3.12
    qemu
  )
  # Needed only while building the tools.
  if [ "${PROVERS_DEPS_PROFILE}" != "runtime" ]; then
    BREW_PKGS+=(
      cmake
      # Sireum's Init.deps() -- which 'sireum setup ive' and 'setup vscode' both
      # run -- runs autoconf and ./configure in a verilator checkout to get its
      # headers, and then downloads a prebuilt verilator rather than making one.
      # Without these it reports "Cannot install Verilator" from inside a
      # shutdown hook, so the install still exits 0 and the failure is easy to
      # miss.  Nothing runs verilator's own build, so the rest of the GNU chain
      # -- flex, bison, help2man -- is not needed on either host.
      autoconf automake
    )
  fi

  # Not --force-bottle or --overwrite: a formula already installed at another
  # version is left as it is rather than being moved under the user's feet.
  # 'brew install' on an installed formula is a no-op, which is what makes this
  # script re-runnable.
  brew install "${BREW_PKGS[@]}"

  # TeX Live is not installed, and neither host installs it any more: it was
  # needed to build the Microkit SDK from source, which built the SDK manual,
  # and only the `microkit` tool is built now.  On a Mac it is the ~6GB mactex
  # cask, so it is left to whoever does want it:
  #   brew install --cask mactex-no-gui

  set +x
  # The acceptance test for this script: every tool the generated makefiles name
  # resolves, and `make` is the GNU 4.x one rather than the 3.81 in /usr/bin.
  # Checked here because each of these otherwise surfaces much later, as a
  # missing-command error part-way through someone's first build.
  echo ""
  echo "Checking the toolchain the generated Microkit makefiles expect"
  _missing=()
  for _tool in clang ld.lld llvm-ar llvm-ranlib llvm-objcopy dtc make \
               qemu-system-aarch64 "${PROVERS_PYTHON}" git wget; do
    if _path="$(command -v "${_tool}" 2>/dev/null)"; then
      printf '  %-20s %s\n' "${_tool}" "${_path}"
    else
      _missing+=("${_tool}")
    fi
  done
  if [ ${#_missing[@]} -gt 0 ]; then
    echo "deps.sh: not on PATH after installing: ${_missing[*]}" >&2
    echo "         Open a new shell, or source bin/env.sh, and check again." >&2
    exit 1
  fi
  _make_major="$(make --version | sed -n '1s/^GNU Make \([0-9]*\).*/\1/p')"
  if [ -z "${_make_major}" ] || [ "${_make_major}" -lt 4 ]; then
    echo "deps.sh: '$(command -v make)' is $(make --version | head -1)." >&2
    echo "         GNU Make 4 or newer is required; bin/env.sh puts Homebrew's" >&2
    echo "         on PATH, so source it and check again." >&2
    exit 1
  fi
  echo ""
  echo "macOS dependencies installed."
  exit 0
fi

# ---------------------------------------------------------------------------
# Ubuntu -- apt
# ---------------------------------------------------------------------------

export DEBIAN_FRONTEND=noninteractive

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
  python3.12 python3.12-venv
  # qemu-system-arm is what supplies qemu-system-aarch64, which the generated
  # makefiles name; the other architectures are not built for.
  qemu-system-arm
  # Sireum's own bootstrap (bin/init.sh) needs unzip or 7z to unpack what it
  # downloads, so this belongs in every profile: without it a container cannot
  # re-run 'sireum --init', let alone a full Sireum install.
  unzip
)
# Needed only while building the tools.
if [ "${PROVERS_DEPS_PROFILE}" != "runtime" ]; then
  PKGS+=(
    cmake
    # Sireum's Init.deps() -- which 'sireum setup ive' and 'setup vscode' both
    # run -- runs autoconf and ./configure in a verilator checkout to get its
    # headers, and then downloads a prebuilt verilator rather than making one.
    # Without this it fails with 'Cannot run program "autoconf"' from inside a
    # shutdown hook, so the install still exits 0 and the failure is easy to
    # miss.
    autoconf
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
