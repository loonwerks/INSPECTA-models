use vstd::prelude::*;
use vstd::slice::slice_subrange;

verus! {

#[cfg_attr(test, derive(PartialEq))]
#[derive(Debug)]
pub struct Ipv4Address(pub [u8; 4]);

/// Define the max possible MTU. Use standard Jumbo size as maximum possible.
pub const MAX_MTU: u16 = 9000;

impl Ipv4Address {
    pub fn from_bytes(data: &[u8]) -> (r: Ipv4Address)
        requires
            data@.len() == 4,
        ensures
            r.0@ =~= data@
    {
        let mut bytes = [0u8; 4];

        let mut i = 0;
        while i < 4
            invariant
                0 <= i <= bytes@.len() == data@.len(),
                forall |j| 0 <= j < i ==> bytes[j] == data[j],
            decreases
                4 - i
        {
            bytes.set(i, data[i]);
            i += 1;
        }
        Ipv4Address(bytes)
    }
}

#[cfg_attr(test, derive(PartialEq))]
#[derive(Debug)]
pub struct Address(pub [u8; 6]);

impl Address {
    pub fn from_bytes(data: &[u8]) -> (r: Address)
        requires
            data@.len() == 6,
        ensures
            r.0@ =~= data@
    {
        let mut bytes = [0u8; 6];

        let mut i = 0;
        while i < 6
            invariant
                0 <= i <= bytes@.len() == data@.len(),
                forall |j| 0 <= j < i ==> bytes[j] == data[j],
            decreases
                6 - i
        {
            bytes.set(i, data[i]);
            i += 1;
        }
        Address(bytes)
    }

    pub fn is_empty(&self) -> (r: bool)
        requires
            self.0@.len() == 6
        ensures
            r == (self.0@ =~= seq![0,0,0,0,0,0])
    {
        let mut i = 0;
        while i < self.0.as_slice().len()
            invariant
                0 <= i <= self.0@.len(),
                forall |j| 0 <= j < i ==> self.0@[j] == 0,
            decreases
                self.0@.len() - i
        {
            if self.0[i] != 0 {
                return false
            }
            i += 1;
        }
        true
    }
}

fn u16_from_be_bytes(bytes: &[u8]) -> (r: u16)
    requires
        bytes@.len() == 2,
    ensures
        r == spec_u16_from_be_bytes(bytes@),
{
    ((bytes[0] as u16) * 256u16) + (bytes[1] as u16)
}

#[cfg_attr(test, derive(PartialEq))]
#[derive(Debug, Clone, Copy)]
#[repr(u16)]
pub enum EtherType {
    Ipv4 = 0x0800,
    Arp = 0x0806,
    Ipv6 = 0x86DD,
}

impl EtherType {
    pub fn from_bytes(bytes: &[u8]) -> (r: Option<EtherType>)
        requires
            bytes@.len() == 2,
        ensures
            frame_arp_subrange(bytes@) == (r.is_some() && r.unwrap() is Arp),
            frame_ipv4_subrange(bytes@) == (r.is_some() && r.unwrap() is Ipv4),
            frame_ipv6_subrange(bytes@) == (r.is_some() && r.unwrap() is Ipv6),
        {
        let raw = u16_from_be_bytes(bytes);
        EtherType::try_from(raw).ok()
    }
}

impl TryFrom<u16> for EtherType {
    type Error = ();

    fn try_from(value: u16) -> (r: Result<Self, Self::Error>)
        ensures
            match value {
                0x0800 => r.is_ok() && r.unwrap() is Ipv4,
                0x0806 => r.is_ok() && r.unwrap() is Arp,
                0x86DD => r.is_ok() && r.unwrap() is Ipv6,
                _ => r.is_err(),
            }
    {
        match value {
            0x0800 => Ok(EtherType::Ipv4),
            0x0806 => Ok(EtherType::Arp),
            0x86DD => Ok(EtherType::Ipv6),
            _ => Err(()),
        }
    }
}

impl From<EtherType> for u16 {
    fn from(value: EtherType) -> Self {
        match value {
            EtherType::Ipv4 => 0x0800,
            EtherType::Arp => 0x0806,
            EtherType::Ipv6 => 0x86DD,
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
    /// Parse an Ethernet II frame and return a high-level representation.
    pub fn parse(frame: &[u8]) -> (r: Option<EthernetRepr>)
        requires
            frame@.len() >= Self::SIZE,
        ensures
            valid_arp_frame(frame) == (r.is_some() && r.unwrap().ethertype is Arp),
            valid_ipv4_frame(frame) == (r.is_some() && r.unwrap().ethertype is Ipv4),
            valid_ipv6_frame(frame) == (r.is_some() && r.unwrap().ethertype is Ipv6),
    {
        let dst_addr = Address::from_bytes(slice_subrange(frame, 0, 6));
        if dst_addr.is_empty() {
            return None;
        }
        let src_addr = Address::from_bytes(slice_subrange(frame, 6, 12));
        let ethertype = EtherType::from_bytes(slice_subrange(frame,12,14))?;

        Some(EthernetRepr {
            src_addr,
            dst_addr,
            ethertype,
        })
    }

    // pub fn emit(&self, frame: &mut [u8]) {
    //     frame[0..6].copy_from_slice(&self.dst_addr.0);
    //     frame[6..12].copy_from_slice(&self.src_addr.0);
    //     let ethertype: u16 = self.ethertype.clone().into();
    //     frame[12..14].copy_from_slice(&ethertype.to_be_bytes());
    // }
}

#[cfg_attr(test, derive(PartialEq))]
#[derive(Debug, Clone, Copy)]
#[repr(u16)]
pub enum ArpOp {
    Request = 1,
    Reply = 2,
}

impl ArpOp {
    pub fn from_bytes(bytes: &[u8]) -> (r: Option<ArpOp>)
        requires
            bytes@.len() == 2,
        ensures
            valid_arp_op_request_subrange(bytes@) == (r.is_some() && r.unwrap() is Request),
            valid_arp_op_reply_subrange(bytes@) == (r.is_some() && r.unwrap() is Reply),
    {
        let raw = u16_from_be_bytes(bytes);
        ArpOp::try_from(raw).ok()
    }
}

impl TryFrom<u16> for ArpOp {
    type Error = ();

    fn try_from(value: u16) -> (r: Result<Self, Self::Error>)
        ensures
            match value {
                1 => r.is_ok() && r.unwrap() is Request,
                2 => r.is_ok() && r.unwrap() is Reply,
                _ => r.is_err(),
            }
    {
        match value {
            1 => Ok(ArpOp::Request),
            2 => Ok(ArpOp::Reply),
            _ => Err(()),
        }
    }
}

impl From<ArpOp> for u16 {
    fn from(value: ArpOp) -> Self {
        match value {
            ArpOp::Request => 1,
            ArpOp::Reply => 2,
        }
    }
}

#[cfg_attr(test, derive(PartialEq))]
#[derive(Debug, Clone, Copy)]
#[repr(u16)]
pub enum HardwareType {
    Ethernet = 1,
}

impl HardwareType {
    pub fn from_bytes(bytes: &[u8]) -> (r: Option<HardwareType>)
        requires
            bytes@.len() == 2,
        ensures
            valid_arp_htype_eth_subrange(bytes@) == (r.is_some() && r.unwrap() is Ethernet)
    {
        let raw = u16_from_be_bytes(bytes);
        HardwareType::try_from(raw).ok()
    }
}

impl TryFrom<u16> for HardwareType {
    type Error = ();

    fn try_from(value: u16) -> (r: Result<Self, Self::Error>)
        ensures
            match value {
                1 => r.is_ok() && r.unwrap() is Ethernet,
                _ => r.is_err(),
            }
    {
        match value {
            1 => Ok(HardwareType::Ethernet),
            _ => Err(()),
        }
    }
}

impl From<HardwareType> for u16 {
    fn from(value: HardwareType) -> Self {
        match value {
            HardwareType::Ethernet => 1,
        }
    }
}

// TODO: Protocol addresses should be variable, but I only care about supporting ipv4 for now
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
    /// Parse an ARP packet and return a high-level representation.
    pub fn parse(packet: &[u8]) -> (r: Option<Arp>)
        requires
            packet@.len() >= Self::SIZE,
        ensures
            valid_arp_op_subrange(packet@.subrange(6, 8)) == valid_arp_op(packet@),
            wellformed_arp_packet(packet@) == r.is_some(),
    {
        let htype = HardwareType::from_bytes(slice_subrange(packet, 0, 2))?;
        let ptype = EtherType::from_bytes(slice_subrange(packet, 2, 4))?;
        if !Self::allowed_ptype(&ptype) {
            return None;
        }
        let hsize = packet[4];
        let psize = packet[5];
        let op = ArpOp::from_bytes(slice_subrange(packet, 6, 8))?;
        let src_addr = Address::from_bytes(slice_subrange(packet, 8, 14));
        let src_protocol_addr = Ipv4Address::from_bytes(slice_subrange(packet, 14, 18));
        let dest_addr = Address::from_bytes(slice_subrange(packet, 18, 24));
        let dest_protocol_addr = Ipv4Address::from_bytes(slice_subrange(packet, 24, 28));
        Some(Arp {
            htype,
            ptype,
            hsize,
            psize,
            op,
            src_addr,
            src_protocol_addr,
            dest_addr,
            dest_protocol_addr,
        })
    }

    fn allowed_ptype(ptype: &EtherType) -> (r: bool)
        ensures
            r == !(ptype is Arp)
    {
        if let EtherType::Arp = ptype {
            false
        } else {
            true
        }
    }

    // pub fn emit(&self, frame: &mut [u8]) {
    //     let htype: u16 = self.htype.clone().into();
    //     let ptype: u16 = self.ptype.clone().into();
    //     let op: u16 = self.op.clone().into();
    //     frame[0..2].copy_from_slice(&htype.to_be_bytes());
    //     frame[2..4].copy_from_slice(&ptype.to_be_bytes());
    //     frame[4] = self.hsize;
    //     frame[5] = self.psize;
    //     frame[6..8].copy_from_slice(&op.to_be_bytes());
    //     frame[8..14].copy_from_slice(&self.src_addr.0);
    //     frame[14..18].copy_from_slice(&self.src_protocol_addr.0);
    //     frame[18..24].copy_from_slice(&self.dest_addr.0);
    //     frame[24..28].copy_from_slice(&self.dest_protocol_addr.0);
    // }
}

#[cfg_attr(test, derive(PartialEq))]
#[derive(Debug, Clone, Copy)]
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
}

impl TryFrom<u8> for IpProtocol {
    type Error = ();

    fn try_from(value: u8) -> (r: Result<Self, Self::Error>)
        ensures
            match value {
                0x00 => r.is_ok() && r.unwrap() is HopByHop,
                0x01 => r.is_ok() && r.unwrap() is Icmp,
                0x02 => r.is_ok() && r.unwrap() is Igmp,
                0x06 => r.is_ok() && r.unwrap() is Tcp,
                0x11 => r.is_ok() && r.unwrap() is Udp,
                0x2b => r.is_ok() && r.unwrap() is Ipv6Route,
                0x2c => r.is_ok() && r.unwrap() is Ipv6Frag,
                0x3a => r.is_ok() && r.unwrap() is Icmpv6,
                0x3b => r.is_ok() && r.unwrap() is Ipv6NoNxt,
                0x3c => r.is_ok() && r.unwrap() is Ipv6Opts,
                _ => r.is_err(),
            }
    {
        match value {
            0x00 => Ok(IpProtocol::HopByHop),
            0x01 => Ok(IpProtocol::Icmp),
            0x02 => Ok(IpProtocol::Igmp),
            0x06 => Ok(IpProtocol::Tcp),
            0x11 => Ok(IpProtocol::Udp),
            0x2b => Ok(IpProtocol::Ipv6Route),
            0x2c => Ok(IpProtocol::Ipv6Frag),
            0x3a => Ok(IpProtocol::Icmpv6),
            0x3b => Ok(IpProtocol::Ipv6NoNxt),
            0x3c => Ok(IpProtocol::Ipv6Opts),
            _ => Err(()),
        }
    }
}

impl From<IpProtocol> for u8 {
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

    pub fn parse(packet: &[u8]) -> (r: Option<Ipv4Repr>)
        requires
            packet@.len() >= Self::SIZE,
        ensures
            wellformed_ipv4_packet(packet@) == (r.is_some() && r.unwrap().length <= MAX_MTU && r.unwrap().length == ipv4_length_subrange(packet@)),
            valid_tcp_packet(packet@) == (r.is_some() && r.unwrap().protocol is Tcp),
            valid_udp_packet(packet@) == (r.is_some() && r.unwrap().protocol is Udp),
            r.is_some() ==> wellformed_ipv4_packet(packet@),
    {
        let protocol = IpProtocol::try_from(packet[9]).ok()?;
        let length =  u16_from_be_bytes(slice_subrange(packet, 2, 4));
        if length > MAX_MTU {
            return None;
        }
        Some(Ipv4Repr { protocol, length })
    }

}

#[cfg_attr(test, derive(PartialEq))]
#[derive(Debug)]
pub struct TcpRepr {
    pub dst_port: u16,
}

impl TcpRepr {
    pub const SIZE: usize = 20;

    pub fn parse(packet: &[u8]) -> (r: TcpRepr)
        requires
            packet@.len() >= Self::SIZE,
        ensures
            r.dst_port == spec_u16_from_be_bytes(packet@.subrange(2, 4)),
    {
        let dst_port = u16_from_be_bytes(slice_subrange(packet, 2, 4));
        TcpRepr { dst_port }
    }
}

#[cfg_attr(test, derive(PartialEq))]
#[derive(Debug)]
pub struct UdpRepr {
    pub dst_port: u16,
}

impl UdpRepr {
    pub const SIZE: usize = 20;

    pub fn parse(packet: &[u8]) -> (r: UdpRepr)
        requires
            packet@.len() >= Self::SIZE,
        ensures
            r.dst_port == spec_u16_from_be_bytes(packet@.subrange(2, 4)),
    {
        let dst_port =  u16_from_be_bytes(slice_subrange(packet, 2, 4));
        UdpRepr { dst_port }
    }
}


pub open spec fn spec_u16_from_be_bytes(s: Seq<u8>) -> u16
    recommends
        s.len() == 2,
{
    // TODO: Why is the full cast needed?
    (((s[0] as u16) * 256u16) + (s[1] as u16)) as u16
}

// -----------------------
// -- EthernetRepr
// -----------------------
pub open spec fn frame_ipv4_subrange(frame: Seq<u8>) -> bool
{
    frame =~= seq![8,0]
}

pub open spec fn frame_ipv6_subrange(frame: Seq<u8>) -> bool
{
    frame =~= seq![134,221]
}

pub open spec fn frame_arp_subrange(frame: Seq<u8>) -> bool
{
    frame =~= seq![8,6]
}

pub open spec fn frames_subrange_valid(frame: Seq<u8>) -> bool
{
    frame_arp_subrange(frame) || frame_ipv4_subrange(frame) || frame_ipv6_subrange(frame)
}

pub open spec fn frame_ipv4(frame: &[u8]) -> bool
{
    frame@.subrange(12,14) =~= seq![8,0]
}

pub open spec fn frame_ipv6(frame: &[u8]) -> bool
{
    frame@.subrange(12,14) =~= seq![134,221]
}

pub open spec fn frame_arp(frame: &[u8]) -> bool
{
    frame@.subrange(12,14) =~= seq![8,6]
}

pub open spec fn frame_is_wellformed_eth2(frame: &[u8]) -> bool
{
    frame_ipv4(frame) || frame_ipv6(frame) || frame_arp(frame)
}

pub open spec fn frame_dst_addr_valid(bytes: Seq<u8>) -> bool
{
    !(bytes.subrange(0,6) =~= seq![0,0,0,0,0,0])
}

pub open spec fn valid_arp_frame(frame: &[u8]) -> bool
{
    frames_subrange_valid(frame@.subrange(12, 14)) &&
        frame_dst_addr_valid(frame@) &&
        frame_arp(frame)
}

pub open spec fn valid_ipv4_frame(frame: &[u8]) -> bool
{
    frames_subrange_valid(frame@.subrange(12, 14)) &&
        frame_dst_addr_valid(frame@) &&
        frame_ipv4(frame)
}

pub open spec fn valid_ipv6_frame(frame: &[u8]) -> bool
{
    frames_subrange_valid(frame@.subrange(12, 14)) &&
        frame_dst_addr_valid(frame@) &&
        frame_ipv6(frame)
}

// -----------------------
// -- Arp
// -----------------------
pub open spec fn arp_valid_ptype_subrange(bytes: Seq<u8>) -> bool
{
    frame_ipv4_subrange(bytes) || frame_ipv6_subrange(bytes)
}

pub open spec fn valid_arp_op_request_subrange(bytes: Seq<u8>) -> bool
{
    bytes =~= seq![0,1]
}

pub open spec fn valid_arp_op_reply_subrange(bytes: Seq<u8>) -> bool
{
    bytes =~= seq![0,2]
}

pub open spec fn valid_arp_op_subrange(bytes: Seq<u8>) -> bool
{
    valid_arp_op_request_subrange(bytes) || valid_arp_op_reply_subrange(bytes)
}

pub open spec fn valid_arp_op_request(bytes: Seq<u8>) -> bool
{
    bytes.subrange(6,8) =~= seq![0,1]
}

pub open spec fn valid_arp_op_reply(bytes: Seq<u8>) -> bool
{
    bytes.subrange(6,8) =~= seq![0,2]
}

pub open spec fn valid_arp_op(bytes: Seq<u8>) -> bool
{
    valid_arp_op_request(bytes) || valid_arp_op_reply(bytes)
}

pub open spec fn valid_arp_htype_eth_subrange(bytes: Seq<u8>) -> bool
{
    bytes =~= seq![0,1]
}

pub open spec fn valid_arp_htype_subrange(bytes: Seq<u8>) -> bool
{
    valid_arp_htype_eth_subrange(bytes)
}

pub open spec fn wellformed_arp_packet(packet: Seq<u8>) -> bool {
    valid_arp_op_subrange(packet.subrange(6, 8)) &&
        valid_arp_htype_subrange(packet.subrange(0, 2)) &&
        arp_valid_ptype_subrange(packet.subrange(2, 4))
}


pub open spec fn wellformed_arp_frame(frame: Seq<u8>) -> bool {
    valid_arp_op_subrange(frame.subrange(20, 22)) &&
        valid_arp_htype_subrange(frame.subrange(14, 16)) &&
        arp_valid_ptype_subrange(frame.subrange(16, 18))
}

// -----------------------
// -- Ipv4
// -----------------------

pub open spec fn ipv4_is_tcp(frame: Seq<u8>) -> bool
{
  frame[23] == 0x06
}

pub open spec fn ipv4_is_udp(frame: Seq<u8>) -> bool
{
  frame[23] == 0x11
}

pub open spec fn valid_ipv4_length(frame: Seq<u8>) -> bool {
    spec_u16_from_be_bytes(frame.subrange(16,18)) <= MAX_MTU
}

pub open spec fn valid_ipv4_protocol(frame: Seq<u8>) -> bool {
    seq![0x00, 0x01, 0x02, 0x06, 0x11, 0x2b, 0x2c, 0x3a, 0x3b, 0x3c].contains(frame[23])
}

pub open spec fn wellformed_ipv4_frame(frame: Seq<u8>) -> bool {
    valid_ipv4_protocol(frame) && valid_ipv4_length(frame)
}

pub open spec fn ipv4_is_tcp_subrange(frame: Seq<u8>) -> bool
{
  frame[9] == 0x06
}

pub open spec fn ipv4_is_udp_subrange(frame: Seq<u8>) -> bool
{
  frame[9] == 0x11
}

pub open spec fn ipv4_length_subrange(bytes: Seq<u8>) -> u16 {
    spec_u16_from_be_bytes(bytes.subrange(2,4))
}

pub open spec fn valid_ipv4_length_subrange(bytes: Seq<u8>) -> bool {
    ipv4_length_subrange(bytes) <= MAX_MTU
}

pub open spec fn valid_ipv4_protocol_subrange(bytes: Seq<u8>) -> bool {
    seq![0x00, 0x01, 0x02, 0x06, 0x11, 0x2b, 0x2c, 0x3a, 0x3b, 0x3c].contains(bytes[9])
}

pub open spec fn wellformed_ipv4_packet(bytes: Seq<u8>) -> bool {
    valid_ipv4_protocol_subrange(bytes) && valid_ipv4_length_subrange(bytes)
}

pub open spec fn valid_tcp_packet(packet: Seq<u8>) -> bool
{
    wellformed_ipv4_packet(packet) && ipv4_is_tcp_subrange(packet)
}

pub open spec fn valid_udp_packet(packet: Seq<u8>) -> bool
{
    wellformed_ipv4_packet(packet) && ipv4_is_udp_subrange(packet)
}

}

