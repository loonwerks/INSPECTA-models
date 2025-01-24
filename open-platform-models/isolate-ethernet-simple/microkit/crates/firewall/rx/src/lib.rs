#![cfg_attr(not(test), no_std)]
#![allow(non_snake_case)]

use core::cell::OnceCell;
use log::{info, trace};

#[cfg(not(test))]
use {core::panic::PanicInfo, log::error};

use firewall_core::{EthFrame, Ipv4ProtoPacket, PacketType, TcpRepr, UdpRepr};

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

fn udp_port_allowed(udp: &UdpRepr) -> bool {
    config::udp::ALLOWED_PORTS
        .iter()
        .any(|x| *x == udp.dst_port)
}

fn tcp_port_allowed(tcp: &TcpRepr) -> bool {
    config::tcp::ALLOWED_PORTS
        .iter()
        .any(|x| *x == tcp.dst_port)
}

fn can_send_frame(frame: &mut [u8]) -> bool {
    let Some(packet) = EthFrame::parse(frame) else {
        info!("Malformed packet. Throw it away.");
        return false;
    };

    match packet.eth_type {
        PacketType::Arp(_) => true,
        PacketType::Ipv4(ip) => match ip.protocol {
            Ipv4ProtoPacket::Tcp(tcp) => {
                let allowed = tcp_port_allowed(&tcp);
                if !allowed {
                    info!("TCP packet filtered out");
                }
                allowed
            }
            Ipv4ProtoPacket::Udp(udp) => {
                let allowed = udp_port_allowed(&udp);
                if !allowed {
                    info!("UDP packet filtered out");
                }
                allowed
            }
            Ipv4ProtoPacket::TxOnly => {
                info!(
                    "Not a TCP or UDP packet. ({:?}) Throw it away.",
                    ip.header.protocol
                );
                false
            }
        },
        PacketType::Ipv6 => {
            info!("Not an IPv4 or Arp packet. Throw it away.");
            false
        }
    }
}

fn firewall(state: &mut State, frame: &mut BaseSwRawEthernetMessageImpl) {
    for i in 0..NUM_MSGS {
        let new_data = eth_get(i, frame);
        if !new_data {
            continue;
        }

        if can_send_frame(frame) {
            eth_put(state, frame);
        }
    }
}

#[no_mangle]
pub extern "C" fn seL4_RxFirewall_RxFirewall_timeTriggered() {
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
fn tcp_port_allowed_test() {
    let tcp_packet = TcpRepr { dst_port: 5760 };
    assert!(tcp_port_allowed(&tcp_packet));
    let tcp_packet = TcpRepr { dst_port: 42 };
    assert!(!tcp_port_allowed(&tcp_packet));
}

#[test]
fn udp_port_allowed_test() {
    let udp_packet = UdpRepr { dst_port: 68 };
    assert!(udp_port_allowed(&udp_packet));
    let udp_packet = UdpRepr { dst_port: 19 };
    assert!(!udp_port_allowed(&udp_packet));
}

#[cfg(test)]
mod can_send_frame_tests {
    use super::*;

    #[test]
    fn malformed_packet() {
        let mut frame = [0u8; 128];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x02, 0xC2,
        ];
        frame[0..14].copy_from_slice(&pkt);
        let res = can_send_frame(&mut frame);
        assert!(!res);
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
        assert!(!res);
    }

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
        assert!(res);
    }

    #[test]
    fn invalid_ipv4_protocols() {
        let mut frame = [0u8; 128];
        // Hop by Hop
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00, 0x45,
            0x0, 0x0, 0x29, 0x15, 0x5f, 0x40, 0x0, 0x80, 0x0, 0xf7, 0x28, 0xc0, 0xa8, 0x0, 0xce,
            0x34, 0x7f, 0xf8, 0x51,
        ];
        frame[0..34].copy_from_slice(&pkt);
        let res = can_send_frame(&mut frame);
        assert!(!res);

        // ICMP
        frame[23] = 0x01;
        let res = can_send_frame(&mut frame);
        assert!(!res);

        // IGMP
        frame[23] = 0x02;
        let res = can_send_frame(&mut frame);
        assert!(!res);

        // Ipv6 Route
        frame[23] = 0x2b;
        let res = can_send_frame(&mut frame);
        assert!(!res);

        // Ipv6 Frag
        frame[23] = 0x2c;
        let res = can_send_frame(&mut frame);
        assert!(!res);

        // ICMPv6
        frame[23] = 0x3a;
        let res = can_send_frame(&mut frame);
        assert!(!res);

        // IPv6 No Nxt
        frame[23] = 0x3b;
        let res = can_send_frame(&mut frame);
        assert!(!res);

        // IPv6 Opts
        frame[23] = 0x3c;
        let res = can_send_frame(&mut frame);
        assert!(!res);
    }

    #[test]
    fn disallowed_tcp() {
        let mut frame = [0u8; 128];
        // Disallowed port
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00, 0x45,
            0x0, 0x0, 0x29, 0x15, 0x5f, 0x40, 0x0, 0x80, 0x6, 0xf7, 0x28, 0xc0, 0xa8, 0x0, 0xce,
            0x34, 0x7f, 0xf8, 0x51, 0xc4, 0x73, 0x1, 0xbb, 0x21, 0x65, 0x90, 0xfb, 0xe4, 0x98,
            0x7c, 0x9d, 0x50, 0x10, 0x3, 0xff, 0xe3, 0xc7, 0x0, 0x0,
        ];
        frame[0..54].copy_from_slice(&pkt);
        let res = can_send_frame(&mut frame);
        assert!(!res);
    }

    #[test]
    fn allowed_tcp() {
        let mut frame = [0u8; 128];
        // Allowed port
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00, 0x45,
            0x0, 0x0, 0x29, 0x15, 0x5f, 0x40, 0x0, 0x80, 0x6, 0xf7, 0x28, 0xc0, 0xa8, 0x0, 0xce,
            0x34, 0x7f, 0xf8, 0x51, 0xc4, 0x73, 0x16, 0x80, 0x21, 0x65, 0x90, 0xfb, 0xe4, 0x98,
            0x7c, 0x9d, 0x50, 0x10, 0x3, 0xff, 0xe3, 0xc7, 0x0, 0x0,
        ];
        frame[0..54].copy_from_slice(&pkt);
        let res = can_send_frame(&mut frame);
        assert!(res);
    }

    #[test]
    fn disallowed_udp() {
        let mut frame = [0u8; 128];
        // Disallowed port
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00, 0x45,
            0x0, 0x0, 0x29, 0x15, 0x5f, 0x40, 0x0, 0x80, 0x11, 0xf7, 0x28, 0xc0, 0xa8, 0x0, 0xce,
            0x34, 0x7f, 0xf8, 0x51, 0xe1, 0x15, 0xe1, 0x15, 0x0, 0x34, 0xb4, 0xe8,
        ];
        frame[0..42].copy_from_slice(&pkt);
        let res = can_send_frame(&mut frame);
        assert!(!res);
    }

    #[test]
    fn allowed_udp() {
        let mut frame = [0u8; 128];
        // Allowed port
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00, 0x45,
            0x0, 0x0, 0x29, 0x15, 0x5f, 0x40, 0x0, 0x80, 0x11, 0xf7, 0x28, 0xc0, 0xa8, 0x0, 0xce,
            0x34, 0x7f, 0xf8, 0x51, 0xe1, 0x15, 0x00, 0x44, 0x0, 0x34, 0xb4, 0xe8,
        ];
        frame[0..42].copy_from_slice(&pkt);
        let res = can_send_frame(&mut frame);
        assert!(res);
    }
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
