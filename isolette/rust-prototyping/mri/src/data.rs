#![allow(non_camel_case_types)]

use vstd::prelude::*;
// use vstd::invariant::*;

use crate::art::DataContent;

verus! {

#[derive(Copy, Clone, PartialEq, Eq, Default)]
pub enum isolette_Isolette_Data_Model_Status_Type {
  #[default] Init_Status,
  On_Status,
  Failed_Status
}

impl DataContent for isolette_Isolette_Data_Model_Status_Type {}

#[derive(Copy, Clone, PartialEq, Eq, Default)]
pub enum isolette_Isolette_Data_Model_On_Off_Type {
    Onn,
    #[default] Off
}

impl DataContent for isolette_Isolette_Data_Model_On_Off_Type {}

#[derive(Copy, Clone, PartialEq, Eq, Default)]
pub enum isolette_Isolette_Data_Model_ValueStatus_Type {
    #[default] Valid,
    Invalid
  }

impl DataContent for isolette_Isolette_Data_Model_ValueStatus_Type {}

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

#[derive(Copy, Clone)]
pub struct isolette_Isolette_Data_Model_Temp_i {
    pub degrees: u32 // should be f32
}

impl Default for isolette_Isolette_Data_Model_Temp_i {
    fn default() -> Self {
        Self { degrees: 0 }
    }
}
  
#[derive(Copy, Clone, PartialEq, Eq, Default)]
pub enum isolette_Isolette_Data_Model_Regulator_Mode_Type {
    Init_Regulator_Mode,
    #[default] Normal_Regulator_Mode,
    Failed_Regulator_Mode
}

impl DataContent for isolette_Isolette_Data_Model_Regulator_Mode_Type {}

#[derive(Clone, Copy)]
pub struct isolette_Isolette_Data_Model_Failure_Flag_i {
  pub flag: bool 
}

impl Default for isolette_Isolette_Data_Model_Failure_Flag_i {
    fn default() -> Self {
        Self { flag: false }
    }
}

}