#include <stdint.h>
#include <microkit.h>
#include "printf.h"

char* ID = "Consumer";

volatile uint8_t *from_producer_vaddr;

#define PORT_FROM_PERIODIC_DISPATCHER 2

int sum = 0;

void init(void) {
  printf("%s: Init\n", ID);
  sum = 0;
}

void notified(microkit_channel channel) {
  switch(channel) {
    case PORT_FROM_PERIODIC_DISPATCHER: {
      //printf("%s: Received a pace\n", ID); 
      int value = (int) *from_producer_vaddr; 
      sum = sum + value;

      printf("%s: Received %i\n", ID, value);
      printf("%s: %i = %i + %i\n", ID, sum, (sum - value), value);
      break;
    }
  }
}
