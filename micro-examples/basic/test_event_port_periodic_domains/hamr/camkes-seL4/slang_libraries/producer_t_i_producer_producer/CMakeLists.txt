if(APPLE)
  cmake_minimum_required(VERSION 3.27)
  if($ENV{CC} MATCHES "^.*ccomp$")
    execute_process(COMMAND uname -m
      OUTPUT_VARIABLE CMAKE_C_COMPILER_TARGET
      OUTPUT_STRIP_TRAILING_WHITESPACE)
  endif()
else()
  cmake_minimum_required(VERSION 3.5.1)
endif(APPLE)

project(producer_t_i_producer_producer)

set(CMAKE_C_STANDARD 99)

add_compile_options(-Werror)
if($ENV{CC} MATCHES "^.*ccomp$")
  add_compile_options(-flongdouble)
endif()

function(to_hex DEC HEX)
  while(DEC GREATER 0)
    math(EXPR _val "${DEC} % 16")
    math(EXPR DEC "${DEC} / 16")
    if(_val EQUAL 10)
      set(_val "A")
    elseif(_val EQUAL 11)
      set(_val "B")
    elseif(_val EQUAL 12)
      set(_val "C")
    elseif(_val EQUAL 13)
      set(_val "D")
    elseif(_val EQUAL 14)
      set(_val "E")
    elseif(_val EQUAL 15)
      set(_val "F")
    endif()
    set(_res "${_val}${_res}")
  endwhile()
  set(${HEX} "0x${_res}" PARENT_SCOPE)
endfunction()

if ("${CMAKE_CXX_COMPILER_ID}" MATCHES "(C|c?)lang")
  to_hex("16777216" stack_size)
  set(CMAKE_EXE_LINKER_FLAGS "-Wl,-stack_size -Wl,${stack_size}")
  if($ENV{CC} MATCHES "^.*ccomp$")
    SET(CMAKE_C_ARCHIVE_CREATE   "<CMAKE_AR> Scr <TARGET> <LINK_FLAGS> <OBJECTS>")
    SET(CMAKE_CXX_ARCHIVE_CREATE "<CMAKE_AR> Scr <TARGET> <LINK_FLAGS> <OBJECTS>")
    SET(CMAKE_C_ARCHIVE_FINISH   "<CMAKE_RANLIB> -no_warning_for_no_symbols -c <TARGET>")
    SET(CMAKE_CXX_ARCHIVE_FINISH "<CMAKE_RANLIB> -no_warning_for_no_symbols -c <TARGET>")
  else()
    add_compile_options(-Dinline=static)
  endif()
  add_compile_options("$<$<CONFIG:Release>:-Os>")
elseif ("${CMAKE_CXX_COMPILER_ID}" STREQUAL "GNU")
  if (WIN32 OR MINGW OR MSYS OR CYGWIN)
    to_hex("16777216" stack_size)
    set(CMAKE_EXE_LINKER_FLAGS "-Wl,--stack,${stack_size}")
    add_compile_options(-fstack-usage -O2)
  elseif(NOT $ENV{CC} MATCHES "^.*ccomp$")
    add_compile_options(-fstack-usage -Dinline=static)
    add_compile_options("$<$<CONFIG:Release>:-Os>")
  endif()
endif()

option(BOUND_CHECK
  "Build the program with sequence bound checking."
  OFF)

if(BOUND_CHECK)
  add_definitions(-DSIREUM_BOUND_CHECK)
endif(BOUND_CHECK)

option(RANGE_CHECK
  "Build the program with range checking."
  OFF)

if(RANGE_CHECK)
  add_definitions(-DSIREUM_RANGE_CHECK)
endif(RANGE_CHECK)

option(NO_PRINT
  "Build the program without console output."
  OFF)

if(NO_PRINT)
  add_definitions(-DSIREUM_NO_PRINT)
endif(NO_PRINT)

option(WITH_LOC
  "Build the program with Slang location info."
  OFF)

if(WITH_LOC)
  add_definitions(-DSIREUM_LOC)
endif(WITH_LOC)


