#include <stdint.h>
#include <microkit.h>

#define PORT_TO_SEL4_ARDUPILOT_ARDUPILOT 61
#define PORT_TO_SEL4_FIREWALL_FIREWALL 60
#define PORT_TO_SEL4_LOWLEVELETHERNETDRIVER_LOWLEVELETHERNETDRIVER 59
#define PORT_TO_END_OF_FRAME_COMPONENT 58
#define PORT_FROM_END_OF_FRAME_COMPONENT 57

void paceComponents(){
  microkit_notify(PORT_TO_SEL4_ARDUPILOT_ARDUPILOT);
  microkit_notify(PORT_TO_SEL4_FIREWALL_FIREWALL);
  microkit_notify(PORT_TO_SEL4_LOWLEVELETHERNETDRIVER_LOWLEVELETHERNETDRIVER);
  microkit_notify(PORT_TO_END_OF_FRAME_COMPONENT);
}

void init(void) {}

void notified(microkit_channel channel) {
  switch(channel) {
    case PORT_FROM_END_OF_FRAME_COMPONENT:
      paceComponents();
      break;
  }
}
