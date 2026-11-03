#!/bin/bash
# Install the DARPA PROVERS development environment onto this machine, by running
# the scripts in bin/ in order.
#
# Requirement: either
#   * Ubuntu 24.04, x86_64 or aarch64, with sudo available to the invoking user
#   * macOS on Apple Silicon, with the Xcode Command Line Tools and Homebrew
#
# One script serves both.  Nothing here branches on the host: bin/versions.sh
# derives what differs (package manager, release assets, toolchain triples) from
# uname, and each step in bin/ acts on that.  See ../readme.md.
#
# Nothing here knows about Vagrant or Docker; it is run directly on a dedicated
# machine, and vagrant/Vagrantfile also copies it into the VM and runs it there.
# docker/Dockerfile.provers does not use it -- it runs the same bin/ scripts as
# separate steps, so that each is a cacheable layer.
#
# Everything is installed under ${PROVERS_DIR} (default ~/provers).  Individual
# steps can be re-run later from bin/ -- ~/bin in the VM (see vagrant/readme.md).
#
# One exception to "everything": export SIREUM_HOME at an existing Sireum and
# that install is adopted rather than replaced -- see bin/sireum.sh.
#
# Outside ${PROVERS_DIR} this touches exactly one file, the shell startup file,
# to source bin/env.sh and bin/functions.sh from it.  PROVERS_SHELL_ALIASES=true
# adds the image builds' shell aliases as well; it is off by default, because a
# machine that is already somebody's own does not want them.

set -Eeuo pipefail

# Captured before env.sh defaults them, so that the startup-file block at the end
# can tell what the caller chose from what env.sh worked out.
_GIVEN_PROVERS_DIR="${PROVERS_DIR-}"
_GIVEN_SIREUM_HOME="${SIREUM_HOME-}"

# bin/ sits next to this script in the repository, but the VM's file provisioner
# lands the two separately, as ~/provers-setup.sh and ~/bin.
BIN_DIR="${BIN_DIR:-$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/bin}"
if [ ! -d "${BIN_DIR}" ]; then
  BIN_DIR="${HOME}/bin"
fi

# shellcheck source=bin/env.sh
source "${BIN_DIR}/env.sh"

mkdir -p "${PROVERS_DIR}"

# Reclaim any unallocated space in root's LVM volume group before installing into
# it.  This is aimed at the Vagrant base box, which formats only about half its
# disk, but it is guarded rather than conditional: on a machine without LVM, on
# one with no free extents, or on macOS, it does nothing.
bash "${BIN_DIR}/disk.sh"

bash "${BIN_DIR}/deps.sh"
bash "${BIN_DIR}/rust.sh"
# no-op where the Verus release bundles a Z3; verus.sh links against it where not
bash "${BIN_DIR}/z3.sh"
bash "${BIN_DIR}/verus.sh"
bash "${BIN_DIR}/microkit-lionsos.sh"
bash "${BIN_DIR}/sireum.sh"

# The IDEs are independent of each other and of the command-line tools, and each
# is opt-in on its own (bin/env.sh defaults all three to false; a VM turns them
# on).  ive.sh and codeive.sh both pull in Init.deps(); see sireum.sh for what
# happens when neither is selected.
if [ "${PROVERS_IVE}" = "true" ]; then
  bash "${BIN_DIR}/ive.sh"
fi
if [ "${PROVERS_CODEIVE}" = "true" ]; then
  bash "${BIN_DIR}/codeive.sh"
fi
if [ "${PROVERS_FMIDE}" = "true" ]; then
  bash "${BIN_DIR}/fmide.sh"
fi

# Last, so that it records what actually got installed
bash "${BIN_DIR}/build-info.sh"

# Which startup file gets the environment.  It has to be one that every shell the
# user's tools start actually reads, which is not the same file everywhere:
#
#   zsh       ~/.zshrc, read by every interactive shell, login or not.  This is
#             the macOS default since Catalina.
#   bash      ~/.bashrc, which an interactive non-login shell reads directly.
#             A login bash reads only the first of ~/.bash_profile,
#             ~/.bash_login and ~/.profile, so that file must source ~/.bashrc
#             (Ubuntu's own ~/.profile does; see below for macOS).
#
# Writing ~/.bash_profile instead on macOS, because Terminal.app and iTerm start
# bash as a *login* shell, is not enough: the shells an IDE terminal, tmux or a
# coding agent starts are non-login ones, which never read it, and quietly get no
# environment -- or a stale one, when the dotfiles are shared with another
# machine whose ~/.bashrc carries that machine's block.
case "${SHELL##*/}" in
  zsh) PROVERS_RC="${HOME}/.zshrc" ;;
  *)   PROVERS_RC="${HOME}/.bashrc" ;;
esac

