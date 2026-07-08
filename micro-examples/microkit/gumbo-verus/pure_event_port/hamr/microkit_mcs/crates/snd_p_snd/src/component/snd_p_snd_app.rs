// This file will not be overwritten if HAMR codegen is rerun

use data::*;
use crate::bridge::snd_p_snd_api::*;
use vstd::prelude::*;

verus! {

  pub struct snd_p_snd {
    // PLACEHOLDER MARKER STATE VARS
  }

  impl snd_p_snd {
    pub fn new() -> Self
    {
      Self {
        // PLACEHOLDER MARKER STATE VAR INIT
      }
    }

    pub fn initialize<API: snd_p_snd_Put_Api> (
      &mut self,
      api: &mut snd_p_snd_Application_Api<API>)
      ensures
        // BEGIN MARKER INITIALIZATION ENSURES
        // guarantee initOutVal
        //   outgoing data ports must be initialized
        api.out_val == 0i32,
        // END MARKER INITIALIZATION ENSURES
    {
      log_info("initialize entrypoint invoked");
      api.put_out_val(0);
    }

    pub fn timeTriggered<API: snd_p_snd_Full_Api> (
      &mut self,
      api: &mut snd_p_snd_Application_Api<API>)
      requires
        // BEGIN MARKER TIME TRIGGERED REQUIRES
        // assume AADL_Requirement
        //   All outgoing event ports must be empty
        old(api).evt.is_none(),
        // END MARKER TIME TRIGGERED REQUIRES
      ensures
        // BEGIN MARKER TIME TRIGGERED ENSURES
        // guarantee sendVal
        //   every dispatch publishes an in-range value on out_val; the
        //   integration constraint provides the upper bound of 100
        api.out_val >= 0i32,
        // guarantee announce
        //   every dispatch announces that data is available to consume
        api.evt.is_some(),
        // END MARKER TIME TRIGGERED ENSURES
    {
      log_info("compute entrypoint invoked");
      api.put_out_val(42);
      api.put_evt();
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
