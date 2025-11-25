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

    const low: Isolette_Data_Model::TempWstatus_i = Isolette_Data_Model::TempWstatus_i { 
          degrees: 97, 
          status: Isolette_Data_Model::ValueStatus::Valid };

    const high: Isolette_Data_Model::TempWstatus_i = Isolette_Data_Model::TempWstatus_i { 
          degrees: 100, 
          status: Isolette_Data_Model::ValueStatus::Valid };

    pub fn initialize<API: operator_interface_oip_oit_Put_Api>(
      &mut self,
      api: &mut operator_interface_oip_oit_Application_Api<API>) 
    {
      log_info("initialize entrypoint invoked");

      api.put_lower_alarm_tempWstatus(Self::low);
      api.put_upper_alarm_tempWstatus(Self::high);

      api.put_lower_desired_tempWstatus(Self::low);
      api.put_upper_desired_tempWstatus(Self::high);


    }

    pub fn timeTriggered<API: operator_interface_oip_oit_Full_Api>(
      &mut self,
      api: &mut operator_interface_oip_oit_Application_Api<API>) 
    {
      log_info("compute entrypoint invoked");

      api.put_lower_alarm_tempWstatus(Self::low);
      api.put_upper_alarm_tempWstatus(Self::high);

      api.put_lower_desired_tempWstatus(Self::low);
      api.put_upper_desired_tempWstatus(Self::high);
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

  // BEGIN MARKER GUMBO METHODS
  pub open spec fn UAT_lower() -> i32
  {
    97i32
  }

  pub open spec fn UAT_upper() -> i32
  {
    102i32
  }
  // END MARKER GUMBO METHODS
}
