#include "p2_t2.h"

void p2_t2_initialize(void) {
  // add initialization code here
  printf("%s: Init\n", microkit_name);
}

void p2_t2_timeTriggered() {
  // add compute phase code here
  //printf("%s: timeTriggered\n", microkit_name);
    
  base_test_data_port_periodic_three_domains_RawEthernetMessage rx;
  getRead_port(rx);

  int val = (int32_t)(rx[0] << 24) |
    (rx[1] << 16) |
    (rx[2] << 8) |
    (rx[3]);

  if (val % 2 == 0) {
    putWrite_port(rx);
    printf("%s: Allowed %d\n", microkit_name, val);
  } else {
    printf("%s: Blocked %d\n", microkit_name, val);
  }
}

