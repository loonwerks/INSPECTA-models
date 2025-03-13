#![cfg_attr(not(test), no_std)]

#![allow(non_camel_case_types)]
#![allow(non_upper_case_globals)]
#![allow(non_snake_case)]
#![allow(unused_parens)]
#![allow(unused_imports)]
#![allow(static_mut_refs)]

mod art;
mod bridge;
mod component;
mod logging;
mod data;
#[cfg(test)]
mod tests;

use data::Isolette_Data_Model::*;
use data::sb_microkit_types::*;
use crate::component::thermostat_rt_mhs_mhs_app::*;
use crate::bridge::manage_heat_source_api::{self as api, *};

#[cfg(feature = "sel4")] #[allow(unused_imports)]
use log::{error, warn, info, debug, trace};

// TODO: why does verus require a main method?
fn main() {}

static mut mhs: Manage_Heat_Source_i_thermostat_rt = Manage_Heat_Source_i_thermostat_rt::new();
static mut init_api: Manage_Heat_Source_i_Application_Api<MHS_Initialization_Api> = api::init_api();
static mut compute_api: Manage_Heat_Source_i_Application_Api<MHS_Compute_Api> = api::compute_api();

#[no_mangle]
pub extern "C" fn thermostat_rt_mhs_mhs_initialize() {
    #[cfg(not(test))] 
    #[cfg(feature = "sel4")] 
    logging::LOGGER.set().unwrap();
    
    #[cfg(feature = "sel4")] 
    info!("initialize invoked");
  
    unsafe {
      mhs.initialise(&mut init_api);
    }
}

#[no_mangle]
pub extern "C" fn thermostat_rt_mhs_mhs_timeTriggered() {
    #[cfg(feature = "sel4")] 
    info!("timeTriggered invoke");

    unsafe {
        mhs.timeTriggered(&mut compute_api);
    }
}

#[no_mangle]
pub extern "C" fn thermostat_rt_mhs_mhs_notify(channel: microkit_channel) {
  // this method is called when the monitor does not handle the passed in channel
  match channel {
      _ =>  {
        #[cfg(feature = "sel4")] 
        warn!("Unexpected channel {}", channel)
      }
  }
}

// Need a Panic handler in a no_std environment
#[panic_handler] 
#[cfg(feature = "sel4")] 
#[cfg(not(test))]
fn panic(info: &core::panic::PanicInfo) -> ! {
  error!("PANIC: {info:#?}");
  loop {}
}