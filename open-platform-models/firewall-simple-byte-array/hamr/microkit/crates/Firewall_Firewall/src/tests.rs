#![cfg(test)]

// This file will not be overwritten if codegen is rerun

mod GUMBOX_tests {
  use serial_test::serial;
  use proptest::prelude::*;

  use crate::bridge::test_api as test_api;
  use crate::testInitializeCB_macro;
  use crate::testComputeCB_macro;
  use crate::testComputeCBwLV_macro;

  // number of valid (i.e., non-rejected) test cases that must be executed for the compute method.
  const numValidComputeTestCases: u32 = 100;

  // how many total test cases (valid + rejected) that may be attempted.
  //   0 means all inputs must satisfy the precondition (if present),
  //   5 means at most 5 rejected inputs are allowed per valid test case
  const computeRejectRatio: u32 = 5;

  const verbosity: u32 = 2;

  testInitializeCB_macro! {
    prop_testInitializeCB_macro, // test name
    config: ProptestConfig { // proptest configuration, built by overriding fields from default config
      cases: numValidComputeTestCases,
      max_global_rejects: numValidComputeTestCases * computeRejectRatio,
      verbose: verbosity,
      ..ProptestConfig::default()
    }
  }

  testComputeCB_macro! {
    prop_testComputeCB_macro, // test name
    config: ProptestConfig { // proptest configuration, built by overriding fields from default config
      cases: numValidComputeTestCases,
      max_global_rejects: numValidComputeTestCases * computeRejectRatio,
      verbose: verbosity,
      ..ProptestConfig::default()
    },
    // strategies for generating each component input
    api_EthernetFramesRxIn: test_api::option_strategy_default(test_api::SW_RawEthernetMessage_strategy_default()),
    api_EthernetFramesTxIn: test_api::option_strategy_default(test_api::SW_RawEthernetMessage_strategy_default())
  }

  testComputeCBwLV_macro! {
    prop_testComputeCBwLV_macro, // test name
    config: ProptestConfig { // proptest configuration, built by overriding fields from default config
      cases: numValidComputeTestCases,
      max_global_rejects: numValidComputeTestCases * computeRejectRatio,
      verbose: verbosity,
      ..ProptestConfig::default()
    },
    // strategies for generating each component input
    api_EthernetFramesRxIn: test_api::option_strategy_default(test_api::SW_RawEthernetMessage_strategy_default()),
    api_EthernetFramesTxIn: test_api::option_strategy_default(test_api::SW_RawEthernetMessage_strategy_default())
  }
}



mod GUMBOX_tests_ORIG {
  use serial_test::serial;

  use crate::bridge::test_api;
  //use data::*;
  use data::*;
  use crate::bridge::Firewall_Firewall_GUMBOX::*;
  
  fn validIpv4() -> SW::RawEthernetMessage {
    let mut frame = [0u8; 1600];
    
    // Hop by Hop
    let pkt = [
      0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00, 0x45,
      0x0, 0x0, 0x29, 0x15, 0x5f, 0x40, 0x0, 0x06, 0x0, 0xf7, 0x28, 0xc0, 0xa8, 0x0, 0xce,
      0x34, 0x7f, 0xf8, 0x51, 0xc4, 0x16, 0x80, 0xbb, 0x21, 0x65, 0x90, 0xfb, 0xe4, 0x98,
      0x7c, 0x9d, 0x50, 0x10, 0x3, 0xff, 0xe3, 0xc7, 0x0, 0x0,
    ];

    frame[0..54].copy_from_slice(&pkt);

    return frame
  }

  #[test]
  #[serial]
  fn sendValidIpv4_RxIn_to_RxOut() {
    // initialize the app
    crate::Firewall_Firewall_initialize();

    let api_EthernetFramesRxIn = Some(validIpv4());
    let api_EthernetFramesTxIn = None;

    // [PutInPorts]: put values on the input ports
    test_api::put_EthernetFramesRxIn(api_EthernetFramesRxIn);
    test_api::put_EthernetFramesTxIn(api_EthernetFramesTxIn);

    // [InvokeEntryPoint]: invoke the entry point test method
    crate::Firewall_Firewall_timeTriggered();

    // [RetrieveOutState]: retrieve values of the output ports via get operations and GUMBO declared local state variable
    let api_EthernetFramesRxOut = test_api::get_EthernetFramesRxOut();
    let api_EthernetFramesTxOut = test_api::get_EthernetFramesTxOut();

    // [CheckPost]: invoke the oracle function
    assert!(compute_CEP_Post(
      api_EthernetFramesRxIn, api_EthernetFramesTxIn,
      api_EthernetFramesRxOut, api_EthernetFramesTxOut));
  }

