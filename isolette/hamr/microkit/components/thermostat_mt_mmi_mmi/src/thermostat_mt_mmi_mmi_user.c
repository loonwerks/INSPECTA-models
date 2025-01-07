#include "thermostat_mt_mmi_mmi.h"

// This file will not be overwritten if codegen is rerun

const float DEFAULT_LOWER_ALARM_TEMPERATURE = 97.0;
const float DEFAULT_UPPER_ALARM_TEMPERATURE = 101.0;
const isolette_Isolette_Data_Model_Status_Type DEFAULT_MONITOR_STATUS = Init_Status;
const bool DEFAULT_MONITOR_INTERFACE_FAILURE_FLAG = false;

isolette_Isolette_Data_Model_On_Off_Type lastCmd = Onn;

void thermostat_mt_mmi_mmi_initialize(void) {
  //printf("%s: thermostat_mt_mmi_mmi_initialize invoked\n", microkit_name);
 
  // set initial lower desired temp
  const isolette_Isolette_Data_Model_Temp_i lowerAlarmTemp = { DEFAULT_LOWER_ALARM_TEMPERATURE };
  put_lower_alarm_temp(& lowerAlarmTemp);
  
  // set initial upper desired temp
  const isolette_Isolette_Data_Model_Temp_i upperAlarmTemp = { DEFAULT_UPPER_ALARM_TEMPERATURE };
  put_upper_alarm_temp(&upperAlarmTemp);

  // set initial regulator status
  put_monitor_status(&DEFAULT_MONITOR_STATUS);

  // set initial regulator failure
  const isolette_Isolette_Data_Model_Failure_Flag_i interfaceFailure = { DEFAULT_MONITOR_INTERFACE_FAILURE_FLAG };
  put_interface_failure(& interfaceFailure);
}

void thermostat_mt_mmi_mmi_timeTriggered(void) {
  //printf("%s: thermostat_mt_mmi_mmi_timeTriggered invoked\n", microkit_name);

  isolette_Isolette_Data_Model_TempWstatus_i lower;
  get_lower_alarm_tempWstatus(&lower);

  isolette_Isolette_Data_Model_TempWstatus_i upper;
  get_upper_alarm_tempWstatus(&upper);

  isolette_Isolette_Data_Model_Monitor_Mode_Type monitor_mode;
  get_monitor_mode(&monitor_mode);

  // =============================================
  //  Set values for Monitor Status (Table A-6)
  // =============================================

  isolette_Isolette_Data_Model_Status_Type monitor_status = Init_Status;

  switch (monitor_mode) {
    case Init_Monitor_Mode:
      // REQ_MMI-1
      monitor_status = Init_Status;
      break;
    case Normal_Monitor_Mode:
      // REQ_MMI-2
      monitor_status = On_Status;
      break;
    case Failed_Monitor_Mode:
      // REQ_MMI-3
      monitor_status = Failed_Status;
      break;
  }
  put_monitor_status(&monitor_status);

  // =============================================
  //  Set values for Monitor Interface Failure internal variable
  // =============================================

  // The interface_failure status defaults to TRUE, which is the safe modality.
  bool interface_failure = true;

  // Extract the value status from both the upper and lower alarm range
  isolette_Isolette_Data_Model_ValueStatus_Type upper_alarm_status = upper.status;
  isolette_Isolette_Data_Model_ValueStatus_Type lower_alarm_status = lower.status;

  // Set the Monitor Interface Failure value based on the status values of the
  //   upper and lower temperature
  if (!(upper_alarm_status == Valid) ||
     !(lower_alarm_status == Valid)) {
    // REQ-MMI-4
    interface_failure = true;
  } else {
    // REQ-MMI-5
    interface_failure = false;
  }

  // create the appropriately typed value to send on the output port and set the port value
  const isolette_Isolette_Data_Model_Failure_Flag_i interface_failure_flag = { interface_failure };
  put_interface_failure(&interface_failure_flag);

  // =============================================
  //  Set values for Alarm Range internal variable
  // =============================================

  if (!interface_failure) {
    //  REQ-MMI-6
    const isolette_Isolette_Data_Model_Temp_i lowerAlarmTemp = { lower.degrees };
    const isolette_Isolette_Data_Model_Temp_i upperAlarmTemp = { upper.degrees };
    put_lower_alarm_temp(&lowerAlarmTemp);
    put_upper_alarm_temp(&upperAlarmTemp);
  } else {
    //  REQ-MMI-7
    const isolette_Isolette_Data_Model_Temp_i lowerAlarmTemp = { lower.degrees };
    const isolette_Isolette_Data_Model_Temp_i upperAlarmTemp = { upper.degrees };
    put_lower_alarm_temp(&lowerAlarmTemp);
    put_upper_alarm_temp(&upperAlarmTemp);
  }
}

void thermostat_mt_mmi_mmi_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
