// This file will not be overwritten if codegen is rerun

#[cfg(test)]
mod tests {
    // NOTE: need to run tests sequentially to prevent race conditions
    //       on the app and the testing apis which are static
    use serial_test::serial;

    use crate::compute_api;
    
    use crate::bridge::extern_c_api as extern_api;
    use crate::bridge::thermostat_rt_mrm_mrm_GUMBOX as GUMBOX;
    use data::*;
    use data::Isolette_Data_Model::*; // manually added

    const failOnUnsatPrecondition: bool = false; // manually added

    //=============================================
    //  H e l p e r     F u n c t i o n s
    //=============================================
    
    // Helper function to set up input ports and state, returning input values
    // Suggested by Grok without prompting
    fn setup_test_state(
        last_regulator_mode: Regulator_Mode,
        temp_status: ValueStatus,
        interface_failure_flag: bool,
        internal_failure_flag: bool,
    ) -> (TempWstatus_i, Failure_Flag_i, Failure_Flag_i) {
        let current_tempWstatus = TempWstatus_i {
            degrees: 96, // i32 as confirmed
            status: temp_status,
        };
        let interface_failure = Failure_Flag_i {
            flag: interface_failure_flag,
        };
        let internal_failure = Failure_Flag_i {
            flag: internal_failure_flag,
        };

        // [PutInPorts]: put values on the input ports
        *extern_api::IN_current_tempWstatus.lock().unwrap() = Some(current_tempWstatus);
        *extern_api::IN_interface_failure.lock().unwrap() = Some(interface_failure);
        *extern_api::IN_internal_failure.lock().unwrap() = Some(internal_failure);

        unsafe {
            // [SetInStateVars]: set the pre-state values of state variables            
            set_lastRegulatorMode(last_regulator_mode);
        }

        (current_tempWstatus, interface_failure, internal_failure)
    }

