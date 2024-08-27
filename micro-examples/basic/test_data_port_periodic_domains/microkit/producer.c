#include <stdint.h>
#include <microkit.h>
#include "printf.h"
#include <string.h>

char* ID = "Producer";

volatile uint8_t *to_consumer_vaddr;

#define PORT_FROM_PERIODIC_DISPATCHER 1

int counter = 0;

void init(void) {
  printf("%s: Init\n", ID);
  counter = 0;
  *((int*) to_consumer_vaddr) = counter;
}

void notified(microkit_channel channel) {
  switch(channel) {
    case PORT_FROM_PERIODIC_DISPATCHER:
      //printf("%s: Received a pace\n", ID);  

      *((int*) to_consumer_vaddr) = ++counter;

      printf("%s: Sent %i \n", ID, counter);

      break;
  }

}
