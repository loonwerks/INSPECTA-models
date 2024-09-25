# Codegen

1. Install [Docker Desktop](https://www.docker.com/products/docker-desktop/)

1. Clone this repo and cd into the ``open-platform-models/isolate-ethernet-simple`` directory (i.e. the directory containing this readme)

   ```
   git clone https://github.com/loonwerks/INSPECTA-models.git
   cd INSPECTA-models/open-platform-models/isolate-ethernet-simple/
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

1. Download and run the CAmkES docker container, mounting the ``open-platform-models/isolate-ethernet-simple`` directory into

   ```
   docker run -it -w /root -v $(pwd):/root/isolate-ethernet-simple jasonbelt/microkit_domain_scheduling
   ```

   This container includes customized versions of Microkit and seL4 that support domain scheduling.  They were built off the following pull requests

   - [microkit #175](https://github.com/seL4/microkit/pull/175)
   - [seL4 #1308](https://github.com/seL4/seL4/pull/1308)

1. *OPTIONAL* Rerun codegen
   
    Launch the Slash script [bin/run-hamr.cmd](bin/run-hamr.cmd) from the command line.  This runs codegen on [ZCU102.impl](platform.aadl#L24) via OSATE and targets the Microkit platform.

   ```
   bin/run-hamr.cmd
   ```

   Note the script deletes [HAMR.aadl](HAMR.aadl) and then restores it after it's finished.  This file conflicts with the version the HAMR OSATE plugin contributes which causes OSATE to not do the correct unit conversions [here](SW.aadl#L14).  The same would have to be done if you launch HAMR from inside the OSATE IDE (assuming that you have the HAMR OSATE plugins installed).

   If the above issue was resolved then the value of the model property [here](SW.aadl#L14) should equal the value of the codegen artficact [here](microkit/include/types.h#L7).

1. Build and simulate the image

    Inside the container do the following

    ```
    export MICROKIT_BOARD=qemu_virt_aarch64
    export MICROKIT_SDK=/root/microkit/release/microkit-sdk-1.4.1-dev.14+cf88629
    cd isolate-ethernet-simple/microkit
    make qemu
    ```

    You should get output similar to

    ```
    ...
    LDR|INFO: jumping to kernel
    Bootstrapping kernel
    Warning: Could not infer GIC interrupt target ID, assuming 0.
    available phys memory regions: 1
    [60000000..c0000000]
    reserved virt address space regions: 3
    [8060000000..8060348000]
    [8060348000..8060362000]
    [8060362000..806036a000]
    Booting all finished, dropped to user space
    MON|INFO: Microkit Bootstrap
    MON|INFO: bootinfo untyped list matches expected list
    MON|INFO: Number of bootstrap invocations: 0x00000009
    MON|INFO: Number of system invocations:    0x0000009e
    MON|INFO: completed bootstrap invocations
    MON|INFO: completed system invocations
    seL4_ArduPilot_A: Init
    seL4_Firewall_Fi: Init
    seL4_LowLevelEth: Init
    seL4_ArduPilot_A: Sent 1
    seL4_Firewall_Fi: Blocked 1
    seL4_LowLevelEth: Received: 0
    seL4_ArduPilot_A: Sent 2
    seL4_Firewall_Fi: Allowed 2
    seL4_LowLevelEth: Received: 2
    seL4_ArduPilot_A: Sent 3
    seL4_Firewall_Fi: Blocked 3
    seL4_LowLevelEth: Received: 2
    seL4_ArduPilot_A: Sent 4
    seL4_Firewall_Fi: Allowed 4
    seL4_LowLevelEth: Received: 4
    ```

## Relevant Microkit Artifacts

  - System Description - [microkit.system](microkit/microkit.system)

  - ArduPilot
      - [Infastructure](microkit/components//seL4_ArduPilot_ArduPilot/src/seL4_ArduPilot_ArduPilot.c)
      - [User supplied behavior code](microkit/components/seL4_ArduPilot_ArduPilot/src/seL4_ArduPilot_ArduPilot_user.c)

   - Firewall
      - [Infrastructure](microkit/components/seL4_Firewall_Firewall/src/seL4_Firewall_Firewall.c)
      - [User supplied behavior code](microkit/components/seL4_Firewall_Firewall/src/seL4_Firewall_Firewall_user.c)

  - LowLevelEthernetDriver
      - [Infrastructure](microkit/components/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/src/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver.c)
      - [User supplied behavior code](microkit/components/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/src/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_user.c)
