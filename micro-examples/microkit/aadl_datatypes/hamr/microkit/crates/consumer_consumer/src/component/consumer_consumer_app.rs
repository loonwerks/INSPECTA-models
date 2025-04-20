#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

// This file will not be overwritten if codegen is rerun

use crate::data::*;
use crate::bridge::consumer_consumer_api::*;
#[cfg(feature = "sel4")]
#[allow(unused_imports)]
use log::{error, warn, info, debug, trace};

pub struct consumer_consumer {
  lastBool: bool,
  lasti64: i64
}

impl consumer_consumer {
  pub fn new() -> Self 
  {
    Self {
      lastBool: false,
      lasti64: 0
    }
  }

  pub fn initialize<API: consumer_consumer_Put_Api>(
    &mut self,
    api: &mut consumer_consumer_Application_Api<API>) 
  {
    #[cfg(feature = "sel4")]
    info!("initialize entrypoint invoked");

    self.lasti64 = -42;
  }

  pub fn timeTriggered<API: consumer_consumer_Full_Api>(
    &mut self,
    api: &mut consumer_consumer_Application_Api<API>) 
  {
    #[cfg(feature = "sel4")]
    info!("compute entrypoint invoked");

    let b = api.get_myBoolean().unwrap();
    info!("{}/{}", self.lastBool, b);

    let i64 = api.get_myInt64().unwrap();
    info!("{}/{}", self.lasti64, i64);

    self.lastBool = b;
    self.lasti64 = i64;
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
