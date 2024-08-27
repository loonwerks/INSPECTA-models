#include <producer_t_i_producer_producer_api.h>
#include <producer_t_i_producer_producer.h>
#include <ext.h>

// This file will not be overwritten so is safe to edit

static char* component_id = "top_impl_Instance_producer_producer";

Unit base_test_event_port_periodic_domains_producer_t_i_producer_producer_initialise_(STACK_FRAME_ONLY) {
  DeclNewStackFrame(caller, "producer_t_i_producer_producer.c", "", "base_test_event_port_periodic_domains_producer_t_i_producer_producer_initialise_", 0);

  printf("%s: base_test_event_port_periodic_domains_producer_t_i_producer_producer_initialise_ called\n", component_id);

  // example usage of api setters

  api_put_emit__base_test_event_port_periodic_domains_producer_t_i_producer_producer(SF_LAST);

  /* example usage of api loggers. Commented out as the constructed String may be too long
  api_logInfo__base_test_event_port_periodic_domains_producer_t_i_producer_producer(SF string("Example logInfo"));

  api_logDebug__base_test_event_port_periodic_domains_producer_t_i_producer_producer(SF string("Example logDebug"));

  api_logError__base_test_event_port_periodic_domains_producer_t_i_producer_producer(SF string("Example logError"));
  */
}

Unit base_test_event_port_periodic_domains_producer_t_i_producer_producer_finalise_(STACK_FRAME_ONLY) {
  DeclNewStackFrame(caller, "producer_t_i_producer_producer.c", "", "base_test_event_port_periodic_domains_producer_t_i_producer_producer_finalise_", 0);
}

Unit base_test_event_port_periodic_domains_producer_t_i_producer_producer_timeTriggered_(STACK_FRAME_ONLY) {
  DeclNewStackFrame(caller, "producer_t_i_producer_producer.c", "", "base_test_event_port_periodic_domains_producer_t_i_producer_producer_timeTriggered_", 0);

}
