#![cfg_attr(not(test), no_std)]
#![allow(non_snake_case)]

//! This Rust code implements a receive-side firewall that 
//! processes incoming Ethernet frames, filters them based on protocol and port 
//! configurations, and forwards allowed packets. It is designed for a `no_std` 
//! environment (except during tests), integrating with the seL4 microkernel and 
//! external C bindings for Ethernet frame I/O.
//!
//! - **`no_std` and Attributes**: The `#![cfg_attr(not(test), no_std)]` directive 
//!   ensures minimal dependencies for embedded or systems use, while 
//!   `#![allow(non_snake_case)]` accommodates external C function naming 
//!   conventions.
//! - **Imports**: Uses `firewall-core` for packet parsing (`EthFrame`, 
//!   `PacketType`, etc.) and `log` for logging, with conditional `error` logging 
//!   for non-test builds.
//! - **Bindings Module**: Defines FFI (Foreign Function Interface) bindings to C 
//!   functions (`get_EthernetFramesRxIn0`, `put_EthernetFramesRxOut0`, etc.) for 
//!   receiving and sending Ethernet frames, wrapped in Rust structs 
//!   (`EthChannelGet`, `EthChannelPut`) for abstraction.
//! - **Channel Constants**: `IN_CHANNEL` and `OUT_CHANNEL` provide static access 
//!   to the input and output Ethernet channels using the FFI bindings.
//! - **Initialization and Trigger Functions**: 
//!   `seL4_RxFirewall_RxFirewall_initialize` logs initialization, and 
//!   `seL4_RxFirewall_RxFirewall_timeTriggered` processes frames periodically, 
//!   both exposed as C-compatible entry points.
//! - **Frame Handling**: Functions like `eth_get` and `eth_put` manage frame I/O 
//!   via the channels, while `can_send_frame` filters packets (allowing ARP, 
//!   TCP/UDP with allowed ports) and `firewall` orchestrates the process.
//! - **Port Filtering**: `tcp_port_allowed` and `udp_port_allowed` check against 
//!   predefined port lists in a `config` module.
//! - **Tests**: Unit tests verify port filtering, frame acceptance/rejection, 
//!   and basic I/O functionality, with mock bindings for testing.

use log::{info, trace};

#[cfg(not(test))]
use {log::error};

use firewall_core::{EthFrame, Ipv4ProtoPacket, PacketType, TcpRepr, UdpRepr};

mod config;

#[cfg(not(test))]
mod bindings {
    use inspecta_types::BaseSwRawEthernetMessageImpl;
    extern "C" {
        /// FFI binding to retrieve an Ethernet frame from the input channel.
        pub fn get_EthernetFramesRxIn0(value: *mut BaseSwRawEthernetMessageImpl) -> bool;

        /// FFI binding to check if the input channel is empty.
        pub fn EthernetFramesRxIn0_is_empty() -> bool;

        /// FFI binding to send an Ethernet frame to the output channel.
        pub fn put_EthernetFramesRxOut0(value: *mut BaseSwRawEthernetMessageImpl) -> bool;
    }

    /// Struct wrapping input channel functions for receiving Ethernet frames.
    pub struct EthChannelGet {
        pub get: unsafe extern "C" fn(*mut BaseSwRawEthernetMessageImpl) -> bool,
        pub empty: unsafe extern "C" fn() -> bool,
    }

    /// Struct wrapping output channel function for sending Ethernet frames.
    pub struct EthChannelPut {
        pub put: unsafe extern "C" fn(*mut BaseSwRawEthernetMessageImpl) -> bool,
    }
}

use inspecta_types::{BaseSwRawEthernetMessageImpl, BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE};

#[cfg(not(test))]
use bindings::*;

/// Static configuration for the input Ethernet channel.
///
/// Provides access to the `get_EthernetFramesRxIn0` and 
/// `EthernetFramesRxIn0_is_empty` bindings.
#[cfg(not(test))]
pub const IN_CHANNEL: EthChannelGet = 
    EthChannelGet {
        get: get_EthernetFramesRxIn0,
        empty: EthernetFramesRxIn0_is_empty,
    };

