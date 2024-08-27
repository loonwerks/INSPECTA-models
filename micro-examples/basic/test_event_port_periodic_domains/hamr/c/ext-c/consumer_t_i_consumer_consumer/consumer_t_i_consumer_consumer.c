#include <consumer_t_i_consumer_consumer_api.h>
#include <consumer_t_i_consumer_consumer.h>
#include <ext.h>

// This file will not be overwritten so is safe to edit

static char* component_id = "top_impl_Instance_consumer_consumer";

Unit base_test_event_port_periodic_domains_consumer_t_i_consumer_consumer_initialise_(STACK_FRAME_ONLY) {
  DeclNewStackFrame(caller, "consumer_t_i_consumer_consumer.c", "", "base_test_event_port_periodic_domains_consumer_t_i_consumer_consumer_initialise_", 0);

  printf("%s: base_test_event_port_periodic_domains_consumer_t_i_consumer_consumer_initialise_ called\n", component_id);

  // example usage of api setters


  /* example usage of api loggers. Commented out as the constructed String may be too long
  api_logInfo__base_test_event_port_periodic_domains_consumer_t_i_consumer_consumer(SF string("Example logInfo"));

  api_logDebug__base_test_event_port_periodic_domains_consumer_t_i_consumer_consumer(SF string("Example logDebug"));

  api_logError__base_test_event_port_periodic_domains_consumer_t_i_consumer_consumer(SF string("Example logError"));
  */
}

Unit base_test_event_port_periodic_domains_consumer_t_i_consumer_consumer_finalise_(STACK_FRAME_ONLY) {
  DeclNewStackFrame(caller, "consumer_t_i_consumer_consumer.c", "", "base_test_event_port_periodic_domains_consumer_t_i_consumer_consumer_finalise_", 0);
}

Unit base_test_event_port_periodic_domains_consumer_t_i_consumer_consumer_timeTriggered_(STACK_FRAME_ONLY) {
  DeclNewStackFrame(caller, "consumer_t_i_consumer_consumer.c", "", "base_test_event_port_periodic_domains_consumer_t_i_consumer_consumer_timeTriggered_", 0);

  // examples of api getter usage

  if(api_get_consume__base_test_event_port_periodic_domains_consumer_t_i_consumer_consumer(SF_LAST )){
    printf("%s: Received event on consume\n", component_id);

    /* alternative using logInfo.  Commented out as the constructed String may be too large
    String consume_str = string("Received event on event port consume");
    api_logInfo__base_test_event_port_periodic_domains_consumer_t_i_consumer_consumer(SF consume_str);
    */
  }
}