  #[test]
  #[serial]
  fn sendValidIpv4_TxIn_to_TxOut() {
    
    // initialize the app
    crate::Firewall_Firewall_initialize();

    let api_EthernetFramesRxIn = None;
    let api_EthernetFramesTxIn = Some(validIpv4());

    // [PutInPorts]: put values on the input ports
    test_api::put_EthernetFramesRxIn(api_EthernetFramesRxIn);
    test_api::put_EthernetFramesTxIn(api_EthernetFramesTxIn);

    // [InvokeEntryPoint]: invoke the entry point test method
    crate::Firewall_Firewall_timeTriggered();

    // [RetrieveOutState]: retrieve values of the output ports via get operations and GUMBO declared local state variable
    let api_EthernetFramesRxOut = test_api::get_EthernetFramesRxOut();
    let api_EthernetFramesTxOut = test_api::get_EthernetFramesTxOut();

    // [CheckPost]: invoke the oracle function
    assert!(compute_CEP_Post(
      api_EthernetFramesRxIn, api_EthernetFramesTxIn,
      api_EthernetFramesRxOut, api_EthernetFramesTxOut));
  }

  #[test]
  #[serial]
  fn sendValidIpv4_TxIn_to_RxOut_VIOLATE_POST() {

    // initialize the app
    crate::Firewall_Firewall_initialize();

    let api_EthernetFramesRxIn = None;
    let api_EthernetFramesTxIn = Some(validIpv4());

    // [PutInPorts]: put values on the input ports
    test_api::put_EthernetFramesRxIn(api_EthernetFramesRxIn);
    test_api::put_EthernetFramesTxIn(api_EthernetFramesTxIn);

    // [InvokeEntryPoint]: invoke the entry point test method
    crate::Firewall_Firewall_timeTriggered();

    // [RetrieveOutState]: retrieve values of the output ports via get operations and GUMBO declared local state variable
    let api_EthernetFramesRxOut = test_api::get_EthernetFramesRxOut();
    let api_EthernetFramesTxOut = test_api::get_EthernetFramesTxOut();

    // [CheckPost]: invoke the oracle function (which should return false)
    assert!(!compute_CEP_Post(
      api_EthernetFramesRxIn, api_EthernetFramesTxIn,
      api_EthernetFramesTxOut, api_EthernetFramesTxOut));
  }
}


mod tests {
  // NOTE: need to run tests sequentially to prevent race conditions
  //       on the app and the testing apis which are static
  use serial_test::serial;

  //use data::*;
  use crate::bridge::Firewall_Firewall_GUMBOX::*;
  use data::*;
  #[test]
  #[serial]
  fn test_initialization() {
    unsafe {
      crate::Firewall_Firewall_initialize();
    }
  }

  #[test]
  #[serial]
  fn test_compute() {
    unsafe {
      crate::Firewall_Firewall_initialize();

      crate::Firewall_Firewall_timeTriggered();
    }    
  }
}



// Tests rewritten from those in the provided Rust firewall code.
// These are not 1:1 due to fundamental difference in directly
// examining bytes using predicates rather than parsing the bytes
// into structures implementing out-of-scope behavior.
mod rewritten_tests {
    // See firewall/core/src/lib.rs
    //fn dest_mac_empty() {}

    use crate::bridge::test_api;
    //use data::*;
    use data::*;
    use crate::bridge::Firewall_Firewall_GUMBOX::*;

