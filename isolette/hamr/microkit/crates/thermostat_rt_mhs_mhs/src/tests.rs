#![cfg(test)]

// This file will not be overwritten if codegen is rerun

mod GUMBOX_tests {
  use serial_test::serial;
  use proptest::prelude::*;

  use crate::bridge::test_api as test_api;
  use crate::testInitializeCB_macro;
  use crate::testComputeCB_macro;
  use crate::testComputeCBwLV_macro;
    
  // number of valid (i.e., non-rejected) test cases that must be executed for the compute method.
  const numValidComputeTestCases: u32 = 100;

  // how many total test cases (valid + rejected) that may be attempted.
  //   0 means all inputs must satisfy the precondition (if present), 
  //   5 means at most 5 rejected inputs are allowed per valid test case
  const computeRejectRatio: u32 = 5;

  const verbosity: u32 = 2;

  testInitializeCB_macro! {
    prop_testInitializeCB_macro, // test name
    config: ProptestConfig { // proptest configuration, built by overriding fields from default config
        cases: numValidComputeTestCases,
        max_global_rejects: numValidComputeTestCases * computeRejectRatio,
        verbose: verbosity,
        ..ProptestConfig::default()
    }
  }

  testComputeCB_macro! {
    prop_testComputeCB_macro, // test name
    config: ProptestConfig { // proptest configuration, built by overriding fields from default config
        cases: numValidComputeTestCases,
        max_global_rejects: numValidComputeTestCases * computeRejectRatio,
        verbose: verbosity,
        ..ProptestConfig::default()
    },
    // strategies for generating each component input
    /*
    In_lastCmd: test_api::Isolette_Data_Model_On_Off_strategy_default(),
    api_current_tempWstatus: test_api::Isolette_Data_Model_TempWstatus_i_strategy_default(),
    api_lower_desired_temp: test_api::Isolette_Data_Model_Temp_i_strategy_default(),
    api_regulator_mode: test_api::Isolette_Data_Model_Regulator_Mode_strategy_default(),
    api_upper_desired_temp: test_api::Isolette_Data_Model_Temp_i_strategy_default()
    */
    api_current_tempWstatus: test_api::Isolette_Data_Model_TempWstatus_i_stategy_cust(
        95..=103, 
        test_api::Isolette_Data_Model_ValueStatus_strategy_default()),
    api_lower_desired_temp: test_api::Isolette_Data_Model_Temp_i_stategy_cust(94..=105),
    api_regulator_mode: test_api::Isolette_Data_Model_Regulator_Mode_strategy_default(),
    api_upper_desired_temp: test_api::Isolette_Data_Model_Temp_i_stategy_cust(94..=105)
  }
  
  testComputeCBwLV_macro! {
    prop_testComputeCBwLV, // test name
    config: ProptestConfig { // proptest configuration, built by overriding fields from default config
        cases: numValidComputeTestCases,
        max_global_rejects: numValidComputeTestCases * computeRejectRatio,
        verbose: verbosity,
        ..ProptestConfig::default()
    },
    // strategies for generating each component input
    /*
    In_lastCmd: test_api::Isolette_Data_Model_On_Off_strategy_default(),
    api_current_tempWstatus: test_api::Isolette_Data_Model_TempWstatus_i_strategy_default(),
    api_lower_desired_temp: test_api::Isolette_Data_Model_Temp_i_strategy_default(),
    api_regulator_mode: test_api::Isolette_Data_Model_Regulator_Mode_strategy_default(),
    api_upper_desired_temp: test_api::Isolette_Data_Model_Temp_i_strategy_default()
    */
    In_lastCmd: test_api::Isolette_Data_Model_On_Off_strategy_default(),
    api_current_tempWstatus: test_api::Isolette_Data_Model_TempWstatus_i_strategy_default(),
    api_lower_desired_temp: test_api::Isolette_Data_Model_Temp_i_stategy_cust(94..=105),
    api_regulator_mode: test_api::Isolette_Data_Model_Regulator_Mode_strategy_default(),
    api_upper_desired_temp: test_api::Isolette_Data_Model_Temp_i_stategy_cust(94..=105)
  }
}

mod tests {
  // NOTE: need to run tests sequentially to prevent race conditions
  //       on the app and the testing apis which are static
  use serial_test::serial;

  use data::*;
  use crate::bridge::thermostat_rt_mhs_mhs_GUMBOX as GUMBOX;
  use crate::bridge::test_api as test_api;
  const failOnUnsatPrecondition: bool = false;

  use data::Isolette_Data_Model::*;

