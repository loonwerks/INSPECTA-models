#include "seL4_ArduPilot_ArduPilot.h"

void seL4_ArduPilot_ArduPilot_initialize(void);
void seL4_ArduPilot_ArduPilot_timeTriggered(void);

volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesTx0_queue_1;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesTx1_queue_1;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesTx2_queue_1;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesTx3_queue_1;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesRx0_queue_1;
sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t EthernetFramesRx0_recv_queue;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesRx1_queue_1;
sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t EthernetFramesRx1_recv_queue;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesRx2_queue_1;
sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t EthernetFramesRx2_recv_queue;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesRx3_queue_1;
sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t EthernetFramesRx3_recv_queue;

#define PORT_FROM_MON 60

bool put_EthernetFramesTx0(const base_SW_RawEthernetMessage_Impl *data) {
  sb_queue_base_SW_RawEthernetMessage_Impl_1_enqueue((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesTx0_queue_1, (base_SW_RawEthernetMessage_Impl *) data);

  return true;
}

bool put_EthernetFramesTx1(const base_SW_RawEthernetMessage_Impl *data) {
  sb_queue_base_SW_RawEthernetMessage_Impl_1_enqueue((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesTx1_queue_1, (base_SW_RawEthernetMessage_Impl *) data);

  return true;
}

bool put_EthernetFramesTx2(const base_SW_RawEthernetMessage_Impl *data) {
  sb_queue_base_SW_RawEthernetMessage_Impl_1_enqueue((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesTx2_queue_1, (base_SW_RawEthernetMessage_Impl *) data);

  return true;
}

bool put_EthernetFramesTx3(const base_SW_RawEthernetMessage_Impl *data) {
  sb_queue_base_SW_RawEthernetMessage_Impl_1_enqueue((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesTx3_queue_1, (base_SW_RawEthernetMessage_Impl *) data);

  return true;
}

base_SW_RawEthernetMessage_Impl last_EthernetFramesRx0_payload;

bool get_EthernetFramesRx0(base_SW_RawEthernetMessage_Impl *data) {
  sb_event_counter_t numDropped;
  base_SW_RawEthernetMessage_Impl fresh_data;
  bool isFresh = sb_queue_base_SW_RawEthernetMessage_Impl_1_dequeue((sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t *) &EthernetFramesRx0_recv_queue, &numDropped, &fresh_data);
  if (isFresh) {
    memcpy(&last_EthernetFramesRx0_payload, &fresh_data, base_SW_RawEthernetMessage_Impl_SIZE);
  }
  memcpy(data, &last_EthernetFramesRx0_payload, base_SW_RawEthernetMessage_Impl_SIZE);
  return isFresh;
}

base_SW_RawEthernetMessage_Impl last_EthernetFramesRx1_payload;

bool get_EthernetFramesRx1(base_SW_RawEthernetMessage_Impl *data) {
  sb_event_counter_t numDropped;
  base_SW_RawEthernetMessage_Impl fresh_data;
  bool isFresh = sb_queue_base_SW_RawEthernetMessage_Impl_1_dequeue((sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t *) &EthernetFramesRx1_recv_queue, &numDropped, &fresh_data);
  if (isFresh) {
    memcpy(&last_EthernetFramesRx1_payload, &fresh_data, base_SW_RawEthernetMessage_Impl_SIZE);
  }
  memcpy(data, &last_EthernetFramesRx1_payload, base_SW_RawEthernetMessage_Impl_SIZE);
  return isFresh;
}

base_SW_RawEthernetMessage_Impl last_EthernetFramesRx2_payload;

bool get_EthernetFramesRx2(base_SW_RawEthernetMessage_Impl *data) {
  sb_event_counter_t numDropped;
  base_SW_RawEthernetMessage_Impl fresh_data;
  bool isFresh = sb_queue_base_SW_RawEthernetMessage_Impl_1_dequeue((sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t *) &EthernetFramesRx2_recv_queue, &numDropped, &fresh_data);
  if (isFresh) {
    memcpy(&last_EthernetFramesRx2_payload, &fresh_data, base_SW_RawEthernetMessage_Impl_SIZE);
  }
  memcpy(data, &last_EthernetFramesRx2_payload, base_SW_RawEthernetMessage_Impl_SIZE);
  return isFresh;
}

base_SW_RawEthernetMessage_Impl last_EthernetFramesRx3_payload;

bool get_EthernetFramesRx3(base_SW_RawEthernetMessage_Impl *data) {
  sb_event_counter_t numDropped;
  base_SW_RawEthernetMessage_Impl fresh_data;
  bool isFresh = sb_queue_base_SW_RawEthernetMessage_Impl_1_dequeue((sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_t *) &EthernetFramesRx3_recv_queue, &numDropped, &fresh_data);
  if (isFresh) {
    memcpy(&last_EthernetFramesRx3_payload, &fresh_data, base_SW_RawEthernetMessage_Impl_SIZE);
  }
  memcpy(data, &last_EthernetFramesRx3_payload, base_SW_RawEthernetMessage_Impl_SIZE);
  return isFresh;
}

void init(void) {
  sb_queue_base_SW_RawEthernetMessage_Impl_1_init((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesTx0_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_init((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesTx1_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_init((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesTx2_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_init((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesTx3_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_init(&EthernetFramesRx0_recv_queue, (sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRx0_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_init(&EthernetFramesRx1_recv_queue, (sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRx1_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_init(&EthernetFramesRx2_recv_queue, (sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRx2_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_Recv_init(&EthernetFramesRx3_recv_queue, (sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRx3_queue_1);

  seL4_ArduPilot_ArduPilot_initialize();
}

void notified(microkit_channel channel) {
  switch (channel) {
    case PORT_FROM_MON:
      seL4_ArduPilot_ArduPilot_timeTriggered();
      break;
  }
}
