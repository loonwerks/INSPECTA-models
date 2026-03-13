# SysML Model — Installation and Codegen

This page covers installation, codegen, and simulation for the **SysML model**
of this micro-example.  The SysML model uses MCS (Mixed-Criticality Scheduling)
user-land scheduling and the generated code lives in
[`hamr/microkit_mcs/`](hamr/microkit_mcs/).

See [readme.md](readme.md) for an overview of both models, the complete list
of supported data types, and an explanation of the generated C and Rust APIs.

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
    [micro-examples/microkit/aadl_datatypes/sysml/bin/run-hamr.cmd](sysml/bin/run-hamr.cmd)
    from the command line, passing `Microkit` as the platform argument.

   ```
   micro-examples/microkit/aadl_datatypes/sysml/bin/run-hamr.cmd Microkit
   ```

1. Build and simulate the seL4 Microkit image with MCS scheduling

    Run the following from this repository's root directory.  The
    ``MICROKIT_SDK`` environment variable must point to a Microkit SDK that
    supports MCS scheduling.  Inside the docker container it is located at
    ``/home/microkit/provers/microkit-sdk-2.1.0``.

    ```
    docker run -it --rm -v $(pwd):/home/microkit/INSPECTA-models jasonbelt/microkit_provers bash -ci \
        "cd INSPECTA-models/micro-examples/microkit/aadl_datatypes/hamr/microkit_mcs && \
        MICROKIT_SDK=/home/microkit/provers/microkit-sdk-2.1.0 && \
        make clean && make qemu"
    ```

    Type ``CTRL-a x`` to exit the QEMU simulation

    You should get output similar to

    ```
    Booting all finished, dropped to user space
    INFO  [sel4_capdl_initializer::initialize] Starting CapDL initializer
    INFO  [sel4_capdl_initializer::initialize] Starting threads
    MON|INFO: Microkit Monitor started!
    MON|INFO: PD 'timer_driver' is now passive!
    producer_producer_MON | INIT!
    MON|INFO: PD 'producer_producer_MON' is now passive!
    consumer_consumer_MON | INIT!
    MON|INFO: PD 'consumer_consumer_MON' is now passive!
    producer_producer | INIT!
    producer_producer: producer_producer_initialize invoked
    SCHEDULER | Marking partition 2 as ready
    MON|INFO: PD 'producer_producer' is now passive!
    consumer_consumer | INIT!
    INFO  [consumer_consumer::component::consumer_consumer_app] initialize entrypoint invoked
    SCHEDULER | Marking partition 3 as ready
    SCHEDULER | All partitions ready, beginning schedule
    MON|INFO: PD 'consumer_consumer' is now passive!
    producer_producer: producer_producer_timeTriggered invoked
    INFO  [consumer_consumer::component::consumer_consumer_app] compute entrypoint invoked
    INFO  [consumer_consumer::component::consumer_consumer_app] false/false
    INFO  [consumer_consumer::component::consumer_consumer_app] -42/1
    producer_producer: producer_producer_timeTriggered invoked
    INFO  [consumer_consumer::component::consumer_consumer_app] compute entrypoint invoked
    INFO  [consumer_consumer::component::consumer_consumer_app] false/true
    INFO  [consumer_consumer::component::consumer_consumer_app] 1/2
    producer_producer: producer_producer_timeTriggered invoked
    INFO  [consumer_consumer::component::consumer_consumer_app] compute entrypoint invoked
    INFO  [consumer_consumer::component::consumer_consumer_app] true/false
    INFO  [consumer_consumer::component::consumer_consumer_app] 2/3
    producer_producer: producer_producer_timeTriggered invoked
    INFO  [consumer_consumer::component::consumer_consumer_app] compute entrypoint invoked
    INFO  [consumer_consumer::component::consumer_consumer_app] false/true
    INFO  [consumer_consumer::component::consumer_consumer_app] 3/4
    producer_producer: producer_producer_timeTriggered invoked
    INFO  [consumer_consumer::component::consumer_consumer_app] compute entrypoint invoked
    INFO  [consumer_consumer::component::consumer_consumer_app] true/false
    INFO  [consumer_consumer::component::consumer_consumer_app] 4/5
    producer_producer: producer_producer_timeTriggered invoked
    INFO  [consumer_consumer::component::consumer_consumer_app] compute entrypoint invoked
    INFO  [consumer_consumer::component::consumer_consumer_app] false/true
    INFO  [consumer_consumer::component::consumer_consumer_app] 5/6    
    ```