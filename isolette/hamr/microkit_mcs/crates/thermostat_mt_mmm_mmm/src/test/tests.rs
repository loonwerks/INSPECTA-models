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
    crate::thermostat_mt_mmm_mmm_initialize();
}

  #[test]
  #[serial]
  fn test_compute() {
    crate::thermostat_mt_mmm_mmm_initialize();

    // populate incoming data ports
    test_apis::put_current_tempWstatus(Isolette_Data_Model::TempWstatus_i::default());
    test_apis::put_interface_failure(Isolette_Data_Model::Failure_Flag_i::default());
    test_apis::put_internal_failure(Isolette_Data_Model::Failure_Flag_i::default());

    crate::thermostat_mt_mmm_mmm_timeTriggered();
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
    In_lastMonitorMode: generators::Isolette_Data_Model_Monitor_Mode_strategy_default(),
    api_current_tempWstatus: generators::Isolette_Data_Model_TempWstatus_i_strategy_default(),
    api_interface_failure: generators::Isolette_Data_Model_Failure_Flag_i_strategy_default(),
    api_internal_failure: generators::Isolette_Data_Model_Failure_Flag_i_strategy_default()
  }
}

mod JH_tests {
  use serial_test::serial;

  use crate::compute_api;

  use crate::bridge::extern_c_api as extern_api;
  use crate::bridge::thermostat_mt_mmm_mmm_GUMBOX as GUMBOX;
  use data::*;
  use data::Isolette_Data_Model::*;

  const failOnUnsatPrecondition: bool = false;

  fn set_lastMonitorMode(last_monitor_mode: Monitor_Mode) {
    unsafe {
        match &mut crate::app {
            Some(inner) => inner.lastMonitorMode = last_monitor_mode,
            None => panic!("app is None")
        }
    }
  }

  fn get_lastMonitorMode() -> Monitor_Mode {
    unsafe {
        match &mut crate::app {
            Some(inner) => inner.lastMonitorMode,
            None => panic!("app is None")
        }
    }
  }

  fn setup_test_state(
      last_monitor_mode: Monitor_Mode,
      temp_status: ValueStatus,
      interface_failure_flag: bool,
      internal_failure_flag: bool,
  ) -> (TempWstatus_i, Failure_Flag_i, Failure_Flag_i) {
      let current_tempWstatus = TempWstatus_i {
          degrees: 96,
          status: temp_status,
      };
      let interface_failure = Failure_Flag_i {
          flag: interface_failure_flag,
      };
      let internal_failure = Failure_Flag_i {
          flag: internal_failure_flag,
      };

      *extern_api::IN_current_tempWstatus.lock().unwrap() = Some(current_tempWstatus);
      *extern_api::IN_interface_failure.lock().unwrap() = Some(interface_failure);
      *extern_api::IN_internal_failure.lock().unwrap() = Some(internal_failure);

      unsafe {
          set_lastMonitorMode(last_monitor_mode);
      }

      (current_tempWstatus, interface_failure, internal_failure)
  }

  macro_rules! run_mmm_CEP_test {
    ($name:ident, $mode:expr, $temp_status:expr, $interface_fail:expr, $internal_fail:expr, $expected_mode:expr) => {
        #[test]
        #[serial]
        fn $name() {
           crate::thermostat_mt_mmm_mmm_initialize();

           let in_last_monitor_mode = $mode;
           let (current_tempWstatus, interface_failure, internal_failure) =
                setup_test_state(in_last_monitor_mode, $temp_status, $interface_fail, $internal_fail);

           crate::thermostat_mt_mmm_mmm_timeTriggered();

           let (monitor_mode, last_monitor_mode) = retrieve_output_and_state();
           unsafe {
              assert!(GUMBOX::compute_CEP_Post(
                       in_last_monitor_mode,
                       last_monitor_mode,
                       current_tempWstatus,
                       interface_failure,
                       internal_failure,
                       monitor_mode
                  ));
                  assert_eq!(monitor_mode, $expected_mode);
                  assert_eq!(last_monitor_mode, $expected_mode);
              }
          }
      };
  }

    fn retrieve_output_and_state() -> (Monitor_Mode, Monitor_Mode) {
      let monitor_mode = extern_api::OUT_monitor_mode
          .lock()
          .unwrap()
          .expect("Not expecting None");

      unsafe {
          let last_monitor_mode = get_lastMonitorMode();
          (monitor_mode, last_monitor_mode)
      }
  }

