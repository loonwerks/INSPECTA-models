// This file will not be overwritten if HAMR codegen is rerun

use data::*;
use crate::bridge::consumer_consumer_api::*;
use vstd::prelude::*;
// PLACEHOLDER MARKER R2U2 MONITOR IMPORT

verus! {

  pub struct consumer_consumer {
    // PLACEHOLDER MARKER STATE VARS
    // PLACEHOLDER MARKER R2U2 MONITOR STATE VAR
  }

  impl consumer_consumer {
    pub fn new() -> Self
    {
      Self {
        // PLACEHOLDER MARKER STATE VAR INIT
        // PLACEHOLDER MARKER R2U2 MONITOR STATE VAR INIT
      }
    }

    pub fn initialize<API: consumer_consumer_Put_Api> (
      &mut self,
      api: &mut consumer_consumer_Application_Api<API>)
      ensures
        // PLACEHOLDER MARKER INITIALIZATION ENSURES
    {
      // PLACEHOLDER MARKER R2U2 MONITOR INITIALIZE

      log_info("initialize entrypoint invoked");
    }

    pub fn timeTriggered<API: consumer_consumer_Full_Api> (
      &mut self,
      api: &mut consumer_consumer_Application_Api<API>)
      requires
        // BEGIN MARKER TIME TRIGGERED REQUIRES
        // assume AADL_Requirement
        //   All outgoing event ports must be empty
        old(api).observed_sample.is_none(),
        // END MARKER TIME TRIGGERED REQUIRES
      ensures
        // PLACEHOLDER MARKER TIME TRIGGERED ENSURES
    {
      // Boolean event-data alert ports carry their mapped guarantee's verdict.
      if let Some(verdict) = api.get_alert_flag() {
        if verdict {
          log_info("R2U2 reports samples_match_until_producer_pauses as true.");
        } else {
          // Do not process another sample while the monitor reports a violation.
          log_warn("R2U2 reports samples_match_until_producer_pauses as false; skipping this dispatch.");
          return
        }
      }

      let sample = api.get_sample();
      match sample {
        Some(value) => {
          // Report exactly what was consumed so the independent monitor can
          // compare producer and consumer observations.
          api.put_observed_sample(value);
          log_info("Received sample.");
        },
        None => log_info("no sample received this dispatch"),
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
  pub fn log_info(msg: &str)
  {
    log::info!("{0}", msg);
  }

  #[verifier::external_body]
  pub fn log_warn(msg: &str)
  {
    log::warn!("{0}", msg);
  }

  #[verifier::external_body]
  pub fn log_warn_channel(channel: u32)
  {
    log::warn!("Unexpected channel: {0}", channel);
  }

  // PLACEHOLDER MARKER GUMBO METHODS

}
