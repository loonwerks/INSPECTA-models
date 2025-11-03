// This file will not be overwritten if codegen is rerun

mod tests {
  // NOTE: need to run tests sequentially to prevent race conditions
  //       on the app and the testing apis which are static
  use serial_test::serial;

  use crate::test::util::*;
  use data::*;

  #[test]
  #[serial]
  fn test_initialization() {
    crate::ArduPilot_ArduPilot_initialize();
}

  #[test]
  #[serial]
  fn test_compute() {
    crate::ArduPilot_ArduPilot_initialize();
    crate::ArduPilot_ArduPilot_timeTriggered();
  }
}
