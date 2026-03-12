# SysML Model — Installation and Codegen

This page covers installation, codegen, and simulation for the **SysML model**
of this micro-example.  The SysML model uses MCS (Mixed-Criticality Scheduling)
user-land scheduling and the generated code lives in
[`hamr/microkit_mcs/`](hamr/microkit_mcs/).

See [readme.md](readme.md) for an overview of both models and an explanation
of AADL event port semantics.

## Installation

1. Install [Docker Desktop](https://www.docker.com/products/docker-desktop/)

1. Clone this repo and cd into it

   ```
   git clone https://github.com/loonwerks/INSPECTA-models.git
   cd INSPECTA-models
   ```

1. *OPTIONAL*

    If you want to rerun codegen then you will need to install HAMR.  You can
    do this inside or outside of the container that you'll pull in the next
    section (the latter is probably preferable as you could then use Sireum
    outside of the container).

    [HAMR Installation Instructions](https://hamr.sireum.org/hamr-doc/installation/)

## Codegen

1. *OPTIONAL* Rerun codegen targeting Microkit

    Launch the Slash script
    [micro-examples/microkit/aadl_port_types/event/sysml/bin/run-hamr.cmd](sysml/bin/run-hamr.cmd)
    from the command line, passing `Microkit` as the platform argument.

   ```
   micro-examples/microkit/aadl_port_types/event/sysml/bin/run-hamr.cmd Microkit
   ```

1. Build and simulate the seL4 Microkit image with MCS scheduling

    Run the following from this repository's root directory.  The
    ``MICROKIT_SDK`` environment variable must point to a Microkit SDK that
    supports MCS scheduling.  Inside the docker container it is located at
    ``/home/microkit/provers/microkit-sdk-2.1.0``.

    ```
    docker run -it --rm -v $(pwd):/home/microkit/INSPECTA-models jasonbelt/microkit_provers bash -ci \
        "cd INSPECTA-models/micro-examples/microkit/aadl_port_types/event/hamr/microkit_mcs && \
        MICROKIT_SDK=/home/microkit/provers/microkit-sdk-2.1.0 && \
        make clean && make qemu"
    ```

    Type ``CTRL-a x`` to exit the QEMU simulation
