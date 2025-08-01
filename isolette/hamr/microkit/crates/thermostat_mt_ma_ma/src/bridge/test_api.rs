#![cfg(test)]

// Do not edit this file as it will be overwritten if codegen is rerun

use crate::bridge::extern_c_api as extern_api;
use data::*;

use proptest::prelude::*;

use crate::bridge::thermostat_mt_ma_ma_GUMBOX as GUMBOX;

pub fn put_upper_alarm_temp(value: Isolette_Data_Model::Temp_i)
{
  *extern_api::IN_upper_alarm_temp.lock().unwrap() = Some(value)
}

pub fn put_lower_alarm_temp(value: Isolette_Data_Model::Temp_i)
{
  *extern_api::IN_lower_alarm_temp.lock().unwrap() = Some(value)
}

pub fn get_alarm_control() -> Isolette_Data_Model::On_Off
{
  return extern_api::OUT_alarm_control.lock().unwrap().expect("Not expecting None")
}

pub fn option_strategy_default
  <T: Clone + std::fmt::Debug, 
   S:  Strategy<Value = T>> (base: S) -> impl Strategy<Value = Option<T>>
{
  option_strategy_bias(1, base)
}

pub fn option_strategy_bias
  <T: Clone + std::fmt::Debug, 
   S:  Strategy<Value = T>> (
  bias: u32,
  base: S) -> impl Strategy<Value = Option<T>>
{
  prop_oneof![
    bias => base.prop_map(Some),
    1 => Just(None),
  ]
}

pub fn Isolette_Data_Model_ValueStatus_strategy_default() -> impl Strategy<Value = Isolette_Data_Model::ValueStatus>
{
  prop_oneof![
    Just(Isolette_Data_Model::ValueStatus::Valid),
    Just(Isolette_Data_Model::ValueStatus::Invalid)
  ]
}

pub fn Isolette_Data_Model_Regulator_Mode_strategy_default() -> impl Strategy<Value = Isolette_Data_Model::Regulator_Mode>
{
  prop_oneof![
    Just(Isolette_Data_Model::Regulator_Mode::Init_Regulator_Mode),
    Just(Isolette_Data_Model::Regulator_Mode::Normal_Regulator_Mode),
    Just(Isolette_Data_Model::Regulator_Mode::Failed_Regulator_Mode)
  ]
}

pub fn Isolette_Data_Model_Status_strategy_default() -> impl Strategy<Value = Isolette_Data_Model::Status>
{
  prop_oneof![
    Just(Isolette_Data_Model::Status::Init_Status),
    Just(Isolette_Data_Model::Status::On_Status),
    Just(Isolette_Data_Model::Status::Failed_Status)
  ]
}

pub fn Isolette_Data_Model_On_Off_strategy_default() -> impl Strategy<Value = Isolette_Data_Model::On_Off>
{
  prop_oneof![
    Just(Isolette_Data_Model::On_Off::Onn),
    Just(Isolette_Data_Model::On_Off::Off)
  ]
}

pub fn Isolette_Data_Model_Monitor_Mode_strategy_default() -> impl Strategy<Value = Isolette_Data_Model::Monitor_Mode>
{
  prop_oneof![
    Just(Isolette_Data_Model::Monitor_Mode::Init_Monitor_Mode),
    Just(Isolette_Data_Model::Monitor_Mode::Normal_Monitor_Mode),
    Just(Isolette_Data_Model::Monitor_Mode::Failed_Monitor_Mode)
  ]
}

pub fn Isolette_Environment_Heat_strategy_default() -> impl Strategy<Value = Isolette_Environment::Heat>
{
  prop_oneof![
    Just(Isolette_Environment::Heat::Dummy_Head_Enum)
  ]
}

pub fn Isolette_Data_Model_Temp_i_strategy_default() -> impl Strategy<Value = Isolette_Data_Model::Temp_i>
{
  Isolette_Data_Model_Temp_i_stategy_cust(
    any::<i32>()
  )
}

