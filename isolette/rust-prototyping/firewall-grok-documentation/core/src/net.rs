//! This Rust code defines a set of structures and implementations for parsing 
//! and representing various network protocol headers, primarily focused on 
//! Ethernet, ARP, IPv4, TCP, and UDP.
//!
//! - **Ipv4Address and Address**: These structures represent IPv4 addresses 
//!   (4 bytes) and MAC addresses (6 bytes), respectively. They provide methods 
//!   to create instances from byte slices, enabling easy conversion from raw 
//!   network data.
//! - **EtherType**: An enum representing Ethernet frame types (e.g., IPv4, ARP, 
//!   IPv6) with a mechanism to parse from bytes and convert to/from 16-bit 
//!   values. It’s used to identify the protocol encapsulated in an Ethernet 
//!   frame.
//! - **EthernetRepr**: This structure models an Ethernet II frame, containing 
//!   source and destination MAC addresses and an EtherType. It includes 
//!   methods to:
//!   - Parse a frame from raw bytes.
//!   - Check if the frame is well-formed (i.e., has a known EtherType).
//!   - Emit the structure back into a byte array.
//! - **ArpOp and HardwareType**: Enums for ARP operation codes (Request, Reply) 
//!   and hardware types (e.g., Ethernet), with parsing and conversion logic 
//!   similar to EtherType.
//! - **Arp**: Represents an ARP packet, including hardware and protocol types, 
//!   sizes, operation code, and source/destination MAC and IP addresses 
//!   (currently fixed to IPv4). It provides parsing, validation 
//!   (well-formedness), and emission capabilities.
//! - **IpProtocol**: An enum for IP protocol numbers (e.g., TCP, UDP, ICMP), 
//!   with conversions to/from 8-bit values.
//! - **Ipv4Repr**: Models an IPv4 header, focusing on the protocol type and 
//!   total length, with parsing and validation logic.
//! - **TcpRepr and UdpRepr**: Simple representations of TCP and UDP headers, 
//!   currently only parsing the destination port from raw bytes.
//! - **Tests**: The code includes unit tests to verify conversions (e.g., 
//!   EtherType to u16) and parsing/emission logic for Ethernet and ARP 
//!   structures.

#[cfg_attr(test, derive(PartialEq))]
#[derive(Debug)]

pub struct Ipv4Address(pub [u8; 4]);

impl Ipv4Address {
    /// Creates an `Ipv4Address` from a byte slice.
    ///
    /// # Arguments
    /// * `data` - A slice of at least 4 bytes representing an IPv4 address.
    ///
    /// # Panics
    /// Panics if `data` is shorter than 4 bytes.
    ///
    /// # Returns
    /// A new `Ipv4Address` instance containing the first 4 bytes of `data`.
    pub fn from_bytes(data: &[u8]) -> Ipv4Address {
        let mut bytes = [0; 4];
        bytes.copy_from_slice(data);
        Ipv4Address(bytes)
    }
}

#[cfg_attr(test, derive(PartialEq))]
#[derive(Debug)]
pub struct Address(pub [u8; 6]);

impl Address {
    /// Creates an `Address` from a byte slice.
    ///
    /// # Arguments
    /// * `data` - A slice of at least 6 bytes representing a MAC address.
    ///
    /// # Panics
    /// Panics if `data` is shorter than 6 bytes.
    ///
    /// # Returns
    /// A new `Address` instance containing the first 6 bytes of `data`.
    pub fn from_bytes(data: &[u8]) -> Address {
        let mut bytes = [0; 6];
        bytes.copy_from_slice(data);
        Address(bytes)
    }

    /// Checks if the MAC address is all zeros.
    ///
    /// # Returns
    /// `true` if all bytes in the address are zero, `false` otherwise.
    pub fn is_empty(&self) -> bool {
        self.0.iter().filter(|x| **x != 0).count() == 0
    }
}

#[cfg_attr(test, derive(PartialEq))]
#[derive(Debug, Clone)]
#[repr(u16)]
pub enum EtherType {
    Ipv4 = 0x0800,
    Arp = 0x0806,
    Ipv6 = 0x86DD,
    Unknown(u16),
}

