#[derive(Debug)]
pub struct Ipv4Address(pub [u8; 4]);

impl Ipv4Address {
    pub fn from_bytes(data: &[u8]) -> Ipv4Address {
        let mut bytes = [0; 4];
        bytes.copy_from_slice(data);
        Ipv4Address(bytes)
    }
}

#[derive(Debug)]
pub struct Address(pub [u8; 6]);

impl Address {
    pub fn from_bytes(data: &[u8]) -> Address {
        let mut bytes = [0; 6];
        bytes.copy_from_slice(data);
        Address(bytes)
    }
}

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

#[derive(Debug)]
pub struct EthernetRepr {
    pub src_addr: Address,
    pub dst_addr: Address,
    pub ethertype: EtherType,
}

impl EthernetRepr {
    pub const SIZE: usize = 14;
    /// Parse an Ethernet II frame and return a high-level representation.
    pub fn parse(frame: &[u8]) -> EthernetRepr {
        let dst_addr = Address::from_bytes(&frame[0..6]);
        let src_addr = Address::from_bytes(&frame[6..12]);
        let ethertype = EtherType::from_bytes(&frame[12..14]);
        EthernetRepr {
            src_addr,
            dst_addr,
            ethertype,
        }
    }

    pub fn is_empty(&self) -> bool {
        self.src_addr.0.iter().filter(|x| **x != 0).count() == 0
    }

    pub fn emit(&self, frame: &mut [u8]) {
        frame[0..6].copy_from_slice(&self.dst_addr.0);
        frame[6..12].copy_from_slice(&self.src_addr.0);
        let ethertype: u16 = self.ethertype.clone().into();
        frame[12..14].copy_from_slice(&ethertype.to_be_bytes());
    }
}

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
    pub fn parse(packet: &[u8]) -> Arp {
        let htype = HardwareType::from_bytes(&packet[0..2]);
        let ptype = EtherType::from_bytes(&packet[2..4]);
        let hsize = packet[4];
        let psize = packet[5];
        let op = ArpOp::from_bytes(&packet[6..8]);
        let src_addr = Address::from_bytes(&packet[8..14]);
        let src_protocol_addr = Ipv4Address::from_bytes(&packet[14..18]);
        let dest_addr = Address::from_bytes(&packet[18..24]);
        let dest_protocol_addr = Ipv4Address::from_bytes(&packet[24..28]);
        Arp {
            htype,
            ptype,
            hsize,
            psize,
            op,
            src_addr,
            src_protocol_addr,
            dest_addr,
            dest_protocol_addr,
        }
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

#[derive(Debug)]
pub struct Ipv4Repr {
    pub protocol: IpProtocol,
}

impl Ipv4Repr {
    pub const SIZE: usize = 20;

    pub fn parse(packet: &[u8]) -> Ipv4Repr {
        let protocol = packet[9].into();
        Ipv4Repr { protocol }
    }
}

#[derive(Debug)]
pub struct TcpRepr {
    pub dst_port: u16,
}

impl TcpRepr {
    // TODO: Need this?
    pub const SIZE: usize = 20;

    pub fn parse(packet: &[u8]) -> TcpRepr {
        let mut data = [0u8; 2];
        data.copy_from_slice(&packet[2..4]);
        let dst_port = u16::from_be_bytes(data);
        TcpRepr { dst_port }
    }
}
