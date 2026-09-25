// This file will not be overwritten if HAMR codegen is rerun

//! Port of SystemTestsIllustrations.scala:
//! https://github.com/santoslab/hamr-system-testing-case-studies/blob/389f2d6a32f4d9c039b94c4bd300fbff6e21ffb9/isolette/hamr/slang/src/test/system/isolette/SystemTestsIllustrations.scala

use crate::system_tests::api;
use crate::system_tests::inspect;
use crate::{system_tests, sys_assert, sys_assert_eq};
use data::Isolette_Data_Model::*;
use crate::system_tests::harness::run_property;
use super::{regulator_post_init, reinitialize, tws};
use proptest::prelude::{Just, Strategy};

system_tests! {
  suite illustrations {
    // "Regulator Initialization Test"
    // https://github.com/santoslab/hamr-system-testing-case-studies/blob/389f2d6a32f4d9c039b94c4bd300fbff6e21ffb9/isolette/hamr/slang/src/test/system/isolette/SystemTestsIllustrations.scala#L58-L72
    fn regulator_initialization() {
      let init = regulator_post_init();
      sys_assert!(init.is_some()); // must run before anything is dispatched -- see tests.rs
      let init = init.unwrap();

      // command to heat source should be off
      sys_assert_eq!(init.mhs_sv_lastCmd, Some(On_Off::Off));
      sys_assert_eq!(init.mhs_heat_control, Some(On_Off::Off));

      // regulator status is Init
      sys_assert_eq!(init.mri_regulator_status, Some(Status::Init_Status));
      // failure flag is FALSE
      sys_assert_eq!(init.mri_interface_failure, Some(Failure_Flag_i { flag: false }));
      // regulator mode is INIT
      sys_assert_eq!(init.mrm_regulator_mode, Some(Regulator_Mode::Init_Regulator_Mode));
    }

    // "Failed Status on Operator Interface"
    // https://github.com/santoslab/hamr-system-testing-case-studies/blob/389f2d6a32f4d9c039b94c4bd300fbff6e21ffb9/isolette/hamr/slang/src/test/system/isolette/SystemTestsIllustrations.scala#L74-L122
    fn failed_status_on_operator_interface() {
      reinitialize();

      // run two hyper-periods
      let _ = api::hstep(2);

      // run to Manage Regulator Interface
      let _ = api::run_to_thread(inspect::channels::thermostat_rt_mri_mri_MON);

      // override inputs for MRI, forcing INVALID status for a temperature input
      inspect::set_thermostat_rt_mri_mri(inspect::thermostat_rt_mri_mri_PreState {
        upper_desired_tempWstatus: tws(101, ValueStatus::Valid),
        lower_desired_tempWstatus: tws(99, ValueStatus::Invalid),
        regulator_mode: Regulator_Mode::Normal_Regulator_Mode,
        current_tempWstatus: tws(100, ValueStatus::Valid),
      });

      // run to end of current hyper-period - and check outputs of selected components
      let _ = api::hstep(1);

      // For RegMRM component, regulator mode output should be FAILED
      sys_assert_eq!(inspect::get_thermostat_rt_mrm_mrm_sv_lastRegulatorMode(),
                     Some(Regulator_Mode::Failed_Regulator_Mode));
      sys_assert_eq!(inspect::get_thermostat_rt_mrm_mrm_regulator_mode(),
                     Some(Regulator_Mode::Failed_Regulator_Mode));

      // checked that mode output remains FAILED (it's latched)
      // run three more hyper-periods
      let _ = api::hstep(3);

      // regulator mode should still be failed
      sys_assert_eq!(inspect::get_thermostat_rt_mrm_mrm_sv_lastRegulatorMode(),
                     Some(Regulator_Mode::Failed_Regulator_Mode));
      sys_assert_eq!(inspect::get_thermostat_rt_mrm_mrm_regulator_mode(),
                     Some(Regulator_Mode::Failed_Regulator_Mode));
    }

    // "Three Hyper-periods"
    // https://github.com/santoslab/hamr-system-testing-case-studies/blob/389f2d6a32f4d9c039b94c4bd300fbff6e21ffb9/isolette/hamr/slang/src/test/system/isolette/SystemTestsIllustrations.scala#L124-L133
    // The JVM test only checks that the run completes; here
    // that means the scheduler accepted the command and nothing overran.
    fn three_hyper_periods() {
      reinitialize();
      let before = api::info_state();
      let after = api::hstep(3);
      sys_assert!(after.flags & (api::FLAG_BAD_COMMAND | api::FLAG_OVERRUN) == 0);
      sys_assert_eq!(after.hyperperiod_num, before.hyperperiod_num + 3);
    }

    // "Violate MHS Gumbo Contract"
    // https://github.com/santoslab/hamr-system-testing-case-studies/blob/389f2d6a32f4d9c039b94c4bd300fbff6e21ffb9/isolette/hamr/slang/src/test/system/isolette/SystemTestsIllustrations.scala#L135-L225
    // Seed the bug in thermostat_rt_mhs_mhs_app.rs (the commented-out
    // "seeded bug/error" line) to see this fail.
    //
    // The JVM test draws its inputs from SlangCheck F32 generators constrained to
    //   low     in [LOWER_DESIRED_TEMPERATURE_LOWER_RANGE, LOWER_DESIRED_TEMPERATURE_UPPER_RANGE] = [97, 99]
    //   high    in [max(low, UPPER_DESIRED_TEMPERATURE_LOWER_RANGE), UPPER_DESIRED_TEMPERATURE_UPPER_RANGE] = [max(low, 98), 100]
    //   current <= low - 0.1, Valid   (generator upper bound low.value - f32"0.1", no lower bound)
    // SlangCheck does not run on target, so proptest generates them instead -- see
    // run_property in the generated harness.rs.  Its In_lastCmd = RegMHS.get_lastCmd()
    // is fixed at Off: the expected output does not depend on it when current < low.
    //
    // Floating point vs integer (see tests.rs): the JVM space is continuous F32 and
    // includes fractional points such as low = 97.3, current = 97.2 that cannot be
    // expressed here.  The SysML model's temperatures are i32, so the generators use
    // the whole-degree grid, and "current below low" becomes current <= low - 1 rather
    // than current <= low - 0.1.
    //
    // Where the JVM test draws one random point per run, this runs 64 per image from a
    // fixed seed, and a failure is shrunk to the smallest (low, high, current) that
    // still fails -- with the bug seeded, (97, 98, 0).
    fn violate_mhs_gumbo_contract() {
      let inputs = || (97i32..=99).prop_flat_map(|low|
        (Just(low), core::cmp::max(low, 98)..=100, i32::MIN..=low - 1));
      sys_assert!(run_property(64, inputs, |(low, high, current)| {
        reinitialize();

        // run up to RegMHS
        let _ = api::run_to_thread(inspect::channels::thermostat_rt_mhs_mhs_MON);

        // force MHS to explore the branch that will uncover the seeded bug
        inspect::set_thermostat_rt_mhs_mhs(inspect::thermostat_rt_mhs_mhs_PreState {
          upper_desired_temp: Temp_i { degrees: high },
          lower_desired_temp: Temp_i { degrees: low },
          regulator_mode: Regulator_Mode::Normal_Regulator_Mode,
          current_tempWstatus: tws(current, ValueStatus::Valid),
          sv_lastCmd: On_Off::Off,
        });

        // now we'll let RegMHS process those inputs
        let _ = api::hstep(1);

        // MHS must send Onn via heat_control when current_tempWstatus < lower_desired_temp.
        // Read both before asserting: reads are destructive, one per step.
        let last_cmd = inspect::get_thermostat_rt_mhs_mhs_sv_lastCmd();
        let heat_control = inspect::get_thermostat_rt_mhs_mhs_heat_control();
        proptest::prop_assert_eq!(last_cmd, Some(On_Off::Onn));
        proptest::prop_assert_eq!(heat_control, Some(On_Off::Onn));
        Ok(())
      }));
    }
  }
}
