#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

// This file will not be overwritten if codegen is rerun

use crate::bridge::seL4_TxFirewall_TxFirewall_api::*;
use data::*;
#[cfg(feature = "sel4")]
#[allow(unused_imports)]
use log::{debug, error, info, trace, warn};
use vstd::prelude::*;

use crate::bridge::seL4_TxFirewall_TxFirewall_GUMBOX as gumbox;
use firewall_core::{Arp, EthFrame, EthernetRepr, Ipv4Packet, PacketType};
mod config;
use crate::SW::SW_RawEthernetMessage_DIM_0;

verus! {

  const NUM_MSGS: usize = 4;

  fn eth_get<API: seL4_TxFirewall_TxFirewall_Get_Api>(
      idx: usize,
      api: &mut seL4_TxFirewall_TxFirewall_Application_Api<API>,
  ) -> (r: Option<SW::RawEthernetMessage>)
      ensures
          match idx {
              0 => (r == api.EthernetFramesTxIn0),
              1 => (r == api.EthernetFramesTxIn1),
              2 => (r == api.EthernetFramesTxIn2),
              3 => (r == api.EthernetFramesTxIn3),
              _ => r.is_none(),
          }
  {
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
  )
      ensures
          match idx {
              0 => (Some(tx_buf) == api.EthernetFramesTxOut0),
              1 => (Some(tx_buf) == api.EthernetFramesTxOut1),
              2 => (Some(tx_buf) == api.EthernetFramesTxOut2),
              3 => (Some(tx_buf) == api.EthernetFramesTxOut3),
              _ => (old(api).EthernetFramesTxOut0 == api.EthernetFramesTxOut0) &&
                    (old(api).EthernetFramesTxOut1 == api.EthernetFramesTxOut1) &&
                    (old(api).EthernetFramesTxOut2 == api.EthernetFramesTxOut2) &&
                    (old(api).EthernetFramesTxOut3 == api.EthernetFramesTxOut3),
          }
  {
      match idx {
          0 => api.put_EthernetFramesTxOut0(tx_buf),
          1 => api.put_EthernetFramesTxOut1(tx_buf),
          2 => api.put_EthernetFramesTxOut2(tx_buf),
          3 => api.put_EthernetFramesTxOut3(tx_buf),
          _ => (),
      }
  }


    fn can_send_packet(packet: &PacketType) -> (r: Option<u16>)
        requires
            (packet is Ipv4) ==> (firewall_core::ipv4_valid_length(*packet))
        ensures
            packet is Arp ==> r.is_some(),
            packet is Ipv4 ==> r.is_some(),
            packet is Ipv6 ==> r.is_none(),
    {
        let size = match packet {
            PacketType::Arp(_) => 64u16,
            // let size = 64u16;
            // TODO: Do we need this now that linux is constructing it?
            // frame[EthernetRepr::SIZE + Arp::SIZE..size as usize].fill(0);
            // size
            PacketType::Ipv4(ip) => ip.header.length + EthernetRepr::SIZE as u16,
            PacketType::Ipv6 => {
                #[cfg(feature = "sel4")]
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

    // fn get_frame_packet(frame: &SW::RawEthernetMessage) -> (r: Option<PacketType>)
    //     requires
    //         frame@.len() == SW_RawEthernetMessage_DIM_0
    //     ensures
    //         r.is_some() ==> (r.unwrap() is Ipv4 ==> firewall_core::ipv4_valid_length(r.unwrap())),
    //         r.is_some() ==> (firewall_core::frame_ipv4(frame) || firewall_core::frame_ipv6(frame) || firewall_core::frame_arp(frame)),
    // {
    //     // let eth = EthFrame::parse(frame)?;
    //     // Some(eth.eth_type)
    //     EthFrame::parse(frame)
    // }

    fn get_frame_packet(frame: &SW::RawEthernetMessage) -> (r: Option<EthFrame>)
        requires
            frame@.len() == SW_RawEthernetMessage_DIM_0
        ensures
            // (r.is_some() && r.unwrap().eth_type is Ipv4 ==> firewall_core::ipv4_valid_length(r.unwrap().eth_type)),
            // Self::hlr_2_3(*frame, true) ==> r.is_some(),
            // r.is_some() ==> Self::hlr_2_3(*frame, true),
            // (Self::frame_is_wellformed_eth2(*frame)) ==> r.is_some(),
                (firewall_core::frame_dst_addr_valid(frame@)
                && firewall_core::frame_is_wellformed_eth2(frame)
                && firewall_core::frame_arp(frame)
                && firewall_core::wellformed_arp_frame(frame@)) ==> (r.is_some() && r.unwrap().eth_type is Arp),
    {
        // assert(Self::frame_is_wellformed_eth2(*frame) == firewall_core::frame_is_wellformed_eth2(frame));
        // let eth = EthFrame::parse(frame)?;
        // Some(eth.eth_type)
        let eth = EthFrame::parse(frame);
        assert(Self::frame_is_wellformed_eth2(*frame) == (firewall_core::frame_dst_addr_valid(frame@)
                && firewall_core::frame_is_wellformed_eth2(frame)));
        assert(Self::frame_has_arp(*frame) == firewall_core::frame_arp(frame));
        assert(Self::arp_is_wellformed(*frame) == firewall_core::wellformed_arp_frame(frame@));
        assert(Self::hlr_2_3(*frame, true) == (firewall_core::frame_dst_addr_valid(frame@)
                && firewall_core::frame_is_wellformed_eth2(frame)
                && firewall_core::frame_arp(frame)
                && firewall_core::wellformed_arp_frame(frame@)));
        eth
    }

    pub fn initialize<API: seL4_TxFirewall_TxFirewall_Put_Api>(
      &mut self,
      api: &mut seL4_TxFirewall_TxFirewall_Application_Api<API>)
    {
      #[cfg(feature = "sel4")]
      info!("initialize entrypoint invoked");
    }

    pub fn timeTriggered<API: seL4_TxFirewall_TxFirewall_Full_Api>(
      &mut self,
      api: &mut seL4_TxFirewall_TxFirewall_Application_Api<API>)
      requires
        // BEGIN MARKER TIME TRIGGERED REQUIRES
        // assume AADL_Requirement
        //   All outgoing event ports must be empty
        old(api).EthernetFramesTxOut0.is_none(),
        old(api).EthernetFramesTxOut1.is_none(),
        old(api).EthernetFramesTxOut2.is_none(),
        old(api).EthernetFramesTxOut3.is_none()
        // END MARKER TIME TRIGGERED REQUIRES
      ensures
        // BEGIN MARKER TIME TRIGGERED ENSURES
        // guarantee hlr_07_tx0_can_send_valid_arp
        api.EthernetFramesTxIn0.is_some() && Self::valid_arp(api.EthernetFramesTxIn0.unwrap()) ==>
          api.EthernetFramesTxOut0.is_some() &&
            (api.EthernetFramesTxIn0.unwrap() == api.EthernetFramesTxOut0.unwrap().message) &&
            Self::valid_output_arp_size(api.EthernetFramesTxOut0.unwrap()),
        // guarantee hlr_12_tx0_can_send_valid_ipv4
        api.EthernetFramesTxIn0.is_some() && Self::valid_ipv4(api.EthernetFramesTxIn0.unwrap()) ==>
          api.EthernetFramesTxOut0.is_some() &&
            (api.EthernetFramesTxIn0.unwrap() == api.EthernetFramesTxOut0.unwrap().message) &&
            Self::valid_output_ipv4_size(api.EthernetFramesTxIn0.unwrap(),api.EthernetFramesTxOut0.unwrap()),
        // guarantee hlr_14_tx0_disallow
        api.EthernetFramesTxIn0.is_some() && !(Self::allow_outbound_frame(api.EthernetFramesTxIn0.unwrap())) ==>
          api.EthernetFramesTxOut0.is_none(),
        // guarantee hlr_16_tx0_no_input
        !(api.EthernetFramesTxIn0.is_some()) ==> api.EthernetFramesTxOut0.is_none(),
        // guarantee hlr_07_tx1_can_send_valid_arp
        api.EthernetFramesTxIn1.is_some() && Self::valid_arp(api.EthernetFramesTxIn1.unwrap()) ==>
          api.EthernetFramesTxOut1.is_some() &&
            (api.EthernetFramesTxIn1.unwrap() == api.EthernetFramesTxOut1.unwrap().message) &&
            Self::valid_output_arp_size(api.EthernetFramesTxOut1.unwrap()),
        // guarantee hlr_12_tx1_can_send_valid_ipv4
        api.EthernetFramesTxIn1.is_some() && Self::valid_ipv4(api.EthernetFramesTxIn1.unwrap()) ==>
          api.EthernetFramesTxOut1.is_some() &&
            (api.EthernetFramesTxIn1.unwrap() == api.EthernetFramesTxOut1.unwrap().message) &&
            Self::valid_output_ipv4_size(api.EthernetFramesTxIn1.unwrap(),api.EthernetFramesTxOut1.unwrap()),
        // guarantee hlr_14_tx1_disallow
        api.EthernetFramesTxIn1.is_some() && !(Self::allow_outbound_frame(api.EthernetFramesTxIn1.unwrap())) ==>
          api.EthernetFramesTxOut1.is_none(),
        // guarantee hlr_16_tx1_no_input
        !(api.EthernetFramesTxIn1.is_some()) ==> api.EthernetFramesTxOut1.is_none(),
        // guarantee hlr_07_tx2_can_send_valid_arp
        api.EthernetFramesTxIn2.is_some() && Self::valid_arp(api.EthernetFramesTxIn2.unwrap()) ==>
          api.EthernetFramesTxOut2.is_some() &&
            (api.EthernetFramesTxIn2.unwrap() == api.EthernetFramesTxOut2.unwrap().message) &&
            Self::valid_output_arp_size(api.EthernetFramesTxOut2.unwrap()),
        // guarantee hlr_12_tx2_can_send_valid_ipv4
        api.EthernetFramesTxIn2.is_some() && Self::valid_ipv4(api.EthernetFramesTxIn2.unwrap()) ==>
          api.EthernetFramesTxOut2.is_some() &&
            (api.EthernetFramesTxIn2.unwrap() == api.EthernetFramesTxOut2.unwrap().message) &&
            Self::valid_output_ipv4_size(api.EthernetFramesTxIn2.unwrap(),api.EthernetFramesTxOut2.unwrap()),
        // guarantee hlr_14_tx2_disallow
        api.EthernetFramesTxIn2.is_some() && !(Self::allow_outbound_frame(api.EthernetFramesTxIn2.unwrap())) ==>
          api.EthernetFramesTxOut2.is_none(),
        // guarantee hlr_16_tx2_no_input
        !(api.EthernetFramesTxIn2.is_some()) ==> api.EthernetFramesTxOut2.is_none(),
        // guarantee hlr_07_tx3_can_send_valid_arp
        api.EthernetFramesTxIn3.is_some() && Self::valid_arp(api.EthernetFramesTxIn3.unwrap()) ==>
          api.EthernetFramesTxOut3.is_some() &&
            (api.EthernetFramesTxIn3.unwrap() == api.EthernetFramesTxOut3.unwrap().message) &&
            Self::valid_output_arp_size(api.EthernetFramesTxOut3.unwrap()),
        // guarantee hlr_12_tx3_can_send_valid_ipv4
        api.EthernetFramesTxIn3.is_some() && Self::valid_ipv4(api.EthernetFramesTxIn3.unwrap()) ==>
          api.EthernetFramesTxOut3.is_some() &&
            (api.EthernetFramesTxIn3.unwrap() == api.EthernetFramesTxOut3.unwrap().message) &&
            Self::valid_output_ipv4_size(api.EthernetFramesTxIn3.unwrap(),api.EthernetFramesTxOut3.unwrap()),
        // guarantee hlr_14_tx3_disallow
        api.EthernetFramesTxIn3.is_some() && !(Self::allow_outbound_frame(api.EthernetFramesTxIn3.unwrap())) ==>
          api.EthernetFramesTxOut3.is_none(),
        // guarantee hlr_16_tx3_no_input
        !(api.EthernetFramesTxIn3.is_some()) ==> api.EthernetFramesTxOut3.is_none()
        // END MARKER TIME TRIGGERED ENSURES
    {
        #[cfg(feature = "sel4")]
        trace!("compute entrypoint invoked");
        // for i in 0..1 {
        //     if let Some(frame) = eth_get(i, api) {
            if let Some(frame) = api.get_EthernetFramesTxIn0() {
            assert(api.EthernetFramesTxIn0.is_some() ==> (api.EthernetFramesTxIn0.unwrap() == frame));
                // assert(Self::frame_is_wellformed_eth2(frame) == firewall_core::frame_is_wellformed_eth2(&frame));

                let res = Self::get_frame_packet(&frame);
                    // assert(Self::frame_is_wellformed_eth2(frame) ==> res.is_some());
                    // assert((api.EthernetFramesTxIn0.is_some() &&
                    //     Self::should_allow_outbound_frame_tx(api.EthernetFramesTxIn0.unwrap(),true))  ==> (res.is_some()));
                    if let Some(eth) = res {
                    {
                        let out = SW::SizedEthernetMessage_Impl {
                            size: 64u16,
                            message: frame,
                        };
                        // eth_put(i, out, api);
                        if let PacketType::Arp(_) = eth.eth_type {
                            api.put_EthernetFramesTxOut0(out);
                            assert(api.EthernetFramesTxOut0.is_some());

                        }
                    }
                    // if let Some(size) = can_send_packet(&packet) {
                    //     let out = SW::SizedEthernetMessage_Impl {
                    //         size,
                    //         message: frame,
                    //     };
                    //     eth_put(i, out, api);
                    // }
                }
            }
        // }
    }

    pub fn notify(
      &mut self,
      channel: microkit_channel)
    {
      // this method is called when the monitor does not handle the passed in channel
      match channel {
        _ => {
          #[cfg(feature = "sel4")]
          warn!("Unexpected channel {}", channel)
        }
      }
    }


    pub open spec fn arp_is_wellformed(frame: SW::RawEthernetMessage) -> bool
    {
        firewall_core::wellformed_arp_frame(frame@)
    }

    pub open spec fn frame_is_wellformed_eth2(frame: SW::RawEthernetMessage) -> bool
    {
        firewall_core::frame_is_wellformed_eth2(&frame) && firewall_core::frame_dst_addr_valid(frame@)
    }

    // pub open spec fn frame_ipv4(frame: SW::RawEthernetMessage) -> bool
    // {
    //     (frame[12] == 8u8) && (frame[13] == 0u8)
    // }

    // pub open spec fn frame_ipv6(frame: SW::RawEthernetMessage) -> bool
    // {
    //     (frame[12] == 134u8) && (frame[13] == 221u8)
    // }

    // pub open spec fn frame_arp(frame: SW::RawEthernetMessage) -> bool
    // {
    //     (frame[12] == 8u8) && (frame[13] == 6u8)
    // }

    pub open spec fn frame_has_ipv4(frame: SW::RawEthernetMessage) -> bool
    {
      firewall_core::frame_ipv4(&frame)
    }

    pub open spec fn frame_has_ipv6(frame: SW::RawEthernetMessage) -> bool
    {
      firewall_core::frame_ipv6(&frame)
    }

    pub open spec fn frame_has_arp(frame: SW::RawEthernetMessage) -> bool
    {
      firewall_core::frame_arp(&frame)
    }

    // BEGIN MARKER GUMBO METHODS
    pub open spec fn two_bytes_to_u16(
      byte0: u8,
      byte1: u8) -> u16
    {
      (((byte0) as u16) * 256u16 + ((byte1) as u16)) as u16
    }

    pub open spec fn frame_is_wellformed_eth2(frame: SW::RawEthernetMessage) -> bool
    {
      Self::valid_frame_ethertype(frame) && Self::valid_frame_dst_addr(frame)
    }

    pub open spec fn valid_frame_ethertype(frame: SW::RawEthernetMessage) -> bool
    {
      Self::frame_has_ipv4(frame) || Self::frame_has_arp(frame) ||
        Self::frame_has_ipv6(frame)
    }

    pub open spec fn valid_frame_dst_addr(frame: SW::RawEthernetMessage) -> bool
    {
      !((frame[0] == 0u8) &&
        (frame[1] == 0u8) &&
        (frame[2] == 0u8) &&
        (frame[3] == 0u8) &&
        (frame[4] == 0u8) &&
        (frame[5] == 0u8))
    }

    pub open spec fn frame_has_ipv4(frame: SW::RawEthernetMessage) -> bool
    {
      (frame[12] == 8u8) &&
        (frame[13] == 0u8)
    }

    pub open spec fn frame_has_ipv6(frame: SW::RawEthernetMessage) -> bool
    {
      (frame[12] == 134u8) &&
        (frame[13] == 221u8)
    }

    pub open spec fn frame_has_arp(frame: SW::RawEthernetMessage) -> bool
    {
      (frame[12] == 8u8) &&
        (frame[13] == 6u8)
    }

    pub open spec fn arp_has_ipv4(frame: SW::RawEthernetMessage) -> bool
    {
      (frame[16] == 8u8) &&
        (frame[17] == 0u8)
    }

    pub open spec fn arp_has_ipv6(frame: SW::RawEthernetMessage) -> bool
    {
      (frame[16] == 134u8) &&
        (frame[17] == 221u8)
    }

    pub open spec fn valid_arp_ptype(frame: SW::RawEthernetMessage) -> bool
    {
      Self::arp_has_ipv4(frame) || Self::arp_has_ipv6(frame)
    }

    pub open spec fn valid_arp_op(frame: SW::RawEthernetMessage) -> bool
    {
      (frame[20] == 0u8) &&
        ((frame[21] == 1u8) ||
          (frame[21] == 2u8))
    }

    pub open spec fn valid_arp_htype(frame: SW::RawEthernetMessage) -> bool
    {
      (frame[14] == 0u8) &&
        (frame[15] == 1u8)
    }

    pub open spec fn wellformed_arp_frame(frame: SW::RawEthernetMessage) -> bool
    {
      Self::valid_arp_op(frame) && Self::valid_arp_htype(frame) &&
        Self::valid_arp_ptype(frame)
    }

    pub open spec fn ipv4_length(frame: SW::RawEthernetMessage) -> u16
    {
      Self::two_bytes_to_u16(frame[16],frame[17])
    }

    pub open spec fn valid_ipv4_length(frame: SW::RawEthernetMessage) -> bool
    {
      Self::ipv4_length(frame) <= 9000u16
    }

    pub open spec fn valid_ipv4_protocol(frame: SW::RawEthernetMessage) -> bool
    {
      (frame[23] == 0u8) ||
        (frame[23] == 1u8) ||
        (frame[23] == 2u8) ||
        (frame[23] == 6u8) ||
        (frame[23] == 17u8) ||
        (frame[23] == 43u8) ||
        (frame[23] == 44u8) ||
        (frame[23] == 58u8) ||
        (frame[23] == 59u8) ||
        (frame[23] == 60u8)
    }

    pub open spec fn wellformed_ipv4_frame(frame: SW::RawEthernetMessage) -> bool
    {
      Self::valid_ipv4_protocol(frame) && Self::valid_ipv4_length(frame)
    }

    pub open spec fn valid_ipv6(frame: SW::RawEthernetMessage) -> bool
    {
      Self::frame_is_wellformed_eth2(frame) && Self::frame_has_ipv6(frame)
    }

    pub open spec fn valid_arp(frame: SW::RawEthernetMessage) -> bool
    {
      Self::frame_is_wellformed_eth2(frame) && Self::frame_has_arp(frame) &&
        Self::wellformed_arp_frame(frame)
    }

    pub open spec fn valid_ipv4(frame: SW::RawEthernetMessage) -> bool
    {
      Self::frame_is_wellformed_eth2(frame) && Self::frame_has_ipv4(frame) &&
        Self::wellformed_ipv4_frame(frame)
    }

    pub open spec fn valid_output_arp_size(output: SW::SizedEthernetMessage_Impl) -> bool
    {
      output.sz == 64u16
    }

    pub open spec fn valid_output_ipv4_size(
      input: SW::RawEthernetMessage,
      output: SW::SizedEthernetMessage_Impl) -> bool
    {
      output.sz == Self::ipv4_length(input) + 14u16
    }

    pub open spec fn allow_outbound_frame(frame: SW::RawEthernetMessage) -> bool
    {
      Self::valid_arp(frame) || Self::valid_ipv4(frame)
    }
    // END MARKER GUMBO METHODS
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
//             let mut tx_buf: SW::RawEthernetMessage = [0; SW::SW_RawEthernetMessage_DIM_0];

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
//             let mut tx_buf: SW::RawEthernetMessage = [0; SW::SW_RawEthernetMessage_DIM_0];

//             let _ = eth_get(4, &mut tx_buf);
//         }
//     }

//     mod put_out {
//         use super::*;

//         #[test]
//         fn valid() {
//             let mut state = State::new();
//             let message: SW::RawEthernetMessage = [0; SW::SW_RawEthernetMessage_DIM_0];

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
//             let message: SW::RawEthernetMessage = [0; SW::SW_RawEthernetMessage_DIM_0];

//             let mut tx_buf: SW::SizedEthernetMessage_Impl =
//                 SW::SizedEthernetMessage_Impl { message, size: 20 };

//             eth_put(&mut state, &mut tx_buf);
//         }
//     }
// }

// #[cfg(test)]
// mod bindings {
//     use super::*;
//     pub fn get_EthernetFramesTxIn0(_value: *mut SW::RawEthernetMessage) -> bool {
//         true
//     }
//     pub fn get_EthernetFramesTxIn1(_value: *mut SW::RawEthernetMessage) -> bool {
//         true
//     }
//     pub fn get_EthernetFramesTxIn2(_value: *mut SW::RawEthernetMessage) -> bool {
//         true
//     }
//     pub fn get_EthernetFramesTxIn3(_value: *mut SW::RawEthernetMessage) -> bool {
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
//         pub get: fn(*mut SW::RawEthernetMessage) -> bool,
//         pub empty: fn() -> bool,
//     }
//     pub struct EthChannelPut {
//         pub put: fn(*mut SW::SizedEthernetMessage_Impl) -> bool,
//     }
// }
