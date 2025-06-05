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

pub struct seL4_RxFirewall_RxFirewall {}

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

fn eth_put<API: seL4_RxFirewall_RxFirewall_Put_Api>(
    idx: usize,
    rx_buf: &mut SW::RawEthernetMessage_Impl,
    api: &mut seL4_RxFirewall_RxFirewall_Application_Api<API>,
) {
    match idx {
        0 => api.put_EthernetFramesRxOut0(*rx_buf),
        1 => api.put_EthernetFramesRxOut1(*rx_buf),
        2 => api.put_EthernetFramesRxOut2(*rx_buf),
        3 => api.put_EthernetFramesRxOut3(*rx_buf),
        _ => (),
    }
}

fn udp_port_allowed(udp: &UdpRepr) -> bool {
    config::udp::ALLOWED_PORTS.contains(&udp.dst_port)
}

fn tcp_port_allowed(tcp: &TcpRepr) -> bool {
    config::tcp::ALLOWED_PORTS.contains(&tcp.dst_port)
}

fn get_frame_packet(frame: &[u8]) -> Option<PacketType> {
    let packet = EthFrame::parse(frame).map(|x| x.eth_type);
    if packet.is_none() {
        info!("Malformed packet. Throw it away.")
    }
    packet
}

fn can_send_packet(packet: &PacketType) -> bool {
    match packet {
        PacketType::Arp(_) => true,
        PacketType::Ipv4(ip) => match &ip.protocol {
            Ipv4ProtoPacket::Tcp(tcp) => {
                let allowed = tcp_port_allowed(tcp);
                if !allowed {
                    info!("TCP packet filtered out");
                }
                allowed
            }
            Ipv4ProtoPacket::Udp(udp) => {
                let allowed = udp_port_allowed(udp);
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
        Self {}
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
        trace!("compute entrypoint invoked");
        for i in 0..NUM_MSGS {
            if let Some(mut frame) = eth_get(i, api) {
                if let Some(packet) = get_frame_packet(&frame) {
                    if can_send_packet(&packet) {
                        eth_put(i, &mut frame, api);
                    }
                }
            }
        }
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
mod parse_frame_tests {
    use super::*;

    #[test]
    fn parse_malformed_packet() {
        let mut frame = [0u8; 128];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x02, 0xC2,
        ];
        frame[0..14].copy_from_slice(&pkt);
        let res = get_frame_packet(&frame);
        assert!(res.is_none());
    }

    #[test]
    fn parse_valid_arp() {
        let mut frame = [0u8; 128];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x06, 0x0,
            0x1, 0x8, 0x0, 0x6, 0x4, 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0xc0, 0xa8, 0x0, 0x1,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xc0, 0xa8, 0x0, 0xce,
        ];
        frame[0..42].copy_from_slice(&pkt);
        let res = get_frame_packet(&frame);
        assert!(res.is_some());
    }
}

#[cfg(test)]
mod can_send_tests {
    use super::*;
    use firewall_core::{
        Address, Arp, ArpOp, EtherType, HardwareType, IpProtocol, Ipv4Address, Ipv4Packet, Ipv4Repr,
    };

    #[test]
    fn packet_valid_arp_request() {
        let packet = PacketType::Arp(Arp {
            htype: HardwareType::Ethernet,
            ptype: EtherType::Ipv4,
            hsize: 0x6,
            psize: 0x4,
            op: ArpOp::Request,
            src_addr: Address([0x2, 0x3, 0x4, 0x5, 0x6, 0x7]),
            src_protocol_addr: Ipv4Address([0xc0, 0xa8, 0x00, 0x01]),
            dest_addr: Address([0x00, 0x00, 0x00, 0x00, 0x00, 0x00]),
            dest_protocol_addr: Ipv4Address([0xc0, 0xa8, 0x0, 0xce]),
        });
        assert!(can_send_packet(&packet));
    }

    #[test]
    fn packet_valid_arp_reply() {
        let packet = PacketType::Arp(Arp {
            htype: HardwareType::Ethernet,
            ptype: EtherType::Ipv4,
            hsize: 0x6,
            psize: 0x4,
            op: ArpOp::Reply,
            src_addr: Address([0x18, 0x20, 0x22, 0x24, 0x26, 0x28]),
            src_protocol_addr: Ipv4Address([0xc0, 0xa8, 0x00, 0xce]),
            dest_addr: Address([0x2, 0x3, 0x4, 0x5, 0x6, 0x7]),
            dest_protocol_addr: Ipv4Address([0xc0, 0xa8, 0x0, 0x01]),
        });
        assert!(can_send_packet(&packet));
    }

    #[test]
    fn packet_invalid_ipv6() {
        let packet = PacketType::Ipv6;
        assert!(!can_send_packet(&packet));
    }

    #[test]
    fn invalid_ipv4_protocols() {
        // Hop by Hop
        let mut packet = PacketType::Ipv4(Ipv4Packet {
            header: Ipv4Repr {
                protocol: IpProtocol::HopByHop,
                length: 0x29,
            },
            protocol: Ipv4ProtoPacket::TxOnly,
        });
        assert!(!can_send_packet(&packet));

        // ICMP
        if let PacketType::Ipv4(ip) = &mut packet {
            ip.header.protocol = IpProtocol::Icmp;
        }
        assert!(!can_send_packet(&packet));

        // IGMP
        if let PacketType::Ipv4(ip) = &mut packet {
            ip.header.protocol = IpProtocol::Igmp;
        }
        assert!(!can_send_packet(&packet));

        // Ipv6 Route
        if let PacketType::Ipv4(ip) = &mut packet {
            ip.header.protocol = IpProtocol::Ipv6Route;
        }
        assert!(!can_send_packet(&packet));

        // Ipv6 Frag
        if let PacketType::Ipv4(ip) = &mut packet {
            ip.header.protocol = IpProtocol::Ipv6Frag;
        }
        assert!(!can_send_packet(&packet));

        // ICMPv6
        if let PacketType::Ipv4(ip) = &mut packet {
            ip.header.protocol = IpProtocol::Icmpv6;
        }
        assert!(!can_send_packet(&packet));

        // IPv6 No Nxt
        if let PacketType::Ipv4(ip) = &mut packet {
            ip.header.protocol = IpProtocol::Ipv6NoNxt;
        }
        assert!(!can_send_packet(&packet));

        // IPv6 Opts
        if let PacketType::Ipv4(ip) = &mut packet {
            ip.header.protocol = IpProtocol::Ipv6Opts;
        }
        assert!(!can_send_packet(&packet));
    }

    #[test]
    fn disallowed_tcp() {
        let packet = PacketType::Ipv4(Ipv4Packet {
            header: Ipv4Repr {
                protocol: IpProtocol::Tcp,
                length: 0x29,
            },
            protocol: Ipv4ProtoPacket::Tcp(TcpRepr { dst_port: 443 }),
        });
        assert!(!can_send_packet(&packet));
    }

    #[test]
    fn allowed_tcp() {
        let packet = PacketType::Ipv4(Ipv4Packet {
            header: Ipv4Repr {
                protocol: IpProtocol::Tcp,
                length: 0x29,
            },
            protocol: Ipv4ProtoPacket::Tcp(TcpRepr { dst_port: 5760 }),
        });
        assert!(can_send_packet(&packet));
    }

    #[test]
    fn disallowed_udp() {
        let packet = PacketType::Ipv4(Ipv4Packet {
            header: Ipv4Repr {
                protocol: IpProtocol::Udp,
                length: 0x29,
            },
            protocol: Ipv4ProtoPacket::Udp(UdpRepr { dst_port: 15 }),
        });
        assert!(!can_send_packet(&packet));
    }

    #[test]
    fn allowed_udp() {
        let packet = PacketType::Ipv4(Ipv4Packet {
            header: Ipv4Repr {
                protocol: IpProtocol::Udp,
                length: 0x29,
            },
            protocol: Ipv4ProtoPacket::Udp(UdpRepr { dst_port: 68 }),
        });
        assert!(can_send_packet(&packet));
    }
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
