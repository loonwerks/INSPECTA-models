#include "heat_source_cpi_heat_controller.h"

// This file will not be overwritten if codegen is rerun

void heat_source_cpi_heat_controller_initialize(void) {
  printf("%s: heat_source_cpi_heat_controller_initialize invoked\n", microkit_name);
}

void heat_source_cpi_heat_controller_timeTriggered(void) {
  printf("%s: heat_source_cpi_heat_controller_timeTriggered invoked\n", microkit_name);
}

void heat_source_cpi_heat_controller_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
