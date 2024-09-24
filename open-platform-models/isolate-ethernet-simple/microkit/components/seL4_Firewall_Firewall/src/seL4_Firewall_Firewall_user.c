#include "../../../include/printf.h"
#include "seL4_Firewall_Firewall.h"

void seL4_Firewall_Firewall_initialize(void) {
  // add initialization code here
  printf("%s: Init\n", microkit_name);
}

void seL4_Firewall_Firewall_timeTriggered() {
  // add compute phase code here
  //printf("%s: timeTriggered\n", microkit_name);

  base_SW_RawEthernetMessage_Impl rx;
  getEthernetFramesTxIn(rx);

  int val = (int32_t)(rx[0] << 24) |
    (rx[1] << 16) |
    (rx[2] << 8) |
    (rx[3]);

  if (val % 2 == 0) {
    putEthernetFramesTxOut(rx);
    printf("%s: Allowed %d\n", microkit_name, val);
  } else {
    printf("%s: Blocked %d\n", microkit_name, val);
  }
}
