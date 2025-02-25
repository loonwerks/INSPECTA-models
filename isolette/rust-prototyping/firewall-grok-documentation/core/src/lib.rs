#![cfg_attr(not(test), no_std)]

//! This Rust code provides a framework for parsing and representing Ethernet 
//! frames and their encapsulated network packets, leveraging the lower-level 
//! structures from the `net` module. It is designed to operate in a `no_std` 
//! environment (except during tests) for lightweight, embedded, or systems 
//! programming use cases.
//!
//! - **`no_std` Attribute**: The `#![cfg_attr(not(test), no_std)]` directive 
//!   ensures the code avoids the Rust standard library unless running tests, 
//!   making it suitable for constrained environments like embedded systems.
//! - **Imports from `net`**: The code re-exports key types (`Arp`, `EtherType`, 
//!   `EthernetRepr`, etc.) from the `net` module for public use, with additional 
//!   test-only exports (`Address`, `ArpOp`, etc.) for validation.
//! - **`PacketType` Enum**: Represents the type of packet encapsulated in an 
//!   Ethernet frame, supporting ARP (`Arp`), IPv4 (`Ipv4Packet`), and IPv6 
//!   (`Ipv6`), with extensibility for other protocols.
//! - **`Ipv4ProtoPacket` Enum**: Models the protocol-specific payload of an 
//!   IPv4 packet, currently supporting TCP (`TcpRepr`), UDP (`UdpRepr`), and a 
//!   `TxOnly` variant for unparsed or transmit-only protocols.
//! - **`Ipv4Packet` Struct**: Combines an `Ipv4Repr` header with an 
//!   `Ipv4ProtoPacket` payload, providing a structured representation of an 
//!   IPv4 packet.
//! - **`EthFrame` Struct and `parse` Method**: The core abstraction, `EthFrame`, 
//!   pairs an `EthernetRepr` header with a `PacketType`. The `parse` method 
//!   decodes raw Ethernet frame bytes into this structure, handling ARP, IPv4 
//!   (with TCP/UDP parsing), and IPv6, while rejecting malformed or unsupported 
//!   frames.
//! - **Tests**: Unit tests in the `eth_frame_tests` module verify parsing 
//!   behavior, including edge cases (e.g., empty destination MAC, malformed 
//!   headers) and valid scenarios (e.g., ARP requests/replies, IPv4 with 
//!   various protocols).

mod net;
pub use net::{Arp, EtherType, EthernetRepr, IpProtocol, Ipv4Repr, TcpRepr, UdpRepr};

#[cfg(test)]
pub use net::{Address, ArpOp, HardwareType, Ipv4Address};

#[cfg_attr(test, derive(PartialEq))]
#[derive(Debug)]
/// Represents the type of packet encapsulated within an Ethernet frame.
///
/// Variants include ARP, IPv4 with a detailed packet structure, and IPv6 as a 
/// placeholder for future expansion.
pub enum PacketType {
    Arp(Arp),
    Ipv4(Ipv4Packet),
    Ipv6,
}

#[cfg_attr(test, derive(PartialEq))]
#[derive(Debug)]
/// Models the protocol-specific payload of an IPv4 packet.
///
/// Supports TCP and UDP with parsed representations, and a `TxOnly` variant for 
/// unparsed or transmit-only protocols.
pub enum Ipv4ProtoPacket {
    Tcp(TcpRepr),
    Udp(UdpRepr),
    TxOnly,
}

#[cfg_attr(test, derive(PartialEq))]
#[derive(Debug)]
/// Represents an IPv4 packet, combining header and protocol payload.
///
/// Contains an `Ipv4Repr` for header details and an `Ipv4ProtoPacket` for the 
/// encapsulated protocol data.
pub struct Ipv4Packet {
    pub header: Ipv4Repr,
    pub protocol: Ipv4ProtoPacket,
}

#[cfg_attr(test, derive(PartialEq))]
#[derive(Debug)]
/// Represents an Ethernet frame with its header and packet type.
///
/// Pairs an `EthernetRepr` header with a `PacketType` to describe the frame's 
/// structure and contents.
pub struct EthFrame {
    pub header: EthernetRepr,
    pub eth_type: PacketType,
}

impl EthFrame {
    /// Parses an Ethernet frame from a byte slice into an `EthFrame` structure.
    ///
    /// # Arguments
    /// * `frame` - A slice containing at least the Ethernet header (14 bytes) 
    ///   and potentially additional packet data.
    ///
    /// # Returns
    /// `Some(EthFrame)` if the frame is valid and supported (ARP, IPv4 with 
    /// TCP/UDP, or IPv6), `None` if malformed, empty (all-zero destination MAC), 
    /// or using an unsupported EtherType.
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
    /// Verifies that `EthFrame::parse` rejects frames with an all-zero 
    /// destination MAC address.
    ///
    /// Constructs a frame with a zeroed destination MAC and confirms parsing 
    /// returns `None` due to the `is_empty` check.
    fn dest_mac_empty() {
        let mut frame = [0u8; 128];
        let pkt = [0u8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0x08, 0];
        frame[0..14].copy_from_slice(&pkt);
        let res = EthFrame::parse(&frame);
        assert!(res.is_none());
    }

    #[test]
    /// Ensures `EthFrame::parse` rejects frames with a malformed Ethernet 
    /// header due to an unknown EtherType.
    ///
    /// Uses a frame with EtherType `0x02C2` (unknown) and verifies parsing 
    /// returns `None`.
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
    /// Confirms `EthFrame::parse` correctly handles an IPv6 Ethernet frame.
    ///
    /// Parses a frame with EtherType `0x86DD` and checks it produces an 
    /// `EthFrame` with `PacketType::Ipv6` and the expected header fields.
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
    /// Verifies `EthFrame::parse` correctly parses a valid ARP request frame.
    ///
    /// Uses a 42-byte frame with an ARP request and confirms the resulting 
    /// `EthFrame` matches the expected header and `PacketType::Arp` structure.
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
    /// Ensures `EthFrame::parse` correctly parses a valid ARP reply frame.
    ///
    /// Uses a 42-byte frame with an ARP reply and verifies the resulting 
    /// `EthFrame` matches the expected header and `PacketType::Arp` structure.
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
    /// Confirms `EthFrame::parse` rejects malformed ARP packets within an 
    /// Ethernet frame.
    ///
    /// Tests three cases: an ARP with an unknown hardware type (`0x0002`), an 
    /// unknown protocol type (`0x0920`), and an unknown operation (`0x0006`), 
    /// each resulting in `None`.
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
    /// Verifies `EthFrame::parse` rejects a malformed IPv4 packet due to an 
    /// unknown protocol.
    ///
    /// Uses a frame with an IPv4 header and protocol `0x07` (unknown), ensuring 
    /// parsing returns `None` due to validation failure in `Ipv4Repr`.
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
    /// Ensures `EthFrame::parse` correctly handles valid IPv4 packets with 
    /// various protocols.
    ///
    /// Tests parsing of IPv4 frames with protocols: HopByHop, ICMP, IGMP, 
    /// Ipv6Route, Ipv6Frag, ICMPv6, Ipv6NoNxt, Ipv6Opts (all as `TxOnly`), plus 
    /// TCP and UDP with parsed headers, confirming each matches the expected 
    /// `EthFrame` structure.
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