/// Static configuration for the output Ethernet channel.
///
/// Provides access to the `put_EthernetFramesRxOut0` binding.
#[cfg(not(test))]
pub const OUT_CHANNEL: EthChannelPut = 
    EthChannelPut {
        put: put_EthernetFramesRxOut0,
    };

/// Initializes the firewall component, logging an info message.
///
/// Exposed as a C-compatible entry point for seL4 integration.
#[no_mangle]
pub extern "C" fn seL4_RxFirewall_RxFirewall_initialize() {
    info!("Init");
}

/// Retrieves an Ethernet frame from the input channel.
///
/// # Arguments
/// * `rx_buf` - A mutable reference to a buffer for storing the received frame.
///
/// # Returns
/// `true` if a frame was successfully retrieved, `false` if the channel is empty.
#[cfg(not(test))]
fn eth_get(rx_buf: &mut BaseSwRawEthernetMessageImpl) -> bool {
    let value = rx_buf.as_mut_ptr() as *mut BaseSwRawEthernetMessageImpl;
    #[allow(unused_unsafe)]
    unsafe {
        if !(IN_CHANNEL.empty)() {
            (IN_CHANNEL.get)(value);
            true
        } else {
            false
        }
    }
}

/// Sends an Ethernet frame to the output channel.
///
/// # Arguments
/// * `rx_buf` - A mutable reference to the frame buffer to be sent.
#[cfg(not(test))]
fn eth_put(rx_buf: &mut BaseSwRawEthernetMessageImpl) {
    let value = rx_buf as *mut BaseSwRawEthernetMessageImpl;
    #[allow(unused_unsafe)]
    unsafe {
        (OUT_CHANNEL.put)(value);
    };
}

/// Checks if a UDP packet’s destination port is allowed.
///
/// # Arguments
/// * `udp` - A reference to the UDP packet representation.
///
/// # Returns
/// `true` if the port is in the allowed list, `false` otherwise.
fn udp_port_allowed(udp: &UdpRepr) -> bool {
    config::udp::ALLOWED_PORTS
        .iter()
        .any(|x| *x == udp.dst_port)
}

/// Checks if a TCP packet’s destination port is allowed.
///
/// # Arguments
/// * `tcp` - A reference to the TCP packet representation.
///
/// # Returns
/// `true` if the port is in the allowed list, `false` otherwise.
fn tcp_port_allowed(tcp: &TcpRepr) -> bool {
    config::tcp::ALLOWED_PORTS
        .iter()
        .any(|x| *x == tcp.dst_port)
}

/// Determines if an Ethernet frame should be forwarded based on firewall rules.
///
/// # Arguments
/// * `frame` - A mutable byte slice containing the Ethernet frame.
///
/// # Returns
/// `true` if the frame is allowed (ARP or TCP/UDP with allowed ports), `false` 
/// otherwise (malformed, IPv6, or disallowed protocols/ports).
fn can_send_frame(frame: &mut [u8]) -> bool {
    let Some(packet) = EthFrame::parse(frame) else {
        info!("Malformed packet. Throw it away.");
        return false;
    };

    match packet.eth_type {
        PacketType::Arp(_) => true,
        PacketType::Ipv4(ip) => match ip.protocol {
            Ipv4ProtoPacket::Tcp(tcp) => {
                let allowed = tcp_port_allowed(&tcp);
                if !allowed {
                    info!("TCP packet filtered out");
                }
                allowed
            }
            Ipv4ProtoPacket::Udp(udp) => {
                let allowed = udp_port_allowed(&udp);
                if !allowed {
                    info!("UDP packet filtered out");
                }
                allowed
            }
            Ipv4ProtoPacket::TxOnly => {
                info!(
                    "Not a TCP or UDP packet. ({:?}) Throw it away.",
                    ip.header.protocol
                );
                false
            }
        },
        PacketType::Ipv6 => {
            info!("Not an IPv4 or Arp packet. Throw it away.");
            false
        }
    }
}

