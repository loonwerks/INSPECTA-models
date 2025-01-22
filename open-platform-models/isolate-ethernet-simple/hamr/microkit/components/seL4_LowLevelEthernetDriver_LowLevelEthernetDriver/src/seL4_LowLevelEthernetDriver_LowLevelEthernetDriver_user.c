#include "seL4_LowLevelEthernetDriver_LowLevelEthernetDriver.h"

base_SW_StructuredEthernetMessage_i rx;

int value = 100;
void seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_initialize(void) {
  // add initialization code here
  printf("%s: Init\n", microkit_name);

  // event data ports so no initialization needed
}

void seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_timeTriggered() {
  // add compute phase code here
  //printf("%s: timeTriggered\n", microkit_name);
  printf("-------%s------\n", microkit_name);

  if (!EthernetFramesTx_is_empty()) {
    base_SW_StructuredEthernetMessage_i tx;
    get_EthernetFramesTx(&tx);

    int val = (int32_t)(tx.rawMessage[0] << 24) |
      (tx.rawMessage[1] << 16) |
      (tx.rawMessage[2] << 8) |
      (tx.rawMessage[3]);

    printf("TX Received: %d\n", val);
  }

  ////////////////////////////////////////////////////
  ////////////////////////////////////////////////////

  {
    value = value - 1;
    
    rx.rawMessage[0] = (value >> 24) & 0xFF; // Most significant byte
    rx.rawMessage[1] = (value >> 16) & 0xFF;
    rx.rawMessage[2] = (value >> 8) & 0xFF;
    rx.rawMessage[3] = value & 0xFF;         // Least significant byte

    put_EthernetFramesRx(&rx);

    printf("RX Sent %d\n", value);
  }
}

void seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_notify(microkit_channel channel){}
