#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

// This file will not be overwritten if codegen is rerun

use crate::data::*;
use crate::bridge::r2u2_monitor_r2u2_r2u2_api::*;
use crate::component::spec::SPEC;
use r2u2_core;
#[cfg(feature = "sel4")]
#[allow(unused_imports)]
use log::{error, warn, info, debug, trace};

pub struct r2u2_monitor_r2u2_r2u2 {
  pub monitor : r2u2_core::Monitor
}

impl r2u2_monitor_r2u2_r2u2 {
  pub fn new() -> Self 
  {
    Self {
      monitor: r2u2_core::Monitor::default()
    }
  }

  pub fn initialize<API: r2u2_monitor_r2u2_r2u2_Put_Api>(
    &mut self,
    api: &mut r2u2_monitor_r2u2_r2u2_Application_Api<API>) 
  {
    r2u2_core::update_binary_file(&SPEC, &mut self.monitor);
    #[cfg(feature = "sel4")]
    info!("initialized R2U2");
  }

  pub fn timeTriggered<API: r2u2_monitor_r2u2_r2u2_Full_Api>(
    &mut self,
    api: &mut r2u2_monitor_r2u2_r2u2_Application_Api<API>) 
  {
    #[cfg(feature = "sel4")]
    info!("compute entrypoint invoked");
    r2u2_core::load_int_signal(&mut self.monitor, 0, api.get_current_tempWstatus().degrees);
    r2u2_core::load_int_signal(&mut self.monitor, 1, api.get_current_tempWstatus().status as i32);
    r2u2_core::load_int_signal(&mut self.monitor, 2, api.get_upper_desired_tempWstatus().degrees);
    r2u2_core::load_int_signal(&mut self.monitor, 3, api.get_upper_desired_tempWstatus().status as i32);
    r2u2_core::load_int_signal(&mut self.monitor, 4, api.get_lower_desired_tempWstatus().degrees);
    r2u2_core::load_int_signal(&mut self.monitor, 5, api.get_lower_desired_tempWstatus().status as i32);
    r2u2_core::load_int_signal(&mut self.monitor, 6, api.get_monitor_mode() as i32);
    r2u2_core::load_int_signal(&mut self.monitor, 7, api.get_heat_control() as i32);
    
    if r2u2_core::monitor_step(&mut self.monitor){
      // for out in r2u2_core::get_output_buffer(&self.monitor) {
      //     #[cfg(feature = "sel4")]
      //     info!("{}:{},{}", out.spec_str, out.verdict.time, if out.verdict.truth {"T"} else {"F"} );
      // }
      for out in r2u2_core::get_contract_buffer(&self.monitor) {
          #[cfg(feature = "sel4")]
          info!("Contract {} {} at {}", out.spec_str, if out.status == r2u2_core::AGC_VERIFIED {"verified"} else if out.status == r2u2_core::AGC_INVALID {"invalid"} else {"inactive"}, out.time);
      }
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