/// Processes an Ethernet frame through the firewall.
///
/// # Arguments
/// * `frame` - A mutable reference to the frame buffer.
///
/// Retrieves a new frame, applies filtering, and sends it if allowed.
#[cfg(not(test))]
fn firewall(frame: &mut BaseSwRawEthernetMessageImpl) {
    let new_data = eth_get(frame);
    if !new_data {
        return;
    }

    if can_send_frame(frame) {
        eth_put(frame);
    }
}

/// Time-triggered entry point for the firewall, called periodically by seL4.
///
/// # Arguments
/// Creates a frame buffer and invokes the firewall processing logic.
#[no_mangle]
#[cfg(not(test))]
pub extern "C" fn seL4_RxFirewall_RxFirewall_timeTriggered() {
    trace!("Triggered");
    let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];

    firewall(&mut frame);
}

/// Tests the TCP port filtering logic.
///
/// Verifies that port 5760 is allowed and port 42 is disallowed based on the 
/// config’s `ALLOWED_PORTS`.
#[test]
fn tcp_port_allowed_test() {
    let tcp_packet = TcpRepr { dst_port: 5760 };
    assert!(tcp_port_allowed(&tcp_packet));
    let tcp_packet = TcpRepr { dst_port: 42 };
    assert!(!tcp_port_allowed(&tcp_packet));
}

/// Tests the UDP port filtering logic.
///
/// Verifies that port 68 is allowed and port 19 is disallowed based on the 
/// config’s `ALLOWED_PORTS`.
#[test]
fn udp_port_allowed_test() {
    let udp_packet = UdpRepr { dst_port: 68 };
    assert!(udp_port_allowed(&udp_packet));
    let udp_packet = UdpRepr { dst_port: 19 };
    assert!(!udp_port_allowed(&udp_packet));
}

/// Tests the firewall initialization function.
///
/// Ensures `seL4_RxFirewall_RxFirewall_initialize` executes without panicking 
/// and logs the expected message.
#[test]
fn sel4_firewall_firewall_initialize_test() {
    seL4_RxFirewall_RxFirewall_initialize();
}

#[cfg(test)]
mod can_send_frame_tests {
    use super::*;

