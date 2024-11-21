#include "consumer_p_p_consumer.h"

void consumer_p_p_consumer_initialize(void) {
  printf("%s: I'm periodic\n", microkit_name);
}

void consumer_p_p_consumer_timeTriggered(void) {
  base_data_1_prod_2_cons_struct_i value;
  bool isFresh = get_read_port(&value);
  printf("%s: retrieved [", microkit_name);
  for (int i = 0; i < value.size; i++) {
    printf("%d", value.elements[i]);
    if (i + 1 != value.size) {
      printf(", ");
    }
  }
  printf("] which is %s\n", isFresh ? "fresh" : "stale");
}