#[test]
fn from_arpop_to_u16_test() {
    let res: u16 = ArpOp::Request.into();
    assert_eq!(res, 1);
    let res: u16 = ArpOp::Reply.into();
    assert_eq!(res, 2);
}

#[test]
fn from_hardwaretype_to_u16_test() {
    let res: u16 = HardwareType::Ethernet.into();
    assert_eq!(res, 1);
}

#[test]
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
}

#[test]
fn from_ethertype_to_u16_test() {
    let res: u16 = EtherType::Ipv4.into();
    assert_eq!(res, 0x0800);
    let res: u16 = EtherType::Arp.into();
    assert_eq!(res, 0x0806);
    let res: u16 = EtherType::Ipv6.into();
    assert_eq!(res, 0x86DD);
}

#[test]
fn mac_address_from_bytes_test() {
    let bytes = [0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08];
    let res = Address::from_bytes(&bytes[1..7]);
    assert_eq!(res, Address([0x02, 0x03, 0x04, 0x05, 0x06, 0x07]));
}

#[test]
fn ethertype_from_bytes_test() {
    let bytes = [0x08u8, 0x00];
    let res = EtherType::from_bytes(&bytes).unwrap();
    assert_eq!(res, EtherType::Ipv4);

    let bytes = [0x08u8, 0x06];
    let res = EtherType::from_bytes(&bytes).unwrap();
    assert_eq!(res, EtherType::Arp);

    let bytes = [0x86u8, 0xDD];
    let res = EtherType::from_bytes(&bytes).unwrap();
    assert_eq!(res, EtherType::Ipv6);

    let bytes = [0x10u8, 0x10];
    let res = EtherType::from_bytes(&bytes);
    assert!(res.is_none());
}

