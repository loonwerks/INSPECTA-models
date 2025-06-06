#include "thermostat_mt_ma_ma.h"

// Do not edit this file as it will be overwritten if codegen is rerun

void thermostat_mt_ma_ma_initialize(void);
void thermostat_mt_ma_ma_notify(microkit_channel channel);
void thermostat_mt_ma_ma_timeTriggered(void);

volatile sb_queue_Isolette_Data_Model_Temp_i_1_t *upper_alarm_temp_queue_1;
sb_queue_Isolette_Data_Model_Temp_i_1_Recv_t upper_alarm_temp_recv_queue;
volatile sb_queue_Isolette_Data_Model_Temp_i_1_t *lower_alarm_temp_queue_1;
sb_queue_Isolette_Data_Model_Temp_i_1_Recv_t lower_alarm_temp_recv_queue;
volatile sb_queue_Isolette_Data_Model_On_Off_1_t *alarm_control_queue_1;
volatile sb_queue_Isolette_Data_Model_Monitor_Mode_1_t *monitor_mode_queue_1;
sb_queue_Isolette_Data_Model_Monitor_Mode_1_Recv_t monitor_mode_recv_queue;
volatile sb_queue_Isolette_Data_Model_TempWstatus_i_1_t *current_tempWstatus_queue_1;
sb_queue_Isolette_Data_Model_TempWstatus_i_1_Recv_t current_tempWstatus_recv_queue;

#define PORT_FROM_MON 50

Isolette_Data_Model_Temp_i last_upper_alarm_temp_payload;

bool get_upper_alarm_temp(Isolette_Data_Model_Temp_i *data) {
  sb_event_counter_t numDropped;
  Isolette_Data_Model_Temp_i fresh_data;
  bool isFresh = sb_queue_Isolette_Data_Model_Temp_i_1_dequeue((sb_queue_Isolette_Data_Model_Temp_i_1_Recv_t *) &upper_alarm_temp_recv_queue, &numDropped, &fresh_data);
  if (isFresh) {
    last_upper_alarm_temp_payload = fresh_data;
  }
  *data = last_upper_alarm_temp_payload;
  return isFresh;
}

Isolette_Data_Model_Temp_i last_lower_alarm_temp_payload;

bool get_lower_alarm_temp(Isolette_Data_Model_Temp_i *data) {
  sb_event_counter_t numDropped;
  Isolette_Data_Model_Temp_i fresh_data;
  bool isFresh = sb_queue_Isolette_Data_Model_Temp_i_1_dequeue((sb_queue_Isolette_Data_Model_Temp_i_1_Recv_t *) &lower_alarm_temp_recv_queue, &numDropped, &fresh_data);
  if (isFresh) {
    last_lower_alarm_temp_payload = fresh_data;
  }
  *data = last_lower_alarm_temp_payload;
  return isFresh;
}

bool put_alarm_control(const Isolette_Data_Model_On_Off *data) {
  sb_queue_Isolette_Data_Model_On_Off_1_enqueue((sb_queue_Isolette_Data_Model_On_Off_1_t *) alarm_control_queue_1, (Isolette_Data_Model_On_Off *) data);

  return true;
}

Isolette_Data_Model_Monitor_Mode last_monitor_mode_payload;

bool get_monitor_mode(Isolette_Data_Model_Monitor_Mode *data) {
  sb_event_counter_t numDropped;
  Isolette_Data_Model_Monitor_Mode fresh_data;
  bool isFresh = sb_queue_Isolette_Data_Model_Monitor_Mode_1_dequeue((sb_queue_Isolette_Data_Model_Monitor_Mode_1_Recv_t *) &monitor_mode_recv_queue, &numDropped, &fresh_data);
  if (isFresh) {
    last_monitor_mode_payload = fresh_data;
  }
  *data = last_monitor_mode_payload;
  return isFresh;
}

Isolette_Data_Model_TempWstatus_i last_current_tempWstatus_payload;

bool get_current_tempWstatus(Isolette_Data_Model_TempWstatus_i *data) {
  sb_event_counter_t numDropped;
  Isolette_Data_Model_TempWstatus_i fresh_data;
  bool isFresh = sb_queue_Isolette_Data_Model_TempWstatus_i_1_dequeue((sb_queue_Isolette_Data_Model_TempWstatus_i_1_Recv_t *) &current_tempWstatus_recv_queue, &numDropped, &fresh_data);
  if (isFresh) {
    last_current_tempWstatus_payload = fresh_data;
  }
  *data = last_current_tempWstatus_payload;
  return isFresh;
}

void init(void) {
  sb_queue_Isolette_Data_Model_Temp_i_1_Recv_init(&upper_alarm_temp_recv_queue, (sb_queue_Isolette_Data_Model_Temp_i_1_t *) upper_alarm_temp_queue_1);

  sb_queue_Isolette_Data_Model_Temp_i_1_Recv_init(&lower_alarm_temp_recv_queue, (sb_queue_Isolette_Data_Model_Temp_i_1_t *) lower_alarm_temp_queue_1);

  sb_queue_Isolette_Data_Model_On_Off_1_init((sb_queue_Isolette_Data_Model_On_Off_1_t *) alarm_control_queue_1);

  sb_queue_Isolette_Data_Model_Monitor_Mode_1_Recv_init(&monitor_mode_recv_queue, (sb_queue_Isolette_Data_Model_Monitor_Mode_1_t *) monitor_mode_queue_1);

  sb_queue_Isolette_Data_Model_TempWstatus_i_1_Recv_init(&current_tempWstatus_recv_queue, (sb_queue_Isolette_Data_Model_TempWstatus_i_1_t *) current_tempWstatus_queue_1);

  thermostat_mt_ma_ma_initialize();
}

void notified(microkit_channel channel) {
  switch (channel) {
    case PORT_FROM_MON:
      thermostat_mt_ma_ma_timeTriggered();
      break;
    default:
      thermostat_mt_ma_ma_notify(channel);
  }
}
