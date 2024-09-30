#include <all.h>

// Option[base.test_event_port_periodic_domains.producer_t_i_Operational_Api]

B Option_CF2493__is(STACK_FRAME void *this) {
  switch(((Type) this)->type) {
    case TNone_04C60E: return T;
    case TSome_A8A5E0: return T;
    default: return F;
  }
}

Option_CF2493 Option_CF2493__as(STACK_FRAME void *this) {
  switch(((Type) this)->type) {
    case TNone_04C60E: break;
    case TSome_A8A5E0: break;
    default:
      fprintf(stderr, "Invalid cast from %s to Option[base.test_event_port_periodic_domains.producer_t_i_Operational_Api].", TYPE_string_(this));
      sfAbortImpl(CALLER "");
  }
  return (Option_CF2493) this;
}

void Option_CF2493_string_(STACK_FRAME String result, Option_CF2493 this);

void Option_CF2493_get_(STACK_FRAME base_test_event_port_periodic_domains_producer_t_i_Operational_Api result, Option_CF2493 this) {
  switch (this->type) {
    case TNone_04C60E: None_04C60E_get_(CALLER (base_test_event_port_periodic_domains_producer_t_i_Operational_Api) result, (None_04C60E) this); return;
    case TSome_A8A5E0: Some_A8A5E0_get_(CALLER (base_test_event_port_periodic_domains_producer_t_i_Operational_Api) result, (Some_A8A5E0) this); return;
    default: fprintf(stderr, "Infeasible TYPE: %s.\n", TYPE_string_(this)); exit(1);
  }
}