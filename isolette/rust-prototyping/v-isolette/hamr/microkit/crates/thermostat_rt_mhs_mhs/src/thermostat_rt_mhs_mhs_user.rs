#![cfg_attr(not(test), no_std)]
#![allow(non_snake_case)]
#![allow(non_upper_case_globals)]
#![allow(static_mut_refs)]

#[allow(unused_imports)]
use log::{error, warn, info, debug, trace};

mod logging;
mod api;
mod types;

use crate::types::sb_aadl_types::*;
use crate::types::sb_microkit_types::*;

static mut lastCmd: isolette_Isolette_Data_Model_On_Off_Type = isolette_Isolette_Data_Model_On_Off_Type::Onn;

#[no_mangle]
pub extern "C" fn thermostat_rt_mhs_mhs_initialize() {
  #[cfg(not(test))]
  logging::LOGGER.set().unwrap();

  info!("initialize invoked");

  unsafe { 
    // REQ-MHS-1: If the Regulator Mode is INIT, the Heat Control shall be
    // set to Off
    
    lastCmd = isolette_Isolette_Data_Model_On_Off_Type::Off; 
    api::put_heat_control(&mut lastCmd);
  }
}

#[no_mangle]
pub extern "C" fn thermostat_rt_mhs_mhs_timeTriggered() {
  info!("timeTriggered invoked!");

  let mut lower = isolette_Isolette_Data_Model_Temp_i { degrees: 0 };
  api::unsafe_get_lower_desired_temp(&mut lower);
  
  let mut upper = isolette_Isolette_Data_Model_Temp_i { degrees: 0 };
  api::unsafe_get_upper_desired_temp(&mut upper);

  let mut regulator_mode = isolette_Isolette_Data_Model_Regulator_Mode_Type::Init_Regulator_Mode;
  api::unsafe_get_regulator_mode(&mut regulator_mode);

  let mut currentTemp = isolette_Isolette_Data_Model_TempWstatus_i { 
     degrees: 0, status:
     isolette_Isolette_Data_Model_ValueStatus_Type::Valid };
  api::unsafe_get_current_tempWstatus(&mut currentTemp);
  
  info!("lower_desired_temp = {lower:?}");
  info!("upper_desired_temp = {upper:?}");

  #[allow(unused_assignments)]
  let mut currentCmd = isolette_Isolette_Data_Model_On_Off_Type::Off;
  unsafe {
    currentCmd = lastCmd;
  }

  match regulator_mode {
    isolette_Isolette_Data_Model_Regulator_Mode_Type::Init_Regulator_Mode => 
      currentCmd = isolette_Isolette_Data_Model_On_Off_Type::Off,

      isolette_Isolette_Data_Model_Regulator_Mode_Type:: Normal_Regulator_Mode => {
      if currentTemp.degrees > upper.degrees {
        currentCmd = isolette_Isolette_Data_Model_On_Off_Type::Off;
      } else if currentTemp.degrees < lower.degrees {
        currentCmd = isolette_Isolette_Data_Model_On_Off_Type::Onn;
      }
    },

    isolette_Isolette_Data_Model_Regulator_Mode_Type::Failed_Regulator_Mode =>
      currentCmd = isolette_Isolette_Data_Model_On_Off_Type::Off,
  }

  api::unsafe_put_heat_control(&mut currentCmd);

  unsafe {
    lastCmd = currentCmd;
  }
}

#[no_mangle]
pub extern "C" fn thermostat_rt_mhs_mhs_notify(channel: microkit_channel) {
  // this method is called when the monitor does not handle the passed in channel
  match channel {
      _ => warn!("Unexpected channel {}", channel)
  }
}

// Need a Panic handler in a no_std environment
#[cfg(not(test))]
#[panic_handler]
fn panic(info: &core::panic::PanicInfo) -> ! {
  error!("PANIC: {info:#?}");
  loop {}
}
