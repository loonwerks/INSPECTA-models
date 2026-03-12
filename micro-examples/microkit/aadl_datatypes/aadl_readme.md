# AADL Model — Installation and Codegen

This page covers installation, codegen, and simulation for the **AADL model**
of this micro-example.  The AADL model uses seL4 domain scheduling and the
generated code lives in [`hamr/microkit/`](hamr/microkit/).

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

    If you want to rerun codegen then you will need to install HAMR
    and OSATE.  You can do this inside or outside of the container that you'll pull in the next section (the latter is probably preferable as you could then use Sireum outside of the container).

    [HAMR Installation Instructions](https://hamr.sireum.org/hamr-doc/installation/)

## Codegen

1. *OPTIONAL* Rerun codegen targeting Microkit

    Launch the Slash script [micro-examples/microkit/aadl_datatypes/aadl/bin/run-hamr.cmd](aadl/bin/run-hamr.cmd) from the command line, passing Microkit as the platform argument.

   ```
   micro-examples/microkit/aadl_datatypes/aadl/bin/run-hamr.cmd Microkit
   ```

1. Build and simulate the seL4 Microkit image

    Run the following from this repository's root directory.

    ```
    docker run -it --rm -v $(pwd):/home/microkit/provers/INSPECTA-models jasonbelt/microkit_provers \
      bash -ci "cd \$HOME/provers/INSPECTA-models/micro-examples/microkit/aadl_datatypes/hamr/microkit \
                && make clean && make qemu"
    ```

    Type ``CTRL-a x`` to exit the QEMU simulation

    You should get output similar to

    ```
    Booting all finished, dropped to user space
    MON|INFO: Microkit Bootstrap
    MON|INFO: bootinfo untyped list matches expected list
    MON|INFO: Number of bootstrap invocations: 0x00000009
    MON|INFO: Number of system invocations:    0x0000011a
    MON|INFO: completed bootstrap invocations
    MON|INFO: completed system invocations
    producer_producer: producer_producer_initialize invoked
    INFO  [consumer_consumer::component::consumer_consumer_app] initialize entrypoint invoked
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