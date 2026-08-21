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

  use data::*;

  // ---------------------------------------------------------------------------
  //  Custom input strategies
  //
  //  The model's datatype invariants are not carried into the generated Rust:
  //  generators.rs draws Temperature.degrees from any::<i32>(), and CEP-Pre checks
  //  only the currentTemp integration assume plus the a1/a2 compute assumes.  So
  //  the default strategies produce set points the model forbids -- in particular
  //  low > high, for which TC_Req_01 (temp < low => fan Off) and TC_Req_02
  //  (temp > high => fan On) both fire and no implementation can satisfy the
  //  postcondition.  tcp_tct_app's compute assumes low <= high for the same reason,
  //  which keeps Verus happy but is ghost, so it does not reach these tests.
  //
  //  The strategies below restate the model's constraints so that the generated
  //  inputs are ones the system actually contracts to accept.  They live here
  //  because tests.rs is preserved across regen and clean.cmd; generators.rs is
  //  overwritten, so they cannot go there.
  // ---------------------------------------------------------------------------

  //  TempControl_SysVerif.sysml, TempControl's GUMBO functions:
  //    def validRange(temp) := temp >= -129 [i32] & temp <= 134 [i32]
  const validRangeMin: i32 = -129;
  const validRangeMax: i32 = 134;

  //  TempControl_SysVerif.sysml, SetPoint's SetPoint_Data_Invariant:
  //    low.degrees >= 50 [i32] & high.degrees <= 110 [i32] & low.degrees <= high.degrees
  const setPointLowMin: i32 = 50;
  const setPointHighMax: i32 = 110;

  /// Temperature restricted to validRange, the integration constraint carried by
  /// TempControl's currentTemp port.  Without it nearly every generated reading is
  /// rejected by CEP-Pre and the run burns its rejection budget rather than
  /// exercising the contract.  Also subsumes Temperature's AbsZero invariant
  /// (degrees > -460).
  fn validRange_temperature_strategy() -> impl Strategy<Value = TempControl_SysVerif::Temperature> {
    generators::TempControl_SysVerif_Temperature_strategy_cust(
      generators::i32_strategy_cust(validRangeMin..=validRangeMax),
      generators::TempControl_SysVerif_TempUnit_strategy_default())
  }

  /// SetPoint satisfying SetPoint_Data_Invariant.  low and high cannot be drawn
  /// independently -- two ranges still yield low > high half the time -- so high is
  /// drawn from a range that starts at the low already chosen.  prop_flat_map rather
  /// than prop_filter: this generates only valid set points instead of discarding
  /// invalid ones against the rejection budget.
  fn validSetPoint_strategy() -> impl Strategy<Value = TempControl_SysVerif::SetPoint> {
    (setPointLowMin..=setPointHighMax).prop_flat_map(|low| {
      (Just(low), low..=setPointHighMax).prop_map(|(low, high)| TempControl_SysVerif::SetPoint {
        low: fahrenheit(low),
        high: fahrenheit(high)
      })
    })
  }

  fn fahrenheit(degrees: i32) -> TempControl_SysVerif::Temperature {
    TempControl_SysVerif::Temperature { degrees, unit: TempControl_SysVerif::TempUnit::Fahrenheit }
  }

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
    api_currentTemp: generators::option_strategy_default(validRange_temperature_strategy()),
    api_fanAck: generators::option_strategy_default(generators::TempControl_SysVerif_FanAck_strategy_default()),
    api_setPoint: generators::option_strategy_default(validSetPoint_strategy())
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
    In_currentSetPoint: validSetPoint_strategy(),
    In_fanError: generators::bool_strategy_default(),
    In_latestTemp: validRange_temperature_strategy(),
    api_currentTemp: generators::option_strategy_default(validRange_temperature_strategy()),
    api_fanAck: generators::option_strategy_default(generators::TempControl_SysVerif_FanAck_strategy_default()),
    api_setPoint: generators::option_strategy_default(validSetPoint_strategy())
  }
}
