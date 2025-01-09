#![cfg_attr(not(test), no_std)]

use log::info;

mod net;
use net::{Arp, EtherType, EthernetRepr, IpProtocol, Ipv4Repr, TcpRepr, UdpRepr};

use inspecta_types::BaseSwRawEthernetMessageImpl;

#[cfg(test)]
use {
    inspecta_types::BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE,
    net::{Address, ArpOp, HardwareType, Ipv4Address},
};

pub mod config {
    pub mod tcp {
        pub const ALLOWED_PORTS: [u16; 1] = [5760u16];
    }
    pub mod udp {
        const NUM_UDP_PORTS: usize = 1;
        pub const ALLOWED_PORTS: [u16; NUM_UDP_PORTS] = [68u16];
    }
}

#[cfg_attr(test, derive(PartialEq))]
#[derive(Debug)]
pub enum PacketType {
    Arp(Arp),
    Ipv4(Ipv4Packet),
}

#[cfg_attr(test, derive(PartialEq))]
#[derive(Debug)]
pub enum Ipv4ProtoPacket {
    Tcp(TcpRepr),
    Udp(UdpRepr),
    TxOnly,
}

#[cfg_attr(test, derive(PartialEq))]
#[derive(Debug)]
pub struct Ipv4Packet {
    pub header: Ipv4Repr,
    pub protocol: Ipv4ProtoPacket,
}

#[cfg_attr(test, derive(PartialEq))]
#[derive(Debug)]
pub struct EthFrame {
    pub header: EthernetRepr,
    pub eth_type: PacketType,
}

