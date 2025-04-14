#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

// This file will not be overwritten if codegen is rerun

use crate::data::*;
use crate::bridge::thermostat_rt_mrm_mrm_api::*;
#[cfg(feature = "sel4")]
#[allow(unused_imports)]
use log::{error, warn, info, debug, trace};
use vstd::prelude::*;

verus! {

  pub struct thermostat_rt_mrm_mrm {
    // BEGIN MARKER STATE VARS
    pub lastRegulatorMode: Isolette_Data_Model::Regulator_Mode
    // END MARKER STATE VARS
  }

  impl thermostat_rt_mrm_mrm {
    pub const fn new() -> Self 
    {
      Self {
        // BEGIN MARKER STATE VAR INIT
        lastRegulatorMode: Isolette_Data_Model::Regulator_Mode::Init_Regulator_Mode
        // END MARKER STATE VAR INIT
      }
    }

    pub fn initialize<API: thermostat_rt_mrm_mrm_Put_Api>(
      &mut self,
      api: &mut thermostat_rt_mrm_mrm_Application_Api<API>)
      ensures
        // BEGIN MARKER INITIALIZATION ENSURES
        // guarantee REQ_MRM_1
        //   The initial mode of the regular is INIT
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=109 
        api.regulator_mode == Isolette_Data_Model::Regulator_Mode::Init_Regulator_Mode
        // END MARKER INITIALIZATION ENSURES 
    {
      #[cfg(feature = "sel4")]
      info!("initialize entrypoint invoked");
    }

    pub fn timeTriggered<API: thermostat_rt_mrm_mrm_Full_Api>(
      &mut self,
      api: &mut thermostat_rt_mrm_mrm_Application_Api<API>)
      ensures
        // BEGIN MARKER TIME TRIGGERED ENSURES
        // case REQ_MRM_2
        //   'transition from Init to Normal'
        //   If the current regulator mode is Init, then
        //   the regulator mode is set to NORMAL iff the regulator status is valid (see Table A-10), i.e.,
        //     if NOT (Regulator Interface Failure OR Regulator Internal Failure)
        //        AND Current Temperature.Status = Valid
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=109 
        (old(self).lastRegulatorMode == Isolette_Data_Model::Regulator_Mode::Init_Regulator_Mode) ==>
          (!(api.interface_failure.flag || api.internal_failure.flag) &&
             (api.current_tempWstatus.status == Isolette_Data_Model::ValueStatus::Valid) ==>
             (api.regulator_mode == Isolette_Data_Model::Regulator_Mode::Normal_Regulator_Mode) &&
               (self.lastRegulatorMode == Isolette_Data_Model::Regulator_Mode::Normal_Regulator_Mode)),
        // case REQ_MRM_Maintain_Normal
        //   'maintaining NORMAL, NORMAL to NORMAL'
        //   If the current regulator mode is Normal, then
        //   the regulator mode is stays normal iff
        //   the regulaor status is not false i.e.,
        //          if NOT(
        //              (Regulator Interface Failure OR Regulator Internal Failure)
        //              OR NOT(Current Temperature.Status = Valid)
        //          )
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=109 
        (old(self).lastRegulatorMode == Isolette_Data_Model::Regulator_Mode::Normal_Regulator_Mode) ==>
          (!(api.interface_failure.flag || api.internal_failure.flag) &&
             (api.current_tempWstatus.status == Isolette_Data_Model::ValueStatus::Valid) ==>
             (api.regulator_mode == Isolette_Data_Model::Regulator_Mode::Normal_Regulator_Mode) &&
               (self.lastRegulatorMode == Isolette_Data_Model::Regulator_Mode::Normal_Regulator_Mode)),
        // case REQ_MRM_3
        //   'transition for NORMAL to FAILED'
        //   If the current regulator mode is Normal, then
        //   the regulator mode is set to Failed iff
        //   the regulator status is false, i.e.,
        //      if  (Regulator Interface Failure OR Regulator Internal Failure)
        //          OR NOT(Current Temperature.Status = Valid)
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=109 
        (old(self).lastRegulatorMode == Isolette_Data_Model::Regulator_Mode::Normal_Regulator_Mode) ==>
          ((api.interface_failure.flag || api.internal_failure.flag) &&
             (api.current_tempWstatus.status != Isolette_Data_Model::ValueStatus::Valid) ==>
             (api.regulator_mode == Isolette_Data_Model::Regulator_Mode::Failed_Regulator_Mode) &&
               (self.lastRegulatorMode == Isolette_Data_Model::Regulator_Mode::Failed_Regulator_Mode)),
        // case REQ_MRM_4
        //   'transition from INIT to FAILED' 
        //   If the current regulator mode is Init, then
        //   the regulator mode and lastRegulatorMode state value is set to Failed iff
        //   the regulator status is false, i.e.,
        //          if  (Regulator Interface Failure OR Regulator Internal Failure)
        //          OR NOT(Current Temperature.Status = Valid)
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=109
        (old(self).lastRegulatorMode == Isolette_Data_Model::Regulator_Mode::Init_Regulator_Mode) ==>
          ((api.interface_failure.flag || api.internal_failure.flag) &&
             (api.current_tempWstatus.status != Isolette_Data_Model::ValueStatus::Valid) ==>
             (api.regulator_mode == Isolette_Data_Model::Regulator_Mode::Failed_Regulator_Mode) &&
               (self.lastRegulatorMode == Isolette_Data_Model::Regulator_Mode::Failed_Regulator_Mode)),
        // case REQ_MRM_MaintainFailed
        //   'maintaining FAIL, FAIL to FAIL'
        //   If the current regulator mode is Failed, then
        //   the regulator mode remains in the Failed state and the LastRegulator mode remains Failed.REQ-MRM-Maintain-Failed
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=109
        (old(self).lastRegulatorMode == Isolette_Data_Model::Regulator_Mode::Failed_Regulator_Mode) ==>
          ((api.regulator_mode == Isolette_Data_Model::Regulator_Mode::Failed_Regulator_Mode) &&
             (self.lastRegulatorMode == Isolette_Data_Model::Regulator_Mode::Failed_Regulator_Mode))
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
