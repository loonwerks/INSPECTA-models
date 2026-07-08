// This file will not be overwritten if HAMR codegen is rerun

use data::*;
use crate::bridge::rcv_p_rcv_api::*;
use vstd::prelude::*;

verus! {

  pub struct rcv_p_rcv {
    // BEGIN MARKER STATE VARS
    pub lastVal: i32,
    // END MARKER STATE VARS
  }

  impl rcv_p_rcv {
    pub fn new() -> Self
    {
      Self {
        // BEGIN MARKER STATE VAR INIT
        lastVal: 0,
        // END MARKER STATE VAR INIT
      }
    }

    pub fn initialize<API: rcv_p_rcv_Put_Api> (
      &mut self,
      api: &mut rcv_p_rcv_Application_Api<API>)
      ensures
        // BEGIN MARKER INITIALIZATION ENSURES
        // guarantee initLastVal
        //   state is initialized before the first dispatch
        self.lastVal == 0i32,
        // END MARKER INITIALIZATION ENSURES
    {
      log_info("initialize entrypoint invoked");
      self.lastVal = 0;
    }

    pub fn timeTriggered<API: rcv_p_rcv_Full_Api> (
      &mut self,
      api: &mut rcv_p_rcv_Application_Api<API>)
      requires
        // BEGIN MARKER TIME TRIGGERED REQUIRES
        // assume dataBounded
        //   a received event announces a value of at most 100; the
        //   integration constraint provides the lower bound of 0
        old(api).evt.is_some() ==>
          (old(api).in_val <= 100i32),
        // END MARKER TIME TRIGGERED REQUIRES
      ensures
        // BEGIN MARKER TIME TRIGGERED ENSURES
        // guarantee latchOnEvent
        //   record the announced value
        api.evt.is_some() ==>
          (self.lastVal == api.in_val),
        // guarantee holdOtherwise
        //   no event, no state change
        !(api.evt.is_some()) ==>
          (self.lastVal == old(self).lastVal),
        // END MARKER TIME TRIGGERED ENSURES
    {
      log_info("compute entrypoint invoked");
      if api.get_evt() {
        self.lastVal = api.get_in_val();
        log_info("received data announcement");
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
  pub fn log_warn_channel(channel: u32)
  {
    log::warn!("Unexpected channel: {0}", channel);
  }

  // PLACEHOLDER MARKER GUMBO METHODS

}
