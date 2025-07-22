// See Readme.md for purpose of this code.
//
// Original author: Jacob Bengel (KSU CS 855 class project exploring
// verification of Dornerworks firewall component)
//
// Commentary and documentation added by John Hatcliff

#![allow(unused_imports)]

use vstd::prelude::*;

verus!{

//------------------------------------------
// Representation of firewall white lists
//------------------------------------------
// 
// Define constant arrays to represent firewall white lists.
// Note: as a simplifying assumption --  to overcome pain points related
// to existential quantification in Verus specs and proofs --
// white lists are assumed to be of size 1.
//
// ToDo: lift the restriction that white lists are size 1.
//
// -- White list data --
//
pub const TCP_ALLOWED_PORTS: [u16; 1] = [5760u16];
pub const UDP_ALLOWED_PORTS: [u16; 1] = [68u16];

// -- White list length --
//
// These constants are needed due to either a Rust or Verus limitation
// with using the len() function on a constant array at runtime.
pub const TCP_ALLOWED_PORTS_LENGTH: usize = 1;
pub const UDP_ALLOWED_PORTS_LENGTH: usize = 1;

//------------------------------------------
// Define bounds on ETH2 frames
//------------------------------------------

pub const MIN_ETH2_FRAME_LENGTH: usize = 64;
pub const MAX_ETH2_FRAME_LENGTH: usize = 1518;

//------------------------
// Small experiments with Verus
//------------------------ 
exec fn bytes_to_u16_experiments() {
    // Verus uses an assert() function; not Rust's assert!() macro
    assert(1 == 1);
    assert(0xABCD == (0xAB * 256) + 0xCD);
    assert(0xABCD == two_bytes_to_u16(0xAB, 0xCD));
}

#[verifier::external_body] // Tell Verus not to verify
exec fn echo(string: &str) {
    println!("{0}", string); // Print statements (or macros) can not be verified
}

//-------------------
//
//-------------------

// Verus doesn't easily support bitwise operators appearing in the
// original firewall.  As a simplification, express conversion from
// bytes to u16 using integer arithmetic.

spec fn two_bytes_to_u16(byte0: u8, byte1: u8) -> u16 {
    ((byte0 as u16) * 256 + (byte1 as u16)) as u16
}

spec fn frame_is_wellformed_eth2(frame: &[u8]) -> bool {
    if !(MIN_ETH2_FRAME_LENGTH <= frame.len() <= MAX_ETH2_FRAME_LENGTH) {
        false
    } else if !(frame[12] >= 0x06 && frame[13] >= 0x00) {
        false
    } else {
        true
    }
}

exec fn compute_frame_is_wellformed_eth2(frame: &[u8]) -> (result: bool)
    ensures
        frame_is_wellformed_eth2(frame) <==> result == true,
{
    // Check that length fits min/max for valid ethernet II
    if !(frame.len() >= MIN_ETH2_FRAME_LENGTH &&
         frame.len() <= MAX_ETH2_FRAME_LENGTH) {
        return false;
    }
    
    // Ethertype geq 0x0600 implies ethernet II
    //           leq 0x05DC implies 802.3
    //           else       undefined
    
    // Simple EtherType check
    // second expression of AND is redundant here
    if !(frame[12] >= 0x06 && frame[13] >= 0x00) {
        // Incorrect EtherType
        return false;
    }
    
    // No checks fail
    // Is wellformed Ethernet II
    return true;
}

spec fn frame_has_arp(frame: &[u8]) -> bool
    recommends
        frame_is_wellformed_eth2(frame),
{
    if !(frame[12] == 0x08 && frame[13] == 0x06) {
        false
    } else {
        true
    }
}

exec fn compute_frame_has_arp(frame: &[u8]) -> (result: bool)
    requires
        frame_is_wellformed_eth2(frame),
    ensures
        frame_has_arp(frame) <==> result == true,
{
    // EtherType eq 0x0806 implies ARP

    // Check for Eth2 EtherType octets equal to 0x0806 for ARP
    if !(frame[12] == 0x08 && frame[13] == 0x06) {
        // Not ARP
        return false;
    }

    // Has ARP
    return true;
}

spec fn frame_has_ipv4(frame: &[u8]) -> bool
    recommends
        frame_is_wellformed_eth2(frame),
{
    if !(frame[12] == 0x08 && frame[13] == 0x00) {
        false
    } else {
        true
    }
}

exec fn compute_frame_has_ipv4(frame: &[u8]) -> (result: bool)
    requires
        frame_is_wellformed_eth2(frame),
    ensures
        frame_has_ipv4(frame) <==> result == true,
{
    // Check for Eth2 EtherType octets equal to 0x0800 for IPv4
    if !(frame[12] == 0x08 && frame[13] == 0x00) {
        // Not IPv4
        return false;
    }

    // Has IPv4
    return true;
}

spec fn frame_has_ipv6(frame: &[u8]) -> bool
    recommends
        frame_is_wellformed_eth2(frame),
{
    if !(frame[12] == 0x86 && frame[13] == 0xDD) {
        false
    } else {
        true
    }
}

exec fn compute_frame_has_ipv6(frame: &[u8]) -> (result: bool)
    requires
        frame_is_wellformed_eth2(frame),
    ensures
        frame_has_ipv6(frame) <==> result == true,
{
    // Check for Eth2 EtherType octets equal to 0x86DD for IPv6
    if !(frame[12] == 0x86 && frame[13] == 0xDD) {
        // Not IPv6
        return false;
    }

    // Has IPv6
    return true;
}

spec fn frame_has_ipv4_tcp(frame: &[u8]) -> bool
    recommends
        frame_is_wellformed_eth2(frame),
        frame_has_ipv4(frame),
{
    if !(frame[23] == 0x06) {
        false
    } else {
        true
    }
}

exec fn compute_frame_has_ipv4_tcp(frame: &[u8]) -> (result: bool)
    requires
        frame_is_wellformed_eth2(frame),
        frame_has_ipv4(frame),
    ensures
        frame_has_ipv4_tcp(frame) <==> result == true,
        
{
    // Check for IPv4 Protocol octet equal to 0x06 for TCP
    if !(frame[23] == 0x06) {
        // Not TCP
        return false;
    }

    // Has TCP
    return true;
}

spec fn frame_has_ipv4_udp(frame: &[u8]) -> bool
    recommends
        frame_is_wellformed_eth2(frame),
        frame_has_ipv4(frame),
{
    if !(frame[23] == 0x11) {
        false
    } else {
        true
    }
}

exec fn compute_frame_has_ipv4_udp(frame: &[u8]) -> (result: bool)
    requires
        frame_is_wellformed_eth2(frame),
        frame_has_ipv4(frame),
    ensures
        frame_has_ipv4_udp(frame) <==> result == true,
{
    // Check for IPv4 Protocol octet equal to 0x11 for UDP
    if !(frame[23] == 0x11) {
        // Not UDP
        return false;
    }

    // Has UDP
    return true;
}

spec fn frame_has_ipv4_tcp_on_allowed_port(frame: &[u8]) -> bool
    recommends
        frame_is_wellformed_eth2(frame),
        frame_has_ipv4(frame),
        frame_has_ipv4_tcp(frame),
{
    // exists|i:int| 0 <= i < TCP_ALLOWED_PORTS.len() ==> TCP_ALLOWED_PORTS[i] == two_bytes_to_u16(frame[36], frame[37])
    TCP_ALLOWED_PORTS[0] == two_bytes_to_u16(frame[36], frame[37])
}

exec fn compute_frame_has_ipv4_tcp_on_allowed_port(frame: &[u8]) -> (result: bool)
    requires
        frame_is_wellformed_eth2(frame),
        frame_has_ipv4(frame),
        frame_has_ipv4_tcp(frame),
    ensures
        frame_has_ipv4_tcp_on_allowed_port(frame) <==> result == true,
        
{
    // Deny by default
    let mut result: bool = false;

    let port_byte0: u8 = frame[36];
    let port_byte1: u8 = frame[37];
    let port: u16 = ((port_byte0 as u16) * 256) + (port_byte1 as u16);

    /*
    // Check that destination port is in the allowed list
    let mut i = 0;
    while i < TCP_ALLOWED_PORTS_LENGTH
        invariant
            TCP_ALLOWED_PORTS_LENGTH == TCP_ALLOWED_PORTS.len(),
            0 <= i <= TCP_ALLOWED_PORTS.len(),
    {
        let allowed_port = TCP_ALLOWED_PORTS[i];
        if port == allowed_port {
            // Destination port matches some allowed port
            result = true;
            proof {
                frame.len() >= MIN_ETH2_FRAME_LENGTH >= 37;
                allowed_port == two_bytes_to_u16(frame[36], frame[37]);
                port == allowed_port;
                exists|j:int| 0 <= j < TCP_ALLOWED_PORTS.len() ==> TCP_ALLOWED_PORTS[j] == two_bytes_to_u16(frame[36], frame[37]);
                frame_has_ipv4_tcp_on_allowed_port(frame);
            }
        } else {
            proof {
                frame.len() >= MIN_ETH2_FRAME_LENGTH >= 37;
                allowed_port == two_bytes_to_u16(frame[36], frame[37]);
                port != allowed_port;
            }
        }
        i += 1;
    }
    return result;
    */

    return TCP_ALLOWED_PORTS[0] == (256 * frame[36] as u16) + frame[37] as u16
}

spec fn frame_has_ipv4_udp_on_allowed_port(frame: &[u8]) -> bool
    recommends
        frame_is_wellformed_eth2(frame),
        frame_has_ipv4(frame),
        frame_has_ipv4_udp(frame),
{
    // exists|i:int| 0 <= i < UDP_ALLOWED_PORTS.len() ==> #[trigger] UDP_ALLOWED_PORTS[i] == two_bytes_to_u16(frame[36], frame[37])
    UDP_ALLOWED_PORTS[0] == two_bytes_to_u16(frame[36], frame[37])
}

exec fn compute_frame_has_ipv4_udp_on_allowed_port(frame: &[u8]) -> (result: bool)
    requires
        frame_is_wellformed_eth2(frame),
        frame_has_ipv4(frame),
        frame_has_ipv4_udp(frame),
    ensures
        frame_has_ipv4_udp_on_allowed_port(frame) <==> result == true,
        
{
    // Deny by default
    let mut result: bool = false;

    let port_byte0: u8 = frame[36];
    let port_byte1: u8 = frame[37];
    let port: u16 = ((port_byte0 as u16) * 256) + (port_byte1 as u16);

    /*
    // Check that destination port is in the allowed list
    let mut i = 0;
    while i < UDP_ALLOWED_PORTS_LENGTH
        invariant
            UDP_ALLOWED_PORTS_LENGTH == UDP_ALLOWED_PORTS.len(),
            0 <= i <= UDP_ALLOWED_PORTS.len(),
    {
        let allowed_port = UDP_ALLOWED_PORTS[i];
        if port == allowed_port {
            // Destination port matches some allowed port
            result = true;
            proof {
                frame.len() >= MIN_ETH2_FRAME_LENGTH >= 37;
                allowed_port == two_bytes_to_u16(frame[36], frame[37]);
                port == allowed_port;
                exists|j:int| 0 <= j < UDP_ALLOWED_PORTS.len() ==> UDP_ALLOWED_PORTS[j] == two_bytes_to_u16(frame[36], frame[37]);
                frame_has_ipv4_udp_on_allowed_port(frame);
            }
        } else {
            proof {
                frame.len() >= MIN_ETH2_FRAME_LENGTH >= 37;
                allowed_port == two_bytes_to_u16(frame[36], frame[37]);
                port != allowed_port;
            }
        }
        i += 1;
    }
    return result;
    */

    return UDP_ALLOWED_PORTS[0] == (256 * frame[36] as u16) + frame[37] as u16
}

spec fn hlr_1_1(frame: &[u8], should_allow: bool) -> bool {
    if !frame_is_wellformed_eth2(frame) {
        should_allow == false
    } else {
        true
    }
}

spec fn hlr_1_2(frame: &[u8], should_allow: bool) -> bool {
    if frame_is_wellformed_eth2(frame) &&
        frame_has_ipv6(frame) {
        should_allow == false
    } else {
        true
    }
}

spec fn hlr_1_3(frame: &[u8], should_allow: bool) -> bool {
    if frame_is_wellformed_eth2(frame) && 
        frame_has_ipv4(frame) && 
        !(frame_has_ipv4_tcp(frame) || frame_has_ipv4_udp(frame)) {
        should_allow == false
    } else {
        true
    }
}

spec fn hlr_1_4(frame: &[u8], should_allow: bool) -> bool {
    if frame_is_wellformed_eth2(frame) &&
        frame_has_ipv4(frame) &&
        frame_has_ipv4_tcp(frame) &&
        !frame_has_ipv4_tcp_on_allowed_port(frame) {
        should_allow == false
    } else {
        true
    }
}

spec fn hlr_1_5(frame: &[u8], should_allow: bool) -> bool {
    if frame_is_wellformed_eth2(frame) &&
        frame_has_ipv4(frame) &&
        frame_has_ipv4_udp(frame) &&
        !frame_has_ipv4_udp_on_allowed_port(frame) {
        should_allow == false
    } else {
        true
    }
}

spec fn hlr_1_6(frame: &[u8], should_allow: bool) -> bool {
    if frame_is_wellformed_eth2(frame) &&
        frame_has_arp(frame) {
        should_allow == true
    } else {
        true
    }
}

spec fn hlr_1_7(frame: &[u8], should_allow: bool) -> bool {
    if frame_is_wellformed_eth2(frame) &&
        frame_has_ipv4(frame) &&
        frame_has_ipv4_tcp(frame) &&
        frame_has_ipv4_tcp_on_allowed_port(frame) {
        should_allow == true
    } else {
        true
    }
}

spec fn hlr_1_8(frame: &[u8], should_allow: bool) -> bool {
    if frame_is_wellformed_eth2(frame) &&
        frame_has_ipv4(frame) &&
        frame_has_ipv4_udp(frame) &&
        frame_has_ipv4_udp_on_allowed_port(frame) {
        should_allow == true
    } else {
        true
    }
}

// RX_Firewall
exec fn compute_should_allow_inbound_frame_rx(frame: &[u8]) -> (result: bool)
    ensures
        hlr_1_1(frame, result),
        hlr_1_2(frame, result),
        hlr_1_3(frame, result),
        hlr_1_4(frame, result),
        hlr_1_5(frame, result),
        hlr_1_6(frame, result),
        hlr_1_7(frame, result),
        hlr_1_8(frame, result),
{
    echo("Computing rules for rx frame...");
    // Inspect frame
    if compute_frame_is_wellformed_eth2(&frame) {
        echo("Frame passes wellformedness check...");
        // Allow ARP
        // HLR 1.6
        if compute_frame_has_arp(&frame) {
            echo("ALLOW: Frame has ARP.");
            return true;
        }

        // Drop IPv6
        // HLR 1.2
        if compute_frame_has_ipv6(&frame) {
            echo("DROP: Frame has IPv6.");
            return false;
        }

        // Inspect (wellformed) IPv4
        if compute_frame_has_ipv4(&frame) {
            echo("Frame has IPv4...");
            // Inspect IPv4/TCP
            // HLR 1.3
            if compute_frame_has_ipv4_tcp(&frame) {
                // Allow or drop according to whitelist
                // HLR 1.4, 1.7
                echo("Frame has TCP...");
                if compute_frame_has_ipv4_tcp_on_allowed_port(&frame) {
                    echo("ALLOW: Frame uses allowed TCP port.");
                    return true;
                } else {
                    echo("DROP: Frame does not use allowed TCP port.");
                    return false;
                }
            }
            
            // Inspect IPv4/UDP
            // HLR 1.3
            if compute_frame_has_ipv4_udp(&frame) {
                // Allow or drop according to whitelist
                // HLRs 1.5, 1.8
                echo("Frame has UDP...");
                if compute_frame_has_ipv4_udp_on_allowed_port(&frame) {
                    echo("ALLOW: Frame uses allowed UDP port.");
                    return true;
                } else {
                    echo("DROP: Frame does not use allowed UDP port.");
                    return false;
                }
            }

            // Drop unrecognized layer 4
            echo("DROP: Frame has unrecognized layer 4.");
            return false;
        }

        // ASSUMPTION: Deny-by-default unrecognized layer 3
        echo("DROP: Frame has unrecognized layer 3.");
        return false;
    }

    // Drop malformed/unrecognized frame
    // HLR 1.1
    echo("DROP: Frame is malformed.");
    return false;
}

spec fn hlr_2_1(frame: &[u8], should_allow: bool) -> bool {
    if !frame_is_wellformed_eth2(frame) {
        should_allow == false
    } else {
        true
    }
}

spec fn hlr_2_2(frame: &[u8], should_allow: bool) -> bool {
    if frame_is_wellformed_eth2(frame) && 
        frame_has_ipv6(frame) {
        should_allow == false
    } else {
        true
    }
}

spec fn hlr_2_3(frame: &[u8], should_allow: bool) -> bool {
    if frame_is_wellformed_eth2(frame) &&
        frame_has_arp(frame) {
        should_allow == true
    } else {
        true
    }
}

spec fn hlr_2_4(frame: &[u8], should_allow: bool) -> bool {
    if frame_is_wellformed_eth2(frame) && 
        frame_has_ipv4(frame) {
        should_allow == true
    } else {
        true
    }
}

// TX_Firewall
exec fn compute_should_allow_outbound_frame_tx(frame: &[u8]) -> (result: bool)
    ensures
        hlr_2_1(frame, result),
        hlr_2_2(frame, result),
        hlr_2_3(frame, result),
        hlr_2_4(frame, result),
{
    echo("Computing rules for tx frame...");
    // Inspect frame
    if compute_frame_is_wellformed_eth2(&frame) {
        // Drop IPv6
        // HLR 2.2
        if compute_frame_has_ipv6(&frame) {
            echo("DROP: Frame has IPv6.");
            return false;
        }

        // Allow ARP
        // HLR 2.3
        if compute_frame_has_arp(&frame) {
            echo("ALLOW: Frame has ARP.");
            return true;
        }

        // Allow IPv4
        // HLR 2.4
        // TODO ambiguous wording about needing to calculate frame size
        if compute_frame_has_ipv4(&frame) {
            echo("ALLOW: Frame has IPv4.");
            return true;
        }

        // ASSUMPTION: Deny-by-default unrecognized layer 3
        echo("DROP: Frame has unrecognized layer 3.");
        return false;
    }

    // Drop malformed/unrecognized frame
    // HLR 2.1
    return false;
}

// Test that tests work at all
#[test]
fn foo() {
    assert!(true);
}

// Tests rewritten from those in the provided Rust firewall code.
// These are not 1:1 due to fundamental difference in directly
// examining bytes using predicates rather than parsing the bytes
// into structures implementing out-of-scope behavior.
mod rewritten_tests {
    // See firewall/core/src/lib.rs
    //fn dest_mac_empty() {}

    use crate::{compute_frame_has_arp, compute_frame_has_ipv4, compute_frame_has_ipv4_tcp, compute_frame_has_ipv4_udp, compute_frame_is_wellformed_eth2};

    // Modified from firewall/core/src/lib.rs
    #[test]
    fn malformed_eth_header() {
        let mut frame = [0u8; 128];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x02, 0xC2,
        ];
        frame[0..14].copy_from_slice(&pkt);
        // let res = EthFrame::parse(&frame);
        // assert!(res.is_none());
        assert!(!compute_frame_is_wellformed_eth2(&frame));
    }

    // Not parsing IPV6 into a struct
    // See firewall/core/src/lib.rs
    // #[test]
    //fn eth_type_ipv6() {}

    // Modified from firewall/core/src/lib.rs
    #[test]
    fn valid_arp_request() {
        let mut frame = [0u8; 128];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x06, 0x0,
            0x1, 0x8, 0x0, 0x6, 0x4, 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0xc0, 0xa8, 0x0, 0x1,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xc0, 0xa8, 0x0, 0xce,
        ];
        frame[0..42].copy_from_slice(&pkt);
        // let res = EthFrame::parse(&frame);
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
        assert!(compute_frame_has_arp(&frame));
    }

    // Modified from firewall/core/src/lib.rs
    #[test]
    fn valid_arp_reply() {
        let mut frame = [0u8; 128];
        let pkt = [
            0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x18, 0x20, 0x22, 0x24, 0x26, 0x28, 0x8, 0x6, 0x0, 0x1,
            0x8, 0x0, 0x6, 0x4, 0x0, 0x2, 0x18, 0x20, 0x22, 0x24, 0x26, 0x28, 0xc0, 0xa8, 0x0,
            0xce, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0xc0, 0xa8, 0x0, 0x1,
        ];
        frame[0..42].copy_from_slice(&pkt);
        // let res = EthFrame::parse(&frame);
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
        assert!(compute_frame_has_arp(&frame));
    }

    // HLRs do not include wellformedness of ARP, just that ARP is passed through
    // See firewall/core/src/lib.rs
    // #[test]
    // fn malformed_arp() {}

    // Modified from firewall/core/src/lib.rs
    #[test]
    fn malformed_ipv4() {
        let mut frame = [0u8; 128];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00, 0x45,
            0x0, 0x0, 0x29, 0x15, 0x5f, 0x40, 0x0, 0x80, 0x7, 0xf7, 0x28, 0xc0, 0xa8, 0x0, 0xce,
            0x34, 0x7f, 0xf8, 0x51,
        ];
        frame[0..34].copy_from_slice(&pkt);
        // let res = EthFrame::parse(&frame);
        // assert!(res.is_none());
        assert!(compute_frame_has_ipv4(&frame));
    }

    // Modified from firewall/core/src/lib.rs
    #[test]
    fn valid_ipv4_protocols() {
        let mut frame = [0u8; 128];
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

        // let res = EthFrame::parse(&frame);
        // assert_eq!(res.as_ref(), Some(&expected));
        assert!(compute_frame_is_wellformed_eth2(&frame));
        assert!(compute_frame_has_ipv4(&frame));

        // ICMP
        frame[23] = 0x01;
        // if let PacketType::Ipv4(pack) = &mut expected.eth_type {
        //     pack.header.protocol = IpProtocol::Icmp;
        // }
        // let res = EthFrame::parse(&frame);
        // assert_eq!(res.as_ref(), Some(&expected));
        assert!(compute_frame_is_wellformed_eth2(&frame));
        assert!(compute_frame_has_ipv4(&frame));

        // IGMP
        frame[23] = 0x02;
        // if let PacketType::Ipv4(pack) = &mut expected.eth_type {
        //     pack.header.protocol = IpProtocol::Igmp;
        // }
        // let res = EthFrame::parse(&frame);
        // assert_eq!(res.as_ref(), Some(&expected));
        assert!(compute_frame_is_wellformed_eth2(&frame));
        assert!(compute_frame_has_ipv4(&frame));

        // Ipv6 Route
        frame[23] = 0x2b;
        // if let PacketType::Ipv4(pack) = &mut expected.eth_type {
        //     pack.header.protocol = IpProtocol::Ipv6Route;
        // }
        // let res = EthFrame::parse(&frame);
        // assert_eq!(res.as_ref(), Some(&expected));
        assert!(compute_frame_is_wellformed_eth2(&frame));
        assert!(compute_frame_has_ipv4(&frame));

        // Ipv6 Frag
        frame[23] = 0x2c;
        // if let PacketType::Ipv4(pack) = &mut expected.eth_type {
        //     pack.header.protocol = IpProtocol::Ipv6Frag;
        // }
        // let res = EthFrame::parse(&frame);
        // assert_eq!(res.as_ref(), Some(&expected));
        assert!(compute_frame_is_wellformed_eth2(&frame));
        assert!(compute_frame_has_ipv4(&frame));

        // ICMPv6
        frame[23] = 0x3a;
        // if let PacketType::Ipv4(pack) = &mut expected.eth_type {
        //     pack.header.protocol = IpProtocol::Icmpv6;
        // }
        // let res = EthFrame::parse(&frame);
        // assert_eq!(res.as_ref(), Some(&expected));
        assert!(compute_frame_is_wellformed_eth2(&frame));
        assert!(compute_frame_has_ipv4(&frame));

        // IPv6 No Nxt
        frame[23] = 0x3b;
        // if let PacketType::Ipv4(pack) = &mut expected.eth_type {
        //     pack.header.protocol = IpProtocol::Ipv6NoNxt;
        // }
        // let res = EthFrame::parse(&frame);
        // assert_eq!(res.as_ref(), Some(&expected));
        assert!(compute_frame_is_wellformed_eth2(&frame));
        assert!(compute_frame_has_ipv4(&frame));

        // IPv6 Opts
        frame[23] = 0x3c;
        // if let PacketType::Ipv4(pack) = &mut expected.eth_type {
        //     pack.header.protocol = IpProtocol::Ipv6Opts;
        // }
        // let res = EthFrame::parse(&frame);
        // assert_eq!(res.as_ref(), Some(&expected));
        assert!(compute_frame_is_wellformed_eth2(&frame));
        assert!(compute_frame_has_ipv4(&frame));

        // TCP
        frame[23] = 0x06;
        // if let PacketType::Ipv4(pack) = &mut expected.eth_type {
        //     pack.header.protocol = IpProtocol::Tcp;
        //     pack.protocol = Ipv4ProtoPacket::Tcp(TcpRepr { dst_port: 443 });
        // }
        // let res = EthFrame::parse(&frame);
        // assert_eq!(res.as_ref(), Some(&expected));
        assert!(compute_frame_is_wellformed_eth2(&frame));
        assert!(compute_frame_has_ipv4(&frame));
        assert!(compute_frame_has_ipv4_tcp(&frame));

        // UDP
        frame[23] = 0x11;
        // if let PacketType::Ipv4(pack) = &mut expected.eth_type {
        //     pack.header.protocol = IpProtocol::Udp;
        //     pack.protocol = Ipv4ProtoPacket::Udp(UdpRepr { dst_port: 443 });
        // }
        // let res = EthFrame::parse(&frame);
        // assert_eq!(res.as_ref(), Some(&expected));
        assert!(compute_frame_is_wellformed_eth2(&frame));
        assert!(compute_frame_has_ipv4(&frame));
        assert!(compute_frame_has_ipv4_udp(&frame));
    }

    

}

