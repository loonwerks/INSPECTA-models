use serial_test::serial;
use proptest::prelude::*;

use crate::test::util::test_apis as test_api;
use crate::test::util::generators as generator;
use crate::testInitializeCB_macro;
use crate::testComputeCB_macro;
use crate::testComputeCBwGSV_macro;
    
// number of valid (i.e., non-rejected) test cases that must be executed for the compute method.
const numValidComputeTestCases: u32 = 100;

// how many total test cases (valid + rejected) that may be attempted.
//   0 means all inputs must satisfy the precondition (if present), 
//   5 means at most 5 rejected inputs are allowed per valid test case
const computeRejectRatio: u32 = 5;

const verbosity: u32 = 2;

testInitializeCB_macro! {
    prop_testInitializeCB_macro, // test name
    config: ProptestConfig { // proptest configuration, built by overriding fields from default config
        cases: numValidComputeTestCases,
        max_global_rejects: numValidComputeTestCases * computeRejectRatio,
        verbose: verbosity,
        ..ProptestConfig::default()
    }
}

testComputeCB_macro! {
    prop_testComputeCB_macro, // test name
    config: ProptestConfig { // proptest configuration, built by overriding fields from default config
        cases: numValidComputeTestCases,
        max_global_rejects: numValidComputeTestCases * computeRejectRatio,
        verbose: verbosity,
        ..ProptestConfig::default()
    },
    // strategies for generating each component input
    /*
    api_current_tempWstatus: generator::Isolette_Data_Model_TempWstatus_i_strategy_default(),
    api_lower_desired_temp: generator::Isolette_Data_Model_Temp_i_strategy_default(),
    api_regulator_mode: generator::Isolette_Data_Model_Regulator_Mode_strategy_default(),
    api_upper_desired_temp: generator::Isolette_Data_Model_Temp_i_strategy_default()
    */
    api_current_tempWstatus: generator::Isolette_Data_Model_TempWstatus_i_strategy_cust(
        95..=103, 
        generator::Isolette_Data_Model_ValueStatus_strategy_default()),
    api_lower_desired_temp: generator::Isolette_Data_Model_Temp_i_strategy_cust(94..=105),
    api_regulator_mode: generator::Isolette_Data_Model_Regulator_Mode_strategy_default(),
    api_upper_desired_temp: generator::Isolette_Data_Model_Temp_i_strategy_cust(94..=105)
}

testComputeCBwGSV_macro! {
    prop_testComputeCBwGSV, // test name
    config: ProptestConfig { // proptest configuration, built by overriding fields from default config
        cases: numValidComputeTestCases,
        max_global_rejects: numValidComputeTestCases * computeRejectRatio,
        verbose: verbosity,
        ..ProptestConfig::default()
    },
    // strategies for generating each component input
    /*
    In_lastCmd: generator::Isolette_Data_Model_On_Off_strategy_default(),
    api_current_tempWstatus: generator::Isolette_Data_Model_TempWstatus_i_strategy_default(),
    api_lower_desired_temp: generator::Isolette_Data_Model_Temp_i_strategy_default(),
    api_regulator_mode: generator::Isolette_Data_Model_Regulator_Mode_strategy_default(),
    api_upper_desired_temp: generator::Isolette_Data_Model_Temp_i_strategy_default()
    */
    In_lastCmd: generator::Isolette_Data_Model_On_Off_strategy_default(),
    api_current_tempWstatus: generator::Isolette_Data_Model_TempWstatus_i_strategy_default(),
    api_lower_desired_temp: generator::Isolette_Data_Model_Temp_i_strategy_cust(94..=105),
    api_regulator_mode: generator::Isolette_Data_Model_Regulator_Mode_strategy_default(),
    api_upper_desired_temp: generator::Isolette_Data_Model_Temp_i_strategy_cust(94..=105)
}