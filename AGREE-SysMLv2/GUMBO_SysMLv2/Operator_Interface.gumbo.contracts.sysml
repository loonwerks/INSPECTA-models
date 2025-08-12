package GUMBO_Contracts {
  private import Isolette_Data_Model::*;
  private import Base_Types::*;

  package Operator_Interface_Thread_i_Contracts {
    private import Isolette_Data_Model::*;
    private import Base_Types::*;
  
    requirement def IntegrationContract {
      doc /*ID Table_A_12_LowerAlarmTemp Range [96..101] http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=112*/
      // typed literals: 96 [s32], 101 [s32]
      require constraint { ((96 <= lower_alarm_tempWstatus.degrees) and
        (lower_alarm_tempWstatus.degrees <= 101)) }
  
      doc /*ID Table_A_12_UpperAlarmTemp Range [97..102] http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=112*/
      // typed literals: 97 [s32], 102 [s32]
      require constraint { ((97 <= upper_alarm_tempWstatus.degrees) and
        (upper_alarm_tempWstatus.degrees <= 102)) }
  
    }
  
  }

}