  //===========================================================
  //  I n i t i a l i z e    Entry Point Tests
  //===========================================================

  #[test]
  #[serial]
  fn test_initialization_REQ_MMM_1() {
      crate::thermostat_mt_mmm_mmm_initialize();

      let (monitor_mode,last_monitor_mode) = retrieve_output_and_state();

      assert!(GUMBOX::initialize_IEP_Post(last_monitor_mode, monitor_mode));

      assert_eq!(monitor_mode, Monitor_Mode::Init_Monitor_Mode);
      assert_eq!(last_monitor_mode, monitor_mode);
  }

  //===========================================================
  //  C o m p u t e    Entry Point Tests
  //===========================================================

  // REQ_MMM_2

  #[test]
  #[serial]
  fn test_REQ_MMM_2_Transition_Init_to_Normal() {
      crate::thermostat_mt_mmm_mmm_initialize();

      let in_last_monitor_mode = Monitor_Mode::Init_Monitor_Mode;
      let (current_tempWstatus, interface_failure, internal_failure) =
          setup_test_state(in_last_monitor_mode, ValueStatus::Valid, false, false);

      crate::thermostat_mt_mmm_mmm_timeTriggered();

      let (monitor_mode, last_monitor_mode) = retrieve_output_and_state();

      unsafe {
          assert!(GUMBOX::compute_CEP_Post(
              in_last_monitor_mode,
              last_monitor_mode,
              current_tempWstatus,
              interface_failure,
              internal_failure,
              monitor_mode
          ));

          assert_eq!(monitor_mode, Monitor_Mode::Normal_Monitor_Mode);
          assert_eq!(last_monitor_mode, Monitor_Mode::Normal_Monitor_Mode);
      }
  }

  run_mmm_CEP_test!(
    test_REQ_MMM_2_Transition_Init_to_Normal_macro,
    Monitor_Mode::Init_Monitor_Mode,
    ValueStatus::Valid,
    false,
    false,
    Monitor_Mode::Normal_Monitor_Mode
  );

  // REQ_MMM_3

  run_mmm_CEP_test!(
    test_REQ_MMM_3_invalid_current_temp,
    Monitor_Mode::Normal_Monitor_Mode,
    ValueStatus::Invalid,
    false,
    false,
    Monitor_Mode::Failed_Monitor_Mode
  );

  run_mmm_CEP_test!(
    test_REQ_MMM_3_interface_failure,
    Monitor_Mode::Normal_Monitor_Mode,
    ValueStatus::Valid,
    true,
    false,
    Monitor_Mode::Failed_Monitor_Mode
  );

  run_mmm_CEP_test!(
    test_REQ_MMM_3_internal_failure,
      Monitor_Mode::Normal_Monitor_Mode,
      ValueStatus::Valid,
      false,
      true,
      Monitor_Mode::Failed_Monitor_Mode
  );

  run_mmm_CEP_test!(
    test_REQ_MMM_3_invalid_current_temp_interface_failure,
      Monitor_Mode::Normal_Monitor_Mode,
      ValueStatus::Invalid,
      true,
      false,
      Monitor_Mode::Failed_Monitor_Mode
  );

  run_mmm_CEP_test!(
    test_REQ_MMM_3_invalid_current_temp_internal_failure,
      Monitor_Mode::Normal_Monitor_Mode,
      ValueStatus::Invalid,
      false,
      true,
      Monitor_Mode::Failed_Monitor_Mode
  );

  run_mmm_CEP_test!(
    test_REQ_MMM_3_interface_failure_internal_failure,
      Monitor_Mode::Normal_Monitor_Mode,
      ValueStatus::Valid,
      true,
      true,
      Monitor_Mode::Failed_Monitor_Mode
  );

  run_mmm_CEP_test!(
  test_REQ_MMM_3_invalid_current_temp_interface_failure_internal_failure,
    Monitor_Mode::Normal_Monitor_Mode,
    ValueStatus::Invalid,
    true,
    true,
    Monitor_Mode::Failed_Monitor_Mode
  );

  run_mmm_CEP_test!(
    test_REQ_MMM_3_Maintain_Normal_macro,
    Monitor_Mode::Normal_Monitor_Mode,
    ValueStatus::Valid,
    false,
    false,
    Monitor_Mode::Normal_Monitor_Mode
  );

// ToDo: Finish writing tests for MMM 4 requirement

}
