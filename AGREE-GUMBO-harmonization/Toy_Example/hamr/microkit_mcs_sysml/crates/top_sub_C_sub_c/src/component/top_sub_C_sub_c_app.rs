// This file will not be overwritten if HAMR codegen is rerun

use data::*;
use crate::bridge::top_sub_C_sub_c_api::*;
use vstd::prelude::*;

verus! {

  pub struct top_sub_C_sub_c {
    // PLACEHOLDER MARKER STATE VARS
  }

  impl top_sub_C_sub_c {
    pub fn new() -> Self
    {
      Self {
        // PLACEHOLDER MARKER STATE VAR INIT
      }
    }

    pub fn initialize<API: top_sub_C_sub_c_Put_Api> (
      &mut self,
      api: &mut top_sub_C_sub_c_Application_Api<API>)
      ensures
        // PLACEHOLDER MARKER INITIALIZATION ENSURES
    {
      log_info("initialize entrypoint invoked");
    }

    pub fn timeTriggered<API: top_sub_C_sub_c_Full_Api> (
      &mut self,
      api: &mut top_sub_C_sub_c_Application_Api<API>)
      requires
        // PLACEHOLDER MARKER TIME TRIGGERED REQUIRES
      ensures
        // BEGIN MARKER TIME TRIGGERED ENSURES
        // guarantee C_Output_Range
        //   C output range
        final(api).Output == final(api).Input1 + final(api).Input2,
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
