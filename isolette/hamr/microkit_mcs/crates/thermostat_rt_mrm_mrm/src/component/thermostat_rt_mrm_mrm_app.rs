// This file will not be overwritten if HAMR codegen is rerun

use data::*;
use data::Isolette_Data_Model::*;
use crate::bridge::thermostat_rt_mrm_mrm_api::*;
use vstd::prelude::*;

#[verus_verify]
pub struct thermostat_rt_mrm_mrm {
  // BEGIN MARKER STATE VARS
  pub lastRegulatorMode: Isolette_Data_Model::Regulator_Mode,
  // END MARKER STATE VARS
}

#[verus_verify]
impl thermostat_rt_mrm_mrm {
  pub fn new() -> Self
  {
    Self {
      // BEGIN MARKER STATE VAR INIT
      lastRegulatorMode: Isolette_Data_Model::Regulator_Mode::default(),
      // END MARKER STATE VAR INIT
    }
  }

  #[verus_spec(
    ensures
      // BEGIN MARKER INITIALIZATION ENSURES
      // guarantee REQ_MRM_1
      //   The initial mode of the regular is INIT
      //   https://www.faa.gov/sites/faa.gov/files/aircraft/air_cert/design_approvals/air_software/AR-08-32.pdf#page=109 
      api.regulator_mode == Isolette_Data_Model::Regulator_Mode::Init_Regulator_Mode,
      // END MARKER INITIALIZATION ENSURES
  )]
  pub fn initialize<API: thermostat_rt_mrm_mrm_Put_Api> (
    &mut self,
    api: &mut thermostat_rt_mrm_mrm_Application_Api<API>)
  {
    log_info("initialize entrypoint invoked");

    self.lastRegulatorMode = Regulator_Mode::Init_Regulator_Mode;
    api.put_regulator_mode(self.lastRegulatorMode);
  }

  #[verus_spec(
    requires
      // PLACEHOLDER MARKER TIME TRIGGERED REQUIRES
    ensures
      // BEGIN MARKER TIME TRIGGERED ENSURES
      // guarantee update_lastRegulatorMode
      self.lastRegulatorMode == api.regulator_mode,
      // case REQ_MRM_2
      //   'transition from Init to Normal'
      //   If the current regulator mode is Init, then
      //   the regulator mode is set to NORMAL iff the regulator status is valid (see Table A-10), i.e.,
      //     if NOT (Regulator Interface Failure OR Regulator Internal Failure)
      //        AND Current Temperature.Status = Valid
      //   https://www.faa.gov/sites/faa.gov/files/aircraft/air_cert/design_approvals/air_software/AR-08-32.pdf#page=109 
      (old(self).lastRegulatorMode == Isolette_Data_Model::Regulator_Mode::Init_Regulator_Mode) ==>
        ((regulator_status(api.interface_failure, api.internal_failure, api.current_tempWstatus) && !(timeout_condition_satisfied())) == (api.regulator_mode == Isolette_Data_Model::Regulator_Mode::Normal_Regulator_Mode)),
      // case REQ_MRM_Maintain_Normal
      //   'maintaining NORMAL, NORMAL to NORMAL'
      //   If the current regulator mode is Normal, then
      //   the regulator mode is stays normal iff
      //   the regulaor status is not false i.e.,
      //          if NOT(
      //              (Regulator Interface Failure OR Regulator Internal Failure)
      //              OR NOT(Current Temperature.Status = Valid)
      //          )
      //   https://www.faa.gov/sites/faa.gov/files/aircraft/air_cert/design_approvals/air_software/AR-08-32.pdf#page=109 
      ((old(self).lastRegulatorMode == Isolette_Data_Model::Regulator_Mode::Normal_Regulator_Mode) &&
        regulator_status(old(api).interface_failure, old(api).internal_failure, old(api).current_tempWstatus)) ==>
        (api.regulator_mode == Isolette_Data_Model::Regulator_Mode::Normal_Regulator_Mode),
      // case REQ_MRM_3
      //   'transition for NORMAL to FAILED'
      //   If the current regulator mode is Normal, then
      //   the regulator mode is set to Failed iff
      //   the regulator status is false, i.e.,
      //      if  (Regulator Interface Failure OR Regulator Internal Failure)
      //          OR NOT(Current Temperature.Status = Valid)
      //   https://www.faa.gov/sites/faa.gov/files/aircraft/air_cert/design_approvals/air_software/AR-08-32.pdf#page=109 
      (old(self).lastRegulatorMode == Isolette_Data_Model::Regulator_Mode::Normal_Regulator_Mode) ==>
        (!(regulator_status(api.interface_failure, api.internal_failure, api.current_tempWstatus)) == (api.regulator_mode == Isolette_Data_Model::Regulator_Mode::Failed_Regulator_Mode)),
      // case REQ_MRM_4
      //   'transition from INIT to FAILED'
      //   If the current regulator mode is Init, then the regulator mode is set to
      //   Failed iff the time during which the thread has been in Init mode exceeds the
      //   Regulator Init Timeout value (parallel to REQ-MMM-4, the monitor's Init timeout).
      //   https://www.faa.gov/sites/faa.gov/files/aircraft/air_cert/design_approvals/air_software/AR-08-32.pdf#page=109
      (old(self).lastRegulatorMode == Isolette_Data_Model::Regulator_Mode::Init_Regulator_Mode) ==>
        (timeout_condition_satisfied() == (api.regulator_mode == Isolette_Data_Model::Regulator_Mode::Failed_Regulator_Mode)),
      // case REQ_MRM_MaintainFailed
      //   'maintaining FAIL, FAIL to FAIL'
      //   If the current regulator mode is Failed, then
      //   the regulator mode remains in the Failed state and the LastRegulator mode remains Failed.REQ-MRM-Maintain-Failed
      //   https://www.faa.gov/sites/faa.gov/files/aircraft/air_cert/design_approvals/air_software/AR-08-32.pdf#page=109
      (old(self).lastRegulatorMode == Isolette_Data_Model::Regulator_Mode::Failed_Regulator_Mode) ==>
        (api.regulator_mode == Isolette_Data_Model::Regulator_Mode::Failed_Regulator_Mode),
      // END MARKER TIME TRIGGERED ENSURES
  )]
  pub fn timeTriggered<API: thermostat_rt_mrm_mrm_Full_Api> (
    &mut self,
    api: &mut thermostat_rt_mrm_mrm_Application_Api<API>)
  {
    //log_info("compute entrypoint invoked");

    // -------------- Get values of input ports ------------------
    let currentTempWstatus: TempWstatus_i = api.get_current_tempWstatus();
    let current_temperature_status: ValueStatus = currentTempWstatus.status;
    let interface_failure: Failure_Flag_i = api.get_interface_failure();
    let internal_failure: Failure_Flag_i = api.get_internal_failure();

    // determine regulator status as specified in FAA REMH Table A-10
    //    regulator_status = NOT (Monitor Interface Failure OR Monitor Internal Failure)
    //                          AND Current Temperature.Status = Valid

    //let regulator_status: bool =
    //        (!(interface_failure.flag || internal_failure.flag)
    //          && (current_temperature_status == ValueStatus::Valid));

    let regulator_status: bool =
      (!(interface_failure.flag || internal_failure.flag)
        && match current_temperature_status {
          ValueStatus::Valid => true,
          ValueStatus::Invalid => false
      });

    match self.lastRegulatorMode {
      // Transitions from INIT mode
      Regulator_Mode::Init_Regulator_Mode => {
        if regulator_status {
          // REQ-MRM-2
          self.lastRegulatorMode = Regulator_Mode::Normal_Regulator_Mode;
        } else if timeout_condition_satisfied_exec() {
          // REQ-MRM-4
          self.lastRegulatorMode = Regulator_Mode::Failed_Regulator_Mode;
        } else {
          // stay in Init
        };
      },

      // Transitions from NORMAL mode
      Regulator_Mode::Normal_Regulator_Mode => {
        if !regulator_status {
          // REQ-MRM-4
          self.lastRegulatorMode = Regulator_Mode::Failed_Regulator_Mode;
        };
      },

      // Transitions from FAILED Mode (do nothing -- system must be rebooted)
      Regulator_Mode::Failed_Regulator_Mode => {
        // do nothing
      }
    };

    api.put_regulator_mode(self.lastRegulatorMode);
  }

  pub fn notify(
    &mut self,
    channel: microkit_channel)
  {
    // this method is called when the monitor does not handle the passed in channel
    match channel {
      _ => {
        log_warn_channel(channel)
      }
    }
  }
}

#[verus_verify(external_body)]
pub fn log_info(msg: &str)
{
  log::info!("{0}", msg);
}

#[verus_verify(external_body)]
pub fn log_warn_channel(channel: u32)
{
  log::warn!("Unexpected channel: {0}", channel);
}

verus!{
  // BEGIN MARKER GUMBO METHODS
  pub open spec fn regulator_status(
    interface_failure: Isolette_Data_Model::Failure_Flag_i,
    internal_failure: Isolette_Data_Model::Failure_Flag_i,
    current_tempWstatus: Isolette_Data_Model::TempWstatus_i) -> bool
  {
    !(interface_failure.flag || internal_failure.flag) &&
      (current_tempWstatus.status == Isolette_Data_Model::ValueStatus::Valid)
  }

  pub open spec fn timeout_condition_satisfied() -> bool
  {
    false
  }
  // END MARKER GUMBO METHODS
}
 
#[verus_spec(ret =>
  ensures
    ret == timeout_condition_satisfied()
)]
pub fn timeout_condition_satisfied_exec() -> bool
{
  false
}
