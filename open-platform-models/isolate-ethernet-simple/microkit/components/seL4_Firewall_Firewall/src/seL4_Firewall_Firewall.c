#include "seL4_Firewall_Firewall.h"

void seL4_Firewall_Firewall_initialize(void);
void seL4_Firewall_Firewall_timeTriggered(void);

volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesTxIn_queue_1;
sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t EthernetFramesTxIn_recv_queue;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesRxOut_queue_1;
volatile sb_queue_base_SW_SizedEthernetMessage_Impl_1_t *EthernetFramesTxOut_queue_1;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesRxIn_queue_1;
sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t EthernetFramesRxIn_recv_queue;

#define PORT_FROM_MON 58

base_SW_RawEthernetMessage_Impl last_EthernetFramesTxIn_payload;

bool get_EthernetFramesTxIn(base_SW_RawEthernetMessage_Impl *data) {
  sb_event_counter_t numDropped;
  base_SW_RawEthernetMessage_Impl fresh_data;
  bool isFresh = false;
  if (sb_queue_base_SW_RawEthernetMessage_Impl_1_dequeue((sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t *) &EthernetFramesTxIn_recv_queue, &numDropped, &fresh_data)) {
    last_EthernetFramesTxIn_payload = fresh_data;
    isFresh = true;
  }
  *data = last_EthernetFramesTxIn_payload;
  return isFresh;
}

bool put_EthernetFramesRxOut(const base_SW_RawEthernetMessage_Impl *data) {
  sb_queue_base_SW_RawEthernetMessage_Impl_1_enqueue((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRxOut_queue_1, (base_SW_RawEthernetMessage_Impl *) data);

  return true;
}

bool put_EthernetFramesTxOut(const base_SW_SizedEthernetMessage_Impl *data) {
  sb_queue_base_SW_SizedEthernetMessage_Impl_1_enqueue((sb_queue_base_SW_SizedEthernetMessage_Impl_1_t *) EthernetFramesTxOut_queue_1, (base_SW_SizedEthernetMessage_Impl *) data);

  return true;
}

base_SW_RawEthernetMessage_Impl last_EthernetFramesRxIn_payload;

bool get_EthernetFramesRxIn(base_SW_RawEthernetMessage_Impl *data) {
  sb_event_counter_t numDropped;
  base_SW_RawEthernetMessage_Impl fresh_data;
  bool isFresh = false;
  if (sb_queue_base_SW_RawEthernetMessage_Impl_1_dequeue((sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t *) &EthernetFramesRxIn_recv_queue, &numDropped, &fresh_data)) {
    last_EthernetFramesRxIn_payload = fresh_data;
    isFresh = true;
  }
  *data = last_EthernetFramesRxIn_payload;
  return isFresh;
}

void init(void) {
  sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_init(&EthernetFramesTxIn_recv_queue, (sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesTxIn_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_init((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRxOut_queue_1);

  sb_queue_base_SW_SizedEthernetMessage_Impl_1_init((sb_queue_base_SW_SizedEthernetMessage_Impl_1_t *) EthernetFramesTxOut_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_init(&EthernetFramesRxIn_recv_queue, (sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRxIn_queue_1);

  seL4_Firewall_Firewall_initialize();
}

void notified(microkit_channel channel) {
  switch (channel) {
    case PORT_FROM_MON:
      seL4_Firewall_Firewall_timeTriggered();
      break;
  }
}
