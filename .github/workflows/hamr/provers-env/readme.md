# PROVERS Environment Setup

This folder contains scripts and a `Vagrantfile` that set up the DARPA PROVERS
development environment.  You can get that environment either:

* [by importing a prebuilt OVA](#using-the-prebuilt-ova),

* [by provisioning a Linux VM using VirtualBox and Vagrant](#setting-up-a-virtualbox-vm-using-vagrant), or

* [in a dedicated Linux machine](#setting-up-a-dedicated-linux-machine).

It installs the same command-line tools, at the same versions and in the same
layout, as
[Dockerfile.provers_LinuxAMD64](../docker/Dockerfile.provers_LinuxAMD64), and
adds the GUI development tools that do not belong in a CI image: the Sireum
IVE, CodeIVE and FMIDE.  If you only need the command-line tools, use the
container instead:

```bash
docker run -it --rm jasonbelt/microkit_provers:latest
```

## Contents

* [What Gets Installed](#what-gets-installed)
  * [Launching the IDEs](#launching-the-ides)
* [Using The Prebuilt OVA](#using-the-prebuilt-ova)
* [Setting Up A VirtualBox VM Using Vagrant](#setting-up-a-virtualbox-vm-using-vagrant)
  * [Requirements](#requirements)
  * [Notes](#notes)
  * [Setup](#setup)
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
| `$VERUS_DIR` | Verus (`VERUS_VER`), including its bundled Z3 (`$VERUS_Z3_PATH`) |
| `$MICROKIT_SDK` | Microkit SDK 1.4.1 built from source with experimental domain scheduling support |
| `$MICROKIT_SDK_CURRENT` | the released Microkit SDK (`MICROKIT_SDK_VER`) |
| `$LIONSOS` | LionsOS, with `$VMM_DIR` pointing at `dep/libvmm` |
| `$SIREUM_HOME` | a full Sireum (kekinian) install, plus the IVE and CodeIVE |
| `$SIREUM_HOME/bin/linux/fmide` | FMIDE, the OSATE-based AADL IDE |
| `$SDFGEN_VENV` | a python venv holding `sdfgen` (`SDFGEN_VER`) |
| `~/.cargo` | Rust (`RUST_TOOLCHAIN_VER` + `RUST_NIGHTLY_VER`), required by Verus and the SDK build |

Those variables, plus `MICROKIT_BOARD` (default `qemu_virt_aarch64`) and the
`PATH` additions, are defined in [bin/env.sh](bin/env.sh), which the setup
sources from `~/.bashrc`.

Tool versions are pinned in [bin/versions.sh](bin/versions.sh) and can each be
overridden by exporting the corresponding variable before running the setup.

Differences from the Dockerfile, all because this is a development environment
rather than a shipped CI image:

* Sireum is installed in full via `bin/build.cmd setup` (which also builds the
  IVE) followed by `sireum setup vscode` (CodeIVE), rather than the minimal
  `init.sh` install.  **Nothing is deleted afterwards** -- the bundled JDK,
  JavaFX, sbt, etc. are all left in place.

* FMIDE is installed via `bin/install/fmide.cmd`.

* The LionsOS examples, `.git` directory and `dep/micropython` /
  `dep/wasm-micro-runtime` are kept.

* GTK/X11 runtime libraries are installed so the three IDEs can actually run.

### Launching the IDEs

[bin/functions.sh](bin/functions.sh) is sourced from `~/.bashrc` and defines a
shell function per IDE.  Each takes an optional directory and defaults to the
current one:

```bash
ive                    # Sireum IVE on $PWD
codium ~/provers       # CodeIVE on ~/provers
fmide                  # FMIDE on $PWD
```

They resolve the right path for the architecture (`bin/linux` vs
`bin/linux/arm`) and background the process so the terminal stays usable.

## Using The Prebuilt OVA

A VM built by the steps below, cleaned up and exported as described in
[Exporting An OVA](#exporting-an-ova), is available as an appliance:

* [provers-env.ova](https://drive.google.com/file/d/1GFuthWnaLRnPwMoOwR_hU7_4tFs5UXyg/view?usp=drive_link)
  -- 12.39 GB, exported 2026-08-04

Import it into VirtualBox 7.0 or above on an **x86_64 host** -- `File > Import
Appliance...`, or:

```bash
VBoxManage import provers-env.ova
```

Then start it and log in as `vagrant` / `vagrant`.  The virtual disk comes from
the same 64 GB base box the Vagrant setup uses, so leave room for it to grow.

It holds what [What Gets Installed](#what-gets-installed) describes, at the
versions [bin/versions.sh](bin/versions.sh) pinned as of
[5edff3d](https://github.com/loonwerks/INSPECTA-models/commit/5edff3d306b7527f12141ef578eb230a3ec30d7d).
That is a snapshot rather than a moving target, so to pick up anything pinned
since, re-run the relevant per-tool script from [Post Setup](#post-setup) inside
the VM, or build the VM yourself as below.

## Setting Up A VirtualBox VM Using Vagrant

### Requirements

* [VirtualBox](https://www.virtualbox.org/) 7.0 or above, on an **x86_64 host**

* [Vagrant](https://www.vagrantup.com/) 2.4 or above

* ~60 GB free disk and a few hours (the Microkit SDK build, the Sireum/IVE
  build, Rust and TeX Live dominate)

On an Apple Silicon / aarch64 host use the
[ARM64 container image](../docker/Dockerfile.provers_LinuxARM64) instead --
this VM is x86_64 only.

### Notes

By default the VM is configured with 4 vCPUs, 8 GB RAM, 64 MB video memory and
an XFCE desktop.  Override any of them via environment variables:

```bash
PROVERS_CPUS=8 PROVERS_MEMORY=16384 bash setup.sh   # recommended if you run the IDEs
PROVERS_DESKTOP=false bash setup.sh                 # headless (the IDEs then need X forwarding)
```

If Canonical's archives are unreachable or slow, point apt somewhere else with
`PROVERS_APT_MIRROR` (and optionally `PROVERS_APT_SECURITY_MIRROR`, which
defaults to the same host, as full mirrors carry the `-security` suite):

```bash
PROVERS_APT_MIRROR=https://mirrors.kernel.org/ubuntu/ bash setup.sh
```

The mirror is validated before anything is installed, and the change can also be
applied to an existing VM by re-running [bin/apt-mirror.sh](bin/apt-mirror.sh)
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
not enough headroom for the full install, so [bin/disk.sh](bin/disk.sh) grows
root into those extents (to ~62 GB) as the first provisioning step.  Only if you
need more than the box's physical 64 GB do you have to install the
`vagrant-disksize` plugin and uncomment the `config.disksize.size` line in the
[Vagrantfile](Vagrantfile).

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
[bin/prep-export.sh](bin/prep-export.sh) drops the caches left behind by the
build (the apt archive cache alone is usually a few GB) and then overwrites the
free space with zeros, which is what actually shrinks the exported image:
deleting a file frees its blocks but leaves the old contents on the virtual
disk, and that garbage does not compress.

```bash
vagrant ssh -c 'PROVERS_EXPORT_DRYRUN=true bash ~/bin/prep-export.sh'   # report only
vagrant ssh -c 'bash ~/bin/prep-export.sh'                             # do it
vagrant halt
VBoxManage export provers-env -o provers-env.ova --vsys 0 \
    --product 'DARPA PROVERS development environment'
```

`PROVERS_EXPORT_DEEP=true` additionally drops the Sireum build output and the
cargo/coursier caches; the installed tools still run, only rebuilding them from
source gets slower.  The script prints the export command when it finishes.

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

* Ubuntu 24.04 (amd64), with `sudo` available to the invoking user

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
| `verus.sh` | Verus |
| `microkit-lionsos.sh` | sdfgen venv, released Microkit SDK, LionsOS |
| `microkit-domains.sh` | Microkit SDK 1.4.1 with domain scheduling |
| `sireum.sh` | Sireum, IVE and CodeIVE |
| `fmide.sh` | FMIDE |
| `firefox.sh` | Firefox, from Mozilla's apt repo rather than the Ubuntu snap (desktop only) |
| `prep-export.sh` | cache cleanup + zero-fill before an OVA export (manual, VM only) |

To move to a newer Verus, for example:

```bash
VERUS_VER=<new-version> bash ~/bin/verus.sh
```

and update [bin/versions.sh](bin/versions.sh) to make it the default.

To move Sireum to a particular kekinian branch, tag or commit:

```bash
SIREUM_V=<branch|tag|sha> bash ~/bin/sireum.sh
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
