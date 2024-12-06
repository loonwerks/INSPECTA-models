#include "producer_p_p_producer.h"

// TODO all components should have access to this common def
struct base_data_1_prod_2_cons_struct_i nullElem = {42, 42};

int counter = 0;
void producer_p_p_producer_initialize(void) {
  printf("%s: I'm periodic\n", microkit_name);

  base_data_1_prod_2_cons_ArrayOfStruct value;
  for (int i = 0; i < base_data_1_prod_2_cons_ArrayOfStruct_DIM; i++) {
    value[i] = nullElem;
  }
  put_write_port(&value);
}

void producer_p_p_producer_timeTriggered(void) {
  printf("---------\n");
  if (counter % 2 == 0) {
    base_data_1_prod_2_cons_ArrayOfStruct value;
    for (int i = 0; i < base_data_1_prod_2_cons_ArrayOfStruct_DIM; i++) {
      if (i < counter) {
        struct base_data_1_prod_2_cons_struct_i val = {i, counter};
        value[i] = val;
      } else {
        value[i] = nullElem;
      }
    }
    put_write_port(&value);
    printf("%s: put array with %d elements\n", microkit_name, counter);
  } else {
    printf("%s: didn't put anything\n", microkit_name);
  }
  counter = (counter + 1) % 10;
}

void producer_p_p_producer_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
