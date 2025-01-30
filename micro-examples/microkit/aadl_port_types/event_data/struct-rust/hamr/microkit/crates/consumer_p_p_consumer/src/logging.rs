#[cfg(not(test))]

use sel4::debug_print;
use sel4_logging::{LevelFilter, Logger, LoggerBuilder};

const LOG_LEVEL: LevelFilter = {
    // LevelFilter::Off
    // LevelFilter::Error
    // LevelFilter::Warn
    LevelFilter::Info
    // LevelFilter::Debug
    // LevelFilter::Trace
};

pub static LOGGER: Logger = LoggerBuilder::const_default()
    .level_filter(LOG_LEVEL)
    .write(|s| debug_print!("{}", s))
    .build();