    // Modified from firewall/core/src/lib.rs
    #[test]
    fn malformed_eth_header() {
        let mut frame = [0u8; 1600];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x02, 0xC2,
        ];
        frame[0..14].copy_from_slice(&pkt);
        // let res = EthFrame::parse(frame);
        // assert!(res.is_none());
        assert!(!frame_is_wellformed_eth2(frame));
    }

    // Not parsing IPV6 into a struct
    // See firewall/core/src/lib.rs
    // #[test]
    //fn eth_type_ipv6() {}

    // Modified from firewall/core/src/lib.rs
    #[test]
    fn valid_arp_request() {
        let mut frame = [0u8; 1600];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x06, 0x0,
            0x1, 0x8, 0x0, 0x6, 0x4, 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0xc0, 0xa8, 0x0, 0x1,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xc0, 0xa8, 0x0, 0xce,
        ];
        frame[0..42].copy_from_slice(&pkt);
        // let res = EthFrame::parse(frame);
        // assert_eq!(
        //     res,
        //     Some(EthFrame {
        //         header: EthernetRepr {
        //             src_addr: Address([0x2, 0x3, 0x4, 0x5, 0x6, 0x7]),
        //             dst_addr: Address([0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff]),
        //             ethertype: EtherType::Arp
        //         },
        //         eth_type: PacketType::Arp(Arp {
        //             htype: HardwareType::Ethernet,
        //             ptype: EtherType::Ipv4,
        //             hsize: 0x6,
        //             psize: 0x4,
        //             op: ArpOp::Request,
        //             src_addr: Address([0x2, 0x3, 0x4, 0x5, 0x6, 0x7]),
        //             src_protocol_addr: Ipv4Address([0xc0, 0xa8, 0x00, 0x01]),
        //             dest_addr: Address([0x00, 0x00, 0x00, 0x00, 0x00, 0x00]),
        //             dest_protocol_addr: Ipv4Address([0xc0, 0xa8, 0x0, 0xce])
        //         })
        //     })
        // );
        assert!(frame_has_arp(frame));
    }

    // Modified from firewall/core/src/lib.rs
    #[test]
    fn valid_arp_reply() {
        let mut frame = [0u8; 1600];
        let pkt = [
            0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x18, 0x20, 0x22, 0x24, 0x26, 0x28, 0x8, 0x6, 0x0, 0x1,
            0x8, 0x0, 0x6, 0x4, 0x0, 0x2, 0x18, 0x20, 0x22, 0x24, 0x26, 0x28, 0xc0, 0xa8, 0x0,
            0xce, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0xc0, 0xa8, 0x0, 0x1,
        ];
        frame[0..42].copy_from_slice(&pkt);
        // let res = EthFrame::parse(frame);
        // assert_eq!(
        //     res,
        //     Some(EthFrame {
        //         header: EthernetRepr {
        //             src_addr: Address([0x18, 0x20, 0x22, 0x24, 0x26, 0x28]),
        //             dst_addr: Address([0x2, 0x3, 0x4, 0x5, 0x6, 0x7]),
        //             ethertype: EtherType::Arp
        //         },
        //         eth_type: PacketType::Arp(Arp {
        //             htype: HardwareType::Ethernet,
        //             ptype: EtherType::Ipv4,
        //             hsize: 0x6,
        //             psize: 0x4,
        //             op: ArpOp::Reply,
        //             src_addr: Address([0x18, 0x20, 0x22, 0x24, 0x26, 0x28]),
        //             src_protocol_addr: Ipv4Address([0xc0, 0xa8, 0x00, 0xce]),
        //             dest_addr: Address([0x2, 0x3, 0x4, 0x5, 0x6, 0x7]),
        //             dest_protocol_addr: Ipv4Address([0xc0, 0xa8, 0x0, 0x01])
        //         })
        //     })
        // );
        assert!(frame_has_arp(frame));
    }

    // HLRs do not include wellformedness of ARP, just that ARP is passed through
    // See firewall/core/src/lib.rs
    // #[test]
    // fn malformed_arp() {}

    // Modified from firewall/core/src/lib.rs
    #[test]
    fn malformed_ipv4() {
        let mut frame = [0u8; 1600];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00, 0x45,
            0x0, 0x0, 0x29, 0x15, 0x5f, 0x40, 0x0, 0x80, 0x7, 0xf7, 0x28, 0xc0, 0xa8, 0x0, 0xce,
            0x34, 0x7f, 0xf8, 0x51,
        ];
        frame[0..34].copy_from_slice(&pkt);
        // let res = EthFrame::parse(frame);
        // assert!(res.is_none());
        assert!(frame_has_ipv4(frame));
    }

    // Modified from firewall/core/src/lib.rs
    #[test]
    fn valid_ipv4_protocols() {
        let mut frame = [0u8; 1600];
        // Hop by Hop
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00, 0x45,
            0x0, 0x0, 0x29, 0x15, 0x5f, 0x40, 0x0, 0x80, 0x0, 0xf7, 0x28, 0xc0, 0xa8, 0x0, 0xce,
            0x34, 0x7f, 0xf8, 0x51, 0xc4, 0x73, 0x1, 0xbb, 0x21, 0x65, 0x90, 0xfb, 0xe4, 0x98,
            0x7c, 0x9d, 0x50, 0x10, 0x3, 0xff, 0xe3, 0xc7, 0x0, 0x0,
        ];
        frame[0..54].copy_from_slice(&pkt);

        // let mut expected = EthFrame {
        //     header: EthernetRepr {
        //         src_addr: Address([0x2, 0x3, 0x4, 0x5, 0x6, 0x7]),
        //         dst_addr: Address([0xff, 0xff, 0xff, 0xff, 0xff, 0xff]),
        //         ethertype: EtherType::Ipv4,
        //     },
        //     eth_type: PacketType::Ipv4(Ipv4Packet {
        //         header: Ipv4Repr {
        //             protocol: IpProtocol::HopByHop,
        //             length: 0x29,
        //         },
        //         protocol: Ipv4ProtoPacket::TxOnly,
        //     }),
        // };

        // let res = EthFrame::parse(frame);
        // assert_eq!(res.as_ref(), Some(&expected));
        assert!(frame_is_wellformed_eth2(frame));
        assert!(frame_has_ipv4(frame));

        // ICMP
        frame[23] = 0x01;
        // if let PacketType::Ipv4(pack) = &mut expected.eth_type {
        //     pack.header.protocol = IpProtocol::Icmp;
        // }
        // let res = EthFrame::parse(frame);
        // assert_eq!(res.as_ref(), Some(&expected));
        assert!(frame_is_wellformed_eth2(frame));
        assert!(frame_has_ipv4(frame));

        // IGMP
        frame[23] = 0x02;
        // if let PacketType::Ipv4(pack) = &mut expected.eth_type {
        //     pack.header.protocol = IpProtocol::Igmp;
        // }
        // let res = EthFrame::parse(frame);
        // assert_eq!(res.as_ref(), Some(&expected));
        assert!(frame_is_wellformed_eth2(frame));
        assert!(frame_has_ipv4(frame));

        // Ipv6 Route
        frame[23] = 0x2b;
        // if let PacketType::Ipv4(pack) = &mut expected.eth_type {
        //     pack.header.protocol = IpProtocol::Ipv6Route;
        // }
        // let res = EthFrame::parse(frame);
        // assert_eq!(res.as_ref(), Some(&expected));
        assert!(frame_is_wellformed_eth2(frame));
        assert!(frame_has_ipv4(frame));

        // Ipv6 Frag
        frame[23] = 0x2c;
        // if let PacketType::Ipv4(pack) = &mut expected.eth_type {
        //     pack.header.protocol = IpProtocol::Ipv6Frag;
        // }
        // let res = EthFrame::parse(frame);
        // assert_eq!(res.as_ref(), Some(&expected));
        assert!(frame_is_wellformed_eth2(frame));
        assert!(frame_has_ipv4(frame));

        // ICMPv6
        frame[23] = 0x3a;
        // if let PacketType::Ipv4(pack) = &mut expected.eth_type {
        //     pack.header.protocol = IpProtocol::Icmpv6;
        // }
        // let res = EthFrame::parse(frame);
        // assert_eq!(res.as_ref(), Some(&expected));
        assert!(frame_is_wellformed_eth2(frame));
        assert!(frame_has_ipv4(frame));

        // IPv6 No Nxt
        frame[23] = 0x3b;
        // if let PacketType::Ipv4(pack) = &mut expected.eth_type {
        //     pack.header.protocol = IpProtocol::Ipv6NoNxt;
        // }
        // let res = EthFrame::parse(frame);
        // assert_eq!(res.as_ref(), Some(&expected));
        assert!(frame_is_wellformed_eth2(frame));
        assert!(frame_has_ipv4(frame));

        // IPv6 Opts
        frame[23] = 0x3c;
        // if let PacketType::Ipv4(pack) = &mut expected.eth_type {
        //     pack.header.protocol = IpProtocol::Ipv6Opts;
        // }
        // let res = EthFrame::parse(frame);
        // assert_eq!(res.as_ref(), Some(&expected));
        assert!(frame_is_wellformed_eth2(frame));
        assert!(frame_has_ipv4(frame));

        // TCP
        frame[23] = 0x06;
        // if let PacketType::Ipv4(pack) = &mut expected.eth_type {
        //     pack.header.protocol = IpProtocol::Tcp;
        //     pack.protocol = Ipv4ProtoPacket::Tcp(TcpRepr { dst_port: 443 });
        // }
        // let res = EthFrame::parse(frame);
        // assert_eq!(res.as_ref(), Some(&expected));
        assert!(frame_is_wellformed_eth2(frame));
        assert!(frame_has_ipv4(frame));
        assert!(frame_has_ipv4_tcp(frame));

        // UDP
        frame[23] = 0x11;
        // if let PacketType::Ipv4(pack) = &mut expected.eth_type {
        //     pack.header.protocol = IpProtocol::Udp;
        //     pack.protocol = Ipv4ProtoPacket::Udp(UdpRepr { dst_port: 443 });
        // }
        // let res = EthFrame::parse(frame);
        // assert_eq!(res.as_ref(), Some(&expected));
        assert!(frame_is_wellformed_eth2(frame));
        assert!(frame_has_ipv4(frame));
        assert!(frame_has_ipv4_udp(frame));
    }
}

