#include "consumer_p_p_consumer.h"

// TODO all components should have access to this common def
struct base_event_data_2_prod_2_cons_struct_i nullElem = {42, 42};

void consumer_p_p_consumer_initialize(void) {
  printf("%s: I'm periodic\n", microkit_name);
}

bool isNullElem(base_event_data_2_prod_2_cons_struct_i value) {
  return value.currentEvent == nullElem.currentEvent && value.totalEventsSent == nullElem.totalEventsSent;
}

void printArray(base_event_data_2_prod_2_cons_ArrayOfStruct value) {
  printf("[");
  for (int i = 0; i < base_event_data_2_prod_2_cons_ArrayOfStruct_DIM; i++)  {
    if (!isNullElem(value[i])) {
      printf("(%d, %d)", value[i].currentEvent, value[i].totalEventsSent);

      if (i + 1 != base_event_data_2_prod_2_cons_ArrayOfStruct_DIM && (!isNullElem(value[i+1]))) {
        printf(", ");
      }
    }
  }
  printf("]");
}

void consumer_p_p_consumer_timeTriggered(void) {
  if (!read_port1_is_empty()) {
    base_event_data_2_prod_2_cons_ArrayOfStruct value;
    while(get_read_port1(&value)) {
      printf("%s: received ", microkit_name);
      printArray(value);
      printf(" on read port 1\n", microkit_name);
    }
  } else {
    printf("%s: nothing received on read port 1\n", microkit_name);
  }

  if (!read_port2_is_empty()) {
    base_event_data_2_prod_2_cons_ArrayOfStruct value;
    while(get_read_port2(&value)) {
      printf("%s: received ", microkit_name);
      printArray(value);
      printf(" on read port 2\n", microkit_name);
    }
  } else {
    printf("%s: nothing received on read port 2\n", microkit_name);
  }
}
