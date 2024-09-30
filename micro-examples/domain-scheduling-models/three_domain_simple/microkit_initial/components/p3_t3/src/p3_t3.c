#include "p3_t3.h"

void p3_t3_initialize(void);
void p3_t3_timeTriggered(void);

volatile uint8_t *read_port;

#define PORT_FROM_MON 56

void getRead_port(uint8_t *value) {
  // TODO need memmove or memcpy
  for (int i = 0; i < base_test_data_port_periodic_three_domains_RawEthernetMessage_SIZE; i++){
    value[i] = read_port[i];
  }
}

void init(void) {
  p3_t3_initialize();
}

void notified(microkit_channel channel) {
  switch (channel) {
    case PORT_FROM_MON:
      p3_t3_timeTriggered();
      break;
  }
}
