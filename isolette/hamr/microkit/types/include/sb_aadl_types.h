#pragma once

#include <stdbool.h>
#include <stdint.h>

// Do not edit this file as it will be overwritten if codegen is rerun

typedef
  enum {Dummy_Head_Enum} isolette_Isolette_Environment_Heat_Type;

typedef
  enum {Init_Monitor_Mode, Normal_Monitor_Mode, Failed_Monitor_Mode} isolette_Isolette_Data_Model_Monitor_Mode_Type;

typedef
  enum {Onn, Off} isolette_Isolette_Data_Model_On_Off_Type;

typedef
  enum {Init_Status, On_Status, Failed_Status} isolette_Isolette_Data_Model_Status_Type;

typedef
  enum {Init_Regulator_Mode, Normal_Regulator_Mode, Failed_Regulator_Mode} isolette_Isolette_Data_Model_Regulator_Mode_Type;

typedef
  enum {Valid, Invalid} isolette_Isolette_Data_Model_ValueStatus_Type;

typedef struct isolette_Isolette_Data_Model_Temp_i {
  float degrees;
} isolette_Isolette_Data_Model_Temp_i;

typedef struct isolette_Isolette_Data_Model_PhysicalTemp_i {
  float degrees;
} isolette_Isolette_Data_Model_PhysicalTemp_i;

typedef struct isolette_Isolette_Data_Model_TempWstatus_i {
  float degrees;
  isolette_Isolette_Data_Model_ValueStatus_Type status;
} isolette_Isolette_Data_Model_TempWstatus_i;

typedef struct isolette_Isolette_Data_Model_Failure_Flag_i {
  bool flag;
} isolette_Isolette_Data_Model_Failure_Flag_i;