    // Helper function to retrieve output and state
    // Suggested by Grok without prompting
    fn retrieve_output_and_state() -> (Regulator_Mode, Regulator_Mode) {
        // [RetrieveOutState]: retrieve values of the output port
        let regulator_mode = extern_api::OUT_regulator_mode
            .lock()
            .unwrap()
            .expect("Not expecting None");

        unsafe {
            // Retrieve value of GUMBO declared local component state
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


    // Macro to generate timeTriggered tests, demonstrating simplified test creation
    // Suggested by Grok without prompting
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

    // Helper function
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
    /// Tests REQ_MRM_1: Initialization sets regulator mode to Init_Regulator_Mode.
    /// Verifies that `initialize` sets `regulator_mode` and `lastRegulatorMode` to `Init_Regulator_Mode`.
    fn test_initialization_REQ_MRM_1() {
        // [InvokeEntryPoint]: invoke initialize entry point

        crate::thermostat_rt_mrm_mrm_initialize();

        // [RetrieveOutState]: retrieve values of the output port
        let regulator_mode = extern_api::OUT_regulator_mode
            .lock()
            .unwrap()
            .expect("Not expecting None");

        unsafe {
            // Retrieve value of GUMBO declared local component state
            let lastRegulatorMode = get_lastRegulatorMode();

            // [CheckPost]: invoke the oracle function
            assert!(GUMBOX::initialize_IEP_Post(lastRegulatorMode, regulator_mode));

            // Manual assertions for clarity
            assert_eq!(regulator_mode, Regulator_Mode::Init_Regulator_Mode);
            assert_eq!(lastRegulatorMode, regulator_mode);
        }
    }

    #[test]
    #[serial]
    /// Tests REQ_MRM_Maintain_Normal: Maintain Normal mode when regulator status is valid.
    /// Verifies that `timeTriggered` keeps `regulator_mode` and `lastRegulatorMode` as `Normal_Regulator_Mode`
    /// when starting in `Normal_Regulator_Mode` with valid temperature status and no failures.
    fn test_REQ_MRM_Maintain_Normal() {
        crate::thermostat_rt_mrm_mrm_initialize();

        let in_last_regulator_mode = Regulator_Mode::Normal_Regulator_Mode;
        let (current_tempWstatus, interface_failure, internal_failure) =
            setup_test_state(in_last_regulator_mode, ValueStatus::Valid, false, false);

        crate::thermostat_rt_mrm_mrm_timeTriggered();

        let (regulator_mode, last_regulator_mode) = retrieve_output_and_state();

        unsafe {
            // [CheckPost]: invoke the oracle function
            assert!(GUMBOX::compute_CEP_Post(
                in_last_regulator_mode,
                last_regulator_mode,
                current_tempWstatus,
                interface_failure,
                internal_failure,
                regulator_mode
            ));

            // Manual assertions for clarity
            assert_eq!(regulator_mode, Regulator_Mode::Normal_Regulator_Mode);
            assert_eq!(last_regulator_mode, Regulator_Mode::Normal_Regulator_Mode);
        }
    }

    #[test]
    #[serial]
    /// Tests REQ_MRM_2: Transition from Init to Normal mode when regulator status is valid.
    /// Verifies that `timeTriggered` sets `regulator_mode` and `lastRegulatorMode` to `Normal_Regulator_Mode`
    /// when starting in `Init_Regulator_Mode` with valid temperature status and no failures.
    fn test_REQ_MRM_2_init_to_normal() {
        crate::thermostat_rt_mrm_mrm_initialize();

        let in_last_regulator_mode = Regulator_Mode::Init_Regulator_Mode;
        let (current_tempWstatus, interface_failure, internal_failure) =
            setup_test_state(in_last_regulator_mode, ValueStatus::Valid, false, false);
            
        crate::thermostat_rt_mrm_mrm_timeTriggered();

        let (regulator_mode, last_regulator_mode) = retrieve_output_and_state();

        unsafe {
            // [CheckPost]: invoke the oracle function
            assert!(GUMBOX::compute_CEP_Post(
                in_last_regulator_mode,
                last_regulator_mode,
                current_tempWstatus,
                interface_failure,
                internal_failure,
                regulator_mode
            ));

            // Manual assertions for clarity
            assert_eq!(regulator_mode, Regulator_Mode::Normal_Regulator_Mode);
            assert_eq!(last_regulator_mode, Regulator_Mode::Normal_Regulator_Mode);
        }
    }

    #[test]
    #[serial]
    /// Tests REQ_MRM_3: Transition from Normal to Failed mode when regulator status is invalid.
    /// Verifies that `timeTriggered` sets `regulator_mode` and `lastRegulatorMode` to `Failed_Regulator_Mode`
    /// when starting in `Normal_Regulator_Mode` with invalid temperature status.
    fn test_REQ_MRM_3_normal_to_failed() {
        crate::thermostat_rt_mrm_mrm_initialize();

        let in_last_regulator_mode = Regulator_Mode::Normal_Regulator_Mode;
        let (current_tempWstatus, interface_failure, internal_failure) =
            setup_test_state(in_last_regulator_mode, ValueStatus::Invalid, false, false);

        crate::thermostat_rt_mrm_mrm_timeTriggered();

        let (regulator_mode, last_regulator_mode) = retrieve_output_and_state();

        unsafe {
            // [CheckPost]: invoke the oracle function
            assert!(GUMBOX::compute_CEP_Post(
                in_last_regulator_mode,
                last_regulator_mode,
                current_tempWstatus,
                interface_failure,
                internal_failure,
                regulator_mode
            ));

            // Manual assertions for clarity
            assert_eq!(regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
            assert_eq!(last_regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
        }
    }

    #[test]
    #[serial]
    /// Tests REQ_MRM_4: Transition from Init to Failed mode when regulator status is invalid.
    /// Verifies that `timeTriggered` sets `regulator_mode` and `lastRegulatorMode` to `Failed_Regulator_Mode`
    /// when starting in `Init_Regulator_Mode` with interface failure.
    fn test_REQ_MRM_4_init_to_failed() {
        crate::thermostat_rt_mrm_mrm_initialize();

        let in_last_regulator_mode = Regulator_Mode::Init_Regulator_Mode;
        let (current_tempWstatus, interface_failure, internal_failure) =
            setup_test_state(in_last_regulator_mode, ValueStatus::Valid, true, false);

        crate::thermostat_rt_mrm_mrm_timeTriggered();

        let (regulator_mode, last_regulator_mode) = retrieve_output_and_state();

        unsafe {
            // [CheckPost]: invoke the oracle function
            assert!(GUMBOX::compute_CEP_Post(
                in_last_regulator_mode,
                last_regulator_mode,
                current_tempWstatus,
                interface_failure,
                internal_failure,
                regulator_mode
            ));

            // Manual assertions for clarity
            assert_eq!(regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
            assert_eq!(last_regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
        }
    }

    #[test]
    #[serial]
    /// Tests REQ_MRM_Maintain_Failed: Maintain Failed mode regardless of regulator status.
    /// Verifies that `timeTriggered` keeps `regulator_mode` and `lastRegulatorMode` as `Failed_Regulator_Mode`
    /// when starting in `Failed_Regulator_Mode` with valid regulator status.
    fn test_REQ_MRM_Maintain_Failed() {
        crate::thermostat_rt_mrm_mrm_initialize();

        let in_last_regulator_mode = Regulator_Mode::Failed_Regulator_Mode;
        let (current_tempWstatus, interface_failure, internal_failure) =
            setup_test_state(in_last_regulator_mode, ValueStatus::Valid, false, false);

        crate::thermostat_rt_mrm_mrm_timeTriggered();

        let (regulator_mode, last_regulator_mode) = retrieve_output_and_state();

        unsafe {
            // [CheckPost]: invoke the oracle function
            assert!(GUMBOX::compute_CEP_Post(
                in_last_regulator_mode,
                last_regulator_mode,
                current_tempWstatus,
                interface_failure,
                internal_failure,
                regulator_mode
            ));

            // Manual assertions for clarity
            assert_eq!(regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
            assert_eq!(last_regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
        }
    }

    #[test]
    #[serial]
    /// Tests REQ_MRM_3 with multiple failure conditions (invalid temp status and both failures).
    /// Verifies that `timeTriggered` sets `regulator_mode` and `lastRegulatorMode` to `Failed_Regulator_Mode`
    /// when starting in `Normal_Regulator_Mode` with invalid temperature status and both failures.
    fn test_REQ_MRM_3_normal_to_failed_multiple() {
        crate::thermostat_rt_mrm_mrm_initialize();

        let in_last_regulator_mode = Regulator_Mode::Normal_Regulator_Mode;
        let (current_tempWstatus, interface_failure, internal_failure) =
            setup_test_state(in_last_regulator_mode, ValueStatus::Invalid, true, true);
          
        crate::thermostat_rt_mrm_mrm_timeTriggered();

        let (regulator_mode, last_regulator_mode) = retrieve_output_and_state();

        unsafe {
            // [CheckPost]: invoke the oracle function
            assert!(GUMBOX::compute_CEP_Post(
                in_last_regulator_mode,
                last_regulator_mode,
                current_tempWstatus,
                interface_failure,
                internal_failure,
                regulator_mode
            ));

            // Manual assertions for clarity
            assert_eq!(regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
            assert_eq!(last_regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
        }
    }

    #[test]
    #[serial]
    /// Tests REQ_MRM_3 with internal failure only.
    /// Verifies that `timeTriggered` sets `regulator_mode` and `lastRegulatorMode` to `Failed_Regulator_Mode`
    /// when starting in `Normal_Regulator_Mode` with internal failure (`internal_failure.flag = true`),
    /// valid temperature status, and no interface failure.
    /// Purpose: Ensures the `internal_failure.flag` condition alone triggers the Normal to Failed transition,
    /// covering a single failure scenario not tested in other REQ_MRM_3 tests.
    fn test_REQ_MRM_3_normal_to_failed_internal_failure() {
        crate::thermostat_rt_mrm_mrm_initialize();

        let in_last_regulator_mode = Regulator_Mode::Normal_Regulator_Mode;
        let (current_tempWstatus, interface_failure, internal_failure) =
            setup_test_state(in_last_regulator_mode, ValueStatus::Valid, false, true);
        
        crate::thermostat_rt_mrm_mrm_timeTriggered();

        let (regulator_mode, last_regulator_mode) = retrieve_output_and_state();

        unsafe {
            // [CheckPost]: invoke the oracle function
            assert!(GUMBOX::compute_CEP_Post(
                in_last_regulator_mode,
                last_regulator_mode,
                current_tempWstatus,
                interface_failure,
                internal_failure,
                regulator_mode
            ));

            // Manual assertions for clarity
            assert_eq!(regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
            assert_eq!(last_regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
        }
    }

    #[test]
    #[serial]
    /// Tests REQ_MRM_4 with internal failure only.
    /// Verifies that `timeTriggered` sets `regulator_mode` and `lastRegulatorMode` to `Failed_Regulator_Mode`
    /// when starting in `Init_Regulator_Mode` with internal failure (`internal_failure.flag = true`),
    /// valid temperature status, and no interface failure.
    /// Purpose: Ensures the `internal_failure.flag` condition alone triggers the Init to Failed transition,
    /// complementing the interface failure test for REQ_MRM_4.
    fn test_REQ_MRM_4_init_to_failed_internal_failure() {
        crate::thermostat_rt_mrm_mrm_initialize();

        let in_last_regulator_mode = Regulator_Mode::Init_Regulator_Mode;
        let (current_tempWstatus, interface_failure, internal_failure) =
            setup_test_state(in_last_regulator_mode, ValueStatus::Valid, false, true);

        crate::thermostat_rt_mrm_mrm_timeTriggered();

        let (regulator_mode, last_regulator_mode) = retrieve_output_and_state();

        unsafe {
            // [CheckPost]: invoke the oracle function
            assert!(GUMBOX::compute_CEP_Post(
                in_last_regulator_mode,
                last_regulator_mode,
                current_tempWstatus,
                interface_failure,
                internal_failure,
                regulator_mode
            ));

            // Manual assertions for clarity
            assert_eq!(regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
            assert_eq!(last_regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
        }
    }

    #[test]
    #[serial]
    /// Tests REQ_MRM_3 with invalid temperature status and interface failure.
    /// Verifies that `timeTriggered` sets `regulator_mode` and `lastRegulatorMode` to `Failed_Regulator_Mode`
    /// when starting in `Normal_Regulator_Mode` with invalid temperature status and interface failure,
    /// but no internal failure.
    /// Purpose: Tests the interaction of `Invalid` status and `interface_failure`, ensuring the Normal to Failed
    /// transition under a pairwise failure condition not covered by other REQ_MRM_3 tests.
    fn test_REQ_MRM_3_normal_to_failed_invalid_and_interface_failure() {
        crate::thermostat_rt_mrm_mrm_initialize();

        let in_last_regulator_mode = Regulator_Mode::Normal_Regulator_Mode;
        let (current_tempWstatus, interface_failure, internal_failure) =
            setup_test_state(in_last_regulator_mode, ValueStatus::Invalid, true, false);
        crate::thermostat_rt_mrm_mrm_timeTriggered();

        let (regulator_mode, last_regulator_mode) = retrieve_output_and_state();

        unsafe {
            // [CheckPost]: invoke the oracle function
            assert!(GUMBOX::compute_CEP_Post(
                in_last_regulator_mode,
                last_regulator_mode,
                current_tempWstatus,
                interface_failure,
                internal_failure,
                regulator_mode
            ));

            // Manual assertions for clarity
            assert_eq!(regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
            assert_eq!(last_regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
        }
    }

    #[test]
    #[serial]
    /// Tests REQ_MRM_4 with invalid temperature status and internal failure.
    /// Verifies that `timeTriggered` sets `regulator_mode` and `lastRegulatorMode` to `Failed_Regulator_Mode`
    /// when starting in `Init_Regulator_Mode` with invalid temperature status and internal failure,
    /// but no interface failure.
    /// Purpose: Tests the interaction of `Invalid` status and `internal_failure`, ensuring the Init to Failed
    /// transition under a pairwise failure condition not covered by other REQ_MRM_4 tests.
    fn test_REQ_MRM_4_init_to_failed_invalid_and_internal_failure() {
        crate::thermostat_rt_mrm_mrm_initialize();

        let in_last_regulator_mode = Regulator_Mode::Init_Regulator_Mode;
        let (current_tempWstatus, interface_failure, internal_failure) =
            setup_test_state(in_last_regulator_mode, ValueStatus::Invalid, false, true);
        
        crate::thermostat_rt_mrm_mrm_timeTriggered();

        let (regulator_mode, last_regulator_mode) = retrieve_output_and_state();

        unsafe {
            // [CheckPost]: invoke the oracle function
            assert!(GUMBOX::compute_CEP_Post(
                in_last_regulator_mode,
                last_regulator_mode,
                current_tempWstatus,
                interface_failure,
                internal_failure,
                regulator_mode
            ));

            // Manual assertions for clarity
            assert_eq!(regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
            assert_eq!(last_regulator_mode, Regulator_Mode::Failed_Regulator_Mode);
        }
    }

    #[test]
    #[serial]
    /// Tests REQ_MRM_Maintain_Failed with invalid regulator status.
    /// Verifies that `timeTriggered` keeps `regulator_mode` and `lastRegulatorMode` as `Failed_Regulator_Mode`
    /// when starting in `Failed_Regulator_Mode` with invalid temperature status and both failures.
    /// Purpose: Ensures the Failed state persists under the worst-case invalid regulator status,
    /// complementing the valid status test for REQ_MRM_Maintain_Failed.
    fn test_REQ_MRM_Maintain_Failed_invalid_status() {
        crate::thermostat_rt_mrm_mrm_initialize();

        let in_last_regulator_mode = Regulator_Mode::Failed_Regulator_Mode;
        let (current_tempWstatus, interface_failure, internal_failure) =
            setup_test_state(in_last_regulator_mode, ValueStatus::Invalid, true, true);

        crate::thermostat_rt_mrm_mrm_timeTriggered();

        let (regulator_mode, last_regulator_mode) = retrieve_output_and_state();

        unsafe {
            // [CheckPost]: invoke the oracle function
            assert!(GUMBOX::compute_CEP_Post(
                in_last_regulator_mode,
                last_regulator_mode,
                current_tempWstatus,
                interface_failure,
                internal_failure,
                regulator_mode
            ));

            // Manual assertions for clarity
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
    /// Helper function-based equivalent of `test_REQ_MRM_2_init_to_normal`.
    /// Tests REQ_MRM_2: Transition from Init to Normal mode when regulator status is valid.
    /// Verifies that `timeTriggered` 
    /// sets `regulator_mode` and `lastRegulatorMode` to `Normal_Regulator_Mode`
    /// when starting in `Init_Regulator_Mode` with valid temperature status and no failures.
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
    /// Helper function-based equivalent of `test_REQ_MRM_3_normal_to_failed`, 
    /// demonstrating parameterized test creation.
    /// Tests REQ_MRM_3: Transition from Normal to Failed mode when regulator status is invalid.
    /// Verifies that `timeTriggered` sets `regulator_mode` and `lastRegulatorMode` to `Failed_Regulator_Mode`
    /// when starting in `Normal_Regulator_Mode` with invalid temperature status.
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
    /// Helper function-based equivalent of `test_REQ_MRM_3_normal_to_failed_internal_failure`.
    /// Tests REQ_MRM_3 with internal failure only.
    /// Verifies that `timeTriggered` 
    /// sets `regulator_mode` and `lastRegulatorMode` to `Failed_Regulator_Mode`
    /// when starting in `Normal_Regulator_Mode` with 
    /// internal failure, valid temperature status, and no interface failure.
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
