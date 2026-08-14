#!/bin/bash -e
# Prepare the VM for export as an OVA.  Run this INSIDE the guest, once the
# build has finished and you are happy with the environment:
#
#   vagrant ssh -c 'bash ~/bin/prep-export.sh'
#
# then halt the VM and export it from the host (the script prints the exact
# command at the end).
#
# This is deliberately NOT part of provisioning: it throws away caches that make
# subsequent installs and rebuilds faster, which is only worth doing when the
# VM is about to be frozen into an image.
#
# Knobs:
#
#   PROVERS_EXPORT_DEEP      also drop the dependency caches -- Sireum's coursier
#                            cache (${SIREUM_HOME}/lib/cache), the cargo registry
#                            and the ivy/sbt caches.  Keeping them is what lets
#                            the exported image resolve dependencies without
#                            reaching the network, which is the point of handing
#                            it to a restricted site.  The tools still run; only
#                            rebuilding them from source gets slower.
#                            (default false)
#   PROVERS_EXPORT_ZEROFILL  overwrite free space with zeros so that it
#                            compresses away during export  (default true)
#   PROVERS_EXPORT_SCRUB     remove shell history and IDE/desktop state left by
#                            manual use of the VM  (default true)
#   PROVERS_EXPORT_DRYRUN    report what would be reclaimed and change nothing
#                            (default false)
#
# Run this over 'vagrant ssh -c' from the host, NOT from an interactive shell
# inside the VM: an interactive shell rewrites ~/.bash_history when it exits, so
# it would put back the history this just deleted.
#
# Scrubbing is a backstop, not the primary tool.  If you are about to hand the
# VM out as an image, take a VirtualBox snapshot before using it and revert to
# that afterwards -- reverting is bit-exact, whereas no list of paths can be
# guaranteed to catch everything an IDE or desktop session leaves behind.
#
# Why the zero-fill matters: deleting a file frees the blocks but leaves their
# old contents on the virtual disk, and that garbage does not compress.  Writing
# zeros over the free space is what actually shrinks the exported image, because
# 'VBoxManage export' writes a stream-optimised, gzip-compressed VMDK.
set -Eeuo pipefail

DEEP="${PROVERS_EXPORT_DEEP:-false}"
ZEROFILL="${PROVERS_EXPORT_ZEROFILL:-true}"
DRYRUN="${PROVERS_EXPORT_DRYRUN:-false}"
SCRUB="${PROVERS_EXPORT_SCRUB:-true}"

human() { sudo du -sh "$1" 2>/dev/null | cut -f1; }

