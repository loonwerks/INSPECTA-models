#include "producer_p_p1_producer.h"

// TODO all components should have access to this common def
struct event_data_2_prod_2_cons_struct_i nullElem = {42, 42};

void producer_p_p1_producer_initialize(void) {
  // event data ports so nothing to init
  printf("%s: I'm periodic\n", microkit_name);
}

int counter = 0;
void producer_p_p1_producer_timeTriggered(void) {
  printf("-------\n");
  printf("%s: ", microkit_name);
  if (counter % 2 == 0) {
    event_data_2_prod_2_cons_ArrayOfStruct value;
    for (int i = 0; i < event_data_2_prod_2_cons_ArrayOfStruct_DIM_0; i++) {
      if (i < counter) {
        struct event_data_2_prod_2_cons_struct_i val = {i, counter};
        value[i] = val;
      } else {
        value[i] = nullElem;
      }
    }
    put_write_port(&value);
    printf("sent event with %d elements\n", counter);
  } else {
    printf("no send\n");
  }
  counter = (counter + 1) % 10;
}

void producer_p_p1_producer_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}