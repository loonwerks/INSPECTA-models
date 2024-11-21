#include "consumer_p_s2_consumer.h"

#define PORT_PACER 57

#define PORT_TO_CHILD 56

void init(void) {
  microkit_notify(PORT_PACER);
}

void notified(microkit_channel channel) {
  switch (channel) {
    case PORT_PACER:
      // notify child
      microkit_notify(PORT_TO_CHILD);

      // send response back to pacer
      microkit_notify(PORT_PACER);
      break;
  }
}