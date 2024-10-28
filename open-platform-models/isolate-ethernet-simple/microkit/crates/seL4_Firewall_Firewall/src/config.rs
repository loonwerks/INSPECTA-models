//
// Copyright 2024, DornerWorks
//
// SPDX-License-Identifier: BSD-2-Clause
//

pub mod net {
    pub const MAC_ADDR: [u8; 6] = [0x00, 0x0A, 0x35, 0x03, 0x78, 0xA1];
    pub const IPV4_ADDR: [u8; 4] = [192, 168, 60, 149];
}

pub mod log {
    use sel4::debug_print;
    use sel4_logging::{LevelFilter, Logger, LoggerBuilder};

    const LOG_LEVEL: LevelFilter = {
        // LevelFilter::Trace
        // LevelFilter::Debug
        LevelFilter::Info
        // LevelFilter::Warn
    };

    pub static LOGGER: Logger = LoggerBuilder::const_default()
        .level_filter(LOG_LEVEL)
        .write(|s| debug_print!("{}", s))
        .build();
}
