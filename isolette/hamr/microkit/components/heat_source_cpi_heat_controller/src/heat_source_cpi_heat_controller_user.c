#include "heat_source_cpi_heat_controller.h"

// This file will not be overwritten if codegen is rerun

isolette_Isolette_Data_Model_On_Off_Type heater_state = Off;

void heat_source_cpi_heat_controller_initialize(void) {
  printf("%s: heat_source_cpi_heat_controller_initialize invoked\n", microkit_name);

  heater_state = Off;

  // must initialize outgoing data ports during initialization phase
  put_heat_out((const isolette_Isolette_Environment_Heat_Type*) &heater_state);
}

void heat_source_cpi_heat_controller_timeTriggered(void) {
  //printf("%s: heat_source_cpi_heat_controller_timeTriggered invoked\n", microkit_name);

  isolette_Isolette_Data_Model_On_Off_Type currCommand;
  get_heat_control(&currCommand);
  if (heater_state != currCommand) {
    heater_state = currCommand;
    printf("%s: Received command: %s\n", microkit_name, heater_state == Off ? "Off" : "On");
  }
}

void heat_source_cpi_heat_controller_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