#[cfg(test)]
mod ethernet_repr_tests {
    use super::*;

    #[test]
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

    // #[test]
    // fn wellformed() {
    //     let mut eth = EthernetRepr {
    //         src_addr: Address([10, 9, 8, 7, 6, 5]),
    //         dst_addr: Address([1, 2, 3, 4, 3, 2]),
    //         ethertype: EtherType::Arp,
    //     };
    //     assert!(eth.is_wellformed());
    //     eth.ethertype = EtherType::Ipv4;
    //     assert!(eth.is_wellformed());
    //     eth.ethertype = EtherType::Ipv6;
    //     assert!(eth.is_wellformed());
    //     eth.ethertype = EtherType::Unknown(5);
    //     assert!(!eth.is_wellformed());
    // }

    // #[test]
    // fn emit() {
    //     let mut res = [0u8; 14];
    //     let expected = [1u8, 2, 3, 4, 3, 2, 10, 9, 8, 7, 6, 5, 0x08, 0x06];
    //     let eth = EthernetRepr {
    //         src_addr: Address([10, 9, 8, 7, 6, 5]),
    //         dst_addr: Address([1, 2, 3, 4, 3, 2]),
    //         ethertype: EtherType::Arp,
    //     };
    //     eth.emit(&mut res);
    //     assert_eq!(res, expected);
    // }
}