impl EtherType {
    /// Parses an `EtherType` from a byte slice.
    ///
    /// # Arguments
    /// * `bytes` - A slice of at least 2 bytes representing the EtherType in big-endian order.
    ///
    /// # Panics
    /// Panics if `bytes` is shorter than 2 bytes.
    ///
    /// # Returns
    /// An `EtherType` variant corresponding to the parsed value, or `Unknown` if unrecognized.
    pub fn from_bytes(bytes: &[u8]) -> Self {
        let mut data = [0u8; 2];
        data.copy_from_slice(bytes);
        let raw = u16::from_be_bytes(data);
        EtherType::from(raw)
    }
}

impl From<u16> for EtherType {
    /// Converts a 16-bit value into an `EtherType`.
    ///
    /// # Arguments
    /// * `value` - The 16-bit EtherType value.
    ///
    /// # Returns
    /// An `EtherType` variant matching the value, or `Unknown` if not predefined.
    fn from(value: u16) -> Self {
        match value {
            0x0800 => EtherType::Ipv4,
            0x0806 => EtherType::Arp,
            0x86DD => EtherType::Ipv6,
            other => EtherType::Unknown(other),
        }
    }
}

impl From<EtherType> for u16 {
    /// Converts an `EtherType` into its 16-bit representation.
    ///
    /// # Arguments
    /// * `value` - The `EtherType` to convert.
    ///
    /// # Returns
    /// The 16-bit value of the `EtherType`.
    fn from(value: EtherType) -> Self {
        match value {
            EtherType::Ipv4 => 0x0800,
            EtherType::Arp => 0x0806,
            EtherType::Ipv6 => 0x86DD,
            EtherType::Unknown(other) => other,
        }
    }
}

#[cfg_attr(test, derive(PartialEq))]
#[derive(Debug)]
pub struct EthernetRepr {
    pub src_addr: Address,
    pub dst_addr: Address,
    pub ethertype: EtherType,
}

impl EthernetRepr {
    pub const SIZE: usize = 14;

    /// Parses an Ethernet II frame from a byte slice.
    ///
    /// # Arguments
    /// * `frame` - A slice of at least 14 bytes containing the Ethernet frame.
    ///
    /// # Returns
    /// `Some(EthernetRepr)` if the frame is well-formed, `None` otherwise.
    pub fn parse(frame: &[u8]) -> Option<EthernetRepr> {
        let dst_addr = Address::from_bytes(&frame[0..6]);
        let src_addr = Address::from_bytes(&frame[6..12]);
        let ethertype = EtherType::from_bytes(&frame[12..14]);
        let e = EthernetRepr {
            src_addr,
            dst_addr,
            ethertype,
        };
        if e.is_wellformed() {
            Some(e)
        } else {
            None
        }
    }

    /// Checks if the destination address is all zeros.
    ///
    /// # Returns
    /// `true` if the destination MAC address is empty, `false` otherwise.
    pub fn is_empty(&self) -> bool {
        self.dst_addr.is_empty()
    }

    /// Verifies if the Ethernet frame has a known EtherType.
    ///
    /// # Returns
    /// `true` if the `ethertype` is a known variant, `false` if `Unknown`.
    pub fn is_wellformed(&self) -> bool {
        if let EtherType::Unknown(_) = self.ethertype {
            return false;
        }
        true
    }

    /// Emits the Ethernet frame into a byte array.
    ///
    /// # Arguments
    /// * `frame` - A mutable slice of at least 14 bytes to store the frame.
    ///
    /// # Panics
    /// Panics if `frame` is shorter than 14 bytes.
    pub fn emit(&self, frame: &mut [u8]) {
        frame[0..6].copy_from_slice(&self.dst_addr.0);
        frame[6..12].copy_from_slice(&self.src_addr.0);
        let ethertype: u16 = self.ethertype.clone().into();
        frame[12..14].copy_from_slice(&ethertype.to_be_bytes());
    }
}

#[cfg_attr(test, derive(PartialEq))]
#[derive(Debug, Clone)]
#[repr(u16)]
pub enum ArpOp {
    Request = 1,
    Reply = 2,
    Unknown(u16),
}

impl ArpOp {
    /// Parses an `ArpOp` from a byte slice.
    ///
    /// # Arguments
    /// * `bytes` - A slice of at least 2 bytes representing the ARP operation in big-endian order.
    ///
    /// # Panics
    /// Panics if `bytes` is shorter than 2 bytes.
    ///
    /// # Returns
    /// An `ArpOp` variant corresponding to the parsed value, or `Unknown` if unrecognized.
    pub fn from_bytes(bytes: &[u8]) -> Self {
        let mut data = [0u8; 2];
        data.copy_from_slice(bytes);
        let raw = u16::from_be_bytes(data);
        ArpOp::from(raw)
    }
}

