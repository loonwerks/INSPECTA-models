#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

// This file will not be overwritten if codegen is rerun

use crate::bridge::seL4_RxFirewall_RxFirewall_api::*;
use crate::data::*;
// #[cfg(feature = "sel4")]
// #[allow(unused_imports)]
use log::{debug, error, info, trace, warn};

use firewall_core::{EthFrame, Ipv4ProtoPacket, PacketType, TcpRepr, UdpRepr};

mod config;

const NUM_MSGS: usize = 4;

pub struct seL4_RxFirewall_RxFirewall {
    idx: usize,
}

fn eth_get<API: seL4_RxFirewall_RxFirewall_Get_Api>(
    idx: usize,
    api: &mut seL4_RxFirewall_RxFirewall_Application_Api<API>,
) -> Option<SW::RawEthernetMessage_Impl> {
    match idx {
        0 => api.get_EthernetFramesRxIn0(),
        1 => api.get_EthernetFramesRxIn1(),
        2 => api.get_EthernetFramesRxIn2(),
        3 => api.get_EthernetFramesRxIn3(),
        _ => None,
    }
}

fn udp_port_allowed(udp: &UdpRepr) -> bool {
    config::udp::ALLOWED_PORTS.contains(&udp.dst_port)
}

fn tcp_port_allowed(tcp: &TcpRepr) -> bool {
    config::tcp::ALLOWED_PORTS.contains(&tcp.dst_port)
}

fn can_send_frame(frame: &[u8]) -> bool {
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

impl seL4_RxFirewall_RxFirewall {
    pub const fn new() -> Self {
        Self { idx: 0 }
    }

    fn idx_increment(&mut self) {
        self.idx = (self.idx + 1) % NUM_MSGS;
    }

    fn eth_put<API: seL4_RxFirewall_RxFirewall_Put_Api>(
        &mut self,
        rx_buf: &mut SW::RawEthernetMessage_Impl,
        api: &mut seL4_RxFirewall_RxFirewall_Application_Api<API>,
    ) {
        match self.idx {
            0 => api.put_EthernetFramesRxOut0(*rx_buf),
            1 => api.put_EthernetFramesRxOut1(*rx_buf),
            2 => api.put_EthernetFramesRxOut2(*rx_buf),
            3 => api.put_EthernetFramesRxOut3(*rx_buf),
            _ => (),
        }
        self.idx_increment();
    }

    fn firewall<API: seL4_RxFirewall_RxFirewall_Full_Api>(
        &mut self,
        api: &mut seL4_RxFirewall_RxFirewall_Application_Api<API>,
    ) {
        for i in 0..NUM_MSGS {
            if let Some(mut frame) = eth_get(i, api) {
                if can_send_frame(&frame) {
                    self.eth_put(&mut frame, api);
                }
            }
        }
    }

    pub fn initialize<API: seL4_RxFirewall_RxFirewall_Put_Api>(
        &mut self,
        api: &mut seL4_RxFirewall_RxFirewall_Application_Api<API>,
    ) {
        #[cfg(feature = "sel4")]
        info!("initialize entrypoint invoked");
    }

    pub fn timeTriggered<API: seL4_RxFirewall_RxFirewall_Full_Api>(
        &mut self,
        api: &mut seL4_RxFirewall_RxFirewall_Application_Api<API>,
    ) {
        #[cfg(feature = "sel4")]
        info!("compute entrypoint invoked");
        self.firewall(api);
    }

    pub fn notify(&mut self, channel: microkit_channel) {
        // this method is called when the monitor does not handle the passed in channel
        {
            #[cfg(feature = "sel4")]
            warn!("Unexpected channel {}", channel)
        }
    }
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
        let res = can_send_frame(&frame);
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
        let res = can_send_frame(&frame);
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
        let res = can_send_frame(&frame);
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
        let res = can_send_frame(&frame);
        assert!(!res);

        // ICMP
        frame[23] = 0x01;
        let res = can_send_frame(&frame);
        assert!(!res);

        // IGMP
        frame[23] = 0x02;
        let res = can_send_frame(&frame);
        assert!(!res);

        // Ipv6 Route
        frame[23] = 0x2b;
        let res = can_send_frame(&frame);
        assert!(!res);

        // Ipv6 Frag
        frame[23] = 0x2c;
        let res = can_send_frame(&frame);
        assert!(!res);

        // ICMPv6
        frame[23] = 0x3a;
        let res = can_send_frame(&frame);
        assert!(!res);

        // IPv6 No Nxt
        frame[23] = 0x3b;
        let res = can_send_frame(&frame);
        assert!(!res);

        // IPv6 Opts
        frame[23] = 0x3c;
        let res = can_send_frame(&frame);
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
        let res = can_send_frame(&frame);
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
        let res = can_send_frame(&frame);
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
        let res = can_send_frame(&frame);
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
        let res = can_send_frame(&frame);
        assert!(res);
    }
}

