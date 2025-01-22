#include "seL4_RxFirewall_RxFirewall.h"

void seL4_RxFirewall_RxFirewall_initialize(void);
void seL4_RxFirewall_RxFirewall_timeTriggered(void);

volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesRxOut0_queue_1;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesRxOut1_queue_1;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesRxOut2_queue_1;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesRxOut3_queue_1;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesRxIn0_queue_1;
sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t EthernetFramesRxIn0_recv_queue;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesRxIn1_queue_1;
sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t EthernetFramesRxIn1_recv_queue;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesRxIn2_queue_1;
sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t EthernetFramesRxIn2_recv_queue;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesRxIn3_queue_1;
sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t EthernetFramesRxIn3_recv_queue;

#define PORT_FROM_MON 58

bool put_EthernetFramesRxOut0(const base_SW_RawEthernetMessage_Impl *data) {
  sb_queue_base_SW_RawEthernetMessage_Impl_1_enqueue((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRxOut0_queue_1, (base_SW_RawEthernetMessage_Impl *) data);

  return true;
}

bool put_EthernetFramesRxOut1(const base_SW_RawEthernetMessage_Impl *data) {
  sb_queue_base_SW_RawEthernetMessage_Impl_1_enqueue((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRxOut1_queue_1, (base_SW_RawEthernetMessage_Impl *) data);

  return true;
}

bool put_EthernetFramesRxOut2(const base_SW_RawEthernetMessage_Impl *data) {
  sb_queue_base_SW_RawEthernetMessage_Impl_1_enqueue((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRxOut2_queue_1, (base_SW_RawEthernetMessage_Impl *) data);

  return true;
}

bool put_EthernetFramesRxOut3(const base_SW_RawEthernetMessage_Impl *data) {
  sb_queue_base_SW_RawEthernetMessage_Impl_1_enqueue((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRxOut3_queue_1, (base_SW_RawEthernetMessage_Impl *) data);

  return true;
}

bool EthernetFramesRxIn0_is_empty(void) {
  return sb_queue_base_SW_RawEthernetMessage_Impl_1_is_empty(&EthernetFramesRxIn0_recv_queue);
}

bool get_EthernetFramesRxIn0_poll(sb_event_counter_t *numDropped, base_SW_RawEthernetMessage_Impl *data) {
  return sb_queue_base_SW_RawEthernetMessage_Impl_1_dequeue((sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t *) &EthernetFramesRxIn0_recv_queue, numDropped, data);
}

bool get_EthernetFramesRxIn0(base_SW_RawEthernetMessage_Impl *data) {
  sb_event_counter_t numDropped;
  return get_EthernetFramesRxIn0_poll (&numDropped, data);
}

bool EthernetFramesRxIn1_is_empty(void) {
  return sb_queue_base_SW_RawEthernetMessage_Impl_1_is_empty(&EthernetFramesRxIn1_recv_queue);
}

bool get_EthernetFramesRxIn1_poll(sb_event_counter_t *numDropped, base_SW_RawEthernetMessage_Impl *data) {
  return sb_queue_base_SW_RawEthernetMessage_Impl_1_dequeue((sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t *) &EthernetFramesRxIn1_recv_queue, numDropped, data);
}

bool get_EthernetFramesRxIn1(base_SW_RawEthernetMessage_Impl *data) {
  sb_event_counter_t numDropped;
  return get_EthernetFramesRxIn1_poll (&numDropped, data);
}

bool EthernetFramesRxIn2_is_empty(void) {
  return sb_queue_base_SW_RawEthernetMessage_Impl_1_is_empty(&EthernetFramesRxIn2_recv_queue);
}

bool get_EthernetFramesRxIn2_poll(sb_event_counter_t *numDropped, base_SW_RawEthernetMessage_Impl *data) {
  return sb_queue_base_SW_RawEthernetMessage_Impl_1_dequeue((sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t *) &EthernetFramesRxIn2_recv_queue, numDropped, data);
}

bool get_EthernetFramesRxIn2(base_SW_RawEthernetMessage_Impl *data) {
  sb_event_counter_t numDropped;
  return get_EthernetFramesRxIn2_poll (&numDropped, data);
}

bool EthernetFramesRxIn3_is_empty(void) {
  return sb_queue_base_SW_RawEthernetMessage_Impl_1_is_empty(&EthernetFramesRxIn3_recv_queue);
}

bool get_EthernetFramesRxIn3_poll(sb_event_counter_t *numDropped, base_SW_RawEthernetMessage_Impl *data) {
  return sb_queue_base_SW_RawEthernetMessage_Impl_1_dequeue((sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t *) &EthernetFramesRxIn3_recv_queue, numDropped, data);
}

bool get_EthernetFramesRxIn3(base_SW_RawEthernetMessage_Impl *data) {
  sb_event_counter_t numDropped;
  return get_EthernetFramesRxIn3_poll (&numDropped, data);
}

void init(void) {
  sb_queue_base_SW_RawEthernetMessage_Impl_1_init((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRxOut0_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_init((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRxOut1_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_init((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRxOut2_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_init((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRxOut3_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_init(&EthernetFramesRxIn0_recv_queue, (sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRxIn0_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_init(&EthernetFramesRxIn1_recv_queue, (sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRxIn1_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_init(&EthernetFramesRxIn2_recv_queue, (sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRxIn2_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_init(&EthernetFramesRxIn3_recv_queue, (sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRxIn3_queue_1);

  seL4_RxFirewall_RxFirewall_initialize();
}

void notified(microkit_channel channel) {
  switch (channel) {
    case PORT_FROM_MON:
      seL4_RxFirewall_RxFirewall_timeTriggered();
      break;
  }
}
