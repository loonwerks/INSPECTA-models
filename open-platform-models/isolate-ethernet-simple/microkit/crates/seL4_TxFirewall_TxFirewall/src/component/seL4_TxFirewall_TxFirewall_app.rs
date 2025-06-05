#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

// This file will not be overwritten if codegen is rerun

use crate::bridge::seL4_TxFirewall_TxFirewall_api::*;
use crate::data::*;
// #[cfg(feature = "sel4")]
// #[allow(unused_imports)]
use log::{debug, error, info, trace, warn};

use firewall_core::{Arp, EthFrame, EthernetRepr, PacketType};

mod config;

const NUM_MSGS: usize = 4;

fn eth_get<API: seL4_TxFirewall_TxFirewall_Get_Api>(
    idx: usize,
    api: &mut seL4_TxFirewall_TxFirewall_Application_Api<API>,
) -> Option<SW::RawEthernetMessage_Impl> {
    match idx {
        0 => api.get_EthernetFramesTxIn0(),
        1 => api.get_EthernetFramesTxIn1(),
        2 => api.get_EthernetFramesTxIn2(),
        3 => api.get_EthernetFramesTxIn3(),
        _ => None,
    }
}

fn eth_put<API: seL4_TxFirewall_TxFirewall_Put_Api>(
    idx: usize,
    tx_buf: SW::SizedEthernetMessage_Impl,
    api: &mut seL4_TxFirewall_TxFirewall_Application_Api<API>,
) {
    match idx {
        0 => api.put_EthernetFramesTxOut0(tx_buf),
        1 => api.put_EthernetFramesTxOut1(tx_buf),
        2 => api.put_EthernetFramesTxOut2(tx_buf),
        3 => api.put_EthernetFramesTxOut3(tx_buf),
        _ => (),
    }
}

fn get_frame_packet(frame: &[u8]) -> Option<PacketType> {
    let packet = EthFrame::parse(frame).map(|x| x.eth_type);
    if packet.is_none() {
        info!("Malformed packet. Throw it away.")
    }
    packet
}

fn can_send_packet(packet: &PacketType) -> Option<u16> {
    let size = match packet {
        PacketType::Arp(_) => 64u16,
        // let size = 64u16;
        // TODO: Do we need this now that linux is constructing it?
        // frame[EthernetRepr::SIZE + Arp::SIZE..size as usize].fill(0);
        // size
        PacketType::Ipv4(ip) => ip.header.length + EthernetRepr::SIZE as u16,
        PacketType::Ipv6 => {
            info!("Not an IPv4 or Arp packet. Throw it away.");
            return None;
        }
    };

    Some(size)
}

pub struct seL4_TxFirewall_TxFirewall {}

impl seL4_TxFirewall_TxFirewall {
    pub const fn new() -> Self {
        Self {}
    }

    pub fn initialize<API: seL4_TxFirewall_TxFirewall_Put_Api>(
        &mut self,
        api: &mut seL4_TxFirewall_TxFirewall_Application_Api<API>,
    ) {
        #[cfg(feature = "sel4")]
        info!("initialize entrypoint invoked");
    }

