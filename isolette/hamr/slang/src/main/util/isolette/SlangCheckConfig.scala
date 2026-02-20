// #Sireum

package isolette

import org.sireum._
import org.sireum.Random.Gen64

/*
GENERATED FROM

ValueStatus.scala

Regulator_Mode.scala

Status.scala

On_Off.scala

Monitor_Mode.scala

Heat.scala

Temp_i.scala

PhysicalTemp_i.scala

TempWstatus_i.scala

Failure_Flag_i.scala

Base_Types.scala

GUMBO__Library.scala

Manage_Regulator_Interface_i_thermostat_rt_mri_mri_Containers.scala

Manage_Heat_Source_i_thermostat_rt_mhs_mhs_Containers.scala

Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_Containers.scala

Detect_Regulator_Failure_i_thermostat_rt_drf_drf_Containers.scala

Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_Containers.scala

Manage_Alarm_i_thermostat_mt_ma_ma_Containers.scala

Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_Containers.scala

Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_Containers.scala

Operator_Interface_Thread_i_operator_interface_oip_oit_Containers.scala

Temperature_Sensor_i_temperature_sensor_cpi_thermostat_Containers.scala

Heat_Source_i_heat_source_cpi_heat_controller_Containers.scala

ObservationKind.scala

Container.scala

DataContent.scala

Aux_Types.scala

*/
@datatype class Config_String(minSize: Z, maxSize: Z, attempts: Z, verbose: B, filter: String => B) {}
@datatype class Config_Z(low: Option[Z], high: Option[Z], attempts: Z, verbose: B, filter: Z => B) {}

@datatype class Config_B(attempts: Z, verbose: B, filter: B => B) {}

@datatype class Config_C(low: Option[C], high: Option[C], attempts: Z, verbose: B, filter: C => B) {}

@datatype class Config_R(low: Option[R], high: Option[R], attempts: Z, verbose: B, filter: R => B) {}

@datatype class Config_F32(low: Option[F32], high: Option[F32], attempts: Z, verbose: B, filter: F32 => B) {}

@datatype class Config_F64(low: Option[F64], high: Option[F64], attempts: Z, verbose: B, filter: F64 => B) {}

@datatype class Config_S8(low: Option[S8], high: Option[S8], attempts: Z, verbose: B, filter: S8 => B) {}

@datatype class Config_S16(low: Option[S16], high: Option[S16], attempts: Z, verbose: B, filter: S16 => B) {}

@datatype class Config_S32(low: Option[S32], high: Option[S32], attempts: Z, verbose: B, filter: S32 => B) {}

@datatype class Config_S64(low: Option[S64], high: Option[S64], attempts: Z, verbose: B, filter: S64 => B) {}

@datatype class Config_U8(low: Option[U8], high: Option[U8], attempts: Z, verbose: B, filter: U8 => B) {}

@datatype class Config_U16(low: Option[U16], high: Option[U16], attempts: Z, verbose: B, filter: U16 => B) {}

@datatype class Config_U32(low: Option[U32], high: Option[U32], attempts: Z, verbose: B, filter: U32 => B) {}

@datatype class Config_U64(low: Option[U64], high: Option[U64], attempts: Z, verbose: B, filter: U64 => B) {}

@datatype class Config__artDataContent(attempts: Z, verbose: B, additiveTypeFiltering: B, typeFilter: ISZ[_artDataContent_DataTypeId.Type], filter: art.DataContent => B) {}

@datatype class Config__artEmpty(attempts: Z, verbose: B, filter: art.Empty => B) {}

@datatype class Config_Base_TypesBoolean_Payload(attempts: Z, verbose: B, filter: Base_Types.Boolean_Payload => B) {}

@datatype class Config_Base_TypesInteger_Payload(attempts: Z, verbose: B, filter: Base_Types.Integer_Payload => B) {}

@datatype class Config_Base_TypesInteger_8_Payload(attempts: Z, verbose: B, filter: Base_Types.Integer_8_Payload => B) {}

@datatype class Config_Base_TypesInteger_16_Payload(attempts: Z, verbose: B, filter: Base_Types.Integer_16_Payload => B) {}

@datatype class Config_Base_TypesInteger_32_Payload(attempts: Z, verbose: B, filter: Base_Types.Integer_32_Payload => B) {}

@datatype class Config_Base_TypesInteger_64_Payload(attempts: Z, verbose: B, filter: Base_Types.Integer_64_Payload => B) {}

@datatype class Config_Base_TypesUnsigned_8_Payload(attempts: Z, verbose: B, filter: Base_Types.Unsigned_8_Payload => B) {}

