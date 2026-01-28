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
    crate::consumer_consumer_initialize();
}

  #[test]
  #[serial]
  fn test_compute() {
    crate::consumer_consumer_initialize();

    // populate incoming data ports
    test_apis::put_myArrayInt32_DataPort([0; Gubmo_Structs_Arrays::Gubmo_Structs_Arrays_MyArrayInt32_DIM_0]);
    test_apis::put_myArrayStruct_DataPort([Gubmo_Structs_Arrays::MyStruct2_i::default(); Gubmo_Structs_Arrays::Gubmo_Structs_Arrays_MyArrayStruct_DIM_0]);
    test_apis::put_myStructArray_DataPort(Gubmo_Structs_Arrays::MyStructArray_i::default());

    crate::consumer_consumer_timeTriggered();
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
    api_myArrayInt32_EventDataPort: generators::option_strategy_default(generators::Gubmo_Structs_Arrays_MyArrayInt32_strategy_default()),
    api_myArrayStruct_EventDataPort: generators::option_strategy_default(generators::Gubmo_Structs_Arrays_MyArrayStruct_strategy_default()),
    api_myStructArray_EventDataPort: generators::option_strategy_default(generators::Gubmo_Structs_Arrays_MyStructArray_i_strategy_default()),
    api_myArrayInt32_DataPort: generators::Gubmo_Structs_Arrays_MyArrayInt32_strategy_default(),
    api_myArrayStruct_DataPort: generators::Gubmo_Structs_Arrays_MyArrayStruct_strategy_default(),
    api_myStructArray_DataPort: generators::Gubmo_Structs_Arrays_MyStructArray_i_strategy_default()
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
    In_myArrayInt32_StateVar: generators::Gubmo_Structs_Arrays_MyArrayInt32_strategy_default(),
    In_myArrayStruct_StateVar: generators::Gubmo_Structs_Arrays_MyArrayStruct_strategy_default(),
    In_myStructArray_StateVar: generators::Gubmo_Structs_Arrays_MyStructArray_i_strategy_default(),
    api_myArrayInt32_EventDataPort: generators::option_strategy_default(generators::Gubmo_Structs_Arrays_MyArrayInt32_strategy_default()),
    api_myArrayStruct_EventDataPort: generators::option_strategy_default(generators::Gubmo_Structs_Arrays_MyArrayStruct_strategy_default()),
    api_myStructArray_EventDataPort: generators::option_strategy_default(generators::Gubmo_Structs_Arrays_MyStructArray_i_strategy_default()),
    api_myArrayInt32_DataPort: generators::Gubmo_Structs_Arrays_MyArrayInt32_strategy_default(),
    api_myArrayStruct_DataPort: generators::Gubmo_Structs_Arrays_MyArrayStruct_strategy_default(),
    api_myStructArray_DataPort: generators::Gubmo_Structs_Arrays_MyStructArray_i_strategy_default()
  }
}
