#include "seL4_ArduPilot_ArduPilot.h"

base_SW_RawEthernetMessage_Impl tx;

int value = 0;
void seL4_ArduPilot_ArduPilot_initialize(void) {
  // add initialization code here
  printf("%s: Init\n", microkit_name);

  for (int i = 0; i < base_SW_RawEthernetMessage_Impl_SIZE; i++) {
    tx[i] = value;
  }
}

void seL4_ArduPilot_ArduPilot_timeTriggered() {
  // add compute phase code here
  //printf("%s: timeTriggered\n", microkit_name);
  printf("-------%s------\n", microkit_name);

  {
    value = value + 1;
    
    tx[0] = (value >> 24) & 0xFF; // Most significant byte
    tx[1] = (value >> 16) & 0xFF;
    tx[2] = (value >> 8) & 0xFF;
    tx[3] = value & 0xFF;         // Least significant byte

    putEthernetFramesTx(tx);

    printf("TX Sent %d\n", value);
  }

  /////////////////////////////////////////////////////////
  /////////////////////////////////////////////////////////

  {
    base_SW_RawEthernetMessage_Impl rx;
    getEthernetFramesRx(rx);

    int val = (int32_t)(rx[0] << 24) |
      (rx[1] << 16) |
      (rx[2] << 8) |
      (rx[3]);

    printf("RX Received: %d\n", val);
  }
}
