// This file will not be overwritten if codegen is rerun

mod tests {
  // NOTE: need to run tests sequentially to prevent race conditions
  //       on the app and the testing apis which are static
  use serial_test::serial;

  use crate::test::util::*;
  use data::*;

  #[test]
  #[serial]
  fn test_initialization() {
    crate::thermostat_mt_mmi_mmi_initialize();
}

  #[test]
  #[serial]
  fn test_compute() {
    crate::thermostat_mt_mmi_mmi_initialize();

    // populate incoming data ports
    test_apis::put_upper_alarm_tempWstatus(Isolette_Data_Model::TempWstatus_i::default());
    test_apis::put_lower_alarm_tempWstatus(Isolette_Data_Model::TempWstatus_i::default());
    test_apis::put_current_tempWstatus(Isolette_Data_Model::TempWstatus_i::default());
    test_apis::put_monitor_mode(Isolette_Data_Model::Monitor_Mode::default());

    crate::thermostat_mt_mmi_mmi_timeTriggered();
  }
}

mod GUMBOX_tests {
  use serial_test::serial;
  use proptest::prelude::*;

  use crate::test::util::*;
  use crate::testInitializeCB_macro;
  use crate::testComputeCB_macro;
    use crate::testComputeCBwGSV_macro;

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
    api_current_tempWstatus: generators::Isolette_Data_Model_TempWstatus_i_strategy_default(),
    api_lower_alarm_tempWstatus: generators::Isolette_Data_Model_TempWstatus_i_strategy_default(),
    api_monitor_mode: generators::Isolette_Data_Model_Monitor_Mode_strategy_default(),
    api_upper_alarm_tempWstatus: generators::Isolette_Data_Model_TempWstatus_i_strategy_default()
  }

  testComputeCBwGSV_macro! {
    prop_testComputeCBwGSV_macro, // test name
    config: ProptestConfig { // proptest configuration, built by overriding fields from default config
      cases: numValidComputeTestCases,
      max_global_rejects: numValidComputeTestCases * computeRejectRatio,
      verbose: verbosity,
      ..ProptestConfig::default()
    },
    // strategies for generating each component input
    In_lastCmd: generators::Isolette_Data_Model_On_Off_strategy_default(),
    api_current_tempWstatus: generators::Isolette_Data_Model_TempWstatus_i_strategy_default(),
    api_lower_alarm_tempWstatus: generators::Isolette_Data_Model_TempWstatus_i_strategy_default(),
    api_monitor_mode: generators::Isolette_Data_Model_Monitor_Mode_strategy_default(),
    api_upper_alarm_tempWstatus: generators::Isolette_Data_Model_TempWstatus_i_strategy_default()
  }
}
