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
  use crate::bridge::thermostat_rt_mhs_mhs_GUMBOX as GUMBOX;
  use crate::data::*;
  use crate::data::Isolette_Data_Model::*;

  const failOnUnsatPrecondition: bool = false;

  #[test]
  #[serial]
  fn test_initialization_REQ_MHS_1() {
      // [InvokeEntryPoint]: invoke the entry point test method
  
      unsafe {
          app.initialize(&mut init_api);
      }
  
      // [RetrieveOutState]: retrieve values of the output ports via get operations and GUMBO declared local state variable
      let heat_control = extern_api::OUT_heat_control
          .lock()
          .unwrap()
          .expect("Not expecting None");
  
      unsafe {
          let lastCmd = app.lastCmd;
  
          // [CheckPost]: invoke the oracle function
          assert!(GUMBOX::initialize_IEP_Post(
              heat_control,
              lastCmd));
  
          // example of manual testing
          assert!(heat_control == On_Off::Off);
          assert!(lastCmd == heat_control);
      }
  }

  #[test]
  #[serial]
  fn test_compute_REQ_MHS_2() {
      // generate values for the incoming ports and state variables
      let current_tempWstatus = TempWstatus_i {
          degrees: 96,
          status: ValueStatus::Valid,
      };
      let lower_desired_temp = Temp_i { degrees: 97 };
      let upper_desired_temp = Temp_i { degrees: 101 };
      let regulator_mode = Regulator_Mode::Normal_Regulator_Mode;
      let Old_lastCmd: On_Off = On_Off::Off;
  
      // [CheckPre]: check/filter based on pre-condition.
      if (!GUMBOX::compute_CEP_Pre(
          Old_lastCmd,
          current_tempWstatus,
          lower_desired_temp,
          regulator_mode,
          upper_desired_temp)) { 
              if failOnUnsatPrecondition {
                  assert!(false, "MRI precondition failed");
              }           
      } else {
          // [PutInPorts]: put values on the input ports
          *extern_api::IN_current_tempWstatus.lock().unwrap() = Some(current_tempWstatus);
          *extern_api::IN_lower_desired_temp.lock().unwrap() = Some(lower_desired_temp);
          *extern_api::IN_upper_desired_temp.lock().unwrap() = Some(upper_desired_temp);
          *extern_api::IN_regulator_mode.lock().unwrap() = Some(regulator_mode);
  
          unsafe {
              // [SetInStateVars]: set the pre-state values of state variables
              app.lastCmd = Old_lastCmd;
  
              // [InvokeEntryPoint]: invoke the entry point test method
              app.timeTriggered(&mut compute_api);
  
              // [RetrieveOutState]: retrieve values of the output ports via get operations and GUMBO declared local state variable
              let api_heat_control = extern_api::OUT_heat_control.lock().unwrap().expect("Not expecting None");
              let lastCmd = app.lastCmd;
  
              // [CheckPost]: invoke the oracle function
              assert!(GUMBOX::compute_CEP_Post(
                  Old_lastCmd,
                  lastCmd,
                  current_tempWstatus,
                  lower_desired_temp,
                  regulator_mode,
                  upper_desired_temp,
                  api_heat_control));
  
              // example of manual testing
              assert!(api_heat_control == On_Off::Onn);
              assert!(lastCmd == api_heat_control);
          }
      }
  }


  #[test]
  #[serial]
  fn test_compute_REQ_MHS_3() {
      // generate values for the incoming ports and state variables
      let current_tempWstatus = TempWstatus_i { degrees: 102, status: ValueStatus::Valid };
      let lower_desired_temp = Temp_i { degrees: 97 };
      let upper_desired_temp = Temp_i { degrees: 101 };
      let regulator_mode = Regulator_Mode::Normal_Regulator_Mode;
      let Old_lastCmd: On_Off = On_Off::Onn;
  
      // [CheckPre]: check/filter based on pre-condition.
      if (!GUMBOX::compute_CEP_Pre(
          Old_lastCmd,
          current_tempWstatus,
          lower_desired_temp,
          regulator_mode,
          upper_desired_temp)) { 
              if failOnUnsatPrecondition {
                  assert!(false, "MRI precondition failed");
              }           
      } else {
          // [PutInPorts]: put values on the input ports
          *extern_api::IN_current_tempWstatus.lock().unwrap() = Some(current_tempWstatus);
          *extern_api::IN_lower_desired_temp.lock().unwrap() = Some(lower_desired_temp);
          *extern_api::IN_upper_desired_temp.lock().unwrap() = Some(upper_desired_temp);
          *extern_api::IN_regulator_mode.lock().unwrap() = Some(regulator_mode);
  
          unsafe {
              // [SetInStateVars]: set the pre-state values of state variables
              app.lastCmd = Old_lastCmd;
  
              // [InvokeEntryPoint]: invoke the entry point test method
              app.timeTriggered(&mut compute_api);
  
              // [RetrieveOutState]: retrieve values of the output ports via get operations and GUMBO declared local state variable
              let api_heat_control = extern_api::OUT_heat_control.lock().unwrap().expect("Not expecting None");
              let lastCmd = app.lastCmd;
  
              // [CheckPost]: invoke the oracle function
              // [CheckPost]: invoke the oracle function
              assert!(GUMBOX::compute_CEP_Post(
                  Old_lastCmd,
                  lastCmd,
                  current_tempWstatus,
                  lower_desired_temp,
                  regulator_mode,
                  upper_desired_temp,
                  api_heat_control));
  
              // example of manual testing
              assert!(api_heat_control == On_Off::Off);
              assert!(lastCmd == api_heat_control);
          }
      }
  }
  
  #[test]
  #[serial]
  fn test_compute_REQ_MHS_4() {
      // generate values for the incoming ports and state variables
      let current_tempWstatus = TempWstatus_i { degrees: 98, status: ValueStatus::Valid };
      let lower_desired_temp = Temp_i { degrees: 97 };
      let upper_desired_temp = Temp_i { degrees: 101 };
      let regulator_mode = Regulator_Mode::Normal_Regulator_Mode;
      let Old_lastCmd: On_Off = On_Off::Onn;
  
      // [CheckPre]: check/filter based on pre-condition.
      if (!GUMBOX::compute_CEP_Pre(
          Old_lastCmd,
          current_tempWstatus,
          lower_desired_temp,
          regulator_mode,
          upper_desired_temp)) { 
              if failOnUnsatPrecondition {
                  assert!(false, "MRI precondition failed");
              }           
      } else {
          // [PutInPorts]: put values on the input ports
          *extern_api::IN_current_tempWstatus.lock().unwrap() = Some(current_tempWstatus);
          *extern_api::IN_lower_desired_temp.lock().unwrap() = Some(lower_desired_temp);
          *extern_api::IN_upper_desired_temp.lock().unwrap() = Some(upper_desired_temp);
          *extern_api::IN_regulator_mode.lock().unwrap() = Some(regulator_mode);
  
          unsafe {
              // [SetInStateVars]: set the pre-state values of state variables
              app.lastCmd = Old_lastCmd;
  
              // [InvokeEntryPoint]: invoke the entry point test method
              app.timeTriggered(&mut compute_api);
  
              // [RetrieveOutState]: retrieve values of the output ports via get operations and GUMBO declared local state variable
              let api_heat_control = extern_api::OUT_heat_control.lock().unwrap().expect("Not expecting None");
              let lastCmd = app.lastCmd;
  
              // [CheckPost]: invoke the oracle function
              // [CheckPost]: invoke the oracle function
              assert!(GUMBOX::compute_CEP_Post(
                  Old_lastCmd,
                  lastCmd,
                  current_tempWstatus,
                  lower_desired_temp,
                  regulator_mode,
                  upper_desired_temp,
                  api_heat_control));
  
              // example of manual testing
              assert!(api_heat_control == Old_lastCmd);
              assert!(lastCmd == api_heat_control);
          }
      }
  }
  
  #[test]
  #[serial]
  fn test_compute_REQ_MHS_5() {
      // generate values for the incoming ports and state variables
      let current_tempWstatus = TempWstatus_i { degrees: 98, status: ValueStatus::Valid };
      let lower_desired_temp = Temp_i { degrees: 97 };
      let upper_desired_temp = Temp_i { degrees: 101 };
      let regulator_mode = Regulator_Mode::Failed_Regulator_Mode;
      let Old_lastCmd: On_Off = On_Off::Onn;
  
      // [CheckPre]: check/filter based on pre-condition.
      if (!GUMBOX::compute_CEP_Pre(
          Old_lastCmd,
          current_tempWstatus,
          lower_desired_temp,
          regulator_mode,
          upper_desired_temp)) { 
              if failOnUnsatPrecondition {
                  assert!(false, "MRI precondition failed");
              }           
      } else {
          // [PutInPorts]: put values on the input ports
          *extern_api::IN_current_tempWstatus.lock().unwrap() = Some(current_tempWstatus);
          *extern_api::IN_lower_desired_temp.lock().unwrap() = Some(lower_desired_temp);
          *extern_api::IN_upper_desired_temp.lock().unwrap() = Some(upper_desired_temp);
          *extern_api::IN_regulator_mode.lock().unwrap() = Some(regulator_mode);
  
          unsafe {
              // [SetInStateVars]: set the pre-state values of state variables
              app.lastCmd = Old_lastCmd;
  
              // [InvokeEntryPoint]: invoke the entry point test method
              app.timeTriggered(&mut compute_api);
  
              // [RetrieveOutState]: retrieve values of the output ports via get operations and GUMBO declared local state variable
              let api_heat_control = extern_api::OUT_heat_control.lock().unwrap().expect("Not expecting None");
              let lastCmd = app.lastCmd;
  
              // [CheckPost]: invoke the oracle function
              // [CheckPost]: invoke the oracle function
              assert!(GUMBOX::compute_CEP_Post(
                  Old_lastCmd,
                  lastCmd,
                  current_tempWstatus,
                  lower_desired_temp,
                  regulator_mode,
                  upper_desired_temp,
                  api_heat_control));
  
              // example of manual testing
              assert!(api_heat_control == On_Off::Off);
              assert!(lastCmd == api_heat_control);
          }
      }
  }  
}
