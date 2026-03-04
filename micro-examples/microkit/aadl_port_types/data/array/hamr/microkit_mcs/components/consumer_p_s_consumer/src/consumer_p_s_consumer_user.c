#include "consumer_p_s_consumer.h"

void consumer_p_s_consumer_initialize(void) {
  printf("%s: I'm sporadic so you'll never hear from me again :(\n", microkit_name);
}

void handle_read_port(void) {
  printf("%s: infeasible\n", microkit_name);
}

void consumer_p_s_consumer_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