impl From<u16> for ArpOp {
    /// Converts a 16-bit value into an `ArpOp`.
    ///
    /// # Arguments
    /// * `value` - The 16-bit ARP operation code.
    ///
    /// # Returns
    /// An `ArpOp` variant matching the value, or `Unknown` if not predefined.
    fn from(value: u16) -> Self {
        match value {
            1 => ArpOp::Request,
            2 => ArpOp::Reply,
            other => ArpOp::Unknown(other),
        }
    }
}

impl From<ArpOp> for u16 {
    /// Converts an `ArpOp` into its 16-bit representation.
    ///
    /// # Arguments
    /// * `value` - The `ArpOp` to convert.
    ///
    /// # Returns
    /// The 16-bit value of the `ArpOp`.
    fn from(value: ArpOp) -> Self {
        match value {
            ArpOp::Request => 1,
            ArpOp::Reply => 2,
            ArpOp::Unknown(other) => other,
        }
    }
}

#[cfg_attr(test, derive(PartialEq))]
#[derive(Debug, Clone)]
#[repr(u16)]
pub enum HardwareType {
    Ethernet = 1,
    Unknown(u16),
}

impl HardwareType {
    /// Parses a `HardwareType` from a byte slice.
    ///
    /// # Arguments
    /// * `bytes` - A slice of at least 2 bytes representing the hardware type in big-endian order.
    ///
    /// # Panics
    /// Panics if `bytes` is shorter than 2 bytes.
    ///
    /// # Returns
    /// A `HardwareType` variant corresponding to the parsed value, or `Unknown` if unrecognized.
    pub fn from_bytes(bytes: &[u8]) -> Self {
        let mut data = [0u8; 2];
        data.copy_from_slice(bytes);
        let raw = u16::from_be_bytes(data);
        HardwareType::from(raw)
    }
}

impl From<u16> for HardwareType {
    /// Converts a 16-bit value into a `HardwareType`.
    ///
    /// # Arguments
    /// * `value` - The 16-bit hardware type value.
    ///
    /// # Returns
    /// A `HardwareType` variant matching the value, or `Unknown` if not predefined.
    fn from(value: u16) -> Self {
        match value {
            1 => HardwareType::Ethernet,
            other => HardwareType::Unknown(other),
        }
    }
}

impl From<HardwareType> for u16 {
    /// Converts a `HardwareType` into its 16-bit representation.
    ///
    /// # Arguments
    /// * `value` - The `HardwareType` to convert.
    ///
    /// # Returns
    /// The 16-bit value of the `HardwareType`.
    fn from(value: HardwareType) -> Self {
        match value {
            HardwareType::Ethernet => 1,
            HardwareType::Unknown(other) => other,
        }
    }
}

#[cfg_attr(test, derive(PartialEq))]
#[derive(Debug)]
pub struct Arp {
    pub htype: HardwareType,
    pub ptype: EtherType,
    pub hsize: u8,
    pub psize: u8,
    pub op: ArpOp,
    pub src_addr: Address,
    pub src_protocol_addr: Ipv4Address,
    pub dest_addr: Address,
    pub dest_protocol_addr: Ipv4Address,
}

impl Arp {
    pub const SIZE: usize = 28;

    /// Parses an ARP packet from a byte slice.
    ///
    /// # Arguments
    /// * `packet` - A slice of at least 28 bytes containing the ARP packet.
    ///
    /// # Returns
    /// `Some(Arp)` if the packet is well-formed, `None` otherwise.
    pub fn parse(packet: &[u8]) -> Option<Arp> {
        let htype = HardwareType::from_bytes(&packet[0..2]);
        let ptype = EtherType::from_bytes(&packet[2..4]);
        let hsize = packet[4];
        let psize = packet[5];
        let op = ArpOp::from_bytes(&packet[6..8]);
        let src_addr = Address::from_bytes(&packet[8..14]);
        let src_protocol_addr = Ipv4Address::from_bytes(&packet[14..18]);
        let dest_addr = Address::from_bytes(&packet[18..24]);
        let dest_protocol_addr = Ipv4Address::from_bytes(&packet[24..28]);
        let a = Arp {
            htype,
            ptype,
            hsize,
            psize,
            op,
            src_addr,
            src_protocol_addr,
            dest_addr,
            dest_protocol_addr,
        };
        if a.is_wellformed() {
            Some(a)
        } else {
            None
        }
    }

