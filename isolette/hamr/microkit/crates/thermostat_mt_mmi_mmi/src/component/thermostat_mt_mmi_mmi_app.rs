#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

// This file will not be overwritten if codegen is rerun

use crate::data::*;
use crate::bridge::thermostat_mt_mmi_mmi_api::*;
#[cfg(feature = "sel4")]
#[allow(unused_imports)]
use log::{error, warn, info, debug, trace};
use vstd::prelude::*;

verus! {

  pub struct thermostat_mt_mmi_mmi {
    // BEGIN MARKER STATE VARS
    pub lastCmd: Isolette_Data_Model::On_Off
    // END MARKER STATE VARS
  }

  impl thermostat_mt_mmi_mmi {
    pub fn new() -> Self 
    {
      Self {
        // BEGIN MARKER STATE VAR INIT
        lastCmd: Isolette_Data_Model::On_Off::default()
        // END MARKER STATE VAR INIT
      }
    }

    pub fn initialize<API: thermostat_mt_mmi_mmi_Put_Api>(
      &mut self,
      api: &mut thermostat_mt_mmi_mmi_Application_Api<API>)
      ensures
        // BEGIN MARKER INITIALIZATION ENSURES
        // guarantee monitorStatusInitiallyInit
        api.monitor_status == Isolette_Data_Model::Status::Init_Status
        // END MARKER INITIALIZATION ENSURES 
    {
      #[cfg(feature = "sel4")]
      info!("initialize entrypoint invoked");
    }

    pub fn timeTriggered<API: thermostat_mt_mmi_mmi_Full_Api>(
      &mut self,
      api: &mut thermostat_mt_mmi_mmi_Application_Api<API>)
      ensures
        // BEGIN MARKER TIME TRIGGERED ENSURES
        // case REQ_MMI_1
        //   If the Manage Monitor Interface mode is INIT,
        //   the Monitor Status shall be set to Init.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 
        (old(api).monitor_mode == Isolette_Data_Model::Monitor_Mode::Init_Monitor_Mode) ==>
          (api.monitor_status == Isolette_Data_Model::Status::Init_Status),
        // case REQ_MMI_2
        //   If the Manage Monitor Interface mode is NORMAL,
        //   the Monitor Status shall be set to On
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 
        (old(api).monitor_mode == Isolette_Data_Model::Monitor_Mode::Normal_Monitor_Mode) ==>
          (api.monitor_status == Isolette_Data_Model::Status::On_Status),
        // case REQ_MMI_3
        //   If the Manage Monitor Interface mode is FAILED,
        //   the Monitor Status shall be set to Failed.
        //   Latency: < Max Operator Response Time
        //   Tolerance: N/A
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 
        (old(api).monitor_mode == Isolette_Data_Model::Monitor_Mode::Failed_Monitor_Mode) ==>
          (api.monitor_status == Isolette_Data_Model::Status::Failed_Status),
        // case REQ_MMI_4
        //   If the Status attribute of the Lower Alarm Temperature
        //   or the Upper Alarm Temperature is Invalid,
        //   the Monitor Interface Failure shall be set to True
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 
        ((old(api).lower_alarm_tempWstatus.status == Isolette_Data_Model::ValueStatus::Invalid) ||
           (old(api).upper_alarm_tempWstatus.status == Isolette_Data_Model::ValueStatus::Invalid)) ==>
          (api.interface_failure.flag),
        // case REQ_MMI_5
        //   If the Status attribute of the Lower Alarm Temperature
        //   and the Upper Alarm Temperature is Valid,
        //   the Monitor Interface Failure shall be set to False
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 
        ((old(api).lower_alarm_tempWstatus.status == Isolette_Data_Model::ValueStatus::Valid) &&
           (old(api).upper_alarm_tempWstatus.status == Isolette_Data_Model::ValueStatus::Valid)) ==>
          (!(api.interface_failure.flag)),
        // case REQ_MMI_6
        //   If the Monitor Interface Failure is False,
        //   the Alarm Range variable shall be set to the Desired Temperature Range
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 
        (true) ==>
          (!(api.interface_failure.flag) ==>
             ((api.lower_alarm_temp.degrees == api.lower_alarm_tempWstatus.degrees) &&
               (api.upper_alarm_temp.degrees == api.upper_alarm_tempWstatus.degrees))),
        // case REQ_MMI_7
        //   If the Monitor Interface Failure is True,
        //   the Alarm Range variable is UNSPECIFIED
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 
        (true) ==>
          (api.interface_failure.flag ==> true)
        // END MARKER TIME TRIGGERED ENSURES 
    {
      #[cfg(feature = "sel4")]
      info!("compute entrypoint invoked");
    }

    pub fn notify(
      &mut self,
      channel: microkit_channel) 
    {
      // this method is called when the monitor does not handle the passed in channel
      match channel {
        _ => {
          #[cfg(feature = "sel4")]
          warn!("Unexpected channel {}", channel)
        }
      }
    }
  }

}
