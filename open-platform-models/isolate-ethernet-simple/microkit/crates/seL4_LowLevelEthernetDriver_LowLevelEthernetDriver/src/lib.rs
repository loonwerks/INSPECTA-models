#![no_std]

use sel4::debug_println;

mod bindings {
    pub const BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE: usize = 1600;
    pub type BaseSwRawEthernetMessageImpl = [u8; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
    extern "C" {
        pub fn putEthernetFramesRx(value: *mut u8);
    }
    extern "C" {
        pub fn getEthernetFramesTx(value: *mut u8);
    }
}
use bindings::{
    getEthernetFramesTx, putEthernetFramesRx, BaseSwRawEthernetMessageImpl,
    BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE,
};

static mut VALUE: BaseSwRawEthernetMessageImpl = [0u8; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
const DEFAULT_RX: u8 = 100;

#[no_mangle]
pub unsafe extern "C" fn seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_initialize() {
    debug_println!("LowLevel_Driver: Init");
    *VALUE.get_mut(3).unwrap() = DEFAULT_RX;
}

#[no_mangle]
pub extern "C" fn seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_timeTriggered() {
    debug_println!("-------LowLevel_Driver------");
    {
        let mut tx: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
        unsafe { getEthernetFramesTx(tx.as_mut_ptr()) };
        let (int_bytes, _rest) = tx.split_at(core::mem::size_of::<u32>());
        let val = u32::from_be_bytes(int_bytes.try_into().unwrap());
        debug_println!("TX Received: {val}");
    }
    {
        let val = unsafe { VALUE.get_mut(3).unwrap() };
        let new_val = if *val > 0 { *val - 1 } else { DEFAULT_RX };
        *val = new_val;
        unsafe { putEthernetFramesRx(VALUE.as_mut_ptr()) };
        debug_println!("RX Sent {new_val}");
    }
}

// Need a Panic handler in a no_std environment
use core::panic::PanicInfo;
#[panic_handler]
fn panic(info: &PanicInfo) -> ! {
    debug_println!("PANIC: {info:#?}");
    loop {}
}
