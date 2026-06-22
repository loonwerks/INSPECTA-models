//@ HAMR: --platform JVM --runtime-monitoring --output-dir ../hamr --package-name isolette
//@ HAMR: --platform JVM --runtime-monitoring --slang-output-dir ../hamr/slang-initial --package-name isolette
//@ HAMR: --platform Microkit --runtime-monitoring --output-dir ../hamr
//@ HAMR: --platform Microkit --runtime-monitoring --sel4-output-dir ../hamr/microkit-initial

// Isolette.sysml
//
// =====================================================================================================================
//
//   I s o l e t t e     P a c k a g e 
// 
//
//   Top-level system specification including
//
//      - thermostat + temp sensor and heater hardware components
//      - system boundary capturing interactions with the environment
//
// =====================================================================================================================
 
// authors Brian Larson and John Hatcliff
// translated to SysMLv2 by Clint McKenzie

package Isolette {
  
  private import Isolette_Data_Model::*;
  private import Regulate::*;
  private import Monitor::*;
  private import HAMR::*;
    
  // top-level system component representing the Isolette
  part def Isolette_Single_Sensor :> System {
    
    // Subcomponents
    
    part thermostat : Thermostat::Thermostat_Single_Sensor_System_i;
    
    part operator_interface : Operator_Interface::Operator_Interface_System_i;
    
    part temperature_sensor : Devices::Temperature_Sensor_System_i;

    part heat_source : Devices::Heat_Source_System_i;
    
    part isolette_processor: Isolette_Processor;
        
    // Ports
    
    // models (abstractly) warming of air inside of Isolette enclosure
    port heat_out : DataPort { out :>> type : Isolette_Environment::Heat; }
    // models (abstractly) commands given by the operator to Isolette operator interface
    port operator_commands : DataPort { in :>> type : Isolette_Environment::Interface_Interaction; }
    // models (abstractly) visual information presented to the operator on Isolette operator interface
    port operator_visual_information : DataPort { out :>> type : Isolette_Environment::Interface_Interaction; }
    // models (abstraction) audio information presented to the operator on Isolette operator interface
    port operator_alarm : DataPort { out :>> type : Isolette_Environment::Interface_Interaction; }
    // models (abstractly) the sensing of the physical air temperature
    port air_temperature : DataPort { in :>> type : Isolette_Data_Model::PhysicalTemp_i; }
    
    // Connections
    
    // ==== INPUT interface to internal components ==== 	    
    // commands from operator flow into the operator interface
    connection oioc : PortConnection
      connect operator_commands to operator_interface.operator_commands;
    // the environment air temperature (abstract/physical) flows into the temp sensor
    connection a2ts : PortConnection
      connect air_temperature to temperature_sensor.air;

    // ==== OUTPUT interface values from internal components ==== 
    // information (audio and visual) flows from the operator interface to the operator
    connection oiovi : PortConnection
      connect operator_interface.operator_visual_information to operator_visual_information;
    connection oioa : PortConnection
      connect operator_interface.operator_alarm to operator_alarm;

    // heat source's (abstract) output is increase in physical air temperature
    connection hs : PortConnection
      connect heat_source.heat_out to heat_out;

    // ==== INTERNAL communication ==== 
    // sensor sends sensed current temperature to thermostat  
    connection ct : PortConnection
      connect temperature_sensor.current_tempWstatus to thermostat.current_tempWstatus;
    
    // thermostat controls turns the heat source off and on  
    connection hc : PortConnection
      connect thermostat.heat_control to heat_source.heat_control;

    // operator interface communicates desired temperature to thermostat
    connection ldt : PortConnection
      connect operator_interface.lower_desired_tempWstatus to thermostat.lower_desired_tempWstatus;
    connection udt : PortConnection
      connect operator_interface.upper_desired_tempWstatus to thermostat.upper_desired_tempWstatus;

    // operator interface communicates alarm temperature to thermostat
    connection lat : PortConnection
      connect operator_interface.lower_alarm_tempWstatus to thermostat.lower_alarm_tempWstatus;
    connection uat : PortConnection
      connect operator_interface.upper_alarm_tempWstatus to thermostat.upper_alarm_tempWstatus;

    // thermostat communicates regulator status to display on operator interface
    connection rs : PortConnection
      connect thermostat.regulator_status to operator_interface.regulator_status;
    // thermostat communicates monitor status to display on operator interface
    connection ms : PortConnection
      connect thermostat.monitor_status to operator_interface.monitor_status;
    // thermostat communicates current sensed temperature to display on operator interface
    connection dt : PortConnection
      connect thermostat.display_temperature to operator_interface.display_temperature;
    // thermostat communicates alarm information to display on operator interface
    connection al : PortConnection
      connect thermostat.alarm_control to operator_interface.alarm_control;

    allocation pb0: Deployment_Properties::Actual_Processor_Binding 
      allocate thermostat to isolette_processor;

    allocation pb1: Deployment_Properties::Actual_Processor_Binding 
      allocate operator_interface to isolette_processor;

    allocation pb2: Deployment_Properties::Actual_Processor_Binding 
      allocate temperature_sensor to isolette_processor;

    allocation pb3: Deployment_Properties::Actual_Processor_Binding 
      allocate heat_source to isolette_processor;

		language "GUMBO" /*{

			functions

				def sysProp_NormalModeHeatOnn(
						regulator_mode: Isolette_Data_Model::Regulator_Mode,
						current_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						lower_desired_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						upper_desired_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						internal_failure: Isolette_Data_Model::Failure_Flag_i,
						heat_control: Isolette_Data_Model::On_Off
						): Base_Types::Boolean :=
					'->:'(
            lower_desired_tempWstatus.status == Isolette_Data_Model::ValueStatus.Valid
					    & upper_desired_tempWstatus.status == Isolette_Data_Model::ValueStatus.Valid
					    & current_tempWstatus.status == Isolette_Data_Model::ValueStatus.Valid
					    & regulator_mode == Isolette_Data_Model::Regulator_Mode.Normal_Regulator_Mode
					    & current_tempWstatus.degrees < lower_desired_tempWstatus.degrees,
					  heat_control == Isolette_Data_Model::On_Off.Onn);

				// Cross-component mode-implication safety properties: a Failed mode forces the
				// safe output. Regulator: Failed -> heat off (REQ_MHS_5). Monitor: Failed ->
				// alarm on (REQ_MA_5). Conditioned on the output mode, so no prior-mode reference.
				def sysProp_FailedModeImpliesHeatOff(
						regulator_mode: Isolette_Data_Model::Regulator_Mode,
						heat_control: Isolette_Data_Model::On_Off
						): Base_Types::Boolean :=
					'->:'(
            regulator_mode == Isolette_Data_Model::Regulator_Mode.Failed_Regulator_Mode,
					  heat_control == Isolette_Data_Model::On_Off.Off);

				// MRM mode-memory bridge: after MRM fires, the latched mode equals the
				// freshly computed output mode (MRM's update_lastRegulatorMode guarantee).
				// Lets a property phrased on lastRegulatorMode pick up MHS's REQ_MHS_5
				// (which is phrased on regulator_mode) at the MRM->MHS boundary.
				def sysProp_RegulatorModeMemory(
						lastRegulatorMode: Isolette_Data_Model::Regulator_Mode,
						regulator_mode: Isolette_Data_Model::Regulator_Mode
						): Base_Types::Boolean :=
					lastRegulatorMode == regulator_mode;

				def sysProp_FailedModeImpliesAlarmOn(
						monitor_mode: Isolette_Data_Model::Monitor_Mode,
						alarm_control: Isolette_Data_Model::On_Off
						): Base_Types::Boolean :=
					'->:'(
            monitor_mode == Isolette_Data_Model::Monitor_Mode.Failed_Monitor_Mode,
					  alarm_control == Isolette_Data_Model::On_Off.Onn);

				// In NORMAL mode the displayed temperature reflects the current temperature
				// (REQ_MRI_4: displayed_temp == ROUND(current_temp); ROUND is the identity).
				def sysProp_NormalDisplayTemp(
						regulator_mode: Isolette_Data_Model::Regulator_Mode,
						current_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						displayed_temp: Isolette_Data_Model::Temp_i
						): Base_Types::Boolean :=
					'->:'(
            regulator_mode == Isolette_Data_Model::Regulator_Mode.Normal_Regulator_Mode,
					  displayed_temp.degrees == current_tempWstatus.degrees);

				def sysProp_NormalModeHeatOff(
						regulator_mode: Isolette_Data_Model::Regulator_Mode,
						current_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						lower_desired_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						upper_desired_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						internal_failure: Isolette_Data_Model::Failure_Flag_i,
						heat_control: Isolette_Data_Model::On_Off
						): Base_Types::Boolean :=
					'->:'(
            lower_desired_tempWstatus.status == Isolette_Data_Model::ValueStatus.Valid
					    & upper_desired_tempWstatus.status == Isolette_Data_Model::ValueStatus.Valid
					    & current_tempWstatus.status == Isolette_Data_Model::ValueStatus.Valid
					    & regulator_mode == Isolette_Data_Model::Regulator_Mode.Normal_Regulator_Mode
					    & current_tempWstatus.degrees > upper_desired_tempWstatus.degrees,
					  heat_control == Isolette_Data_Model::On_Off.Off);

				// Failure -> heat off, via the regulator leaving Normal mode.
				// Under any regulator error condition the mode is non-Normal (Normal -> Failed
				// by REQ_MRM_3; Failed stays Failed; Init stays Init by REQ_MRM_Maintain_Init or
				// times out to Failed by REQ_MRM_4) -- never Normal. MHS then forces heat off in
				// both non-Normal modes (REQ_MHS_1 for Init, REQ_MHS_5 for Failed).

				def sysProp_CTInvalidImpliesRegulatorNotNormal(
						current_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						regulator_mode: Isolette_Data_Model::Regulator_Mode
						): Base_Types::Boolean :=
					'->:'(
            current_tempWstatus.status != Isolette_Data_Model::ValueStatus.Valid,
					  regulator_mode != Isolette_Data_Model::Regulator_Mode.Normal_Regulator_Mode);

				def sysProp_InvalidCTHeatOff(
						current_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						heat_control: Isolette_Data_Model::On_Off
						): Base_Types::Boolean :=
					'->:'(
            current_tempWstatus.status != Isolette_Data_Model::ValueStatus.Valid,
					  heat_control == Isolette_Data_Model::On_Off.Off);

				def sysProp_InvalidUDTImpliesRegulatorNotNormal(
						upper_desired_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						regulator_mode: Isolette_Data_Model::Regulator_Mode
						): Base_Types::Boolean :=
					'->:'(
            upper_desired_tempWstatus.status != Isolette_Data_Model::ValueStatus.Valid,
					  regulator_mode != Isolette_Data_Model::Regulator_Mode.Normal_Regulator_Mode);

				def sysProp_InvalidUDTHeatOff(
						upper_desired_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						heat_control: Isolette_Data_Model::On_Off
						): Base_Types::Boolean :=
					'->:'(
            upper_desired_tempWstatus.status != Isolette_Data_Model::ValueStatus.Valid,
					  heat_control == Isolette_Data_Model::On_Off.Off);

				def sysProp_InvalidLDTImpliesRegulatorNotNormal(
						lower_desired_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						regulator_mode: Isolette_Data_Model::Regulator_Mode
						): Base_Types::Boolean :=
					'->:'(
            lower_desired_tempWstatus.status != Isolette_Data_Model::ValueStatus.Valid,
					  regulator_mode != Isolette_Data_Model::Regulator_Mode.Normal_Regulator_Mode);

				def sysProp_InvalidLDTHeatOff(
						lower_desired_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						heat_control: Isolette_Data_Model::On_Off
						): Base_Types::Boolean :=
					'->:'(
            lower_desired_tempWstatus.status != Isolette_Data_Model::ValueStatus.Valid,
					  heat_control == Isolette_Data_Model::On_Off.Off);

				def sysProp_InternalFailureImpliesRegulatorNotNormal(
						internal_failure: Isolette_Data_Model::Failure_Flag_i,
						regulator_mode: Isolette_Data_Model::Regulator_Mode
						): Base_Types::Boolean :=
					'->:'(
            internal_failure.flag,
					  regulator_mode != Isolette_Data_Model::Regulator_Mode.Normal_Regulator_Mode);

				def sysProp_InternalFailureHeatOff(
						internal_failure: Isolette_Data_Model::Failure_Flag_i,
						heat_control: Isolette_Data_Model::On_Off
						): Base_Types::Boolean :=
					'->:'(
            internal_failure.flag,
					  heat_control == Isolette_Data_Model::On_Off.Off);

				def sysProp_ErrorConditionImpliesRegulatorNotNormal(
						lower_desired_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						upper_desired_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						current_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						internal_failure: Isolette_Data_Model::Failure_Flag_i,
						regulator_mode: Isolette_Data_Model::Regulator_Mode
						): Base_Types::Boolean :=
					'->:'(
            (lower_desired_tempWstatus.status != Isolette_Data_Model::ValueStatus.Valid)
					    | (upper_desired_tempWstatus.status != Isolette_Data_Model::ValueStatus.Valid)
					    | (current_tempWstatus.status != Isolette_Data_Model::ValueStatus.Valid)
					    | internal_failure.flag,
					  regulator_mode != Isolette_Data_Model::Regulator_Mode.Normal_Regulator_Mode);

				def sysProp_ErrorConditionHeatOff(
						lower_desired_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						upper_desired_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						current_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						internal_failure: Isolette_Data_Model::Failure_Flag_i,
						heat_control: Isolette_Data_Model::On_Off
						): Base_Types::Boolean :=
					'->:'(
            (lower_desired_tempWstatus.status != Isolette_Data_Model::ValueStatus.Valid)
					    | (upper_desired_tempWstatus.status != Isolette_Data_Model::ValueStatus.Valid)
					    | (current_tempWstatus.status != Isolette_Data_Model::ValueStatus.Valid)
					    | internal_failure.flag,
					  heat_control == Isolette_Data_Model::On_Off.Off);

				def sysProp_REQ_MRI_7(
						lower_desired_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						upper_desired_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						interface_failure: Isolette_Data_Model::Failure_Flag_i
						): Base_Types::Boolean :=
					(interface_failure.flag == (not(
						(upper_desired_tempWstatus.status == Isolette_Data_Model::ValueStatus.Valid)
						& (lower_desired_tempWstatus.status == Isolette_Data_Model::ValueStatus.Valid))));

				def sysProp_REQ_MRI_8(
						lower_desired_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						upper_desired_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						lower_desired_temp: Isolette_Data_Model::Temp_i,
						upper_desired_temp: Isolette_Data_Model::Temp_i,
						interface_failure: Isolette_Data_Model::Failure_Flag_i
						): Base_Types::Boolean :=
					'->:'(
            not interface_failure.flag,
					  lower_desired_temp.degrees == lower_desired_tempWstatus.degrees
							& upper_desired_temp.degrees == upper_desired_tempWstatus.degrees);

				def sysProp_lower_is_not_higher_than_upper(
						lower_desired_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						upper_desired_tempWstatus: Isolette_Data_Model::TempWstatus_i
						): Base_Types::Boolean :=
					lower_desired_tempWstatus.degrees <= upper_desired_tempWstatus.degrees;

				def sysProp_lower_is_lower_temp(
						lower_desired_temp: Isolette_Data_Model::Temp_i,
						upper_desired_temp: Isolette_Data_Model::Temp_i
						): Base_Types::Boolean :=
					lower_desired_temp.degrees <= upper_desired_temp.degrees;

				def sysProp_NormalModeAlarmOn(
						lower_alarm_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						upper_alarm_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						current_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						monitor_mode: Isolette_Data_Model::Monitor_Mode,
						interface_failure: Isolette_Data_Model::Failure_Flag_i,
						alarm_control: Isolette_Data_Model::On_Off
						): Base_Types::Boolean :=
					'->:'(
            lower_alarm_tempWstatus.status == Isolette_Data_Model::ValueStatus.Valid
					    & upper_alarm_tempWstatus.status == Isolette_Data_Model::ValueStatus.Valid
					    & current_tempWstatus.status == Isolette_Data_Model::ValueStatus.Valid
					    & monitor_mode == Isolette_Data_Model::Monitor_Mode.Normal_Monitor_Mode
					    & (current_tempWstatus.degrees < lower_alarm_tempWstatus.degrees
						    | current_tempWstatus.degrees > upper_alarm_tempWstatus.degrees),
					  alarm_control == Isolette_Data_Model::On_Off.Onn);

				def sysProp_NormalModeAlarmOff(
						lower_alarm_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						upper_alarm_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						current_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						monitor_mode: Isolette_Data_Model::Monitor_Mode,
						interface_failure: Isolette_Data_Model::Failure_Flag_i,
						alarm_control: Isolette_Data_Model::On_Off
						): Base_Types::Boolean :=
					'->:'(
            lower_alarm_tempWstatus.status == Isolette_Data_Model::ValueStatus.Valid
					    & upper_alarm_tempWstatus.status == Isolette_Data_Model::ValueStatus.Valid
					    & current_tempWstatus.status == Isolette_Data_Model::ValueStatus.Valid
					    & monitor_mode == Isolette_Data_Model::Monitor_Mode.Normal_Monitor_Mode
					    & current_tempWstatus.degrees >= lower_alarm_tempWstatus.degrees + 1[s32]
					    & current_tempWstatus.degrees <= upper_alarm_tempWstatus.degrees - 1[s32],
					  alarm_control == Isolette_Data_Model::On_Off.Off);

				// Monitor failure -> alarm on (in steady state). An invalid current temperature
				// keeps the monitor out of NORMAL (REQ_MMM_2 iff for Init, REQ_MMM_3 iff for Normal,
				// Failed_Mode_Absorbing for Failed); guarded by monitor_mode != Init (past startup),
				// the mode is Failed, and MA raises the alarm (REQ_MA_5).
				def sysProp_InvalidCTMonitorNotNormal(
						current_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						monitor_mode: Isolette_Data_Model::Monitor_Mode
						): Base_Types::Boolean :=
					'->:'(
            current_tempWstatus.status != Isolette_Data_Model::ValueStatus.Valid,
					  monitor_mode != Isolette_Data_Model::Monitor_Mode.Normal_Monitor_Mode);

				def sysProp_InvalidCTAlarmOn(
						current_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						monitor_mode: Isolette_Data_Model::Monitor_Mode,
						alarm_control: Isolette_Data_Model::On_Off
						): Base_Types::Boolean :=
					'->:'(
            current_tempWstatus.status != Isolette_Data_Model::ValueStatus.Valid
					    & monitor_mode != Isolette_Data_Model::Monitor_Mode.Init_Monitor_Mode,
					  alarm_control == Isolette_Data_Model::On_Off.Onn);

				// REQ-MMI-4 (converse of REQ-MMI-5): an invalid alarm temperature forces a monitor
				// interface failure. Lets an invalid alarm temp drive monitor_status false -> mode
				// out of Normal, the way an invalid current temp does directly.
				def sysProp_REQ_MMI_4(
						lower_alarm_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						upper_alarm_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						interface_failure: Isolette_Data_Model::Failure_Flag_i
						): Base_Types::Boolean :=
					'->:'(
            lower_alarm_tempWstatus.status != Isolette_Data_Model::ValueStatus.Valid
					    | upper_alarm_tempWstatus.status != Isolette_Data_Model::ValueStatus.Valid,
					  interface_failure.flag);

				def sysProp_InvalidLATMonitorNotNormal(
						lower_alarm_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						monitor_mode: Isolette_Data_Model::Monitor_Mode
						): Base_Types::Boolean :=
					'->:'(
            lower_alarm_tempWstatus.status != Isolette_Data_Model::ValueStatus.Valid,
					  monitor_mode != Isolette_Data_Model::Monitor_Mode.Normal_Monitor_Mode);

				def sysProp_InvalidLATAlarmOn(
						lower_alarm_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						monitor_mode: Isolette_Data_Model::Monitor_Mode,
						alarm_control: Isolette_Data_Model::On_Off
						): Base_Types::Boolean :=
					'->:'(
            lower_alarm_tempWstatus.status != Isolette_Data_Model::ValueStatus.Valid
					    & monitor_mode != Isolette_Data_Model::Monitor_Mode.Init_Monitor_Mode,
					  alarm_control == Isolette_Data_Model::On_Off.Onn);

				def sysProp_InvalidUATMonitorNotNormal(
						upper_alarm_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						monitor_mode: Isolette_Data_Model::Monitor_Mode
						): Base_Types::Boolean :=
					'->:'(
            upper_alarm_tempWstatus.status != Isolette_Data_Model::ValueStatus.Valid,
					  monitor_mode != Isolette_Data_Model::Monitor_Mode.Normal_Monitor_Mode);

				def sysProp_InvalidUATAlarmOn(
						upper_alarm_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						monitor_mode: Isolette_Data_Model::Monitor_Mode,
						alarm_control: Isolette_Data_Model::On_Off
						): Base_Types::Boolean :=
					'->:'(
            upper_alarm_tempWstatus.status != Isolette_Data_Model::ValueStatus.Valid
					    & monitor_mode != Isolette_Data_Model::Monitor_Mode.Init_Monitor_Mode,
					  alarm_control == Isolette_Data_Model::On_Off.Onn);

				def sysProp_InternalFailureMonitorNotNormal(
						internal_failure: Isolette_Data_Model::Failure_Flag_i,
						monitor_mode: Isolette_Data_Model::Monitor_Mode
						): Base_Types::Boolean :=
					'->:'(
            internal_failure.flag,
					  monitor_mode != Isolette_Data_Model::Monitor_Mode.Normal_Monitor_Mode);

				def sysProp_InternalFailureAlarmOn(
						internal_failure: Isolette_Data_Model::Failure_Flag_i,
						monitor_mode: Isolette_Data_Model::Monitor_Mode,
						alarm_control: Isolette_Data_Model::On_Off
						): Base_Types::Boolean :=
					'->:'(
            internal_failure.flag
					    & monitor_mode != Isolette_Data_Model::Monitor_Mode.Init_Monitor_Mode,
					  alarm_control == Isolette_Data_Model::On_Off.Onn);

				def sysProp_ErrorConditionMonitorNotNormal(
						lower_alarm_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						upper_alarm_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						current_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						internal_failure: Isolette_Data_Model::Failure_Flag_i,
						monitor_mode: Isolette_Data_Model::Monitor_Mode
						): Base_Types::Boolean :=
					'->:'(
            lower_alarm_tempWstatus.status != Isolette_Data_Model::ValueStatus.Valid
					    | upper_alarm_tempWstatus.status != Isolette_Data_Model::ValueStatus.Valid
					    | current_tempWstatus.status != Isolette_Data_Model::ValueStatus.Valid
					    | internal_failure.flag,
					  monitor_mode != Isolette_Data_Model::Monitor_Mode.Normal_Monitor_Mode);

				def sysProp_ErrorConditionAlarmOn(
						lower_alarm_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						upper_alarm_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						current_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						internal_failure: Isolette_Data_Model::Failure_Flag_i,
						monitor_mode: Isolette_Data_Model::Monitor_Mode,
						alarm_control: Isolette_Data_Model::On_Off
						): Base_Types::Boolean :=
					'->:'(
            (lower_alarm_tempWstatus.status != Isolette_Data_Model::ValueStatus.Valid
					    | upper_alarm_tempWstatus.status != Isolette_Data_Model::ValueStatus.Valid
					    | current_tempWstatus.status != Isolette_Data_Model::ValueStatus.Valid
					    | internal_failure.flag)
					    & monitor_mode != Isolette_Data_Model::Monitor_Mode.Init_Monitor_Mode,
					  alarm_control == Isolette_Data_Model::On_Off.Onn);

				def sysProp_REQ_MMI_5(
						lower_alarm_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						upper_alarm_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						interface_failure: Isolette_Data_Model::Failure_Flag_i
						): Base_Types::Boolean :=
					'->:'(
            upper_alarm_tempWstatus.status == Isolette_Data_Model::ValueStatus.Valid
					    & lower_alarm_tempWstatus.status == Isolette_Data_Model::ValueStatus.Valid,
					  not interface_failure.flag);

				def sysProp_REQ_MMI_6(
						lower_alarm_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						upper_alarm_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						lower_alarm_temp: Isolette_Data_Model::Temp_i,
						upper_alarm_temp: Isolette_Data_Model::Temp_i,
						interface_failure: Isolette_Data_Model::Failure_Flag_i
						): Base_Types::Boolean :=
					'->:'(
            not interface_failure.flag,
					  lower_alarm_temp.degrees == lower_alarm_tempWstatus.degrees
							& upper_alarm_temp.degrees == upper_alarm_tempWstatus.degrees);

				def sysProp_I_Assm_lower_alarm_tempWstatus(
						lower_alarm_tempWstatus: Isolette_Data_Model::TempWstatus_i
						): Base_Types::Boolean :=
					GUMBO_Library::GUMBO__Library::Allowed_LowerAlarmTempWStatus(lower_alarm_tempWstatus);

				def sysProp_I_Assm_upper_alarm_tempWstatus(
						upper_alarm_tempWstatus: Isolette_Data_Model::TempWstatus_i
						): Base_Types::Boolean :=
					GUMBO_Library::GUMBO__Library::Allowed_UpperAlarmTempWStatus(upper_alarm_tempWstatus);

				def sysProp_Allowed_AlarmTempWstatus_Ranges(
						lower_alarm_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						upper_alarm_tempWstatus: Isolette_Data_Model::TempWstatus_i
						): Base_Types::Boolean :=
					GUMBO_Library::GUMBO__Library::Allowed_AlarmTempWStatus_Ranges(
						lower_alarm_tempWstatus, upper_alarm_tempWstatus);

				def sysProp_Table_A_12_LowerAlarmTemp(
						lower_alarm_temp: Isolette_Data_Model::Temp_i
						): Base_Types::Boolean :=
					GUMBO_Library::GUMBO__Library::Allowed_LowerAlarmTemp(lower_alarm_temp.degrees);

				def sysProp_Table_A_12_UpperAlarmTemp(
						upper_alarm_temp: Isolette_Data_Model::Temp_i
						): Base_Types::Boolean :=
					GUMBO_Library::GUMBO__Library::Allowed_UpperAlarmTemp(upper_alarm_temp.degrees);

				// the model's Figure A-7 property: the alarm range is ordered.
				def sysProp_Figure_A_7(
						lower_alarm_temp: Isolette_Data_Model::Temp_i,
						upper_alarm_temp: Isolette_Data_Model::Temp_i
						): Base_Types::Boolean :=
					lower_alarm_temp.degrees <= upper_alarm_temp.degrees;

				// links MMM's mode decision to MMI's failure flag (from the REQ-MMM-2
				// iff, REQ-MMM-3, and the Figure A-6 absorbing Failed mode, formalized
				// as MMM's Failed_Mode_Absorbing case); lets MA's NORMAL-mode-guarded
				// alarm range assumes be discharged from the failure-guarded MMI
				// properties carried by Post_MMM
				def sysProp_NormalModeImpliesNoInterfaceFailure(
						monitor_mode: Isolette_Data_Model::Monitor_Mode,
						interface_failure: Isolette_Data_Model::Failure_Flag_i
						): Base_Types::Boolean :=
					'->:'(
            monitor_mode == Isolette_Data_Model::Monitor_Mode.Normal_Monitor_Mode,
					  not interface_failure.flag);

				def sysProp_Allowed_LowerAlarmTempWstatus(
						lower_alarm_tempWstatus: Isolette_Data_Model::TempWstatus_i
						): Base_Types::Boolean :=
					GUMBO_Library::GUMBO__Library::Allowed_LowerAlarmTempWStatus(lower_alarm_tempWstatus);

				def sysProp_Allowed_UpperAlarmTempWstatus(
						upper_alarm_tempWstatus: Isolette_Data_Model::TempWstatus_i
						): Base_Types::Boolean :=
					GUMBO_Library::GUMBO__Library::Allowed_UpperAlarmTempWStatus(upper_alarm_tempWstatus);

				def sysProp_Allowed_AlarmTempWStatus_Ranges(
						lower_alarm_tempWstatus: Isolette_Data_Model::TempWstatus_i,
						upper_alarm_tempWstatus: Isolette_Data_Model::TempWstatus_i
						): Base_Types::Boolean :=
					GUMBO_Library::GUMBO__Library::Allowed_AlarmTempWStatus_Ranges(
						lower_alarm_tempWstatus, upper_alarm_tempWstatus);

			composition nominal {

				components
					oi  = operator_interface.oip.oit;
					ts  = temperature_sensor.cpi.thermostat;
					drf = thermostat.rt.drf.drf;
					mri = thermostat.rt.mri.mri;
					mrm = thermostat.rt.mrm.mrm;
					mhs = thermostat.rt.mhs.mhs;
					dmf = thermostat.mt.dmf.dmf;
					mmi = thermostat.mt.mmi.mmi;
					mmm = thermostat.mt.mmm.mmm;
					ma  = thermostat.mt.ma.ma;
					hs  = heat_source.cpi.heat_controller;

				ports
					lower_desired_tempWstatus = oi.lower_desired_tempWstatus;
					upper_desired_tempWstatus = oi.upper_desired_tempWstatus;
					lower_alarm_tempWstatus   = oi.lower_alarm_tempWstatus;
					upper_alarm_tempWstatus   = oi.upper_alarm_tempWstatus;
					current_tempWstatus       = ts.current_tempWstatus;
					internal_failure          = drf.internal_failure;
					reg_interface_failure     = mri.interface_failure;
					lower_desired_temp        = mri.lower_desired_temp;
					upper_desired_temp        = mri.upper_desired_temp;
					displayed_temp            = mri.displayed_temp;
					regulator_mode            = mrm.regulator_mode;
					heat_control              = mhs.heat_control;
					mon_interface_failure     = mmi.interface_failure;
					mon_internal_failure      = dmf.internal_failure;
					lower_alarm_temp          = mmi.lower_alarm_temp;
					upper_alarm_temp          = mmi.upper_alarm_temp;
					monitor_mode              = mmm.monitor_mode;
					alarm_control             = ma.alarm_control;

				state
					ma_lastCmd = ma.lastCmd;
					mmi_lastCmd = mmi.lastCmd;
					reg_last_mode = mrm.lastRegulatorMode;

				schema {
					split {
						sequence { oi },
						sequence { ts }
					};
					label ts_oi_done;
					split {
						sequence { drf; mri; mrm; mhs },
						sequence { dmf; mmi; mmm; ma }
					};
					hs
				}

				property Normal_Mode_Heat
					"end-to-end: in normal mode, heat turns on below the desired range" {

					after oi "OI orders the desired range":
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);

					at ts_oi_done "join: carried past ts":
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);

					before drf "branch entry: desired temp ordering carries in":
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);

					after drf "frame condition preserves desired temp ordering":
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);

					after mri "derived from MRI component contract":
						sysProp_REQ_MRI_7(lower_desired_tempWstatus, upper_desired_tempWstatus, reg_interface_failure)
						& sysProp_REQ_MRI_8(lower_desired_tempWstatus, upper_desired_tempWstatus,
								lower_desired_temp, upper_desired_temp, reg_interface_failure)
						& sysProp_lower_is_lower_temp(lower_desired_temp, upper_desired_temp);

					after mrm "frame condition preserves MRI properties":
						sysProp_REQ_MRI_7(lower_desired_tempWstatus, upper_desired_tempWstatus, reg_interface_failure)
						& sysProp_REQ_MRI_8(lower_desired_tempWstatus, upper_desired_tempWstatus,
								lower_desired_temp, upper_desired_temp, reg_interface_failure)
						& sysProp_lower_is_lower_temp(lower_desired_temp, upper_desired_temp);

					after mhs "end-to-end: normal mode heat control":
						sysProp_NormalModeHeatOnn(regulator_mode, current_tempWstatus,
							lower_desired_tempWstatus, upper_desired_tempWstatus,
							internal_failure, heat_control);
				}

				property Normal_Mode_Heat_Off
					"end-to-end: in normal mode, heat turns off above the desired range" {

					after oi "OI orders the desired range":
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);

					at ts_oi_done "join: carried past ts":
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);

					before drf "branch entry: desired temp ordering carries in":
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);

					after drf "frame condition preserves desired temp ordering":
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);

					after mri "derived from MRI component contract":
						sysProp_REQ_MRI_7(lower_desired_tempWstatus, upper_desired_tempWstatus, reg_interface_failure)
						& sysProp_REQ_MRI_8(lower_desired_tempWstatus, upper_desired_tempWstatus,
								lower_desired_temp, upper_desired_temp, reg_interface_failure)
						& sysProp_lower_is_lower_temp(lower_desired_temp, upper_desired_temp);

					after mrm "frame condition preserves MRI properties":
						sysProp_REQ_MRI_7(lower_desired_tempWstatus, upper_desired_tempWstatus, reg_interface_failure)
						& sysProp_REQ_MRI_8(lower_desired_tempWstatus, upper_desired_tempWstatus,
								lower_desired_temp, upper_desired_temp, reg_interface_failure)
						& sysProp_lower_is_lower_temp(lower_desired_temp, upper_desired_temp);

					after mhs "end-to-end: normal mode heat control (off above range)":
						sysProp_NormalModeHeatOff(regulator_mode, current_tempWstatus,
							lower_desired_tempWstatus, upper_desired_tempWstatus,
							internal_failure, heat_control);
				}

				property Invalid_CT_Heat_Off
					"end-to-end: an invalid current temperature keeps the regulator out of Normal, so heat is off" {

					after oi:
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);
					at ts_oi_done:
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);
					before drf:
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);
					after drf:
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);
					after mri:
						sysProp_REQ_MRI_7(lower_desired_tempWstatus, upper_desired_tempWstatus, reg_interface_failure)
						& sysProp_REQ_MRI_8(lower_desired_tempWstatus, upper_desired_tempWstatus,
								lower_desired_temp, upper_desired_temp, reg_interface_failure)
						& sysProp_lower_is_lower_temp(lower_desired_temp, upper_desired_temp);
					after mrm "MRM keeps the mode out of Normal on invalid current temp (REQ_MRM_3 / Maintain_Init / REQ_MRM_4)":
						sysProp_REQ_MRI_7(lower_desired_tempWstatus, upper_desired_tempWstatus, reg_interface_failure)
						& sysProp_REQ_MRI_8(lower_desired_tempWstatus, upper_desired_tempWstatus,
								lower_desired_temp, upper_desired_temp, reg_interface_failure)
						& sysProp_lower_is_lower_temp(lower_desired_temp, upper_desired_temp)
						& sysProp_CTInvalidImpliesRegulatorNotNormal(current_tempWstatus, regulator_mode);
					after mhs "non-Normal mode implies heat off (REQ_MHS_1 Init / REQ_MHS_5 Failed)":
						sysProp_InvalidCTHeatOff(current_tempWstatus, heat_control);
				}

				property Invalid_UDT_Heat_Off
					"end-to-end: an invalid upper desired temp keeps the regulator out of Normal, so heat is off" {

					after oi:
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);
					at ts_oi_done:
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);
					before drf:
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);
					after drf:
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);
					after mri:
						sysProp_REQ_MRI_7(lower_desired_tempWstatus, upper_desired_tempWstatus, reg_interface_failure)
						& sysProp_REQ_MRI_8(lower_desired_tempWstatus, upper_desired_tempWstatus,
								lower_desired_temp, upper_desired_temp, reg_interface_failure)
						& sysProp_lower_is_lower_temp(lower_desired_temp, upper_desired_temp);
					after mrm "REQ_MRI_7 links invalid upper desired to interface failure; MRM keeps mode out of Normal":
						sysProp_REQ_MRI_7(lower_desired_tempWstatus, upper_desired_tempWstatus, reg_interface_failure)
						& sysProp_REQ_MRI_8(lower_desired_tempWstatus, upper_desired_tempWstatus,
								lower_desired_temp, upper_desired_temp, reg_interface_failure)
						& sysProp_lower_is_lower_temp(lower_desired_temp, upper_desired_temp)
						& sysProp_InvalidUDTImpliesRegulatorNotNormal(upper_desired_tempWstatus, regulator_mode);
					after mhs:
						sysProp_InvalidUDTHeatOff(upper_desired_tempWstatus, heat_control);
				}

				property Invalid_LDT_Heat_Off
					"end-to-end: an invalid lower desired temp keeps the regulator out of Normal, so heat is off" {

					after oi:
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);
					at ts_oi_done:
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);
					before drf:
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);
					after drf:
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);
					after mri:
						sysProp_REQ_MRI_7(lower_desired_tempWstatus, upper_desired_tempWstatus, reg_interface_failure)
						& sysProp_REQ_MRI_8(lower_desired_tempWstatus, upper_desired_tempWstatus,
								lower_desired_temp, upper_desired_temp, reg_interface_failure)
						& sysProp_lower_is_lower_temp(lower_desired_temp, upper_desired_temp);
					after mrm "REQ_MRI_7 links invalid lower desired to interface failure; MRM keeps mode out of Normal":
						sysProp_REQ_MRI_7(lower_desired_tempWstatus, upper_desired_tempWstatus, reg_interface_failure)
						& sysProp_REQ_MRI_8(lower_desired_tempWstatus, upper_desired_tempWstatus,
								lower_desired_temp, upper_desired_temp, reg_interface_failure)
						& sysProp_lower_is_lower_temp(lower_desired_temp, upper_desired_temp)
						& sysProp_InvalidLDTImpliesRegulatorNotNormal(lower_desired_tempWstatus, regulator_mode);
					after mhs:
						sysProp_InvalidLDTHeatOff(lower_desired_tempWstatus, heat_control);
				}

				property Internal_Failure_Heat_Off
					"end-to-end: a regulator internal failure keeps the regulator out of Normal, so heat is off" {

					after oi:
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);
					at ts_oi_done:
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);
					before drf:
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);
					after drf:
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);
					after mri:
						sysProp_REQ_MRI_7(lower_desired_tempWstatus, upper_desired_tempWstatus, reg_interface_failure)
						& sysProp_REQ_MRI_8(lower_desired_tempWstatus, upper_desired_tempWstatus,
								lower_desired_temp, upper_desired_temp, reg_interface_failure)
						& sysProp_lower_is_lower_temp(lower_desired_temp, upper_desired_temp);
					after mrm "internal failure keeps mode out of Normal (REQ_MRM_3 / Maintain_Init)":
						sysProp_REQ_MRI_7(lower_desired_tempWstatus, upper_desired_tempWstatus, reg_interface_failure)
						& sysProp_REQ_MRI_8(lower_desired_tempWstatus, upper_desired_tempWstatus,
								lower_desired_temp, upper_desired_temp, reg_interface_failure)
						& sysProp_lower_is_lower_temp(lower_desired_temp, upper_desired_temp)
						& sysProp_InternalFailureImpliesRegulatorNotNormal(internal_failure, regulator_mode);
					after mhs:
						sysProp_InternalFailureHeatOff(internal_failure, heat_control);
				}

				property Error_Condition_Heat_Off
					"end-to-end: any regulator error condition keeps the regulator out of Normal, so heat is off" {

					after oi:
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);
					at ts_oi_done:
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);
					before drf:
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);
					after drf:
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);
					after mri:
						sysProp_REQ_MRI_7(lower_desired_tempWstatus, upper_desired_tempWstatus, reg_interface_failure)
						& sysProp_REQ_MRI_8(lower_desired_tempWstatus, upper_desired_tempWstatus,
								lower_desired_temp, upper_desired_temp, reg_interface_failure)
						& sysProp_lower_is_lower_temp(lower_desired_temp, upper_desired_temp);
					after mrm "any error condition keeps mode out of Normal (REQ_MRI_7 + CT/internal-failure terms)":
						sysProp_REQ_MRI_7(lower_desired_tempWstatus, upper_desired_tempWstatus, reg_interface_failure)
						& sysProp_REQ_MRI_8(lower_desired_tempWstatus, upper_desired_tempWstatus,
								lower_desired_temp, upper_desired_temp, reg_interface_failure)
						& sysProp_lower_is_lower_temp(lower_desired_temp, upper_desired_temp)
						& sysProp_ErrorConditionImpliesRegulatorNotNormal(lower_desired_tempWstatus,
								upper_desired_tempWstatus, current_tempWstatus, internal_failure, regulator_mode);
					after mhs:
						sysProp_ErrorConditionHeatOff(lower_desired_tempWstatus, upper_desired_tempWstatus,
							current_tempWstatus, internal_failure, heat_control);
				}

				property Normal_Mode_Alarm
					"end-to-end: in normal mode, alarm turns on outside the alarm range" {

					after oi "OI integration and alarm range constraints":
						sysProp_Allowed_LowerAlarmTempWstatus(lower_alarm_tempWstatus)
						& sysProp_Allowed_UpperAlarmTempWstatus(upper_alarm_tempWstatus)
						& sysProp_Allowed_AlarmTempWStatus_Ranges(lower_alarm_tempWstatus, upper_alarm_tempWstatus);

					at ts_oi_done "join: carried past ts":
						sysProp_Allowed_LowerAlarmTempWstatus(lower_alarm_tempWstatus)
						& sysProp_Allowed_UpperAlarmTempWstatus(upper_alarm_tempWstatus)
						& sysProp_Allowed_AlarmTempWStatus_Ranges(lower_alarm_tempWstatus, upper_alarm_tempWstatus);

					before dmf "branch entry: alarm temp constraints carry in":
						sysProp_Allowed_LowerAlarmTempWstatus(lower_alarm_tempWstatus)
						& sysProp_Allowed_UpperAlarmTempWstatus(upper_alarm_tempWstatus)
						& sysProp_Allowed_AlarmTempWStatus_Ranges(lower_alarm_tempWstatus, upper_alarm_tempWstatus);

					after dmf "frame condition preserves alarm temp constraints":
						sysProp_Allowed_LowerAlarmTempWstatus(lower_alarm_tempWstatus)
						& sysProp_Allowed_UpperAlarmTempWstatus(upper_alarm_tempWstatus)
						& sysProp_Allowed_AlarmTempWStatus_Ranges(lower_alarm_tempWstatus, upper_alarm_tempWstatus);

					after mmi "derived from MMI component contract + write frame + pre-assertions;
							|the alarm range properties hold only when the monitor interface is not failing
							|(REQ-MMI-7 leaves the range UNSPECIFIED on failure)":
						sysProp_REQ_MMI_5(lower_alarm_tempWstatus, upper_alarm_tempWstatus, mon_interface_failure)
						& sysProp_REQ_MMI_6(lower_alarm_tempWstatus, upper_alarm_tempWstatus,
								lower_alarm_temp, upper_alarm_temp, mon_interface_failure)
						& '->:'(not mon_interface_failure.flag,
								sysProp_Figure_A_7(lower_alarm_temp, upper_alarm_temp)
								& sysProp_Table_A_12_LowerAlarmTemp(lower_alarm_temp)
								& sysProp_Table_A_12_UpperAlarmTemp(upper_alarm_temp));

					after mmm "frame condition preserves MMI properties;
							|MMM additionally links NORMAL mode to the absence of interface failure":
						sysProp_REQ_MMI_5(lower_alarm_tempWstatus, upper_alarm_tempWstatus, mon_interface_failure)
						& sysProp_REQ_MMI_6(lower_alarm_tempWstatus, upper_alarm_tempWstatus,
								lower_alarm_temp, upper_alarm_temp, mon_interface_failure)
						& '->:'(not mon_interface_failure.flag,
								sysProp_Figure_A_7(lower_alarm_temp, upper_alarm_temp)
								& sysProp_Table_A_12_LowerAlarmTemp(lower_alarm_temp)
								& sysProp_Table_A_12_UpperAlarmTemp(upper_alarm_temp))
						& sysProp_NormalModeImpliesNoInterfaceFailure(monitor_mode, mon_interface_failure);

					after ma "end-to-end: normal mode alarm control":
						sysProp_NormalModeAlarmOn(lower_alarm_tempWstatus, upper_alarm_tempWstatus,
							current_tempWstatus, monitor_mode, mon_interface_failure, alarm_control);
				}

				property Normal_Mode_Alarm_Off
					"end-to-end: in normal mode, alarm turns off within the alarm range" {

					after oi "OI integration and alarm range constraints":
						sysProp_Allowed_LowerAlarmTempWstatus(lower_alarm_tempWstatus)
						& sysProp_Allowed_UpperAlarmTempWstatus(upper_alarm_tempWstatus)
						& sysProp_Allowed_AlarmTempWStatus_Ranges(lower_alarm_tempWstatus, upper_alarm_tempWstatus);

					at ts_oi_done "join: carried past ts":
						sysProp_Allowed_LowerAlarmTempWstatus(lower_alarm_tempWstatus)
						& sysProp_Allowed_UpperAlarmTempWstatus(upper_alarm_tempWstatus)
						& sysProp_Allowed_AlarmTempWStatus_Ranges(lower_alarm_tempWstatus, upper_alarm_tempWstatus);

					before dmf "branch entry: alarm temp constraints carry in":
						sysProp_Allowed_LowerAlarmTempWstatus(lower_alarm_tempWstatus)
						& sysProp_Allowed_UpperAlarmTempWstatus(upper_alarm_tempWstatus)
						& sysProp_Allowed_AlarmTempWStatus_Ranges(lower_alarm_tempWstatus, upper_alarm_tempWstatus);

					after dmf "frame condition preserves alarm temp constraints":
						sysProp_Allowed_LowerAlarmTempWstatus(lower_alarm_tempWstatus)
						& sysProp_Allowed_UpperAlarmTempWstatus(upper_alarm_tempWstatus)
						& sysProp_Allowed_AlarmTempWStatus_Ranges(lower_alarm_tempWstatus, upper_alarm_tempWstatus);

					after mmi "derived from MMI component contract + write frame + pre-assertions;
							|the alarm range properties hold only when the monitor interface is not failing
							|(REQ-MMI-7 leaves the range UNSPECIFIED on failure)":
						sysProp_REQ_MMI_5(lower_alarm_tempWstatus, upper_alarm_tempWstatus, mon_interface_failure)
						& sysProp_REQ_MMI_6(lower_alarm_tempWstatus, upper_alarm_tempWstatus,
								lower_alarm_temp, upper_alarm_temp, mon_interface_failure)
						& '->:'(not mon_interface_failure.flag,
								sysProp_Figure_A_7(lower_alarm_temp, upper_alarm_temp)
								& sysProp_Table_A_12_LowerAlarmTemp(lower_alarm_temp)
								& sysProp_Table_A_12_UpperAlarmTemp(upper_alarm_temp));

					after mmm "frame condition preserves MMI properties;
							|MMM additionally links NORMAL mode to the absence of interface failure":
						sysProp_REQ_MMI_5(lower_alarm_tempWstatus, upper_alarm_tempWstatus, mon_interface_failure)
						& sysProp_REQ_MMI_6(lower_alarm_tempWstatus, upper_alarm_tempWstatus,
								lower_alarm_temp, upper_alarm_temp, mon_interface_failure)
						& '->:'(not mon_interface_failure.flag,
								sysProp_Figure_A_7(lower_alarm_temp, upper_alarm_temp)
								& sysProp_Table_A_12_LowerAlarmTemp(lower_alarm_temp)
								& sysProp_Table_A_12_UpperAlarmTemp(upper_alarm_temp))
						& sysProp_NormalModeImpliesNoInterfaceFailure(monitor_mode, mon_interface_failure);

					after ma "end-to-end: normal mode alarm control (off within range)":
						sysProp_NormalModeAlarmOff(lower_alarm_tempWstatus, upper_alarm_tempWstatus,
							current_tempWstatus, monitor_mode, mon_interface_failure, alarm_control);
				}

				property Invalid_CT_Alarm_On
					"end-to-end: an invalid current temperature raises the alarm once the monitor is past startup (mode != Init)" {

					after oi "OI integration and alarm range constraints":
						sysProp_Allowed_LowerAlarmTempWstatus(lower_alarm_tempWstatus)
						& sysProp_Allowed_UpperAlarmTempWstatus(upper_alarm_tempWstatus)
						& sysProp_Allowed_AlarmTempWStatus_Ranges(lower_alarm_tempWstatus, upper_alarm_tempWstatus);

					at ts_oi_done "join: carried past ts":
						sysProp_Allowed_LowerAlarmTempWstatus(lower_alarm_tempWstatus)
						& sysProp_Allowed_UpperAlarmTempWstatus(upper_alarm_tempWstatus)
						& sysProp_Allowed_AlarmTempWStatus_Ranges(lower_alarm_tempWstatus, upper_alarm_tempWstatus);

					before dmf "branch entry: alarm temp constraints carry in":
						sysProp_Allowed_LowerAlarmTempWstatus(lower_alarm_tempWstatus)
						& sysProp_Allowed_UpperAlarmTempWstatus(upper_alarm_tempWstatus)
						& sysProp_Allowed_AlarmTempWStatus_Ranges(lower_alarm_tempWstatus, upper_alarm_tempWstatus);

					after dmf "frame condition preserves alarm temp constraints":
						sysProp_Allowed_LowerAlarmTempWstatus(lower_alarm_tempWstatus)
						& sysProp_Allowed_UpperAlarmTempWstatus(upper_alarm_tempWstatus)
						& sysProp_Allowed_AlarmTempWStatus_Ranges(lower_alarm_tempWstatus, upper_alarm_tempWstatus);

					after mmi "MA precondition plumbing (as in Normal_Mode_Alarm)":
						sysProp_REQ_MMI_5(lower_alarm_tempWstatus, upper_alarm_tempWstatus, mon_interface_failure)
						& sysProp_REQ_MMI_6(lower_alarm_tempWstatus, upper_alarm_tempWstatus,
								lower_alarm_temp, upper_alarm_temp, mon_interface_failure)
						& '->:'(not mon_interface_failure.flag,
								sysProp_Figure_A_7(lower_alarm_temp, upper_alarm_temp)
								& sysProp_Table_A_12_LowerAlarmTemp(lower_alarm_temp)
								& sysProp_Table_A_12_UpperAlarmTemp(upper_alarm_temp));

					after mmm "MMM keeps the mode out of Normal on invalid current temp (REQ_MMM_2/3 iffs, Failed_Mode_Absorbing)":
						sysProp_REQ_MMI_5(lower_alarm_tempWstatus, upper_alarm_tempWstatus, mon_interface_failure)
						& sysProp_REQ_MMI_6(lower_alarm_tempWstatus, upper_alarm_tempWstatus,
								lower_alarm_temp, upper_alarm_temp, mon_interface_failure)
						& '->:'(not mon_interface_failure.flag,
								sysProp_Figure_A_7(lower_alarm_temp, upper_alarm_temp)
								& sysProp_Table_A_12_LowerAlarmTemp(lower_alarm_temp)
								& sysProp_Table_A_12_UpperAlarmTemp(upper_alarm_temp))
						& sysProp_NormalModeImpliesNoInterfaceFailure(monitor_mode, mon_interface_failure)
						& sysProp_InvalidCTMonitorNotNormal(current_tempWstatus, monitor_mode);

					after ma "end-to-end: invalid current temp & past startup implies alarm on (Failed mode, REQ_MA_5)":
						sysProp_InvalidCTAlarmOn(current_tempWstatus, monitor_mode, alarm_control);
				}

				property Invalid_LAT_Alarm_On
					"end-to-end: an invalid lower alarm temp raises the alarm once the monitor is past startup (mode != Init)" {

					after oi:
						sysProp_Allowed_LowerAlarmTempWstatus(lower_alarm_tempWstatus)
						& sysProp_Allowed_UpperAlarmTempWstatus(upper_alarm_tempWstatus)
						& sysProp_Allowed_AlarmTempWStatus_Ranges(lower_alarm_tempWstatus, upper_alarm_tempWstatus);
					at ts_oi_done:
						sysProp_Allowed_LowerAlarmTempWstatus(lower_alarm_tempWstatus)
						& sysProp_Allowed_UpperAlarmTempWstatus(upper_alarm_tempWstatus)
						& sysProp_Allowed_AlarmTempWStatus_Ranges(lower_alarm_tempWstatus, upper_alarm_tempWstatus);
					before dmf:
						sysProp_Allowed_LowerAlarmTempWstatus(lower_alarm_tempWstatus)
						& sysProp_Allowed_UpperAlarmTempWstatus(upper_alarm_tempWstatus)
						& sysProp_Allowed_AlarmTempWStatus_Ranges(lower_alarm_tempWstatus, upper_alarm_tempWstatus);
					after dmf:
						sysProp_Allowed_LowerAlarmTempWstatus(lower_alarm_tempWstatus)
						& sysProp_Allowed_UpperAlarmTempWstatus(upper_alarm_tempWstatus)
						& sysProp_Allowed_AlarmTempWStatus_Ranges(lower_alarm_tempWstatus, upper_alarm_tempWstatus);
					after mmi "MA precondition plumbing + REQ_MMI_4 (invalid alarm temp -> interface failure)":
						sysProp_REQ_MMI_4(lower_alarm_tempWstatus, upper_alarm_tempWstatus, mon_interface_failure)
						& sysProp_REQ_MMI_5(lower_alarm_tempWstatus, upper_alarm_tempWstatus, mon_interface_failure)
						& sysProp_REQ_MMI_6(lower_alarm_tempWstatus, upper_alarm_tempWstatus,
								lower_alarm_temp, upper_alarm_temp, mon_interface_failure)
						& '->:'(not mon_interface_failure.flag,
								sysProp_Figure_A_7(lower_alarm_temp, upper_alarm_temp)
								& sysProp_Table_A_12_LowerAlarmTemp(lower_alarm_temp)
								& sysProp_Table_A_12_UpperAlarmTemp(upper_alarm_temp));
					after mmm "invalid lower alarm temp -> interface failure (REQ_MMI_4) -> monitor_status false -> not Normal":
						sysProp_REQ_MMI_4(lower_alarm_tempWstatus, upper_alarm_tempWstatus, mon_interface_failure)
						& sysProp_REQ_MMI_5(lower_alarm_tempWstatus, upper_alarm_tempWstatus, mon_interface_failure)
						& sysProp_REQ_MMI_6(lower_alarm_tempWstatus, upper_alarm_tempWstatus,
								lower_alarm_temp, upper_alarm_temp, mon_interface_failure)
						& '->:'(not mon_interface_failure.flag,
								sysProp_Figure_A_7(lower_alarm_temp, upper_alarm_temp)
								& sysProp_Table_A_12_LowerAlarmTemp(lower_alarm_temp)
								& sysProp_Table_A_12_UpperAlarmTemp(upper_alarm_temp))
						& sysProp_NormalModeImpliesNoInterfaceFailure(monitor_mode, mon_interface_failure)
						& sysProp_InvalidLATMonitorNotNormal(lower_alarm_tempWstatus, monitor_mode);
					after ma "end-to-end: invalid lower alarm temp & past startup implies alarm on":
						sysProp_InvalidLATAlarmOn(lower_alarm_tempWstatus, monitor_mode, alarm_control);
				}

				property Invalid_UAT_Alarm_On
					"end-to-end: an invalid upper alarm temp raises the alarm once the monitor is past startup (mode != Init)" {

					after oi:
						sysProp_Allowed_LowerAlarmTempWstatus(lower_alarm_tempWstatus)
						& sysProp_Allowed_UpperAlarmTempWstatus(upper_alarm_tempWstatus)
						& sysProp_Allowed_AlarmTempWStatus_Ranges(lower_alarm_tempWstatus, upper_alarm_tempWstatus);
					at ts_oi_done:
						sysProp_Allowed_LowerAlarmTempWstatus(lower_alarm_tempWstatus)
						& sysProp_Allowed_UpperAlarmTempWstatus(upper_alarm_tempWstatus)
						& sysProp_Allowed_AlarmTempWStatus_Ranges(lower_alarm_tempWstatus, upper_alarm_tempWstatus);
					before dmf:
						sysProp_Allowed_LowerAlarmTempWstatus(lower_alarm_tempWstatus)
						& sysProp_Allowed_UpperAlarmTempWstatus(upper_alarm_tempWstatus)
						& sysProp_Allowed_AlarmTempWStatus_Ranges(lower_alarm_tempWstatus, upper_alarm_tempWstatus);
					after dmf:
						sysProp_Allowed_LowerAlarmTempWstatus(lower_alarm_tempWstatus)
						& sysProp_Allowed_UpperAlarmTempWstatus(upper_alarm_tempWstatus)
						& sysProp_Allowed_AlarmTempWStatus_Ranges(lower_alarm_tempWstatus, upper_alarm_tempWstatus);
					after mmi "MA precondition plumbing + REQ_MMI_4 (invalid alarm temp -> interface failure)":
						sysProp_REQ_MMI_4(lower_alarm_tempWstatus, upper_alarm_tempWstatus, mon_interface_failure)
						& sysProp_REQ_MMI_5(lower_alarm_tempWstatus, upper_alarm_tempWstatus, mon_interface_failure)
						& sysProp_REQ_MMI_6(lower_alarm_tempWstatus, upper_alarm_tempWstatus,
								lower_alarm_temp, upper_alarm_temp, mon_interface_failure)
						& '->:'(not mon_interface_failure.flag,
								sysProp_Figure_A_7(lower_alarm_temp, upper_alarm_temp)
								& sysProp_Table_A_12_LowerAlarmTemp(lower_alarm_temp)
								& sysProp_Table_A_12_UpperAlarmTemp(upper_alarm_temp));
					after mmm "invalid upper alarm temp -> interface failure (REQ_MMI_4) -> monitor_status false -> not Normal":
						sysProp_REQ_MMI_4(lower_alarm_tempWstatus, upper_alarm_tempWstatus, mon_interface_failure)
						& sysProp_REQ_MMI_5(lower_alarm_tempWstatus, upper_alarm_tempWstatus, mon_interface_failure)
						& sysProp_REQ_MMI_6(lower_alarm_tempWstatus, upper_alarm_tempWstatus,
								lower_alarm_temp, upper_alarm_temp, mon_interface_failure)
						& '->:'(not mon_interface_failure.flag,
								sysProp_Figure_A_7(lower_alarm_temp, upper_alarm_temp)
								& sysProp_Table_A_12_LowerAlarmTemp(lower_alarm_temp)
								& sysProp_Table_A_12_UpperAlarmTemp(upper_alarm_temp))
						& sysProp_NormalModeImpliesNoInterfaceFailure(monitor_mode, mon_interface_failure)
						& sysProp_InvalidUATMonitorNotNormal(upper_alarm_tempWstatus, monitor_mode);
					after ma "end-to-end: invalid upper alarm temp & past startup implies alarm on":
						sysProp_InvalidUATAlarmOn(upper_alarm_tempWstatus, monitor_mode, alarm_control);
				}

				property Internal_Failure_Alarm_On
					"end-to-end: a monitor internal failure raises the alarm once the monitor is past startup (mode != Init)" {

					after oi:
						sysProp_Allowed_LowerAlarmTempWstatus(lower_alarm_tempWstatus)
						& sysProp_Allowed_UpperAlarmTempWstatus(upper_alarm_tempWstatus)
						& sysProp_Allowed_AlarmTempWStatus_Ranges(lower_alarm_tempWstatus, upper_alarm_tempWstatus);
					at ts_oi_done:
						sysProp_Allowed_LowerAlarmTempWstatus(lower_alarm_tempWstatus)
						& sysProp_Allowed_UpperAlarmTempWstatus(upper_alarm_tempWstatus)
						& sysProp_Allowed_AlarmTempWStatus_Ranges(lower_alarm_tempWstatus, upper_alarm_tempWstatus);
					before dmf:
						sysProp_Allowed_LowerAlarmTempWstatus(lower_alarm_tempWstatus)
						& sysProp_Allowed_UpperAlarmTempWstatus(upper_alarm_tempWstatus)
						& sysProp_Allowed_AlarmTempWStatus_Ranges(lower_alarm_tempWstatus, upper_alarm_tempWstatus);
					after dmf:
						sysProp_Allowed_LowerAlarmTempWstatus(lower_alarm_tempWstatus)
						& sysProp_Allowed_UpperAlarmTempWstatus(upper_alarm_tempWstatus)
						& sysProp_Allowed_AlarmTempWStatus_Ranges(lower_alarm_tempWstatus, upper_alarm_tempWstatus);
					after mmi "MA precondition plumbing (as in Normal_Mode_Alarm)":
						sysProp_REQ_MMI_5(lower_alarm_tempWstatus, upper_alarm_tempWstatus, mon_interface_failure)
						& sysProp_REQ_MMI_6(lower_alarm_tempWstatus, upper_alarm_tempWstatus,
								lower_alarm_temp, upper_alarm_temp, mon_interface_failure)
						& '->:'(not mon_interface_failure.flag,
								sysProp_Figure_A_7(lower_alarm_temp, upper_alarm_temp)
								& sysProp_Table_A_12_LowerAlarmTemp(lower_alarm_temp)
								& sysProp_Table_A_12_UpperAlarmTemp(upper_alarm_temp));
					after mmm "internal failure drops monitor_status -> mode out of Normal":
						sysProp_REQ_MMI_5(lower_alarm_tempWstatus, upper_alarm_tempWstatus, mon_interface_failure)
						& sysProp_REQ_MMI_6(lower_alarm_tempWstatus, upper_alarm_tempWstatus,
								lower_alarm_temp, upper_alarm_temp, mon_interface_failure)
						& '->:'(not mon_interface_failure.flag,
								sysProp_Figure_A_7(lower_alarm_temp, upper_alarm_temp)
								& sysProp_Table_A_12_LowerAlarmTemp(lower_alarm_temp)
								& sysProp_Table_A_12_UpperAlarmTemp(upper_alarm_temp))
						& sysProp_NormalModeImpliesNoInterfaceFailure(monitor_mode, mon_interface_failure)
						& sysProp_InternalFailureMonitorNotNormal(mon_internal_failure, monitor_mode);
					after ma "end-to-end: monitor internal failure & past startup implies alarm on":
						sysProp_InternalFailureAlarmOn(mon_internal_failure, monitor_mode, alarm_control);
				}

				property Error_Condition_Alarm_On
					"end-to-end: any monitor error condition raises the alarm once the monitor is past startup (mode != Init)" {

					after oi:
						sysProp_Allowed_LowerAlarmTempWstatus(lower_alarm_tempWstatus)
						& sysProp_Allowed_UpperAlarmTempWstatus(upper_alarm_tempWstatus)
						& sysProp_Allowed_AlarmTempWStatus_Ranges(lower_alarm_tempWstatus, upper_alarm_tempWstatus);
					at ts_oi_done:
						sysProp_Allowed_LowerAlarmTempWstatus(lower_alarm_tempWstatus)
						& sysProp_Allowed_UpperAlarmTempWstatus(upper_alarm_tempWstatus)
						& sysProp_Allowed_AlarmTempWStatus_Ranges(lower_alarm_tempWstatus, upper_alarm_tempWstatus);
					before dmf:
						sysProp_Allowed_LowerAlarmTempWstatus(lower_alarm_tempWstatus)
						& sysProp_Allowed_UpperAlarmTempWstatus(upper_alarm_tempWstatus)
						& sysProp_Allowed_AlarmTempWStatus_Ranges(lower_alarm_tempWstatus, upper_alarm_tempWstatus);
					after dmf:
						sysProp_Allowed_LowerAlarmTempWstatus(lower_alarm_tempWstatus)
						& sysProp_Allowed_UpperAlarmTempWstatus(upper_alarm_tempWstatus)
						& sysProp_Allowed_AlarmTempWStatus_Ranges(lower_alarm_tempWstatus, upper_alarm_tempWstatus);
					after mmi "MA precondition plumbing + REQ_MMI_4 (invalid alarm temp -> interface failure)":
						sysProp_REQ_MMI_4(lower_alarm_tempWstatus, upper_alarm_tempWstatus, mon_interface_failure)
						& sysProp_REQ_MMI_5(lower_alarm_tempWstatus, upper_alarm_tempWstatus, mon_interface_failure)
						& sysProp_REQ_MMI_6(lower_alarm_tempWstatus, upper_alarm_tempWstatus,
								lower_alarm_temp, upper_alarm_temp, mon_interface_failure)
						& '->:'(not mon_interface_failure.flag,
								sysProp_Figure_A_7(lower_alarm_temp, upper_alarm_temp)
								& sysProp_Table_A_12_LowerAlarmTemp(lower_alarm_temp)
								& sysProp_Table_A_12_UpperAlarmTemp(upper_alarm_temp));
					after mmm "any error condition (invalid alarm temp via REQ_MMI_4, invalid CT, or internal failure) -> mode out of Normal":
						sysProp_REQ_MMI_4(lower_alarm_tempWstatus, upper_alarm_tempWstatus, mon_interface_failure)
						& sysProp_REQ_MMI_5(lower_alarm_tempWstatus, upper_alarm_tempWstatus, mon_interface_failure)
						& sysProp_REQ_MMI_6(lower_alarm_tempWstatus, upper_alarm_tempWstatus,
								lower_alarm_temp, upper_alarm_temp, mon_interface_failure)
						& '->:'(not mon_interface_failure.flag,
								sysProp_Figure_A_7(lower_alarm_temp, upper_alarm_temp)
								& sysProp_Table_A_12_LowerAlarmTemp(lower_alarm_temp)
								& sysProp_Table_A_12_UpperAlarmTemp(upper_alarm_temp))
						& sysProp_NormalModeImpliesNoInterfaceFailure(monitor_mode, mon_interface_failure)
						& sysProp_ErrorConditionMonitorNotNormal(lower_alarm_tempWstatus,
								upper_alarm_tempWstatus, current_tempWstatus, mon_internal_failure, monitor_mode);
					after ma "end-to-end: any monitor error condition & past startup implies alarm on":
						sysProp_ErrorConditionAlarmOn(lower_alarm_tempWstatus, upper_alarm_tempWstatus,
							current_tempWstatus, mon_internal_failure, monitor_mode, alarm_control);
				}

				property Failed_Mode_Heat_Off
					"end-to-end: whenever the regulator is in Failed mode, the heat is off (REQ_MHS_5)" {

					after oi:
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);
					at ts_oi_done:
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);
					before drf:
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);
					after drf:
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);
					after mri:
						sysProp_REQ_MRI_7(lower_desired_tempWstatus, upper_desired_tempWstatus, reg_interface_failure)
						& sysProp_REQ_MRI_8(lower_desired_tempWstatus, upper_desired_tempWstatus,
								lower_desired_temp, upper_desired_temp, reg_interface_failure)
						& sysProp_lower_is_lower_temp(lower_desired_temp, upper_desired_temp);
					after mrm:
						sysProp_REQ_MRI_7(lower_desired_tempWstatus, upper_desired_tempWstatus, reg_interface_failure)
						& sysProp_REQ_MRI_8(lower_desired_tempWstatus, upper_desired_tempWstatus,
								lower_desired_temp, upper_desired_temp, reg_interface_failure)
						& sysProp_lower_is_lower_temp(lower_desired_temp, upper_desired_temp);
					after mhs "Failed mode implies heat off (REQ_MHS_5)":
						sysProp_FailedModeImpliesHeatOff(regulator_mode, heat_control);
				}

				property Failed_Mode_Alarm_On
					"end-to-end: whenever the monitor is in Failed mode, the alarm is on (REQ_MA_5)" {

					after oi "OI integration and alarm range constraints":
						sysProp_Allowed_LowerAlarmTempWstatus(lower_alarm_tempWstatus)
						& sysProp_Allowed_UpperAlarmTempWstatus(upper_alarm_tempWstatus)
						& sysProp_Allowed_AlarmTempWStatus_Ranges(lower_alarm_tempWstatus, upper_alarm_tempWstatus);
					at ts_oi_done:
						sysProp_Allowed_LowerAlarmTempWstatus(lower_alarm_tempWstatus)
						& sysProp_Allowed_UpperAlarmTempWstatus(upper_alarm_tempWstatus)
						& sysProp_Allowed_AlarmTempWStatus_Ranges(lower_alarm_tempWstatus, upper_alarm_tempWstatus);
					before dmf:
						sysProp_Allowed_LowerAlarmTempWstatus(lower_alarm_tempWstatus)
						& sysProp_Allowed_UpperAlarmTempWstatus(upper_alarm_tempWstatus)
						& sysProp_Allowed_AlarmTempWStatus_Ranges(lower_alarm_tempWstatus, upper_alarm_tempWstatus);
					after dmf:
						sysProp_Allowed_LowerAlarmTempWstatus(lower_alarm_tempWstatus)
						& sysProp_Allowed_UpperAlarmTempWstatus(upper_alarm_tempWstatus)
						& sysProp_Allowed_AlarmTempWStatus_Ranges(lower_alarm_tempWstatus, upper_alarm_tempWstatus);
					after mmi "MA precondition plumbing (as in Normal_Mode_Alarm)":
						sysProp_REQ_MMI_5(lower_alarm_tempWstatus, upper_alarm_tempWstatus, mon_interface_failure)
						& sysProp_REQ_MMI_6(lower_alarm_tempWstatus, upper_alarm_tempWstatus,
								lower_alarm_temp, upper_alarm_temp, mon_interface_failure)
						& '->:'(not mon_interface_failure.flag,
								sysProp_Figure_A_7(lower_alarm_temp, upper_alarm_temp)
								& sysProp_Table_A_12_LowerAlarmTemp(lower_alarm_temp)
								& sysProp_Table_A_12_UpperAlarmTemp(upper_alarm_temp));
					after mmm:
						sysProp_REQ_MMI_5(lower_alarm_tempWstatus, upper_alarm_tempWstatus, mon_interface_failure)
						& sysProp_REQ_MMI_6(lower_alarm_tempWstatus, upper_alarm_tempWstatus,
								lower_alarm_temp, upper_alarm_temp, mon_interface_failure)
						& '->:'(not mon_interface_failure.flag,
								sysProp_Figure_A_7(lower_alarm_temp, upper_alarm_temp)
								& sysProp_Table_A_12_LowerAlarmTemp(lower_alarm_temp)
								& sysProp_Table_A_12_UpperAlarmTemp(upper_alarm_temp))
						& sysProp_NormalModeImpliesNoInterfaceFailure(monitor_mode, mon_interface_failure);
					after ma "Failed mode implies alarm on (REQ_MA_5)":
						sysProp_FailedModeImpliesAlarmOn(monitor_mode, alarm_control);
				}

				property Normal_Display_Temp
					"end-to-end: in normal mode, the displayed temperature reflects the current temperature (REQ_MRI_4)" {

					after oi:
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);
					at ts_oi_done:
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);
					before drf:
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);
					after drf:
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);
					after mri "MRI sets displayed temp to the current temperature in Normal mode (REQ_MRI_4)":
						sysProp_NormalDisplayTemp(regulator_mode, current_tempWstatus, displayed_temp);
				}

				property Regulator_Failsafe_Latching
					"loop invariant: whenever the regulator's latched mode (lastRegulatorMode) is Failed,
					|the heat is off -- in every cycle. With MRM's absorbing Failed mode (REQ_MRM_MaintainFailed)
					|this yields: once the regulator fails, the heat stays off forever." {

					at START "fail-safe loop invariant on the carried (latched) mode":
						sysProp_FailedModeImpliesHeatOff(reg_last_mode, heat_control);

					after oi:
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);
					at ts_oi_done:
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);
					before drf:
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);
					after drf:
						sysProp_lower_is_not_higher_than_upper(lower_desired_tempWstatus, upper_desired_tempWstatus);
					after mri:
						sysProp_REQ_MRI_7(lower_desired_tempWstatus, upper_desired_tempWstatus, reg_interface_failure)
						& sysProp_REQ_MRI_8(lower_desired_tempWstatus, upper_desired_tempWstatus,
								lower_desired_temp, upper_desired_temp, reg_interface_failure)
						& sysProp_lower_is_lower_temp(lower_desired_temp, upper_desired_temp);
					after mrm "mode-memory bridge: lastRegulatorMode == regulator_mode (+ carried temp ordering)":
						sysProp_REQ_MRI_7(lower_desired_tempWstatus, upper_desired_tempWstatus, reg_interface_failure)
						& sysProp_REQ_MRI_8(lower_desired_tempWstatus, upper_desired_tempWstatus,
								lower_desired_temp, upper_desired_temp, reg_interface_failure)
						& sysProp_lower_is_lower_temp(lower_desired_temp, upper_desired_temp)
						& sysProp_RegulatorModeMemory(reg_last_mode, regulator_mode);
					after mhs "Failed (latched) mode implies heat off, via REQ_MHS_5 + the mode-memory bridge":
						sysProp_FailedModeImpliesHeatOff(reg_last_mode, heat_control);
					before hs "carried across the join to the cycle end":
						sysProp_FailedModeImpliesHeatOff(reg_last_mode, heat_control);
					after hs "frame preserves the invariant across the heat-source step":
						sysProp_FailedModeImpliesHeatOff(reg_last_mode, heat_control);
					at END "loop invariant closes (Post-Pre: END |- START)":
						sysProp_FailedModeImpliesHeatOff(reg_last_mode, heat_control);
				}

				property State_Var_Alias_Exercise
					"scaffolding: exercises state-var alias references in composition assertions" {
					after oi: ma_lastCmd == ma_lastCmd & mmi_lastCmd == mmi_lastCmd;
				}
			}

    }*/
  }

  part def Isolette_Processor :> Processor {
    attribute :>> Frame_Period = 1000 [ms];
    attribute :>> Clock_Period = 1 [ms];
  }
}
