#include "consumer_p_p_consumer.h"

#define PORT_PACER 59

#define PORT_TO_CHILD 58

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