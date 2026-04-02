# AADL Model — Installation and Codegen

This page covers installation, codegen, and simulation for the **AADL model**
of this micro-example.  The AADL model uses seL4 domain scheduling and the
generated code lives in [`hamr/microkit/`](hamr/microkit/).

See [readme.md](readme.md) for an overview of both models and an explanation
of AADL event data port semantics and queue sizes.

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

    Launch the Slash script [micro-examples/microkit/port_queues/event_data_array/aadl/bin/run-hamr.cmd](aadl/bin/run-hamr.cmd) from the command line, passing `Microkit` as the platform argument.

   ```
   micro-examples/microkit/port_queues/event_data_array/aadl/bin/run-hamr.cmd Microkit
   ```

1. Build and simulate the seL4 Microkit image

    Run the following from this repository's root directory.

    ```
    docker run -it --rm -v $(pwd):/home/microkit/provers/INSPECTA-models jasonbelt/microkit_provers \
      bash -ci "cd \$HOME/provers/INSPECTA-models/micro-examples/microkit/port_queues/event_data_array/hamr/microkit \
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
    producer_p_p_producer: I'm periodic
    consumer_p_s1_consumer: I'm sporadic
    consumer_p_s2_consumer: I'm sporadic
    consumer_p_s5_consumer: I'm sporadic
    ---------------------------------------
    producer_p_p_producer: Sent 0 events.
    ---------------------------------------
    producer_p_p_producer: Sent 1 events.
    consumer_p_s1_consumer: received []
    consumer_p_s2_consumer: received []
    consumer_p_s5_consumer: received []
    ---------------------------------------
    producer_p_p_producer: Sent 2 events.
    consumer_p_s1_consumer: received [(0, 1)]
    consumer_p_s2_consumer: received []
    consumer_p_s2_consumer: received [(0, 1)]
    consumer_p_s5_consumer: received []
    consumer_p_s5_consumer: received [(0, 1)]
    ---------------------------------------
    producer_p_p_producer: Sent 3 events.
    consumer_p_s1_consumer: received [(0, 2), (1, 2)]
    consumer_p_s2_consumer: received [(0, 1)]
    consumer_p_s2_consumer: received [(0, 2), (1, 2)]
    consumer_p_s5_consumer: received []
    consumer_p_s5_consumer: received [(0, 1)]
    consumer_p_s5_consumer: received [(0, 2), (1, 2)]
    ---------------------------------------
    producer_p_p_producer: Sent 4 events.
    consumer_p_s1_consumer: received [(0, 3), (1, 3), (2, 3)]
    consumer_p_s2_consumer: received [(0, 2), (1, 2)]
    consumer_p_s2_consumer: received [(0, 3), (1, 3), (2, 3)]
    consumer_p_s5_consumer: received []
    consumer_p_s5_consumer: received [(0, 1)]
    consumer_p_s5_consumer: received [(0, 2), (1, 2)]
    consumer_p_s5_consumer: received [(0, 3), (1, 3), (2, 3)]
    ---------------------------------------
    producer_p_p_producer: Sent 5 events.
    consumer_p_s1_consumer: received [(0, 4), (1, 4), (2, 4), (3, 4)]
    consumer_p_s2_consumer: received [(0, 3), (1, 3), (2, 3)]
    consumer_p_s2_consumer: received [(0, 4), (1, 4), (2, 4), (3, 4)]
    consumer_p_s5_consumer: received []
    consumer_p_s5_consumer: received [(0, 1)]
    consumer_p_s5_consumer: received [(0, 2), (1, 2)]
    consumer_p_s5_consumer: received [(0, 3), (1, 3), (2, 3)]
    consumer_p_s5_consumer: received [(0, 4), (1, 4), (2, 4), (3, 4)]
    ---------------------------------------
    producer_p_p_producer: Sent 6 events.
    consumer_p_s1_consumer: received [(0, 5), (1, 5), (2, 5), (3, 5), (4, 5)]
    consumer_p_s2_consumer: received [(0, 4), (1, 4), (2, 4), (3, 4)]
    consumer_p_s2_consumer: received [(0, 5), (1, 5), (2, 5), (3, 5), (4, 5)]
    consumer_p_s5_consumer: received [(0, 1)]
    consumer_p_s5_consumer: received [(0, 2), (1, 2)]
    consumer_p_s5_consumer: received [(0, 3), (1, 3), (2, 3)]
    consumer_p_s5_consumer: received [(0, 4), (1, 4), (2, 4), (3, 4)]
    consumer_p_s5_consumer: received [(0, 5), (1, 5), (2, 5), (3, 5), (4, 5)]
    ---------------------------------------
    producer_p_p_producer: Sent 0 events.
    ```
