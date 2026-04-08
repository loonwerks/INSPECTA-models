// This file will not be overwritten if codegen is rerun

use data::*;
use crate::bridge::monitor_process_monitor_thread_api::*;
use vstd::prelude::*;

verus! {

  pub struct monitor_process_monitor_thread {
    // PLACEHOLDER MARKER STATE VARS
  }

  impl monitor_process_monitor_thread {
    pub fn new() -> Self
    {
      Self {
        // PLACEHOLDER MARKER STATE VAR INIT
      }
    }

    pub fn initialize<API: monitor_process_monitor_thread_Put_Api> (
      &mut self,
      api: &mut monitor_process_monitor_thread_Application_Api<API>)
      ensures
        // PLACEHOLDER MARKER INITIALIZATION ENSURES
    {
      log_info("initialize entrypoint invoked");
    }

    pub fn timeTriggered<API: monitor_process_monitor_thread_Full_Api> (
      &mut self,
      api: &mut monitor_process_monitor_thread_Application_Api<API>)
      requires
        // PLACEHOLDER MARKER TIME TRIGGERED REQUIRES
      ensures
        // PLACEHOLDER MARKER TIME TRIGGERED ENSURES
    {
      //log_info("compute entrypoint invoked");
      log_state(api.get_producer_p_p1_producer_write_port(), api.get_producer_p_p2_producer_write_port());
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
  pub fn log_state(p1: Option<i8>, p2: Option<i8>)
  {
    if let Some(v) = p1 {
      log::info!("Received {:?} from producer_p_p1", v);
    }
    if let Some(v) = p2 {
      log::info!("Received {:?} from producer_p_p2", v);
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
