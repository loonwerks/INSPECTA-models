#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

// This file will not be overwritten if codegen is rerun

use data::*;
use crate::bridge::seL4_R2U2Monitor_R2U2Monitor_api::*;
use crate::component::spec::SPEC;
use fixedstr::ztr64;
#[cfg(feature = "sel4")]
#[allow(unused_imports)]
use log::{error, warn, info, debug, trace};

pub struct seL4_R2U2Monitor_R2U2Monitor {
  pub monitor : r2u2_core::Monitor,
  pub message : ztr64
}

impl seL4_R2U2Monitor_R2U2Monitor {
  pub fn new() -> Self 
  {
    Self {
      monitor: r2u2_core::Monitor::default(),
      message: ztr64::from("")
    }
  }

  pub fn initialize<API: seL4_R2U2Monitor_R2U2Monitor_Put_Api> (
    &mut self,
    api: &mut seL4_R2U2Monitor_R2U2Monitor_Application_Api<API>) 
  {
    r2u2_core::update_binary_file(&SPEC, &mut self.monitor);
    #[cfg(feature = "sel4")]
    info!("initialize entrypoint invoked");
  }

  pub fn timeTriggered<API: seL4_R2U2Monitor_R2U2Monitor_Full_Api> (
    &mut self,
    api: &mut seL4_R2U2Monitor_R2U2Monitor_Application_Api<API>) 
  {
    r2u2_core::load_bool_signal(&mut self.monitor, 0, api.get_EthernetFramesRxDriver0().is_some());
    r2u2_core::load_bool_signal(&mut self.monitor, 1, api.get_EthernetFramesRxDriver1().is_some());
    r2u2_core::load_bool_signal(&mut self.monitor, 2, api.get_EthernetFramesRxDriver2().is_some());
    r2u2_core::load_bool_signal(&mut self.monitor, 3, api.get_EthernetFramesRxDriver3().is_some());
    r2u2_core::load_bool_signal(&mut self.monitor, 4, api.get_EthernetFramesRxArduPilot0().is_some());
    r2u2_core::load_bool_signal(&mut self.monitor, 5, api.get_EthernetFramesRxArduPilot1().is_some());
    r2u2_core::load_bool_signal(&mut self.monitor, 6, api.get_EthernetFramesRxArduPilot2().is_some());
    r2u2_core::load_bool_signal(&mut self.monitor, 7, api.get_EthernetFramesRxArduPilot3().is_some());
    
    if r2u2_core::monitor_step(&mut self.monitor){
      for out in r2u2_core::get_output_buffer(&self.monitor) {
        if out.spec_num == 0 {
          if out.verdict.truth{
            self.message = ztr64::from("No attacks on incoming ports.");
          } else {
            self.message = ztr64::from("Some malicious activity detected.");
          }
        } else if out.spec_num == 1 {
          if out.verdict.truth{
            self.message = ztr64::from("High malicious activity detected!");
          }
        }
      }
      #[cfg(feature = "sel4")]
      info!("{}", self.message);
    } else {
      #[cfg(feature = "sel4")]
      error!("Unexpected R2U2 error!")
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
