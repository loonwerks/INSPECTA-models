#include "tsp_tst.h"

// This file will not be overwritten if HAMR codegen is rerun

void tsp_tst_initialize(void) {
  printf("%s: tsp_tst_initialize invoked\n", microkit_name);
}

void tsp_tst_timeTriggered(void) {
  printf("%s: tsp_tst_timeTriggered invoked\n", microkit_name);
}

void tsp_tst_notify(microkit_channel channel) {
  // this method is called when the monitor does not handle the passed in channel
  switch (channel) {
    default:
      printf("%s: Unexpected channel %d\n", microkit_name, channel);
  }
}
