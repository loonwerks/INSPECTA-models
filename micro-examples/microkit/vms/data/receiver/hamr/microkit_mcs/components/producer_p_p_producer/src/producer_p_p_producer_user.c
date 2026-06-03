#include "producer_p_p_producer.h"

// This file will not be overwritten if HAMR codegen is rerun

void producer_p_p_producer_initialize(void) {
  printf("%s: producer_p_p_producer_initialize invoked\n", microkit_name);
}

void producer_p_p_producer_timeTriggered(void) {
  printf("%s: producer_p_p_producer_timeTriggered invoked\n", microkit_name);
}

void producer_p_p_producer_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
