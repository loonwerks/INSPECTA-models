#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

// This file will not be overwritten if codegen is rerun

use data::*;
use crate::bridge::seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_api::*;
#[cfg(feature = "sel4")]
#[allow(unused_imports)]
use log::{error, warn, info, debug, trace};

mod packets;

pub struct seL4_LowLevelEthernetDriver_LowLevelEthernetDriver {
  pub index: usize
}

impl seL4_LowLevelEthernetDriver_LowLevelEthernetDriver {
  pub fn new() -> Self 
  {
    Self {
      index: 0
    }
  }

  pub fn initialize<API: seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Put_Api> (
    &mut self,
    api: &mut seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Application_Api<API>) 
  {
    #[cfg(feature = "sel4")]
    info!("initialize entrypoint invoked");
  }

  pub fn timeTriggered<API: seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Full_Api> (
    &mut self,
    api: &mut seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Application_Api<API>) 
  {
    // Rx0
    if packets::MESSAGES[self.index].len() < SW::SW_RawEthernetMessage_DIM_0{
      let mut rx0_message: SW::RawEthernetMessage = [0; SW::SW_RawEthernetMessage_DIM_0];
      rx0_message[0..packets::MESSAGES[self.index].len()].copy_from_slice(&packets::MESSAGES[self.index]);
      api.put_EthernetFramesRx0(rx0_message);
    }

    // Rx1
    if self.index + 1 < packets::MESSAGES.len(){
      if packets::MESSAGES[self.index+1].len() < SW::SW_RawEthernetMessage_DIM_0{
        let mut rx1_message: SW::RawEthernetMessage = [0; SW::SW_RawEthernetMessage_DIM_0];
        rx1_message[0..packets::MESSAGES[self.index+1].len()].copy_from_slice(&packets::MESSAGES[self.index+1]);
        api.put_EthernetFramesRx1(rx1_message);
      }
    }

    // Rx2
    if self.index + 2 < packets::MESSAGES.len(){
      if packets::MESSAGES[self.index+2].len() < SW::SW_RawEthernetMessage_DIM_0{
        let mut rx2_message: SW::RawEthernetMessage = [0; SW::SW_RawEthernetMessage_DIM_0];
        rx2_message[0..packets::MESSAGES[self.index+2].len()].copy_from_slice(&packets::MESSAGES[self.index+2]);
        api.put_EthernetFramesRx2(rx2_message);
      }
    }

    // Rx3
    if self.index + 3 < packets::MESSAGES.len(){
      if packets::MESSAGES[self.index+3].len() < SW::SW_RawEthernetMessage_DIM_0{
        let mut rx3_message: SW::RawEthernetMessage = [0; SW::SW_RawEthernetMessage_DIM_0];
        rx3_message[0..packets::MESSAGES[self.index+3].len()].copy_from_slice(&packets::MESSAGES[self.index+3]);
        api.put_EthernetFramesRx3(rx3_message);
      }
    }

    if self.index + 4 < packets::MESSAGES.len(){
      self.index += 4;
    } else {
      self.index = 0;
    }

    // print Tx0
    if let Some(frame) = api.get_EthernetFramesTx0() {
      #[cfg(feature = "sel4")]
      info!("Tx0 successfully received packet");
    }

    // print Tx1
    if let Some(frame) = api.get_EthernetFramesTx1() {
      #[cfg(feature = "sel4")]
      info!("Tx1 successfully received packet");
    }

    // print Tx2
    if let Some(frame) = api.get_EthernetFramesTx2() {
      #[cfg(feature = "sel4")]
      info!("Tx2 successfully received packet");
    }

    // print Tx3
    if let Some(frame) = api.get_EthernetFramesTx3() {
      #[cfg(feature = "sel4")]
      info!("Tx3 successfully received packet");
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
}
