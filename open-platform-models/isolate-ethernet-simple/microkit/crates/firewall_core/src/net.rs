#[cfg_attr(test, derive(PartialEq))]
#[derive(Debug)]
pub struct Ipv4Address(pub [u8; 4]);

impl Ipv4Address {
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
    pub fn from_bytes(data: &[u8]) -> Address {
        let mut bytes = [0; 6];
        bytes.copy_from_slice(data);
        Address(bytes)
    }

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
    pub fn from_bytes(bytes: &[u8]) -> Self {
        let mut data = [0u8; 2];
        data.copy_from_slice(bytes);
        let raw = u16::from_be_bytes(data);
        EtherType::from(raw)
    }
}

impl From<u16> for EtherType {
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
    /// Parse an Ethernet II frame and return a high-level representation.
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

    pub fn is_empty(&self) -> bool {
        self.dst_addr.is_empty()
    }

    pub fn is_wellformed(&self) -> bool {
        if let EtherType::Unknown(_) = self.ethertype {
            return false;
        }
        true
    }

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
    pub fn from_bytes(bytes: &[u8]) -> Self {
        let mut data = [0u8; 2];
        data.copy_from_slice(bytes);
        let raw = u16::from_be_bytes(data);
        ArpOp::from(raw)
    }
}

impl From<u16> for ArpOp {
    fn from(value: u16) -> Self {
        match value {
            1 => ArpOp::Request,
            2 => ArpOp::Reply,
            other => ArpOp::Unknown(other),
        }
    }
}

impl From<ArpOp> for u16 {
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
    pub fn from_bytes(bytes: &[u8]) -> Self {
        let mut data = [0u8; 2];
        data.copy_from_slice(bytes);
        let raw = u16::from_be_bytes(data);
        HardwareType::from(raw)
    }
}

impl From<u16> for HardwareType {
    fn from(value: u16) -> Self {
        match value {
            1 => HardwareType::Ethernet,
            other => HardwareType::Unknown(other),
        }
    }
}

impl From<HardwareType> for u16 {
    fn from(value: HardwareType) -> Self {
        match value {
            HardwareType::Ethernet => 1,
            HardwareType::Unknown(other) => other,
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
    /// Parse an Ethernet II frame and return a high-level representation.
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

    fn parse_length(packet: &[u8]) -> u16 {
        let mut data = [0u8; 2];
        data.copy_from_slice(&packet[2..4]);
        u16::from_be_bytes(data)
    }

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
    // TODO: Need this?
    // pub const SIZE: usize = 20;

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
    // TODO: Need this?
    // pub const SIZE: usize = 20;

    pub fn parse(packet: &[u8]) -> UdpRepr {
        let mut data = [0u8; 2];
        data.copy_from_slice(&packet[2..4]);
        let dst_port = u16::from_be_bytes(data);
        UdpRepr { dst_port }
    }
}

#[test]
fn from_arpop_to_u16_test() {
    let res: u16 = ArpOp::Request.into();
    assert_eq!(res, 1);
    let res: u16 = ArpOp::Reply.into();
    assert_eq!(res, 2);
    let res: u16 = ArpOp::Unknown(5).into();
    assert_eq!(res, 5);
}

#[test]
fn from_hardwaretype_to_u16_test() {
    let res: u16 = HardwareType::Ethernet.into();
    assert_eq!(res, 1);
    let res: u16 = HardwareType::Unknown(5).into();
    assert_eq!(res, 5);
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
    let res: u8 = IpProtocol::Unknown(0xC2).into();
    assert_eq!(res, 0xC2);
}

#[test]
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
fn mac_address_from_bytes_test() {
    let bytes = [0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08];
    let res = Address::from_bytes(&bytes[1..7]);
    assert_eq!(res, Address([0x02, 0x03, 0x04, 0x05, 0x06, 0x07]));
}

#[test]
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
