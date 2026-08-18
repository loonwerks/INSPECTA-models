# PROVERS Environment

The DARPA PROVERS development environment: Verus, the seL4 Microkit SDK,
LionsOS, sdfgen, Rust and Sireum/HAMR, at one pinned set of versions.

The Microkit SDK is the released one with a single fix applied to its `microkit`
tool, without which a domain-scheduled virtual machine hangs; see
[bin/microkit-vcpu-domain.sh](bin/microkit-vcpu-domain.sh).

It is defined once, as the install scripts in [bin](bin), and delivered three
ways.  A container and a VM built a week apart install the same tools at the same
versions, because they run the same scripts.

## Layout

```
provers-env/
  bin/               the install scripts -- the substance
  docker/            container image           -> docker/readme.md
  vagrant/           VirtualBox VM             -> vagrant/readme.md
  provers-setup.sh   runs bin/ in order, for a dedicated machine
```

## Getting It

| | for | start here |
| --- | --- | --- |
| prebuilt OVA | a desktop VM with the IDEs, without waiting for a build | [vagrant/readme.md](vagrant/readme.md#using-the-prebuilt-ova) |
| container | command-line tools and CI | [docker/readme.md](docker/readme.md) |
| Vagrant VM | the same desktop VM, built from source | [vagrant/readme.md](vagrant/readme.md#setting-up-a-virtualbox-vm-using-vagrant) |
| dedicated machine | an Ubuntu 24.04 box you already have | [vagrant/readme.md](vagrant/readme.md#setting-up-a-dedicated-linux-machine) |

The VM adds the GUI tools -- the Sireum IVE, CodeIVE and FMIDE -- which a
container has no use for.  Everything else is common.

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

**One set of install steps.** Both delivery paths run `bin/`.  They differ only
in the flags they pass -- `PROVERS_DEPS_PROFILE`, `PROVERS_SIREUM_PROFILE`, the
three `PROVERS_IVE`/`_CODEIVE`/`_FMIDE` knobs, and whether `bin/slim.sh` runs.
Each readme lists the values it uses.

**One set of architecture rules.** The scripts run on x86_64 and aarch64, and
work out the differences themselves from `uname -m`: Rust host triple, Microkit
SDK tarball, and whether Z3, Verus and sdfgen come from a release or have to be
built from source.  Nothing above `bin/` branches on architecture.

## What Is In bin/

Sourced for configuration:

| | |
| --- | --- |
| [versions.sh](bin/versions.sh) | every pinned version, the image name, and the per-architecture values derived from them |
| [env.sh](bin/env.sh) | `$PROVERS_DIR` and the tool paths, plus which IDEs to install |

Install steps, in the order `provers-setup.sh` runs them:

| | |
| --- | --- |
| [disk.sh](bin/disk.sh) | grows root into unused LVM extents; a no-op where there are none |
| [deps.sh](bin/deps.sh) | apt packages, per `PROVERS_DEPS_PROFILE` |
| [rust.sh](bin/rust.sh) | rustup and the pinned toolchain |
| [z3.sh](bin/z3.sh) | Z3 from source, where Verus has no release for the architecture |
| [verus.sh](bin/verus.sh) | Verus, from its release or from source |
| [microkit-lionsos.sh](bin/microkit-lionsos.sh) | sdfgen, the released Microkit SDK, LionsOS at the pinned commit (`dep/sddf` and `dep/libvmm` only) |
| [microkit-vcpu-domain.sh](bin/microkit-vcpu-domain.sh) | rebuilds the SDK's `microkit` tool with the [seL4/microkit#586](https://github.com/seL4/microkit/pull/586) vCPU domain fix |
| [sireum.sh](bin/sireum.sh) | Sireum, per `PROVERS_SIREUM_PROFILE` |
| [ive.sh](bin/ive.sh) | Sireum IVE (optional) |
| [codeive.sh](bin/codeive.sh) | CodeIVE (optional) |
| [fmide.sh](bin/fmide.sh) | FMIDE (optional) |

Housekeeping:

| | |
| --- | --- |
| [build-info.sh](bin/build-info.sh) | records what got installed, as a manifest and as container labels |
| [slim.sh](bin/slim.sh) | drops build leftovers and caches; run at the end of an image build.  The VM does not run it -- `prep-export.sh` does its own, gentler cleanup before an OVA export, keeping the dependency caches |
| [check-env.sh](bin/check-env.sh) | fails a build whose declared environment disagrees with `versions.sh` |
| [apt-mirror.sh](bin/apt-mirror.sh) | points apt at `PROVERS_APT_MIRROR` |
| [functions.sh](bin/functions.sh) | the `ive`, `codium` and `fmide` launchers, sourced from `~/.bashrc` |
| [firefox.sh](bin/firefox.sh) | Firefox from Mozilla's apt repo rather than the snap (desktop only) |
| [prep-export.sh](bin/prep-export.sh) | cleanup and zero-fill before exporting an OVA (VM only) |

Each is standalone and re-runnable, which is how a single tool gets updated in
place -- see [vagrant/readme.md](vagrant/readme.md#post-setup).
