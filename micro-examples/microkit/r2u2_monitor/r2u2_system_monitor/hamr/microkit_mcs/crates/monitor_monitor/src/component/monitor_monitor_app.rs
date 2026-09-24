// This file will not be overwritten if HAMR codegen is rerun

use data::*;
use crate::bridge::monitor_monitor_api::*;
use vstd::prelude::*;

verus! {

  pub struct monitor_monitor {
    // PLACEHOLDER MARKER STATE VARS
  }

  impl monitor_monitor {
    pub fn new() -> Self
    {
      Self {
        // PLACEHOLDER MARKER STATE VAR INIT
      }
    }

    pub fn initialize<API: monitor_monitor_Put_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>)
      ensures
        // PLACEHOLDER MARKER INITIALIZATION ENSURES
    {
      log_info("initialize entrypoint invoked");
    }

    pub fn timeTriggered<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>)
      requires
        // BEGIN MARKER TIME TRIGGERED REQUIRES
        // assume AADL_Requirement
        //   All outgoing event ports must be empty
        old(api).alert_flag.is_none(),
        // END MARKER TIME TRIGGERED REQUIRES
      ensures
        // BEGIN MARKER TIME TRIGGERED ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).alert_flag == final(api).alert_flag,
        // END MARKER TIME TRIGGERED ENSURES
    {
      let sent_sample = api.get_sent_sample();
      let observed_sample = api.get_observed_sample();
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

    /// Optional implementation placeholder for the R2U2 verdict of monitored property `samples_match_until_producer_pauses`.
    pub fn handle_samples_match_until_producer_pauses_verdict<API: monitor_monitor_Full_Api> (
      &mut self,
      api: &mut monitor_monitor_Application_Api<API>,
      verdict: Option<bool>)
      ensures
        // BEGIN MARKER R2U2 samples_match_until_producer_pauses VERDICT ENSURES
        // guarantee Monitor_Requirement
        //   Alert ports are reserved for the monitor
        old(api).alert_flag == final(api).alert_flag,
        // END MARKER R2U2 samples_match_until_producer_pauses VERDICT ENSURES
    {
      // This verdict is already mapped to monitor-owned alert port
      // alert_flag. Do not write this port from this callback.
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
