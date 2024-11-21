#include "consumer_p_p_consumer.h"

struct base_data_1_prod_2_cons_struct_i nullElem = {42, 42};

void consumer_p_p_consumer_initialize(void) {
  printf("%s: I'm periodic\n", microkit_name);
}

bool isNullElem(base_data_1_prod_2_cons_struct_i value) {
  return value.currentEvent == nullElem.currentEvent && value.totalEventsSent == nullElem.totalEventsSent;
}

void consumer_p_p_consumer_timeTriggered(void) {
  base_data_1_prod_2_cons_ArrayOfStruct value;
  bool isFresh = get_read_port(&value);
  printf("%s: retrieved [", microkit_name);
  for (int i = 0; i < base_data_1_prod_2_cons_ArrayOfStruct_DIM; i++) {
    if(!isNullElem(value[i])) {
      printf("(%d, %d)", value[i].currentEvent, value[i].totalEventsSent);

      if (i + 1 != base_data_1_prod_2_cons_ArrayOfStruct_DIM && (!isNullElem(value[i+1]))) {
        printf(", ");
      }
    }
  }
  printf("] which is %s\n", isFresh ? "fresh" : "stale");
}
