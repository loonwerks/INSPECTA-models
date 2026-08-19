# PROVERS Environment Setup

This folder contains scripts and a `Vagrantfile` that set up the DARPA PROVERS
development environment.  You can get that environment either:

* [by importing a prebuilt OVA](#using-the-prebuilt-ova),

* [by provisioning a Linux VM using VirtualBox and Vagrant](#setting-up-a-virtualbox-vm-using-vagrant),

* [natively on an Apple Silicon Mac](#setting-up-an-apple-silicon-mac), or

* [natively on an Ubuntu 24.04 machine](#setting-up-an-ubuntu-2404-machine).

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
  * [What Ends Up In Your Environment](#what-ends-up-in-your-environment)
  * [Launching the IDEs](#launching-the-ides)
  * [Choosing the IDEs](#choosing-the-ides)
  * [Hosts](#hosts)
* [Using The Prebuilt OVA](#using-the-prebuilt-ova)
* [Setting Up A VirtualBox VM Using Vagrant](#setting-up-a-virtualbox-vm-using-vagrant)
  * [Requirements](#requirements)
  * [Notes](#notes)
  * [Guest Additions](#guest-additions)
  * [Setup](#setup)
  * [What Is In This VM](#what-is-in-this-vm)
  * [Smoke Test](#smoke-test)
  * [Exporting An OVA](#exporting-an-ova)
* [Setting Up An Apple Silicon Mac](#setting-up-an-apple-silicon-mac)
  * [Requirements](#requirements-1)
  * [Steps](#steps)
  * [Using A Sireum You Already Have](#using-a-sireum-you-already-have)
  * [What It Changes On The Machine](#what-it-changes-on-the-machine)
  * [Smoke Test](#smoke-test-1)
* [Setting Up An Ubuntu 24.04 Machine](#setting-up-an-ubuntu-2404-machine)
  * [Requirements](#requirements-2)
  * [Steps](#steps-1)
* [Post Setup](#post-setup)
  * [Optionals](#optionals)

## What Gets Installed

Everything lands under `$PROVERS_DIR` (default `~/provers`):

| | |
| --- | --- |
| `$VERUS_DIR` | Verus (`VERUS_VER`); `$VERUS_Z3_PATH` points at the Z3 it runs with |
| `$MICROKIT_SDK` | the released Microkit SDK (`MICROKIT_SDK_VER`), with its `microkit` tool rebuilt to carry the [seL4/microkit#586](https://github.com/seL4/microkit/pull/586) vCPU domain fix -- without it a domain-scheduled virtual machine never receives its guest's virtual timer interrupt.  `$MICROKIT_SDK/VCPU-DOMAIN-PATCH` records what was changed |
| `$LIONSOS` | LionsOS at the pinned commit (`LIONSOS_VER`).  Only the `dep/sddf` and `dep/libvmm` submodules are checked out -- the generated Microkit makefiles use sDDF and the VM examples use libvmm; the rest is LionsOS the operating system, which nothing here builds |
| `$SIREUM_HOME` | a full Sireum (kekinian) install, built from `SIREUM_V` |
| `$SIREUM_PLATFORM_BIN/idea` | Sireum IVE, the IntelliJ-based IDE (optional) |
| `$SIREUM_PLATFORM_BIN/vscodium` | CodeIVE, the VSCodium-based IDE (optional) |
| `$SIREUM_PLATFORM_BIN/fmide` | FMIDE, the OSATE-based AADL IDE (optional) |

`$SIREUM_PLATFORM_BIN` is where Sireum puts this host's binaries: `bin/linux` on
x86_64 Linux, `bin/linux/arm` on aarch64 Linux, and `bin/mac` on macOS -- where
each IDE is an `.app` bundle (`idea/IVE.app`, `vscodium/CodeIVE.app`, and
`fmide.app` directly under `bin/mac`).  `bin/env.sh` works it out; the launchers
below use it, so you should not have to.
| `$SDFGEN_VENV` | a python venv holding `sdfgen` (`SDFGEN_VER`) |
| `~/.cargo` | Rust (`RUST_TOOLCHAIN_VER`), required by Verus, the generated crates and the Microkit tool rebuild |

Those paths are defined in [bin/env.sh](../bin/env.sh), which the setup sources
from the shell startup file -- `~/.bashrc` under bash on Linux,
`~/.bash_profile` under bash on macOS, `~/.zshrc` under zsh.

### What Ends Up In Your Environment

Four variables and the `PATH` additions, and nothing else:

| | |
| --- | --- |
| `SIREUM_HOME` | the Sireum install; its launcher and HAMR codegen read it |
| `MICROKIT_SDK` | generated Makefiles stop with an explicit error without it |
| `MICROKIT_BOARD` | likewise; defaults to `qemu_virt_aarch64` |
| `LIONSOS` | the models' `system.mk` stops without it, and derives `SDDF` and `LIBVMM` from it |

That is the contract: what a *build* needs, and what you may rely on.  Everything
else in `bin/versions.sh` and `bin/env.sh` -- the pinned versions, the repo URLs,
`$VERUS_DIR`, `$Z3_DIR`, `$SDFGEN_VENV`, the per-host derivations -- is internal
to the install scripts, which each source `bin/env.sh` and so see those values
without them having to be exported into every command you subsequently run.  It
used to export all forty.

The tools themselves are reached through `PATH` rather than through variables:
`$VERUS_DIR`, `$SIREUM_HOME/bin`, the sdfgen venv, `~/.cargo/bin`, and on macOS
Homebrew's keg-only `llvm` and GNU `make` directories.

Tool versions are pinned in [bin/versions.sh](../bin/versions.sh) and can each be
overridden by exporting the corresponding variable before running the setup.
Sireum is pinned there too, to a kekinian revision rather than tracking
`master`, so that setups run weeks apart install the same thing.  That file is
the single source of truth for the VM and the container alike; see
[../docker/readme.md](../docker/readme.md) for how the image consumes it.

This setup runs the scripts with `PROVERS_DEPS_PROFILE=vm` and
`PROVERS_SIREUM_PROFILE=full`, and does not run `bin/slim.sh`, so the VM keeps
what a shipped image drops: the LionsOS examples and its `.git` directory, the
full JDK including JavaFX, and the build caches.

### Launching the IDEs

[bin/functions.sh](../bin/functions.sh) is sourced from `~/.bashrc` and defines a
shell function per IDE.  Each takes an optional directory and defaults to the
current one:

```bash
ive                    # Sireum IVE on $PWD
codium ~/provers       # CodeIVE on ~/provers
fmide                  # FMIDE on $PWD
```

They resolve the right path for the host (`bin/linux`, `bin/linux/arm` or
`bin/mac`) and background the process so the terminal stays usable.  On macOS
they launch the `.app` bundle through `open -na`, which opens a second instance
rather than raising the running one, so `ive ~/a` and `ive ~/b` give you two
windows as they do on Linux.

### Choosing the IDEs

Each IDE is installed by its own script and can be skipped:

| | script | knob |
| --- | --- | --- |
| Sireum IVE | [bin/ive.sh](../bin/ive.sh) | `PROVERS_IVE` |
| CodeIVE | [bin/codeive.sh](../bin/codeive.sh) | `PROVERS_CODEIVE` |
| FMIDE | [bin/fmide.sh](../bin/fmide.sh) | `PROVERS_FMIDE` |

**Whether they are on by default depends on what you are building.**  A VM is a
desktop image whose point is the GUI tools, so `vagrant/Vagrantfile` turns all
three on; a container has no use for them and the Dockerfile leaves them off.
A bare-metal or Mac install gets none of them unless asked -- `bin/env.sh`
defaults all three to `false`, since a machine that is already somebody's own
should not have 3GB of IntelliJ pushed onto it uninvited.

So when creating the VM, name the ones you want to *skip*:

```bash
PROVERS_IVE=false PROVERS_CODEIVE=false bash setup.sh
```

and when running `provers-setup.sh` directly, name the ones you want:

```bash
PROVERS_IVE=true PROVERS_FMIDE=true bash provers-setup.sh
```

`bin/sireum.sh` installs Sireum itself and none of them, so the command-line
tools work with all three declined.  One wrinkle: `sireum setup ive` and
`sireum setup vscode` both run Sireum's `Init.deps()`, which is what installs
Logika's solvers (Z3, CVC) among other extras -- so with either IDE selected
the solvers arrive with it.  With both declined -- which is now the default off
a VM -- `bin/sireum.sh` installs the two solvers directly instead, so Logika
still works either way.

FMIDE is installed with `-v`.  It is the longest single step of a setup and
prints nothing without it, which is hard to tell apart from a hang; set
`FMIDE_ARGS="--verbose+"` for more still.

### Hosts

The scripts run on Ubuntu 24.04 (x86_64 and aarch64) and on macOS (Apple
Silicon).  `bin/versions.sh` derives everything that differs -- package manager,
Rust host triple, Microkit SDK tarball, Verus release asset, `$VERUS_Z3_PATH` --
from `uname -s` and `uname -m`, so one set of pins drives all three and
`provers-setup.sh` is the same command everywhere.

Three things are prebuilt on some hosts and have to be built from source on
others, which is most of why the setups differ so much in how long they take:

| | Ubuntu x86_64 | Ubuntu aarch64 | macOS arm64 |
| --- | --- | --- | --- |
| packages | apt | apt | Homebrew |
| Z3 | bundled in the Verus release | built by `bin/z3.sh` | bundled in the Verus release |
| Verus | published release asset | built with `vargo` against that Z3 | published release asset |
| sdfgen | PyPI wheel | built from source, which needs zig (`ZIG_VER`) | PyPI wheel |

So aarch64 Linux is the slow one and Apple Silicon the quick one: upstream
publishes an arm64 macOS asset for all three, so a Mac builds none of them.

The `microkit` tool is rebuilt from source on all three, but that is a small
cargo build rather than a full SDK build -- see
[microkit-vcpu-domain.sh](../bin/microkit-vcpu-domain.sh).

## Using The Prebuilt OVA

A VM built by the steps below, cleaned up and exported as described in
[Exporting An OVA](#exporting-an-ova), is available as an appliance for both
architectures:

| host | appliance | size | exported | versions pinned as of |
| --- | --- | --- | --- | --- |
| Apple Silicon / aarch64 | [provers-env-arm64-2026.08.13.ova](https://drive.google.com/file/d/1Ts_zRfmRGkWSU2jz1-j_AthrZ1doUD0K/view?usp=sharing) | 12.61 GB | 2026-08-17 | [52a1822](https://github.com/loonwerks/INSPECTA-models/commit/52a18225) |
| x86_64 | [provers-env-amd64-2026.08.13.ova](https://drive.google.com/file/d/16-7AmlTBj9AsrHB80anUvI8fV7Qdt1YE/view?usp=sharing) | 12.32 GB | 2026-08-17 | [52a1822](https://github.com/loonwerks/INSPECTA-models/commit/52a18225) |

Both are named for `PROVERS_BUILD_VER` rather than for the day they were
written: a rebuild that *replaces* a published build keeps that build's version,
which is why the pair above are named 2026.08.13 but were exported later.  See
[Exporting An OVA](#exporting-an-ova).

The `versions pinned as of` column is the commit whose `bin/versions.sh` each
appliance was built from, and it is worth reading.  The pair above predate the
move to a single patched Microkit SDK and the 2026.08 Verus toolchain, so a VM
built from the current scripts installs a different set; publishing a matching
appliance means rebuilding and re-exporting.

An [earlier x86_64 appliance](https://drive.google.com/file/d/1GFuthWnaLRnPwMoOwR_hU7_4tFs5UXyg/view?usp=drive_link)
(2026-08-04, pinned at
[5edff3d](https://github.com/loonwerks/INSPECTA-models/commit/5edff3d306b7527f12141ef578eb230a3ec30d7d))
remains available, but the pair above are built from the same pins and are what
to use unless you need to reproduce something against the older one.

Take the one matching your host, not your preference: VirtualBox virtualizes
only its own architecture, so the x86_64 appliance will not start on Apple
Silicon and vice versa.  Import into VirtualBox 7.1 or above --
`File > Import Appliance...`, or:

```bash
VBoxManage import provers-env-arm64-2026.08.13.ova
```

Then start it and log in as `vagrant` / `vagrant`.  The virtual disk comes from
the same 64 GB base box the Vagrant setup uses, so leave room for it to grow.

Each holds what [What Gets Installed](#what-gets-installed) describes, at the
versions [bin/versions.sh](../bin/versions.sh) pinned in the commit above.  That
is a snapshot rather than a moving target, so to pick up anything pinned since,
re-run the relevant per-tool script from [Post Setup](#post-setup) inside the VM,
or build the VM yourself as below.

What is actually in an appliance is recorded inside it, so it can be checked
without guessing from the name -- `cat ~/provers/build-info` in the running VM,
or before importing:

```bash
VBoxManage import provers-env-arm64-2026.08.13.ova -n
```

which prints the tool versions and build date as the appliance's description.

## Setting Up A VirtualBox VM Using Vagrant

### Requirements

* [VirtualBox](https://www.virtualbox.org/) 7.1 or above.  Either architecture
  works, but only the host's own: VirtualBox virtualizes x86 on x86 and ARM on
  ARM, and refuses the other with `VBOX_E_PLATFORM_ARCH_NOT_SUPPORTED`.  So an
  x86_64 OVA has to be built on an x86_64 machine, and an arm64 one on Apple
  Silicon or another aarch64 host

* [Vagrant](https://www.vagrantup.com/) 2.4 or above

* ~60 GB free disk and a few hours (the Sireum/IVE build, Rust and TeX Live
  dominate, plus Z3 and Verus on aarch64)

The [Vagrantfile](Vagrantfile) selects the box architecture from the host, so
no configuration is needed either way; `PROVERS_ARCH` overrides it if you have
reason to.  Note that an aarch64 build compiles Z3, Verus and sdfgen from
source, so it takes considerably longer -- see [Hosts](#hosts).

### Notes

By default the VM is configured with 4 vCPUs, 8 GB RAM, 64 MB video memory and
an XFCE desktop.  Override any of them via environment variables:

```bash
PROVERS_CPUS=8 PROVERS_MEMORY=16384 bash setup.sh   # recommended if you run the IDEs
PROVERS_DESKTOP=false bash setup.sh                 # headless (the IDEs then need X forwarding)
PROVERS_IVE=false PROVERS_FMIDE=false bash setup.sh # skip individual IDEs (a VM installs all three)
PROVERS_ARCH=amd64 bash setup.sh                    # override the box architecture
PROVERS_VM_NAME=provers bash setup.sh               # fix the VirtualBox machine name
```

The machine is named `provers-env-<arch>-<build version>` unless
`PROVERS_VM_NAME` says otherwise.  Both parts matter once you build for more
than one architecture: VirtualBox refuses a duplicate name, and the name is what
an exported appliance inherits.

The build version is `PROVERS_BUILD_VER` from
[bin/versions.sh](../bin/versions.sh) -- the same one the container tags and the
OVA carry.  It used to be the day of the last `vagrant up` rather than of the
build, since Vagrant re-applies the configured name every time, so a VM built
one evening was renamed by the next morning's boot; a pin does not drift.  It
does still change if you change the pin, so look a registered VM up with
`VBoxManage list vms` rather than assuming.  Appliances are unaffected either
way; see [Exporting An OVA](#exporting-an-ova).

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
vagrant ssh -c 'PROVERS_EXPORT_DRYRUN=true TZ=America/Chicago bash ~/bin/prep-export.sh'   # report only
vagrant ssh -c 'TZ=America/Chicago bash ~/bin/prep-export.sh'                              # do it
```

It finishes by printing the full `VBoxManage export` command to run on the host,
with the metadata already filled in from `build-info` -- versions, build date,
which IDEs are installed -- so the appliance answers "what is in this?" without
being started.

The name it chooses is `provers-env-<arch>-<build version>`, e.g.
`provers-env-arm64-2026.08.13`, and it is passed as `--vmname` so that it is both
the OVA's filename and what VirtualBox calls the VM on import.

The build version is `PROVERS_BUILD_VER`, pinned in
[bin/versions.sh](../bin/versions.sh) and recorded in `build-info`, so the OVA,
the VM it came from and the container image built from the same pins all carry
it.  That also means a rebuild meant to *replace* a published build keeps its
version rather than taking today's date -- set `PROVERS_BUILD_VER` to a new date
when publishing a genuinely new build.  `PROVERS_OVA_DATE=YYYY.MM.DD` overrides
it for one export, and `PROVERS_OVA_NAME` replaces the whole name.

Set `TZ` to the building host's zone, as above.  It matters only for an
appliance exported from a VM built before that pin existed, where the name falls
back to the build date `build-info` records in UTC -- a build finishing in the
evening is already the next day there, and would be named a day late.

Take the machine to export from `VBoxManage list vms` rather than assuming it:
Vagrant re-applies the configured name on every `vagrant up`, so a VM whose name
predates a change to `PROVERS_BUILD_VER` is renamed by its next boot.  The
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
already finished its filesystem work, so powering it off is safe:

```bash
VBoxManage controlvm "$(VBoxManage list runningvms | grep -o '"provers-env[^"]*"' | tr -d '"')" poweroff
```

Note this is a manual step, deliberately kept out of provisioning -- the caches
it removes are worth keeping in a VM you are still working in.

## Setting Up An Apple Silicon Mac

The same `provers-setup.sh` runs natively on macOS -- no VM, no container.  It is
the quickest of the setups here, because every upstream publishes an arm64 macOS
build: Verus and its Z3 are unpacked from a release and sdfgen from a PyPI
wheel, where an aarch64 Linux host has to compile all three.

Prefer this over the arm64 OVA unless you need the VM itself (a Linux
filesystem, a fixed appliance to hand out, or isolation from your own machine).

### Requirements

* macOS on Apple Silicon.  Intel Macs fall out of the same derivation in
  `bin/versions.sh` -- upstream publishes for them too -- but are not tested

* the Xcode Command Line Tools:

  ```bash
  xcode-select --install
  ```

* [Homebrew](https://brew.sh).  `bin/deps.sh` installs every OS dependency
  through it, but does not install Homebrew itself: its installer wants `sudo`
  and makes lasting changes to the machine, which is your call rather than a
  setup script's

* ~30 GB free disk, and an hour or two -- dominated by the Sireum build, which
  you can skip entirely if you already have Sireum (see below)

### Steps

```bash
bash provers-setup.sh
```

That installs the command-line tools only.  It reports what it is doing in a few
lines per step; `PROVERS_TRACE=true` adds the full shell trace, which is what to
reach for when a step fails.  The IDEs are opt-in -- add any of
`PROVERS_IVE=true`, `PROVERS_CODEIVE=true`, `PROVERS_FMIDE=true`:

```bash
PROVERS_IVE=true PROVERS_FMIDE=true bash provers-setup.sh
```

then open a new terminal to pick up the environment.  As on Linux, everything
lands under `$PROVERS_DIR` (default `~/provers`) and each step can be re-run on
its own from `bin/` -- see [Post Setup](#post-setup).

The environment is wired into `~/.zshrc`, since that is what a Mac logs in to.
`provers-setup.sh` picks the file from `$SHELL`, so a Mac you have switched to
bash gets `~/.bash_profile` -- not `~/.bashrc`, because Terminal.app starts bash
as a *login* shell and a login bash never reads `~/.bashrc` on its own.

### Using A Sireum You Already Have

A Mac running this is usually a development machine that already has a Sireum
checkout, and rebuilding it -- or worse, checking it out to the pinned
`SIREUM_V` over whatever you were working on -- is not what installing an
environment should do.  So export `SIREUM_HOME` at it and that install is
adopted:

```bash
SIREUM_HOME=~/devel/sireum/kekinian bash provers-setup.sh
```

`bin/sireum.sh` then reports what it found and stops, touching nothing, and the
rest of the setup refers to that install -- `PATH`, the IDE installers,
`build-info`.  The choice is recorded in the startup file, so new shells agree.

The test is "`SIREUM_HOME` is set and holds either `bin/sireum.jar` or
`bin/build.cmd`" -- a working install, or a kekinian checkout that has not been
built yet.  The second half matters: the jar is a build product, and kekinian
ignores all of `bin/` bar its tracked scripts, so a plain `git clone` has no jar
and a test for one alone would walk straight past it and check the clone out to
the pinned `SIREUM_V`.  A checkout with no jar is adopted and said to be
unbootstrapped, rather than bootstrapped on your behalf -- running its
`bin/sireum` would download a JDK, Scala and the jar into it, which is more than
this script should do to a tree it was told to leave alone.

Leave `SIREUM_HOME` unset and the pinned revision is cloned and built into
`$PROVERS_DIR/Sireum` as on any other host.

### What It Changes On The Machine

Everything this environment installs is under `$PROVERS_DIR` and is removed by
deleting that directory.  Outside it, the setup:

* installs Homebrew formulae: `llvm`, `lld`, `dtc`, `make`, `wget`, `git`,
  `python@3.12`, `qemu`, and -- unless `PROVERS_DEPS_PROFILE=runtime` -- `cmake`,
  `ninja`, `pandoc`, `autoconf`, `automake`, `riscv64-elf-gcc`.  TeX Live is
  *not* installed; the Linux side pulls it in for the VM image, but on a Mac
  that is the ~6 GB `mactex` cask and nothing here needs it

* installs `rustup` and the pinned toolchain into `~/.cargo`, and makes that
  toolchain the rustup default.  On a machine with its own Rust work, skip that
  last part with `PROVERS_RUST_DEFAULT=false bash provers-setup.sh` -- nothing
  installed here depends on the default, since Verus comes from a release, the
  `microkit` tool build names its toolchain explicitly, and generated crates
  select their own through `rust-toolchain.toml`

* appends a `# provers-env` block to `~/.zshrc` (or `~/.bash_profile` under
  bash), which is what puts `verus`, `sireum` and the rest on `PATH`.  It is
  appended once -- re-running the setup will not add it twice -- and removing
  that block undoes it.  That is the only file outside `$PROVERS_DIR` the setup
  writes: the shell aliases the container and the VM carry are not installed
  here, since they are a convenience for an image handed out rather than
  something to put in the dotfiles of a machine that is already yours
  (`PROVERS_SHELL_ALIASES=true` if you want them anyway)

Two Homebrew directories go on `PATH` ahead of `/usr/bin`, from
[bin/env.sh](../bin/env.sh): `llvm`'s, because that formula is keg-only and is
where the `clang`, `ld.lld`, `llvm-ar`, `llvm-ranlib` and `llvm-objcopy` the
generated Microkit makefiles invoke come from; and `make`'s `gnubin`, because
macOS ships GNU Make 3.81 and the sDDF makefiles are not written for it.
`bin/deps.sh` ends by checking that each of those tools resolves and that `make`
is 4.x, so a `PATH` problem shows up there rather than part-way through your
first build.

`bin/slim.sh` refuses to run on macOS: it exists to shrink an image, partly by
deleting `$SIREUM_HOME/bin/mac`, which is the install in use here.

### Smoke Test

```bash
verus --version                 # ... Platform: macos_aarch64
sireum --version
# sdfgen is a python library, not a CLI -- it ships no console script
python3 -c 'import sdfgen, importlib.metadata as m; print("sdfgen", m.version("sdfgen"))'
ls $MICROKIT_SDK/board
qemu-system-aarch64 --version
make --version | head -1        # GNU Make 4.x, not the 3.81 in /usr/bin
ive                             # and codium, fmide
```

## Setting Up An Ubuntu 24.04 Machine

The counterpart of [the Mac setup above](#setting-up-an-apple-silicon-mac): the
same `provers-setup.sh`, on a machine you already have, with no VM in between.

### Requirements

* Ubuntu 24.04, x86_64 or aarch64, with `sudo` available to the invoking user

* ~60 GB free disk.  Allow a few hours on x86_64, and considerably longer on
  aarch64, where Z3, Verus and sdfgen are all built from source -- see
  [Hosts](#hosts)

### Steps

```bash
bash provers-setup.sh
```

As on a Mac, that installs the command-line tools only; the IDEs are opt-in with
`PROVERS_IVE=true`, `PROVERS_CODEIVE=true`, `PROVERS_FMIDE=true`.  The
environment is wired into `~/.bashrc`, which is what an Ubuntu login shell
reads.

Then open a new shell to pick up the environment.  `provers-setup.sh` uses the
scripts in `bin/` next to it; in the Vagrant VM those same scripts are installed
to `~/bin`.

## Post Setup

Each step is a standalone script and can be re-run on its own to update or
repair that tool -- from `~/bin` in the VM, or from `bin/` on a dedicated
machine or a Mac:

| script | what it (re-)installs |
| --- | --- |
| `disk.sh` | grows root into unused extents in its LVM volume group (VM only; a no-op elsewhere) |
| `apt-mirror.sh` | points apt at `PROVERS_APT_MIRROR` (Linux only; no-op when unset) |
| `deps.sh` | apt build/runtime/GUI dependencies, or the Homebrew formulae on macOS |
| `rust.sh` | rustup and the pinned toolchain (`PROVERS_RUST_DEFAULT=false` to leave the rustup default alone) |
| `z3.sh` | Z3 (`Z3_VER`), built from source; a no-op wherever the Verus release bundles one, i.e. everywhere but aarch64 Linux (see [Hosts](#hosts)) |
| `verus.sh` | Verus |
| `microkit-lionsos.sh` | sdfgen venv, released Microkit SDK, LionsOS |
| `microkit-vcpu-domain.sh` | rebuilds the SDK's `microkit` tool with the vCPU domain fix |
| `sireum.sh` | Sireum itself (no IDEs), or adopts the one `SIREUM_HOME` points at |
| `slim.sh` | deletes build leftovers and caches; not run by this setup (see [What Gets Installed](#what-gets-installed)), and refuses to run on macOS |
| `ive.sh` | Sireum IVE |
| `codeive.sh` | CodeIVE |
| `fmide.sh` | FMIDE |
| `firefox.sh` | Firefox, from Mozilla's apt repo rather than the Ubuntu snap (Linux desktop only) |
| `prep-export.sh` | cache cleanup + zero-fill before an OVA export (manual, VM only) |

Each is quiet by default -- a few lines saying what it is doing.  Set
`PROVERS_TRACE=true` for the full `set -x` trace of every command, which is
worth doing when diagnosing a failure:

```bash
PROVERS_TRACE=true bash ~/bin/verus.sh
```

The VM and container builds set it themselves: their logs are the only record of
an unattended build, so tracing them is worth the volume.  `PROVERS_TRACE=false`
on the host turns it off for those too.

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

Note that this is the path taken when Sireum is installed *by* the setup.  Where
`SIREUM_HOME` already points at a Sireum of your own -- an install with a
`bin/sireum.jar`, or a kekinian checkout with a `bin/build.cmd` -- `sireum.sh`
adopts it and does nothing else, `SIREUM_V` included; see
[Using A Sireum You Already Have](#using-a-sireum-you-already-have).

`SIREUM_V` accepts a release, or a commit or branch, and `bin/sireum.sh` tells
them apart by asking whether a release of that name publishes an `install.cmd`:

| `SIREUM_V` | what happens | how long |
| --- | --- | --- |
| a numbered release, e.g. `4.20260810.80aad0c2` | its `cli` distribution is unpacked, by running the release's own `install.cmd` with `DISTRO=cli` and `DIR=$SIREUM_HOME` | ~1 minute |
| `dev` | the same, since `dev` is a real release -- but a *moving* one, re-cut as kekinian advances, so it tracks the tip rather than pinning anything | ~1 minute |
| a commit or branch, e.g. `e8f69b3d...` or `master` | kekinian is cloned at it and built | ~an hour |

`PROVERS_SIREUM_FROM_SOURCE` overrides the choice -- `true` builds from source
even for a release, `false` refuses rather than falling back to a source build.

Two things to know about the release path.  It needs `SIREUM_HOME` to end in
`/Sireum`, because `install.cmd` unpacks into the parent directory (the setup
refuses up front rather than scattering a distribution beside where it said it
would go).  And `install.cmd` also unpacks `org.sireum.m2.zip`, ~253MB of Maven
artifacts, into `$HOME` -- which is its own doing and lands outside
`$PROVERS_DIR`.

A release distribution ships no `build.cmd`, so where no IDE is selected the
solvers are installed with `sireum --init` instead of `build.cmd z3`/`cvc`; that
pulls the rest of the dependency set with them.  `PROVERS_SIREUM_SOLVERS=false`
skips it.

`SIREUM_INIT_V` matters only on the source path: it selects the release whose
prebuilt `sireum.jar` bootstraps that build.  `bin/init.sh`
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
