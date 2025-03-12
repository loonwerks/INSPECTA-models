#include "thermostat_rt_mri_mri.h"

// This file will not be overwritten if codegen is rerun

void thermostat_rt_mri_mri_initialize(void) {
  printf("%s: thermostat_rt_mri_mri_initialize invoked\n", microkit_name);
}

void thermostat_rt_mri_mri_timeTriggered(void) {
  printf("%s: thermostat_rt_mri_mri_timeTriggered invoked\n", microkit_name);
}

void thermostat_rt_mri_mri_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
