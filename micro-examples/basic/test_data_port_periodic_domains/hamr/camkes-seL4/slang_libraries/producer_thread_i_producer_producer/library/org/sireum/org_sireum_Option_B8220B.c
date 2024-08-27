#include <all.h>

// Option[base.test_data_port_periodic_domains.producer_thread_i_Initialization_Api]

B Option_B8220B__is(STACK_FRAME void *this) {
  switch(((Type) this)->type) {
    case TNone_B7029D: return T;
    case TSome_AA275E: return T;
    default: return F;
  }
}

Option_B8220B Option_B8220B__as(STACK_FRAME void *this) {
  switch(((Type) this)->type) {
    case TNone_B7029D: break;
    case TSome_AA275E: break;
    default:
      fprintf(stderr, "Invalid cast from %s to Option[base.test_data_port_periodic_domains.producer_thread_i_Initialization_Api].", TYPE_string_(this));
      sfAbortImpl(CALLER "");
  }
  return (Option_B8220B) this;
}

void Option_B8220B_string_(STACK_FRAME String result, Option_B8220B this);

void Option_B8220B_get_(STACK_FRAME base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api result, Option_B8220B this) {
  switch (this->type) {
    case TNone_B7029D: None_B7029D_get_(CALLER (base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api) result, (None_B7029D) this); return;
    case TSome_AA275E: Some_AA275E_get_(CALLER (base_test_data_port_periodic_domains_producer_thread_i_Initialization_Api) result, (Some_AA275E) this); return;
    default: fprintf(stderr, "Infeasible TYPE: %s.\n", TYPE_string_(this)); exit(1);
  }
}