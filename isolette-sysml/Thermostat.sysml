package Thermostat {
	
	private import Isolette_Data_Model::*;
	private import AADL::*;
	private import AADL_Project::*;
   	private import GUMBOLib::*;

	// =====================================================================================================================
	//
	//   T h e r m o s t a t 
	// 
	//   See Section A-5 and Figure A-2
	//
	// =====================================================================================================================
	// See Figure A-2 for overall architecture and Tables A-4, A-5, and A-6 for data descriptions
	
	part def Thermostat_Single_Sensor_System_i :> System {
		
        part rt: Regulate::Regulate_Temperature_i;
        part mt: Monitor::Monitor_Temperature_i;

		// ======== INPUTS =======
		// receive current temperature (with status info) from temp sensor
		in port current_tempWstatus : DataPort { in :>> type : Isolette_Data_Model::TempWstatus_i; }
		// receive desired temperature range (with status info) from operator interface
		in port lower_desired_tempWstatus : DataPort { in :>> type : Isolette_Data_Model::TempWstatus_i; }
		in port upper_desired_tempWstatus : DataPort { in :>> type : Isolette_Data_Model::TempWstatus_i; }
		// receive alarm temperature range (with status info) from operator interface
		in port lower_alarm_tempWstatus : DataPort { in :>> type : Isolette_Data_Model::TempWstatus_i; }
		in port upper_alarm_tempWstatus : DataPort { in :>> type : Isolette_Data_Model::TempWstatus_i; }

		// ======== OUTPUTS =========
		// send heat control command to heat source 
		out port heat_control: DataPort  { out :>> type : Isolette_Data_Model::On_Off; }
		// send information to operator interface
		out port regulator_status : DataPort { out :>> type : Isolette_Data_Model::Status; }
		out port monitor_status : DataPort { out :>> type : Isolette_Data_Model::Status; }
		out port display_temperature : DataPort { out :>> type : Isolette_Data_Model::Temp_i; }
		out port alarm_control : DataPort { out :>> type : Isolette_Data_Model::On_Off; }

		// ==== INPUT interface to internal components ====
		// current temperature from temp sensor 	
		connection tctm: PortConnection
			connect current_tempWstatus to mt.current_tempWstatus;
		connection tctr: PortConnection
			connect current_tempWstatus to rt.current_tempWstatus;
		// desired temperature range
		connection tudt: PortConnection
			connect upper_desired_tempWstatus to rt.upper_desired_tempWstatus;
		connection tldt: PortConnection
			connect lower_desired_tempWstatus to rt.lower_desired_tempWstatus;
		// alarm temperature range
		connection tuat: PortConnection
			connect upper_alarm_tempWstatus to mt.upper_alarm_tempWstatus;
		connection tlat: PortConnection
			connect lower_alarm_tempWstatus to mt.lower_alarm_tempWstatus;

		// ==== OUTPUT interface from internal components ==== 	
		// display temperature
		connection tdt: PortConnection
			connect rt.displayed_temp to display_temperature;
		// subsystem status
		connection trs: PortConnection
			connect rt.regulator_status to regulator_status;
		connection tms : PortConnection
			connect mt.monitor_status to monitor_status;
		// alarm control
		connection ta : PortConnection
			connect mt.alarm_control to alarm_control;
		// heat control
		connection thc: PortConnection
			connect rt.heat_control to heat_control;
    }
}
	