#[cfg(test)]
mod arp_tests {
    use super::*;

    #[test]
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
    fn valid_ptype() {
        assert!(Arp::allowed_ptype(&EtherType::Ipv4));
        assert!(Arp::allowed_ptype(&EtherType::Ipv6));
        assert!(!Arp::allowed_ptype(&EtherType::Arp));
    }

    // #[test]
    // fn emit() {
    //     let expect = [
    //         0x0u8, 0x1, 0x8, 0x0, 0x6, 0x4, 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0xc0, 0xa8,
    //         0x0, 0x1, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xc0, 0xa8, 0x0, 0xce,
    //     ];
    //     let arp = Arp {
    //         htype: HardwareType::Ethernet,
    //         ptype: EtherType::Ipv4,
    //         hsize: 0x6,
    //         psize: 0x4,
    //         op: ArpOp::Request,
    //         src_addr: Address([0x2, 0x3, 0x4, 0x5, 0x6, 0x7]),
    //         src_protocol_addr: Ipv4Address([0xc0, 0xa8, 0x00, 0x01]),
    //         dest_addr: Address([0x00, 0x00, 0x00, 0x00, 0x00, 0x00]),
    //         dest_protocol_addr: Ipv4Address([0xc0, 0xa8, 0x0, 0xce]),
    //     };
    //     let mut res = [0u8; 28];

    //     arp.emit(&mut res);
    //     assert_eq!(res, expect);
    // }
}
