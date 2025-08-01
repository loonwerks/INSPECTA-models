// Monitor.sysml
package Monitor {
	
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

				package Manage_Monitor_Interface_iContracts {
		  import Isolette_Data_Model::*
		  import Base_Types::*

		  attribute lastCmd: Isolette_Data_Model::On_Off

		  calc def timeout_condition_satisfied {
		    return true
		  }

		  requirement def InitializeContract {
		    require constraint { (monitor_status == Isolette_Data_Model::Status::Init_Status) }
		  }

		  requirement def MonitorIntegration {
		    doc /*Range http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=112*/
		    assume constraint { ((96 <= lower_alarm_tempWstatus.degrees) and (lower_alarm_tempWstatus.degrees <= 101)) }
		    doc /*Range http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=112*/
		    assume constraint { ((97 <= upper_alarm_tempWstatus.degrees) and (upper_alarm_tempWstatus.degrees <= 102)) }
		    require constraint { true }
		  }

		  // compute case REQ_MMI_1
		  requirement def ComputeCase_REQ_MMI_1 {
		    refines ComputeBase
		    doc /*If the Manage Monitor Interface mode is INIT,
											|the Monitor Status shall be set to Init.
											|http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 */
		    assume constraint { (monitor_mode == Isolette_Data_Model::Monitor_Mode.Init_Monitor_Mode) }
		    require constraint { (monitor_status == Isolette_Data_Model::Status::Init_Status) }
		  }

		  // compute case REQ_MMI_2
		  requirement def ComputeCase_REQ_MMI_2 {
		    refines ComputeBase
		    doc /*If the Manage Monitor Interface mode is NORMAL,
											|the Monitor Status shall be set to On
											|http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 */
		    assume constraint { (monitor_mode == Isolette_Data_Model::Monitor_Mode.Normal_Monitor_Mode) }
		    require constraint { (monitor_status == Isolette_Data_Model::Status::On_Status) }
		  }

		  // compute case REQ_MMI_3
		  requirement def ComputeCase_REQ_MMI_3 {
		    refines ComputeBase
		    doc /*If the Manage Monitor Interface mode is FAILED,
											|the Monitor Status shall be set to Failed.
											|Latency: < Max Operator Response Time
											|Tolerance: N/A
											|http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 */
		    assume constraint { (monitor_mode == Isolette_Data_Model::Monitor_Mode.Failed_Monitor_Mode) }
		    require constraint { (monitor_status == Isolette_Data_Model::Status::Failed_Status) }
		  }

		  // compute case REQ_MMI_4
		  requirement def ComputeCase_REQ_MMI_4 {
		    refines ComputeBase
		    doc /*If the Status attribute of the Lower Alarm Temperature
											|or the Upper Alarm Temperature is Invalid,
											|the Monitor Interface Failure shall be set to True
											|http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 */
		    assume constraint { ((lower_alarm_tempWstatus.status == Isolette_Data_Model::ValueStatus.Invalid) or (upper_alarm_tempWstatus.status == Isolette_Data_Model::ValueStatus.Invalid)) }
		    require constraint { interface_failure.flag }
		  }

		  // compute case REQ_MMI_5
		  requirement def ComputeCase_REQ_MMI_5 {
		    refines ComputeBase
		    doc /*If the Status attribute of the Lower Alarm Temperature
												|and the Upper Alarm Temperature is Valid,
												|the Monitor Interface Failure shall be set to False
												|http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 */
		    assume constraint { ((lower_alarm_tempWstatus.status == Isolette_Data_Model::ValueStatus.Valid) and (upper_alarm_tempWstatus.status == Isolette_Data_Model::ValueStatus.Valid)) }
		    require constraint { (not interface_failure.flag) }
		  }

		  // compute case REQ_MMI_6
		  requirement def ComputeCase_REQ_MMI_6 {
		    refines ComputeBase
		    doc /*If the Monitor Interface Failure is False,
											|the Alarm Range variable shall be set to the Desired Temperature Range
											|http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 */
		    assume constraint { true }
		    require constraint { (((not interface_failure.flag)) implies (((lower_alarm_temp.degrees == lower_alarm_tempWstatus.degrees) and (upper_alarm_temp.degrees == upper_alarm_tempWstatus.degrees)))) }
		  }

		  // compute case REQ_MMI_7
		  requirement def ComputeCase_REQ_MMI_7 {
		    refines ComputeBase
		    doc /*If the Monitor Interface Failure is True,
											|the Alarm Range variable is UNSPECIFIED
											|http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 */
		    assume constraint { true }
		    require constraint { (interface_failure.flag implies true) }
		  }

		}
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
		
				package Manage_Monitor_Mode_iContracts {
		  import Isolette_Data_Model::*
		  import Base_Types::*

		  attribute lastMonitorMode: Isolette_Data_Model::Monitor_Mode

		  calc def timeout_condition_satisfied {
		    return F
		  }

		  requirement def InitializeContract {
		    doc /*Upon the first dispatch of the thread, the monitor mode is Init. http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=114*/
		    require constraint { (monitor_mode == Isolette_Data_Model::Monitor_Mode.Init_Monitor_Mode) }
		  }

		  // compute case REQ_MMM_2
		  requirement def ComputeCase_REQ_MMM_2 {
		    refines ComputeBase
		    doc /*If the current mode is Init, then
											|the mode is set to NORMAL iff the monitor status is true (valid) (see Table A-15), i.e.,
											|if  NOT (Monitor Interface Failure OR Monitor Internal Failure)
											|AND Current Temperature.Status = Valid
											|http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=114 */
		    assume constraint { (lastMonitorMode == Isolette_Data_Model::Monitor_Mode.Init_Monitor_Mode) }
		    require constraint { (((((not ((interface_failure.flag or internal_failure.flag)))) and (current_tempWstatus.status == Isolette_Data_Model::ValueStatus.Valid))) implies ((monitor_mode == Isolette_Data_Model::Monitor_Mode.Normal_Monitor_Mode))) }
		  }

		  // compute case REQ_MMM_3
		  requirement def ComputeCase_REQ_MMM_3 {
		    refines ComputeBase
		    doc /*If the current Monitor mode is Normal, then
											|the Monitor mode is set to Failed iff
											|the Monitor status is false, i.e.,
											|if  (Monitor Interface Failure OR Monitor Internal Failure)
											|OR NOT(Current Temperature.Status = Valid)
											|http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=114 */
		    assume constraint { (lastMonitorMode == Isolette_Data_Model::Monitor_Mode.Normal_Monitor_Mode) }
		    require constraint { (((((interface_failure.flag or internal_failure.flag)) or (current_tempWstatus.status != Isolette_Data_Model::ValueStatus.Valid))) implies ((monitor_mode == Isolette_Data_Model::Monitor_Mode.Failed_Monitor_Mode))) }
		  }

		  // compute case REQ_MMM_4
		  requirement def ComputeCase_REQ_MMM_4 {
		    refines ComputeBase
		    doc /*If the current mode is Init, then
											|the mode is set to Failed iff the time during
											|which the thread has been in Init mode exceeds the
											|Monitor Init Timeout value.
											|http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=114 */
		    assume constraint { (lastMonitorMode == Isolette_Data_Model::Monitor_Mode.Init_Monitor_Mode) }
		    require constraint { (false == ((monitor_mode == Isolette_Data_Model::Monitor_Mode.Failed_Monitor_Mode))) }
		  }

		}
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

				package Manage_Alarm_iContracts {
		  import Isolette_Data_Model::*
		  import Base_Types::*

		  attribute lastCmd: Isolette_Data_Model::On_Off

		  calc def timeout_condition_satisfied {
		    return true
		  }

		  requirement def InitializeContract {
		    doc /*If the Monitor Mode is INIT, the Alarm Control shall be set to Off. http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=115*/
		    require constraint { ((alarm_control == Isolette_Data_Model::On_Off.Off) and (lastCmd == Isolette_Data_Model::On_Off.Off)) }
		  }

		  // compute case REQ_MA_1
		  requirement def ComputeCase_REQ_MA_1 {
		    refines ComputeBase
		    doc /*If the Monitor Mode is INIT, the Alarm Control shall be set
											|to Off.
											|http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=115 */
		    assume constraint { (monitor_mode == Isolette_Data_Model::Monitor_Mode.Init_Monitor_Mode) }
		    require constraint { ((alarm_control == Isolette_Data_Model::On_Off.Off) and (lastCmd == Isolette_Data_Model::On_Off.Off)) }
		  }

		  // compute case REQ_MA_2
		  requirement def ComputeCase_REQ_MA_2 {
		    refines ComputeBase
		    doc /*If the Monitor Mode is NORMAL and the Current Temperature is
											|less than the Lower Alarm Temperature or greater than the Upper Alarm
											|Temperature, the Alarm Control shall be set to On.
											|http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=115 */
		    assume constraint { ((monitor_mode == Isolette_Data_Model::Monitor_Mode.Normal_Monitor_Mode) and (((current_tempWstatus.degrees < lower_alarm_temp.degrees) or (current_tempWstatus.degrees > upper_alarm_temp.degrees)))) }
		    require constraint { ((alarm_control == Isolette_Data_Model::On_Off.Onn) and (lastCmd == Isolette_Data_Model::On_Off.Onn)) }
		  }

		  // compute case REQ_MA_3
		  requirement def ComputeCase_REQ_MA_3 {
		    refines ComputeBase
		    doc /*If the Monitor Mode is NORMAL and the Current Temperature
											|is greater than or equal to the Lower Alarm Temperature and less than
											|the Lower Alarm Temperature +0.5 degrees, or the Current Temperature is
											|greater than the Upper Alarm Temperature -0.5 degrees and less than or equal
											|to the Upper Alarm Temperature, the value of the Alarm Control shall
											|not be changed.
											|http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=115 */
		    assume constraint { ((monitor_mode == Isolette_Data_Model::Monitor_Mode.Normal_Monitor_Mode) and (((((current_tempWstatus.degrees >= lower_alarm_temp.degrees) and (current_tempWstatus.degrees < (lower_alarm_temp.degrees + 1)))) or ((((current_tempWstatus.degrees > (upper_alarm_temp.degrees - 1))) and (current_tempWstatus.degrees <= upper_alarm_temp.degrees)))))) }
		    require constraint { ((alarm_control == In(lastCmd)) and (lastCmd == In(lastCmd))) }
		  }

		  // compute case REQ_MA_4
		  requirement def ComputeCase_REQ_MA_4 {
		    refines ComputeBase
		    doc /*If the Monitor Mode is NORMAL and the value of the Current
											|Temperature is greater than or equal to the Lower Alarm Temperature
											|+0.5 degrees and less than or equal to the Upper Alarm Temperature
											|-0.5 degrees, the Alarm Control shall be set to Off.
											|http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=115 */
		    assume constraint { (((monitor_mode == Isolette_Data_Model::Monitor_Mode.Normal_Monitor_Mode) and (current_tempWstatus.degrees >= (lower_alarm_temp.degrees + 1))) and (current_tempWstatus.degrees <= (upper_alarm_temp.degrees - 1))) }
		    require constraint { ((alarm_control == Isolette_Data_Model::On_Off.Off) and (lastCmd == Isolette_Data_Model::On_Off.Off)) }
		  }

		  // compute case REQ_MA_5
		  requirement def ComputeCase_REQ_MA_5 {
		    refines ComputeBase
		    doc /*If the Monitor Mode is FAILED, the Alarm Control shall be
											|set to On.
											|http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=116 */
		    assume constraint { (monitor_mode == Isolette_Data_Model::Monitor_Mode.Failed_Monitor_Mode) }
		    require constraint { ((alarm_control == Isolette_Data_Model::On_Off.Onn) and (lastCmd == Isolette_Data_Model::On_Off.Onn)) }
		  }

		}		
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
