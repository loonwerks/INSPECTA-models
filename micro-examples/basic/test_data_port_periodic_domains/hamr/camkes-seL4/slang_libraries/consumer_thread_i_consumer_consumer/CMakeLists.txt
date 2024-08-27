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

project(consumer_thread_i_consumer_consumer)

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


add_library(consumer_thread_i_consumer_consumer STATIC
        runtime/lib.h
        runtime/stackframe.c
        runtime/type.c
        runtime/misc.c
        runtime/misc.h
        runtime/stackframe.h
        runtime/type.h
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
        library/art/type-art_art_Port_A2C07C.h
        library/art/art_art_Port_A2C07C.h
        library/art/art_art_Port_A2C07C.c
        library/base/type-base_Base_Types.h
        library/base/base_Base_Types.h
        library/base/base_Base_Types.c
        library/base/Base_Types/type-base_Base_Types_Boolean_Payload.h
        library/base/Base_Types/base_Base_Types_Boolean_Payload.h
        library/base/Base_Types/base_Base_Types_Boolean_Payload.c
        library/base/Base_Types/type-base_Base_Types_Integer_8_Payload.h
        library/base/Base_Types/base_Base_Types_Integer_8_Payload.h
        library/base/Base_Types/base_Base_Types_Integer_8_Payload.c
        library/base/type-base_TranspilerToucher.h
        library/base/base_TranspilerToucher.h
        library/base/base_TranspilerToucher.c
        library/base/consumer_thread_i_consumer_consumer/type-base_consumer_thread_i_consumer_consumer_consumer.h
        library/base/consumer_thread_i_consumer_consumer/base_consumer_thread_i_consumer_consumer_consumer.h
        library/base/consumer_thread_i_consumer_consumer/base_consumer_thread_i_consumer_consumer_consumer.c
        library/base/test_data_port_periodic_domains/type-base_test_data_port_periodic_domains_consumer_thread_i_Api.h
        library/base/test_data_port_periodic_domains/base_test_data_port_periodic_domains_consumer_thread_i_Api.h
        library/base/test_data_port_periodic_domains/base_test_data_port_periodic_domains_consumer_thread_i_Api.c
        library/base/test_data_port_periodic_domains/type-base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api.h
        library/base/test_data_port_periodic_domains/base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api.h
        library/base/test_data_port_periodic_domains/base_test_data_port_periodic_domains_consumer_thread_i_Initialization_Api.c
        library/base/test_data_port_periodic_domains/type-base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api.h
        library/base/test_data_port_periodic_domains/base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api.h
        library/base/test_data_port_periodic_domains/base_test_data_port_periodic_domains_consumer_thread_i_Operational_Api.c
        library/base/test_data_port_periodic_domains/type-base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer.h
        library/base/test_data_port_periodic_domains/base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer.h
        library/base/test_data_port_periodic_domains/type-base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge.h
        library/base/test_data_port_periodic_domains/base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge.h
        library/base/test_data_port_periodic_domains/base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge.c
        library/base/test_data_port_periodic_domains/consumer_thread_i_consumer_consumer_Bridge/type-base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints.h
        library/base/test_data_port_periodic_domains/consumer_thread_i_consumer_consumer_Bridge/base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints.h
        library/base/test_data_port_periodic_domains/consumer_thread_i_consumer_consumer_Bridge/base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_Bridge_EntryPoints.c
        library/base/test_data_port_periodic_domains/type-base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_seL4Nix.h
        library/base/test_data_port_periodic_domains/base_test_data_port_periodic_domains_consumer_thread_i_consumer_consumer_seL4Nix.h
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
        library/org/sireum/type-org_sireum_None_734370.h
        library/org/sireum/org_sireum_None_734370.h
        library/org/sireum/org_sireum_None_734370.c
        library/org/sireum/type-org_sireum_None_964667.h
        library/org/sireum/org_sireum_None_964667.h
        library/org/sireum/org_sireum_None_964667.c
        library/org/sireum/type-org_sireum_None_CFD2B0.h
        library/org/sireum/org_sireum_None_CFD2B0.h
        library/org/sireum/org_sireum_None_CFD2B0.c
        library/org/sireum/type-org_sireum_None_D3113C.h
        library/org/sireum/org_sireum_None_D3113C.h
        library/org/sireum/org_sireum_None_D3113C.c
        library/org/sireum/type-org_sireum_None_E19B61.h
        library/org/sireum/org_sireum_None_E19B61.h
        library/org/sireum/org_sireum_None_E19B61.c
        library/org/sireum/type-org_sireum_None_F97B7F.h
        library/org/sireum/org_sireum_None_F97B7F.h
        library/org/sireum/org_sireum_None_F97B7F.c
        library/org/sireum/type-org_sireum_Nothing.h
        library/org/sireum/org_sireum_Nothing.h
        library/org/sireum/org_sireum_Nothing.c
        library/org/sireum/type-org_sireum_Option_0247A1.h
        library/org/sireum/org_sireum_Option_0247A1.h
        library/org/sireum/org_sireum_Option_0247A1.c
        library/org/sireum/type-org_sireum_Option_7640EA.h
        library/org/sireum/org_sireum_Option_7640EA.h
        library/org/sireum/org_sireum_Option_7640EA.c
        library/org/sireum/type-org_sireum_Option_7BBFBE.h
        library/org/sireum/org_sireum_Option_7BBFBE.h
        library/org/sireum/org_sireum_Option_7BBFBE.c
        library/org/sireum/type-org_sireum_Option_8E9F45.h
        library/org/sireum/org_sireum_Option_8E9F45.h
        library/org/sireum/org_sireum_Option_8E9F45.c
        library/org/sireum/type-org_sireum_Option_C131BE.h
        library/org/sireum/org_sireum_Option_C131BE.h
        library/org/sireum/org_sireum_Option_C131BE.c
        library/org/sireum/type-org_sireum_Option_FC3847.h
        library/org/sireum/org_sireum_Option_FC3847.h
        library/org/sireum/org_sireum_Option_FC3847.c
        library/org/sireum/type-org_sireum_S8.h
        library/org/sireum/org_sireum_S8.h
        library/org/sireum/org_sireum_S8.c
        library/org/sireum/type-org_sireum_Some.h
        library/org/sireum/org_sireum_Some.h
        library/org/sireum/org_sireum_Some.c
        library/org/sireum/type-org_sireum_Some_13E02D.h
        library/org/sireum/org_sireum_Some_13E02D.h
        library/org/sireum/org_sireum_Some_13E02D.c
        library/org/sireum/type-org_sireum_Some_1CCCBF.h
        library/org/sireum/org_sireum_Some_1CCCBF.h
        library/org/sireum/org_sireum_Some_1CCCBF.c
        library/org/sireum/type-org_sireum_Some_240B2F.h
        library/org/sireum/org_sireum_Some_240B2F.h
        library/org/sireum/org_sireum_Some_240B2F.c
        library/org/sireum/type-org_sireum_Some_D29615.h
        library/org/sireum/org_sireum_Some_D29615.h
        library/org/sireum/org_sireum_Some_D29615.c
        ext/ext.c
        ext/ext.h
        ext/consumer_thread_i_consumer_consumer.h
        ext/consumer_thread_i_consumer_consumer.c
        ext/consumer_thread_i_consumer_consumer_api.h
        ext/consumer_thread_i_consumer_consumer_api.c
        ext/consumer_thread_i_consumer_consumer_adapter.h
        ext/consumer_thread_i_consumer_consumer_adapter.c)

target_include_directories(consumer_thread_i_consumer_consumer
        PUBLIC PUBLIC app-consumer
        PUBLIC PUBLIC runtime
        PUBLIC PUBLIC library/art
        PUBLIC PUBLIC library/art/Art
        PUBLIC PUBLIC library/art/Bridge
        PUBLIC PUBLIC library/art/DispatchPropertyProtocol
        PUBLIC PUBLIC library/art/PortMode
        PUBLIC PUBLIC library/base
        PUBLIC PUBLIC library/base/Base_Types
        PUBLIC PUBLIC library/base/consumer_thread_i_consumer_consumer
        PUBLIC PUBLIC library/base/test_data_port_periodic_domains
        PUBLIC PUBLIC library/base/test_data_port_periodic_domains/consumer_thread_i_consumer_consumer_Bridge
        PUBLIC PUBLIC library/org/sireum
        PUBLIC PUBLIC ext)


add_definitions(-DCAMKES)

if(TARGET muslc)
  target_link_libraries(consumer_thread_i_consumer_consumer
                        muslc)
endif()