@datatype class Config_Base_TypesUnsigned_16_Payload(attempts: Z, verbose: B, filter: Base_Types.Unsigned_16_Payload => B) {}

@datatype class Config_Base_TypesUnsigned_32_Payload(attempts: Z, verbose: B, filter: Base_Types.Unsigned_32_Payload => B) {}

@datatype class Config_Base_TypesUnsigned_64_Payload(attempts: Z, verbose: B, filter: Base_Types.Unsigned_64_Payload => B) {}

@datatype class Config_Base_TypesFloat_Payload(attempts: Z, verbose: B, filter: Base_Types.Float_Payload => B) {}

@datatype class Config_Base_TypesFloat_32_Payload(attempts: Z, verbose: B, filter: Base_Types.Float_32_Payload => B) {}

@datatype class Config_Base_TypesFloat_64_Payload(attempts: Z, verbose: B, filter: Base_Types.Float_64_Payload => B) {}

@datatype class Config_Base_TypesCharacter_Payload(attempts: Z, verbose: B, filter: Base_Types.Character_Payload => B) {}

@datatype class Config_Base_TypesString_Payload(attempts: Z, verbose: B, filter: Base_Types.String_Payload => B) {}

@datatype class Config_ISZB(minSize: Z, maxSize: Z, attempts: Z, verbose: B, filter: ISZ[B] => B) {}

@datatype class Config_Base_TypesBits_Payload(attempts: Z, verbose: B, filter: Base_Types.Bits_Payload => B) {}

@datatype class Config_DevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container(attempts: Z, verbose: B, additiveTypeFiltering: B, typeFilter: ISZ[DevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_DataTypeId.Type], filter: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container => B) {}

@datatype class Config_DevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_P(attempts: Z, verbose: B, filter: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container_P => B) {}

@datatype class Config_DevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PS(attempts: Z, verbose: B, filter: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PS => B) {}

@datatype class Config_DevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container(attempts: Z, verbose: B, additiveTypeFiltering: B, typeFilter: ISZ[DevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_DataTypeId.Type], filter: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container => B) {}

@datatype class Config_DevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_P(attempts: Z, verbose: B, filter: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container_P => B) {}

@datatype class Config_DevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PS(attempts: Z, verbose: B, filter: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PS => B) {}

@datatype class Config_DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container(attempts: Z, verbose: B, additiveTypeFiltering: B, typeFilter: ISZ[DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_DataTypeId.Type], filter: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container => B) {}

@datatype class Config_DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_P(attempts: Z, verbose: B, filter: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_P => B) {}

@datatype class Config_DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PS(attempts: Z, verbose: B, filter: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PS => B) {}

@datatype class Config_DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container(attempts: Z, verbose: B, additiveTypeFiltering: B, typeFilter: ISZ[DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_DataTypeId.Type], filter: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container => B) {}

@datatype class Config_DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_P(attempts: Z, verbose: B, filter: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_P => B) {}

@datatype class Config_DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PS(attempts: Z, verbose: B, filter: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PS => B) {}

@datatype class Config_Isolette_Data_ModelFailure_Flag_i(attempts: Z, verbose: B, filter: Isolette_Data_Model.Failure_Flag_i => B) {}

@datatype class Config_Isolette_Data_ModelFailure_Flag_i_Payload(attempts: Z, verbose: B, filter: Isolette_Data_Model.Failure_Flag_i_Payload => B) {}

@datatype class Config_Isolette_Data_ModelMonitor_ModeType(attempts: Z, verbose: B, filter: Isolette_Data_Model.Monitor_Mode.Type => B) {}

@datatype class Config_Isolette_Data_ModelMonitor_Mode_Payload(attempts: Z, verbose: B, filter: Isolette_Data_Model.Monitor_Mode_Payload => B) {}

@datatype class Config_Isolette_Data_ModelOn_OffType(attempts: Z, verbose: B, filter: Isolette_Data_Model.On_Off.Type => B) {}

@datatype class Config_Isolette_Data_ModelOn_Off_Payload(attempts: Z, verbose: B, filter: Isolette_Data_Model.On_Off_Payload => B) {}

@datatype class Config_Isolette_Data_ModelPhysicalTemp_i(attempts: Z, verbose: B, filter: Isolette_Data_Model.PhysicalTemp_i => B) {}

@datatype class Config_Isolette_Data_ModelPhysicalTemp_i_Payload(attempts: Z, verbose: B, filter: Isolette_Data_Model.PhysicalTemp_i_Payload => B) {}

