#![cfg(feature = "sel4")]

// This file will not be overwritten if codegen is rerun

use sel4::debug_print;
use sel4_logging::{LevelFilter, Logger, LoggerBuilder};

const LOG_LEVEL: LevelFilter = {
  // LevelFilter::Off // lowest level of logging
  // LevelFilter::Error
  // LevelFilter::Warn
  // LevelFilter::Info
  // LevelFilter::Debug
  LevelFilter::Trace // highest level of logging
};

pub static LOGGER: Logger = LoggerBuilder::const_default()
  .level_filter(LOG_LEVEL)
  .write(|s| debug_print!("{}", s))
  .build();
