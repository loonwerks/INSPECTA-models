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
    test_apis::put_producer_producer_p_myArrayInt32_DataPort([0; Gubmo_Structs_Arrays::Gubmo_Structs_Arrays_MyArrayInt32_DIM_0]);
    test_apis::put_producer_producer_p_myArrayStruct_DataPort([Gubmo_Structs_Arrays::MyStruct2_i::default(); Gubmo_Structs_Arrays::Gubmo_Structs_Arrays_MyArrayStruct_DIM_0]);
    test_apis::put_producer_producer_p_myStructArray_DataPort(Gubmo_Structs_Arrays::MyStructArray_i::default());

    crate::domain_monitor_process_domain_monitor_thread_timeTriggered();
  }
}
