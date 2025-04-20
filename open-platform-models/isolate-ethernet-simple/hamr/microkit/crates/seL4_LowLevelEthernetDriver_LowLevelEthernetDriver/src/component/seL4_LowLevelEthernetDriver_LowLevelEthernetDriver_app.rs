#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

// This file will not be overwritten if codegen is rerun

use crate::data::*;
use crate::bridge::seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_api::*;
#[cfg(feature = "sel4")]
#[allow(unused_imports)]
use log::{error, warn, info, debug, trace};

pub struct seL4_LowLevelEthernetDriver_LowLevelEthernetDriver {
}

impl seL4_LowLevelEthernetDriver_LowLevelEthernetDriver {
  pub fn new() -> Self 
  {
    Self {
    }
  }

  pub fn initialize<API: seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Put_Api>(
    &mut self,
    api: &mut seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Application_Api<API>) 
  {
    #[cfg(feature = "sel4")]
    info!("initialize entrypoint invoked");
  }

  pub fn timeTriggered<API: seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Full_Api>(
    &mut self,
    api: &mut seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_Application_Api<API>) 
  {
    #[cfg(feature = "sel4")]
    info!("compute entrypoint invoked");
  }

  pub fn notify(
    &mut self,
    channel: microkit_channel) 
  {
    // this method is called when the monitor does not handle the passed in channel
    match channel {
      _ => {
        #[cfg(feature = "sel4")]
        warn!("Unexpected channel {}", channel)
      }
    }
  }
}
