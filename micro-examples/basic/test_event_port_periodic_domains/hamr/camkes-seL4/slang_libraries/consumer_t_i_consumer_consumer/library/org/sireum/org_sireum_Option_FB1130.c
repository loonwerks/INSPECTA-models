#include <all.h>

// Option[base.test_event_port_periodic_domains.consumer_t_i_Operational_Api]

B Option_FB1130__is(STACK_FRAME void *this) {
  switch(((Type) this)->type) {
    case TNone_5F2743: return T;
    case TSome_2EDC17: return T;
    default: return F;
  }
}

Option_FB1130 Option_FB1130__as(STACK_FRAME void *this) {
  switch(((Type) this)->type) {
    case TNone_5F2743: break;
    case TSome_2EDC17: break;
    default:
      fprintf(stderr, "Invalid cast from %s to Option[base.test_event_port_periodic_domains.consumer_t_i_Operational_Api].", TYPE_string_(this));
      sfAbortImpl(CALLER "");
  }
  return (Option_FB1130) this;
}

void Option_FB1130_string_(STACK_FRAME String result, Option_FB1130 this);

void Option_FB1130_get_(STACK_FRAME base_test_event_port_periodic_domains_consumer_t_i_Operational_Api result, Option_FB1130 this) {
  switch (this->type) {
    case TNone_5F2743: None_5F2743_get_(CALLER (base_test_event_port_periodic_domains_consumer_t_i_Operational_Api) result, (None_5F2743) this); return;
    case TSome_2EDC17: Some_2EDC17_get_(CALLER (base_test_event_port_periodic_domains_consumer_t_i_Operational_Api) result, (Some_2EDC17) this); return;
    default: fprintf(stderr, "Infeasible TYPE: %s.\n", TYPE_string_(this)); exit(1);
  }
}