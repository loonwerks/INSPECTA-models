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

[Microkit System Description](microkit.system)

### Behavior Code
#### ArduPilot: SW::ArduPilot.Impl

 - **Entry Points**

    Initialize: [Rust](crates/ArduPilot_ArduPilot/src/component/ArduPilot_ArduPilot_app.rs#L22-L28)

    TimeTriggered: [Rust](crates/ArduPilot_ArduPilot/src/component/ArduPilot_ArduPilot_app.rs#L30-L36)


 - **APIs**

     <table>
     <tr><th colspan=3>Ports</th></tr>
     <tr><td>EthernetFramesRx</td>
     <td><a href=../../aadl/SW.aadl#L280-L280>Model</a></td>
     <td><a href=crates/ArduPilot_ArduPilot/src/bridge/ArduPilot_ArduPilot_api.rs#L55-L62>Rust API</a></td>
     <tr><td>EthernetFramesTx</td>
     <td><a href=../../aadl/SW.aadl#L281-L281>Model</a></td>
     <td><a href=crates/ArduPilot_ArduPilot/src/bridge/ArduPilot_ArduPilot_api.rs#L42-L51>Rust API</a></td></table>



#### Firewall: SW::Firewall.Impl

 - **Entry Points**

    Initialize: [Rust](crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L27-L33)

    TimeTriggered: [Rust](crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L35-L83)


 - **APIs**

     <table>
     <tr><th colspan=3>Ports</th></tr>
     <tr><td>EthernetFramesRxIn</td>
     <td><a href=../../aadl/SW.aadl#L66-L66>Model</a></td>
     <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_api.rs#L99-L108>Rust API</a></td>
     <tr><td>EthernetFramesTxIn</td>
     <td><a href=../../aadl/SW.aadl#L69-L69>Model</a></td>
     <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_api.rs#L89-L98>Rust API</a></td>
     <tr><td>EthernetFramesRxOut</td>
     <td><a href=../../aadl/SW.aadl#L67-L67>Model</a></td>
     <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_api.rs#L62-L73>Rust API</a></td>
     <tr><td>EthernetFramesTxOut</td>
     <td><a href=../../aadl/SW.aadl#L70-L70>Model</a></td>
     <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_api.rs#L74-L85>Rust API</a></td></table>

- **GUMBO**

    <table>
    <tr><th colspan=4>Compute</th></tr>
    <tr><td>guarantee rx</td>
    <td><a href=../../aadl/SW.aadl#L238-L242>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L47-L49>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L307-L323>GUMBOX</a></td>
    </tr>
    <tr><td>guarantee tx</td>
    <td><a href=../../aadl/SW.aadl#L244-L248>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L54-L56>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L331-L347>GUMBOX</a></td>
    </tr></table>
    <table>
    <tr><th colspan=4>GUMBO Methods</th></tr>
    <tr><td>TCP_ALLOWED_PORTS</td>
    <td><a href=../../aadl/SW.aadl#L89-L89>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L445-L448>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L17-L20>GUMBOX</a></td>
    </tr>
    <tr><td>UDP_ALLOWED_PORTS</td>
    <td><a href=../../aadl/SW.aadl#L91-L91>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L450-L453>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L22-L25>GUMBOX</a></td>
    </tr>
    <tr><td>two_bytes_to_u16</td>
    <td><a href=../../aadl/SW.aadl#L94-L95>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L455-L460>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L27-L32>GUMBOX</a></td>
    </tr>
    <tr><td>frame_is_wellformed_eth2</td>
    <td><a href=../../aadl/SW.aadl#L97-L100>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L462-L470>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L34-L42>GUMBOX</a></td>
    </tr>
    <tr><td>frame_has_ipv4</td>
    <td><a href=../../aadl/SW.aadl#L103-L106>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L472-L481>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L44-L54>GUMBOX</a></td>
    </tr>
    <tr><td>frame_has_ipv4_tcp</td>
    <td><a href=../../aadl/SW.aadl#L108-L111>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L483-L491>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L56-L65>GUMBOX</a></td>
    </tr>
    <tr><td>frame_has_ipv4_udp</td>
    <td><a href=../../aadl/SW.aadl#L113-L116>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L493-L501>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L67-L76>GUMBOX</a></td>
    </tr>
    <tr><td>frame_has_ipv4_tcp_on_allowed_port</td>
    <td><a href=../../aadl/SW.aadl#L118-L122>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L503-L508>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L78-L84>GUMBOX</a></td>
    </tr>
    <tr><td>frame_has_ipv4_tcp_on_allowed_port_quant</td>
    <td><a href=../../aadl/SW.aadl#L124-L126>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L510-L513>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L86-L94>GUMBOX</a></td>
    </tr>
    <tr><td>frame_has_ipv4_udp_on_allowed_port</td>
    <td><a href=../../aadl/SW.aadl#L128-L132>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L515-L520>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L96-L102>GUMBOX</a></td>
    </tr>
    <tr><td>frame_has_ipv4_udp_on_allowed_port_quant</td>
    <td><a href=../../aadl/SW.aadl#L134-L136>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L522-L525>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L104-L112>GUMBOX</a></td>
    </tr>
    <tr><td>frame_has_ipv6</td>
    <td><a href=../../aadl/SW.aadl#L138-L141>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L527-L536>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L114-L124>GUMBOX</a></td>
    </tr>
    <tr><td>frame_has_arp</td>
    <td><a href=../../aadl/SW.aadl#L143-L146>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L538-L547>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L126-L136>GUMBOX</a></td>
    </tr>
    <tr><td>hlr_1_1</td>
    <td><a href=../../aadl/SW.aadl#L153-L155>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L549-L558>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L138-L147>GUMBOX</a></td>
    </tr>
    <tr><td>hlr_1_2</td>
    <td><a href=../../aadl/SW.aadl#L157-L159>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L560-L569>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L149-L158>GUMBOX</a></td>
    </tr>
    <tr><td>hlr_1_3</td>
    <td><a href=../../aadl/SW.aadl#L161-L165>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L571-L581>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L160-L170>GUMBOX</a></td>
    </tr>
    <tr><td>hlr_1_4</td>
    <td><a href=../../aadl/SW.aadl#L167-L172>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L583-L594>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L172-L183>GUMBOX</a></td>
    </tr>
    <tr><td>hlr_1_5</td>
    <td><a href=../../aadl/SW.aadl#L174-L179>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L596-L607>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L185-L196>GUMBOX</a></td>
    </tr>
    <tr><td>hlr_1_6</td>
    <td><a href=../../aadl/SW.aadl#L181-L184>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L609-L618>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L198-L207>GUMBOX</a></td>
    </tr>
    <tr><td>hlr_1_7</td>
    <td><a href=../../aadl/SW.aadl#L186-L191>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L620-L631>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L209-L220>GUMBOX</a></td>
    </tr>
    <tr><td>hlr_1_8</td>
    <td><a href=../../aadl/SW.aadl#L193-L198>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L633-L644>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L222-L233>GUMBOX</a></td>
    </tr>
    <tr><td>should_allow_inbound_frame_rx</td>
    <td><a href=../../aadl/SW.aadl#L200-L208>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L646-L657>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L235-L246>GUMBOX</a></td>
    </tr>
    <tr><td>hlr_2_1</td>
    <td><a href=../../aadl/SW.aadl#L214-L216>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L659-L668>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L248-L257>GUMBOX</a></td>
    </tr>
    <tr><td>hlr_2_2</td>
    <td><a href=../../aadl/SW.aadl#L218-L220>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L670-L679>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L259-L268>GUMBOX</a></td>
    </tr>
    <tr><td>hlr_2_3</td>
    <td><a href=../../aadl/SW.aadl#L222-L224>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L681-L690>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L270-L279>GUMBOX</a></td>
    </tr>
    <tr><td>hlr_2_4</td>
    <td><a href=../../aadl/SW.aadl#L226-L228>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L692-L701>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L281-L290>GUMBOX</a></td>
    </tr>
    <tr><td>should_allow_outbound_frame_tx</td>
    <td><a href=../../aadl/SW.aadl#L230-L234>GUMBO</a></td>
    <td><a href=crates/Firewall_Firewall/src/component/Firewall_Firewall_app.rs#L703-L710>Verus</a></td>
    <td><a href=crates/Firewall_Firewall/src/bridge/Firewall_Firewall_GUMBOX.rs#L292-L299>GUMBOX</a></td>
    </tr></table>


#### LowLevelEthernetDriver: SW::LowLevelEthernetDriver.Impl

 - **Entry Points**

    Initialize: [Rust](crates/LowLevelEthernetDriver_LowLevelEthernetDriver/src/component/LowLevelEthernetDriver_LowLevelEthernetDriver_app.rs#L22-L28)

    TimeTriggered: [Rust](crates/LowLevelEthernetDriver_LowLevelEthernetDriver/src/component/LowLevelEthernetDriver_LowLevelEthernetDriver_app.rs#L30-L36)


 - **APIs**

     <table>
     <tr><th colspan=3>Ports</th></tr>
     <tr><td>EthernetFramesTx</td>
     <td><a href=../../aadl/SW.aadl#L33-L33>Model</a></td>
     <td><a href=crates/LowLevelEthernetDriver_LowLevelEthernetDriver/src/bridge/LowLevelEthernetDriver_LowLevelEthernetDriver_api.rs#L55-L62>Rust API</a></td>
     <tr><td>EthernetFramesRx</td>
     <td><a href=../../aadl/SW.aadl#L32-L32>Model</a></td>
     <td><a href=crates/LowLevelEthernetDriver_LowLevelEthernetDriver/src/bridge/LowLevelEthernetDriver_LowLevelEthernetDriver_api.rs#L42-L51>Rust API</a></td></table>


