#ifndef SIREUM_GEN_H
#define SIREUM_GEN_H

#ifdef __cplusplus
extern "C" {
#endif

#include <misc.h>
#include <type-art_Art.h>
#include <type-art_Art_BridgeId.h>
#include <type-art_Art_PortId.h>
#include <type-art_Bridge.h>
#include <type-art_Bridge_EntryPoints.h>
#include <type-art_Bridge_Ports.h>
#include <type-art_DataContent.h>
#include <type-art_DispatchPropertyProtocol.h>
#include <type-art_DispatchPropertyProtocol_Periodic.h>
#include <type-art_Empty.h>
#include <type-art_PortMode_Type.h>
#include <type-art_UPort.h>
#include <type-art_art_Port_9CBF18.h>
#include <type-base_Base_Types_Boolean_Payload.h>
#include <type-base_TranspilerToucher.h>
#include <type-base_consumer_t_i_consumer_consumer_consumer.h>
#include <type-base_test_event_port_periodic_domains_consumer_t_i_Api.h>
#include <type-base_test_event_port_periodic_domains_consumer_t_i_Initialization_Api.h>
#include <type-base_test_event_port_periodic_domains_consumer_t_i_Operational_Api.h>
#include <type-base_test_event_port_periodic_domains_consumer_t_i_consumer_consumer.h>
#include <type-base_test_event_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge.h>
#include <type-base_test_event_port_periodic_domains_consumer_t_i_consumer_consumer_Bridge_EntryPoints.h>
#include <type-base_test_event_port_periodic_domains_consumer_t_i_consumer_consumer_seL4Nix.h>
#include <type-org_sireum_IS_820232.h>
#include <type-org_sireum_IS_948B60.h>
#include <type-org_sireum_IS_D10119.h>
#include <type-org_sireum_MBox2_37E193.h>
#include <type-org_sireum_MBox2_43CC67.h>
#include <type-org_sireum_MS_83D5EB.h>
#include <type-org_sireum_None.h>
#include <type-org_sireum_None_5F2743.h>
#include <type-org_sireum_None_734370.h>
#include <type-org_sireum_None_964667.h>
#include <type-org_sireum_None_9DDC16.h>
#include <type-org_sireum_None_CFD2B0.h>
#include <type-org_sireum_None_ED72E1.h>
#include <type-org_sireum_Nothing.h>
#include <type-org_sireum_Option_0247A1.h>
#include <type-org_sireum_Option_6288B6.h>
#include <type-org_sireum_Option_7BBFBE.h>
#include <type-org_sireum_Option_8E9F45.h>
#include <type-org_sireum_Option_C622DB.h>
#include <type-org_sireum_Option_FB1130.h>
#include <type-org_sireum_Some.h>
#include <type-org_sireum_Some_0B69ED.h>
#include <type-org_sireum_Some_2EDC17.h>
#include <type-org_sireum_Some_4782C6.h>
#include <type-org_sireum_Some_D29615.h>

#if defined(static_assert)
#define STATIC_ASSERT static_assert
#define GLOBAL_STATIC_ASSERT(a, b, c) static_assert(b, c)
#else
#define STATIC_ASSERT(pred, explanation); {char assert=1/(pred);(void)assert;}
#define GLOBAL_STATIC_ASSERT(unique, pred, explanation); namespace ASSERTION {char unique=1/(pred);}
#endif

size_t sizeOf(Type t);
void Type_assign(void *dest, void *src, size_t destSize);

#ifdef __cplusplus
}
#endif

#endif