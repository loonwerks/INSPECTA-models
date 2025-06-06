#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

// This file will not be overwritten if codegen is rerun

use crate::data::*;
use crate::data::Isolette_Data_Model::*; // manually add to shorten data type references
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
      // partially achieves REQ_MMI_1
      api.put_monitor_status(Status::Init_Status);

      // Note (from JMH): We do not have allocated component requirements for the
      // remaining outputs.   However, HAMR infrastructure (based on
      // AADL's semantics) requires that all output data ports are
      // initialized.  This is not currently formalizable in the GUMBO
      // contract language.   It could possibly be added.
      // Alternatively, the "must be initialized" property could also
      // be checked by static analysis.
      // To achieve the initialization, we simply used HAMR-generated 
      // default values for components.
      api.put_interface_failure(Failure_Flag_i::default());
      api.put_lower_alarm_temp(Temp_i::default());
      api.put_upper_alarm_temp(Temp_i::default());     
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

      //============================
      // Get input port values
      //============================

      let lower: TempWstatus_i = api.get_lower_alarm_tempWstatus();
      let upper: TempWstatus_i = api.get_upper_alarm_tempWstatus();
      let monitor_mode: Monitor_Mode = api.get_monitor_mode();

      // Note (JMH): The original FAA REMH requirements doc lists the 
      // current temp as an input in Figure A-5: "Monitor Temperature
      // Dependency Diagram", but that value is never referenced in
      // the component requirements.  Therefore, we omit fetching its value.

      // let current_temp: TempWstatus_i = api.get_current_tempWstatus();

      // =============================================
      //  Set values for Monitor Status output (Table A-6)
      // =============================================

      let mut monitor_status: Status = Status::Init_Status;

      match monitor_mode {
        // INIT Mode
        Monitor_Mode::Init_Monitor_Mode => {
          // REQ-MRI-1
          monitor_status = Status::Init_Status;
        }

        // NORMAL Mode
        Monitor_Mode::Normal_Monitor_Mode => {
          // REQ-MRI-2
          monitor_status = Status::On_Status;
        }

        // FAILED Mode
        Monitor_Mode::Failed_Monitor_Mode => {
          // REQ-MRI-3    
          monitor_status = Status::Failed_Status;
        }
      }

      api.put_monitor_status(monitor_status);

      // =============================================
      //  Set values for Monitor Interface Failure output
      // =============================================

      // The interface_failure status defaults to TRUE (i.e., failing), which is the safe modality.
      #[allow(unused_assignments)]
      let mut interface_failure: bool = true;

      // Extract the value status from both the upper and lower alarm range
      let upper_desired_temp_status: ValueStatus = upper.status;
      let lower_desired_temp_status: ValueStatus = lower.status;

      // Set the Monitor Interface Failure value based on the status values of the
      //   upper and lower temperature

      if !(upper_desired_temp_status == ValueStatus::Valid) ||
          !(lower_desired_temp_status == ValueStatus::Valid) {
          // REQ-MRI-4
          interface_failure = true;
      } else {
          // REQ-MRI-5
          interface_failure = false;
      }

      // create the appropriately typed value to send on the output port and set the port value
      let interface_failure_flag = Failure_Flag_i { flag: interface_failure };
      api.put_interface_failure(interface_failure_flag);

      // =============================================
      //  Set values for Alarm Range 
      // =============================================

      if !interface_failure {
          // REQ-MMI-6
          api.put_lower_alarm_temp(Temp_i { degrees: lower.degrees } );
          api.put_upper_alarm_temp(Temp_i { degrees: upper.degrees } );
      } else {
          // REQ-MMI-7
          api.put_lower_alarm_temp(Temp_i::default() );
          api.put_upper_alarm_temp(Temp_i::default() );
      }    
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

    // BEGIN MARKER GUMBO METHODS
    pub open spec fn timeout_condition_satisfied() -> bool 
    {
      true
    }
    // END MARKER GUMBO METHODS
  }
}
