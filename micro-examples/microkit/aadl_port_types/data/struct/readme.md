# AADL Data Ports

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
|Threads|3|
|Ports|3|
|Connections|2|



## Codegen

1. Install [Docker Desktop](https://www.docker.com/products/docker-desktop/)

1. Clone this repo and cd into it ``INSPECTA-models``

   ```
   git clone https://github.com/loonwerks/INSPECTA-models.git
   cd INSPECTA-models
   ```

1. *OPTIONAL*

    If you want to rerun codegen then you will need to install Sireum
    and OSATE.  You can do this inside or outside of the container that you'll install in the next step (the latter is probably preferable as you could then use Sireum outside of the container).

    Copy/paste the following to install Sireum
    ```
    git clone https://github.com/sireum/kekinian.git
    kekinian/bin/build.cmd
    ```

    This installs/build Sireum from source rather than via a binary distribution (which is probably the prefered method for PROVERS).  

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


    Now install OSATE and the Sireum OSATE plugins into your current directory (or wherever as indicated via the ``-o`` option).  For Windows/Linux 

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

1. Download and run the CAmkES docker container, mounting the ``INSPECTA-models`` directory into it

   ```
   docker run -it -w /root -v $(pwd):/root/INSPECTA-models jasonbelt/microkit_domain_scheduling
   ```

   This container includes customized versions of Microkit and seL4 that support domain scheduling.  They were built off the following pull requests

   - [microkit #175](https://github.com/seL4/microkit/pull/175)
   - [seL4 #1308](https://github.com/seL4/seL4/pull/1308)

1. *OPTIONAL* Rerun codegen
   
    Launch the Slash script [micro-examples/microkit/aadl_port_types/data/aadl/bin/run-hamr.cmd](aadl/bin/run-hamr.cmd) from the command line.  

   ```
   micro-examples/microkit/aadl_port_types/data/aadl/bin/run-hamr.cmd
   ```

1. Build and simulate the image

    Inside the container do the following

    ```
    export MICROKIT_BOARD=qemu_virt_aarch64
    export MICROKIT_SDK=/root/microkit/release/microkit-sdk-1.4.1-dev.14+cf88629
    cd $HOME/INSPECTA-models/micro-examples/microkit/aadl_port_types/data/hamr/microkit
    make qemu
    ```

    Type ``CTRL-a x`` to exit the QEMU simulation
    
    The producer is populating [this](aadl/data_1_prod_2_cons.aadl#L25-L29) datatype via [this](hamr/microkit/components/producer_p_p_producer/src/producer_p_p_producer_user.c#L14-L28) implementation to the consumers so you should get output similar to

    ```
    Booting all finished, dropped to user space
    MON|INFO: Microkit Bootstrap
    MON|INFO: bootinfo untyped list matches expected list
    MON|INFO: Number of bootstrap invocations: 0x00000009
    MON|INFO: Number of system invocations:    0x000000c9
    MON|INFO: completed bootstrap invocations
    MON|INFO: completed system invocations
    consumer_p_p_con: I'm periodic
    consumer_p_s_con: I'm sporadic so you'll never hear from me again :(
    producer_p_p_pro: I'm periodic
    ---------
    producer_p_p_pro: put 0 elements into the struct's array
    consumer_p_p_con: retrieved [] which is fresh
    ---------
    producer_p_p_pro: didn't put anything
    consumer_p_p_con: retrieved [] which is stale
    ---------
    producer_p_p_pro: put 2 elements into the struct's array
    consumer_p_p_con: retrieved [0, 1] which is fresh
    ---------
    producer_p_p_pro: didn't put anything
    consumer_p_p_con: retrieved [0, 1] which is stale
    ---------
    producer_p_p_pro: put 4 elements into the struct's array
    consumer_p_p_con: retrieved [0, 1, 2, 3] which is fresh
    ---------
    producer_p_p_pro: didn't put anything
    consumer_p_p_con: retrieved [0, 1, 2, 3] which is stale
    ---------
    producer_p_p_pro: put 6 elements into the struct's array
    consumer_p_p_con: retrieved [0, 1, 2, 3, 4, 5] which is fresh
    ---------
    producer_p_p_pro: didn't put anything
    consumer_p_p_con: retrieved [0, 1, 2, 3, 4, 5] which is stale
    ---------
    producer_p_p_pro: put 8 elements into the struct's array
    consumer_p_p_con: retrieved [0, 1, 2, 3, 4, 5, 6, 7] which is fresh
    ---------
    producer_p_p_pro: didn't put anything
    consumer_p_p_con: retrieved [0, 1, 2, 3, 4, 5, 6, 7] which is stale
    ---------
    producer_p_p_pro: put 0 elements into the struct's array
    consumer_p_p_con: retrieved [] which is fresh
    ```