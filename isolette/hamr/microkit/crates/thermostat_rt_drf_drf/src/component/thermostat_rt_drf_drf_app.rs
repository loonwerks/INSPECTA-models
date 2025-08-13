// This file will not be overwritten if codegen is rerun

use data::*;
use crate::bridge::thermostat_rt_drf_drf_api::*;

pub struct thermostat_rt_drf_drf {
}

impl thermostat_rt_drf_drf {
  pub fn new() -> Self 
  {
    Self {
    }
  }

  pub fn initialize<API: thermostat_rt_drf_drf_Put_Api>(
    &mut self,
    api: &mut thermostat_rt_drf_drf_Application_Api<API>) 
  {
    log_info("initialize entrypoint invoked");
  }

  pub fn timeTriggered<API: thermostat_rt_drf_drf_Full_Api>(
    &mut self,
    api: &mut thermostat_rt_drf_drf_Application_Api<API>) 
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

pub fn log_info(message: &str) {
  log::info!("{}", message);
}

pub fn log_warn_channel(channel: u32) {
  log::warn!("Unexpected channel {}", channel);
}