// This file will not be overwritten if HAMR codegen is rerun

//! System tests.  This file is preserved across regeneration.
//!
//! The tests are ports of the JVM system tests in
//! hamr-system-testing-case-studies/isolette/hamr/slang/src/test/system/isolette,
//! one file per Scala test class, under `system_tests/tests/`.  The suite name in
//! each file is the class name, so `make CONFIG=test_scheduler.mk TESTS=cat::` (or
//! `bin/run-tests.cmd cat::`) runs one class.
//!
//! Tests must be **order-independent** and establish their own preconditions: the
//! runner normalizes the schedule position between tests, but component state is
//! whatever the previous test left behind.  The JVM tests get that for free by
//! calling `Art.initializePhase` at the top of every test; the Microkit system is
//! initialized once, at boot.  [`reinitialize`] below is the stand-in.
//!
//! Assertions are `sys_assert!` / `sys_assert_eq!`, not `assert!`: a panic would
//! take the protection domain down and end the run.
//!
//! Port reads are destructive: `inspect::get_*` dequeues through the controller's
//! own receive cursor, so a second read of the same port with no dispatch in between
//! returns `None`.  Read each port once per step.  This is where the JVM idiom of
//! `check_concrete_outputs(..)` followed by `must_match(.., get_api_X())` on the same
//! port does not carry over.
//!
//! Floating point vs integer temperatures: the two isolette models type their
//! temperatures differently.
//!
//! | | JVM (AADL model, Slang) | Microkit (SysML model, Rust) |
//! |---|---|---|
//! | `Temp`, `TempWstatus`, `PhysicalTemp` | `value: F32` | `degrees: i32` (`Base_Types::Integer_32`) |
//! | literal in a test | `TempWstatus_impl(98f, ValueStatus.Valid)` | `tws(98, ValueStatus::Valid)` |
//! | MRI's displayed temperature | `ROUND(currentTemp.value)` (`ROUND` is currently the identity on `F32`) | `current_temp.degrees`, no rounding needed |
//!
//! Every temperature the JVM tests use is a whole number (`98f`, `99f`, `101f`, ...),
//! so those port exactly and every `<`, `>` and `==` the components apply to them
//! decides the same way in both models.  Anything fractional does not: a JVM value of
//! `97.5f` has no Microkit equivalent, and the boundary between "below" and "at" the
//! lower bound moves from `low - 0.1` (the smallest step the JVM test generators use)
//! to `low - 1`.  The one test where that matters is `violate_mhs_gumbo_contract`,
//! whose JVM version draws `current < low` from a generator bounded at
//! `low - 0.1`; see the note there.

use crate::system_tests::api;
use crate::system_tests::inspect;
use crate::system_test_files;
use data::Isolette_Data_Model::*;

system_test_files! {
  mod illustrations_tests;  // SystemTestsIllustrations.scala -- runs first, see regulator_post_init
  mod zhaoxiang_tests;      // SystemTestsZhaoxiang.scala
  mod cat_tests;            // SystemTestsCat.scala
  mod smoke_tests;
}

/// `TempWstatus_i` literal, the counterpart of the JVM's `TempWstatus_impl(<n>f, status)`.
/// Takes whole degrees: the SysML model's temperatures are `i32`, not `F32` -- see the
/// module note above.
pub fn tws(degrees: i32, status: ValueStatus) -> TempWstatus_i {
  TempWstatus_i { degrees, status }
}

/// Stand-in for `Art.initializePhase(scheduler)`.
///
/// Every GUMBO state variable is set back to the value its component's `initialize`
/// leaves it at.  Those five variables are the only state the Rust components keep,
/// so after the next dispatch of each thread the thermostat is back in its
/// post-initialization state.  Output ports are not reset; every thread is periodic
/// and rewrites its outputs each hyperperiod, so they recover on the first step.
///
/// Not reset: the temperature sensor's private sweep (`lastTemp`/`delta` in
/// `temperature_sensor_cpi_thermostat_user.c`), which has no injection point.  The
/// ported tests do not depend on it, because each one injects the current
/// temperature itself.
///
/// Call it at the start of the test, before any step: the values are adopted on
/// each thread's next dispatch.
pub fn reinitialize() {
  inspect::put_thermostat_rt_mhs_mhs_sv_lastCmd(On_Off::Off);
  inspect::put_thermostat_rt_mrm_mrm_sv_lastRegulatorMode(Regulator_Mode::Init_Regulator_Mode);
  inspect::put_thermostat_mt_mmi_mmi_sv_lastCmd(On_Off::default()); // MMI's initialize leaves it unset
  inspect::put_thermostat_mt_ma_ma_sv_lastCmd(On_Off::Off);
  inspect::put_thermostat_mt_mmm_mmm_sv_lastMonitorMode(Monitor_Mode::Init_Monitor_Mode);
}

