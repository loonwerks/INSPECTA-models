#include <stdint.h>
#include <microkit.h>
#include "printf.h"

char* ID = "end_of_schedule_ping";

#define PORT_FROM_PERIODIC_DISPATCHER 3

#define PORT_TO_PERIODIC_DISPATCHER 4

void init(void) {
  printf("%s: Init\n", ID);
}

void notified(microkit_channel channel) {
  switch(channel) {
    case PORT_FROM_PERIODIC_DISPATCHER:
      //printf("%s: Received ping\n", ID);  
      microkit_notify(PORT_TO_PERIODIC_DISPATCHER);
    break;
  }
}