    /// Verifies if the ARP packet is well-formed.
    ///
    /// # Returns
    /// `true` if the hardware type, protocol type, and operation are known variants, `false` otherwise.
    pub fn is_wellformed(&self) -> bool {
        if let HardwareType::Unknown(_) = self.htype {
            return false;
        }
        match self.ptype {
            EtherType::Ipv4 => (),
            EtherType::Ipv6 => (),
            _ => return false,
        };
        if let ArpOp::Unknown(_) = self.op {
            return false;
        }
        true
    }

    /// Emits the ARP packet into a byte array.
    ///
    /// # Arguments
    /// * `frame` - A mutable slice of at least 28 bytes to store the ARP packet.
    ///
    /// # Panics
    /// Panics if `frame` is shorter than 28 bytes.
    pub fn emit(&self, frame: &mut [u8]) {
        let htype: u16 = self.htype.clone().into();
        let ptype: u16 = self.ptype.clone().into();
        let op: u16 = self.op.clone().into();
        frame[0..2].copy_from_slice(&htype.to_be_bytes());
        frame[2..4].copy_from_slice(&ptype.to_be_bytes());
        frame[4] = self.hsize;
        frame[5] = self.psize;
        frame[6..8].copy_from_slice(&op.to_be_bytes());
        frame[8..14].copy_from_slice(&self.src_addr.0);
        frame[14..18].copy_from_slice(&self.src_protocol_addr.0);
        frame[18..24].copy_from_slice(&self.dest_addr.0);
        frame[24..28].copy_from_slice(&self.dest_protocol_addr.0);
    }
}

#[cfg_attr(test, derive(PartialEq))]
#[derive(Debug, Clone)]
#[repr(u8)]
pub enum IpProtocol {
    HopByHop = 0x00,
    Icmp = 0x01,
    Igmp = 0x02,
    Tcp = 0x06,
    Udp = 0x11,
    Ipv6Route = 0x2b,
    Ipv6Frag = 0x2c,
    Icmpv6 = 0x3a,
    Ipv6NoNxt = 0x3b,
    Ipv6Opts = 0x3c,
    Unknown(u8),
}

impl From<u8> for IpProtocol {
    /// Converts an 8-bit value into an `IpProtocol`.
    ///
    /// # Arguments
    /// * `value` - The 8-bit IP protocol number.
    ///
    /// # Returns
    /// An `IpProtocol` variant matching the value, or `Unknown` if not predefined.
    fn from(value: u8) -> Self {
        match value {
            0x00 => IpProtocol::HopByHop,
            0x01 => IpProtocol::Icmp,
            0x02 => IpProtocol::Igmp,
            0x06 => IpProtocol::Tcp,
            0x11 => IpProtocol::Udp,
            0x2b => IpProtocol::Ipv6Route,
            0x2c => IpProtocol::Ipv6Frag,
            0x3a => IpProtocol::Icmpv6,
            0x3b => IpProtocol::Ipv6NoNxt,
            0x3c => IpProtocol::Ipv6Opts,
            other => IpProtocol::Unknown(other),
        }
    }
}

impl From<IpProtocol> for u8 {
    /// Converts an `IpProtocol` into its 8-bit representation.
    ///
    /// # Arguments
    /// * `value` - The `IpProtocol` to convert.
    ///
    /// # Returns
    /// The 8-bit value of the `IpProtocol`.
    fn from(value: IpProtocol) -> Self {
        match value {
            IpProtocol::HopByHop => 0x00,
            IpProtocol::Icmp => 0x01,
            IpProtocol::Igmp => 0x02,
            IpProtocol::Tcp => 0x06,
            IpProtocol::Udp => 0x11,
            IpProtocol::Ipv6Route => 0x2b,
            IpProtocol::Ipv6Frag => 0x2c,
            IpProtocol::Icmpv6 => 0x3a,
            IpProtocol::Ipv6NoNxt => 0x3b,
            IpProtocol::Ipv6Opts => 0x3c,
            IpProtocol::Unknown(other) => other,
        }
    }
}

