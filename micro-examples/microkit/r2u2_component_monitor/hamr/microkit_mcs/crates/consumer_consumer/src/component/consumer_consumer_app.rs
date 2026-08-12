// This file will not be overwritten if HAMR codegen is rerun

use data::*;
use crate::bridge::consumer_consumer_api::*;
use vstd::prelude::*;
// BEGIN MARKER R2U2 MONITOR IMPORT
use super::r2u2_monitor::*;
// END MARKER R2U2 MONITOR IMPORT

verus! {

  pub struct consumer_consumer {
    // PLACEHOLDER MARKER STATE VARS,
    // BEGIN MARKER R2U2 MONITOR STATE VAR
    pub r2u2_monitor: R2U2Monitor,
    // END MARKER R2U2 MONITOR STATE VAR
  }

  impl consumer_consumer {
    pub fn new() -> Self
    {
      Self {
        // PLACEHOLDER MARKER STATE VAR INIT
        // BEGIN MARKER R2U2 MONITOR STATE VAR INIT
        r2u2_monitor: default_r2u2_monitor(),
        // END MARKER R2U2 MONITOR STATE VAR INIT
      }
    }

    pub fn initialize<API: consumer_consumer_Put_Api> (
      &mut self,
      api: &mut consumer_consumer_Application_Api<API>)
      ensures
        // PLACEHOLDER MARKER INITIALIZATION ENSURES
    {
      // BEGIN MARKER R2U2 MONITOR INITIALIZE
      load_spec(&mut self.r2u2_monitor);
      // END MARKER R2U2 MONITOR INITIALIZE

      log_info("initialize entrypoint invoked");
    }

    pub fn timeTriggered<API: consumer_consumer_Full_Api> (
      &mut self,
      api: &mut consumer_consumer_Application_Api<API>)
      requires
        // PLACEHOLDER MARKER TIME TRIGGERED REQUIRES
      ensures
        // PLACEHOLDER MARKER TIME TRIGGERED ENSURES
    {
      let sample = api.get_sample();
      log_info("compute entrypoint invoked");
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
