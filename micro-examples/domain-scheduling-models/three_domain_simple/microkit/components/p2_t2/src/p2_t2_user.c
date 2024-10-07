#include "p2_t2.h"

void p2_t2_initialize(void) {
  // add initialization code here
  printf("%s: Init\n", microkit_name);
}

void p2_t2_timeTriggered() {
  // add compute phase code here
  //printf("%s: timeTriggered\n", microkit_name);
    
  int32_t value = getRead_port();

  if (value % 2 == 0) {
    putWrite_port(value);
    printf("%s: Allowed %d\n", microkit_name, value);
  } else {
    printf("%s: Blocked %d\n", microkit_name, value);
  }
}

