#ifndef SIREUM_H_base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints
#define SIREUM_H_base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints

#ifdef __cplusplus
extern "C" {
#endif

#include <types.h>

// base.test_event_data_port_periodic_domains.consumer_t_i_consumer_consumer_Bridge.EntryPoints

#define base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints_consumer_t_i_consumer_consumer_BridgeId_(this) ((this)->consumer_t_i_consumer_consumer_BridgeId)
#define base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints_read_port_Id_(this) ((this)->read_port_Id)
#define base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints_dispatchTriggers_(this) ((Option_0247A1) &(this)->dispatchTriggers)
#define base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints_initialization_api_(this) ((base_test_event_data_port_periodic_domains_consumer_t_i_Initialization_Api) &(this)->initialization_api)
#define base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints_operational_api_(this) ((base_test_event_data_port_periodic_domains_consumer_t_i_Operational_Api) &(this)->operational_api)
#define base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints_dataInPortIds_(this) ((IS_D10119) &(this)->dataInPortIds)
#define base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints_eventInPortIds_(this) ((IS_D10119) &(this)->eventInPortIds)
#define base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints_dataOutPortIds_(this) ((IS_D10119) &(this)->dataOutPortIds)
#define base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints_eventOutPortIds_(this) ((IS_D10119) &(this)->eventOutPortIds)

B base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints__eq(base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints this, base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints other);
inline B base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints__ne(base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints this, base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints other) {
  return !base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints__eq(this, other);
};
B base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints__equiv(base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints this, base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints other);
inline B base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints__inequiv(base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints this, base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints other) {
  return !base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints__equiv(this, other);
};
void base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints_string_(STACK_FRAME String result, base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints this);
void base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints_cprint(base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints this, B isOut);

inline B base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints__is(STACK_FRAME void* this) {
  return ((base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints) this)->type == Tbase_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints;
}

inline base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints__as(STACK_FRAME void *this) {
  if (base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints__is(CALLER this)) return (base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints) this;
  sfAbortImpl(CALLER "Invalid cast to base.test_event_data_port_periodic_domains.consumer_t_i_consumer_consumer_Bridge.EntryPoints.");
  abort();
}

void base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints_apply(STACK_FRAME base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints this, art_Art_BridgeId consumer_t_i_consumer_consumer_BridgeId, art_Art_PortId read_port_Id, Option_0247A1 dispatchTriggers, base_test_event_data_port_periodic_domains_consumer_t_i_Initialization_Api initialization_api, base_test_event_data_port_periodic_domains_consumer_t_i_Operational_Api operational_api);

Unit base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints_initialise_(STACK_FRAME base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints this);

Unit base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints_compute_(STACK_FRAME base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints this);

Unit base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints_finalise_(STACK_FRAME base_test_event_data_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints this);

#ifdef __cplusplus
}
#endif

#endif