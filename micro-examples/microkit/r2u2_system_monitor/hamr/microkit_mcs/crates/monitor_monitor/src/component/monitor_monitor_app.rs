// This file will not be overwritten if HAMR codegen is rerun

use data::*;
use crate::bridge::monitor_monitor_api::*;
use vstd::prelude::*;

verus! {

  // BEGIN MARKER R2U2 SPEC
  const SPEC: [u8; include_bytes!("spec.bin").len()] = *include_bytes!("spec.bin");
  // END MARKER R2U2 SPEC

  pub struct monitor_monitor {
    // PLACEHOLDER MARKER STATE VARS,
    // BEGIN MARKER R2U2 MONITOR STATE VAR
    pub r2u2_monitor: r2u2_core::Monitor,
    // END MARKER R2U2 MONITOR STATE VAR
  }

  impl monitor_monitor {
    pub fn new() -> Self
    {
      Self {
        // PLACEHOLDER MARKER STATE VAR INIT
        // BEGIN MARKER R2U2 MONITOR STATE VAR INIT
        r2u2_monitor: r2u2_core::Monitor::default(),
        // END MARKER R2U2 MONITOR STATE VAR INIT
      }
    }

    pub fn initialize<API: monitor_monitor_Put_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>)
      ensures
        // PLACEHOLDER MARKER INITIALIZATION ENSURES
    {
      // BEGIN MARKER R2U2 MONITOR INITIALIZE
      r2u2_core::update_binary_file(&SPEC, &mut self.r2u2_monitor);
      // END MARKER R2U2 MONITOR INITIALIZE

      log_info("initialize entrypoint invoked");
    }

    pub fn timeTriggered<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>)
      requires
        // PLACEHOLDER MARKER TIME TRIGGERED REQUIRES
      ensures
        // PLACEHOLDER MARKER TIME TRIGGERED ENSURES
    {
      // BEGIN MARKER R2U2 MONITOR COMPUTE
      let sent_sample = api.get_sent_sample();
      let observed_sample = api.get_observed_sample();

      r2u2_core::load_bool_signal(&mut self.r2u2_monitor, 0, sent_sample.is_some()); // Loading signal api_sent_sample_nonEmpty into index 0
      r2u2_core::load_bool_signal(&mut self.r2u2_monitor, 1, observed_sample.is_some()); // Loading signal api_observed_sample_nonEmpty into index 1

      r2u2_core::monitor_step(&mut self.r2u2_monitor);
      for out in r2u2_core::get_output_buffer(&self.r2u2_monitor) {
          log::info!("{}:{},{}", out.spec_num, out.verdict.time, if out.verdict.truth {"T"} else {"F"} );
      }
      // END MARKER R2U2 MONITOR COMPUTE

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
