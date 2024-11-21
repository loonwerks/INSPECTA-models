#include <printf.h>
#include <stdint.h>
#include <microkit.h>
#include <sb_types.h>

bool read_port1_is_empty(void);
bool get_read_port1_poll(sb_event_counter_t *numDropped, base_event_data_2_prod_2_cons_ArrayOfStruct *data);
bool get_read_port1(base_event_data_2_prod_2_cons_ArrayOfStruct *data);
bool read_port2_is_empty(void);
bool get_read_port2_poll(sb_event_counter_t *numDropped, base_event_data_2_prod_2_cons_ArrayOfStruct *data);
bool get_read_port2(base_event_data_2_prod_2_cons_ArrayOfStruct *data);