# empty a directory, keeping the directory itself
drop() {
  local path="$1" why="$2"
  [ -e "${path}" ] || return 0
  printf "  %-34s %6s  %s\n" "${path}" "$(human "${path}")" "${why}"
  [ "${DRYRUN}" = "true" ] && return 0
  sudo rm -rf -- "${path:?}"/* 2>/dev/null || true
}

# remove a file or directory outright
nuke() {
  local path="$1"
  [ -e "${path}" ] || return 0
  printf "  %-44s %6s\n" "${path/#${HOME}/\~}" "$(human "${path}")"
  [ "${DRYRUN}" = "true" ] && return 0
  sudo rm -rf -- "${path:?}" 2>/dev/null || true
}

echo "Before:"
df -h / | tail -1
echo

if [ "${DRYRUN}" = "true" ]; then
  echo "DRY RUN -- nothing will be removed."
  echo
fi

echo "Reclaiming (safe):"
[ "${DRYRUN}" = "true" ] || sudo apt-get clean
drop /var/cache/apt          "downloaded .deb archives"
drop /var/lib/apt/lists      "package indexes (rebuilt by apt-get update)"
drop /home/"${USER}"/.cache/pip "pip wheel cache"
drop /tmp                    "temporary files"
# Sireum's build output, as opposed to its dependency cache below: recreated by
# the next build, and never something that has to be re-downloaded.
drop "${SIREUM_HOME:-${HOME}/provers/Sireum}/out" "Sireum build output"

if [ "${DRYRUN}" != "true" ]; then
  sudo journalctl --vacuum-time=1d > /dev/null 2>&1 || true
  sudo find /var/log -type f \( -name '*.gz' -o -name '*.1' -o -name '*.old' \) -delete 2>/dev/null || true
fi
echo "  rotated logs / journal             trimmed"

if [ "${SCRUB}" = "true" ]; then
  echo
  echo "Scrubbing per-user state (shell history, IDE and desktop residue):"
  # ~/.ssh/authorized_keys is deliberately NOT touched -- removing it locks
  # vagrant out of the VM.  ~/.bashrc and ~/.bash_aliases are kept too: the
  # provisioning wrote the PROVERS environment into them.
  #
  # Dropping ~/.config/xfce4 resets the desktop to defaults, which is what you
  # want in an image.  It is safe because the settings that matter are written
  # system-wide: /etc/xdg/xfce4/helpers.rc names the browser, and the per-user
  # copy is only ever a cache of that (or, worse, a stale
  # WebBrowserDismissed=true from someone dismissing the browser prompt).
  for p in \
      "${HOME}/.bash_history" "${HOME}/.viminfo" "${HOME}/.lesshst" \
      "${HOME}/.python_history" "${HOME}/.wget-hsts" "${HOME}/.ssh/known_hosts" \
      "${HOME}/.xsession-errors" "${HOME}/.xsession-errors.old" \
      "${HOME}/.local/share/recently-used.xbel" "${HOME}/.cache/thumbnails" \
      "${HOME}/.cache/sessions" "${HOME}/.config/xfce4" "${HOME}/.cache/xfce4" \
      "${HOME}/.config/JetBrains" \
      "${HOME}/.cache/JetBrains" "${HOME}/.local/share/JetBrains" \
      "${HOME}/.java/.userPrefs" "${HOME}/.config/VSCodium" \
      "${HOME}/.vscode-oss" "${HOME}/.eclipse" "${HOME}/.recently-used" \
      /root/.bash_history /root/.viminfo; do
    nuke "${p}"
  done
  echo "  (nothing listed above means there was no residue to remove)"
fi

if [ "${DEEP}" = "true" ]; then
  echo
  echo "Reclaiming (deep -- slower to rebuild afterwards):"
  # Sireum's coursier cache lives inside the install, not under ~/.cache --
  # Coursier.defaultCacheDir falls back to ${SIREUM_HOME}/lib/cache when
  # COURSIER_CACHE is unset.  ~/.cache/coursier is nearly always empty here.
  drop "${SIREUM_HOME:-${HOME}/provers/Sireum}/lib/cache" "Sireum coursier artifact cache"
  drop "${HOME}/.cargo/registry"      "cargo source/registry cache"
  drop "${HOME}/.cache/coursier"      "coursier artifact cache (non-Sireum)"
  drop "${HOME}/.ivy2"                "ivy cache"
  drop "${HOME}/.sbt"                 "sbt cache"
else
  echo
  echo "Skipping deep caches (set PROVERS_EXPORT_DEEP=true to include them):"
  for d in "${SIREUM_HOME:-${HOME}/provers/Sireum}/lib/cache" \
           "${HOME}/.cargo/registry" \
           "${HOME}/.cache/coursier" "${HOME}/.ivy2" "${HOME}/.sbt"; do
    [ -e "${d}" ] && printf "  %-34s %6s\n" "${d}" "$(human "${d}")"
  done
fi

echo
echo "After cleanup:"
df -h / | tail -1

if [ "${ZEROFILL}" = "true" ] && [ "${DRYRUN}" != "true" ]; then
  echo
  avail_mb="$(df -BM --output=avail / | tail -1 | tr -dc '0-9')"
  echo "Zero-filling ~$(( avail_mb / 1024 )) GiB of free space on / ..."
  echo "(measured at ~400 MB/s on an SSD-backed host, so typically a minute or two)"
  # Written as the invoking user, not root, so that ext4's 5% reserved blocks
  # stay free -- filling those too can disrupt services still running in the VM.
  dd if=/dev/zero of="${HOME}/ZEROFILL" bs=16M status=none || true
  rm -f "${HOME}/ZEROFILL"
  sync
  echo "done."
  # /boot is a separate 2GB partition with very little churn, so it is left
  # alone; zero-filling it would save almost nothing.
fi

# The appliance carries the same facts as ${PROVERS_DIR}/build-info, so that they
# are visible from 'VBoxManage import -n' or an appliance manager without
# starting the VM.  Read them back rather than restating them.
INFO="${PROVERS_DIR:-${HOME}/provers}/build-info"
if [ -r "${INFO}" ]; then
  # shellcheck disable=SC1090
  . "${INFO}"
  BUILD_DATE="${PROVERS_BUILD_DATE}"
  OVA_DESC="Verus ${VERUS_VER}, Microkit SDK ${MICROKIT_SDK_VER} and ${MICROKIT_DOMAINS_SDK_VER} (domain scheduling), LionsOS, sdfgen ${SDFGEN_VER}, Rust ${RUST_TOOLCHAIN_VER}, Sireum ${SIREUM_V}. IDEs: ${PROVERS_IDES}. Built ${PROVERS_BUILD_DATE} for ${PROVERS_ARCH}."
else
  BUILD_DATE="$(date -u +%Y-%m-%dT%H:%M:%SZ)"
  OVA_DESC="DARPA PROVERS development environment"
fi

# The name the appliance carries, which is also what an importer's VirtualBox
# will call the VM.  It is 'provers-env-<arch>-<build date>', matching the name
# the Vagrantfile gives the machine it was built from.
#
# Getting the date to agree takes some care.  build-info records the build in
# UTC, while the Vagrantfile names the VM from the *host's* clock, so a build
# finishing in the evening local time is already the next day in UTC and the two
# disagree.  TZ decides which is meant: set it to the building host's zone and
# the date matches the VM name.  PROVERS_OVA_DATE states it outright, for when
# the export happens on a different machine or long afterwards.
if [ -n "${PROVERS_OVA_DATE:-}" ]; then
  OVA_DATE="${PROVERS_OVA_DATE}"
else
  # date -d understands the ISO-8601 Z suffix and renders it in $TZ; if that
  # fails for any reason, fall back to the UTC date as recorded.
  OVA_DATE="$(date -d "${BUILD_DATE}" +%Y.%m.%d 2>/dev/null \
              || printf '%s' "${BUILD_DATE%%T*}" | tr - .)"
fi

# uname says aarch64/x86_64; the Vagrantfile names VMs with the Debian-style
# arm64/amd64, because that is the vocabulary Vagrant's box_architecture wants.
# Follow the Vagrantfile, so the OVA and the VM it came from read the same.
case "${PROVERS_ARCH:-$(uname -m)}" in
  aarch64|arm64) OVA_ARCH=arm64 ;;
  *)             OVA_ARCH=amd64 ;;
esac

OVA_NAME="${PROVERS_OVA_NAME:-provers-env-${OVA_ARCH}-${OVA_DATE}}"
OVA_VERSION="${OVA_DATE//./-}"

echo
echo "Now halt the VM and export it from the host.  \$SRC is the VirtualBox machine"
echo "to export -- its name is dated from whenever 'vagrant up' last ran, so take it"
echo "from 'VBoxManage list vms' rather than assuming.  What the OVA is called, and"
echo "what an importer's VirtualBox names the VM, come from --vmname below instead:"
echo
echo "  vagrant halt"
echo "  SRC=\$(VBoxManage list vms | grep -o '\"provers-env[^\"]*\"' | tr -d '\"' | head -1)"
echo "  VBoxManage export \"\${SRC}\" -o ${OVA_NAME}.ova \\"
echo "      --vsys 0 \\"
echo "      --vmname      '${OVA_NAME}' \\"
echo "      --product     'DARPA PROVERS development environment' \\"
echo "      --producturl  'https://github.com/loonwerks/INSPECTA-models' \\"
echo "      --vendor      'Kansas State University SAnToS Laboratory' \\"
echo "      --vendorurl   'https://sireum.org' \\"
echo "      --version     '${OVA_VERSION}' \\"
echo "      --description '${OVA_DESC}'"
echo
echo "The same facts are in ${INFO} inside the VM."
echo
echo "The name above is derived from the build date in UTC (${BUILD_DATE}),"
echo "rendered in ${TZ:-UTC}.  If that is not the date this build should carry,"
echo "re-run with TZ set to the building host's zone, or PROVERS_OVA_DATE=YYYY.MM.DD"
echo "(or PROVERS_OVA_NAME to set the whole name)."
echo
echo "Export writes a compressed VMDK, so the .ova is typically far smaller than"
echo "the VM directory on disk."
