#include "operator_interface_oip_oit.h"

// This file will not be overwritten if codegen is rerun

const float DEFAULT_LOWER_DESIRED_TEMPERATURE = 97;
const float DEFAULT_UPPER_DESIRED_TEMPERATURE = 99;
const float DEFAULT_LOWER_ALARM_TEMPERATURE = 97;
const float DEFAULT_UPPER_ALARM_TEMPERATURE = 101;

const isolette_Isolette_Data_Model_TempWstatus_i initLowerDesiredTempWstatus = { DEFAULT_LOWER_DESIRED_TEMPERATURE, Valid };
const isolette_Isolette_Data_Model_TempWstatus_i initUpperDesiredTempWstatus = { DEFAULT_UPPER_DESIRED_TEMPERATURE, Valid };
const isolette_Isolette_Data_Model_TempWstatus_i initLowerAlarmTempWstatus = { DEFAULT_LOWER_ALARM_TEMPERATURE, Valid };
const isolette_Isolette_Data_Model_TempWstatus_i initUpperAlarmTempWstatus = { DEFAULT_UPPER_ALARM_TEMPERATURE, Valid };

const isolette_Isolette_Data_Model_TempWstatus_i lastLowerDesiredTempWstatus = initLowerDesiredTempWstatus;
const isolette_Isolette_Data_Model_TempWstatus_i lastUpperDesiredTempWstatus = initUpperDesiredTempWstatus;
const isolette_Isolette_Data_Model_TempWstatus_i lastLowerAlarmTempWstatus = initLowerAlarmTempWstatus;
const isolette_Isolette_Data_Model_TempWstatus_i lastUpperAlarmTempWstatus = initUpperAlarmTempWstatus;

void operator_interface_oip_oit_initialize(void) {
  printf("%s: operator_interface_oip_oit_initialize invoked\n", microkit_name);

  put_lower_desired_tempWstatus(&initLowerDesiredTempWstatus);
  put_upper_desired_tempWstatus(&initUpperDesiredTempWstatus);
  put_lower_alarm_tempWstatus(&initLowerAlarmTempWstatus);
  put_upper_alarm_tempWstatus(&initUpperAlarmTempWstatus);
}

void operator_interface_oip_oit_timeTriggered(void) {
  //printf("%s: operator_interface_oip_oit_timeTriggered invoked\n", microkit_name);

  isolette_Isolette_Data_Model_Status_Type rs;
  if (get_regulator_status(&rs)) {
    printf("%s: Regulator Status: %s\n", microkit_name, rs == Init_Status ? "Init" : rs == On_Status ? "On" : "Failed");
  }

  isolette_Isolette_Data_Model_Status_Type ms;
  if (get_monitor_status(&ms)) {
    printf("%s: Monitor Status: %s\n", microkit_name, ms == Init_Status ? "Init" : ms == On_Status ? "On" : "Failed");
  }

  isolette_Isolette_Data_Model_Temp_i temp;
  if (get_display_temperature(&temp)) {
    printf("%s: Display Temperature %f\n", microkit_name, temp.degrees);
  }

  isolette_Isolette_Data_Model_On_Off_Type alarm;
  if (get_alarm_control(&alarm)) {
    printf("%s: Alamr: %s\n", microkit_name, alarm == Onn ? "on" : "off");
  }

}

void operator_interface_oip_oit_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
