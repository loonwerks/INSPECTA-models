#include "consumer_p_s_consumer.h"

void consumer_p_s_consumer_initialize(void) {
  printf("%s: I'm sporadic so you'll never hear from me again :(\n", microkit_name);
}

void handle_read_port(void) {
  printf("%s: infeasible\n", microkit_name);
}
