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
    crate::seL4_TxFirewall_TxFirewall_initialize();
}

  #[test]
  #[serial]
  fn test_compute() {
    crate::seL4_TxFirewall_TxFirewall_initialize();
    crate::seL4_TxFirewall_TxFirewall_timeTriggered();
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

  testComputeCB_macro! {
    prop_testComputeCB_macro, // test name
    config: ProptestConfig { // proptest configuration, built by overriding fields from default config
      cases: numValidComputeTestCases,
      max_global_rejects: numValidComputeTestCases * computeRejectRatio,
      verbose: verbosity,
      ..ProptestConfig::default()
    },
    // strategies for generating each component input
    api_EthernetFramesTxIn0: test_api::option_strategy_default(test_api::SW_RawEthernetMessage_strategy_default()),
    api_EthernetFramesTxIn1: test_api::option_strategy_default(test_api::SW_RawEthernetMessage_strategy_default()),
    api_EthernetFramesTxIn2: test_api::option_strategy_default(test_api::SW_RawEthernetMessage_strategy_default()),
    api_EthernetFramesTxIn3: test_api::option_strategy_default(test_api::SW_RawEthernetMessage_strategy_default())
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
    api_EthernetFramesTxIn0: test_api::option_strategy_default(test_api::SW_RawEthernetMessage_strategy_default()),
    api_EthernetFramesTxIn1: test_api::option_strategy_default(test_api::SW_RawEthernetMessage_strategy_default()),
    api_EthernetFramesTxIn2: test_api::option_strategy_default(test_api::SW_RawEthernetMessage_strategy_default()),
    api_EthernetFramesTxIn3: test_api::option_strategy_default(test_api::SW_RawEthernetMessage_strategy_default())
  }
}