#[cfg_attr(test, derive(PartialEq))]
#[derive(Debug)]
pub struct Ipv4Repr {
    pub protocol: IpProtocol,
    pub length: u16,
}

impl Ipv4Repr {
    pub const SIZE: usize = 20;

    /// Parses an IPv4 header from a byte slice.
    ///
    /// # Arguments
    /// * `packet` - A slice of at least 20 bytes containing the IPv4 header.
    ///
    /// # Returns
    /// `Some(Ipv4Repr)` if the header is well-formed, `None` otherwise.
    pub fn parse(packet: &[u8]) -> Option<Ipv4Repr> {
        let protocol = packet[9].into();
        let length = Self::parse_length(packet);
        let i = Ipv4Repr { protocol, length };
        if i.is_wellformed() {
            Some(i)
        } else {
            None
        }
    }

    /// Extracts the total length from an IPv4 header.
    ///
    /// # Arguments
    /// * `packet` - A slice containing at least 4 bytes of the IPv4 header.
    ///
    /// # Returns
    /// The 16-bit total length in big-endian order.
    fn parse_length(packet: &[u8]) -> u16 {
        let mut data = [0u8; 2];
        data.copy_from_slice(&packet[2..4]);
        u16::from_be_bytes(data)
    }

    /// Verifies if the IPv4 header has a known protocol.
    ///
    /// # Returns
    /// `true` if the `protocol` is a known variant, `false` if `Unknown`.
    pub fn is_wellformed(&self) -> bool {
        if let IpProtocol::Unknown(_) = self.protocol {
            return false;
        }
        true
    }
}

#[cfg_attr(test, derive(PartialEq))]
#[derive(Debug)]
pub struct TcpRepr {
    pub dst_port: u16,
}

impl TcpRepr {
    /// Parses a TCP header from a byte slice, extracting the destination port.
    ///
    /// # Arguments
    /// * `packet` - A slice of at least 4 bytes containing the TCP header.
    ///
    /// # Returns
    /// A `TcpRepr` with the parsed destination port.
    pub fn parse(packet: &[u8]) -> TcpRepr {
        let mut data = [0u8; 2];
        data.copy_from_slice(&packet[2..4]);
        let dst_port = u16::from_be_bytes(data);
        TcpRepr { dst_port }
    }
}

#[cfg_attr(test, derive(PartialEq))]
#[derive(Debug)]
pub struct UdpRepr {
    pub dst_port: u16,
}

impl UdpRepr {
    /// Parses a UDP header from a byte slice, extracting the destination port.
    ///
    /// # Arguments
    /// * `packet` - A slice of at least 4 bytes containing the UDP header.
    ///
    /// # Returns
    /// A `UdpRepr` with the parsed destination port.
    pub fn parse(packet: &[u8]) -> UdpRepr {
        let mut data = [0u8; 2];
        data.copy_from_slice(&packet[2..4]);
        let dst_port = u16::from_be_bytes(data);
        UdpRepr { dst_port }
    }
}

// Tests omitted for brevity, as they don't require additional documentation beyond what's already present.
// ... (previous struct and impl definitions remain unchanged) ...

//======================================================================
//  Tests
//======================================================================

// Overall Purpose of Tests
// The tests collectively ensure the reliability of the network protocol parsing library by:
//  - Validating Conversions: Checking that enums (ArpOp, HardwareType, IpProtocol, EtherType) 
//    correctly map to and from their numeric representations.
// Testing Parsing: Ensuring raw byte data is accurately transformed into structured 
//   representations (Address, EtherType, EthernetRepr, Arp).
// Verifying Serialization: Confirming that structured data can be faithfully emitted back into byte arrays.
//  Checking Validation: Ensuring is_wellformed and is_empty methods correctly assess the state of the data.
//
// These tests cover critical functionality for a network packet handling library, 
//  ensuring it behaves as expected for both valid and invalid inputs.

#[test]
/// Verifies that the `From<ArpOp> for u16` implementation correctly converts 
/// `ArpOp` variants into their 16-bit values.
///
/// Checks that `ArpOp::Request` converts to `1`, `ArpOp::Reply` to `2`, and 
/// `ArpOp::Unknown(5)` retains its value `5`.
fn from_arpop_to_u16_test() {
    let res: u16 = ArpOp::Request.into();
    assert_eq!(res, 1);
    let res: u16 = ArpOp::Reply.into();
    assert_eq!(res, 2);
    let res: u16 = ArpOp::Unknown(5).into();
    assert_eq!(res, 5);
}

