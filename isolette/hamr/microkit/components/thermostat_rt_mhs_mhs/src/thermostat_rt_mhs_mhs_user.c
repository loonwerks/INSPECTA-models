#include "thermostat_rt_mhs_mhs.h"

// This file will not be overwritten if codegen is rerun

isolette_Isolette_Data_Model_On_Off_Type lastCmd = Onn;

void thermostat_rt_mhs_mhs_initialize(void) {
  printf("%s: thermostat_rt_mhs_mhs_initialize invoked\n", microkit_name);

  lastCmd = Off;
  // REQ-MHS-1: If the Regulator Mode is INIT, the Heat Control shall be
  // set to Off
  isolette_Isolette_Data_Model_On_Off_Type currentCmd = Off;
  put_heat_control(&currentCmd);
}

void thermostat_rt_mhs_mhs_timeTriggered(void) {
  //printf("%s: thermostat_rt_mhs_mhs_timeTriggered invoked\n", microkit_name);

  // -------------- Get values of input ports ------------------
  isolette_Isolette_Data_Model_Temp_i lower;
  get_lower_desired_temp(&lower);

  isolette_Isolette_Data_Model_Temp_i upper;
  get_upper_desired_temp(&upper);

  isolette_Isolette_Data_Model_Regulator_Mode_Type regulator_mode;
  get_regulator_mode(&regulator_mode);

  isolette_Isolette_Data_Model_TempWstatus_i currentTemp;
  get_current_tempWstatus(&currentTemp);

  //================ compute / control logic ===========================
 
  // current command defaults to value of last command (REQ-MHS-4)
  isolette_Isolette_Data_Model_On_Off_Type currentCmd = lastCmd;

  switch (regulator_mode) {
    case Init_Regulator_Mode:
      // REQ-MHS-1
      currentCmd = Off;
      break;
    case Normal_Regulator_Mode:
      if (currentTemp.degrees > upper.degrees) {
        // REQ-MHS-3
        currentCmd = Off;
      } else if (currentTemp.degrees < lower.degrees) {
        // REQ-MHS-2
        //currentCmd = Off; // seeded bug/error
        currentCmd = Onn;
      }

      // otherwise currentCmd defaults to lastCmd (REQ-MHS-4)

      break;
    case Failed_Regulator_Mode:
      // REQ-MHS-5
      currentCmd = Off;
      break;
  }

  put_heat_control(&currentCmd);

  lastCmd = currentCmd;
}

void thermostat_rt_mhs_mhs_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
