#include "monitor_monitor.h"

// This file will not be overwritten if HAMR codegen is rerun

void monitor_monitor_initialize(void) {
  printf("%s: monitor_monitor_initialize invoked\n", microkit_name);
}

void monitor_monitor_timeTriggered(void) {
  printf("%s: monitor_monitor_timeTriggered invoked\n", microkit_name);
}

void monitor_monitor_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