#[test]
/// Ensures the `From<HardwareType> for u16` implementation accurately maps 
/// `HardwareType` variants to their 16-bit representations.
///
/// Tests that `HardwareType::Ethernet` becomes `1` and `HardwareType::Unknown(5)` 
/// remains `5`.
fn from_hardwaretype_to_u16_test() {
    let res: u16 = HardwareType::Ethernet.into();
    assert_eq!(res, 1);
    let res: u16 = HardwareType::Unknown(5).into();
    assert_eq!(res, 5);
}

#[test]
/// Confirms that the `From<IpProtocol> for u8` implementation correctly 
/// translates `IpProtocol` variants into their 8-bit protocol numbers.
///
/// Validates conversions for all defined variants (e.g., `HopByHop` to `0x00`, 
/// `Tcp` to `0x06`, `Udp` to `0x11`) and ensures `Unknown(0xC2)` retains `0xC2`.
fn from_ipprotocol_to_u8_test() {
    let res: u8 = IpProtocol::HopByHop.into();
    assert_eq!(res, 0);
    let res: u8 = IpProtocol::Icmp.into();
    assert_eq!(res, 0x01);
    let res: u8 = IpProtocol::Igmp.into();
    assert_eq!(res, 0x02);
    let res: u8 = IpProtocol::Tcp.into();
    assert_eq!(res, 0x06);
    let res: u8 = IpProtocol::Udp.into();
    assert_eq!(res, 0x11);
    let res: u8 = IpProtocol::Ipv6Route.into();
    assert_eq!(res, 0x2b);
    let res: u8 = IpProtocol::Ipv6Frag.into();
    assert_eq!(res, 0x2c);
    let res: u8 = IpProtocol::Icmpv6.into();
    assert_eq!(res, 0x3a);
    let res: u8 = IpProtocol::Ipv6NoNxt.into();
    assert_eq!(res, 0x3b);
    let res: u8 = IpProtocol::Ipv6Opts.into();
    assert_eq!(res, 0x3c);
    let res: u8 = IpProtocol::Unknown(0xC2).into();
    assert_eq!(res, 0xC2);
}

#[test]
/// Tests the `From<EtherType> for u16` implementation to ensure `EtherType` 
/// variants are converted into their correct 16-bit EtherType values.
///
/// Checks that `Ipv4` maps to `0x0800`, `Arp` to `0x0806`, `Ipv6` to `0x86DD`, 
/// and `Unknown(2)` stays `2`.
fn from_ethertype_to_u16_test() {
    let res: u16 = EtherType::Ipv4.into();
    assert_eq!(res, 0x0800);
    let res: u16 = EtherType::Arp.into();
    assert_eq!(res, 0x0806);
    let res: u16 = EtherType::Ipv6.into();
    assert_eq!(res, 0x86DD);
    let res: u16 = EtherType::Unknown(2).into();
    assert_eq!(res, 2);
}

#[test]
/// Validates that `Address::from_bytes` correctly constructs a MAC address 
/// from a byte slice.
///
/// Uses an 8-byte input array and confirms that the first 6 bytes 
/// (`[0x02, 0x03, 0x04, 0x05, 0x06, 0x07]`) are extracted into an `Address`.
fn mac_address_from_bytes_test() {
    let bytes = [0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08];
    let res = Address::from_bytes(&bytes[1..7]);
    assert_eq!(res, Address([0x02, 0x03, 0x04, 0x05, 0x06, 0x07]));
}

#[test]
/// Ensures `EtherType::from_bytes` accurately parses a 2-byte slice into the 
/// correct `EtherType` variant.
///
/// Tests parsing of `0x0800` as `Ipv4`, `0x0806` as `Arp`, `0x86DD` as `Ipv6`, 
/// and `0x1010` as `Unknown(0x1010)`.
fn ethertype_from_bytes_test() {
    let bytes = [0x08u8, 0x00];
    let res = EtherType::from_bytes(&bytes);
    assert_eq!(res, EtherType::Ipv4);

    let bytes = [0x08u8, 0x06];
    let res = EtherType::from_bytes(&bytes);
    assert_eq!(res, EtherType::Arp);

    let bytes = [0x86u8, 0xDD];
    let res = EtherType::from_bytes(&bytes);
    assert_eq!(res, EtherType::Ipv6);

    let bytes = [0x10u8, 0x10];
    let res = EtherType::from_bytes(&bytes);
    assert_eq!(res, EtherType::Unknown(0x1010));
}

