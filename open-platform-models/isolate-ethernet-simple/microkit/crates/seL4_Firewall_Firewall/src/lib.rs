#![cfg_attr(not(test), no_std)]

use core::cell::OnceCell;
use log::{debug, info, trace};

#[cfg(not(test))]
use {core::panic::PanicInfo, log::error};

mod config;
mod net;

mod bindings {
    pub const BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE: usize = 1600;
    pub type BaseSwRawEthernetMessageImpl = [u8; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
    #[repr(C)]
    pub struct BaseSwSizedEthernetMessageImpl {
        pub message: BaseSwRawEthernetMessageImpl,
        pub size: u16,
    }
    #[cfg(not(test))]
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

use bindings::*;

#[cfg(test)]
use mock::*;

use net::{
    Address, Arp, ArpOp, EtherType, EthernetRepr, IpProtocol, Ipv4Address, Ipv4Repr, TcpRepr,
    UdpRepr,
};

static mut STATE: OnceCell<State> = OnceCell::new();

struct State {
    rx_idx: u8,
    tx_idx: u8,
}

fn filter_udp(udp: &UdpRepr) -> bool {
    !config::firewall::udp::ALLOWED_PORTS
        .iter()
        .any(|x| *x == udp.dst_port)
}

fn filter_tcp(tcp: &TcpRepr) -> bool {
    !config::firewall::tcp::ALLOWED_PORTS
        .iter()
        .any(|x| *x == tcp.dst_port)
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

fn can_send_rx_frame(frame: &mut BaseSwRawEthernetMessageImpl) -> bool {
    let Some(header) = EthernetRepr::parse(frame) else {
        return false;
    };

    // TODO: Do we still need this? Probably not because queuing tells us whether we have new data. However, is an all zero dest mac address a malformed packet?
    if header.is_empty() {
        return false;
    }
    debug!("Dest Mac: {:?}", header.dst_addr);
    debug!("Source Mac: {:?}", header.src_addr);
    debug!("EtherType: {:?}", header.ethertype);
    match header.ethertype {
        EtherType::Arp => {
            if Arp::parse(&frame[EthernetRepr::SIZE..]).is_some() {
                trace!("Good ARP Packet. Send it along");
                true
            } else {
                info!("Malformed ARP Packet. Throw it away.");
                false
            }
        }
        EtherType::Ipv4 => {
            trace!("PACKET:\n {:?}", &frame[0..64]);
            let Some(ip) = Ipv4Repr::parse(&frame[EthernetRepr::SIZE..]) else {
                info!("Malformed IPv4 Packet. Throw it away.");
                return false;
            };
            // TODO: Check that the entire IPv4 Packet is not malformed

            match ip.protocol {
                IpProtocol::Tcp => {
                    let tcp = TcpRepr::parse(&frame[EthernetRepr::SIZE + Ipv4Repr::SIZE..]);
                    // TODO: Check that TCP Packet is not malformed

                    if filter_tcp(&tcp) {
                        info!("TCP packet filtered out");
                        false
                    } else {
                        trace!("Good TCP Packet. Send it along");
                        true
                    }
                }
                IpProtocol::Udp => {
                    let udp = UdpRepr::parse(&frame[EthernetRepr::SIZE + Ipv4Repr::SIZE..]);
                    // TODO: Check that UDP Packet is not malformed

                    if filter_udp(&udp) {
                        info!("UDP packet filtered out");
                        false
                    } else {
                        trace!("Good UDP Packet. Send it along");
                        true
                    }
                }
                // Throw away any packet that isn't TCP or UDP
                _ => {
                    info!(
                        "Not a TCP or UDP packet. ({:?}) Throw it away.",
                        ip.protocol
                    );
                    false
                }
            }
        }
        // Throw away any packet that isn't IPv4 or Arp
        _ => {
            info!("Not an IPv4 or Arp packet. Throw it away.");
            false
        }
    }
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

fn can_send_tx_frame(frame: &mut BaseSwRawEthernetMessageImpl) -> Option<u16> {
    let header = EthernetRepr::parse(frame)?;

    if header.is_empty() {
        return None;
    }
    // TODO: Need to do any filtering this direction?

    let size = match header.ethertype {
        EtherType::Arp => {
            let size = 64u16;
            frame[EthernetRepr::SIZE + Arp::SIZE..size as usize].fill(0);
            size
        }
        EtherType::Ipv4 => {
            let Some(ip) = Ipv4Repr::parse(&frame[EthernetRepr::SIZE..]) else {
                info!("Malformed IPv4 Packet. Throw it away.");
                return None;
            };
            ip.length + EthernetRepr::SIZE as u16
        }
        _ => {
            info!("Not an IPv4 or Arp packet. Throw it away.");
            return None;
        }
    };
    Some(size)
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
fn test_filter_tcp() {
    let tcp_packet = TcpRepr { dst_port: 5760 };
    assert!(!filter_tcp(&tcp_packet));
    let tcp_packet = TcpRepr { dst_port: 42 };
    assert!(filter_tcp(&tcp_packet));
}

#[test]
fn test_filter_udp() {
    let udp_packet = UdpRepr { dst_port: 68 };
    assert!(!filter_udp(&udp_packet));
    let udp_packet = UdpRepr { dst_port: 19 };
    assert!(filter_udp(&udp_packet));
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
mod can_send_rx_frame_tests {
    use super::*;

    #[test]
    fn dest_mac_empty() {
        let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
        let pkt = [0u8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0x08, 0];
        frame[0..14].copy_from_slice(&pkt);
        let res = can_send_rx_frame(&mut frame);
        assert!(!res);
    }

    #[test]
    fn malformed_eth_header() {
        let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x02, 0xC2,
        ];
        frame[0..14].copy_from_slice(&pkt);
        let res = can_send_rx_frame(&mut frame);
        assert!(!res);
    }

    #[test]
    fn eth_type_ipv6() {
        let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x86, 0xDD,
        ];
        frame[0..14].copy_from_slice(&pkt);
        let res = can_send_rx_frame(&mut frame);
        assert!(!res);
    }

    #[test]
    fn valid_arp_request() {
        let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x06, 0x0,
            0x1, 0x8, 0x0, 0x6, 0x4, 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0xc0, 0xa8, 0x0, 0x1,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xc0, 0xa8, 0x0, 0xce,
        ];
        frame[0..42].copy_from_slice(&pkt);
        let res = can_send_rx_frame(&mut frame);
        assert!(res);
    }

    #[test]
    fn valid_arp_reply() {
        let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
        let pkt = [
            0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x18, 0x20, 0x22, 0x24, 0x26, 0x28, 0x8, 0x6, 0x0, 0x1,
            0x8, 0x0, 0x6, 0x4, 0x0, 0x2, 0x18, 0x20, 0x22, 0x24, 0x26, 0x28, 0xc0, 0xa8, 0x0,
            0xce, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0xc0, 0xa8, 0x0, 0x1,
        ];
        frame[0..42].copy_from_slice(&pkt);
        let res = can_send_rx_frame(&mut frame);
        assert!(res);
    }

    #[test]
    fn malformed_arp() {
        let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x06, 0x0,
            0x2, 0x8, 0x0, 0x6, 0x4, 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0xc0, 0xa8, 0x0, 0x1,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xc0, 0xa8, 0x0, 0xce,
        ];
        frame[0..42].copy_from_slice(&pkt);
        let res = can_send_rx_frame(&mut frame);
        assert!(!res);

        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x06, 0x0,
            0x1, 0x9, 0x20, 0x6, 0x4, 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0xc0, 0xa8, 0x0, 0x1,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xc0, 0xa8, 0x0, 0xce,
        ];
        frame[0..42].copy_from_slice(&pkt);
        let res = can_send_rx_frame(&mut frame);
        assert!(!res);

        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x06, 0x0,
            0x1, 0x8, 0x0, 0x6, 0x4, 0x0, 0x6, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0xc0, 0xa8, 0x0, 0x1,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xc0, 0xa8, 0x0, 0xce,
        ];
        frame[0..42].copy_from_slice(&pkt);
        let res = can_send_rx_frame(&mut frame);
        assert!(!res);
    }

