// This file will not be overwritten if codegen is rerun

use data::*;
use crate::bridge::operator_interface_oip_oit_api::*;
use vstd::prelude::*;

verus! {

  pub struct operator_interface_oip_oit {
  }

  impl operator_interface_oip_oit {
    pub fn new() -> Self 
    {
      Self {
      }
    }

    pub fn initialize<API: operator_interface_oip_oit_Put_Api>(
      &mut self,
      api: &mut operator_interface_oip_oit_Application_Api<API>) 
    {
      log_info("initialize entrypoint invoked");
    }

    pub fn timeTriggered<API: operator_interface_oip_oit_Full_Api>(
      &mut self,
      api: &mut operator_interface_oip_oit_Application_Api<API>) 
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
  pub fn log_info(message: &str) {
    log::info!("{}", message);
  }

  #[verifier::external_body]
  pub fn log_warn_channel(channel: u32) {
    log::warn!("Unexpected channel {}", channel);
  }
}
