#include <stdint.h>
#include <microkit.h>

#define PORT_TO_P1_T1_MON 61
#define PORT_TO_P2_T2_MON 59
#define PORT_TO_P3_T3_MON 57

void init(void) {}

void notified(microkit_channel channel) {
  switch(channel) {
    case PORT_TO_P1_T1_MON:
      microkit_notify(PORT_TO_P1_T1_MON);
      break;
    case PORT_TO_P2_T2_MON:
      microkit_notify(PORT_TO_P2_T2_MON);
      break;
    case PORT_TO_P3_T3_MON:
      microkit_notify(PORT_TO_P3_T3_MON);
      break;
  }
}
