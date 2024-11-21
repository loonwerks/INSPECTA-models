#include <stdint.h>
#include <microkit.h>

#define PORT_TO_PRODUCER_P_P_PRODUCER_MON 61
#define PORT_TO_CONSUMER_P_S1_CONSUMER_MON 59
#define PORT_TO_CONSUMER_P_S2_CONSUMER_MON 57
#define PORT_TO_CONSUMER_P_S5_CONSUMER_MON 55

void init(void) {}

void notified(microkit_channel channel) {
  switch(channel) {
    case PORT_TO_PRODUCER_P_P_PRODUCER_MON:
      microkit_notify(PORT_TO_PRODUCER_P_P_PRODUCER_MON);
      break;
    case PORT_TO_CONSUMER_P_S1_CONSUMER_MON:
      microkit_notify(PORT_TO_CONSUMER_P_S1_CONSUMER_MON);
      break;
    case PORT_TO_CONSUMER_P_S2_CONSUMER_MON:
      microkit_notify(PORT_TO_CONSUMER_P_S2_CONSUMER_MON);
      break;
    case PORT_TO_CONSUMER_P_S5_CONSUMER_MON:
      microkit_notify(PORT_TO_CONSUMER_P_S5_CONSUMER_MON);
      break;
  }
}
