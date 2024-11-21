#include <printf.h>
#include <stdint.h>
#include <microkit.h>
#include <sb_types.h>

bool read_port_is_empty(void);
bool get_read_port_poll(sb_event_counter_t *numDropped, int8_t *data);
bool get_read_port(int8_t *data);
