#include "producer_p_p2_producer.h"

// TODO all components should have access to this common def
struct base_event_data_2_prod_2_cons_struct_i nullElem = {42, 42};

void producer_p_p2_producer_initialize(void) {
  // event data ports so nothing to init
  printf("%s: I'm periodic\n", microkit_name);
}

int counter = 0;
void producer_p_p2_producer_timeTriggered(void) {
  printf("%s: ", microkit_name);
  if (counter % 3 == 0) {
    base_event_data_2_prod_2_cons_ArrayOfStruct value;
    for (int i = 0; i < base_event_data_2_prod_2_cons_ArrayOfStruct_DIM; i++) {
      if (i < counter) {
        struct base_event_data_2_prod_2_cons_struct_i val = {i, counter};
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
