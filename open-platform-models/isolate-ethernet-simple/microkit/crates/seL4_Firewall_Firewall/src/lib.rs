#![cfg_attr(not(test), no_std)]

use core::cell::OnceCell;
use log::{debug, info, trace};

#[cfg(not(test))]
use {core::panic::PanicInfo, log::error};

use firewall_core::*;

mod config;

#[cfg(not(test))]
mod bindings {
    use inspecta_types::{BaseSwRawEthernetMessageImpl, BaseSwSizedEthernetMessageImpl};
    extern "C" {
        pub fn get_EthernetFramesRxIn0(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn get_EthernetFramesRxIn1(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn get_EthernetFramesRxIn2(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn get_EthernetFramesRxIn3(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn get_EthernetFramesTxIn0(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn get_EthernetFramesTxIn1(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn get_EthernetFramesTxIn2(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn get_EthernetFramesTxIn3(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn put_EthernetFramesRxOut0(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn put_EthernetFramesRxOut1(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn put_EthernetFramesRxOut2(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn put_EthernetFramesRxOut3(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn put_EthernetFramesTxOut0(value: *mut BaseSwSizedEthernetMessageImpl) -> bool;
        pub fn put_EthernetFramesTxOut1(value: *mut BaseSwSizedEthernetMessageImpl) -> bool;
        pub fn put_EthernetFramesTxOut2(value: *mut BaseSwSizedEthernetMessageImpl) -> bool;
        pub fn put_EthernetFramesTxOut3(value: *mut BaseSwSizedEthernetMessageImpl) -> bool;
    }
}

use inspecta_types::{
    BaseSwRawEthernetMessageImpl, BaseSwSizedEthernetMessageImpl,
    BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE,
};

#[cfg(not(test))]
use bindings::*;

#[cfg(test)]
use mock::*;

static mut STATE: OnceCell<State> = OnceCell::new();

struct State {
    rx_idx: u8,
    tx_idx: u8,
}

#[no_mangle]
pub extern "C" fn seL4_Firewall_Firewall_initialize() {
    #[cfg(not(test))]
    config::log::LOGGER.set().unwrap();
    info!("Init");
    let state = State {
        rx_idx: 0,
        tx_idx: 0,
    };

    unsafe {
        let _ = STATE.set(state);
    };
}

fn get_EthernetFramesRxIn(idx: u8, rx_buf: &mut BaseSwRawEthernetMessageImpl) -> bool {
    let value = rx_buf.as_mut_ptr() as *mut BaseSwRawEthernetMessageImpl;
    match idx {
        0 => unsafe { get_EthernetFramesRxIn0(value) },
        1 => unsafe { get_EthernetFramesRxIn1(value) },
        2 => unsafe { get_EthernetFramesRxIn2(value) },
        3 => unsafe { get_EthernetFramesRxIn3(value) },
        _ => false,
    }
}

fn put_EthernetFramesRxOut(state: &mut State, rx_buf: &mut BaseSwRawEthernetMessageImpl) {
    let value = rx_buf as *mut BaseSwRawEthernetMessageImpl;
    match state.rx_idx {
        0 => unsafe { put_EthernetFramesRxOut0(value) },
        1 => unsafe { put_EthernetFramesRxOut1(value) },
        2 => unsafe { put_EthernetFramesRxOut2(value) },
        3 => unsafe { put_EthernetFramesRxOut3(value) },
        _ => panic!(),
    };
    state.rx_idx = (state.rx_idx + 1) % 4;
}

fn get_EthernetFramesTxIn(idx: u8, tx_buf: &mut BaseSwRawEthernetMessageImpl) -> bool {
    let value = tx_buf.as_mut_ptr() as *mut BaseSwRawEthernetMessageImpl;
    match idx {
        0 => unsafe { get_EthernetFramesTxIn0(value) },
        1 => unsafe { get_EthernetFramesTxIn1(value) },
        2 => unsafe { get_EthernetFramesTxIn2(value) },
        3 => unsafe { get_EthernetFramesTxIn3(value) },
        _ => false,
    }
}

fn put_EthernetFramesTxOut(state: &mut State, tx_buf: &mut BaseSwSizedEthernetMessageImpl) {
    let value = tx_buf as *mut BaseSwSizedEthernetMessageImpl;
    match state.tx_idx {
        0 => unsafe { put_EthernetFramesTxOut0(value) },
        1 => unsafe { put_EthernetFramesTxOut1(value) },
        2 => unsafe { put_EthernetFramesTxOut2(value) },
        3 => unsafe { put_EthernetFramesTxOut3(value) },
        _ => panic!(),
    };
    state.tx_idx = (state.tx_idx + 1) % 4;
}

fn firewall_rx(state: &mut State, frame: &mut BaseSwRawEthernetMessageImpl) {
    // RX path
    for i in 0u8..4u8 {
        let new_data = get_EthernetFramesRxIn(i, frame);
        if !new_data {
            continue;
        }

        if can_send_rx_frame(frame) {
            put_EthernetFramesRxOut(state, frame);
        }
    }
}

fn firewall_tx(state: &mut State, frame: &mut BaseSwRawEthernetMessageImpl) {
    for i in 0u8..4u8 {
        let new_data = get_EthernetFramesTxIn(i, frame);
        if !new_data {
            continue;
        }

        if let Some(size) = can_send_tx_frame(frame) {
            let mut out = BaseSwSizedEthernetMessageImpl {
                size,
                message: *frame,
            };
            put_EthernetFramesTxOut(state, &mut out);
        }
    }
}

#[no_mangle]
pub extern "C" fn seL4_Firewall_Firewall_timeTriggered() {
    trace!("Triggered");
    let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
    let state = unsafe { STATE.get_mut().unwrap() };

    firewall_rx(state, &mut frame);
    firewall_tx(state, &mut frame);
}

// Need a Panic handler in a no_std environment
#[cfg(not(test))]
#[panic_handler]
fn panic(info: &PanicInfo) -> ! {
    error!("PANIC: {info:#?}");
    loop {}
}

#[test]
fn sel4_firewall_firewall_initialize_test() {
    seL4_Firewall_Firewall_initialize();

    let state = unsafe { STATE.get_mut().unwrap() };
    assert_eq!(state.rx_idx, 0);
    assert_eq!(state.tx_idx, 0);
}

#[test]
fn get_ethernet_frames_rx_in_test() {
    let mut rx_buf: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];

    let res = get_EthernetFramesRxIn(0, &mut rx_buf);
    assert!(res);
    let res = get_EthernetFramesRxIn(1, &mut rx_buf);
    assert!(res);
    let res = get_EthernetFramesRxIn(2, &mut rx_buf);
    assert!(res);
    let res = get_EthernetFramesRxIn(3, &mut rx_buf);
    assert!(res);
    let res = get_EthernetFramesRxIn(4, &mut rx_buf);
    assert!(!res);
}

#[test]
fn put_ethernet_frames_rx_out_valid_test() {
    let mut state = State {
        rx_idx: 0,
        tx_idx: 0,
    };
    let mut rx_buf: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];

    assert_eq!(state.rx_idx, 0);
    assert_eq!(state.tx_idx, 0);
    put_EthernetFramesRxOut(&mut state, &mut rx_buf);
    assert_eq!(state.rx_idx, 1);
    assert_eq!(state.tx_idx, 0);
    put_EthernetFramesRxOut(&mut state, &mut rx_buf);
    assert_eq!(state.rx_idx, 2);
    assert_eq!(state.tx_idx, 0);
    put_EthernetFramesRxOut(&mut state, &mut rx_buf);
    assert_eq!(state.rx_idx, 3);
    assert_eq!(state.tx_idx, 0);
    put_EthernetFramesRxOut(&mut state, &mut rx_buf);
    assert_eq!(state.rx_idx, 0);
    assert_eq!(state.tx_idx, 0);
    put_EthernetFramesRxOut(&mut state, &mut rx_buf);
    assert_eq!(state.rx_idx, 1);
    assert_eq!(state.tx_idx, 0);
}

#[test]
#[should_panic]
fn put_ethernet_frames_rx_out_bad_state_test() {
    let mut state = State {
        rx_idx: 4,
        tx_idx: 0,
    };
    let mut rx_buf: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];

    put_EthernetFramesRxOut(&mut state, &mut rx_buf);
}

#[test]
fn get_ethernet_frames_tx_in_test() {
    let mut tx_buf: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];

    let res = get_EthernetFramesTxIn(0, &mut tx_buf);
    assert!(res);
    let res = get_EthernetFramesTxIn(1, &mut tx_buf);
    assert!(res);
    let res = get_EthernetFramesTxIn(2, &mut tx_buf);
    assert!(res);
    let res = get_EthernetFramesTxIn(3, &mut tx_buf);
    assert!(res);
    let res = get_EthernetFramesTxIn(4, &mut tx_buf);
    assert!(!res);
}

#[test]
fn put_ethernet_frames_tx_out_valid_test() {
    let mut state = State {
        rx_idx: 0,
        tx_idx: 0,
    };
    let message: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];

    let mut tx_buf: BaseSwSizedEthernetMessageImpl =
        BaseSwSizedEthernetMessageImpl { message, size: 20 };

    assert_eq!(state.rx_idx, 0);
    assert_eq!(state.tx_idx, 0);
    put_EthernetFramesTxOut(&mut state, &mut tx_buf);
    assert_eq!(state.rx_idx, 0);
    assert_eq!(state.tx_idx, 1);
    put_EthernetFramesTxOut(&mut state, &mut tx_buf);
    assert_eq!(state.rx_idx, 0);
    assert_eq!(state.tx_idx, 2);
    put_EthernetFramesTxOut(&mut state, &mut tx_buf);
    assert_eq!(state.rx_idx, 0);
    assert_eq!(state.tx_idx, 3);
    put_EthernetFramesTxOut(&mut state, &mut tx_buf);
    assert_eq!(state.rx_idx, 0);
    assert_eq!(state.tx_idx, 0);
    put_EthernetFramesTxOut(&mut state, &mut tx_buf);
    assert_eq!(state.rx_idx, 0);
    assert_eq!(state.tx_idx, 1);
}

#[test]
#[should_panic]
fn put_ethernet_frames_tx_out_bad_state_test() {
    let mut state = State {
        rx_idx: 0,
        tx_idx: 4,
    };
    let message: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];

    let mut tx_buf: BaseSwSizedEthernetMessageImpl =
        BaseSwSizedEthernetMessageImpl { message, size: 20 };

    put_EthernetFramesTxOut(&mut state, &mut tx_buf);
}

#[cfg(test)]
mod mock {
    use super::*;
    pub fn get_EthernetFramesRxIn0(value: *mut BaseSwRawEthernetMessageImpl) -> bool {
        true
    }
    pub fn get_EthernetFramesRxIn1(value: *mut BaseSwRawEthernetMessageImpl) -> bool {
        true
    }

    pub fn get_EthernetFramesRxIn2(value: *mut BaseSwRawEthernetMessageImpl) -> bool {
        true
    }

    pub fn get_EthernetFramesRxIn3(value: *mut BaseSwRawEthernetMessageImpl) -> bool {
        true
    }
    pub fn get_EthernetFramesTxIn0(value: *mut BaseSwRawEthernetMessageImpl) -> bool {
        true
    }
    pub fn get_EthernetFramesTxIn1(value: *mut BaseSwRawEthernetMessageImpl) -> bool {
        true
    }
    pub fn get_EthernetFramesTxIn2(value: *mut BaseSwRawEthernetMessageImpl) -> bool {
        true
    }
    pub fn get_EthernetFramesTxIn3(value: *mut BaseSwRawEthernetMessageImpl) -> bool {
        true
    }
    pub fn put_EthernetFramesRxOut0(value: *mut BaseSwRawEthernetMessageImpl) -> bool {
        true
    }
    pub fn put_EthernetFramesRxOut1(value: *mut BaseSwRawEthernetMessageImpl) -> bool {
        true
    }
    pub fn put_EthernetFramesRxOut2(value: *mut BaseSwRawEthernetMessageImpl) -> bool {
        true
    }
    pub fn put_EthernetFramesRxOut3(value: *mut BaseSwRawEthernetMessageImpl) -> bool {
        true
    }
    pub fn put_EthernetFramesTxOut0(value: *mut BaseSwSizedEthernetMessageImpl) -> bool {
        true
    }
    pub fn put_EthernetFramesTxOut1(value: *mut BaseSwSizedEthernetMessageImpl) -> bool {
        true
    }
    pub fn put_EthernetFramesTxOut2(value: *mut BaseSwSizedEthernetMessageImpl) -> bool {
        true
    }
    pub fn put_EthernetFramesTxOut3(value: *mut BaseSwSizedEthernetMessageImpl) -> bool {
        true
    }
}
