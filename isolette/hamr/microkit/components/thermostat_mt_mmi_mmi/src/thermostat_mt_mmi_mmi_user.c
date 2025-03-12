#include "thermostat_mt_mmi_mmi.h"

// This file will not be overwritten if codegen is rerun

void thermostat_mt_mmi_mmi_initialize(void) {
  printf("%s: thermostat_mt_mmi_mmi_initialize invoked\n", microkit_name);
}

void thermostat_mt_mmi_mmi_timeTriggered(void) {
  printf("%s: thermostat_mt_mmi_mmi_timeTriggered invoked\n", microkit_name);
}

void thermostat_mt_mmi_mmi_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