  #[test]
  #[serial]
  fn test_initialization_REQ_MHS_1() {
      // [InvokeEntryPoint]: invoke the entry point test method

      unsafe {
        crate::thermostat_rt_mhs_mhs_initialize();
      }
  
      // [RetrieveOutState]: retrieve values of the output ports via get operations and GUMBO declared local state variable
      let heat_control = test_api::get_heat_control();
  
      unsafe {
          let lastCmd = test_api::get_lastCmd();
  
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
          test_api::put_current_tempWstatus(current_tempWstatus);
          test_api::put_lower_desired_temp(lower_desired_temp);
          test_api::put_upper_desired_temp(upper_desired_temp);
          test_api::put_regulator_mode(regulator_mode);
  
          unsafe {
            // initialize the app
            crate::thermostat_rt_mhs_mhs_initialize();

              // [SetInStateVars]: set the pre-state values of state variables
            set_lastCmd(Old_lastCmd);
            //app.unwrap().lastCmd = Old_lastCmd;

              // [InvokeEntryPoint]: invoke the entry point test method
            //app.unwrap().timeTriggered(&mut compute_api);
            crate::thermostat_rt_mhs_mhs_timeTriggered();
  
              // [RetrieveOutState]: retrieve values of the output ports via get operations and GUMBO declared local state variable
            let api_heat_control = test_api::get_heat_control();
            //let lastCmd = app.unwrap().lastCmd;
            let lastCmd = test_api::get_lastCmd();

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
          test_api::put_current_tempWstatus(current_tempWstatus);
          test_api::put_lower_desired_temp(lower_desired_temp);
          test_api::put_upper_desired_temp(upper_desired_temp);
          test_api::put_regulator_mode(regulator_mode);
  
          unsafe {
            // initialize the app
            crate::thermostat_rt_mhs_mhs_initialize();

              // [SetInStateVars]: set the pre-state values of state variables
              //app.unwrap().lastCmd = Old_lastCmd;
              set_lastCmd(Old_lastCmd);
  
              // [InvokeEntryPoint]: invoke the entry point test method
              //app.timeTriggered(&mut compute_api);
              crate::thermostat_rt_mhs_mhs_timeTriggered();
  
              // [RetrieveOutState]: retrieve values of the output ports via get operations and GUMBO declared local state variable
              //let api_heat_control = extern_api::OUT_heat_control.lock().unwrap().expect("Not expecting None");
              //let lastCmd = app.unwrap().lastCmd;
              let api_heat_control = test_api::get_heat_control();
              let lastCmd = test_api::get_lastCmd();
  
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
          test_api::put_current_tempWstatus(current_tempWstatus);
          test_api::put_lower_desired_temp(lower_desired_temp);
          test_api::put_upper_desired_temp(upper_desired_temp);
          test_api::put_regulator_mode(regulator_mode);
  
          unsafe {
            // initialize the app
            crate::thermostat_rt_mhs_mhs_initialize();

              // [SetInStateVars]: set the pre-state values of state variables
              //app.unwrap().lastCmd = Old_lastCmd;
              set_lastCmd(Old_lastCmd);
  
              // [InvokeEntryPoint]: invoke the entry point test method
              //app.timeTriggered(&mut compute_api);
              crate::thermostat_rt_mhs_mhs_timeTriggered();
  
              // [RetrieveOutState]: retrieve values of the output ports via get operations and GUMBO declared local state variable
              //let api_heat_control = extern_api::OUT_heat_control.lock().unwrap().expect("Not expecting None");
              //let lastCmd = app.unwrap().lastCmd;
              let api_heat_control =  test_api::get_heat_control();
              let lastCmd = test_api::get_lastCmd();
  
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
          test_api::put_current_tempWstatus(current_tempWstatus);
          test_api::put_lower_desired_temp(lower_desired_temp);
          test_api::put_upper_desired_temp(upper_desired_temp);
          test_api::put_regulator_mode(regulator_mode);
    
          unsafe {
            crate::thermostat_rt_mhs_mhs_initialize();

              // [SetInStateVars]: set the pre-state values of state variables
              //app.unwrap().lastCmd = Old_lastCmd;
              set_lastCmd(Old_lastCmd);
  
              // [InvokeEntryPoint]: invoke the entry point test method
              //app.timeTriggered(&mut compute_api);
              crate::thermostat_rt_mhs_mhs_timeTriggered();
  
              // [RetrieveOutState]: retrieve values of the output ports via get operations and GUMBO declared local state variable
              //let api_heat_control = extern_api::OUT_heat_control.lock().unwrap().expect("Not expecting None");
              //let lastCmd = app.unwrap().lastCmd;
              let api_heat_control =  test_api::get_heat_control();
              let lastCmd = test_api::get_lastCmd();
  
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

  fn set_lastCmd(value: On_Off) {
    unsafe {
        match &mut crate::app {
            Some(inner) => inner.lastCmd = value,
            None => panic!("The app is None")
        }
    }
  }
}
