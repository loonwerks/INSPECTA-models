# platform::ZCU102.Impl

## AADL Architecture
![arch.png](../../aadl/diagrams/arch.png)
|System: [platform::ZCU102.Impl]()|
|:--|

|Thread: SW::ArduPilot.Impl |
|:--|
|Type: [ArduPilot](../../aadl/SW.aadl#L192-L197)<br>Implementation: [ArduPilot.Impl](../../aadl/SW.aadl#L198-L205)|
|Periodic : 100 ms|

|Thread: SW::Firewall.Impl |
|:--|
|Type: [Firewall](../../aadl/SW.aadl#L112-L161)<br>Implementation: [Firewall.Impl](../../aadl/SW.aadl#L162-L169)<br>GUMBO: [Subclause](../../aadl/SW.aadl#L120-L160)|
|Periodic : 100 ms|

|Thread: SW::LowLevelEthernetDriver.Impl |
|:--|
|Type: [LowLevelEthernetDriver](../../aadl/SW.aadl#L78-L84)<br>Implementation: [LowLevelEthernetDriver.Impl](../../aadl/SW.aadl#L85-L92)|
|Periodic : 100 ms|


## Rust Code

[Microkit System Description](microkit.system)

### Behavior Code
#### ArduPilot: SW::ArduPilot.Impl

 - **Entry Points**

    Initialize: [Rust](crates/seL4_ArduPilot_ArduPilot/src/component/seL4_ArduPilot_ArduPilot_app.rs#L22-L28)

    TimeTriggered: [Rust](crates/seL4_ArduPilot_ArduPilot/src/component/seL4_ArduPilot_ArduPilot_app.rs#L30-L36)


 - **APIs**

     <table>
     <tr><th colspan=3>Ports</th></tr>
     <tr><td>EthernetFramesRx</td>
     <td><a href=../../aadl/SW.aadl#L195-L195>Model</a></td>
     <td><a href=crates/seL4_ArduPilot_ArduPilot/src/bridge/seL4_ArduPilot_ArduPilot_api.rs#L55-L62>Rust API</a></td>
     <tr><td>EthernetFramesTx</td>
     <td><a href=../../aadl/SW.aadl#L196-L196>Model</a></td>
     <td><a href=crates/seL4_ArduPilot_ArduPilot/src/bridge/seL4_ArduPilot_ArduPilot_api.rs#L42-L51>Rust API</a></td></table>



#### Firewall: SW::Firewall.Impl

 - **Entry Points**

    Initialize: [Rust](crates/seL4_Firewall_Firewall/src/component/seL4_Firewall_Firewall_app.rs#L25-L31)

    TimeTriggered: [Rust](crates/seL4_Firewall_Firewall/src/component/seL4_Firewall_Firewall_app.rs#L33-L92)


 - **APIs**

     <table>
     <tr><th colspan=3>Ports</th></tr>
     <tr><td>EthernetFramesRxIn</td>
     <td><a href=../../aadl/SW.aadl#L115-L115>Model</a></td>
     <td><a href=crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_api.rs#L99-L108>Rust API</a></td>
     <tr><td>EthernetFramesTxIn</td>
     <td><a href=../../aadl/SW.aadl#L116-L116>Model</a></td>
     <td><a href=crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_api.rs#L89-L98>Rust API</a></td>
     <tr><td>EthernetFramesRxOut</td>
     <td><a href=../../aadl/SW.aadl#L117-L117>Model</a></td>
     <td><a href=crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_api.rs#L62-L73>Rust API</a></td>
     <tr><td>EthernetFramesTxOut</td>
     <td><a href=../../aadl/SW.aadl#L118-L118>Model</a></td>
     <td><a href=crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_api.rs#L74-L85>Rust API</a></td></table>

- **GUMBO**

    <table>
    <tr><th colspan=4>Compute</th></tr>
    <tr><td>assume onlyOneInEvent</td>
    <td><a href=../../aadl/SW.aadl#L122-L124>GUMBO</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/component/seL4_Firewall_Firewall_app.rs#L42-L44>Verus</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_GUMBOX.rs#L24-L30>GUMBOX</a></td>
    </tr>
    <tr><td>guarantee RC_INSPECTA_00_HLR_2</td>
    <td><a href=../../aadl/SW.aadl#L126-L129>GUMBO</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/component/seL4_Firewall_Firewall_app.rs#L49-L51>Verus</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_GUMBOX.rs#L68-L77>GUMBOX</a></td>
    </tr>
    <tr><td>guarantee RC_INSPECTA_00_HLR_4</td>
    <td><a href=../../aadl/SW.aadl#L131-L138>GUMBO</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/component/seL4_Firewall_Firewall_app.rs#L54-L56>Verus</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_GUMBOX.rs#L87-L99>GUMBOX</a></td>
    </tr>
    <tr><td>guarantee RC_INSPECTA_00_HLR_5</td>
    <td><a href=../../aadl/SW.aadl#L140-L145>GUMBO</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/component/seL4_Firewall_Firewall_app.rs#L63-L65>Verus</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_GUMBOX.rs#L109-L120>GUMBOX</a></td>
    </tr>
    <tr><td>guarantee RC_INSPECTA_00_HLR_6</td>
    <td><a href=../../aadl/SW.aadl#L147-L152>GUMBO</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/component/seL4_Firewall_Firewall_app.rs#L71-L73>Verus</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_GUMBOX.rs#L130-L142>GUMBOX</a></td>
    </tr>
    <tr><td>guarantee RC_INSPECTA_00_HLR_7</td>
    <td><a href=../../aadl/SW.aadl#L154-L159>GUMBO</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/component/seL4_Firewall_Firewall_app.rs#L80-L82>Verus</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_GUMBOX.rs#L152-L163>GUMBOX</a></td>
    </tr></table>


#### LowLevelEthernetDriver: SW::LowLevelEthernetDriver.Impl

 - **Entry Points**

    Initialize: [Rust](crates/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/src/component/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_app.rs#L22-L28)

    TimeTriggered: [Rust](crates/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/src/component/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_app.rs#L30-L36)


 - **APIs**

     <table>
     <tr><th colspan=3>Ports</th></tr>
     <tr><td>EthernetFramesTx</td>
     <td><a href=../../aadl/SW.aadl#L83-L83>Model</a></td>
     <td><a href=crates/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/src/bridge/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_api.rs#L55-L62>Rust API</a></td>
     <tr><td>EthernetFramesRx</td>
     <td><a href=../../aadl/SW.aadl#L82-L82>Model</a></td>
     <td><a href=crates/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/src/bridge/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_api.rs#L42-L51>Rust API</a></td></table>


