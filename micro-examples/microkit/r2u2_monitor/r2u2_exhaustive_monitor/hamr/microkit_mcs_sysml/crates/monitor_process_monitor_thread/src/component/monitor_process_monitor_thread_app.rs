// This file will not be overwritten if HAMR codegen is rerun

use data::*;
use crate::bridge::monitor_process_monitor_thread_api::*;
use vstd::prelude::*;
// BEGIN MARKER R2U2 MONITOR IMPORT
use super::r2u2_monitor::*;
// END MARKER R2U2 MONITOR IMPORT

verus! {

  pub struct monitor_process_monitor_thread {
    // BEGIN MARKER STATE VARS
    pub sprevious_level: i32,
    // END MARKER STATE VARS
    // BEGIN MARKER R2U2 MONITOR STATE VAR
    pub r2u2_monitor: R2U2Monitor,
    // END MARKER R2U2 MONITOR STATE VAR
  }

  impl monitor_process_monitor_thread {
    pub fn new() -> Self
    {
      Self {
        // BEGIN MARKER STATE VAR INIT
        sprevious_level: 0,
        // END MARKER STATE VAR INIT
        // BEGIN MARKER R2U2 MONITOR STATE VAR INIT
        r2u2_monitor: default_r2u2_monitor(),
        // END MARKER R2U2 MONITOR STATE VAR INIT
      }
    }

    pub fn initialize<API: monitor_process_monitor_thread_Put_Api> (
      &mut self,
      api: &mut monitor_process_monitor_thread_Application_Api<API>)
      ensures
        // PLACEHOLDER MARKER INITIALIZATION ENSURES
    {
      // BEGIN MARKER R2U2 MONITOR INITIALIZE
      load_spec(&mut self.r2u2_monitor);
      // END MARKER R2U2 MONITOR INITIALIZE

      log_info("initialize entrypoint invoked");
    }

    pub fn timeTriggered<API: monitor_process_monitor_thread_Full_Api> (
      &mut self,
      api: &mut monitor_process_monitor_thread_Application_Api<API>)
      requires
        // PLACEHOLDER MARKER TIME TRIGGERED REQUIRES
      ensures
        // PLACEHOLDER MARKER TIME TRIGGERED ENSURES
    {
      let pulse = api.get_pulse();
      let sample = api.get_sample();
      let level = api.get_level();
      let flag = api.get_flag();
      let boolean_value = api.get_boolean_value();
      let character_value = api.get_character_value();
      let signed_8_value = api.get_signed_8_value();
      let signed_16_value = api.get_signed_16_value();
      let unsigned_8_value = api.get_unsigned_8_value();
      let unsigned_16_value = api.get_unsigned_16_value();
      let operating_state = api.get_operating_state();
      let samples = api.get_samples();
      let telemetry = api.get_telemetry();
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

  // BEGIN MARKER GUMBO METHODS
  pub open spec fn isNonNegative(value: i32) -> bool
  {
    value >= 0i32
  }

  pub open spec fn absoluteValue(value: i32) -> i32
  {
    if (value < 0i32) {
      (-value) as i32
    } else {
      value
    }
  }
  // END MARKER GUMBO METHODS

}
