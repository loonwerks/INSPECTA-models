#include "temperature_sensor_cpi_thermostat.h"

// This file will not be overwritten if codegen is rerun

// lower desired temp [97..99]
// upper desired temp [98..100]
// lower alarm temp 97
// upper alarm temp 101
const float DEFAULT_CURRENT_TEMPERATURE = 98.0;
const float DEFAULT_LOWER_ALARM_TEMPERATURE = 97.0;
const float DEFAULT_UPPER_ALARM_TEMPERATURE = 101.0;

isolette_Isolette_Data_Model_PhysicalTemp_i lastTemperature = { DEFAULT_CURRENT_TEMPERATURE };

isolette_Isolette_Data_Model_On_Off_Type assumed_heater_state = Off;

const float delta = 1;

int frame = 0;
void temperature_sensor_cpi_thermostat_initialize(void) {
  printf("%s: temperature_sensor_cpi_thermostat_initialize invoked\n", microkit_name);

  isolette_Isolette_Data_Model_Temp_i defaultTemp = {DEFAULT_CURRENT_TEMPERATURE};
  isolette_Isolette_Data_Model_TempWstatus_i defaultTempWstatus = {defaultTemp.degrees, Valid};
  put_current_tempWstatus(&defaultTempWstatus);
}

void temperature_sensor_cpi_thermostat_timeTriggered(void) {
  //printf("%s: temperature_sensor_cpi_thermostat_timeTriggered invoked\n", microkit_name);
  
  // the temp sensor is the first component scheduled per frame so have it emit a 
  printf("####### FRAME %d #######\n", frame++);

  // temp sensor's air port is not connected so we'll instead simulate it (the slang
  // implementation uses an injection service for this)

  // isolette_Isolette_Data_Model_PhysicalTemp_i t;
  // get_air(&t);

  // heat_source is in a different sel4 partition so we can't "query" it to determine
  // if it's on or off.  So we'll assume it's initially off and then turns on when the
  // temp rises above the setpoint

  lastTemperature.degrees = lastTemperature.degrees + (assumed_heater_state == Onn ? delta : -1 * delta);
  
  isolette_Isolette_Data_Model_PhysicalTemp_i t = lastTemperature; // get_air

  isolette_Isolette_Data_Model_TempWstatus_i current_tempWstatus = { t.degrees, Valid };

  put_current_tempWstatus((const isolette_Isolette_Data_Model_TempWstatus_i *) & current_tempWstatus);

  if (assumed_heater_state == Onn && lastTemperature.degrees > DEFAULT_UPPER_ALARM_TEMPERATURE) {
    assumed_heater_state = Off;
  }
  if (assumed_heater_state == Off && lastTemperature.degrees < DEFAULT_LOWER_ALARM_TEMPERATURE) {
    assumed_heater_state = Onn;
  }
}

void temperature_sensor_cpi_thermostat_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