    #[test]
    /// Verifies that `can_send_frame` rejects malformed packets.
    ///
    /// Uses a frame with an unknown EtherType (`0x02C2`) and confirms it 
    /// returns `false`.
    fn malformed_packet() {
        let mut frame = [0u8; 128];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x02, 0xC2,
        ];
        frame[0..14].copy_from_slice(&pkt);
        let res = can_send_frame(&mut frame);
        assert!(!res);
    }

    #[test]
    /// Ensures `can_send_frame` disallows IPv6 packets.
    ///
    /// Tests an IPv6 frame (EtherType `0x86DD`) and confirms it returns `false`.
    fn disallowed_ipv6() {
        let mut frame = [0u8; 128];
        // IPv6 Frame
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x86, 0xdd,
        ];
        frame[0..14].copy_from_slice(&pkt);
        let res = can_send_frame(&mut frame);
        assert!(!res);
    }

    #[test]
    /// Confirms `can_send_frame` allows valid ARP packets.
    ///
    /// Uses a valid ARP request frame and verifies it returns `true`.
    fn valid_arp() {
        let mut frame = [0u8; 128];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x06, 0x0,
            0x1, 0x8, 0x0, 0x6, 0x4, 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0xc0, 0xa8, 0x0, 0x1,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xc0, 0xa8, 0x0, 0xce,
        ];
        frame[0..42].copy_from_slice(&pkt);
        let res = can_send_frame(&mut frame);
        assert!(res);
    }

    #[test]
    /// Verifies `can_send_frame` rejects IPv4 packets with unsupported protocols.
    ///
    /// Tests frames with protocols: HopByHop, ICMP, IGMP, Ipv6Route, Ipv6Frag, 
    /// ICMPv6, Ipv6NoNxt, and Ipv6Opts, ensuring each returns `false`.
    fn invalid_ipv4_protocols() {
        let mut frame = [0u8; 128];
        // Hop by Hop
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00, 0x45,
            0x0, 0x0, 0x29, 0x15, 0x5f, 0x40, 0x0, 0x80, 0x0, 0xf7, 0x28, 0xc0, 0xa8, 0x0, 0xce,
            0x34, 0x7f, 0xf8, 0x51,
        ];
        frame[0..34].copy_from_slice(&pkt);
        let res = can_send_frame(&mut frame);
        assert!(!res);

        // ICMP
        frame[23] = 0x01;
        let res = can_send_frame(&mut frame);
        assert!(!res);

        // IGMP
        frame[23] = 0x02;
        let res = can_send_frame(&mut frame);
        assert!(!res);

        // Ipv6 Route
        frame[23] = 0x2b;
        let res = can_send_frame(&mut frame);
        assert!(!res);

        // Ipv6 Frag
        frame[23] = 0x2c;
        let res = can_send_frame(&mut frame);
        assert!(!res);

        // ICMPv6
        frame[23] = 0x3a;
        let res = can_send_frame(&mut frame);
        assert!(!res);

        // IPv6 No Nxt
        frame[23] = 0x3b;
        let res = can_send_frame(&mut frame);
        assert!(!res);

        // IPv6 Opts
        frame[23] = 0x3c;
        let res = can_send_frame(&mut frame);
        assert!(!res);
    }

    #[test]
    /// Ensures `can_send_frame` rejects TCP packets with disallowed ports.
    ///
    /// Uses a TCP frame with destination port 443 (disallowed) and confirms it 
    /// returns `false`.
    fn disallowed_tcp() {
        let mut frame = [0u8; 128];
        // Disallowed port
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00, 0x45,
            0x0, 0x0, 0x29, 0x15, 0x5f, 0x40, 0x0, 0x80, 0x6, 0xf7, 0x28, 0xc0, 0xa8, 0x0, 0xce,
            0x34, 0x7f, 0xf8, 0x51, 0xc4, 0x73, 0x1, 0xbb, 0x21, 0x65, 0x90, 0xfb, 0xe4, 0x98,
            0x7c, 0x9d, 0x50, 0x10, 0x3, 0xff, 0xe3, 0xc7, 0x0, 0x0,
        ];
        frame[0..54].copy_from_slice(&pkt);
        let res = can_send_frame(&mut frame);
        assert!(!res);
    }

    #[test]
    /// Confirms `can_send_frame` allows TCP packets with allowed ports.
    ///
    /// Uses a TCP frame with destination port 5760 (allowed) and verifies it 
    /// returns `true`.
    fn allowed_tcp() {
        let mut frame = [0u8; 128];
        // Allowed port
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00, 0x45,
            0x0, 0x0, 0x29, 0x15, 0x5f, 0x40, 0x0, 0x80, 0x6, 0xf7, 0x28, 0xc0, 0xa8, 0x0, 0xce,
            0x34, 0x7f, 0xf8, 0x51, 0xc4, 0x73, 0x16, 0x80, 0x21, 0x65, 0x90, 0xfb, 0xe4, 0x98,
            0x7c, 0x9d, 0x50, 0x10, 0x3, 0xff, 0xe3, 0xc7, 0x0, 0x0,
        ];
        frame[0..54].copy_from_slice(&pkt);
        let res = can_send_frame(&mut frame);
        assert!(res);
    }

    #[test]
    /// Ensures `can_send_frame` rejects UDP packets with disallowed ports.
    ///
    /// Uses a UDP frame with destination port 57621 (disallowed) and confirms 
    /// it returns `false`.
    fn disallowed_udp() {
        let mut frame = [0u8; 128];
        // Disallowed port
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00, 0x45,
            0x0, 0x0, 0x29, 0x15, 0x5f, 0x40, 0x0, 0x80, 0x11, 0xf7, 0x28, 0xc0, 0xa8, 0x0, 0xce,
            0x34, 0x7f, 0xf8, 0x51, 0xe1, 0x15, 0xe1, 0x15, 0x0, 0x34, 0xb4, 0xe8,
        ];
        frame[0..42].copy_from_slice(&pkt);
        let res = can_send_frame(&mut frame);
        assert!(!res);
    }

    #[test]
    /// Confirms `can_send_frame` allows UDP packets with allowed ports.
    ///
    /// Uses a UDP frame with destination port 68 (allowed) and verifies it 
    /// returns `true`.
    fn allowed_udp() {
        let mut frame = [0u8; 128];
        // Allowed port
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00, 0x45,
            0x0, 0x0, 0x29, 0x15, 0x5f, 0x40, 0x0, 0x80, 0x11, 0xf7, 0x28, 0xc0, 0xa8, 0x0, 0xce,
            0x34, 0x7f, 0xf8, 0x51, 0xe1, 0x15, 0x00, 0x44, 0x0, 0x34, 0xb4, 0xe8,
        ];
        frame[0..42].copy_from_slice(&pkt);
        let res = can_send_frame(&mut frame);
        assert!(res);
    }
}

