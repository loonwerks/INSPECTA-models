// This file will not be overwritten if codegen is rerun

#[cfg(test)]
mod tests {
  // NOTE: need to run tests sequentially to prevent race conditions
  //       on the app and the testing apis which are static
  use serial_test::serial;

  use crate::bridge::extern_c_api as extern_api;
  use data::*;
  use crate::bridge::thermostat_mt_mmi_mmi_GUMBOX as GUMBOX; // manually added
  use data::Isolette_Data_Model::*;                   // manually added

  //=============================================
  //  Component Inputs/Outputs
  //
  // Inputs
  //
  //   lower_alarm_tempWstatus (lower): TempWstatus_i 
  //   upper_alarm_tempWstatus (upper): TempWstatus_i 
  //   monitor_mode (mode): Monitor_Mode
  //   (note: current_tempWstatus not used)
  //
  // Outputs
  //   monitor_status (status): Status
  //   interface_failure: Failure_Flag_i
  //   lower_alarm_temp (lower): Temp_i
  //   upper_alarm_temp (upper): Temp_i
  //=============================================


  //=============================================
  //  H e l p e r     F u n c t i o n s
  //
  //  Manually written based on Grok-engineered solution for MRM
  //=============================================
    
    // Helper function to set up input ports and state, returning
    // input values.
    //
    // Note: MRI's behavior is independent of the actual temperature
    // values in lower and upper -- they are simply passed through
    // and the contract enforces that outputs equal inputs.  We may
    // want some annotation for testing indicating that the function
    // is independent of the actual temperature value.  Though this
    // should also be apparent due to coverage being achieved with a
    // single temperature value.
    //
    // XXXXXXXX stopped here XXXXXXXXXX
    fn setup_test_state(
        lower: TempWstatus_i,
        upper: TempWstatus_i,
        monitor_mode: Monitor_Mode
        // current temp - not used - hard code value below in method body
    ) -> (Status, Failure_Flag_i, Temp_i, Temp_i) {
        // hard code value until interface or requirements for
        // component are updated.
        let current_tempWstatus = TempWstatus_i {
            degrees: 96, // i32 
            status: ValueStatus::Valid
        };
        let interface_failure = Failure_Flag_i {
            flag: interface_failure_flag,
        };
        let internal_failure = Failure_Flag_i {
            flag: internal_failure_flag,
        };

        // [PutInPorts]: put values on the input ports
        *extern_api::IN_current_tempWstatus.lock().unwrap() = Some(current_tempWstatus);
        *extern_api::IN_interface_failure.lock().unwrap() = Some(interface_failure);
        *extern_api::IN_internal_failure.lock().unwrap() = Some(internal_failure);

        unsafe {
            // [SetInStateVars]: set the pre-state values of state variables            
            set_lastRegulatorMode(last_regulator_mode);
        }

        (current_tempWstatus, interface_failure, internal_failure)
    }

    // Helper function to retrieve output and state
    // Suggested by Grok without prompting
    fn retrieve_output_and_state() -> (Regulator_Mode, Regulator_Mode) {
        // [RetrieveOutState]: retrieve values of the output port
        let regulator_mode = extern_api::OUT_regulator_mode
            .lock()
            .unwrap()
            .expect("Not expecting None");

        unsafe {
            // Retrieve value of GUMBO declared local component state
            let last_regulator_mode = get_lastRegulatorMode();
            (regulator_mode, last_regulator_mode)
        }
    }

    fn set_lastRegulatorMode(last_regulator_mode: Regulator_Mode) {
        unsafe {
            match &mut crate::app {
                Some(inner) => inner.lastRegulatorMode = last_regulator_mode,
                None => panic!("app is None")
            }
        }
    }

    fn get_lastRegulatorMode() -> Regulator_Mode {
        unsafe {
            match &mut crate::app {
                Some(inner) => inner.lastRegulatorMode,
                None => panic!("app is None")
            }
        }
    }


    // Macro to generate timeTriggered tests, demonstrating simplified test creation
    // Suggested by Grok without prompting
    macro_rules! run_thermostat_test {
        ($name:ident, $mode:expr, $temp_status:expr, $interface_fail:expr, $internal_fail:expr, $expected_mode:expr) => {
            #[test]
            #[serial]
            fn $name() {
                crate::thermostat_rt_mrm_mrm_initialize();

                let in_last_regulator_mode = $mode;
                let (current_tempWstatus, interface_failure, internal_failure) =
                    setup_test_state(in_last_regulator_mode, $temp_status, $interface_fail, $internal_fail);

                crate::thermostat_rt_mrm_mrm_timeTriggered();

                let (regulator_mode, last_regulator_mode) = retrieve_output_and_state();
                unsafe {
                    assert!(GUMBOX::compute_CEP_Post(
                        in_last_regulator_mode,
                        last_regulator_mode,
                        current_tempWstatus,
                        interface_failure,
                        internal_failure,
                        regulator_mode
                    ));
                    assert_eq!(regulator_mode, $expected_mode);
                    assert_eq!(last_regulator_mode, $expected_mode);
                }
            }
        };
    }

    // Helper function
    fn test_time_triggered(
        in_last_regulator_mode: Regulator_Mode,
        temp_status: ValueStatus,
        interface_failure_flag: bool,
        internal_failure_flag: bool,
        expected_mode: Regulator_Mode,
    ) {
        let (current_tempWstatus, interface_failure, internal_failure) =
            setup_test_state(in_last_regulator_mode, temp_status, interface_failure_flag, internal_failure_flag);
        crate::thermostat_rt_mrm_mrm_timeTriggered();
        let (regulator_mode, last_regulator_mode) = retrieve_output_and_state();
        unsafe {
            assert!(GUMBOX::compute_CEP_Post(
                in_last_regulator_mode,
                last_regulator_mode,
                current_tempWstatus,
                interface_failure,
                internal_failure,
                regulator_mode
            ));
        }
    }


