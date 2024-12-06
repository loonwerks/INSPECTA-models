#include "consumer_p_s2_consumer.h"

// TODO all components should have access to this common def
struct base_event_data_port_port_queues_struct_i nullElem = {42, 42};

void consumer_p_s2_consumer_initialize(void) {
  printf("%s: I'm sporadic\n", microkit_name);
}

bool isNullElem(base_event_data_port_port_queues_struct_i value) {
  return value.currentEvent == nullElem.currentEvent && value.totalEventsSent == nullElem.totalEventsSent;
}

void printArray(base_event_data_port_port_queues_ArrayOfStruct value) {
  printf("[");
  for (int i = 0; i < base_event_data_port_port_queues_ArrayOfStruct_DIM; i++)  {
    if (!isNullElem(value[i])) {
      printf("(%d, %d)", value[i].currentEvent, value[i].totalEventsSent);

      if (i + 1 != base_event_data_port_port_queues_ArrayOfStruct_DIM && (!isNullElem(value[i+1]))) {
        printf(", ");
      }
    }
  }
  printf("]");
}

void handle_read_port(void) {
  base_event_data_port_port_queues_ArrayOfStruct value;
  while(get_read_port(&value)) {
    printf("%s: received ", microkit_name);
    printArray(value);
    printf("\n");
  }
}

void consumer_p_s2_consumer_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}