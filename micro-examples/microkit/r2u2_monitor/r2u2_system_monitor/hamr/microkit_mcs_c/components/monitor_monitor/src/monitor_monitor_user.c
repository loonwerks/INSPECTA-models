#include "monitor_monitor.h"

// This file will not be overwritten if HAMR codegen is rerun

void monitor_monitor_initialize(void) {
  printf("%s: monitor_monitor_initialize invoked\n", microkit_name);
}

void monitor_monitor_timeTriggered(void) {
  int32_t sent_sample;
  int32_t observed_sample;
  get_sent_sample(&sent_sample);
  get_observed_sample(&observed_sample);
}

void monitor_monitor_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