  /*
    fn test_initialization() {
      // [InvokeEntryPoint]: invoke the entry point test method
  
      unsafe {
        crate::thermostat_rt_mri_mri_initialize();
      }
  
      // [RetrieveOutState]: retrieve values of the output ports via get operations and GUMBO declared local state variable
      let regulator_status = extern_api::OUT_regulator_status.lock().unwrap().expect("Not expecting None");
      let display_temp = extern_api::OUT_displayed_temp.lock().unwrap().expect("Not expecting None");
      let upper_desired = extern_api::OUT_upper_desired_temp.lock().unwrap().expect("Not expecting none");
      let lower_desired = extern_api::OUT_lower_desired_temp.lock().unwrap().expect("Not expecting None");
      let interface_failure = extern_api::OUT_interface_failure.lock().unwrap().expect("Not expecting None");
  
      unsafe {
  
          // [CheckPost]: invoke the oracle function
          assert!(GUMBOX::initialize_IEP_Post(
              display_temp,
              interface_failure,
              lower_desired,
              regulator_status,
              upper_desired
          ));
  
          // example of manual testing
          assert!(regulator_status == Status::Init_Status);
          assert!(display_temp == Temp_i::default());
          assert!(upper_desired == Temp_i::default());
          assert!(lower_desired == Temp_i::default());
          assert!(interface_failure == Failure_Flag_i::default());
      }
  }
   */

  #[test]
  #[serial]
  fn test_initialization() {
    unsafe {
      crate::thermostat_mt_mmi_mmi_initialize();
    }
  }

  /*
  fn test_compute_normal() {
      // generate values for the incoming ports and state variables
      let api_current_tempWstatus = TempWstatus_i {
          degrees: 99,
          status: ValueStatus::Valid,
      };
      let api_lower_desired_tempWstatus = TempWstatus_i {
          degrees: 98,
          status: ValueStatus::Valid,
      };
      let api_regulator_mode = Regulator_Mode::Normal_Regulator_Mode;
      let api_upper_desired_tempWstatus = TempWstatus_i {
          degrees: 101,
          status: ValueStatus::Valid,
      };
      let Old_example_state_variable: i32 = 42;
  
      // [CheckPre]: check/filter based on pre-condition.
      if (!GUMBOX::compute_CEP_Pre(
          api_current_tempWstatus,
          api_lower_desired_tempWstatus,
          api_regulator_mode,
          api_upper_desired_tempWstatus,
      )) {
          if failOnUnsatPrecondition {
              assert!(false, "MRI precondition failed");
          }
      } else {
          // [PutInPorts]: put values on the input ports
          *extern_api::IN_regulator_mode.lock().unwrap() = Some(api_regulator_mode);
          *extern_api::IN_lower_desired_tempWstatus.lock().unwrap() = Some(api_lower_desired_tempWstatus);
          *extern_api::IN_upper_desired_tempWstatus.lock().unwrap() = Some(api_upper_desired_tempWstatus);
          *extern_api::IN_current_tempWstatus.lock().unwrap() = Some(api_current_tempWstatus);
  
          unsafe {
            crate::thermostat_rt_mri_mri_initialize();

              // [SetInStateVars]: set the pre-state values of state variables
  
              // [InvokeEntryPoint]: invoke the entry point test method
              crate::thermostat_rt_mri_mri_timeTriggered();
  
              // [RetrieveOutState]: retrieve values of the output ports via get operations and GUMBO declared local state variable
              let api_regulator_status = extern_api::OUT_regulator_status.lock().unwrap().expect("Not expecting None");
              let api_displayed_temp = extern_api::OUT_displayed_temp.lock().unwrap().expect("Not expecting None");
              let api_upper_desired_temp = extern_api::OUT_upper_desired_temp.lock().unwrap().expect("Not expecting none");
              let api_lower_desired_temp = extern_api::OUT_lower_desired_temp.lock().unwrap().expect("Not expecting None");
              let api_interface_failure = extern_api::OUT_interface_failure.lock().unwrap().expect("Not expecting None");
  
              // [CheckPost]: invoke the oracle function
              assert!(GUMBOX::compute_CEP_Post(
                  api_current_tempWstatus,
                  api_lower_desired_tempWstatus,
                  api_regulator_mode,
                  api_upper_desired_tempWstatus,
                  api_displayed_temp,
                  api_interface_failure,
                  api_lower_desired_temp,
                  api_regulator_status,
                  api_upper_desired_temp
              ));
  
              // example of manual testing
              assert!(!api_interface_failure.flag);
              assert!(api_regulator_status == Status::On_Status);
              assert!(api_displayed_temp.degrees == api_current_tempWstatus.degrees);
              assert!(api_lower_desired_temp.degrees == api_lower_desired_tempWstatus.degrees);
              assert!(api_upper_desired_temp.degrees == api_upper_desired_tempWstatus.degrees);
          }
      }
  }
   */
  #[test]
  #[serial]
  fn test_compute() {
    unsafe {
      crate::thermostat_mt_mmi_mmi_initialize();

      crate::thermostat_mt_mmi_mmi_timeTriggered();
    }
  }
}