pub fn Isolette_Data_Model_Temp_i_stategy_cust<i32_strategy: Strategy<Value = i32>> (degrees_strategy: i32_strategy) -> impl Strategy<Value = Isolette_Data_Model::Temp_i>
{
  (degrees_strategy).prop_map(|(degrees)| {
    Isolette_Data_Model::Temp_i { degrees }
  })
}

pub fn Isolette_Data_Model_PhysicalTemp_i_strategy_default() -> impl Strategy<Value = Isolette_Data_Model::PhysicalTemp_i>
{
  Isolette_Data_Model_PhysicalTemp_i_stategy_cust(
    any::<i32>()
  )
}

pub fn Isolette_Data_Model_PhysicalTemp_i_stategy_cust<i32_strategy: Strategy<Value = i32>> (degrees_strategy: i32_strategy) -> impl Strategy<Value = Isolette_Data_Model::PhysicalTemp_i>
{
  (degrees_strategy).prop_map(|(degrees)| {
    Isolette_Data_Model::PhysicalTemp_i { degrees }
  })
}

pub fn Isolette_Data_Model_TempWstatus_i_strategy_default() -> impl Strategy<Value = Isolette_Data_Model::TempWstatus_i>
{
  Isolette_Data_Model_TempWstatus_i_stategy_cust(
    any::<i32>(),
    Isolette_Data_Model_ValueStatus_strategy_default()
  )
}

pub fn Isolette_Data_Model_TempWstatus_i_stategy_cust
  <i32_strategy: Strategy<Value = i32>, 
   Isolette_Data_Model_ValueStatus_strategy: Strategy<Value = Isolette_Data_Model::ValueStatus>> (
  degrees_strategy: i32_strategy,
  status_strategy: Isolette_Data_Model_ValueStatus_strategy) -> impl Strategy<Value = Isolette_Data_Model::TempWstatus_i>
{
  (degrees_strategy, status_strategy).prop_map(|(degrees, status)| {
    Isolette_Data_Model::TempWstatus_i { degrees, status }
  })
}

pub fn Isolette_Data_Model_Failure_Flag_i_strategy_default() -> impl Strategy<Value = Isolette_Data_Model::Failure_Flag_i>
{
  Isolette_Data_Model_Failure_Flag_i_stategy_cust(
    any::<bool>()
  )
}

pub fn Isolette_Data_Model_Failure_Flag_i_stategy_cust<bool_strategy: Strategy<Value = bool>> (flag_strategy: bool_strategy) -> impl Strategy<Value = Isolette_Data_Model::Failure_Flag_i>
{
  (flag_strategy).prop_map(|(flag)| {
    Isolette_Data_Model::Failure_Flag_i { flag }
  })
}

pub fn put_monitor_mode(value: Isolette_Data_Model::Monitor_Mode)
{
  *extern_api::IN_monitor_mode.lock().unwrap() = Some(value)
}

pub fn put_current_tempWstatus(value: Isolette_Data_Model::TempWstatus_i)
{
  *extern_api::IN_current_tempWstatus.lock().unwrap() = Some(value)
}

#[cfg(test)]
pub fn get_lastCmd() -> Isolette_Data_Model::On_Off
{
  unsafe {
    match &crate::app {
      Some(inner) => inner.lastCmd,
      None => panic!("The app is None")
    }
  }
}

#[cfg(test)]
pub fn put_lastCmd(value: Isolette_Data_Model::On_Off)
{
  unsafe {
    match &mut crate::app {
      Some(inner) => inner.lastCmd = value,
      None => panic!("The app is None")
    }
  }
}

/** Contract-based test harness for the initialize entry point
  */
