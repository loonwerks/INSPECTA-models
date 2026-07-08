// This file will not be overwritten if HAMR codegen is rerun

use data::*;
use crate::bridge::userland_monitor_process_userland_monitor_thread_api::*;
use vstd::prelude::*;

#[verus_verify]
pub struct userland_monitor_process_userland_monitor_thread {
  // PLACEHOLDER MARKER STATE VARS
}

#[verus_verify]
impl userland_monitor_process_userland_monitor_thread {
  pub fn new() -> Self
  {
    Self {
      // PLACEHOLDER MARKER STATE VAR INIT
    }
  }

  #[verus_spec(
    ensures
      // PLACEHOLDER MARKER INITIALIZATION ENSURES
  )]
  pub fn initialize<API: userland_monitor_process_userland_monitor_thread_Put_Api> (
    &mut self,
    api: &mut userland_monitor_process_userland_monitor_thread_Application_Api<API>)
  {
    log_info("initialize entrypoint invoked");
  }

  #[verus_spec(
    requires
      // PLACEHOLDER MARKER TIME TRIGGERED REQUIRES
    ensures
      // PLACEHOLDER MARKER TIME TRIGGERED ENSURES
  )]
  pub fn timeTriggered<API: userland_monitor_process_userland_monitor_thread_Full_Api> (
    &mut self,
    api: &mut userland_monitor_process_userland_monitor_thread_Application_Api<API>)
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

#[verus_verify(external_body)]
pub fn log_info(msg: &str)
{
  log::info!("{0}", msg);
}

#[verus_verify(external_body)]
pub fn log_warn_channel(channel: u32)
{
  log::warn!("Unexpected channel: {0}", channel);
}

// PLACEHOLDER MARKER GUMBO METHODS

// Schedule channel IDs used to identify which thread yielded or runs next

pub const pad: u32 = 0;

pub const tsp_tst_MON: u32 = 2;

pub const tcp_tct_MON: u32 = 3;

pub const fp_ft_MON: u32 = 4;

pub const userland_monitor_process_userland_monitor_thread_MON: u32 = 5;