#[cfg(test)]
mod rx_ethernet_frames_tests {
    use super::*;

    mod get_in {
        use super::*;

        #[test]
        /// Verifies that `eth_get` retrieves a frame successfully in test mode.
        ///
        /// Uses a mock binding returning `true` and confirms `eth_get` returns 
        /// `true`.
        fn valid() {
            let mut rx_buf: BaseSwRawEthernetMessageImpl =
                [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];

            let res = eth_get(&mut rx_buf);
            assert!(res);
        }
    }

    mod put_out {
        use super::*;

        #[test]
        /// Ensures `eth_put` executes without panicking in test mode.
        ///
        /// Uses a mock binding and verifies the function completes successfully.
        fn valid() {
            let mut rx_buf: BaseSwRawEthernetMessageImpl =
                [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];

            eth_put(&mut rx_buf);
        }
    }
}

#[cfg(test)]
mod bindings {
    use super::*;

    /// Mock binding for retrieving an Ethernet frame (test only).
    pub fn get_EthernetFramesRxIn0(_value: *mut BaseSwRawEthernetMessageImpl) -> bool {
        true
    }

    /// Mock binding to check if the input channel is empty (test only).
    pub fn EthernetFramesRxIn0_is_empty() -> bool {
        false
    }

    /// Mock binding for sending an Ethernet frame (test only).
    pub fn put_EthernetFramesRxOut0(_value: *mut BaseSwRawEthernetMessageImpl) -> bool {
        true
    }

    /// Mock struct for input channel functions (test only).
    pub struct EthChannelGet {
        pub get: fn(*mut BaseSwRawEthernetMessageImpl) -> bool,
        pub empty: fn() -> bool,
    }

    /// Mock struct for output channel function (test only).
    pub struct EthChannelPut {
        pub put: fn(*mut BaseSwRawEthernetMessageImpl) -> bool,
    }
}

#[cfg(test)]
fn eth_get(rx_buf: &mut BaseSwRawEthernetMessageImpl) -> bool {
    let value = rx_buf.as_mut_ptr() as *mut BaseSwRawEthernetMessageImpl;
    crate::bindings::get_EthernetFramesRxIn0(value)
}

#[cfg(test)]
fn eth_put(rx_buf: &mut BaseSwRawEthernetMessageImpl) {
    let value = rx_buf as *mut BaseSwRawEthernetMessageImpl;
    crate::bindings::put_EthernetFramesRxOut0(value);
}

#[cfg(test)]
pub const IN_CHANNEL: bindings::EthChannelGet = 
    bindings::EthChannelGet {
        get: bindings::get_EthernetFramesRxIn0,
        empty: bindings::EthernetFramesRxIn0_is_empty,
    };

#[cfg(test)]
pub const OUT_CHANNEL: bindings::EthChannelPut = 
    bindings::EthChannelPut {
        put: bindings::put_EthernetFramesRxOut0,
    };

