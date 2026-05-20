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
    test_apis::put_producer_p_p_producer_write_port(data_1_prod_2_cons_struct::struct_i::default());

    crate::domain_monitor_process_domain_monitor_thread_timeTriggered();
  }
}
