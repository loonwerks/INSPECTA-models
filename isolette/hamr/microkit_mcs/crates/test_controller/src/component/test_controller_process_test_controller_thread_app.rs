// This file will not be overwritten if HAMR codegen is rerun

use data::*;
use crate::bridge::test_controller_process_test_controller_thread_api::*;
use vstd::prelude::*;

pub struct test_controller_process_test_controller_thread {
  // PLACEHOLDER MARKER STATE VARS
}

impl test_controller_process_test_controller_thread {
  pub fn new() -> Self
  {
    Self {
      // PLACEHOLDER MARKER STATE VAR INIT
    }
  }

  pub fn initialize<API: test_controller_process_test_controller_thread_Put_Api> (
    &mut self,
    api: &mut test_controller_process_test_controller_thread_Application_Api<API>)
  {
    log_info("initialize entrypoint invoked");
  }

  pub fn timeTriggered<API: test_controller_process_test_controller_thread_Full_Api> (
    &mut self,
    api: &mut test_controller_process_test_controller_thread_Application_Api<API>)
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

pub fn log_info(msg: &str)
{
  log::info!("{0}", msg);
}

pub fn log_warn_channel(channel: u32)
{
  log::warn!("Unexpected channel: {0}", channel);
}

// PLACEHOLDER MARKER GUMBO METHODS
