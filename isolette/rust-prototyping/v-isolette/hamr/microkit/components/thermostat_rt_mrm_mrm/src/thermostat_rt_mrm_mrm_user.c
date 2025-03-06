#include "thermostat_rt_mrm_mrm.h"

// This file will not be overwritten if codegen is rerun

void thermostat_rt_mrm_mrm_initialize(void) {
  printf("%s: thermostat_rt_mrm_mrm_initialize invoked\n", microkit_name);

  isolette_Isolette_Data_Model_Regulator_Mode_Type mode = Normal_Regulator_Mode;

  put_regulator_mode(&mode);
}

void thermostat_rt_mrm_mrm_timeTriggered(void) {
  printf("%s: thermostat_rt_mrm_mrm_timeTriggered invoked\n", microkit_name);
}

void thermostat_rt_mrm_mrm_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
