#include "p1_t1.h"

base_test_data_port_periodic_three_domains_RawEthernetMessage tx;
int value = 0;

void p1_t1_initialize(void) {
  // add initialization code here
  printf("%s: Init\n", microkit_name);

  for (int i = 0; i < base_test_data_port_periodic_three_domains_RawEthernetMessage_SIZE; i++) {
    tx[i] = value;
  }
}

void p1_t1_timeTriggered() {
  // add compute phase code here
  //printf("%s: timeTriggered\n", microkit_name);

  value = value + 1;
  
  tx[0] = (value >> 24) & 0xFF; // Most significant byte
  tx[1] = (value >> 16) & 0xFF;
  tx[2] = (value >> 8) & 0xFF;
  tx[3] = value & 0xFF;         // Least significant byte

  putWrite_port(tx);

  printf("%s: Sent %d\n", microkit_name, value);
}
