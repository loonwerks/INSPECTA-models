#![cfg_attr(not(test), no_std)]

use vstd::prelude::*;
use vstd::slice::slice_subrange;

mod net;
mod spec;
pub use net::{Arp, EtherType, EthernetRepr, IpProtocol, Ipv4Repr, TcpRepr, UdpRepr};

#[cfg(test)]
pub use net::{Address, ArpOp, HardwareType, Ipv4Address};

verus! {

pub use net::{wellformed_arp_frame, wellformed_ipv4_frame, frame_dst_addr_valid, frame_is_wellformed_eth2, frame_arp, frame_ipv4, frame_ipv6};

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
    HopByHop,
    Icmp,
    Igmp,
    Ipv6Route,
    Ipv6Frag,
    Icmpv6,
    Ipv6NoNxt,
    Ipv6Opts,
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

pub const ARP_TOTAL: usize = EthernetRepr::SIZE + Arp::SIZE;
pub const IPV4_TOTAL: usize = EthernetRepr::SIZE + Ipv4Repr::SIZE;
pub const TCP_TOTAL: usize = EthernetRepr::SIZE + Ipv4Repr::SIZE + TcpRepr::SIZE;
pub const UDP_TOTAL: usize = EthernetRepr::SIZE + Ipv4Repr::SIZE + UdpRepr::SIZE;

impl EthFrame {
    pub fn parse(frame: &[u8]) -> (r: Option<EthFrame>)
        requires
            frame@.len() >= TCP_TOTAL,
            frame@.len() >= UDP_TOTAL,
            frame@.len() >= ARP_TOTAL
        ensures
            net::wellformed_arp_packet(frame@.subrange(14, 42)) == net::wellformed_arp_frame(frame@),
            net::wellformed_ipv4_packet(frame@.subrange(14, 34)) == net::wellformed_ipv4_frame(frame@),

            (
                net::frame_dst_addr_valid(frame@)
                && frame_is_wellformed_eth2(frame)
                && net::frame_arp(frame)
                && net::wellformed_arp_frame(frame@)
            )
                    == (r.is_some() && r.unwrap().eth_type is Arp),
            (
                net::frame_dst_addr_valid(frame@)
                && frame_is_wellformed_eth2(frame)
                && net::frame_ipv4(frame)
                && net::wellformed_ipv4_frame(frame@)
            )
                    == (r.is_some() && r.unwrap().eth_type is Ipv4),
            (
                net::frame_dst_addr_valid(frame@)
                && frame_is_wellformed_eth2(frame)
                && net::frame_ipv6(frame)
            )
                    == (r.is_some() && r.unwrap().eth_type is Ipv6),
            (r.is_some() && r.unwrap().eth_type is Ipv4) ==> ipv4_valid_length(r.unwrap().eth_type),

    {
        let header = EthernetRepr::parse(slice_subrange(frame, 0, EthernetRepr::SIZE));

           assert( (
                net::frame_dst_addr_valid(frame@)
                && frame_is_wellformed_eth2(frame)
            )
                    == header.is_some()
        );

           assert( (
                net::frame_dst_addr_valid(frame@)
                && frame_is_wellformed_eth2(frame)
                && net::frame_arp(frame)
            )
                    == (header.is_some() && header.unwrap().ethertype is Arp)
        );
            assert(net::wellformed_arp_packet(frame@.subrange(14, 42)) == net::wellformed_arp_frame(frame@));
            assert(net::wellformed_ipv4_packet(frame@.subrange(14, 34)) == net::wellformed_ipv4_frame(frame@));




        let header = header?;

        let eth_type = match header.ethertype {
             EtherType::Arp => {
                 let a = Arp::parse(slice_subrange(frame, EthernetRepr::SIZE, ARP_TOTAL));

                 let a = a?;
                 PacketType::Arp(a)
             }
            EtherType::Ipv4 => {
                let ip = Ipv4Repr::parse(slice_subrange(frame, EthernetRepr::SIZE, IPV4_TOTAL));
                // TODO: Check that the entire IPv4 Packet is not malformed
                assert(ip.is_some() ==> ip.unwrap().length <= net::MAX_MTU);
                let ip = ip?;

                let protocol = match ip.protocol {
                    IpProtocol::Tcp => Ipv4ProtoPacket::Tcp(TcpRepr::parse(
                        slice_subrange(frame, IPV4_TOTAL, TCP_TOTAL),
                    )),
                    IpProtocol::Udp => Ipv4ProtoPacket::Udp(UdpRepr::parse(
                        slice_subrange(frame, IPV4_TOTAL, UDP_TOTAL),
                    )),
                    IpProtocol::HopByHop => Ipv4ProtoPacket::HopByHop,
                    IpProtocol::Icmp => Ipv4ProtoPacket::Icmp,
                    IpProtocol::Igmp => Ipv4ProtoPacket::Igmp,
                    IpProtocol::Ipv6Route => Ipv4ProtoPacket::Ipv6Route,
                    IpProtocol::Ipv6Frag => Ipv4ProtoPacket::Ipv6Frag,
                    IpProtocol::Icmpv6 => Ipv4ProtoPacket::Icmpv6,
                    IpProtocol::Ipv6NoNxt => Ipv4ProtoPacket::Ipv6NoNxt,
                    IpProtocol::Ipv6Opts => Ipv4ProtoPacket::Ipv6Opts,
                };
                PacketType::Ipv4(Ipv4Packet {
                    header: ip,
                    protocol,
                })
            }
            EtherType::Ipv6 => PacketType::Ipv6,
        };

            assert((
                net::frame_dst_addr_valid(frame@)
                && frame_is_wellformed_eth2(frame)
                && net::frame_ipv4(frame)
                && net::wellformed_ipv4_frame(frame@)
            )
                    == (eth_type is Ipv4));
        assert(eth_type is Ipv4 ==> net::valid_ipv4_length(frame@));
        assert(eth_type is Ipv4 && net::valid_ipv4_length(frame@) ==> ipv4_valid_length(eth_type));

           assert( (
                net::frame_dst_addr_valid(frame@)
                && frame_is_wellformed_eth2(frame)
                && net::frame_arp(frame)
                && net::wellformed_arp_packet(frame@.subrange(14, 28))
            )
                    == (eth_type is Arp)
        );


        Some(EthFrame { header, eth_type })
    }
}


pub open spec fn ipv4_valid_length(p: PacketType) -> bool
{
    // p is Ipv4
    p->Ipv4_0.header.length <= net::MAX_MTU
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
                protocol: Ipv4ProtoPacket::HopByHop,
            }),
        };

        let res = EthFrame::parse(&frame);
        assert_eq!(res.as_ref(), Some(&expected));

        // ICMP
        frame[23] = 0x01;
        if let PacketType::Ipv4(pack) = &mut expected.eth_type {
            pack.header.protocol = IpProtocol::Icmp;
            pack.protocol = Ipv4ProtoPacket::Icmp;
        }
        let res = EthFrame::parse(&frame);
        assert_eq!(res.as_ref(), Some(&expected));

        // IGMP
        frame[23] = 0x02;
        if let PacketType::Ipv4(pack) = &mut expected.eth_type {
            pack.header.protocol = IpProtocol::Igmp;
            pack.protocol = Ipv4ProtoPacket::Igmp;
        }
        let res = EthFrame::parse(&frame);
        assert_eq!(res.as_ref(), Some(&expected));

        // Ipv6 Route
        frame[23] = 0x2b;
        if let PacketType::Ipv4(pack) = &mut expected.eth_type {
            pack.header.protocol = IpProtocol::Ipv6Route;
            pack.protocol = Ipv4ProtoPacket::Ipv6Route;
        }
        let res = EthFrame::parse(&frame);
        assert_eq!(res.as_ref(), Some(&expected));

        // Ipv6 Frag
        frame[23] = 0x2c;
        if let PacketType::Ipv4(pack) = &mut expected.eth_type {
            pack.header.protocol = IpProtocol::Ipv6Frag;
            pack.protocol = Ipv4ProtoPacket::Ipv6Frag;
        }
        let res = EthFrame::parse(&frame);
        assert_eq!(res.as_ref(), Some(&expected));

        // ICMPv6
        frame[23] = 0x3a;
        if let PacketType::Ipv4(pack) = &mut expected.eth_type {
            pack.header.protocol = IpProtocol::Icmpv6;
            pack.protocol = Ipv4ProtoPacket::Icmpv6;
        }
        let res = EthFrame::parse(&frame);
        assert_eq!(res.as_ref(), Some(&expected));

        // IPv6 No Nxt
        frame[23] = 0x3b;
        if let PacketType::Ipv4(pack) = &mut expected.eth_type {
            pack.header.protocol = IpProtocol::Ipv6NoNxt;
            pack.protocol = Ipv4ProtoPacket::Ipv6NoNxt;
        }
        let res = EthFrame::parse(&frame);
        assert_eq!(res.as_ref(), Some(&expected));

        // IPv6 Opts
        frame[23] = 0x3c;
        if let PacketType::Ipv4(pack) = &mut expected.eth_type {
            pack.header.protocol = IpProtocol::Ipv6Opts;
            pack.protocol = Ipv4ProtoPacket::Ipv6Opts;
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
