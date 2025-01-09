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
    use super::NUM_MSGS;
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
    pub const FRAMES_RX_IN_FUNCS: [unsafe extern "C" fn(
        *mut BaseSwRawEthernetMessageImpl,
    ) -> bool; NUM_MSGS] = [
        get_EthernetFramesRxIn0,
        get_EthernetFramesRxIn1,
        get_EthernetFramesRxIn2,
        get_EthernetFramesRxIn3,
    ];
    pub const FRAMES_TX_IN_FUNCS: [unsafe extern "C" fn(
        *mut BaseSwRawEthernetMessageImpl,
    ) -> bool; NUM_MSGS] = [
        get_EthernetFramesTxIn0,
        get_EthernetFramesTxIn1,
        get_EthernetFramesTxIn2,
        get_EthernetFramesTxIn3,
    ];
    pub const FRAMES_TX_OUT_FUNCS: [unsafe extern "C" fn(
        *mut BaseSwSizedEthernetMessageImpl,
    ) -> bool; NUM_MSGS] = [
        put_EthernetFramesTxOut0,
        put_EthernetFramesTxOut1,
        put_EthernetFramesTxOut2,
        put_EthernetFramesTxOut3,
    ];
    pub const FRAMES_RX_OUT_FUNCS: [unsafe extern "C" fn(
        *mut BaseSwRawEthernetMessageImpl,
    ) -> bool; NUM_MSGS] = [
        put_EthernetFramesRxOut0,
        put_EthernetFramesRxOut1,
        put_EthernetFramesRxOut2,
        put_EthernetFramesRxOut3,
    ];
}

use inspecta_types::{
    BaseSwRawEthernetMessageImpl, BaseSwSizedEthernetMessageImpl,
    BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE,
};

use bindings::*;

static mut STATE: OnceCell<State> = OnceCell::new();

struct State {
    rx_idx: usize,
    tx_idx: usize,
}

impl State {
    fn new() -> State {
        State {
            rx_idx: 0,
            tx_idx: 0,
        }
    }

    fn rx_increment(&mut self) {
        self.rx_idx = (self.rx_idx + 1) % NUM_MSGS;
    }

    fn tx_increment(&mut self) {
        self.tx_idx = (self.tx_idx + 1) % NUM_MSGS;
    }
}

#[no_mangle]
pub extern "C" fn seL4_Firewall_Firewall_initialize() {
    #[cfg(not(test))]
    config::log::LOGGER.set().unwrap();
    info!("Init");
    let state = State::new();

    unsafe {
        let _ = STATE.set(state);
    };
}

fn get_rx_in(idx: usize, rx_buf: &mut BaseSwRawEthernetMessageImpl) -> bool {
    let value = rx_buf.as_mut_ptr() as *mut BaseSwRawEthernetMessageImpl;
    #[allow(unused_unsafe)]
    unsafe {
        FRAMES_RX_IN_FUNCS[idx](value)
    }
}

fn put_rx_out(state: &mut State, rx_buf: &mut BaseSwRawEthernetMessageImpl) {
    let value = rx_buf as *mut BaseSwRawEthernetMessageImpl;
    #[allow(unused_unsafe)]
    unsafe {
        FRAMES_RX_OUT_FUNCS[state.rx_idx](value)
    };
    state.rx_increment();
}

fn get_tx_in(idx: usize, tx_buf: &mut BaseSwRawEthernetMessageImpl) -> bool {
    let value = tx_buf.as_mut_ptr() as *mut BaseSwRawEthernetMessageImpl;
    #[allow(unused_unsafe)]
    unsafe {
        FRAMES_TX_IN_FUNCS[idx](value)
    }
}

fn put_tx_out(state: &mut State, tx_buf: &mut BaseSwSizedEthernetMessageImpl) {
    let value = tx_buf as *mut BaseSwSizedEthernetMessageImpl;
    #[allow(unused_unsafe)]
    unsafe {
        FRAMES_TX_OUT_FUNCS[state.tx_idx](value)
    };
    state.tx_increment();
}

fn firewall_rx(state: &mut State, frame: &mut BaseSwRawEthernetMessageImpl) {
    // RX path
    for i in 0..NUM_MSGS {
        let new_data = get_rx_in(i, frame);
        if !new_data {
            continue;
        }

        if can_send_rx_frame(frame) {
            put_rx_out(state, frame);
        }
    }
}

