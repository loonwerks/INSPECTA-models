// This file will not be overwritten if HAMR codegen is rerun

use data::*;
use crate::bridge::consumer_consumer_api::*;
use vstd::prelude::*;

verus! {

  // BEGIN MARKER R2U2 SPEC
  const SPEC: [u8; include_bytes!("spec.bin").len()] = *include_bytes!("spec.bin");
  // END MARKER R2U2 SPEC

  pub struct consumer_consumer {
    // PLACEHOLDER MARKER STATE VARS,
    // BEGIN MARKER R2U2 MONITOR STATE VAR
    pub r2u2_monitor: r2u2_core::Monitor,
    // END MARKER R2U2 MONITOR STATE VAR
  }

  impl consumer_consumer {
    pub fn new() -> Self
    {
      Self {
        // PLACEHOLDER MARKER STATE VAR INIT
        // BEGIN MARKER R2U2 MONITOR STATE VAR INIT
        r2u2_monitor: r2u2_core::Monitor::default(),
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
      r2u2_core::update_binary_file(&SPEC, &mut self.r2u2_monitor);
      // END MARKER R2U2 MONITOR INITIALIZE

      log_info("initialize entrypoint invoked");
    }

    // BEGIN MARKER R2U2 MONITOR PRE TIME TRIGGERED
    pub fn pre_timeTriggered<API: consumer_consumer_Full_Api> (
      &mut self,
      api: &consumer_consumer_Application_Api<API>)
    {
      let sample = api.peek_sample();

      r2u2_core::load_bool_signal(&mut self.r2u2_monitor, 0, sample.is_some()); // Loading signal api_sample_nonEmpty into index 0
    }
    // END MARKER R2U2 MONITOR PRE TIME TRIGGERED

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

    // BEGIN MARKER R2U2 MONITOR POST TIME TRIGGERED
    pub fn post_timeTriggered<API: consumer_consumer_Full_Api> (
      &mut self,
      api: &mut consumer_consumer_Application_Api<API>)
    {


      r2u2_core::monitor_step(&mut self.r2u2_monitor);
      for out in r2u2_core::get_output_buffer(&self.r2u2_monitor) {
          log::info!("{}:{},{}", out.spec_num, out.verdict.time, if out.verdict.truth {"T"} else {"F"} );
      }
    }
    // END MARKER R2U2 MONITOR POST TIME TRIGGERED

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
