// This file will not be overwritten if HAMR codegen is rerun

use data::*;
use crate::bridge::producer_producer_api::*;
use vstd::prelude::*;

verus! {

  pub struct producer_producer {
    // PLACEHOLDER MARKER STATE VARS
    pub next_sample: i32, // The value sent on the next periodic dispatch.
  }

  impl producer_producer {
    pub fn new() -> Self
    {
      Self {
        // PLACEHOLDER MARKER STATE VAR INIT
        next_sample: 0,
      }
    }

    pub fn initialize<API: producer_producer_Put_Api> (
      &mut self,
      api: &mut producer_producer_Application_Api<API>)
      ensures
        // PLACEHOLDER MARKER INITIALIZATION ENSURES
    {
      log_info("initialize entrypoint invoked");
    }

    pub fn timeTriggered<API: producer_producer_Full_Api> (
      &mut self,
      api: &mut producer_producer_Application_Api<API>)
      requires
        // BEGIN MARKER TIME TRIGGERED REQUIRES
        // assume AADL_Requirement
        //   All outgoing event ports must be empty
        old(api).sample.is_none(),
        // END MARKER TIME TRIGGERED REQUIRES
      ensures
        // PLACEHOLDER MARKER TIME TRIGGERED ENSURES
    {
      // Event alert ports are emitted only when their mapped guarantee is false.
      if api.get_sample_alert() {
        log_warn("Oh no! Its been too long since I dispatched a message...doing it now!");
        api.put_sample(self.next_sample);
        self.next_sample = self.next_sample.wrapping_add(1);
        return
      }

      // Send for three dispatches, then remain silent for three dispatches.
      // The silent run violates F[0,2](sample_nonEmpty), allowing the consumer
      // to demonstrate both true and false R2U2 verdicts.
      let sample = self.next_sample;
      let phase = sample % 6;
      if phase < 3 {
        api.put_sample(sample);
        log_info("Sent sample!");
      }
      self.next_sample = sample.wrapping_add(1);
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
