// This file will not be overwritten if codegen is rerun

use data::*;
use data::Isolette_Data_Model::*;
use crate::bridge::thermostat_rt_mri_mri_api::*;
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
        api.regulator_status == Isolette_Data_Model::Status::Init_Status,
        // END MARKER INITIALIZATION ENSURES
    {
      log_info("initialize entrypoint invoked");

      api.put_regulator_status(Status::Init_Status);
      //api.put_regulator_status(Status::Failed_Status); // seeded bug

      // Note (from JMH): We do not have allocated component requirements for the
      // remaining outputs.   However, HAMR infrastructure (based on
      // AADL's semantics) requires that all output data ports are
      // initialized.  This is not currently formalizable in the GUMBO
      // contract language.   It could possibly be added.
      // Alternatively, the "must be initialized" property could also
      // be checked by static analysis.
      // To achieve the initialization, we simply used HAMR-generated 
      // default values for components.
      api.put_displayed_temp(Temp_i::default());
      api.put_interface_failure(Failure_Flag_i::default());
      api.put_lower_desired_temp(Temp_i::default());
      api.put_upper_desired_temp(Temp_i::default());      
    }

    pub fn timeTriggered<API: thermostat_rt_mri_mri_Full_Api>(
      &mut self,
      api: &mut thermostat_rt_mri_mri_Application_Api<API>)
      requires
        // BEGIN MARKER TIME TRIGGERED REQUIRES
        // assume lower_is_not_higher_than_upper
        old(api).lower_desired_tempWstatus.degrees <= old(api).upper_desired_tempWstatus.degrees,
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
          (true),
        // END MARKER TIME TRIGGERED ENSURES
    {
      log_info("compute entrypoint invoked");

      //============================
      // Get input port values
      //============================

      let lower: TempWstatus_i = api.get_lower_desired_tempWstatus();
      let upper: TempWstatus_i = api.get_upper_desired_tempWstatus();
      let regulator_mode: Regulator_Mode = api.get_regulator_mode();
      let current_temp: TempWstatus_i = api.get_current_tempWstatus();

      log_current_temp(current_temp.degrees);

      // =============================================
      //  Set values for regulator_status output (Table A-6)
      // =============================================

      #[allow(unused_assignments)]
      let mut regulator_status: Status = Status::Init_Status;

      match regulator_mode {
          // INIT Mode
          Regulator_Mode::Init_Regulator_Mode => {
              // REQ-MRI-1
              regulator_status = Status::Init_Status;
          }

          // NORMAL Mode
          Regulator_Mode::Normal_Regulator_Mode => {
              // REQ-MRI-2
              regulator_status = Status::On_Status;
          }

          // FAILED Mode
          Regulator_Mode::Failed_Regulator_Mode => {
              // REQ-MRI-3    
              regulator_status = Status::Failed_Status;
          }
      }
      //self.example_state_variable = 1; // seeded bug

      api.put_regulator_status(regulator_status);

      // =============================================
      //  Set values for Display Temperature output (Table A-6)
      // =============================================

      // Latency: < Max Operator Response Time
      // Tolerance: +/- 0.6 degrees F
      //

      let mut display_temperature: Temp_i = Temp_i::default();

      match regulator_mode {
          // NORMAL mode
          Regulator_Mode::Normal_Regulator_Mode => {
              // REQ-MRI-4
              display_temperature = Temp_i { degrees: current_temp.degrees };
          }

          // INIT, FAILED Modes

          // REQ-MRI
          //
          // leave the current temperature set to the default temperature value above.
          // This fulfills the requirement since the value when the Regulator Mode is not NORMAL
          // is unspecified.

          Regulator_Mode::Init_Regulator_Mode => {} // do nothing
          Regulator_Mode::Failed_Regulator_Mode => {} // do nothing
      }

      api.put_displayed_temp(display_temperature);


      // =============================================
      //  Set values for Regulator Interface Failure output
      // =============================================

      // The interface_failure status defaults to TRUE (i.e., failing), which is the safe modality.
      #[allow(unused_assignments)]
      let mut interface_failure: bool = true;

      // Extract the value status from both the upper and lower alarm range
      let upper_desired_temp_status: ValueStatus = upper.status;
      let lower_desired_temp_status: ValueStatus = lower.status;

      // Set the Monitor Interface Failure value based on the status values of the
      //   upper and lower temperature

      /*
      if !(upper_desired_temp_status == ValueStatus::Valid) ||
          !(lower_desired_temp_status == ValueStatus::Valid) {
          // REQ-MRI-6
          interface_failure = true;
      } else {
          // REQ-MRI-7
          interface_failure = false;
      }
      */

      // alt version using match rather than if/else
      match (upper_desired_temp_status, lower_desired_temp_status) {
          (ValueStatus::Invalid, _) |
          (_, ValueStatus::Invalid) 
            => interface_failure = true,
          _ => interface_failure = false
      }


      // create the appropriately typed value to send on the output port and set the port value
      let interface_failure_flag = Failure_Flag_i { flag: interface_failure };
      api.put_interface_failure(interface_failure_flag);

      // =============================================
      //  Set values for Desired Range output
      // =============================================

      if !interface_failure {
          // REQ-MRI-8
          api.put_lower_desired_temp(Temp_i { degrees: lower.degrees } );
          api.put_upper_desired_temp(Temp_i { degrees: upper.degrees } );

      } else {
          // REQ-MRI-9
          api.put_lower_desired_temp(Temp_i::default() );
          api.put_upper_desired_temp(Temp_i::default() );
      }    
    }

    pub fn notify(
      &mut self,
      channel: microkit_channel) 
    {
      // this method is called when the monitor does not handle the passed in channel
      match channel {
        _ => {
          log_warn_channel(channel);
        }
      }
    }

    // BEGIN MARKER GUMBO METHODS
    pub open spec fn ROUND(num: i32) -> i32
    {
      num
    }
    // END MARKER GUMBO METHODS
  }

  #[verifier::external_body]
  pub fn log_info(message: &str) {
    log::info!("{}", message);
  }

  #[verifier::external_body]
  pub fn log_current_temp(temp: i32) {
    log::info!("current temp: {}", temp);
  }

  #[verifier::external_body]
  pub fn log_warn_channel(channel: u32) {
    log::warn!("Unexpected channel {}", channel);
  }
}
