#![allow(non_camel_case_types)]

use vstd::prelude::*;
// use vstd::invariant::*;

use crate::art::DataContent;

verus! {

pub type microkit_channel = cty::uint32_t;

#[repr(C)]
#[derive(Copy, Clone, PartialEq, Eq)]
pub enum isolette_Isolette_Data_Model_Status_Type {
  Init_Status,
  On_Status,
  Failed_Status
}

impl Default for isolette_Isolette_Data_Model_Status_Type {
    fn default() -> Self {
        isolette_Isolette_Data_Model_Status_Type::Init_Status
    }
}

impl DataContent for isolette_Isolette_Data_Model_Status_Type {}
    
#[repr(C)]
#[derive(Copy, Clone, PartialEq, Eq, Default)]
pub enum isolette_Isolette_Data_Model_On_Off_Type {
    Onn,
    #[default] Off
}

impl DataContent for isolette_Isolette_Data_Model_On_Off_Type {}

#[repr(C)]
#[derive(Copy, Clone, PartialEq, Eq, Default, Debug)]
pub enum isolette_Isolette_Data_Model_ValueStatus_Type {
    #[default] Valid,
    Invalid
  }

impl DataContent for isolette_Isolette_Data_Model_ValueStatus_Type {}

#[repr(C)]
#[derive(Copy, Clone)]
pub struct isolette_Isolette_Data_Model_TempWstatus_i {
    pub degrees: u32,
    pub status: isolette_Isolette_Data_Model_ValueStatus_Type
}

impl Default for isolette_Isolette_Data_Model_TempWstatus_i {
    fn default() -> Self {
        Self { degrees: 0, status: isolette_Isolette_Data_Model_ValueStatus_Type::Valid }
    }
}

impl DataContent for isolette_Isolette_Data_Model_TempWstatus_i {}

#[repr(C)]
#[derive(Copy, Clone, PartialEq, Debug)]
pub struct isolette_Isolette_Data_Model_Temp_i {
    pub degrees: u32 // should be f32
}

impl Default for isolette_Isolette_Data_Model_Temp_i {
    fn default() -> Self {
        Self { degrees: 0 }
    }
}
  
#[repr(C)]
#[derive(Copy, Clone, PartialEq, Eq)]
pub enum isolette_Isolette_Data_Model_Regulator_Mode_Type {
    Init_Regulator_Mode,
    Normal_Regulator_Mode,
    Failed_Regulator_Mode
}

impl Default for isolette_Isolette_Data_Model_Regulator_Mode_Type {
    fn default() -> Self {
        isolette_Isolette_Data_Model_Regulator_Mode_Type::Init_Regulator_Mode
    }
}

impl DataContent for isolette_Isolette_Data_Model_Regulator_Mode_Type {}

#[repr(C)]
#[derive(Clone, Copy, PartialEq)]
pub struct isolette_Isolette_Data_Model_Failure_Flag_i {
  pub flag: bool 
}

impl Default for isolette_Isolette_Data_Model_Failure_Flag_i {
    fn default() -> Self {
        Self { flag: false }
    }
}

}