#include "seL4_LowLevelEthernetDriver_LowLevelEthernetDriver.h"

void seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_initialize(void);
void seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_timeTriggered(void);

volatile sb_queue_base_SW_SizedEthernetMessage_Impl_1_t *EthernetFramesTx_queue_1;
sb_queue_base_SW_SizedEthernetMessage_Impl_1_Recv_t EthernetFramesTx_recv_queue;
volatile sb_queue_base_SW_RawEthernetMessage_Impl_1_t *EthernetFramesRx_queue_1;

#define PORT_FROM_MON 56

base_SW_SizedEthernetMessage_Impl last_EthernetFramesTx_payload;

bool get_EthernetFramesTx(base_SW_SizedEthernetMessage_Impl *data) {
  sb_event_counter_t numDropped;
  base_SW_SizedEthernetMessage_Impl fresh_data;
  bool isFresh = false;
  if (sb_queue_base_SW_SizedEthernetMessage_Impl_1_dequeue((sb_queue_base_SW_SizedEthernetMessage_Impl_1_Recv_t *) &EthernetFramesTx_recv_queue, &numDropped, &fresh_data)) {
    last_EthernetFramesTx_payload = fresh_data;
    isFresh = true;
  }
  *data = last_EthernetFramesTx_payload;
  return isFresh;
}

bool put_EthernetFramesRx(const base_SW_RawEthernetMessage_Impl *data) {
  sb_queue_base_SW_RawEthernetMessage_Impl_1_enqueue((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRx_queue_1, (base_SW_RawEthernetMessage_Impl *) data);

  return true;
}

void init(void) {
  sb_queue_base_SW_SizedEthernetMessage_Impl_1_Recv_init(&EthernetFramesTx_recv_queue, (sb_queue_base_SW_SizedEthernetMessage_Impl_1_t *) EthernetFramesTx_queue_1);

  sb_queue_base_SW_RawEthernetMessage_Impl_1_init((sb_queue_base_SW_RawEthernetMessage_Impl_1_t *) EthernetFramesRx_queue_1);

  seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_initialize();
}

void notified(microkit_channel channel) {
  switch (channel) {
    case PORT_FROM_MON:
      seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_timeTriggered();
      break;
  }
}