@datatype class Config_Isolette_Data_ModelRegulator_ModeType(attempts: Z, verbose: B, filter: Isolette_Data_Model.Regulator_Mode.Type => B) {}

@datatype class Config_Isolette_Data_ModelRegulator_Mode_Payload(attempts: Z, verbose: B, filter: Isolette_Data_Model.Regulator_Mode_Payload => B) {}

@datatype class Config_Isolette_Data_ModelStatusType(attempts: Z, verbose: B, filter: Isolette_Data_Model.Status.Type => B) {}

@datatype class Config_Isolette_Data_ModelStatus_Payload(attempts: Z, verbose: B, filter: Isolette_Data_Model.Status_Payload => B) {}

@datatype class Config_Isolette_Data_ModelTempWstatus_i(attempts: Z, verbose: B, filter: Isolette_Data_Model.TempWstatus_i => B) {}

@datatype class Config_Isolette_Data_ModelTempWstatus_i_Payload(attempts: Z, verbose: B, filter: Isolette_Data_Model.TempWstatus_i_Payload => B) {}

@datatype class Config_Isolette_Data_ModelTemp_i(attempts: Z, verbose: B, filter: Isolette_Data_Model.Temp_i => B) {}

@datatype class Config_Isolette_Data_ModelTemp_i_Payload(attempts: Z, verbose: B, filter: Isolette_Data_Model.Temp_i_Payload => B) {}

@datatype class Config_Isolette_Data_ModelValueStatusType(attempts: Z, verbose: B, filter: Isolette_Data_Model.ValueStatus.Type => B) {}

@datatype class Config_Isolette_Data_ModelValueStatus_Payload(attempts: Z, verbose: B, filter: Isolette_Data_Model.ValueStatus_Payload => B) {}

@datatype class Config_Isolette_EnvironmentHeatType(attempts: Z, verbose: B, filter: Isolette_Environment.Heat.Type => B) {}

@datatype class Config_Isolette_EnvironmentHeat_Payload(attempts: Z, verbose: B, filter: Isolette_Environment.Heat_Payload => B) {}

@datatype class Config_MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container(attempts: Z, verbose: B, additiveTypeFiltering: B, typeFilter: ISZ[MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_DataTypeId.Type], filter: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container => B) {}

@datatype class Config_MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_P(attempts: Z, verbose: B, filter: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_P => B) {}

@datatype class Config_MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PS(attempts: Z, verbose: B, filter: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PS => B) {}

@datatype class Config_MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container(attempts: Z, verbose: B, additiveTypeFiltering: B, typeFilter: ISZ[MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_DataTypeId.Type], filter: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container => B) {}

@datatype class Config_MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_P(attempts: Z, verbose: B, filter: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_P => B) {}

@datatype class Config_MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PS(attempts: Z, verbose: B, filter: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PS => B) {}

@datatype class Config_MonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container(attempts: Z, verbose: B, additiveTypeFiltering: B, typeFilter: ISZ[MonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_DataTypeId.Type], filter: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container => B) {}

@datatype class Config_MonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P(attempts: Z, verbose: B, filter: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P => B) {}

@datatype class Config_MonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS(attempts: Z, verbose: B, filter: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS => B) {}

@datatype class Config_MonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container(attempts: Z, verbose: B, additiveTypeFiltering: B, typeFilter: ISZ[MonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_DataTypeId.Type], filter: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container => B) {}

@datatype class Config_MonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P(attempts: Z, verbose: B, filter: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P => B) {}

@datatype class Config_MonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS(attempts: Z, verbose: B, filter: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS => B) {}

@datatype class Config_MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container(attempts: Z, verbose: B, additiveTypeFiltering: B, typeFilter: ISZ[MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_DataTypeId.Type], filter: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container => B) {}

@datatype class Config_MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P(attempts: Z, verbose: B, filter: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P => B) {}

@datatype class Config_MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS(attempts: Z, verbose: B, filter: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS => B) {}

@datatype class Config_MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container(attempts: Z, verbose: B, additiveTypeFiltering: B, typeFilter: ISZ[MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_DataTypeId.Type], filter: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container => B) {}

@datatype class Config_MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P(attempts: Z, verbose: B, filter: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P => B) {}

@datatype class Config_MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS(attempts: Z, verbose: B, filter: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS => B) {}

@datatype class Config_MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container(attempts: Z, verbose: B, additiveTypeFiltering: B, typeFilter: ISZ[MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_DataTypeId.Type], filter: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container => B) {}

@datatype class Config_MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P(attempts: Z, verbose: B, filter: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P => B) {}

