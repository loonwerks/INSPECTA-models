#include <producer_t_i_producer_producer_api.h>
#include <producer_t_i_producer_producer.h>
#include <ext.h>

// This file will not be overwritten so is safe to edit

static char* component_id = "top_impl_Instance_producer_producer";
uint8_t value = 0;

Unit base_test_event_data_port_periodic_domains_producer_t_i_producer_producer_initialise_(STACK_FRAME_ONLY) {
  DeclNewStackFrame(caller, "producer_t_i_producer_producer.c", "", "base_test_event_data_port_periodic_domains_producer_t_i_producer_producer_initialise_", 0);

  printf("%s: base_test_event_data_port_periodic_domains_producer_t_i_producer_producer_initialise_ called\n", component_id);

  // example usage of api setters

  uint8_t t0[numBytes_S8];
  byte_array_default(SF t0, numBits_S8, numBytes_S8);
  api_put_write_port__base_test_event_data_port_periodic_domains_producer_t_i_producer_producer(SF numBits_S8, t0);

  /* example usage of api loggers. Commented out as the constructed String may be too long
  api_logInfo__base_test_event_data_port_periodic_domains_producer_t_i_producer_producer(SF string("Example logInfo"));

  api_logDebug__base_test_event_data_port_periodic_domains_producer_t_i_producer_producer(SF string("Example logDebug"));

  api_logError__base_test_event_data_port_periodic_domains_producer_t_i_producer_producer(SF string("Example logError"));
  */
}

Unit base_test_event_data_port_periodic_domains_producer_t_i_producer_producer_finalise_(STACK_FRAME_ONLY) {
  DeclNewStackFrame(caller, "producer_t_i_producer_producer.c", "", "base_test_event_data_port_periodic_domains_producer_t_i_producer_producer_finalise_", 0);
}

Unit base_test_event_data_port_periodic_domains_producer_t_i_producer_producer_timeTriggered_(STACK_FRAME_ONLY) {
  DeclNewStackFrame(caller, "producer_t_i_producer_producer.c", "", "base_test_event_data_port_periodic_domains_producer_t_i_producer_producer_timeTriggered_", 0);

  uint8_t t0[numBytes_S8];
  t0[0] = value++;
  api_put_write_port__base_test_event_data_port_periodic_domains_producer_t_i_producer_producer(SF numBits_S8, t0);
}
