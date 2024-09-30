#include "p1_t1.h"

void p1_t1_initialize(void) {
  // add initialization code here
  printf("%s: Init\n", microkit_name);
}

void p1_t1_timeTriggered() {
  // add compute phase code here
  printf("%s: timeTriggered\n", microkit_name);
}
