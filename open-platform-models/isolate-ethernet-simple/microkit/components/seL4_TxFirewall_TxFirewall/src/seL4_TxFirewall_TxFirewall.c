#include "seL4_TxFirewall_TxFirewall.h"

void seL4_TxFirewall_TxFirewall_initialize(void);
void seL4_TxFirewall_TxFirewall_timeTriggered(void);

volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesTxIn0_queue_1;
sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t EthernetFramesTxIn0_recv_queue;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesTxIn1_queue_1;
sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t EthernetFramesTxIn1_recv_queue;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesTxIn2_queue_1;
sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t EthernetFramesTxIn2_recv_queue;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesTxIn3_queue_1;
sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t EthernetFramesTxIn3_recv_queue;
volatile sb_queue_base_SW_SizedEthernetMessage_Impl_1_t *EthernetFramesTxOut0_queue_1;
volatile sb_queue_base_SW_SizedEthernetMessage_Impl_1_t *EthernetFramesTxOut1_queue_1;
volatile sb_queue_base_SW_SizedEthernetMessage_Impl_1_t *EthernetFramesTxOut2_queue_1;
volatile sb_queue_base_SW_SizedEthernetMessage_Impl_1_t *EthernetFramesTxOut3_queue_1;

#define PORT_FROM_MON 56

bool EthernetFramesTxIn0_is_empty(void) {
  return sb_queue_base_SW_RawEthernetMessage_Impl_1_is_empty(&EthernetFramesTxIn0_recv_queue);
}

bool get_EthernetFramesTxIn0_poll(sb_event_counter_t *numDropped, base_SW_RawEthernetMessage_Impl *data) {
  return sb_queue_base_SW_RawEthernetMessage_Impl_1_dequeue((sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t *) &EthernetFramesTxIn0_recv_queue, numDropped, data);
}

bool get_EthernetFramesTxIn0(base_SW_RawEthernetMessage_Impl *data) {
  sb_event_counter_t numDropped;
  return get_EthernetFramesTxIn0_poll (&numDropped, data);
}

bool EthernetFramesTxIn1_is_empty(void) {
  return sb_queue_base_SW_RawEthernetMessage_Impl_1_is_empty(&EthernetFramesTxIn1_recv_queue);
}

bool get_EthernetFramesTxIn1_poll(sb_event_counter_t *numDropped, base_SW_RawEthernetMessage_Impl *data) {
  return sb_queue_base_SW_RawEthernetMessage_Impl_1_dequeue((sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t *) &EthernetFramesTxIn1_recv_queue, numDropped, data);
}

bool get_EthernetFramesTxIn1(base_SW_RawEthernetMessage_Impl *data) {
  sb_event_counter_t numDropped;
  return get_EthernetFramesTxIn1_poll (&numDropped, data);
}

bool EthernetFramesTxIn2_is_empty(void) {
  return sb_queue_base_SW_RawEthernetMessage_Impl_1_is_empty(&EthernetFramesTxIn2_recv_queue);
}

bool get_EthernetFramesTxIn2_poll(sb_event_counter_t *numDropped, base_SW_RawEthernetMessage_Impl *data) {
  return sb_queue_base_SW_RawEthernetMessage_Impl_1_dequeue((sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t *) &EthernetFramesTxIn2_recv_queue, numDropped, data);
}

bool get_EthernetFramesTxIn2(base_SW_RawEthernetMessage_Impl *data) {
  sb_event_counter_t numDropped;
  return get_EthernetFramesTxIn2_poll (&numDropped, data);
}

bool EthernetFramesTxIn3_is_empty(void) {
  return sb_queue_base_SW_RawEthernetMessage_Impl_1_is_empty(&EthernetFramesTxIn3_recv_queue);
}

bool get_EthernetFramesTxIn3_poll(sb_event_counter_t *numDropped, base_SW_RawEthernetMessage_Impl *data) {
  return sb_queue_base_SW_RawEthernetMessage_Impl_1_dequeue((sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t *) &EthernetFramesTxIn3_recv_queue, numDropped, data);
}

bool get_EthernetFramesTxIn3(base_SW_RawEthernetMessage_Impl *data) {
  sb_event_counter_t numDropped;
  return get_EthernetFramesTxIn3_poll (&numDropped, data);
}

bool put_EthernetFramesTxOut0(const base_SW_SizedEthernetMessage_Impl *data) {
  sb_queue_base_SW_SizedEthernetMessage_Impl_1_enqueue((sb_queue_base_SW_SizedEthernetMessage_Impl_1_t *) EthernetFramesTxOut0_queue_1, (base_SW_SizedEthernetMessage_Impl *) data);

  return true;
}

bool put_EthernetFramesTxOut1(const base_SW_SizedEthernetMessage_Impl *data) {
  sb_queue_base_SW_SizedEthernetMessage_Impl_1_enqueue((sb_queue_base_SW_SizedEthernetMessage_Impl_1_t *) EthernetFramesTxOut1_queue_1, (base_SW_SizedEthernetMessage_Impl *) data);

  return true;
}

bool put_EthernetFramesTxOut2(const base_SW_SizedEthernetMessage_Impl *data) {
  sb_queue_base_SW_SizedEthernetMessage_Impl_1_enqueue((sb_queue_base_SW_SizedEthernetMessage_Impl_1_t *) EthernetFramesTxOut2_queue_1, (base_SW_SizedEthernetMessage_Impl *) data);

  return true;
}

bool put_EthernetFramesTxOut3(const base_SW_SizedEthernetMessage_Impl *data) {
  sb_queue_base_SW_SizedEthernetMessage_Impl_1_enqueue((sb_queue_base_SW_SizedEthernetMessage_Impl_1_t *) EthernetFramesTxOut3_queue_1, (base_SW_SizedEthernetMessage_Impl *) data);

  return true;
}

void init(void) {
  sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_init(&EthernetFramesTxIn0_recv_queue, (sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesTxIn0_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_init(&EthernetFramesTxIn1_recv_queue, (sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesTxIn1_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_init(&EthernetFramesTxIn2_recv_queue, (sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesTxIn2_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_init(&EthernetFramesTxIn3_recv_queue, (sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesTxIn3_queue_1);

  sb_queue_base_SW_SizedEthernetMessage_Impl_1_init((sb_queue_base_SW_SizedEthernetMessage_Impl_1_t *) EthernetFramesTxOut0_queue_1);

  sb_queue_base_SW_SizedEthernetMessage_Impl_1_init((sb_queue_base_SW_SizedEthernetMessage_Impl_1_t *) EthernetFramesTxOut1_queue_1);

  sb_queue_base_SW_SizedEthernetMessage_Impl_1_init((sb_queue_base_SW_SizedEthernetMessage_Impl_1_t *) EthernetFramesTxOut2_queue_1);

  sb_queue_base_SW_SizedEthernetMessage_Impl_1_init((sb_queue_base_SW_SizedEthernetMessage_Impl_1_t *) EthernetFramesTxOut3_queue_1);

  seL4_TxFirewall_TxFirewall_initialize();
}

void notified(microkit_channel channel) {
  switch (channel) {
    case PORT_FROM_MON:
      seL4_TxFirewall_TxFirewall_timeTriggered();
      break;
  }
}