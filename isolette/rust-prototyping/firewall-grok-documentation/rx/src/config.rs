//
// Copyright 2024, DornerWorks
//
// SPDX-License-Identifier: BSD-2-Clause
//

// pub mod net {
//     pub const MAC_ADDR: [u8; 6] = [0x00, 0x0A, 0x35, 0x03, 0x78, 0xA1];
// }

//! This Rust code defines a configuration module (`config.rs`) for the 
//! firewall-core package, specifying allowed TCP and UDP ports for filtering 
//! network traffic. It is a simple, standalone module with no external 
//! dependencies, designed to be used by other components like `rx_firewall.rs` 
//! to enforce port-based firewall rules.
//!
//! - **TCP Configuration**: The `tcp` submodule defines a constant array 
//!   `ALLOWED_PORTS` containing a single TCP port (5760), indicating which TCP 
//!   ports are permitted by the firewall.
//! - **UDP Configuration**: The `udp` submodule defines a constant array 
//!   `ALLOWED_PORTS` with a single UDP port (68), using a `NUM_UDP_PORTS` 
//!   constant to set the array size, specifying allowed UDP ports.
//! - **Purpose**: Provides a centralized, compile-time configuration for port 
//!   filtering, enabling the firewall to allow or block packets based on their 
//!   destination ports.

/// TCP configuration submodule for the firewall.
pub mod tcp {
    /// Array of allowed TCP ports for firewall filtering.
    ///
    /// Contains a single port, 5760, which packets must match to be permitted.
    pub const ALLOWED_PORTS: [u16; 1] = [5760u16];
}

/// UDP configuration submodule for the firewall.
pub mod udp {
    /// Number of allowed UDP ports (used to define array size).
    const NUM_UDP_PORTS: usize = 1;

    /// Array of allowed UDP ports for firewall filtering.
    ///
    /// Contains a single port, 68, which packets must match to be permitted.
    pub const ALLOWED_PORTS: [u16; NUM_UDP_PORTS] = [68u16];
}
