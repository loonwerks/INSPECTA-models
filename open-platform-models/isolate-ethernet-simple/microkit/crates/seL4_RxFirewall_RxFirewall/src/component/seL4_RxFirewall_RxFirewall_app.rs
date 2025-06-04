#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

// This file will not be overwritten if codegen is rerun

use crate::data::*;
use crate::bridge::seL4_RxFirewall_RxFirewall_api::*;
#[cfg(feature = "sel4")]
#[allow(unused_imports)]
use log::{error, warn, info, debug, trace};
use vstd::prelude::*;

verus! {

  pub struct seL4_RxFirewall_RxFirewall {
  }

  impl seL4_RxFirewall_RxFirewall {
    pub fn new() -> Self 
    {
      Self {
      }
    }

    pub fn initialize<API: seL4_RxFirewall_RxFirewall_Put_Api>(
      &mut self,
      api: &mut seL4_RxFirewall_RxFirewall_Application_Api<API>) 
    {
      #[cfg(feature = "sel4")]
      info!("initialize entrypoint invoked");
    }

    pub fn timeTriggered<API: seL4_RxFirewall_RxFirewall_Full_Api>(
      &mut self,
      api: &mut seL4_RxFirewall_RxFirewall_Application_Api<API>)
      requires
        // BEGIN MARKER TIME TRIGGERED REQUIRES
        // assume AADL_Requirement
        //   All outgoing event ports must be empty
        old(api).EthernetFramesRxOut0.is_none(),
        old(api).EthernetFramesRxOut1.is_none(),
        old(api).EthernetFramesRxOut2.is_none(),
        old(api).EthernetFramesRxOut3.is_none()
        // END MARKER TIME TRIGGERED REQUIRES
      ensures
        // BEGIN MARKER TIME TRIGGERED ENSURES
        // guarantee rx
        api.EthernetFramesRxIn0.is_some() ==>
          (api.EthernetFramesRxOut0.is_some() ==> Self::should_allow_inbound_frame_rx(api.EthernetFramesRxIn0.unwrap(),true)) &&
            (api.EthernetFramesRxOut0.is_none() ==> Self::should_allow_inbound_frame_rx(api.EthernetFramesRxIn0.unwrap(),false))
        // END MARKER TIME TRIGGERED ENSURES 
    {
      #[cfg(feature = "sel4")]
      info!("compute entrypoint invoked");
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
      exists|i:int| 0 <= i && i <= Self::TCP_ALLOWED_PORTS().len() - 1 ==> Self::TCP_ALLOWED_PORTS()[i] == Self::two_bytes_to_u16(frame[36],frame[37])
    }

    pub open spec fn frame_has_ipv4_udp_on_allowed_port(frame: SW::RawEthernetMessage) -> bool 
    {
      Self::frame_is_wellformed_eth2(frame) && Self::frame_has_ipv4(frame) &&
        Self::frame_has_ipv4_udp(frame) ==>
        (Self::UDP_ALLOWED_PORTS()[0] == Self::two_bytes_to_u16(frame[36],frame[37]))
    }

    pub open spec fn frame_has_ipv4_udp_on_allowed_port_quant(frame: SW::RawEthernetMessage) -> bool 
    {
      exists|i:int| 0 <= i && i <= Self::UDP_ALLOWED_PORTS().len() - 1 ==> Self::UDP_ALLOWED_PORTS()[i] == Self::two_bytes_to_u16(frame[36],frame[37])
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
    // END MARKER GUMBO METHODS
  }

}