    pub fn timeTriggered<API: seL4_TxFirewall_TxFirewall_Full_Api>(
        &mut self,
        api: &mut seL4_TxFirewall_TxFirewall_Application_Api<API>,
    ) {
        #[cfg(feature = "sel4")]
        trace!("compute entrypoint invoked");
        for i in 0..NUM_MSGS {
            if let Some(frame) = eth_get(i, api) {
                if let Some(packet) = get_frame_packet(&frame) {
                    if let Some(size) = can_send_packet(&packet) {
                        let out = SW::SizedEthernetMessage_Impl {
                            size,
                            message: frame,
                        };
                        eth_put(i, out, api);
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
        Address, Arp, ArpOp, EtherType, HardwareType, IpProtocol, Ipv4Address, Ipv4Packet,
        Ipv4ProtoPacket, Ipv4Repr, TcpRepr, UdpRepr,
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
        assert_eq!(Some(64u16), can_send_packet(&packet));
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
        assert_eq!(Some(64u16), can_send_packet(&packet));
    }

    #[test]
    fn packet_invalid_ipv6() {
        let packet = PacketType::Ipv6;
        assert!(can_send_packet(&packet).is_none());
    }

    #[test]
    fn valid_ipv4() {
        let packet = PacketType::Ipv4(Ipv4Packet {
            header: Ipv4Repr {
                protocol: IpProtocol::Tcp,
                length: 0x29,
            },
            protocol: Ipv4ProtoPacket::Tcp(TcpRepr { dst_port: 443 }),
        });
        assert_eq!(Some(0x37), can_send_packet(&packet));

        let packet = PacketType::Ipv4(Ipv4Packet {
            header: Ipv4Repr {
                protocol: IpProtocol::Tcp,
                length: 0x15,
            },
            protocol: Ipv4ProtoPacket::Tcp(TcpRepr { dst_port: 5760 }),
        });
        assert_eq!(Some(0x23), can_send_packet(&packet));

        let packet = PacketType::Ipv4(Ipv4Packet {
            header: Ipv4Repr {
                protocol: IpProtocol::Udp,
                length: 0x10,
            },
            protocol: Ipv4ProtoPacket::Udp(UdpRepr { dst_port: 15 }),
        });
        assert_eq!(Some(0x1E), can_send_packet(&packet));

        let packet = PacketType::Ipv4(Ipv4Packet {
            header: Ipv4Repr {
                protocol: IpProtocol::Udp,
                length: 0x53,
            },
            protocol: Ipv4ProtoPacket::Udp(UdpRepr { dst_port: 68 }),
        });
        assert_eq!(Some(0x61), can_send_packet(&packet));

        let packet = PacketType::Ipv4(Ipv4Packet {
            header: Ipv4Repr {
                protocol: IpProtocol::Icmp,
                length: 0x25,
            },
            protocol: Ipv4ProtoPacket::TxOnly,
        });
        assert_eq!(Some(0x33), can_send_packet(&packet));
    }
}

// #[cfg(test)]
// mod tx_ethernet_frames_tests {
//     use super::*;

//     mod get_in {
//         use super::*;

//         #[test]
//         fn valid() {
//             let mut tx_buf: SW::RawEthernetMessage_Impl = [0; SW::SW_RawEthernetMessage_Impl_DIM_0];

//             let res = eth_get(0, &mut tx_buf);
//             assert!(res);
//             let res = eth_get(1, &mut tx_buf);
//             assert!(res);
//             let res = eth_get(2, &mut tx_buf);
//             assert!(res);
//             let res = eth_get(3, &mut tx_buf);
//             assert!(res);
//             let res = eth_get(0, &mut tx_buf);
//             assert!(res);
//             let res = eth_get(1, &mut tx_buf);
//             assert!(res);
//         }

//         #[test]
//         #[should_panic]
//         fn out_of_bounds() {
//             let mut tx_buf: SW::RawEthernetMessage_Impl = [0; SW::SW_RawEthernetMessage_Impl_DIM_0];

//             let _ = eth_get(4, &mut tx_buf);
//         }
//     }

//     mod put_out {
//         use super::*;

//         #[test]
//         fn valid() {
//             let mut state = State::new();
//             let message: SW::RawEthernetMessage_Impl = [0; SW::SW_RawEthernetMessage_Impl_DIM_0];

//             let mut tx_buf: SW::SizedEthernetMessage_Impl =
//                 SW::SizedEthernetMessage_Impl { message, size: 20 };

//             assert_eq!(state.idx, 0);
//             eth_put(&mut state, &mut tx_buf);
//             assert_eq!(state.idx, 1);
//             eth_put(&mut state, &mut tx_buf);
//             assert_eq!(state.idx, 2);
//             eth_put(&mut state, &mut tx_buf);
//             assert_eq!(state.idx, 3);
//             eth_put(&mut state, &mut tx_buf);
//             assert_eq!(state.idx, 0);
//             eth_put(&mut state, &mut tx_buf);
//             assert_eq!(state.idx, 1);
//         }

//         #[test]
//         #[should_panic]
//         fn out_of_bounds() {
//             let mut state = State::new();
//             state.idx = 4;
//             let message: SW::RawEthernetMessage_Impl = [0; SW::SW_RawEthernetMessage_Impl_DIM_0];

//             let mut tx_buf: SW::SizedEthernetMessage_Impl =
//                 SW::SizedEthernetMessage_Impl { message, size: 20 };

//             eth_put(&mut state, &mut tx_buf);
//         }
//     }
// }

// #[cfg(test)]
// mod bindings {
//     use super::*;
//     pub fn get_EthernetFramesTxIn0(_value: *mut SW::RawEthernetMessage_Impl) -> bool {
//         true
//     }
//     pub fn get_EthernetFramesTxIn1(_value: *mut SW::RawEthernetMessage_Impl) -> bool {
//         true
//     }
//     pub fn get_EthernetFramesTxIn2(_value: *mut SW::RawEthernetMessage_Impl) -> bool {
//         true
//     }
//     pub fn get_EthernetFramesTxIn3(_value: *mut SW::RawEthernetMessage_Impl) -> bool {
//         true
//     }
//     pub fn EthernetFramesTxIn0_is_empty() -> bool {
//         false
//     }
//     pub fn EthernetFramesTxIn1_is_empty() -> bool {
//         false
//     }
//     pub fn EthernetFramesTxIn2_is_empty() -> bool {
//         false
//     }
//     pub fn EthernetFramesTxIn3_is_empty() -> bool {
//         false
//     }
//     pub fn put_EthernetFramesTxOut0(_value: *mut SW::SizedEthernetMessage_Impl) -> bool {
//         true
//     }
//     pub fn put_EthernetFramesTxOut1(_value: *mut SW::SizedEthernetMessage_Impl) -> bool {
//         true
//     }
//     pub fn put_EthernetFramesTxOut2(_value: *mut SW::SizedEthernetMessage_Impl) -> bool {
//         true
//     }
//     pub fn put_EthernetFramesTxOut3(_value: *mut SW::SizedEthernetMessage_Impl) -> bool {
//         true
//     }
//     pub struct EthChannelGet {
//         pub get: fn(*mut SW::RawEthernetMessage_Impl) -> bool,
//         pub empty: fn() -> bool,
//     }
//     pub struct EthChannelPut {
//         pub put: fn(*mut SW::SizedEthernetMessage_Impl) -> bool,
//     }
// }
