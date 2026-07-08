# AADL Model — Installation and Codegen

This page covers installation, codegen, simulation, and verification for the **AADL model** of
the Isolette.  The AADL model uses OSATE/FMIDE for codegen.  Generated code lives in
[`hamr/slang/`](hamr/slang/) (JVM target) and [`hamr/microkit/`](hamr/microkit/) (Microkit
with domain scheduling).

See [readme.md](readme.md) for an overview of the system architecture and GUMBO contracts.

## Table of Contents

- [Installation](#installation)
- [Codegen](#codegen)
  - [JVM (Slang) + runtime monitoring](#jvm-slang--runtime-monitoring)
  - [Microkit (Domain Scheduling) + runtime monitoring](#microkit-domain-scheduling--runtime-monitoring)
    - [Static System Verification](#static-system-verification)

## Installation

1. Install [Docker Desktop](https://www.docker.com/products/docker-desktop/)

1. Clone this repo and cd into it

   ```sh
   git clone https://github.com/loonwerks/INSPECTA-models.git
   cd INSPECTA-models
   ```

1. Development Tools

    This documentation covers several distinct tasks, each requiring specific tools (e.g., Verus for Rust verification or Sireum for HAMR code generation). Ensure you have installed the tools required by the task by following the [official installation instructions](https://hamr.sireum.org/hamr-doc/installation/).

## Codegen

### JVM (Slang) + runtime monitoring

1. *OPTIONAL* Rerun codegen targeting the JVM

    **Requires:**
    - Sireum
    - FMIDE

    Launch the Slash script [isolette/aadl/bin/run-hamr.cmd](aadl/bin/run-hamr.cmd) targeting the JVM

    ```sh
    isolette/aadl/bin/run-hamr.cmd --platform JVM --runtime-monitoring --slang-output-dir isolette/hamr/slang
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

    ```sh
    sireum hamr sysml logika --sourcepath isolette/sysml
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
    - FMIDE

    Launch the Slash script [isolette/aadl/bin/run-hamr.cmd](aadl/bin/run-hamr.cmd) targeting Microkit

    ```sh
    isolette/aadl/bin/run-hamr.cmd --platform Microkit --runtime-monitoring --sel4-output-dir isolette/hamr/microkit
    ```

1. Run Rust unit tests

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
    Booting all finished, dropped to user space
    MON|INFO: Microkit Bootstrap
    MON|INFO: bootinfo untyped list matches expected list
    MON|INFO: Number of bootstrap invocations: 0x0000000a
    MON|INFO: Number of system invocations:    0x00000568
    MON|INFO: completed bootstrap invocations
    MON|INFO: completed system invocations
    temperature_sensor_cpi_thermostat: temperature_sensor_cpi_thermostat_initialize invoked
    INFO  [thermostat_mt_mmm_mmm::component::thermostat_mt_mmm_mmm_app] initialize entrypoint invoked
    INFO  [thermostat_mt_mmi_mmi::component::thermostat_mt_mmi_mmi_app] initialize entrypoint invoked
    INFO  [thermostat_mt_ma_ma::component::thermostat_mt_ma_ma_app] initialize entrypoint invoked
    INFO  [thermostat_mt_dmf_dmf::component::thermostat_mt_dmf_dmf_app] initialize entrypoint invoked
    INFO  [thermostat_rt_mri_mri::component::thermostat_rt_mri_mri_app] initialize entrypoint invoked
    INFO  [thermostat_rt_mrm_mrm::component::thermostat_rt_mrm_mrm_app] initialize entrypoint invoked
    INFO  [thermostat_rt_mhs_mhs::component::thermostat_rt_mhs_mhs_app] initialize entrypoint invoked
    INFO  [thermostat_rt_drf_drf::component::thermostat_rt_drf_drf_app] initialize entrypoint invoked
    heat_source_cpi_heat_controller: heat_source_cpi_heat_controller_initialize invoked
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] initialize entrypoint invoked
    ####### FRAME 0 #######
    INFO  [thermostat_rt_mhs_mhs::component::thermostat_rt_mhs_mhs_app] Sent Onn
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Regulator Status: Init_Status
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Monitor Status: On_Status
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Display Temperature: Temp_i { degrees: 0 }
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Alarm: Onn
    ####### FRAME 1 #######
    INFO  [thermostat_rt_mhs_mhs::component::thermostat_rt_mhs_mhs_app] Sent Onn
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Regulator Status: On_Status
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Monitor Status: On_Status
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Display Temperature: Temp_i { degrees: 96 }
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Alarm: Onn
    ####### FRAME 2 #######
    INFO  [thermostat_rt_mhs_mhs::component::thermostat_rt_mhs_mhs_app] Sent Onn
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Regulator Status: On_Status
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Monitor Status: On_Status
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Display Temperature: Temp_i { degrees: 95 }
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Alarm: Onn
    ####### FRAME 3 #######
    INFO  [thermostat_rt_mhs_mhs::component::thermostat_rt_mhs_mhs_app] Sent Onn
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Regulator Status: On_Status
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Monitor Status: On_Status
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Display Temperature: Temp_i { degrees: 95 }
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Alarm: Onn
    ####### FRAME 4 #######
    INFO  [thermostat_rt_mhs_mhs::component::thermostat_rt_mhs_mhs_app] Sent Onn
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Regulator Status: On_Status
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Monitor Status: On_Status
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Display Temperature: Temp_i { degrees: 96 }
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Alarm: Onn
    ####### FRAME 5 #######
    INFO  [thermostat_rt_mhs_mhs::component::thermostat_rt_mhs_mhs_app] Sent Onn
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Regulator Status: On_Status
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Monitor Status: On_Status
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Display Temperature: Temp_i { degrees: 96 }
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Alarm: Onn
    ####### FRAME 6 #######
    INFO  [thermostat_rt_mhs_mhs::component::thermostat_rt_mhs_mhs_app] Sent Onn
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Regulator Status: On_Status
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Monitor Status: On_Status
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Display Temperature: Temp_i { degrees: 97 }
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Alarm: Onn
    ####### FRAME 7 #######
    INFO  [thermostat_rt_mhs_mhs::component::thermostat_rt_mhs_mhs_app] Sent Onn
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Regulator Status: On_Status
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Monitor Status: On_Status
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Display Temperature: Temp_i { degrees: 97 }
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Alarm: Onn
    ####### FRAME 8 #######
    INFO  [thermostat_rt_mhs_mhs::component::thermostat_rt_mhs_mhs_app] Sent Onn
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Regulator Status: On_Status
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Monitor Status: On_Status
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Display Temperature: Temp_i { degrees: 98 }
    INFO  [operator_interface_oip_oit::component::operator_interface_oip_oit_app] Alarm: Off
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
[`composition nominal`](aadl/aadl/packages/Isolette.aadl#L545) block — without needing an actual
running schedule.  There are two levels:

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
