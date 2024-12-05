#include "seL4_Firewall_Firewall.h"

void seL4_Firewall_Firewall_initialize(void);
void seL4_Firewall_Firewall_timeTriggered(void);

volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesTxIn0_queue_1;
sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t EthernetFramesTxIn0_recv_queue;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesTxIn1_queue_1;
sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t EthernetFramesTxIn1_recv_queue;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesTxIn2_queue_1;
sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t EthernetFramesTxIn2_recv_queue;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesTxIn3_queue_1;
sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t EthernetFramesTxIn3_recv_queue;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesRxOut0_queue_1;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesRxOut1_queue_1;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesRxOut2_queue_1;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesRxOut3_queue_1;
volatile sb_queue_base_SW_SizedEthernetMessage_Impl_1_t *EthernetFramesTxOut0_queue_1;
volatile sb_queue_base_SW_SizedEthernetMessage_Impl_1_t *EthernetFramesTxOut1_queue_1;
volatile sb_queue_base_SW_SizedEthernetMessage_Impl_1_t *EthernetFramesTxOut2_queue_1;
volatile sb_queue_base_SW_SizedEthernetMessage_Impl_1_t *EthernetFramesTxOut3_queue_1;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesRxIn0_queue_1;
sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t EthernetFramesRxIn0_recv_queue;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesRxIn1_queue_1;
sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t EthernetFramesRxIn1_recv_queue;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesRxIn2_queue_1;
sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t EthernetFramesRxIn2_recv_queue;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesRxIn3_queue_1;
sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t EthernetFramesRxIn3_recv_queue;

#define PORT_FROM_MON 58

base_SW_RawEthernetMessage_Impl last_EthernetFramesTxIn0_payload;

bool get_EthernetFramesTxIn0(base_SW_RawEthernetMessage_Impl *data) {
  sb_event_counter_t numDropped;
  base_SW_RawEthernetMessage_Impl fresh_data;
  bool isFresh = sb_queue_base_SW_RawEthernetMessage_Impl_1_dequeue((sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t *) &EthernetFramesTxIn0_recv_queue, &numDropped, &fresh_data);
  if (isFresh) {
    memcpy(&last_EthernetFramesTxIn0_payload, &fresh_data, base_SW_RawEthernetMessage_Impl_SIZE);
  }
  memcpy(data, &last_EthernetFramesTxIn0_payload, base_SW_RawEthernetMessage_Impl_SIZE);
  return isFresh;
}

base_SW_RawEthernetMessage_Impl last_EthernetFramesTxIn1_payload;

bool get_EthernetFramesTxIn1(base_SW_RawEthernetMessage_Impl *data) {
  sb_event_counter_t numDropped;
  base_SW_RawEthernetMessage_Impl fresh_data;
  bool isFresh = sb_queue_base_SW_RawEthernetMessage_Impl_1_dequeue((sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t *) &EthernetFramesTxIn1_recv_queue, &numDropped, &fresh_data);
  if (isFresh) {
    memcpy(&last_EthernetFramesTxIn1_payload, &fresh_data, base_SW_RawEthernetMessage_Impl_SIZE);
  }
  memcpy(data, &last_EthernetFramesTxIn1_payload, base_SW_RawEthernetMessage_Impl_SIZE);
  return isFresh;
}

base_SW_RawEthernetMessage_Impl last_EthernetFramesTxIn2_payload;

bool get_EthernetFramesTxIn2(base_SW_RawEthernetMessage_Impl *data) {
  sb_event_counter_t numDropped;
  base_SW_RawEthernetMessage_Impl fresh_data;
  bool isFresh = sb_queue_base_SW_RawEthernetMessage_Impl_1_dequeue((sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t *) &EthernetFramesTxIn2_recv_queue, &numDropped, &fresh_data);
  if (isFresh) {
    memcpy(&last_EthernetFramesTxIn2_payload, &fresh_data, base_SW_RawEthernetMessage_Impl_SIZE);
  }
  memcpy(data, &last_EthernetFramesTxIn2_payload, base_SW_RawEthernetMessage_Impl_SIZE);
  return isFresh;
}

base_SW_RawEthernetMessage_Impl last_EthernetFramesTxIn3_payload;

bool get_EthernetFramesTxIn3(base_SW_RawEthernetMessage_Impl *data) {
  sb_event_counter_t numDropped;
  base_SW_RawEthernetMessage_Impl fresh_data;
  bool isFresh = sb_queue_base_SW_RawEthernetMessage_Impl_1_dequeue((sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t *) &EthernetFramesTxIn3_recv_queue, &numDropped, &fresh_data);
  if (isFresh) {
    memcpy(&last_EthernetFramesTxIn3_payload, &fresh_data, base_SW_RawEthernetMessage_Impl_SIZE);
  }
  memcpy(data, &last_EthernetFramesTxIn3_payload, base_SW_RawEthernetMessage_Impl_SIZE);
  return isFresh;
}

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

base_SW_RawEthernetMessage_Impl last_EthernetFramesRxIn0_payload;

