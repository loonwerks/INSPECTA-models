#include "consumer_p_p_consumer.h"

void consumer_p_p_consumer_initialize(void) {
  printf("%s: I'm periodic\n", microkit_name);
}

void consumer_p_p_consumer_timeTriggered(void) {
  int8_t value;
  bool isFresh = get_read_port(&value);
  printf("%s: retrieved %d which is %s\n", microkit_name, value, isFresh ? "fresh" : "stale");
}

void consumer_p_p_consumer_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
