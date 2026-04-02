# AADL Model — Installation and Codegen

This page covers installation, codegen, and simulation for the **AADL model**
of this micro-example.  The AADL model uses seL4 domain scheduling and the
generated code lives in [`hamr/microkit/`](hamr/microkit/).

See [readme.md](readme.md) for an overview of all models and an explanation
of AADL event port semantics.

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

1. *OPTIONAL* Rerun codegen targeting Microkit

    Launch the Slash script [micro-examples/microkit/aadl_port_types/event/aadl/bin/run-hamr.cmd](aadl/bin/run-hamr.cmd) from the command line, passing `Microkit` as the platform argument.

   ```
   micro-examples/microkit/aadl_port_types/event/aadl/bin/run-hamr.cmd Microkit
   ```

1. Build and simulate the seL4 Microkit image

    Run the following from this repository's root directory.  The docker image ``jasonbelt/microkit_domain_scheduling`` contains customized versions of Microkit and seL4 that support domain scheduling. They were built off the following pull requests

   - [microkit #175](https://github.com/seL4/microkit/pull/175)
   - [seL4 #1308](https://github.com/seL4/seL4/pull/1308)

    ```
    docker run -it --rm -v $(pwd):/home/microkit/provers/INSPECTA-models jasonbelt/microkit_provers \
      bash -ci "cd \$HOME/provers/INSPECTA-models/micro-examples/microkit/aadl_port_types/event/hamr/microkit \
                && make clean && make qemu"
    ```

    Type ``CTRL-a x`` to exit the QEMU simulation

    You should get output similar to

    ```
   Booting all finished, dropped to user space
   MON|INFO: Microkit Bootstrap
   MON|INFO: bootinfo untyped list matches expected list
   MON|INFO: Number of bootstrap invocations: 0x00000009
   MON|INFO: Number of system invocations:    0x000000f7
   MON|INFO: completed bootstrap invocations
   MON|INFO: completed system invocations
   producer_p_p1_producer: I'm periodic
   producer_p_p2_producer: I'm periodic
   consumer_p_p_consumer: I'm periodic
   consumer_p_s_consumer: I'm sporadic
   -------
   producer_p_p1_producer: sent an event
   producer_p_p2_producer: sent an event
   consumer_p_p_consumer: received a read port 1 event
   consumer_p_p_consumer: received a read port 2 event
   consumer_p_s_consumer: received a read port 1 event
   consumer_p_s_consumer: received a read port 2 event
   -------
   producer_p_p1_producer: no send
   producer_p_p2_producer: no send
   consumer_p_p_consumer: nothing received on read port 1
   consumer_p_p_consumer: nothing received on read port 2
   -------
   producer_p_p1_producer: sent an event
   producer_p_p2_producer: no send
   consumer_p_p_consumer: received a read port 1 event
   consumer_p_p_consumer: nothing received on read port 2
   consumer_p_s_consumer: received a read port 1 event
   -------
   producer_p_p1_producer: no send
   producer_p_p2_producer: sent an event
   consumer_p_p_consumer: nothing received on read port 1
   consumer_p_p_consumer: received a read port 2 event
   consumer_p_s_consumer: received a read port 2 event
   -------
   producer_p_p1_producer: sent an event
   producer_p_p2_producer: no send
   consumer_p_p_consumer: received a read port 1 event
   consumer_p_p_consumer: nothing received on read port 2
   consumer_p_s_consumer: received a read port 1 event
   -------
   producer_p_p1_producer: no send
   producer_p_p2_producer: no send
   consumer_p_p_consumer: nothing received on read port 1
   consumer_p_p_consumer: nothing received on read port 2
   -------
   producer_p_p1_producer: sent an event
   producer_p_p2_producer: sent an event
   consumer_p_p_consumer: received a read port 1 event
   consumer_p_p_consumer: received a read port 2 event
   consumer_p_s_consumer: received a read port 1 event
   consumer_p_s_consumer: received a read port 2 event
    ```