#[cfg(test)]
mod ethernet_repr_tests {
    use super::*;

    #[test]
    /// Verifies that `EthernetRepr::parse` correctly interprets a 14-byte 
    /// Ethernet frame and rejects invalid frames.
    ///
    /// Tests a valid frame with `dst_addr` as all `0xFF`, `src_addr` as 
    /// `[0x2, 0x3, 0x4, 0x5, 0x6, 0x7]`, and `ethertype` as `Ipv4` (`0x0800`).
    /// Ensures a frame with an unknown EtherType (`0x2020`) returns `None`.
    fn parse() {
        let bytes = [
            0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00,
        ];
        let eth = EthernetRepr::parse(&bytes);
        assert_eq!(
            eth,
            Some(EthernetRepr {
                src_addr: Address([0x2, 0x3, 0x4, 0x5, 0x6, 0x7]),
                dst_addr: Address([0xff, 0xff, 0xff, 0xff, 0xff, 0xff]),
                ethertype: EtherType::Ipv4
            })
        );

        let bytes = [
            0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x20, 0x20,
        ];
        let eth = EthernetRepr::parse(&bytes);
        assert!(eth.is_none());
    }

    #[test]
    /// Confirms that `EthernetRepr::is_empty` correctly identifies when the 
    /// destination MAC address is all zeros.
    ///
    /// Tests an instance with `dst_addr` as `[0, 0, 0, 0, 0, 0]` returns `true`.
    /// Tests a non-zero `dst_addr` (`[1, 2, 3, 4, 3, 2]`) returns `false`.
    fn empty() {
        let eth = EthernetRepr {
            src_addr: Address([0, 0, 0, 0, 0, 0]),
            dst_addr: Address([0, 0, 0, 0, 0, 0]),
            ethertype: EtherType::Arp,
        };
        assert!(eth.is_empty());

        let eth = EthernetRepr {
            src_addr: Address([0, 0, 0, 0, 0, 0]),
            dst_addr: Address([1, 2, 3, 4, 3, 2]),
            ethertype: EtherType::Arp,
        };
        assert!(!eth.is_empty());
    }

    #[test]
    /// Ensures `EthernetRepr::is_wellformed` accurately determines if the 
    /// EtherType is a known variant.
    ///
    /// Tests that `Ipv4`, `Arp`, and `Ipv6` return `true`.
    /// Tests that `Unknown(5)` returns `false`.
    fn wellformed() {
        let mut eth = EthernetRepr {
            src_addr: Address([10, 9, 8, 7, 6, 5]),
            dst_addr: Address([1, 2, 3, 4, 3, 2]),
            ethertype: EtherType::Arp,
        };
        assert!(eth.is_wellformed());
        eth.ethertype = EtherType::Ipv4;
        assert!(eth.is_wellformed());
        eth.ethertype = EtherType::Ipv6;
        assert!(eth.is_wellformed());
        eth.ethertype = EtherType::Unknown(5);
        assert!(!eth.is_wellformed());
    }

    #[test]
    /// Validates that `EthernetRepr::emit` correctly serializes the structure 
    /// into a 14-byte array.
    ///
    /// Uses an instance with `dst_addr` `[1, 2, 3, 4, 3, 2]`, `src_addr` 
    /// `[10, 9, 8, 7, 6, 5]`, and `ethertype` `Arp` (`0x0806`).
    /// Checks the output matches the expected byte array.
    fn emit() {
        let mut res = [0u8; 14];
        let expected = [1u8, 2, 3, 4, 3, 2, 10, 9, 8, 7, 6, 5, 0x08, 0x06];
        let eth = EthernetRepr {
            src_addr: Address([10, 9, 8, 7, 6, 5]),
            dst_addr: Address([1, 2, 3, 4, 3, 2]),
            ethertype: EtherType::Arp,
        };
        eth.emit(&mut res);
        assert_eq!(res, expected);
    }
}

#[cfg(test)]
mod arp_tests {
    use super::*;

