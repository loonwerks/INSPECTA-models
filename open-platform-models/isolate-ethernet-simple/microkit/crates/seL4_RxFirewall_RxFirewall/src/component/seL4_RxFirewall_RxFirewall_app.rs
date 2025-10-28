#![allow(non_camel_case_types)]
#![allow(non_snake_case)]

// This file will not be overwritten if codegen is rerun

use crate::bridge::seL4_RxFirewall_RxFirewall_api::*;
use data::*;
#[cfg(feature = "sel4")]
// #[allow(unused_imports)]
use log::{debug, error, info, trace, warn};
use vstd::prelude::*;
use vstd::slice::slice_subrange;
use SW::{SW_EthIpUdpHeaders_DIM_0, SW_UdpPayload_DIM_0, UdpFrame_Impl};

use crate::SW::SW_RawEthernetMessage_DIM_0;
use firewall_core::{EthFrame, IpProtocol, Ipv4ProtoPacket, PacketType, TcpRepr, UdpRepr};

verus! {


    fn udp_frame_from_raw_eth(value: SW::RawEthernetMessage) -> (r: UdpFrame_Impl)
        ensures
            r.headers@ =~= value@.subrange(0, SW_EthIpUdpHeaders_DIM_0 as int),
            r.payload@ =~= value@.subrange(SW_EthIpUdpHeaders_DIM_0 as int, SW_RawEthernetMessage_DIM_0 as int),
     {
        let mut headers = [0u8; SW_EthIpUdpHeaders_DIM_0];
        let mut payload = [0u8; SW_RawEthernetMessage_DIM_0-SW_EthIpUdpHeaders_DIM_0];
        let mut i = 0;
        while i < SW_EthIpUdpHeaders_DIM_0
            invariant
                0 <= i <= headers@.len() < value@.len(),
                forall |j| 0 <= j < i ==> headers[j] == value[j],
            decreases
                SW_EthIpUdpHeaders_DIM_0 - i
        {
            headers.set(i, value[i]);
            i += 1;
        }

        let mut i = 0;
        while i < SW_UdpPayload_DIM_0
            invariant
                0 <= i <= payload@.len() <= value@.len()-SW_EthIpUdpHeaders_DIM_0,
                forall |j| 0 <= j < i ==> #[trigger] payload[j] == value[j+SW_EthIpUdpHeaders_DIM_0],
            decreases
                SW_UdpPayload_DIM_0 - i
        {
            payload.set(i, value[i+SW_EthIpUdpHeaders_DIM_0]);
            i += 1;
        }
        UdpFrame_Impl { headers, payload}
    }

    mod config;

    const NUM_MSGS: usize = 4;

    #[verifier::external_body]
    fn info(s: &str) {
        #[cfg(feature = "sel4")]
        info!("{s}");
    }

    #[verifier::external_body]
    fn info_protocol(protocol: IpProtocol) {
        #[cfg(feature = "sel4")]
        info!("Not a TCP or UDP packet. ({:?}) Throw it away.", protocol);
    }

    #[verifier::external_body]
    fn trace(s: &str) {
        #[cfg(feature = "sel4")]
        trace!("{s}");
    }

    #[verifier::external_body]
    fn warn_channel(channel: microkit_channel) {
        #[cfg(feature = "sel4")]
        warn!("Unexpected channel {}", channel)
    }

    pub struct seL4_RxFirewall_RxFirewall {}

    // fn eth_get<API: seL4_RxFirewall_RxFirewall_Get_Api>(
    //     idx: usize,
    //     api: &mut seL4_RxFirewall_RxFirewall_Application_Api<API>,
    // ) -> Option<SW::RawEthernetMessage> {
    //     match idx {
    //         0 => api.get_EthernetFramesRxIn0(),
    //         1 => api.get_EthernetFramesRxIn1(),
    //         2 => api.get_EthernetFramesRxIn2(),
    //         3 => api.get_EthernetFramesRxIn3(),
    //         _ => None,
    //     }
    // }

    // fn eth_put<API: seL4_RxFirewall_RxFirewall_Put_Api>(
    //     idx: usize,
    //     rx_buf: &mut SW::RawEthernetMessage,
    //     api: &mut seL4_RxFirewall_RxFirewall_Application_Api<API>,
    // ) {
    //     match idx {
    //         0 => api.put_EthernetFramesRxOut0(*rx_buf),
    //         1 => api.put_EthernetFramesRxOut1(*rx_buf),
    //         2 => api.put_EthernetFramesRxOut2(*rx_buf),
    //         3 => api.put_EthernetFramesRxOut3(*rx_buf),
    //         _ => (),
    //     }
    // }

    fn port_allowed(allowed_ports: &[u16], port: u16) -> (r: bool)
        ensures
            r == allowed_ports@.contains(port),
    {
        let mut i: usize = 0;
        while i < allowed_ports.len()
            invariant
                0 <= i <= allowed_ports@.len(),
                forall |j| 0 <= j < i ==> allowed_ports@[j] != port,
            decreases
                allowed_ports@.len() - i
        {
            if allowed_ports[i] == port {
                return true;
            }
            i += 1;
        }
        false
    }

    fn udp_port_allowed(port: u16) -> (r: bool)
        ensures
            r == config::udp::ALLOWED_PORTS@.contains(port),
    {
        port_allowed(&config::udp::ALLOWED_PORTS, port)
    }

    fn tcp_port_allowed(port: u16) -> (r: bool)
        ensures
            r == config::tcp::ALLOWED_PORTS@.contains(port),
    {
        port_allowed(&config::tcp::ALLOWED_PORTS, port)
    }

    pub open spec fn packet_is_whitelisted_tcp(packet: &PacketType) -> bool
    {
        packet is Ipv4 &&
            packet->Ipv4_0.protocol is Tcp &&
            seL4_RxFirewall_RxFirewall::ipv4_tcp_on_allowed_port_quant(packet->Ipv4_0.protocol->Tcp_0.dst_port)
    }


    pub open spec fn packet_is_whitelisted_udp(packet: &PacketType) -> bool
    {
        packet is Ipv4 &&
            packet->Ipv4_0.protocol is Udp &&
            seL4_RxFirewall_RxFirewall::ipv4_udp_on_allowed_port_quant(packet->Ipv4_0.protocol->Udp_0.dst_port)
    }

    fn can_send_to_vmm(packet: &PacketType) -> (r: bool)
        requires
            config::udp::ALLOWED_PORTS =~= seL4_RxFirewall_RxFirewall::UDP_ALLOWED_PORTS(),
        ensures
            ((packet is Arp) ||
                packet_is_whitelisted_udp(packet)
            ) == (r == true),
    {
        match packet {
            PacketType::Arp(_) => true,
            PacketType::Ipv4(ip) => match &ip.protocol {
                // Ipv4ProtoPacket::Tcp(tcp) => {
                //     let allowed = tcp_port_allowed(tcp.dst_port);
                //     if !allowed {
                //         info("TCP packet filtered out");
                //     }
                //     allowed
                // }
                Ipv4ProtoPacket::Udp(udp) => {
                    udp_port_allowed(udp.dst_port)
                }
                _ => {
                    info_protocol(ip.header.protocol);
                    false
                }
            },
            PacketType::Ipv6 => {
                info("Not an IPv4 or Arp packet. Throw it away.");
                false
            },
        }
    }

    pub open spec fn packet_is_mavlink_udp(packet: &PacketType) -> bool
    {
        packet is Ipv4 &&
            packet->Ipv4_0.protocol is Udp &&
            packet->Ipv4_0.protocol->Udp_0.dst_port == 14550
    }

fn can_send_to_mavlink(packet: &PacketType) -> (r: bool)
        ensures
            (packet_is_mavlink_udp(packet)) == (r == true),
    {
        match packet {
            PacketType::Ipv4(ip) => match &ip.protocol {
                Ipv4ProtoPacket::Udp(udp) => {
                    if udp.dst_port == 14550 {
                        true
                    } else {
                        info("UDP packet filtered out");
                        false
                    }
                }
                _ => {
                    info_protocol(ip.header.protocol);
                    false
                }
            },
            _ => {
                info("Not an IPv4 or Arp packet. Throw it away.");
                false
            },
        }
    }

impl seL4_RxFirewall_RxFirewall {
    pub const fn new() -> Self {
        Self {}
    }

    pub fn get_frame_packet(frame: &SW::RawEthernetMessage) -> (r: Option<EthFrame>)
        requires
            frame@.len() == SW_RawEthernetMessage_DIM_0
        ensures
            Self::valid_arp(*frame) == firewall_core::res_is_arp(r),
            Self::valid_ipv4_udp(*frame) == firewall_core::res_is_udp(r),
            Self::valid_ipv4_tcp(*frame) == firewall_core::res_is_tcp(r),
            Self::valid_ipv4_tcp(*frame) ==> firewall_core::tcp_port_bytes_match(frame, r),
            Self::valid_ipv4_udp(*frame) ==> firewall_core::udp_port_bytes_match(frame, r),
    {
        let eth = EthFrame::parse(frame);
        if eth.is_none() {
            info("Malformed packet. Throw it away.")
        }
        eth
    }

    pub fn initialize<API: seL4_RxFirewall_RxFirewall_Put_Api>(
      &mut self,
      api: &mut seL4_RxFirewall_RxFirewall_Application_Api<API>)
    {
      info("initialize entrypoint invoked");
    }

    pub fn timeTriggered<API: seL4_RxFirewall_RxFirewall_Full_Api> (
      &mut self,
      api: &mut seL4_RxFirewall_RxFirewall_Application_Api<API>)
      requires
        config::udp::ALLOWED_PORTS =~= Self::UDP_ALLOWED_PORTS(),
        // BEGIN MARKER TIME TRIGGERED REQUIRES
        // assume AADL_Requirement
        //   All outgoing event ports must be empty
        old(api).VmmOut0.is_none(),
        old(api).VmmOut1.is_none(),
        old(api).VmmOut2.is_none(),
        old(api).VmmOut3.is_none(),
        old(api).MavlinkOut0.is_none(),
        old(api).MavlinkOut1.is_none(),
        old(api).MavlinkOut2.is_none(),
        old(api).MavlinkOut3.is_none()
        // END MARKER TIME TRIGGERED REQUIRES
      ensures
        // BEGIN MARKER TIME TRIGGERED ENSURES
        // guarantee hlr_05_rx0_can_send_arp_to_vmm
        api.EthernetFramesRxIn0.is_some() && Self::valid_arp(api.EthernetFramesRxIn0.unwrap()) ==>
          api.VmmOut0.is_some() &&
            (api.EthernetFramesRxIn0.unwrap() == api.VmmOut0.unwrap()) &&
            api.MavlinkOut0.is_none(),
        // guarantee hlr_18_rx0_can_send_mavlink_udp
        api.EthernetFramesRxIn0.is_some() && Self::valid_ipv4_udp_mavlink(api.EthernetFramesRxIn0.unwrap()) ==>
          api.MavlinkOut0.is_some() && Self::input_eq_mav_output(api.EthernetFramesRxIn0.unwrap(),api.MavlinkOut0.unwrap()) &&
            api.VmmOut0.is_none(),
        // guarantee hlr_13_rx0_can_send_ipv4_udp
        api.EthernetFramesRxIn0.is_some() && Self::valid_ipv4_udp_port(api.EthernetFramesRxIn0.unwrap()) ==>
          api.VmmOut0.is_some() &&
            (api.EthernetFramesRxIn0.unwrap() == api.VmmOut0.unwrap()) &&
            api.MavlinkOut0.is_none(),
        // guarantee hlr_15_rx0_disallow
        api.EthernetFramesRxIn0.is_some() && !(Self::allow_outbound_frame(api.EthernetFramesRxIn0.unwrap())) ==>
          api.VmmOut0.is_none() && api.MavlinkOut0.is_none(),
        // guarantee hlr_17_rx0_no_input
        !(api.EthernetFramesRxIn0.is_some()) ==>
          api.VmmOut0.is_none() && api.MavlinkOut0.is_none(),
        // guarantee hlr_05_rx1_can_send_arp_to_vmm
        api.EthernetFramesRxIn1.is_some() && Self::valid_arp(api.EthernetFramesRxIn1.unwrap()) ==>
          api.VmmOut1.is_some() &&
            (api.EthernetFramesRxIn1.unwrap() == api.VmmOut1.unwrap()) &&
            api.MavlinkOut1.is_none(),
        // guarantee hlr_18_rx1_can_send_mavlink_udp
        api.EthernetFramesRxIn1.is_some() && Self::valid_ipv4_udp_mavlink(api.EthernetFramesRxIn1.unwrap()) ==>
          api.MavlinkOut1.is_some() && Self::input_eq_mav_output(api.EthernetFramesRxIn1.unwrap(),api.MavlinkOut1.unwrap()) &&
            api.VmmOut1.is_none(),
        // guarantee hlr_13_rx1_can_send_ipv4_udp
        api.EthernetFramesRxIn1.is_some() && Self::valid_ipv4_udp_port(api.EthernetFramesRxIn1.unwrap()) ==>
          api.VmmOut1.is_some() &&
            (api.EthernetFramesRxIn1.unwrap() == api.VmmOut1.unwrap()) &&
            api.MavlinkOut1.is_none(),
        // guarantee hlr_15_rx1_disallow
        api.EthernetFramesRxIn1.is_some() && !(Self::allow_outbound_frame(api.EthernetFramesRxIn1.unwrap())) ==>
          api.VmmOut1.is_none() && api.MavlinkOut1.is_none(),
        // guarantee hlr_17_rx1_no_input
        !(api.EthernetFramesRxIn1.is_some()) ==>
          api.VmmOut1.is_none() && api.MavlinkOut1.is_none(),
        // guarantee hlr_05_rx2_can_send_arp_to_vmm
        api.EthernetFramesRxIn2.is_some() && Self::valid_arp(api.EthernetFramesRxIn2.unwrap()) ==>
          api.VmmOut2.is_some() &&
            (api.EthernetFramesRxIn2.unwrap() == api.VmmOut2.unwrap()) &&
            api.MavlinkOut2.is_none(),
        // guarantee hlr_18_rx2_can_send_mavlink_udp
        api.EthernetFramesRxIn2.is_some() && Self::valid_ipv4_udp_mavlink(api.EthernetFramesRxIn2.unwrap()) ==>
          api.MavlinkOut2.is_some() && Self::input_eq_mav_output(api.EthernetFramesRxIn2.unwrap(),api.MavlinkOut2.unwrap()) &&
            api.VmmOut2.is_none(),
        // guarantee hlr_13_rx2_can_send_ipv4_udp
        api.EthernetFramesRxIn2.is_some() && Self::valid_ipv4_udp_port(api.EthernetFramesRxIn2.unwrap()) ==>
          api.VmmOut2.is_some() &&
            (api.EthernetFramesRxIn2.unwrap() == api.VmmOut2.unwrap()) &&
            api.MavlinkOut2.is_none(),
        // guarantee hlr_15_rx2_disallow
        api.EthernetFramesRxIn2.is_some() && !(Self::allow_outbound_frame(api.EthernetFramesRxIn2.unwrap())) ==>
          api.VmmOut2.is_none() && api.MavlinkOut2.is_none(),
        // guarantee hlr_17_rx2_no_input
        !(api.EthernetFramesRxIn2.is_some()) ==>
          api.VmmOut2.is_none() && api.MavlinkOut2.is_none(),
        // guarantee hlr_05_rx3_can_send_arp_to_vmm
        api.EthernetFramesRxIn3.is_some() && Self::valid_arp(api.EthernetFramesRxIn3.unwrap()) ==>
          api.VmmOut3.is_some() &&
            (api.EthernetFramesRxIn3.unwrap() == api.VmmOut3.unwrap()) &&
            api.MavlinkOut3.is_none(),
        // guarantee hlr_18_rx3_can_send_mavlink_udp
        api.EthernetFramesRxIn3.is_some() && Self::valid_ipv4_udp_mavlink(api.EthernetFramesRxIn3.unwrap()) ==>
          api.MavlinkOut3.is_some() && Self::input_eq_mav_output(api.EthernetFramesRxIn3.unwrap(),api.MavlinkOut3.unwrap()) &&
            api.VmmOut3.is_none(),
        // guarantee hlr_13_rx3_can_send_ipv4_udp
        api.EthernetFramesRxIn3.is_some() && Self::valid_ipv4_udp_port(api.EthernetFramesRxIn3.unwrap()) ==>
          api.VmmOut3.is_some() &&
            (api.EthernetFramesRxIn3.unwrap() == api.VmmOut3.unwrap()) &&
            api.MavlinkOut3.is_none(),
        // guarantee hlr_15_rx3_disallow
        api.EthernetFramesRxIn3.is_some() && !(Self::allow_outbound_frame(api.EthernetFramesRxIn3.unwrap())) ==>
          api.VmmOut3.is_none() && api.MavlinkOut3.is_none(),
        // guarantee hlr_17_rx3_no_input
        !(api.EthernetFramesRxIn3.is_some()) ==>
          api.VmmOut3.is_none() && api.MavlinkOut3.is_none()
        // END MARKER TIME TRIGGERED ENSURES
    {
        trace("compute entrypoint invoked");

        // Rx0 ports
        if let Some(frame) = api.get_EthernetFramesRxIn0() {
            if let Some(eth) = Self::get_frame_packet(&frame) {
                if can_send_to_vmm(&eth.eth_type) {
                    api.put_VmmOut0(frame);
                } else if can_send_to_mavlink(&eth.eth_type) {
                    let output = udp_frame_from_raw_eth(frame);
                    api.put_MavlinkOut0(output);
                }
            }
        }

        // Rx1 ports
        if let Some(frame) = api.get_EthernetFramesRxIn1() {
            if let Some(eth) = Self::get_frame_packet(&frame) {
                if can_send_to_vmm(&eth.eth_type) {
                    api.put_VmmOut1(frame);
                } else if can_send_to_mavlink(&eth.eth_type) {
                    let output = udp_frame_from_raw_eth(frame);
                    api.put_MavlinkOut1(output);
                }
            }
        }

        // Rx2 ports
        if let Some(frame) = api.get_EthernetFramesRxIn2() {
            if let Some(eth) = Self::get_frame_packet(&frame) {
                if can_send_to_vmm(&eth.eth_type) {
                    api.put_VmmOut2(frame);
                } else if can_send_to_mavlink(&eth.eth_type) {
                    let output = udp_frame_from_raw_eth(frame);
                    api.put_MavlinkOut2(output);
                }
            }
        }

        // Rx3 ports
        if let Some(frame) = api.get_EthernetFramesRxIn3() {
            if let Some(eth) = Self::get_frame_packet(&frame) {
                if can_send_to_vmm(&eth.eth_type) {
                    api.put_VmmOut3(frame);
                } else if can_send_to_mavlink(&eth.eth_type) {
                    let output = udp_frame_from_raw_eth(frame);
                    api.put_MavlinkOut3(output);
                }
            }
        }

    }

    pub fn notify(
      &mut self,
      channel: microkit_channel)
    {
      // this method is called when the monitor does not handle the passed in channel
      match channel {
        _ => {
            warn_channel(channel);
        }
      }
    }

    // TODO: This should be generated, but waiting on HAMR support
    pub open spec fn input_eq_mav_output(frame: SW::RawEthernetMessage, output: UdpFrame_Impl) -> bool
    {
        (forall |i: int| 0 <= i < output.headers.len() ==> #[trigger] frame[i] == output.headers[i]) &&
        (forall |i: int| 0 <= i < output.payload.len() ==> #[trigger] frame[i+output.headers.len()] == output.payload[i])
    }

    pub open spec fn ipv4_udp_on_allowed_port_quant(port: u16) -> bool
    {
        exists|i:int| 0 <= i && i <= Self::UDP_ALLOWED_PORTS().len() - 1 && Self::UDP_ALLOWED_PORTS()[i] == port
    }

    pub open spec fn ipv4_tcp_on_allowed_port_quant(port: u16) -> bool
    {
        exists|i:int| 0 <= i && i <= Self::TCP_ALLOWED_PORTS().len() - 1 && Self::TCP_ALLOWED_PORTS()[i] == port
    }

    // BEGIN MARKER GUMBO METHODS
    pub open spec fn TCP_ALLOWED_PORTS() -> SW::u16Array
    {
      [5760u16]
    }

    pub open spec fn UDP_ALLOWED_PORTS() -> SW::u16Array
    {
      [68u16]
    }

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

    pub open spec fn valid_ipv4_length(frame: SW::RawEthernetMessage) -> bool
    {
      Self::two_bytes_to_u16(frame[16],frame[17]) <= 9000u16
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

    pub open spec fn ipv4_is_tcp(frame: SW::RawEthernetMessage) -> bool
    {
      frame[23] == 6u8
    }

    pub open spec fn ipv4_is_udp(frame: SW::RawEthernetMessage) -> bool
    {
      frame[23] == 17u8
    }

    pub open spec fn tcp_is_valid_port(frame: SW::RawEthernetMessage) -> bool
    {
      Self::two_bytes_to_u16(frame[36],frame[37]) == Self::TCP_ALLOWED_PORTS()[0]
    }

    pub open spec fn udp_is_valid_port(frame: SW::RawEthernetMessage) -> bool
    {
      Self::two_bytes_to_u16(frame[36],frame[37]) == Self::UDP_ALLOWED_PORTS()[0]
    }

    pub open spec fn udp_is_mavlink_port(frame: SW::RawEthernetMessage) -> bool
    {
      Self::two_bytes_to_u16(frame[36],frame[37]) == 14550
    }

    pub open spec fn frame_has_ipv4_tcp_on_allowed_port_quant(frame: SW::RawEthernetMessage) -> bool
    {
      exists|i:int| 0 <= i < Self::TCP_ALLOWED_PORTS().len() && Self::TCP_ALLOWED_PORTS()[i] == Self::two_bytes_to_u16(frame[36],frame[37])
    }

    pub open spec fn frame_has_ipv4_udp_on_allowed_port_quant(frame: SW::RawEthernetMessage) -> bool
    {
      exists|i:int| 0 <= i < Self::UDP_ALLOWED_PORTS().len() && Self::UDP_ALLOWED_PORTS()[i] == Self::two_bytes_to_u16(frame[36],frame[37])
    }

    pub open spec fn valid_arp(frame: SW::RawEthernetMessage) -> bool
    {
      Self::frame_is_wellformed_eth2(frame) && Self::frame_has_arp(frame) &&
        Self::wellformed_arp_frame(frame)
    }

    pub open spec fn valid_ipv4_tcp(frame: SW::RawEthernetMessage) -> bool
    {
      Self::frame_is_wellformed_eth2(frame) && Self::frame_has_ipv4(frame) &&
        Self::wellformed_ipv4_frame(frame) &&
        Self::ipv4_is_tcp(frame)
    }

    pub open spec fn valid_ipv4_udp(frame: SW::RawEthernetMessage) -> bool
    {
      Self::frame_is_wellformed_eth2(frame) && Self::frame_has_ipv4(frame) &&
        Self::wellformed_ipv4_frame(frame) &&
        Self::ipv4_is_udp(frame)
    }

    pub open spec fn valid_ipv4_tcp_port(frame: SW::RawEthernetMessage) -> bool
    {
      Self::valid_ipv4_tcp(frame) && Self::frame_has_ipv4_tcp_on_allowed_port_quant(frame)
    }

    pub open spec fn valid_ipv4_udp_port(frame: SW::RawEthernetMessage) -> bool
    {
      Self::valid_ipv4_udp(frame) && Self::frame_has_ipv4_udp_on_allowed_port_quant(frame)
    }

    pub open spec fn valid_ipv4_udp_mavlink(frame: SW::RawEthernetMessage) -> bool
    {
      Self::valid_ipv4_udp(frame) && Self::udp_is_mavlink_port(frame)
    }

    pub open spec fn allow_outbound_frame(frame: SW::RawEthernetMessage) -> bool
    {
      Self::valid_arp(frame) || Self::valid_ipv4_udp_mavlink(frame) ||
        Self::valid_ipv4_udp_port(frame)
    }

    // pub open spec fn input_eq_mav_output(
    //   frame: SW::RawEthernetMessage,
    //   output: SW::UdpFrame_Impl) -> bool
    // {
    //   frame == output
    // }
    // END MARKER GUMBO METHODS
  }

}

#[test]
fn tcp_port_allowed_test() {
    assert!(tcp_port_allowed(5760));
    assert!(!tcp_port_allowed(42));
}

#[test]
fn udp_port_allowed_test() {
    assert!(udp_port_allowed(68));
    assert!(!udp_port_allowed(19));
}

#[cfg(test)]
mod parse_frame_tests {
    use super::*;

    #[test]
    fn parse_malformed_packet() {
        let mut frame = [0u8; 1600];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x02, 0xC2,
        ];
        frame[0..14].copy_from_slice(&pkt);
        let res = seL4_RxFirewall_RxFirewall::get_frame_packet(&frame);
        assert!(res.is_none());
    }

    #[test]
    fn parse_valid_arp() {
        let mut frame = [0u8; 1600];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x06, 0x0,
            0x1, 0x8, 0x0, 0x6, 0x4, 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0xc0, 0xa8, 0x0, 0x1,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xc0, 0xa8, 0x0, 0xce,
        ];
        frame[0..42].copy_from_slice(&pkt);
        let res = seL4_RxFirewall_RxFirewall::get_frame_packet(&frame);
        assert!(res.is_some());
    }
}

#[cfg(test)]
mod can_send_tests {
    use super::*;
    use firewall_core::{
        Address, Arp, ArpOp, EtherType, HardwareType, IpProtocol, Ipv4Address, Ipv4Packet, Ipv4Repr,
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
        assert!(can_send_to_vmm(&packet));
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
        assert!(can_send_to_vmm(&packet));
    }

    #[test]
    fn packet_invalid_ipv6() {
        let packet = PacketType::Ipv6;
        assert!(!can_send_to_vmm(&packet));
    }

    #[test]
    fn invalid_ipv4_protocols() {
        // Hop by Hop
        let mut packet = PacketType::Ipv4(Ipv4Packet {
            header: Ipv4Repr {
                protocol: IpProtocol::HopByHop,
                length: 0x29,
            },
            protocol: Ipv4ProtoPacket::HopByHop,
        });
        assert!(!can_send_to_vmm(&packet));

        // ICMP
        if let PacketType::Ipv4(ip) = &mut packet {
            ip.header.protocol = IpProtocol::Icmp;
        }
        assert!(!can_send_to_vmm(&packet));

        // IGMP
        if let PacketType::Ipv4(ip) = &mut packet {
            ip.header.protocol = IpProtocol::Igmp;
        }
        assert!(!can_send_to_vmm(&packet));

        // Ipv6 Route
        if let PacketType::Ipv4(ip) = &mut packet {
            ip.header.protocol = IpProtocol::Ipv6Route;
        }
        assert!(!can_send_to_vmm(&packet));

        // Ipv6 Frag
        if let PacketType::Ipv4(ip) = &mut packet {
            ip.header.protocol = IpProtocol::Ipv6Frag;
        }
        assert!(!can_send_to_vmm(&packet));

        // ICMPv6
        if let PacketType::Ipv4(ip) = &mut packet {
            ip.header.protocol = IpProtocol::Icmpv6;
        }
        assert!(!can_send_to_vmm(&packet));

        // IPv6 No Nxt
        if let PacketType::Ipv4(ip) = &mut packet {
            ip.header.protocol = IpProtocol::Ipv6NoNxt;
        }
        assert!(!can_send_to_vmm(&packet));

        // IPv6 Opts
        if let PacketType::Ipv4(ip) = &mut packet {
            ip.header.protocol = IpProtocol::Ipv6Opts;
        }
        assert!(!can_send_to_vmm(&packet));
    }

    #[test]
    fn disallowed_tcp() {
        let packet = PacketType::Ipv4(Ipv4Packet {
            header: Ipv4Repr {
                protocol: IpProtocol::Tcp,
                length: 0x29,
            },
            protocol: Ipv4ProtoPacket::Tcp(TcpRepr { dst_port: 443 }),
        });
        assert!(!can_send_to_vmm(&packet));
    }

    #[test]
    fn allowed_tcp() {
        let packet = PacketType::Ipv4(Ipv4Packet {
            header: Ipv4Repr {
                protocol: IpProtocol::Tcp,
                length: 0x29,
            },
            protocol: Ipv4ProtoPacket::Tcp(TcpRepr { dst_port: 5760 }),
        });
        assert!(can_send_to_vmm(&packet));
    }

    #[test]
    fn disallowed_udp() {
        let packet = PacketType::Ipv4(Ipv4Packet {
            header: Ipv4Repr {
                protocol: IpProtocol::Udp,
                length: 0x29,
            },
            protocol: Ipv4ProtoPacket::Udp(UdpRepr { dst_port: 15 }),
        });
        assert!(!can_send_to_vmm(&packet));
    }

    #[test]
    fn allowed_udp() {
        let packet = PacketType::Ipv4(Ipv4Packet {
            header: Ipv4Repr {
                protocol: IpProtocol::Udp,
                length: 0x29,
            },
            protocol: Ipv4ProtoPacket::Udp(UdpRepr { dst_port: 68 }),
        });
        assert!(can_send_to_vmm(&packet));
    }
}

// #[cfg(test)]
// mod rx_ethernet_frames_tests {
//     use super::*;

//     mod get_in {
//         use super::*;

//         #[test]
//         fn valid() {
//             let mut rx_buf: SW::RawEthernetMessage = [0; SW::SW_RawEthernetMessage_DIM_0];

//             let res = eth_get(0, &mut rx_buf);
//             assert!(res);
//             let res = eth_get(1, &mut rx_buf);
//             assert!(res);
//             let res = eth_get(2, &mut rx_buf);
//             assert!(res);
//             let res = eth_get(3, &mut rx_buf);
//             assert!(res);
//             let res = eth_get(0, &mut rx_buf);
//             assert!(res);
//             let res = eth_get(1, &mut rx_buf);
//             assert!(res);
//         }

//         #[test]
//         #[should_panic]
//         fn out_of_bounds() {
//             let mut rx_buf: SW::RawEthernetMessage = [0; SW::SW_RawEthernetMessage_DIM_0];

//             let _ = eth_get(4, &mut rx_buf);
//         }
//     }

//     mod put_out {
//         use super::*;

//         #[test]
//         fn valid() {
//             let mut state = State::new();
//             let mut rx_buf: SW::RawEthernetMessage = [0; SW::SW_RawEthernetMessage_DIM_0];

//             assert_eq!(state.idx, 0);
//             eth_put(&mut state, &mut rx_buf);
//             assert_eq!(state.idx, 1);
//             eth_put(&mut state, &mut rx_buf);
//             assert_eq!(state.idx, 2);
//             eth_put(&mut state, &mut rx_buf);
//             assert_eq!(state.idx, 3);
//             eth_put(&mut state, &mut rx_buf);
//             assert_eq!(state.idx, 0);
//             eth_put(&mut state, &mut rx_buf);
//             assert_eq!(state.idx, 1);
//         }

//         #[test]
//         #[should_panic]
//         fn out_of_bounds() {
//             let mut state = State::new();
//             state.idx = 4;
//             let mut rx_buf: SW::RawEthernetMessage = [0; SW::SW_RawEthernetMessage_DIM_0];

//             eth_put(&mut state, &mut rx_buf);
//         }
//     }
// }

// #[cfg(test)]
// mod bindings {
//     use super::*;
//     pub fn get_EthernetFramesRxIn0(_value: *mut SW::RawEthernetMessage) -> bool {
//         true
//     }
//     pub fn get_EthernetFramesRxIn1(_value: *mut SW::RawEthernetMessage) -> bool {
//         true
//     }

//     pub fn get_EthernetFramesRxIn2(_value: *mut SW::RawEthernetMessage) -> bool {
//         true
//     }

//     pub fn get_EthernetFramesRxIn3(_value: *mut SW::RawEthernetMessage) -> bool {
//         true
//     }
//     pub fn EthernetFramesRxIn0_is_empty() -> bool {
//         false
//     }
//     pub fn EthernetFramesRxIn1_is_empty() -> bool {
//         false
//     }
//     pub fn EthernetFramesRxIn2_is_empty() -> bool {
//         false
//     }
//     pub fn EthernetFramesRxIn3_is_empty() -> bool {
//         false
//     }
//     pub fn put_EthernetFramesRxOut0(_value: *mut SW::RawEthernetMessage) -> bool {
//         true
//     }
//     pub fn put_EthernetFramesRxOut1(_value: *mut SW::RawEthernetMessage) -> bool {
//         true
//     }
//     pub fn put_EthernetFramesRxOut2(_value: *mut SW::RawEthernetMessage) -> bool {
//         true
//     }
//     pub fn put_EthernetFramesRxOut3(_value: *mut SW::RawEthernetMessage) -> bool {
//         true
//     }
//     pub struct EthChannelGet {
//         pub get: fn(*mut SW::RawEthernetMessage) -> bool,
//         pub empty: fn() -> bool,
//     }
//     pub struct EthChannelPut {
//         pub put: fn(*mut SW::RawEthernetMessage) -> bool,
//     }
// }
