//@ HAMR: --platform JVM  --runtime-monitoring --slang-output-dir ../hamr/slang --package-name isolette
//@ HAMR: --platform Microkit --sel4-output-dir ../hamr/microkit
//@ HAMR: --platform Microkit --sel4-output-dir ../hamr/microkit-initial
//@ HAMR: --platform JVM --slang-output-dir ../hamr/slang-initial --package-name isolette
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
	private import R2U2::*;
	private import AADL::*;
	private import AADL_Project::*;
    private import AADL_Project::Time_Units::*;
    
	// top-level system component representing the Isolette
	part def Isolette_Single_Sensor :> System {
		
		// Subcomponents
		
		part thermostat : Thermostat::Thermostat_Single_Sensor_System_i;
		
		part operator_interface : Operator_Interface::Operator_Interface_System_i;
		
		part temperature_sensor : Devices::Temperature_Sensor_System_i;

		part heat_source : Devices::Heat_Source_System_i;
		
		part isolette_processor: Isolette_Processor;

		part r2u2_monitor: R2U2_Monitor_System_i;
				
		// Ports
		
		// models (abstractly) warming of air inside of Isolette enclosure
		out port heat_out : DataPort { out :> type : Isolette_Environment::Heat; }
		// models (abstractly) commands given by the operator to Isolette operator interface
		in port operator_commands : DataPort { in :> type : Isolette_Environment::Interface_Interaction; }
		// models (abstractly) visual information presented to the operator on Isolette operator interface
		out port operator_visual_information : DataPort { out :> type : Isolette_Environment::Interface_Interaction; }
		// models (abstraction) audio information presented to the operator on Isolette operator interface
		out port operator_alarm : DataPort { out :> type : Isolette_Environment::Interface_Interaction; }
		// models (abstractly) the sensing of the physical air temperature
		in port air_temperature : DataPort { in :> type : Isolette_Data_Model::PhysicalTemp_i; }
		
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

		// temperature sensor communicates current temperature to R2U2
		connection mct : PortConnection
			connect temperature_sensor.current_tempWstatus to r2u2_monitor.current_tempWstatus;
		// operator interface communicates alarm temperature to R2U2
		connection mlat : PortConnection
			connect operator_interface.lower_alarm_tempWstatus to r2u2_monitor.lower_alarm_tempWstatus;
		connection muat : PortConnection
			connect operator_interface.upper_alarm_tempWstatus to r2u2_monitor.upper_alarm_tempWstatus;
		// thermostat communicates alarm control to R2U2
		connection mac : PortConnection
			connect thermostat.alarm_control to r2u2_monitor.alarm_control;
		// thermostat communicates monitor status to R2U2
		connection mms : PortConnection
			connect thermostat.monitor_status to r2u2_monitor.monitor_status;
		// thermostat communicates monitor internal failure to R2U2
		connection mmif : PortConnection
			connect thermostat.monitor_internal_failure to r2u2_monitor.monitor_internal_failure;
		// thermostat communicates monitor mode to R2U2
		connection mmm : PortConnection
			connect thermostat.monitor_mode to r2u2_monitor.monitor_mode;
		// operator interface communicates desired temperature to R2U2
		connection mldt : PortConnection
			connect operator_interface.lower_desired_tempWstatus to r2u2_monitor.lower_desired_tempWstatus;
		connection mudt : PortConnection
			connect operator_interface.upper_desired_tempWstatus to r2u2_monitor.upper_desired_tempWstatus;
		// thermostat communicates regulator status to R2U2
		connection mrs : PortConnection
			connect thermostat.regulator_status to r2u2_monitor.regulator_status;
		// thermostat communicates current sensed temperature to R2U2
		connection mdt : PortConnection
			connect thermostat.display_temperature to r2u2_monitor.displayed_temp;
		// thermostat communicates heat control to R2U2  
		connection mhc : PortConnection
			connect thermostat.heat_control to r2u2_monitor.heat_control;
		// thermostat communicates regulator internal failure to R2U2
		connection mrif : PortConnection
			connect thermostat.regulator_internal_failure to r2u2_monitor.regulator_internal_failure;
		// thermostat communicates regulator mode to R2U2
		connection mrm : PortConnection
			connect thermostat.regulator_mode to r2u2_monitor.regulator_mode;

		allocation pb0: Deployment_Properties::Actual_Processor_Binding 
			allocate thermostat to isolette_processor;

		allocation pb1: Deployment_Properties::Actual_Processor_Binding 
			allocate operator_interface to isolette_processor;

		allocation pb2: Deployment_Properties::Actual_Processor_Binding 
			allocate temperature_sensor to isolette_processor;

		allocation pb3: Deployment_Properties::Actual_Processor_Binding 
			allocate heat_source to isolette_processor;

		allocation pb4: Deployment_Properties::Actual_Processor_Binding
			allocate r2u2_monitor to isolette_processor;
	}
	
	part def Isolette_Processor :> Processor {
	    attribute Frame_Period: Timing_Properties::Frame_Period = 1000 [millisecond];
    	attribute Clock_Period: Timing_Properties::Clock_Period = 1 [millisecond];
	}
}