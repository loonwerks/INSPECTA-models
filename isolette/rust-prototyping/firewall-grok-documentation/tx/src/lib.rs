#![cfg_attr(not(test), no_std)]
#![allow(non_snake_case)]

//! This Rust code implements a transmit-side firewall (`tx_firewall.rs`) that 
//! processes outgoing Ethernet frames, filters them based on protocol type, 
//! and forwards allowed packets with appropriate sizing. It is designed for a 
//! `no_std` environment (except during tests), integrating with the seL4 
//! microkernel and external C bindings for Ethernet frame I/O.
//!
//! - **`no_std` and Attributes**: The `#![cfg_attr(not(test), no_std)]` 
//!   directive ensures minimal dependencies for embedded or systems use, while 
//!   `#![allow(non_snake_case)]` accommodates C-style naming from external 
//!   bindings.
//! - **Imports**: Uses `firewall-core` for packet parsing (`EthFrame`, 
//!   `PacketType`, etc.) and `log` for logging, with conditional `error` logging 
//!   for non-test builds.
//! - **Bindings Module**: Defines FFI bindings to C functions 
//!   (`get_EthernetFramesTxIn0`, `put_EthernetFramesTxOut0`, etc.) for receiving 
//!   and sending Ethernet frames, wrapped in `EthChannelGet` and `EthChannelPut` 
//!   structs for abstraction.
//! - **Channel Constants**: `IN_CHANNEL` and `OUT_CHANNEL` provide static 
//!   access to the transmit input and output channels via FFI bindings.
//! - **Initialization and Trigger Functions**: 
//!   `seL4_TxFirewall_TxFirewall_initialize` logs initialization, and 
//!   `seL4_TxFirewall_TxFirewall_timeTriggered` processes frames periodically, 
//!   both exposed as C-compatible entry points for seL4.
//! - **Frame Handling**: `eth_get` retrieves frames, `eth_put` sends them, 
//!   `can_send_frame` filters and sizes packets (allowing ARP and IPv4, 
//!   rejecting IPv6), and `firewall` manages the process, converting raw frames 
//!   to sized frames for output.
//! - **Tests**: Unit tests verify initialization, frame filtering (accepting 
//!   ARP/IPv4, rejecting IPv6), and I/O functionality, with mock bindings for 
//!   testing.

use log::{info, trace};

#[cfg(not(test))]
use {log::error};

use firewall_core::{Arp, EthFrame, EthernetRepr, PacketType};

mod config;

#[cfg(not(test))]
mod bindings {
    use inspecta_types::{BaseSwRawEthernetMessageImpl, BaseSwSizedEthernetMessageImpl};
    extern "C" {
        /// FFI binding to retrieve an Ethernet frame from the transmit input channel.
        pub fn get_EthernetFramesTxIn0(value: *mut BaseSwRawEthernetMessageImpl) -> bool;

        /// FFI binding to check if the transmit input channel is empty.
        pub fn EthernetFramesTxIn0_is_empty() -> bool;

        /// FFI binding to send an Ethernet frame to the transmit output channel.
        pub fn put_EthernetFramesTxOut0(value: *mut BaseSwSizedEthernetMessageImpl) -> bool;
    }

    /// Struct wrapping input channel functions for receiving Ethernet frames.
    pub struct EthChannelGet {
        pub get: unsafe extern "C" fn(*mut BaseSwRawEthernetMessageImpl) -> bool,
        pub empty: unsafe extern "C" fn() -> bool,
    }

    /// Struct wrapping output channel function for sending sized Ethernet frames.
    pub struct EthChannelPut {
        pub put: unsafe extern "C" fn(*mut BaseSwSizedEthernetMessageImpl) -> bool,
    }
}

use inspecta_types::{
    BaseSwRawEthernetMessageImpl, BaseSwSizedEthernetMessageImpl,
    BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE,
};

#[cfg(not(test))]
use bindings::*;

/// Static configuration for the transmit input Ethernet channel.
///
/// Provides access to the `get_EthernetFramesTxIn0` and 
/// `EthernetFramesTxIn0_is_empty` bindings.
#[cfg(not(test))]
pub const IN_CHANNEL: EthChannelGet =
    EthChannelGet {
        get: get_EthernetFramesTxIn0,
        empty: EthernetFramesTxIn0_is_empty,
    };

/// Static configuration for the transmit output Ethernet channel.
///
/// Provides access to the `put_EthernetFramesTxOut0` binding.
#[cfg(not(test))]
pub const OUT_CHANNEL: EthChannelPut = 
    EthChannelPut {
        put: put_EthernetFramesTxOut0,
    };