#[test]
fn init_test() {
    let state = seL4_RxFirewall_RxFirewall::new();
    assert_eq!(state.idx, 0);
}

#[test]
fn state_increment_tests() {
    let mut state = seL4_RxFirewall_RxFirewall::new();
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

// #[cfg(test)]
// mod rx_ethernet_frames_tests {
//     use super::*;

//     mod get_in {
//         use super::*;

//         #[test]
//         fn valid() {
//             let mut rx_buf: SW::RawEthernetMessage_Impl = [0; SW::SW_RawEthernetMessage_Impl_DIM_0];

//             let res = eth_get(0, &mut rx_buf);
//             assert!(res);
//             let res = eth_get(1, &mut rx_buf);
//             assert!(res);
//             let res = eth_get(2, &mut rx_buf);
//             assert!(res);
//             let res = eth_get(3, &mut rx_buf);
//             assert!(res);
//             let res = eth_get(0, &mut rx_buf);
//             assert!(res);
//             let res = eth_get(1, &mut rx_buf);
//             assert!(res);
//         }

//         #[test]
//         #[should_panic]
//         fn out_of_bounds() {
//             let mut rx_buf: SW::RawEthernetMessage_Impl = [0; SW::SW_RawEthernetMessage_Impl_DIM_0];

//             let _ = eth_get(4, &mut rx_buf);
//         }
//     }

//     mod put_out {
//         use super::*;

//         #[test]
//         fn valid() {
//             let mut state = State::new();
//             let mut rx_buf: SW::RawEthernetMessage_Impl = [0; SW::SW_RawEthernetMessage_Impl_DIM_0];

//             assert_eq!(state.idx, 0);
//             eth_put(&mut state, &mut rx_buf);
//             assert_eq!(state.idx, 1);
//             eth_put(&mut state, &mut rx_buf);
//             assert_eq!(state.idx, 2);
//             eth_put(&mut state, &mut rx_buf);
//             assert_eq!(state.idx, 3);
//             eth_put(&mut state, &mut rx_buf);
//             assert_eq!(state.idx, 0);
//             eth_put(&mut state, &mut rx_buf);
//             assert_eq!(state.idx, 1);
//         }

//         #[test]
//         #[should_panic]
//         fn out_of_bounds() {
//             let mut state = State::new();
//             state.idx = 4;
//             let mut rx_buf: SW::RawEthernetMessage_Impl = [0; SW::SW_RawEthernetMessage_Impl_DIM_0];

//             eth_put(&mut state, &mut rx_buf);
//         }
//     }
// }

// #[cfg(test)]
// mod bindings {
//     use super::*;
//     pub fn get_EthernetFramesRxIn0(_value: *mut SW::RawEthernetMessage_Impl) -> bool {
//         true
//     }
//     pub fn get_EthernetFramesRxIn1(_value: *mut SW::RawEthernetMessage_Impl) -> bool {
//         true
//     }

//     pub fn get_EthernetFramesRxIn2(_value: *mut SW::RawEthernetMessage_Impl) -> bool {
//         true
//     }

//     pub fn get_EthernetFramesRxIn3(_value: *mut SW::RawEthernetMessage_Impl) -> bool {
//         true
//     }
//     pub fn EthernetFramesRxIn0_is_empty() -> bool {
//         false
//     }
//     pub fn EthernetFramesRxIn1_is_empty() -> bool {
//         false
//     }
//     pub fn EthernetFramesRxIn2_is_empty() -> bool {
//         false
//     }
//     pub fn EthernetFramesRxIn3_is_empty() -> bool {
//         false
//     }
//     pub fn put_EthernetFramesRxOut0(_value: *mut SW::RawEthernetMessage_Impl) -> bool {
//         true
//     }
//     pub fn put_EthernetFramesRxOut1(_value: *mut SW::RawEthernetMessage_Impl) -> bool {
//         true
//     }
//     pub fn put_EthernetFramesRxOut2(_value: *mut SW::RawEthernetMessage_Impl) -> bool {
//         true
//     }
//     pub fn put_EthernetFramesRxOut3(_value: *mut SW::RawEthernetMessage_Impl) -> bool {
//         true
//     }
//     pub struct EthChannelGet {
//         pub get: fn(*mut SW::RawEthernetMessage_Impl) -> bool,
//         pub empty: fn() -> bool,
//     }
//     pub struct EthChannelPut {
//         pub put: fn(*mut SW::RawEthernetMessage_Impl) -> bool,
//     }
// }
