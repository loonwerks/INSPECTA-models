#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

// This file will not be overwritten if codegen is rerun

use crate::data::*;
use crate::bridge::consumer_consumer_api::*;
#[cfg(feature = "sel4")]
#[allow(unused_imports)]
use log::{error, warn, info, debug, trace};

pub struct consumer_consumer {
}

impl consumer_consumer {
  pub const fn new() -> Self 
  {
    Self {
    }
  }

  pub fn initialize<API: consumer_consumer_Put_Api>(
    &mut self,
    api: &mut consumer_consumer_Application_Api<API>) 
  {
    #[cfg(feature = "sel4")]
    info!("initialize entrypoint invoked");
  }

  pub fn timeTriggered<API: consumer_consumer_Full_Api>(
    &mut self,
    api: &mut consumer_consumer_Application_Api<API>) 
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
