#include "consumer_p_p_consumer.h"

void consumer_p_p_consumer_initialize(void) {
  printf("%s: I'm periodic\n", microkit_name);
}

void consumer_p_p_consumer_timeTriggered(void) {
  if(get_read_port1()) {
    printf("%s: received a read port 1 event\n", microkit_name);
  } else {
    printf("%s: nothing received on read port 1\n", microkit_name);
  }

  if(get_read_port2()) {
    printf("%s: received a read port 2 event\n", microkit_name);
  } else {
    printf("%s: nothing received on read port 2\n", microkit_name);    
  }
}
