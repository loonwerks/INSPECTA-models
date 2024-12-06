#include "consumer_p_s_consumer.h"

void consumer_p_s_consumer_initialize(void) {
  // implement me
  printf("%s: I'm sporadic\n", microkit_name);
}

void handle_read_port1(void) {
  int8_t value;
  while (get_read_port1(&value)) {
    printf("%s: Received %d on read port 1\n", microkit_name, value);
  }
}

void handle_read_port2(void) {
  int8_t value;
  while (get_read_port2(&value)) {
    printf("%s: Received %d on read port 2\n", microkit_name, value);
  }
}

void consumer_p_s_consumer_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
