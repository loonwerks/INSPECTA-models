# platform::ZCU102.Impl

## AADL Architecture
![arch.png](../../aadl/diagrams/arch.png)
|System: [platform::ZCU102.Impl]()|
|:--|

|Thread: SW::ArduPilot.Impl |
|:--|
|Type: [ArduPilot](../../aadl/SW.aadl#L192)<br>Implementation: [ArduPilot.Impl](../../aadl/SW.aadl#L198)|
|Periodic : 100 ms|

|Thread: SW::Firewall.Impl |
|:--|
|Type: [Firewall](../../aadl/SW.aadl#L112)<br>Implementation: [Firewall.Impl](../../aadl/SW.aadl#L162)<br>GUMBO: [Subclause](../../aadl/SW.aadl#L120)|
|Periodic : 100 ms|

|Thread: SW::LowLevelEthernetDriver.Impl |
|:--|
|Type: [LowLevelEthernetDriver](../../aadl/SW.aadl#L78)<br>Implementation: [LowLevelEthernetDriver.Impl](../../aadl/SW.aadl#L85)|
|Periodic : 100 ms|


## Rust Code


### Behavior Code
#### ArduPilot: SW::ArduPilot.Impl

 - **Entry Points**


    Initialize: [Rust](crates/seL4_ArduPilot_ArduPilot/src/component/seL4_ArduPilot_ArduPilot_app.rs#L22)

    TimeTriggered: [Rust](crates/seL4_ArduPilot_ArduPilot/src/component/seL4_ArduPilot_ArduPilot_app.rs#L30)


- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/SW.aadl#L195'>EthernetFramesRx</a></td>
        <td>In</td><td>Event Data</td>
        <td>SW::StructuredEthernetMessage.i</td><td><a title='Memory Map: Lines 24-28' href='microkit.system#L24'>Memory Map</a> → <a title='C Shared Memory Variable: Line 10' href='components/seL4_ArduPilot_ArduPilot/src/seL4_ArduPilot_ArduPilot.c#L10'>C var_addr</a> → <a title='C Interface: Lines 29-32' href='components/seL4_ArduPilot_ArduPilot/src/seL4_ArduPilot_ArduPilot.c#L29'>C Interface</a> → <a title='C Extern: Line 14' href='crates/seL4_ArduPilot_ArduPilot/src/bridge/extern_c_api.rs#L14'>C Extern</a> → <a title='Rust/C Interface: Lines 18-28' href='crates/seL4_ArduPilot_ArduPilot/src/bridge/extern_c_api.rs#L18'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 22-29' href='crates/seL4_ArduPilot_ArduPilot/src/bridge/seL4_ArduPilot_ArduPilot_api.rs#L22'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 55-62' href='crates/seL4_ArduPilot_ArduPilot/src/bridge/seL4_ArduPilot_ArduPilot_api.rs#L55'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/SW.aadl#L196'>EthernetFramesTx</a></td>
        <td>Out</td><td>Event Data</td>
        <td>SW::StructuredEthernetMessage.i</td><td><a title='Rust/Verus API: Lines 42-51' href='crates/seL4_ArduPilot_ArduPilot/src/bridge/seL4_ArduPilot_ArduPilot_api.rs#L42'>Rust/Verus API</a> → <a title='Unverified Rust Interface: Lines 12-17' href='crates/seL4_ArduPilot_ArduPilot/src/bridge/seL4_ArduPilot_ArduPilot_api.rs#L12'>Unverified Rust Interface</a> → <a title='Rust/C Interface: Lines 30-35' href='crates/seL4_ArduPilot_ArduPilot/src/bridge/extern_c_api.rs#L30'>Rust/C Interface</a> → <a title='C Extern: Line 15' href='crates/seL4_ArduPilot_ArduPilot/src/bridge/extern_c_api.rs#L15'>C Extern</a> → <a title='C Interface: Lines 15-19' href='components/seL4_ArduPilot_ArduPilot/src/seL4_ArduPilot_ArduPilot.c#L15'>C Interface</a> → <a title='C Shared Memory Variable: Line 9' href='components/seL4_ArduPilot_ArduPilot/src/seL4_ArduPilot_ArduPilot.c#L9'>C var_addr</a> → <a title='Memory Map: Lines 19-23' href='microkit.system#L19'>Memory Map</a></td></tr>
    </table>


#### Firewall: SW::Firewall.Impl

 - **Entry Points**


    Initialize: [Rust](crates/seL4_Firewall_Firewall/src/component/seL4_Firewall_Firewall_app.rs#L19)

    TimeTriggered: [Rust](crates/seL4_Firewall_Firewall/src/component/seL4_Firewall_Firewall_app.rs#L26)


- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/SW.aadl#L115'>EthernetFramesRxIn</a></td>
        <td>In</td><td>Event Data</td>
        <td>SW::StructuredEthernetMessage.i</td><td><a title='Memory Map: Lines 57-61' href='microkit.system#L57'>Memory Map</a> → <a title='C Shared Memory Variable: Line 13' href='components/seL4_Firewall_Firewall/src/seL4_Firewall_Firewall.c#L13'>C var_addr</a> → <a title='C Interface: Lines 51-54' href='components/seL4_Firewall_Firewall/src/seL4_Firewall_Firewall.c#L51'>C Interface</a> → <a title='C Extern: Line 14' href='crates/seL4_Firewall_Firewall/src/bridge/extern_c_api.rs#L14'>C Extern</a> → <a title='Rust/C Interface: Lines 20-30' href='crates/seL4_Firewall_Firewall/src/bridge/extern_c_api.rs#L20'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 30-37' href='crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_api.rs#L30'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 89-98' href='crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_api.rs#L89'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/SW.aadl#L116'>EthernetFramesTxIn</a></td>
        <td>In</td><td>Event Data</td>
        <td>SW::StructuredEthernetMessage.i</td><td><a title='Memory Map: Lines 42-46' href='microkit.system#L42'>Memory Map</a> → <a title='C Shared Memory Variable: Line 9' href='components/seL4_Firewall_Firewall/src/seL4_Firewall_Firewall.c#L9'>C var_addr</a> → <a title='C Interface: Lines 26-29' href='components/seL4_Firewall_Firewall/src/seL4_Firewall_Firewall.c#L26'>C Interface</a> → <a title='C Extern: Line 15' href='crates/seL4_Firewall_Firewall/src/bridge/extern_c_api.rs#L15'>C Extern</a> → <a title='Rust/C Interface: Lines 32-42' href='crates/seL4_Firewall_Firewall/src/bridge/extern_c_api.rs#L32'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 40-47' href='crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_api.rs#L40'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 99-108' href='crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_api.rs#L99'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/SW.aadl#L117'>EthernetFramesRxOut</a></td>
        <td>Out</td><td>Event Data</td>
        <td>SW::StructuredEthernetMessage.i</td><td><a title='Rust/Verus API: Lines 62-73' href='crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_api.rs#L62'>Rust/Verus API</a> → <a title='Unverified Rust Interface: Lines 12-17' href='crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_api.rs#L12'>Unverified Rust Interface</a> → <a title='Rust/C Interface: Lines 44-49' href='crates/seL4_Firewall_Firewall/src/bridge/extern_c_api.rs#L44'>Rust/C Interface</a> → <a title='C Extern: Line 16' href='crates/seL4_Firewall_Firewall/src/bridge/extern_c_api.rs#L16'>C Extern</a> → <a title='C Interface: Lines 31-35' href='components/seL4_Firewall_Firewall/src/seL4_Firewall_Firewall.c#L31'>C Interface</a> → <a title='C Shared Memory Variable: Line 11' href='components/seL4_Firewall_Firewall/src/seL4_Firewall_Firewall.c#L11'>C var_addr</a> → <a title='Memory Map: Lines 47-51' href='microkit.system#L47'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/SW.aadl#L118'>EthernetFramesTxOut</a></td>
        <td>Out</td><td>Event Data</td>
        <td>SW::StructuredEthernetMessage.i</td><td><a title='Rust/Verus API: Lines 74-85' href='crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_api.rs#L74'>Rust/Verus API</a> → <a title='Unverified Rust Interface: Lines 20-25' href='crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_api.rs#L20'>Unverified Rust Interface</a> → <a title='Rust/C Interface: Lines 51-56' href='crates/seL4_Firewall_Firewall/src/bridge/extern_c_api.rs#L51'>Rust/C Interface</a> → <a title='C Extern: Line 17' href='crates/seL4_Firewall_Firewall/src/bridge/extern_c_api.rs#L17'>C Extern</a> → <a title='C Interface: Lines 37-41' href='components/seL4_Firewall_Firewall/src/seL4_Firewall_Firewall.c#L37'>C Interface</a> → <a title='C Shared Memory Variable: Line 12' href='components/seL4_Firewall_Firewall/src/seL4_Firewall_Firewall.c#L12'>C var_addr</a> → <a title='Memory Map: Lines 52-56' href='microkit.system#L52'>Memory Map</a></td></tr>
    </table>
- **GUMBO**

    <table>
    <tr><th colspan=4>Compute</th></tr>
    <tr><td>assume onlyOneInEvent</td>
    <td><a href=../../aadl/SW.aadl#L122>GUMBO</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/component/seL4_Firewall_Firewall_app.rs#L35>Verus</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_GUMBOX.rs#L24>GUMBOX</a></td>
    </tr>
    <tr><td>guarantee RC_INSPECTA_00_HLR_2</td>
    <td><a href=../../aadl/SW.aadl#L126>GUMBO</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/component/seL4_Firewall_Firewall_app.rs#L42>Verus</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_GUMBOX.rs#L68>GUMBOX</a></td>
    </tr>
    <tr><td>guarantee RC_INSPECTA_00_HLR_4</td>
    <td><a href=../../aadl/SW.aadl#L131>GUMBO</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/component/seL4_Firewall_Firewall_app.rs#L47>Verus</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_GUMBOX.rs#L87>GUMBOX</a></td>
    </tr>
    <tr><td>guarantee RC_INSPECTA_00_HLR_5</td>
    <td><a href=../../aadl/SW.aadl#L140>GUMBO</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/component/seL4_Firewall_Firewall_app.rs#L56>Verus</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_GUMBOX.rs#L109>GUMBOX</a></td>
    </tr>
    <tr><td>guarantee RC_INSPECTA_00_HLR_6</td>
    <td><a href=../../aadl/SW.aadl#L147>GUMBO</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/component/seL4_Firewall_Firewall_app.rs#L64>Verus</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_GUMBOX.rs#L130>GUMBOX</a></td>
    </tr>
    <tr><td>guarantee RC_INSPECTA_00_HLR_7</td>
    <td><a href=../../aadl/SW.aadl#L154>GUMBO</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/component/seL4_Firewall_Firewall_app.rs#L73>Verus</a></td>
    <td><a href=crates/seL4_Firewall_Firewall/src/bridge/seL4_Firewall_Firewall_GUMBOX.rs#L152>GUMBOX</a></td>
    </tr></table>


#### LowLevelEthernetDriver: SW::LowLevelEthernetDriver.Impl

 - **Entry Points**


    Initialize: [Rust](crates/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/src/component/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_app.rs#L22)

    TimeTriggered: [Rust](crates/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/src/component/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_app.rs#L30)


- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/SW.aadl#L83'>EthernetFramesTx</a></td>
        <td>In</td><td>Event Data</td>
        <td>SW::StructuredEthernetMessage.i</td><td><a title='Memory Map: Lines 75-79' href='microkit.system#L75'>Memory Map</a> → <a title='C Shared Memory Variable: Line 9' href='components/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/src/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver.c#L9'>C var_addr</a> → <a title='C Interface: Lines 23-26' href='components/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/src/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver.c#L23'>C Interface</a> → <a title='C Extern: Line 14' href='crates/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/src/bridge/extern_c_api.rs#L14'>C Extern</a> → <a title='Rust/C Interface: Lines 18-28' href='crates/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/src/bridge/extern_c_api.rs#L18'>Rust/C Interface</a> → <a title='Unverified Rust Interface: Lines 22-29' href='crates/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/src/bridge/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_api.rs#L22'>Unverified Rust Interface</a> → <a title='Rust/Verus API: Lines 55-62' href='crates/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/src/bridge/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_api.rs#L55'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/SW.aadl#L82'>EthernetFramesRx</a></td>
        <td>Out</td><td>Event Data</td>
        <td>SW::StructuredEthernetMessage.i</td><td><a title='Rust/Verus API: Lines 42-51' href='crates/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/src/bridge/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_api.rs#L42'>Rust/Verus API</a> → <a title='Unverified Rust Interface: Lines 12-17' href='crates/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/src/bridge/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_api.rs#L12'>Unverified Rust Interface</a> → <a title='Rust/C Interface: Lines 30-35' href='crates/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/src/bridge/extern_c_api.rs#L30'>Rust/C Interface</a> → <a title='C Extern: Line 15' href='crates/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/src/bridge/extern_c_api.rs#L15'>C Extern</a> → <a title='C Interface: Lines 28-32' href='components/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/src/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver.c#L28'>C Interface</a> → <a title='C Shared Memory Variable: Line 11' href='components/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver/src/seL4_LowLevelEthernetDriver_LowLevelEthernetDriver.c#L11'>C var_addr</a> → <a title='Memory Map: Lines 80-84' href='microkit.system#L80'>Memory Map</a></td></tr>
    </table>

