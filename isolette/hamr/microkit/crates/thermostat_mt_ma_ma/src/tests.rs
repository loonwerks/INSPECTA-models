#![cfg(test)]

// This file will not be overwritten if codegen is rerun

mod tests {
  // NOTE: need to run tests sequentially to prevent race conditions
  //       on the app and the testing apis which are static
  use serial_test::serial;

  use crate::bridge::test_api;
  use data::*;

  #[test]
  #[serial]
  fn test_initialization() {
    crate::thermostat_mt_ma_ma_initialize();
}

  #[test]
  #[serial]
  fn test_compute() {
    crate::thermostat_mt_ma_ma_initialize();

    // populate incoming data ports
    test_api::put_current_tempWstatus(Isolette_Data_Model::TempWstatus_i::default());
    test_api::put_lower_alarm_temp(Isolette_Data_Model::Temp_i::default());
    test_api::put_upper_alarm_temp(Isolette_Data_Model::Temp_i::default());
    test_api::put_monitor_mode(Isolette_Data_Model::Monitor_Mode::default());

    crate::thermostat_mt_ma_ma_timeTriggered();
  }
}

mod GUMBOX_tests {
  use serial_test::serial;
  use proptest::prelude::*;

  use crate::bridge::test_api;
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


  /* 
   MHS's precondition requires:
     - lower_alarm_temp ∈ [96, 101]
     - upper_alarm_temp ∈ [97, 102]  
   We generate only values within these ranges as all other values would be rejected.  
   Additionally, we narrow current_temp to focus on values near the alarm thresholds.
  */
  const current_temp_range: std::ops::RangeInclusive<i32> = 91..=107;
  const lower_alarm_temp_range: std::ops::RangeInclusive<i32> = 96..=101;
  const upper_alarm_temp_range: std::ops::RangeInclusive<i32> = 97..=102;

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
    api_current_tempWstatus: test_api::Isolette_Data_Model_TempWstatus_i_strategy_default(),
    api_lower_alarm_temp: test_api::Isolette_Data_Model_Temp_i_strategy_default(),
    api_monitor_mode: test_api::Isolette_Data_Model_Monitor_Mode_strategy_default(),
    api_upper_alarm_temp: test_api::Isolette_Data_Model_Temp_i_strategy_default()
    */
    api_current_tempWstatus: test_api::Isolette_Data_Model_TempWstatus_i_strategy_cust(
      current_temp_range, test_api::Isolette_Data_Model_ValueStatus_strategy_default()),
    api_lower_alarm_temp: test_api::Isolette_Data_Model_Temp_i_strategy_cust(lower_alarm_temp_range),
    api_monitor_mode: test_api::Isolette_Data_Model_Monitor_Mode_strategy_default(),
    api_upper_alarm_temp: test_api::Isolette_Data_Model_Temp_i_strategy_cust(upper_alarm_temp_range)
  }

  testComputeCBwLV_macro! {
    prop_testComputeCBwLV_macro, // test name
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
    api_lower_alarm_temp: test_api::Isolette_Data_Model_Temp_i_strategy_default(),
    api_monitor_mode: test_api::Isolette_Data_Model_Monitor_Mode_strategy_default(),
    api_upper_alarm_temp: test_api::Isolette_Data_Model_Temp_i_strategy_default()
    */
    In_lastCmd: test_api::Isolette_Data_Model_On_Off_strategy_default(),
    api_current_tempWstatus: test_api::Isolette_Data_Model_TempWstatus_i_strategy_cust(
      current_temp_range, test_api::Isolette_Data_Model_ValueStatus_strategy_default()),
    api_lower_alarm_temp: test_api::Isolette_Data_Model_Temp_i_strategy_cust(lower_alarm_temp_range),
    api_monitor_mode: test_api::Isolette_Data_Model_Monitor_Mode_strategy_default(),
    api_upper_alarm_temp: test_api::Isolette_Data_Model_Temp_i_strategy_cust(upper_alarm_temp_range)
  }
}
