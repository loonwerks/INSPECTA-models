#include "thermostat_rt_mrm_mrm.h"

// This file will not be overwritten if codegen is rerun

isolette_Isolette_Data_Model_Regulator_Mode_Type lastRegulatorMode = Init_Regulator_Mode;

void thermostat_rt_mrm_mrm_initialize(void) {
  //printf("%s: thermostat_rt_mrm_mrm_initialize invoked\n", microkit_name);

  lastRegulatorMode = Init_Regulator_Mode;
  put_regulator_mode(&lastRegulatorMode);
}

void thermostat_rt_mrm_mrm_timeTriggered(void) {
  //printf("%s: thermostat_rt_mrm_mrm_timeTriggered invoked\n", microkit_name);

  // -------------- Get values of input ports ------------------

  isolette_Isolette_Data_Model_TempWstatus_i current_tempWstatus;
  get_current_tempWstatus(&current_tempWstatus);

  isolette_Isolette_Data_Model_ValueStatus_Type current_temperature_status = current_tempWstatus.status;

  isolette_Isolette_Data_Model_Failure_Flag_i interface_failure;
  get_interface_failure(&interface_failure);

  isolette_Isolette_Data_Model_Failure_Flag_i internal_failure;
  get_internal_failure(&internal_failure);

  // determine regulator status as specified in FAA REMH Table A-10
  //  regulator_status = NOT (Monitor Interface Failure OR Monitor Internal Failure)
  //                          AND Current Temperature.Status = Valid
  bool regulator_status =
    (!(interface_failure.flag || internal_failure.flag)
      && (current_temperature_status == Valid));

  switch (lastRegulatorMode) {
    case Init_Regulator_Mode:
      if (regulator_status) {
        // REQ-MRM-2
        lastRegulatorMode = Normal_Regulator_Mode;
      } else {
        // REQ-MRM-3
        lastRegulatorMode = Failed_Regulator_Mode;
      }

      // otherwise we stay in Init mode

      break;
    case Normal_Regulator_Mode:
      if (!regulator_status) {
        // REQ-MRM-4
        lastRegulatorMode = Failed_Regulator_Mode;
      }
      break;
    case Failed_Regulator_Mode:
      // do nothing
      break;
  }

  put_regulator_mode(&lastRegulatorMode);
}

void thermostat_rt_mrm_mrm_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
