// R2U2.sysml
package R2U2 {
	
	private import Isolette_Data_Model::*;
	private import AADL::*;
	private import AADL_Project::*;
    private import AADL_Project::Time_Units::*;
		
	part def R2U2_Monitor_System_i :> System {

	    //thread for R2U2 monitor interface implementation
	    part r2u2 : R2U2_Monitor_Interface_Process_i;

		// ===== INPUTs (monitored variables) 
	    // value of current temp (with status flag) from air temp sensor
	    in port current_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
	    // operator-specific alarm range for temperature for Monitor component
	    in port lower_alarm_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
	    in port upper_alarm_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
	    // control signal (on/off) to alarm component produced by the Monitor component
	    in port alarm_control : DataPort { in :> type : Isolette_Data_Model::On_Off; }
	    // enumerated type describing status/mode of monitor (Init, On/operating, or Failed) produced by the Monitor component
	    in port monitor_status : DataPort { in :> type : Isolette_Data_Model::Status; }
		// in case of internal failure, a flag is raised
		in port monitor_internal_failure : DataPort { in :> type : Isolette_Data_Model::Failure_Flag_i; }
		// mode of monitor (Init, Normal, Failed) 
		in port monitor_mode : DataPort { in :> type : Isolette_Data_Model::Monitor_Mode; }
		//  desired temperature range for temperature for Regulate component	
        in port lower_desired_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
        in port upper_desired_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
		// status of regulate temperature function
        in port regulator_status : DataPort { in :> type : Isolette_Data_Model::Status; }
        // value of current temperature to display produced by the Regulate component
        in port displayed_temp : DataPort { in :> type : Isolette_Data_Model::Temp_i; }
        // actuation command to heater produced by the Regulate component
        in port heat_control : DataPort { in :> type : Isolette_Data_Model::On_Off; }
		// in case of internal failure, a flag is raised
		in port regulator_internal_failure : DataPort { in :> type : Isolette_Data_Model::Failure_Flag_i; }
		// mode of regulator (Init, Normal, Failed) 
		in port regulator_mode : DataPort { in :> type : Isolette_Data_Model::Regulator_Mode; }
	    
	    // ==== INPUT INTERFACE values to internal components ====
	    connection muat : PortConnection
	    	connect upper_alarm_tempWstatus to r2u2.upper_alarm_tempWstatus;
	    connection mlat : PortConnection
	    	connect lower_alarm_tempWstatus to r2u2.lower_alarm_tempWstatus;
	    connection mcti : PortConnection
	    	connect current_tempWstatus to r2u2.current_tempWstatus;
		connection malrm : PortConnection
	    	connect alarm_control to r2u2.alarm_control;
		connection mms : PortConnection
	    	connect monitor_status to r2u2.monitor_status;
		connection mif : PortConnection
			connect monitor_internal_failure to r2u2.monitor_internal_failure;
		connection mm : PortConnection
			connect monitor_mode to r2u2.monitor_mode;
		connection rudt : PortConnection
            connect upper_desired_tempWstatus to r2u2.upper_desired_tempWstatus;
        connection rldt : PortConnection
            connect lower_desired_tempWstatus to r2u2.lower_desired_tempWstatus;
		connection rdt : PortConnection
            connect displayed_temp to r2u2.displayed_temp;
        connection rrs : PortConnection
            connect regulator_status to r2u2.regulator_status;
        connection rhc : PortConnection
            connect heat_control to r2u2.heat_control;
		connection rif : PortConnection
			connect regulator_internal_failure to r2u2.regulator_internal_failure;
		connection rm : PortConnection
			connect regulator_mode to r2u2.regulator_mode;
	}
	
	// ===========================================================================================================
    // 
	//   R 2 U 2    M o n i t o r   I n t e r f a c e   T h r e a d
	// 
	// ===========================================================================================================
	
	part def R2U2_Monitor_Interface_Process_i :> Process {

		part r2u2: R2U2_Monitor_Interface_i;

		// ===== INPUTs (monitored variables) 
	    // value of current temp (with status flag) from air temp sensor
	    in port current_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
	    // operator-specific alarm range for temperature for Monitor component
	    in port lower_alarm_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
	    in port upper_alarm_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
	    // control signal (on/off) to alarm component produced by the Monitor component
	    in port alarm_control : DataPort { in :> type : Isolette_Data_Model::On_Off; }
	    // enumerated type describing status/mode of monitor (Init, On/operating, or Failed) produced by the Monitor component
	    in port monitor_status : DataPort { in :> type : Isolette_Data_Model::Status; }
		// in case of internal failure, a flag is raised
		in port monitor_internal_failure : DataPort { in :> type : Isolette_Data_Model::Failure_Flag_i; }
		// mode of monitor (Init, Normal, Failed) 
		in port monitor_mode : DataPort { in :> type : Isolette_Data_Model::Monitor_Mode; }
		//  desired temperature range for temperature for Regulate component	
        in port lower_desired_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
        in port upper_desired_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
		// status of regulate temperature function
        in port regulator_status : DataPort { in :> type : Isolette_Data_Model::Status; }
        // value of current temperature to display produced by the Regulate component
        in port displayed_temp : DataPort { in :> type : Isolette_Data_Model::Temp_i; }
        // actuation command to heater produced by the Regulate component
        in port heat_control : DataPort { in :> type : Isolette_Data_Model::On_Off; }
		// in case of internal failure, a flag is raised
		in port regulator_internal_failure : DataPort { in :> type : Isolette_Data_Model::Failure_Flag_i; }
		// mode of regulator (Init, Normal, Failed) 
		in port regulator_mode : DataPort { in :> type : Isolette_Data_Model::Regulator_Mode; }

		connection uatw: PortConnection connect upper_alarm_tempWstatus to r2u2.upper_alarm_tempWstatus;
		connection latw: PortConnection connect lower_alarm_tempWstatus to r2u2.lower_alarm_tempWstatus;
		connection ctw: PortConnection connect current_tempWstatus to r2u2.current_tempWstatus;
		connection ms: PortConnection connect monitor_status to r2u2.monitor_status;
		connection ac: PortConnection connect alarm_control to r2u2.alarm_control;
		connection mif: PortConnection connect monitor_internal_failure to r2u2.monitor_internal_failure;
		connection mm: PortConnection connect monitor_mode to r2u2.monitor_mode;
		connection udtw: PortConnection connect upper_desired_tempWstatus to r2u2.upper_desired_tempWstatus;
		connection ldtw: PortConnection connect lower_desired_tempWstatus to r2u2.lower_desired_tempWstatus;
		connection rs: PortConnection connect regulator_status to r2u2.regulator_status;
		connection hc: PortConnection connect heat_control to r2u2.heat_control;
		connection rif: PortConnection connect regulator_internal_failure to r2u2.regulator_internal_failure;
		connection rm: PortConnection connect regulator_mode to r2u2.regulator_mode;

		attribute Domain: CASE_Scheduling::Domain = 13;
	}

	part def R2U2_Monitor_Interface_i :> Thread {

		attribute :>> Dispatch_Protocol = Supported_Dispatch_Protocols::Periodic;
		attribute :>> Period = 1000 [millisecond];
		attribute Domain: CASE_Scheduling::Domain = 13;
		attribute Microkit_Language: HAMR::Microkit_Language = HAMR::Microkit_Languages::Rust;

		// ===== INPUTs (monitored variables) 
	    // value of current temp (with status flag) from air temp sensor
	    in port current_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
	    // operator-specific alarm range for temperature for Monitor component
	    in port lower_alarm_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
	    in port upper_alarm_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
	    // control signal (on/off) to alarm component produced by the Monitor component
	    in port alarm_control : DataPort { in :> type : Isolette_Data_Model::On_Off; }
	    // enumerated type describing status/mode of monitor (Init, On/operating, or Failed) produced by the Monitor component
	    in port monitor_status : DataPort { in :> type : Isolette_Data_Model::Status; }
		// in case of internal failure, a flag is raised
		in port monitor_internal_failure : DataPort { in :> type : Isolette_Data_Model::Failure_Flag_i; }
		// mode of monitor (Init, Normal, Failed) 
		in port monitor_mode : DataPort { in :> type : Isolette_Data_Model::Monitor_Mode; }
		//  desired temperature range for temperature for Regulate component	
        in port lower_desired_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
        in port upper_desired_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
		// status of regulate temperature function
        in port regulator_status : DataPort { in :> type : Isolette_Data_Model::Status; }
        // value of current temperature to display produced by the Regulate component
        in port displayed_temp : DataPort { in :> type : Isolette_Data_Model::Temp_i; }
        // actuation command to heater produced by the Regulate component
        in port heat_control : DataPort { in :> type : Isolette_Data_Model::On_Off; }
		// in case of internal failure, a flag is raised
		in port regulator_internal_failure : DataPort { in :> type : Isolette_Data_Model::Failure_Flag_i; }
		// mode of regulator (Init, Normal, Failed) 
		in port regulator_mode : DataPort { in :> type : Isolette_Data_Model::Regulator_Mode; }
	}
}