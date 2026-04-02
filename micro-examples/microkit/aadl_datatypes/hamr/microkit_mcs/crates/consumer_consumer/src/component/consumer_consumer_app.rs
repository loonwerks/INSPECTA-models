// This file will not be overwritten if codegen is rerun

use data::*;
use crate::bridge::consumer_consumer_api::*;
use vstd::prelude::*;

verus! {

  pub struct consumer_consumer {
    lastBool: bool,
    lasti64: i64,
    // PLACEHOLDER MARKER STATE VARS
  }

  impl consumer_consumer {
    pub fn new() -> Self
    {
      Self {
        lastBool: false,
        lasti64: 0,
        // PLACEHOLDER MARKER STATE VAR INIT
      }
    }

    pub fn initialize<API: consumer_consumer_Put_Api> (
      &mut self,
      api: &mut consumer_consumer_Application_Api<API>)
      ensures
        // PLACEHOLDER MARKER INITIALIZATION ENSURES
    {
      log_info("initialize entrypoint invoked");

      self.lasti64 = -42;
    }

    pub fn timeTriggered<API: consumer_consumer_Full_Api> (
      &mut self,
      api: &mut consumer_consumer_Application_Api<API>)
      requires
        // PLACEHOLDER MARKER TIME TRIGGERED REQUIRES
      ensures
        // PLACEHOLDER MARKER TIME TRIGGERED ENSURES
    {
      log_info("compute entrypoint invoked");

      if let Some(b) = api.get_myBoolean() {
        log_info_bool(self.lastBool, b);
        //log::info!("{}/{}", self.lastBool, b);
        self.lastBool = b;
      } 

      if let Some(i64) = api.get_myInt64() {
        log_info_i64(self.lasti64, i64);
        //log::info!("{}/{}", self.lasti64, i64);
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
          log_warn_channel(channel)
        }
      }
    }
  }

  #[verifier::external_body]
  pub fn log_info_bool(last: bool, b: bool) {
    log::info!("{}/{}", last, b);
  }

  #[verifier::external_body]
  pub fn log_info_i64(last: i64, b: i64) {
    log::info!("{}/{}", last, b);
  }

  #[verifier::external_body]
  pub fn log_info(msg: &str)
  {
    log::info!("{0}", msg);
  }

  #[verifier::external_body]
  pub fn log_warn_channel(channel: u32)
  {
    log::warn!("Unexpected channel: {0}", channel);
  }

  // PLACEHOLDER MARKER GUMBO METHODS

}
