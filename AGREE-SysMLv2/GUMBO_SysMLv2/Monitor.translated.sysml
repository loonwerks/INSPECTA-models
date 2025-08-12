// Monitor.sysml
package Monitor {
	private import GUMBO_Contracts::*;

	
	private import Isolette_Data_Model::*;
	private import AADL::*;
	private import AADL_Project::*;
    private import AADL_Project::Time_Units::*;
		
	//A.5.2 Monitor Temperature Function
	// thread to manage the interactions between with operator interface and the rest
    // of the monitor process
	part def Monitor_Temperature_i :> System {

	    //thread for monitor interface implementation
	    part mmi : Manage_Monitor_Interface_Process_i;

	    //thread for manage alarm implementation
	    part ma : Manage_Alarm_Process_i;
	    //thread for monitor mode implementation
	    part mmm : Manage_Monitor_Mode_Process_i;

		part dmf: Detect_Monitor_Failure_Process_i;

		// ===== INPUTs (monitored variables) ...based in part on Figure A.2 and Table A.5 
	    // value of current temp (with status flag) from air temp sensor
	    in port current_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
	    // operator-specific alarm range for temperature
	    in port lower_alarm_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
	    in port upper_alarm_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
	    
	    // ===== OUTPUTs (controlled variables)  ...based in part on Figure A.2 and Table A.6
	    // control signal (on/off) to alarm component 
	    out port alarm_control : DataPort { out :> type : Isolette_Data_Model::On_Off; }
	    // enumerated type describing status/mode of monitor (Init, On/operating, or Failed)
	    out port monitor_status : DataPort { out :> type : Isolette_Data_Model::Status; }
	    
	    // ==== INPUT INTERFACE values to internal components ====
	    // send incoming alarm temperature range to monitor interface manager
	    connection muat : PortConnection
	    	connect upper_alarm_tempWstatus to mmi.upper_alarm_tempWstatus;
	    connection mlat : PortConnection
	    	connect lower_alarm_tempWstatus to mmi.lower_alarm_tempWstatus;
	  
	    // send incoming current temperature (with status value)
	    //   ...to interface manager
	    connection mcti : PortConnection
	    	connect current_tempWstatus to mmi.current_tempWstatus;
  	    //   ...to alarm manager (controls alarm)
	    connection mcta : PortConnection
	    	connect current_tempWstatus to ma.current_tempWstatus;
	    //   ...to mode manager
	    connection mctm : PortConnection
	    	connect current_tempWstatus to mmm.current_tempWstatus;
	
	    // ==== OUTPUT INTERFACE values from internal components ==== 
	    // send alarm actuation command (on/off) 
	    connection malrm : PortConnection
	    	connect ma.alarm_control to alarm_control;
	    // send status of monitor subsystem  to operator interface
	    connection mms : PortConnection
	    	connect mmi.monitor_status to monitor_status;
	  
	    // ==== INTERNAL communication ====
	    // send validated alarm range from interface manager to alarm manager (controller)
   	    connection maul : PortConnection
   	    	connect mmi.upper_alarm_temp to ma.upper_alarm_temp;
	    connection mall : PortConnection
	    	connect mmi.lower_alarm_temp to ma.lower_alarm_temp;
	
	    // send monitor mode from mode manager 
	    //  ... to interface manager
	    connection mmmi : PortConnection
	    	connect mmm.monitor_mode to mmi.monitor_mode;
	    //  ... to alarm manager
	    connection mmma : PortConnection
	    	connect mmm.monitor_mode to ma.monitor_mode;
	  
	    // send regulator interface failure to mode manager
	    connection intff : PortConnection
	    	connect mmi.interface_failure to mmm.interface_failure;

		connection mif: PortConnection 
			connect dmf.internal_failure to mmm.internal_failure;
	}
	
	// ===========================================================================================================
    // 
	//   M a n a g e    M o n i t o r   I n t e r f a c e   T h r e a d
	// 
	// 
	// A.5.2.1 Manage Monitor Interface Function
	// 
	// ===========================================================================================================
	
	part def Manage_Monitor_Interface_Process_i :> Process {

		part mmi: Manage_Monitor_Interface_i;

		// ==== INPUTS ====	(see Figure A.5)
		//  operator specified alarm range
		in port upper_alarm_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
		in port lower_alarm_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
		// current temperature from temp sensor
		in port current_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
		// current mode of monitor subsystem (init, normal or failed)
		in port monitor_mode : DataPort { in :> type : Isolette_Data_Model::Monitor_Mode; }
		
		// ==== OUTPUTS ====	(see Figure A.5)  
		// validated ("valid" status confirmed & removed) alarm temperature range from operator interface
		out port upper_alarm_temp : DataPort { out :> type : Isolette_Data_Model::Temp_i; }
		out port lower_alarm_temp : DataPort { out :> type : Isolette_Data_Model::Temp_i; }
		// overall status of monitor subsystem sent to operator interface
	    out port monitor_status : DataPort { out :> type : Isolette_Data_Model::Status; }
	    // flag indicating improper inputs; sent to determine overall subsystem mode 
		out port interface_failure : DataPort { out :> type : Isolette_Data_Model::Failure_Flag_i; }

		connection uatw: PortConnection connect upper_alarm_tempWstatus to mmi.upper_alarm_tempWstatus;
		connection latw: PortConnection connect lower_alarm_tempWstatus to mmi.lower_alarm_tempWstatus;
		connection ctw: PortConnection connect current_tempWstatus to mmi.current_tempWstatus;
		connection mm: PortConnection connect monitor_mode to mmi.monitor_mode;

		connection uat: PortConnection connect mmi.upper_alarm_temp to upper_alarm_temp;
		connection lat: PortConnection connect mmi.lower_alarm_temp to lower_alarm_temp;
		connection ms: PortConnection connect mmi.monitor_status to monitor_status;
		connection ifc: PortConnection connect mmi.interface_failure to interface_failure;

		attribute Domain: CASE_Scheduling::Domain = 4;
	}

	part def Manage_Monitor_Interface_i :> Thread {

		attribute :>> Dispatch_Protocol = Supported_Dispatch_Protocols::Periodic;
		attribute :>> Period = 1000 [millisecond];
		attribute Domain: CASE_Scheduling::Domain = 4;
		attribute Microkit_Language: HAMR::Microkit_Language = HAMR::Microkit_Languages::Rust;

		// ==== INPUTS ====	(see Figure A.5)
		//  operator specified alarm range
		in port upper_alarm_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
		in port lower_alarm_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
		// current temperature from temp sensor
		in port current_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
		// current mode of monitor subsystem (init, normal or failed)
		in port monitor_mode : DataPort { in :> type : Isolette_Data_Model::Monitor_Mode; }
		
		// ==== OUTPUTS ====	(see Figure A.5)  
		// validated ("valid" status confirmed & removed) alarm temperature range from operator interface
		out port upper_alarm_temp : DataPort { out :> type : Isolette_Data_Model::Temp_i; }
		out port lower_alarm_temp : DataPort { out :> type : Isolette_Data_Model::Temp_i; }
		// overall status of monitor subsystem sent to operator interface
	    out port monitor_status : DataPort { out :> type : Isolette_Data_Model::Status; }
	    // flag indicating improper inputs; sent to determine overall subsystem mode 
		out port interface_failure : DataPort { out :> type : Isolette_Data_Model::Failure_Flag_i; }

				// GUMBO contracts moved to GUMBO_Contracts::Manage_Monitor_Interface_i_Contracts

	}
	
	// ===========================================================================================================
	//
	//   M a n a g e    M o n i t o r   M o d e   T h r e a d
	//
	//  
	//   A.5.2.2 Manage Monitor Mode Function
	// 
	// ===========================================================================================================
	
	// A.5.2.2 Manage Monitor Mode Function
	
	part def Manage_Monitor_Mode_Process_i :> Process {
		
		part mmm: Manage_Monitor_Mode_i;

		// ==== INPUTS ========		
		// current temperature from temp sensor
		in port current_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
		// status of operator interface interactions
		in port interface_failure : DataPort { in :> type : Isolette_Data_Model::Failure_Flag_i; }
		// in case of internal failure, a flag is raised
		in port internal_failure : DataPort { in :> type : Isolette_Data_Model::Failure_Flag_i; }
		   
		// ==== OUTPUTS ========		
		// mode of regulator (Init, Normal, Failed)   
		out port monitor_mode : DataPort { out :> type : Isolette_Data_Model::Monitor_Mode; }

		connection ctw: PortConnection connect current_tempWstatus to mmm.current_tempWstatus;
		connection iff: PortConnection connect interface_failure to mmm.interface_failure;
		connection inf: PortConnection connect internal_failure to mmm.internal_failure;

		connection mm: PortConnection connect mmm.monitor_mode to monitor_mode;

		attribute Domain: CASE_Scheduling::Domain = 3;
	}

	part def Manage_Monitor_Mode_i :> Thread {
		
		attribute :>> Dispatch_Protocol = Supported_Dispatch_Protocols::Periodic;
		attribute :>> Period = 1000 [millisecond];
		attribute Domain: CASE_Scheduling::Domain = 3;
		attribute Microkit_Language: HAMR::Microkit_Language = HAMR::Microkit_Languages::Rust;

		// ==== INPUTS ========		
		// current temperature from temp sensor
		in port current_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
		// status of operator interface interactions
		in port interface_failure : DataPort { in :> type : Isolette_Data_Model::Failure_Flag_i; }
		// in case of internal failure, a flag is raised
		in port internal_failure : DataPort { in :> type : Isolette_Data_Model::Failure_Flag_i; }
		   
		// ==== OUTPUTS ========		
		// mode of regulator (Init, Normal, Failed)   
		out port monitor_mode : DataPort { out :> type : Isolette_Data_Model::Monitor_Mode; }
		
				// GUMBO contracts moved to GUMBO_Contracts::Manage_Monitor_Mode_i_Contracts

	}
	
	// ===========================================================================================================
	//
	//   M a n a g e    A l a r m    T h r e a d
	//
	//  
	//    A.5.2.3 Manage Alarm Function
	// 
	// ===========================================================================================================
	
	
	// A.5.2.3 Manage Alarm Function
	// The Manage Alarm Function turns the Alarm Control on when the Current
	// Temperature of the Room falls below or rises above the Alarm Temperature Range.
	
	part def Manage_Alarm_Process_i :> Process {

		part ma: Manage_Alarm_i;

		// ======== INPUTS =======
		// current temperature (from temp sensor)
		in port current_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
		// lowest and upper bound of operator specified alarm temperature range
		in port lower_alarm_temp : DataPort { in :> type : Isolette_Data_Model::Temp_i; }
		in port upper_alarm_temp : DataPort { in :> type : Isolette_Data_Model::Temp_i; }
		// subsystem mode
		in port monitor_mode : DataPort { in :> type : Isolette_Data_Model::Monitor_Mode; }
		  
		// ======== OUTPUTS =======
		// command to turn alarm on/off (actuation command)  
		out port alarm_control : DataPort { out :> type : Isolette_Data_Model::On_Off; }

		connection ctw: PortConnection connect current_tempWstatus to ma.current_tempWstatus;
		connection lat: PortConnection connect lower_alarm_temp to ma.lower_alarm_temp;
		connection uat: PortConnection connect upper_alarm_temp to ma.upper_alarm_temp;
		connection mm: PortConnection connect monitor_mode to ma.monitor_mode;

		connection ac: PortConnection connect ma.alarm_control to alarm_control;

		attribute Domain: CASE_Scheduling::Domain = 5;
	}

	part def Manage_Alarm_i :> Thread {
		attribute :>> Dispatch_Protocol = Supported_Dispatch_Protocols::Periodic;
		attribute :>> Period = 1000 [millisecond];
		attribute Domain: CASE_Scheduling::Domain = 5;
		attribute Microkit_Language: HAMR::Microkit_Language = HAMR::Microkit_Languages::Rust;

		// ======== INPUTS =======
		// current temperature (from temp sensor)
		in port current_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
		// lowest and upper bound of operator specified alarm temperature range
		in port lower_alarm_temp : DataPort { in :> type : Isolette_Data_Model::Temp_i; }
		in port upper_alarm_temp : DataPort { in :> type : Isolette_Data_Model::Temp_i; }
		// subsystem mode
		in port monitor_mode : DataPort { in :> type : Isolette_Data_Model::Monitor_Mode; }
		  
		// ======== OUTPUTS =======
		// command to turn alarm on/off (actuation command)  
		out port alarm_control : DataPort { out :> type : Isolette_Data_Model::On_Off; }

				// GUMBO contracts moved to GUMBO_Contracts::Manage_Alarm_i_Contracts
		
	}

	// ===========================================================================================================
	//
	//   D e t e c t    M o n i t o r    T h r e a d
	//
	//  
	//    A.5.2.4 Detect Monitor Failure Function
	//    "The Detect Monitor Failure Function identifies internal failures, (e.g., a memory check failure)
	//     in the Monitor Temperature Function. It defines a single Boolean-valued internal variable,
	//     Monitor Internal Failure, which is set to True if an internal failure is detected. "
	// 
	//    http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=116
	// ===========================================================================================================	

	part def Detect_Monitor_Failure_Process_i :> Process {

		part dmf: Detect_Monitor_Failure_i;

		out port internal_failure : DataPort { out :> type : Isolette_Data_Model::Failure_Flag_i; }

		connection inf: PortConnection connect dmf.internal_failure to internal_failure;

		attribute Domain: CASE_Scheduling::Domain = 6;
	}

	part def Detect_Monitor_Failure_i :> Thread {

		attribute :>> Dispatch_Protocol = Supported_Dispatch_Protocols::Periodic;
		attribute :>> Period = 1000 [millisecond];
		attribute Domain: CASE_Scheduling::Domain = 6;
		attribute Microkit_Language: HAMR::Microkit_Language = HAMR::Microkit_Languages::Rust;

		out port internal_failure : DataPort { out :> type : Isolette_Data_Model::Failure_Flag_i; }

	}
}
