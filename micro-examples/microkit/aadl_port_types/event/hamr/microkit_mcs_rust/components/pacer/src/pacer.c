#include <stdint.h>
#include <microkit.h>

#define PORT_TO_PRODUCER_P_P1_PRODUCER_MON 61
#define PORT_TO_PRODUCER_P_P2_PRODUCER_MON 59
#define PORT_TO_CONSUMER_P_P_CONSUMER_MON 57
#define PORT_TO_CONSUMER_P_S_CONSUMER_MON 55

void init(void) {}

void notified(microkit_channel channel) {
  switch(channel) {
    case PORT_TO_PRODUCER_P_P1_PRODUCER_MON:
      microkit_notify(PORT_TO_PRODUCER_P_P1_PRODUCER_MON);
      break;
    case PORT_TO_PRODUCER_P_P2_PRODUCER_MON:
      microkit_notify(PORT_TO_PRODUCER_P_P2_PRODUCER_MON);
      break;
    case PORT_TO_CONSUMER_P_P_CONSUMER_MON:
      microkit_notify(PORT_TO_CONSUMER_P_P_CONSUMER_MON);
      break;
    case PORT_TO_CONSUMER_P_S_CONSUMER_MON:
      microkit_notify(PORT_TO_CONSUMER_P_S_CONSUMER_MON);
      break;
  }
}
