#![cfg_attr(not(test), no_std)]
#![allow(non_snake_case)]

#[allow(unused_imports)]
use log::{error, warn, info, debug, trace};

mod logging;
mod api;
use types::*;

#[no_mangle]
pub extern "C" fn consumer_consumer_initialize() {
  #[cfg(not(test))]
  logging::LOGGER.set().unwrap();

  info!("initialize invoked");
}

#[no_mangle]
pub extern "C" fn consumer_consumer_timeTriggered() {
  info!("timeTriggered invoked");
}

#[no_mangle]
pub extern "C" fn consumer_consumer_notify(channel: microkit_channel) {
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
