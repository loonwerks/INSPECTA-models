#include "p3_t3.h"

void p3_t3_initialize(void) {
  // add initialization code here
  printf("%s: Init\n", microkit_name);
}

void p3_t3_timeTriggered() {
  // add compute phase code here
  //printf("%s: timeTriggered\n", microkit_name);

  int32_t value = getRead_port();

  printf("%s: Received: %d\n", microkit_name, value);  
}
