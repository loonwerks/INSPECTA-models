# PROVERS Environment Setup

This folder contains scripts and a `Vagrantfile` that set up the DARPA PROVERS
development environment.  You can get that environment either:

* [by importing a prebuilt OVA](#using-the-prebuilt-ova),

* [by provisioning a Linux VM using VirtualBox and Vagrant](#setting-up-a-virtualbox-vm-using-vagrant), or

* [in a dedicated Linux machine](#setting-up-a-dedicated-linux-machine).

It installs the command-line tools from [../bin](../bin), and adds the GUI
development tools that only make sense on a desktop: the Sireum IVE, CodeIVE and
FMIDE.  If you only need the command-line tools, the container built from the
same scripts is lighter -- see [../docker/readme.md](../docker/readme.md).

## Layout

```
provers-env/
  bin/               install scripts, shared by everything below
  docker/            the container image -- ../docker/readme.md
  vagrant/           this directory: the VirtualBox VM
  provers-setup.sh   runs bin/ in order; used by the VM and on bare metal
```

`bin/` is the substance; this directory and `docker/` are two ways of running
it, which is what keeps the VM and the image on the same tools and versions.
[../readme.md](../readme.md) is the overview of all three.

## Contents

* [Layout](#layout)
* [What Gets Installed](#what-gets-installed)
  * [Launching the IDEs](#launching-the-ides)
  * [Choosing the IDEs](#choosing-the-ides)
  * [Architecture](#architecture)
* [Using The Prebuilt OVA](#using-the-prebuilt-ova)
* [Setting Up A VirtualBox VM Using Vagrant](#setting-up-a-virtualbox-vm-using-vagrant)
  * [Requirements](#requirements)
  * [Notes](#notes)
  * [Guest Additions](#guest-additions)
  * [Setup](#setup)
  * [What Is In This VM](#what-is-in-this-vm)
  * [Smoke Test](#smoke-test)
  * [Exporting An OVA](#exporting-an-ova)
* [Setting Up A Dedicated Linux Machine](#setting-up-a-dedicated-linux-machine)
  * [Requirement](#requirement)
  * [Steps](#steps)
* [Post Setup](#post-setup)
  * [Optionals](#optionals)

## What Gets Installed

Everything lands under `$PROVERS_DIR` (default `~/provers`):

| | |
| --- | --- |
| `$VERUS_DIR` | Verus (`VERUS_VER`); `$VERUS_Z3_PATH` points at the Z3 it runs with |
| `$MICROKIT_SDK` | Microkit SDK 1.4.1 built from source with experimental domain scheduling support |
| `$MICROKIT_SDK_CURRENT` | the released Microkit SDK (`MICROKIT_SDK_VER`) |
| `$LIONSOS` | LionsOS, with `$VMM_DIR` pointing at `dep/libvmm` |
| `$SIREUM_HOME` | a full Sireum (kekinian) install, built from `SIREUM_V` |
| `$SIREUM_HOME/bin/linux/idea` | Sireum IVE, the IntelliJ-based IDE (optional) |
| `$SIREUM_HOME/bin/linux/vscodium` | CodeIVE, the VSCodium-based IDE (optional) |
| `$SIREUM_HOME/bin/linux/fmide` | FMIDE, the OSATE-based AADL IDE (optional) |
| `$SDFGEN_VENV` | a python venv holding `sdfgen` (`SDFGEN_VER`) |
| `~/.cargo` | Rust (`RUST_TOOLCHAIN_VER` + `RUST_NIGHTLY_VER`), required by Verus and the SDK build |

Those variables, plus `MICROKIT_BOARD` (default `qemu_virt_aarch64`) and the
`PATH` additions, are defined in [bin/env.sh](../bin/env.sh), which the setup
sources from `~/.bashrc`.

Tool versions are pinned in [bin/versions.sh](../bin/versions.sh) and can each be
overridden by exporting the corresponding variable before running the setup.
Sireum is pinned there too, to a kekinian revision rather than tracking
`master`, so that setups run weeks apart install the same thing.  That file is
the single source of truth for the VM and the container alike; see
[../docker/readme.md](../docker/readme.md) for how the image consumes it.

This setup runs the scripts with `PROVERS_DEPS_PROFILE=vm` and
`PROVERS_SIREUM_PROFILE=full`, and does not run `bin/slim.sh`, so the VM keeps
what a shipped image drops: the LionsOS examples, its `.git` directory and the
micropython / wasm-micro-runtime deps, the full JDK including JavaFX, and the
build caches.

### Launching the IDEs

[bin/functions.sh](../bin/functions.sh) is sourced from `~/.bashrc` and defines a
shell function per IDE.  Each takes an optional directory and defaults to the
current one:

```bash
ive                    # Sireum IVE on $PWD
codium ~/provers       # CodeIVE on ~/provers
fmide                  # FMIDE on $PWD
```

They resolve the right path for the architecture (`bin/linux` vs
`bin/linux/arm`) and background the process so the terminal stays usable.

### Choosing the IDEs

Each IDE is installed by its own script and can be skipped:

| | script | knob |
| --- | --- | --- |
| Sireum IVE | [bin/ive.sh](../bin/ive.sh) | `PROVERS_IVE` |
| CodeIVE | [bin/codeive.sh](../bin/codeive.sh) | `PROVERS_CODEIVE` |
| FMIDE | [bin/fmide.sh](../bin/fmide.sh) | `PROVERS_FMIDE` |

All three default to `true`.  Set one to anything else to skip it, either when
creating the VM or when running `provers-setup.sh` directly:

```bash
PROVERS_IVE=false PROVERS_CODEIVE=false bash setup.sh
```

`bin/sireum.sh` installs Sireum itself and none of them, so the command-line
tools work with all three declined.  One wrinkle: `sireum setup ive` and
`sireum setup vscode` both run Sireum's `Init.deps()`, which is what installs
Logika's solvers (Z3, CVC) among other extras -- so with either IDE selected
the solvers arrive with it.  With both declined, `bin/sireum.sh` installs the
two solvers directly instead, so Logika still works.

### Architecture

The scripts run on x86_64 and aarch64.  `bin/versions.sh` derives everything
that differs -- Rust host triple, Microkit SDK tarball, cross-toolchain build,
`$VERUS_Z3_PATH` -- from `uname -m`, so the same pins drive both.

Three things are prebuilt on x86_64 and have to be built from source on
aarch64, which is most of why an aarch64 setup takes considerably longer:

| | x86_64 | aarch64 |
| --- | --- | --- |
| Z3 | bundled in the Verus release | built by `bin/z3.sh` |
| Verus | published release asset | built with `vargo` against that Z3 |
| sdfgen | PyPI wheel | built from source, which needs zig (`ZIG_VER`) |

## Using The Prebuilt OVA

A VM built by the steps below, cleaned up and exported as described in
[Exporting An OVA](#exporting-an-ova), is available as an appliance:

* [provers-env.ova](https://drive.google.com/file/d/1GFuthWnaLRnPwMoOwR_hU7_4tFs5UXyg/view?usp=drive_link)
  -- 12.39 GB, exported 2026-08-04

This OVA is x86_64, so it needs an **x86_64 host**; VirtualBox on Apple Silicon
cannot run it, and there is no arm64 OVA published yet.  Import it into
VirtualBox 7.0 or above -- `File > Import Appliance...`, or:

```bash
VBoxManage import provers-env.ova
```

Then start it and log in as `vagrant` / `vagrant`.  The virtual disk comes from
the same 64 GB base box the Vagrant setup uses, so leave room for it to grow.

It holds what [What Gets Installed](#what-gets-installed) describes, at the
versions [bin/versions.sh](../bin/versions.sh) pinned as of
[5edff3d](https://github.com/loonwerks/INSPECTA-models/commit/5edff3d306b7527f12141ef578eb230a3ec30d7d).
That is a snapshot rather than a moving target, so to pick up anything pinned
since, re-run the relevant per-tool script from [Post Setup](#post-setup) inside
the VM, or build the VM yourself as below.

## Setting Up A VirtualBox VM Using Vagrant

### Requirements

* [VirtualBox](https://www.virtualbox.org/) 7.1 or above.  Either architecture
  works, but only the host's own: VirtualBox virtualizes x86 on x86 and ARM on
  ARM, and refuses the other with `VBOX_E_PLATFORM_ARCH_NOT_SUPPORTED`.  So an
  x86_64 OVA has to be built on an x86_64 machine, and an arm64 one on Apple
  Silicon or another aarch64 host

* [Vagrant](https://www.vagrantup.com/) 2.4 or above

* ~60 GB free disk and a few hours (the Microkit SDK build, the Sireum/IVE
  build, Rust and TeX Live dominate)

The [Vagrantfile](Vagrantfile) selects the box architecture from the host, so
no configuration is needed either way; `PROVERS_ARCH` overrides it if you have
reason to.  Note that an aarch64 build compiles Z3, Verus and sdfgen from
source, so it takes considerably longer -- see [Architecture](#architecture).

### Notes

By default the VM is configured with 4 vCPUs, 8 GB RAM, 64 MB video memory and
an XFCE desktop.  Override any of them via environment variables:

```bash
PROVERS_CPUS=8 PROVERS_MEMORY=16384 bash setup.sh   # recommended if you run the IDEs
PROVERS_DESKTOP=false bash setup.sh                 # headless (the IDEs then need X forwarding)
PROVERS_IVE=false PROVERS_FMIDE=false bash setup.sh # skip individual IDEs
PROVERS_ARCH=amd64 bash setup.sh                    # override the box architecture
PROVERS_VM_NAME=provers bash setup.sh               # fix the VirtualBox machine name
```

The machine is named `provers-env-<arch>-<date>` unless `PROVERS_VM_NAME` says
otherwise.  Both parts matter once you build for more than one architecture:
VirtualBox refuses a duplicate name, and the name is what an exported appliance
inherits.

If Canonical's archives are unreachable or slow, point apt somewhere else with
`PROVERS_APT_MIRROR` (and optionally `PROVERS_APT_SECURITY_MIRROR`, which
defaults to the same host, as full mirrors carry the `-security` suite):

```bash
PROVERS_APT_MIRROR=https://mirrors.kernel.org/ubuntu/ bash setup.sh
```

The mirror is validated before anything is installed, and the change can also be
applied to an existing VM by re-running [bin/apt-mirror.sh](../bin/apt-mirror.sh)
with the variable set.

The base box is
[bento/ubuntu-24.04](https://portal.cloud.hashicorp.com/vagrant/discover/bento/ubuntu-24.04),
pinned in the [Vagrantfile](Vagrantfile) to version `202510.26.0` (Ubuntu
24.04.4 LTS, kernel 6.8.0-137) so that rebuilds reproduce the same base image.
Bump `config.vm.box_version` deliberately, and re-run the build to confirm the
new base still works, rather than leaving it to float.

The disk size comes from that box, which is 64 GB.  Ubuntu's installer formats
only about half of that as the root volume and leaves the rest as free extents
in the volume group, which is
not enough headroom for the full install, so [bin/disk.sh](../bin/disk.sh) grows
root into those extents (to ~62 GB) as the first provisioning step.  Only if you
need more than the box's physical 64 GB do you have to install the
`vagrant-disksize` plugin and uncomment the `config.disksize.size` line in the
[Vagrantfile](Vagrantfile).

### Guest Additions

They provide clipboard sharing, drag-and-drop, guest display resizing and the
`/vagrant` shared folder.  How they arrive depends on the architecture:

* **x86_64** -- the `vagrant-vbguest` plugin installs them, as it always has.

* **aarch64** -- the plugin cannot: it invokes `VBoxLinuxAdditions.run` by name
  and the ISO's ARM installer is `VBoxLinuxAdditions-arm64.run`, so it would run
  the x86 build.  Two provisioners do it instead.  `guest-additions` runs the ARM
  installer with `--nox11` before the desktop exists, which is what gets the
  kernel modules in; `desktop-additions` re-runs it afterwards to add the display
  driver and `VBoxClient`.  Without that second step the desktop does not follow
  the window when it is resized.

The `/vagrant` share is disabled on aarch64 by default.  Vagrant mounts shared
folders before provisioners run, so on the build that installs the additions it
cannot mount yet -- and a failed mount aborts `vagrant up`.  Nothing in the build
needs it: `provers-setup.sh` and `bin/` arrive through file provisioners, which
use SSH.  Once a VM has the additions, `PROVERS_SYNCED_FOLDER=true` turns it on
for later ups.

### Setup

Run the following in this directory:

* macOS/Linux

  ```bash
  bash setup.sh
  ```

* Windows

  ```bash
  setup.bat
  ```

The setup is fully automatic; there is no need to interact with Vagrant or the
VM until it is done.  With the desktop enabled, a successful setup ends with an
X11 login screen -- log in as `vagrant` / `vagrant`.  Otherwise:

```bash
vagrant ssh
```

Once it is up, taking a VM snapshot in VirtualBox makes it easy to roll back
later.

If the setup fails part-way (e.g. the network drops during provisioning),
destroy the VM before re-running `setup.sh`:

```bash
vagrant destroy
```

### What Is In This VM

`~/provers/build-info` records the versions of everything installed, the
architecture, which IDEs were selected and when it was built.  It is written at
the end of provisioning, is shell-sourceable, and is also copied to
`/etc/provers-env-release`.  `bin/prep-export.sh` reads it to fill in the OVA's
product, version and description fields, so an exported appliance answers the
same question from `VBoxManage import -n` without being started.

Re-run `bash ~/bin/build-info.sh` after updating a tool to refresh it.

### Smoke Test

Inside the VM:

```bash
verus --version
sireum --version
# sdfgen is a python library, not a CLI -- it ships no console script
python3 -c 'import sdfgen, importlib.metadata as m; print("sdfgen", m.version("sdfgen"))'
ls $MICROKIT_SDK/board
qemu-system-aarch64 --version
ive    # and codium, fmide -- from the desktop session
```

### Exporting An OVA

To hand the finished VM to someone as an appliance, clean it up first --
[bin/prep-export.sh](../bin/prep-export.sh) drops the caches left behind by the
build (the apt archive cache alone is usually a few GB) and then overwrites the
free space with zeros, which is what actually shrinks the exported image:
deleting a file frees its blocks but leaves the old contents on the virtual
disk, and that garbage does not compress.

```bash
vagrant ssh -c 'PROVERS_EXPORT_DRYRUN=true bash ~/bin/prep-export.sh'   # report only
vagrant ssh -c 'TZ=America/Chicago bash ~/bin/prep-export.sh'          # do it
```

It finishes by printing the full `VBoxManage export` command to run on the host,
with the metadata already filled in from `build-info` -- versions, build date,
which IDEs are installed -- so the appliance answers "what is in this?" without
being started.

The name it chooses is `provers-env-<arch>-<build date>`, e.g.
`provers-env-arm64-2026.08.13`, and it is passed as `--vmname` so that it is both
the OVA's filename and what VirtualBox calls the VM on import.

Set `TZ` to the building host's zone, as above.  `build-info` records the build
in UTC, so a build finishing in the evening is already the next day in UTC and
would otherwise be named a day late.  `PROVERS_OVA_DATE=YYYY.MM.DD` states the
date outright, and `PROVERS_OVA_NAME` replaces the whole name, for exporting on
a different machine or long after the build.

Take the machine to export from `VBoxManage list vms` rather than assuming it:
Vagrant re-applies the configured name on every `vagrant up`, and that name is
dated, so a VM built one evening is renamed by the next morning's boot.  The
printed command looks it up for you.  `--vmname` is what keeps the appliance
correct regardless.

The Sireum build output (`$SIREUM_HOME/out`) goes with it -- the next build
recreates it, and nothing has to be re-downloaded.  The *dependency* caches are
kept: Sireum's coursier cache (`$SIREUM_HOME/lib/cache`) and the cargo registry
are what let the exported image resolve dependencies without reaching
crates.io or Maven Central, which is much of the point of handing an appliance
to a restricted site.  `PROVERS_EXPORT_DEEP=true` drops those too, where the
network is known to be open and size matters more.

Halt with `vagrant halt` rather than shutting down from inside the desktop
session.  Ubuntu 24.04 guests can hang at the very end of shutdown under
VirtualBox -- everything stops correctly, including the unmount of `/vagrant`,
and then the guest spins instead of powering off.  `vagrant halt` forces the
power off after `graceful_halt_timeout` (60s), so it rides through this; a
shutdown started inside the guest just hangs.  If it does hang, the VM has
already finished its filesystem work, so
`VBoxManage controlvm provers-env poweroff` is safe.

Note this is a manual step, deliberately kept out of provisioning -- the caches
it removes are worth keeping in a VM you are still working in.

## Setting Up A Dedicated Linux Machine

### Requirement

* Ubuntu 24.04, x86_64 or aarch64, with `sudo` available to the invoking user

### Steps

```bash
bash provers-setup.sh
```

then open a new shell to pick up the environment.  `provers-setup.sh` uses the
scripts in `bin/` next to it; in the Vagrant VM those same scripts are installed
to `~/bin`.

## Post Setup

Each step is a standalone script and can be re-run on its own to update or
repair that tool -- from `~/bin` in the VM, or from `bin/` on a dedicated
machine:

| script | what it (re-)installs |
| --- | --- |
| `disk.sh` | grows root into unused extents in its LVM volume group (VM only) |
| `apt-mirror.sh` | points apt at `PROVERS_APT_MIRROR` (no-op when unset) |
| `deps.sh` | apt build/runtime/GUI dependencies |
| `rust.sh` | rustup and the pinned toolchains |
| `z3.sh` | Z3 (`Z3_VER`), built from source; no-op on x86_64 (see [Architecture](#architecture)) |
| `verus.sh` | Verus |
| `microkit-lionsos.sh` | sdfgen venv, released Microkit SDK, LionsOS |
| `microkit-domains.sh` | Microkit SDK 1.4.1 with domain scheduling |
| `sireum.sh` | Sireum itself (no IDEs) |
| `slim.sh` | deletes build leftovers and caches; not run by this setup (see [What Gets Installed](#what-gets-installed)) |
| `ive.sh` | Sireum IVE |
| `codeive.sh` | CodeIVE |
| `fmide.sh` | FMIDE |
| `firefox.sh` | Firefox, from Mozilla's apt repo rather than the Ubuntu snap (desktop only) |
| `prep-export.sh` | cache cleanup + zero-fill before an OVA export (manual, VM only) |

To move to a newer Verus, for example:

```bash
VERUS_VER=<new-version> bash ~/bin/verus.sh
```

and update [bin/versions.sh](../bin/versions.sh) to make it the default.

To move Sireum off the pinned revision, to a particular kekinian branch, tag or
commit:

```bash
SIREUM_V=<branch|tag|sha> bash ~/bin/sireum.sh
```

`SIREUM_V` selects the source that gets built; `SIREUM_INIT_V` selects the
release whose prebuilt `sireum.jar` bootstraps that build.  `bin/init.sh`
derives the second from the first, but only when `SIREUM_V` is a `4.*` release
tag -- given a bare commit SHA it derives the release `4.<sha>`, which does not
exist.  So pass both when pinning to a SHA:

```bash
SIREUM_V=<sha> SIREUM_INIT_V=<4.x release tag> bash ~/bin/sireum.sh
```

To pin the FMIDE component versions (see `$SIREUM_HOME/bin/install/fmide.cmd
--help` for the full list):

```bash
FMIDE_ARGS="--osate 2.14.0" bash ~/bin/fmide.sh
```

`SIREUM_V` and `FMIDE_ARGS` are also forwarded from the host into the VM during
provisioning.

### Optionals

* [CLion](https://www.jetbrains.com/clion/), for browsing HAMR generated C code
  (license required / free 30-day evaluation):

  ```bash
  $SIREUM_HOME/bin/install/clion.cmd
  ```

* Other tools shipped with kekinian's installers -- `alt-ergo`, `compcert`,
  `isabelle`, `rocq`, `rustrover`, ... -- are all under
  `$SIREUM_HOME/bin/install`.
