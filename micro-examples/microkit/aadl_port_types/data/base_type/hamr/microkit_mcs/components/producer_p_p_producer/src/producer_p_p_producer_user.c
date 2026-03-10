#include "producer_p_p_producer.h"

int8_t value = 0;

void producer_p_p_producer_initialize(void) {
  put_write_port(&value); // must initialize outgoing data ports
  
  printf("%s: I'm periodic, initialized outgoing data port\n", microkit_name);
}

void producer_p_p_producer_timeTriggered(void) {
  printf("------\n");
  value = value + 1;
  if (value % 2 == 0) {
    put_write_port(&value);
    printf("%s: sent %d\n", microkit_name, value);
  } else {
    printf("%s: nothing sent\n", microkit_name);
  }
}

void producer_p_p_producer_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}