    #[test]
    fn malformed_ipv4() {
        let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00, 0x45,
            0x0, 0x0, 0x29, 0x15, 0x5f, 0x40, 0x0, 0x80, 0x7, 0xf7, 0x28, 0xc0, 0xa8, 0x0, 0xce,
            0x34, 0x7f, 0xf8, 0x51,
        ];
        frame[0..34].copy_from_slice(&pkt);
        let res = can_send_rx_frame(&mut frame);
        assert!(!res);
    }

    #[test]
    fn invalid_ipv4_protocols() {
        let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
        // Hop by Hop
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00, 0x45,
            0x0, 0x0, 0x29, 0x15, 0x5f, 0x40, 0x0, 0x80, 0x0, 0xf7, 0x28, 0xc0, 0xa8, 0x0, 0xce,
            0x34, 0x7f, 0xf8, 0x51,
        ];
        frame[0..34].copy_from_slice(&pkt);
        let res = can_send_rx_frame(&mut frame);
        assert!(!res);

        // ICMP
        frame[23] = 0x01;
        let res = can_send_rx_frame(&mut frame);
        assert!(!res);

        // IGMP
        frame[23] = 0x02;
        let res = can_send_rx_frame(&mut frame);
        assert!(!res);

        // Ipv6 Route
        frame[23] = 0x2b;
        let res = can_send_rx_frame(&mut frame);
        assert!(!res);

        // Ipv6 Frag
        frame[23] = 0x2c;
        let res = can_send_rx_frame(&mut frame);
        assert!(!res);

        // ICMPv6
        frame[23] = 0x3a;
        let res = can_send_rx_frame(&mut frame);
        assert!(!res);

        // IPv6 No Nxt
        frame[23] = 0x3b;
        let res = can_send_rx_frame(&mut frame);
        assert!(!res);

        // IPv6 Opts
        frame[23] = 0x3c;
        let res = can_send_rx_frame(&mut frame);
        assert!(!res);
    }

    #[test]
    fn valid_tcp() {
        let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
        // Disallowed port
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00, 0x45,
            0x0, 0x0, 0x29, 0x15, 0x5f, 0x40, 0x0, 0x80, 0x6, 0xf7, 0x28, 0xc0, 0xa8, 0x0, 0xce,
            0x34, 0x7f, 0xf8, 0x51, 0xc4, 0x73, 0x1, 0xbb, 0x21, 0x65, 0x90, 0xfb, 0xe4, 0x98,
            0x7c, 0x9d, 0x50, 0x10, 0x3, 0xff, 0xe3, 0xc7, 0x0, 0x0,
        ];
        frame[0..54].copy_from_slice(&pkt);
        let res = can_send_rx_frame(&mut frame);
        assert!(!res);

        // Allowed port
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00, 0x45,
            0x0, 0x0, 0x29, 0x15, 0x5f, 0x40, 0x0, 0x80, 0x6, 0xf7, 0x28, 0xc0, 0xa8, 0x0, 0xce,
            0x34, 0x7f, 0xf8, 0x51, 0xc4, 0x73, 0x16, 0x80, 0x21, 0x65, 0x90, 0xfb, 0xe4, 0x98,
            0x7c, 0x9d, 0x50, 0x10, 0x3, 0xff, 0xe3, 0xc7, 0x0, 0x0,
        ];
        frame[0..54].copy_from_slice(&pkt);
        let res = can_send_rx_frame(&mut frame);
        assert!(res);
    }

    #[test]
    fn valid_udp() {
        let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
        // Disallowed port
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00, 0x45,
            0x0, 0x0, 0x29, 0x15, 0x5f, 0x40, 0x0, 0x80, 0x11, 0xf7, 0x28, 0xc0, 0xa8, 0x0, 0xce,
            0x34, 0x7f, 0xf8, 0x51, 0xe1, 0x15, 0xe1, 0x15, 0x0, 0x34, 0xb4, 0xe8,
        ];
        frame[0..42].copy_from_slice(&pkt);
        let res = can_send_rx_frame(&mut frame);
        assert!(!res);

        // Allowed port
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00, 0x45,
            0x0, 0x0, 0x29, 0x15, 0x5f, 0x40, 0x0, 0x80, 0x11, 0xf7, 0x28, 0xc0, 0xa8, 0x0, 0xce,
            0x34, 0x7f, 0xf8, 0x51, 0xe1, 0x15, 0x00, 0x44, 0x0, 0x34, 0xb4, 0xe8,
        ];
        frame[0..42].copy_from_slice(&pkt);
        let res = can_send_rx_frame(&mut frame);
        assert!(res);
    }
}

