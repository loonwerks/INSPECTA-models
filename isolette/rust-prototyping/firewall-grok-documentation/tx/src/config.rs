//
// Copyright 2024, DornerWorks
//
// SPDX-License-Identifier: BSD-2-Clause
//

// pub mod net {
//     pub const MAC_ADDR: [u8; 6] = [0x00, 0x0A, 0x35, 0x03, 0x78, 0xA1];
// }

//! This Rust code defines a logging module (`log.rs`) for the transmit-side 
//! firewall, providing a `no_std`-compatible logging setup using the `sel4` 
//! microkernel’s debug output and the `sel4_logging` crate. It is conditionally 
//! compiled for non-test environments, ensuring minimal dependencies.
//!
//! - **Conditional Compilation**: The `#![cfg(not(test))]` directive ensures 
//!   this module is only included in production builds, avoiding standard 
//!   library dependencies in `no_std` contexts.
//! - **Logging Configuration**: The `log` submodule configures a static 
//!   `Logger` instance with a fixed log level (`Info`) and a custom output 
//!   function (`debug_print`) from `sel4`, integrating logging with the 
//!   microkernel’s debug facilities.
//! - **Purpose**: Provides a lightweight, compile-time logging solution for 
//!   `tx_firewall.rs`, enabling informational messages (e.g., "Init", 
//!   "Triggered") to be output via seL4’s debug mechanism without requiring a 
//!   full standard library.

/// Logging submodule for the transmit-side firewall.
#[cfg(not(test))]
pub mod log {
    use sel4::debug_print;
    use sel4_logging::{LevelFilter, Logger, LoggerBuilder};

    /// Compile-time constant defining the log level for the firewall.
    ///
    /// Set to `Info`, with commented options for `Trace`, `Debug`, and `Warn` 
    /// allowing easy reconfiguration.
    const LOG_LEVEL: LevelFilter = {
        // LevelFilter::Trace
        // LevelFilter::Debug
        LevelFilter::Info
        // LevelFilter::Warn
    };

    /// Static logger instance for the transmit firewall.
    ///
    /// Configured with the `Info` log level and outputs messages using 
    /// `sel4::debug_print` for `no_std` compatibility.
    pub static LOGGER: Logger = LoggerBuilder::const_default()
        .level_filter(LOG_LEVEL)
        .write(|s| debug_print!("{}", s))
        .build();
}

