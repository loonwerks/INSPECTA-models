#include <camkes.h>
#include <stdio.h>
#include <sb_types.h>
#include <sb_producer_t_i.h>

void init(const int64_t *in_arg) {
  printf("[%s] called\n", get_instance_name());
}

void compute(const int64_t *in_arg) {
  printf("---------------------------------------\n");
  if (sb_emit_enqueue() ) {
    printf("[%s] Sent event\n", get_instance_name());
  } else {
    printf("[%s] Unable to send\n", get_instance_name());
  }
}