#[cfg(test)]
mod can_send_tx_frame_tests {
    use super::*;

    #[test]
    fn dest_mac_empty() {
        let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
        let pkt = [0u8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0x08, 0];
        frame[0..14].copy_from_slice(&pkt);
        let res = can_send_tx_frame(&mut frame);
        assert!(res.is_none());
    }

    #[test]
    fn malformed_eth_header() {
        let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x02, 0xC2,
        ];
        frame[0..14].copy_from_slice(&pkt);
        let res = can_send_tx_frame(&mut frame);
        assert!(res.is_none());
    }

    #[test]
    fn eth_type_ipv6() {
        let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x86, 0xDD,
        ];
        frame[0..14].copy_from_slice(&pkt);
        let res = can_send_tx_frame(&mut frame);
        assert!(res.is_none());
    }

    #[test]
    fn valid_arp_request() {
        let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x06, 0x0,
            0x1, 0x8, 0x0, 0x6, 0x4, 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0xc0, 0xa8, 0x0, 0x1,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xc0, 0xa8, 0x0, 0xce,
        ];
        frame[0..42].copy_from_slice(&pkt);
        let res = can_send_tx_frame(&mut frame);
        assert!(res.is_some());
        assert_eq!(res.unwrap(), 64);
    }

    #[test]
    fn valid_arp_reply() {
        let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
        let pkt = [
            0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x18, 0x20, 0x22, 0x24, 0x26, 0x28, 0x8, 0x6, 0x0, 0x1,
            0x8, 0x0, 0x6, 0x4, 0x0, 0x2, 0x18, 0x20, 0x22, 0x24, 0x26, 0x28, 0xc0, 0xa8, 0x0,
            0xce, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0xc0, 0xa8, 0x0, 0x1,
        ];
        frame[0..42].copy_from_slice(&pkt);
        let res = can_send_tx_frame(&mut frame);
        assert!(res.is_some());
        assert_eq!(res.unwrap(), 64);
    }

    #[test]
    #[ignore = "Should the tx side check that ARPs are well formed?"]
    fn malformed_arp() {
        let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x06, 0x0,
            0x2, 0x8, 0x0, 0x6, 0x4, 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0xc0, 0xa8, 0x0, 0x1,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xc0, 0xa8, 0x0, 0xce,
        ];
        frame[0..42].copy_from_slice(&pkt);
        let res = can_send_tx_frame(&mut frame);
        assert!(res.is_none());

        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x06, 0x0,
            0x1, 0x9, 0x20, 0x6, 0x4, 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0xc0, 0xa8, 0x0, 0x1,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xc0, 0xa8, 0x0, 0xce,
        ];
        frame[0..42].copy_from_slice(&pkt);
        let res = can_send_tx_frame(&mut frame);
        assert!(res.is_none());

        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x06, 0x0,
            0x1, 0x8, 0x0, 0x6, 0x4, 0x0, 0x6, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0xc0, 0xa8, 0x0, 0x1,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xc0, 0xa8, 0x0, 0xce,
        ];
        frame[0..42].copy_from_slice(&pkt);
        let res = can_send_tx_frame(&mut frame);
        assert!(res.is_none());
    }

    #[test]
    fn malformed_ipv4() {
        let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00, 0x45,
            0x0, 0x0, 0x29, 0x15, 0x5f, 0x40, 0x0, 0x80, 0x7, 0xf7, 0x28, 0xc0, 0xa8, 0x0, 0xce,
            0x34, 0x7f, 0xf8, 0x51,
        ];
        frame[0..34].copy_from_slice(&pkt);
        let res = can_send_tx_frame(&mut frame);
        assert!(res.is_none());
    }

    #[test]
    fn valid_ipv4() {
        let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00, 0x45,
            0x0, 0x0, 0x20, 0x15, 0x5f, 0x40, 0x0, 0x80, 0x0, 0xf7, 0x28, 0xc0, 0xa8, 0x0, 0xce,
            0x34, 0x7f, 0xf8, 0x51,
        ];
        frame[0..34].copy_from_slice(&pkt);
        let res = can_send_tx_frame(&mut frame);
        assert!(res.is_some());
        assert_eq!(res.unwrap(), 46);

        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00, 0x45,
            0x0, 0x0, 0x33, 0x15, 0x5f, 0x40, 0x0, 0x80, 0x1, 0xf7, 0x28, 0xc0, 0xa8, 0x0, 0xce,
            0x34, 0x7f, 0xf8, 0x51,
        ];
        frame[0..34].copy_from_slice(&pkt);
        let res = can_send_tx_frame(&mut frame);
        assert!(res.is_some());
        assert_eq!(res.unwrap(), 65);
    }
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
