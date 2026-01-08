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
      ensures
        // BEGIN MARKER TIME TRIGGERED ENSURES
        // guarantee Allowed_AlarmTempWStatus_Ranges
        //   An integration constraint can only refer to a single port, so need a general requires
        //   clause to relate the lower and upper temps
        GUMBO_Library::Allowed_AlarmTempWStatus_Ranges_spec(api.lower_alarm_tempWstatus, api.upper_alarm_tempWstatus),
        // END MARKER TIME TRIGGERED ENSURES
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
  pub open spec fn Allowed_UpperAlarmTempWStatus(upper: Isolette_Data_Model::TempWstatus_i) -> bool
  {
    GUMBO_Library::Allowed_UpperAlarmTempWStatus_spec(upper)
  }
  // END MARKER GUMBO METHODS
}
