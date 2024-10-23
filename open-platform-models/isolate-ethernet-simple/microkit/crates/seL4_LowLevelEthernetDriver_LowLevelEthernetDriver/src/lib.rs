// use core::cell::RefCell;

// use std::sync::{LazyLock, Mutex};
// static VALUE: LazyLock<Mutex<Vec<u8>>> = LazyLock::new(|| Mutex::new(vec![]));

// use core::cell::LazyCell;

// static VALUE: LazyCell<Mutex<Vec<u8>>> = LazyCell::new(|| Mutex::new(vec![]));

// #[no_mangle]
// pub extern "C" fn seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_initialize() {
//     println!("Rust: Initialize");
//     VALUE.lock().unwrap().push(1);
// }

// #[no_mangle]
// pub extern "C" fn seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_timeTriggered() {
//     print!("Rust: ");
//     {
//         let mut v = VALUE.lock().unwrap();
//         let last = v.last().unwrap().clone() + 1;
//         println!("Updating value: {last}");
//         v.push(last);
//     }
//     let val_ptr = VALUE.lock().unwrap().as_mut_ptr();
//     unsafe {
//         putEthernetFramesRx(val_ptr);
//     }
// }
#![no_std]

use sel4::debug_println;

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

// #![feature(thread_local)]
// #![feature(slice_pattern)]
// use core::cell::RefCell;

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

// #[thread_local]
// static VALUE: RefCell<BaseSwRawEthernetMessageImpl> =
//     RefCell::new([0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE]);

// #[no_mangle]
// pub extern "C" fn seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_initialize() {
//     let mut rx = VALUE.borrow_mut();
//     rx.iter_mut().for_each(|x| *x = DEFAULT_RX);
// }

// #[no_mangle]
// pub extern "C" fn seL4_LowLevelEthernetDriver_LowLevelEthernetDriver_timeTriggered() {
//     {
//         let mut tx: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
//         unsafe { getEthernetFramesTx(tx.as_mut_ptr()) };
//         let (int_bytes, _rest) = tx.split_at(core::mem::size_of::<u32>());
//         let _val = u32::from_be_bytes(int_bytes.try_into().unwrap());
//         // println!("TX Received: {val}");
//     }
//     {
//         let mut ptr = VALUE.borrow_mut();
//         let val = ptr.get_mut(0).unwrap();
//         let new_val = if *val > 0 { *val - 1 } else { DEFAULT_RX };
//         *val = new_val;
//         unsafe { putEthernetFramesRx(ptr.as_mut_ptr()) };
//         // println!("RX Sent {new_val}");
//     }
// }

use core::panic::PanicInfo;
#[panic_handler]
fn panic(_info: &PanicInfo) -> ! {
    loop {}
}
