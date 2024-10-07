#include "p1_t1.h"

void p1_t1_initialize(void);
void p1_t1_timeTriggered(void);

volatile int32_t *write_port;

#define PORT_FROM_MON 60

void putWrite_port(int32_t value) {
  *write_port = value;
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
