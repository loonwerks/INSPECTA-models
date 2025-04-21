#include "producer_p_p2_producer.h"

int counter = 0;

void producer_p_p2_producer_initialize(void) {
   printf("%s: I'm periodic\n", microkit_name);
  // event data ports so nothing to initialize
}

void producer_p_p2_producer_timeTriggered(void) {
  if (counter % 3 == 0) {
    event_data_2_prod_2_cons_struct_i value;
    value.size = counter;
    for (int i = 0; i < counter; i++){
      value.elements[i] = i;
    }
    put_write_port(&value);
    printf("%s: put %d elements into the struct's array\n", microkit_name, counter);
  } else {
    printf("%s: didn't put anything\n", microkit_name);
  }
  counter = (counter + 1) % 10;
}

void producer_p_p2_producer_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}