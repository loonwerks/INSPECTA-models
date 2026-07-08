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
    crate::userland_monitor_process_userland_monitor_thread_initialize();
}

  #[test]
  #[serial]
  fn test_compute() {
    crate::userland_monitor_process_userland_monitor_thread_initialize();

    // populate incoming data ports
    test_apis::put_sched_state(hamr::SchedState::default());
    test_apis::put_sched_schedule(hamr::Schedule::default());

    crate::userland_monitor_process_userland_monitor_thread_timeTriggered();
  }
}
