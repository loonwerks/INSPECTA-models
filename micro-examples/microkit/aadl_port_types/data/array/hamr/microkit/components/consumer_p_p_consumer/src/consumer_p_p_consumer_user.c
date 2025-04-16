#include "consumer_p_p_consumer.h"

struct data_1_prod_2_cons_struct_i nullElem = {42, 42};

void consumer_p_p_consumer_initialize(void) {
  printf("%s: I'm periodic\n", microkit_name);
}

bool isNullElem(data_1_prod_2_cons_struct_i value) {
  return value.currentEvent == nullElem.currentEvent && value.totalEventsSent == nullElem.totalEventsSent;
}

void consumer_p_p_consumer_timeTriggered(void) {
  data_1_prod_2_cons_ArrayOfStruct value;
  bool isFresh = get_read_port(&value);
  printf("%s: retrieved [", microkit_name);
  for (int i = 0; i < data_1_prod_2_cons_ArrayOfStruct_DIM_0; i++) {
    if(!isNullElem(value[i])) {
      printf("(%d, %d)", value[i].currentEvent, value[i].totalEventsSent);

      if (i + 1 != data_1_prod_2_cons_ArrayOfStruct_DIM_0 && (!isNullElem(value[i+1]))) {
        printf(", ");
      }
    }
  }
  printf("] which is %s\n", isFresh ? "fresh" : "stale");
}

void consumer_p_p_consumer_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
