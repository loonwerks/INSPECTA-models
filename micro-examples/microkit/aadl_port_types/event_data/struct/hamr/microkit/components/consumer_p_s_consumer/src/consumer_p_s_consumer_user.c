#include "consumer_p_s_consumer.h"

void consumer_p_s_consumer_initialize(void) {
  printf("%s: I'm sporadic\n", microkit_name);
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

void handle_read_port1(void) {
  base_event_data_2_prod_2_cons_struct_i value;
  while(get_read_port1(&value)) {
    printf("%s: received ", microkit_name);
    printStruct(value);
    printf(" on read port 1\n");
  }
}

void handle_read_port2(void) {
  base_event_data_2_prod_2_cons_struct_i value;
  while(get_read_port2(&value)) {
    printf("%s: received ", microkit_name);
    printStruct(value);
    printf(" on read port 2\n");
  }
}
