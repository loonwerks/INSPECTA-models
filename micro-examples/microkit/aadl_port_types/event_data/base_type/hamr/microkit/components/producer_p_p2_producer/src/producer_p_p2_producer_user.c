#include "producer_p_p2_producer.h"

void producer_p_p2_producer_initialize(void) {
  // event ports so nothing to init
  printf("%s: I'm periodic\n", microkit_name);
}

int8_t counter = 0;
void producer_p_p2_producer_timeTriggered(void) {
  printf("%s: ", microkit_name);
  if (counter % 3 == 0) {
    put_write_port(&counter);
    printf("sent event %d\n", counter);
  } else {
    printf("no send\n");
  }
  counter = counter + 1;
}
