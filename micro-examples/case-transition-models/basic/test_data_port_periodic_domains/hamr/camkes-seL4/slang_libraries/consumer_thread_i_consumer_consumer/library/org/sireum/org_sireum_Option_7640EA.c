#include <all.h>

// Option[base.test_data_port_periodic_domains.consumer_thread_i_Operational_Api]

B Option_7640EA__is(STACK_FRAME void *this) {
  switch(((Type) this)->type) {
    case TNone_D3113C: return T;
    case TSome_1CCCBF: return T;
    default: return F;
  }
}

Option_7640EA Option_7640EA__as(STACK_FRAME void *this) {
  switch(((Type) this)->type) {
    case TNone_D3113C: break;
    case TSome_1CCCBF: break;
    default:
      fprintf(stderr, "Invalid cast from %s to Option[base.test_data_port_periodic_domains.consumer_thread_i_Operational_Api].", TYPE_string_(this));
      sfAbortImpl(CALLER "");
  }
  return (Option_7640EA) this;
}

void Option_7640EA_string_(STACK_FRAME String result, Option_7640EA this);

void Option_7640EA_get_(STACK_FRAME base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api result, Option_7640EA this) {
  switch (this->type) {
    case TNone_D3113C: None_D3113C_get_(CALLER (base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api) result, (None_D3113C) this); return;
    case TSome_1CCCBF: Some_1CCCBF_get_(CALLER (base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api) result, (Some_1CCCBF) this); return;
    default: fprintf(stderr, "Infeasible TYPE: %s.\n", TYPE_string_(this)); exit(1);
  }
}