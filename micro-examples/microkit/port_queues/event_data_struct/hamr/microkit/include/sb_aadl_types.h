#ifndef SB_AADL_TYPES_H
#define SB_AADL_TYPES_H

#include <stdint.h>

typedef struct base_event_data_port_port_queues_struct_i base_event_data_port_port_queues_struct_i;

#define base_event_data_port_port_queues_ArrayOfInts_SIZE 40
#define base_event_data_port_port_queues_ArrayOfInts_DIM 10

typedef int32_t base_event_data_port_port_queues_ArrayOfInts [base_event_data_port_port_queues_ArrayOfInts_DIM];

typedef
  struct base_event_data_port_port_queues_ArrayOfInts_container{
    base_event_data_port_port_queues_ArrayOfInts f;
  } base_event_data_port_port_queues_ArrayOfInts_container;

struct base_event_data_port_port_queues_struct_i {
  int32_t size;
  base_event_data_port_port_queues_ArrayOfInts elements;
};

#endif