#![allow(non_camel_case_types)]
#![allow(non_snake_case)]
#![allow(non_upper_case_globals)]

// Do not edit this file as it will be overwritten if codegen is rerun

#[repr(C)]
#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub enum isolette_Isolette_Data_Model_ValueStatus_Type {
  Valid = 0,
  Invalid = 1
}

#[repr(C)]
#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub enum isolette_Isolette_Data_Model_Regulator_Mode_Type {
  Init_Regulator_Mode = 0,
  Normal_Regulator_Mode = 1,
  Failed_Regulator_Mode = 2
}

#[repr(C)]
#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub enum isolette_Isolette_Data_Model_Status_Type {
  Init_Status = 0,
  On_Status = 1,
  Failed_Status = 2
}

#[repr(C)]
#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub enum isolette_Isolette_Data_Model_On_Off_Type {
  Onn = 0,
  Off = 1
}

#[repr(C)]
#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub enum isolette_Isolette_Data_Model_Monitor_Mode_Type {
  Init_Monitor_Mode = 0,
  Normal_Monitor_Mode = 1,
  Failed_Monitor_Mode = 2
}

#[repr(C)]
#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub enum isolette_Isolette_Environment_Heat_Type {
  Dummy_Head_Enum = 0
}

#[repr(C)]
#[derive(Debug, Clone, Copy)]
pub struct isolette_Isolette_Data_Model_Temp_i {
  pub degrees: cty::c_float
}

#[repr(C)]
#[derive(Debug, Clone, Copy)]
pub struct isolette_Isolette_Data_Model_PhysicalTemp_i {
  pub degrees: cty::c_float
}

#[repr(C)]
#[derive(Debug, Clone, Copy)]
pub struct isolette_Isolette_Data_Model_TempWstatus_i {
  pub degrees: cty::c_float,
  pub status: isolette_Isolette_Data_Model_ValueStatus_Type
}

#[repr(C)]
#[derive(Debug, Clone, Copy)]
pub struct isolette_Isolette_Data_Model_Failure_Flag_i {
  pub flag: bool
}