@datatype class Config_MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS(attempts: Z, verbose: B, filter: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS => B) {}

@datatype class Config_MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container(attempts: Z, verbose: B, additiveTypeFiltering: B, typeFilter: ISZ[MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_DataTypeId.Type], filter: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container => B) {}

@datatype class Config_MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P(attempts: Z, verbose: B, filter: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P => B) {}

@datatype class Config_MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS(attempts: Z, verbose: B, filter: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS => B) {}

@datatype class Config_Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container(attempts: Z, verbose: B, additiveTypeFiltering: B, typeFilter: ISZ[Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_DataTypeId.Type], filter: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container => B) {}

@datatype class Config_Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P(attempts: Z, verbose: B, filter: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P => B) {}

@datatype class Config_Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS(attempts: Z, verbose: B, filter: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS => B) {}

@datatype class Config_Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container(attempts: Z, verbose: B, additiveTypeFiltering: B, typeFilter: ISZ[Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_DataTypeId.Type], filter: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container => B) {}

@datatype class Config_Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P(attempts: Z, verbose: B, filter: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P => B) {}

@datatype class Config_Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS(attempts: Z, verbose: B, filter: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS => B) {}

@datatype class Config_RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container(attempts: Z, verbose: B, additiveTypeFiltering: B, typeFilter: ISZ[RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_DataTypeId.Type], filter: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container => B) {}

@datatype class Config_RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P(attempts: Z, verbose: B, filter: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P => B) {}

@datatype class Config_RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS(attempts: Z, verbose: B, filter: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS => B) {}

@datatype class Config_RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container(attempts: Z, verbose: B, additiveTypeFiltering: B, typeFilter: ISZ[RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_DataTypeId.Type], filter: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container => B) {}

@datatype class Config_RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P(attempts: Z, verbose: B, filter: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P => B) {}

@datatype class Config_RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS(attempts: Z, verbose: B, filter: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS => B) {}

@datatype class Config_RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container(attempts: Z, verbose: B, additiveTypeFiltering: B, typeFilter: ISZ[RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_DataTypeId.Type], filter: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container => B) {}

@datatype class Config_RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P(attempts: Z, verbose: B, filter: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P => B) {}

@datatype class Config_RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS(attempts: Z, verbose: B, filter: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS => B) {}

@datatype class Config_RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container(attempts: Z, verbose: B, additiveTypeFiltering: B, typeFilter: ISZ[RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_DataTypeId.Type], filter: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container => B) {}

@datatype class Config_RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P(attempts: Z, verbose: B, filter: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P => B) {}

@datatype class Config_RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS(attempts: Z, verbose: B, filter: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS => B) {}

@datatype class Config_RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container(attempts: Z, verbose: B, additiveTypeFiltering: B, typeFilter: ISZ[RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_DataTypeId.Type], filter: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container => B) {}

@datatype class Config_RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P(attempts: Z, verbose: B, filter: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P => B) {}

@datatype class Config_RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS(attempts: Z, verbose: B, filter: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS => B) {}

@datatype class Config_RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container(attempts: Z, verbose: B, additiveTypeFiltering: B, typeFilter: ISZ[RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_DataTypeId.Type], filter: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container => B) {}

@datatype class Config_RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P(attempts: Z, verbose: B, filter: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P => B) {}

@datatype class Config_RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS(attempts: Z, verbose: B, filter: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS => B) {}

@datatype class Config_RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container(attempts: Z, verbose: B, additiveTypeFiltering: B, typeFilter: ISZ[RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_DataTypeId.Type], filter: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container => B) {}

@datatype class Config_RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P(attempts: Z, verbose: B, filter: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P => B) {}

@datatype class Config_RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS(attempts: Z, verbose: B, filter: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS => B) {}

@datatype class Config_RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container(attempts: Z, verbose: B, additiveTypeFiltering: B, typeFilter: ISZ[RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_DataTypeId.Type], filter: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container => B) {}

@datatype class Config_RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P(attempts: Z, verbose: B, filter: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P => B) {}

@datatype class Config_RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS(attempts: Z, verbose: B, filter: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS => B) {}

@datatype class Config_utilContainer(attempts: Z, verbose: B, additiveTypeFiltering: B, typeFilter: ISZ[utilContainer_DataTypeId.Type], filter: util.Container => B) {}

@datatype class Config_utilEmptyContainer(attempts: Z, verbose: B, filter: util.EmptyContainer => B) {}

@datatype class Config_runtimemonitorObservationKindType(attempts: Z, verbose: B, filter: runtimemonitor.ObservationKind.Type => B) {}


