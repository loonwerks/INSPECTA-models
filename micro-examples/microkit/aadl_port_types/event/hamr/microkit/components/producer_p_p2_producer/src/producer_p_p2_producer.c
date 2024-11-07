#include "producer_p_p2_producer.h"

void producer_p_p2_producer_initialize(void);
void producer_p_p2_producer_timeTriggered(void);

volatile sb_queue_uint8_t_1_t *write_port_queue_1;

#define PORT_FROM_MON 58

bool put_write_port() {
  uint8_t eventPayload = 0; // always send 0 as the event payload
  uint8_t *data = &eventPayload;
  sb_queue_uint8_t_1_enqueue((sb_queue_uint8_t_1_t *) write_port_queue_1, (uint8_t *) data);

  return true;
}

void init(void) {
  sb_queue_uint8_t_1_init((sb_queue_uint8_t_1_t *) write_port_queue_1);

  producer_p_p2_producer_initialize();
}

void notified(microkit_channel channel) {
  switch (channel) {
    case PORT_FROM_MON:
      producer_p_p2_producer_timeTriggered();
      break;
  }
}
