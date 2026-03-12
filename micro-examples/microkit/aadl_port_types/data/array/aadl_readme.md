# AADL Model — Installation and Codegen

This page covers installation, codegen, and simulation for the **AADL model**
of this micro-example.  The AADL model uses seL4 domain scheduling and the
generated code lives in [`hamr/microkit/`](hamr/microkit/).

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

    If you want to rerun codegen then you will need to install HAMR
    and OSATE.  You can do this inside or outside of the container that you'll pull in the next section (the latter is probably preferable as you could then use Sireum outside of the container).

    [HAMR Installation Instructions](https://hamr.sireum.org/hamr-doc/installation/)

## Codegen

1. *OPTIONAL* Rerun codegen targetting Microkit

    Launch the Slash script [micro-examples/microkit/aadl_port_types/data/array/aadl/bin/run-hamr.cmd](aadl/bin/run-hamr.cmd) from the command line, passing `Microkit` as the platform argument.

   ```
   micro-examples/microkit/aadl_port_types/data/array/aadl/bin/run-hamr.cmd Microkit
   ```
   
1. Build and simulate the seL4 Microkit image

    Run the following from this repository's root directory.  The docker image ``jasonbelt/microkit_provers`` contains customized versions of Microkit and seL4 that support domain scheduling. They were built off the following pull requests

   - [microkit #175](https://github.com/seL4/microkit/pull/175)
   - [seL4 #1308](https://github.com/seL4/seL4/pull/1308)

    ```
    docker run -it --rm -v $(pwd):/home/microkit/provers/INSPECTA-models jasonbelt/microkit_provers \
      bash -ci "cd \$HOME/provers/INSPECTA-models/micro-examples/microkit/aadl_port_types/data/array/hamr/microkit \
                && make qemu"
    ```

    Type ``CTRL-a x`` to exit the QEMU simulation

   The producer is populating [this](aadl/data_1_prod_2_cons.aadl#L23-L29) datatype via [this](hamr/microkit/components/producer_p_p_producer/src/producer_p_p_producer_user.c#L17-L35) implementation to the consumers so you should get output similar to

    ```
   Booting all finished, dropped to user space
   MON|INFO: Microkit Bootstrap
   MON|INFO: bootinfo untyped list matches expected list
   MON|INFO: Number of bootstrap invocations: 0x00000009
   MON|INFO: Number of system invocations:    0x000000c0
   MON|INFO: completed bootstrap invocations
   MON|INFO: completed system invocations
   producer_p_p_producer: I'm periodic
   consumer_p_p_consumer: I'm periodic
   consumer_p_s_consumer: I'm sporadic so you'll never hear from me again :(
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
