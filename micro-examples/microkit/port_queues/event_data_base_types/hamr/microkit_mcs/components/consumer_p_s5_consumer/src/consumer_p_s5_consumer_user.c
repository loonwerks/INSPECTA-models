#include "consumer_p_s5_consumer.h"

void consumer_p_s5_consumer_initialize(void) {
  printf("%s: I'm sporadic\n", microkit_name);
}

void handle_read_port(void) {
    int32_t received = 0;
    int8_t value;

    // keep dequeuing until no more things can be had
    while(get_read_port(&value)) {
      printf("%s: received value {%d}\n", microkit_name, value);
      received++;
    }

    printf("%s: %i events received\n", microkit_name, received);
}

void consumer_p_s5_consumer_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}