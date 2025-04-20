#include "producer_producer.h"
#include <stdint.h>
// This file will not be overwritten if codegen is rerun

int64_t i = 0;

void send(void) {
  bool b = i % 2 == 0 ? true : false;
  put_myBoolean(&b);
  
  put_myInt64(&i);
  i++;
}

void producer_producer_initialize(void) {
  printf("%s: producer_producer_initialize invoked\n", microkit_name);
  send();
}

void producer_producer_timeTriggered(void) {
  printf("%s: producer_producer_timeTriggered invoked\n", microkit_name);
  send();
}

void producer_producer_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