fn firewall_tx(state: &mut State, frame: &mut BaseSwRawEthernetMessageImpl) {
    for i in 0..NUM_MSGS {
        let new_data = get_tx_in(i, frame);
        if !new_data {
            continue;
        }

        if let Some(size) = can_send_tx_frame(frame) {
            let mut out = BaseSwSizedEthernetMessageImpl {
                size,
                message: *frame,
            };
            put_tx_out(state, &mut out);
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
fn state_increment_tests() {
    let mut state = State::new();
    assert_eq!(state.rx_idx, 0);
    assert_eq!(state.tx_idx, 0);
    state.rx_increment();
    assert_eq!(state.rx_idx, 1);
    assert_eq!(state.tx_idx, 0);
    state.rx_increment();
    assert_eq!(state.rx_idx, 2);
    assert_eq!(state.tx_idx, 0);
    state.rx_increment();
    assert_eq!(state.rx_idx, 3);
    assert_eq!(state.tx_idx, 0);
    state.rx_increment();
    assert_eq!(state.rx_idx, 0);
    assert_eq!(state.tx_idx, 0);
    state.tx_increment();
    assert_eq!(state.rx_idx, 0);
    assert_eq!(state.tx_idx, 1);
    state.tx_increment();
    assert_eq!(state.rx_idx, 0);
    assert_eq!(state.tx_idx, 2);
    state.tx_increment();
    assert_eq!(state.rx_idx, 0);
    assert_eq!(state.tx_idx, 3);
    state.tx_increment();
    assert_eq!(state.rx_idx, 0);
    assert_eq!(state.tx_idx, 0);
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

            let res = get_rx_in(0, &mut rx_buf);
            assert!(res);
            let res = get_rx_in(1, &mut rx_buf);
            assert!(res);
            let res = get_rx_in(2, &mut rx_buf);
            assert!(res);
            let res = get_rx_in(3, &mut rx_buf);
            assert!(res);
            let res = get_rx_in(0, &mut rx_buf);
            assert!(res);
            let res = get_rx_in(1, &mut rx_buf);
            assert!(res);
        }

        #[test]
        #[should_panic]
        fn out_of_bounds() {
            let mut rx_buf: BaseSwRawEthernetMessageImpl =
                [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];

            let _ = get_rx_in(4, &mut rx_buf);
        }
    }

    mod put_out {
        use super::*;

        #[test]
        fn valid() {
            let mut state = State::new();
            let mut rx_buf: BaseSwRawEthernetMessageImpl =
                [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];

            assert_eq!(state.rx_idx, 0);
            assert_eq!(state.tx_idx, 0);
            put_rx_out(&mut state, &mut rx_buf);
            assert_eq!(state.rx_idx, 1);
            assert_eq!(state.tx_idx, 0);
            put_rx_out(&mut state, &mut rx_buf);
            assert_eq!(state.rx_idx, 2);
            assert_eq!(state.tx_idx, 0);
            put_rx_out(&mut state, &mut rx_buf);
            assert_eq!(state.rx_idx, 3);
            assert_eq!(state.tx_idx, 0);
            put_rx_out(&mut state, &mut rx_buf);
            assert_eq!(state.rx_idx, 0);
            assert_eq!(state.tx_idx, 0);
            put_rx_out(&mut state, &mut rx_buf);
            assert_eq!(state.rx_idx, 1);
            assert_eq!(state.tx_idx, 0);
        }

        #[test]
        #[should_panic]
        fn out_of_bounds() {
            let mut state = State::new();
            state.rx_idx = 4;
            let mut rx_buf: BaseSwRawEthernetMessageImpl =
                [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];

            put_rx_out(&mut state, &mut rx_buf);
        }
    }
}

#[cfg(test)]
mod tx_ethernet_frames_tests {
    use super::*;

    mod get_in {
        use super::*;

        #[test]
        fn valid() {
            let mut tx_buf: BaseSwRawEthernetMessageImpl =
                [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];

            let res = get_tx_in(0, &mut tx_buf);
            assert!(res);
            let res = get_tx_in(1, &mut tx_buf);
            assert!(res);
            let res = get_tx_in(2, &mut tx_buf);
            assert!(res);
            let res = get_tx_in(3, &mut tx_buf);
            assert!(res);
            let res = get_tx_in(0, &mut tx_buf);
            assert!(res);
            let res = get_tx_in(1, &mut tx_buf);
            assert!(res);
        }

        #[test]
        #[should_panic]
        fn out_of_bounds() {
            let mut tx_buf: BaseSwRawEthernetMessageImpl =
                [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];

            let _ = get_tx_in(4, &mut tx_buf);
        }
    }

    mod put_out {
        use super::*;

        #[test]
        fn valid() {
            let mut state = State::new();
            let message: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];

            let mut tx_buf: BaseSwSizedEthernetMessageImpl =
                BaseSwSizedEthernetMessageImpl { message, size: 20 };

            assert_eq!(state.rx_idx, 0);
            assert_eq!(state.tx_idx, 0);
            put_tx_out(&mut state, &mut tx_buf);
            assert_eq!(state.rx_idx, 0);
            assert_eq!(state.tx_idx, 1);
            put_tx_out(&mut state, &mut tx_buf);
            assert_eq!(state.rx_idx, 0);
            assert_eq!(state.tx_idx, 2);
            put_tx_out(&mut state, &mut tx_buf);
            assert_eq!(state.rx_idx, 0);
            assert_eq!(state.tx_idx, 3);
            put_tx_out(&mut state, &mut tx_buf);
            assert_eq!(state.rx_idx, 0);
            assert_eq!(state.tx_idx, 0);
            put_tx_out(&mut state, &mut tx_buf);
            assert_eq!(state.rx_idx, 0);
            assert_eq!(state.tx_idx, 1);
        }

        #[test]
        #[should_panic]
        fn out_of_bounds() {
            let mut state = State::new();
            state.tx_idx = 4;
            let message: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];

            let mut tx_buf: BaseSwSizedEthernetMessageImpl =
                BaseSwSizedEthernetMessageImpl { message, size: 20 };

            put_tx_out(&mut state, &mut tx_buf);
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
    pub fn get_EthernetFramesTxIn0(_value: *mut BaseSwRawEthernetMessageImpl) -> bool {
        true
    }
    pub fn get_EthernetFramesTxIn1(_value: *mut BaseSwRawEthernetMessageImpl) -> bool {
        true
    }
    pub fn get_EthernetFramesTxIn2(_value: *mut BaseSwRawEthernetMessageImpl) -> bool {
        true
    }
    pub fn get_EthernetFramesTxIn3(_value: *mut BaseSwRawEthernetMessageImpl) -> bool {
        true
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
    pub fn put_EthernetFramesTxOut0(_value: *mut BaseSwSizedEthernetMessageImpl) -> bool {
        true
    }
    pub fn put_EthernetFramesTxOut1(_value: *mut BaseSwSizedEthernetMessageImpl) -> bool {
        true
    }
    pub fn put_EthernetFramesTxOut2(_value: *mut BaseSwSizedEthernetMessageImpl) -> bool {
        true
    }
    pub fn put_EthernetFramesTxOut3(_value: *mut BaseSwSizedEthernetMessageImpl) -> bool {
        true
    }
    pub const FRAMES_RX_IN_FUNCS: [fn(*mut BaseSwRawEthernetMessageImpl) -> bool; NUM_MSGS] = [
        get_EthernetFramesRxIn0,
        get_EthernetFramesRxIn1,
        get_EthernetFramesRxIn2,
        get_EthernetFramesRxIn3,
    ];
    pub const FRAMES_TX_IN_FUNCS: [fn(*mut BaseSwRawEthernetMessageImpl) -> bool; NUM_MSGS] = [
        get_EthernetFramesTxIn0,
        get_EthernetFramesTxIn1,
        get_EthernetFramesTxIn2,
        get_EthernetFramesTxIn3,
    ];
    pub const FRAMES_TX_OUT_FUNCS: [fn(*mut BaseSwSizedEthernetMessageImpl) -> bool; NUM_MSGS] = [
        put_EthernetFramesTxOut0,
        put_EthernetFramesTxOut1,
        put_EthernetFramesTxOut2,
        put_EthernetFramesTxOut3,
    ];
    pub const FRAMES_RX_OUT_FUNCS: [fn(*mut BaseSwRawEthernetMessageImpl) -> bool; NUM_MSGS] = [
        put_EthernetFramesRxOut0,
        put_EthernetFramesRxOut1,
        put_EthernetFramesRxOut2,
        put_EthernetFramesRxOut3,
    ];
}
