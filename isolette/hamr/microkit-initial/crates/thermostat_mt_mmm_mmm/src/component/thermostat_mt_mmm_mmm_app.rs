#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

// This file will not be overwritten if codegen is rerun

use data::*;
use crate::bridge::thermostat_mt_mmm_mmm_api::*;
#[cfg(feature = "sel4")]
#[allow(unused_imports)]
use log::{error, warn, info, debug, trace};
use vstd::prelude::*;

verus! {

  pub struct thermostat_mt_mmm_mmm {
    // BEGIN MARKER STATE VARS
    pub lastMonitorMode: Isolette_Data_Model::Monitor_Mode
    // END MARKER STATE VARS
  }

  impl thermostat_mt_mmm_mmm {
    pub fn new() -> Self 
    {
      Self {
        // BEGIN MARKER STATE VAR INIT
        lastMonitorMode: Isolette_Data_Model::Monitor_Mode::default()
        // END MARKER STATE VAR INIT
      }
    }

    pub fn initialize<API: thermostat_mt_mmm_mmm_Put_Api>(
      &mut self,
      api: &mut thermostat_mt_mmm_mmm_Application_Api<API>)
      ensures
        // BEGIN MARKER INITIALIZATION ENSURES
        // guarantee REQ_MMM_1
        //   Upon the first dispatch of the thread, the monitor mode is Init.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=114 
        api.monitor_mode == Isolette_Data_Model::Monitor_Mode::Init_Monitor_Mode
        // END MARKER INITIALIZATION ENSURES 
    {
      #[cfg(feature = "sel4")]
      info!("initialize entrypoint invoked");
    }

    pub fn timeTriggered<API: thermostat_mt_mmm_mmm_Full_Api>(
      &mut self,
      api: &mut thermostat_mt_mmm_mmm_Application_Api<API>)
      ensures
        // BEGIN MARKER TIME TRIGGERED ENSURES
        // case REQ_MMM_2
        //   If the current mode is Init, then
        //   the mode is set to NORMAL iff the monitor status is true (valid) (see Table A-15), i.e.,
        //   if  NOT (Monitor Interface Failure OR Monitor Internal Failure)
        //   AND Current Temperature.Status = Valid
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=114 
        (old(self).lastMonitorMode == Isolette_Data_Model::Monitor_Mode::Init_Monitor_Mode) ==>
          (!(api.interface_failure.flag || api.internal_failure.flag) &&
             (api.current_tempWstatus.status == Isolette_Data_Model::ValueStatus::Valid) ==>
             (api.monitor_mode == Isolette_Data_Model::Monitor_Mode::Normal_Monitor_Mode)),
        // case REQ_MMM_3
        //   If the current Monitor mode is Normal, then
        //   the Monitor mode is set to Failed iff
        //   the Monitor status is false, i.e.,
        //   if  (Monitor Interface Failure OR Monitor Internal Failure)
        //   OR NOT(Current Temperature.Status = Valid)
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=114 
        (old(self).lastMonitorMode == Isolette_Data_Model::Monitor_Mode::Normal_Monitor_Mode) ==>
          (api.interface_failure.flag || api.internal_failure.flag ||
             (api.current_tempWstatus.status != Isolette_Data_Model::ValueStatus::Valid) ==>
             (api.monitor_mode == Isolette_Data_Model::Monitor_Mode::Failed_Monitor_Mode)),
        // case REQ_MMM_4
        //   If the current mode is Init, then
        //   the mode is set to Failed iff the time during
        //   which the thread has been in Init mode exceeds the
        //   Monitor Init Timeout value.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=114 
        (old(self).lastMonitorMode == Isolette_Data_Model::Monitor_Mode::Init_Monitor_Mode) ==>
          (false == (api.monitor_mode == Isolette_Data_Model::Monitor_Mode::Failed_Monitor_Mode))
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
