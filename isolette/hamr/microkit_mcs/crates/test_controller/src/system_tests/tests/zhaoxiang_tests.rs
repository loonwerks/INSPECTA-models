// This file will not be overwritten if HAMR codegen is rerun

//! Port of SystemTestsZhaoxiang.scala:
//! https://github.com/santoslab/hamr-system-testing-case-studies/blob/389f2d6a32f4d9c039b94c4bd300fbff6e21ffb9/isolette/hamr/slang/src/test/system/isolette/SystemTestsZhaoxiang.scala
//!
//! Every test after the first shares one body, `run_regulator` in tests.rs, and
//! differs only in its inputs and the expected heat control.

use crate::system_tests::inspect;
use crate::{system_tests, sys_assert, sys_assert_eq};
use data::Isolette_Data_Model::*;
use super::{regulator_post_init, run_regulator, run_regulator_with_mrm_mode, tws, RegulatorInputs};

system_tests! {
  suite zhaoxiang {
    // "Regulator Subsystem Initialization Test"
    // https://github.com/santoslab/hamr-system-testing-case-studies/blob/389f2d6a32f4d9c039b94c4bd300fbff6e21ffb9/isolette/hamr/slang/src/test/system/isolette/SystemTestsZhaoxiang.scala#L44-L101
    // The JVM test's second read of each
    // port (must_match after check_concrete_outputs) is the same snapshot value here.
    fn regulator_subsystem_initialization() {
      let init = regulator_post_init();
      sys_assert!(init.is_some()); // must run before anything is dispatched -- see tests.rs
      let init = init.unwrap();

      sys_assert_eq!(init.mhs_sv_lastCmd, Some(On_Off::Off));
      sys_assert_eq!(init.mhs_heat_control, Some(On_Off::Off));

      // Temp_impl.example() / Failure_Flag_impl.example() are the defaults MRI's
      // initialize publishes
      sys_assert_eq!(init.mri_displayed_temp, Some(Temp_i::default()));
      sys_assert_eq!(init.mri_interface_failure, Some(Failure_Flag_i::default()));
      sys_assert_eq!(init.mri_lower_desired_temp, Some(Temp_i::default()));
      sys_assert_eq!(init.mri_regulator_status, Some(Status::Init_Status));
      sys_assert_eq!(init.mri_upper_desired_temp, Some(Temp_i::default()));

      sys_assert_eq!(init.mrm_sv_lastRegulatorMode, Some(Regulator_Mode::Init_Regulator_Mode));
      sys_assert_eq!(init.mrm_regulator_mode, Some(Regulator_Mode::Init_Regulator_Mode));
    }

    // "Normal Mode -- Heat Control On"
    // https://github.com/santoslab/hamr-system-testing-case-studies/blob/389f2d6a32f4d9c039b94c4bd300fbff6e21ffb9/isolette/hamr/slang/src/test/system/isolette/SystemTestsZhaoxiang.scala#L126-L233
    fn normal_mode_heat_control_on() {
      run_regulator(RegulatorInputs {
        upper_desired: tws(101, ValueStatus::Valid),
        lower_desired: tws(99, ValueStatus::Valid),
        current: tws(98, ValueStatus::Valid),
        internal_failure: false,
        mode: Regulator_Mode::Normal_Regulator_Mode,
      });
      sys_assert_eq!(inspect::get_thermostat_rt_mhs_mhs_heat_control(), Some(On_Off::Onn));
    }

    // "Normal Mode -- Heat Control Off"
    // https://github.com/santoslab/hamr-system-testing-case-studies/blob/389f2d6a32f4d9c039b94c4bd300fbff6e21ffb9/isolette/hamr/slang/src/test/system/isolette/SystemTestsZhaoxiang.scala#L258-L377
    fn normal_mode_heat_control_off() {
      run_regulator(RegulatorInputs {
        upper_desired: tws(101, ValueStatus::Valid),
        lower_desired: tws(99, ValueStatus::Valid),
        current: tws(102, ValueStatus::Valid),
        internal_failure: false,
        mode: Regulator_Mode::Normal_Regulator_Mode,
      });
      sys_assert_eq!(inspect::get_thermostat_rt_mhs_mhs_heat_control(), Some(On_Off::Off));
    }

    // "Lower Desired Temperature Invalid -- Heat Control Off"
    // https://github.com/santoslab/hamr-system-testing-case-studies/blob/389f2d6a32f4d9c039b94c4bd300fbff6e21ffb9/isolette/hamr/slang/src/test/system/isolette/SystemTestsZhaoxiang.scala#L393-L511
    fn lower_desired_temperature_invalid_heat_control_off() {
      run_regulator(RegulatorInputs {
        upper_desired: tws(101, ValueStatus::Valid),
        lower_desired: tws(99, ValueStatus::Invalid),
        current: tws(98, ValueStatus::Valid),
        internal_failure: false,
        mode: Regulator_Mode::Normal_Regulator_Mode,
      });
      sys_assert_eq!(inspect::get_thermostat_rt_mhs_mhs_heat_control(), Some(On_Off::Off));
    }

    // "Upper Desired Temperature Invalid -- Heat Control Off"
    // https://github.com/santoslab/hamr-system-testing-case-studies/blob/389f2d6a32f4d9c039b94c4bd300fbff6e21ffb9/isolette/hamr/slang/src/test/system/isolette/SystemTestsZhaoxiang.scala#L513-L546
    fn upper_desired_temperature_invalid_heat_control_off() {
      run_regulator(RegulatorInputs {
        upper_desired: tws(101, ValueStatus::Invalid),
        lower_desired: tws(99, ValueStatus::Valid),
        current: tws(98, ValueStatus::Valid),
        internal_failure: false,
        mode: Regulator_Mode::Normal_Regulator_Mode,
      });
      sys_assert_eq!(inspect::get_thermostat_rt_mhs_mhs_heat_control(), Some(On_Off::Off));
    }

    // "Current Temperature Invalid -- Heat Control Off"
    // https://github.com/santoslab/hamr-system-testing-case-studies/blob/389f2d6a32f4d9c039b94c4bd300fbff6e21ffb9/isolette/hamr/slang/src/test/system/isolette/SystemTestsZhaoxiang.scala#L548-L581
    fn current_temperature_invalid_heat_control_off() {
      run_regulator(RegulatorInputs {
        upper_desired: tws(101, ValueStatus::Valid),
        lower_desired: tws(99, ValueStatus::Valid),
        current: tws(98, ValueStatus::Invalid),
        internal_failure: false,
        mode: Regulator_Mode::Normal_Regulator_Mode,
      });
      sys_assert_eq!(inspect::get_thermostat_rt_mhs_mhs_heat_control(), Some(On_Off::Off));
    }

    // "Normal Mode -- Heat Control Unchanged"
    // https://github.com/santoslab/hamr-system-testing-case-studies/blob/389f2d6a32f4d9c039b94c4bd300fbff6e21ffb9/isolette/hamr/slang/src/test/system/isolette/SystemTestsZhaoxiang.scala#L586-L619
    fn normal_mode_heat_control_unchanged() {
      run_regulator(RegulatorInputs {
        upper_desired: tws(101, ValueStatus::Valid),
        lower_desired: tws(99, ValueStatus::Valid),
        current: tws(100, ValueStatus::Valid),
        internal_failure: false,
        mode: Regulator_Mode::Normal_Regulator_Mode,
      });
      let heat_control = inspect::get_thermostat_rt_mhs_mhs_heat_control();
      let last_cmd = inspect::get_thermostat_rt_mhs_mhs_sv_lastCmd();
      sys_assert!(heat_control.is_some());
      sys_assert_eq!(heat_control, last_cmd);
    }

    // "Fail Mode -- Heat Control Off"
    // https://github.com/santoslab/hamr-system-testing-case-studies/blob/389f2d6a32f4d9c039b94c4bd300fbff6e21ffb9/isolette/hamr/slang/src/test/system/isolette/SystemTestsZhaoxiang.scala#L620-L653
    //
    // NOTE: adapted, not a straight port.  The Scala original (linked above) fails on
    // the JVM, and a straight port failed the same way on Microkit, both with
    // heat_control == Onn.
    // It puts Failed only into MRI's regulator_mode input.  MRI uses that input for
    // nothing but its own regulator_status and displayed_temp; MHS takes its mode from
    // MRM, which recomputes it from the (all valid, no failure) inputs and outputs
    // Normal.  With current 98 < lower 99, Normal turns the heat on.  This version
    // puts MRM itself in Failed -- see run_regulator_with_mrm_mode -- and checks that
    // MRM output Failed before checking the heat.
    fn fail_mode_heat_control_off() {
      run_regulator_with_mrm_mode(RegulatorInputs {
        upper_desired: tws(101, ValueStatus::Valid),
        lower_desired: tws(99, ValueStatus::Valid),
        current: tws(98, ValueStatus::Valid),
        internal_failure: false,
        mode: Regulator_Mode::Failed_Regulator_Mode,
      }, Some(Regulator_Mode::Failed_Regulator_Mode));
      // MHS saw Failed, not merely a regulator that happened to leave the heat off
      sys_assert_eq!(inspect::get_thermostat_rt_mrm_mrm_regulator_mode(),
                     Some(Regulator_Mode::Failed_Regulator_Mode));
      sys_assert_eq!(inspect::get_thermostat_rt_mhs_mhs_heat_control(), Some(On_Off::Off));
    }
  }
}
