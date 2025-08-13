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


### Behavior Code
#### ArduPilot: SW::ArduPilot.Impl

 - **Entry Points**


    Initialize: [Rust](crates/seL4_ArduPilot_ArduPilot/src/component/seL4_ArduPilot_ArduPilot_app.rs#L22-L28)

    TimeTriggered: [Rust](crates/seL4_ArduPilot_ArduPilot/src/component/seL4_ArduPilot_ArduPilot_app.rs#L30-L36)


- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/SW.aadl#L195-L195'>EthernetFramesRx</a></td>
        <td>In</td><td>Event Data</td>
        <td>SW::StructuredEthernetMessage.i</td><td><a title='Memory Map' href='microkit.system#L22-L26'>Memory Map</a> -> <a title='C Extern' href='crates/seL4_ArduPilot_ArduPilot/src/bridge/extern_c_api.rs#L15-L15'>C Extern</a> -> <a title='Rust/C Interface' href='crates/seL4_ArduPilot_ArduPilot/src/bridge/extern_c_api.rs#L25-L35'>Rust/C Interface</a> -> <a title='Unverified Rust Interface' href='crates/seL4_ArduPilot_ArduPilot/src/bridge/seL4_ArduPilot_ArduPilot_api.rs#L22-L29'>Unverified Rust Interface</a> -> <a title='Rust/Verus API' href='crates/seL4_ArduPilot_ArduPilot/src/bridge/seL4_ArduPilot_ArduPilot_api.rs#L55-L62'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/SW.aadl#L196-L196'>EthernetFramesTx</a></td>
        <td>Out</td><td>Event Data</td>
        <td>SW::StructuredEthernetMessage.i</td><td><a title='Rust/Verus API' href='crates/seL4_ArduPilot_ArduPilot/src/bridge/seL4_ArduPilot_ArduPilot_api.rs#L42-L51'>Rust/Verus API</a> -> <a title='Unverified Rust Interface' href='crates/seL4_ArduPilot_ArduPilot/src/bridge/seL4_ArduPilot_ArduPilot_api.rs#L12-L17'>Unverified Rust Interface</a> -> <a title='Rust/C Interface' href='crates/seL4_ArduPilot_ArduPilot/src/bridge/extern_c_api.rs#L18-L23'>Rust/C Interface</a> -> <a title='C Extern' href='crates/seL4_ArduPilot_ArduPilot/src/bridge/extern_c_api.rs#L14-L14'>C Extern</a> -> <a title='Memory Map' href='microkit.system#L17-L21'>Memory Map</a></td></tr>
    </table>


#### Firewall: SW::Firewall.Impl

 - **Entry Points**


    Initialize: [Rust](crates/seL4_Firewall_Firewall/src/component/seL4_Firewall_Firewall_app.rs#L19-L24)

    TimeTriggered: [Rust](crates/seL4_Firewall_Firewall/src/component/seL4_Firewall_Firewall_app.rs#L26-L84)


- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/SW.aadl#L115-L115'>EthernetFramesRxIn</a></td>
        <td>In</td><td>Event Data</td>
        <td>SW::StructuredEthernetMessage.i</td><td><a title='Memory Map' href='microkit.system#L49-L53'>Memory Map</a> -> <a title='C Extern' href='crates/seL4_Firewall_Firewall/src/bridge/extern_c_api.rs#L17-L17'>C Extern</a> -> <a title='Rust/C Interface' href='crates/seL4_Firewall_Firewall/src/bridge/extern_c_api.rs#L46-L56'>Rust/C Interface</a> -> <a title='Unverified Rust Interface' href='crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_api.rs#L40-L47'>Unverified Rust Interface</a> -> <a title='Rust/Verus API' href='crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_api.rs#L99-L108'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/SW.aadl#L116-L116'>EthernetFramesTxIn</a></td>
        <td>In</td><td>Event Data</td>
        <td>SW::StructuredEthernetMessage.i</td><td><a title='Memory Map' href='microkit.system#L34-L38'>Memory Map</a> -> <a title='C Extern' href='crates/seL4_Firewall_Firewall/src/bridge/extern_c_api.rs#L14-L14'>C Extern</a> -> <a title='Rust/C Interface' href='crates/seL4_Firewall_Firewall/src/bridge/extern_c_api.rs#L20-L30'>Rust/C Interface</a> -> <a title='Unverified Rust Interface' href='crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_api.rs#L30-L37'>Unverified Rust Interface</a> -> <a title='Rust/Verus API' href='crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_api.rs#L89-L98'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/SW.aadl#L117-L117'>EthernetFramesRxOut</a></td>
        <td>Out</td><td>Event Data</td>
        <td>SW::StructuredEthernetMessage.i</td><td><a title='Rust/Verus API' href='crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_api.rs#L62-L73'>Rust/Verus API</a> -> <a title='Unverified Rust Interface' href='crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_api.rs#L12-L17'>Unverified Rust Interface</a> -> <a title='Rust/C Interface' href='crates/seL4_Firewall_Firewall/src/bridge/extern_c_api.rs#L32-L37'>Rust/C Interface</a> -> <a title='C Extern' href='crates/seL4_Firewall_Firewall/src/bridge/extern_c_api.rs#L15-L15'>C Extern</a> -> <a title='Memory Map' href='microkit.system#L39-L43'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/SW.aadl#L118-L118'>EthernetFramesTxOut</a></td>
        <td>Out</td><td>Event Data</td>
        <td>SW::StructuredEthernetMessage.i</td><td><a title='Rust/Verus API' href='crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_api.rs#L74-L85'>Rust/Verus API</a> -> <a title='Unverified Rust Interface' href='crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_api.rs#L20-L25'>Unverified Rust Interface</a> -> <a title='Rust/C Interface' href='crates/seL4_Firewall_Firewall/src/bridge/extern_c_api.rs#L39-L44'>Rust/C Interface</a> -> <a title='C Extern' href='crates/seL4_Firewall_Firewall/src/bridge/extern_c_api.rs#L16-L16'>C Extern</a> -> <a title='Memory Map' href='microkit.system#L44-L48'>Memory Map</a></td></tr>
    </table>
- **GUMBO**

    <table>
    <tr><th colspan=4>Compute</th></tr>
    <tr><td>assume onlyOneInEvent</td>
    <td><a href=../../aadl/SW.aadl#L122-L124>GUMBO</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/component/seL4_Firewall_Firewall_app.rs#L35-L38>Verus</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_GUMBOX.rs#L24-L30>GUMBOX</a></td>
    </tr>
    <tr><td>guarantee RC_INSPECTA_00_HLR_2</td>
    <td><a href=../../aadl/SW.aadl#L126-L129>GUMBO</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/component/seL4_Firewall_Firewall_app.rs#L42-L46>Verus</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_GUMBOX.rs#L68-L77>GUMBOX</a></td>
    </tr>
    <tr><td>guarantee RC_INSPECTA_00_HLR_4</td>
    <td><a href=../../aadl/SW.aadl#L131-L138>GUMBO</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/component/seL4_Firewall_Firewall_app.rs#L47-L55>Verus</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_GUMBOX.rs#L87-L99>GUMBOX</a></td>
    </tr>
    <tr><td>guarantee RC_INSPECTA_00_HLR_5</td>
    <td><a href=../../aadl/SW.aadl#L140-L145>GUMBO</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/component/seL4_Firewall_Firewall_app.rs#L56-L63>Verus</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_GUMBOX.rs#L109-L120>GUMBOX</a></td>
    </tr>
    <tr><td>guarantee RC_INSPECTA_00_HLR_6</td>
    <td><a href=../../aadl/SW.aadl#L147-L152>GUMBO</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/component/seL4_Firewall_Firewall_app.rs#L64-L72>Verus</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_GUMBOX.rs#L130-L142>GUMBOX</a></td>
    </tr>
    <tr><td>guarantee RC_INSPECTA_00_HLR_7</td>
    <td><a href=../../aadl/SW.aadl#L154-L159>GUMBO</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/component/seL4_Firewall_Firewall_app.rs#L73-L80>Verus</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_GUMBOX.rs#L152-L163>GUMBOX</a></td>
    </tr></table>


#### LowLevelEthernetDriver: SW::LowLevelEthernetDriver.Impl

 - **Entry Points**


    Initialize: [Rust](crates/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/src/component/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_app.rs#L22-L28)

    TimeTriggered: [Rust](crates/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/src/component/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_app.rs#L30-L36)


- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/SW.aadl#L83-L83'>EthernetFramesTx</a></td>
        <td>In</td><td>Event Data</td>
        <td>SW::StructuredEthernetMessage.i</td><td><a title='Memory Map' href='microkit.system#L61-L65'>Memory Map</a> -> <a title='C Extern' href='crates/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/src/bridge/extern_c_api.rs#L14-L14'>C Extern</a> -> <a title='Rust/C Interface' href='crates/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/src/bridge/extern_c_api.rs#L18-L28'>Rust/C Interface</a> -> <a title='Unverified Rust Interface' href='crates/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/src/bridge/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_api.rs#L22-L29'>Unverified Rust Interface</a> -> <a title='Rust/Verus API' href='crates/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/src/bridge/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_api.rs#L55-L62'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/SW.aadl#L82-L82'>EthernetFramesRx</a></td>
        <td>Out</td><td>Event Data</td>
        <td>SW::StructuredEthernetMessage.i</td><td><a title='Rust/Verus API' href='crates/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/src/bridge/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_api.rs#L42-L51'>Rust/Verus API</a> -> <a title='Unverified Rust Interface' href='crates/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/src/bridge/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_api.rs#L12-L17'>Unverified Rust Interface</a> -> <a title='Rust/C Interface' href='crates/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/src/bridge/extern_c_api.rs#L30-L35'>Rust/C Interface</a> -> <a title='C Extern' href='crates/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/src/bridge/extern_c_api.rs#L15-L15'>C Extern</a> -> <a title='Memory Map' href='microkit.system#L66-L70'>Memory Map</a></td></tr>
    </table>

