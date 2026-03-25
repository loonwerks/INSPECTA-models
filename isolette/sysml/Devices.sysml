// Devices.sysml

package Devices {
  
  private import Isolette_Data_Model::*;
  private import HAMR::*;
      
  //-------------------------------------------------------------------------
  // temperature_sensor
  //-------------------------------------------------------------------------
  part def Temperature_Sensor_System_i :> System {
    
    part cpi : Temperature_Sensor_Process_i;

    port air : DataPort { in :>> type : Isolette_Data_Model::PhysicalTemp_i; }
    port current_tempWstatus : DataPort { out :>> type : Isolette_Data_Model::TempWstatus_i; }
        
    connection c1 : PortConnection
      connect air to cpi.air;
    connection c2 : PortConnection
      connect cpi.current_tempWstatus to current_tempWstatus;
  }
  
  part def Temperature_Sensor_Process_i :> Process {
    
    part thermostat : Temperature_Sensor_i;

    port air : DataPort { in :>> type : Isolette_Data_Model::PhysicalTemp_i; }
    port current_tempWstatus : DataPort { out :>> type : Isolette_Data_Model::TempWstatus_i; }
        
    //Connections
    connection c1 : PortConnection
      connect air to thermostat.air;
    connection c2 : PortConnection
      connect thermostat.current_tempWstatus to current_tempWstatus;

    attribute :>> Domain = Isolette_Properties::Domain_TS;
  }
  
  part def Temperature_Sensor_i :> Abstract_Definitions::Periodic_Thread {

    port air : DataPort { in :>> type : Isolette_Data_Model::PhysicalTemp_i; }
    port current_tempWstatus : DataPort { out :>> type : Isolette_Data_Model::TempWstatus_i; }
  }
  
  //-------------------------------------------------------------------------
  // heat_source
  //-------------------------------------------------------------------------
  part def Heat_Source_System_i :> System {
    
    part cpi : Heat_Source_Process_i;

    port heat_control : DataPort { in :>> type : Isolette_Data_Model::On_Off; }
    port heat_out : DataPort { out :>> type : Isolette_Environment::Heat; }
        
    connection c3 : PortConnection
      connect heat_control to cpi.heat_control;
    connection c4 : PortConnection
      connect cpi.heat_out to heat_out;
  }
  
  part def Heat_Source_Process_i :> Process {
    
    part heat_controller : Heat_Source_i;

    port heat_control : DataPort { in :>> type : Isolette_Data_Model::On_Off; }
    port heat_out : DataPort { out :>> type : Isolette_Environment::Heat; }
        
    connection c3 : PortConnection
      connect heat_control to heat_controller.heat_control;
    connection c4 : PortConnection
      connect heat_controller.heat_out to heat_out;

    attribute :>> Domain = Isolette_Properties::Domain_HS;
  }
  
  part def Heat_Source_i :>  Abstract_Definitions::Periodic_Thread {
    
    port heat_control : DataPort { in :>> type : Isolette_Data_Model::On_Off; }
    port heat_out : DataPort { out :>> type : Isolette_Environment::Heat; }
  }
}
