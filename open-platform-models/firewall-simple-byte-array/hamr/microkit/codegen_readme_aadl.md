# SW::seL4.Impl

## AADL Architecture
![arch.png](../../aadl/diagrams/arch.png)
|System: [SW::seL4.Impl]()|
|:--|

|Thread: SW::ArduPilot.Impl |
|:--|
|Type: [ArduPilot](../../aadl/SW.aadl#L277-L282)<br>Implementation: [ArduPilot.Impl](../../aadl/SW.aadl#L283-L290)|
|Periodic : 100 ms|

|Thread: SW::Firewall.Impl |
|:--|
|Type: [Firewall](../../aadl/SW.aadl#L63-L71)<br>Implementation: [Firewall.Impl](../../aadl/SW.aadl#L72-L253)<br>
GUMBO: [Subclause](../../aadl/SW.aadl#L79-L250)|
|Periodic : 100 ms|

|Thread: SW::LowLevelEthernetDriver.Impl |
|:--|
|Type: [LowLevelEthernetDriver](../../aadl/SW.aadl#L28-L34)<br>Implementation: [LowLevelEthernetDriver.Impl](../../aadl/SW.aadl#L35-L42)|
|Periodic : 100 ms|


## Rust Code


### Behavior Code
#### ArduPilot: SW::ArduPilot.Impl

 - **Entry Points**


    Initialize: [Rust](crates/ArduPilot_ArduPilot/src/component/ArduPilot_ArduPilot_app.rs#L22-L28)

    TimeTriggered: [Rust](crates/ArduPilot_ArduPilot/src/component/ArduPilot_ArduPilot_app.rs#L30-L36)


- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/SW.aadl#L280-L280'>EthernetFramesRx</a></td>
        <td>In</td><td>Event Data</td>
        <td>SW::RawEthernetMessage</td><td><a title='Memory Map' href='microkit.system#L24-L28'>Memory Map</a> -> <a title='C Extern' href='crates/ArduPilot_ArduPilot/src/bridge/extern_c_api.rs#L14-L14'>C Extern</a> -> <a title='Rust/C Interface' href='crates/ArduPilot_ArduPilot/src/bridge/extern_c_api.rs#L18-L28'>Rust/C Interface</a> -> <a title='Unverified Rust Interface' href='crates/ArduPilot_ArduPilot/src/bridge/ArduPilot_ArduPilot_api.rs#L22-L29'>Unverified Rust Interface</a> -> <a title='Rust/Verus API' href='crates/ArduPilot_ArduPilot/src/bridge/ArduPilot_ArduPilot_api.rs#L55-L62'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/SW.aadl#L281-L281'>EthernetFramesTx</a></td>
        <td>Out</td><td>Event Data</td>
        <td>SW::RawEthernetMessage</td><td><a title='Rust/Verus API' href='crates/ArduPilot_ArduPilot/src/bridge/ArduPilot_ArduPilot_api.rs#L42-L51'>Rust/Verus API</a> -> <a title='Unverified Rust Interface' href='crates/ArduPilot_ArduPilot/src/bridge/ArduPilot_ArduPilot_api.rs#L12-L17'>Unverified Rust Interface</a> -> <a title='Rust/C Interface' href='crates/ArduPilot_ArduPilot/src/bridge/extern_c_api.rs#L30-L35'>Rust/C Interface</a> -> <a title='C Extern' href='crates/ArduPilot_ArduPilot/src/bridge/extern_c_api.rs#L15-L15'>C Extern</a> -> <a title='Memory Map' href='microkit.system#L19-L23'>Memory Map</a></td></tr>
    </table>


#### Firewall: SW::Firewall.Impl

 - **Entry Points**


    Initialize: [Rust](crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L21-L26)

    TimeTriggered: [Rust](crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L28-L75)


- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/SW.aadl#L66-L66'>EthernetFramesRxIn</a></td>
        <td>In</td><td>Event Data</td>
        <td>SW::RawEthernetMessage</td><td><a title='Memory Map' href='microkit.system#L57-L61'>Memory Map</a> -> <a title='C Extern' href='crates/Firewall_Firewall/src/bridge/extern_c_api.rs#L14-L14'>C Extern</a> -> <a title='Rust/C Interface' href='crates/Firewall_Firewall/src/bridge/extern_c_api.rs#L20-L30'>Rust/C Interface</a> -> <a title='Unverified Rust Interface' href='crates/Firewall_Firewall/src/bridge/Firewall_Firewall_api.rs#L30-L37'>Unverified Rust Interface</a> -> <a title='Rust/Verus API' href='crates/Firewall_Firewall/src/bridge/Firewall_Firewall_api.rs#L89-L98'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/SW.aadl#L69-L69'>EthernetFramesTxIn</a></td>
        <td>In</td><td>Event Data</td>
        <td>SW::RawEthernetMessage</td><td><a title='Memory Map' href='microkit.system#L42-L46'>Memory Map</a> -> <a title='C Extern' href='crates/Firewall_Firewall/src/bridge/extern_c_api.rs#L15-L15'>C Extern</a> -> <a title='Rust/C Interface' href='crates/Firewall_Firewall/src/bridge/extern_c_api.rs#L32-L42'>Rust/C Interface</a> -> <a title='Unverified Rust Interface' href='crates/Firewall_Firewall/src/bridge/Firewall_Firewall_api.rs#L40-L47'>Unverified Rust Interface</a> -> <a title='Rust/Verus API' href='crates/Firewall_Firewall/src/bridge/Firewall_Firewall_api.rs#L99-L108'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/SW.aadl#L67-L67'>EthernetFramesRxOut</a></td>
        <td>Out</td><td>Event Data</td>
        <td>SW::RawEthernetMessage</td><td><a title='Rust/Verus API' href='crates/Firewall_Firewall/src/bridge/Firewall_Firewall_api.rs#L62-L73'>Rust/Verus API</a> -> <a title='Unverified Rust Interface' href='crates/Firewall_Firewall/src/bridge/Firewall_Firewall_api.rs#L12-L17'>Unverified Rust Interface</a> -> <a title='Rust/C Interface' href='crates/Firewall_Firewall/src/bridge/extern_c_api.rs#L44-L49'>Rust/C Interface</a> -> <a title='C Extern' href='crates/Firewall_Firewall/src/bridge/extern_c_api.rs#L16-L16'>C Extern</a> -> <a title='Memory Map' href='microkit.system#L47-L51'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/SW.aadl#L70-L70'>EthernetFramesTxOut</a></td>
        <td>Out</td><td>Event Data</td>
        <td>SW::RawEthernetMessage</td><td><a title='Rust/Verus API' href='crates/Firewall_Firewall/src/bridge/Firewall_Firewall_api.rs#L74-L85'>Rust/Verus API</a> -> <a title='Unverified Rust Interface' href='crates/Firewall_Firewall/src/bridge/Firewall_Firewall_api.rs#L20-L25'>Unverified Rust Interface</a> -> <a title='Rust/C Interface' href='crates/Firewall_Firewall/src/bridge/extern_c_api.rs#L51-L56'>Rust/C Interface</a> -> <a title='C Extern' href='crates/Firewall_Firewall/src/bridge/extern_c_api.rs#L17-L17'>C Extern</a> -> <a title='Memory Map' href='microkit.system#L52-L56'>Memory Map</a></td></tr>
    </table>
- **GUMBO**

    <table>
    <tr><th colspan=4>Compute</th></tr>
    <tr><td>guarantee rx</td>
    <td><a href=../../aadl/SW.aadl#L238-L242>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L40-L43>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L297-L313>GUMBOX</a></td>
    </tr>
    <tr><td>guarantee tx</td>
    <td><a href=../../aadl/SW.aadl#L244-L248>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L47-L50>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L321-L337>GUMBOX</a></td>
    </tr></table>
    <table>
    <tr><th colspan=4>GUMBO Methods</th></tr>
    <tr><td>TCP_ALLOWED_PORTS</td>
    <td><a href=../../aadl/SW.aadl#L89-L89>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L430-L433>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L17-L20>GUMBOX</a></td>
    </tr>
    <tr><td>UDP_ALLOWED_PORTS</td>
    <td><a href=../../aadl/SW.aadl#L91-L91>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L435-L438>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L22-L25>GUMBOX</a></td>
    </tr>
    <tr><td>two_bytes_to_u16</td>
    <td><a href=../../aadl/SW.aadl#L94-L95>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L440-L445>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L27-L32>GUMBOX</a></td>
    </tr>
    <tr><td>frame_is_wellformed_eth2</td>
    <td><a href=../../aadl/SW.aadl#L97-L100>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L447-L455>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L34-L42>GUMBOX</a></td>
    </tr>
    <tr><td>frame_has_ipv4</td>
    <td><a href=../../aadl/SW.aadl#L103-L106>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L457-L466>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L44-L54>GUMBOX</a></td>
    </tr>
    <tr><td>frame_has_ipv4_tcp</td>
    <td><a href=../../aadl/SW.aadl#L108-L111>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L468-L476>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L56-L65>GUMBOX</a></td>
    </tr>
    <tr><td>frame_has_ipv4_udp</td>
    <td><a href=../../aadl/SW.aadl#L113-L116>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L478-L486>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L67-L76>GUMBOX</a></td>
    </tr>
    <tr><td>frame_has_ipv4_tcp_on_allowed_port</td>
    <td><a href=../../aadl/SW.aadl#L118-L122>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L488-L493>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L78-L84>GUMBOX</a></td>
    </tr>
    <tr><td>frame_has_ipv4_tcp_on_allowed_port_quant</td>
    <td><a href=../../aadl/SW.aadl#L124-L126>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L495-L498>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L86-L89>GUMBOX</a></td>
    </tr>
    <tr><td>frame_has_ipv4_udp_on_allowed_port</td>
    <td><a href=../../aadl/SW.aadl#L128-L132>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L500-L505>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L91-L97>GUMBOX</a></td>
    </tr>
    <tr><td>frame_has_ipv4_udp_on_allowed_port_quant</td>
    <td><a href=../../aadl/SW.aadl#L134-L136>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L507-L510>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L99-L102>GUMBOX</a></td>
    </tr>
    <tr><td>frame_has_ipv6</td>
    <td><a href=../../aadl/SW.aadl#L138-L141>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L512-L521>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L104-L114>GUMBOX</a></td>
    </tr>
    <tr><td>frame_has_arp</td>
    <td><a href=../../aadl/SW.aadl#L143-L146>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L523-L532>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L116-L126>GUMBOX</a></td>
    </tr>
    <tr><td>hlr_1_1</td>
    <td><a href=../../aadl/SW.aadl#L153-L155>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L534-L543>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L128-L137>GUMBOX</a></td>
    </tr>
    <tr><td>hlr_1_2</td>
    <td><a href=../../aadl/SW.aadl#L157-L159>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L545-L554>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L139-L148>GUMBOX</a></td>
    </tr>
    <tr><td>hlr_1_3</td>
    <td><a href=../../aadl/SW.aadl#L161-L165>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L556-L566>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L150-L160>GUMBOX</a></td>
    </tr>
    <tr><td>hlr_1_4</td>
    <td><a href=../../aadl/SW.aadl#L167-L172>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L568-L579>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L162-L173>GUMBOX</a></td>
    </tr>
    <tr><td>hlr_1_5</td>
    <td><a href=../../aadl/SW.aadl#L174-L179>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L581-L592>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L175-L186>GUMBOX</a></td>
    </tr>
    <tr><td>hlr_1_6</td>
    <td><a href=../../aadl/SW.aadl#L181-L184>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L594-L603>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L188-L197>GUMBOX</a></td>
    </tr>
    <tr><td>hlr_1_7</td>
    <td><a href=../../aadl/SW.aadl#L186-L191>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L605-L616>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L199-L210>GUMBOX</a></td>
    </tr>
    <tr><td>hlr_1_8</td>
    <td><a href=../../aadl/SW.aadl#L193-L198>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L618-L629>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L212-L223>GUMBOX</a></td>
    </tr>
    <tr><td>should_allow_inbound_frame_rx</td>
    <td><a href=../../aadl/SW.aadl#L200-L208>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L631-L642>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L225-L236>GUMBOX</a></td>
    </tr>
    <tr><td>hlr_2_1</td>
    <td><a href=../../aadl/SW.aadl#L214-L216>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L644-L653>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L238-L247>GUMBOX</a></td>
    </tr>
    <tr><td>hlr_2_2</td>
    <td><a href=../../aadl/SW.aadl#L218-L220>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L655-L664>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L249-L258>GUMBOX</a></td>
    </tr>
    <tr><td>hlr_2_3</td>
    <td><a href=../../aadl/SW.aadl#L222-L224>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L666-L675>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L260-L269>GUMBOX</a></td>
    </tr>
    <tr><td>hlr_2_4</td>
    <td><a href=../../aadl/SW.aadl#L226-L228>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L677-L686>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L271-L280>GUMBOX</a></td>
    </tr>
    <tr><td>should_allow_outbound_frame_tx</td>
    <td><a href=../../aadl/SW.aadl#L230-L234>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L688-L695>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L282-L289>GUMBOX</a></td>
    </tr></table>


#### LowLevelEthernetDriver: SW::LowLevelEthernetDriver.Impl

 - **Entry Points**


    Initialize: [Rust](crates/LowLevelEthernetDriver_LowLevelEthernetDriver/src/component/LowLevelEthernetDriver_LowLevelEthernetDriver_app.rs#L22-L28)

    TimeTriggered: [Rust](crates/LowLevelEthernetDriver_LowLevelEthernetDriver/src/component/LowLevelEthernetDriver_LowLevelEthernetDriver_app.rs#L30-L36)


- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/SW.aadl#L33-L33'>EthernetFramesTx</a></td>
        <td>In</td><td>Event Data</td>
        <td>SW::RawEthernetMessage</td><td><a title='Memory Map' href='microkit.system#L75-L79'>Memory Map</a> -> <a title='C Extern' href='crates/LowLevelEthernetDriver_LowLevelEthernetDriver/src/bridge/extern_c_api.rs#L14-L14'>C Extern</a> -> <a title='Rust/C Interface' href='crates/LowLevelEthernetDriver_LowLevelEthernetDriver/src/bridge/extern_c_api.rs#L18-L28'>Rust/C Interface</a> -> <a title='Unverified Rust Interface' href='crates/LowLevelEthernetDriver_LowLevelEthernetDriver/src/bridge/LowLevelEthernetDriver_LowLevelEthernetDriver_api.rs#L22-L29'>Unverified Rust Interface</a> -> <a title='Rust/Verus API' href='crates/LowLevelEthernetDriver_LowLevelEthernetDriver/src/bridge/LowLevelEthernetDriver_LowLevelEthernetDriver_api.rs#L55-L62'>Rust/Verus API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/SW.aadl#L32-L32'>EthernetFramesRx</a></td>
        <td>Out</td><td>Event Data</td>
        <td>SW::RawEthernetMessage</td><td><a title='Rust/Verus API' href='crates/LowLevelEthernetDriver_LowLevelEthernetDriver/src/bridge/LowLevelEthernetDriver_LowLevelEthernetDriver_api.rs#L42-L51'>Rust/Verus API</a> -> <a title='Unverified Rust Interface' href='crates/LowLevelEthernetDriver_LowLevelEthernetDriver/src/bridge/LowLevelEthernetDriver_LowLevelEthernetDriver_api.rs#L12-L17'>Unverified Rust Interface</a> -> <a title='Rust/C Interface' href='crates/LowLevelEthernetDriver_LowLevelEthernetDriver/src/bridge/extern_c_api.rs#L30-L35'>Rust/C Interface</a> -> <a title='C Extern' href='crates/LowLevelEthernetDriver_LowLevelEthernetDriver/src/bridge/extern_c_api.rs#L15-L15'>C Extern</a> -> <a title='Memory Map' href='microkit.system#L80-L84'>Memory Map</a></td></tr>
    </table>

