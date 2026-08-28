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
    crate::monitor_process_monitor_thread_initialize();
  }

  #[test]
  #[serial]
  fn test_compute() {
    crate::monitor_process_monitor_thread_initialize();

    // populate incoming data ports
    test_apis::put_level(0);
    test_apis::put_boolean_value(false);
    test_apis::put_character_value(0);
    test_apis::put_signed_8_value(0);
    test_apis::put_signed_16_value(0);
    test_apis::put_unsigned_8_value(0);
    test_apis::put_unsigned_16_value(0);

    crate::monitor_process_monitor_thread_timeTriggered();
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
    api_pulse: generators::option_strategy_default(generators::u8_strategy_default()),
    api_flag: generators::option_strategy_default(generators::bool_strategy_default()),
    api_operating_state: generators::option_strategy_default(generators::Exhaustive_Monitor_OperatingState_strategy_default()),
    api_sample: generators::option_strategy_default(generators::i32_strategy_default()),
    api_samples: generators::option_strategy_default(generators::Exhaustive_Monitor_Samples_strategy_default()),
    api_telemetry: generators::option_strategy_default(generators::Exhaustive_Monitor_Telemetry_i_strategy_default()),
    api_boolean_value: generators::bool_strategy_default(),
    api_character_value: generators::u8_strategy_default(),
    api_level: generators::i32_strategy_default(),
    api_signed_16_value: generators::i16_strategy_default(),
    api_signed_8_value: generators::i8_strategy_default(),
    api_unsigned_16_value: generators::u16_strategy_default(),
    api_unsigned_8_value: generators::u8_strategy_default()
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
    In_sprevious_level: generators::i32_strategy_default(),
    api_pulse: generators::option_strategy_default(generators::u8_strategy_default()),
    api_flag: generators::option_strategy_default(generators::bool_strategy_default()),
    api_operating_state: generators::option_strategy_default(generators::Exhaustive_Monitor_OperatingState_strategy_default()),
    api_sample: generators::option_strategy_default(generators::i32_strategy_default()),
    api_samples: generators::option_strategy_default(generators::Exhaustive_Monitor_Samples_strategy_default()),
    api_telemetry: generators::option_strategy_default(generators::Exhaustive_Monitor_Telemetry_i_strategy_default()),
    api_boolean_value: generators::bool_strategy_default(),
    api_character_value: generators::u8_strategy_default(),
    api_level: generators::i32_strategy_default(),
    api_signed_16_value: generators::i16_strategy_default(),
    api_signed_8_value: generators::i8_strategy_default(),
    api_unsigned_16_value: generators::u16_strategy_default(),
    api_unsigned_8_value: generators::u8_strategy_default()
  }
}