/// The regulator subsystem's outputs as `initialize` left them, before any dispatch.
#[derive(Clone, Copy)]
pub struct RegulatorPostInit {
  pub mhs_heat_control: Option<On_Off>,
  pub mhs_sv_lastCmd: Option<On_Off>,
  pub mri_displayed_temp: Option<Temp_i>,
  pub mri_interface_failure: Option<Failure_Flag_i>,
  pub mri_lower_desired_temp: Option<Temp_i>,
  pub mri_regulator_status: Option<Status>,
  pub mri_upper_desired_temp: Option<Temp_i>,
  pub mrm_regulator_mode: Option<Regulator_Mode>,
  pub mrm_sv_lastRegulatorMode: Option<Regulator_Mode>,
}

static mut REGULATOR_POST_INIT: Option<RegulatorPostInit> = None;

/// The post-initialization outputs, captured on the first call.
///
/// This is the one thing [`reinitialize`] cannot recreate: state variables can be
/// injected, but what `initialize` *published* exists only until the first dispatch
/// overwrites it.  So the snapshot is taken once, before anything has been
/// dispatched, and every initialization test reads it -- which also sidesteps the
/// destructive reads, since three tests look at the same values.
///
/// Returns `None` when the first call comes after a dispatch.  Each file's
/// initialization test is its first test, and `illustrations_tests.rs` is the first
/// file above, so the full suite and a `TESTS=<class>::` run both reach one before
/// anything steps.  Keep it that way when adding tests.
pub fn regulator_post_init() -> Option<RegulatorPostInit> {
  unsafe {
    if let Some(s) = REGULATOR_POST_INIT {
      return Some(s);
    }
  }
  let st = api::info_state();
  if st.last_dispatched_ch != 0 || st.hyperperiod_num != 0 {
    return None;
  }
  let s = RegulatorPostInit {
    mhs_heat_control: inspect::get_thermostat_rt_mhs_mhs_heat_control(),
    mhs_sv_lastCmd: inspect::get_thermostat_rt_mhs_mhs_sv_lastCmd(),
    mri_displayed_temp: inspect::get_thermostat_rt_mri_mri_displayed_temp(),
    mri_interface_failure: inspect::get_thermostat_rt_mri_mri_interface_failure(),
    mri_lower_desired_temp: inspect::get_thermostat_rt_mri_mri_lower_desired_temp(),
    mri_regulator_status: inspect::get_thermostat_rt_mri_mri_regulator_status(),
    mri_upper_desired_temp: inspect::get_thermostat_rt_mri_mri_upper_desired_temp(),
    mrm_regulator_mode: inspect::get_thermostat_rt_mrm_mrm_regulator_mode(),
    mrm_sv_lastRegulatorMode: inspect::get_thermostat_rt_mrm_mrm_sv_lastRegulatorMode(),
  };
  unsafe {
    REGULATOR_POST_INIT = Some(s);
  }
  Some(s)
}

/// The inputs every regulator test in the Cat and Zhaoxiang classes injects.
pub struct RegulatorInputs {
  pub upper_desired: TempWstatus_i,
  pub lower_desired: TempWstatus_i,
  pub current: TempWstatus_i,
  pub internal_failure: bool,
  pub mode: Regulator_Mode,
}

