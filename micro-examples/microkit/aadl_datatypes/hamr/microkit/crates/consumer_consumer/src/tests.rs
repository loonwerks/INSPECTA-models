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
    crate::consumer_consumer_initialize();
}

  #[test]
  #[serial]
  fn test_compute() {
    crate::consumer_consumer_initialize();

    test_api::put_myBoolean(Some(true));
    test_api::put_myInt64(Some(0));
    
    crate::consumer_consumer_timeTriggered();
  }
}
