#![cfg_attr(not(test), no_std)]
#![allow(non_snake_case)]

use core::cell::OnceCell;
use log::{info, trace};

#[cfg(not(test))]
use {core::panic::PanicInfo, log::error};

use firewall_core::*;

mod config;

const NUM_MSGS: usize = 4;

#[cfg(not(test))]
mod bindings {
    use inspecta_types::BaseSwRawEthernetMessageImpl;
    extern "C" {
        pub fn get_EthernetFramesRxIn0(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn get_EthernetFramesRxIn1(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn get_EthernetFramesRxIn2(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn get_EthernetFramesRxIn3(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn EthernetFramesRxIn0_is_empty() -> bool;
        pub fn EthernetFramesRxIn1_is_empty() -> bool;
        pub fn EthernetFramesRxIn2_is_empty() -> bool;
        pub fn EthernetFramesRxIn3_is_empty() -> bool;
        pub fn put_EthernetFramesRxOut0(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn put_EthernetFramesRxOut1(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn put_EthernetFramesRxOut2(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn put_EthernetFramesRxOut3(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
    }
    pub struct EthChannelGet {
        pub get: unsafe extern "C" fn(*mut BaseSwRawEthernetMessageImpl) -> bool,
        pub empty: unsafe extern "C" fn() -> bool,
    }
    pub struct EthChannelPut {
        pub put: unsafe extern "C" fn(*mut BaseSwRawEthernetMessageImpl) -> bool,
    }
}

use inspecta_types::{BaseSwRawEthernetMessageImpl, BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE};

use bindings::*;

pub const IN_CHANNELS: [EthChannelGet; NUM_MSGS] = [
    EthChannelGet {
        get: get_EthernetFramesRxIn0,
        empty: EthernetFramesRxIn0_is_empty,
    },
    EthChannelGet {
        get: get_EthernetFramesRxIn1,
        empty: EthernetFramesRxIn1_is_empty,
    },
    EthChannelGet {
        get: get_EthernetFramesRxIn2,
        empty: EthernetFramesRxIn2_is_empty,
    },
    EthChannelGet {
        get: get_EthernetFramesRxIn3,
        empty: EthernetFramesRxIn3_is_empty,
    },
];
pub const OUT_CHANNELS: [EthChannelPut; NUM_MSGS] = [
    EthChannelPut {
        put: put_EthernetFramesRxOut0,
    },
    EthChannelPut {
        put: put_EthernetFramesRxOut1,
    },
    EthChannelPut {
        put: put_EthernetFramesRxOut2,
    },
    EthChannelPut {
        put: put_EthernetFramesRxOut3,
    },
];
static mut STATE: OnceCell<State> = OnceCell::new();

struct State {
    idx: usize,
}

impl State {
    fn new() -> State {
        State { idx: 0 }
    }

    fn idx_increment(&mut self) {
        self.idx = (self.idx + 1) % NUM_MSGS;
    }
}

#[no_mangle]
pub extern "C" fn seL4_RxFirewall_RxFirewall_initialize() {
    #[cfg(not(test))]
    config::log::LOGGER.set().unwrap();
    info!("Init");
    let state = State::new();

    unsafe {
        let _ = STATE.set(state);
    };
}

fn eth_get(idx: usize, rx_buf: &mut BaseSwRawEthernetMessageImpl) -> bool {
    let value = rx_buf.as_mut_ptr() as *mut BaseSwRawEthernetMessageImpl;
    let channel = &IN_CHANNELS[idx];
    #[allow(unused_unsafe)]
    unsafe {
        if !(channel.empty)() {
            (channel.get)(value);
            true
        } else {
            false
        }
    }
}

fn eth_put(state: &mut State, rx_buf: &mut BaseSwRawEthernetMessageImpl) {
    let value = rx_buf as *mut BaseSwRawEthernetMessageImpl;
    let channel = &OUT_CHANNELS[state.idx];
    #[allow(unused_unsafe)]
    unsafe {
        (channel.put)(value);
    };
    state.idx_increment();
}

fn firewall_rx(state: &mut State, frame: &mut BaseSwRawEthernetMessageImpl) {
    // RX path
    for i in 0..NUM_MSGS {
        let new_data = eth_get(i, frame);
        if !new_data {
            continue;
        }

        if can_send_rx_frame(frame) {
            eth_put(state, frame);
        }
    }
}

#[no_mangle]
pub extern "C" fn seL4_RxFirewall_RxFirewall_timeTriggered() {
    trace!("Triggered");
    let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
    let state = unsafe { STATE.get_mut().unwrap() };

    firewall_rx(state, &mut frame);
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
    seL4_RxFirewall_RxFirewall_initialize();

    let state = unsafe { STATE.get_mut().unwrap() };
    assert_eq!(state.idx, 0);
}

#[test]
fn state_increment_tests() {
    let mut state = State::new();
    assert_eq!(state.idx, 0);
    state.idx_increment();
    assert_eq!(state.idx, 1);
    state.idx_increment();
    assert_eq!(state.idx, 2);
    state.idx_increment();
    assert_eq!(state.idx, 3);
    state.idx_increment();
    assert_eq!(state.idx, 0);
    state.idx_increment();
    assert_eq!(state.idx, 1);
}

#[cfg(test)]
mod rx_ethernet_frames_tests {
    use super::*;

    mod get_in {
        use super::*;

        #[test]
        fn valid() {
            let mut rx_buf: BaseSwRawEthernetMessageImpl =
                [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];

            let res = eth_get(0, &mut rx_buf);
            assert!(res);
            let res = eth_get(1, &mut rx_buf);
            assert!(res);
            let res = eth_get(2, &mut rx_buf);
            assert!(res);
            let res = eth_get(3, &mut rx_buf);
            assert!(res);
            let res = eth_get(0, &mut rx_buf);
            assert!(res);
            let res = eth_get(1, &mut rx_buf);
            assert!(res);
        }

        #[test]
        #[should_panic]
        fn out_of_bounds() {
            let mut rx_buf: BaseSwRawEthernetMessageImpl =
                [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];

            let _ = eth_get(4, &mut rx_buf);
        }
    }

    mod put_out {
        use super::*;

        #[test]
        fn valid() {
            let mut state = State::new();
            let mut rx_buf: BaseSwRawEthernetMessageImpl =
                [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];

            assert_eq!(state.idx, 0);
            eth_put(&mut state, &mut rx_buf);
            assert_eq!(state.idx, 1);
            eth_put(&mut state, &mut rx_buf);
            assert_eq!(state.idx, 2);
            eth_put(&mut state, &mut rx_buf);
            assert_eq!(state.idx, 3);
            eth_put(&mut state, &mut rx_buf);
            assert_eq!(state.idx, 0);
            eth_put(&mut state, &mut rx_buf);
            assert_eq!(state.idx, 1);
        }

        #[test]
        #[should_panic]
        fn out_of_bounds() {
            let mut state = State::new();
            state.idx = 4;
            let mut rx_buf: BaseSwRawEthernetMessageImpl =
                [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];

            eth_put(&mut state, &mut rx_buf);
        }
    }
}

#[cfg(test)]
mod bindings {
    use super::*;
    pub fn get_EthernetFramesRxIn0(_value: *mut BaseSwRawEthernetMessageImpl) -> bool {
        true
    }
    pub fn get_EthernetFramesRxIn1(_value: *mut BaseSwRawEthernetMessageImpl) -> bool {
        true
    }

    pub fn get_EthernetFramesRxIn2(_value: *mut BaseSwRawEthernetMessageImpl) -> bool {
        true
    }

    pub fn get_EthernetFramesRxIn3(_value: *mut BaseSwRawEthernetMessageImpl) -> bool {
        true
    }
    pub fn EthernetFramesRxIn0_is_empty() -> bool {
        false
    }
    pub fn EthernetFramesRxIn1_is_empty() -> bool {
        false
    }
    pub fn EthernetFramesRxIn2_is_empty() -> bool {
        false
    }
    pub fn EthernetFramesRxIn3_is_empty() -> bool {
        false
    }
    pub fn put_EthernetFramesRxOut0(_value: *mut BaseSwRawEthernetMessageImpl) -> bool {
        true
    }
    pub fn put_EthernetFramesRxOut1(_value: *mut BaseSwRawEthernetMessageImpl) -> bool {
        true
    }
    pub fn put_EthernetFramesRxOut2(_value: *mut BaseSwRawEthernetMessageImpl) -> bool {
        true
    }
    pub fn put_EthernetFramesRxOut3(_value: *mut BaseSwRawEthernetMessageImpl) -> bool {
        true
    }
    pub struct EthChannelGet {
        pub get: fn(*mut BaseSwRawEthernetMessageImpl) -> bool,
        pub empty: fn() -> bool,
    }
    pub struct EthChannelPut {
        pub put: fn(*mut BaseSwRawEthernetMessageImpl) -> bool,
    }
}
