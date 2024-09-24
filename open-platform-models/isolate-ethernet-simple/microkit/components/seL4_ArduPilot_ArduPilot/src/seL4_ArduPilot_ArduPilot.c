#include "seL4_ArduPilot_ArduPilot.h"

void seL4_ArduPilot_ArduPilot_initialize(void);
void seL4_ArduPilot_ArduPilot_timeTriggered(void);

volatile uint8_t *EthernetFramesRx;
volatile uint8_t *EthernetFramesTx;

#define PORT_FROM_PACER 61

void getEthernetFramesRx(uint8_t *value) {
  // TODO need memmove or memcpy
  for (int i = 0; i < base_SW_RawEthernetMessage_Impl_SIZE; i++){
    value[i] = EthernetFramesRx[i];
  }
}

void putEthernetFramesTx(uint8_t *value) {
  // TODO need memmove or memcpy
  for (int i = 0; i < base_SW_RawEthernetMessage_Impl_SIZE; i++){
    EthernetFramesTx[i] = value[i];
  }
}

void init(void) {
  seL4_ArduPilot_ArduPilot_initialize();
}

void notified(microkit_channel channel) {
  switch (channel) {
    case PORT_FROM_PACER:
      seL4_ArduPilot_ArduPilot_timeTriggered();
      break;
  }
}
