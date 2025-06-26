#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

// This file will not be overwritten if codegen is rerun

use data::*;
use crate::bridge::seL4_TxFirewall_TxFirewall_api::*;
#[cfg(feature = "sel4")]
#[allow(unused_imports)]
use log::{error, warn, info, debug, trace};
use vstd::prelude::*;

verus! {

  pub struct seL4_TxFirewall_TxFirewall {
  }

  impl seL4_TxFirewall_TxFirewall {
    pub fn new() -> Self 
    {
      Self {
      }
    }

    pub fn initialize<API: seL4_TxFirewall_TxFirewall_Put_Api>(
      &mut self,
      api: &mut seL4_TxFirewall_TxFirewall_Application_Api<API>) 
    {
      #[cfg(feature = "sel4")]
      info!("initialize entrypoint invoked");
    }

    pub fn timeTriggered<API: seL4_TxFirewall_TxFirewall_Full_Api>(
      &mut self,
      api: &mut seL4_TxFirewall_TxFirewall_Application_Api<API>)
      requires
        // BEGIN MARKER TIME TRIGGERED REQUIRES
        // assume AADL_Requirement
        //   All outgoing event ports must be empty
        old(api).EthernetFramesTxOut0.is_none(),
        old(api).EthernetFramesTxOut1.is_none(),
        old(api).EthernetFramesTxOut2.is_none(),
        old(api).EthernetFramesTxOut3.is_none()
        // END MARKER TIME TRIGGERED REQUIRES
      ensures
        // BEGIN MARKER TIME TRIGGERED ENSURES
        // guarantee hlr_07_tx0_can_send_valid_arp
        api.EthernetFramesTxIn0.is_some() && Self::valid_arp(api.EthernetFramesTxIn0.unwrap()) ==>
          api.EthernetFramesTxOut0.is_some() &&
            (api.EthernetFramesTxIn0.unwrap() == api.EthernetFramesTxOut0.unwrap().message) &&
            Self::valid_output_arp_size(api.EthernetFramesTxOut0.unwrap()),
        // guarantee hlr_12_tx0_can_send_valid_ipv4
        api.EthernetFramesTxIn0.is_some() && Self::valid_ipv4(api.EthernetFramesTxIn0.unwrap()) ==>
          api.EthernetFramesTxOut0.is_some() &&
            (api.EthernetFramesTxIn0.unwrap() == api.EthernetFramesTxOut0.unwrap().message) &&
            Self::valid_output_ipv4_size(api.EthernetFramesTxIn0.unwrap(),api.EthernetFramesTxOut0.unwrap()),
        // guarantee hlr_14_tx0_disallow
        api.EthernetFramesTxIn0.is_some() && !(Self::allow_outbound_frame(api.EthernetFramesTxIn0.unwrap())) ==>
          api.EthernetFramesTxOut0.is_none(),
        // guarantee hlr_16_tx0_no_input
        !(api.EthernetFramesTxIn0.is_some()) ==> api.EthernetFramesTxOut0.is_none(),
        // guarantee hlr_07_tx1_can_send_valid_arp
        api.EthernetFramesTxIn1.is_some() && Self::valid_arp(api.EthernetFramesTxIn1.unwrap()) ==>
          api.EthernetFramesTxOut1.is_some() &&
            (api.EthernetFramesTxIn1.unwrap() == api.EthernetFramesTxOut1.unwrap().message) &&
            Self::valid_output_arp_size(api.EthernetFramesTxOut1.unwrap()),
        // guarantee hlr_12_tx1_can_send_valid_ipv4
        api.EthernetFramesTxIn1.is_some() && Self::valid_ipv4(api.EthernetFramesTxIn1.unwrap()) ==>
          api.EthernetFramesTxOut1.is_some() &&
            (api.EthernetFramesTxIn1.unwrap() == api.EthernetFramesTxOut1.unwrap().message) &&
            Self::valid_output_ipv4_size(api.EthernetFramesTxIn1.unwrap(),api.EthernetFramesTxOut1.unwrap()),
        // guarantee hlr_14_tx1_disallow
        api.EthernetFramesTxIn1.is_some() && !(Self::allow_outbound_frame(api.EthernetFramesTxIn1.unwrap())) ==>
          api.EthernetFramesTxOut1.is_none(),
        // guarantee hlr_16_tx1_no_input
        !(api.EthernetFramesTxIn1.is_some()) ==> api.EthernetFramesTxOut1.is_none(),
        // guarantee hlr_07_tx2_can_send_valid_arp
        api.EthernetFramesTxIn2.is_some() && Self::valid_arp(api.EthernetFramesTxIn2.unwrap()) ==>
          api.EthernetFramesTxOut2.is_some() &&
            (api.EthernetFramesTxIn2.unwrap() == api.EthernetFramesTxOut2.unwrap().message) &&
            Self::valid_output_arp_size(api.EthernetFramesTxOut2.unwrap()),
        // guarantee hlr_12_tx2_can_send_valid_ipv4
        api.EthernetFramesTxIn2.is_some() && Self::valid_ipv4(api.EthernetFramesTxIn2.unwrap()) ==>
          api.EthernetFramesTxOut2.is_some() &&
            (api.EthernetFramesTxIn2.unwrap() == api.EthernetFramesTxOut2.unwrap().message) &&
            Self::valid_output_ipv4_size(api.EthernetFramesTxIn2.unwrap(),api.EthernetFramesTxOut2.unwrap()),
        // guarantee hlr_14_tx2_disallow
        api.EthernetFramesTxIn2.is_some() && !(Self::allow_outbound_frame(api.EthernetFramesTxIn2.unwrap())) ==>
          api.EthernetFramesTxOut2.is_none(),
        // guarantee hlr_16_tx2_no_input
        !(api.EthernetFramesTxIn2.is_some()) ==> api.EthernetFramesTxOut2.is_none(),
        // guarantee hlr_07_tx3_can_send_valid_arp
        api.EthernetFramesTxIn3.is_some() && Self::valid_arp(api.EthernetFramesTxIn3.unwrap()) ==>
          api.EthernetFramesTxOut3.is_some() &&
            (api.EthernetFramesTxIn3.unwrap() == api.EthernetFramesTxOut3.unwrap().message) &&
            Self::valid_output_arp_size(api.EthernetFramesTxOut3.unwrap()),
        // guarantee hlr_12_tx3_can_send_valid_ipv4
        api.EthernetFramesTxIn3.is_some() && Self::valid_ipv4(api.EthernetFramesTxIn3.unwrap()) ==>
          api.EthernetFramesTxOut3.is_some() &&
            (api.EthernetFramesTxIn3.unwrap() == api.EthernetFramesTxOut3.unwrap().message) &&
            Self::valid_output_ipv4_size(api.EthernetFramesTxIn3.unwrap(),api.EthernetFramesTxOut3.unwrap()),
        // guarantee hlr_14_tx3_disallow
        api.EthernetFramesTxIn3.is_some() && !(Self::allow_outbound_frame(api.EthernetFramesTxIn3.unwrap())) ==>
          api.EthernetFramesTxOut3.is_none(),
        // guarantee hlr_16_tx3_no_input
        !(api.EthernetFramesTxIn3.is_some()) ==> api.EthernetFramesTxOut3.is_none()
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

    pub open spec fn ipv4_length(frame: SW::RawEthernetMessage) -> u16 
    {
      Self::two_bytes_to_u16(frame[16],frame[17])
    }

    pub open spec fn valid_ipv4_length(frame: SW::RawEthernetMessage) -> bool 
    {
      Self::ipv4_length(frame) <= 9000u16
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

    pub open spec fn valid_ipv6(frame: SW::RawEthernetMessage) -> bool 
    {
      Self::frame_is_wellformed_eth2(frame) && Self::frame_has_ipv6(frame)
    }

    pub open spec fn valid_arp(frame: SW::RawEthernetMessage) -> bool 
    {
      Self::frame_is_wellformed_eth2(frame) && Self::frame_has_arp(frame) &&
        Self::wellformed_arp_frame(frame)
    }

    pub open spec fn valid_ipv4(frame: SW::RawEthernetMessage) -> bool 
    {
      Self::frame_is_wellformed_eth2(frame) && Self::frame_has_ipv4(frame) &&
        Self::wellformed_ipv4_frame(frame)
    }

    pub open spec fn valid_output_arp_size(output: SW::SizedEthernetMessage_Impl) -> bool 
    {
      output.len() == 64u16
    }

    pub open spec fn valid_output_ipv4_size(
      input: SW::RawEthernetMessage,
      output: SW::SizedEthernetMessage_Impl) -> bool 
    {
      output.len() == Self::ipv4_length(input) + 14u16
    }

    pub open spec fn allow_outbound_frame(frame: SW::RawEthernetMessage) -> bool 
    {
      Self::valid_arp(frame) || Self::valid_ipv4(frame)
    }
    // END MARKER GUMBO METHODS
  }

}
