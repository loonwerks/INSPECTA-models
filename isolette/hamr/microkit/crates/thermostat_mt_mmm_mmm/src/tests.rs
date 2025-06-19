// This file will not be overwritten if codegen is rerun

#[cfg(test)]
mod tests {
  // NOTE: need to run tests sequentially to prevent race conditions
  //       on the app and the testing apis which are static
  use serial_test::serial;

  use crate::compute_api; // manually added

  use crate::bridge::extern_c_api as extern_api;
  use crate::bridge::thermostat_mt_mmm_mmm_GUMBOX as GUMBOX; // manually added
  use data::*;
  use data::Isolette_Data_Model::*; // manually added

  const failOnUnsatPrecondition: bool = false;  // manually added

//================================================================================
//  H e l p e r     F u n c t i o n s    and    M a c r o s 
//================================================================================

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

  // Helper function to set up input ports and state, returning input values.
  // Adapted from MRM testing.
  // Suggested by Grok (for MRM component) without prompting
  fn setup_test_state(
      last_monitor_mode: Monitor_Mode, 
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
          set_lastMonitorMode(last_monitor_mode);
      }

      (current_tempWstatus, interface_failure, internal_failure)
  }

  // Macro to generate timeTriggered tests, demonstrating simplified test creation
  // Adapted from MRM
  // Suggested by Grok (for MRM) without prompting
  macro_rules! run_mmm_CEP_test {
    (// inputs
     $name:ident, // test name
     $mode:expr,  // pre-state mode (value of last_monitor_mode state variable in pre-state)
     $temp_status:expr, // status of input temperature (behavior doesn't care about actual temperature)
     $interface_fail:expr, // interface failure value
     $internal_fail:expr, // internal failure value
     // outputs
     $expected_mode:expr // expected value of BOTH monitor_mode output port and last_monitor_mode state variable
    ) => {
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

  // Helper function to retrieve output and state.
  // Adapted from MRM testing.
  // Suggested by Grok (from MRM) without prompting
    fn retrieve_output_and_state() -> (Monitor_Mode, Monitor_Mode) {
      // [RetrieveOutState]: retrieve values of the output port
      let monitor_mode = extern_api::OUT_monitor_mode
          .lock()
          .unwrap()
          .expect("Not expecting None");

      unsafe {
          // Retrieve value of GUMBO declared local component state
          let last_monitor_mode = get_lastMonitorMode();
          (monitor_mode, last_monitor_mode)
      }
  }

  //===========================================================
  //  I n i t i a l i z e    Entry Point Tests
  //===========================================================

  #[test]
  #[serial]
  /// Tests REQ_MRM_1: Initialization sets regulator mode to Init_Regulator_Mode.
  /// Verifies that `initialize` sets `regulator_mode` and `lastRegulatorMode` to `Init_Regulator_Mode`.
  fn test_initialization_REQ_MMM_1() {
      // [InvokeEntryPoint]: invoke initialize entry point

      crate::thermostat_mt_mmm_mmm_initialize();

      let (monitor_mode,last_monitor_mode) = retrieve_output_and_state();

      // [CheckPost]: invoke the oracle function
      assert!(GUMBOX::initialize_IEP_Post(last_monitor_mode, monitor_mode));

      // Illustrate manual assertions (expressing directly the desired post-condition)
      assert_eq!(monitor_mode, Monitor_Mode::Init_Monitor_Mode);
      assert_eq!(last_monitor_mode, monitor_mode);
  }

  //===========================================================
  //  C o m p u t e    Entry Point Tests
  //===========================================================

  // REQ_MMM_2
  //   If the current mode is Init, then
  //   the mode is set to NORMAL iff the monitor status is true (valid) (see Table A-15), i.e.,
  //   if  NOT (Monitor Interface Failure OR Monitor Internal Failure)
  //   AND Current Temperature.Status = Valid
  //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=114 

  #[test]
  #[serial]
  /// Tests REQ_MRM_2: Transition to Normal mode when monitor status is valid.
  /// Verifies that `timeTriggered` transitions from Init mode to Normal mode
  /// (sets `monitor_mode` and `lastMonitorMode` as `Normal_Monitor_Mode`)
  /// when valid temperature status and no failures.
  fn test_REQ_MMM_2_Transition_Init_to_Normal() {
      crate::thermostat_mt_mmm_mmm_initialize();

      let in_last_monitor_mode = Monitor_Mode::Init_Monitor_Mode;
      let (current_tempWstatus, interface_failure, internal_failure) =
          setup_test_state(in_last_monitor_mode, ValueStatus::Valid, false, false);

      crate::thermostat_mt_mmm_mmm_timeTriggered();

      let (monitor_mode, last_monitor_mode) = retrieve_output_and_state();

      unsafe {
          // [CheckPost]: invoke the oracle function
          assert!(GUMBOX::compute_CEP_Post(
              in_last_monitor_mode,
              last_monitor_mode,
              current_tempWstatus,
              interface_failure,
              internal_failure,
              monitor_mode
          ));

          // Manual assertions for clarity
          assert_eq!(monitor_mode, Monitor_Mode::Normal_Monitor_Mode);
          assert_eq!(last_monitor_mode, Monitor_Mode::Normal_Monitor_Mode);
      }
  }

  run_mmm_CEP_test!(
    test_REQ_MMM_2_Transition_Init_to_Normal_macro,
    Monitor_Mode::Init_Monitor_Mode, // start in Normal mode
    ValueStatus::Valid, // input temp is valid
    false, // no interface failure
    false, // no internal failure
    Monitor_Mode::Normal_Monitor_Mode
  );

  // Note: a monitor status failure doesn't necessarily mean that the mode should transition to failure.
  //  If the execution has not reached the time out limit, temp invalid, interface failure, or internal failure
  //  should lead the component to stay in Init mode
  

  // REQ_MMM_3
  //   If the current Monitor mode is Normal, then
  //   the Monitor mode is set to Failed iff
  //   the Monitor status is false, i.e.,
  //   if  (Monitor Interface Failure OR Monitor Internal Failure)
  //   OR NOT(Current Temperature.Status = Valid)
  //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=114 
  //
  //  Note: the phrasing of this requirement and its reliance on "iff" to capture the "stay in Normal mode" scenario
  //   is not accurate, because "not transitioning to Failed mode" could also allow a "transition back to Init mode".
  //   Therefore, I believe its best to have an explicit additional requirement for the "stay in Normal mode" scenario.

  // Verifies that, when starting in Normal mode, an Invalid sensed temperature (current_tempWstatus) causes a transition to Failed mode
  run_mmm_CEP_test!(
    test_REQ_MMM_3_invalid_current_temp, 
    Monitor_Mode::Normal_Monitor_Mode, // start in Normal mode
    ValueStatus::Invalid, // input temp is invalid
    false, // no interface failure
    false, // no internal failure
    Monitor_Mode::Failed_Monitor_Mode
  );
  
  // Verifies that, when starting in Normal mode, an interface failure causes a transition to Failed mode
  run_mmm_CEP_test!(
    test_REQ_MMM_3_interface_failure, 
    Monitor_Mode::Normal_Monitor_Mode, // start in Normal mode
    ValueStatus::Valid, // input temp is valid
    true, // interface failure
    false, // no internal failure
    Monitor_Mode::Failed_Monitor_Mode
  );

  // Verifies that, when starting in Normal mode, an internal failure causes a transition to Failed mode
  run_mmm_CEP_test!(
    test_REQ_MMM_3_internal_failure, 
      Monitor_Mode::Normal_Monitor_Mode, // start in Normal mode
      ValueStatus::Valid, // input temp is valid
      false, // no interface failure
      true, // internal failure
      Monitor_Mode::Failed_Monitor_Mode
  );

  // ---
  // Now consider remaining combinations of conditions to achieve condition coverage
  // ---

  // Verifies that, when starting in Normal mode, an Invalid current temp + interface failure causes a transition to Failed mode
  run_mmm_CEP_test!(
    test_REQ_MMM_3_invalid_current_temp_interface_failure, 
      Monitor_Mode::Normal_Monitor_Mode, // start in Normal mode
      ValueStatus::Invalid, // input temp is invalid
      true, // interface failure
      false, // no internal failure
      Monitor_Mode::Failed_Monitor_Mode
  );

  // Verifies that, when starting in Normal mode, an Invalid current temp + internal failure causes a transition to Failed mode
  run_mmm_CEP_test!(
    test_REQ_MMM_3_invalid_current_temp_internal_failure, 
      Monitor_Mode::Normal_Monitor_Mode, // start in Normal mode
      ValueStatus::Invalid, // input temp is invalid
      false, // no interface failure
      true, // internal failure
      Monitor_Mode::Failed_Monitor_Mode
  );
  
  // Verifies that, when starting in Normal mode, an interface failure + internal failure causes a transition to Failed mode
  run_mmm_CEP_test!(
    test_REQ_MMM_3_interface_failure_internal_failure, 
      Monitor_Mode::Normal_Monitor_Mode, // start in Normal mode
      ValueStatus::Valid, // input temp is valid
      true, // interface failure
      true, // internal failure
      Monitor_Mode::Failed_Monitor_Mode
  );
  
  // Verifies that, when starting in Normal mode, an invalid current temp + interface failure + internal failure causes a transition to Failed mode
  run_mmm_CEP_test!(
  test_REQ_MMM_3_invalid_current_temp_interface_failure_internal_failure, 
    Monitor_Mode::Normal_Monitor_Mode, // start in Normal mode
    ValueStatus::Invalid, // input temp is invalid
    true, // interface failure
    true, // internal failure
    Monitor_Mode::Failed_Monitor_Mode
  );
  
  // Tests REQ_MRM_Maintain_Normal: Maintain Normal mode when regulator status is valid (derived from REQ_MMM_3).
  // Verifies that `timeTriggered` keeps `regulator_mode` and `lastRegulatorMode` as `Normal_Regulator_Mode`
  // when starting in `Normal_Regulator_Mode` with valid temperature status and no failures.
  run_mmm_CEP_test!(
    test_REQ_MMM_3_Maintain_Normal_macro,
    Monitor_Mode::Normal_Monitor_Mode, // start in Normal mode
    ValueStatus::Valid, // input temp is valid
    false, // no interface failure
    false, // no internal failure
    Monitor_Mode::Normal_Monitor_Mode
  );

// ToDo: Finish writing tests for MMM 4 requirement

}
