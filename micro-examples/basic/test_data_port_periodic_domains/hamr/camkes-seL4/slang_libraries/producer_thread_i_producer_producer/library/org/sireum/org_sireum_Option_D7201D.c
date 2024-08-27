#include <all.h>

// Option[base.test_data_port_periodic_domains.producer_thread_i_Operational_Api]

B Option_D7201D__is(STACK_FRAME void *this) {
  switch(((Type) this)->type) {
    case TNone_F1003F: return T;
    case TSome_E023D9: return T;
    default: return F;
  }
}

Option_D7201D Option_D7201D__as(STACK_FRAME void *this) {
  switch(((Type) this)->type) {
    case TNone_F1003F: break;
    case TSome_E023D9: break;
    default:
      fprintf(stderr, "Invalid cast from %s to Option[base.test_data_port_periodic_domains.producer_thread_i_Operational_Api].", TYPE_string_(this));
      sfAbortImpl(CALLER "");
  }
  return (Option_D7201D) this;
}

void Option_D7201D_string_(STACK_FRAME String result, Option_D7201D this);

void Option_D7201D_get_(STACK_FRAME base_test_data_port_periodic_domains_producer_thread_i_Operational_Api result, Option_D7201D this) {
  switch (this->type) {
    case TNone_F1003F: None_F1003F_get_(CALLER (base_test_data_port_periodic_domains_producer_thread_i_Operational_Api) result, (None_F1003F) this); return;
    case TSome_E023D9: Some_E023D9_get_(CALLER (base_test_data_port_periodic_domains_producer_thread_i_Operational_Api) result, (Some_E023D9) this); return;
    default: fprintf(stderr, "Infeasible TYPE: %s.\n", TYPE_string_(this)); exit(1);
  }
}