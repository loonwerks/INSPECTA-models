#include "thermostat_mt_mmm_mmm.h"

// This file will not be overwritten if codegen is rerun

isolette_Isolette_Data_Model_Monitor_Mode_Type lastMonitorMode = Init_Monitor_Mode;

bool timeout_condition_satisified() {
  return false;
}

void thermostat_mt_mmm_mmm_initialize(void) {
  printf("%s: thermostat_mt_mmm_mmm_initialize invoked\n", microkit_name);

  lastMonitorMode = Init_Monitor_Mode;
  put_monitor_mode(&lastMonitorMode);
}

void thermostat_mt_mmm_mmm_timeTriggered(void) {
  //printf("%s: thermostat_mt_mmm_mmm_timeTriggered invoked\n", microkit_name);

  isolette_Isolette_Data_Model_TempWstatus_i current_tempWstatus;
  get_current_tempWstatus(&current_tempWstatus);

  isolette_Isolette_Data_Model_Failure_Flag_i interface_failure;
  get_interface_failure(&interface_failure);

  isolette_Isolette_Data_Model_Failure_Flag_i internal_failure;
  get_internal_failure(&internal_failure);


  // determine monitor status as specified in FAA REMH Table A-15
  //  monitor_status = NOT (Monitor Interface Failure OR Monitor Internal Failure)
  //                          AND Current Temperature.Status = Valid
  bool monitor_status =
    (!(interface_failure.flag || internal_failure.flag) && current_tempWstatus.status == Valid);
    
  switch (lastMonitorMode) {
    case Init_Monitor_Mode:
      // REQ-MMM-2
      if (monitor_status) {
        lastMonitorMode = Normal_Monitor_Mode;
      }
      else if (timeout_condition_satisified()) {
        // REQ-MMM-4
        lastMonitorMode = Failed_Monitor_Mode;
      } else {
        // otherwise we stay in Init Mode
      }
      break;
    case Normal_Monitor_Mode:
      if (!monitor_status) {
        // REQ-MMM3
        lastMonitorMode = Failed_Monitor_Mode;
      }
      break;
    case Failed_Monitor_Mode:
      // do nothing
      break;
  }
  put_monitor_mode(&lastMonitorMode);
}

void thermostat_mt_mmm_mmm_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
