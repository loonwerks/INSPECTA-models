#include "ArduPilot_ArduPilot.h"

// Do not edit this file as it will be overwritten if codegen is rerun

void ArduPilot_ArduPilot_initialize(void);
void ArduPilot_ArduPilot_notify(microkit_channel channel);
void ArduPilot_ArduPilot_timeTriggered(void);

volatile sb_queue_SW_RawEthernetMessage_1_t *EthernetFramesTx_queue_1;
volatile sb_queue_SW_RawEthernetMessage_1_t *EthernetFramesRx_queue_1;
sb_queue_SW_RawEthernetMessage_1_Recv_t EthernetFramesRx_recv_queue;

#define PORT_FROM_MON 60

bool put_EthernetFramesTx(const SW_RawEthernetMessage *data) {
  sb_queue_SW_RawEthernetMessage_1_enqueue((sb_queue_SW_RawEthernetMessage_1_t *) EthernetFramesTx_queue_1, (SW_RawEthernetMessage *) data);

  return true;
}

bool EthernetFramesRx_is_empty(void) {
  return sb_queue_SW_RawEthernetMessage_1_is_empty(&EthernetFramesRx_recv_queue);
}

bool get_EthernetFramesRx_poll(sb_event_counter_t *numDropped, SW_RawEthernetMessage *data) {
  return sb_queue_SW_RawEthernetMessage_1_dequeue((sb_queue_SW_RawEthernetMessage_1_Recv_t *) &EthernetFramesRx_recv_queue, numDropped, data);
}

bool get_EthernetFramesRx(SW_RawEthernetMessage *data) {
  sb_event_counter_t numDropped;
  return get_EthernetFramesRx_poll (&numDropped, data);
}

void init(void) {
  sb_queue_SW_RawEthernetMessage_1_init((sb_queue_SW_RawEthernetMessage_1_t *) EthernetFramesTx_queue_1);

  sb_queue_SW_RawEthernetMessage_1_Recv_init(&EthernetFramesRx_recv_queue, (sb_queue_SW_RawEthernetMessage_1_t *) EthernetFramesRx_queue_1);

  ArduPilot_ArduPilot_initialize();
}

void notified(microkit_channel channel) {
  switch (channel) {
    case PORT_FROM_MON:
      ArduPilot_ArduPilot_timeTriggered();
      break;
    default:
      ArduPilot_ArduPilot_notify(channel);
  }
}
