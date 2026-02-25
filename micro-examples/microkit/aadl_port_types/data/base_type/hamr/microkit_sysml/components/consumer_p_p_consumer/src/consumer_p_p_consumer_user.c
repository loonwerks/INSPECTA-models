#include "consumer_p_p_consumer.h"

// This file will not be overwritten if codegen is rerun

int8_t value;
void consumer_p_p_consumer_initialize(void) {
  printf("%s: consumer_p_p_consumer_initialize invoked\n", microkit_name);
}

void consumer_p_p_consumer_timeTriggered(void) {
  //printf("%s: consumer_p_p_consumer_timeTriggered invoked\n", microkit_name);
  if (get_read_port(&value)) {
    printf("%s: received %d\n", microkit_name, value);
  }
}

void consumer_p_p_consumer_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
