// This file will not be overwritten if codegen is rerun

#[cfg(test)]
mod tests {
  // NOTE: need to run tests sequentially to prevent race conditions
  //       on the app and the testing apis which are static
  use serial_test::serial;

  use crate::compute_api;
  use crate::init_api;
  use crate::app;

  use crate::bridge::extern_c_api as extern_api;
  use crate::data::*;

  #[test]
  #[serial]
  fn test_initialization() {
    unsafe {
      app.initialize(&mut init_api);
    }
  }

  #[test]
  #[serial]
  fn test_compute() {
    unsafe {
      app.timeTriggered(&mut compute_api);
    }
  }
}
