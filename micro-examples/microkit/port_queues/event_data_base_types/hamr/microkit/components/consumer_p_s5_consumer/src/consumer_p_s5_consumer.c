#include "consumer_p_s5_consumer.h"

void consumer_p_s5_consumer_initialize(void);
void handle_read_port(void);

volatile sb_queue_int8_t_5_t *read_port_queue_5;
sb_queue_int8_t_5_Recv_t read_port_recv_queue;

#define PORT_FROM_MON 54

bool read_port_is_empty(void) {
  return sb_queue_int8_t_5_is_empty(&read_port_recv_queue);
}

bool get_read_port_poll(sb_event_counter_t *numDropped, int8_t *data) {
  return sb_queue_int8_t_5_dequeue((sb_queue_int8_t_5_Recv_t *) &read_port_recv_queue, numDropped, data);
}

bool get_read_port(int8_t *data) {
  sb_event_counter_t numDropped;
  return get_read_port_poll (&numDropped, data);
}

void init(void) {
  sb_queue_int8_t_5_Recv_init(&read_port_recv_queue, (sb_queue_int8_t_5_t *) read_port_queue_5);

  consumer_p_s5_consumer_initialize();
}

void notified(microkit_channel channel) {
  switch (channel) {
    case PORT_FROM_MON:
      if (!read_port_is_empty()) {
        handle_read_port();
      }
      break;
  }
}
