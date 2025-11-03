// This file will not be overwritten if codegen is rerun

use data::*;
use data::Isolette_Data_Model::*;
use crate::bridge::thermostat_rt_mhs_mhs_api::*;
use vstd::prelude::*;

verus! {

/// Helper macros to make usage ergonomic

  pub struct thermostat_rt_mhs_mhs {
    // BEGIN MARKER STATE VARS
    pub lastCmd: Isolette_Data_Model::On_Off
    // END MARKER STATE VARS
  }

  impl thermostat_rt_mhs_mhs {
    pub fn new() -> Self 
    {
      Self {
        // BEGIN MARKER STATE VAR INIT
        lastCmd: Isolette_Data_Model::On_Off::default()
        // END MARKER STATE VAR INIT
      }
    }

    pub fn initialize<API: thermostat_rt_mhs_mhs_Put_Api>(
      &mut self,
      api: &mut thermostat_rt_mhs_mhs_Application_Api<API>)
      ensures
        // BEGIN MARKER INITIALIZATION ENSURES
        // guarantee initlastCmd
        self.lastCmd == Isolette_Data_Model::On_Off::Off,
        // guarantee REQ_MHS_1
        //   If the Regulator Mode is INIT, the Heat Control shall be
        //   set to Off.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=110 
        api.heat_control == Isolette_Data_Model::On_Off::Off,
        // END MARKER INITIALIZATION ENSURES 
    {
      log_info("initialize entrypoint invoked");

      self.lastCmd = On_Off::Off;
      // REQ-MHS-1: If the Regulator Mode is INIT, the Heat Control shall be
      // set to Off
      let currentCmd = On_Off::Off;
      api.put_heat_control(currentCmd)      
    }

    pub fn timeTriggered<API: thermostat_rt_mhs_mhs_Full_Api>(
      &mut self,
      api: &mut thermostat_rt_mhs_mhs_Application_Api<API>)
      requires
        // BEGIN MARKER TIME TRIGGERED REQUIRES
        // assume lower_is_lower_temp
        old(api).lower_desired_temp.degrees <= old(api).upper_desired_temp.degrees,
        // END MARKER TIME TRIGGERED REQUIRES
      ensures
        // BEGIN MARKER TIME TRIGGERED ENSURES
        // guarantee lastCmd
        //   Set lastCmd to value of output Cmd port
        self.lastCmd == api.heat_control,
        // case REQ_MHS_1
        //   If the Regulator Mode is INIT, the Heat Control shall be
        //   set to Off.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=110 
        (old(api).regulator_mode == Isolette_Data_Model::Regulator_Mode::Init_Regulator_Mode) ==>
          (api.heat_control == Isolette_Data_Model::On_Off::Off),
        // case REQ_MHS_2
        //   If the Regulator Mode is NORMAL and the Current Temperature is less than
        //   the Lower Desired Temperature, the Heat Control shall be set to On.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=110 
        ((old(api).regulator_mode == Isolette_Data_Model::Regulator_Mode::Normal_Regulator_Mode) &&
          (old(api).current_tempWstatus.degrees < old(api).lower_desired_temp.degrees)) ==>
          (api.heat_control == Isolette_Data_Model::On_Off::Onn),
        // case REQ_MHS_3
        //   If the Regulator Mode is NORMAL and the Current Temperature is greater than
        //   the Upper Desired Temperature, the Heat Control shall be set to Off.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=110 
        ((old(api).regulator_mode == Isolette_Data_Model::Regulator_Mode::Normal_Regulator_Mode) &&
          (old(api).current_tempWstatus.degrees > old(api).upper_desired_temp.degrees)) ==>
          (api.heat_control == Isolette_Data_Model::On_Off::Off),
        // case REQ_MHS_4
        //   If the Regulator Mode is NORMAL and the Current
        //   Temperature is greater than or equal to the Lower Desired Temperature
        //   and less than or equal to the Upper Desired Temperature, the value of
        //   the Heat Control shall not be changed.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=110 
        ((old(api).regulator_mode == Isolette_Data_Model::Regulator_Mode::Normal_Regulator_Mode) &&
          ((old(api).current_tempWstatus.degrees >= old(api).lower_desired_temp.degrees) &&
            (old(api).current_tempWstatus.degrees <= old(api).upper_desired_temp.degrees))) ==>
          (api.heat_control == old(self).lastCmd),
        // case REQ_MHS_5
        //   If the Regulator Mode is FAILED, the Heat Control shall be
        //   set to Off.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=111 
        (old(api).regulator_mode == Isolette_Data_Model::Regulator_Mode::Failed_Regulator_Mode) ==>
          (api.heat_control == Isolette_Data_Model::On_Off::Off),
        // END MARKER TIME TRIGGERED ENSURES 
    {
      // -------------- Get values of input ports ------------------
      let lower: Temp_i = api.get_lower_desired_temp(); 
      let upper: Temp_i = api.get_upper_desired_temp(); 
      let regulator_mode: Regulator_Mode = api.get_regulator_mode();
      let currentTemp: TempWstatus_i = api.get_current_tempWstatus();

      //================ compute / control logic ===========================

      // current command defaults to value of last command (REQ-MHS-4)
      let mut currentCmd: On_Off = self.lastCmd;

      match regulator_mode {

          // ----- INIT Mode --------
          Regulator_Mode::Init_Regulator_Mode => {
              // REQ-MHS-1
              currentCmd = On_Off::Off;
          },

          // ------ NORMAL Mode -------
          Regulator_Mode::Normal_Regulator_Mode => {
              if (currentTemp.degrees > upper.degrees) {
                  // REQ-MHS-3
                  currentCmd = On_Off::Off;
              } else if (currentTemp.degrees < lower.degrees) {
                  assert(api.current_tempWstatus.degrees < api.lower_desired_temp.degrees);
                  // REQ-MHS-2
                  //currentCmd = On_Off::Off; // seeded bug/error
                  currentCmd = On_Off::Onn;
              }
              // otherwise currentCmd defaults to lastCmd (REQ-MHS-4)
          },

          // ------ FAILED Mode -------
          Regulator_Mode::Failed_Regulator_Mode => {
              // REQ-MHS-5
              currentCmd = On_Off::Off;
          }
      }

      // -------------- Set values of output ports ------------------
      api.put_heat_control(currentCmd);
      self.lastCmd = currentCmd       
    }

    pub fn notify(
      &mut self,
      channel: microkit_channel) 
    {
      // this method is called when the monitor does not handle the passed in channel
      match channel {
        _ => {
          log_warn_channel(channel);
        }
      }
    }
  }

  #[verifier::external_body]
  pub fn log_info(message: &str) {
    log::info!("{}", message);
  }

  #[verifier::external_body]
  pub fn log_warn_channel(channel: u32) {
    log::warn!("Unexpected channel {}", channel);
  }

}
