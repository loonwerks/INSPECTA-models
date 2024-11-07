#include "producer_p_p_producer.h"

void producer_p_p_producer_initialize(void);
void producer_p_p_producer_timeTriggered(void);

volatile sb_queue_int8_t_1_t *write_port_queue_1;

#define PORT_FROM_MON 60

bool put_write_port(const int8_t *data) {
  sb_queue_int8_t_1_enqueue((sb_queue_int8_t_1_t *) write_port_queue_1, (int8_t *) data);

  return true;
}

void init(void) {
  sb_queue_int8_t_1_init((sb_queue_int8_t_1_t *) write_port_queue_1);

  producer_p_p_producer_initialize();
}

void notified(microkit_channel channel) {
  switch (channel) {
    case PORT_FROM_MON:
      producer_p_p_producer_timeTriggered();
      break;
  }
}