    #[test]
    /// Tests that `Arp::parse` correctly constructs an `Arp` struct from a 
    /// 28-byte ARP packet and rejects invalid ones.
    ///
    /// Parses a valid ARP request packet and matches it against an expected 
    /// `Arp` instance.
    /// Alters the hardware type to `5` (unknown) and ensures parsing returns 
    /// `None`.
    fn parse() {
        let mut pkt = [
            0x0, 0x1, 0x8, 0x0, 0x6, 0x4, 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0xc0, 0xa8, 0x0,
            0x1, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xc0, 0xa8, 0x0, 0xce,
        ];
        let expect = Arp {
            htype: HardwareType::Ethernet,
            ptype: EtherType::Ipv4,
            hsize: 0x6,
            psize: 0x4,
            op: ArpOp::Request,
            src_addr: Address([0x2, 0x3, 0x4, 0x5, 0x6, 0x7]),
            src_protocol_addr: Ipv4Address([0xc0, 0xa8, 0x00, 0x01]),
            dest_addr: Address([0x00, 0x00, 0x00, 0x00, 0x00, 0x00]),
            dest_protocol_addr: Ipv4Address([0xc0, 0xa8, 0x0, 0xce]),
        };
        let res = Arp::parse(&pkt);
        assert_eq!(res, Some(expect));

        pkt[0] = 5;
        let res = Arp::parse(&pkt);
        assert!(res.is_none());
    }

    #[test]
    /// Verifies that `Arp::is_wellformed` correctly identifies valid ARP packets 
    /// based on hardware type, protocol type, and operation.
    ///
    /// Tests a valid packet with `Ethernet`, `Ipv4`, and `Request` returns `true`.
    /// Checks `Ipv6` protocol type also returns `true`.
    /// Ensures `false` for unknown hardware type (`22`), unknown protocol type 
    /// (`51` or `Arp`), and unknown operation (`6`).
    fn wellformed() {
        let mut arp = Arp {
            htype: HardwareType::Ethernet,
            ptype: EtherType::Ipv4,
            hsize: 0x6,
            psize: 0x4,
            op: ArpOp::Request,
            src_addr: Address([0x2, 0x3, 0x4, 0x5, 0x6, 0x7]),
            src_protocol_addr: Ipv4Address([0xc0, 0xa8, 0x00, 0x01]),
            dest_addr: Address([0x00, 0x00, 0x00, 0x00, 0x00, 0x00]),
            dest_protocol_addr: Ipv4Address([0xc0, 0xa8, 0x0, 0xce]),
        };
        assert!(arp.is_wellformed());
        arp.ptype = EtherType::Ipv6;
        assert!(arp.is_wellformed());

        arp.htype = HardwareType::Unknown(22);
        assert!(!arp.is_wellformed());

        arp.htype = HardwareType::Ethernet;
        arp.ptype = EtherType::Unknown(51);
        assert!(!arp.is_wellformed());
        arp.ptype = EtherType::Arp;
        assert!(!arp.is_wellformed());

        arp.ptype = EtherType::Ipv4;
        arp.op = ArpOp::Unknown(6);
        assert!(!arp.is_wellformed());
    }

    #[test]
    /// Confirms that `Arp::emit` accurately serializes an `Arp` struct into a 
    /// 28-byte array.
    ///
    /// Uses a valid ARP request instance and checks the emitted bytes match 
    /// the expected array.
    fn emit() {
        let expect = [
            0x0u8, 0x1, 0x8, 0x0, 0x6, 0x4, 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0xc0, 0xa8,
            0x0, 0x1, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xc0, 0xa8, 0x0, 0xce,
        ];
        let arp = Arp {
            htype: HardwareType::Ethernet,
            ptype: EtherType::Ipv4,
            hsize: 0x6,
            psize: 0x4,
            op: ArpOp::Request,
            src_addr: Address([0x2, 0x3, 0x4, 0x5, 0x6, 0x7]),
            src_protocol_addr: Ipv4Address([0xc0, 0xa8, 0x00, 0x01]),
            dest_addr: Address([0x00, 0x00, 0x00, 0x00, 0x00, 0x00]),
            dest_protocol_addr: Ipv4Address([0xc0, 0xa8, 0x0, 0xce]),
        };
        let mut res = [0u8; 28];

        arp.emit(&mut res);
        assert_eq!(res, expect);
    }
}