bool get_EthernetFramesRxIn0(base_SW_RawEthernetMessage_Impl *data) {
  sb_event_counter_t numDropped;
  base_SW_RawEthernetMessage_Impl fresh_data;
  bool isFresh = sb_queue_base_SW_RawEthernetMessage_Impl_1_dequeue((sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t *) &EthernetFramesRxIn0_recv_queue, &numDropped, &fresh_data);
  if (isFresh) {
    memcpy(&last_EthernetFramesRxIn0_payload, &fresh_data, base_SW_RawEthernetMessage_Impl_SIZE);
  }
  memcpy(data, &last_EthernetFramesRxIn0_payload, base_SW_RawEthernetMessage_Impl_SIZE);
  return isFresh;
}

base_SW_RawEthernetMessage_Impl last_EthernetFramesRxIn1_payload;

bool get_EthernetFramesRxIn1(base_SW_RawEthernetMessage_Impl *data) {
  sb_event_counter_t numDropped;
  base_SW_RawEthernetMessage_Impl fresh_data;
  bool isFresh = sb_queue_base_SW_RawEthernetMessage_Impl_1_dequeue((sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t *) &EthernetFramesRxIn1_recv_queue, &numDropped, &fresh_data);
  if (isFresh) {
    memcpy(&last_EthernetFramesRxIn1_payload, &fresh_data, base_SW_RawEthernetMessage_Impl_SIZE);
  }
  memcpy(data, &last_EthernetFramesRxIn1_payload, base_SW_RawEthernetMessage_Impl_SIZE);
  return isFresh;
}

base_SW_RawEthernetMessage_Impl last_EthernetFramesRxIn2_payload;

bool get_EthernetFramesRxIn2(base_SW_RawEthernetMessage_Impl *data) {
  sb_event_counter_t numDropped;
  base_SW_RawEthernetMessage_Impl fresh_data;
  bool isFresh = sb_queue_base_SW_RawEthernetMessage_Impl_1_dequeue((sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t *) &EthernetFramesRxIn2_recv_queue, &numDropped, &fresh_data);
  if (isFresh) {
    memcpy(&last_EthernetFramesRxIn2_payload, &fresh_data, base_SW_RawEthernetMessage_Impl_SIZE);
  }
  memcpy(data, &last_EthernetFramesRxIn2_payload, base_SW_RawEthernetMessage_Impl_SIZE);
  return isFresh;
}

base_SW_RawEthernetMessage_Impl last_EthernetFramesRxIn3_payload;

bool get_EthernetFramesRxIn3(base_SW_RawEthernetMessage_Impl *data) {
  sb_event_counter_t numDropped;
  base_SW_RawEthernetMessage_Impl fresh_data;
  bool isFresh = sb_queue_base_SW_RawEthernetMessage_Impl_1_dequeue((sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t *) &EthernetFramesRxIn3_recv_queue, &numDropped, &fresh_data);
  if (isFresh) {
    memcpy(&last_EthernetFramesRxIn3_payload, &fresh_data, base_SW_RawEthernetMessage_Impl_SIZE);
  }
  memcpy(data, &last_EthernetFramesRxIn3_payload, base_SW_RawEthernetMessage_Impl_SIZE);
  return isFresh;
}

void init(void) {
  sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_init(&EthernetFramesTxIn0_recv_queue, (sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesTxIn0_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_init(&EthernetFramesTxIn1_recv_queue, (sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesTxIn1_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_init(&EthernetFramesTxIn2_recv_queue, (sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesTxIn2_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_init(&EthernetFramesTxIn3_recv_queue, (sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesTxIn3_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_init((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRxOut0_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_init((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRxOut1_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_init((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRxOut2_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_init((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRxOut3_queue_1);

  sb_queue_base_SW_SizedEthernetMessage_Impl_1_init((sb_queue_base_SW_SizedEthernetMessage_Impl_1_t *) EthernetFramesTxOut0_queue_1);

  sb_queue_base_SW_SizedEthernetMessage_Impl_1_init((sb_queue_base_SW_SizedEthernetMessage_Impl_1_t *) EthernetFramesTxOut1_queue_1);

  sb_queue_base_SW_SizedEthernetMessage_Impl_1_init((sb_queue_base_SW_SizedEthernetMessage_Impl_1_t *) EthernetFramesTxOut2_queue_1);

  sb_queue_base_SW_SizedEthernetMessage_Impl_1_init((sb_queue_base_SW_SizedEthernetMessage_Impl_1_t *) EthernetFramesTxOut3_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_init(&EthernetFramesRxIn0_recv_queue, (sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRxIn0_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_init(&EthernetFramesRxIn1_recv_queue, (sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRxIn1_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_init(&EthernetFramesRxIn2_recv_queue, (sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRxIn2_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_init(&EthernetFramesRxIn3_recv_queue, (sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRxIn3_queue_1);

  seL4_Firewall_Firewall_initialize();
}

void notified(microkit_channel channel) {
  switch (channel) {
    case PORT_FROM_MON:
      seL4_Firewall_Firewall_timeTriggered();
      break;
  }
}
