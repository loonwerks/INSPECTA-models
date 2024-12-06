#include "consumer_p_s_consumer.h"

void consumer_p_s_consumer_initialize(void) {
  printf("%s: I'm sporadic\n", microkit_name);
}

void handle_read_port1(void) {
  while(get_read_port1()) {
    printf("%s: received a read port 1 event\n", microkit_name);
  }
}

void handle_read_port2(void) {
  while(get_read_port2()) {
    printf("%s: received a read port 2 event\n", microkit_name);
  }
}

void consumer_p_s_consumer_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
