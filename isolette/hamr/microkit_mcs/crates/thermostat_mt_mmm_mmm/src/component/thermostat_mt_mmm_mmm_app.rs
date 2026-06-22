// This file will not be overwritten if HAMR codegen is rerun

use data::*;
use data::Isolette_Data_Model::*;
use crate::bridge::thermostat_mt_mmm_mmm_api::*;
use vstd::prelude::*;

#[verus_verify]
pub struct thermostat_mt_mmm_mmm {
  // BEGIN MARKER STATE VARS
  pub lastMonitorMode: Isolette_Data_Model::Monitor_Mode,
  // END MARKER STATE VARS
}

#[verus_verify]
impl thermostat_mt_mmm_mmm {
  pub fn new() -> Self
  {
    Self {
      // BEGIN MARKER STATE VAR INIT
      lastMonitorMode: Isolette_Data_Model::Monitor_Mode::default(),
      // END MARKER STATE VAR INIT
    }
  }

  #[verus_spec(
    ensures
      // BEGIN MARKER INITIALIZATION ENSURES
      // guarantee REQ_MMM_1
      //   Upon the first dispatch of the thread, the monitor mode is Init.
      //   https://www.faa.gov/sites/faa.gov/files/aircraft/air_cert/design_approvals/air_software/AR-08-32.pdf#page=114 
      api.monitor_mode == Isolette_Data_Model::Monitor_Mode::Init_Monitor_Mode,
      // guarantee update_lastMonitorMode
      self.lastMonitorMode == api.monitor_mode,
      // END MARKER INITIALIZATION ENSURES
  )]
  pub fn initialize<API: thermostat_mt_mmm_mmm_Put_Api> (
    &mut self,
    api: &mut thermostat_mt_mmm_mmm_Application_Api<API>)
  {
    log_info("initialize entrypoint invoked");
    self.lastMonitorMode = Monitor_Mode::Init_Monitor_Mode;
    api.put_monitor_mode(self.lastMonitorMode);
  }

  #[verus_spec(
    requires
      // PLACEHOLDER MARKER TIME TRIGGERED REQUIRES
    ensures
      // BEGIN MARKER TIME TRIGGERED ENSURES
      // case REQ_MMM_2
      //   If the current mode is Init, then
      //   the mode is set to NORMAL iff the monitor status is true (valid) (see Table A-15), i.e.,
      //   if  NOT (Monitor Interface Failure OR Monitor Internal Failure)
      //   AND Current Temperature.Status = Valid.
      //   Formalized as an iff per the requirement text (with the Init timeout
      //   transition of REQ-MMM-4 excluded so the two cases cannot conflict);
      //   the converse direction is needed at the system level to conclude
      //   that NORMAL mode implies no monitor interface failure.
      //   https://www.faa.gov/sites/faa.gov/files/aircraft/air_cert/design_approvals/air_software/AR-08-32.pdf#page=114 
      (old(self).lastMonitorMode == Isolette_Data_Model::Monitor_Mode::Init_Monitor_Mode) ==>
        ((monitor_status(api.interface_failure, api.internal_failure, api.current_tempWstatus) && !(timeout_condition_satisfied())) == (api.monitor_mode == Isolette_Data_Model::Monitor_Mode::Normal_Monitor_Mode)),
      // case REQ_MMM_Maintain_Normal
      //   'maintaining NORMAL, NORMAL to NORMAL'
      //   If the current monitor mode is Normal and the monitor status is true
      //   (no interface/internal failure and the current temperature is valid),
      //   the monitor mode stays Normal.
      //   https://www.faa.gov/sites/faa.gov/files/aircraft/air_cert/design_approvals/air_software/AR-08-32.pdf#page=114 
      ((old(self).lastMonitorMode == Isolette_Data_Model::Monitor_Mode::Normal_Monitor_Mode) &&
        monitor_status(old(api).interface_failure, old(api).internal_failure, old(api).current_tempWstatus)) ==>
        (api.monitor_mode == Isolette_Data_Model::Monitor_Mode::Normal_Monitor_Mode),
      // case REQ_MMM_3
      //   If the current Monitor mode is Normal, then
      //   the Monitor mode is set to Failed iff
      //   the Monitor status is false, i.e.,
      //   if  (Monitor Interface Failure OR Monitor Internal Failure)
      //   OR NOT(Current Temperature.Status = Valid)
      //   https://www.faa.gov/sites/faa.gov/files/aircraft/air_cert/design_approvals/air_software/AR-08-32.pdf#page=114 
      (old(self).lastMonitorMode == Isolette_Data_Model::Monitor_Mode::Normal_Monitor_Mode) ==>
        (!(monitor_status(api.interface_failure, api.internal_failure, api.current_tempWstatus)) == (api.monitor_mode == Isolette_Data_Model::Monitor_Mode::Failed_Monitor_Mode)),
      // case REQ_MMM_4
      //   If the current mode is Init, then
      //   the mode is set to Failed iff the time during
      //   which the thread has been in Init mode exceeds the
      //   Monitor Init Timeout value.
      //   https://www.faa.gov/sites/faa.gov/files/aircraft/air_cert/design_approvals/air_software/AR-08-32.pdf#page=114 
      (old(self).lastMonitorMode == Isolette_Data_Model::Monitor_Mode::Init_Monitor_Mode) ==>
        (timeout_condition_satisfied() == (api.monitor_mode == Isolette_Data_Model::Monitor_Mode::Failed_Monitor_Mode)),
      // case Failed_Mode_Absorbing
      //   If the current mode is Failed, the mode remains Failed.
      //   Derived requirement -- not numbered in the requirements spec, but
      //   implied by Figure A-6: the monitor mode state machine has no
      //   transitions out of the Failed mode.  The diagram's implicit frame
      //   (no arc drawn means no transition) must be stated explicitly here:
      //   a contract that is silent for lastMonitorMode == Failed allows any
      //   mode value (e.g., Failed to Normal while the interface is still
      //   failing), which would break the system-level property that NORMAL
      //   mode implies no monitor interface failure.
      //   https://www.faa.gov/sites/faa.gov/files/aircraft/air_cert/design_approvals/air_software/AR-08-32.pdf#page=114 
      (old(self).lastMonitorMode == Isolette_Data_Model::Monitor_Mode::Failed_Monitor_Mode) ==>
        (api.monitor_mode == Isolette_Data_Model::Monitor_Mode::Failed_Monitor_Mode),
      // END MARKER TIME TRIGGERED ENSURES
  )]
  pub fn timeTriggered<API: thermostat_mt_mmm_mmm_Full_Api> (
    &mut self,
    api: &mut thermostat_mt_mmm_mmm_Application_Api<API>)
  {
    //log_info("compute entrypoint invoked");

     // -------------- Get values of input ports ------------------
     let currentTempWstatus: TempWstatus_i = api.get_current_tempWstatus();
     let current_temperature_status: ValueStatus = currentTempWstatus.status;
     let interface_failure: Failure_Flag_i = api.get_interface_failure();
     let internal_failure: Failure_Flag_i = api.get_internal_failure();

     // determine monitor status as specified in FAA REMH Table A-15
     //  monitor_status = NOT (Monitor Interface Failure OR Monitor Internal Failure)
     //                          AND Current Temperature.Status = Valid
     //let monitor_status: bool =
     //       (!(interface_failure.flag || internal_failure.flag)
     //         && (current_temperature_status == ValueStatus::Valid));
    let monitor_status: bool = match (interface_failure.flag, internal_failure.flag, current_temperature_status) {
      (false, false, ValueStatus::Valid) => true,
      _ => false,
    };

     match self.lastMonitorMode {
       // Transitions from INIT mode
       Monitor_Mode::Init_Monitor_Mode => {
         if monitor_status {
            // REQ-MRM-2
            self.lastMonitorMode = Monitor_Mode::Normal_Monitor_Mode;
         } else if Self::timeout_condition_satisfied_exec() {
            // REQ-MMM-4
            self.lastMonitorMode = Monitor_Mode::Failed_Monitor_Mode;
         } else {
            // assert(self.lastMonitorMode == Monitor_Mode::Init_Monitor_Mode); // debugging assertion -- should succeed, but fails
            // otherwise we stay in Init mode
            // ToDo: the following assignment isn't needed in the Slang code for Logika to verify.
            // Why is it needed here??
            self.lastMonitorMode = Monitor_Mode::Init_Monitor_Mode;
         };
       },

       // Transitions from NORMAL mode
       Monitor_Mode::Normal_Monitor_Mode => {
         if !monitor_status {
             // REQ-MRM-4
            self.lastMonitorMode = Monitor_Mode::Failed_Monitor_Mode;
         };
       },

       // Transitions from FAILED Mode (do nothing -- system must be rebooted)
       Monitor_Mode::Failed_Monitor_Mode => {
          // do nothing
       }
    };

     api.put_monitor_mode(self.lastMonitorMode);
  }

  pub fn notify(
    &mut self,
    channel: microkit_channel)
  {
    // this method is called when the monitor does not handle the passed in channel
    match channel {
      _ => {
        #[cfg(feature = "sel4")]
        log_warn_channel(channel)
      }
    }
  }

  #[verus_spec( res =>
    ensures (res == timeout_condition_satisfied())
  )]
  fn timeout_condition_satisfied_exec() -> bool
  {
    false
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

verus! {

  // BEGIN MARKER GUMBO METHODS
  pub open spec fn monitor_status(
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
