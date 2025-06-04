// This file will not be overwritten if codegen is rerun

#[cfg(test)]
mod tests {
  // NOTE: need to run tests sequentially to prevent race conditions
  //       on the app and the testing apis which are static
  use serial_test::serial;

  use crate::compute_api;
  use crate::init_api;
  use crate::app;

  use crate::bridge::extern_c_api as extern_api;
  use crate::data::*;
  use crate::data::Isolette_Data_Model::*;
  use crate::bridge::thermostat_rt_mri_mri_GUMBOX as GUMBOX;

  const failOnUnsatPrecondition: bool = false;

  
  #[test]
  #[serial]
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
  
  #[test]
  #[serial]
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
  
  #[test]
  #[serial]
  fn test_compute_interface_failure() {
      // generate values for the incoming ports and state variables
      let api_current_tempWstatus = TempWstatus_i {
          degrees: 99,
          status: ValueStatus::Valid,
      };
      let api_lower_desired_tempWstatus = TempWstatus_i {
          degrees: 98,
          status: ValueStatus::Invalid,
      };
      let api_regulator_mode = Regulator_Mode::Normal_Regulator_Mode;
      let api_upper_desired_tempWstatus = TempWstatus_i {
          degrees: 101,
          status: ValueStatus::Valid,
      };
      let Old_example_state_variable: i32 = 19;
  
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
  
              // [RetrieveOutState]: retrieve values of the output ports via get operations and GUMBO declared local state variable// get values of outgoing ports
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
              assert!(api_interface_failure.flag);
              assert!(api_regulator_status == Status::On_Status);
              assert!(api_displayed_temp.degrees == 99);
              assert!(api_lower_desired_temp.degrees == Temp_i::default().degrees);
              assert!(api_upper_desired_temp.degrees == Temp_i::default().degrees);
          }
        }
  }
}

