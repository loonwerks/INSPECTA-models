#include "producer_p_p_producer.h"

int counter = 0;

void producer_p_p_producer_initialize(void) {
  printf("%s: I'm periodic\n", microkit_name);
}

void producer_p_p_producer_timeTriggered(void) {
  for(int i = 1; i <= counter; i++) {
    base_event_data_port_port_queues_struct_i payload;
    payload.size = i;
    for(int j = 0; j < i; j++) {
      payload.elements[j] = j;
    }
    put_write_port(&payload);
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