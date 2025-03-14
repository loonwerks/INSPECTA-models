#include "producer_p_p2_producer.h"

void producer_p_p2_producer_initialize(void) {
  // event ports so nothing to init
  printf("%s: I'm periodic\n", microkit_name);
}

int counter = 0;
void producer_p_p2_producer_timeTriggered(void) {
  printf("%s: ", microkit_name);
  if (counter % 3 == 0) {
    put_write_port();
    printf("sent an event\n");
  } else {
    printf("no send\n");
  }
  counter = counter + 1;
}

void producer_p_p2_producer_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}