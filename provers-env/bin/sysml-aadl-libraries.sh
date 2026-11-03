#!/bin/bash -e
# Install the SysML v2 AADL libraries at the pinned commit
# ${SYSML_AADL_LIBRARIES_VER} into ${SYSML_AADL_LIBRARIES}.
#
# The SysML models' run-hamr.cmd and ci.cmd put $SYSML_AADL_LIBRARIES on the
# --sourcepath of `sireum hamr sysml ...`; env.sh exports it.
#
# A tree that SYSML_AADL_LIBRARIES already named before env.sh ran -- a
# developer's own clone -- is adopted, not replaced: this reports its commit
# against the pin and leaves it alone.  The installed clone keeps its .git, which
# is small, so that `git -C $SYSML_AADL_LIBRARIES log -1` says what is there.
set -Eeuo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/env.sh"

if [ "${SYSML_AADL_LIBRARIES}" != "${PROVERS_DIR}/sysml-aadl-libraries" ]; then
  _rev=$(git -C "${SYSML_AADL_LIBRARIES}" rev-parse HEAD 2> /dev/null || echo "not a git checkout")
  echo "Adopting the SysML AADL libraries at ${SYSML_AADL_LIBRARIES} (${_rev})"
  if [ "${_rev}" != "${SYSML_AADL_LIBRARIES_VER}" ]; then
    echo "  note: versions.sh pins ${SYSML_AADL_LIBRARIES_VER}"
  fi
  exit 0
fi

echo "Cloning the SysML AADL libraries ${SYSML_AADL_LIBRARIES_VER}"
mkdir -p "${PROVERS_DIR}"
rm -rf "${SYSML_AADL_LIBRARIES}"
git clone "${SYSML_AADL_LIBRARIES_REPO}" "${SYSML_AADL_LIBRARIES}"
# -c advice.detachedHead=false: the pin is a commit, so this is always a
# detached checkout and git's advice about it is just noise.
git -C "${SYSML_AADL_LIBRARIES}" -c advice.detachedHead=false checkout "${SYSML_AADL_LIBRARIES_VER}"
