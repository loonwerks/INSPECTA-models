#include "p2_t2.h"

void p2_t2_initialize(void);
void p2_t2_timeTriggered(void);

volatile uint8_t *read_port;
volatile uint8_t *write_port;

#define PORT_FROM_MON 58

void getRead_port(uint8_t *value) {
  // TODO need memmove or memcpy
  for (int i = 0; i < base_test_data_port_periodic_three_domains_RawEthernetMessage_SIZE; i++){
    value[i] = read_port[i];
  }
}

void putWrite_port(uint8_t *value) {
  // TODO need memmove or memcpy
  for (int i = 0; i < base_test_data_port_periodic_three_domains_RawEthernetMessage_SIZE; i++){
    write_port[i] = value[i];
  }
}

void init(void) {
  p2_t2_initialize();
}

void notified(microkit_channel channel) {
  switch (channel) {
    case PORT_FROM_MON:
      p2_t2_timeTriggered();
      break;
  }
}
