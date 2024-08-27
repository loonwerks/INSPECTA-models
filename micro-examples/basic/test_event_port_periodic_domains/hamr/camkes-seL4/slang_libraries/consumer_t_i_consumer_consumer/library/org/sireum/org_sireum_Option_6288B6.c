#include <all.h>

// Option[base.test_event_port_periodic_domains.consumer_t_i_Initialization_Api]

B Option_6288B6__is(STACK_FRAME void *this) {
  switch(((Type) this)->type) {
    case TNone_9DDC16: return T;
    case TSome_0B69ED: return T;
    default: return F;
  }
}

Option_6288B6 Option_6288B6__as(STACK_FRAME void *this) {
  switch(((Type) this)->type) {
    case TNone_9DDC16: break;
    case TSome_0B69ED: break;
    default:
      fprintf(stderr, "Invalid cast from %s to Option[base.test_event_port_periodic_domains.consumer_t_i_Initialization_Api].", TYPE_string_(this));
      sfAbortImpl(CALLER "");
  }
  return (Option_6288B6) this;
}

void Option_6288B6_string_(STACK_FRAME String result, Option_6288B6 this);

void Option_6288B6_get_(STACK_FRAME base_test_event_port_periodic_domains_consumer_t_i_Initialization_Api result, Option_6288B6 this) {
  switch (this->type) {
    case TNone_9DDC16: None_9DDC16_get_(CALLER (base_test_event_port_periodic_domains_consumer_t_i_Initialization_Api) result, (None_9DDC16) this); return;
    case TSome_0B69ED: Some_0B69ED_get_(CALLER (base_test_event_port_periodic_domains_consumer_t_i_Initialization_Api) result, (Some_0B69ED) this); return;
    default: fprintf(stderr, "Infeasible TYPE: %s.\n", TYPE_string_(this)); exit(1);
  }
}