# SysML Model — Installation and Codegen

This page covers installation, codegen, and simulation for the **SysML model**
of this micro-example.  The SysML model uses MCS (Mixed-Criticality Scheduling)
user-land scheduling and the generated code lives in
[`hamr/microkit_mcs/`](hamr/microkit_mcs/).

See [readme.md](readme.md) for an overview of both models and an explanation
of AADL data port semantics.

## Installation

1. Install [Docker Desktop](https://www.docker.com/products/docker-desktop/)

1. Clone this repo and cd into it

   ```
   git clone https://github.com/loonwerks/INSPECTA-models.git
   cd INSPECTA-models
   ```

1. *OPTIONAL*

    If you want to rerun codegen then you will need to install HAMR.  You can do this inside or outside of the container that you'll pull in the next section (the latter is probably preferable as you could then use Sireum outside of the container).

    [HAMR Installation Instructions](https://hamr.sireum.org/hamr-doc/installation/)

## Codegen

1. *OPTIONAL* Rerun codegen targeting Microkit

    Launch the Slash script
    [micro-examples/microkit/aadl_port_types/data/array/sysml/bin/run-hamr.cmd](sysml/bin/run-hamr.cmd)
    from the command line, passing `Microkit` as the platform argument.

   ```
   micro-examples/microkit/aadl_port_types/data/array/sysml/bin/run-hamr.cmd Microkit
   ```

1. Build and simulate the seL4 Microkit image with MCS scheduling

    Run the following from this repository's root directory.  The
    ``MICROKIT_SDK`` environment variable must point to a Microkit SDK that
    supports MCS scheduling.  Inside the docker container it is located at
    ``/home/microkit/provers/microkit-sdk-2.1.0``.

    ```
    docker run -it --rm -v $(pwd):/home/microkit/INSPECTA-models jasonbelt/microkit_provers bash -ci \
        "cd INSPECTA-models/micro-examples/microkit/aadl_port_types/data/array/hamr/microkit_mcs && \
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
    producer_p_p_producer_MON | INIT!
    MON|INFO: PD 'producer_p_p_producer_MON' is now passive!
    consumer_p_s_consumer_MON | INIT!
    MON|INFO: PD 'consumer_p_s_consumer_MON' is now passive!
    consumer_p_p_consumer_MON | INIT!
    MON|INFO: PD 'consumer_p_p_consumer_MON' is now passive!
    producer_p_p_producer | INIT!
    producer_p_p_producer: I'm periodic
    SCHEDULER | Marking partition 2 as ready
    MON|INFO: PD 'producer_p_p_producer' is now passive!
    consumer_p_s_consumer | INIT!
    consumer_p_s_consumer: I'm sporadic so you'll never hear from me again :(
    consumer_p_p_consumer | INIT!
    consumer_p_p_consumer: I'm periodic
    SCHEDULER | Marking partition 3 as ready
    MON|INFO: PD 'consumer_p_p_consumer' is now passive!
    SCHEDULER | Marking partition 4 as ready
    SCHEDULER | All partitions ready, beginning schedule
    MON|INFO: PD 'consumer_p_s_consumer' is now passive!
    ---------
    producer_p_p_producer: put array with 0 elements
    consumer_p_p_consumer: retrieved [] which is fresh
    ---------
    producer_p_p_producer: didn't put anything
    consumer_p_p_consumer: retrieved [] which is stale
    ---------
    producer_p_p_producer: put array with 2 elements
    consumer_p_p_consumer: retrieved [(0, 2), (1, 2)] which is fresh
    ---------
    producer_p_p_producer: didn't put anything
    consumer_p_p_consumer: retrieved [(0, 2), (1, 2)] which is stale
    ---------
    producer_p_p_producer: put array with 4 elements
    consumer_p_p_consumer: retrieved [(0, 4), (1, 4), (2, 4), (3, 4)] which is fresh
    ---------
    producer_p_p_producer: didn't put anything
    consumer_p_p_consumer: retrieved [(0, 4), (1, 4), (2, 4), (3, 4)] which is stale
    ---------
    producer_p_p_producer: put array with 6 elements
    consumer_p_p_consumer: retrieved [(0, 6), (1, 6), (2, 6), (3, 6), (4, 6), (5, 6)] which is fresh
    ---------
    producer_p_p_producer: didn't put anything
    consumer_p_p_consumer: retrieved [(0, 6), (1, 6), (2, 6), (3, 6), (4, 6), (5, 6)] which is stale
    ---------
    producer_p_p_producer: put array with 8 elements
    consumer_p_p_consumer: retrieved [(0, 8), (1, 8), (2, 8), (3, 8), (4, 8), (5, 8), (6, 8), (7, 8)] which is fresh
    ---------
    producer_p_p_producer: didn't put anything
    consumer_p_p_consumer: retrieved [(0, 8), (1, 8), (2, 8), (3, 8), (4, 8), (5, 8), (6, 8), (7, 8)] which is stale
    ---------
    producer_p_p_producer: put array with 0 elements
    consumer_p_p_consumer: retrieved [] which is fresh    
    ```