use vstd::prelude::*;
// use vstd::invariant::*;

use crate::art::DataContent;

verus! {

#[derive(Copy, Clone, Default)]
pub enum OnOff {
    Onn,
    #[default] Off
}

impl DataContent for OnOff {}

#[derive(Copy, Clone, Default)]
pub enum ValueStatus {
    #[default] Valid,
    Invalid
  }

impl DataContent for ValueStatus {}

#[derive(Copy, Clone)]
pub struct TempWstatus_i {
    pub degrees: u32,
    pub status: ValueStatus
}

impl Default for TempWstatus_i {
    fn default() -> Self {
        Self { degrees: 0, status: ValueStatus::Valid }
    }
}

impl DataContent for TempWstatus_i {}

#[derive(Copy, Clone)]
pub struct Temp_i {
    pub degrees: u32 // should be f32
}

impl Default for Temp_i {
    fn default() -> Self {
        Self { degrees: 0 }
    }
}
  
#[derive(Copy, Clone, PartialEq, Eq, Default)]
pub enum Regulator_Mode {
    Init_Regulator_Mode,
    #[default] Normal_Regulator_Mode,
    Failed_Regulator_Mode
}

impl DataContent for Regulator_Mode {}

}