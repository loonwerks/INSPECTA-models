#include <all.h>

// Option[base.test_event_port_periodic_domains.producer_t_i_Initialization_Api]

B Option_CFF091__is(STACK_FRAME void *this) {
  switch(((Type) this)->type) {
    case TNone_5A630D: return T;
    case TSome_4CD075: return T;
    default: return F;
  }
}

Option_CFF091 Option_CFF091__as(STACK_FRAME void *this) {
  switch(((Type) this)->type) {
    case TNone_5A630D: break;
    case TSome_4CD075: break;
    default:
      fprintf(stderr, "Invalid cast from %s to Option[base.test_event_port_periodic_domains.producer_t_i_Initialization_Api].", TYPE_string_(this));
      sfAbortImpl(CALLER "");
  }
  return (Option_CFF091) this;
}

void Option_CFF091_string_(STACK_FRAME String result, Option_CFF091 this);

void Option_CFF091_get_(STACK_FRAME base_test_event_port_periodic_domains_producer_t_i_Initialization_Api result, Option_CFF091 this) {
  switch (this->type) {
    case TNone_5A630D: None_5A630D_get_(CALLER (base_test_event_port_periodic_domains_producer_t_i_Initialization_Api) result, (None_5A630D) this); return;
    case TSome_4CD075: Some_4CD075_get_(CALLER (base_test_event_port_periodic_domains_producer_t_i_Initialization_Api) result, (Some_4CD075) this); return;
    default: fprintf(stderr, "Infeasible TYPE: %s.\n", TYPE_string_(this)); exit(1);
  }
}