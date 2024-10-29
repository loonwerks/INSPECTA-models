//
// Copyright 2024, DornerWorks
//
// SPDX-License-Identifier: BSD-2-Clause
//

pub mod sizes {
    pub const DRIVER_DMA: usize = 0x20_0000;
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
