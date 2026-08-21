# Launchers for the IDEs installed by provers-env, sourced from the shell
# startup file (~/.bashrc on Ubuntu, ~/.zshrc on macOS).
#
# Each takes an optional directory to open and defaults to the current one:
#
#   ive              # open $PWD in Sireum IVE
#   codium ~/provers # open ~/provers in CodeIVE
#   fmide            # open $PWD in FMIDE
#
# Unlike their bare-metal counterparts these background the process so the
# terminal stays usable.  This file is sourced, not executed, and by zsh as well
# as bash -- so nothing here may use bash-only syntax.

# ${SIREUM_PLATFORM_BIN} comes from env.sh: bin/linux, bin/linux/arm or bin/mac.
_provers_sireum_bin() {
  echo "${SIREUM_PLATFORM_BIN:-${SIREUM_HOME}/bin/linux}"
}

_provers_launch() {
  local what=$1; shift
  local exe=$1; shift
  local dir=${1:-$PWD}
  if [ ! -x "${exe}" ]; then
    echo "${what} is not installed (${exe} not found)" >&2
    return 1
  fi
  "${exe}" "${dir}" &> /dev/null &
}

# macOS installs each IDE as an .app bundle, which is launched through 'open'
# rather than by executing a file.  The directory is passed as a document for
# 'open' to hand to the application, which is what resolves a relative path such
# as the '.' in 'codium .': the application itself is started with / as its
# working directory, so anything reaching it as a plain argument -- which is
# what 'open -n ... --args' does -- is resolved against / and the IDE opens the
# whole file system.  'open' returns immediately, so unlike the Linux path there
# is nothing to background.
_provers_launch_app() {
  local what=$1; shift
  local app=$1; shift
  local dir=${1:-$PWD}
  if [ ! -d "${app}" ]; then
    echo "${what} is not installed (${app} not found)" >&2
    return 1
  fi
  open -a "${app}" "${dir}"
}

ive() {
  if [ "${PROVERS_OS}" = "darwin" ]; then
    _provers_launch_app "Sireum IVE" "$(_provers_sireum_bin)/idea/IVE.app" "$@"
  else
    _provers_launch "Sireum IVE" "$(_provers_sireum_bin)/idea/bin/IVE.sh" "$@"
  fi
}

codium() {
  local bin
  bin=$(_provers_sireum_bin)
  if [ "${PROVERS_OS}" = "darwin" ]; then
    _provers_launch_app "CodeIVE" "${bin}/vscodium/CodeIVE.app" "$@"
  # the launcher is named CodeIVE.sh on x86_64 Linux and codeive on aarch64
  elif [ -x "${bin}/vscodium/bin/CodeIVE.sh" ]; then
    _provers_launch "CodeIVE" "${bin}/vscodium/bin/CodeIVE.sh" "$@"
  else
    _provers_launch "CodeIVE" "${bin}/vscodium/bin/codeive" "$@"
  fi
}

fmide() {
  if [ "${PROVERS_OS}" = "darwin" ]; then
    # ${SIREUM_HOME}/bin/mac/fmide.app, not under a fmide/ directory as on Linux
    _provers_launch_app "FMIDE" "$(_provers_sireum_bin)/fmide.app" "$@"
  else
    _provers_launch "FMIDE" "$(_provers_sireum_bin)/fmide/fmide" "$@"
  fi
}
