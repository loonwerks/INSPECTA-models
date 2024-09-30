#include "p3_t3.h"

void p3_t3_initialize(void) {
  // add initialization code here
  printf("%s: Init\n", microkit_name);
}

void p3_t3_timeTriggered() {
  // add compute phase code here
  //printf("%s: timeTriggered\n", microkit_name);

  base_test_data_port_periodic_three_domains_RawEthernetMessage rx;
  getRead_port(rx);

  int val = (int32_t)(rx[0] << 24) |
    (rx[1] << 16) |
    (rx[2] << 8) |
    (rx[3]);

  printf("%s: Received: %d\n", microkit_name, val);  
}
