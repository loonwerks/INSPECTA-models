# Isolate-Ethernet-Simple

![aarch](diagrams/arch.png)

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
   
    Launch the Slash script [bin/run-hamr.cmd](bin/run-hamr.cmd) from the command line.  This runs codegen on [ZCU102.impl](platform.aadl#L24) via OSATE and targets the Microkit platform.

   ```
   open-platform-models/isolate-ethernet-simple/aadl/bin/run-hamr.cmd
   ```

   Note the script deletes [HAMR.aadl](HAMR.aadl) and then restores it after it's finished.  This file conflicts with the version the HAMR OSATE plugin contributes which causes OSATE to not do the correct unit conversions [here](SW.aadl#L14).  The same would have to be done if you launch HAMR from inside the OSATE IDE (assuming that you have the HAMR OSATE plugins installed).

   If the above issue was resolved then the value of the model property [here](SW.aadl#L14) should equal the value of the codegen artficact [here](microkit/include/types.h#L7).

1. Build and simulate the seL4 Microkit image

    Run the following from this repository's root directory.  The docker image ``jasonbelt/microkit_domain_scheduling`` contains customized versions of Microkit and seL4 that support domain scheduling. They were built off the following pull requests

   - [microkit #175](https://github.com/seL4/microkit/pull/175)
   - [seL4 #1308](https://github.com/seL4/seL4/pull/1308)

   ```
    docker run -it --rm -v $(pwd):/home/microkit/inspecta-models jasonbelt/microkit_domain_scheduling \
        bash -ci "cd \$HOME/inspecta-models/open-platform-models/isolate-ethernet-simple/microkit \
                  && make qemu"
    ```

    You should get output similar to

    ```
    Booting all finished, dropped to user space
    MON|INFO: Microkit Bootstrap
    MON|INFO: bootinfo untyped list matches expected list
    MON|INFO: Number of bootstrap invocations: 0x00000009
    MON|INFO: Number of system invocations:    0x000000cf
    MON|INFO: completed bootstrap invocations
    MON|INFO: cseL4_ArduPilot_A: Init
    ompleted system invocations
    seL4_Firewall_Fi: Init
    seL4_LowLevelEth: Init
    -------seL4_ArduPilot_A------
    TX Sent 1
    RX Received: 0
    -------seL4_Firewall_Fi------
    TX Blocked 1
    RX Allowed 0
    -------seL4_LowLevelEth------
    TX Received: 0
    RX Sent 99
    -------seL4_ArduPilot_A------
    TX Sent 2
    RX Received: 0
    -------seL4_Firewall_Fi------
    TX Allowed 2
    RX Blocked 99
    -------seL4_LowLevelEth------
    TX Received: 2
    RX Sent 98
    -------seL4_ArduPilot_A------
    TX Sent 3
    RX Received: 0
    -------seL4_Firewall_Fi------
    TX Blocked 3
    RX Allowed 98
    -------seL4_LowLevelEth------
    TX Received: 2
    RX Sent 97
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

## Microkit Architecture

![arch](microkit/microkit.dot.png)