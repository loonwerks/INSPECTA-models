#![cfg(test)]

// This file will not be overwritten if codegen is rerun

mod tests {
  // NOTE: need to run tests sequentially to prevent race conditions
  //       on the app and the testing apis which are static
  use serial_test::serial;

  use crate::bridge::test_api;
  use data::*;

  #[test]
  #[serial]
  fn test_initialization() {
    unsafe {
      crate::seL4_ArduPilot_ArduPilot_initialize();
    }
  }

  #[test]
  #[serial]
  fn test_compute() {
    unsafe {
      crate::seL4_ArduPilot_ArduPilot_initialize();

      crate::seL4_ArduPilot_ArduPilot_timeTriggered();
    }
  }
}
