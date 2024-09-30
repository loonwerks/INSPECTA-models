#include "seL4_LowLevelEthernetDriver_LowLevelEthernetDriver.h"

base_SW_RawEthernetMessage_Impl rx;

int value = 100;
void seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_initialize(void) {
  // add initialization code here
  printf("%s: Init\n", microkit_name);

  for (int i = 0; i < base_SW_RawEthernetMessage_Impl_SIZE; i++) {
    rx[i] = value;
  }
}

void seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_timeTriggered() {
  // add compute phase code here
  //printf("%s: timeTriggered\n", microkit_name);
  printf("-------%s------\n", microkit_name);

  {
    base_SW_RawEthernetMessage_Impl tx;
    getEthernetFramesTx(tx);

    int val = (int32_t)(tx[0] << 24) |
      (tx[1] << 16) |
      (tx[2] << 8) |
      (tx[3]);

    printf("TX Received: %d\n", val);
  }

  ////////////////////////////////////////////////////
  ////////////////////////////////////////////////////

  {
    value = value - 1;
    
    rx[0] = (value >> 24) & 0xFF; // Most significant byte
    rx[1] = (value >> 16) & 0xFF;
    rx[2] = (value >> 8) & 0xFF;
    rx[3] = value & 0xFF;         // Least significant byte

    putEthernetFramesRx(rx);

    printf("RX Sent %d\n", value);
  }
}
