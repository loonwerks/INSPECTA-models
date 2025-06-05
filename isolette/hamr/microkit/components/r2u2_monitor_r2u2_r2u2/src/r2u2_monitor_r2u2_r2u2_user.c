#include "r2u2_monitor_r2u2_r2u2.h"

// This file will not be overwritten if codegen is rerun

void r2u2_monitor_r2u2_r2u2_initialize(void) {
  printf("%s: r2u2_monitor_r2u2_r2u2_initialize invoked\n", microkit_name);
}

void r2u2_monitor_r2u2_r2u2_timeTriggered(void) {
  printf("%s: r2u2_monitor_r2u2_r2u2_timeTriggered invoked\n", microkit_name);
}

void r2u2_monitor_r2u2_r2u2_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