/// The shared body of the Cat/Zhaoxiang regulator tests, up to the assertion:
///
/// ```scala
/// Art.initializePhase(scheduler)
/// compute(ISZ(Hstep(2)))
/// compute(ISZ(RunToThread("RegMRI")))
/// RegMRI.put_concrete_inputs(upper, lower, mode, current)
/// RegMRM.put_internal_failure(internal_failure)
/// RegMRM.put_current_tempWstatus(current)
/// RegMHS.put_current_tempWstatus(current)
/// compute(ISZ(Hstep(1)))
/// ```
///
/// Injection acts as the producer, and the sensor's `current_tempWstatus` fans out to
/// MRI, MRM and MHS alike, so the one put in `set_thermostat_rt_mri_mri` is all three
/// of the JVM's `put_current_tempWstatus` calls.  Likewise MRM's `internal_failure`
/// is published by DRF, which has already run when we park before MRI.
pub fn run_regulator(i: RegulatorInputs) {
  run_regulator_with_mrm_mode(i, None);
}

/// [`run_regulator`], optionally forcing MRM's `lastRegulatorMode` for the stepped
/// hyperperiod.
///
/// `RegulatorInputs::mode` is only MRI's `regulator_mode` input, and MRI uses that
/// input for nothing but its own `regulator_status` and `displayed_temp`.  MHS takes
/// its mode from MRM, which recomputes it from the failure flags and the current
/// temperature.  So setting `mode` alone cannot put the regulator into a mode; the
/// JVM "Fail Mode -- Heat Control Off" tests do exactly that and fail (Onn, not Off)
/// on both the JVM and Microkit.  Forcing MRM's state variable does it: MRM adopts
/// the value on its next dispatch, which comes after MRI's in the stepped
/// hyperperiod, and Failed is latched, so MRM outputs it and MHS reads it.
pub fn run_regulator_with_mrm_mode(i: RegulatorInputs, mrm_mode: Option<Regulator_Mode>) {
  reinitialize();
  let _ = api::hstep(2);
  let _ = api::run_to_thread(inspect::channels::thermostat_rt_mri_mri_MON);
  if let Some(m) = mrm_mode {
    inspect::put_thermostat_rt_mrm_mrm_sv_lastRegulatorMode(m);
  }
  inspect::set_thermostat_rt_mri_mri(inspect::thermostat_rt_mri_mri_PreState {
    regulator_mode: i.mode,
    lower_desired_tempWstatus: i.lower_desired,
    upper_desired_tempWstatus: i.upper_desired,
    current_tempWstatus: i.current,
  });
  inspect::put_thermostat_rt_drf_drf_internal_failure(Failure_Flag_i { flag: i.internal_failure });
  let _ = api::hstep(1);
}

/// The inputs every monitor test in the Cat class injects.
pub struct MonitorInputs {
  pub upper_alarm: TempWstatus_i,
  pub lower_alarm: TempWstatus_i,
  pub current: TempWstatus_i,
  pub internal_failure: bool,
  pub mode: Monitor_Mode,
  pub last_cmd: On_Off,
}

/// The shared body of the Cat monitor tests, up to the assertion:
///
/// ```scala
/// Art.initializePhase(scheduler)
/// compute(ISZ(Hstep(2)))
/// compute(ISZ(RunToThread("MonMMI")))
/// MonMMI.put_concrete_inputs(upper, lower, mode, current, In_lastCmd = last_cmd)
/// MonMMM.put_current_tempWstatus(current)
/// MonMMM.put_internal_failure(internal_failure)
/// MonMA.put_current_tempWstatus(current)
/// compute(ISZ(Hstep(1)))
/// ```
///
/// As in [`run_regulator`], the sensor put reaches MMI, MMM and MA, and MMM's
/// `internal_failure` comes from DMF, which runs before MMI.
pub fn run_monitor(i: MonitorInputs) {
  reinitialize();
  let _ = api::hstep(2);
  let _ = api::run_to_thread(inspect::channels::thermostat_mt_mmi_mmi_MON);
  inspect::set_thermostat_mt_mmi_mmi(inspect::thermostat_mt_mmi_mmi_PreState {
    monitor_mode: i.mode,
    lower_alarm_tempWstatus: i.lower_alarm,
    upper_alarm_tempWstatus: i.upper_alarm,
    current_tempWstatus: i.current,
    sv_lastCmd: i.last_cmd,
  });
  inspect::put_thermostat_mt_dmf_dmf_internal_failure(Failure_Flag_i { flag: i.internal_failure });
  let _ = api::hstep(1);
}
