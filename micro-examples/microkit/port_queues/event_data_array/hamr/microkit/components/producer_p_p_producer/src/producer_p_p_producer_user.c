#include "producer_p_p_producer.h"

// TODO all components should have access to this common def
struct event_data_port_port_queues_struct_i nullElem = {42, 42};

int counter = 0;

void producer_p_p_producer_initialize(void) {
  printf("%s: I'm periodic\n", microkit_name);  
}

void producer_p_p_producer_timeTriggered(void) {
  printf("---------------------------------------\n");

  for (int i = 0; i < counter; i++) {
    event_data_port_port_queues_ArrayOfStruct payload;
    for(int j = 0; j < event_data_port_port_queues_ArrayOfStruct_DIM_0; j++) {
      if (j < i) {
        payload[j].currentEvent = j;
        payload[j].totalEventsSent = i;
      } else {
        payload[j] = nullElem;
      }
    }
    put_write_port(&payload);
  }
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