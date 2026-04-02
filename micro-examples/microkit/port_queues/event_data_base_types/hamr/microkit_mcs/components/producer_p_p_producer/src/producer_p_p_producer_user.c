#include "producer_p_p_producer.h"

int8_t counter = 0;

void producer_p_p_producer_initialize(void) {
  printf("%s: I'm periodic\n", microkit_name);
}

void producer_p_p_producer_timeTriggered(void) {
  for(int8_t i = 1; i <= counter; i++) {
    put_write_port(&i);
  }
  printf("---------------------------------------\n");
  printf("%s: Sent %i events.\n", microkit_name, counter);

  counter = (counter + 1) % 7; // send b/w 0 to 6 events per dispatch  
}

void producer_p_p_producer_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}