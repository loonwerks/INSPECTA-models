# SysML Model — Installation and Codegen

This page covers installation, codegen, simulation, and verification for the **SysML model** of
the Isolette.  The SysML model uses Sireum directly for codegen (no OSATE required).  Generated
code lives in [`hamr/slang/`](hamr/slang/) (JVM target), [`hamr/microkit/`](hamr/microkit/)
(Microkit with domain scheduling), and [`hamr/microkit_mcs/`](hamr/microkit_mcs/)
(Microkit with user-land scheduling).

See [readme.md](readme.md) for an overview of the system architecture and GUMBO contracts.

## Table of Contents

- [Installation](#installation)
- [Codegen](#codegen)
  - [JVM (Slang) + runtime monitoring](#jvm-slang--runtime-monitoring)
  - [Microkit (Domain Scheduling) + runtime monitoring](#microkit-domain-scheduling--runtime-monitoring)
    - [Static System Verification](#static-system-verification)
  - [Microkit (User-Land Scheduling)](#microkit-user-land-scheduling)
    - [Verus Attribute Syntax](#verus-attribute-syntax)
  - [Component/System Verification Runtime Monitoring](#componentsystem-verification-runtime-monitoring)
  - [System Verification (VC Generation)](#system-verification-vc-generation)

## Installation

1. Install [Docker Desktop](https://www.docker.com/products/docker-desktop/)

1. Clone this repo and cd into it

   ```sh
   git clone https://github.com/loonwerks/INSPECTA-models.git
   cd INSPECTA-models
   ```

1. Clone the [SysMLv2 AADL Libraries](https://github.com/santoslab/sysml-aadl-libraries)

    Either clone the libraries directly into the Isolette's `sysml` directory

    ```sh
    git clone https://github.com/santoslab/sysml-aadl-libraries.git isolette/sysml/sysml-aadl-libraries
    ```

    Or, clone the libraries elsewhere and set the ``SYSML_AADL_LIBRARIES`` environment variable

    ```sh
    git clone https://github.com/santoslab/sysml-aadl-libraries.git
    export SYSML_AADL_LIBRARIES=$(pwd)/sysml-aadl-libraries
    ```

1. Development Tools

    This documentation covers several distinct tasks, each requiring specific tools (e.g., Verus for Rust verification or Sireum for HAMR code generation). Ensure you have installed the tools required by the task by following the [official installation instructions](https://hamr.sireum.org/hamr-doc/installation/).

## Codegen

### JVM (Slang) + runtime monitoring

1. *OPTIONAL* Rerun codegen targeting the JVM

    **Requires:**
    - Sireum
    - SysMLv2 AADL Libraries

    Launch the Slash script [isolette/sysml/bin/run-hamr.cmd](sysml/bin/run-hamr.cmd) targeting the JVM

    ```sh
    isolette/sysml/bin/run-hamr.cmd --platform JVM --runtime-monitoring --slang-output-dir isolette/hamr/slang
    ```

1. Run the JUnit tests

    **Requires:**
    - Sireum

    ```sh
    sireum proyek test isolette/hamr/slang
    ```

1. Build and run the JVM application

    **Requires:**
    - Sireum

    ```sh
    sireum proyek run isolette/hamr/slang isolette.Demo
    ```

1. Verify model-level integration constraint contracts with Logika <a id="logika-constraint-check"></a>

    **Requires:**
    - Sireum
    - SysMLv2 AADL Libraries

    If you cloned the SysMLv2 AADL Libraries into the Isolette's 'sysml' directory then run

    ```sh
    sireum hamr sysml logika --sourcepath isolette/sysml
    ```

    If you instead set the ``SYSML_AADL_LIBRARIES`` environment variable then run

    ```sh
    sireum hamr sysml logika --sourcepath isolette/sysml:$SYSML_AADL_LIBRARIES
    ```

1. Verify code-level contracts with Logika

    **Requires:**
    - Sireum

    ```sh
    isolette/hamr/slang/bin/run-logika.cmd
    ```

### Microkit (Domain Scheduling) + runtime monitoring

1. *OPTIONAL* Rerun codegen targeting Microkit with domain scheduling

    **Requires:**
    - Sireum
    - SysMLv2 AADL Libraries

    Launch the Slash script [isolette/sysml/bin/run-hamr.cmd](sysml/bin/run-hamr.cmd) targeting Microkit

    ```sh
    isolette/sysml/bin/run-hamr.cmd --platform Microkit --runtime-monitoring --sel4-output-dir isolette/hamr/microkit
    ```

1. Run the Rust unit tests

    **Requires:**
    - Rust

    ```sh
    make -C isolette/hamr/microkit test
    ```

1. Verify model-level integration constraint contracts with Logika

    *Refer to this [task](#logika-constraint-check) in the JVM section*

1. Verify code-level and system-level contracts with Verus

    **Requires:**
    - Rust
    - Verus

    ```sh
    make -C isolette/hamr/microkit verus
    ```

    The `verus` target verifies every component crate and then discharges the system-level
    proof — see [Static System Verification](#static-system-verification) below for running the
    system proof and individual system properties in isolation.

1. Build and simulate the seL4 Microkit image

    **Requires:**
    - Docker Desktop

    Run the following from this repository's root directory.  The docker image
    `jasonbelt/microkit_provers` contains customized versions of Microkit and seL4 that
    support domain scheduling, built off
    [microkit #175](https://github.com/seL4/microkit/pull/175) and
    [seL4 #1308](https://github.com/seL4/seL4/pull/1308).

    The build uses ``cargo-verus`` which also verifies the code-level contracts.

    ```sh
    docker run -it --rm -v $(pwd):/home/microkit/provers/INSPECTA-models jasonbelt/microkit_provers \
      bash -ci "cd \$HOME/provers/INSPECTA-models/isolette/hamr/microkit && make clean && make qemu"
    ```

    Type ``CTRL-a x`` to exit the QEMU simulation

    You should see output similar to the following

    ```
    Bootstrapping kernel
    Warning: Could not infer GIC interrupt target ID, assuming 0.
    available phys memory regions: 1
      [60000000..c0000000]
    reserved virt address space regions: 3
      [8060000000..8060348000]
      [8060348000..80603ae000]
      [80603ae000..80603b6000]
    Booting all finished, dropped to user space
    MON|INFO: Microkit Bootstrap
    MON|INFO: bootinfo untyped list matches expected list
    MON|INFO: Number of bootstrap invocations: 0x0000000a
    MON|INFO: Number of system invocations:    0x000002ac
    MON|INFO: completed bootstrap invocations
    thermostat_mt_ma: thermostat_mt_ma_ma_initialize invoked
    MON|INFO: completed system invocations
    thermostat_rt_mr: thermostat_rt_mri_mri_initialize invoked
    thermostat_rt_mr: thermostat_rt_mrm_mrm_initialize invoked
    thermostat_rt_mh: thermostat_rt_mhs_mhs_initialize invoked
    thermostat_rt_dr: thermostat_rt_drf_drf_initialize invoked
    heat_source_cpi_: heat_source_cpi_heat_controller_initialize invoked
    operator_interfa: operator_interface_oip_oit_initialize invoked
    temperature_sens: temperature_sensor_cpi_thermostat_initialize invoked
    thermostat_mt_mm: thermostat_mt_mmm_mmm_initialize invoked
    thermostat_mt_mm: thermostat_mt_mmi_mmi_initialize invoked
    thermostat_mt_dm: thermostat_mt_dmf_dmf_timeTriggered invoked
    operator_interfa: Regulator Status: Init
    operator_interfa: Monitor Status: Init
    operator_interfa: Display Temperature 0.000000
    operator_interfa: Alamr: off
    ####### FRAME 0 #######
    thermostat_mt_dm: thermostat_mt_dmf_dmf_timeTriggered invoked
    operator_interfa: Regulator Status: On
    operator_interfa: Monitor Status: On
    operator_interfa: Display Temperature 97.000000
    operator_interfa: Alamr: off
    ####### FRAME 1 #######
    thermostat_mt_dm: thermostat_mt_dmf_dmf_timeTriggered invoked
    heat_source_cpi_: Received command: On
    operator_interfa: Regulator Status: On
    operator_interfa: Monitor Status: On
    operator_interfa: Display Temperature 96.000000
    operator_interfa: Alamr: on
    ####### FRAME 2 #######
    thermostat_mt_dm: thermostat_mt_dmf_dmf_timeTriggered invoked
    operator_interfa: Regulator Status: On
    operator_interfa: Monitor Status: On
    operator_interfa: Display Temperature 97.000000
    operator_interfa: Alamr: on
    ####### FRAME 3 #######
    thermostat_mt_dm: thermostat_mt_dmf_dmf_timeTriggered invoked
    operator_interfa: Regulator Status: On
    operator_interfa: Monitor Status: On
    operator_interfa: Display Temperature 98.000000
    operator_interfa: Alamr: off
    ####### FRAME 4 #######
    thermostat_mt_dm: thermostat_mt_dmf_dmf_timeTriggered invoked
    operator_interfa: Regulator Status: On
    operator_interfa: Monitor Status: On
    operator_interfa: Display Temperature 99.000000
    operator_interfa: Alamr: off
    ####### FRAME 5 #######
    thermostat_mt_dm: thermostat_mt_dmf_dmf_timeTriggered invoked
    heat_source_cpi_: Received command: Off
    operator_interfa: Regulator Status: On
    operator_interfa: Monitor Status: On
    operator_interfa: Display Temperature 100.000000
    operator_interfa: Alamr: off
    ####### FRAME 6 #######
    thermostat_mt_dm: thermostat_mt_dmf_dmf_timeTriggered invoked
    operator_interfa: Regulator Status: On
    operator_interfa: Monitor Status: On
    operator_interfa: Display Temperature 101.000000
    operator_interfa: Alamr: off
    ####### FRAME 7 #######
    thermostat_mt_dm: thermostat_mt_dmf_dmf_timeTriggered invoked
    operator_interfa: Regulator Status: On
    operator_interfa: Monitor Status: On
    operator_interfa: Display Temperature 102.000000
    operator_interfa: Alamr: on
    ```

1. Build and simulate the seL4 Microkit image with the domain monitor enabled

    **Requires:**
    - Docker Desktop

    This is the same as the previous step, except the build is pointed at the
    alternate system description `monitor.microkit.system` by passing
    `MSD=monitor.microkit.system` to ``make``.

    HAMR generates the domain monitor as an additional **monitor protection domain**
    that is connected to all of the outgoing ports of the threads in the system.  As
    each thread's output is released to the communication infrastructure, a copy is
    also delivered to the monitor, giving it a global, read-only view of everything
    flowing between components.  This makes the monitor a convenient place to observe
    system behavior, check cross-component properties at runtime, and record or report
    on the values being exchanged.

    The monitor is a normal HAMR thread component: it has ``initialize`` and
    ``timeTriggered`` entrypoints that you can fill in with your own application logic
    (for example, logging the observed port values or asserting a runtime property).

    Run the following from this repository's root directory.

    ```sh
    docker run -it --rm -v $(pwd):/home/microkit/provers/INSPECTA-models jasonbelt/microkit_provers \
      bash -ci "cd \$HOME/provers/INSPECTA-models/isolette/hamr/microkit && make clean && make qemu MSD=monitor.microkit.system"
    ```

    Type ``CTRL-a x`` to exit the QEMU simulation

#### Static System Verification

In addition to the per-component code-level contracts, HAMR generates a **system-level proof**
for the domain-scheduling build.  This static (verification-condition) side of system
verification is discharged by Verus at build time; it reasons about the composed system against
the model's system-level (nominal) specification — the
[`composition nominal`](sysml/Isolette.sysml#L571) block — without needing an actual running
schedule.  There are two levels:

- **Component code-level contracts** — each component crate is verified with ``cargo-verus``
  against the requires/guarantees clauses generated from its GUMBO contract.
- **System-level proof** — a dedicated ``sys_nominal_proof`` crate discharges the system-level
  verification conditions generated from the model's system-level (nominal) GUMBO
  specification.

The `make verus` step above runs both: it verifies every component crate and then discharges
the `sys_nominal_proof` system proof.

1. Discharge only the system-level verification conditions

    **Requires:**
    - Rust
    - Verus

    The system-level proof can be run in isolation — without re-verifying every component crate
    — by invoking the `sys_nominal_proof` crate directly:

    ```sh
    make -C isolette/hamr/microkit/crates/sys_nominal_proof
    ```

    Run this way, the system proof **assumes** that each component's behavior code satisfies its
    component-level contracts (those are discharged separately by the per-component Verus
    verification above); it then proves the system-level properties hold given those component
    contracts.

    The `sys_nominal_proof` Makefile additionally provides a target per system property, so each
    property's verification conditions can be checked in isolation.  For example:

    ```sh
    make -C isolette/hamr/microkit/crates/sys_nominal_proof normal_mode_heat
    ```

### Microkit (User-Land Scheduling)

This variant uses the official Microkit SDK (2.x.x) with user-land scheduling.  Generated
code lives in [`hamr/microkit_mcs/`](hamr/microkit_mcs/).

User-land scheduling is not specific to the SysML front-end — it is equally supported for AADL
models; this project simply does not include an AADL example of that configuration.

Unlike the domain scheduling variant above (which relies on customized Microkit/seL4 builds
with kernel-level domain scheduling support), user-land scheduling implements the static
cyclic schedule in user space on top of the standard Microkit SDK.  Because the schedule is
driven by a dedicated scheduler protection domain rather than baked into the kernel, the
system has explicit, programmable control over scheduling.  This opens up capabilities that
are not possible with the default kernel-level scheduler — for example, the scheduler could
broadcast the schedule to the components at runtime, so that each component knows where it
sits within the major frame.  HAMR ships a worked example of exactly this: the
**`userland_monitor.mk`** build (below) runs a user-land scheduler that publishes the current
schedule state into a shared memory region, alongside a dedicated, developer-editable monitor
protection domain that maps that region and reacts as the major frame advances.

Building on this user-land foundation, the variant adds contract-based **runtime monitoring** —
the dynamic side of assurance, described below.  It also supports the same **static system
verification** introduced for the domain-scheduling variant (see
[Static System Verification](#static-system-verification)); the two sections below cover how
each applies to the user-land build:

- **[Component/System Verification Runtime Monitoring](#componentsystem-verification-runtime-monitoring)**
  — dynamically checks GUMBO contracts (component- and system-level) against actual runtime
  values.
- **[System Verification (VC Generation)](#system-verification-vc-generation)** — statically
  discharges the verification conditions generated from the model's GUMBO specifications,
  including a system-level proof.

#### Verus Attribute Syntax

This variant uses the **Verus attribute syntax** (`--verus-attribute-syntax`), which
places Verus contracts in Rust `#[verus_spec(...)]` attributes rather than inside
`verus! { }` macro blocks.  This means the component implementation code is standard
Rust that compiles with the normal Rust toolchain, while the contracts live in
attributes that are only processed by Verus.

This separation facilitates **mutation testing**: because the component code is ordinary
Rust (not embedded in a Verus macro), standard Rust mutation testing tools can parse,
instrument, and mutate the implementation code without needing to understand Verus syntax.
Mutations that violate a GUMBO contract will be caught either by Verus verification
(statically) or by the runtime monitors (dynamically), providing confidence that the
contracts are strong enough to detect behavioral deviations.

1. *OPTIONAL* Rerun codegen targeting Microkit with user-land scheduling

    **Requires:**
    - Sireum
    - SysMLv2 AADL Libraries

    Launch the Slash script [isolette/sysml/bin/run-hamr.cmd](sysml/bin/run-hamr.cmd) with user-land scheduling options

    ```sh
    isolette/sysml/bin/run-hamr.cmd --platform Microkit --runtime-monitoring --scheduling UserLand --verus-attribute-syntax --sel4-output-dir isolette/hamr/microkit_mcs
    ```

1. Run the Rust unit tests

    **Requires:**
    - Rust

    ```
    make -C isolette/hamr/microkit_mcs test
    ```

1. Build and simulate the seL4 Microkit image

    **Requires:**
    - Docker Desktop

    Run the following from this repository's root directory.  This variant uses the
    official Microkit SDK (2.x.x) available in the docker image as `$MICROKIT_SDK_CURRENT`.

    The build uses ``cargo-verus`` which also verifies the code-level contracts.

    ```sh
    docker run -it --rm -v $(pwd):/home/microkit/provers/INSPECTA-models jasonbelt/microkit_provers bash -ci \
      "cd \$HOME/provers/INSPECTA-models/isolette/hamr/microkit_mcs && make clean && \
      MICROKIT_SDK=\$MICROKIT_SDK_CURRENT \
      make qemu"
    ```

    Type ``CTRL-a x`` to exit the QEMU simulation

1. Build and simulate with the schedule-broadcast monitor

    **Requires:**
    - Docker Desktop

    The `userland_monitor.mk` configuration demonstrates the schedule-broadcast capability
    described above.  The scheduler publishes the current schedule state (the active timeslice)
    into a shared memory region, and a dedicated monitor protection domain maps that region so
    it can observe where the system is within the major frame.  The monitor is also hooked up
    to the outgoing ports of the threads, so it additionally has a global, read-only view of
    the values flowing between components — letting it correlate the broadcast schedule with
    the data being exchanged at each point in the major frame.  The monitor's `initialize` and
    `timeTriggered` entrypoints (in
    [`crates/userland_monitor/src/component/`](hamr/microkit_mcs/crates/userland_monitor/src/component/))
    are developer-editable skeletons — add your own logic to react to the broadcast schedule.

    ```sh
    docker run -it --rm -v $(pwd):/home/microkit/provers/INSPECTA-models jasonbelt/microkit_provers bash -ci \
      "cd \$HOME/provers/INSPECTA-models/isolette/hamr/microkit_mcs && make clean && \
      MICROKIT_SDK=\$MICROKIT_SDK_CURRENT \
      make CONFIG=userland_monitor.mk qemu"
    ```

    Type ``CTRL-a x`` to exit the QEMU simulation

### Component/System Verification Runtime Monitoring

Building on the user-land scheduling variant, HAMR can instrument the system with a single,
system-wide runtime monitor that checks GUMBO contracts against actual runtime values at each
dispatch, reporting any violations.  To give the monitor the data its contracts reference,
codegen hooks it up to the outgoing ports of all the threads and, in addition, adds channels
exposing the threads' GUMBO state variables that the monitor is connected to — so it can
evaluate contract clauses over both the values flowing between components and the threads'
internal state.  Monitoring is available at two levels, each selected at build time with a
`CONFIG=<file>.mk` argument; codegen emits one config per flavor:

- **`gumbo_monitor.mk`** — **component-level** monitoring.  The monitor checks each component's
  GUMBO contracts (the requires/guarantees clauses derived from the SysML model), running around
  each thread's slot in the schedule: just **before** a thread is dispatched it checks that
  thread's preconditions, and just **after** the thread yields it checks its postconditions.
  This is the runtime counterpart of the per-component Verus verification.
- **`sys_nominal_monitor.mk`** — **system-level** monitoring.  The monitor additionally
  checks the model's system-level (nominal) assertions against the values exchanged between
  components.  This is the runtime counterpart of the system-level proof
  ([VC Generation](#system-verification-vc-generation)).  This configuration is built off (and
  effectively extends) `gumbo_monitor.mk`, so the same monitor also performs the
  component-level checks described above — running the system-level assertion checks on top of
  the per-component GUMBO contract checks.

1. Build and simulate with component-level (GUMBO) runtime monitoring

    **Requires:**
    - Docker Desktop

    ```sh
    docker run -it --rm -v $(pwd):/home/microkit/provers/INSPECTA-models jasonbelt/microkit_provers bash -ci \
      "cd \$HOME/provers/INSPECTA-models/isolette/hamr/microkit_mcs && make clean && \
      MICROKIT_SDK=\$MICROKIT_SDK_CURRENT \
      make CONFIG=gumbo_monitor.mk qemu"
    ```

    Type ``CTRL-a x`` to exit the QEMU simulation

1. Build and simulate with system-level (assertion) runtime monitoring

    **Requires:**
    - Docker Desktop

    ```sh
    docker run -it --rm -v $(pwd):/home/microkit/provers/INSPECTA-models jasonbelt/microkit_provers bash -ci \
      "cd \$HOME/provers/INSPECTA-models/isolette/hamr/microkit_mcs && make clean && \
      MICROKIT_SDK=\$MICROKIT_SDK_CURRENT \
      make CONFIG=sys_nominal_monitor.mk qemu"
    ```

    Type ``CTRL-a x`` to exit the QEMU simulation

### System Verification (VC Generation)

Static system verification (verification-condition generation) is documented in full under the
domain-scheduling variant — see [Static System Verification](#static-system-verification).  The
same checks are available for the user-land build:

1. Verify model-level integration constraint contracts with Logika

    *Refer to this [task](#logika-constraint-check) in the JVM section*

1. Discharge code-level and system-level verification conditions with Verus

    **Requires:**
    - Rust
    - Verus

    ```sh
    make -C isolette/hamr/microkit_mcs verus
    ```

    This verifies every component crate and discharges the `sys_nominal_proof` system proof.
    For running the system proof and individual system properties in isolation, see
    [Static System Verification](#static-system-verification) (substitute `microkit_mcs` for
    `microkit` in the paths).
