#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

// This file will not be overwritten if codegen is rerun

use crate::data::*;
use crate::data::Isolette_Data_Model::*;
use crate::bridge::thermostat_mt_mmm_mmm_api::*;
#[cfg(feature = "sel4")]
#[allow(unused_imports)]
use log::{error, warn, info, debug, trace};
use vstd::prelude::*;

verus! {

  pub struct thermostat_mt_mmm_mmm {
    // BEGIN MARKER STATE VARS
    pub lastMonitorMode: Isolette_Data_Model::Monitor_Mode
    // END MARKER STATE VARS
  }

  impl thermostat_mt_mmm_mmm {
    pub fn new() -> Self 
    {
      Self {
        // BEGIN MARKER STATE VAR INIT
        lastMonitorMode: Isolette_Data_Model::Monitor_Mode::default()
        // END MARKER STATE VAR INIT
      }
    }

    // ToDo: Placeholder for method who signature may be auto-generated from an 
    //  GUMBO abstract method.  This method should be considered abstract at the GUMBO level
    //  because its semantic condition "timeout condition satisfies" appears in requirements
    //  and the GUMBO formalization of the requirements, but its meaning is defined in terms
    //  of lower-level implementation checks on timing that be only obtained via executable
    //  functions at the platform level.
    //  Note that if GUMBO adds notions of timing, it may be the case that the semantics
    //  could be specified at the model level.
    //
    // For now, we don't implement the time out condition; 
    // We just assume that the init mode never times out, i.e., the system has infinite
    // time to try to establish the validity conditions on the inputs that lead to the component 
    // transitioning from the Init mode to the Normal mode.
    //
    // Jason: I wasn't sure how to declare this method wrt "self", etc.  It doesn't have any mutable state.
    // Maybe it should go in the struct block instead?
    fn timeout_condition_satisfied(
      &mut self
    ) -> (res: bool)
      ensures (res == false)
    {
      false
    }


    pub fn initialize<API: thermostat_mt_mmm_mmm_Put_Api>(
      &mut self,
      api: &mut thermostat_mt_mmm_mmm_Application_Api<API>)
      ensures
        // BEGIN MARKER INITIALIZATION ENSURES
        // guarantee REQ_MMM_1
        //   Upon the first dispatch of the thread, the monitor mode is Init.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=114 
        api.monitor_mode == Isolette_Data_Model::Monitor_Mode::Init_Monitor_Mode
        // END MARKER INITIALIZATION ENSURES 
    {
      #[cfg(feature = "sel4")]
      info!("initialize entrypoint invoked");
      self.lastMonitorMode = Monitor_Mode::Init_Monitor_Mode;
      api.put_monitor_mode(self.lastMonitorMode);
    }

    pub fn timeTriggered<API: thermostat_mt_mmm_mmm_Full_Api>(
      &mut self,
      api: &mut thermostat_mt_mmm_mmm_Application_Api<API>)
      ensures
        // BEGIN MARKER TIME TRIGGERED ENSURES
        // case REQ_MMM_2
        //   If the current mode is Init, then
        //   the mode is set to NORMAL iff the monitor status is true (valid) (see Table A-15), i.e.,
        //   if  NOT (Monitor Interface Failure OR Monitor Internal Failure)
        //   AND Current Temperature.Status = Valid
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=114 
        (old(self).lastMonitorMode == Isolette_Data_Model::Monitor_Mode::Init_Monitor_Mode) ==>
          (!(api.interface_failure.flag || api.internal_failure.flag) &&
             (api.current_tempWstatus.status == Isolette_Data_Model::ValueStatus::Valid) ==>
             (api.monitor_mode == Isolette_Data_Model::Monitor_Mode::Normal_Monitor_Mode)),
        // case REQ_MMM_3
        //   If the current Monitor mode is Normal, then
        //   the Monitor mode is set to Failed iff
        //   the Monitor status is false, i.e.,
        //   if  (Monitor Interface Failure OR Monitor Internal Failure)
        //   OR NOT(Current Temperature.Status = Valid)
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=114 
        (old(self).lastMonitorMode == Isolette_Data_Model::Monitor_Mode::Normal_Monitor_Mode) ==>
          (api.interface_failure.flag || api.internal_failure.flag ||
             (api.current_tempWstatus.status != Isolette_Data_Model::ValueStatus::Valid) ==>
             (api.monitor_mode == Isolette_Data_Model::Monitor_Mode::Failed_Monitor_Mode)),
        // case REQ_MMM_4
        //   If the current mode is Init, then
        //   the mode is set to Failed iff the time during
        //   which the thread has been in Init mode exceeds the
        //   Monitor Init Timeout value.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=114 
        (old(self).lastMonitorMode == Isolette_Data_Model::Monitor_Mode::Init_Monitor_Mode) ==>
          (false == (api.monitor_mode == Isolette_Data_Model::Monitor_Mode::Failed_Monitor_Mode))
        // END MARKER TIME TRIGGERED ENSURES 
    {
      #[cfg(feature = "sel4")]
      info!("compute entrypoint invoked");

       // -------------- Get values of input ports ------------------
       let currentTempWstatus: TempWstatus_i = api.get_current_tempWstatus();
       let current_temperature_status: ValueStatus = currentTempWstatus.status;
       let interface_failure: Failure_Flag_i = api.get_interface_failure();
       let internal_failure: Failure_Flag_i = api.get_internal_failure();

       // determine monitor status as specified in FAA REMH Table A-15
       //  monitor_status = NOT (Monitor Interface Failure OR Monitor Internal Failure)
       //                          AND Current Temperature.Status = Valid
       let monitor_status: bool = 
              (!(interface_failure.flag || internal_failure.flag)
                && (current_temperature_status == ValueStatus::Valid));

       match self.lastMonitorMode {
         // Transitions from INIT mode
         Monitor_Mode::Init_Monitor_Mode => {
           if monitor_status {
              // REQ-MRM-2
              self.lastMonitorMode = Monitor_Mode::Normal_Monitor_Mode;
           } else if self.timeout_condition_satisfied() {
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
          warn!("Unexpected channel {}", channel)
        }
      }
    }
  }

}
