#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

// This file will not be overwritten if codegen is rerun

use data::*;
use crate::bridge::seL4_ArduPilot_ArduPilot_api::*;
#[cfg(feature = "sel4")]
#[allow(unused_imports)]
use log::{error, warn, info, debug, trace};

mod packets;

pub struct seL4_ArduPilot_ArduPilot {
  pub index : usize
}

impl seL4_ArduPilot_ArduPilot {
  pub fn new() -> Self 
  {
    Self {
      index: 0
    }
  }

  pub fn initialize<API: seL4_ArduPilot_ArduPilot_Put_Api> (
    &mut self,
    api: &mut seL4_ArduPilot_ArduPilot_Application_Api<API>) 
  {
    #[cfg(feature = "sel4")]
    info!("initialize entrypoint invoked");
  }

  pub fn timeTriggered<API: seL4_ArduPilot_ArduPilot_Full_Api> (
    &mut self,
    api: &mut seL4_ArduPilot_ArduPilot_Application_Api<API>) 
  {
    // Tx0
    if packets::MESSAGES[self.index].len() < SW::SW_RawEthernetMessage_DIM_0{
      let mut tx0_message: SW::RawEthernetMessage = [0; SW::SW_RawEthernetMessage_DIM_0];
      tx0_message[0..packets::MESSAGES[self.index].len()].copy_from_slice(&packets::MESSAGES[self.index]);
      api.put_EthernetFramesTx0(tx0_message);
    }

    // Tx1
    if self.index + 1 < packets::MESSAGES.len(){
      if packets::MESSAGES[self.index+1].len() < SW::SW_RawEthernetMessage_DIM_0{
        let mut tx1_message: SW::RawEthernetMessage = [0; SW::SW_RawEthernetMessage_DIM_0];
        tx1_message[0..packets::MESSAGES[self.index+1].len()].copy_from_slice(&packets::MESSAGES[self.index+1]);
        api.put_EthernetFramesTx1(tx1_message);
      }
    }

    // Tx2
    if self.index + 2 < packets::MESSAGES.len(){
      if packets::MESSAGES[self.index+2].len() < SW::SW_RawEthernetMessage_DIM_0{
        let mut tx2_message: SW::RawEthernetMessage = [0; SW::SW_RawEthernetMessage_DIM_0];
        tx2_message[0..packets::MESSAGES[self.index+2].len()].copy_from_slice(&packets::MESSAGES[self.index+2]);
        api.put_EthernetFramesTx2(tx2_message);
      }
    }

    // Tx3
    if self.index + 3 < packets::MESSAGES.len(){
      if packets::MESSAGES[self.index+3].len() < SW::SW_RawEthernetMessage_DIM_0{
        let mut tx3_message: SW::RawEthernetMessage = [0; SW::SW_RawEthernetMessage_DIM_0];
        tx3_message[0..packets::MESSAGES[self.index+3].len()].copy_from_slice(&packets::MESSAGES[self.index+3]);
        api.put_EthernetFramesTx3(tx3_message);
      }
    }

    if self.index + 4 < packets::MESSAGES.len(){
      self.index += 4;
    } else {
      self.index = 0;
    }

    // Rx0
    if let Some(frame) = api.get_EthernetFramesRx0() {
      #[cfg(feature = "sel4")]
      info!("Rx0 successfully received packet");
    }

    // Rx1
    if let Some(frame) = api.get_EthernetFramesRx1() {
      #[cfg(feature = "sel4")]
      info!("Rx1 successfully received packet");
    }

    // Rx2
    if let Some(frame) = api.get_EthernetFramesRx2() {
      #[cfg(feature = "sel4")]
      info!("Rx2 successfully received packet");
    }

    // Rx3
    if let Some(frame) = api.get_EthernetFramesRx3() {
      #[cfg(feature = "sel4")]
      info!("Rx3 successfully received packet");
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
