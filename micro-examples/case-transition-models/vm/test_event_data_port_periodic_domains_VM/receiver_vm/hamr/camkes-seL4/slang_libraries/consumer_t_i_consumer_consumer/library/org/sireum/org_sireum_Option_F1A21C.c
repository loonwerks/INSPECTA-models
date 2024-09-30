#include <all.h>

// Option[base.test_event_data_port_periodic_domains.consumer_t_i_Operational_Api]

B Option_F1A21C__is(STACK_FRAME void *this) {
  switch(((Type) this)->type) {
    case TNone_539999: return T;
    case TSome_2159FF: return T;
    default: return F;
  }
}

Option_F1A21C Option_F1A21C__as(STACK_FRAME void *this) {
  switch(((Type) this)->type) {
    case TNone_539999: break;
    case TSome_2159FF: break;
    default:
      fprintf(stderr, "Invalid cast from %s to Option[base.test_event_data_port_periodic_domains.consumer_t_i_Operational_Api].", TYPE_string_(this));
      sfAbortImpl(CALLER "");
  }
  return (Option_F1A21C) this;
}

void Option_F1A21C_string_(STACK_FRAME String result, Option_F1A21C this);

void Option_F1A21C_get_(STACK_FRAME base_test_event_data_port_periodic_domains_consumer_t_i_Operational_Api result, Option_F1A21C this) {
  switch (this->type) {
    case TNone_539999: None_539999_get_(CALLER (base_test_event_data_port_periodic_domains_consumer_t_i_Operational_Api) result, (None_539999) this); return;
    case TSome_2159FF: Some_2159FF_get_(CALLER (base_test_event_data_port_periodic_domains_consumer_t_i_Operational_Api) result, (Some_2159FF) this); return;
    default: fprintf(stderr, "Infeasible TYPE: %s.\n", TYPE_string_(this)); exit(1);
  }
}