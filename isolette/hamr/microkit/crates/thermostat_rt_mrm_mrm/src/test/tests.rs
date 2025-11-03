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
    crate::thermostat_rt_mrm_mrm_initialize();
}

  #[test]
  #[serial]
  fn test_compute() {
    crate::thermostat_rt_mrm_mrm_initialize();

    // populate incoming data ports
    test_apis::put_current_tempWstatus(Isolette_Data_Model::TempWstatus_i::default());
    test_apis::put_interface_failure(Isolette_Data_Model::Failure_Flag_i::default());
    test_apis::put_internal_failure(Isolette_Data_Model::Failure_Flag_i::default());

    crate::thermostat_rt_mrm_mrm_timeTriggered();
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
    api_interface_failure: generators::Isolette_Data_Model_Failure_Flag_i_strategy_default(),
    api_internal_failure: generators::Isolette_Data_Model_Failure_Flag_i_strategy_default()
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
    In_lastRegulatorMode: generators::Isolette_Data_Model_Regulator_Mode_strategy_default(),
    api_current_tempWstatus: generators::Isolette_Data_Model_TempWstatus_i_strategy_default(),
    api_interface_failure: generators::Isolette_Data_Model_Failure_Flag_i_strategy_default(),
    api_internal_failure: generators::Isolette_Data_Model_Failure_Flag_i_strategy_default()
  }
}