impl EthFrame {
    pub fn parse(frame: &[u8]) -> Option<EthFrame> {
        let header = EthernetRepr::parse(frame)?;

        // TODO: Do we still need this? Probably not because queuing tells us whether we have new data. However, is an all zero dest mac address a malformed packet?
        if header.is_empty() {
            return None;
        }

        let eth_type = match header.ethertype {
            EtherType::Arp => {
                Arp::parse(&frame[EthernetRepr::SIZE..]).map(|arp| PacketType::Arp(arp))?
            }
            EtherType::Ipv4 => {
                let ip = Ipv4Repr::parse(&frame[EthernetRepr::SIZE..])?;
                // TODO: Check that the entire IPv4 Packet is not malformed

                let protocol = match ip.protocol {
                    IpProtocol::Tcp => Ipv4ProtoPacket::Tcp(TcpRepr::parse(
                        &frame[EthernetRepr::SIZE + Ipv4Repr::SIZE..],
                    )),
                    IpProtocol::Udp => Ipv4ProtoPacket::Udp(UdpRepr::parse(
                        &frame[EthernetRepr::SIZE + Ipv4Repr::SIZE..],
                    )),
                    _ => Ipv4ProtoPacket::TxOnly,
                };
                PacketType::Ipv4(Ipv4Packet {
                    header: ip,
                    protocol,
                })
            }
            // Throw away any frame that is not Ipv4 or Arp
            _ => {
                info!("Not an IPv4 or Arp packet. Throw it away.");
                return None;
            }
        };

        Some(EthFrame { header, eth_type })
    }
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

pub fn can_send_rx_frame(frame: &mut BaseSwRawEthernetMessageImpl) -> bool {
    let Some(packet) = EthFrame::parse(frame) else {
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
    }
}

pub fn can_send_tx_frame(frame: &mut BaseSwRawEthernetMessageImpl) -> Option<u16> {
    let packet = EthFrame::parse(frame)?;

    let size = match packet.eth_type {
        PacketType::Arp(_) => {
            let size = 64u16;
            // TODO: Do we need this now that linux is constructing it?
            frame[EthernetRepr::SIZE + Arp::SIZE..size as usize].fill(0);
            size
        }
        PacketType::Ipv4(ip) => ip.header.length + EthernetRepr::SIZE as u16,
    };

    Some(size)
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
mod eth_frame_tests {
    use super::*;

    #[test]
    fn dest_mac_empty() {
        let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
        let pkt = [0u8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0x08, 0];
        frame[0..14].copy_from_slice(&pkt);
        let res = EthFrame::parse(&mut frame);
        assert!(res.is_none());
    }

    #[test]
    fn malformed_eth_header() {
        let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x02, 0xC2,
        ];
        frame[0..14].copy_from_slice(&pkt);
        let res = EthFrame::parse(&mut frame);
        assert!(res.is_none());
    }

    #[test]
    fn eth_type_ipv6() {
        let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x86, 0xDD,
        ];
        frame[0..14].copy_from_slice(&pkt);
        let res = EthFrame::parse(&mut frame);
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
        let res = EthFrame::parse(&mut frame);
        assert_eq!(
            res,
            Some(EthFrame {
                header: EthernetRepr {
                    src_addr: Address([0x2, 0x3, 0x4, 0x5, 0x6, 0x7]),
                    dst_addr: Address([0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff]),
                    ethertype: EtherType::Arp
                },
                eth_type: PacketType::Arp(Arp {
                    htype: HardwareType::Ethernet,
                    ptype: EtherType::Ipv4,
                    hsize: 0x6,
                    psize: 0x4,
                    op: ArpOp::Request,
                    src_addr: Address([0x2, 0x3, 0x4, 0x5, 0x6, 0x7]),
                    src_protocol_addr: Ipv4Address([0xc0, 0xa8, 0x00, 0x01]),
                    dest_addr: Address([0x00, 0x00, 0x00, 0x00, 0x00, 0x00]),
                    dest_protocol_addr: Ipv4Address([0xc0, 0xa8, 0x0, 0xce])
                })
            })
        );
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
        let res = EthFrame::parse(&mut frame);
        assert_eq!(
            res,
            Some(EthFrame {
                header: EthernetRepr {
                    src_addr: Address([0x18, 0x20, 0x22, 0x24, 0x26, 0x28]),
                    dst_addr: Address([0x2, 0x3, 0x4, 0x5, 0x6, 0x7]),
                    ethertype: EtherType::Arp
                },
                eth_type: PacketType::Arp(Arp {
                    htype: HardwareType::Ethernet,
                    ptype: EtherType::Ipv4,
                    hsize: 0x6,
                    psize: 0x4,
                    op: ArpOp::Reply,
                    src_addr: Address([0x18, 0x20, 0x22, 0x24, 0x26, 0x28]),
                    src_protocol_addr: Ipv4Address([0xc0, 0xa8, 0x00, 0xce]),
                    dest_addr: Address([0x2, 0x3, 0x4, 0x5, 0x6, 0x7]),
                    dest_protocol_addr: Ipv4Address([0xc0, 0xa8, 0x0, 0x01])
                })
            })
        );
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
        let res = EthFrame::parse(&mut frame);
        assert!(res.is_none());

        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x06, 0x0,
            0x1, 0x9, 0x20, 0x6, 0x4, 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0xc0, 0xa8, 0x0, 0x1,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xc0, 0xa8, 0x0, 0xce,
        ];
        frame[0..42].copy_from_slice(&pkt);
        let res = EthFrame::parse(&mut frame);
        assert!(res.is_none());

        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x06, 0x0,
            0x1, 0x8, 0x0, 0x6, 0x4, 0x0, 0x6, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0xc0, 0xa8, 0x0, 0x1,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xc0, 0xa8, 0x0, 0xce,
        ];
        frame[0..42].copy_from_slice(&pkt);
        let res = EthFrame::parse(&mut frame);
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
        let res = EthFrame::parse(&mut frame);
        assert!(res.is_none());
    }

    #[test]
    fn valid_ipv4_protocols() {
        let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
        // Hop by Hop
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00, 0x45,
            0x0, 0x0, 0x29, 0x15, 0x5f, 0x40, 0x0, 0x80, 0x0, 0xf7, 0x28, 0xc0, 0xa8, 0x0, 0xce,
            0x34, 0x7f, 0xf8, 0x51, 0xc4, 0x73, 0x1, 0xbb, 0x21, 0x65, 0x90, 0xfb, 0xe4, 0x98,
            0x7c, 0x9d, 0x50, 0x10, 0x3, 0xff, 0xe3, 0xc7, 0x0, 0x0,
        ];
        frame[0..54].copy_from_slice(&pkt);

        let mut expected = EthFrame {
            header: EthernetRepr {
                src_addr: Address([0x2, 0x3, 0x4, 0x5, 0x6, 0x7]),
                dst_addr: Address([0xff, 0xff, 0xff, 0xff, 0xff, 0xff]),
                ethertype: EtherType::Ipv4,
            },
            eth_type: PacketType::Ipv4(Ipv4Packet {
                header: Ipv4Repr {
                    protocol: IpProtocol::HopByHop,
                    length: 0x29,
                },
                protocol: Ipv4ProtoPacket::TxOnly,
            }),
        };

        let res = EthFrame::parse(&mut frame);
        assert_eq!(res.as_ref(), Some(&expected));

        // ICMP
        frame[23] = 0x01;
        if let PacketType::Ipv4(pack) = &mut expected.eth_type {
            pack.header.protocol = IpProtocol::Icmp;
        }
        let res = EthFrame::parse(&mut frame);
        assert_eq!(res.as_ref(), Some(&expected));

        // IGMP
        frame[23] = 0x02;
        if let PacketType::Ipv4(pack) = &mut expected.eth_type {
            pack.header.protocol = IpProtocol::Igmp;
        }
        let res = EthFrame::parse(&mut frame);
        assert_eq!(res.as_ref(), Some(&expected));

        // Ipv6 Route
        frame[23] = 0x2b;
        if let PacketType::Ipv4(pack) = &mut expected.eth_type {
            pack.header.protocol = IpProtocol::Ipv6Route;
        }
        let res = EthFrame::parse(&mut frame);
        assert_eq!(res.as_ref(), Some(&expected));

        // Ipv6 Frag
        frame[23] = 0x2c;
        if let PacketType::Ipv4(pack) = &mut expected.eth_type {
            pack.header.protocol = IpProtocol::Ipv6Frag;
        }
        let res = EthFrame::parse(&mut frame);
        assert_eq!(res.as_ref(), Some(&expected));

        // ICMPv6
        frame[23] = 0x3a;
        if let PacketType::Ipv4(pack) = &mut expected.eth_type {
            pack.header.protocol = IpProtocol::Icmpv6;
        }
        let res = EthFrame::parse(&mut frame);
        assert_eq!(res.as_ref(), Some(&expected));

        // IPv6 No Nxt
        frame[23] = 0x3b;
        if let PacketType::Ipv4(pack) = &mut expected.eth_type {
            pack.header.protocol = IpProtocol::Ipv6NoNxt;
        }
        let res = EthFrame::parse(&mut frame);
        assert_eq!(res.as_ref(), Some(&expected));

        // IPv6 Opts
        frame[23] = 0x3c;
        if let PacketType::Ipv4(pack) = &mut expected.eth_type {
            pack.header.protocol = IpProtocol::Ipv6Opts;
        }
        let res = EthFrame::parse(&mut frame);
        assert_eq!(res.as_ref(), Some(&expected));

        // TCP
        frame[23] = 0x06;
        if let PacketType::Ipv4(pack) = &mut expected.eth_type {
            pack.header.protocol = IpProtocol::Tcp;
            pack.protocol = Ipv4ProtoPacket::Tcp(TcpRepr { dst_port: 443 });
        }
        let res = EthFrame::parse(&mut frame);
        assert_eq!(res.as_ref(), Some(&expected));

        // UDP
        frame[23] = 0x11;
        if let PacketType::Ipv4(pack) = &mut expected.eth_type {
            pack.header.protocol = IpProtocol::Udp;
            pack.protocol = Ipv4ProtoPacket::Udp(UdpRepr { dst_port: 443 });
        }
        let res = EthFrame::parse(&mut frame);
        assert_eq!(res.as_ref(), Some(&expected));
    }
}

#[cfg(test)]
mod can_send_rx_frame_tests {
    use super::*;

    #[test]
    fn malformed_packet() {
        let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x02, 0xC2,
        ];
        frame[0..14].copy_from_slice(&pkt);
        let res = can_send_rx_frame(&mut frame);
        assert!(!res);
    }

    #[test]
    fn valid_arp() {
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
    fn disallowed_tcp() {
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
    }

    #[test]
    fn allowed_tcp() {
        let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
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
    fn disallowed_udp() {
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
    }

    #[test]
    fn allowed_udp() {
        let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];
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
    fn valid_arp() {
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
