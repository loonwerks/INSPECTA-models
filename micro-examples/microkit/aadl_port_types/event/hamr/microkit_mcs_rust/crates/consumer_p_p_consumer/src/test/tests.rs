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
    crate::consumer_p_p_consumer_initialize();
}

  #[test]
  #[serial]
  fn test_compute() {
    crate::consumer_p_p_consumer_initialize();
    crate::consumer_p_p_consumer_timeTriggered();
  }
}
