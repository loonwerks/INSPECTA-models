#!/bin/bash -e
# Grow the root filesystem into unallocated space in its LVM volume group.
#
# The bento/ubuntu-24.04 box ships a 64GB virtual disk, but Ubuntu's installer
# allocates only about half of it to the root logical volume and leaves the rest
# as free extents in the volume group:
#
#   sda                         64G
#   `-sda3                      62G
#     `-ubuntu--vg-ubuntu--lv   31G   <- only this is formatted
#
# The full PROVERS install (Sireum/IVE, CodeIVE, FMIDE, the Microkit SDKs, the
# Rust toolchains and Verus) does not comfortably fit in the ~23GB that leaves
# free, so reclaim the unused extents before anything else is installed.
#
# ext4 grows online, so this is safe on a running system, and it is a no-op once
# the volume group has no free extents.  Guests not using LVM are left alone.
set -Eeuo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/env.sh"

# Nothing to do off Linux: this is about the Vagrant base box's half-allocated
# LVM volume group, and a Mac's disk is whatever size it is.
if [ "${PROVERS_OS}" != "linux" ]; then
  echo "disk.sh: ${PROVERS_OS} has no LVM volume group to grow; nothing to do."
  exit 0
fi

if ! command -v lvs > /dev/null 2>&1; then
  echo "LVM tools are not installed; leaving the root filesystem as-is."
  exit 0
fi

ROOT_SRC="$(findmnt -no SOURCE /)"
ROOT_DEV="$(readlink -f "${ROOT_SRC}")"

# findmnt reports the device-mapper name (/dev/mapper/ubuntu--vg-ubuntu--lv)
# while lvs reports the LV path (/dev/ubuntu-vg/ubuntu-lv).  Both resolve to the
# same /dev/dm-N node, so compare the resolved devices rather than the names.
ROOT_LV=""
while read -r lv_path; do
  [ -n "${lv_path}" ] || continue
  if [ "$(readlink -f "${lv_path}")" = "${ROOT_DEV}" ]; then
    ROOT_LV="${lv_path}"
    break
  fi
done < <(sudo lvs --noheadings -o lv_path | awk '{$1=$1};1')

if [ -z "${ROOT_LV}" ]; then
  echo "Root (${ROOT_SRC}) is not an LVM logical volume; nothing to grow."
  exit 0
fi

VG_NAME="$(sudo lvs --noheadings -o vg_name "${ROOT_LV}" | awk '{$1=$1};1')"
FREE_EXTENTS="$(sudo vgs --noheadings -o vg_free_count "${VG_NAME}" | awk '{$1=$1};1')"

if [ "${FREE_EXTENTS}" -eq 0 ]; then
  echo "Volume group ${VG_NAME} has no free extents; root is already at full size."
  exit 0
fi

sudo lvextend -l +100%FREE "${ROOT_LV}"

case "$(findmnt -no FSTYPE /)" in
  ext2 | ext3 | ext4) sudo resize2fs "${ROOT_LV}" ;;
  xfs) sudo xfs_growfs / ;;
  *)
    # The LV is bigger either way; carrying on just means the install runs in
    # the space it would have had anyway, which is not worth failing over.
    echo "Unrecognised root filesystem; ${ROOT_LV} was extended but not resized." >&2
    ;;
esac

df -h /
