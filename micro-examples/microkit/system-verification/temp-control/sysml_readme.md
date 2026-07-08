# Temp-Control — Codegen, Build, Simulate, and Verify

Instructions for the SysML v2 temp-control model.  For a system overview see [readme.md](readme.md).

HAMR codegen targets **seL4 Microkit with user-land scheduling** (plus **runtime monitoring** and
static **system verification**); the generated project is under
[`hamr/microkit_mcs/`](hamr/microkit_mcs/).  Unlike domain scheduling (which relies on customized
Microkit/seL4 builds), user-land scheduling implements the static cyclic schedule in user space on
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
`sireum hamr sysml codegen`).  Pass the flags for this variant — user-land scheduling, runtime
monitoring, and Verus attribute syntax:

```sh
sysml/bin/run-hamr.cmd \
  --platform Microkit \
  --scheduling UserLand \
  --runtime-monitoring \
  --verus-attribute-syntax \
  --sel4-output-dir hamr/microkit_mcs
```

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

HAMR instruments the system with a single, system-wide runtime monitor that checks GUMBO contracts
against actual runtime values at each dispatch and reports violations.  Codegen wires the monitor to
the outgoing ports of all threads and adds channels exposing the GUMBO state variables its contracts
reference.  Levels are selected at build time with a `CONFIG=<file>.mk` argument:

- **`gumbo_monitor.mk`** — **component-level** monitoring.  Around each thread's slot in the
  schedule, the monitor checks that thread's preconditions just **before** dispatch and its
  postconditions just **after** it yields.  This is the runtime counterpart of the per-component
  Verus verification.
- **`sys_nominal_monitor.mk`** — **system-level** monitoring.  Additionally checks the model's
  system-level (nominal) assertions — `TC_Req_01` / `TC_Req_02` — against the values exchanged
  between components.  This is the runtime counterpart of the `sys_nominal_proof` proof.  It is built
  off (and extends) `gumbo_monitor.mk`, so it also performs the component-level checks.
- **`userland_monitor.mk`** — schedule-broadcast demo.  The user-land scheduler publishes the current
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
