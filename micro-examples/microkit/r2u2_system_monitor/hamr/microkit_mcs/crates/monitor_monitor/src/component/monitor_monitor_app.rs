// This file will not be overwritten if HAMR codegen is rerun

use data::*;
use crate::bridge::monitor_monitor_api::*;
use vstd::prelude::*;

verus! {

  pub struct monitor_monitor {
    // PLACEHOLDER MARKER STATE VARS,
    // BEGIN MARKER R2U2 MONITOR STATE VAR
    pub r2u2_monitor: R2U2Monitor,
    // END MARKER R2U2 MONITOR STATE VAR
  }

  impl monitor_monitor {
    pub fn new() -> Self
    {
      Self {
        // PLACEHOLDER MARKER STATE VAR INIT
        // BEGIN MARKER R2U2 MONITOR STATE VAR INIT
        r2u2_monitor: default_r2u2_monitor(),
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
      load_spec(&mut self.r2u2_monitor);
      // END MARKER R2U2 MONITOR INITIALIZE

      log_info("initialize entrypoint invoked");
    }

    // BEGIN MARKER R2U2 MONITOR PRE TIME TRIGGERED
    #[verifier::external_body]
    pub fn pre_timeTriggered<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &monitor_monitor_Application_Api<API>)
    {
      // HAMR-generated R2U2 hook. Do not edit; changes are replaced during regeneration.
      let sent_sample = api.peek_sent_sample();
      let observed_sample = api.peek_observed_sample();

      r2u2_core::load_bool_signal(&mut self.r2u2_monitor, 0, sent_sample.is_some()); // Loading signal api_sent_sample_nonEmpty into index 0
      r2u2_core::load_bool_signal(&mut self.r2u2_monitor, 1, observed_sample.is_some()); // Loading signal api_observed_sample_nonEmpty into index 1
      r2u2_core::load_int_signal(&mut self.r2u2_monitor, 2, observed_sample.unwrap_or_default().into()); // Loading signal api_observed_sample into index 2
      r2u2_core::load_int_signal(&mut self.r2u2_monitor, 3, sent_sample.unwrap_or_default().into()); // Loading signal api_sent_sample into index 3
    }
    // END MARKER R2U2 MONITOR PRE TIME TRIGGERED

    pub fn timeTriggered<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>)
      requires
        // PLACEHOLDER MARKER TIME TRIGGERED REQUIRES
      ensures
        // PLACEHOLDER MARKER TIME TRIGGERED ENSURES
    {
      log_info("compute entrypoint invoked");
    }

    // BEGIN MARKER R2U2 MONITOR POST TIME TRIGGERED
    #[verifier::external_body]
    pub fn post_timeTriggered<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>)
    {
      // HAMR-generated R2U2 hook. Do not edit; changes are replaced during regeneration.
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

  // BEGIN MARKER R2U2 SPEC
  #[verifier::external_body]
  pub struct R2U2Monitor { inner: r2u2_core::Monitor }

  #[verifier::external]
  impl core::ops::Deref for R2U2Monitor {
    type Target = r2u2_core::Monitor;
    fn deref(&self) -> &Self::Target { &self.inner }
  }

  #[verifier::external]
  impl core::ops::DerefMut for R2U2Monitor {
    fn deref_mut(&mut self) -> &mut Self::Target { &mut self.inner }
  }

  #[verifier::external_body]
  fn default_r2u2_monitor() -> R2U2Monitor {
    R2U2Monitor { inner: r2u2_core::Monitor::default() }
  }

  #[verifier::external_body]
  fn load_spec(monitor: &mut R2U2Monitor) {
    r2u2_core::update_binary_file(include_bytes!("spec.bin"), monitor);
  }
  // END MARKER R2U2 SPEC

}