pub fn testInitializeCB() -> Result<(), TestCaseError>
{
  // [InvokeEntryPoint]: Invoke the entry point
  crate::thermostat_mt_ma_ma_initialize();

  // [RetrieveOutState]: retrieve values of the output ports via get operations and GUMBO declared local state variable
  let lastCmd = get_lastCmd();
  let api_alarm_control = get_alarm_control();

  // [CheckPost]: invoke the oracle function
  prop_assert!(
    GUMBOX::initialize_IEP_Post(
      lastCmd,
      api_alarm_control
    ),
    "Postcondition failed: incorrect output behavior"
  );

  // Return Ok(()) if all assertions pass
  Ok(())
}

#[macro_export]
macro_rules!
testInitializeCB_macro {
  (
    $test_name: ident,
    config: $config:expr
  ) => {
    proptest!{
      #![proptest_config($config)]
      #[test]
      #[serial]
      fn $test_name(empty in ::proptest::strategy::Just(())) {
        $crate::bridge::test_api::testInitializeCB()?;
      }
    }
  };
}

/** Contract-based test harness for the compute entry point
  *
  * @param api_current_tempWstatus incoming data port
  * @param api_lower_alarm_temp incoming data port
  * @param api_monitor_mode incoming data port
  * @param api_upper_alarm_temp incoming data port
  */
pub fn testComputeCB(
  api_current_tempWstatus: Isolette_Data_Model::TempWstatus_i,
  api_lower_alarm_temp: Isolette_Data_Model::Temp_i,
  api_monitor_mode: Isolette_Data_Model::Monitor_Mode,
  api_upper_alarm_temp: Isolette_Data_Model::Temp_i) -> Result<(), TestCaseError>
{
  // Initialize the app
  crate::thermostat_mt_ma_ma_initialize();

  // [SaveInLocal]: retrieve and save the current (input) values of GUMBO-declared local state variables as retrieved
  //                from the component state
  let In_lastCmd: Isolette_Data_Model::On_Off = get_lastCmd();

  // [CheckPre]: check/filter based on pre-condition.
  prop_assume! {
    GUMBOX::compute_CEP_Pre (
      In_lastCmd,
      api_current_tempWstatus,
      api_lower_alarm_temp,
      api_monitor_mode,
      api_upper_alarm_temp
    ),
     "Precondition failed: invalid input combination"
  }

  // [PutInPorts]: Set values on the input ports
  put_current_tempWstatus(api_current_tempWstatus);
  put_lower_alarm_temp(api_lower_alarm_temp);
  put_monitor_mode(api_monitor_mode);
  put_upper_alarm_temp(api_upper_alarm_temp);

  // [InvokeEntryPoint]: Invoke the entry point
  crate::thermostat_mt_ma_ma_timeTriggered();

  // [RetrieveOutState]: retrieve values of the output ports via get operations and GUMBO declared local state variable
  let lastCmd = get_lastCmd();
  let api_alarm_control = get_alarm_control();

  // [CheckPost]: invoke the oracle function
  prop_assert!(
    GUMBOX::compute_CEP_Post(
      In_lastCmd,
      lastCmd,
      api_current_tempWstatus,
      api_lower_alarm_temp,
      api_monitor_mode,
      api_upper_alarm_temp,
      api_alarm_control
    ),
    "Postcondition failed: incorrect output behavior"
  );

  // Return Ok(()) if all assertions pass
  Ok(())
}

#[macro_export]
macro_rules!
testComputeCB_macro {
  (
    $test_name: ident,
    config: $config:expr,
    api_current_tempWstatus: $api_current_tempWstatus_strat:expr,
    api_lower_alarm_temp: $api_lower_alarm_temp_strat:expr,
    api_monitor_mode: $api_monitor_mode_strat:expr,
    api_upper_alarm_temp: $api_upper_alarm_temp_strat:expr
  ) => {
    proptest!{
      #![proptest_config($config)]
      #[test]
      #[serial]
      fn $test_name(
        (api_current_tempWstatus, api_lower_alarm_temp, api_monitor_mode, api_upper_alarm_temp)
        in ($api_current_tempWstatus_strat, $api_lower_alarm_temp_strat, $api_monitor_mode_strat, $api_upper_alarm_temp_strat)
      ) {
        $crate::bridge::test_api::testComputeCB(
          api_current_tempWstatus,
          api_lower_alarm_temp,
          api_monitor_mode,
          api_upper_alarm_temp
        )?;
      }
    }
  };
}

