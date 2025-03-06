#![cfg_attr(not(test), no_std)]
#![allow(non_snake_case)]
#![allow(non_upper_case_globals)]
#![allow(static_mut_refs)]

#[allow(unused_imports)]
use log::{error, warn, info, debug, trace};

mod logging;
mod api;

use types::Isolette_Data_Model::*;
use types::sb_microkit_types::*;

static mut lastCmd: On_Off = On_Off::Onn;

#[no_mangle]
pub extern "C" fn thermostat_rt_mhs_mhs_initialize() {
  #[cfg(not(test))]
  logging::LOGGER.set().unwrap();

  info!("initialize invoked");

  unsafe { 
    // REQ-MHS-1: If the Regulator Mode is INIT, the Heat Control shall be
    // set to Off
    
    lastCmd = On_Off::Off; 
    api::put_heat_control(&mut lastCmd);
  }
}

#[no_mangle]
pub extern "C" fn thermostat_rt_mhs_mhs_timeTriggered() {
  info!("timeTriggered invoked!");

  let mut lower = Temp_i { degrees: 0 };
  api::unsafe_get_lower_desired_temp(&mut lower);
  
  let mut upper = Temp_i { degrees: 0 };
  api::unsafe_get_upper_desired_temp(&mut upper);

  let mut regulator_mode = Regulator_Mode::Init_Regulator_Mode;
  api::unsafe_get_regulator_mode(&mut regulator_mode);

  let mut currentTemp = TempWstatus_i { 
     degrees: 0, status:
     ValueStatus::Valid };
  api::unsafe_get_current_tempWstatus(&mut currentTemp);
  
  info!("lower_desired_temp = {lower:?}");
  info!("upper_desired_temp = {upper:?}");

  #[allow(unused_assignments)]
  let mut currentCmd = On_Off::Off;
  unsafe {
    currentCmd = lastCmd;
  }

  match regulator_mode {
    Regulator_Mode::Init_Regulator_Mode => 
      currentCmd = On_Off::Off,

      Regulator_Mode:: Normal_Regulator_Mode => {
      if currentTemp.degrees > upper.degrees {
        currentCmd = On_Off::Off;
      } else if currentTemp.degrees < lower.degrees {
        currentCmd = On_Off::Onn;
      }
    },

    Regulator_Mode::Failed_Regulator_Mode =>
      currentCmd = On_Off::Off,
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
