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
    crate::monitor_process_monitor_thread_initialize();
}

  #[test]
  #[serial]
  fn test_compute() {
    crate::monitor_process_monitor_thread_initialize();
    crate::monitor_process_monitor_thread_timeTriggered();
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
    api_ArduPilot_ArduPilot_EthernetFramesTx: generators::option_strategy_default(generators::SW_StructuredEthernetMessage_i_strategy_default()),
    api_Firewall_Firewall_EthernetFramesRxOut: generators::option_strategy_default(generators::SW_StructuredEthernetMessage_i_strategy_default()),
    api_Firewall_Firewall_EthernetFramesTxOut: generators::option_strategy_default(generators::SW_StructuredEthernetMessage_i_strategy_default()),
    api_LowLevelEthernetDriver_LowLevelEthernetDriver_EthernetFramesRx: generators::option_strategy_default(generators::SW_StructuredEthernetMessage_i_strategy_default())
  }
}
