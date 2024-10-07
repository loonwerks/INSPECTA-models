#include "p2_t2.h"

void p2_t2_initialize(void);
void p2_t2_timeTriggered(void);

volatile int32_t *read_port;
volatile int32_t *write_port;

#define PORT_FROM_MON 58

int32_t getRead_port() {
  return *read_port;
}

void putWrite_port(int32_t value) {
  *write_port = value;
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
