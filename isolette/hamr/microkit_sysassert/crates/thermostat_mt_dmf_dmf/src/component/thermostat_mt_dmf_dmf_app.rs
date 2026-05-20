// This file will not be overwritten if HAMR codegen is rerun

use data::*;
use crate::bridge::thermostat_mt_dmf_dmf_api::*;
use vstd::prelude::*;

#[verus_verify]
pub struct thermostat_mt_dmf_dmf {
  // PLACEHOLDER MARKER STATE VARS
}

#[verus_verify]
impl thermostat_mt_dmf_dmf {
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
  pub fn initialize<API: thermostat_mt_dmf_dmf_Put_Api> (
    &mut self,
    api: &mut thermostat_mt_dmf_dmf_Application_Api<API>)
  {
    log_info("initialize entrypoint invoked");
  }

  #[verus_spec(
    requires
      // PLACEHOLDER MARKER TIME TRIGGERED REQUIRES
    ensures
      // PLACEHOLDER MARKER TIME TRIGGERED ENSURES
  )]
  pub fn timeTriggered<API: thermostat_mt_dmf_dmf_Full_Api> (
    &mut self,
    api: &mut thermostat_mt_dmf_dmf_Application_Api<API>)
  {
    //log_info("compute entrypoint invoked");
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
