#include "consumer_consumer.h"

// This file will not be overwritten if HAMR codegen is rerun

void consumer_consumer_initialize(void) {
  printf("%s: consumer_consumer_initialize invoked\n", microkit_name);
}

void consumer_consumer_timeTriggered(void) {
  int32_t sample;
  if (get_sample(&sample)) {
    printf("%s: Received sample\n", microkit_name);
  } else {
    printf("%s: No sample received this dispatch\n", microkit_name);
  }
}

void consumer_consumer_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