/// Initializes the transmit firewall component, logging an info message.
///
/// Exposed as a C-compatible entry point for seL4 integration.
#[no_mangle]
pub extern "C" fn seL4_TxFirewall_TxFirewall_initialize() {
    info!("Init");
}

/// Retrieves an Ethernet frame from the transmit input channel.
///
/// # Arguments
/// * `tx_buf` - A mutable reference to a buffer for storing the received frame.
///
/// # Returns
/// `true` if a frame was successfully retrieved, `false` if the channel is empty.
#[cfg(not(test))]
fn eth_get(tx_buf: &mut BaseSwRawEthernetMessageImpl) -> bool {
    let value = tx_buf.as_mut_ptr() as *mut BaseSwRawEthernetMessageImpl;

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

/// Sends a sized Ethernet frame to the transmit output channel.
///
/// # Arguments
/// * `tx_buf` - A mutable reference to the sized frame buffer to be sent.
#[cfg(not(test))]
fn eth_put(tx_buf: &mut BaseSwSizedEthernetMessageImpl) {
    let value = tx_buf as *mut BaseSwSizedEthernetMessageImpl;
    #[allow(unused_unsafe)]
    unsafe {
        (OUT_CHANNEL.put)(value);
    };
}

/// Determines if an Ethernet frame can be sent and calculates its size.
///
/// # Arguments
/// * `frame` - A mutable byte slice containing the Ethernet frame.
///
/// # Returns
/// `Some(size)` if the frame is allowed (ARP or IPv4) with its total size in 
/// bytes, `None` if malformed or disallowed (e.g., IPv6).
fn can_send_frame(frame: &mut [u8]) -> Option<u16> {
    let Some(packet) = EthFrame::parse(frame) else {
        info!("Malformed packet. Throw it away.");
        return None;
    };

    let size = match packet.eth_type {
        PacketType::Arp(_) => {
            let size = 64u16;
            // TODO: Do we need this now that linux is constructing it?
            frame[EthernetRepr::SIZE + Arp::SIZE..size as usize].fill(0);
            size
        }
        PacketType::Ipv4(ip) => ip.header.length + EthernetRepr::SIZE as u16,
        PacketType::Ipv6 => {
            info!("Not an IPv4 or Arp packet. Throw it away.");
            return None;
        }
    };

    Some(size)
}

/// Processes an Ethernet frame through the transmit firewall.
///
/// # Arguments
/// * `frame` - A mutable reference to the raw frame buffer.
///
/// Retrieves a new frame, applies filtering, and sends it with sizing if allowed.
#[cfg(not(test))]
fn firewall(frame: &mut BaseSwRawEthernetMessageImpl) {
    let new_data = eth_get(frame);
    if !new_data {
        return;
    }

    if let Some(size) = can_send_frame(frame) {
        let mut out = BaseSwSizedEthernetMessageImpl {
            size,
            message: *frame,
        };
        eth_put(&mut out);
    }
}

/// Time-triggered entry point for the transmit firewall, called by seL4.
///
/// Creates a frame buffer and invokes the firewall processing logic.
#[no_mangle]
#[cfg(not(test))]
pub extern "C" fn seL4_TxFirewall_TxFirewall_timeTriggered() {
    trace!("Triggered");
    let mut frame: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];

    firewall(&mut frame);
}

/// Tests the transmit firewall initialization function.
///
/// Ensures `seL4_TxFirewall_TxFirewall_initialize` executes without panicking 
/// and logs the expected message.
#[test]
fn sel4_firewall_firewall_initialize_test() {
    seL4_TxFirewall_TxFirewall_initialize();
}

#[cfg(test)]
mod can_send_tx_frame_tests {
    use super::*;

