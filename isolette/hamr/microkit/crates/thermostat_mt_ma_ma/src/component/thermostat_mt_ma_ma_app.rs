// This file will not be overwritten if codegen is rerun

use data::*;
use data::Isolette_Data_Model::*;
use crate::bridge::thermostat_mt_ma_ma_api::*;
use vstd::prelude::*;

verus! {

  pub struct thermostat_mt_ma_ma {
    // BEGIN MARKER STATE VARS
    pub lastCmd: Isolette_Data_Model::On_Off
    // END MARKER STATE VARS
  }

  impl thermostat_mt_ma_ma {
    pub fn new() -> Self 
    {
      Self {
        // BEGIN MARKER STATE VAR INIT
        lastCmd: Isolette_Data_Model::On_Off::default()
        // END MARKER STATE VAR INIT
      }
    }

    pub fn initialize<API: thermostat_mt_ma_ma_Put_Api>(
      &mut self,
      api: &mut thermostat_mt_ma_ma_Application_Api<API>)
      ensures
        // BEGIN MARKER INITIALIZATION ENSURES
        // guarantee REQ_MA_1
        //   If the Monitor Mode is INIT, the Alarm Control shall be set
        //   to Off.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=115 
        (api.alarm_control == Isolette_Data_Model::On_Off::Off) &&
          (self.lastCmd == Isolette_Data_Model::On_Off::Off)
        // END MARKER INITIALIZATION ENSURES 
    {
      log_info("initialize entrypoint invoked");

      self.lastCmd = On_Off::Off;
      api.put_alarm_control(self.lastCmd);
    }

    pub fn timeTriggered<API: thermostat_mt_ma_ma_Full_Api>(
      &mut self,
      api: &mut thermostat_mt_ma_ma_Application_Api<API>)
      requires
        // BEGIN MARKER TIME TRIGGERED REQUIRES
        // assume Figure_A_7
        //   This is not explicitly stated in the requirements, but a reasonable
        //   assumption is that the lower alarm must be at least 1.0f less than
        //   the upper alarm in order to account for the 0.5f tolerance
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=115 
        old(api).upper_alarm_temp.degrees - old(api).lower_alarm_temp.degrees >= 1i32,
        // assume Table_A_12_LowerAlarmTemp
        //   Range [96..101]
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=112 
        (96i32 <= old(api).lower_alarm_temp.degrees) &&
          (old(api).lower_alarm_temp.degrees <= 101i32),
        // assume Table_A_12_UpperAlarmTemp
        //   Range [97..102]
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=112 
        (97i32 <= old(api).upper_alarm_temp.degrees) &&
          (old(api).upper_alarm_temp.degrees <= 102i32)
        // END MARKER TIME TRIGGERED REQUIRES
      ensures
        // BEGIN MARKER TIME TRIGGERED ENSURES
        // case REQ_MA_1
        //   If the Monitor Mode is INIT, the Alarm Control shall be set
        //   to Off.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=115 
        (old(api).monitor_mode == Isolette_Data_Model::Monitor_Mode::Init_Monitor_Mode) ==>
          ((api.alarm_control == Isolette_Data_Model::On_Off::Off) &&
             (self.lastCmd == Isolette_Data_Model::On_Off::Off)),
        // case REQ_MA_2
        //   If the Monitor Mode is NORMAL and the Current Temperature is
        //   less than the Lower Alarm Temperature or greater than the Upper Alarm
        //   Temperature, the Alarm Control shall be set to On.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=115 
        ((old(api).monitor_mode == Isolette_Data_Model::Monitor_Mode::Normal_Monitor_Mode) &&
          ((old(api).current_tempWstatus.degrees < old(api).lower_alarm_temp.degrees) ||
            (old(api).current_tempWstatus.degrees > old(api).upper_alarm_temp.degrees))) ==>
          ((api.alarm_control == Isolette_Data_Model::On_Off::Onn) &&
             (self.lastCmd == Isolette_Data_Model::On_Off::Onn)),
        // case REQ_MA_3
        //   If the Monitor Mode is NORMAL and the Current Temperature
        //   is greater than or equal to the Lower Alarm Temperature and less than
        //   the Lower Alarm Temperature +0.5 degrees, or the Current Temperature is
        //   greater than the Upper Alarm Temperature -0.5 degrees and less than or equal
        //   to the Upper Alarm Temperature, the value of the Alarm Control shall
        //   not be changed.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=115 
        ((old(api).monitor_mode == Isolette_Data_Model::Monitor_Mode::Normal_Monitor_Mode) &&
          ((old(api).current_tempWstatus.degrees >= old(api).lower_alarm_temp.degrees) &&
            (old(api).current_tempWstatus.degrees < old(api).lower_alarm_temp.degrees + 1i32) ||
            (old(api).current_tempWstatus.degrees > old(api).upper_alarm_temp.degrees - 1i32) &&
              (old(api).current_tempWstatus.degrees <= old(api).upper_alarm_temp.degrees))) ==>
          ((api.alarm_control == old(self).lastCmd) &&
             (self.lastCmd == old(self).lastCmd)),
        // case REQ_MA_4
        //   If the Monitor Mode is NORMAL and the value of the Current
        //   Temperature is greater than or equal to the Lower Alarm Temperature
        //   +0.5 degrees and less than or equal to the Upper Alarm Temperature
        //   -0.5 degrees, the Alarm Control shall be set to Off.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=115 
        ((old(api).monitor_mode == Isolette_Data_Model::Monitor_Mode::Normal_Monitor_Mode) &&
          ((old(api).current_tempWstatus.degrees >= old(api).lower_alarm_temp.degrees + 1i32) &&
            (old(api).current_tempWstatus.degrees <= old(api).upper_alarm_temp.degrees - 1i32))) ==>
          ((api.alarm_control == Isolette_Data_Model::On_Off::Off) &&
             (self.lastCmd == Isolette_Data_Model::On_Off::Off)),
        // case REQ_MA_5
        //   If the Monitor Mode is FAILED, the Alarm Control shall be
        //   set to On.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=116 
        (old(api).monitor_mode == Isolette_Data_Model::Monitor_Mode::Failed_Monitor_Mode) ==>
          ((api.alarm_control == Isolette_Data_Model::On_Off::Onn) &&
             (self.lastCmd == Isolette_Data_Model::On_Off::Onn))
        // END MARKER TIME TRIGGERED ENSURES 
    {
      log_info("compute entrypoint invoked");

      let lowerAlarm = api.get_lower_alarm_temp();

      let upperAlarm = api.get_upper_alarm_temp();

      let monitor_mode = api.get_monitor_mode();

      let currentTemp = api.get_current_tempWstatus();

      #[allow(unused_assignments)]
      let mut currentCmd = self.lastCmd;

      match monitor_mode {
        Monitor_Mode::Init_Monitor_Mode => {
          // REQ_MA_1
          currentCmd = On_Off::Off;
        },
        Monitor_Mode::Normal_Monitor_Mode => {
          if (currentTemp.degrees < lowerAlarm.degrees || currentTemp.degrees > upperAlarm.degrees) {
            // REQ_MA_2
            currentCmd = On_Off::Onn;
          } else if ((currentTemp.degrees < lowerAlarm.degrees + 1) || 
            (currentTemp.degrees > upperAlarm.degrees - 1)) {
            // REQ_MA_3
            currentCmd = self.lastCmd;
          } else {
            // REQ_MA_4
            //currentCmd = On_Off::Onn; // seeded bug
            currentCmd = On_Off::Off;
          }
        },
        Monitor_Mode::Failed_Monitor_Mode => {
          // REQ_MA_5
          currentCmd = On_Off::Onn;
        }
      }
      self.lastCmd = currentCmd;
      api.put_alarm_control(currentCmd);
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

    // BEGIN MARKER GUMBO METHODS
    pub open spec fn timeout_condition_satisfied() -> bool
    {
      true
    }
    // END MARKER GUMBO METHODS
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
