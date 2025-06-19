#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

// This file will not be overwritten if codegen is rerun

use data::*;
use crate::bridge::thermostat_rt_mri_mri_api::*;
#[cfg(feature = "sel4")]
#[allow(unused_imports)]
use log::{error, warn, info, debug, trace};
use vstd::prelude::*;

verus! {

  pub struct thermostat_rt_mri_mri {
  }

  impl thermostat_rt_mri_mri {
    pub fn new() -> Self 
    {
      Self {
      }
    }

    pub fn initialize<API: thermostat_rt_mri_mri_Put_Api>(
      &mut self,
      api: &mut thermostat_rt_mri_mri_Application_Api<API>)
      ensures
        // BEGIN MARKER INITIALIZATION ENSURES
        // guarantee RegulatorStatusIsInitiallyInit
        api.regulator_status == Isolette_Data_Model::Status::Init_Status
        // END MARKER INITIALIZATION ENSURES 
    {
      #[cfg(feature = "sel4")]
      info!("initialize entrypoint invoked");
    }

    pub fn timeTriggered<API: thermostat_rt_mri_mri_Full_Api>(
      &mut self,
      api: &mut thermostat_rt_mri_mri_Application_Api<API>)
      requires
        // BEGIN MARKER TIME TRIGGERED REQUIRES
        // assume lower_is_not_higher_than_upper
        old(api).lower_desired_tempWstatus.degrees <= old(api).upper_desired_tempWstatus.degrees
        // END MARKER TIME TRIGGERED REQUIRES
      ensures
        // BEGIN MARKER TIME TRIGGERED ENSURES
        // case REQ_MRI_1
        //   If the Regulator Mode is INIT,
        //   the Regulator Status shall be set to Init.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=107 
        (old(api).regulator_mode == Isolette_Data_Model::Regulator_Mode::Init_Regulator_Mode) ==>
          (api.regulator_status == Isolette_Data_Model::Status::Init_Status),
        // case REQ_MRI_2
        //   If the Regulator Mode is NORMAL,
        //   the Regulator Status shall be set to On
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=107 
        (old(api).regulator_mode == Isolette_Data_Model::Regulator_Mode::Normal_Regulator_Mode) ==>
          (api.regulator_status == Isolette_Data_Model::Status::On_Status),
        // case REQ_MRI_3
        //   If the Regulator Mode is FAILED,
        //   the Regulator Status shall be set to Failed.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=107 
        (old(api).regulator_mode == Isolette_Data_Model::Regulator_Mode::Failed_Regulator_Mode) ==>
          (api.regulator_status == Isolette_Data_Model::Status::Failed_Status),
        // case REQ_MRI_4
        //   If the Regulator Mode is NORMAL, the
        //   Display Temperature shall be set to the value of the
        //   Current Temperature rounded to the nearest integer.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 
        (old(api).regulator_mode == Isolette_Data_Model::Regulator_Mode::Normal_Regulator_Mode) ==>
          (api.displayed_temp.degrees == api.current_tempWstatus.degrees),
        // case REQ_MRI_5
        //   If the Regulator Mode is not NORMAL,
        //   the value of the Display Temperature is UNSPECIFIED.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 
        (true) ==>
          (true),
        // case REQ_MRI_6
        //   If the Status attribute of the Lower Desired Temperature
        //   or the Upper Desired Temperature is Invalid,
        //   the Regulator Interface Failure shall be set to True.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 
        ((old(api).upper_desired_tempWstatus.status != Isolette_Data_Model::ValueStatus::Valid) ||
           (old(api).upper_desired_tempWstatus.status != Isolette_Data_Model::ValueStatus::Valid)) ==>
          (api.interface_failure.flag),
        // case REQ_MRI_7
        //   If the Status attribute of the Lower Desired Temperature
        //   and the Upper Desired Temperature is Valid,
        //   the Regulator Interface Failure shall be set to False.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 
        (true) ==>
          (api.interface_failure.flag == !((api.upper_desired_tempWstatus.status == Isolette_Data_Model::ValueStatus::Valid) &&
             (api.lower_desired_tempWstatus.status == Isolette_Data_Model::ValueStatus::Valid))),
        // case REQ_MRI_8
        //   If the Regulator Interface Failure is False,
        //   the Desired Range shall be set to the Desired Temperature Range.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 
        (true) ==>
          (!(api.interface_failure.flag) ==>
             ((api.lower_desired_temp.degrees == api.lower_desired_tempWstatus.degrees) &&
               (api.upper_desired_temp.degrees == api.upper_desired_tempWstatus.degrees))),
        // case REQ_MRI_9
        //   If the Regulator Interface Failure is True,
        //   the Desired Range is UNSPECIFIED.
        //   the Desired Range shall be set to the Desired Temperature Range.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 
        (true) ==>
          (true)
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
