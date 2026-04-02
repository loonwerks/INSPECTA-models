#include "temperature_sensor_cpi_thermostat.h"
#include <stdbool.h>
#include <stdint.h>

// This file will not be overwritten if codegen is rerun

// alarm range : [97, 101]
int32_t low = 96;
int32_t high = 102;

int32_t delta = -1;
int32_t lastTemp = 97;

int32_t frame = 0;

void send(void) {
  struct Isolette_Data_Model_TempWstatus_i value = { lastTemp, Valid };
  put_current_tempWstatus(&value);
}

void temperature_sensor_cpi_thermostat_initialize(void) {
  printf("%s: temperature_sensor_cpi_thermostat_initialize invoked\n", microkit_name);
  send();
}

void temperature_sensor_cpi_thermostat_timeTriggered(void) {
  //printf("%s: temperature_sensor_cpi_thermostat_timeTriggered invoked\n", microkit_name);
  printf("####### FRAME %d #######\n", frame);

  if (frame % 2 == 0) {
    lastTemp = lastTemp + delta;

    if (lastTemp < low || lastTemp > high) {
      delta = delta * -1;
    }
  }
  send();

  frame = frame + 1;
}

void temperature_sensor_cpi_thermostat_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
