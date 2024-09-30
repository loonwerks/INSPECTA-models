#include "seL4_LowLevelEthernetDriver_LowLevelEthernetDriver.h"

void seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_initialize(void);
void seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_timeTriggered(void);

volatile uint8_t *EthernetFramesRx;
volatile uint8_t *EthernetFramesTx;

#define PORT_FROM_MON 56

void putEthernetFramesRx(uint8_t *value) {
  // TODO need memmove or memcpy
  for (int i = 0; i < base_SW_RawEthernetMessage_Impl_SIZE; i++){
    EthernetFramesRx[i] = value[i];
  }
}

void getEthernetFramesTx(uint8_t *value) {
  // TODO need memmove or memcpy
  for (int i = 0; i < base_SW_RawEthernetMessage_Impl_SIZE; i++){
    value[i] = EthernetFramesTx[i];
  }
}

void init(void) {
  seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_initialize();
}

void notified(microkit_channel channel) {
  switch (channel) {
    case PORT_FROM_MON:
      seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_timeTriggered();
      break;
  }
}
