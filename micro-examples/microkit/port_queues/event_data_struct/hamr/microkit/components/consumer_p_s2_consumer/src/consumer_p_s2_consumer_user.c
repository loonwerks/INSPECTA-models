#include "consumer_p_s2_consumer.h"

void consumer_p_s2_consumer_initialize(void) {
  printf("%s: I'm sporadic\n", microkit_name);
}

void handle_read_port(void) {
  int received = 0;
  base_event_data_port_port_queues_struct_i value;

  while(get_read_port(&value)) {
    printf("%s: received [", microkit_name);
    for(int i = 0; i < value.size; i++) {
      printf("%d", value.elements[i]);
      if (i != value.size - 1) {
        printf(", ");
      }
    }
    printf("]\n");

    received++;
  }

  printf("%s: %i events received\n", microkit_name, received);
}
