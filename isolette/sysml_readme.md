# SysML Model — Installation and Codegen

This page covers installation, codegen, simulation, and verification for the **SysML model** of
the Isolette.  The SysML model uses Sireum directly for codegen (no OSATE required).  Generated
code lives in [`hamr/slang/`](hamr/slang/) (JVM target) and [`hamr/microkit/`](hamr/microkit/)
(Microkit target).

See [readme.md](readme.md) for an overview of the system architecture and GUMBO contracts.

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

### JVM (Slang)

1. *OPTIONAL* Rerun codegen targeting the JVM

    **Requires:**
    - Sireum
    - SysMLv2 AADL Libraries

    Launch the Slash script [isolette/sysml/bin/run-hamr.cmd](sysml/bin/run-hamr.cmd) from the command line targeting the JVM

    ```sh
    isolette/sysml/bin/run-hamr.cmd JVM
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
    sireum hamr sysm logika --sourcepath isolette/sysml
    ```

    If you instead set the ``SYSML_AADL_LIBRARIES`` environment variable then run

    ```sh
    sireum hamr sysm logika --sourcepath isolette/sysml:$SYSML_AADL_LIBRARIES
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
    - SysMLv2 AADL Libraries

    Launch the Slash script [isolette/sysml/bin/run-hamr.cmd](sysml/bin/run-hamr.cmd) targeting Microkit

    ```sh
    isolette/sysml/bin/run-hamr.cmd Microkit
    ```

1. Run the Rust unit tests
    
    **Requires:**
    - Rust

    ```
    make -C isolette/hamr/microkit test
    ```

1. Verify model-level integration constraint contracts with Logika

    *Refer to this [task](#logika-constraint-check) in the JVM section*

1. Verify code-level contracts with Verus

    **Requires:**
    - Rust
    - Verus

    ```
    make -C isolette/hamr/microkit verus
    ```

1. Build and simulate the seL4 Microkit image

    Run the following from this repository's root directory.  The docker image
    `jasonbelt/microkit_provers` contains customized versions of Microkit and seL4 that
    support domain scheduling, built off
    [microkit #175](https://github.com/seL4/microkit/pull/175) and
    [seL4 #1308](https://github.com/seL4/seL4/pull/1308).

    The build uses ``cargo-verus`` which also verifies the code-level contracts.

    ```
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
