# Temp-Control — Codegen, Build, Simulate, and Verify

Instructions for the SysML v2 temp-control model.  For a system overview see [readme.md](readme.md).

HAMR codegen targets **seL4 Microkit with userland scheduling** (plus **runtime monitoring** and
static **system verification**); the generated project is under
[`hamr/microkit_mcs/`](hamr/microkit_mcs/).  Unlike domain scheduling (which relies on customized
Microkit/seL4 builds), userland scheduling implements the static cyclic schedule in user space on
top of the standard Microkit SDK, driven by a dedicated scheduler protection domain.  Build/simulate
commands use the docker image `jasonbelt/microkit_provers`, which bundles the official Microkit SDK
(`$MICROKIT_SDK_CURRENT`), the Rust/Verus toolchains, and QEMU.  Type `CTRL-a x` to exit a QEMU
simulation.

## Table of Contents
  * [Installation](#installation)
  * [Codegen](#codegen)
  * [Verus Attribute Syntax](#verus-attribute-syntax)
  * [Build and simulate](#build-and-simulate)
  * [Runtime monitoring](#runtime-monitoring)
  * [R2U2 runtime assurance for the C sensor](#r2u2-runtime-assurance-for-the-c-sensor)
  * [Static system verification](#static-system-verification)
  * [Unit tests](#unit-tests)

---

## Installation

1. Install [Docker Desktop](https://www.docker.com/products/docker-desktop/)

1. Clone this repo and cd into the temp-control directory

   ```sh
   git clone https://github.com/loonwerks/INSPECTA-models.git
   cd INSPECTA-models/micro-examples/microkit/system-verification/temp-control
   ```

1. Clone the [SysMLv2 AADL Libraries](https://github.com/santoslab/sysml-aadl-libraries)

    Either clone the libraries directly into the temp-control's `sysml` directory

    ```sh
    git clone https://github.com/santoslab/sysml-aadl-libraries.git sysml/sysml-aadl-libraries
    ```

    Or, clone the libraries elsewhere and set the ``SYSML_AADL_LIBRARIES`` environment variable

    ```sh
    git clone https://github.com/santoslab/sysml-aadl-libraries.git
    export SYSML_AADL_LIBRARIES=$(pwd)/sysml-aadl-libraries
    ```

1. Development Tools

    This documentation covers several distinct tasks, each requiring specific tools (e.g., Verus for Rust verification or Sireum for HAMR code generation). Ensure you have installed the tools required by the task by following the [official installation instructions](https://hamr.sireum.org/hamr-doc/installation/).

## Codegen

**Requires:** Sireum, SysMLv2 AADL Libraries

Regenerate the Microkit project from the model with the driver script
[`sysml/bin/run-hamr.cmd`](sysml/bin/run-hamr.cmd) (a Sireum Slang script that invokes
`sireum hamr sysml codegen`).  Pass the flags for this variant — userland scheduling, runtime
monitoring, Verus attribute syntax, and the R2U2 aux code directories:

```sh
sysml/bin/run-hamr.cmd \
  --platform Microkit \
  --scheduling UserLand \
  --runtime-monitoring \
  --verus-attribute-syntax \
  --sel4-aux-code-dirs hamr/microkit_mcs/r2u2/r2u2-lib:hamr/microkit_mcs/r2u2/sensor-spec \
  --sel4-aux-code-symlink \
  --sel4-output-dir hamr/microkit_mcs
```

Multiple aux code directories are separated by the platform path separator (`:` on
macOS/Linux, `;` on Windows).  The `--sel4-aux-code-*` options are **not optional** for this project: the C temperature
sensor's implementation embeds an R2U2 runtime monitor (see
[R2U2 runtime assurance for the C sensor](#r2u2-runtime-assurance-for-the-c-sensor)), so the
generated makefiles must compile and link the vendored R2U2 engine or the sensor will not
build.  They require a Sireum release with Microkit aux-code support (July 2026 or later).

`SIREUM_HOME` must be set; if the SysML AADL libraries are not vendored next to the model, set
`SYSML_AADL_LIBRARIES` so codegen can find them.

## Verus Attribute Syntax

This build uses the **Verus attribute syntax** (`--verus-attribute-syntax`), which places Verus
contracts in Rust `#[verus_spec(...)]` attributes rather than inside `verus! { }` macro blocks.
The component implementation code is therefore standard Rust that compiles with the normal Rust
toolchain, while the contracts live in attributes only processed by Verus.

This separation facilitates **mutation testing**: because the component code is ordinary Rust,
standard Rust mutation tools can instrument and mutate it without understanding Verus syntax.
Mutations that violate a GUMBO contract are caught either statically (Verus) or dynamically (the
runtime monitors), providing confidence that the contracts are strong enough to detect behavioral
deviations.

## Build and simulate

**Requires:** Docker Desktop

Build and simulate the system.  This variant uses the official Microkit SDK (available in the docker
image as `$MICROKIT_SDK_CURRENT`).  The build uses `cargo-verus` for the Rust component crates,
which also verifies their code-level contracts.  Run from this model directory:

```sh
docker run -it --rm -v $(pwd):/home/microkit/provers/temp-control jasonbelt/microkit_provers bash -ci \
  "cd \$HOME/provers/temp-control/hamr/microkit_mcs && make clean && \
  MICROKIT_SDK=\$MICROKIT_SDK_CURRENT make qemu"
```

You should see the three threads' `initialize` / `timeTriggered` entrypoints logging as the major
frame advances (the sensor publishing a temperature, the controller receiving it, and the fan
receiving `On`/`Off` commands).

## Runtime monitoring

The `--runtime-monitoring` codegen flag (see [Codegen](#codegen)) *generates* the monitoring
infrastructure; it does not activate it.  Which monitor — if any — is compiled into the image is
chosen at build time with a `CONFIG=<file>.mk` argument; the base `make qemu` (no `CONFIG`) contains
no monitor and performs no runtime checks.

Two of the configs select HAMR's **contract monitor**: a single, system-wide monitor protection
domain that checks GUMBO contracts against actual runtime values at each dispatch and reports
violations.  Codegen wires it to the outgoing ports of all threads and adds channels exposing the
GUMBO state variables its contracts reference; the two configs differ only in how much it checks:

- **`gumbo_monitor.mk`** — **component-level** monitoring.  Around each thread's slot in the
  schedule, the monitor checks that thread's preconditions just **before** dispatch and its
  postconditions just **after** it yields.  This is the runtime counterpart of the per-component
  Verus verification.
- **`sys_nominal_monitor.mk`** — **system-level** monitoring.  Additionally checks the model's
  system-level (nominal) assertions — `TC_Req_01` / `TC_Req_02` — against the values exchanged
  between components.  This is the runtime counterpart of the `sys_nominal_proof` proof.  It is built
  off (and extends) `gumbo_monitor.mk`, so it also performs the component-level checks.

The remaining config builds a **separate** monitor that is not a contract checker:

- **`userland_monitor.mk`** — schedule-broadcast demo.  The userland scheduler publishes the current
  schedule state (active timeslice) into a shared memory region; a dedicated, developer-editable
  monitor protection domain maps that region and reacts as the major frame advances.  Its
  `initialize` / `timeTriggered` entrypoints (under
  [`crates/userland_monitor/src/component/`](hamr/microkit_mcs/crates/userland_monitor/src/component/))
  are skeletons to fill in with your own logic.

Component-level (GUMBO) runtime monitoring:

```sh
docker run -it --rm -v $(pwd):/home/microkit/provers/temp-control jasonbelt/microkit_provers bash -ci \
  "cd \$HOME/provers/temp-control/hamr/microkit_mcs && make clean && \
  MICROKIT_SDK=\$MICROKIT_SDK_CURRENT make CONFIG=gumbo_monitor.mk qemu"
```

System-level (assertion) runtime monitoring:

```sh
docker run -it --rm -v $(pwd):/home/microkit/provers/temp-control jasonbelt/microkit_provers bash -ci \
  "cd \$HOME/provers/temp-control/hamr/microkit_mcs && make clean && \
  MICROKIT_SDK=\$MICROKIT_SDK_CURRENT make CONFIG=sys_nominal_monitor.mk qemu"
```

Schedule-broadcast monitor demo:

```sh
docker run -it --rm -v $(pwd):/home/microkit/provers/temp-control jasonbelt/microkit_provers bash -ci \
  "cd \$HOME/provers/temp-control/hamr/microkit_mcs && make clean && \
  MICROKIT_SDK=\$MICROKIT_SDK_CURRENT make CONFIG=userland_monitor.mk qemu"
```

## R2U2 runtime assurance for the C sensor

The temperature sensor (`tsp_tst`) is deliberately implemented in **C** to illustrate how
system verification handles components Verus cannot reason about: the system proof
**trusts** the sensor's GUMBO integration contract

```
guarantee Sensor_Temperature_Range:
  currentTemp.degrees >= -40 [i32] & currentTemp.degrees <= 122 [i32];
```

and records that trust in
[`crates/sys_nominal_proof/TRUSTED_ASSUMPTIONS.md`](hamr/microkit_mcs/crates/sys_nominal_proof/TRUSTED_ASSUMPTIONS.md)
(see [Static system verification](#static-system-verification)).  Where the Rust components'
contracts are discharged statically by Verus, the C sensor's contract is instead checked
**continuously at runtime** by an embedded [R2U2](https://github.com/R2U2/r2u2) monitor —
NASA's stream-based runtime verification engine for Mission-time LTL (MLTL).  The contract
is expressed as an MLTL specification
([`r2u2/sensor-spec/sensor.c2po`](hamr/microkit_mcs/r2u2/sensor-spec/sensor.c2po)), compiled
offline by R2U2's C2PO compiler, and embedded in the sensor as a byte array.  The sensor's
user code ([`components/tsp_tst/src/tsp_tst_user.c`](hamr/microkit_mcs/components/tsp_tst/src/tsp_tst_user.c))
feeds every value it emits on `currentTemp` to the monitor and reports a verdict each frame,
so a contract violation is detected in the same frame it occurs.  This turns the proof's
trusted assumption into a *checked* assumption: either the sensor conforms on this
execution, or the violation is flagged the moment it happens.

The R2U2 C engine is linked into the sensor via HAMR codegen's `--sel4-aux-code-dirs`
option, which makes user-supplied C library directories available under
[`aux_code/`](hamr/microkit_mcs/aux_code/) and wires them into the generated build (include
paths, compile rules, and linking into the C component ELFs; Rust components are
unaffected).  This project additionally passes `--sel4-aux-code-symlink`, so `aux_code/`
contains relative **symlinks** to the [`r2u2/`](hamr/microkit_mcs/r2u2/) directories rather
than copies of their C files (without the flag, the files are copied).  Note for Windows
users: the symlinks require `git config core.symlinks true` (and Developer Mode) to check
out correctly.  The monitor engine is entirely static — no heap, spec loaded from the
embedded array, verdicts delivered via callback — and runs inside the sensor's protection
domain.  See [`r2u2/readme.md`](hamr/microkit_mcs/r2u2/readme.md) for the directory layout,
the (small, documented) freestanding patches to the vendored engine, and how to recompile
the specification after editing it.

### Codegen with R2U2

The `--sel4-aux-code-dirs`/`--sel4-aux-code-symlink` options are part of this project's
canonical regeneration command — see [Codegen](#codegen).  The sensor's monitor lives in the
user-editable `tsp_tst_user.c`, so regeneration preserves it.

### Build and run with R2U2

No extra build flags are needed — the aux code is part of the generated makefiles, so the
normal [build and simulate](#build-and-simulate) command applies:

```sh
docker run -it --rm -v $(pwd):/home/microkit/provers/temp-control jasonbelt/microkit_provers bash -ci \
  "cd \$HOME/provers/temp-control/hamr/microkit_mcs && make clean && \
  MICROKIT_SDK=\$MICROKIT_SDK_CURRENT make qemu"
```

Each major frame the sensor publishes a reading and the monitor reports its verdict:

```
tsp_tst: R2U2: SensorRange satisfied at time 3
```

The nominal random-walk sensor never leaves the contract's range, so all verdicts are
`satisfied`.  To watch the monitor catch a violation, set `R2U2_DEMO_FAULT_PERIOD` in
[`tsp_tst_user.c`](hamr/microkit_mcs/components/tsp_tst/src/tsp_tst_user.c) to a small
`N > 0` and rebuild; every Nth dispatch the sensor then emits a faulty 130 F reading:

```
tsp_tst: R2U2: SensorRange VIOLATED at time 5 -- contract assumed by TempControl does not hold
```

The injected value 130 is chosen to violate the sensor's own contract (`> 122`) while still
satisfying TempControl's integration assumption (`<= 134`), so the fault is visible only to
the R2U2 monitor — the downstream verified components behave normally (130 is above the set
point, so the fan turns on).

## Static system verification

**Requires:** Rust, Verus

In addition to the per-component code-level contracts, HAMR generates a **system-level proof**
discharged by Verus.  It reasons about the composed system against the model's `composition nominal`
specification (properties `TC_Req_01` / `TC_Req_02` and the `Control_Invariant_Base` loop
invariant), without needing an actual running schedule.

Verify every Rust component crate **and** discharge the system proof:

```sh
make -C hamr/microkit_mcs verus
```

Discharge **only** the system-level verification conditions (without re-verifying the component
crates) by invoking the `sys_nominal_proof` crate directly:

```sh
make -C hamr/microkit_mcs/crates/sys_nominal_proof
```

Run this way, the system proof **assumes** each component satisfies its component-level contract.
For the Rust components (`tcp_tct`, `fp_ft`) that assumption is discharged separately by their own
`cargo-verus` verification.  The **temperature sensor (`tsp_tst`) is a C component**, so Verus
cannot discharge its contract — the proof **trusts** it, and this is recorded in
[`crates/sys_nominal_proof/TRUSTED_ASSUMPTIONS.md`](hamr/microkit_mcs/crates/sys_nominal_proof/TRUSTED_ASSUMPTIONS.md).
The `sys_nominal_proof` build re-announces the trusted contract on every run; a trusted C
component's contract must be established by other means (e.g. testing).

The `sys_nominal_proof` Makefile also provides a target per system property, so each property's
verification conditions can be checked in isolation:

```sh
make -C hamr/microkit_mcs/crates/sys_nominal_proof tc_req_01
make -C hamr/microkit_mcs/crates/sys_nominal_proof tc_req_02
```

## Unit tests

**Requires:** Rust

```sh
make -C hamr/microkit_mcs test
```
