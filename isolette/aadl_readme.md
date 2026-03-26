# AADL Model — Installation and Codegen

This page covers installation, codegen, simulation, and verification for the **AADL model** of
the Isolette.  The AADL model uses OSATE/FMIDE for codegen.  Generated code lives in
[`hamr/slang/`](hamr/slang/) (JVM target) and [`hamr/microkit/`](hamr/microkit/) (Microkit
target).

See [readme.md](readme.md) for an overview of the system architecture and GUMBO contracts.

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

### JVM (Slang)

1. *OPTIONAL* Rerun codegen targeting the JVM

    **Requires:**
    - Sireum
    - FMIDE

    Launch the Slash script [isolette/aadl/bin/run-hamr.cmd](aadl/bin/run-hamr.cmd) targeting the JVM

    ```sh
    isolette/aadl/bin/run-hamr.cmd JVM
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

1. Verify code-level contracts with Logika

    **Requires:**
    - Sireum

    ```sh
    isolette/hamr/slang/bin/run-logika.cmd
    ```

### Microkit

1. *OPTIONAL* Rerun codegen targeting Microkit

    **Requires:**
    - Sireum
    - FMIDE

    Launch the Slash script [isolette/aadl/bin/run-hamr.cmd](aadl/bin/run-hamr.cmd) targting Microkit

    ```sh
    isolette/aadl/bin/run-hamr.cmd Microkit
    ```

1. Run Rust unit tests
    
    **Requires:**
    - Rust

    ```sh
    make -C isolette/hamr/microkit test
    ```

1. Verify code-level contracts with Verus

    **Requires:**
    - Rust
    - Verus

    ```sh
    make -C isolette/hamr/microkit verus
    ```

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
