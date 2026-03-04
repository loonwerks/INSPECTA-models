#include "consumer_p_s_consumer.h"

// This file will not be overwritten if codegen is rerun

void consumer_p_s_consumer_initialize(void) {
  printf("%s: consumer_p_s_consumer_initialize invoked\n", microkit_name);
}

void consumer_p_s_consumer_timeTriggered(void) {
  printf("%s: consumer_p_s_consumer_timeTriggered invoked\n", microkit_name);
}

void consumer_p_s_consumer_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
