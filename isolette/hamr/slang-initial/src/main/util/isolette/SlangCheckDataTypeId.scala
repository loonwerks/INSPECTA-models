// #Sireum

package isolette

import org.sireum._
import org.sireum.Random.Gen64

/*
GENERATED FROM

PhysicalTemp_i.scala

TempWstatus_i.scala

Temp_i.scala

On_Off.scala

Failure_Flag_i.scala

ValueStatus.scala

Status.scala

Regulator_Mode.scala

Monitor_Mode.scala

Heat.scala

Interface_Interaction.scala

Air_Interaction.scala

Base_Types.scala

Manage_Regulator_Interface_i_thermostat_rt_mri_mri_Containers.scala

Manage_Heat_Source_i_thermostat_rt_mhs_mhs_Containers.scala

Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_Containers.scala

Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_Containers.scala

Manage_Alarm_i_thermostat_mt_ma_ma_Containers.scala

Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_Containers.scala

Operator_Interface_Thread_i_operator_interface_oip_oit_Containers.scala

Container.scala

DataContent.scala

Aux_Types.scala

*/

@enum object _artDataContent_DataTypeId {
   "_artEmpty_Id"
   "Base_TypesBits_Payload_Id"
   "Base_TypesBoolean_Payload_Id"
   "Base_TypesCharacter_Payload_Id"
   "Base_TypesFloat_32_Payload_Id"
   "Base_TypesFloat_64_Payload_Id"
   "Base_TypesFloat_Payload_Id"
   "Base_TypesInteger_16_Payload_Id"
   "Base_TypesInteger_32_Payload_Id"
   "Base_TypesInteger_64_Payload_Id"
   "Base_TypesInteger_8_Payload_Id"
   "Base_TypesInteger_Payload_Id"
   "Base_TypesString_Payload_Id"
   "Base_TypesUnsigned_16_Payload_Id"
   "Base_TypesUnsigned_32_Payload_Id"
   "Base_TypesUnsigned_64_Payload_Id"
   "Base_TypesUnsigned_8_Payload_Id"
   "Isolette_Data_ModelFailure_Flag_i_Payload_Id"
   "Isolette_Data_ModelMonitor_Mode_Payload_Id"
   "Isolette_Data_ModelOn_Off_Payload_Id"
   "Isolette_Data_ModelPhysicalTemp_i_Payload_Id"
   "Isolette_Data_ModelRegulator_Mode_Payload_Id"
   "Isolette_Data_ModelStatus_Payload_Id"
   "Isolette_Data_ModelTempWstatus_i_Payload_Id"
   "Isolette_Data_ModelTemp_i_Payload_Id"
   "Isolette_Data_ModelValueStatus_Payload_Id"
   "Isolette_EnvironmentAir_Interaction_Payload_Id"
   "Isolette_EnvironmentHeat_Payload_Id"
   "Isolette_EnvironmentInterface_Interaction_Payload_Id"
   "MonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P_Id"
   "MonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS_Id"
   "MonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P_Id"
   "MonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS_Id"
   "MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P_Id"
   "MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS_Id"
   "MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P_Id"
   "MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS_Id"
   "MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P_Id"
   "MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS_Id"
   "MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P_Id"
   "MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS_Id"
   "Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P_Id"
   "Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS_Id"
   "Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P_Id"
   "Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS_Id"
   "RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P_Id"
   "RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS_Id"
   "RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P_Id"
   "RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS_Id"
   "RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P_Id"
   "RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS_Id"
   "RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P_Id"
   "RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS_Id"
   "RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P_Id"
   "RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS_Id"
   "RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P_Id"
   "RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS_Id"
   "utilEmptyContainer_Id"
}

@enum object MonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_DataTypeId {
   "MonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P_Id"
   "MonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS_Id"
}

@enum object MonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_DataTypeId {
   "MonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P_Id"
   "MonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS_Id"
}

@enum object MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_DataTypeId {
   "MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P_Id"
   "MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS_Id"
}

@enum object MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_DataTypeId {
   "MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P_Id"
   "MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS_Id"
}

@enum object MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_DataTypeId {
   "MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P_Id"
   "MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS_Id"
}

@enum object MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_DataTypeId {
   "MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P_Id"
   "MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS_Id"
}

@enum object Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_DataTypeId {
   "Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P_Id"
   "Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS_Id"
}

@enum object Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_DataTypeId {
   "Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P_Id"
   "Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS_Id"
}

@enum object RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_DataTypeId {
   "RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P_Id"
   "RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS_Id"
}

@enum object RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_DataTypeId {
   "RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P_Id"
   "RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS_Id"
}

@enum object RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_DataTypeId {
   "RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P_Id"
   "RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS_Id"
}

@enum object RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_DataTypeId {
   "RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P_Id"
   "RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS_Id"
}

@enum object RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_DataTypeId {
   "RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P_Id"
   "RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS_Id"
}

@enum object RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_DataTypeId {
   "RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P_Id"
   "RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS_Id"
}

@enum object utilContainer_DataTypeId {
   "MonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P_Id"
   "MonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS_Id"
   "MonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P_Id"
   "MonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS_Id"
   "MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P_Id"
   "MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS_Id"
   "MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P_Id"
   "MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS_Id"
   "MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P_Id"
   "MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS_Id"
   "MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P_Id"
   "MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS_Id"
   "Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P_Id"
   "Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS_Id"
   "Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P_Id"
   "Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS_Id"
   "RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P_Id"
   "RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS_Id"
   "RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P_Id"
   "RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS_Id"
   "RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P_Id"
   "RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS_Id"
   "RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P_Id"
   "RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS_Id"
   "RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P_Id"
   "RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS_Id"
   "RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P_Id"
   "RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS_Id"
   "utilEmptyContainer_Id"
}

