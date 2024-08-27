#include <all.h>

// Option[base.test_data_port_periodic_domains.consumer_thread_i_Initialization_Api]

B Option_C131BE__is(STACK_FRAME void *this) {
  switch(((Type) this)->type) {
    case TNone_F97B7F: return T;
    case TSome_13E02D: return T;
    default: return F;
  }
}

Option_C131BE Option_C131BE__as(STACK_FRAME void *this) {
  switch(((Type) this)->type) {
    case TNone_F97B7F: break;
    case TSome_13E02D: break;
    default:
      fprintf(stderr, "Invalid cast from %s to Option[base.test_data_port_periodic_domains.consumer_thread_i_Initialization_Api].", TYPE_string_(this));
      sfAbortImpl(CALLER "");
  }
  return (Option_C131BE) this;
}

void Option_C131BE_string_(STACK_FRAME String result, Option_C131BE this);

void Option_C131BE_get_(STACK_FRAME base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api result, Option_C131BE this) {
  switch (this->type) {
    case TNone_F97B7F: None_F97B7F_get_(CALLER (base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api) result, (None_F97B7F) this); return;
    case TSome_13E02D: Some_13E02D_get_(CALLER (base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api) result, (Some_13E02D) this); return;
    default: fprintf(stderr, "Infeasible TYPE: %s.\n", TYPE_string_(this)); exit(1);
  }
}