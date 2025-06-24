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
        // guarantee rx0_allow
        api.EthernetFramesRxIn0.is_some() && Self::allow_outbound_frame(api.EthernetFramesRxIn0.unwrap()) ==>
          api.EthernetFramesRxOut0.is_some() &&
            (api.EthernetFramesRxIn0.unwrap() == api.EthernetFramesRxOut0.unwrap()),
        // guarantee rx0_disallow
        api.EthernetFramesRxIn0.is_some() && !(Self::allow_outbound_frame(api.EthernetFramesRxIn0.unwrap())) ==>
          api.EthernetFramesRxOut0.is_none(),
        // guarantee rx0_no_input
        !(api.EthernetFramesRxIn0.is_some()) ==> api.EthernetFramesRxOut0.is_none(),
        // guarantee rx1_allow
        api.EthernetFramesRxIn1.is_some() && Self::allow_outbound_frame(api.EthernetFramesRxIn1.unwrap()) ==>
          api.EthernetFramesRxOut1.is_some() &&
            (api.EthernetFramesRxIn1.unwrap() == api.EthernetFramesRxOut1.unwrap()),
        // guarantee rx1_disallow
        api.EthernetFramesRxIn1.is_some() && !(Self::allow_outbound_frame(api.EthernetFramesRxIn1.unwrap())) ==>
          api.EthernetFramesRxOut1.is_none(),
        // guarantee rx1_no_input
        !(api.EthernetFramesRxIn1.is_some()) ==> api.EthernetFramesRxOut1.is_none(),
        // guarantee rx2_allow
        api.EthernetFramesRxIn2.is_some() && Self::allow_outbound_frame(api.EthernetFramesRxIn2.unwrap()) ==>
          api.EthernetFramesRxOut2.is_some() &&
            (api.EthernetFramesRxIn2.unwrap() == api.EthernetFramesRxOut2.unwrap()),
        // guarantee rx2_disallow
        api.EthernetFramesRxIn2.is_some() && !(Self::allow_outbound_frame(api.EthernetFramesRxIn2.unwrap())) ==>
          api.EthernetFramesRxOut2.is_none(),
        // guarantee rx2_no_input
        !(api.EthernetFramesRxIn2.is_some()) ==> api.EthernetFramesRxOut2.is_none(),
        // guarantee rx3_allow
        api.EthernetFramesRxIn3.is_some() && Self::allow_outbound_frame(api.EthernetFramesRxIn3.unwrap()) ==>
          api.EthernetFramesRxOut3.is_some() &&
            (api.EthernetFramesRxIn3.unwrap() == api.EthernetFramesRxOut3.unwrap()),
        // guarantee rx3_disallow
        api.EthernetFramesRxIn3.is_some() && !(Self::allow_outbound_frame(api.EthernetFramesRxIn3.unwrap())) ==>
          api.EthernetFramesRxOut3.is_none(),
        // guarantee rx3_no_input
        !(api.EthernetFramesRxIn3.is_some()) ==> api.EthernetFramesRxOut3.is_none()
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
      [5760u16]
    }

    pub open spec fn UDP_ALLOWED_PORTS() -> SW::u16Array 
    {
      [68u16]
    }

    pub open spec fn two_bytes_to_u16(
      byte0: u8,
      byte1: u8) -> u16 
    {
      (((byte0) as u16) * 256u16 + ((byte1) as u16)) as u16
    }

    pub open spec fn frame_is_wellformed_eth2(frame: SW::RawEthernetMessage) -> bool 
    {
      Self::valid_frame_ethertype(frame) && Self::valid_frame_dst_addr(frame)
    }

    pub open spec fn valid_frame_ethertype(frame: SW::RawEthernetMessage) -> bool 
    {
      Self::frame_has_ipv4(frame) || Self::frame_has_arp(frame) ||
        Self::frame_has_ipv6(frame)
    }

    pub open spec fn valid_frame_dst_addr(frame: SW::RawEthernetMessage) -> bool 
    {
      !((frame[0] == 0u8) &&
        (frame[1] == 0u8) &&
        (frame[2] == 0u8) &&
        (frame[3] == 0u8) &&
        (frame[4] == 0u8) &&
        (frame[5] == 0u8))
    }

    pub open spec fn frame_has_ipv4(frame: SW::RawEthernetMessage) -> bool 
    {
      (frame[12] == 8u8) &&
        (frame[13] == 0u8)
    }

    pub open spec fn frame_has_ipv6(frame: SW::RawEthernetMessage) -> bool 
    {
      (frame[12] == 134u8) &&
        (frame[13] == 221u8)
    }

    pub open spec fn frame_has_arp(frame: SW::RawEthernetMessage) -> bool 
    {
      (frame[12] == 8u8) &&
        (frame[13] == 6u8)
    }

    pub open spec fn arp_has_ipv4(frame: SW::RawEthernetMessage) -> bool 
    {
      (frame[16] == 8u8) &&
        (frame[17] == 0u8)
    }

    pub open spec fn arp_has_ipv6(frame: SW::RawEthernetMessage) -> bool 
    {
      (frame[16] == 134u8) &&
        (frame[17] == 221u8)
    }

    pub open spec fn valid_arp_ptype(frame: SW::RawEthernetMessage) -> bool 
    {
      Self::arp_has_ipv4(frame) || Self::arp_has_ipv6(frame)
    }

    pub open spec fn valid_arp_op(frame: SW::RawEthernetMessage) -> bool 
    {
      (frame[20] == 0u8) &&
        ((frame[21] == 1u8) ||
          (frame[21] == 2u8))
    }

    pub open spec fn valid_arp_htype(frame: SW::RawEthernetMessage) -> bool 
    {
      (frame[14] == 0u8) &&
        (frame[15] == 1u8)
    }

    pub open spec fn wellformed_arp_frame(frame: SW::RawEthernetMessage) -> bool 
    {
      Self::valid_arp_op(frame) && Self::valid_arp_htype(frame) &&
        Self::valid_arp_ptype(frame)
    }

    pub open spec fn valid_ipv4_length(frame: SW::RawEthernetMessage) -> bool 
    {
      Self::two_bytes_to_u16(frame[16],frame[17]) <= 9000u16
    }

    pub open spec fn valid_ipv4_protocol(frame: SW::RawEthernetMessage) -> bool 
    {
      (frame[23] == 0u8) ||
        (frame[23] == 1u8) ||
        (frame[23] == 2u8) ||
        (frame[23] == 6u8) ||
        (frame[23] == 17u8) ||
        (frame[23] == 43u8) ||
        (frame[23] == 44u8) ||
        (frame[23] == 58u8) ||
        (frame[23] == 59u8) ||
        (frame[23] == 60u8)
    }

    pub open spec fn wellformed_ipv4_frame(frame: SW::RawEthernetMessage) -> bool 
    {
      Self::valid_ipv4_protocol(frame) && Self::valid_ipv4_length(frame)
    }

    pub open spec fn ipv4_is_tcp(frame: SW::RawEthernetMessage) -> bool 
    {
      frame[23] == 6u8
    }

    pub open spec fn ipv4_is_udp(frame: SW::RawEthernetMessage) -> bool 
    {
      frame[23] == 17u8
    }

    pub open spec fn tcp_is_valid_port(frame: SW::RawEthernetMessage) -> bool 
    {
      Self::two_bytes_to_u16(frame[36],frame[37]) == Self::TCP_ALLOWED_PORTS()[0]
    }

    pub open spec fn udp_is_valid_port(frame: SW::RawEthernetMessage) -> bool 
    {
      Self::two_bytes_to_u16(frame[36],frame[37]) == Self::UDP_ALLOWED_PORTS()[0]
    }

    pub open spec fn frame_has_ipv4_tcp_on_allowed_port_quant(frame: SW::RawEthernetMessage) -> bool 
    {
      exists|i:int| 0 <= i && i <= Self::TCP_ALLOWED_PORTS().len() - 1 ==> Self::TCP_ALLOWED_PORTS()[i] == Self::two_bytes_to_u16(frame[36],frame[37])
    }

    pub open spec fn frame_has_ipv4_udp_on_allowed_port_quant(frame: SW::RawEthernetMessage) -> bool 
    {
      exists|i:int| 0 <= i && i <= Self::UDP_ALLOWED_PORTS().len() - 1 ==> Self::UDP_ALLOWED_PORTS()[i] == Self::two_bytes_to_u16(frame[36],frame[37])
    }

    pub open spec fn hlr_05(frame: SW::RawEthernetMessage) -> bool 
    {
      Self::frame_is_wellformed_eth2(frame) && Self::frame_has_arp(frame) &&
        Self::wellformed_arp_frame(frame)
    }

    pub open spec fn hlr_06(frame: SW::RawEthernetMessage) -> bool 
    {
      Self::frame_is_wellformed_eth2(frame) && Self::frame_has_ipv4(frame) &&
        Self::wellformed_ipv4_frame(frame) &&
        Self::ipv4_is_tcp(frame) &&
        Self::frame_has_ipv4_tcp_on_allowed_port_quant(frame)
    }

    pub open spec fn hlr_13(frame: SW::RawEthernetMessage) -> bool 
    {
      Self::frame_is_wellformed_eth2(frame) && Self::frame_has_ipv4(frame) &&
        Self::wellformed_ipv4_frame(frame) &&
        Self::ipv4_is_udp(frame) &&
        Self::frame_has_ipv4_udp_on_allowed_port_quant(frame)
    }

    pub open spec fn allow_outbound_frame(frame: SW::RawEthernetMessage) -> bool 
    {
      Self::hlr_05(frame) || Self::hlr_06(frame) ||
        Self::hlr_13(frame)
    }
    // END MARKER GUMBO METHODS
  }

}
