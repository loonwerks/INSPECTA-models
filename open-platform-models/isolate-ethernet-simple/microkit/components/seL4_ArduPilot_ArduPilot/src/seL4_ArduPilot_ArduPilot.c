#include "seL4_ArduPilot_ArduPilot.h"

// Do not edit this file as it will be overwritten if codegen is rerun

void seL4_ArduPilot_ArduPilot_initialize(void);
void seL4_ArduPilot_ArduPilot_notify(microkit_channel channel);
void seL4_ArduPilot_ArduPilot_timeTriggered(void);

volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesTx_queue_1;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesRx_queue_1;
sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t EthernetFramesRx_recv_queue;

#define PORT_FROM_MON 60

bool put_EthernetFramesTx(const base_SW_RawEthernetMessage_Impl *data) {
  sb_queue_base_SW_RawEthernetMessage_Impl_1_enqueue((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesTx_queue_1, (base_SW_RawEthernetMessage_Impl *) data);

  return true;
}

base_SW_RawEthernetMessage_Impl last_EthernetFramesRx_payload;

bool get_EthernetFramesRx(base_SW_RawEthernetMessage_Impl *data) {
  sb_event_counter_t numDropped;
  base_SW_RawEthernetMessage_Impl fresh_data;
  bool isFresh = sb_queue_base_SW_RawEthernetMessage_Impl_1_dequeue((sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t *) &EthernetFramesRx_recv_queue, &numDropped, &fresh_data);
  if (isFresh) {
    memcpy(&last_EthernetFramesRx_payload, &fresh_data, base_SW_RawEthernetMessage_Impl_SIZE);
  }
  memcpy(data, &last_EthernetFramesRx_payload, base_SW_RawEthernetMessage_Impl_SIZE);
  return isFresh;
}

void init(void) {
  sb_queue_base_SW_RawEthernetMessage_Impl_1_init((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesTx_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_init(&EthernetFramesRx_recv_queue, (sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRx_queue_1);

  seL4_ArduPilot_ArduPilot_initialize();
}

void notified(microkit_channel channel) {
  switch (channel) {
    case PORT_FROM_MON:
      seL4_ArduPilot_ArduPilot_timeTriggered();
      break;
    default:
      seL4_ArduPilot_ArduPilot_notify(channel);
  }
}
