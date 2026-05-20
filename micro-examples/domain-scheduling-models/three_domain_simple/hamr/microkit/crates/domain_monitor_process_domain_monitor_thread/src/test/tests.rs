// This file will not be overwritten if HAMR codegen is rerun

mod tests {
  // NOTE: need to run tests sequentially to prevent race conditions
  //       on the app and the testing apis which are static
  use serial_test::serial;

  use crate::test::util::*;
  use data::*;

  #[test]
  #[serial]
  fn test_initialization() {
    crate::domain_monitor_process_domain_monitor_thread_initialize();
}

  #[test]
  #[serial]
  fn test_compute() {
    crate::domain_monitor_process_domain_monitor_thread_initialize();

    // populate incoming data ports
    test_apis::put_p1_t1_write_port(0);
    test_apis::put_p2_t2_write_port(0);

    crate::domain_monitor_process_domain_monitor_thread_timeTriggered();
  }
}
