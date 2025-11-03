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
    crate::operator_interface_oip_oit_initialize();
}

  #[test]
  #[serial]
  fn test_compute() {
    crate::operator_interface_oip_oit_initialize();

    // populate incoming data ports
    test_apis::put_regulator_status(Isolette_Data_Model::Status::default());
    test_apis::put_monitor_status(Isolette_Data_Model::Status::default());
    test_apis::put_display_temperature(Isolette_Data_Model::Temp_i::default());
    test_apis::put_alarm_control(Isolette_Data_Model::On_Off::default());

    crate::operator_interface_oip_oit_timeTriggered();
  }
}

mod GUMBOX_tests {
  use serial_test::serial;
  use proptest::prelude::*;

  use crate::test::util::*;
  use crate::testInitializeCB_macro;
  use crate::testComputeCB_macro;

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
    api_alarm_control: generators::Isolette_Data_Model_On_Off_strategy_default(),
    api_display_temperature: generators::Isolette_Data_Model_Temp_i_strategy_default(),
    api_monitor_status: generators::Isolette_Data_Model_Status_strategy_default(),
    api_regulator_status: generators::Isolette_Data_Model_Status_strategy_default()
  }
}
