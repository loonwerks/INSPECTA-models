// This file will not be overwritten if HAMR codegen is rerun

use data::*;
use crate::bridge::producer_producer_api::*;
use vstd::prelude::*;

verus! {

  pub struct producer_producer {
    // PLACEHOLDER MARKER STATE VARS
    // Position in the 12-dispatch demonstration cycle and the next payload.
    pub dispatch_count: u8,
    pub next_sample: i32,
  }

  impl producer_producer {
    pub fn new() -> Self
    {
      Self {
        // PLACEHOLDER MARKER STATE VAR INIT
        dispatch_count: 0,
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
        // PLACEHOLDER MARKER TIME TRIGGERED REQUIRES
      ensures
        // PLACEHOLDER MARKER TIME TRIGGERED ENSURES
    {
      // Phases 0..2 form a short, satisfying burst followed by a pause at 3.
      // Phases 4..10 form a seven-sample burst, exceeding the U[0,5] bound,
      // followed by another pause at 11.  The cycle therefore demonstrates
      // both true and false R2U2 verdicts without changing the monitor.
      let phase = self.dispatch_count;
      if phase != 3 && phase != 11 {
        let sample = self.next_sample;
        api.put_sample(sample);
        log_info("Producer sent sample!");
        self.next_sample = sample.wrapping_add(1);
      } else {
        log_info("Producer paused.");
      }
      self.dispatch_count = phase.wrapping_add(1) % 12;
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
