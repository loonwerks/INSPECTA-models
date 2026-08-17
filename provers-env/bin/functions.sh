# Launchers for the IDEs installed by provers-env, sourced from ~/.bashrc.
#
# Each takes an optional directory to open and defaults to the current one:
#
#   ive              # open $PWD in Sireum IVE
#   codium ~/provers # open ~/provers in CodeIVE
#   fmide            # open $PWD in FMIDE
#
# Unlike their bare-metal counterparts these background the process so the
# terminal stays usable.  This file is meant to be sourced, not executed.

# Sireum lays out aarch64 binaries under bin/linux/arm, x86_64 under bin/linux.
_provers_sireum_bin() {
  case "$(uname -m)" in
    aarch64|arm64) echo "${SIREUM_HOME}/bin/linux/arm" ;;
    *)             echo "${SIREUM_HOME}/bin/linux" ;;
  esac
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

ive() {
  _provers_launch "Sireum IVE" "$(_provers_sireum_bin)/idea/bin/IVE.sh" "$@"
}

codium() {
  local bin
  bin=$(_provers_sireum_bin)
  # the launcher is named CodeIVE.sh on x86_64 and codeive on aarch64
  if [ -x "${bin}/vscodium/bin/CodeIVE.sh" ]; then
    _provers_launch "CodeIVE" "${bin}/vscodium/bin/CodeIVE.sh" "$@"
  else
    _provers_launch "CodeIVE" "${bin}/vscodium/bin/codeive" "$@"
  fi
}

fmide() {
  _provers_launch "FMIDE" "$(_provers_sireum_bin)/fmide/fmide" "$@"
}