add_library(producer_t_i_producer_producer STATIC
        runtime/misc.c
        runtime/type.c
        runtime/type.h
        runtime/misc.h
        runtime/stackframe.c
        runtime/lib.h
        runtime/stackframe.h
        runtime/ztype.h
        runtime/type-composite.h
        runtime/types.h
        runtime/types.c
        runtime/all.h
        runtime/all.c
        library/art/type-art_Art.h
        library/art/art_Art.h
        library/art/art_Art.c
        library/art/Art/type-art_Art_BridgeId.h
        library/art/Art/art_Art_BridgeId.h
        library/art/Art/art_Art_BridgeId.c
        library/art/Art/type-art_Art_PortId.h
        library/art/Art/art_Art_PortId.h
        library/art/Art/art_Art_PortId.c
        library/art/type-art_Bridge.h
        library/art/art_Bridge.h
        library/art/art_Bridge.c
        library/art/Bridge/type-art_Bridge_EntryPoints.h
        library/art/Bridge/art_Bridge_EntryPoints.h
        library/art/Bridge/art_Bridge_EntryPoints.c
        library/art/Bridge/type-art_Bridge_Ports.h
        library/art/Bridge/art_Bridge_Ports.h
        library/art/Bridge/art_Bridge_Ports.c
        library/art/type-art_DataContent.h
        library/art/art_DataContent.h
        library/art/art_DataContent.c
        library/art/type-art_DispatchPropertyProtocol.h
        library/art/art_DispatchPropertyProtocol.h
        library/art/art_DispatchPropertyProtocol.c
        library/art/DispatchPropertyProtocol/type-art_DispatchPropertyProtocol_Periodic.h
        library/art/DispatchPropertyProtocol/art_DispatchPropertyProtocol_Periodic.h
        library/art/DispatchPropertyProtocol/art_DispatchPropertyProtocol_Periodic.c
        library/art/type-art_Empty.h
        library/art/art_Empty.h
        library/art/art_Empty.c
        library/art/PortMode/type-art_PortMode_Type.h
        library/art/PortMode/art_PortMode_Type.h
        library/art/PortMode/art_PortMode_Type.c
        library/art/type-art_UPort.h
        library/art/art_UPort.h
        library/art/art_UPort.c
        library/art/type-art_art_Port_9CBF18.h
        library/art/art_art_Port_9CBF18.h
        library/art/art_art_Port_9CBF18.c
        library/base/Base_Types/type-base_Base_Types_Boolean_Payload.h
        library/base/Base_Types/base_Base_Types_Boolean_Payload.h
        library/base/Base_Types/base_Base_Types_Boolean_Payload.c
        library/base/type-base_TranspilerToucher.h
        library/base/base_TranspilerToucher.h
        library/base/base_TranspilerToucher.c
        library/base/producer_t_i_producer_producer/type-base_producer_t_i_producer_producer_producer.h
        library/base/producer_t_i_producer_producer/base_producer_t_i_producer_producer_producer.h
        library/base/producer_t_i_producer_producer/base_producer_t_i_producer_producer_producer.c
        library/base/test_event_port_periodic_domains/type-base_test_event_port_periodic_domains_producer_t_i_Api.h
        library/base/test_event_port_periodic_domains/base_test_event_port_periodic_domains_producer_t_i_Api.h
        library/base/test_event_port_periodic_domains/base_test_event_port_periodic_domains_producer_t_i_Api.c
        library/base/test_event_port_periodic_domains/type-base_test_event_port_periodic_domains_producer_t_i_Initialization_Api.h
        library/base/test_event_port_periodic_domains/base_test_event_port_periodic_domains_producer_t_i_Initialization_Api.h
        library/base/test_event_port_periodic_domains/base_test_event_port_periodic_domains_producer_t_i_Initialization_Api.c
        library/base/test_event_port_periodic_domains/type-base_test_event_port_periodic_domains_producer_t_i_Operational_Api.h
        library/base/test_event_port_periodic_domains/base_test_event_port_periodic_domains_producer_t_i_Operational_Api.h
        library/base/test_event_port_periodic_domains/base_test_event_port_periodic_domains_producer_t_i_Operational_Api.c
        library/base/test_event_port_periodic_domains/type-base_test_event_port_periodic_domains_producer_t_i_producer_producer.h
        library/base/test_event_port_periodic_domains/base_test_event_port_periodic_domains_producer_t_i_producer_producer.h
        library/base/test_event_port_periodic_domains/type-base_test_event_port_periodic_domains_producer_t_i_producer_producer_Bridge.h
        library/base/test_event_port_periodic_domains/base_test_event_port_periodic_domains_producer_t_i_producer_producer_Bridge.h
        library/base/test_event_port_periodic_domains/base_test_event_port_periodic_domains_producer_t_i_producer_producer_Bridge.c
        library/base/test_event_port_periodic_domains/producer_t_i_producer_producer_Bridge/type-base_test_event_port_periodic_domains_producer_t_i_producer_producer_Bridge_EntryPoints.h
        library/base/test_event_port_periodic_domains/producer_t_i_producer_producer_Bridge/base_test_event_port_periodic_domains_producer_t_i_producer_producer_Bridge_EntryPoints.h
        library/base/test_event_port_periodic_domains/producer_t_i_producer_producer_Bridge/base_test_event_port_periodic_domains_producer_t_i_producer_producer_Bridge_EntryPoints.c
        library/base/test_event_port_periodic_domains/type-base_test_event_port_periodic_domains_producer_t_i_producer_producer_seL4Nix.h
        library/base/test_event_port_periodic_domains/base_test_event_port_periodic_domains_producer_t_i_producer_producer_seL4Nix.h
        library/org/sireum/type-org_sireum_IS_820232.h
        library/org/sireum/org_sireum_IS_820232.h
        library/org/sireum/org_sireum_IS_820232.c
        library/org/sireum/type-org_sireum_IS_948B60.h
        library/org/sireum/org_sireum_IS_948B60.h
        library/org/sireum/org_sireum_IS_948B60.c
        library/org/sireum/type-org_sireum_IS_D10119.h
        library/org/sireum/org_sireum_IS_D10119.h
        library/org/sireum/org_sireum_IS_D10119.c
        library/org/sireum/type-org_sireum_MBox2_37E193.h
        library/org/sireum/org_sireum_MBox2_37E193.h
        library/org/sireum/org_sireum_MBox2_37E193.c
        library/org/sireum/type-org_sireum_MBox2_43CC67.h
        library/org/sireum/org_sireum_MBox2_43CC67.h
        library/org/sireum/org_sireum_MBox2_43CC67.c
        library/org/sireum/type-org_sireum_MS_83D5EB.h
        library/org/sireum/org_sireum_MS_83D5EB.h
        library/org/sireum/org_sireum_MS_83D5EB.c
        library/org/sireum/type-org_sireum_None.h
        library/org/sireum/org_sireum_None.h
        library/org/sireum/org_sireum_None.c
        library/org/sireum/type-org_sireum_None_04C60E.h
        library/org/sireum/org_sireum_None_04C60E.h
        library/org/sireum/org_sireum_None_04C60E.c
        library/org/sireum/type-org_sireum_None_5A630D.h
        library/org/sireum/org_sireum_None_5A630D.h
        library/org/sireum/org_sireum_None_5A630D.c
        library/org/sireum/type-org_sireum_None_734370.h
        library/org/sireum/org_sireum_None_734370.h
        library/org/sireum/org_sireum_None_734370.c
        library/org/sireum/type-org_sireum_None_964667.h
        library/org/sireum/org_sireum_None_964667.h
        library/org/sireum/org_sireum_None_964667.c
        library/org/sireum/type-org_sireum_None_CFD2B0.h
        library/org/sireum/org_sireum_None_CFD2B0.h
        library/org/sireum/org_sireum_None_CFD2B0.c
        library/org/sireum/type-org_sireum_Nothing.h
        library/org/sireum/org_sireum_Nothing.h
        library/org/sireum/org_sireum_Nothing.c
        library/org/sireum/type-org_sireum_Option_0247A1.h
        library/org/sireum/org_sireum_Option_0247A1.h
        library/org/sireum/org_sireum_Option_0247A1.c
        library/org/sireum/type-org_sireum_Option_7BBFBE.h
        library/org/sireum/org_sireum_Option_7BBFBE.h
        library/org/sireum/org_sireum_Option_7BBFBE.c
        library/org/sireum/type-org_sireum_Option_8E9F45.h
        library/org/sireum/org_sireum_Option_8E9F45.h
        library/org/sireum/org_sireum_Option_8E9F45.c
        library/org/sireum/type-org_sireum_Option_CF2493.h
        library/org/sireum/org_sireum_Option_CF2493.h
        library/org/sireum/org_sireum_Option_CF2493.c
        library/org/sireum/type-org_sireum_Option_CFF091.h
        library/org/sireum/org_sireum_Option_CFF091.h
        library/org/sireum/org_sireum_Option_CFF091.c
        library/org/sireum/type-org_sireum_Some.h
        library/org/sireum/org_sireum_Some.h
        library/org/sireum/org_sireum_Some.c
        library/org/sireum/type-org_sireum_Some_4CD075.h
        library/org/sireum/org_sireum_Some_4CD075.h
        library/org/sireum/org_sireum_Some_4CD075.c
        library/org/sireum/type-org_sireum_Some_A8A5E0.h
        library/org/sireum/org_sireum_Some_A8A5E0.h
        library/org/sireum/org_sireum_Some_A8A5E0.c
        library/org/sireum/type-org_sireum_Some_D29615.h
        library/org/sireum/org_sireum_Some_D29615.h
        library/org/sireum/org_sireum_Some_D29615.c
        ext/ext.c
        ext/ext.h
        ext/producer_t_i_producer_producer.h
        ext/producer_t_i_producer_producer.c
        ext/producer_t_i_producer_producer_api.h
        ext/producer_t_i_producer_producer_api.c
        ext/producer_t_i_producer_producer_adapter.h
        ext/producer_t_i_producer_producer_adapter.c)

target_include_directories(producer_t_i_producer_producer
        PUBLIC PUBLIC app-producer
        PUBLIC PUBLIC runtime
        PUBLIC PUBLIC library/art
        PUBLIC PUBLIC library/art/Art
        PUBLIC PUBLIC library/art/Bridge
        PUBLIC PUBLIC library/art/DispatchPropertyProtocol
        PUBLIC PUBLIC library/art/PortMode
        PUBLIC PUBLIC library/base/Base_Types
        PUBLIC PUBLIC library/base
        PUBLIC PUBLIC library/base/producer_t_i_producer_producer
        PUBLIC PUBLIC library/base/test_event_port_periodic_domains
        PUBLIC PUBLIC library/base/test_event_port_periodic_domains/producer_t_i_producer_producer_Bridge
        PUBLIC PUBLIC library/org/sireum
        PUBLIC PUBLIC ext)


add_definitions(-DCAMKES)

if(TARGET muslc)
  target_link_libraries(producer_t_i_producer_producer
                        muslc)
endif()