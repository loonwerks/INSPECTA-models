#include "temperature_sensor_cpi_thermostat.h"

// This file will not be overwritten if codegen is rerun

isolette_Isolette_Data_Model_TempWstatus_i temp = {97, Valid};
uint32_t delta = 1;
void temperature_sensor_cpi_thermostat_initialize(void) {
  printf("%s: temperature_sensor_cpi_thermostat_initialize invoked\n", microkit_name);

  put_current_tempWstatus(&temp);
}

void temperature_sensor_cpi_thermostat_timeTriggered(void) {
  printf("%s: temperature_sensor_cpi_thermostat_timeTriggered invoked\n", microkit_name);

  if (temp.degrees > 101) {
    delta = -1;
  } else if (temp.degrees < 95) {
    delta = 1;
  }
  temp.degrees = temp.degrees + delta;

  put_current_tempWstatus(&temp);
}

void temperature_sensor_cpi_thermostat_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
