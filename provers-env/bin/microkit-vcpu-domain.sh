#!/bin/bash -e
# Rebuild the Microkit ${MICROKIT_SDK_VER} SDK's `microkit` tool carrying the
# vCPU domain fix, and install it over the released one in ${MICROKIT_SDK}.
#
# Why this exists.  A Microkit virtual machine combined with domain scheduling
# hangs on the stock 2.3.0 SDK: in tool/microkit/src/capdl/builder.rs a PD's own
# TCB gets `domain: pd.domain`, but the VM's vCPU TCB is built with
# `domain: None` and never assigned.  The vCPU is left in domain 0 while its VMM
# sits in whatever domain the system description gave it, so the two are never
# scheduled together, the guest's virtual timer interrupt is never delivered, and
# a Linux guest stops during its arch_timer probe.  `<virtual_machine>` has no
# attribute to work around it.
#
# Fixed upstream by https://github.com/seL4/microkit/pull/586, merged on
# 2026-07-30 -- a week after 2.3.0 was released, so no release carries it yet.
# The same one-line fix had already been merged into the `domains` fork this
# environment used to build a whole SDK from (JE-Archer/microkit#1) and was left
# behind when domain scheduling was upstreamed for 2.3.0; see also
# au-ts/libvmm#138.  This script goes away with the first release after 2.3.0,
# where the released SDK can be used as-is.
#
# Only the host tool is rebuilt.  The change lives entirely in the capDL spec the
# tool emits, which the unmodified initialiser consumes, so the kernel, loader,
# monitor, libmicrokit and headers stay exactly as released -- and nothing here
# needs an aarch64 cross toolchain or a seL4 build, which is what made the old
# fork-SDK build the longest step of the setup.
set -Eeuxo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/env.sh"
source "${HOME}/.cargo/env"

if [ ! -d "${MICROKIT_SDK}" ]; then
  echo "microkit-vcpu-domain.sh: ${MICROKIT_SDK} is not there; run microkit-lionsos.sh first" >&2
  exit 1
fi

BUILD_DIR=${PROVERS_DIR}/microkit-${MICROKIT_SDK_VER}-tool-build
rm -rf "${BUILD_DIR}"

# The tag is MICROKIT_SDK_VER, so the tool is rebuilt from exactly the sources
# the installed SDK was released from.  No submodules: seL4 is one, and only
# tool/microkit is built here.
git clone --depth 1 --branch "${MICROKIT_SDK_VER}" "${MICROKIT_REPO}" "${BUILD_DIR}"
cd "${BUILD_DIR}"

# Anchored on the comment that marks the VM vCPU TCB, because the same
# `domain: None` initialiser also appears in the ordinary PD path above it, which
# must be left alone.  Asserting exactly one match means an upstream edit that
# moves this code fails the build here rather than silently patching nothing.
python3 - <<'PATCH'
import io
p = "tool/microkit/src/capdl/builder.rs"
s = io.open(p, encoding="utf-8").read()
old = """                            master_fault_ep: None, // Not used on MCS kernel.
                            domain: None,
"""
new = """                            master_fault_ep: None, // Not used on MCS kernel.
                            // seL4/microkit#586: a VM's vCPU must run in the
                            // same scheduling domain as its VMM.
                            domain: pd.domain,
"""
if s.count(old) != 1:
    raise SystemExit(
        "microkit-vcpu-domain.sh: expected exactly one VM vCPU TCB site in %s, found %d.\n"
        "The upstream source has moved; re-check the patch against this tag." % (p, s.count(old)))
io.open(p, "w", encoding="utf-8").write(s.replace(old, new, 1))
print("patched %s" % p)
PATCH

# Built with the environment's pinned toolchain, which rust.sh has installed.
# tool/microkit/Cargo.toml sets rust-version = 1.94.0; RUST_TOOLCHAIN_VER
# satisfies that today, and if a future bump does not, cargo says so outright
# ("requires rustc 1.94.0 or newer") rather than failing obscurely.
cargo "+${RUST_TOOLCHAIN_VER}-${RUST_HOST_TRIPLE}" build --release -p microkit-tool

install -m 0755 target/release/microkit "${MICROKIT_SDK}/bin/microkit"

# Smoke test: the tool prints usage and exits 0 for --help.  A tool that cannot
# start is worth catching here rather than in someone's first `make`.
"${MICROKIT_SDK}/bin/microkit" --help > /dev/null

# Record what was done to the SDK, so that an installed environment can say why
# its tool differs from the released one.
cat > "${MICROKIT_SDK}/VCPU-DOMAIN-PATCH" <<EOF
The 'bin/microkit' tool in this SDK is not the released binary.  It was rebuilt
from ${MICROKIT_REPO} at tag ${MICROKIT_SDK_VER} with one change:

  tool/microkit/src/capdl/builder.rs
  -   domain: None,
  +   domain: pd.domain,   // for the VM vCPU TCB

Without it a domain-scheduled virtual machine never receives its guest's virtual
timer interrupt and a Linux guest hangs in arch_timer probe.  It is the fix from
https://github.com/seL4/microkit/pull/586, which is in Microkit's main branch
but not in any release as of ${MICROKIT_SDK_VER}.

Everything else in this SDK -- kernel, loader, monitor, initialiser, libmicrokit
and headers -- is the unmodified ${MICROKIT_SDK_VER} release.

Applied by provers-env/bin/microkit-vcpu-domain.sh.
EOF

cd "${PROVERS_DIR}"
rm -rf "${BUILD_DIR}"
