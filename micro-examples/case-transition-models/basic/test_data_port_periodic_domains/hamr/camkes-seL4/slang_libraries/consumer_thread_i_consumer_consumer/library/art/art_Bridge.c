#include <all.h>

// art.Bridge

B art_Bridge__is(STACK_FRAME void *this) {
  switch(((Type) this)->type) {
    case Tbase_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge: return T;
    default: return F;
  }
}

art_Bridge art_Bridge__as(STACK_FRAME void *this) {
  switch(((Type) this)->type) {
    case Tbase_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge: break;
    default:
      fprintf(stderr, "Invalid cast from %s to art.Bridge.", TYPE_string_(this));
      sfAbortImpl(CALLER "");
  }
  return (art_Bridge) this;
}

void art_Bridge_string_(STACK_FRAME String result, art_Bridge this);

void art_Bridge_name_(STACK_FRAME String result, art_Bridge this) {
  switch (this->type) {
    case Tbase_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge: Type_assign(result, base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_name_((base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge) this), sizeof(struct StaticString)); return;
    default: fprintf(stderr, "Infeasible TYPE: %s.\n", TYPE_string_(this)); exit(1);
  }
}