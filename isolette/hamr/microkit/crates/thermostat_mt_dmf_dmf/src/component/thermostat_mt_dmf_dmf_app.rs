#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

// This file will not be overwritten if codegen is rerun

use crate::data::*;
use crate::bridge::thermostat_mt_dmf_dmf_api::*;
#[cfg(feature = "sel4")]
#[allow(unused_imports)]
use log::{error, warn, info, debug, trace};

pub struct thermostat_mt_dmf_dmf {
}

impl thermostat_mt_dmf_dmf {
  pub fn new() -> Self 
  {
    Self {
    }
  }

  pub fn initialize<API: thermostat_mt_dmf_dmf_Put_Api>(
    &mut self,
    api: &mut thermostat_mt_dmf_dmf_Application_Api<API>) 
  {
    #[cfg(feature = "sel4")]
    info!("initialize entrypoint invoked");
  }

  pub fn timeTriggered<API: thermostat_mt_dmf_dmf_Full_Api>(
    &mut self,
    api: &mut thermostat_mt_dmf_dmf_Application_Api<API>) 
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
}
