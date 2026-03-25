// Isolette_Properties.sysml
//
// Property constants corresponding to the AADL Isolette_Properties property set.
// In the AADL model these are referenced via Isolette_Properties::<name>; in SysML
// they are inlined as literal attribute values in each component definition.
// This file collects them in one place for documentation and cross-reference.

package Isolette_Properties {

  private import HAMR::*;

  // Thread dispatch period (Table A-9 reference period: 1000ms)
  attribute ThreadPeriod: Time = 1000 [ms];

  // Default compute execution time
  attribute Default_Compute_Execution_Time: Timing_Properties::Compute_Execution_Time = 60 [ms] .. 60[ms] as Timing_Properties::Compute_Execution_Time;

  // Stack size (217088 bytes; must be 4K-aligned for seL4)
  attribute StackSize: Stack_Size = 217088 [byte];

  // seL4/Microkit domain scheduling constants.
  // Each process part definition carries this value as: attribute :>> Domain = <n>;
  attribute Domain_TS: CASE_Scheduling::Domain  = 2 as CASE_Scheduling::Domain; // Temperature Sensor
  attribute Domain_MMM: CASE_Scheduling::Domain = 3 as CASE_Scheduling::Domain; // Manage Monitor Mode
  attribute Domain_MMI: CASE_Scheduling::Domain = 4 as CASE_Scheduling::Domain; // Manage Monitor Interface
  attribute Domain_MA: CASE_Scheduling::Domain  = 5 as CASE_Scheduling::Domain; // Manage Alarm
  attribute Domain_DMF: CASE_Scheduling::Domain = 6 as CASE_Scheduling::Domain; // Detect Monitor Failure
  attribute Domain_MRI: CASE_Scheduling::Domain = 7 as CASE_Scheduling::Domain; // Manage Regulator Interface
  attribute Domain_MRM: CASE_Scheduling::Domain = 8 as CASE_Scheduling::Domain; // Manage Regulator Mode
  attribute Domain_MHS: CASE_Scheduling::Domain = 9 as CASE_Scheduling::Domain; // Manage Heat Source
  attribute Domain_DRF: CASE_Scheduling::Domain = 10 as CASE_Scheduling::Domain; // Detect Regulator Failure
  attribute Domain_HS: CASE_Scheduling::Domain  = 11 as CASE_Scheduling::Domain; // Heat Source
  attribute Domain_OI: CASE_Scheduling::Domain  = 12 as CASE_Scheduling::Domain; // Operator Interface
}
