# AADL Port Queues

 Table of Contents
  * [Diagrams](#diagrams)
    * [AADL Arch](#aadl-arch)
  * [Metrics](#metrics)
    * [AADL Metrics](#aadl-metrics)

## Diagrams
### AADL Arch
![AADL Arch](aadl/diagrams/arch.svg)

## Metrics
### AADL Metrics
| | |
|--|--|
|Threads|4|
|Ports|4|
|Connections|3|



## Installation


1. Install [Docker Desktop](https://www.docker.com/products/docker-desktop/)

1. Clone this repo and cd into it

   ```
   git clone https://github.com/loonwerks/INSPECTA-models.git
   cd INSPECTA-models
   ```

1. *OPTIONAL*

    If you want to rerun codegen then you will need to install Sireum
    and OSATE.  You can do this inside or outside of the container that you'll pull in the next section (the latter is probably preferable as you could then use Sireum outside of the container).

    Copy/paste the following to install Sireum
    ```
    git clone https://github.com/sireum/kekinian.git
    ```

    This installs/builds Sireum from source rather than via a binary distribution (which is probably the prefered method for PROVERS).  

    Now set ``SIREUM_HOME`` to point to where you cloned kekinian and add ``$SIREUM_HOME/bin`` to your path.  E.g. for bash

    ```
    echo "export SIREUM_HOME=$(pwd)/kekinian" >> $HOME/.bashrc
    echo "export PATH=\$SIREUM_HOME/bin:\$PATH" >> $HOME/.bashrc
    source $HOME/.bashrc
    ```

    To update Sireum in the future do the following
    ```
    cd $SIREUM_HOME
    git pull --rec
    bin/build.cmd
    ```

    Run the following to install IVE and CodeIVE which provide IDE support for Slang and SysMLv2 respectively.
    ```
    sireum setup ive
    sireum setup vscode
    ```

    Run the following to install OSATE and the Sireum plugins which provides IDE and codegen support for AADL. This will install OSATE into your current directory (or wherever as indicated via the ``-o`` option).  For Windows/Linux 
    ```
    sireum hamr phantom -u -v -o $(pwd)/osate
    ```

    or for Mac copy/paste
    ```
    sireum hamr phantom -u -v -o $(pwd)/osate.app
    ```

    Now set ``OSATE_HOME`` to point to where you installed Osate

    ```
    echo "export OSATE_HOME=$(pwd)/osate" >> $HOME/.bashrc
    source $HOME/.bashrc
    ```

## Codegen

1. *OPTIONAL* Rerun codegen targetting Microkit
   
    Launch the Slash script [micro-examples/microkit/port_queues/event_data_struct/aadl/bin/run-hamr.cmd](aadl/bin/run-hamr.cmd) from the command line.  

   ```
   micro-examples/microkit/port_queues/event_data_struct/aadl/bin/run-hamr.cmd
   ```

1. Build and simulate the seL4 Microkit image

    Run the following from this repository's root directory.  The docker image ``jasonbelt/microkit_domain_scheduling`` contains customized versions of Microkit and seL4 that support domain scheduling. They were built off the following pull requests

   - [microkit #175](https://github.com/seL4/microkit/pull/175)
   - [seL4 #1308](https://github.com/seL4/seL4/pull/1308)

   ```
    docker run -it --rm -v $(pwd):/home/microkit/inspecta-models jasonbelt/microkit_domain_scheduling \
      bash -ci "cd \$HOME/inspecta-models/micro-examples/microkit/port_queues/event_data_struct/hamr/microkit \
                && make qemu"
    ```

    Type ``CTRL-a x`` to exit the QEMU simulation

    The producer is populating [this](aadl/event_data_port_queues.aadl#L19-L23) datatype via [this](hamr/microkit/components/producer_p_p_producer/src/producer_p_p_producer_user.c#L9-L22) implementation to the consumers so you should get output similar to

    ```
    Booting all finished, dropped to user space
    MON|INFO: Microkit Bootstrap
    MON|INFO: bootinfo untyped list matches expected list
    MON|INFO: Number of bootstrap invocations: 0x00000009
    MON|INFO: Number of system invocations:    0x00000103
    MON|INFO: completed bootstrap invocations
    consumer_p_s1_co: I'm sporadic
    MON|INFO: completed system invocations
    consumer_p_s2_co: I'm sporadic
    consumer_p_s5_co: I'm sporadic
    producer_p_p_pro: I'm periodic
    ---------------------------------------
    producer_p_p_pro: Sent 0 events.
    ---------------------------------------
    producer_p_p_pro: Sent 1 events.
    consumer_p_s1_co: received [0]
    consumer_p_s1_co: 1 events received
    consumer_p_s2_co: received [0]
    consumer_p_s2_co: 1 events received
    consumer_p_s5_co: received [0]
    consumer_p_s5_co: 1 events received
    ---------------------------------------
    producer_p_p_pro: Sent 2 events.
    consumer_p_s1_co: received [0, 1]
    consumer_p_s1_co: 1 events received
    consumer_p_s2_co: received [0]
    consumer_p_s2_co: received [0, 1]
    consumer_p_s2_co: 2 events received
    consumer_p_s5_co: received [0]
    consumer_p_s5_co: received [0, 1]
    consumer_p_s5_co: 2 events received
    ---------------------------------------
    producer_p_p_pro: Sent 3 events.
    consumer_p_s1_co: received [0, 1, 2]
    consumer_p_s1_co: 1 events received
    consumer_p_s2_co: received [0, 1]
    consumer_p_s2_co: received [0, 1, 2]
    consumer_p_s2_co: 2 events received
    consumer_p_s5_co: received [0]
    consumer_p_s5_co: received [0, 1]
    consumer_p_s5_co: received [0, 1, 2]
    consumer_p_s5_co: 3 events received
    ---------------------------------------
    producer_p_p_pro: Sent 4 events.
    consumer_p_s1_co: received [0, 1, 2, 3]
    consumer_p_s1_co: 1 events received
    consumer_p_s2_co: received [0, 1, 2]
    consumer_p_s2_co: received [0, 1, 2, 3]
    consumer_p_s2_co: 2 events received
    consumer_p_s5_co: received [0]
    consumer_p_s5_co: received [0, 1]
    consumer_p_s5_co: received [0, 1, 2]
    consumer_p_s5_co: received [0, 1, 2, 3]
    consumer_p_s5_co: 4 events received
    ---------------------------------------
    producer_p_p_pro: Sent 5 events.
    consumer_p_s1_co: received [0, 1, 2, 3, 4]
    consumer_p_s1_co: 1 events received
    consumer_p_s2_co: received [0, 1, 2, 3]
    consumer_p_s2_co: received [0, 1, 2, 3, 4]
    consumer_p_s2_co: 2 events received
    consumer_p_s5_co: received [0]
    consumer_p_s5_co: received [0, 1]
    consumer_p_s5_co: received [0, 1, 2]
    consumer_p_s5_co: received [0, 1, 2, 3]
    consumer_p_s5_co: received [0, 1, 2, 3, 4]
    consumer_p_s5_co: 5 events received
    ---------------------------------------
    producer_p_p_pro: Sent 6 events.
    consumer_p_s1_co: received [0, 1, 2, 3, 4, 5]
    consumer_p_s1_co: 1 events received
    consumer_p_s2_co: received [0, 1, 2, 3, 4]
    consumer_p_s2_co: received [0, 1, 2, 3, 4, 5]
    consumer_p_s2_co: 2 events received
    consumer_p_s5_co: received [0, 1]
    consumer_p_s5_co: received [0, 1, 2]
    consumer_p_s5_co: received [0, 1, 2, 3]
    consumer_p_s5_co: received [0, 1, 2, 3, 4]
    consumer_p_s5_co: received [0, 1, 2, 3, 4, 5]
    consumer_p_s5_co: 5 events received
    ---------------------------------------
    producer_p_p_pro: Sent 0 events.
    ---------------------------------------
    ```