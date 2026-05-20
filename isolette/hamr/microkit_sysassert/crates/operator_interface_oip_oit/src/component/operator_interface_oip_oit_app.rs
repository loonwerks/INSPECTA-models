// This file will not be overwritten if HAMR codegen is rerun

use data::*;
use Isolette_Data_Model::*;
use crate::bridge::operator_interface_oip_oit_api::*;
use vstd::prelude::*;

#[verus_verify]
pub struct operator_interface_oip_oit {
  // PLACEHOLDER MARKER STATE VARS
}

#[verus_verify]
impl operator_interface_oip_oit {
  pub fn new() -> Self
  {
    Self {
      // PLACEHOLDER MARKER STATE VAR INIT
    }
  }

  const lower_desired: Isolette_Data_Model::TempWstatus_i = Isolette_Data_Model::TempWstatus_i {
        degrees: 97,
        status: Isolette_Data_Model::ValueStatus::Valid };

  const upper_desired: Isolette_Data_Model::TempWstatus_i = Isolette_Data_Model::TempWstatus_i {
        degrees: 99,
        status: Isolette_Data_Model::ValueStatus::Valid };

  const lower_alarm: Isolette_Data_Model::TempWstatus_i = Isolette_Data_Model::TempWstatus_i {
        degrees: 97,
        status: Isolette_Data_Model::ValueStatus::Valid };

  const upper_alarm: Isolette_Data_Model::TempWstatus_i = Isolette_Data_Model::TempWstatus_i {
        degrees: 101,
        status: Isolette_Data_Model::ValueStatus::Valid };

  #[verus_spec(
    ensures
      // PLACEHOLDER MARKER INITIALIZATION ENSURES
  )]
  pub fn initialize<API: operator_interface_oip_oit_Put_Api> (
    &mut self,
    api: &mut operator_interface_oip_oit_Application_Api<API>)
  {
    log_info("initialize entrypoint invoked");

    api.put_lower_alarm_tempWstatus(Self::lower_desired);
    api.put_upper_alarm_tempWstatus(Self::upper_desired);

    api.put_lower_desired_tempWstatus(Self::lower_alarm);
    api.put_upper_desired_tempWstatus(Self::upper_alarm);
  }

  #[verus_spec(
    requires
      // PLACEHOLDER MARKER TIME TRIGGERED REQUIRES
    ensures
      // BEGIN MARKER TIME TRIGGERED ENSURES
      // guarantee Allowed_AlarmTempWStatus_Ranges
      //   An integration constraint can only refer to a single port, so need a general requires
      //   clause to relate the lower and upper temps
      GUMBO_Library::Allowed_AlarmTempWStatus_Ranges_spec(api.lower_alarm_tempWstatus, api.upper_alarm_tempWstatus),
      // END MARKER TIME TRIGGERED ENSURES
  )]
  pub fn timeTriggered<API: operator_interface_oip_oit_Full_Api> (
    &mut self,
    api: &mut operator_interface_oip_oit_Application_Api<API>)
  {
    //log_info("compute entrypoint invoked");

    log_state(api.get_regulator_status(), api.get_monitor_status(), api.get_display_temperature(), api.get_alarm_control());

    api.put_lower_alarm_tempWstatus(Self::lower_desired);
    api.put_upper_alarm_tempWstatus(Self::upper_desired);

    api.put_lower_desired_tempWstatus(Self::lower_alarm);
    api.put_upper_desired_tempWstatus(Self::upper_alarm);
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
pub fn log_state(reg: Status, mon: Status, temp: Temp_i, alarm: On_Off) {
  log::info!("Regulator Status: {:?}", reg);
  log::info!("Monitor Status: {:?}", mon);
  log::info!("Display Temperature: {:?}", temp);
  log::info!("Alarm: {:?}", alarm);
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

verus! {

  // BEGIN MARKER GUMBO METHODS
  pub open spec fn Allowed_UpperAlarmTempWStatus(upper: Isolette_Data_Model::TempWstatus_i) -> bool
  {
    GUMBO_Library::Allowed_UpperAlarmTempWStatus_spec(upper)
  }
  // END MARKER GUMBO METHODS

}
