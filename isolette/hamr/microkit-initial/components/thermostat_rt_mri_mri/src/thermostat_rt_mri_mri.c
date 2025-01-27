#include "thermostat_rt_mri_mri.h"

// Do not edit this file as it will be overwritten if codegen is rerun

void thermostat_rt_mri_mri_initialize(void);
void thermostat_rt_mri_mri_notify(microkit_channel channel);
void thermostat_rt_mri_mri_timeTriggered(void);

volatile sb_queue_base_Isolette_Data_Model_Temp_i_1_t *upper_desired_temp_queue_1;
volatile sb_queue_base_Isolette_Data_Model_Temp_i_1_t *lower_desired_temp_queue_1;
volatile sb_queue_base_Isolette_Data_Model_Temp_i_1_t *displayed_temp_queue_1;
volatile sb_queue_base_Isolette_Data_Model_Status_Type_1_t *regulator_status_queue_1;
volatile sb_queue_base_Isolette_Data_Model_Failure_Flag_i_1_t *interface_failure_queue_1;
volatile sb_queue_base_Isolette_Data_Model_Regulator_Mode_Type_1_t *regulator_mode_queue_1;
sb_queue_base_Isolette_Data_Model_Regulator_Mode_Type_1_Recv_t regulator_mode_recv_queue;
volatile sb_queue_base_Isolette_Data_Model_TempWstatus_i_1_t *lower_desired_tempWstatus_queue_1;
sb_queue_base_Isolette_Data_Model_TempWstatus_i_1_Recv_t lower_desired_tempWstatus_recv_queue;
volatile sb_queue_base_Isolette_Data_Model_TempWstatus_i_1_t *upper_desired_tempWstatus_queue_1;
sb_queue_base_Isolette_Data_Model_TempWstatus_i_1_Recv_t upper_desired_tempWstatus_recv_queue;
volatile sb_queue_base_Isolette_Data_Model_TempWstatus_i_1_t *current_tempWstatus_queue_1;
sb_queue_base_Isolette_Data_Model_TempWstatus_i_1_Recv_t current_tempWstatus_recv_queue;

#define PORT_FROM_MON 60

bool put_upper_desired_temp(const base_Isolette_Data_Model_Temp_i *data) {
  sb_queue_base_Isolette_Data_Model_Temp_i_1_enqueue((sb_queue_base_Isolette_Data_Model_Temp_i_1_t *) upper_desired_temp_queue_1, (base_Isolette_Data_Model_Temp_i *) data);

  return true;
}

bool put_lower_desired_temp(const base_Isolette_Data_Model_Temp_i *data) {
  sb_queue_base_Isolette_Data_Model_Temp_i_1_enqueue((sb_queue_base_Isolette_Data_Model_Temp_i_1_t *) lower_desired_temp_queue_1, (base_Isolette_Data_Model_Temp_i *) data);

  return true;
}

bool put_displayed_temp(const base_Isolette_Data_Model_Temp_i *data) {
  sb_queue_base_Isolette_Data_Model_Temp_i_1_enqueue((sb_queue_base_Isolette_Data_Model_Temp_i_1_t *) displayed_temp_queue_1, (base_Isolette_Data_Model_Temp_i *) data);

  return true;
}

bool put_regulator_status(const base_Isolette_Data_Model_Status_Type *data) {
  sb_queue_base_Isolette_Data_Model_Status_Type_1_enqueue((sb_queue_base_Isolette_Data_Model_Status_Type_1_t *) regulator_status_queue_1, (base_Isolette_Data_Model_Status_Type *) data);

  return true;
}

bool put_interface_failure(const base_Isolette_Data_Model_Failure_Flag_i *data) {
  sb_queue_base_Isolette_Data_Model_Failure_Flag_i_1_enqueue((sb_queue_base_Isolette_Data_Model_Failure_Flag_i_1_t *) interface_failure_queue_1, (base_Isolette_Data_Model_Failure_Flag_i *) data);

  return true;
}

base_Isolette_Data_Model_Regulator_Mode_Type last_regulator_mode_payload;