// Test that packets are correctly allowed/disallowed by the high level specification RX
mod high_level_specification_rx {
    use crate::compute_should_allow_inbound_frame_rx;


    // Test HLS for malformed Ethernet II header
    #[test]
    fn malformed() {
        let mut frame = [0u8; 128];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x02, 0xC2,
        ];
        frame[0..14].copy_from_slice(&pkt);
        
        assert!(!compute_should_allow_inbound_frame_rx(&frame));
    }

    // Test HLS for various IPv4 frames
    #[test]
    fn ipv4() {
        let mut frame = [0u8; 128];
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
        assert!(!compute_should_allow_inbound_frame_rx(&frame));
        // Correct the port to 5760
        frame[36] = 0x16;
        frame[37] = 0x80;
        //assert!(compute_should_allow_inbound_frame_rx(&frame));

        // UDP
        frame[23] = 0x11;
        // Drop due to wrong port
        //assert!(!compute_should_allow_inbound_frame_rx(&frame));
        // Correct the port to 68
        frame[36] = 0x00;
        frame[37] = 0x44;
        //assert!(compute_should_allow_inbound_frame_rx(&frame));
        
        // IGMP
        frame[23] = 0x02;
        //assert!(!compute_should_allow_inbound_frame_rx(&frame));
    }

    // Test HLS for IPv6
    #[test]
    fn ipv6() {
        let mut frame = [0u8; 128];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x86, 0xDD,
        ];
        frame[0..14].copy_from_slice(&pkt);
        assert!(!compute_should_allow_inbound_frame_rx(&frame));
    }

    // Test HLS for ARP
    #[test]
    fn arp() {
        let mut frame = [0u8; 128];
        let pkt = [
            0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x18, 0x20, 0x22, 0x24, 0x26, 0x28, 0x8, 0x6, 0x0, 0x1,
            0x8, 0x0, 0x6, 0x4, 0x0, 0x2, 0x18, 0x20, 0x22, 0x24, 0x26, 0x28, 0xc0, 0xa8, 0x0,
            0xce, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0xc0, 0xa8, 0x0, 0x1,
        ];
        frame[0..42].copy_from_slice(&pkt);
        assert!(compute_should_allow_inbound_frame_rx(&frame));
    }

}

} //verus!

