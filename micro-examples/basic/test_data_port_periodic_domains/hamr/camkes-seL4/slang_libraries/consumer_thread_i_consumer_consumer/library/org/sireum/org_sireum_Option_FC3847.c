#include <all.h>

// Option[S8]

B Option_FC3847__is(STACK_FRAME void *this) {
  switch(((Type) this)->type) {
    case TNone_E19B61: return T;
    case TSome_240B2F: return T;
    default: return F;
  }
}

Option_FC3847 Option_FC3847__as(STACK_FRAME void *this) {
  switch(((Type) this)->type) {
    case TNone_E19B61: break;
    case TSome_240B2F: break;
    default:
      fprintf(stderr, "Invalid cast from %s to Option[S8].", TYPE_string_(this));
      sfAbortImpl(CALLER "");
  }
  return (Option_FC3847) this;
}

void Option_FC3847_string_(STACK_FRAME String result, Option_FC3847 this);