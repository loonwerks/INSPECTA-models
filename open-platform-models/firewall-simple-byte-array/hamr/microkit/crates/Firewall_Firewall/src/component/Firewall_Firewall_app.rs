#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

// This file will not be overwritten if codegen is rerun

//use data::*;
use data::*;
use crate::bridge::Firewall_Firewall_api::*;
#[cfg(feature = "sel4")]
#[allow(unused_imports)]
use log::{error, warn, info, debug, trace};
use vstd::prelude::*;
use crate::bridge::Firewall_Firewall_GUMBOX as gumbox;

verus! {

  pub struct Firewall_Firewall {
  }

  impl Firewall_Firewall {
    pub fn new() -> Self 
    {
      Self {
      }
    }

    pub fn initialize<API: Firewall_Firewall_Put_Api>(
      &mut self,
      api: &mut Firewall_Firewall_Application_Api<API>) 
    {
      #[cfg(feature = "sel4")]
      info!("initialize entrypoint invoked");
    }

    pub fn timeTriggered<API: Firewall_Firewall_Full_Api>(
      &mut self,
      api: &mut Firewall_Firewall_Application_Api<API>)
      requires
        // BEGIN MARKER TIME TRIGGERED REQUIRES
        // assume AADL_Requirement
        //   All outgoing event ports must be empty
        old(api).EthernetFramesRxOut.is_none(),
        old(api).EthernetFramesTxOut.is_none()
        // END MARKER TIME TRIGGERED REQUIRES
      ensures
        // BEGIN MARKER TIME TRIGGERED ENSURES
        // guarantee rx
        (api.EthernetFramesRxIn.is_some() ==>
          (api.EthernetFramesRxOut.is_some() ==>
            Self::should_allow_inbound_frame_rx(api.EthernetFramesRxIn.unwrap(),true) &&
              (api.EthernetFramesRxIn.unwrap() == api.EthernetFramesRxOut.unwrap())) &&
            (api.EthernetFramesRxOut.is_none() ==> Self::should_allow_inbound_frame_rx(api.EthernetFramesRxIn.unwrap(),false))) &&
          (!(api.EthernetFramesRxIn.is_some()) ==> api.EthernetFramesRxOut.is_none()),
        // guarantee tx
        (api.EthernetFramesTxIn.is_some() ==>
          (api.EthernetFramesTxOut.is_some() ==>
            Self::should_allow_outbound_frame_tx(api.EthernetFramesTxIn.unwrap(),true) &&
              (api.EthernetFramesTxIn.unwrap() == api.EthernetFramesTxOut.unwrap())) &&
            (api.EthernetFramesTxOut.is_none() ==> Self::should_allow_outbound_frame_tx(api.EthernetFramesTxIn.unwrap(),false))) &&
          (!(api.EthernetFramesTxIn.is_some()) ==> api.EthernetFramesTxOut.is_none())
        // END MARKER TIME TRIGGERED ENSURES 
    {
      #[cfg(feature = "sel4")]
      info!("compute entrypoint invoked");  

      match api.get_EthernetFramesRxIn() {
        Some(rxIn) => {
          if (Self::compute_should_allow_inbound_frame_rx(rxIn)) {
            api.put_EthernetFramesRxOut(rxIn);
          }
        },
        None => {},
      }

      match api.get_EthernetFramesTxIn() {
        Some(txIn) => {
          if (Self::compute_should_allow_outbound_frame_tx(txIn)) {
            api.put_EthernetFramesTxOut(txIn);
          }
        },
        None => {},
      }
    }

    pub fn notify(
      &mut self,
      channel: microkit_channel) 
    {
      // this method is called when the monitor does not handle the passed in channel
      match channel {
        _ => {
          #[cfg(feature = "sel4")]
          warn!("Unexpected channel {}", channel)
        }
      }
    }



    #[verifier::external_body] // Tell Verus not to verify
    exec fn echo(string: &str) {
      #[cfg(feature = "sel4")]
      info!("{0}", string); // Print statements (or macros) can not be verified
    }

    // Rx_Firewall
    exec fn compute_should_allow_inbound_frame_rx(frame: SW::RawEthernetMessage) -> (result: bool)
        ensures
            Self::should_allow_inbound_frame_rx(frame, result)
    {
        Self::echo("Computing rules for rx frame...");
        // Inspect frame
        if Self::compute_frame_is_wellformed_eth2(frame) {
            Self::echo("Frame passes wellformedness check...");
            // Allow ARP
            // HLR 1.6
            if Self::compute_frame_has_arp(frame) {
                Self::echo("ALLOW: Frame has ARP.");
                return true;
            }

            // Drop IPv6
            // HLR 1.2
            if Self::compute_frame_has_ipv6(frame) {
                Self::echo("DROP: Frame has IPv6.");
                return false;
            }

            // Inspect (wellformed) IPv4
            if Self::compute_frame_has_ipv4(frame) {
                Self::echo("Frame has IPv4...");
                // Inspect IPv4/TCP
                // HLR 1.3
                if Self::compute_frame_has_ipv4_tcp(frame) {
                    // Allow or drop according to whitelist
                    // HLR 1.4, 1.7
                    Self::echo("Frame has TCP...");
                    if Self::compute_frame_has_ipv4_tcp_on_allowed_port(frame) {
                        Self::echo("ALLOW: Frame uses allowed TCP port.");
                        return true;
                    } else {
                        Self::echo("DROP: Frame does not use allowed TCP port.");
                        return false;
                    }
                }

                // Inspect IPv4/UDP
                // HLR 1.3
                if Self::compute_frame_has_ipv4_udp(frame) {
                    // Allow or drop according to whitelist
                    // HLRs 1.5, 1.8
                    Self::echo("Frame has UDP...");
                    if Self::compute_frame_has_ipv4_udp_on_allowed_port(frame) {
                        Self::echo("ALLOW: Frame uses allowed UDP port.");
                        return true;
                    } else {
                        Self::echo("DROP: Frame does not use allowed UDP port.");
                        return false;
                    }
                }

                // Drop unrecognized layer 4
                Self::echo("DROP: Frame has unrecognized layer 4.");
                return false;
            }

            // ASSUMPTION: Deny-by-default unrecognized layer 3
            Self::echo("DROP: Frame has unrecognized layer 3.");
            return false;
        }

        // Drop malformed/unrecognized frame
        // HLR 1.1
        Self::echo("DROP: Frame is malformed.");
        return false;
    }

    // TX_Firewall
    exec fn compute_should_allow_outbound_frame_tx(frame: SW::RawEthernetMessage) -> (result: bool)
        ensures
            Self::should_allow_outbound_frame_tx(frame, result)
    {
       Self::echo("Computing rules for tx frame...");
        // Inspect frame
        if Self::compute_frame_is_wellformed_eth2(frame) {
            // Drop IPv6
            // HLR 2.2
            if Self::compute_frame_has_ipv6(frame) {
                Self::echo("DROP: Frame has IPv6.");
                return false;
            }

            // Allow ARP
            // HLR 2.3
            if Self::compute_frame_has_arp(frame) {
                Self::echo("ALLOW: Frame has ARP.");
                return true;
            }

            // Allow IPv4
            // HLR 2.4
            // TODO ambiguous wording about needing to calculate frame size
            if Self::compute_frame_has_ipv4(frame) {
                Self::echo("ALLOW: Frame has IPv4.");
                return true;
            }

            // ASSUMPTION: Deny-by-default unrecognized layer 3
            Self::echo("DROP: Frame has unrecognized layer 3.");
            return false;
        }

        // Drop malformed/unrecognized frame
        // HLR 2.1
        return false;
    }

    exec fn compute_frame_is_wellformed_eth2(frame: SW::RawEthernetMessage) -> (result: bool)
      ensures
        Self::frame_is_wellformed_eth2(frame) <==> result,
    {
      // Ethertype geq 0x0600 implies ethernet II
      //           leq 0x05DC implies 802.3
      //           else       undefined

      // Simple EtherType check
      // second expression of AND is redundant here
      if !(frame[12] >= 0x06 && frame[13] >= 0x00) {
          // Incorrect EtherType
          return false;
      }

      // No checks fail
      // Is wellformed Ethernet II
      return true;
    }

    exec fn compute_frame_has_ipv6(frame: SW::RawEthernetMessage) -> (result: bool)
      requires
        Self::frame_is_wellformed_eth2(frame),
      ensures
        Self::frame_has_ipv6(frame) <==> result,
    {
      // Check for Eth2 EtherType octets equal to 0x86DD for IPv6
      if !(frame[12] == 0x86 && frame[13] == 0xDD) {
          // Not IPv6
          return false;
      }

      // Has IPv6
      return true;
    }

    exec fn compute_frame_has_arp(frame: SW::RawEthernetMessage) -> (result: bool)
      requires
          Self::frame_is_wellformed_eth2(frame),
      ensures
          Self::frame_has_arp(frame) <==> result,
    {
      // EtherType eq 0x0806 implies ARP

      // Check for Eth2 EtherType octets equal to 0x0806 for ARP
      if !(frame[12] == 0x08 && frame[13] == 0x06) {
          // Not ARP
          return false;
      }

      // Has ARP
      return true;
    }

    pub exec fn compute_frame_has_ipv4(frame: SW::RawEthernetMessage) -> (result: bool)
      requires
          Self::frame_is_wellformed_eth2(frame),
      ensures
          Self::frame_has_ipv4(frame) <==> result,
    {

      // Check for Eth2 EtherType octets equal to 0x0800 for IPv4
      if !(frame[12] == 0x08 && frame[13] == 0x00) {
        // Not IPv4
        return false;
      }

      // Has IPv4
      return true;
    }

    exec fn compute_frame_has_ipv4_tcp(frame: SW::RawEthernetMessage) -> (result: bool)
      requires
          Self::frame_is_wellformed_eth2(frame),
          Self::frame_has_ipv4(frame),
      ensures
          Self::frame_has_ipv4_tcp(frame) <==> result,
    {
        // Check for IPv4 Protocol octet equal to 0x06 for TCP
        if !(frame[23] == 0x06) {
            // Not TCP
            return false;
        }

        // Has TCP
        return true;
    }

    exec fn compute_frame_has_ipv4_udp(frame: SW::RawEthernetMessage) -> (result: bool)
      requires
          Self::frame_is_wellformed_eth2(frame),
          Self::frame_has_ipv4(frame),
      ensures
          Self::frame_has_ipv4_udp(frame) <==> result == true,
    {
        // Check for IPv4 Protocol octet equal to 0x11 for UDP
        if !(frame[23] == 0x11) {
            // Not UDP
            return false;
        }

        // Has UDP
        return true;
    }

    exec fn compute_frame_has_ipv4_tcp_on_allowed_port(frame: SW::RawEthernetMessage) -> (result: bool)
      requires
          Self::frame_is_wellformed_eth2(frame),
          Self::frame_has_ipv4(frame),
          Self::frame_has_ipv4_tcp(frame),
      ensures
          Self::frame_has_ipv4_tcp_on_allowed_port(frame) <==> result,
    {
        // Deny by default
        //let mut result: bool = false;

        //let port_byte0: u8 = frame[36];
        //let port_byte1: u8 = frame[37];
        //let port: u16 = ((port_byte0 as u16) * 256) + (port_byte1 as u16);

        /*
        // Check that destination port is in the allowed list
        let mut i = 0;
        while i < TCP_ALLOWED_PORTS_LENGTH
            invariant
                TCP_ALLOWED_PORTS_LENGTH == TCP_ALLOWED_PORTS.len(),
                0 <= i <= TCP_ALLOWED_PORTS.len(),
        {
            let allowed_port = TCP_ALLOWED_PORTS[i];
            if port == allowed_port {
                // Destination port matches some allowed port
                result = true;
                proof {
                    frame.len() >= MIN_ETH2_FRAME_LENGTH >= 37;
                    allowed_port == two_bytes_to_u16(frame[36], frame[37]);
                    port == allowed_port;
                    exists|j:int| 0 <= j < TCP_ALLOWED_PORTS.len() ==> TCP_ALLOWED_PORTS[j] == two_bytes_to_u16(frame[36], frame[37]);
                    frame_has_ipv4_tcp_on_allowed_port(frame);
                }
            } else {
                proof {
                    frame.len() >= MIN_ETH2_FRAME_LENGTH >= 37;
                    allowed_port == two_bytes_to_u16(frame[36], frame[37]);
                    port != allowed_port;
                }
            }
            i += 1;
        }
        return result;
        */

        return Self::compute_TCP_ALLOWED_PORTS()[0] == (256 * frame[36] as u16) + frame[37] as u16
    }

    exec fn compute_frame_has_ipv4_udp_on_allowed_port(frame: SW::RawEthernetMessage) -> (result: bool)
      requires
        Self::frame_is_wellformed_eth2(frame),
        Self::frame_has_ipv4(frame),
        Self::frame_has_ipv4_udp(frame),
      ensures
        Self::frame_has_ipv4_udp_on_allowed_port(frame) <==> result,

    {
        // Deny by default
        //let mut result: bool = false;

        //let port_byte0: u8 = frame[36];
        //let port_byte1: u8 = frame[37];
        //let port: u16 = ((port_byte0 as u16) * 256) + (port_byte1 as u16);

        /*
        // Check that destination port is in the allowed list
        let mut i = 0;
        while i < UDP_ALLOWED_PORTS_LENGTH
            invariant
                UDP_ALLOWED_PORTS_LENGTH == UDP_ALLOWED_PORTS.len(),
                0 <= i <= UDP_ALLOWED_PORTS.len(),
        {
            let allowed_port = UDP_ALLOWED_PORTS[i];
            if port == allowed_port {
                // Destination port matches some allowed port
                result = true;
                proof {
                    frame.len() >= MIN_ETH2_FRAME_LENGTH >= 37;
                    allowed_port == two_bytes_to_u16(frame[36], frame[37]);
                    port == allowed_port;
                    exists|j:int| 0 <= j < UDP_ALLOWED_PORTS.len() ==> UDP_ALLOWED_PORTS[j] == two_bytes_to_u16(frame[36], frame[37]);
                    frame_has_ipv4_udp_on_allowed_port(frame);
                }
            } else {
                proof {
                    frame.len() >= MIN_ETH2_FRAME_LENGTH >= 37;
                    allowed_port == two_bytes_to_u16(frame[36], frame[37]);
                    port != allowed_port;
                }
            }
            i += 1;
        }
        return result;
        */

        return Self::compute_UDP_ALLOWED_PORTS()[0] == (256 * frame[36] as u16) + frame[37] as u16
    }

    exec fn compute_two_bytes_to_u16(byte0: u8, byte1: u8) -> (res: u16)
      ensures res == Self::two_bytes_to_u16(byte0, byte1)
    {
      return (((byte0) as u16) * 256u16 + ((byte1) as u16)) as u16
    }


    exec fn compute_TCP_ALLOWED_PORTS() -> (res: SW::u16Array)
      ensures res == Self::TCP_ALLOWED_PORTS()
    {
       [5760u16, 0u16, 0u16, 0u16]
    }

    exec fn compute_UDP_ALLOWED_PORTS() -> (res: SW::u16Array)
      ensures res == Self::UDP_ALLOWED_PORTS()
    {
       [68u16, 0u16, 0u16, 0u16]
    }




    // BEGIN MARKER GUMBO METHODS
    pub open spec fn TCP_ALLOWED_PORTS() -> SW::u16Array 
    {
      [5760u16, 0u16, 0u16, 0u16]
    }

    pub open spec fn UDP_ALLOWED_PORTS() -> SW::u16Array 
    {
      [68u16, 0u16, 0u16, 0u16]
    }

    pub open spec fn two_bytes_to_u16(
      byte0: u8,
      byte1: u8) -> u16 
    {
      (((byte0) as u16) * 256u16 + ((byte1) as u16)) as u16
    }

    pub open spec fn frame_is_wellformed_eth2(frame: SW::RawEthernetMessage) -> bool 
    {
      if (!((frame[12] >= 6u8) &&
        (frame[13] >= 0u8))) {
        false
      } else {
        true
      }
    }

    pub open spec fn frame_has_ipv4(frame: SW::RawEthernetMessage) -> bool 
    {
      Self::frame_is_wellformed_eth2(frame) ==>
        (if (!((frame[12] == 8u8) &&
          (frame[13] == 0u8))) {
          false
        } else {
          true
        })
    }

    pub open spec fn frame_has_ipv4_tcp(frame: SW::RawEthernetMessage) -> bool 
    {
      Self::frame_is_wellformed_eth2(frame) && Self::frame_has_ipv4(frame) ==>
        (if (frame[23] != 6u8) {
          false
        } else {
          true
        })
    }

    pub open spec fn frame_has_ipv4_udp(frame: SW::RawEthernetMessage) -> bool 
    {
      Self::frame_is_wellformed_eth2(frame) && Self::frame_has_ipv4(frame) ==>
        (if (!(frame[23] == 17u8)) {
          false
        } else {
          true
        })
    }

    pub open spec fn frame_has_ipv4_tcp_on_allowed_port(frame: SW::RawEthernetMessage) -> bool 
    {
      Self::frame_is_wellformed_eth2(frame) && Self::frame_has_ipv4(frame) &&
        Self::frame_has_ipv4_tcp(frame) ==>
        (Self::TCP_ALLOWED_PORTS()[0] == Self::two_bytes_to_u16(frame[36],frame[37]))
    }

    pub open spec fn frame_has_ipv4_tcp_on_allowed_port_quant(frame: SW::RawEthernetMessage) -> bool 
    {
      exists|i:int| 0 <= i <= Self::TCP_ALLOWED_PORTS().len() - 1 && Self::TCP_ALLOWED_PORTS()[i] == Self::two_bytes_to_u16(frame[36],frame[37])
    }

    pub open spec fn frame_has_ipv4_udp_on_allowed_port(frame: SW::RawEthernetMessage) -> bool 
    {
      Self::frame_is_wellformed_eth2(frame) && Self::frame_has_ipv4(frame) &&
        Self::frame_has_ipv4_udp(frame) ==>
        (Self::UDP_ALLOWED_PORTS()[0] == Self::two_bytes_to_u16(frame[36],frame[37]))
    }

    pub open spec fn frame_has_ipv4_udp_on_allowed_port_quant(frame: SW::RawEthernetMessage) -> bool 
    {
      exists|i:int| 0 <= i <= Self::UDP_ALLOWED_PORTS().len() - 1 && Self::UDP_ALLOWED_PORTS()[i] == Self::two_bytes_to_u16(frame[36],frame[37])
    }

    pub open spec fn frame_has_ipv6(frame: SW::RawEthernetMessage) -> bool 
    {
      Self::frame_is_wellformed_eth2(frame) ==>
        (if (!((frame[12] == 134u8) &&
          (frame[13] == 221u8))) {
          false
        } else {
          true
        })
    }

    pub open spec fn frame_has_arp(frame: SW::RawEthernetMessage) -> bool 
    {
      Self::frame_is_wellformed_eth2(frame) ==>
        (if (!((frame[12] == 8u8) &&
          (frame[13] == 6u8))) {
          false
        } else {
          true
        })
    }

    pub open spec fn hlr_1_1(
      frame: SW::RawEthernetMessage,
      should_allow: bool) -> bool 
    {
      if (!(Self::frame_is_wellformed_eth2(frame))) {
        should_allow == false
      } else {
        true
      }
    }

    pub open spec fn hlr_1_2(
      frame: SW::RawEthernetMessage,
      should_allow: bool) -> bool 
    {
      if (Self::frame_is_wellformed_eth2(frame) && Self::frame_has_ipv6(frame)) {
        should_allow == false
      } else {
        true
      }
    }

    pub open spec fn hlr_1_3(
      frame: SW::RawEthernetMessage,
      should_allow: bool) -> bool 
    {
      if (Self::frame_is_wellformed_eth2(frame) && Self::frame_has_ipv4(frame) &&
        !(Self::frame_has_ipv4_tcp(frame) || Self::frame_has_ipv4_udp(frame))) {
        should_allow == false
      } else {
        true
      }
    }

    pub open spec fn hlr_1_4(
      frame: SW::RawEthernetMessage,
      should_allow: bool) -> bool 
    {
      if (Self::frame_is_wellformed_eth2(frame) && Self::frame_has_ipv4(frame) &&
        Self::frame_has_ipv4_tcp(frame) &&
        !(Self::frame_has_ipv4_tcp_on_allowed_port(frame))) {
        should_allow == false
      } else {
        true
      }
    }

    pub open spec fn hlr_1_5(
      frame: SW::RawEthernetMessage,
      should_allow: bool) -> bool 
    {
      if (Self::frame_is_wellformed_eth2(frame) && Self::frame_has_ipv4(frame) &&
        Self::frame_has_ipv4_udp(frame) &&
        !(Self::frame_has_ipv4_udp_on_allowed_port(frame))) {
        should_allow == false
      } else {
        true
      }
    }

    pub open spec fn hlr_1_6(
      frame: SW::RawEthernetMessage,
      should_allow: bool) -> bool 
    {
      if (Self::frame_is_wellformed_eth2(frame) && Self::frame_has_arp(frame)) {
        should_allow == true
      } else {
        true
      }
    }

    pub open spec fn hlr_1_7(
      frame: SW::RawEthernetMessage,
      should_allow: bool) -> bool 
    {
      if (Self::frame_is_wellformed_eth2(frame) && Self::frame_has_ipv4(frame) &&
        Self::frame_has_ipv4_tcp(frame) &&
        Self::frame_has_ipv4_tcp_on_allowed_port(frame)) {
        should_allow == true
      } else {
        true
      }
    }

    pub open spec fn hlr_1_8(
      frame: SW::RawEthernetMessage,
      should_allow: bool) -> bool 
    {
      if (Self::frame_is_wellformed_eth2(frame) && Self::frame_has_ipv4(frame) &&
        Self::frame_has_ipv4_udp(frame) &&
        Self::frame_has_ipv4_udp_on_allowed_port(frame)) {
        should_allow == true
      } else {
        true
      }
    }

    pub open spec fn should_allow_inbound_frame_rx(
      frame: SW::RawEthernetMessage,
      should_allow: bool) -> bool 
    {
      Self::hlr_1_1(frame,should_allow) && Self::hlr_1_2(frame,should_allow) &&
        Self::hlr_1_3(frame,should_allow) &&
        Self::hlr_1_4(frame,should_allow) &&
        Self::hlr_1_5(frame,should_allow) &&
        Self::hlr_1_6(frame,should_allow) &&
        Self::hlr_1_7(frame,should_allow) &&
        Self::hlr_1_8(frame,should_allow)
    }

    pub open spec fn hlr_2_1(
      frame: SW::RawEthernetMessage,
      should_allow: bool) -> bool 
    {
      if (!(Self::frame_is_wellformed_eth2(frame))) {
        should_allow == false
      } else {
        true
      }
    }

    pub open spec fn hlr_2_2(
      frame: SW::RawEthernetMessage,
      should_allow: bool) -> bool 
    {
      if (Self::frame_is_wellformed_eth2(frame) && Self::frame_has_ipv6(frame)) {
        should_allow == false
      } else {
        true
      }
    }

    pub open spec fn hlr_2_3(
      frame: SW::RawEthernetMessage,
      should_allow: bool) -> bool 
    {
      if (Self::frame_is_wellformed_eth2(frame) && Self::frame_has_arp(frame)) {
        should_allow == true
      } else {
        true
      }
    }

    pub open spec fn hlr_2_4(
      frame: SW::RawEthernetMessage,
      should_allow: bool) -> bool 
    {
      if (Self::frame_is_wellformed_eth2(frame) && Self::frame_has_ipv4(frame)) {
        should_allow == true
      } else {
        true
      }
    }

    pub open spec fn should_allow_outbound_frame_tx(
      frame: SW::RawEthernetMessage,
      should_allow: bool) -> bool 
    {
      Self::hlr_2_1(frame,should_allow) && Self::hlr_2_2(frame,should_allow) &&
        Self::hlr_2_3(frame,should_allow) &&
        Self::hlr_2_4(frame,should_allow)
    }
    // END MARKER GUMBO METHODS
  }

}
