#include "p1_t1.h"

void p1_t1_initialize(void);
void p1_t1_timeTriggered(void);

volatile uint8_t *write_port;

#define PORT_FROM_MON 60

void putWrite_port(uint8_t *value) {
  // TODO need memmove or memcpy
  for (int i = 0; i < base_test_data_port_periodic_three_domains_RawEthernetMessage_SIZE; i++){
    write_port[i] = value[i];
  }
}

void init(void) {
  p1_t1_initialize();
}

void notified(microkit_channel channel) {
  switch (channel) {
    case PORT_FROM_MON:
      p1_t1_timeTriggered();
      break;
  }
}