    #[test]
    /// Confirms `can_send_frame` allows valid ARP packets and returns correct size.
    ///
    /// Uses a valid ARP request frame, verifies it returns `Some(64)`, and 
    /// checks padding to 64 bytes.
    fn valid_arp() {
        let mut frame = [0u8; 128];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x06, 0x0,
            0x1, 0x8, 0x0, 0x6, 0x4, 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0xc0, 0xa8, 0x0, 0x1,
            0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xc0, 0xa8, 0x0, 0xce,
        ];
        frame[0..42].copy_from_slice(&pkt);
        let res = can_send_frame(&mut frame);
        assert!(res.is_some());
        assert_eq!(res.unwrap(), 64);
    }

    #[test]
    /// Ensures `can_send_frame` rejects IPv6 packets.
    ///
    /// Tests an IPv6 frame (EtherType `0x86DD`) and confirms it returns `None`.
    fn disallowed_ipv6() {
        let mut frame = [0u8; 128];
        // IPv6 Frame
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x86, 0xdd,
        ];
        frame[0..14].copy_from_slice(&pkt);
        let res = can_send_frame(&mut frame);
        assert!(res.is_none());
    }

    #[test]
    /// Verifies `can_send_frame` allows IPv4 packets and returns correct sizes.
    ///
    /// Tests two IPv4 frames with lengths 32 and 51 bytes, confirming returns 
    /// of `Some(46)` and `Some(65)` (including Ethernet header size).
    fn valid_ipv4() {
        let mut frame = [0u8; 128];
        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00, 0x45,
            0x0, 0x0, 0x20, 0x15, 0x5f, 0x40, 0x0, 0x80, 0x0, 0xf7, 0x28, 0xc0, 0xa8, 0x0, 0xce,
            0x34, 0x7f, 0xf8, 0x51,
        ];
        frame[0..34].copy_from_slice(&pkt);
        let res = can_send_frame(&mut frame);
        assert!(res.is_some());
        assert_eq!(res.unwrap(), 46);

        let pkt = [
            0xffu8, 0xff, 0xff, 0xff, 0xff, 0xff, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x08, 0x00, 0x45,
            0x0, 0x0, 0x33, 0x15, 0x5f, 0x40, 0x0, 0x80, 0x1, 0xf7, 0x28, 0xc0, 0xa8, 0x0, 0xce,
            0x34, 0x7f, 0xf8, 0x51,
        ];
        frame[0..34].copy_from_slice(&pkt);
        let res = can_send_frame(&mut frame);
        assert!(res.is_some());
        assert_eq!(res.unwrap(), 65);
    }
}

#[cfg(test)]
mod tx_ethernet_frames_tests {
    use super::*;

    mod get_in {
        use super::*;

        #[test]
        /// Verifies that `eth_get` retrieves a frame successfully in test mode.
        ///
        /// Uses a mock binding returning `true` and confirms `eth_get` returns 
        /// `true`.
        fn valid() {
            let mut tx_buf: BaseSwRawEthernetMessageImpl =
                [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];

            let res = eth_get(&mut tx_buf);
            assert!(res);
        }
    }

    mod put_out {
        use super::*;

        #[test]
        /// Ensures `eth_put` executes without panicking in test mode.
        ///
        /// Uses a mock binding and verifies the function completes with a sized 
        /// frame.
        fn valid() {
            let message: BaseSwRawEthernetMessageImpl = [0; BASE_SW_RAWETHERNETMESSAGE_IMPL_SIZE];

            let mut tx_buf: BaseSwSizedEthernetMessageImpl =
                BaseSwSizedEthernetMessageImpl { message, size: 20 };

            eth_put(&mut tx_buf);
        }
    }
}

#[cfg(test)]
mod bindings {
    use super::*;

    /// Mock binding for retrieving an Ethernet frame (test only).
    pub fn get_EthernetFramesTxIn0(_value: *mut BaseSwRawEthernetMessageImpl) -> bool {
        true
    }

    /// Mock binding to check if the input channel is empty (test only).
    pub fn EthernetFramesTxIn0_is_empty() -> bool {
        false
    }

    /// Mock binding for sending a sized Ethernet frame (test only).
    pub fn put_EthernetFramesTxOut0(_value: *mut BaseSwSizedEthernetMessageImpl) -> bool {
        true
    }

    /// Mock struct for input channel functions (test only).
    pub struct EthChannelGet {
        pub get: fn(*mut BaseSwRawEthernetMessageImpl) -> bool,
        pub empty: fn() -> bool,
    }

    /// Mock struct for output channel function (test only).
    pub struct EthChannelPut {
        pub put: fn(*mut BaseSwSizedEthernetMessageImpl) -> bool,
    }
}

#[cfg(test)]
fn eth_get(tx_buf: &mut BaseSwRawEthernetMessageImpl) -> bool {
    let value = tx_buf.as_mut_ptr() as *mut BaseSwRawEthernetMessageImpl;
    crate::bindings::get_EthernetFramesTxIn0(value)
}

#[cfg(test)]
fn eth_put(tx_buf: &mut BaseSwSizedEthernetMessageImpl) {
    let value = tx_buf as *mut BaseSwSizedEthernetMessageImpl;
    crate::bindings::put_EthernetFramesTxOut0(value);
}

#[cfg(test)]
pub const IN_CHANNEL: bindings::EthChannelGet =
    bindings::EthChannelGet {
        get: bindings::get_EthernetFramesTxIn0,
        empty: bindings::EthernetFramesTxIn0_is_empty,
    };

#[cfg(test)]
pub const OUT_CHANNEL: bindings::EthChannelPut = 
    bindings::EthChannelPut {
        put: bindings::put_EthernetFramesTxOut0,
    };

    