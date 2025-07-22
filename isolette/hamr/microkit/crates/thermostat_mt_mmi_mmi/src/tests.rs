// This file will not be overwritten if codegen is rerun

#[cfg(test)]
mod tests {
  // NOTE: need to run tests sequentially to prevent race conditions
  //       on the app and the testing apis which are static
  use serial_test::serial;

  use crate::bridge::extern_c_api as extern_api;
  use data::*;
  use crate::bridge::thermostat_mt_mmi_mmi_GUMBOX as GUMBOX; // manually added
  use data::Isolette_Data_Model::*;                   // manually added

  #[test]
  #[serial]
  fn test_initialization() {
    unsafe {
      crate::thermostat_mt_mmi_mmi_initialize();
    }
  }
}
