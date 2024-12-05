#include "seL4_LowLevelEthernetDriver_LowLevelEthernetDriver.h"

void seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_initialize(void);
void seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_timeTriggered(void);

volatile sb_queue_base_SW_SizedEthernetMessage_Impl_1_t *EthernetFramesTx0_queue_1;
sb_queue_base_SW_SizedEthernetMessage_Impl_1_Recv_t EthernetFramesTx0_recv_queue;
volatile sb_queue_base_SW_SizedEthernetMessage_Impl_1_t *EthernetFramesTx1_queue_1;
sb_queue_base_SW_SizedEthernetMessage_Impl_1_Recv_t EthernetFramesTx1_recv_queue;
volatile sb_queue_base_SW_SizedEthernetMessage_Impl_1_t *EthernetFramesTx2_queue_1;
sb_queue_base_SW_SizedEthernetMessage_Impl_1_Recv_t EthernetFramesTx2_recv_queue;
volatile sb_queue_base_SW_SizedEthernetMessage_Impl_1_t *EthernetFramesTx3_queue_1;
sb_queue_base_SW_SizedEthernetMessage_Impl_1_Recv_t EthernetFramesTx3_recv_queue;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesRx0_queue_1;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesRx1_queue_1;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesRx2_queue_1;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesRx3_queue_1;

#define PORT_FROM_MON 56

base_SW_SizedEthernetMessage_Impl last_EthernetFramesTx0_payload;

bool get_EthernetFramesTx0(base_SW_SizedEthernetMessage_Impl *data) {
  sb_event_counter_t numDropped;
  base_SW_SizedEthernetMessage_Impl fresh_data;
  bool isFresh = sb_queue_base_SW_SizedEthernetMessage_Impl_1_dequeue((sb_queue_base_SW_SizedEthernetMessage_Impl_1_Recv_t *) &EthernetFramesTx0_recv_queue, &numDropped, &fresh_data);
  if (isFresh) {
    last_EthernetFramesTx0_payload = fresh_data;
  }
  *data = last_EthernetFramesTx0_payload;
  return isFresh;
}

base_SW_SizedEthernetMessage_Impl last_EthernetFramesTx1_payload;

bool get_EthernetFramesTx1(base_SW_SizedEthernetMessage_Impl *data) {
  sb_event_counter_t numDropped;
  base_SW_SizedEthernetMessage_Impl fresh_data;
  bool isFresh = sb_queue_base_SW_SizedEthernetMessage_Impl_1_dequeue((sb_queue_base_SW_SizedEthernetMessage_Impl_1_Recv_t *) &EthernetFramesTx1_recv_queue, &numDropped, &fresh_data);
  if (isFresh) {
    last_EthernetFramesTx1_payload = fresh_data;
  }
  *data = last_EthernetFramesTx1_payload;
  return isFresh;
}

base_SW_SizedEthernetMessage_Impl last_EthernetFramesTx2_payload;

bool get_EthernetFramesTx2(base_SW_SizedEthernetMessage_Impl *data) {
  sb_event_counter_t numDropped;
  base_SW_SizedEthernetMessage_Impl fresh_data;
  bool isFresh = sb_queue_base_SW_SizedEthernetMessage_Impl_1_dequeue((sb_queue_base_SW_SizedEthernetMessage_Impl_1_Recv_t *) &EthernetFramesTx2_recv_queue, &numDropped, &fresh_data);
  if (isFresh) {
    last_EthernetFramesTx2_payload = fresh_data;
  }
  *data = last_EthernetFramesTx2_payload;
  return isFresh;
}

base_SW_SizedEthernetMessage_Impl last_EthernetFramesTx3_payload;

bool get_EthernetFramesTx3(base_SW_SizedEthernetMessage_Impl *data) {
  sb_event_counter_t numDropped;
  base_SW_SizedEthernetMessage_Impl fresh_data;
  bool isFresh = sb_queue_base_SW_SizedEthernetMessage_Impl_1_dequeue((sb_queue_base_SW_SizedEthernetMessage_Impl_1_Recv_t *) &EthernetFramesTx3_recv_queue, &numDropped, &fresh_data);
  if (isFresh) {
    last_EthernetFramesTx3_payload = fresh_data;
  }
  *data = last_EthernetFramesTx3_payload;
  return isFresh;
}

bool put_EthernetFramesRx0(const base_SW_RawEthernetMessage_Impl *data) {
  sb_queue_base_SW_RawEthernetMessage_Impl_1_enqueue((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRx0_queue_1, (base_SW_RawEthernetMessage_Impl *) data);

  return true;
}

bool put_EthernetFramesRx1(const base_SW_RawEthernetMessage_Impl *data) {
  sb_queue_base_SW_RawEthernetMessage_Impl_1_enqueue((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRx1_queue_1, (base_SW_RawEthernetMessage_Impl *) data);

  return true;
}

bool put_EthernetFramesRx2(const base_SW_RawEthernetMessage_Impl *data) {
  sb_queue_base_SW_RawEthernetMessage_Impl_1_enqueue((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRx2_queue_1, (base_SW_RawEthernetMessage_Impl *) data);

  return true;
}

bool put_EthernetFramesRx3(const base_SW_RawEthernetMessage_Impl *data) {
  sb_queue_base_SW_RawEthernetMessage_Impl_1_enqueue((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRx3_queue_1, (base_SW_RawEthernetMessage_Impl *) data);

  return true;
}

void init(void) {
  sb_queue_base_SW_SizedEthernetMessage_Impl_1_Recv_init(&EthernetFramesTx0_recv_queue, (sb_queue_base_SW_SizedEthernetMessage_Impl_1_t *) EthernetFramesTx0_queue_1);

  sb_queue_base_SW_SizedEthernetMessage_Impl_1_Recv_init(&EthernetFramesTx1_recv_queue, (sb_queue_base_SW_SizedEthernetMessage_Impl_1_t *) EthernetFramesTx1_queue_1);

  sb_queue_base_SW_SizedEthernetMessage_Impl_1_Recv_init(&EthernetFramesTx2_recv_queue, (sb_queue_base_SW_SizedEthernetMessage_Impl_1_t *) EthernetFramesTx2_queue_1);

  sb_queue_base_SW_SizedEthernetMessage_Impl_1_Recv_init(&EthernetFramesTx3_recv_queue, (sb_queue_base_SW_SizedEthernetMessage_Impl_1_t *) EthernetFramesTx3_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_init((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRx0_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_init((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRx1_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_init((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRx2_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_init((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRx3_queue_1);

  seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_initialize();
}

void notified(microkit_channel channel) {
  switch (channel) {
    case PORT_FROM_MON:
      seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_timeTriggered();
      break;
  }
}
