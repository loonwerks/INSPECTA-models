#include "seL4_Firewall_Firewall.h"

void seL4_Firewall_Firewall_initialize(void);
void seL4_Firewall_Firewall_timeTriggered(void);

volatile uint8_t *EthernetFramesRxIn;
volatile uint8_t *EthernetFramesTxIn;
volatile uint8_t *EthernetFramesRxOut;
volatile uint8_t *EthernetFramesTxOut;

#define PORT_FROM_MON 58

void getEthernetFramesRxIn(uint8_t *value) {
  // TODO need memmove or memcpy
  for (int i = 0; i < base_SW_RawEthernetMessage_Impl_SIZE; i++){
    value[i] = EthernetFramesRxIn[i];
  }
}

void getEthernetFramesTxIn(uint8_t *value) {
  // TODO need memmove or memcpy
  for (int i = 0; i < base_SW_RawEthernetMessage_Impl_SIZE; i++){
    value[i] = EthernetFramesTxIn[i];
  }
}

void putEthernetFramesRxOut(uint8_t *value) {
  // TODO need memmove or memcpy
  for (int i = 0; i < base_SW_RawEthernetMessage_Impl_SIZE; i++){
    EthernetFramesRxOut[i] = value[i];
  }
}

void putEthernetFramesTxOut(uint8_t *value) {
  // TODO need memmove or memcpy
  for (int i = 0; i < base_SW_RawEthernetMessage_Impl_SIZE; i++){
    EthernetFramesTxOut[i] = value[i];
  }
}

void init(void) {
  seL4_Firewall_Firewall_initialize();
}

void notified(microkit_channel channel) {
  switch (channel) {
    case PORT_FROM_MON:
      seL4_Firewall_Firewall_timeTriggered();
      break;
  }
}
