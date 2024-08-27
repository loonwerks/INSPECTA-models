#include <all.h>

// Option[base.test_event_data_port_periodic_domains.producer_t_i_Initialization_Api]

B Option_1F3783__is(STACK_FRAME void *this) {
  switch(((Type) this)->type) {
    case TNone_608F86: return T;
    case TSome_FDB73B: return T;
    default: return F;
  }
}

Option_1F3783 Option_1F3783__as(STACK_FRAME void *this) {
  switch(((Type) this)->type) {
    case TNone_608F86: break;
    case TSome_FDB73B: break;
    default:
      fprintf(stderr, "Invalid cast from %s to Option[base.test_event_data_port_periodic_domains.producer_t_i_Initialization_Api].", TYPE_string_(this));
      sfAbortImpl(CALLER "");
  }
  return (Option_1F3783) this;
}

void Option_1F3783_string_(STACK_FRAME String result, Option_1F3783 this);

void Option_1F3783_get_(STACK_FRAME base_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api result, Option_1F3783 this) {
  switch (this->type) {
    case TNone_608F86: None_608F86_get_(CALLER (base_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api) result, (None_608F86) this); return;
    case TSome_FDB73B: Some_FDB73B_get_(CALLER (base_test_event_data_port_periodic_domains_producer_t_i_Initialization_Api) result, (Some_FDB73B) this); return;
    default: fprintf(stderr, "Infeasible TYPE: %s.\n", TYPE_string_(this)); exit(1);
  }
}