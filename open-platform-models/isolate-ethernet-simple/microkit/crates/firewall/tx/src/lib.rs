#![cfg_attr(not(test), no_std)]
#![allow(non_snake_case)]

use core::cell::OnceCell;
use log::{info, trace};

#[cfg(not(test))]
use {core::panic::PanicInfo, log::error};

use firewall_core::{Arp, EthFrame, EthernetRepr, PacketType};

mod config;

const NUM_MSGS: usize = 4;

#[cfg(not(test))]
mod bindings {
    use inspecta_types::{BaseSwRawEthernetMessageImpl, BaseSwSizedEthernetMessageImpl};
    extern "C" {
        pub fn get_EthernetFramesTxIn0(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn get_EthernetFramesTxIn1(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn get_EthernetFramesTxIn2(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn get_EthernetFramesTxIn3(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
        pub fn EthernetFramesTxIn0_is_empty() -> bool;
        pub fn EthernetFramesTxIn1_is_empty() -> bool;
        pub fn EthernetFramesTxIn2_is_empty() -> bool;
        pub fn EthernetFramesTxIn3_is_empty() -> bool;
        pub fn put_EthernetFramesTxOut0(value: *mut BaseSwSizedEthernetMessageImpl) -> bool;
        pub fn put_EthernetFramesTxOut1(value: *mut BaseSwSizedEthernetMessageImpl) -> bool;
        pub fn put_EthernetFramesTxOut2(value: *mut BaseSwSizedEthernetMessageImpl) -> bool;
        pub fn put_EthernetFramesTxOut3(value: *mut BaseSwSizedEthernetMessageImpl) -> bool;
    }
    pub struct EthChannelGet {
        pub get: unsafe extern "C" fn(*mut BaseSwRawEthernetMessageImpl) -> bool,
        pub empty: unsafe extern "C" fn() -> bool,
    }
    pub struct EthChannelPut {
        pub put: unsafe extern "C" fn(*mut BaseSwSizedEthernetMessageImpl) -> bool,
    }
}

use inspecta_types::{
    BaseSwRawEthernetMessageImpl, BaseSwSizedEthernetMessageImpl,
    BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE,
};

use bindings::*;

pub const IN_CHANNELS: [EthChannelGet; NUM_MSGS] = [
    EthChannelGet {
        get: get_EthernetFramesTxIn0,
        empty: EthernetFramesTxIn0_is_empty,
    },
    EthChannelGet {
        get: get_EthernetFramesTxIn1,
        empty: EthernetFramesTxIn1_is_empty,
    },
    EthChannelGet {
        get: get_EthernetFramesTxIn2,
        empty: EthernetFramesTxIn2_is_empty,
    },
    EthChannelGet {
        get: get_EthernetFramesTxIn3,
        empty: EthernetFramesTxIn3_is_empty,
    },
];
pub const OUT_CHANNELS: [EthChannelPut; NUM_MSGS] = [
    EthChannelPut {
        put: put_EthernetFramesTxOut0,
    },
    EthChannelPut {
        put: put_EthernetFramesTxOut1,
    },
    EthChannelPut {
        put: put_EthernetFramesTxOut2,
    },
    EthChannelPut {
        put: put_EthernetFramesTxOut3,
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
pub extern "C" fn seL4_TxFirewall_TxFirewall_initialize() {
    #[cfg(not(test))]
    config::log::LOGGER.set().unwrap();
    info!("Init");
    let state = State::new();

    unsafe {
        let _ = STATE.set(state);
    };
}

fn eth_get(idx: usize, tx_buf: &mut BaseSwRawEthernetMessageImpl) -> bool {
    let value = tx_buf.as_mut_ptr() as *mut BaseSwRawEthernetMessageImpl;
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

fn eth_put(state: &mut State, tx_buf: &mut BaseSwSizedEthernetMessageImpl) {
    let value = tx_buf as *mut BaseSwSizedEthernetMessageImpl;
    let channel = &OUT_CHANNELS[state.idx];
    #[allow(unused_unsafe)]
    unsafe {
        (channel.put)(value);
    };
    state.idx_increment();
}

fn can_send_frame(frame: &mut [u8]) -> Option<u16> {
    let Some(packet) = EthFrame::parse(frame) else {
        info!("Malformed packet. Throw it away.");
        return None;
    };

    let size = match packet.eth_type {
        PacketType::Arp(_) => {
            let size = 64u16;
            // TODO: Do we need this now that linux is constructing it?
            frame[EthernetRepr::SIZE + Arp::SIZE..size as usize].fill(0);
            size
        }
        PacketType::Ipv4(ip) => ip.header.length + EthernetRepr::SIZE as u16,
        PacketType::Ipv6 => {
            info!("Not an IPv4 or Arp packet. Throw it away.");
            return None;
        }
    };

    Some(size)
}

fn firewall(state: &mut State, frame: &mut BaseSwRawEthernetMessageImpl) {
    for i in 0..NUM_MSGS {
        let new_data = eth_get(i, frame);
        if !new_data {
            continue;
        }

        if let Some(size) = can_send_frame(frame) {
            let mut out = BaseSwSizedEthernetMessageImpl {
                size,
                message: *frame,
            };
            eth_put(state, &mut out);
        }
    }
}

#[no_mangle]
pub extern "C" fn seL4_TxFirewall_TxFirewall_timeTriggered() {
    trace!("Triggered");
    let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
    let state = unsafe { STATE.get_mut().unwrap() };

    firewall(state, &mut frame);
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
    seL4_TxFirewall_TxFirewall_initialize();

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
mod can_send_tx_frame_tests {
    use super::*;

    #[test]
    fn valid_arp() {
        let mut frame = [0u8; 128];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x06, 0x0,
            0x1, 0x8, 0x0, 0x6, 0x4, 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0xc0, 0xa8, 0x0, 0x1,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xc0, 0xa8, 0x0, 0xce,
        ];
        frame[0..42].copy_from_slice(&pkt);
        let res = can_send_frame(&mut frame);
        assert!(res.is_some());
        assert_eq!(res.unwrap(), 64);
    }

    #[test]
    fn disallowed_ipv6() {
        let mut frame = [0u8; 128];
        // IPv6 Frame
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x86, 0xdd,
        ];
        frame[0..14].copy_from_slice(&pkt);
        let res = can_send_frame(&mut frame);
        assert!(res.is_none());
    }

    #[test]
    fn valid_ipv4() {
        let mut frame = [0u8; 128];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00, 0x45,
            0x0, 0x0, 0x20, 0x15, 0x5f, 0x40, 0x0, 0x80, 0x0, 0xf7, 0x28, 0xc0, 0xa8, 0x0, 0xce,
            0x34, 0x7f, 0xf8, 0x51,
        ];
        frame[0..34].copy_from_slice(&pkt);
        let res = can_send_frame(&mut frame);
        assert!(res.is_some());
        assert_eq!(res.unwrap(), 46);

        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00, 0x45,
            0x0, 0x0, 0x33, 0x15, 0x5f, 0x40, 0x0, 0x80, 0x1, 0xf7, 0x28, 0xc0, 0xa8, 0x0, 0xce,
            0x34, 0x7f, 0xf8, 0x51,
        ];
        frame[0..34].copy_from_slice(&pkt);
        let res = can_send_frame(&mut frame);
        assert!(res.is_some());
        assert_eq!(res.unwrap(), 65);
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

            let res = eth_get(0, &mut tx_buf);
            assert!(res);
            let res = eth_get(1, &mut tx_buf);
            assert!(res);
            let res = eth_get(2, &mut tx_buf);
            assert!(res);
            let res = eth_get(3, &mut tx_buf);
            assert!(res);
            let res = eth_get(0, &mut tx_buf);
            assert!(res);
            let res = eth_get(1, &mut tx_buf);
            assert!(res);
        }

        #[test]
        #[should_panic]
        fn out_of_bounds() {
            let mut tx_buf: BaseSwRawEthernetMessageImpl =
                [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];

            let _ = eth_get(4, &mut tx_buf);
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

            assert_eq!(state.idx, 0);
            eth_put(&mut state, &mut tx_buf);
            assert_eq!(state.idx, 1);
            eth_put(&mut state, &mut tx_buf);
            assert_eq!(state.idx, 2);
            eth_put(&mut state, &mut tx_buf);
            assert_eq!(state.idx, 3);
            eth_put(&mut state, &mut tx_buf);
            assert_eq!(state.idx, 0);
            eth_put(&mut state, &mut tx_buf);
            assert_eq!(state.idx, 1);
        }

        #[test]
        #[should_panic]
        fn out_of_bounds() {
            let mut state = State::new();
            state.idx = 4;
            let message: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];

            let mut tx_buf: BaseSwSizedEthernetMessageImpl =
                BaseSwSizedEthernetMessageImpl { message, size: 20 };

            eth_put(&mut state, &mut tx_buf);
        }
    }
}

#[cfg(test)]
mod bindings {
    use super::*;
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
    pub fn EthernetFramesTxIn0_is_empty() -> bool {
        false
    }
    pub fn EthernetFramesTxIn1_is_empty() -> bool {
        false
    }
    pub fn EthernetFramesTxIn2_is_empty() -> bool {
        false
    }
    pub fn EthernetFramesTxIn3_is_empty() -> bool {
        false
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
    pub struct EthChannelGet {
        pub get: fn(*mut BaseSwRawEthernetMessageImpl) -> bool,
        pub empty: fn() -> bool,
    }
    pub struct EthChannelPut {
        pub put: fn(*mut BaseSwSizedEthernetMessageImpl) -> bool,
    }
}
