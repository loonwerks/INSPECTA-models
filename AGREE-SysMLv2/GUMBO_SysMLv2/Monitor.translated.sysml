// Monitor.sysml
package Monitor {
    
    private import Isolette_Data_Model::*;
    private import AADL::*;
    private import AADL_Project::*;
    private import AADL_Project::Time_Units::*;
        
    part def Monitor_Temperature_i :> System {
        part mmi : Manage_Monitor_Interface_Process_i;
        part ma : Manage_Alarm_Process_i;
        part mmm : Manage_Monitor_Mode_Process_i;
        part dmf: Detect_Monitor_Failure_Process_i;
        
        in port current_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
        in port lower_alarm_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
        in port upper_alarm_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
        out port alarm_control : DataPort { out :> type : Isolette_Data_Model::On_Off; }
        out port monitor_status : DataPort { out :> type : Isolette_Data_Model::Status; }

        connection muat : PortConnection connect upper_alarm_tempWstatus to mmi.upper_alarm_tempWstatus;
        connection mlat : PortConnection connect lower_alarm_tempWstatus to mmi.lower_alarm_tempWstatus;
        connection mcti : PortConnection connect current_tempWstatus to mmi.current_tempWstatus;
        connection mcta : PortConnection connect current_tempWstatus to ma.current_tempWstatus;
        connection mctm : PortConnection connect current_tempWstatus to mmm.current_tempWstatus;
        connection malrm : PortConnection connect ma.alarm_control to alarm_control;
        connection mms : PortConnection connect mmi.monitor_status to monitor_status;
        connection maul : PortConnection connect mmi.upper_alarm_temp to ma.upper_alarm_temp;
        connection mall : PortConnection connect mmi.lower_alarm_temp to ma.lower_alarm_temp;
        connection mmmi : PortConnection connect mmm.monitor_mode to mmi.monitor_mode;
        connection mmma : PortConnection connect mmm.monitor_mode to ma.monitor_mode;
        connection intff : PortConnection connect mmi.interface_failure to mmm.interface_failure;
        connection mif: PortConnection connect dmf.internal_failure to mmm.internal_failure;
    }

    part def Manage_Monitor_Interface_Process_i :> Process {
        part mmi: Manage_Monitor_Interface_i;
        in port upper_alarm_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
        in port lower_alarm_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
        in port current_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
        in port monitor_mode : DataPort { in :> type : Isolette_Data_Model::Monitor_Mode; }
        out port upper_alarm_temp : DataPort { out :> type : Isolette_Data_Model::Temp_i; }
        out port lower_alarm_temp : DataPort { out :> type : Isolette_Data_Model::Temp_i; }
        out port monitor_status : DataPort { out :> type : Isolette_Data_Model::Status; }
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
        in port upper_alarm_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
        in port lower_alarm_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
        in port current_tempWstatus : DataPort { in :> type : Isolette_Data_Model::TempWstatus_i; }
        in port monitor_mode : DataPort { in :> type : Isolette_Data_Model::Monitor_Mode; }
        out port upper_alarm_temp : DataPort { out :> type : Isolette_Data_Model::Temp_i; }
        out port lower_alarm_temp : DataPort { out :> type : Isolette_Data_Model::Temp_i; }
        out port monitor_status : DataPort { out :> type : Isolette_Data_Model::Status; }
        out port interface_failure : DataPort { out :> type : Isolette_Data_Model::Failure_Flag_i; }
        
    }
}


