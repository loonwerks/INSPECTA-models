#include "thermostat_mt_dmf_dmf.h"

// This file will not be overwritten if codegen is rerun

void thermostat_mt_dmf_dmf_initialize(void) {
  printf("%s: thermostat_mt_dmf_dmf_initialize invoked\n", microkit_name);
}

void thermostat_mt_dmf_dmf_timeTriggered(void) {
  printf("%s: thermostat_mt_dmf_dmf_timeTriggered invoked\n", microkit_name);
}

void thermostat_mt_dmf_dmf_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
