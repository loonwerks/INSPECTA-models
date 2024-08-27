#include <consumer_thread_i_consumer_consumer_api.h>
#include <consumer_thread_i_consumer_consumer.h>
#include <ext.h>

// This file will not be overwritten so is safe to edit

static char* component_id = "top_impl_Instance_consumer_consumer";

Unit base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_initialise_(STACK_FRAME_ONLY) {
  DeclNewStackFrame(caller, "consumer_thread_i_consumer_consumer.c", "", "base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_initialise_", 0);

  printf("%s: base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_initialise_ called\n", component_id);

  // example usage of api setters


  /* example usage of api loggers. Commented out as the constructed String may be too long
  api_logInfo__base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer(SF string("Example logInfo"));

  api_logDebug__base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer(SF string("Example logDebug"));

  api_logError__base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer(SF string("Example logError"));
  */
}

Unit base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_finalise_(STACK_FRAME_ONLY) {
  DeclNewStackFrame(caller, "consumer_thread_i_consumer_consumer.c", "", "base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_finalise_", 0);
}

Unit base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_timeTriggered_(STACK_FRAME_ONLY) {
  DeclNewStackFrame(caller, "consumer_thread_i_consumer_consumer.c", "", "base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_timeTriggered_", 0);

  // examples of api getter usage

  S8 t0;
  if(api_get_read_port__base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer(SF &t0)) {
    DeclNewString(read_port_str);
    String__append(SF (String) &read_port_str, string("Received data on data port read_port: "));
    S8_string_(SF (String) &read_port_str, t0);
    api_logInfo__base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer(SF (String) &read_port_str);
  }
}
