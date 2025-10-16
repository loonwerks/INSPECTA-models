// This file will not be overwritten if codegen is rerun

use log::LevelFilter;

#[cfg(feature = "sel4")]
use sel4::debug_print;

#[cfg(feature = "sel4")]
use sel4_logging::{Logger, LoggerBuilder};

#[cfg(test)]
use std::sync::Once;

const LOG_LEVEL: LevelFilter = {
  // LevelFilter::Off // lowest level of logging
  // LevelFilter::Error
  // LevelFilter::Warn
  LevelFilter::Info
  // LevelFilter::Debug
  // LevelFilter::Trace // highest level of logging
};

#[cfg(feature = "sel4")]
pub static LOGGER: Logger = LoggerBuilder::const_default()
    .level_filter(LOG_LEVEL)
    .write(|s| debug_print!("{}", s))
    .build();

#[cfg(test)]
static INIT: Once = Once::new();

pub fn init_logging() {
    #[cfg(all(feature = "sel4", not(test)))]
    {
        LOGGER.set().unwrap();
    }

    #[cfg(test)]
    {
        INIT.call_once(|| {
            let _ = env_logger::builder()
                .is_test(cfg!(test))
                .filter_level(LOG_LEVEL)
                .try_init();
        });
    }
}
