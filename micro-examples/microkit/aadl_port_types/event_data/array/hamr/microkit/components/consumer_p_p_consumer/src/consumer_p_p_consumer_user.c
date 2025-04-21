#include "consumer_p_p_consumer.h"

// TODO all components should have access to this common def
struct event_data_2_prod_2_cons_struct_i nullElem = {42, 42};

void consumer_p_p_consumer_initialize(void) {
  printf("%s: I'm periodic\n", microkit_name);
}

bool isNullElem(event_data_2_prod_2_cons_struct_i value) {
  return value.currentEvent == nullElem.currentEvent && value.totalEventsSent == nullElem.totalEventsSent;
}

void printArray(event_data_2_prod_2_cons_ArrayOfStruct value) {
  printf("[");
  for (int i = 0; i < event_data_2_prod_2_cons_ArrayOfStruct_DIM_0; i++)  {
    if (!isNullElem(value[i])) {
      printf("(%d, %d)", value[i].currentEvent, value[i].totalEventsSent);

      if (i + 1 != event_data_2_prod_2_cons_ArrayOfStruct_DIM_0 && (!isNullElem(value[i+1]))) {
        printf(", ");
      }
    }
  }
  printf("]");
}

void consumer_p_p_consumer_timeTriggered(void) {
  if (!read_port1_is_empty()) {
    event_data_2_prod_2_cons_ArrayOfStruct value;
    while(get_read_port1(&value)) {
      printf("%s: received ", microkit_name);
      printArray(value);
      printf(" on read port 1\n", microkit_name);
    }
  } else {
    printf("%s: nothing received on read port 1\n", microkit_name);
  }

  if (!read_port2_is_empty()) {
    event_data_2_prod_2_cons_ArrayOfStruct value;
    while(get_read_port2(&value)) {
      printf("%s: received ", microkit_name);
      printArray(value);
      printf(" on read port 2\n", microkit_name);
    }
  } else {
    printf("%s: nothing received on read port 2\n", microkit_name);
  }
}

void consumer_p_p_consumer_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}