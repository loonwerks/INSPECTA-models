#include "producer_p_p_producer.h"

int counter = 0;

void producer_p_p_producer_initialize(void) {
  printf("%s: I'm periodic\n", microkit_name);

  base_data_1_prod_2_cons_struct_i value;
  value.size = 0;

  put_write_port(&value);
}

void producer_p_p_producer_timeTriggered(void) {
  printf("---------\n");
  if (counter % 2 == 0) {
    base_data_1_prod_2_cons_struct_i value;
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
