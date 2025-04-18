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

use data::Isolette_Data_Model::*;
use data::sb_microkit_types::*;

use crate::bridge::manage_regulator_interface_api::{self as api, *};
use crate::component::manage_regulator_interface_app::*;

#[cfg(feature = "sel4")] #[allow(unused_imports)]
use log::{error, warn, info, debug, trace};

// TODO: why does verus require a main method?
fn main() {}

static mut mri: Manage_Regulator_Interface = Manage_Regulator_Interface::new();
static mut init_api: Manage_Regulator_Interface_Application_Api<MRI_Initialization_Api> = api::init_api();
static mut compute_api: Manage_Regulator_Interface_Application_Api<MRI_Compute_Api> = api::compute_api();

#[no_mangle]
pub extern "C" fn thermostat_rt_mri_mri_initialize() {
  #[cfg(not(test))] 
  #[cfg(feature = "sel4")] 
  logging::LOGGER.set().unwrap();
  
  #[cfg(feature = "sel4")] 
  info!("VERUS initialize invoked");

  unsafe {
    mri.initialise(&mut init_api);
  }
}

#[no_mangle]
pub extern "C" fn thermostat_rt_mri_mri_timeTriggered() {
    #[cfg(feature = "sel4")] 
    info!("VERUS timeTriggered invoke");

    unsafe {
        mri.timeTriggered(&mut compute_api);
    }
}

#[no_mangle]
pub extern "C" fn thermostat_rt_mri_mri_notify(channel: microkit_channel) {
  // this method is called when the monitor does not handle the passed in channel
  match channel {
      _ => {
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