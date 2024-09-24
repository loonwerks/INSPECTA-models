#include <stdint.h>
#include <microkit.h>

#define PORT_TO_PACER 57
#define PORT_FROM_PACER 58

void init(void) {
  microkit_notify(PORT_TO_PACER);
}

void notified(microkit_channel channel) {
  switch (channel) {
    case PORT_FROM_PACER:
      microkit_notify(PORT_TO_PACER);
      break;
  }
}