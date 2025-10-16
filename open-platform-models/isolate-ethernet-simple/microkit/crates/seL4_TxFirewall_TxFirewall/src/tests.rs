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
        crate::seL4_TxFirewall_TxFirewall_initialize();
    }

    #[test]
    #[serial]
    fn test_compute() {
        crate::seL4_TxFirewall_TxFirewall_initialize();
        crate::seL4_TxFirewall_TxFirewall_timeTriggered();
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
      api_EthernetFramesTxIn0: test_api::option_strategy_default(SW_RawEthernetMessage_strategy_default()),
      api_EthernetFramesTxIn1: test_api::option_strategy_default(SW_RawEthernetMessage_strategy_default()),
      api_EthernetFramesTxIn2: test_api::option_strategy_default(SW_RawEthernetMessage_strategy_default()),
      api_EthernetFramesTxIn3: test_api::option_strategy_default(SW_RawEthernetMessage_strategy_default())
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
    //   api_EthernetFramesTxIn0: test_api::option_strategy_default(SW_RawEthernetMessage_strategy_default()),
    //   api_EthernetFramesTxIn1: test_api::option_strategy_default(SW_RawEthernetMessage_strategy_default()),
    //   api_EthernetFramesTxIn2: test_api::option_strategy_default(SW_RawEthernetMessage_strategy_default()),
    //   api_EthernetFramesTxIn3: test_api::option_strategy_default(SW_RawEthernetMessage_strategy_default())
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
                v[0] = byte1(hwtype);
                v[1] = byte2(hwtype);
                v[2] = byte1(ethertype);
                v[3] = byte2(ethertype);
                v[6] = byte1(op);
                v[7] = byte2(op);
                v
            })
    }

    fn ipv4_protocol_strategy() -> impl Strategy<Value = u8> {
        prop_oneof![
           5 => Just(0x00), // HopByHop
           5 => Just(0x01), // Icmp
           5 => Just(0x02), // Igmp
           5 => Just(0x06), // Tcp
           5 => Just(0x11), // Udp
           5 => Just(0x2b), // Ipv6Route
           5 => Just(0x2c), // Ipv6Frag
           5 => Just(0x3a), // Icmpv6
           5 => Just(0x3b), // Ipv6NoNxt
           5 => Just(0x3c), // Ipv6Opts
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

    fn ipv4_strategy() -> impl Strategy<Value = Vec<u8>> {
        (
            ipv4_length_strategy(),
            ipv4_protocol_strategy(),
            proptest::collection::vec(any::<u8>(), 40),
        )
            .prop_map(|(length, proto, mut v)| {
                v[2] = byte1(length);
                v[3] = byte2(length);
                v[9] = proto;
                v
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
                    v.splice(0..6, dst_mac.iter().cloned());
                    v[12] = byte1(ethertype);
                    v[13] = byte2(ethertype);
                    v.splice(14..14 + pack.len(), pack.iter().cloned());
                    let boxed: Box<[u8; SW::SW_RawEthernetMessage_DIM_0]> =
                        v.into_boxed_slice().try_into().unwrap();
                    *boxed
                })
        })
    }

    pub fn SW_u16Array_strategy_default() -> impl Strategy<Value = SW::u16Array> {
        SW_u16Array_stategy_cust(any::<u16>())
    }

    pub fn SW_u16Array_stategy_cust<u16_strategy: Strategy<Value = u16>>(
        base_strategy: u16_strategy,
    ) -> impl Strategy<Value = SW::u16Array> {
        proptest::collection::vec(base_strategy, SW::SW_u16Array_DIM_0).prop_map(|v| {
            let boxed: Box<[u16; SW::SW_u16Array_DIM_0]> = v.into_boxed_slice().try_into().unwrap();
            *boxed
        })
    }

    pub fn SW_SizedEthernetMessage_Impl_strategy_default(
    ) -> impl Strategy<Value = SW::SizedEthernetMessage_Impl> {
        SW_SizedEthernetMessage_Impl_stategy_cust(
            SW_RawEthernetMessage_strategy_default(),
            any::<u16>(),
        )
    }

    pub fn SW_SizedEthernetMessage_Impl_stategy_cust<
        SW_RawEthernetMessage_strategy: Strategy<Value = SW::RawEthernetMessage>,
        u16_strategy: Strategy<Value = u16>,
    >(
        message_strategy: SW_RawEthernetMessage_strategy,
        sz_strategy: u16_strategy,
    ) -> impl Strategy<Value = SW::SizedEthernetMessage_Impl> {
        (message_strategy, sz_strategy)
            .prop_map(|(message, sz)| SW::SizedEthernetMessage_Impl { message, sz })
    }
}
