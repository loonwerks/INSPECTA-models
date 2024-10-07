#include "p3_t3.h"

void p3_t3_initialize(void);
void p3_t3_timeTriggered(void);

volatile int32_t *read_port;

#define PORT_FROM_MON 56

int32_t getRead_port() {
  return *read_port;
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
