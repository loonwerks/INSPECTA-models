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
set -Eeuxo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/env.sh"

: "${FMIDE_ARGS:=}"

if [ ! -x "${SIREUM_HOME}/bin/install/fmide.cmd" ]; then
  echo "ERROR: ${SIREUM_HOME}/bin/install/fmide.cmd not found -- run sireum.sh first" >&2
  exit 1
fi

# shellcheck disable=SC2086
"${SIREUM_HOME}/bin/install/fmide.cmd" ${FMIDE_ARGS}
