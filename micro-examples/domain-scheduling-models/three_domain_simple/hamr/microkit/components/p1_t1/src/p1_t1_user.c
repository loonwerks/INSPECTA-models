#include "p1_t1.h"

int value = 0;

void p1_t1_initialize(void) {
  // add initialization code here
  printf("%s: Init\n", microkit_name);

  put_write_port(&value);
}

void p1_t1_timeTriggered() {
  // add compute phase code here
  //printf("%s: timeTriggered\n", microkit_name);

  value = value + 1;

  put_write_port(&value);

  printf("%s: Sent %d\n", microkit_name, value);
}

void p1_t1_notify(microkit_channel channel) {
}