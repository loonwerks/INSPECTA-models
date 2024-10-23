#![cfg_attr(not(test), no_std)]

mod net;
pub use net::{Arp, EtherType, EthernetRepr, IpProtocol, Ipv4Repr, TcpRepr, UdpRepr};

#[cfg(test)]
pub use net::{Address, ArpOp, HardwareType, Ipv4Address};

#[cfg_attr(test, derive(PartialEq))]
#[derive(Debug)]
pub enum PacketType {
    Arp(Arp),
    Ipv4(Ipv4Packet),
    Ipv6,
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
            EtherType::Arp => Arp::parse(&frame[EthernetRepr::SIZE..]).map(PacketType::Arp)?,
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
            EtherType::Ipv6 => PacketType::Ipv6,
            // Throw away any frame that has an unknown EtherType
            _ => return None,
        };

        Some(EthFrame { header, eth_type })
    }
}

#[cfg(test)]
mod eth_frame_tests {
    use super::*;

    #[test]
    fn dest_mac_empty() {
        let mut frame = [0u8; 128];
        let pkt = [0u8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0x08, 0];
        frame[0..14].copy_from_slice(&pkt);
        let res = EthFrame::parse(&frame);
        assert!(res.is_none());
    }

    #[test]
    fn malformed_eth_header() {
        let mut frame = [0u8; 128];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x02, 0xC2,
        ];
        frame[0..14].copy_from_slice(&pkt);
        let res = EthFrame::parse(&frame);
        assert!(res.is_none());
    }

    #[test]
    fn eth_type_ipv6() {
        let mut frame = [0u8; 128];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x86, 0xDD,
        ];
        frame[0..14].copy_from_slice(&pkt);
        let res = EthFrame::parse(&frame);
        assert_eq!(
            res,
            Some(EthFrame {
                header: EthernetRepr {
                    src_addr: Address([0x2, 0x3, 0x4, 0x5, 0x6, 0x7]),
                    dst_addr: Address([0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff]),
                    ethertype: EtherType::Ipv6
                },
                eth_type: PacketType::Ipv6,
            })
        );
    }

    #[test]
    fn valid_arp_request() {
        let mut frame = [0u8; 128];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x06, 0x0,
            0x1, 0x8, 0x0, 0x6, 0x4, 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0xc0, 0xa8, 0x0, 0x1,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xc0, 0xa8, 0x0, 0xce,
        ];
        frame[0..42].copy_from_slice(&pkt);
        let res = EthFrame::parse(&frame);
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
        let mut frame = [0u8; 128];
        let pkt = [
            0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x18, 0x20, 0x22, 0x24, 0x26, 0x28, 0x8, 0x6, 0x0, 0x1,
            0x8, 0x0, 0x6, 0x4, 0x0, 0x2, 0x18, 0x20, 0x22, 0x24, 0x26, 0x28, 0xc0, 0xa8, 0x0,
            0xce, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0xc0, 0xa8, 0x0, 0x1,
        ];
        frame[0..42].copy_from_slice(&pkt);
        let res = EthFrame::parse(&frame);
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
        let mut frame = [0u8; 128];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x06, 0x0,
            0x2, 0x8, 0x0, 0x6, 0x4, 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0xc0, 0xa8, 0x0, 0x1,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xc0, 0xa8, 0x0, 0xce,
        ];
        frame[0..42].copy_from_slice(&pkt);
        let res = EthFrame::parse(&frame);
        assert!(res.is_none());

        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x06, 0x0,
            0x1, 0x9, 0x20, 0x6, 0x4, 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0xc0, 0xa8, 0x0, 0x1,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xc0, 0xa8, 0x0, 0xce,
        ];
        frame[0..42].copy_from_slice(&pkt);
        let res = EthFrame::parse(&frame);
        assert!(res.is_none());

        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x06, 0x0,
            0x1, 0x8, 0x0, 0x6, 0x4, 0x0, 0x6, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0xc0, 0xa8, 0x0, 0x1,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xc0, 0xa8, 0x0, 0xce,
        ];
        frame[0..42].copy_from_slice(&pkt);
        let res = EthFrame::parse(&frame);
        assert!(res.is_none());
    }

    #[test]
    fn malformed_ipv4() {
        let mut frame = [0u8; 128];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00, 0x45,
            0x0, 0x0, 0x29, 0x15, 0x5f, 0x40, 0x0, 0x80, 0x7, 0xf7, 0x28, 0xc0, 0xa8, 0x0, 0xce,
            0x34, 0x7f, 0xf8, 0x51,
        ];
        frame[0..34].copy_from_slice(&pkt);
        let res = EthFrame::parse(&frame);
        assert!(res.is_none());
    }

    #[test]
    fn valid_ipv4_protocols() {
        let mut frame = [0u8; 128];
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

        let res = EthFrame::parse(&frame);
        assert_eq!(res.as_ref(), Some(&expected));

        // ICMP
        frame[23] = 0x01;
        if let PacketType::Ipv4(pack) = &mut expected.eth_type {
            pack.header.protocol = IpProtocol::Icmp;
        }
        let res = EthFrame::parse(&frame);
        assert_eq!(res.as_ref(), Some(&expected));

        // IGMP
        frame[23] = 0x02;
        if let PacketType::Ipv4(pack) = &mut expected.eth_type {
            pack.header.protocol = IpProtocol::Igmp;
        }
        let res = EthFrame::parse(&frame);
        assert_eq!(res.as_ref(), Some(&expected));

        // Ipv6 Route
        frame[23] = 0x2b;
        if let PacketType::Ipv4(pack) = &mut expected.eth_type {
            pack.header.protocol = IpProtocol::Ipv6Route;
        }
        let res = EthFrame::parse(&frame);
        assert_eq!(res.as_ref(), Some(&expected));

        // Ipv6 Frag
        frame[23] = 0x2c;
        if let PacketType::Ipv4(pack) = &mut expected.eth_type {
            pack.header.protocol = IpProtocol::Ipv6Frag;
        }
        let res = EthFrame::parse(&frame);
        assert_eq!(res.as_ref(), Some(&expected));

        // ICMPv6
        frame[23] = 0x3a;
        if let PacketType::Ipv4(pack) = &mut expected.eth_type {
            pack.header.protocol = IpProtocol::Icmpv6;
        }
        let res = EthFrame::parse(&frame);
        assert_eq!(res.as_ref(), Some(&expected));

        // IPv6 No Nxt
        frame[23] = 0x3b;
        if let PacketType::Ipv4(pack) = &mut expected.eth_type {
            pack.header.protocol = IpProtocol::Ipv6NoNxt;
        }
        let res = EthFrame::parse(&frame);
        assert_eq!(res.as_ref(), Some(&expected));

        // IPv6 Opts
        frame[23] = 0x3c;
        if let PacketType::Ipv4(pack) = &mut expected.eth_type {
            pack.header.protocol = IpProtocol::Ipv6Opts;
        }
        let res = EthFrame::parse(&frame);
        assert_eq!(res.as_ref(), Some(&expected));

        // TCP
        frame[23] = 0x06;
        if let PacketType::Ipv4(pack) = &mut expected.eth_type {
            pack.header.protocol = IpProtocol::Tcp;
            pack.protocol = Ipv4ProtoPacket::Tcp(TcpRepr { dst_port: 443 });
        }
        let res = EthFrame::parse(&frame);
        assert_eq!(res.as_ref(), Some(&expected));

        // UDP
        frame[23] = 0x11;
        if let PacketType::Ipv4(pack) = &mut expected.eth_type {
            pack.header.protocol = IpProtocol::Udp;
            pack.protocol = Ipv4ProtoPacket::Udp(UdpRepr { dst_port: 443 });
        }
        let res = EthFrame::parse(&frame);
        assert_eq!(res.as_ref(), Some(&expected));
    }
}