package IsoletteContracts {
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
    assume constraint { (((96 <= lower_alarm_tempWstatus.degrees) and and) and (lower_alarm_tempWstatus.degrees <= 101)) and (((97 <= upper_alarm_tempWstatus.degrees) and and) and (upper_alarm_tempWstatus.degrees <= 102)) }
    require constraint { true }
  }

  // If the Manage Monitor Interface mode is INIT,\n                                    the Monitor Status shall be set to Init.
  requirement def REQ_MMI_1 {
    doc /* If the Manage Monitor Interface mode is INIT,\n                                    the Monitor Status shall be set to Init. */
    assume constraint { (monitor_mode == Isolette_Data_Model::Monitor_Mode::Init_Monitor_Mode) }
    require constraint { (monitor_status == Isolette_Data_Model::Status::Init_Status) }
  }

  // If the Manage Monitor Interface mode is NORMAL,\n                                    the Monitor Status shall be set to On
  requirement def REQ_MMI_2 {
    doc /* If the Manage Monitor Interface mode is NORMAL,\n                                    the Monitor Status shall be set to On */
    assume constraint { (monitor_mode == Isolette_Data_Model::Monitor_Mode::Normal_Monitor_Mode) }
    require constraint { (monitor_status ==? Isolette_Data_Model::Status::On_Status) }
  }

  // If the Manage Monitor Interface mode is FAILED,\n                                    |the Monitor Status shall be set to Failed.\n                                    |Latency: < Max Operator Response Time\n                                    |Tolerance: N/A\n                                    |http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 
  requirement def REQ_MMI_3 {
    doc /* If the Manage Monitor Interface mode is FAILED,\n                                    |the Monitor Status shall be set to Failed.\n                                    |Latency: < Max Operator Response Time\n                                    |Tolerance: N/A\n                                    |http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113  */
    assume constraint { (monitor_mode == Isolette_Data_Model::Monitor_Mode::Failed_Monitor_Mode) }
    require constraint { (monitor_status == Isolette_Data_Model::Status::Failed_Status) }
  }

  // If the Status attribute of the Lower Alarm Temperature\n                                    |or the Upper Alarm Temperature is Invalid,\n                                    |the Monitor Interface Failure shall be set to True\n                                    |http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 
  requirement def REQ_MMI_4 {
    doc /* If the Status attribute of the Lower Alarm Temperature\n                                    |or the Upper Alarm Temperature is Invalid,\n                                    |the Monitor Interface Failure shall be set to True\n                                    |http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113  */
    assume constraint { ((lower_alarm_tempWstatus.status == Isolette_Data_Model::ValueStatus::Invalid) or (upper_alarm_tempWstatus.status == Isolette_Data_Model::ValueStatus::Invalid)) }
    require constraint { interface_failure.flag }
  }

  // If the Status attribute of the Lower Alarm Temperature\n                                        |and the Upper Alarm Temperature is Valid,\n                                        |the Monitor Interface Failure shall be set to False\n                                        |http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 
  requirement def REQ_MMI_5 {
    doc /* If the Status attribute of the Lower Alarm Temperature\n                                        |and the Upper Alarm Temperature is Valid,\n                                        |the Monitor Interface Failure shall be set to False\n                                        |http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113  */
    assume constraint { ((lower_alarm_tempWstatus.status == Isolette_Data_Model::ValueStatus::Valid) and (upper_alarm_tempWstatus.status == Isolette_Data_Model::ValueStatus::Valid)) }
    require constraint { (not interface_failure.flag) }
  }

  // If the Monitor Interface Failure is False,\n                                    |the Alarm Range variable shall be set to the Desired Temperature Range\n                                    |http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 
  requirement def REQ_MMI_6 {
    doc /* If the Monitor Interface Failure is False,\n                                    |the Alarm Range variable shall be set to the Desired Temperature Range\n                                    |http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113  */
    assume constraint { true }
    require constraint { (((not interface_failure.flag)) implies (((lower_alarm_temp.degrees == lower_alarm_tempWstatus.degrees) and (upper_alarm_temp.degrees == upper_alarm_tempWstatus.degrees)))) }
  }

  // If the Monitor Interface Failure is True,\n                                    |the Alarm Range variable is UNSPECIFIED\n                                    |http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 
  requirement def REQ_MMI_7 {
    doc /* If the Monitor Interface Failure is True,\n                                    |the Alarm Range variable is UNSPECIFIED\n                                    |http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113  */
    assume constraint { true }
    require constraint { (interface_failure.flag implies true) }
  }

}
