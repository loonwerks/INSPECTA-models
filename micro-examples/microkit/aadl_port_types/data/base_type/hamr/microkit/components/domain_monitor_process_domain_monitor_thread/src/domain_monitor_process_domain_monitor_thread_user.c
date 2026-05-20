#include "domain_monitor_process_domain_monitor_thread.h"

// This file will not be overwritten if HAMR codegen is rerun

void domain_monitor_process_domain_monitor_thread_initialize(void) {
  printf("%s: domain_monitor_process_domain_monitor_thread_initialize invoked\n", microkit_name);
}

void domain_monitor_process_domain_monitor_thread_timeTriggered(void) {
  printf("%s: domain_monitor_process_domain_monitor_thread_timeTriggered invoked\n", microkit_name);
}

void domain_monitor_process_domain_monitor_thread_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
