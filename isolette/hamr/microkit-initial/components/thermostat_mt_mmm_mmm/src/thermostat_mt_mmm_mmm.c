#include "thermostat_mt_mmm_mmm.h"

// Do not edit this file as it will be overwritten if codegen is rerun

void thermostat_mt_mmm_mmm_initialize(void);
void thermostat_mt_mmm_mmm_notify(microkit_channel channel);
void thermostat_mt_mmm_mmm_timeTriggered(void);

volatile sb_queue_base_Isolette_Data_Model_Failure_Flag_i_1_t *interface_failure_queue_1;
sb_queue_base_Isolette_Data_Model_Failure_Flag_i_1_Recv_t interface_failure_recv_queue;
volatile sb_queue_base_Isolette_Data_Model_Monitor_Mode_Type_1_t *monitor_mode_queue_1;
volatile sb_queue_base_Isolette_Data_Model_Failure_Flag_i_1_t *internal_failure_queue_1;
sb_queue_base_Isolette_Data_Model_Failure_Flag_i_1_Recv_t internal_failure_recv_queue;
volatile sb_queue_base_Isolette_Data_Model_TempWstatus_i_1_t *current_tempWstatus_queue_1;
sb_queue_base_Isolette_Data_Model_TempWstatus_i_1_Recv_t current_tempWstatus_recv_queue;

#define PORT_FROM_MON 48

base_Isolette_Data_Model_Failure_Flag_i last_interface_failure_payload;

bool get_interface_failure(base_Isolette_Data_Model_Failure_Flag_i *data) {
  sb_event_counter_t numDropped;
  base_Isolette_Data_Model_Failure_Flag_i fresh_data;
  bool isFresh = sb_queue_base_Isolette_Data_Model_Failure_Flag_i_1_dequeue((sb_queue_base_Isolette_Data_Model_Failure_Flag_i_1_Recv_t *) &interface_failure_recv_queue, &numDropped, &fresh_data);
  if (isFresh) {
    last_interface_failure_payload = fresh_data;
  }
  *data = last_interface_failure_payload;
  return isFresh;
}

bool put_monitor_mode(const base_Isolette_Data_Model_Monitor_Mode_Type *data) {
  sb_queue_base_Isolette_Data_Model_Monitor_Mode_Type_1_enqueue((sb_queue_base_Isolette_Data_Model_Monitor_Mode_Type_1_t *) monitor_mode_queue_1, (base_Isolette_Data_Model_Monitor_Mode_Type *) data);

  return true;
}

base_Isolette_Data_Model_Failure_Flag_i last_internal_failure_payload;

bool get_internal_failure(base_Isolette_Data_Model_Failure_Flag_i *data) {
  sb_event_counter_t numDropped;
  base_Isolette_Data_Model_Failure_Flag_i fresh_data;
  bool isFresh = sb_queue_base_Isolette_Data_Model_Failure_Flag_i_1_dequeue((sb_queue_base_Isolette_Data_Model_Failure_Flag_i_1_Recv_t *) &internal_failure_recv_queue, &numDropped, &fresh_data);
  if (isFresh) {
    last_internal_failure_payload = fresh_data;
  }
  *data = last_internal_failure_payload;
  return isFresh;
}

base_Isolette_Data_Model_TempWstatus_i last_current_tempWstatus_payload;

bool get_current_tempWstatus(base_Isolette_Data_Model_TempWstatus_i *data) {
  sb_event_counter_t numDropped;
  base_Isolette_Data_Model_TempWstatus_i fresh_data;
  bool isFresh = sb_queue_base_Isolette_Data_Model_TempWstatus_i_1_dequeue((sb_queue_base_Isolette_Data_Model_TempWstatus_i_1_Recv_t *) &current_tempWstatus_recv_queue, &numDropped, &fresh_data);
  if (isFresh) {
    last_current_tempWstatus_payload = fresh_data;
  }
  *data = last_current_tempWstatus_payload;
  return isFresh;
}

void init(void) {
  sb_queue_base_Isolette_Data_Model_Failure_Flag_i_1_Recv_init(&interface_failure_recv_queue, (sb_queue_base_Isolette_Data_Model_Failure_Flag_i_1_t *) interface_failure_queue_1);

  sb_queue_base_Isolette_Data_Model_Monitor_Mode_Type_1_init((sb_queue_base_Isolette_Data_Model_Monitor_Mode_Type_1_t *) monitor_mode_queue_1);

  sb_queue_base_Isolette_Data_Model_Failure_Flag_i_1_Recv_init(&internal_failure_recv_queue, (sb_queue_base_Isolette_Data_Model_Failure_Flag_i_1_t *) internal_failure_queue_1);

  sb_queue_base_Isolette_Data_Model_TempWstatus_i_1_Recv_init(&current_tempWstatus_recv_queue, (sb_queue_base_Isolette_Data_Model_TempWstatus_i_1_t *) current_tempWstatus_queue_1);

  thermostat_mt_mmm_mmm_initialize();
}

void notified(microkit_channel channel) {
  switch (channel) {
    case PORT_FROM_MON:
      thermostat_mt_mmm_mmm_timeTriggered();
      break;
    default:
      thermostat_mt_mmm_mmm_notify(channel);
  }
}