#include "top_sub_C_sub_c.h"

// This file will not be overwritten if HAMR codegen is rerun

void top_sub_C_sub_c_initialize(void) {
  printf("%s: top_sub_C_sub_c_initialize invoked\n", microkit_name);
}

void top_sub_C_sub_c_timeTriggered(void) {
  printf("%s: top_sub_C_sub_c_timeTriggered invoked\n", microkit_name);
}

void top_sub_C_sub_c_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
