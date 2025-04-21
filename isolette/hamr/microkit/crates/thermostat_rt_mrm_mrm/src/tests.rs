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
  use crate::bridge::thermostat_rt_mrm_mrm_GUMBOX as GUMBOX;
  use crate::data::*;
  use crate::data::Isolette_Data_Model::*; // manually added

  const failOnUnsatPrecondition: bool = false; // manually added
  
  #[test]
  #[serial]
  // ToDo: Add appropriate documentation for RustDoc
  //
  // John H Note: as a complete newbie (Rust newbie too, it tool me at least 30 minutes to write
  //  this test by looking at MHS exmaple.  I think it would help to include code
  //  for retrieving values from component output ports and GUMBO-declared local state.)
  fn test_initialization_REQ_MRM_1() {

    // [InvokeEntryPoint]: invoke initialize entry point

    unsafe {
      app.initialize(&mut init_api);
    }

    // [RetrieveOutState]: retrieve values of the output port
    // ToDo: consider auto-generating this (as an example)
    let regulator_mode = extern_api::OUT_regulator_mode
    .lock()
    .unwrap()
    .expect("Not expecting None");

    unsafe {
       // Retrieve value of GUMBO declared local component state
       // ToDo: consider auto-generating this as an example
      let lastRegulatorMode = app.lastRegulatorMode;

       // [CheckPost]: invoke the oracle function
       assert!(GUMBOX::initialize_IEP_Post(
          lastRegulatorMode,
          regulator_mode));

    // example of manual testing
    assert!(regulator_mode == Regulator_Mode::Init_Regulator_Mode);
    assert!(lastRegulatorMode == regulator_mode);
}

  }

  // John H Note: as a complete newbie (Rust newbie too, it tool me at least 30 minutes to write
  //  this test by looking at MHS exmaple.  I think it would help to include code
  //  for retrieving values from component output ports and GUMBO-declared local state.)
  #[test]
  #[serial]
  fn test_REQ_MRM_Maintain_Normal() {
    // generate values for the incoming ports and state variables
    let current_tempWstatus = TempWstatus_i {
      degrees: 96, // Value is relevant to test; only status (below) matters
      status: ValueStatus::Valid,
    };
    let interface_failure = Failure_Flag_i { flag: false };
    let internal_failure = Failure_Flag_i { flag: false };
    let In_lastRegulatorMode = Regulator_Mode::Normal_Regulator_Mode;

    // Note: There is no pre-condition for this component

    // [PutInPorts]: put values on the input ports
    *extern_api::IN_current_tempWstatus.lock().unwrap() = Some(current_tempWstatus);
    *extern_api::IN_interface_failure.lock().unwrap() = Some(interface_failure);
    *extern_api::IN_internal_failure.lock().unwrap() = Some(internal_failure);

    unsafe {
      // [SetInStateVars]: set the pre-state values of state variables
      app.lastRegulatorMode = In_lastRegulatorMode;

      app.timeTriggered(&mut compute_api);
    }

    // [RetrieveOutState]: retrieve values of the output port
    // ToDo: consider auto-generating this (as an example)
    let regulator_mode = extern_api::OUT_regulator_mode
    .lock()
    .unwrap()
    .expect("Not expecting None");

    unsafe {
       // Retrieve value of GUMBO declared local component state
       // ToDo: consider auto-generating this as an example
      let lastRegulatorMode = app.lastRegulatorMode;

       // [CheckPost]: invoke the oracle function
       assert!(GUMBOX::compute_CEP_Post(
          In_lastRegulatorMode,
          lastRegulatorMode,
          current_tempWstatus,
          interface_failure,
          internal_failure,
          regulator_mode));
    }
  }
}
