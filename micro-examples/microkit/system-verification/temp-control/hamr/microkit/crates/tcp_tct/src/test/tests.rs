// This file will not be overwritten if HAMR codegen is rerun

mod tests {
  // NOTE: need to run tests sequentially to prevent race conditions
  //       on the app and the testing apis which are static
  use serial_test::serial;

  use crate::test::util::*;
  use data::*;

  #[test]
  #[serial]
  fn test_initialization() {
    crate::tcp_tct_initialize();
}

  #[test]
  #[serial]
  fn test_compute() {
    crate::tcp_tct_initialize();
    crate::tcp_tct_timeTriggered();
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
    api_currentTemp: generators::option_strategy_default(generators::TempControl_SysVerif_Temperature_strategy_default()),
    api_fanAck: generators::option_strategy_default(generators::TempControl_SysVerif_FanAck_strategy_default()),
    api_setPoint: generators::option_strategy_default(generators::TempControl_SysVerif_SetPoint_strategy_default())
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
    In_currentFanState: generators::TempControl_SysVerif_FanCmd_strategy_default(),
    In_currentSetPoint: generators::TempControl_SysVerif_SetPoint_strategy_default(),
    In_fanError: generators::bool_strategy_default(),
    In_latestTemp: generators::TempControl_SysVerif_Temperature_strategy_default(),
    api_currentTemp: generators::option_strategy_default(generators::TempControl_SysVerif_Temperature_strategy_default()),
    api_fanAck: generators::option_strategy_default(generators::TempControl_SysVerif_FanAck_strategy_default()),
    api_setPoint: generators::option_strategy_default(generators::TempControl_SysVerif_SetPoint_strategy_default())
  }
}
