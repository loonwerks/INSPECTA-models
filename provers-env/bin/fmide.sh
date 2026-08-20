#!/bin/bash -e
# Install FMIDE (the OSATE-based AADL IDE) into $SIREUM_HOME/bin/linux/fmide.
# Requires sireum.sh to have been run first.
#
# Component versions (awas, gumbo, hamr, agree, briefcase, jkind, resolute,
# osate, eclipse) default to whatever the installer pins; override them via
# FMIDE_ARGS, e.g.
#
#   FMIDE_ARGS="--osate 2.14.0" bash ~/bin/fmide.sh
#
# Run '$SIREUM_HOME/bin/install/fmide.cmd --help' to see them all.
#
# -v is passed by default: this is the longest single step of the setup and says
# nothing at all without it, which is indistinguishable from a hang.  Use
# FMIDE_ARGS="--verbose+" for more.
set -Eeuo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/env.sh"

: "${FMIDE_ARGS:=}"

if [ ! -x "${SIREUM_HOME}/bin/install/fmide.cmd" ]; then
  echo "ERROR: ${SIREUM_HOME}/bin/install/fmide.cmd not found -- run sireum.sh first" >&2
  exit 1
fi

# fmide.cmd *toggles* verbosity on each occurrence of -v/--verbose rather than
# setting it, so passing our own alongside a user's would turn it back off.  Add
# it only when FMIDE_ARGS does not already say something about verbosity.
FMIDE_VERBOSE="-v"
case " ${FMIDE_ARGS} " in
  *" -v "* | *" --verbose "* | *" --verbose+ "*) FMIDE_VERBOSE="" ;;
esac

# shellcheck disable=SC2086
"${SIREUM_HOME}/bin/install/fmide.cmd" ${FMIDE_VERBOSE} ${FMIDE_ARGS}
