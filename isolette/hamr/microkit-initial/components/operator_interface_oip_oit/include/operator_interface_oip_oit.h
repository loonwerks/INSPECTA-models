#pragma once

#include <printf.h>
#include <util.h>
#include <stdint.h>
#include <microkit.h>
#include <sb_types.h>

// Do not edit this file as it will be overwritten if codegen is rerun


bool get_display_temperature(Isolette_Data_Model_Temp_i *data);
bool get_regulator_status(Isolette_Data_Model_Status *data);
bool get_monitor_status(Isolette_Data_Model_Status *data);
bool get_alarm_control(Isolette_Data_Model_On_Off *data);
bool put_lower_desired_tempWstatus(const Isolette_Data_Model_TempWstatus_i *data);
bool put_upper_desired_tempWstatus(const Isolette_Data_Model_TempWstatus_i *data);
bool put_lower_alarm_tempWstatus(const Isolette_Data_Model_TempWstatus_i *data);
bool put_upper_alarm_tempWstatus(const Isolette_Data_Model_TempWstatus_i *data);
