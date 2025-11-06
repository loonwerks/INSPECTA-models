// This file will not be overwritten if codegen is rerun

use data::*;
use crate::bridge::consumer_consumer_api::*;

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
    log::info!("initialize entrypoint invoked");

    self.lasti64 = -42;
  }

  pub fn timeTriggered<API: consumer_consumer_Full_Api>(
    &mut self,
    api: &mut consumer_consumer_Application_Api<API>) 
  {
    #[cfg(feature = "sel4")]
    log::info!("compute entrypoint invoked");

    if let Some(b) = api.get_myBoolean() {
      log::info!("{}/{}", self.lastBool, b);
      self.lastBool = b;
    } 

    if let Some(i64) = api.get_myInt64() {
      log::info!("{}/{}", self.lasti64, i64);
      self.lasti64 = i64;
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
        log::warn!("Unexpected channel {}", channel)
      }
    }
  }
}
