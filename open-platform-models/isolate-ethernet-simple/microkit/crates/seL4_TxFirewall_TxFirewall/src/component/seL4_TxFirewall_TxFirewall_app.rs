#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

// This file will not be overwritten if codegen is rerun

use crate::data::*;
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
        // guarantee tx
        api.EthernetFramesTxIn0.is_some() ==>
          (api.EthernetFramesTxOut0.is_some() ==> Self::should_allow_outbound_frame_tx(api.EthernetFramesTxIn0.unwrap(),true)) &&
            (api.EthernetFramesTxOut0.is_none() ==> Self::should_allow_outbound_frame_tx(api.EthernetFramesTxIn0.unwrap(),false))
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
