#include "p2_t2.h"

void p2_t2_initialize(void) {
  // add initialization code here
  printf("%s: Init\n", microkit_name);
}

void p2_t2_timeTriggered() {
  // add compute phase code here
  printf("%s: timeTriggered\n", microkit_name);
}
