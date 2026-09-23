// This file will not be overwritten if HAMR codegen is rerun

//! Port of SystemTestsCat.scala:
//! https://github.com/santoslab/hamr-system-testing-case-studies/blob/389f2d6a32f4d9c039b94c4bd300fbff6e21ffb9/isolette/hamr/slang/src/test/system/isolette/SystemTestsCat.scala
//!
//! The regulator tests share one body, `run_regulator` in tests.rs, and the monitor
//! tests share `run_monitor`; each test differs only in its inputs and expectation.

use crate::system_tests::inspect;
use crate::{system_tests, sys_assert, sys_assert_eq};
use data::Isolette_Data_Model::*;
use super::{regulator_post_init, run_monitor, run_regulator, run_regulator_with_mrm_mode, tws,
            MonitorInputs, RegulatorInputs};

system_tests! {
  suite cat {
    // "Regulator Subsystem Initialization Test"
    // https://github.com/santoslab/hamr-system-testing-case-studies/blob/389f2d6a32f4d9c039b94c4bd300fbff6e21ffb9/isolette/hamr/slang/src/test/system/isolette/SystemTestsCat.scala#L43-L100
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
    // https://github.com/santoslab/hamr-system-testing-case-studies/blob/389f2d6a32f4d9c039b94c4bd300fbff6e21ffb9/isolette/hamr/slang/src/test/system/isolette/SystemTestsCat.scala#L125-L232
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

    // "Normal Mode -- Cat - Heat Control Off"
    // https://github.com/santoslab/hamr-system-testing-case-studies/blob/389f2d6a32f4d9c039b94c4bd300fbff6e21ffb9/isolette/hamr/slang/src/test/system/isolette/SystemTestsCat.scala#L234-L341
    fn normal_mode_cat_heat_control_off() {
      run_regulator(RegulatorInputs {
        upper_desired: tws(101, ValueStatus::Valid),
        lower_desired: tws(99, ValueStatus::Valid),
        current: tws(102, ValueStatus::Valid),
        internal_failure: false,
        mode: Regulator_Mode::Normal_Regulator_Mode,
      });
      sys_assert_eq!(inspect::get_thermostat_rt_mhs_mhs_heat_control(), Some(On_Off::Off));
    }

    // "Normal Mode -- Heat Control Off"
    // https://github.com/santoslab/hamr-system-testing-case-studies/blob/389f2d6a32f4d9c039b94c4bd300fbff6e21ffb9/isolette/hamr/slang/src/test/system/isolette/SystemTestsCat.scala#L366-L485
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
    // https://github.com/santoslab/hamr-system-testing-case-studies/blob/389f2d6a32f4d9c039b94c4bd300fbff6e21ffb9/isolette/hamr/slang/src/test/system/isolette/SystemTestsCat.scala#L501-L619
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
    // https://github.com/santoslab/hamr-system-testing-case-studies/blob/389f2d6a32f4d9c039b94c4bd300fbff6e21ffb9/isolette/hamr/slang/src/test/system/isolette/SystemTestsCat.scala#L621-L654
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
    // https://github.com/santoslab/hamr-system-testing-case-studies/blob/389f2d6a32f4d9c039b94c4bd300fbff6e21ffb9/isolette/hamr/slang/src/test/system/isolette/SystemTestsCat.scala#L656-L689
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
    // https://github.com/santoslab/hamr-system-testing-case-studies/blob/389f2d6a32f4d9c039b94c4bd300fbff6e21ffb9/isolette/hamr/slang/src/test/system/isolette/SystemTestsCat.scala#L694-L727
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

    // "Internal Failure -- Heat Control Off"
    // https://github.com/santoslab/hamr-system-testing-case-studies/blob/389f2d6a32f4d9c039b94c4bd300fbff6e21ffb9/isolette/hamr/slang/src/test/system/isolette/SystemTestsCat.scala#L729-L762
    // The JVM original carries
    // "TODO: The test is failing when it should not".
    fn internal_failure_heat_control_off() {
      run_regulator(RegulatorInputs {
        upper_desired: tws(101, ValueStatus::Valid),
        lower_desired: tws(99, ValueStatus::Valid),
        current: tws(98, ValueStatus::Valid),
        internal_failure: true,
        mode: Regulator_Mode::Normal_Regulator_Mode,
      });
      sys_assert_eq!(inspect::get_thermostat_rt_mhs_mhs_heat_control(), Some(On_Off::Off));
    }

    // "Fail Mode -- Heat Control Off"
    // https://github.com/santoslab/hamr-system-testing-case-studies/blob/389f2d6a32f4d9c039b94c4bd300fbff6e21ffb9/isolette/hamr/slang/src/test/system/isolette/SystemTestsCat.scala#L764-L797
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

    // "Normal Mode -- Alarm On"
    // https://github.com/santoslab/hamr-system-testing-case-studies/blob/389f2d6a32f4d9c039b94c4bd300fbff6e21ffb9/isolette/hamr/slang/src/test/system/isolette/SystemTestsCat.scala#L799-L908
    fn normal_mode_alarm_on() {
      run_monitor(MonitorInputs {
        upper_alarm: tws(101, ValueStatus::Valid),
        lower_alarm: tws(99, ValueStatus::Valid),
        current: tws(98, ValueStatus::Valid),
        internal_failure: false,
        mode: Monitor_Mode::Normal_Monitor_Mode,
        last_cmd: On_Off::Off,
      });
      sys_assert_eq!(inspect::get_thermostat_mt_ma_ma_alarm_control(), Some(On_Off::Onn));
    }

    // "Upper Alarm Temperature Invalid -- Alarm On"
    // https://github.com/santoslab/hamr-system-testing-case-studies/blob/389f2d6a32f4d9c039b94c4bd300fbff6e21ffb9/isolette/hamr/slang/src/test/system/isolette/SystemTestsCat.scala#L910-L1019
    fn upper_alarm_temperature_invalid_alarm_on() {
      run_monitor(MonitorInputs {
        upper_alarm: tws(101, ValueStatus::Invalid),
        lower_alarm: tws(99, ValueStatus::Valid),
        current: tws(98, ValueStatus::Valid),
        internal_failure: false,
        mode: Monitor_Mode::Normal_Monitor_Mode,
        last_cmd: On_Off::Off,
      });
      sys_assert_eq!(inspect::get_thermostat_mt_ma_ma_alarm_control(), Some(On_Off::Onn));
    }

    // "Lower Alarm Temperature Invalid -- Alarm On"
    // https://github.com/santoslab/hamr-system-testing-case-studies/blob/389f2d6a32f4d9c039b94c4bd300fbff6e21ffb9/isolette/hamr/slang/src/test/system/isolette/SystemTestsCat.scala#L1021-L1130
    fn lower_alarm_temperature_invalid_alarm_on() {
      run_monitor(MonitorInputs {
        upper_alarm: tws(101, ValueStatus::Valid),
        lower_alarm: tws(99, ValueStatus::Invalid),
        current: tws(98, ValueStatus::Valid),
        internal_failure: false,
        mode: Monitor_Mode::Normal_Monitor_Mode,
        last_cmd: On_Off::Off,
      });
      sys_assert_eq!(inspect::get_thermostat_mt_ma_ma_alarm_control(), Some(On_Off::Onn));
    }

    // "Current Temperature Invalid -- Alarm On"
    // https://github.com/santoslab/hamr-system-testing-case-studies/blob/389f2d6a32f4d9c039b94c4bd300fbff6e21ffb9/isolette/hamr/slang/src/test/system/isolette/SystemTestsCat.scala#L1132-L1241
    fn current_temperature_invalid_alarm_on() {
      run_monitor(MonitorInputs {
        upper_alarm: tws(101, ValueStatus::Valid),
        lower_alarm: tws(99, ValueStatus::Valid),
        current: tws(98, ValueStatus::Invalid),
        internal_failure: false,
        mode: Monitor_Mode::Normal_Monitor_Mode,
        last_cmd: On_Off::Off,
      });
      sys_assert_eq!(inspect::get_thermostat_mt_ma_ma_alarm_control(), Some(On_Off::Onn));
    }

    // "Internal Failure -- Alarm On"
    // https://github.com/santoslab/hamr-system-testing-case-studies/blob/389f2d6a32f4d9c039b94c4bd300fbff6e21ffb9/isolette/hamr/slang/src/test/system/isolette/SystemTestsCat.scala#L1243-L1352
    fn internal_failure_alarm_on() {
      run_monitor(MonitorInputs {
        upper_alarm: tws(101, ValueStatus::Valid),
        lower_alarm: tws(99, ValueStatus::Valid),
        current: tws(98, ValueStatus::Valid),
        internal_failure: true,
        mode: Monitor_Mode::Normal_Monitor_Mode,
        last_cmd: On_Off::Off,
      });
      sys_assert_eq!(inspect::get_thermostat_mt_ma_ma_alarm_control(), Some(On_Off::Onn));
    }

    // "Fail Mode -- Alarm On"
    // https://github.com/santoslab/hamr-system-testing-case-studies/blob/389f2d6a32f4d9c039b94c4bd300fbff6e21ffb9/isolette/hamr/slang/src/test/system/isolette/SystemTestsCat.scala#L1354-L1463
    fn fail_mode_alarm_on() {
      run_monitor(MonitorInputs {
        upper_alarm: tws(101, ValueStatus::Valid),
        lower_alarm: tws(99, ValueStatus::Valid),
        current: tws(98, ValueStatus::Valid),
        internal_failure: false,
        mode: Monitor_Mode::Failed_Monitor_Mode,
        last_cmd: On_Off::Off,
      });
      sys_assert_eq!(inspect::get_thermostat_mt_ma_ma_alarm_control(), Some(On_Off::Onn));
    }

    // "Normal Mode -- Alarm Off"
    // https://github.com/santoslab/hamr-system-testing-case-studies/blob/389f2d6a32f4d9c039b94c4bd300fbff6e21ffb9/isolette/hamr/slang/src/test/system/isolette/SystemTestsCat.scala#L1465-L1574
    fn normal_mode_alarm_off() {
      run_monitor(MonitorInputs {
        upper_alarm: tws(101, ValueStatus::Valid),
        lower_alarm: tws(99, ValueStatus::Valid),
        current: tws(100, ValueStatus::Valid),
        internal_failure: false,
        mode: Monitor_Mode::Normal_Monitor_Mode,
        last_cmd: On_Off::Off,
      });
      sys_assert_eq!(inspect::get_thermostat_mt_ma_ma_alarm_control(), Some(On_Off::Off));
    }
  }
}
