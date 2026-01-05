#![cfg(test)]

// This file will not be overwritten if codegen is rerun

mod tests {
    // NOTE: need to run tests sequentially to prevent race conditions
    //       on the app and the testing apis which are static
    use serial_test::serial;

    use crate::bridge::test_api;
    use data::*;

    #[test]
    #[serial]
    fn test_initialization() {
        crate::seL4_RxFirewall_RxFirewall_initialize();
    }

    #[test]
    #[serial]
    fn test_compute() {
        crate::seL4_RxFirewall_RxFirewall_initialize();
        crate::seL4_RxFirewall_RxFirewall_timeTriggered();
    }
}

mod GUMBOX_tests {
    use data::SW;
    use proptest::prelude::*;
    use serial_test::serial;

    use crate::bridge::test_api;
    use crate::testComputeCB_macro;
    use crate::testComputeCBwLV_macro;
    use crate::testInitializeCB_macro;

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
      api_EthernetFramesRxIn0: test_api::option_strategy_default(SW_RawEthernetMessage_strategy_default()),
      api_EthernetFramesRxIn1: test_api::option_strategy_default(SW_RawEthernetMessage_strategy_default()),
      api_EthernetFramesRxIn2: test_api::option_strategy_default(SW_RawEthernetMessage_strategy_default()),
      api_EthernetFramesRxIn3: test_api::option_strategy_default(SW_RawEthernetMessage_strategy_default())
    }

    // testComputeCBwLV_macro! {
    //   prop_testComputeCBwLV_macro, // test name
    //   config: ProptestConfig { // proptest configuration, built by overriding fields from default config
    //     cases: numValidComputeTestCases,
    //     max_global_rejects: numValidComputeTestCases * computeRejectRatio,
    //     verbose: verbosity,
    //     ..ProptestConfig::default()
    //   },
    //   // strategies for generating each component input
    //   api_EthernetFramesRxIn0: test_api::option_strategy_default(SW_RawEthernetMessage_strategy_default()),
    //   api_EthernetFramesRxIn1: test_api::option_strategy_default(SW_RawEthernetMessage_strategy_default()),
    //   api_EthernetFramesRxIn2: test_api::option_strategy_default(SW_RawEthernetMessage_strategy_default()),
    //   api_EthernetFramesRxIn3: test_api::option_strategy_default(SW_RawEthernetMessage_strategy_default())
    // }

    pub fn SW_RawEthernetMessage_strategy_default() -> impl Strategy<Value = SW::RawEthernetMessage>
    {
        SW_RawEthernetMessage_stategy_cust(any::<u8>())
    }

    fn byte1(val: u16) -> u8 {
        (val >> 8) as u8
    }

    fn byte2(val: u16) -> u8 {
        (val & 0xFF) as u8
    }

    fn copy_u16(v: &mut [u8], data: u16) {
        v[0] = byte1(data);
        v[1] = byte2(data);
    }

    fn ethertype_strategy() -> impl Strategy<Value = u16> {
        prop_oneof![
          20 => Just(0x0800), // IPv4
          10 => Just(0x0806), // ARP
          2 => Just(0x86DD),  // IPv6
          1 => any::<u16>(),
        ]
    }

    fn arp_ethertype_strategy() -> impl Strategy<Value = u16> {
        prop_oneof![
          20 => Just(0x0800),
          2 => Just(0x0806),
          20 => Just(0x86DD),
          1 => any::<u16>(),
        ]
    }

    fn arp_hwtype_strategy() -> impl Strategy<Value = u16> {
        prop_oneof![
          40 => Just(0x0001),
          1 => any::<u16>(),
        ]
    }

    fn arp_op_strategy() -> impl Strategy<Value = u16> {
        prop_oneof![
          20 => Just(0x0001),
          20 => Just(0x0002),
          1 => any::<u16>(),
        ]
    }

    fn arp_strategy() -> impl Strategy<Value = Vec<u8>> {
        (
            arp_hwtype_strategy(),
            arp_ethertype_strategy(),
            arp_op_strategy(),
            proptest::collection::vec(any::<u8>(), 28),
        )
            .prop_map(|(hwtype, ethertype, op, mut v)| {
                copy_u16(&mut v[0..=1], hwtype);
                copy_u16(&mut v[2..=3], ethertype);
                copy_u16(&mut v[6..=7], op);
                v
            })
    }

    fn ipv4_protocol_strategy() -> impl Strategy<Value = u8> {
        prop_oneof![
           4 => Just(0x00), // HopByHop
           4 => Just(0x01), // Icmp
           4 => Just(0x02), // Igmp
           10 => Just(0x06), // Tcp
           10 => Just(0x11), // Udp
           4 => Just(0x2b), // Ipv6Route
           4 => Just(0x2c), // Ipv6Frag
           4 => Just(0x3a), // Icmpv6
           4 => Just(0x3b), // Ipv6NoNxt
           4 => Just(0x3c), // Ipv6Opts
           1 => any::<u8>(),
        ]
    }

    fn ipv4_length_strategy() -> impl Strategy<Value = u16> {
        prop_oneof![
          40 => (0u16..=9000),
          1 => (9001u16..),
        ]
    }

    fn dst_mac_strategy() -> impl Strategy<Value = Vec<u8>> {
        prop_oneof![
          50 => proptest::collection::vec(any::<u8>(), 6),
          1 => Just(vec![0,0,0,0,0,0]),
        ]
    }

    fn udp_port_strategy() -> impl Strategy<Value = u16> {
        prop_oneof![
          1 => Just(68),
          4 => any::<u16>(),
        ]
    }

    fn udp_strategy() -> impl Strategy<Value = Vec<u8>> {
        (
            udp_port_strategy(),
            proptest::collection::vec(any::<u8>(), 20),
        )
            .prop_map(|(port, mut v)| {
                copy_u16(&mut v[2..=3], port);
                v
            })
    }

    fn tcp_port_strategy() -> impl Strategy<Value = u16> {
        prop_oneof![
          1 => Just(5760),
          4 => any::<u16>(),
        ]
    }

    fn tcp_strategy() -> impl Strategy<Value = Vec<u8>> {
        (
            tcp_port_strategy(),
            proptest::collection::vec(any::<u8>(), 20),
        )
            .prop_map(|(port, mut v)| {
                copy_u16(&mut v[2..=3], port);
                v
            })
    }

    fn ipv4_strategy() -> impl Strategy<Value = Vec<u8>> {
        ipv4_protocol_strategy().prop_flat_map(|proto| {
            let proto_packet = match proto {
                // Tcp
                0x06 => tcp_strategy().boxed(),
                // Udp
                0x11 => udp_strategy().boxed(),
                _ => default_packet_strategy().boxed(),
            };
            (
                ipv4_length_strategy(),
                proto_packet,
                proptest::collection::vec(any::<u8>(), 40),
            )
                .prop_map(move |(length, proto_pack, mut v)| {
                    copy_u16(&mut v[2..=3], length);
                    v[9] = proto;
                    v.splice(20..20 + proto_pack.len(), proto_pack);
                    v
                })
        })
    }

    fn default_packet_strategy() -> impl Strategy<Value = Vec<u8>> {
        proptest::collection::vec(any::<u8>(), 1)
    }

    pub fn SW_RawEthernetMessage_stategy_cust<u8_strategy: Strategy<Value = u8> + Copy>(
        base_strategy: u8_strategy,
    ) -> impl Strategy<Value = SW::RawEthernetMessage> {
        ethertype_strategy().prop_flat_map(move |ethertype| {
            let packet = match ethertype {
                0x0800 => ipv4_strategy().boxed(),
                0x0806 => arp_strategy().boxed(),
                _ => default_packet_strategy().boxed(),
            };
            (
                dst_mac_strategy(),
                packet,
                proptest::collection::vec(base_strategy, SW::SW_RawEthernetMessage_DIM_0),
            )
                .prop_map(move |(dst_mac, pack, mut v)| {
                    v.splice(0..6, dst_mac);
                    copy_u16(&mut v[12..=13], ethertype);
                    v.splice(14..14 + pack.len(), pack);
                    let boxed: Box<[u8; SW::SW_RawEthernetMessage_DIM_0]> =
                        v.into_boxed_slice().try_into().unwrap();
                    *boxed
                })
        })
    }
}
