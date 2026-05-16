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

mod JH_tests {
    use serial_test::serial;

    use crate::compute_api;

    use crate::bridge::extern_c_api as extern_api;
    use crate::bridge::thermostat_rt_mrm_mrm_GUMBOX as GUMBOX;
    use data::*;
    use data::Isolette_Data_Model::*;

    const failOnUnsatPrecondition: bool = false;

    fn setup_test_state(
        last_regulator_mode: Regulator_Mode,
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
            set_lastRegulatorMode(last_regulator_mode);
        }

        (current_tempWstatus, interface_failure, internal_failure)
    }

    fn retrieve_output_and_state() -> (Regulator_Mode, Regulator_Mode) {
        let regulator_mode = extern_api::OUT_regulator_mode
            .lock()
            .unwrap()
            .expect("Not expecting None");

        unsafe {
            let last_regulator_mode = get_lastRegulatorMode();
            (regulator_mode, last_regulator_mode)
        }
    }

    fn set_lastRegulatorMode(last_regulator_mode: Regulator_Mode) {
        unsafe {
            match &mut crate::app {
                Some(inner) => inner.lastRegulatorMode = last_regulator_mode,
                None => panic!("app is None")
            }
        }
    }

    fn get_lastRegulatorMode() -> Regulator_Mode {
        unsafe {
            match &mut crate::app {
                Some(inner) => inner.lastRegulatorMode,
                None => panic!("app is None")
            }
        }
    }

    macro_rules! run_thermostat_test {
        ($name:ident, $mode:expr, $temp_status:expr, $interface_fail:expr, $internal_fail:expr, $expected_mode:expr) => {
            #[test]
            #[serial]
            fn $name() {
                crate::thermostat_rt_mrm_mrm_initialize();

                let in_last_regulator_mode = $mode;
                let (current_tempWstatus, interface_failure, internal_failure) =
                    setup_test_state(in_last_regulator_mode, $temp_status, $interface_fail, $internal_fail);

                crate::thermostat_rt_mrm_mrm_timeTriggered();

                let (regulator_mode, last_regulator_mode) = retrieve_output_and_state();
                unsafe {
                    assert!(GUMBOX::compute_CEP_Post(
                        in_last_regulator_mode,
                        last_regulator_mode,
                        current_tempWstatus,
                        interface_failure,
                        internal_failure,
                        regulator_mode
                    ));
                    assert_eq!(regulator_mode, $expected_mode);
                    assert_eq!(last_regulator_mode, $expected_mode);
                }
            }
        };
    }

    fn test_time_triggered(
        in_last_regulator_mode: Regulator_Mode,
        temp_status: ValueStatus,
        interface_failure_flag: bool,
        internal_failure_flag: bool,
        expected_mode: Regulator_Mode,
    ) {
        let (current_tempWstatus, interface_failure, internal_failure) =
            setup_test_state(in_last_regulator_mode, temp_status, interface_failure_flag, internal_failure_flag);
        crate::thermostat_rt_mrm_mrm_timeTriggered();
        let (regulator_mode, last_regulator_mode) = retrieve_output_and_state();
        unsafe {
            assert!(GUMBOX::compute_CEP_Post(
                in_last_regulator_mode,
                last_regulator_mode,
                current_tempWstatus,
                interface_failure,
                internal_failure,
                regulator_mode
            ));
        }
    }

    #[test]
    #[serial]
    fn test_initialization_REQ_MRM_1() {
        crate::thermostat_rt_mrm_mrm_initialize();

        let regulator_mode = extern_api::OUT_regulator_mode
            .lock()
            .unwrap()
            .expect("Not expecting None");

        unsafe {
            let lastRegulatorMode = get_lastRegulatorMode();

            assert!(GUMBOX::initialize_IEP_Post(lastRegulatorMode, regulator_mode));

            assert_eq!(regulator_mode, Regulator_Mode::Init_Regulator_Mode);
            assert_eq!(lastRegulatorMode, regulator_mode);
        }
    }

    #[test]
    #[serial]
    fn test_REQ_MRM_Maintain_Normal() {
        crate::thermostat_rt_mrm_mrm_initialize();

        let in_last_regulator_mode = Regulator_Mode::Normal_Regulator_Mode;
        let (current_tempWstatus, interface_failure, internal_failure) =
            setup_test_state(in_last_regulator_mode, ValueStatus::Valid, false, false);

        crate::thermostat_rt_mrm_mrm_timeTriggered();

        let (regulator_mode, last_regulator_mode) = retrieve_output_and_state();

        unsafe {
            assert!(GUMBOX::compute_CEP_Post(
                in_last_regulator_mode,
                last_regulator_mode,
                current_tempWstatus,
                interface_failure,
                internal_failure,
                regulator_mode
            ));

            assert_eq!(regulator_mode, Regulator_Mode::Normal_Regulator_Mode);
            assert_eq!(last_regulator_mode, Regulator_Mode::Normal_Regulator_Mode);
        }
    }

    #[test]
    #[serial]
    fn test_REQ_MRM_2_init_to_normal() {
        crate::thermostat_rt_mrm_mrm_initialize();

        let in_last_regulator_mode = Regulator_Mode::Init_Regulator_Mode;
        let (current_tempWstatus, interface_failure, internal_failure) =
            setup_test_state(in_last_regulator_mode, ValueStatus::Valid, false, false);

        crate::thermostat_rt_mrm_mrm_timeTriggered();

        let (regulator_mode, last_regulator_mode) = retrieve_output_and_state();

        unsafe {
            assert!(GUMBOX::compute_CEP_Post(
                in_last_regulator_mode,
                last_regulator_mode,
                current_tempWstatus,
                interface_failure,
                internal_failure,
                regulator_mode
            ));

            assert_eq!(regulator_mode, Regulator_Mode::Normal_Regulator_Mode);
            assert_eq!(last_regulator_mode, Regulator_Mode::Normal_Regulator_Mode);
        }
    }

    #[test]
    #[serial]
    fn test_REQ_MRM_3_normal_to_failed() {
        crate::thermostat_rt_mrm_mrm_initialize();

        let in_last_regulator_mode = Regulator_Mode::Normal_Regulator_Mode;
        let (current_tempWstatus, interface_failure, internal_failure) =
            setup_test_state(in_last_regulator_mode, ValueStatus::Invalid, false, false);

        crate::thermostat_rt_mrm_mrm_timeTriggered();

        let (regulator_mode, last_regulator_mode) = retrieve_output_and_state();

        unsafe {
            assert!(GUMBOX::compute_CEP_Post(
                in_last_regulator_mode,
                last_regulator_mode,
                current_tempWstatus,
                interface_failure,
                internal_failure,
                regulator_mode
            ));

            assert_eq!(regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
            assert_eq!(last_regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
        }
    }

    #[test]
    #[serial]
    fn test_REQ_MRM_4_init_to_failed() {
        crate::thermostat_rt_mrm_mrm_initialize();

        let in_last_regulator_mode = Regulator_Mode::Init_Regulator_Mode;
        let (current_tempWstatus, interface_failure, internal_failure) =
            setup_test_state(in_last_regulator_mode, ValueStatus::Valid, true, false);

        crate::thermostat_rt_mrm_mrm_timeTriggered();

        let (regulator_mode, last_regulator_mode) = retrieve_output_and_state();

        unsafe {
            assert!(GUMBOX::compute_CEP_Post(
                in_last_regulator_mode,
                last_regulator_mode,
                current_tempWstatus,
                interface_failure,
                internal_failure,
                regulator_mode
            ));

            assert_eq!(regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
            assert_eq!(last_regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
        }
    }

    #[test]
    #[serial]
    fn test_REQ_MRM_Maintain_Failed() {
        crate::thermostat_rt_mrm_mrm_initialize();

        let in_last_regulator_mode = Regulator_Mode::Failed_Regulator_Mode;
        let (current_tempWstatus, interface_failure, internal_failure) =
            setup_test_state(in_last_regulator_mode, ValueStatus::Valid, false, false);

        crate::thermostat_rt_mrm_mrm_timeTriggered();

        let (regulator_mode, last_regulator_mode) = retrieve_output_and_state();

        unsafe {
            assert!(GUMBOX::compute_CEP_Post(
                in_last_regulator_mode,
                last_regulator_mode,
                current_tempWstatus,
                interface_failure,
                internal_failure,
                regulator_mode
            ));

            assert_eq!(regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
            assert_eq!(last_regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
        }
    }

    #[test]
    #[serial]
    fn test_REQ_MRM_3_normal_to_failed_multiple() {
        crate::thermostat_rt_mrm_mrm_initialize();

        let in_last_regulator_mode = Regulator_Mode::Normal_Regulator_Mode;
        let (current_tempWstatus, interface_failure, internal_failure) =
            setup_test_state(in_last_regulator_mode, ValueStatus::Invalid, true, true);

        crate::thermostat_rt_mrm_mrm_timeTriggered();

        let (regulator_mode, last_regulator_mode) = retrieve_output_and_state();

        unsafe {
            assert!(GUMBOX::compute_CEP_Post(
                in_last_regulator_mode,
                last_regulator_mode,
                current_tempWstatus,
                interface_failure,
                internal_failure,
                regulator_mode
            ));

            assert_eq!(regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
            assert_eq!(last_regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
        }
    }

    #[test]
    #[serial]
    fn test_REQ_MRM_3_normal_to_failed_internal_failure() {
        crate::thermostat_rt_mrm_mrm_initialize();

        let in_last_regulator_mode = Regulator_Mode::Normal_Regulator_Mode;
        let (current_tempWstatus, interface_failure, internal_failure) =
            setup_test_state(in_last_regulator_mode, ValueStatus::Valid, false, true);

        crate::thermostat_rt_mrm_mrm_timeTriggered();

        let (regulator_mode, last_regulator_mode) = retrieve_output_and_state();

        unsafe {
            assert!(GUMBOX::compute_CEP_Post(
                in_last_regulator_mode,
                last_regulator_mode,
                current_tempWstatus,
                interface_failure,
                internal_failure,
                regulator_mode
            ));

            assert_eq!(regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
            assert_eq!(last_regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
        }
    }

    #[test]
    #[serial]
    fn test_REQ_MRM_4_init_to_failed_internal_failure() {
        crate::thermostat_rt_mrm_mrm_initialize();

        let in_last_regulator_mode = Regulator_Mode::Init_Regulator_Mode;
        let (current_tempWstatus, interface_failure, internal_failure) =
            setup_test_state(in_last_regulator_mode, ValueStatus::Valid, false, true);

        crate::thermostat_rt_mrm_mrm_timeTriggered();

        let (regulator_mode, last_regulator_mode) = retrieve_output_and_state();

        unsafe {
            assert!(GUMBOX::compute_CEP_Post(
                in_last_regulator_mode,
                last_regulator_mode,
                current_tempWstatus,
                interface_failure,
                internal_failure,
                regulator_mode
            ));

            assert_eq!(regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
            assert_eq!(last_regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
        }
    }

    #[test]
    #[serial]
    fn test_REQ_MRM_3_normal_to_failed_invalid_and_interface_failure() {
        crate::thermostat_rt_mrm_mrm_initialize();

        let in_last_regulator_mode = Regulator_Mode::Normal_Regulator_Mode;
        let (current_tempWstatus, interface_failure, internal_failure) =
            setup_test_state(in_last_regulator_mode, ValueStatus::Invalid, true, false);
        crate::thermostat_rt_mrm_mrm_timeTriggered();

        let (regulator_mode, last_regulator_mode) = retrieve_output_and_state();

        unsafe {
            assert!(GUMBOX::compute_CEP_Post(
                in_last_regulator_mode,
                last_regulator_mode,
                current_tempWstatus,
                interface_failure,
                internal_failure,
                regulator_mode
            ));

            assert_eq!(regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
            assert_eq!(last_regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
        }
    }

    #[test]
    #[serial]
    fn test_REQ_MRM_4_init_to_failed_invalid_and_internal_failure() {
        crate::thermostat_rt_mrm_mrm_initialize();

        let in_last_regulator_mode = Regulator_Mode::Init_Regulator_Mode;
        let (current_tempWstatus, interface_failure, internal_failure) =
            setup_test_state(in_last_regulator_mode, ValueStatus::Invalid, false, true);

        crate::thermostat_rt_mrm_mrm_timeTriggered();

        let (regulator_mode, last_regulator_mode) = retrieve_output_and_state();

        unsafe {
            assert!(GUMBOX::compute_CEP_Post(
                in_last_regulator_mode,
                last_regulator_mode,
                current_tempWstatus,
                interface_failure,
                internal_failure,
                regulator_mode
            ));

            assert_eq!(regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
            assert_eq!(last_regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
        }
    }

    #[test]
    #[serial]
    fn test_REQ_MRM_Maintain_Failed_invalid_status() {
        crate::thermostat_rt_mrm_mrm_initialize();

        let in_last_regulator_mode = Regulator_Mode::Failed_Regulator_Mode;
        let (current_tempWstatus, interface_failure, internal_failure) =
            setup_test_state(in_last_regulator_mode, ValueStatus::Invalid, true, true);

        crate::thermostat_rt_mrm_mrm_timeTriggered();

        let (regulator_mode, last_regulator_mode) = retrieve_output_and_state();

        unsafe {
            assert!(GUMBOX::compute_CEP_Post(
                in_last_regulator_mode,
                last_regulator_mode,
                current_tempWstatus,
                interface_failure,
                internal_failure,
                regulator_mode
            ));

            assert_eq!(regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
            assert_eq!(last_regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
        }
    }

    //======================================================
    //  Illustration of macro-based tests
    //======================================================

    run_thermostat_test!(
        test_macro_REQ_MRM_2_init_to_normal,
        Regulator_Mode::Init_Regulator_Mode,
        ValueStatus::Valid,
        false,
        false,
        Regulator_Mode::Normal_Regulator_Mode
    );

    run_thermostat_test!(
        test_macro_REQ_MRM_3_normal_to_failed,
        Regulator_Mode::Normal_Regulator_Mode,
        ValueStatus::Invalid,
        false,
        false,
        Regulator_Mode::Failed_Regulator_Mode
    );

    run_thermostat_test!(
        test_macro_REQ_MRM_3_normal_to_failed_internal_failure,
        Regulator_Mode::Normal_Regulator_Mode,
        ValueStatus::Valid,
        false,
        true,
        Regulator_Mode::Failed_Regulator_Mode
    );

    //======================================================
    //  Illustration of helper-function-based tests
    //======================================================

    #[test]
    #[serial]
    fn test_helper_REQ_MRM_2_init_to_normal() {
       test_time_triggered(
        Regulator_Mode::Init_Regulator_Mode,
        ValueStatus::Valid,
        false,
        false,
        Regulator_Mode::Normal_Regulator_Mode,
       )
    }

    #[test]
    #[serial]
    fn test_helper_REQ_MRM_3_normal_to_failed() {
       test_time_triggered(
        Regulator_Mode::Normal_Regulator_Mode,
        ValueStatus::Invalid,
        false,
        false,
        Regulator_Mode::Failed_Regulator_Mode
       )
    }

    #[test]
    #[serial]
    fn test_helper_REQ_MRM_3_normal_to_failed_internal_failure() {
        test_time_triggered(
            Regulator_Mode::Normal_Regulator_Mode,
            ValueStatus::Valid,
            false,
            true,
            Regulator_Mode::Failed_Regulator_Mode,
        );
    }
}
