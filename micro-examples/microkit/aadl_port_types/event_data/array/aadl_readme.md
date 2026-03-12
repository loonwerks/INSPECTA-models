# AADL Model — Installation and Codegen

This page covers installation, codegen, and simulation for the **AADL model**
of this micro-example.  The AADL model uses seL4 domain scheduling and the
generated code lives in [`hamr/microkit/`](hamr/microkit/).

See [readme.md](readme.md) for an overview of both models and an explanation
of AADL event data port semantics.

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

    Launch the Slash script [micro-examples/microkit/aadl_port_types/event_data/array/aadl/bin/run-hamr.cmd](aadl/bin/run-hamr.cmd) from the command line, passing `Microkit` as the platform argument.

   ```
   micro-examples/microkit/aadl_port_types/event_data/array/aadl/bin/run-hamr.cmd Microkit
   ```

1. Build and simulate the seL4 Microkit image

    Run the following from this repository's root directory.

    ```
    docker run -it --rm -v $(pwd):/home/microkit/provers/INSPECTA-models jasonbelt/microkit_provers \
      bash -ci "cd \$HOME/provers/INSPECTA-models/micro-examples/microkit/aadl_port_types/event_data/array/hamr/microkit \
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
    producer_p_p1_producer: sent event with 0 elements
    producer_p_p2_producer: sent event with 0 elements
    consumer_p_p_consumer: received [] on read port 1
    consumer_p_p_consumer: received [] on read port 2
    consumer_p_s_consumer: received [] on read port 1
    consumer_p_s_consumer: received [] on read port 2
    -------
    producer_p_p1_producer: no send
    producer_p_p2_producer: no send
    consumer_p_p_consumer: nothing received on read port 1
    consumer_p_p_consumer: nothing received on read port 2
    -------
    producer_p_p1_producer: sent event with 2 elements
    producer_p_p2_producer: no send
    consumer_p_p_consumer: received [(0, 2), (1, 2)] on read port 1
    consumer_p_p_consumer: nothing received on read port 2
    consumer_p_s_consumer: received [(0, 2), (1, 2)] on read port 1
    -------
    producer_p_p1_producer: no send
    producer_p_p2_producer: sent event with 3 elements
    consumer_p_p_consumer: nothing received on read port 1
    consumer_p_p_consumer: received [(0, 3), (1, 3), (2, 3)] on read port 2
    consumer_p_s_consumer: received [(0, 3), (1, 3), (2, 3)] on read port 2
    -------
    producer_p_p1_producer: sent event with 4 elements
    producer_p_p2_producer: no send
    consumer_p_p_consumer: received [(0, 4), (1, 4), (2, 4), (3, 4)] on read port 1
    consumer_p_p_consumer: nothing received on read port 2
    consumer_p_s_consumer: received [(0, 4), (1, 4), (2, 4), (3, 4)] on read port 1
    -------
    producer_p_p1_producer: no send
    producer_p_p2_producer: no send
    consumer_p_p_consumer: nothing received on read port 1
    consumer_p_p_consumer: nothing received on read port 2
    -------
    producer_p_p1_producer: sent event with 6 elements
    producer_p_p2_producer: sent event with 6 elements
    consumer_p_p_consumer: received [(0, 6), (1, 6), (2, 6), (3, 6), (4, 6), (5, 6)] on read port 1
    consumer_p_p_consumer: received [(0, 6), (1, 6), (2, 6), (3, 6), (4, 6), (5, 6)] on read port 2
    consumer_p_s_consumer: received [(0, 6), (1, 6), (2, 6), (3, 6), (4, 6), (5, 6)] on read port 1
    consumer_p_s_consumer: received [(0, 6), (1, 6), (2, 6), (3, 6), (4, 6), (5, 6)] on read port 2
    -------
    producer_p_p1_producer: no send
    producer_p_p2_producer: no send
    consumer_p_p_consumer: nothing received on read port 1
    consumer_p_p_consumer: nothing received on read port 2
    -------
    producer_p_p1_producer: sent event with 8 elements
    producer_p_p2_producer: no send
    consumer_p_p_consumer: received [(0, 8), (1, 8), (2, 8), (3, 8), (4, 8), (5, 8), (6, 8), (7, 8)] on read port 1
    consumer_p_p_consumer: nothing received on read port 2
    consumer_p_s_consumer: received [(0, 8), (1, 8), (2, 8), (3, 8), (4, 8), (5, 8), (6, 8), (7, 8)] on read port 1
    -------
    producer_p_p1_producer: no send
    producer_p_p2_producer: sent event with 9 elements
    consumer_p_p_consumer: nothing received on read port 1
    consumer_p_p_consumer: received [(0, 9), (1, 9), (2, 9), (3, 9), (4, 9), (5, 9), (6, 9), (7, 9), (8, 9)] on read port 2
    consumer_p_s_consumer: received [(0, 9), (1, 9), (2, 9), (3, 9), (4, 9), (5, 9), (6, 9), (7, 9), (8, 9)] on read port 2
    -------
    producer_p_p1_producer: sent event with 0 elements
    producer_p_p2_producer: sent event with 0 elements
    consumer_p_p_consumer: received [] on read port 1
    consumer_p_p_consumer: received [] on read port 2
    consumer_p_s_consumer: received [] on read port 1
    consumer_p_s_consumer: received [] on read port 2
    -------
    ```
