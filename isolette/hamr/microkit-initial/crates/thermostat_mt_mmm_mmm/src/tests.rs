// This file will not be overwritten if codegen is rerun

#[cfg(test)]
mod tests {
  // NOTE: need to run tests sequentially to prevent race conditions
  //       on the app and the testing apis which are static
  use serial_test::serial;

  use crate::bridge::extern_c_api as extern_api;
  use crate::data::*;

  #[test]
  #[serial]
  fn test_initialization() {
    unsafe {
      crate::thermostat_mt_mmm_mmm_initialize();
    }
  }

  #[test]
  #[serial]
  fn test_compute() {
    unsafe {
      crate::thermostat_mt_mmm_mmm_initialize();

      crate::thermostat_mt_mmm_mmm_timeTriggered();
    }
  }
}
