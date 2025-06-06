#include "Firewall_Firewall.h"

// Do not edit this file as it will be overwritten if codegen is rerun

void Firewall_Firewall_initialize(void);
void Firewall_Firewall_notify(microkit_channel channel);
void Firewall_Firewall_timeTriggered(void);

volatile sb_queue_SW_RawEthernetMessage_1_t *EthernetFramesTxIn_queue_1;
sb_queue_SW_RawEthernetMessage_1_Recv_t EthernetFramesTxIn_recv_queue;
volatile sb_queue_SW_RawEthernetMessage_1_t *EthernetFramesRxOut_queue_1;
volatile sb_queue_SW_RawEthernetMessage_1_t *EthernetFramesTxOut_queue_1;
volatile sb_queue_SW_RawEthernetMessage_1_t *EthernetFramesRxIn_queue_1;
sb_queue_SW_RawEthernetMessage_1_Recv_t EthernetFramesRxIn_recv_queue;

#define PORT_FROM_MON 58

bool EthernetFramesTxIn_is_empty(void) {
  return sb_queue_SW_RawEthernetMessage_1_is_empty(&EthernetFramesTxIn_recv_queue);
}

bool get_EthernetFramesTxIn_poll(sb_event_counter_t *numDropped, SW_RawEthernetMessage *data) {
  return sb_queue_SW_RawEthernetMessage_1_dequeue((sb_queue_SW_RawEthernetMessage_1_Recv_t *) &EthernetFramesTxIn_recv_queue, numDropped, data);
}

bool get_EthernetFramesTxIn(SW_RawEthernetMessage *data) {
  sb_event_counter_t numDropped;
  return get_EthernetFramesTxIn_poll (&numDropped, data);
}

bool put_EthernetFramesRxOut(const SW_RawEthernetMessage *data) {
  sb_queue_SW_RawEthernetMessage_1_enqueue((sb_queue_SW_RawEthernetMessage_1_t *) EthernetFramesRxOut_queue_1, (SW_RawEthernetMessage *) data);

  return true;
}

bool put_EthernetFramesTxOut(const SW_RawEthernetMessage *data) {
  sb_queue_SW_RawEthernetMessage_1_enqueue((sb_queue_SW_RawEthernetMessage_1_t *) EthernetFramesTxOut_queue_1, (SW_RawEthernetMessage *) data);

  return true;
}

bool EthernetFramesRxIn_is_empty(void) {
  return sb_queue_SW_RawEthernetMessage_1_is_empty(&EthernetFramesRxIn_recv_queue);
}

bool get_EthernetFramesRxIn_poll(sb_event_counter_t *numDropped, SW_RawEthernetMessage *data) {
  return sb_queue_SW_RawEthernetMessage_1_dequeue((sb_queue_SW_RawEthernetMessage_1_Recv_t *) &EthernetFramesRxIn_recv_queue, numDropped, data);
}

bool get_EthernetFramesRxIn(SW_RawEthernetMessage *data) {
  sb_event_counter_t numDropped;
  return get_EthernetFramesRxIn_poll (&numDropped, data);
}

void init(void) {
  sb_queue_SW_RawEthernetMessage_1_Recv_init(&EthernetFramesTxIn_recv_queue, (sb_queue_SW_RawEthernetMessage_1_t *) EthernetFramesTxIn_queue_1);

  sb_queue_SW_RawEthernetMessage_1_init((sb_queue_SW_RawEthernetMessage_1_t *) EthernetFramesRxOut_queue_1);

  sb_queue_SW_RawEthernetMessage_1_init((sb_queue_SW_RawEthernetMessage_1_t *) EthernetFramesTxOut_queue_1);

  sb_queue_SW_RawEthernetMessage_1_Recv_init(&EthernetFramesRxIn_recv_queue, (sb_queue_SW_RawEthernetMessage_1_t *) EthernetFramesRxIn_queue_1);

  Firewall_Firewall_initialize();
}

void notified(microkit_channel channel) {
  switch (channel) {
    case PORT_FROM_MON:
      Firewall_Firewall_timeTriggered();
      break;
    default:
      Firewall_Firewall_notify(channel);
  }
}
