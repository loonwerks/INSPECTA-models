#include "operator_interface_oip_oit.h"

// This file will not be overwritten if codegen is rerun

isolette_Isolette_Data_Model_TempWstatus_i lower_desired_tempWstatus = {95, Valid};
isolette_Isolette_Data_Model_TempWstatus_i upper_desired_tempWstatus = {101, Valid};

void operator_interface_oip_oit_initialize(void) {
  printf("%s: operator_interface_oip_oit_initialize invoked\n", microkit_name);

  put_lower_desired_tempWstatus(&lower_desired_tempWstatus);
  put_upper_desired_tempWstatus(&upper_desired_tempWstatus);
}

void operator_interface_oip_oit_timeTriggered(void) {
  printf("%s: operator_interface_oip_oit_timeTriggered invoked\n", microkit_name);

  
}

void operator_interface_oip_oit_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