// Test that packets are correctly allowed/disallowed by the high level specification RX
mod high_level_specification_rx {
        
    use crate::bridge::test_api;
    use data::*;
    use crate::bridge::Firewall_Firewall_GUMBOX::*;

    // Test HLS for malformed Ethernet II header
    #[test]
    fn malformed() {
        let mut frame = [0u8; 1600];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x02, 0xC2,
        ];
        frame[0..14].copy_from_slice(&pkt);
        
        assert!(should_allow_inbound_frame_rx(frame, false));
    }

    // Test HLS for various IPv4 frames
    #[test]
    fn ipv4() {
        let mut frame = [0u8; 1600];
        // Hop by Hop
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00, 0x45,
            0x0, 0x0, 0x29, 0x15, 0x5f, 0x40, 0x0, 0x80, 0x0, 0xf7, 0x28, 0xc0, 0xa8, 0x0, 0xce,
            0x34, 0x7f, 0xf8, 0x51, 0xc4, 0x73, 0x1, 0xbb, 0x21, 0x65, 0x90, 0xfb, 0xe4, 0x98,
            0x7c, 0x9d, 0x50, 0x10, 0x3, 0xff, 0xe3, 0xc7, 0x0, 0x0,
        ];
        frame[0..54].copy_from_slice(&pkt);
        

        // TCP
        frame[23] = 0x06;
        // Drop due to wrong port
        assert!(should_allow_inbound_frame_rx(frame, false));

        // Correct the port to 5760
        frame[36] = 0x16;
        frame[37] = 0x80;
        assert!(should_allow_inbound_frame_rx(frame, true));

        // UDP
        frame[23] = 0x11;
        // Drop due to wrong port
        assert!(should_allow_inbound_frame_rx(frame, false));

        // Correct the port to 68
        frame[36] = 0x00;
        frame[37] = 0x44;
        assert!(should_allow_inbound_frame_rx(frame, true));
        
        // IGMP
        frame[23] = 0x02;
        assert!(should_allow_inbound_frame_rx(frame, false));
    }

    // Test HLS for IPv6
    #[test]
    fn ipv6() {
        let mut frame = [0u8; 1600];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x86, 0xDD,
        ];
        frame[0..14].copy_from_slice(&pkt);
        assert!(should_allow_inbound_frame_rx(frame, false));
    }

    // Test HLS for ARP
    #[test]
    fn arp() {
        let mut frame = [0u8; 1600];
        let pkt = [
            0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x18, 0x20, 0x22, 0x24, 0x26, 0x28, 0x8, 0x6, 0x0, 0x1,
            0x8, 0x0, 0x6, 0x4, 0x0, 0x2, 0x18, 0x20, 0x22, 0x24, 0x26, 0x28, 0xc0, 0xa8, 0x0,
            0xce, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0xc0, 0xa8, 0x0, 0x1,
        ];
        frame[0..42].copy_from_slice(&pkt);
        assert!(should_allow_inbound_frame_rx(frame, true));
    }

}
