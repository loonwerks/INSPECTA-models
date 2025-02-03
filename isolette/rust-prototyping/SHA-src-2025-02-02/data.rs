use crate::art::DataContent;

#[derive(Clone, Default)]
pub enum OnOff {
    Onn,
    #[default] Off
}

impl DataContent for OnOff {}

#[derive(Clone)]
enum ValueStatus {
    Valid,
    Invalid
  }

impl DataContent for ValueStatus {}

#[derive(Clone)]
pub struct TempWstatus_i {
    pub degrees: u32,
    status: ValueStatus
}

impl DataContent for TempWstatus_i {}


pub struct Temp_i {
    pub degrees: u32 // should be f32
}
  
#[derive(Clone)]
pub enum Regulator_Mode {
    Init_Regulator_Mode,
    Normal_Regulator_Mode,
    Failed_Regulator_Mode
}

impl DataContent for Regulator_Mode {}