/** Contract-based test harness for the compute entry point
  *
  * @param In_lastCmd pre-state state variable
  * @param api_current_tempWstatus incoming data port
  * @param api_lower_alarm_temp incoming data port
  * @param api_monitor_mode incoming data port
  * @param api_upper_alarm_temp incoming data port
  */
pub fn testComputeCBwLV(
  In_lastCmd: Isolette_Data_Model::On_Off,
  api_current_tempWstatus: Isolette_Data_Model::TempWstatus_i,
  api_lower_alarm_temp: Isolette_Data_Model::Temp_i,
  api_monitor_mode: Isolette_Data_Model::Monitor_Mode,
  api_upper_alarm_temp: Isolette_Data_Model::Temp_i) -> Result<(), TestCaseError>
{
  // Initialize the app
  crate::thermostat_mt_ma_ma_initialize();

  // [CheckPre]: check/filter based on pre-condition.
  prop_assume! {
    GUMBOX::compute_CEP_Pre (
      In_lastCmd,
      api_current_tempWstatus,
      api_lower_alarm_temp,
      api_monitor_mode,
      api_upper_alarm_temp
    ),
     "Precondition failed: invalid input combination"
  }

  // [PutInPorts]: Set values on the input ports
  put_current_tempWstatus(api_current_tempWstatus);
  put_lower_alarm_temp(api_lower_alarm_temp);
  put_monitor_mode(api_monitor_mode);
  put_upper_alarm_temp(api_upper_alarm_temp);

  // [SetInStateVars]: set the pre-state values of state variables
  put_lastCmd(In_lastCmd);

  // [InvokeEntryPoint]: Invoke the entry point
  crate::thermostat_mt_ma_ma_timeTriggered();

  // [RetrieveOutState]: retrieve values of the output ports via get operations and GUMBO declared local state variable
  let lastCmd = get_lastCmd();
  let api_alarm_control = get_alarm_control();

  // [CheckPost]: invoke the oracle function
  prop_assert!(
    GUMBOX::compute_CEP_Post(
      In_lastCmd,
      lastCmd,
      api_current_tempWstatus,
      api_lower_alarm_temp,
      api_monitor_mode,
      api_upper_alarm_temp,
      api_alarm_control
    ),
    "Postcondition failed: incorrect output behavior"
  );

  // Return Ok(()) if all assertions pass
  Ok(())
}

#[macro_export]
macro_rules!
testComputeCBwLV_macro {
  (
    $test_name: ident,
    config: $config:expr,
    In_lastCmd: $In_lastCmd_strat:expr,
    api_current_tempWstatus: $api_current_tempWstatus_strat:expr,
    api_lower_alarm_temp: $api_lower_alarm_temp_strat:expr,
    api_monitor_mode: $api_monitor_mode_strat:expr,
    api_upper_alarm_temp: $api_upper_alarm_temp_strat:expr
  ) => {
    proptest!{
      #![proptest_config($config)]
      #[test]
      #[serial]
      fn $test_name(
        (In_lastCmd, api_current_tempWstatus, api_lower_alarm_temp, api_monitor_mode, api_upper_alarm_temp)
        in ($In_lastCmd_strat, $api_current_tempWstatus_strat, $api_lower_alarm_temp_strat, $api_monitor_mode_strat, $api_upper_alarm_temp_strat)
      ) {
        $crate::bridge::test_api::testComputeCBwLV(
          In_lastCmd,
          api_current_tempWstatus,
          api_lower_alarm_temp,
          api_monitor_mode,
          api_upper_alarm_temp
        )?;
      }
    }
  };
}