# A login bash must reach ~/.bashrc.  On macOS nothing arranges that by default,
# so the login file bash would read is given the usual line, unless it already
# mentions ~/.bashrc.  A new ~/.bash_profile is created only when there is no
# login file at all: creating one beside an existing ~/.profile would stop bash
# from reading that ~/.profile.
if [ "${PROVERS_RC}" = "${HOME}/.bashrc" ]; then
  PROVERS_LOGIN_RC="${HOME}/.bash_profile"
  for f in "${HOME}/.bash_profile" "${HOME}/.bash_login" "${HOME}/.profile"; do
    if [ -e "${f}" ]; then PROVERS_LOGIN_RC="${f}"; break; fi
  done
  if ! grep -q '\.bashrc' "${PROVERS_LOGIN_RC}" 2>/dev/null; then
    echo "Making ${PROVERS_LOGIN_RC} read ~/.bashrc, so login shells get the PROVERS environment too"
    {
      echo ""
      echo "# provers-env: login shells read ~/.bashrc too"
      echo '[ -f "$HOME/.bashrc" ] && . "$HOME/.bashrc"'
    } >> "${PROVERS_LOGIN_RC}"
  fi
fi

# A path under the home directory is written as $HOME/..., so that the block still
# works when the startup file is shared with a machine whose home is elsewhere
# (/home/<user> on Linux, /Users/<user> on macOS).  Quoted, for spaces.
home_rel() {
  case "$1" in
    "${HOME}"/*) printf '"$HOME/%s"' "${1#"${HOME}"/}" ;;
    *)           printf '"%s"' "$1" ;;
  esac
}

# Every line is guarded on the thing it refers to still being there.  A startup
# file outlives the install: the checkout gets moved or deleted, and an
# unguarded 'source' of a path that has gone then greets every new terminal with
# an error, in a file most people do not think to look in.  Guarded, it simply
# does nothing.
#
# SIREUM_HOME is recorded only when this run installed Sireum itself, i.e. when
# the caller did NOT point it at an existing install.
#
# When the caller did set it, their own environment is where it comes from and a
# second copy here would only be one more thing to keep in step.
#
# When they did not, it must be written out, and it is not enough to leave
# env.sh to derive it.  env.sh honours a SIREUM_HOME that is already set, and
# this block is appended to the *end* of the startup file -- so on a machine
# whose startup file already exports a SIREUM_HOME of its own further up (a
# developer machine with its own kekinian checkout, say), that earlier value
# wins and the Sireum this run just built is never reached.  Writing it here,
# after that line and before env.sh is sourced, is what makes the freshly
# installed one the one a new shell uses; env.sh then puts its bin/ on PATH.
echo "Wiring the PROVERS environment into ${PROVERS_RC}"
if grep -q "^# provers-env$" "${PROVERS_RC}" 2>/dev/null; then
  # Left as it is: the block may have been edited by hand.  But a block whose
  # PROVERS_BIN_DIR is not this checkout's -- written on another machine sharing
  # these dotfiles, or before the checkout moved -- would otherwise be skipped in
  # silence and leave the shell without this environment, so it is reported.
  _line=$(grep -n "export PROVERS_BIN_DIR=" "${PROVERS_RC}" | head -1)
  _dir=$(printf '%s' "${_line#*export PROVERS_BIN_DIR=}" | tr -d '"')
  _dir=${_dir//\$HOME/${HOME}}
  if [ -z "${_line}" ] || [ ! -d "${_dir}" ] || [ "$(cd "${_dir}" && pwd -P)" != "$(cd "${BIN_DIR}" && pwd -P)" ]; then
    echo "WARNING: ${PROVERS_RC} already has a provers-env block, but its PROVERS_BIN_DIR"
    echo "         (line ${_line%%:*}: ${_dir:-none}) is not ${BIN_DIR}."
    echo "         Change that line to:"
    echo "           [ -d $(home_rel "${BIN_DIR}") ] && export PROVERS_BIN_DIR=$(home_rel "${BIN_DIR}")"
  fi
  unset _line _dir
else
  _block=$(mktemp)
  {
    echo "# provers-env"
    echo "export LANG=en_US.UTF-8"
    # PROVERS_BIN_DIR rather than letting env.sh work it out: it derives the
    # directory from BASH_SOURCE, which zsh does not set.
    echo "[ -d $(home_rel "${BIN_DIR}") ] && export PROVERS_BIN_DIR=$(home_rel "${BIN_DIR}")"
    # Only the choices the caller actually made are recorded, so that the
    # defaults stay in one place -- bin/env.sh -- rather than being frozen into
    # someone's startup file where a later change to them would not be picked up.
    if [ -n "${_GIVEN_PROVERS_DIR}" ]; then
      echo "[ -d $(home_rel "${_GIVEN_PROVERS_DIR}") ] && export PROVERS_DIR=$(home_rel "${_GIVEN_PROVERS_DIR}")"
    fi
    if [ -z "${_GIVEN_SIREUM_HOME}" ]; then
      echo "[ -d $(home_rel "${SIREUM_HOME}") ] && export SIREUM_HOME=$(home_rel "${SIREUM_HOME}")"
    fi
    # :- so that an unset PROVERS_BIN_DIR (its directory gone) is a failed test
    # rather than an unbound-variable error under 'set -u'.
    echo '[ -f "${PROVERS_BIN_DIR:-}/env.sh" ] && . "${PROVERS_BIN_DIR}/env.sh"'
    echo '[ -f "${PROVERS_BIN_DIR:-}/functions.sh" ] && . "${PROVERS_BIN_DIR}/functions.sh"'
    echo ""
  } > "${_block}"
  # Before the startup file's "not interactive, stop here" guard, when it has one
  # (Ubuntu's default ~/.bashrc does): appended after it, the block would be
  # skipped by every non-interactive shell -- `bash -lc ...`, which is how a
  # coding agent or a build tool runs its commands.  A comment directly above
  # the guard stays with it.
  _guard=$(grep -nE '^[[:space:]]*(case \$- in|\[ -z "\$PS1" \] && return|\[\[ \$- != \*i\* \]\] && return)' \
             "${PROVERS_RC}" 2>/dev/null | head -1 | cut -d: -f1 || true)
  if [ -n "${_guard}" ]; then
    if [ "${_guard}" -gt 1 ] && sed -n "$((_guard - 1))p" "${PROVERS_RC}" | grep -q '^[[:space:]]*#'; then
      _guard=$((_guard - 1))
    fi
    echo "  (before the non-interactive guard at line ${_guard})"
    awk -v at="${_guard}" -v block="${_block}" \
      'NR == at { while ((getline l < block) > 0) print l } { print }' \
      "${PROVERS_RC}" > "${PROVERS_RC}.provers-tmp"
    # through cat, so that a startup file that is a symlink (into a dotfiles
    # repository, say) stays one
    cat "${PROVERS_RC}.provers-tmp" > "${PROVERS_RC}"
    rm -f "${PROVERS_RC}.provers-tmp"
  else
    { echo ""; cat "${_block}"; } >> "${PROVERS_RC}"
  fi
  rm -f "${_block}"
  unset _block _guard
fi

# Shell aliases, off unless asked for.  These are a convenience for the images
# this project hands out -- they match the ones docker/Dockerfile.provers writes
# into its final stage, so that a shell in the container and a shell in the VM
# behave the same.  They are somebody's preference rather than part of the
# environment, and writing them into the dotfiles of a machine that is already
# somebody's own is not this script's business, so a bare-metal or Mac install
# gets none of them.  vagrant/Vagrantfile passes PROVERS_SHELL_ALIASES=true.
if [ "${PROVERS_SHELL_ALIASES:-false}" = "true" ]; then
  # ~/.bash_aliases is a bash convention that zsh does not read, so where the
  # startup file is a zsh one the aliases go in it directly.
  case "${PROVERS_RC}" in
    *.zshrc) PROVERS_ALIASES="${PROVERS_RC}" ;;
    *)       PROVERS_ALIASES="${HOME}/.bash_aliases" ;;
  esac
  echo "Installing the shell aliases into ${PROVERS_ALIASES}"
  if [ ! -f "${PROVERS_ALIASES}" ] || ! grep -q "alias ddir=" "${PROVERS_ALIASES}"; then
    {
      echo "alias env='env | sort'"
      echo "alias ddir='ls -lFaG'"
      echo "alias dir='ls -lFG'"
      echo "alias ..='cd ..'"
    } >> "${PROVERS_ALIASES}"
  fi
fi

set +x
echo ""
echo "PROVERS environment setup complete (${PROVERS_OS}/${PROVERS_ARCH})."
echo "Installed under ${PROVERS_DIR}:"
echo "  Verus                      ${VERUS_DIR}"
echo "  Microkit SDK ${MICROKIT_SDK_VER}         ${MICROKIT_SDK}"
echo "  LionsOS ${LIONSOS_VER}            ${LIONSOS}"
echo "  Sireum                     ${SIREUM_HOME}"
if [ "${PROVERS_IVE}" = "true" ]; then
  echo "  Sireum IVE                 installed"
fi
if [ "${PROVERS_CODEIVE}" = "true" ]; then
  echo "  CodeIVE                    installed"
fi
if [ "${PROVERS_FMIDE}" = "true" ]; then
  if [ "${PROVERS_OS}" = "darwin" ]; then
    echo "  FMIDE                      ${SIREUM_PLATFORM_BIN}/fmide.app"
  else
    echo "  FMIDE                      ${SIREUM_PLATFORM_BIN}/fmide"
  fi
fi
echo ""
echo "Open a new shell (or 'source ${BIN_DIR}/env.sh') to pick up the environment."
if [ "${PROVERS_IVE}" = "true" ] || [ "${PROVERS_CODEIVE}" = "true" ] ||
   [ "${PROVERS_FMIDE}" = "true" ]; then
  echo "The installed IDEs are launched with the 'ive', 'codium' and 'fmide' shell functions."
else
  echo "No IDE was installed.  Add one with PROVERS_IVE=true, PROVERS_CODEIVE=true or"
  echo "PROVERS_FMIDE=true -- either re-running this script or the matching bin/ script."
fi
