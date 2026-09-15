// This file will not be overwritten if HAMR codegen is rerun

use data::*;
use crate::bridge::top_sub_A_sub_a_api::*;
use vstd::prelude::*;

verus! {

  pub struct top_sub_A_sub_a {
    // PLACEHOLDER MARKER STATE VARS
  }

  impl top_sub_A_sub_a {
    pub fn new() -> Self
    {
      Self {
        // PLACEHOLDER MARKER STATE VAR INIT
      }
    }

    pub fn initialize<API: top_sub_A_sub_a_Put_Api> (
      &mut self,
      api: &mut top_sub_A_sub_a_Application_Api<API>)
      ensures
        // BEGIN MARKER INITIALIZATION ENSURES
        // guarantee A_Initial_Input_Range
        //   A input is initially in the system range
        final(api).Input < 10i32,
        // END MARKER INITIALIZATION ENSURES
    {
      log_info("initialize entrypoint invoked");
    }

    pub fn timeTriggered<API: top_sub_A_sub_a_Full_Api> (
      &mut self,
      api: &mut top_sub_A_sub_a_Application_Api<API>)
      requires
        // BEGIN MARKER TIME TRIGGERED REQUIRES
        // assume A_Input_Range
        //   A input range
        old(api).Input < 50i32,
        // END MARKER TIME TRIGGERED REQUIRES
      ensures
        // BEGIN MARKER TIME TRIGGERED ENSURES
        // guarantee A_Output_Range
        //   A output range
        final(api).Output < 2i32 * final(api).Input,
        // END MARKER TIME TRIGGERED ENSURES
    {
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
