#pragma once

#include <printf.h>
#include <util.h>
#include <stdint.h>
#include <microkit.h>
#include <sb_types.h>

// Do not edit this file as it will be overwritten if codegen is rerun


bool get_upper_alarm_temp(base_Isolette_Data_Model_Temp_i *data);
bool get_lower_alarm_temp(base_Isolette_Data_Model_Temp_i *data);
bool put_alarm_control(const base_Isolette_Data_Model_On_Off_Type *data);
bool get_monitor_mode(base_Isolette_Data_Model_Monitor_Mode_Type *data);
bool get_current_tempWstatus(base_Isolette_Data_Model_TempWstatus_i *data);
