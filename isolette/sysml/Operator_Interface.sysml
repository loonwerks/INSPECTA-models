package Operator_Interface {
  
  private import Isolette_Data_Model::*;
  private import HAMR::*;

  // =====================================================================================================================
  //
  //   O p e r a t o r     I n t e r f a c e
  // 
  //   See Section A-5 and Figure A-2
  //
  // =====================================================================================================================

  part def Operator_Interface_System_i :> System {
    
    part oip : Operator_Interface_Process_i;
        
    // ======== INPUT from Operator to Operator Interface =======		
    in port operator_commands : DataPort { in :>> type : Isolette_Environment::Interface_Interaction; }
    // ======== OUTPUT from Operator Interface to Operator  =======	
    // models (abstractly) visual information presented to the operator on Isolette operator interface
    out port operator_visual_information : DataPort { out :>> type : Isolette_Environment::Interface_Interaction; }
    // models (abstraction) audio information presented to the operator on Isolette operator interface
    out port operator_alarm : DataPort { out :>> type : Isolette_Environment::Interface_Interaction; }
    // models (abstractly) the sensing of the physical air temperature
    // ==== INPUTS from thermostat to operator interface 
    in port regulator_status : DataPort { in :>> type : Isolette_Data_Model::Status; }
    in port monitor_status : DataPort { in :>> type : Isolette_Data_Model::Status; }
    in port display_temperature : DataPort { in :>> type : Isolette_Data_Model::Temp_i; }
    in port alarm_control : DataPort { in :>> type : Isolette_Data_Model::On_Off; }

    // ==== OUTPUTS from operator interface to thermostat 
    out port lower_desired_tempWstatus : DataPort { out :>> type : Isolette_Data_Model::TempWstatus_i; }
    out port upper_desired_tempWstatus : DataPort { out :>> type : Isolette_Data_Model::TempWstatus_i; }
    out port lower_alarm_tempWstatus : DataPort { out :>> type : Isolette_Data_Model::TempWstatus_i; }
    out port upper_alarm_tempWstatus : DataPort { out :>> type : Isolette_Data_Model::TempWstatus_i; }

    connection c1: PortConnection
      connect regulator_status to oip.regulator_status;
    connection c2: PortConnection
      connect monitor_status to oip.monitor_status;
    connection c3: PortConnection
      connect display_temperature to oip.display_temperature;
    connection c4: PortConnection
      connect alarm_control to oip.alarm_control;

    connection c5: PortConnection
      connect oip.lower_desired_tempWstatus to lower_desired_tempWstatus;
    connection c6: PortConnection
      connect oip.upper_desired_tempWstatus to upper_desired_tempWstatus;
    connection c7: PortConnection
      connect oip.lower_alarm_tempWstatus to lower_alarm_tempWstatus;
    connection c8: PortConnection
      connect oip.upper_alarm_tempWstatus to upper_alarm_tempWstatus;
  }
  
  part def Operator_Interface_Process_i :> Process {
    
    part oit : Operator_Interface_Thread_i;
        
    // ==== INPUTS from thermostat to operator interface
    in port regulator_status : DataPort { in :>> type : Isolette_Data_Model::Status; }
    in port monitor_status : DataPort { in :>> type : Isolette_Data_Model::Status; }
    in port display_temperature : DataPort { in :>> type : Isolette_Data_Model::Temp_i; }
    in port alarm_control : DataPort { in :>> type : Isolette_Data_Model::On_Off; }

    // ==== OUTPUTS from operator interface to thermostat 
    out port lower_desired_tempWstatus : DataPort { out :>> type : Isolette_Data_Model::TempWstatus_i; }
    out port upper_desired_tempWstatus : DataPort { out :>> type : Isolette_Data_Model::TempWstatus_i; }
    out port lower_alarm_tempWstatus : DataPort { out :>> type : Isolette_Data_Model::TempWstatus_i; }
    out port upper_alarm_tempWstatus : DataPort { out :>> type : Isolette_Data_Model::TempWstatus_i; }

    connection c1: PortConnection
      connect regulator_status to oit.regulator_status;
    connection c2: PortConnection
      connect monitor_status to oit.monitor_status;
    connection c3: PortConnection
      connect  display_temperature to oit.display_temperature;
    connection c4: PortConnection
      connect alarm_control to oit.alarm_control;

    connection c5: PortConnection
      connect oit.lower_desired_tempWstatus to lower_desired_tempWstatus;
    connection c6: PortConnection
      connect oit.upper_desired_tempWstatus to upper_desired_tempWstatus;
    connection c7: PortConnection
      connect oit.lower_alarm_tempWstatus to lower_alarm_tempWstatus;
    connection c8: PortConnection
      connect oit.upper_alarm_tempWstatus to upper_alarm_tempWstatus;

    attribute :>> Domain = 12;
  }
  
  part def Operator_Interface_Thread_i :> Thread {

    attribute :>> Dispatch_Protocol = Supported_Dispatch_Protocols::Periodic;
    attribute :>> Period = 1000 [ms];
    attribute :>> Implementation_Language = Implementation_Languages::Rust;
    
    // ==== INPUTS from thermostat to operator interface
    in port regulator_status : DataPort { in :>> type : Isolette_Data_Model::Status; }
    in port monitor_status : DataPort { in :>> type : Isolette_Data_Model::Status; }
    in port display_temperature : DataPort { in :>> type : Isolette_Data_Model::Temp_i; }
    in port alarm_control : DataPort { in :>> type : Isolette_Data_Model::On_Off; }

    // ==== OUTPUTS from operator interface to thermostat 
    out port lower_desired_tempWstatus : DataPort { out :>> type : Isolette_Data_Model::TempWstatus_i; }
    out port upper_desired_tempWstatus : DataPort { out :>> type : Isolette_Data_Model::TempWstatus_i; }
    out port lower_alarm_tempWstatus : DataPort { out :>> type : Isolette_Data_Model::TempWstatus_i; }
    out port upper_alarm_tempWstatus : DataPort { out :>> type : Isolette_Data_Model::TempWstatus_i; }

    language "GUMBO" /*{
        
      functions
        // example of a subclause function calling a library function
        def Allowed_UpperAlarmTempWStatus(upper: Isolette_Data_Model::TempWstatus_i): Base_Types::Boolean :=
          GUMBO_Library::GUMBO__Library::Allowed_UpperAlarmTempWStatus(upper);
          
      integration
        
        // the following guarantee integration constraints should imply the 
        // receiver's (manage monitor interface) assume integration constraints
        
        guarantee Allowed_LowerAlarmTempWstatus	
          "Table_A_12_LowerAlarmTemp: Range [96..101]:
          |http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=112 " :
          GUMBO_Library::GUMBO__Library::Allowed_LowerAlarmTempWStatus(lower_alarm_tempWstatus);
          
        guarantee Allowed_UpperAlarmTempWstatus	
          "Table_A_12_UpperAlarmTemp: Range [97..102]
          |http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=112 " :
          // example of calling a subclause function from an integraiton constraint
          Allowed_UpperAlarmTempWStatus(upper_alarm_tempWstatus);
      
      compute
        guarantee Allowed_AlarmTempWStatus_Ranges
          "An integration constraint can only refer to a single port, so need a general requires
          |clause to relate the lower and upper temps":
          GUMBO_Library::GUMBO__Library::Allowed_AlarmTempWStatus_Ranges(lower_alarm_tempWstatus, upper_alarm_tempWstatus);
    }*/
  }
}
