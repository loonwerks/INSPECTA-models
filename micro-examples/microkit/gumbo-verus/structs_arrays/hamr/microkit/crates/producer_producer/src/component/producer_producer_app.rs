// This file will not be overwritten if codegen is rerun

use data::*;
use crate::bridge::producer_producer_api::*;

pub struct producer_producer {
}

impl producer_producer {
  pub fn new() -> Self
  {
    Self {
    }
  }

  pub fn initialize<API: producer_producer_Put_Api> (
    &mut self,
    api: &mut producer_producer_Application_Api<API>)
  {
    log_info("initialize entrypoint invoked");
  }

  pub fn timeTriggered<API: producer_producer_Full_Api> (
    &mut self,
    api: &mut producer_producer_Application_Api<API>)
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
