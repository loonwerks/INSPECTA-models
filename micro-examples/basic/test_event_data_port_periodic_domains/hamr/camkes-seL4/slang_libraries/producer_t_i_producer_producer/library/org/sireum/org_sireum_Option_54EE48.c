#include <all.h>

// Option[base.test_event_data_port_periodic_domains.producer_t_i_Operational_Api]

B Option_54EE48__is(STACK_FRAME void *this) {
  switch(((Type) this)->type) {
    case TNone_C54AB4: return T;
    case TSome_9F6875: return T;
    default: return F;
  }
}

Option_54EE48 Option_54EE48__as(STACK_FRAME void *this) {
  switch(((Type) this)->type) {
    case TNone_C54AB4: break;
    case TSome_9F6875: break;
    default:
      fprintf(stderr, "Invalid cast from %s to Option[base.test_event_data_port_periodic_domains.producer_t_i_Operational_Api].", TYPE_string_(this));
      sfAbortImpl(CALLER "");
  }
  return (Option_54EE48) this;
}

void Option_54EE48_string_(STACK_FRAME String result, Option_54EE48 this);

void Option_54EE48_get_(STACK_FRAME base_test_event_data_port_periodic_domains_producer_t_i_Operational_Api result, Option_54EE48 this) {
  switch (this->type) {
    case TNone_C54AB4: None_C54AB4_get_(CALLER (base_test_event_data_port_periodic_domains_producer_t_i_Operational_Api) result, (None_C54AB4) this); return;
    case TSome_9F6875: Some_9F6875_get_(CALLER (base_test_event_data_port_periodic_domains_producer_t_i_Operational_Api) result, (Some_9F6875) this); return;
    default: fprintf(stderr, "Infeasible TYPE: %s.\n", TYPE_string_(this)); exit(1);
  }
}