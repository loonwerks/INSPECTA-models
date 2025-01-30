#![cfg_attr(not(test), no_std)]

#[allow(unused_imports)]
use log::{error, warn, info, debug, trace};

mod logging;
mod api;
use types::*;

#[no_mangle]
pub extern "C" fn consumer_p_p_consumer_initialize() {
    #[cfg(not(test))]
    logging::LOGGER.set().unwrap();

    info!("initialize called");
}

#[no_mangle]
pub extern "C" fn consumer_p_p_consumer_timeTriggered() {
   //info!("timeTriggered called");

    let mut data = base_event_data_2_prod_2_cons_struct_i {
        size: 0,
        elements: [0; BASE_EVENT_DATA_2_PROD_2_CONS_ARRAY_OF_INTS_DIM],
    };

    if unsafe { api::get_read_port1(&mut data) } {
        info!("received {:?} on read port 1", data);
    } else {
        info!("nothing received on read port 1");
    }

    if api::c_get_read_port2(&mut data) {
        info!("received {:?} on read port 2", data);
    } else {
        info!("nothing received on read port 2");
    }
}

#[no_mangle]
pub extern "C" fn consumer_p_p_consumer_notify(channel: microkit_channel) {
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

