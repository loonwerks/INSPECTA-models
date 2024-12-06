#include "consumer_p_p_consumer.h"

void consumer_p_p_consumer_initialize(void) {
  printf("%s: I'm periodic\n", microkit_name);
}

void printStruct(base_event_data_2_prod_2_cons_struct_i value) {
  printf("[");
  for (int i = 0; i < value.size; i++){
    printf("%d", value.elements[i]);
    if(i + 1 != value.size) {
      printf(", ");
    }
  }
  printf("]");
}

void consumer_p_p_consumer_timeTriggered(void) {
  if (!read_port1_is_empty()) {
    base_event_data_2_prod_2_cons_struct_i value;
    while(get_read_port1(&value)) {
      printf("%s: received ", microkit_name);
      printStruct(value);
      printf(" on read port 1\n");
    }
  } else {
    printf("%s: nothing received on read port 1\n", microkit_name);
  }

  if (!read_port2_is_empty()) {
    base_event_data_2_prod_2_cons_struct_i value;
    while(get_read_port2(&value)) {
      printf("%s: received ", microkit_name);
      printStruct(value);
      printf(" on read port 2\n");
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
