#include "../../../include/printf.h"
#include "seL4_LowLevelEthernetDriver_LowLevelEthernetDriver.h"

void seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_initialize(void) {
  // add initialization code here
  printf("%s: Init\n", microkit_name);
}

void seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_timeTriggered() {
  // add compute phase code here
  //printf("%s: timeTriggered\n", microkit_name);

  base_SW_RawEthernetMessage_Impl rx;
  getEthernetFramesTx(rx);

  int val = (int32_t)(rx[0] << 24) |
    (rx[1] << 16) |
    (rx[2] << 8) |
    (rx[3]);

  printf("%s: Received: %d\n", microkit_name, val);
}
