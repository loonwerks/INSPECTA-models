#ifndef SIREUM_TYPE_H_art_Bridge
#define SIREUM_TYPE_H_art_Bridge

#ifdef __cplusplus
extern "C" {
#endif

#include <misc.h>

// art.Bridge

#include <type-base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge.h>

typedef union art_Bridge *art_Bridge;
union art_Bridge {
  TYPE type;
  struct base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge;
};

#define DeclNewart_Bridge(x) union art_Bridge x = { 0 }

#ifdef __cplusplus
}
#endif

#endif