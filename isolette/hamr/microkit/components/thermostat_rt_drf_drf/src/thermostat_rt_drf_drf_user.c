#include "thermostat_rt_drf_drf.h"

// This file will not be overwritten if codegen is rerun

void thermostat_rt_drf_drf_initialize(void) {
  //printf("%s: thermostat_rt_drf_drf_initialize invoked\n", microkit_name);

  const isolette_Isolette_Data_Model_Failure_Flag_i internal_failure = { false };  
  put_internal_failure(&internal_failure);
}

void thermostat_rt_drf_drf_timeTriggered(void) {
  //printf("%s: thermostat_rt_drf_drf_timeTriggered invoked\n", microkit_name);
}

void thermostat_rt_drf_drf_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
