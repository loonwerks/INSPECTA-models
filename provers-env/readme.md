# PROVERS Environment

The DARPA PROVERS development environment: Verus, the seL4 Microkit SDK,
LionsOS, sdfgen, Rust and Sireum/HAMR, at one pinned set of versions.

The Microkit SDK is the released 2.3.0 one with a single fix applied to
its `microkit` tool, without which a domain-scheduled virtual machine hangs; see
[bin/microkit-vcpu-domain.sh](bin/microkit-vcpu-domain.sh).

It is defined once, as the install scripts in [bin](bin), and delivered four
ways.  A container and a VM built a week apart install the same tools at the same
versions, because they run the same scripts.

## Layout

```
provers-env/
  bin/               the install scripts -- the substance
  docker/            container image           -> docker/readme.md
  vagrant/           VirtualBox VM             -> vagrant/readme.md
  provers-setup.sh   runs bin/ in order, for a machine of your own
```

## Getting It

| | for | start here |
| --- | --- | --- |
| prebuilt OVA | a desktop VM with the IDEs, without waiting for a build | [vagrant/readme.md](vagrant/readme.md#using-the-prebuilt-ova) |
| container | command-line tools and CI | [docker/readme.md](docker/readme.md) |
| Vagrant VM | the same desktop VM, built from source | [vagrant/readme.md](vagrant/readme.md#setting-up-a-virtualbox-vm-using-vagrant) |
| Apple Silicon Mac | native tools on the Mac itself, no VM | [vagrant/readme.md](vagrant/readme.md#setting-up-an-apple-silicon-mac) |
| Ubuntu 24.04 machine | native tools on the machine itself, no VM | [vagrant/readme.md](vagrant/readme.md#setting-up-an-ubuntu-2404-machine) |

The VM adds the GUI tools -- the Sireum IVE, CodeIVE and FMIDE -- which a
container has no use for.  A Mac gets those as `.app` bundles.  Everything else
is common.

On Apple Silicon the native install is worth preferring over the arm64 OVA where
it will do: it is much the quickest of the four to set up, because every
upstream publishes an arm64 macOS asset and so nothing is built from source --
which is the opposite of aarch64 Linux, where Z3, Verus and sdfgen all are.

## How It Stays Consistent

**One set of versions.** [bin/versions.sh](bin/versions.sh) pins every tool,
including which kekinian revision Sireum is built from, which LionsOS commit is
checked out, and which image `docker.sh` publishes.  The VM sources it directly;
the container build sources it too and passes each value as `--build-arg`, so
neither carries a second copy.  Every pin can be overridden by exporting it
before running a setup.

It also pins `PROVERS_BUILD_VER`, the version a build carries -- the container
tags, the VirtualBox machine name and the OVA name all come from it, so the
three delivery paths built from one set of pins identify themselves the same
way.  It is a date, but a pinned one: a rebuild that fixes a pin republishes
over the build it replaces rather than standing beside it.  Bump it when
publishing a genuinely new build.

**One set of install steps.** Every delivery path runs `bin/`.  They differ only
in the flags they pass -- `PROVERS_DEPS_PROFILE`, `PROVERS_SIREUM_PROFILE`, the
three `PROVERS_IVE`/`_CODEIVE`/`_FMIDE` knobs, `PROVERS_SHELL_ALIASES`, and
whether `bin/slim.sh` runs.  Each readme lists the values it uses.

The images are the opinionated ones.  A container and a VM are built to be handed
out, so they carry things an installed machine should not have imposed on it, and
the defaults in `bin/env.sh` are the conservative ones an image then overrides:

| | default | container | VM |
| --- | --- | --- | --- |
| IVE / CodeIVE / FMIDE | off | off | **on** |
| shell aliases | off | written by the Dockerfile | **on** |
| shell trace (`PROVERS_TRACE`) | off | **on** | **on** |

So a bare-metal or Mac install gets the command-line tools and nothing else
unless it asks -- `PROVERS_IVE=true bash provers-setup.sh` and so on -- and says
a few lines per step rather than echoing every command it runs.  An image build
is unattended and its log is the only record of what happened, so those keep the
trace.

**One set of host rules.** The scripts run on Ubuntu (x86_64 and aarch64) and on
macOS (Apple Silicon), and work out the differences themselves from `uname -s`
and `uname -m`: package manager, Rust host triple, Microkit SDK tarball, Verus
release asset, and whether Z3, Verus and sdfgen come from a release or have to be
built from source.  Nothing above `bin/` branches on the host -- the same
`provers-setup.sh` is what you run on all three.

| | Ubuntu x86_64 | Ubuntu aarch64 | macOS arm64 |
| --- | --- | --- | --- |
| packages | apt | apt | Homebrew |
| Z3 | in the Verus release | built by `z3.sh` | in the Verus release |
| Verus | released asset | built with `vargo` | released asset |
| sdfgen | PyPI wheel | built from source (zig) | PyPI wheel |
| IDEs | `bin/linux` | `bin/linux/arm` | `bin/mac`, as `.app` bundles |

The `microkit` tool is rebuilt from source on all three, but that is a small
cargo build rather than a full SDK build -- see
[microkit-vcpu-domain.sh](bin/microkit-vcpu-domain.sh).

## What Is In bin/

Sourced for configuration:

| | |
| --- | --- |
| [versions.sh](bin/versions.sh) | every pinned version, the image name, and the per-host values derived from them |
| [env.sh](bin/env.sh) | `$PROVERS_DIR` and the tool paths, plus which IDEs to install.  Exports only the four a build needs -- `SIREUM_HOME`, `MICROKIT_SDK`, `MICROKIT_BOARD`, `LIONSOS` -- and `PATH`; everything else stays internal to the scripts |

Install steps, in the order `provers-setup.sh` runs them:

| | |
| --- | --- |
| [disk.sh](bin/disk.sh) | grows root into unused LVM extents; a no-op where there are none, and on macOS |
| [deps.sh](bin/deps.sh) | apt packages or Homebrew formulae, per `PROVERS_DEPS_PROFILE` |
| [rust.sh](bin/rust.sh) | rustup and the pinned toolchain |
| [z3.sh](bin/z3.sh) | Z3 from source, where Verus has no release for the host |
| [verus.sh](bin/verus.sh) | Verus, from its release or from source |
| [microkit-lionsos.sh](bin/microkit-lionsos.sh) | sdfgen, the released Microkit SDK, LionsOS at the pinned commit (`dep/sddf` and `dep/libvmm` only) |
| [microkit-vcpu-domain.sh](bin/microkit-vcpu-domain.sh) | rebuilds the SDK's `microkit` tool with the [seL4/microkit#586](https://github.com/seL4/microkit/pull/586) vCPU domain fix |
| [sireum.sh](bin/sireum.sh) | Sireum, per `PROVERS_SIREUM_PROFILE`: unpacks the `cli` distribution where `SIREUM_V` names a release, builds from source where it names a commit -- or adopts the one `SIREUM_HOME` already points at |
| [ive.sh](bin/ive.sh) | Sireum IVE (opt-in; the VM turns it on) |
| [codeive.sh](bin/codeive.sh) | CodeIVE (opt-in; the VM turns it on) |
| [fmide.sh](bin/fmide.sh) | FMIDE (opt-in; the VM turns it on), installed with `-v` |

Housekeeping:

| | |
| --- | --- |
| [build-info.sh](bin/build-info.sh) | records what got installed, as a manifest and as container labels |
| [slim.sh](bin/slim.sh) | drops build leftovers and caches; run at the end of an image build.  The VM does not run it -- `prep-export.sh` does its own, gentler cleanup before an OVA export, keeping the dependency caches.  Refuses to run on macOS, where the binaries it deletes are the ones in use |
| [check-env.sh](bin/check-env.sh) | fails a build whose declared environment disagrees with `versions.sh` |
| [apt-mirror.sh](bin/apt-mirror.sh) | points apt at `PROVERS_APT_MIRROR` (Linux only) |
| [functions.sh](bin/functions.sh) | the `ive`, `codium` and `fmide` launchers, sourced from the shell startup file |
| [firefox.sh](bin/firefox.sh) | Firefox from Mozilla's apt repo rather than the snap (Linux desktop only) |
| [prep-export.sh](bin/prep-export.sh) | cleanup and zero-fill before exporting an OVA (VM only) |

Each is standalone and re-runnable, which is how a single tool gets updated in
place -- see [vagrant/readme.md](vagrant/readme.md#post-setup).
