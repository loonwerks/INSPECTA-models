#include <stdint.h>
#include <microkit.h>

#define PORT_TO_ARDUPILOT_ARDUPILOT_MON 61
#define PORT_TO_FIREWALL_FIREWALL_MON 59
#define PORT_TO_LOWLEVELETHERNETDRIVER_LOWLEVELETHERNETDRIVER_MON 57

void init(void) {}

void notified(microkit_channel channel) {
  switch(channel) {
    case PORT_TO_ARDUPILOT_ARDUPILOT_MON:
      microkit_notify(PORT_TO_ARDUPILOT_ARDUPILOT_MON);
      break;
    case PORT_TO_FIREWALL_FIREWALL_MON:
      microkit_notify(PORT_TO_FIREWALL_FIREWALL_MON);
      break;
    case PORT_TO_LOWLEVELETHERNETDRIVER_LOWLEVELETHERNETDRIVER_MON:
      microkit_notify(PORT_TO_LOWLEVELETHERNETDRIVER_LOWLEVELETHERNETDRIVER_MON);
      break;
  }
}
