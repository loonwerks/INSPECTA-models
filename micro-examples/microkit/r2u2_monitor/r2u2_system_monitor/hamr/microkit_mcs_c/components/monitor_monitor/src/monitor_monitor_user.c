#include "monitor_monitor.h"

// This file will not be overwritten if HAMR codegen is rerun

void monitor_monitor_initialize(void) {
  printf("%s: monitor_monitor_initialize invoked\n", microkit_name);
}

void monitor_monitor_timeTriggered(void) {
  int32_t sent_sample;
  get_sent_sample(&sent_sample);
  int32_t observed_sample;
  get_observed_sample(&observed_sample);
  printf("%s: monitor_monitor_timeTriggered invoked\n", microkit_name);
}

void monitor_monitor_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}

// Optional implementation placeholder for the R2U2 verdict of monitored property `samples_match_until_producer_pauses`.
void handle_samples_match_until_producer_pauses_verdict(r2u2_verdict_status_t verdict) {
  // This verdict is already mapped to monitor-owned alert port
  // alert_flag. Do not write this port from this callback.
}
