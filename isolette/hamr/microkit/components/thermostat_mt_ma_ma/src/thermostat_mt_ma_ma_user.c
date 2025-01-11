#include "thermostat_mt_ma_ma.h"

// This file will not be overwritten if codegen is rerun

isolette_Isolette_Data_Model_On_Off_Type lastCmd = Onn;

void thermostat_mt_ma_ma_initialize(void) {
  printf("%s: thermostat_mt_ma_ma_initialize invoked\n", microkit_name);

  lastCmd = Off;
  put_alarm_control((const isolette_Isolette_Data_Model_On_Off_Type*) & lastCmd);
}

void thermostat_mt_ma_ma_timeTriggered(void) {
  //printf("%s: thermostat_mt_ma_ma_timeTriggered invoked\n", microkit_name);

  isolette_Isolette_Data_Model_Temp_i lowerAlarm;
  get_lower_alarm_temp(&lowerAlarm);

  isolette_Isolette_Data_Model_Temp_i upperAlarm;
  get_upper_alarm_temp(&upperAlarm);

  isolette_Isolette_Data_Model_Monitor_Mode_Type monitor_mode;
  get_monitor_mode(&monitor_mode);

  isolette_Isolette_Data_Model_TempWstatus_i currentTemp;
  get_current_tempWstatus(&currentTemp);

  isolette_Isolette_Data_Model_On_Off_Type currentCmd = lastCmd;

  switch (monitor_mode) {
    case Init_Monitor_Mode:
      // REQ_MA_1
      currentCmd = Off;
      break;
    case Normal_Monitor_Mode:
      if (currentTemp.degrees < lowerAlarm.degrees | currentTemp.degrees > upperAlarm.degrees) {
        // REQ_MA_2
        currentCmd = Onn;
      } else if ((currentTemp.degrees < lowerAlarm.degrees + 0.5) | (currentTemp.degrees > upperAlarm.degrees - 0.5)) {
        // REQ_MA_3
        currentCmd = lastCmd;
      } else {
        // REQ_MA_4
        currentCmd = Off;
      }
      break;
    case Failed_Monitor_Mode:
      // REQ_MA_5
      currentCmd = Onn;
      break;
  }

  lastCmd = currentCmd;
  put_alarm_control(&currentCmd);
}

void thermostat_mt_ma_ma_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