bool get_regulator_mode(base_Isolette_Data_Model_Regulator_Mode_Type *data) {
  sb_event_counter_t numDropped;
  base_Isolette_Data_Model_Regulator_Mode_Type fresh_data;
  bool isFresh = sb_queue_base_Isolette_Data_Model_Regulator_Mode_Type_1_dequeue((sb_queue_base_Isolette_Data_Model_Regulator_Mode_Type_1_Recv_t *) &regulator_mode_recv_queue, &numDropped, &fresh_data);
  if (isFresh) {
    last_regulator_mode_payload = fresh_data;
  }
  *data = last_regulator_mode_payload;
  return isFresh;
}

base_Isolette_Data_Model_TempWstatus_i last_lower_desired_tempWstatus_payload;

bool get_lower_desired_tempWstatus(base_Isolette_Data_Model_TempWstatus_i *data) {
  sb_event_counter_t numDropped;
  base_Isolette_Data_Model_TempWstatus_i fresh_data;
  bool isFresh = sb_queue_base_Isolette_Data_Model_TempWstatus_i_1_dequeue((sb_queue_base_Isolette_Data_Model_TempWstatus_i_1_Recv_t *) &lower_desired_tempWstatus_recv_queue, &numDropped, &fresh_data);
  if (isFresh) {
    last_lower_desired_tempWstatus_payload = fresh_data;
  }
  *data = last_lower_desired_tempWstatus_payload;
  return isFresh;
}

base_Isolette_Data_Model_TempWstatus_i last_upper_desired_tempWstatus_payload;

bool get_upper_desired_tempWstatus(base_Isolette_Data_Model_TempWstatus_i *data) {
  sb_event_counter_t numDropped;
  base_Isolette_Data_Model_TempWstatus_i fresh_data;
  bool isFresh = sb_queue_base_Isolette_Data_Model_TempWstatus_i_1_dequeue((sb_queue_base_Isolette_Data_Model_TempWstatus_i_1_Recv_t *) &upper_desired_tempWstatus_recv_queue, &numDropped, &fresh_data);
  if (isFresh) {
    last_upper_desired_tempWstatus_payload = fresh_data;
  }
  *data = last_upper_desired_tempWstatus_payload;
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
  sb_queue_base_Isolette_Data_Model_Temp_i_1_init((sb_queue_base_Isolette_Data_Model_Temp_i_1_t *) upper_desired_temp_queue_1);

  sb_queue_base_Isolette_Data_Model_Temp_i_1_init((sb_queue_base_Isolette_Data_Model_Temp_i_1_t *) lower_desired_temp_queue_1);

  sb_queue_base_Isolette_Data_Model_Temp_i_1_init((sb_queue_base_Isolette_Data_Model_Temp_i_1_t *) displayed_temp_queue_1);

  sb_queue_base_Isolette_Data_Model_Status_Type_1_init((sb_queue_base_Isolette_Data_Model_Status_Type_1_t *) regulator_status_queue_1);

  sb_queue_base_Isolette_Data_Model_Failure_Flag_i_1_init((sb_queue_base_Isolette_Data_Model_Failure_Flag_i_1_t *) interface_failure_queue_1);

  sb_queue_base_Isolette_Data_Model_Regulator_Mode_Type_1_Recv_init(&regulator_mode_recv_queue, (sb_queue_base_Isolette_Data_Model_Regulator_Mode_Type_1_t *) regulator_mode_queue_1);

  sb_queue_base_Isolette_Data_Model_TempWstatus_i_1_Recv_init(&lower_desired_tempWstatus_recv_queue, (sb_queue_base_Isolette_Data_Model_TempWstatus_i_1_t *) lower_desired_tempWstatus_queue_1);

  sb_queue_base_Isolette_Data_Model_TempWstatus_i_1_Recv_init(&upper_desired_tempWstatus_recv_queue, (sb_queue_base_Isolette_Data_Model_TempWstatus_i_1_t *) upper_desired_tempWstatus_queue_1);

  sb_queue_base_Isolette_Data_Model_TempWstatus_i_1_Recv_init(&current_tempWstatus_recv_queue, (sb_queue_base_Isolette_Data_Model_TempWstatus_i_1_t *) current_tempWstatus_queue_1);

  thermostat_rt_mri_mri_initialize();
}

void notified(microkit_channel channel) {
  switch (channel) {
    case PORT_FROM_MON:
      thermostat_rt_mri_mri_timeTriggered();
      break;
    default:
      thermostat_rt_mri_mri_notify(channel);
  }
}
