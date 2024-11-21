#include "consumer_p_p_consumer.h"

void consumer_p_p_consumer_initialize(void) {
  printf("%s: I'm periodic\n", microkit_name);
}

void consumer_p_p_consumer_timeTriggered(void) {
  int8_t p1;
  if(get_read_port1(&p1)) {
    printf("%s: received %d on read port 1 event\n", microkit_name, p1);
  } else {
    printf("%s: nothing received on read port 1\n", microkit_name);
  }

  int8_t p2;
  if(get_read_port2(&p2)) {
    printf("%s: received %d on read port 2 event\n", microkit_name, p2);
  } else {
    printf("%s: nothing received on read port 2\n", microkit_name);    
  }
}
