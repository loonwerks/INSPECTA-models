#include "seL4_ArduPilot_ArduPilot.h"

base_SW_StructuredEthernetMessage_i tx;

int value = 0;
void seL4_ArduPilot_ArduPilot_initialize(void) {
  // event data ports so no initialization needed
  printf("%s: Init\n", microkit_name);
}

void seL4_ArduPilot_ArduPilot_timeTriggered() {
  // add compute phase code here
  //printf("%s: timeTriggered\n", microkit_name);
  printf("-------%s------\n", microkit_name); 

  {
    value = value + 1;
    
    tx.rawMessage[0] = (value >> 24) & 0xFF; // Most significant byte
    tx.rawMessage[1] = (value >> 16) & 0xFF;
    tx.rawMessage[2] = (value >> 8) & 0xFF;
    tx.rawMessage[3] = value & 0xFF;         // Least significant byte

    put_EthernetFramesTx(&tx);

    printf("TX Sent %d\n", value);
  }

  /////////////////////////////////////////////////////////
  /////////////////////////////////////////////////////////

  if (!EthernetFramesRx_is_empty()) {
    base_SW_StructuredEthernetMessage_i rx;
    get_EthernetFramesRx(&rx);

    int val = (int32_t)(rx.rawMessage[0] << 24) |
      (rx.rawMessage[1] << 16) |
      (rx.rawMessage[2] << 8) |
      (rx.rawMessage[3]);

    printf("RX Received: %d\n", val);
  }
}

void seL4_ArduPilot_ArduPilot_notify(microkit_channel channel){}