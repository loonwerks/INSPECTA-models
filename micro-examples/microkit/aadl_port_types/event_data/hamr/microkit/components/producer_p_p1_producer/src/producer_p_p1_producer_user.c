#include "producer_p_p1_producer.h"

void producer_p_p1_producer_initialize(void) {
    // event data ports so nothing to init
  printf("%s: I'm periodic\n", microkit_name);
}

int8_t counter = 0;
void producer_p_p1_producer_timeTriggered(void) {
  printf("-------\n");
  printf("%s: ", microkit_name);
  if (counter % 2 == 0) {
    put_write_port(&counter);
    printf("sent event %d\n", counter);
  } else {
    printf("no send\n");
  }
  counter = counter + 1;
}