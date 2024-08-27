#include <all.h>

// Option[base.test_event_data_port_periodic_domains.consumer_t_i_Initialization_Api]

B Option_0CB775__is(STACK_FRAME void *this) {
  switch(((Type) this)->type) {
    case TNone_F1EF25: return T;
    case TSome_6B73B7: return T;
    default: return F;
  }
}

Option_0CB775 Option_0CB775__as(STACK_FRAME void *this) {
  switch(((Type) this)->type) {
    case TNone_F1EF25: break;
    case TSome_6B73B7: break;
    default:
      fprintf(stderr, "Invalid cast from %s to Option[base.test_event_data_port_periodic_domains.consumer_t_i_Initialization_Api].", TYPE_string_(this));
      sfAbortImpl(CALLER "");
  }
  return (Option_0CB775) this;
}

void Option_0CB775_string_(STACK_FRAME String result, Option_0CB775 this);

void Option_0CB775_get_(STACK_FRAME base_test_event_data_port_periodic_domains_consumer_t_i_Initialization_Api result, Option_0CB775 this) {
  switch (this->type) {
    case TNone_F1EF25: None_F1EF25_get_(CALLER (base_test_event_data_port_periodic_domains_consumer_t_i_Initialization_Api) result, (None_F1EF25) this); return;
    case TSome_6B73B7: Some_6B73B7_get_(CALLER (base_test_event_data_port_periodic_domains_consumer_t_i_Initialization_Api) result, (Some_6B73B7) this); return;
    default: fprintf(stderr, "Infeasible TYPE: %s.\n", TYPE_string_(this)); exit(1);
  }
}