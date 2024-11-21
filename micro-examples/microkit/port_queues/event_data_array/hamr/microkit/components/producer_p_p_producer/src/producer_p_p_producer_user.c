#include "producer_p_p_producer.h"

// TODO all components should have access to this common def
struct base_event_data_port_port_queues_struct_i nullElem = {42, 42};

int counter = 0;

void producer_p_p_producer_initialize(void) {
  printf("%s: I'm periodic\n", microkit_name);  
}

void producer_p_p_producer_timeTriggered(void) {
  printf("---------------------------------------\n");

  for (int i = 0; i < counter; i++) {
    base_event_data_port_port_queues_ArrayOfStruct payload;
    for(int j = 0; j < base_event_data_port_port_queues_ArrayOfStruct_DIM; j++) {
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
