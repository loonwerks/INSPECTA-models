// #Sireum
// @formatter:off

// This file is auto-generated from PhysicalTemp_i.scala, TempWstatus_i.scala, Temp_i.scala, On_Off.scala, Failure_Flag_i.scala, ValueStatus.scala, Status.scala, Regulator_Mode.scala, Monitor_Mode.scala, Heat.scala, Interface_Interaction.scala, Air_Interaction.scala, Base_Types.scala, Manage_Regulator_Interface_i_thermostat_rt_mri_mri_Containers.scala, Manage_Heat_Source_i_thermostat_rt_mhs_mhs_Containers.scala, Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_Containers.scala, Detect_Regulator_Failure_i_thermostat_rt_drf_drf_Containers.scala, Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_Containers.scala, Manage_Alarm_i_thermostat_mt_ma_ma_Containers.scala, Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_Containers.scala, Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_Containers.scala, Operator_Interface_Thread_i_operator_interface_oip_oit_Containers.scala, Temperature_Sensor_i_temperature_sensor_cpi_thermostat_Containers.scala, Heat_Source_i_heat_source_cpi_heat_controller_Containers.scala, ObservationKind.scala, Container.scala, DataContent.scala, Aux_Types.scala

package isolette

import org.sireum._

object MsgPack {

  object Constants {

    val Isolette_Data_ModelPhysicalTemp_i: Z = -32

    val Isolette_Data_ModelPhysicalTemp_i_Payload: Z = -31

    val Isolette_Data_ModelTempWstatus_i: Z = -30

    val Isolette_Data_ModelTempWstatus_i_Payload: Z = -29

    val Isolette_Data_ModelTemp_i: Z = -28

    val Isolette_Data_ModelTemp_i_Payload: Z = -27

    val Isolette_Data_ModelOn_Off_Payload: Z = -26

    val Isolette_Data_ModelFailure_Flag_i: Z = -25

    val Isolette_Data_ModelFailure_Flag_i_Payload: Z = -24

    val Isolette_Data_ModelValueStatus_Payload: Z = -23

    val Isolette_Data_ModelStatus_Payload: Z = -22

    val Isolette_Data_ModelRegulator_Mode_Payload: Z = -21

    val Isolette_Data_ModelMonitor_Mode_Payload: Z = -20

    val Isolette_EnvironmentHeat_Payload: Z = -19

    val Isolette_EnvironmentInterface_Interaction_Payload: Z = -18

    val Isolette_EnvironmentAir_Interaction_Payload: Z = -17

    val Base_TypesBoolean_Payload: Z = -16

    val Base_TypesInteger_Payload: Z = -15

    val Base_TypesInteger_8_Payload: Z = -14

    val Base_TypesInteger_16_Payload: Z = -13

    val Base_TypesInteger_32_Payload: Z = -12

    val Base_TypesInteger_64_Payload: Z = -11

    val Base_TypesUnsigned_8_Payload: Z = -10

    val Base_TypesUnsigned_16_Payload: Z = -9

    val Base_TypesUnsigned_32_Payload: Z = -8

    val Base_TypesUnsigned_64_Payload: Z = -7

    val Base_TypesFloat_Payload: Z = -6

    val Base_TypesFloat_32_Payload: Z = -5

    val Base_TypesFloat_64_Payload: Z = -4

    val Base_TypesCharacter_Payload: Z = -3

    val Base_TypesString_Payload: Z = -2

    val Base_TypesBits_Payload: Z = -1

    val RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P: Z = 0

    val RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS: Z = 1

    val RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P: Z = 2

    val RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS: Z = 3

    val RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P: Z = 4

    val RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS: Z = 5

    val RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P: Z = 6

    val RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS: Z = 7

    val RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P: Z = 8

    val RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS: Z = 9

    val RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P: Z = 10

    val RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS: Z = 11

    val RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P: Z = 12

    val RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS: Z = 13

    val RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P: Z = 14

    val RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS: Z = 15

    val MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P: Z = 16

    val MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS: Z = 17

    val MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P: Z = 18

    val MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS: Z = 19

    val MonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P: Z = 20

    val MonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS: Z = 21

    val MonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P: Z = 22

    val MonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS: Z = 23

    val MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P: Z = 24

    val MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS: Z = 25

    val MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P: Z = 26

    val MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS: Z = 27

    val MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_P: Z = 28

    val MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PS: Z = 29

    val MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_P: Z = 30

    val MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PS: Z = 31

    val Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P: Z = 32

    val Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS: Z = 33

    val Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P: Z = 34

    val Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS: Z = 35

    val DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_P: Z = 36

    val DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PS: Z = 37

    val DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_P: Z = 38

    val DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PS: Z = 39

    val DevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_P: Z = 40

    val DevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PS: Z = 41

    val DevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_P: Z = 42

    val DevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PS: Z = 43

    val utilEmptyContainer: Z = 44

    val _artEmpty: Z = 45

  }

  object Writer {

    @record class Default(val writer: MessagePack.Writer.Impl) extends Writer

  }

  @msig trait Writer {

    def writer: MessagePack.Writer

    def writeIsolette_Data_ModelPhysicalTemp_i(o: Isolette_Data_Model.PhysicalTemp_i): Unit = {
      writer.writeZ(Constants.Isolette_Data_ModelPhysicalTemp_i)
      writer.writeF32(o.degrees)
    }

    def writeIsolette_Data_ModelPhysicalTemp_i_Payload(o: Isolette_Data_Model.PhysicalTemp_i_Payload): Unit = {
      writer.writeZ(Constants.Isolette_Data_ModelPhysicalTemp_i_Payload)
      writeIsolette_Data_ModelPhysicalTemp_i(o.value)
    }

    def writeIsolette_Data_ModelTempWstatus_i(o: Isolette_Data_Model.TempWstatus_i): Unit = {
      writer.writeZ(Constants.Isolette_Data_ModelTempWstatus_i)
      writer.writeF32(o.degrees)
      writeIsolette_Data_ModelValueStatusType(o.status)
    }

    def writeIsolette_Data_ModelTempWstatus_i_Payload(o: Isolette_Data_Model.TempWstatus_i_Payload): Unit = {
      writer.writeZ(Constants.Isolette_Data_ModelTempWstatus_i_Payload)
      writeIsolette_Data_ModelTempWstatus_i(o.value)
    }

    def writeIsolette_Data_ModelTemp_i(o: Isolette_Data_Model.Temp_i): Unit = {
      writer.writeZ(Constants.Isolette_Data_ModelTemp_i)
      writer.writeF32(o.degrees)
    }

    def writeIsolette_Data_ModelTemp_i_Payload(o: Isolette_Data_Model.Temp_i_Payload): Unit = {
      writer.writeZ(Constants.Isolette_Data_ModelTemp_i_Payload)
      writeIsolette_Data_ModelTemp_i(o.value)
    }

    def writeIsolette_Data_ModelOn_OffType(o: Isolette_Data_Model.On_Off.Type): Unit = {
      writer.writeZ(o.ordinal)
    }

    def writeIsolette_Data_ModelOn_Off_Payload(o: Isolette_Data_Model.On_Off_Payload): Unit = {
      writer.writeZ(Constants.Isolette_Data_ModelOn_Off_Payload)
      writeIsolette_Data_ModelOn_OffType(o.value)
    }

    def writeIsolette_Data_ModelFailure_Flag_i(o: Isolette_Data_Model.Failure_Flag_i): Unit = {
      writer.writeZ(Constants.Isolette_Data_ModelFailure_Flag_i)
      writer.writeB(o.flag)
    }

    def writeIsolette_Data_ModelFailure_Flag_i_Payload(o: Isolette_Data_Model.Failure_Flag_i_Payload): Unit = {
      writer.writeZ(Constants.Isolette_Data_ModelFailure_Flag_i_Payload)
      writeIsolette_Data_ModelFailure_Flag_i(o.value)
    }

    def writeIsolette_Data_ModelValueStatusType(o: Isolette_Data_Model.ValueStatus.Type): Unit = {
      writer.writeZ(o.ordinal)
    }

    def writeIsolette_Data_ModelValueStatus_Payload(o: Isolette_Data_Model.ValueStatus_Payload): Unit = {
      writer.writeZ(Constants.Isolette_Data_ModelValueStatus_Payload)
      writeIsolette_Data_ModelValueStatusType(o.value)
    }

    def writeIsolette_Data_ModelStatusType(o: Isolette_Data_Model.Status.Type): Unit = {
      writer.writeZ(o.ordinal)
    }

    def writeIsolette_Data_ModelStatus_Payload(o: Isolette_Data_Model.Status_Payload): Unit = {
      writer.writeZ(Constants.Isolette_Data_ModelStatus_Payload)
      writeIsolette_Data_ModelStatusType(o.value)
    }

    def writeIsolette_Data_ModelRegulator_ModeType(o: Isolette_Data_Model.Regulator_Mode.Type): Unit = {
      writer.writeZ(o.ordinal)
    }

    def writeIsolette_Data_ModelRegulator_Mode_Payload(o: Isolette_Data_Model.Regulator_Mode_Payload): Unit = {
      writer.writeZ(Constants.Isolette_Data_ModelRegulator_Mode_Payload)
      writeIsolette_Data_ModelRegulator_ModeType(o.value)
    }

    def writeIsolette_Data_ModelMonitor_ModeType(o: Isolette_Data_Model.Monitor_Mode.Type): Unit = {
      writer.writeZ(o.ordinal)
    }

    def writeIsolette_Data_ModelMonitor_Mode_Payload(o: Isolette_Data_Model.Monitor_Mode_Payload): Unit = {
      writer.writeZ(Constants.Isolette_Data_ModelMonitor_Mode_Payload)
      writeIsolette_Data_ModelMonitor_ModeType(o.value)
    }

    def writeIsolette_EnvironmentHeatType(o: Isolette_Environment.Heat.Type): Unit = {
      writer.writeZ(o.ordinal)
    }

    def writeIsolette_EnvironmentHeat_Payload(o: Isolette_Environment.Heat_Payload): Unit = {
      writer.writeZ(Constants.Isolette_EnvironmentHeat_Payload)
      writeIsolette_EnvironmentHeatType(o.value)
    }

    def writeIsolette_EnvironmentInterface_InteractionType(o: Isolette_Environment.Interface_Interaction.Type): Unit = {
      writer.writeZ(o.ordinal)
    }

    def writeIsolette_EnvironmentInterface_Interaction_Payload(o: Isolette_Environment.Interface_Interaction_Payload): Unit = {
      writer.writeZ(Constants.Isolette_EnvironmentInterface_Interaction_Payload)
      writeIsolette_EnvironmentInterface_InteractionType(o.value)
    }

    def writeIsolette_EnvironmentAir_InteractionType(o: Isolette_Environment.Air_Interaction.Type): Unit = {
      writer.writeZ(o.ordinal)
    }

    def writeIsolette_EnvironmentAir_Interaction_Payload(o: Isolette_Environment.Air_Interaction_Payload): Unit = {
      writer.writeZ(Constants.Isolette_EnvironmentAir_Interaction_Payload)
      writeIsolette_EnvironmentAir_InteractionType(o.value)
    }

    def writeBase_TypesBoolean_Payload(o: Base_Types.Boolean_Payload): Unit = {
      writer.writeZ(Constants.Base_TypesBoolean_Payload)
      writer.writeB(o.value)
    }

    def writeBase_TypesInteger_Payload(o: Base_Types.Integer_Payload): Unit = {
      writer.writeZ(Constants.Base_TypesInteger_Payload)
      writer.writeZ(o.value)
    }

    def writeBase_TypesInteger_8_Payload(o: Base_Types.Integer_8_Payload): Unit = {
      writer.writeZ(Constants.Base_TypesInteger_8_Payload)
      writer.writeS8(o.value)
    }

    def writeBase_TypesInteger_16_Payload(o: Base_Types.Integer_16_Payload): Unit = {
      writer.writeZ(Constants.Base_TypesInteger_16_Payload)
      writer.writeS16(o.value)
    }

    def writeBase_TypesInteger_32_Payload(o: Base_Types.Integer_32_Payload): Unit = {
      writer.writeZ(Constants.Base_TypesInteger_32_Payload)
      writer.writeS32(o.value)
    }

    def writeBase_TypesInteger_64_Payload(o: Base_Types.Integer_64_Payload): Unit = {
      writer.writeZ(Constants.Base_TypesInteger_64_Payload)
      writer.writeS64(o.value)
    }

    def writeBase_TypesUnsigned_8_Payload(o: Base_Types.Unsigned_8_Payload): Unit = {
      writer.writeZ(Constants.Base_TypesUnsigned_8_Payload)
      writer.writeU8(o.value)
    }

    def writeBase_TypesUnsigned_16_Payload(o: Base_Types.Unsigned_16_Payload): Unit = {
      writer.writeZ(Constants.Base_TypesUnsigned_16_Payload)
      writer.writeU16(o.value)
    }

    def writeBase_TypesUnsigned_32_Payload(o: Base_Types.Unsigned_32_Payload): Unit = {
      writer.writeZ(Constants.Base_TypesUnsigned_32_Payload)
      writer.writeU32(o.value)
    }

    def writeBase_TypesUnsigned_64_Payload(o: Base_Types.Unsigned_64_Payload): Unit = {
      writer.writeZ(Constants.Base_TypesUnsigned_64_Payload)
      writer.writeU64(o.value)
    }

    def writeBase_TypesFloat_Payload(o: Base_Types.Float_Payload): Unit = {
      writer.writeZ(Constants.Base_TypesFloat_Payload)
      writer.writeR(o.value)
    }

    def writeBase_TypesFloat_32_Payload(o: Base_Types.Float_32_Payload): Unit = {
      writer.writeZ(Constants.Base_TypesFloat_32_Payload)
      writer.writeF32(o.value)
    }

    def writeBase_TypesFloat_64_Payload(o: Base_Types.Float_64_Payload): Unit = {
      writer.writeZ(Constants.Base_TypesFloat_64_Payload)
      writer.writeF64(o.value)
    }

    def writeBase_TypesCharacter_Payload(o: Base_Types.Character_Payload): Unit = {
      writer.writeZ(Constants.Base_TypesCharacter_Payload)
      writer.writeC(o.value)
    }

    def writeBase_TypesString_Payload(o: Base_Types.String_Payload): Unit = {
      writer.writeZ(Constants.Base_TypesString_Payload)
      writer.writeString(o.value)
    }

    def writeBase_TypesBits_Payload(o: Base_Types.Bits_Payload): Unit = {
      writer.writeZ(Constants.Base_TypesBits_Payload)
      writer.writeISZ(o.value, writer.writeB _)
    }

    def writeRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container(o: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container): Unit = {
      o match {
        case o: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P => writeRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P(o)
        case o: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS => writeRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS(o)
      }
    }

    def writeRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P(o: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P): Unit = {
      writer.writeZ(Constants.RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P)
      writeIsolette_Data_ModelTempWstatus_i(o.api_current_tempWstatus)
      writeIsolette_Data_ModelTempWstatus_i(o.api_lower_desired_tempWstatus)
      writeIsolette_Data_ModelRegulator_ModeType(o.api_regulator_mode)
      writeIsolette_Data_ModelTempWstatus_i(o.api_upper_desired_tempWstatus)
    }

    def writeRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS(o: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS): Unit = {
      writer.writeZ(Constants.RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS)
      writeIsolette_Data_ModelTempWstatus_i(o.api_current_tempWstatus)
      writeIsolette_Data_ModelTempWstatus_i(o.api_lower_desired_tempWstatus)
      writeIsolette_Data_ModelRegulator_ModeType(o.api_regulator_mode)
      writeIsolette_Data_ModelTempWstatus_i(o.api_upper_desired_tempWstatus)
    }

    def writeRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container(o: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container): Unit = {
      o match {
        case o: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P => writeRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P(o)
        case o: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS => writeRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS(o)
      }
    }

    def writeRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P(o: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P): Unit = {
      writer.writeZ(Constants.RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P)
      writeIsolette_Data_ModelTemp_i(o.api_displayed_temp)
      writeIsolette_Data_ModelFailure_Flag_i(o.api_interface_failure)
      writeIsolette_Data_ModelTemp_i(o.api_lower_desired_temp)
      writeIsolette_Data_ModelStatusType(o.api_regulator_status)
      writeIsolette_Data_ModelTemp_i(o.api_upper_desired_temp)
    }

    def writeRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS(o: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS): Unit = {
      writer.writeZ(Constants.RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS)
      writeIsolette_Data_ModelTemp_i(o.api_displayed_temp)
      writeIsolette_Data_ModelFailure_Flag_i(o.api_interface_failure)
      writeIsolette_Data_ModelTemp_i(o.api_lower_desired_temp)
      writeIsolette_Data_ModelStatusType(o.api_regulator_status)
      writeIsolette_Data_ModelTemp_i(o.api_upper_desired_temp)
    }

    def writeRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container(o: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container): Unit = {
      o match {
        case o: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P => writeRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P(o)
        case o: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS => writeRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS(o)
      }
    }

    def writeRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P(o: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P): Unit = {
      writer.writeZ(Constants.RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P)
      writeIsolette_Data_ModelTempWstatus_i(o.api_current_tempWstatus)
      writeIsolette_Data_ModelTemp_i(o.api_lower_desired_temp)
      writeIsolette_Data_ModelRegulator_ModeType(o.api_regulator_mode)
      writeIsolette_Data_ModelTemp_i(o.api_upper_desired_temp)
    }

    def writeRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS(o: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS): Unit = {
      writer.writeZ(Constants.RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS)
      writeIsolette_Data_ModelOn_OffType(o.In_lastCmd)
      writeIsolette_Data_ModelTempWstatus_i(o.api_current_tempWstatus)
      writeIsolette_Data_ModelTemp_i(o.api_lower_desired_temp)
      writeIsolette_Data_ModelRegulator_ModeType(o.api_regulator_mode)
      writeIsolette_Data_ModelTemp_i(o.api_upper_desired_temp)
    }

    def writeRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container(o: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container): Unit = {
      o match {
        case o: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P => writeRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P(o)
        case o: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS => writeRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS(o)
      }
    }

    def writeRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P(o: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P): Unit = {
      writer.writeZ(Constants.RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P)
      writeIsolette_Data_ModelOn_OffType(o.api_heat_control)
    }

    def writeRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS(o: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS): Unit = {
      writer.writeZ(Constants.RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS)
      writeIsolette_Data_ModelOn_OffType(o.lastCmd)
      writeIsolette_Data_ModelOn_OffType(o.api_heat_control)
    }

    def writeRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container(o: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container): Unit = {
      o match {
        case o: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P => writeRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P(o)
        case o: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS => writeRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS(o)
      }
    }

    def writeRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P(o: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P): Unit = {
      writer.writeZ(Constants.RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P)
      writeIsolette_Data_ModelTempWstatus_i(o.api_current_tempWstatus)
      writeIsolette_Data_ModelFailure_Flag_i(o.api_interface_failure)
      writeIsolette_Data_ModelFailure_Flag_i(o.api_internal_failure)
    }

    def writeRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS(o: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS): Unit = {
      writer.writeZ(Constants.RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS)
      writeIsolette_Data_ModelRegulator_ModeType(o.In_lastRegulatorMode)
      writeIsolette_Data_ModelTempWstatus_i(o.api_current_tempWstatus)
      writeIsolette_Data_ModelFailure_Flag_i(o.api_interface_failure)
      writeIsolette_Data_ModelFailure_Flag_i(o.api_internal_failure)
    }

    def writeRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container(o: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container): Unit = {
      o match {
        case o: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P => writeRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P(o)
        case o: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS => writeRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS(o)
      }
    }

    def writeRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P(o: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P): Unit = {
      writer.writeZ(Constants.RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P)
      writeIsolette_Data_ModelRegulator_ModeType(o.api_regulator_mode)
    }

    def writeRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS(o: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS): Unit = {
      writer.writeZ(Constants.RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS)
      writeIsolette_Data_ModelRegulator_ModeType(o.lastRegulatorMode)
      writeIsolette_Data_ModelRegulator_ModeType(o.api_regulator_mode)
    }

    def writeRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container(o: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container): Unit = {
      o match {
        case o: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P => writeRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P(o)
        case o: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS => writeRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS(o)
      }
    }

    def writeRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P(o: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P): Unit = {
      writer.writeZ(Constants.RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P)
    }

    def writeRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS(o: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS): Unit = {
      writer.writeZ(Constants.RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS)
    }

    def writeRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container(o: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container): Unit = {
      o match {
        case o: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P => writeRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P(o)
        case o: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS => writeRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS(o)
      }
    }

    def writeRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P(o: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P): Unit = {
      writer.writeZ(Constants.RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P)
      writeIsolette_Data_ModelFailure_Flag_i(o.api_internal_failure)
    }

    def writeRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS(o: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS): Unit = {
      writer.writeZ(Constants.RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS)
      writeIsolette_Data_ModelFailure_Flag_i(o.api_internal_failure)
    }

    def writeMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container(o: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container): Unit = {
      o match {
        case o: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P => writeMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P(o)
        case o: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS => writeMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS(o)
      }
    }

    def writeMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P(o: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P): Unit = {
      writer.writeZ(Constants.MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P)
      writeIsolette_Data_ModelTempWstatus_i(o.api_current_tempWstatus)
      writeIsolette_Data_ModelTempWstatus_i(o.api_lower_alarm_tempWstatus)
      writeIsolette_Data_ModelMonitor_ModeType(o.api_monitor_mode)
      writeIsolette_Data_ModelTempWstatus_i(o.api_upper_alarm_tempWstatus)
    }

    def writeMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS(o: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS): Unit = {
      writer.writeZ(Constants.MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS)
      writeIsolette_Data_ModelOn_OffType(o.In_lastCmd)
      writeIsolette_Data_ModelTempWstatus_i(o.api_current_tempWstatus)
      writeIsolette_Data_ModelTempWstatus_i(o.api_lower_alarm_tempWstatus)
      writeIsolette_Data_ModelMonitor_ModeType(o.api_monitor_mode)
      writeIsolette_Data_ModelTempWstatus_i(o.api_upper_alarm_tempWstatus)
    }

    def writeMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container(o: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container): Unit = {
      o match {
        case o: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P => writeMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P(o)
        case o: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS => writeMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS(o)
      }
    }

    def writeMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P(o: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P): Unit = {
      writer.writeZ(Constants.MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P)
      writeIsolette_Data_ModelFailure_Flag_i(o.api_interface_failure)
      writeIsolette_Data_ModelTemp_i(o.api_lower_alarm_temp)
      writeIsolette_Data_ModelStatusType(o.api_monitor_status)
      writeIsolette_Data_ModelTemp_i(o.api_upper_alarm_temp)
    }

    def writeMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS(o: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS): Unit = {
      writer.writeZ(Constants.MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS)
      writeIsolette_Data_ModelOn_OffType(o.lastCmd)
      writeIsolette_Data_ModelFailure_Flag_i(o.api_interface_failure)
      writeIsolette_Data_ModelTemp_i(o.api_lower_alarm_temp)
      writeIsolette_Data_ModelStatusType(o.api_monitor_status)
      writeIsolette_Data_ModelTemp_i(o.api_upper_alarm_temp)
    }

    def writeMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container(o: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container): Unit = {
      o match {
        case o: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P => writeMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P(o)
        case o: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS => writeMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS(o)
      }
    }

    def writeMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P(o: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P): Unit = {
      writer.writeZ(Constants.MonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P)
      writeIsolette_Data_ModelTempWstatus_i(o.api_current_tempWstatus)
      writeIsolette_Data_ModelTemp_i(o.api_lower_alarm_temp)
      writeIsolette_Data_ModelMonitor_ModeType(o.api_monitor_mode)
      writeIsolette_Data_ModelTemp_i(o.api_upper_alarm_temp)
    }

    def writeMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS(o: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS): Unit = {
      writer.writeZ(Constants.MonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS)
      writeIsolette_Data_ModelOn_OffType(o.In_lastCmd)
      writeIsolette_Data_ModelTempWstatus_i(o.api_current_tempWstatus)
      writeIsolette_Data_ModelTemp_i(o.api_lower_alarm_temp)
      writeIsolette_Data_ModelMonitor_ModeType(o.api_monitor_mode)
      writeIsolette_Data_ModelTemp_i(o.api_upper_alarm_temp)
    }

    def writeMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container(o: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container): Unit = {
      o match {
        case o: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P => writeMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P(o)
        case o: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS => writeMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS(o)
      }
    }

    def writeMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P(o: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P): Unit = {
      writer.writeZ(Constants.MonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P)
      writeIsolette_Data_ModelOn_OffType(o.api_alarm_control)
    }

    def writeMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS(o: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS): Unit = {
      writer.writeZ(Constants.MonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS)
      writeIsolette_Data_ModelOn_OffType(o.lastCmd)
      writeIsolette_Data_ModelOn_OffType(o.api_alarm_control)
    }

    def writeMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container(o: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container): Unit = {
      o match {
        case o: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P => writeMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P(o)
        case o: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS => writeMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS(o)
      }
    }

    def writeMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P(o: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P): Unit = {
      writer.writeZ(Constants.MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P)
      writeIsolette_Data_ModelTempWstatus_i(o.api_current_tempWstatus)
      writeIsolette_Data_ModelFailure_Flag_i(o.api_interface_failure)
      writeIsolette_Data_ModelFailure_Flag_i(o.api_internal_failure)
    }

    def writeMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS(o: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS): Unit = {
      writer.writeZ(Constants.MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS)
      writeIsolette_Data_ModelMonitor_ModeType(o.In_lastMonitorMode)
      writeIsolette_Data_ModelTempWstatus_i(o.api_current_tempWstatus)
      writeIsolette_Data_ModelFailure_Flag_i(o.api_interface_failure)
      writeIsolette_Data_ModelFailure_Flag_i(o.api_internal_failure)
    }

    def writeMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container(o: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container): Unit = {
      o match {
        case o: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P => writeMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P(o)
        case o: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS => writeMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS(o)
      }
    }

    def writeMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P(o: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P): Unit = {
      writer.writeZ(Constants.MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P)
      writeIsolette_Data_ModelMonitor_ModeType(o.api_monitor_mode)
    }

    def writeMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS(o: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS): Unit = {
      writer.writeZ(Constants.MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS)
      writeIsolette_Data_ModelMonitor_ModeType(o.lastMonitorMode)
      writeIsolette_Data_ModelMonitor_ModeType(o.api_monitor_mode)
    }

    def writeMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container(o: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container): Unit = {
      o match {
        case o: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_P => writeMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_P(o)
        case o: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PS => writeMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PS(o)
      }
    }

    def writeMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_P(o: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_P): Unit = {
      writer.writeZ(Constants.MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_P)
    }

    def writeMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PS(o: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PS): Unit = {
      writer.writeZ(Constants.MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PS)
    }

    def writeMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container(o: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container): Unit = {
      o match {
        case o: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_P => writeMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_P(o)
        case o: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PS => writeMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PS(o)
      }
    }

    def writeMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_P(o: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_P): Unit = {
      writer.writeZ(Constants.MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_P)
      writeIsolette_Data_ModelFailure_Flag_i(o.api_internal_failure)
    }

    def writeMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PS(o: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PS): Unit = {
      writer.writeZ(Constants.MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PS)
      writeIsolette_Data_ModelFailure_Flag_i(o.api_internal_failure)
    }

    def writeOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container(o: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container): Unit = {
      o match {
        case o: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P => writeOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P(o)
        case o: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS => writeOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS(o)
      }
    }

    def writeOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P(o: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P): Unit = {
      writer.writeZ(Constants.Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P)
      writeIsolette_Data_ModelOn_OffType(o.api_alarm_control)
      writeIsolette_Data_ModelTemp_i(o.api_display_temperature)
      writeIsolette_Data_ModelStatusType(o.api_monitor_status)
      writeIsolette_Data_ModelStatusType(o.api_regulator_status)
    }

    def writeOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS(o: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS): Unit = {
      writer.writeZ(Constants.Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS)
      writeIsolette_Data_ModelOn_OffType(o.api_alarm_control)
      writeIsolette_Data_ModelTemp_i(o.api_display_temperature)
      writeIsolette_Data_ModelStatusType(o.api_monitor_status)
      writeIsolette_Data_ModelStatusType(o.api_regulator_status)
    }

    def writeOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container(o: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container): Unit = {
      o match {
        case o: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P => writeOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P(o)
        case o: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS => writeOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS(o)
      }
    }

    def writeOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P(o: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P): Unit = {
      writer.writeZ(Constants.Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P)
      writeIsolette_Data_ModelTempWstatus_i(o.api_lower_alarm_tempWstatus)
      writeIsolette_Data_ModelTempWstatus_i(o.api_lower_desired_tempWstatus)
      writeIsolette_Data_ModelTempWstatus_i(o.api_upper_alarm_tempWstatus)
      writeIsolette_Data_ModelTempWstatus_i(o.api_upper_desired_tempWstatus)
    }

    def writeOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS(o: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS): Unit = {
      writer.writeZ(Constants.Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS)
      writeIsolette_Data_ModelTempWstatus_i(o.api_lower_alarm_tempWstatus)
      writeIsolette_Data_ModelTempWstatus_i(o.api_lower_desired_tempWstatus)
      writeIsolette_Data_ModelTempWstatus_i(o.api_upper_alarm_tempWstatus)
      writeIsolette_Data_ModelTempWstatus_i(o.api_upper_desired_tempWstatus)
    }

    def writeDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container(o: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container): Unit = {
      o match {
        case o: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_P => writeDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_P(o)
        case o: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PS => writeDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PS(o)
      }
    }

    def writeDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_P(o: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_P): Unit = {
      writer.writeZ(Constants.DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_P)
      writeIsolette_Data_ModelPhysicalTemp_i(o.api_air)
    }

    def writeDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PS(o: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PS): Unit = {
      writer.writeZ(Constants.DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PS)
      writeIsolette_Data_ModelPhysicalTemp_i(o.api_air)
    }

    def writeDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container(o: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container): Unit = {
      o match {
        case o: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_P => writeDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_P(o)
        case o: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PS => writeDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PS(o)
      }
    }

    def writeDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_P(o: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_P): Unit = {
      writer.writeZ(Constants.DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_P)
      writeIsolette_Data_ModelTempWstatus_i(o.api_current_tempWstatus)
    }

    def writeDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PS(o: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PS): Unit = {
      writer.writeZ(Constants.DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PS)
      writeIsolette_Data_ModelTempWstatus_i(o.api_current_tempWstatus)
    }

    def writeDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container(o: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container): Unit = {
      o match {
        case o: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container_P => writeDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_P(o)
        case o: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PS => writeDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PS(o)
      }
    }

    def writeDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_P(o: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container_P): Unit = {
      writer.writeZ(Constants.DevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_P)
      writeIsolette_Data_ModelOn_OffType(o.api_heat_control)
    }

    def writeDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PS(o: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PS): Unit = {
      writer.writeZ(Constants.DevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PS)
      writeIsolette_Data_ModelOn_OffType(o.api_heat_control)
    }

    def writeDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container(o: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container): Unit = {
      o match {
        case o: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container_P => writeDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_P(o)
        case o: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PS => writeDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PS(o)
      }
    }

    def writeDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_P(o: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container_P): Unit = {
      writer.writeZ(Constants.DevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_P)
      writeIsolette_EnvironmentHeatType(o.api_heat_out)
    }

    def writeDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PS(o: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PS): Unit = {
      writer.writeZ(Constants.DevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PS)
      writeIsolette_EnvironmentHeatType(o.api_heat_out)
    }

    def writeruntimemonitorObservationKindType(o: runtimemonitor.ObservationKind.Type): Unit = {
      writer.writeZ(o.ordinal)
    }

    def writeutilContainer(o: util.Container): Unit = {
      o match {
        case o: util.EmptyContainer => writeutilEmptyContainer(o)
        case o: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P => writeMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P(o)
        case o: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS => writeMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS(o)
        case o: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P => writeMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P(o)
        case o: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS => writeMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS(o)
        case o: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P => writeMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P(o)
        case o: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P => writeRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P(o)
        case o: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS => writeMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS(o)
        case o: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS => writeRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS(o)
        case o: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P => writeMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P(o)
        case o: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P => writeRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P(o)
        case o: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS => writeMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS(o)
        case o: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS => writeRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS(o)
        case o: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container_P => writeDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_P(o)
        case o: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PS => writeDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PS(o)
        case o: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container_P => writeDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_P(o)
        case o: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PS => writeDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PS(o)
        case o: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_P => writeMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_P(o)
        case o: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P => writeRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P(o)
        case o: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PS => writeMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PS(o)
        case o: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_P => writeMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_P(o)
        case o: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS => writeRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS(o)
        case o: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PS => writeMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PS(o)
        case o: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P => writeRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P(o)
        case o: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS => writeRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS(o)
        case o: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P => writeMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P(o)
        case o: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS => writeMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS(o)
        case o: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P => writeMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P(o)
        case o: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS => writeMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS(o)
        case o: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P => writeRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P(o)
        case o: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS => writeRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS(o)
        case o: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P => writeRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P(o)
        case o: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS => writeRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS(o)
        case o: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P => writeRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P(o)
        case o: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS => writeRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS(o)
        case o: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P => writeRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P(o)
        case o: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS => writeRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS(o)
        case o: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_P => writeDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_P(o)
        case o: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PS => writeDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PS(o)
        case o: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_P => writeDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_P(o)
        case o: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PS => writeDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PS(o)
        case o: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P => writeOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P(o)
        case o: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS => writeOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS(o)
        case o: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P => writeOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P(o)
        case o: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS => writeOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS(o)
      }
    }

    def writeutilEmptyContainer(o: util.EmptyContainer): Unit = {
      writer.writeZ(Constants.utilEmptyContainer)
    }

    def write_artDataContent(o: art.DataContent): Unit = {
      o match {
        case o: art.Empty => write_artEmpty(o)
        case o: Base_Types.Boolean_Payload => writeBase_TypesBoolean_Payload(o)
        case o: Base_Types.Integer_Payload => writeBase_TypesInteger_Payload(o)
        case o: Base_Types.Integer_8_Payload => writeBase_TypesInteger_8_Payload(o)
        case o: Base_Types.Integer_16_Payload => writeBase_TypesInteger_16_Payload(o)
        case o: Base_Types.Integer_32_Payload => writeBase_TypesInteger_32_Payload(o)
        case o: Base_Types.Integer_64_Payload => writeBase_TypesInteger_64_Payload(o)
        case o: Base_Types.Unsigned_8_Payload => writeBase_TypesUnsigned_8_Payload(o)
        case o: Base_Types.Unsigned_16_Payload => writeBase_TypesUnsigned_16_Payload(o)
        case o: Base_Types.Unsigned_32_Payload => writeBase_TypesUnsigned_32_Payload(o)
        case o: Base_Types.Unsigned_64_Payload => writeBase_TypesUnsigned_64_Payload(o)
        case o: Base_Types.Float_Payload => writeBase_TypesFloat_Payload(o)
        case o: Base_Types.Float_32_Payload => writeBase_TypesFloat_32_Payload(o)
        case o: Base_Types.Float_64_Payload => writeBase_TypesFloat_64_Payload(o)
        case o: Base_Types.Character_Payload => writeBase_TypesCharacter_Payload(o)
        case o: Base_Types.String_Payload => writeBase_TypesString_Payload(o)
        case o: Base_Types.Bits_Payload => writeBase_TypesBits_Payload(o)
        case o: util.EmptyContainer => writeutilEmptyContainer(o)
        case o: Isolette_Environment.Heat_Payload => writeIsolette_EnvironmentHeat_Payload(o)
        case o: Isolette_Data_Model.On_Off_Payload => writeIsolette_Data_ModelOn_Off_Payload(o)
        case o: Isolette_Data_Model.Status_Payload => writeIsolette_Data_ModelStatus_Payload(o)
        case o: Isolette_Data_Model.Temp_i_Payload => writeIsolette_Data_ModelTemp_i_Payload(o)
        case o: Isolette_Data_Model.ValueStatus_Payload => writeIsolette_Data_ModelValueStatus_Payload(o)
        case o: Isolette_Data_Model.Monitor_Mode_Payload => writeIsolette_Data_ModelMonitor_Mode_Payload(o)
        case o: Isolette_Data_Model.TempWstatus_i_Payload => writeIsolette_Data_ModelTempWstatus_i_Payload(o)
        case o: Isolette_Data_Model.Regulator_Mode_Payload => writeIsolette_Data_ModelRegulator_Mode_Payload(o)
        case o: Isolette_Data_Model.Failure_Flag_i_Payload => writeIsolette_Data_ModelFailure_Flag_i_Payload(o)
        case o: Isolette_Data_Model.PhysicalTemp_i_Payload => writeIsolette_Data_ModelPhysicalTemp_i_Payload(o)
        case o: Isolette_Environment.Air_Interaction_Payload => writeIsolette_EnvironmentAir_Interaction_Payload(o)
        case o: Isolette_Environment.Interface_Interaction_Payload => writeIsolette_EnvironmentInterface_Interaction_Payload(o)
        case o: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P => writeMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P(o)
        case o: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS => writeMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS(o)
        case o: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P => writeMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P(o)
        case o: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS => writeMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS(o)
        case o: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P => writeMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P(o)
        case o: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P => writeRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P(o)
        case o: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS => writeMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS(o)
        case o: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS => writeRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS(o)
        case o: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P => writeMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P(o)
        case o: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P => writeRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P(o)
        case o: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS => writeMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS(o)
        case o: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS => writeRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS(o)
        case o: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container_P => writeDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_P(o)
        case o: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PS => writeDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PS(o)
        case o: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container_P => writeDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_P(o)
        case o: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PS => writeDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PS(o)
        case o: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_P => writeMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_P(o)
        case o: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P => writeRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P(o)
        case o: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PS => writeMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PS(o)
        case o: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_P => writeMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_P(o)
        case o: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS => writeRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS(o)
        case o: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PS => writeMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PS(o)
        case o: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P => writeRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P(o)
        case o: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS => writeRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS(o)
        case o: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P => writeMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P(o)
        case o: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS => writeMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS(o)
        case o: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P => writeMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P(o)
        case o: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS => writeMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS(o)
        case o: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P => writeRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P(o)
        case o: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS => writeRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS(o)
        case o: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P => writeRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P(o)
        case o: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS => writeRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS(o)
        case o: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P => writeRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P(o)
        case o: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS => writeRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS(o)
        case o: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P => writeRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P(o)
        case o: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS => writeRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS(o)
        case o: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_P => writeDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_P(o)
        case o: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PS => writeDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PS(o)
        case o: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_P => writeDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_P(o)
        case o: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PS => writeDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PS(o)
        case o: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P => writeOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P(o)
        case o: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS => writeOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS(o)
        case o: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P => writeOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P(o)
        case o: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS => writeOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS(o)
      }
    }

    def write_artEmpty(o: art.Empty): Unit = {
      writer.writeZ(Constants._artEmpty)
    }

    def result: ISZ[U8] = {
      return writer.result
    }

  }

  object Reader {

    @record class Default(val reader: MessagePack.Reader.Impl) extends Reader {
      def errorOpt: Option[MessagePack.ErrorMsg] = {
        return reader.errorOpt
      }
    }

  }

  @msig trait Reader {

    def reader: MessagePack.Reader

    def readIsolette_Data_ModelPhysicalTemp_i(): Isolette_Data_Model.PhysicalTemp_i = {
      val r = readIsolette_Data_ModelPhysicalTemp_iT(F)
      return r
    }

    def readIsolette_Data_ModelPhysicalTemp_iT(typeParsed: B): Isolette_Data_Model.PhysicalTemp_i = {
      if (!typeParsed) {
        reader.expectZ(Constants.Isolette_Data_ModelPhysicalTemp_i)
      }
      val degrees = reader.readF32()
      return Isolette_Data_Model.PhysicalTemp_i(degrees)
    }

    def readIsolette_Data_ModelPhysicalTemp_i_Payload(): Isolette_Data_Model.PhysicalTemp_i_Payload = {
      val r = readIsolette_Data_ModelPhysicalTemp_i_PayloadT(F)
      return r
    }

    def readIsolette_Data_ModelPhysicalTemp_i_PayloadT(typeParsed: B): Isolette_Data_Model.PhysicalTemp_i_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Isolette_Data_ModelPhysicalTemp_i_Payload)
      }
      val value = readIsolette_Data_ModelPhysicalTemp_i()
      return Isolette_Data_Model.PhysicalTemp_i_Payload(value)
    }

    def readIsolette_Data_ModelTempWstatus_i(): Isolette_Data_Model.TempWstatus_i = {
      val r = readIsolette_Data_ModelTempWstatus_iT(F)
      return r
    }

    def readIsolette_Data_ModelTempWstatus_iT(typeParsed: B): Isolette_Data_Model.TempWstatus_i = {
      if (!typeParsed) {
        reader.expectZ(Constants.Isolette_Data_ModelTempWstatus_i)
      }
      val degrees = reader.readF32()
      val status = readIsolette_Data_ModelValueStatusType()
      return Isolette_Data_Model.TempWstatus_i(degrees, status)
    }

    def readIsolette_Data_ModelTempWstatus_i_Payload(): Isolette_Data_Model.TempWstatus_i_Payload = {
      val r = readIsolette_Data_ModelTempWstatus_i_PayloadT(F)
      return r
    }

    def readIsolette_Data_ModelTempWstatus_i_PayloadT(typeParsed: B): Isolette_Data_Model.TempWstatus_i_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Isolette_Data_ModelTempWstatus_i_Payload)
      }
      val value = readIsolette_Data_ModelTempWstatus_i()
      return Isolette_Data_Model.TempWstatus_i_Payload(value)
    }

    def readIsolette_Data_ModelTemp_i(): Isolette_Data_Model.Temp_i = {
      val r = readIsolette_Data_ModelTemp_iT(F)
      return r
    }

    def readIsolette_Data_ModelTemp_iT(typeParsed: B): Isolette_Data_Model.Temp_i = {
      if (!typeParsed) {
        reader.expectZ(Constants.Isolette_Data_ModelTemp_i)
      }
      val degrees = reader.readF32()
      return Isolette_Data_Model.Temp_i(degrees)
    }

    def readIsolette_Data_ModelTemp_i_Payload(): Isolette_Data_Model.Temp_i_Payload = {
      val r = readIsolette_Data_ModelTemp_i_PayloadT(F)
      return r
    }

    def readIsolette_Data_ModelTemp_i_PayloadT(typeParsed: B): Isolette_Data_Model.Temp_i_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Isolette_Data_ModelTemp_i_Payload)
      }
      val value = readIsolette_Data_ModelTemp_i()
      return Isolette_Data_Model.Temp_i_Payload(value)
    }

    def readIsolette_Data_ModelOn_OffType(): Isolette_Data_Model.On_Off.Type = {
      val r = reader.readZ()
      return Isolette_Data_Model.On_Off.byOrdinal(r).get
    }

    def readIsolette_Data_ModelOn_Off_Payload(): Isolette_Data_Model.On_Off_Payload = {
      val r = readIsolette_Data_ModelOn_Off_PayloadT(F)
      return r
    }

    def readIsolette_Data_ModelOn_Off_PayloadT(typeParsed: B): Isolette_Data_Model.On_Off_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Isolette_Data_ModelOn_Off_Payload)
      }
      val value = readIsolette_Data_ModelOn_OffType()
      return Isolette_Data_Model.On_Off_Payload(value)
    }

    def readIsolette_Data_ModelFailure_Flag_i(): Isolette_Data_Model.Failure_Flag_i = {
      val r = readIsolette_Data_ModelFailure_Flag_iT(F)
      return r
    }

    def readIsolette_Data_ModelFailure_Flag_iT(typeParsed: B): Isolette_Data_Model.Failure_Flag_i = {
      if (!typeParsed) {
        reader.expectZ(Constants.Isolette_Data_ModelFailure_Flag_i)
      }
      val flag = reader.readB()
      return Isolette_Data_Model.Failure_Flag_i(flag)
    }

    def readIsolette_Data_ModelFailure_Flag_i_Payload(): Isolette_Data_Model.Failure_Flag_i_Payload = {
      val r = readIsolette_Data_ModelFailure_Flag_i_PayloadT(F)
      return r
    }

    def readIsolette_Data_ModelFailure_Flag_i_PayloadT(typeParsed: B): Isolette_Data_Model.Failure_Flag_i_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Isolette_Data_ModelFailure_Flag_i_Payload)
      }
      val value = readIsolette_Data_ModelFailure_Flag_i()
      return Isolette_Data_Model.Failure_Flag_i_Payload(value)
    }

    def readIsolette_Data_ModelValueStatusType(): Isolette_Data_Model.ValueStatus.Type = {
      val r = reader.readZ()
      return Isolette_Data_Model.ValueStatus.byOrdinal(r).get
    }

    def readIsolette_Data_ModelValueStatus_Payload(): Isolette_Data_Model.ValueStatus_Payload = {
      val r = readIsolette_Data_ModelValueStatus_PayloadT(F)
      return r
    }

    def readIsolette_Data_ModelValueStatus_PayloadT(typeParsed: B): Isolette_Data_Model.ValueStatus_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Isolette_Data_ModelValueStatus_Payload)
      }
      val value = readIsolette_Data_ModelValueStatusType()
      return Isolette_Data_Model.ValueStatus_Payload(value)
    }

    def readIsolette_Data_ModelStatusType(): Isolette_Data_Model.Status.Type = {
      val r = reader.readZ()
      return Isolette_Data_Model.Status.byOrdinal(r).get
    }

    def readIsolette_Data_ModelStatus_Payload(): Isolette_Data_Model.Status_Payload = {
      val r = readIsolette_Data_ModelStatus_PayloadT(F)
      return r
    }

    def readIsolette_Data_ModelStatus_PayloadT(typeParsed: B): Isolette_Data_Model.Status_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Isolette_Data_ModelStatus_Payload)
      }
      val value = readIsolette_Data_ModelStatusType()
      return Isolette_Data_Model.Status_Payload(value)
    }

    def readIsolette_Data_ModelRegulator_ModeType(): Isolette_Data_Model.Regulator_Mode.Type = {
      val r = reader.readZ()
      return Isolette_Data_Model.Regulator_Mode.byOrdinal(r).get
    }

    def readIsolette_Data_ModelRegulator_Mode_Payload(): Isolette_Data_Model.Regulator_Mode_Payload = {
      val r = readIsolette_Data_ModelRegulator_Mode_PayloadT(F)
      return r
    }

    def readIsolette_Data_ModelRegulator_Mode_PayloadT(typeParsed: B): Isolette_Data_Model.Regulator_Mode_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Isolette_Data_ModelRegulator_Mode_Payload)
      }
      val value = readIsolette_Data_ModelRegulator_ModeType()
      return Isolette_Data_Model.Regulator_Mode_Payload(value)
    }

    def readIsolette_Data_ModelMonitor_ModeType(): Isolette_Data_Model.Monitor_Mode.Type = {
      val r = reader.readZ()
      return Isolette_Data_Model.Monitor_Mode.byOrdinal(r).get
    }

    def readIsolette_Data_ModelMonitor_Mode_Payload(): Isolette_Data_Model.Monitor_Mode_Payload = {
      val r = readIsolette_Data_ModelMonitor_Mode_PayloadT(F)
      return r
    }

    def readIsolette_Data_ModelMonitor_Mode_PayloadT(typeParsed: B): Isolette_Data_Model.Monitor_Mode_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Isolette_Data_ModelMonitor_Mode_Payload)
      }
      val value = readIsolette_Data_ModelMonitor_ModeType()
      return Isolette_Data_Model.Monitor_Mode_Payload(value)
    }

    def readIsolette_EnvironmentHeatType(): Isolette_Environment.Heat.Type = {
      val r = reader.readZ()
      return Isolette_Environment.Heat.byOrdinal(r).get
    }

    def readIsolette_EnvironmentHeat_Payload(): Isolette_Environment.Heat_Payload = {
      val r = readIsolette_EnvironmentHeat_PayloadT(F)
      return r
    }

    def readIsolette_EnvironmentHeat_PayloadT(typeParsed: B): Isolette_Environment.Heat_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Isolette_EnvironmentHeat_Payload)
      }
      val value = readIsolette_EnvironmentHeatType()
      return Isolette_Environment.Heat_Payload(value)
    }

    def readIsolette_EnvironmentInterface_InteractionType(): Isolette_Environment.Interface_Interaction.Type = {
      val r = reader.readZ()
      return Isolette_Environment.Interface_Interaction.byOrdinal(r).get
    }

    def readIsolette_EnvironmentInterface_Interaction_Payload(): Isolette_Environment.Interface_Interaction_Payload = {
      val r = readIsolette_EnvironmentInterface_Interaction_PayloadT(F)
      return r
    }

    def readIsolette_EnvironmentInterface_Interaction_PayloadT(typeParsed: B): Isolette_Environment.Interface_Interaction_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Isolette_EnvironmentInterface_Interaction_Payload)
      }
      val value = readIsolette_EnvironmentInterface_InteractionType()
      return Isolette_Environment.Interface_Interaction_Payload(value)
    }

    def readIsolette_EnvironmentAir_InteractionType(): Isolette_Environment.Air_Interaction.Type = {
      val r = reader.readZ()
      return Isolette_Environment.Air_Interaction.byOrdinal(r).get
    }

    def readIsolette_EnvironmentAir_Interaction_Payload(): Isolette_Environment.Air_Interaction_Payload = {
      val r = readIsolette_EnvironmentAir_Interaction_PayloadT(F)
      return r
    }

    def readIsolette_EnvironmentAir_Interaction_PayloadT(typeParsed: B): Isolette_Environment.Air_Interaction_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Isolette_EnvironmentAir_Interaction_Payload)
      }
      val value = readIsolette_EnvironmentAir_InteractionType()
      return Isolette_Environment.Air_Interaction_Payload(value)
    }

    def readBase_TypesBoolean_Payload(): Base_Types.Boolean_Payload = {
      val r = readBase_TypesBoolean_PayloadT(F)
      return r
    }

    def readBase_TypesBoolean_PayloadT(typeParsed: B): Base_Types.Boolean_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Base_TypesBoolean_Payload)
      }
      val value = reader.readB()
      return Base_Types.Boolean_Payload(value)
    }

    def readBase_TypesInteger_Payload(): Base_Types.Integer_Payload = {
      val r = readBase_TypesInteger_PayloadT(F)
      return r
    }

    def readBase_TypesInteger_PayloadT(typeParsed: B): Base_Types.Integer_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Base_TypesInteger_Payload)
      }
      val value = reader.readZ()
      return Base_Types.Integer_Payload(value)
    }

    def readBase_TypesInteger_8_Payload(): Base_Types.Integer_8_Payload = {
      val r = readBase_TypesInteger_8_PayloadT(F)
      return r
    }

    def readBase_TypesInteger_8_PayloadT(typeParsed: B): Base_Types.Integer_8_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Base_TypesInteger_8_Payload)
      }
      val value = reader.readS8()
      return Base_Types.Integer_8_Payload(value)
    }

    def readBase_TypesInteger_16_Payload(): Base_Types.Integer_16_Payload = {
      val r = readBase_TypesInteger_16_PayloadT(F)
      return r
    }

    def readBase_TypesInteger_16_PayloadT(typeParsed: B): Base_Types.Integer_16_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Base_TypesInteger_16_Payload)
      }
      val value = reader.readS16()
      return Base_Types.Integer_16_Payload(value)
    }

    def readBase_TypesInteger_32_Payload(): Base_Types.Integer_32_Payload = {
      val r = readBase_TypesInteger_32_PayloadT(F)
      return r
    }

    def readBase_TypesInteger_32_PayloadT(typeParsed: B): Base_Types.Integer_32_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Base_TypesInteger_32_Payload)
      }
      val value = reader.readS32()
      return Base_Types.Integer_32_Payload(value)
    }

    def readBase_TypesInteger_64_Payload(): Base_Types.Integer_64_Payload = {
      val r = readBase_TypesInteger_64_PayloadT(F)
      return r
    }

    def readBase_TypesInteger_64_PayloadT(typeParsed: B): Base_Types.Integer_64_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Base_TypesInteger_64_Payload)
      }
      val value = reader.readS64()
      return Base_Types.Integer_64_Payload(value)
    }

    def readBase_TypesUnsigned_8_Payload(): Base_Types.Unsigned_8_Payload = {
      val r = readBase_TypesUnsigned_8_PayloadT(F)
      return r
    }

    def readBase_TypesUnsigned_8_PayloadT(typeParsed: B): Base_Types.Unsigned_8_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Base_TypesUnsigned_8_Payload)
      }
      val value = reader.readU8()
      return Base_Types.Unsigned_8_Payload(value)
    }

    def readBase_TypesUnsigned_16_Payload(): Base_Types.Unsigned_16_Payload = {
      val r = readBase_TypesUnsigned_16_PayloadT(F)
      return r
    }

    def readBase_TypesUnsigned_16_PayloadT(typeParsed: B): Base_Types.Unsigned_16_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Base_TypesUnsigned_16_Payload)
      }
      val value = reader.readU16()
      return Base_Types.Unsigned_16_Payload(value)
    }

    def readBase_TypesUnsigned_32_Payload(): Base_Types.Unsigned_32_Payload = {
      val r = readBase_TypesUnsigned_32_PayloadT(F)
      return r
    }

    def readBase_TypesUnsigned_32_PayloadT(typeParsed: B): Base_Types.Unsigned_32_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Base_TypesUnsigned_32_Payload)
      }
      val value = reader.readU32()
      return Base_Types.Unsigned_32_Payload(value)
    }

    def readBase_TypesUnsigned_64_Payload(): Base_Types.Unsigned_64_Payload = {
      val r = readBase_TypesUnsigned_64_PayloadT(F)
      return r
    }

    def readBase_TypesUnsigned_64_PayloadT(typeParsed: B): Base_Types.Unsigned_64_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Base_TypesUnsigned_64_Payload)
      }
      val value = reader.readU64()
      return Base_Types.Unsigned_64_Payload(value)
    }

    def readBase_TypesFloat_Payload(): Base_Types.Float_Payload = {
      val r = readBase_TypesFloat_PayloadT(F)
      return r
    }

    def readBase_TypesFloat_PayloadT(typeParsed: B): Base_Types.Float_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Base_TypesFloat_Payload)
      }
      val value = reader.readR()
      return Base_Types.Float_Payload(value)
    }

    def readBase_TypesFloat_32_Payload(): Base_Types.Float_32_Payload = {
      val r = readBase_TypesFloat_32_PayloadT(F)
      return r
    }

    def readBase_TypesFloat_32_PayloadT(typeParsed: B): Base_Types.Float_32_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Base_TypesFloat_32_Payload)
      }
      val value = reader.readF32()
      return Base_Types.Float_32_Payload(value)
    }

    def readBase_TypesFloat_64_Payload(): Base_Types.Float_64_Payload = {
      val r = readBase_TypesFloat_64_PayloadT(F)
      return r
    }

    def readBase_TypesFloat_64_PayloadT(typeParsed: B): Base_Types.Float_64_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Base_TypesFloat_64_Payload)
      }
      val value = reader.readF64()
      return Base_Types.Float_64_Payload(value)
    }

    def readBase_TypesCharacter_Payload(): Base_Types.Character_Payload = {
      val r = readBase_TypesCharacter_PayloadT(F)
      return r
    }

    def readBase_TypesCharacter_PayloadT(typeParsed: B): Base_Types.Character_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Base_TypesCharacter_Payload)
      }
      val value = reader.readC()
      return Base_Types.Character_Payload(value)
    }

    def readBase_TypesString_Payload(): Base_Types.String_Payload = {
      val r = readBase_TypesString_PayloadT(F)
      return r
    }

    def readBase_TypesString_PayloadT(typeParsed: B): Base_Types.String_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Base_TypesString_Payload)
      }
      val value = reader.readString()
      return Base_Types.String_Payload(value)
    }

    def readBase_TypesBits_Payload(): Base_Types.Bits_Payload = {
      val r = readBase_TypesBits_PayloadT(F)
      return r
    }

    def readBase_TypesBits_PayloadT(typeParsed: B): Base_Types.Bits_Payload = {
      if (!typeParsed) {
        reader.expectZ(Constants.Base_TypesBits_Payload)
      }
      val value = reader.readISZ(reader.readB _)
      return Base_Types.Bits_Payload(value)
    }

    def readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container(): Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P => val r = readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PT(T); return r
        case Constants.RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS => val r = readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container.")
          val r = readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PST(T)
          return r
      }
    }

    def readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P(): Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P = {
      val r = readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PT(F)
      return r
    }

    def readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PT(typeParsed: B): Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P = {
      if (!typeParsed) {
        reader.expectZ(Constants.RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P)
      }
      val api_current_tempWstatus = readIsolette_Data_ModelTempWstatus_i()
      val api_lower_desired_tempWstatus = readIsolette_Data_ModelTempWstatus_i()
      val api_regulator_mode = readIsolette_Data_ModelRegulator_ModeType()
      val api_upper_desired_tempWstatus = readIsolette_Data_ModelTempWstatus_i()
      return Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P(api_current_tempWstatus, api_lower_desired_tempWstatus, api_regulator_mode, api_upper_desired_tempWstatus)
    }

    def readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS(): Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS = {
      val r = readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PST(F)
      return r
    }

    def readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PST(typeParsed: B): Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS = {
      if (!typeParsed) {
        reader.expectZ(Constants.RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS)
      }
      val api_current_tempWstatus = readIsolette_Data_ModelTempWstatus_i()
      val api_lower_desired_tempWstatus = readIsolette_Data_ModelTempWstatus_i()
      val api_regulator_mode = readIsolette_Data_ModelRegulator_ModeType()
      val api_upper_desired_tempWstatus = readIsolette_Data_ModelTempWstatus_i()
      return Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS(api_current_tempWstatus, api_lower_desired_tempWstatus, api_regulator_mode, api_upper_desired_tempWstatus)
    }

    def readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container(): Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P => val r = readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PT(T); return r
        case Constants.RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS => val r = readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container.")
          val r = readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PST(T)
          return r
      }
    }

    def readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P(): Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P = {
      val r = readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PT(F)
      return r
    }

    def readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PT(typeParsed: B): Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P = {
      if (!typeParsed) {
        reader.expectZ(Constants.RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P)
      }
      val api_displayed_temp = readIsolette_Data_ModelTemp_i()
      val api_interface_failure = readIsolette_Data_ModelFailure_Flag_i()
      val api_lower_desired_temp = readIsolette_Data_ModelTemp_i()
      val api_regulator_status = readIsolette_Data_ModelStatusType()
      val api_upper_desired_temp = readIsolette_Data_ModelTemp_i()
      return Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P(api_displayed_temp, api_interface_failure, api_lower_desired_temp, api_regulator_status, api_upper_desired_temp)
    }

    def readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS(): Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS = {
      val r = readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PST(F)
      return r
    }

    def readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PST(typeParsed: B): Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS = {
      if (!typeParsed) {
        reader.expectZ(Constants.RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS)
      }
      val api_displayed_temp = readIsolette_Data_ModelTemp_i()
      val api_interface_failure = readIsolette_Data_ModelFailure_Flag_i()
      val api_lower_desired_temp = readIsolette_Data_ModelTemp_i()
      val api_regulator_status = readIsolette_Data_ModelStatusType()
      val api_upper_desired_temp = readIsolette_Data_ModelTemp_i()
      return Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS(api_displayed_temp, api_interface_failure, api_lower_desired_temp, api_regulator_status, api_upper_desired_temp)
    }

    def readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container(): Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P => val r = readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PT(T); return r
        case Constants.RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS => val r = readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container.")
          val r = readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PST(T)
          return r
      }
    }

    def readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P(): Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P = {
      val r = readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PT(F)
      return r
    }

    def readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PT(typeParsed: B): Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P = {
      if (!typeParsed) {
        reader.expectZ(Constants.RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P)
      }
      val api_current_tempWstatus = readIsolette_Data_ModelTempWstatus_i()
      val api_lower_desired_temp = readIsolette_Data_ModelTemp_i()
      val api_regulator_mode = readIsolette_Data_ModelRegulator_ModeType()
      val api_upper_desired_temp = readIsolette_Data_ModelTemp_i()
      return Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P(api_current_tempWstatus, api_lower_desired_temp, api_regulator_mode, api_upper_desired_temp)
    }

    def readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS(): Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS = {
      val r = readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PST(F)
      return r
    }

    def readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PST(typeParsed: B): Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS = {
      if (!typeParsed) {
        reader.expectZ(Constants.RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS)
      }
      val In_lastCmd = readIsolette_Data_ModelOn_OffType()
      val api_current_tempWstatus = readIsolette_Data_ModelTempWstatus_i()
      val api_lower_desired_temp = readIsolette_Data_ModelTemp_i()
      val api_regulator_mode = readIsolette_Data_ModelRegulator_ModeType()
      val api_upper_desired_temp = readIsolette_Data_ModelTemp_i()
      return Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS(In_lastCmd, api_current_tempWstatus, api_lower_desired_temp, api_regulator_mode, api_upper_desired_temp)
    }

    def readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container(): Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P => val r = readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PT(T); return r
        case Constants.RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS => val r = readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container.")
          val r = readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PST(T)
          return r
      }
    }

    def readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P(): Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P = {
      val r = readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PT(F)
      return r
    }

    def readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PT(typeParsed: B): Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P = {
      if (!typeParsed) {
        reader.expectZ(Constants.RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P)
      }
      val api_heat_control = readIsolette_Data_ModelOn_OffType()
      return Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P(api_heat_control)
    }

    def readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS(): Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS = {
      val r = readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PST(F)
      return r
    }

    def readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PST(typeParsed: B): Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS = {
      if (!typeParsed) {
        reader.expectZ(Constants.RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS)
      }
      val lastCmd = readIsolette_Data_ModelOn_OffType()
      val api_heat_control = readIsolette_Data_ModelOn_OffType()
      return Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS(lastCmd, api_heat_control)
    }

    def readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container(): Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P => val r = readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PT(T); return r
        case Constants.RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS => val r = readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container.")
          val r = readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PST(T)
          return r
      }
    }

    def readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P(): Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P = {
      val r = readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PT(F)
      return r
    }

    def readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PT(typeParsed: B): Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P = {
      if (!typeParsed) {
        reader.expectZ(Constants.RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P)
      }
      val api_current_tempWstatus = readIsolette_Data_ModelTempWstatus_i()
      val api_interface_failure = readIsolette_Data_ModelFailure_Flag_i()
      val api_internal_failure = readIsolette_Data_ModelFailure_Flag_i()
      return Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P(api_current_tempWstatus, api_interface_failure, api_internal_failure)
    }

    def readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS(): Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS = {
      val r = readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PST(F)
      return r
    }

    def readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PST(typeParsed: B): Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS = {
      if (!typeParsed) {
        reader.expectZ(Constants.RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS)
      }
      val In_lastRegulatorMode = readIsolette_Data_ModelRegulator_ModeType()
      val api_current_tempWstatus = readIsolette_Data_ModelTempWstatus_i()
      val api_interface_failure = readIsolette_Data_ModelFailure_Flag_i()
      val api_internal_failure = readIsolette_Data_ModelFailure_Flag_i()
      return Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS(In_lastRegulatorMode, api_current_tempWstatus, api_interface_failure, api_internal_failure)
    }

    def readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container(): Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P => val r = readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PT(T); return r
        case Constants.RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS => val r = readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container.")
          val r = readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PST(T)
          return r
      }
    }

    def readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P(): Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P = {
      val r = readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PT(F)
      return r
    }

    def readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PT(typeParsed: B): Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P = {
      if (!typeParsed) {
        reader.expectZ(Constants.RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P)
      }
      val api_regulator_mode = readIsolette_Data_ModelRegulator_ModeType()
      return Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P(api_regulator_mode)
    }

    def readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS(): Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS = {
      val r = readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PST(F)
      return r
    }

    def readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PST(typeParsed: B): Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS = {
      if (!typeParsed) {
        reader.expectZ(Constants.RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS)
      }
      val lastRegulatorMode = readIsolette_Data_ModelRegulator_ModeType()
      val api_regulator_mode = readIsolette_Data_ModelRegulator_ModeType()
      return Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS(lastRegulatorMode, api_regulator_mode)
    }

    def readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container(): Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P => val r = readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PT(T); return r
        case Constants.RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS => val r = readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container.")
          val r = readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PST(T)
          return r
      }
    }

    def readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P(): Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P = {
      val r = readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PT(F)
      return r
    }

    def readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PT(typeParsed: B): Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P = {
      if (!typeParsed) {
        reader.expectZ(Constants.RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P)
      }
      return Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P()
    }

    def readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS(): Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS = {
      val r = readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PST(F)
      return r
    }

    def readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PST(typeParsed: B): Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS = {
      if (!typeParsed) {
        reader.expectZ(Constants.RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS)
      }
      return Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS()
    }

    def readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container(): Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P => val r = readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PT(T); return r
        case Constants.RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS => val r = readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container.")
          val r = readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PST(T)
          return r
      }
    }

    def readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P(): Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P = {
      val r = readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PT(F)
      return r
    }

    def readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PT(typeParsed: B): Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P = {
      if (!typeParsed) {
        reader.expectZ(Constants.RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P)
      }
      val api_internal_failure = readIsolette_Data_ModelFailure_Flag_i()
      return Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P(api_internal_failure)
    }

    def readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS(): Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS = {
      val r = readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PST(F)
      return r
    }

    def readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PST(typeParsed: B): Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS = {
      if (!typeParsed) {
        reader.expectZ(Constants.RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS)
      }
      val api_internal_failure = readIsolette_Data_ModelFailure_Flag_i()
      return Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS(api_internal_failure)
    }

    def readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container(): Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P => val r = readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PT(T); return r
        case Constants.MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS => val r = readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container.")
          val r = readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PST(T)
          return r
      }
    }

    def readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P(): Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P = {
      val r = readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PT(F)
      return r
    }

    def readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PT(typeParsed: B): Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P = {
      if (!typeParsed) {
        reader.expectZ(Constants.MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P)
      }
      val api_current_tempWstatus = readIsolette_Data_ModelTempWstatus_i()
      val api_lower_alarm_tempWstatus = readIsolette_Data_ModelTempWstatus_i()
      val api_monitor_mode = readIsolette_Data_ModelMonitor_ModeType()
      val api_upper_alarm_tempWstatus = readIsolette_Data_ModelTempWstatus_i()
      return Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P(api_current_tempWstatus, api_lower_alarm_tempWstatus, api_monitor_mode, api_upper_alarm_tempWstatus)
    }

    def readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS(): Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS = {
      val r = readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PST(F)
      return r
    }

    def readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PST(typeParsed: B): Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS = {
      if (!typeParsed) {
        reader.expectZ(Constants.MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS)
      }
      val In_lastCmd = readIsolette_Data_ModelOn_OffType()
      val api_current_tempWstatus = readIsolette_Data_ModelTempWstatus_i()
      val api_lower_alarm_tempWstatus = readIsolette_Data_ModelTempWstatus_i()
      val api_monitor_mode = readIsolette_Data_ModelMonitor_ModeType()
      val api_upper_alarm_tempWstatus = readIsolette_Data_ModelTempWstatus_i()
      return Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS(In_lastCmd, api_current_tempWstatus, api_lower_alarm_tempWstatus, api_monitor_mode, api_upper_alarm_tempWstatus)
    }

    def readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container(): Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P => val r = readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PT(T); return r
        case Constants.MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS => val r = readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container.")
          val r = readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PST(T)
          return r
      }
    }

    def readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P(): Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P = {
      val r = readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PT(F)
      return r
    }

    def readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PT(typeParsed: B): Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P = {
      if (!typeParsed) {
        reader.expectZ(Constants.MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P)
      }
      val api_interface_failure = readIsolette_Data_ModelFailure_Flag_i()
      val api_lower_alarm_temp = readIsolette_Data_ModelTemp_i()
      val api_monitor_status = readIsolette_Data_ModelStatusType()
      val api_upper_alarm_temp = readIsolette_Data_ModelTemp_i()
      return Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P(api_interface_failure, api_lower_alarm_temp, api_monitor_status, api_upper_alarm_temp)
    }

    def readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS(): Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS = {
      val r = readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PST(F)
      return r
    }

    def readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PST(typeParsed: B): Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS = {
      if (!typeParsed) {
        reader.expectZ(Constants.MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS)
      }
      val lastCmd = readIsolette_Data_ModelOn_OffType()
      val api_interface_failure = readIsolette_Data_ModelFailure_Flag_i()
      val api_lower_alarm_temp = readIsolette_Data_ModelTemp_i()
      val api_monitor_status = readIsolette_Data_ModelStatusType()
      val api_upper_alarm_temp = readIsolette_Data_ModelTemp_i()
      return Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS(lastCmd, api_interface_failure, api_lower_alarm_temp, api_monitor_status, api_upper_alarm_temp)
    }

    def readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container(): Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.MonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P => val r = readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PT(T); return r
        case Constants.MonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS => val r = readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container.")
          val r = readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PST(T)
          return r
      }
    }

    def readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P(): Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P = {
      val r = readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PT(F)
      return r
    }

    def readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PT(typeParsed: B): Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P = {
      if (!typeParsed) {
        reader.expectZ(Constants.MonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P)
      }
      val api_current_tempWstatus = readIsolette_Data_ModelTempWstatus_i()
      val api_lower_alarm_temp = readIsolette_Data_ModelTemp_i()
      val api_monitor_mode = readIsolette_Data_ModelMonitor_ModeType()
      val api_upper_alarm_temp = readIsolette_Data_ModelTemp_i()
      return Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P(api_current_tempWstatus, api_lower_alarm_temp, api_monitor_mode, api_upper_alarm_temp)
    }

    def readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS(): Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS = {
      val r = readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PST(F)
      return r
    }

    def readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PST(typeParsed: B): Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS = {
      if (!typeParsed) {
        reader.expectZ(Constants.MonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS)
      }
      val In_lastCmd = readIsolette_Data_ModelOn_OffType()
      val api_current_tempWstatus = readIsolette_Data_ModelTempWstatus_i()
      val api_lower_alarm_temp = readIsolette_Data_ModelTemp_i()
      val api_monitor_mode = readIsolette_Data_ModelMonitor_ModeType()
      val api_upper_alarm_temp = readIsolette_Data_ModelTemp_i()
      return Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS(In_lastCmd, api_current_tempWstatus, api_lower_alarm_temp, api_monitor_mode, api_upper_alarm_temp)
    }

    def readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container(): Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.MonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P => val r = readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PT(T); return r
        case Constants.MonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS => val r = readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container.")
          val r = readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PST(T)
          return r
      }
    }

    def readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P(): Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P = {
      val r = readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PT(F)
      return r
    }

    def readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PT(typeParsed: B): Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P = {
      if (!typeParsed) {
        reader.expectZ(Constants.MonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P)
      }
      val api_alarm_control = readIsolette_Data_ModelOn_OffType()
      return Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P(api_alarm_control)
    }

    def readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS(): Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS = {
      val r = readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PST(F)
      return r
    }

    def readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PST(typeParsed: B): Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS = {
      if (!typeParsed) {
        reader.expectZ(Constants.MonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS)
      }
      val lastCmd = readIsolette_Data_ModelOn_OffType()
      val api_alarm_control = readIsolette_Data_ModelOn_OffType()
      return Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS(lastCmd, api_alarm_control)
    }

    def readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container(): Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P => val r = readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PT(T); return r
        case Constants.MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS => val r = readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container.")
          val r = readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PST(T)
          return r
      }
    }

    def readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P(): Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P = {
      val r = readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PT(F)
      return r
    }

    def readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PT(typeParsed: B): Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P = {
      if (!typeParsed) {
        reader.expectZ(Constants.MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P)
      }
      val api_current_tempWstatus = readIsolette_Data_ModelTempWstatus_i()
      val api_interface_failure = readIsolette_Data_ModelFailure_Flag_i()
      val api_internal_failure = readIsolette_Data_ModelFailure_Flag_i()
      return Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P(api_current_tempWstatus, api_interface_failure, api_internal_failure)
    }

    def readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS(): Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS = {
      val r = readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PST(F)
      return r
    }

    def readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PST(typeParsed: B): Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS = {
      if (!typeParsed) {
        reader.expectZ(Constants.MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS)
      }
      val In_lastMonitorMode = readIsolette_Data_ModelMonitor_ModeType()
      val api_current_tempWstatus = readIsolette_Data_ModelTempWstatus_i()
      val api_interface_failure = readIsolette_Data_ModelFailure_Flag_i()
      val api_internal_failure = readIsolette_Data_ModelFailure_Flag_i()
      return Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS(In_lastMonitorMode, api_current_tempWstatus, api_interface_failure, api_internal_failure)
    }

    def readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container(): Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P => val r = readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PT(T); return r
        case Constants.MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS => val r = readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container.")
          val r = readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PST(T)
          return r
      }
    }

    def readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P(): Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P = {
      val r = readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PT(F)
      return r
    }

    def readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PT(typeParsed: B): Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P = {
      if (!typeParsed) {
        reader.expectZ(Constants.MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P)
      }
      val api_monitor_mode = readIsolette_Data_ModelMonitor_ModeType()
      return Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P(api_monitor_mode)
    }

    def readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS(): Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS = {
      val r = readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PST(F)
      return r
    }

    def readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PST(typeParsed: B): Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS = {
      if (!typeParsed) {
        reader.expectZ(Constants.MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS)
      }
      val lastMonitorMode = readIsolette_Data_ModelMonitor_ModeType()
      val api_monitor_mode = readIsolette_Data_ModelMonitor_ModeType()
      return Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS(lastMonitorMode, api_monitor_mode)
    }

    def readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container(): Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_P => val r = readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PT(T); return r
        case Constants.MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PS => val r = readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container.")
          val r = readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PST(T)
          return r
      }
    }

    def readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_P(): Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_P = {
      val r = readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PT(F)
      return r
    }

    def readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PT(typeParsed: B): Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_P = {
      if (!typeParsed) {
        reader.expectZ(Constants.MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_P)
      }
      return Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_P()
    }

    def readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PS(): Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PS = {
      val r = readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PST(F)
      return r
    }

    def readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PST(typeParsed: B): Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PS = {
      if (!typeParsed) {
        reader.expectZ(Constants.MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PS)
      }
      return Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PS()
    }

    def readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container(): Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_P => val r = readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PT(T); return r
        case Constants.MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PS => val r = readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container.")
          val r = readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PST(T)
          return r
      }
    }

    def readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_P(): Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_P = {
      val r = readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PT(F)
      return r
    }

    def readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PT(typeParsed: B): Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_P = {
      if (!typeParsed) {
        reader.expectZ(Constants.MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_P)
      }
      val api_internal_failure = readIsolette_Data_ModelFailure_Flag_i()
      return Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_P(api_internal_failure)
    }

    def readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PS(): Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PS = {
      val r = readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PST(F)
      return r
    }

    def readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PST(typeParsed: B): Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PS = {
      if (!typeParsed) {
        reader.expectZ(Constants.MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PS)
      }
      val api_internal_failure = readIsolette_Data_ModelFailure_Flag_i()
      return Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PS(api_internal_failure)
    }

    def readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container(): Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P => val r = readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PT(T); return r
        case Constants.Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS => val r = readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container.")
          val r = readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PST(T)
          return r
      }
    }

    def readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P(): Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P = {
      val r = readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PT(F)
      return r
    }

    def readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PT(typeParsed: B): Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P = {
      if (!typeParsed) {
        reader.expectZ(Constants.Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P)
      }
      val api_alarm_control = readIsolette_Data_ModelOn_OffType()
      val api_display_temperature = readIsolette_Data_ModelTemp_i()
      val api_monitor_status = readIsolette_Data_ModelStatusType()
      val api_regulator_status = readIsolette_Data_ModelStatusType()
      return Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P(api_alarm_control, api_display_temperature, api_monitor_status, api_regulator_status)
    }

    def readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS(): Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS = {
      val r = readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PST(F)
      return r
    }

    def readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PST(typeParsed: B): Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS = {
      if (!typeParsed) {
        reader.expectZ(Constants.Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS)
      }
      val api_alarm_control = readIsolette_Data_ModelOn_OffType()
      val api_display_temperature = readIsolette_Data_ModelTemp_i()
      val api_monitor_status = readIsolette_Data_ModelStatusType()
      val api_regulator_status = readIsolette_Data_ModelStatusType()
      return Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS(api_alarm_control, api_display_temperature, api_monitor_status, api_regulator_status)
    }

    def readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container(): Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P => val r = readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PT(T); return r
        case Constants.Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS => val r = readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container.")
          val r = readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PST(T)
          return r
      }
    }

    def readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P(): Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P = {
      val r = readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PT(F)
      return r
    }

    def readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PT(typeParsed: B): Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P = {
      if (!typeParsed) {
        reader.expectZ(Constants.Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P)
      }
      val api_lower_alarm_tempWstatus = readIsolette_Data_ModelTempWstatus_i()
      val api_lower_desired_tempWstatus = readIsolette_Data_ModelTempWstatus_i()
      val api_upper_alarm_tempWstatus = readIsolette_Data_ModelTempWstatus_i()
      val api_upper_desired_tempWstatus = readIsolette_Data_ModelTempWstatus_i()
      return Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P(api_lower_alarm_tempWstatus, api_lower_desired_tempWstatus, api_upper_alarm_tempWstatus, api_upper_desired_tempWstatus)
    }

    def readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS(): Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS = {
      val r = readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PST(F)
      return r
    }

    def readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PST(typeParsed: B): Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS = {
      if (!typeParsed) {
        reader.expectZ(Constants.Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS)
      }
      val api_lower_alarm_tempWstatus = readIsolette_Data_ModelTempWstatus_i()
      val api_lower_desired_tempWstatus = readIsolette_Data_ModelTempWstatus_i()
      val api_upper_alarm_tempWstatus = readIsolette_Data_ModelTempWstatus_i()
      val api_upper_desired_tempWstatus = readIsolette_Data_ModelTempWstatus_i()
      return Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS(api_lower_alarm_tempWstatus, api_lower_desired_tempWstatus, api_upper_alarm_tempWstatus, api_upper_desired_tempWstatus)
    }

    def readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container(): Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_P => val r = readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PT(T); return r
        case Constants.DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PS => val r = readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container.")
          val r = readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PST(T)
          return r
      }
    }

    def readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_P(): Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_P = {
      val r = readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PT(F)
      return r
    }

    def readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PT(typeParsed: B): Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_P = {
      if (!typeParsed) {
        reader.expectZ(Constants.DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_P)
      }
      val api_air = readIsolette_Data_ModelPhysicalTemp_i()
      return Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_P(api_air)
    }

    def readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PS(): Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PS = {
      val r = readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PST(F)
      return r
    }

    def readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PST(typeParsed: B): Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PS = {
      if (!typeParsed) {
        reader.expectZ(Constants.DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PS)
      }
      val api_air = readIsolette_Data_ModelPhysicalTemp_i()
      return Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PS(api_air)
    }

    def readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container(): Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_P => val r = readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PT(T); return r
        case Constants.DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PS => val r = readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container.")
          val r = readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PST(T)
          return r
      }
    }

    def readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_P(): Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_P = {
      val r = readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PT(F)
      return r
    }

    def readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PT(typeParsed: B): Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_P = {
      if (!typeParsed) {
        reader.expectZ(Constants.DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_P)
      }
      val api_current_tempWstatus = readIsolette_Data_ModelTempWstatus_i()
      return Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_P(api_current_tempWstatus)
    }

    def readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PS(): Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PS = {
      val r = readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PST(F)
      return r
    }

    def readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PST(typeParsed: B): Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PS = {
      if (!typeParsed) {
        reader.expectZ(Constants.DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PS)
      }
      val api_current_tempWstatus = readIsolette_Data_ModelTempWstatus_i()
      return Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PS(api_current_tempWstatus)
    }

    def readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container(): Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.DevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_P => val r = readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PT(T); return r
        case Constants.DevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PS => val r = readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container.")
          val r = readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PST(T)
          return r
      }
    }

    def readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_P(): Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container_P = {
      val r = readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PT(F)
      return r
    }

    def readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PT(typeParsed: B): Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container_P = {
      if (!typeParsed) {
        reader.expectZ(Constants.DevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_P)
      }
      val api_heat_control = readIsolette_Data_ModelOn_OffType()
      return Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container_P(api_heat_control)
    }

    def readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PS(): Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PS = {
      val r = readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PST(F)
      return r
    }

    def readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PST(typeParsed: B): Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PS = {
      if (!typeParsed) {
        reader.expectZ(Constants.DevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PS)
      }
      val api_heat_control = readIsolette_Data_ModelOn_OffType()
      return Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PS(api_heat_control)
    }

    def readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container(): Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.DevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_P => val r = readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PT(T); return r
        case Constants.DevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PS => val r = readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container.")
          val r = readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PST(T)
          return r
      }
    }

    def readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_P(): Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container_P = {
      val r = readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PT(F)
      return r
    }

    def readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PT(typeParsed: B): Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container_P = {
      if (!typeParsed) {
        reader.expectZ(Constants.DevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_P)
      }
      val api_heat_out = readIsolette_EnvironmentHeatType()
      return Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container_P(api_heat_out)
    }

    def readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PS(): Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PS = {
      val r = readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PST(F)
      return r
    }

    def readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PST(typeParsed: B): Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PS = {
      if (!typeParsed) {
        reader.expectZ(Constants.DevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PS)
      }
      val api_heat_out = readIsolette_EnvironmentHeatType()
      return Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PS(api_heat_out)
    }

    def readruntimemonitorObservationKindType(): runtimemonitor.ObservationKind.Type = {
      val r = reader.readZ()
      return runtimemonitor.ObservationKind.byOrdinal(r).get
    }

    def readutilContainer(): util.Container = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants.utilEmptyContainer => val r = readutilEmptyContainerT(T); return r
        case Constants.MonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P => val r = readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PT(T); return r
        case Constants.MonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS => val r = readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PST(T); return r
        case Constants.MonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P => val r = readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PT(T); return r
        case Constants.MonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS => val r = readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PST(T); return r
        case Constants.MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P => val r = readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PT(T); return r
        case Constants.RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P => val r = readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PT(T); return r
        case Constants.MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS => val r = readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PST(T); return r
        case Constants.RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS => val r = readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PST(T); return r
        case Constants.MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P => val r = readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PT(T); return r
        case Constants.RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P => val r = readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PT(T); return r
        case Constants.MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS => val r = readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PST(T); return r
        case Constants.RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS => val r = readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PST(T); return r
        case Constants.DevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_P => val r = readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PT(T); return r
        case Constants.DevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PS => val r = readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PST(T); return r
        case Constants.DevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_P => val r = readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PT(T); return r
        case Constants.DevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PS => val r = readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PST(T); return r
        case Constants.MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_P => val r = readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PT(T); return r
        case Constants.RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P => val r = readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PT(T); return r
        case Constants.MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PS => val r = readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PST(T); return r
        case Constants.MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_P => val r = readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PT(T); return r
        case Constants.RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS => val r = readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PST(T); return r
        case Constants.MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PS => val r = readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PST(T); return r
        case Constants.RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P => val r = readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PT(T); return r
        case Constants.RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS => val r = readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PST(T); return r
        case Constants.MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P => val r = readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PT(T); return r
        case Constants.MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS => val r = readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PST(T); return r
        case Constants.MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P => val r = readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PT(T); return r
        case Constants.MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS => val r = readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PST(T); return r
        case Constants.RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P => val r = readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PT(T); return r
        case Constants.RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS => val r = readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PST(T); return r
        case Constants.RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P => val r = readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PT(T); return r
        case Constants.RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS => val r = readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PST(T); return r
        case Constants.RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P => val r = readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PT(T); return r
        case Constants.RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS => val r = readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PST(T); return r
        case Constants.RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P => val r = readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PT(T); return r
        case Constants.RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS => val r = readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PST(T); return r
        case Constants.DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_P => val r = readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PT(T); return r
        case Constants.DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PS => val r = readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PST(T); return r
        case Constants.DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_P => val r = readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PT(T); return r
        case Constants.DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PS => val r = readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PST(T); return r
        case Constants.Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P => val r = readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PT(T); return r
        case Constants.Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS => val r = readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PST(T); return r
        case Constants.Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P => val r = readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PT(T); return r
        case Constants.Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS => val r = readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of util.Container.")
          val r = readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PST(T)
          return r
      }
    }

    def readutilEmptyContainer(): util.EmptyContainer = {
      val r = readutilEmptyContainerT(F)
      return r
    }

    def readutilEmptyContainerT(typeParsed: B): util.EmptyContainer = {
      if (!typeParsed) {
        reader.expectZ(Constants.utilEmptyContainer)
      }
      return util.EmptyContainer()
    }

    def read_artDataContent(): art.DataContent = {
      val i = reader.curr
      val t = reader.readZ()
      t match {
        case Constants._artEmpty => val r = read_artEmptyT(T); return r
        case Constants.Base_TypesBoolean_Payload => val r = readBase_TypesBoolean_PayloadT(T); return r
        case Constants.Base_TypesInteger_Payload => val r = readBase_TypesInteger_PayloadT(T); return r
        case Constants.Base_TypesInteger_8_Payload => val r = readBase_TypesInteger_8_PayloadT(T); return r
        case Constants.Base_TypesInteger_16_Payload => val r = readBase_TypesInteger_16_PayloadT(T); return r
        case Constants.Base_TypesInteger_32_Payload => val r = readBase_TypesInteger_32_PayloadT(T); return r
        case Constants.Base_TypesInteger_64_Payload => val r = readBase_TypesInteger_64_PayloadT(T); return r
        case Constants.Base_TypesUnsigned_8_Payload => val r = readBase_TypesUnsigned_8_PayloadT(T); return r
        case Constants.Base_TypesUnsigned_16_Payload => val r = readBase_TypesUnsigned_16_PayloadT(T); return r
        case Constants.Base_TypesUnsigned_32_Payload => val r = readBase_TypesUnsigned_32_PayloadT(T); return r
        case Constants.Base_TypesUnsigned_64_Payload => val r = readBase_TypesUnsigned_64_PayloadT(T); return r
        case Constants.Base_TypesFloat_Payload => val r = readBase_TypesFloat_PayloadT(T); return r
        case Constants.Base_TypesFloat_32_Payload => val r = readBase_TypesFloat_32_PayloadT(T); return r
        case Constants.Base_TypesFloat_64_Payload => val r = readBase_TypesFloat_64_PayloadT(T); return r
        case Constants.Base_TypesCharacter_Payload => val r = readBase_TypesCharacter_PayloadT(T); return r
        case Constants.Base_TypesString_Payload => val r = readBase_TypesString_PayloadT(T); return r
        case Constants.Base_TypesBits_Payload => val r = readBase_TypesBits_PayloadT(T); return r
        case Constants.utilEmptyContainer => val r = readutilEmptyContainerT(T); return r
        case Constants.Isolette_EnvironmentHeat_Payload => val r = readIsolette_EnvironmentHeat_PayloadT(T); return r
        case Constants.Isolette_Data_ModelOn_Off_Payload => val r = readIsolette_Data_ModelOn_Off_PayloadT(T); return r
        case Constants.Isolette_Data_ModelStatus_Payload => val r = readIsolette_Data_ModelStatus_PayloadT(T); return r
        case Constants.Isolette_Data_ModelTemp_i_Payload => val r = readIsolette_Data_ModelTemp_i_PayloadT(T); return r
        case Constants.Isolette_Data_ModelValueStatus_Payload => val r = readIsolette_Data_ModelValueStatus_PayloadT(T); return r
        case Constants.Isolette_Data_ModelMonitor_Mode_Payload => val r = readIsolette_Data_ModelMonitor_Mode_PayloadT(T); return r
        case Constants.Isolette_Data_ModelTempWstatus_i_Payload => val r = readIsolette_Data_ModelTempWstatus_i_PayloadT(T); return r
        case Constants.Isolette_Data_ModelRegulator_Mode_Payload => val r = readIsolette_Data_ModelRegulator_Mode_PayloadT(T); return r
        case Constants.Isolette_Data_ModelFailure_Flag_i_Payload => val r = readIsolette_Data_ModelFailure_Flag_i_PayloadT(T); return r
        case Constants.Isolette_Data_ModelPhysicalTemp_i_Payload => val r = readIsolette_Data_ModelPhysicalTemp_i_PayloadT(T); return r
        case Constants.Isolette_EnvironmentAir_Interaction_Payload => val r = readIsolette_EnvironmentAir_Interaction_PayloadT(T); return r
        case Constants.Isolette_EnvironmentInterface_Interaction_Payload => val r = readIsolette_EnvironmentInterface_Interaction_PayloadT(T); return r
        case Constants.MonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P => val r = readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PT(T); return r
        case Constants.MonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS => val r = readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PST(T); return r
        case Constants.MonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P => val r = readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PT(T); return r
        case Constants.MonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS => val r = readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PST(T); return r
        case Constants.MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P => val r = readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PT(T); return r
        case Constants.RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P => val r = readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PT(T); return r
        case Constants.MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS => val r = readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PST(T); return r
        case Constants.RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS => val r = readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PST(T); return r
        case Constants.MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P => val r = readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PT(T); return r
        case Constants.RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P => val r = readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PT(T); return r
        case Constants.MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS => val r = readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PST(T); return r
        case Constants.RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS => val r = readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PST(T); return r
        case Constants.DevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_P => val r = readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PT(T); return r
        case Constants.DevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PS => val r = readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PST(T); return r
        case Constants.DevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_P => val r = readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PT(T); return r
        case Constants.DevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PS => val r = readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PST(T); return r
        case Constants.MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_P => val r = readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PT(T); return r
        case Constants.RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P => val r = readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PT(T); return r
        case Constants.MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PS => val r = readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PST(T); return r
        case Constants.MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_P => val r = readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PT(T); return r
        case Constants.RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS => val r = readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PST(T); return r
        case Constants.MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PS => val r = readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PST(T); return r
        case Constants.RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P => val r = readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PT(T); return r
        case Constants.RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS => val r = readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PST(T); return r
        case Constants.MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P => val r = readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PT(T); return r
        case Constants.MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS => val r = readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PST(T); return r
        case Constants.MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P => val r = readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PT(T); return r
        case Constants.MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS => val r = readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PST(T); return r
        case Constants.RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P => val r = readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PT(T); return r
        case Constants.RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS => val r = readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PST(T); return r
        case Constants.RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P => val r = readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PT(T); return r
        case Constants.RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS => val r = readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PST(T); return r
        case Constants.RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P => val r = readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PT(T); return r
        case Constants.RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS => val r = readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PST(T); return r
        case Constants.RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P => val r = readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PT(T); return r
        case Constants.RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS => val r = readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PST(T); return r
        case Constants.DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_P => val r = readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PT(T); return r
        case Constants.DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PS => val r = readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PST(T); return r
        case Constants.DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_P => val r = readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PT(T); return r
        case Constants.DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PS => val r = readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PST(T); return r
        case Constants.Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P => val r = readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PT(T); return r
        case Constants.Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS => val r = readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PST(T); return r
        case Constants.Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P => val r = readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PT(T); return r
        case Constants.Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS => val r = readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PST(T); return r
        case _ =>
          reader.error(i, s"$t is not a valid type of art.DataContent.")
          val r = readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PST(T)
          return r
      }
    }

    def read_artEmpty(): art.Empty = {
      val r = read_artEmptyT(F)
      return r
    }

    def read_artEmptyT(typeParsed: B): art.Empty = {
      if (!typeParsed) {
        reader.expectZ(Constants._artEmpty)
      }
      return art.Empty()
    }

  }

  def to[T](data: ISZ[U8], f: Reader => T): Either[T, MessagePack.ErrorMsg] = {
    val rd = Reader.Default(MessagePack.reader(data))
    rd.reader.init()
    val r = f(rd)
    rd.errorOpt match {
      case Some(e) => return Either.Right(e)
      case _ => return Either.Left(r)
    }
  }

  def fromIsolette_Data_ModelPhysicalTemp_i(o: Isolette_Data_Model.PhysicalTemp_i, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeIsolette_Data_ModelPhysicalTemp_i(o)
    return w.result
  }

  def toIsolette_Data_ModelPhysicalTemp_i(data: ISZ[U8]): Either[Isolette_Data_Model.PhysicalTemp_i, MessagePack.ErrorMsg] = {
    def fIsolette_Data_ModelPhysicalTemp_i(reader: Reader): Isolette_Data_Model.PhysicalTemp_i = {
      val r = reader.readIsolette_Data_ModelPhysicalTemp_i()
      return r
    }
    val r = to(data, fIsolette_Data_ModelPhysicalTemp_i _)
    return r
  }

  def fromIsolette_Data_ModelPhysicalTemp_i_Payload(o: Isolette_Data_Model.PhysicalTemp_i_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeIsolette_Data_ModelPhysicalTemp_i_Payload(o)
    return w.result
  }

  def toIsolette_Data_ModelPhysicalTemp_i_Payload(data: ISZ[U8]): Either[Isolette_Data_Model.PhysicalTemp_i_Payload, MessagePack.ErrorMsg] = {
    def fIsolette_Data_ModelPhysicalTemp_i_Payload(reader: Reader): Isolette_Data_Model.PhysicalTemp_i_Payload = {
      val r = reader.readIsolette_Data_ModelPhysicalTemp_i_Payload()
      return r
    }
    val r = to(data, fIsolette_Data_ModelPhysicalTemp_i_Payload _)
    return r
  }

  def fromIsolette_Data_ModelTempWstatus_i(o: Isolette_Data_Model.TempWstatus_i, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeIsolette_Data_ModelTempWstatus_i(o)
    return w.result
  }

  def toIsolette_Data_ModelTempWstatus_i(data: ISZ[U8]): Either[Isolette_Data_Model.TempWstatus_i, MessagePack.ErrorMsg] = {
    def fIsolette_Data_ModelTempWstatus_i(reader: Reader): Isolette_Data_Model.TempWstatus_i = {
      val r = reader.readIsolette_Data_ModelTempWstatus_i()
      return r
    }
    val r = to(data, fIsolette_Data_ModelTempWstatus_i _)
    return r
  }

  def fromIsolette_Data_ModelTempWstatus_i_Payload(o: Isolette_Data_Model.TempWstatus_i_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeIsolette_Data_ModelTempWstatus_i_Payload(o)
    return w.result
  }

  def toIsolette_Data_ModelTempWstatus_i_Payload(data: ISZ[U8]): Either[Isolette_Data_Model.TempWstatus_i_Payload, MessagePack.ErrorMsg] = {
    def fIsolette_Data_ModelTempWstatus_i_Payload(reader: Reader): Isolette_Data_Model.TempWstatus_i_Payload = {
      val r = reader.readIsolette_Data_ModelTempWstatus_i_Payload()
      return r
    }
    val r = to(data, fIsolette_Data_ModelTempWstatus_i_Payload _)
    return r
  }

  def fromIsolette_Data_ModelTemp_i(o: Isolette_Data_Model.Temp_i, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeIsolette_Data_ModelTemp_i(o)
    return w.result
  }

  def toIsolette_Data_ModelTemp_i(data: ISZ[U8]): Either[Isolette_Data_Model.Temp_i, MessagePack.ErrorMsg] = {
    def fIsolette_Data_ModelTemp_i(reader: Reader): Isolette_Data_Model.Temp_i = {
      val r = reader.readIsolette_Data_ModelTemp_i()
      return r
    }
    val r = to(data, fIsolette_Data_ModelTemp_i _)
    return r
  }

  def fromIsolette_Data_ModelTemp_i_Payload(o: Isolette_Data_Model.Temp_i_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeIsolette_Data_ModelTemp_i_Payload(o)
    return w.result
  }

  def toIsolette_Data_ModelTemp_i_Payload(data: ISZ[U8]): Either[Isolette_Data_Model.Temp_i_Payload, MessagePack.ErrorMsg] = {
    def fIsolette_Data_ModelTemp_i_Payload(reader: Reader): Isolette_Data_Model.Temp_i_Payload = {
      val r = reader.readIsolette_Data_ModelTemp_i_Payload()
      return r
    }
    val r = to(data, fIsolette_Data_ModelTemp_i_Payload _)
    return r
  }

  def fromIsolette_Data_ModelOn_Off_Payload(o: Isolette_Data_Model.On_Off_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeIsolette_Data_ModelOn_Off_Payload(o)
    return w.result
  }

  def toIsolette_Data_ModelOn_Off_Payload(data: ISZ[U8]): Either[Isolette_Data_Model.On_Off_Payload, MessagePack.ErrorMsg] = {
    def fIsolette_Data_ModelOn_Off_Payload(reader: Reader): Isolette_Data_Model.On_Off_Payload = {
      val r = reader.readIsolette_Data_ModelOn_Off_Payload()
      return r
    }
    val r = to(data, fIsolette_Data_ModelOn_Off_Payload _)
    return r
  }

  def fromIsolette_Data_ModelFailure_Flag_i(o: Isolette_Data_Model.Failure_Flag_i, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeIsolette_Data_ModelFailure_Flag_i(o)
    return w.result
  }

  def toIsolette_Data_ModelFailure_Flag_i(data: ISZ[U8]): Either[Isolette_Data_Model.Failure_Flag_i, MessagePack.ErrorMsg] = {
    def fIsolette_Data_ModelFailure_Flag_i(reader: Reader): Isolette_Data_Model.Failure_Flag_i = {
      val r = reader.readIsolette_Data_ModelFailure_Flag_i()
      return r
    }
    val r = to(data, fIsolette_Data_ModelFailure_Flag_i _)
    return r
  }

  def fromIsolette_Data_ModelFailure_Flag_i_Payload(o: Isolette_Data_Model.Failure_Flag_i_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeIsolette_Data_ModelFailure_Flag_i_Payload(o)
    return w.result
  }

  def toIsolette_Data_ModelFailure_Flag_i_Payload(data: ISZ[U8]): Either[Isolette_Data_Model.Failure_Flag_i_Payload, MessagePack.ErrorMsg] = {
    def fIsolette_Data_ModelFailure_Flag_i_Payload(reader: Reader): Isolette_Data_Model.Failure_Flag_i_Payload = {
      val r = reader.readIsolette_Data_ModelFailure_Flag_i_Payload()
      return r
    }
    val r = to(data, fIsolette_Data_ModelFailure_Flag_i_Payload _)
    return r
  }

  def fromIsolette_Data_ModelValueStatus_Payload(o: Isolette_Data_Model.ValueStatus_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeIsolette_Data_ModelValueStatus_Payload(o)
    return w.result
  }

  def toIsolette_Data_ModelValueStatus_Payload(data: ISZ[U8]): Either[Isolette_Data_Model.ValueStatus_Payload, MessagePack.ErrorMsg] = {
    def fIsolette_Data_ModelValueStatus_Payload(reader: Reader): Isolette_Data_Model.ValueStatus_Payload = {
      val r = reader.readIsolette_Data_ModelValueStatus_Payload()
      return r
    }
    val r = to(data, fIsolette_Data_ModelValueStatus_Payload _)
    return r
  }

  def fromIsolette_Data_ModelStatus_Payload(o: Isolette_Data_Model.Status_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeIsolette_Data_ModelStatus_Payload(o)
    return w.result
  }

  def toIsolette_Data_ModelStatus_Payload(data: ISZ[U8]): Either[Isolette_Data_Model.Status_Payload, MessagePack.ErrorMsg] = {
    def fIsolette_Data_ModelStatus_Payload(reader: Reader): Isolette_Data_Model.Status_Payload = {
      val r = reader.readIsolette_Data_ModelStatus_Payload()
      return r
    }
    val r = to(data, fIsolette_Data_ModelStatus_Payload _)
    return r
  }

  def fromIsolette_Data_ModelRegulator_Mode_Payload(o: Isolette_Data_Model.Regulator_Mode_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeIsolette_Data_ModelRegulator_Mode_Payload(o)
    return w.result
  }

  def toIsolette_Data_ModelRegulator_Mode_Payload(data: ISZ[U8]): Either[Isolette_Data_Model.Regulator_Mode_Payload, MessagePack.ErrorMsg] = {
    def fIsolette_Data_ModelRegulator_Mode_Payload(reader: Reader): Isolette_Data_Model.Regulator_Mode_Payload = {
      val r = reader.readIsolette_Data_ModelRegulator_Mode_Payload()
      return r
    }
    val r = to(data, fIsolette_Data_ModelRegulator_Mode_Payload _)
    return r
  }

  def fromIsolette_Data_ModelMonitor_Mode_Payload(o: Isolette_Data_Model.Monitor_Mode_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeIsolette_Data_ModelMonitor_Mode_Payload(o)
    return w.result
  }

  def toIsolette_Data_ModelMonitor_Mode_Payload(data: ISZ[U8]): Either[Isolette_Data_Model.Monitor_Mode_Payload, MessagePack.ErrorMsg] = {
    def fIsolette_Data_ModelMonitor_Mode_Payload(reader: Reader): Isolette_Data_Model.Monitor_Mode_Payload = {
      val r = reader.readIsolette_Data_ModelMonitor_Mode_Payload()
      return r
    }
    val r = to(data, fIsolette_Data_ModelMonitor_Mode_Payload _)
    return r
  }

  def fromIsolette_EnvironmentHeat_Payload(o: Isolette_Environment.Heat_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeIsolette_EnvironmentHeat_Payload(o)
    return w.result
  }

  def toIsolette_EnvironmentHeat_Payload(data: ISZ[U8]): Either[Isolette_Environment.Heat_Payload, MessagePack.ErrorMsg] = {
    def fIsolette_EnvironmentHeat_Payload(reader: Reader): Isolette_Environment.Heat_Payload = {
      val r = reader.readIsolette_EnvironmentHeat_Payload()
      return r
    }
    val r = to(data, fIsolette_EnvironmentHeat_Payload _)
    return r
  }

  def fromIsolette_EnvironmentInterface_Interaction_Payload(o: Isolette_Environment.Interface_Interaction_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeIsolette_EnvironmentInterface_Interaction_Payload(o)
    return w.result
  }

  def toIsolette_EnvironmentInterface_Interaction_Payload(data: ISZ[U8]): Either[Isolette_Environment.Interface_Interaction_Payload, MessagePack.ErrorMsg] = {
    def fIsolette_EnvironmentInterface_Interaction_Payload(reader: Reader): Isolette_Environment.Interface_Interaction_Payload = {
      val r = reader.readIsolette_EnvironmentInterface_Interaction_Payload()
      return r
    }
    val r = to(data, fIsolette_EnvironmentInterface_Interaction_Payload _)
    return r
  }

  def fromIsolette_EnvironmentAir_Interaction_Payload(o: Isolette_Environment.Air_Interaction_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeIsolette_EnvironmentAir_Interaction_Payload(o)
    return w.result
  }

  def toIsolette_EnvironmentAir_Interaction_Payload(data: ISZ[U8]): Either[Isolette_Environment.Air_Interaction_Payload, MessagePack.ErrorMsg] = {
    def fIsolette_EnvironmentAir_Interaction_Payload(reader: Reader): Isolette_Environment.Air_Interaction_Payload = {
      val r = reader.readIsolette_EnvironmentAir_Interaction_Payload()
      return r
    }
    val r = to(data, fIsolette_EnvironmentAir_Interaction_Payload _)
    return r
  }

  def fromBase_TypesBoolean_Payload(o: Base_Types.Boolean_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeBase_TypesBoolean_Payload(o)
    return w.result
  }

  def toBase_TypesBoolean_Payload(data: ISZ[U8]): Either[Base_Types.Boolean_Payload, MessagePack.ErrorMsg] = {
    def fBase_TypesBoolean_Payload(reader: Reader): Base_Types.Boolean_Payload = {
      val r = reader.readBase_TypesBoolean_Payload()
      return r
    }
    val r = to(data, fBase_TypesBoolean_Payload _)
    return r
  }

  def fromBase_TypesInteger_Payload(o: Base_Types.Integer_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeBase_TypesInteger_Payload(o)
    return w.result
  }

  def toBase_TypesInteger_Payload(data: ISZ[U8]): Either[Base_Types.Integer_Payload, MessagePack.ErrorMsg] = {
    def fBase_TypesInteger_Payload(reader: Reader): Base_Types.Integer_Payload = {
      val r = reader.readBase_TypesInteger_Payload()
      return r
    }
    val r = to(data, fBase_TypesInteger_Payload _)
    return r
  }

  def fromBase_TypesInteger_8_Payload(o: Base_Types.Integer_8_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeBase_TypesInteger_8_Payload(o)
    return w.result
  }

  def toBase_TypesInteger_8_Payload(data: ISZ[U8]): Either[Base_Types.Integer_8_Payload, MessagePack.ErrorMsg] = {
    def fBase_TypesInteger_8_Payload(reader: Reader): Base_Types.Integer_8_Payload = {
      val r = reader.readBase_TypesInteger_8_Payload()
      return r
    }
    val r = to(data, fBase_TypesInteger_8_Payload _)
    return r
  }

  def fromBase_TypesInteger_16_Payload(o: Base_Types.Integer_16_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeBase_TypesInteger_16_Payload(o)
    return w.result
  }

  def toBase_TypesInteger_16_Payload(data: ISZ[U8]): Either[Base_Types.Integer_16_Payload, MessagePack.ErrorMsg] = {
    def fBase_TypesInteger_16_Payload(reader: Reader): Base_Types.Integer_16_Payload = {
      val r = reader.readBase_TypesInteger_16_Payload()
      return r
    }
    val r = to(data, fBase_TypesInteger_16_Payload _)
    return r
  }

  def fromBase_TypesInteger_32_Payload(o: Base_Types.Integer_32_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeBase_TypesInteger_32_Payload(o)
    return w.result
  }

  def toBase_TypesInteger_32_Payload(data: ISZ[U8]): Either[Base_Types.Integer_32_Payload, MessagePack.ErrorMsg] = {
    def fBase_TypesInteger_32_Payload(reader: Reader): Base_Types.Integer_32_Payload = {
      val r = reader.readBase_TypesInteger_32_Payload()
      return r
    }
    val r = to(data, fBase_TypesInteger_32_Payload _)
    return r
  }

  def fromBase_TypesInteger_64_Payload(o: Base_Types.Integer_64_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeBase_TypesInteger_64_Payload(o)
    return w.result
  }

  def toBase_TypesInteger_64_Payload(data: ISZ[U8]): Either[Base_Types.Integer_64_Payload, MessagePack.ErrorMsg] = {
    def fBase_TypesInteger_64_Payload(reader: Reader): Base_Types.Integer_64_Payload = {
      val r = reader.readBase_TypesInteger_64_Payload()
      return r
    }
    val r = to(data, fBase_TypesInteger_64_Payload _)
    return r
  }

  def fromBase_TypesUnsigned_8_Payload(o: Base_Types.Unsigned_8_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeBase_TypesUnsigned_8_Payload(o)
    return w.result
  }

  def toBase_TypesUnsigned_8_Payload(data: ISZ[U8]): Either[Base_Types.Unsigned_8_Payload, MessagePack.ErrorMsg] = {
    def fBase_TypesUnsigned_8_Payload(reader: Reader): Base_Types.Unsigned_8_Payload = {
      val r = reader.readBase_TypesUnsigned_8_Payload()
      return r
    }
    val r = to(data, fBase_TypesUnsigned_8_Payload _)
    return r
  }

  def fromBase_TypesUnsigned_16_Payload(o: Base_Types.Unsigned_16_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeBase_TypesUnsigned_16_Payload(o)
    return w.result
  }

  def toBase_TypesUnsigned_16_Payload(data: ISZ[U8]): Either[Base_Types.Unsigned_16_Payload, MessagePack.ErrorMsg] = {
    def fBase_TypesUnsigned_16_Payload(reader: Reader): Base_Types.Unsigned_16_Payload = {
      val r = reader.readBase_TypesUnsigned_16_Payload()
      return r
    }
    val r = to(data, fBase_TypesUnsigned_16_Payload _)
    return r
  }

  def fromBase_TypesUnsigned_32_Payload(o: Base_Types.Unsigned_32_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeBase_TypesUnsigned_32_Payload(o)
    return w.result
  }

  def toBase_TypesUnsigned_32_Payload(data: ISZ[U8]): Either[Base_Types.Unsigned_32_Payload, MessagePack.ErrorMsg] = {
    def fBase_TypesUnsigned_32_Payload(reader: Reader): Base_Types.Unsigned_32_Payload = {
      val r = reader.readBase_TypesUnsigned_32_Payload()
      return r
    }
    val r = to(data, fBase_TypesUnsigned_32_Payload _)
    return r
  }

  def fromBase_TypesUnsigned_64_Payload(o: Base_Types.Unsigned_64_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeBase_TypesUnsigned_64_Payload(o)
    return w.result
  }

  def toBase_TypesUnsigned_64_Payload(data: ISZ[U8]): Either[Base_Types.Unsigned_64_Payload, MessagePack.ErrorMsg] = {
    def fBase_TypesUnsigned_64_Payload(reader: Reader): Base_Types.Unsigned_64_Payload = {
      val r = reader.readBase_TypesUnsigned_64_Payload()
      return r
    }
    val r = to(data, fBase_TypesUnsigned_64_Payload _)
    return r
  }

  def fromBase_TypesFloat_Payload(o: Base_Types.Float_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeBase_TypesFloat_Payload(o)
    return w.result
  }

  def toBase_TypesFloat_Payload(data: ISZ[U8]): Either[Base_Types.Float_Payload, MessagePack.ErrorMsg] = {
    def fBase_TypesFloat_Payload(reader: Reader): Base_Types.Float_Payload = {
      val r = reader.readBase_TypesFloat_Payload()
      return r
    }
    val r = to(data, fBase_TypesFloat_Payload _)
    return r
  }

  def fromBase_TypesFloat_32_Payload(o: Base_Types.Float_32_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeBase_TypesFloat_32_Payload(o)
    return w.result
  }

  def toBase_TypesFloat_32_Payload(data: ISZ[U8]): Either[Base_Types.Float_32_Payload, MessagePack.ErrorMsg] = {
    def fBase_TypesFloat_32_Payload(reader: Reader): Base_Types.Float_32_Payload = {
      val r = reader.readBase_TypesFloat_32_Payload()
      return r
    }
    val r = to(data, fBase_TypesFloat_32_Payload _)
    return r
  }

  def fromBase_TypesFloat_64_Payload(o: Base_Types.Float_64_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeBase_TypesFloat_64_Payload(o)
    return w.result
  }

  def toBase_TypesFloat_64_Payload(data: ISZ[U8]): Either[Base_Types.Float_64_Payload, MessagePack.ErrorMsg] = {
    def fBase_TypesFloat_64_Payload(reader: Reader): Base_Types.Float_64_Payload = {
      val r = reader.readBase_TypesFloat_64_Payload()
      return r
    }
    val r = to(data, fBase_TypesFloat_64_Payload _)
    return r
  }

  def fromBase_TypesCharacter_Payload(o: Base_Types.Character_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeBase_TypesCharacter_Payload(o)
    return w.result
  }

  def toBase_TypesCharacter_Payload(data: ISZ[U8]): Either[Base_Types.Character_Payload, MessagePack.ErrorMsg] = {
    def fBase_TypesCharacter_Payload(reader: Reader): Base_Types.Character_Payload = {
      val r = reader.readBase_TypesCharacter_Payload()
      return r
    }
    val r = to(data, fBase_TypesCharacter_Payload _)
    return r
  }

  def fromBase_TypesString_Payload(o: Base_Types.String_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeBase_TypesString_Payload(o)
    return w.result
  }

  def toBase_TypesString_Payload(data: ISZ[U8]): Either[Base_Types.String_Payload, MessagePack.ErrorMsg] = {
    def fBase_TypesString_Payload(reader: Reader): Base_Types.String_Payload = {
      val r = reader.readBase_TypesString_Payload()
      return r
    }
    val r = to(data, fBase_TypesString_Payload _)
    return r
  }

  def fromBase_TypesBits_Payload(o: Base_Types.Bits_Payload, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeBase_TypesBits_Payload(o)
    return w.result
  }

  def toBase_TypesBits_Payload(data: ISZ[U8]): Either[Base_Types.Bits_Payload, MessagePack.ErrorMsg] = {
    def fBase_TypesBits_Payload(reader: Reader): Base_Types.Bits_Payload = {
      val r = reader.readBase_TypesBits_Payload()
      return r
    }
    val r = to(data, fBase_TypesBits_Payload _)
    return r
  }

  def fromRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container(o: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container(o)
    return w.result
  }

  def toRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container(data: ISZ[U8]): Either[Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container, MessagePack.ErrorMsg] = {
    def fRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container(reader: Reader): Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container = {
      val r = reader.readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container()
      return r
    }
    val r = to(data, fRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container _)
    return r
  }

  def fromRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P(o: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P(o)
    return w.result
  }

  def toRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P(data: ISZ[U8]): Either[Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P, MessagePack.ErrorMsg] = {
    def fRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P(reader: Reader): Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P = {
      val r = reader.readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P()
      return r
    }
    val r = to(data, fRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P _)
    return r
  }

  def fromRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS(o: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS(o)
    return w.result
  }

  def toRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS(data: ISZ[U8]): Either[Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS, MessagePack.ErrorMsg] = {
    def fRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS(reader: Reader): Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS = {
      val r = reader.readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS()
      return r
    }
    val r = to(data, fRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS _)
    return r
  }

  def fromRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container(o: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container(o)
    return w.result
  }

  def toRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container(data: ISZ[U8]): Either[Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container, MessagePack.ErrorMsg] = {
    def fRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container(reader: Reader): Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container = {
      val r = reader.readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container()
      return r
    }
    val r = to(data, fRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container _)
    return r
  }

  def fromRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P(o: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P(o)
    return w.result
  }

  def toRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P(data: ISZ[U8]): Either[Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P, MessagePack.ErrorMsg] = {
    def fRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P(reader: Reader): Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P = {
      val r = reader.readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P()
      return r
    }
    val r = to(data, fRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P _)
    return r
  }

  def fromRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS(o: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS(o)
    return w.result
  }

  def toRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS(data: ISZ[U8]): Either[Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS, MessagePack.ErrorMsg] = {
    def fRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS(reader: Reader): Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS = {
      val r = reader.readRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS()
      return r
    }
    val r = to(data, fRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS _)
    return r
  }

  def fromRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container(o: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container(o)
    return w.result
  }

  def toRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container(data: ISZ[U8]): Either[Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container, MessagePack.ErrorMsg] = {
    def fRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container(reader: Reader): Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container = {
      val r = reader.readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container()
      return r
    }
    val r = to(data, fRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container _)
    return r
  }

  def fromRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P(o: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P(o)
    return w.result
  }

  def toRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P(data: ISZ[U8]): Either[Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P, MessagePack.ErrorMsg] = {
    def fRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P(reader: Reader): Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P = {
      val r = reader.readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P()
      return r
    }
    val r = to(data, fRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P _)
    return r
  }

  def fromRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS(o: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS(o)
    return w.result
  }

  def toRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS(data: ISZ[U8]): Either[Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS, MessagePack.ErrorMsg] = {
    def fRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS(reader: Reader): Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS = {
      val r = reader.readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS()
      return r
    }
    val r = to(data, fRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS _)
    return r
  }

  def fromRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container(o: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container(o)
    return w.result
  }

  def toRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container(data: ISZ[U8]): Either[Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container, MessagePack.ErrorMsg] = {
    def fRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container(reader: Reader): Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container = {
      val r = reader.readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container()
      return r
    }
    val r = to(data, fRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container _)
    return r
  }

  def fromRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P(o: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P(o)
    return w.result
  }

  def toRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P(data: ISZ[U8]): Either[Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P, MessagePack.ErrorMsg] = {
    def fRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P(reader: Reader): Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P = {
      val r = reader.readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P()
      return r
    }
    val r = to(data, fRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P _)
    return r
  }

  def fromRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS(o: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS(o)
    return w.result
  }

  def toRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS(data: ISZ[U8]): Either[Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS, MessagePack.ErrorMsg] = {
    def fRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS(reader: Reader): Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS = {
      val r = reader.readRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS()
      return r
    }
    val r = to(data, fRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS _)
    return r
  }

  def fromRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container(o: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container(o)
    return w.result
  }

  def toRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container(data: ISZ[U8]): Either[Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container, MessagePack.ErrorMsg] = {
    def fRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container(reader: Reader): Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container = {
      val r = reader.readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container()
      return r
    }
    val r = to(data, fRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container _)
    return r
  }

  def fromRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P(o: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P(o)
    return w.result
  }

  def toRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P(data: ISZ[U8]): Either[Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P, MessagePack.ErrorMsg] = {
    def fRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P(reader: Reader): Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P = {
      val r = reader.readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P()
      return r
    }
    val r = to(data, fRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P _)
    return r
  }

  def fromRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS(o: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS(o)
    return w.result
  }

  def toRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS(data: ISZ[U8]): Either[Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS, MessagePack.ErrorMsg] = {
    def fRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS(reader: Reader): Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS = {
      val r = reader.readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS()
      return r
    }
    val r = to(data, fRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS _)
    return r
  }

  def fromRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container(o: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container(o)
    return w.result
  }

  def toRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container(data: ISZ[U8]): Either[Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container, MessagePack.ErrorMsg] = {
    def fRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container(reader: Reader): Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container = {
      val r = reader.readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container()
      return r
    }
    val r = to(data, fRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container _)
    return r
  }

  def fromRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P(o: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P(o)
    return w.result
  }

  def toRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P(data: ISZ[U8]): Either[Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P, MessagePack.ErrorMsg] = {
    def fRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P(reader: Reader): Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P = {
      val r = reader.readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P()
      return r
    }
    val r = to(data, fRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P _)
    return r
  }

  def fromRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS(o: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS(o)
    return w.result
  }

  def toRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS(data: ISZ[U8]): Either[Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS, MessagePack.ErrorMsg] = {
    def fRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS(reader: Reader): Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS = {
      val r = reader.readRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS()
      return r
    }
    val r = to(data, fRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS _)
    return r
  }

  def fromRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container(o: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container(o)
    return w.result
  }

  def toRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container(data: ISZ[U8]): Either[Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container, MessagePack.ErrorMsg] = {
    def fRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container(reader: Reader): Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container = {
      val r = reader.readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container()
      return r
    }
    val r = to(data, fRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container _)
    return r
  }

  def fromRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P(o: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P(o)
    return w.result
  }

  def toRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P(data: ISZ[U8]): Either[Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P, MessagePack.ErrorMsg] = {
    def fRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P(reader: Reader): Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P = {
      val r = reader.readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P()
      return r
    }
    val r = to(data, fRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P _)
    return r
  }

  def fromRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS(o: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS(o)
    return w.result
  }

  def toRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS(data: ISZ[U8]): Either[Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS, MessagePack.ErrorMsg] = {
    def fRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS(reader: Reader): Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS = {
      val r = reader.readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS()
      return r
    }
    val r = to(data, fRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS _)
    return r
  }

  def fromRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container(o: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container(o)
    return w.result
  }

  def toRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container(data: ISZ[U8]): Either[Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container, MessagePack.ErrorMsg] = {
    def fRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container(reader: Reader): Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container = {
      val r = reader.readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container()
      return r
    }
    val r = to(data, fRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container _)
    return r
  }

  def fromRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P(o: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P(o)
    return w.result
  }

  def toRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P(data: ISZ[U8]): Either[Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P, MessagePack.ErrorMsg] = {
    def fRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P(reader: Reader): Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P = {
      val r = reader.readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P()
      return r
    }
    val r = to(data, fRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P _)
    return r
  }

  def fromRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS(o: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS(o)
    return w.result
  }

  def toRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS(data: ISZ[U8]): Either[Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS, MessagePack.ErrorMsg] = {
    def fRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS(reader: Reader): Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS = {
      val r = reader.readRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS()
      return r
    }
    val r = to(data, fRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS _)
    return r
  }

  def fromMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container(o: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container(o)
    return w.result
  }

  def toMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container(data: ISZ[U8]): Either[Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container, MessagePack.ErrorMsg] = {
    def fMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container(reader: Reader): Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container = {
      val r = reader.readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container()
      return r
    }
    val r = to(data, fMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container _)
    return r
  }

  def fromMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P(o: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P(o)
    return w.result
  }

  def toMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P(data: ISZ[U8]): Either[Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P, MessagePack.ErrorMsg] = {
    def fMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P(reader: Reader): Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P = {
      val r = reader.readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P()
      return r
    }
    val r = to(data, fMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P _)
    return r
  }

  def fromMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS(o: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS(o)
    return w.result
  }

  def toMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS(data: ISZ[U8]): Either[Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS, MessagePack.ErrorMsg] = {
    def fMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS(reader: Reader): Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS = {
      val r = reader.readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS()
      return r
    }
    val r = to(data, fMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS _)
    return r
  }

  def fromMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container(o: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container(o)
    return w.result
  }

  def toMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container(data: ISZ[U8]): Either[Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container, MessagePack.ErrorMsg] = {
    def fMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container(reader: Reader): Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container = {
      val r = reader.readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container()
      return r
    }
    val r = to(data, fMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container _)
    return r
  }

  def fromMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P(o: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P(o)
    return w.result
  }

  def toMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P(data: ISZ[U8]): Either[Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P, MessagePack.ErrorMsg] = {
    def fMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P(reader: Reader): Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P = {
      val r = reader.readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P()
      return r
    }
    val r = to(data, fMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P _)
    return r
  }

  def fromMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS(o: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS(o)
    return w.result
  }

  def toMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS(data: ISZ[U8]): Either[Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS, MessagePack.ErrorMsg] = {
    def fMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS(reader: Reader): Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS = {
      val r = reader.readMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS()
      return r
    }
    val r = to(data, fMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS _)
    return r
  }

  def fromMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container(o: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container(o)
    return w.result
  }

  def toMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container(data: ISZ[U8]): Either[Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container, MessagePack.ErrorMsg] = {
    def fMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container(reader: Reader): Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container = {
      val r = reader.readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container()
      return r
    }
    val r = to(data, fMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container _)
    return r
  }

  def fromMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P(o: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P(o)
    return w.result
  }

  def toMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P(data: ISZ[U8]): Either[Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P, MessagePack.ErrorMsg] = {
    def fMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P(reader: Reader): Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P = {
      val r = reader.readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P()
      return r
    }
    val r = to(data, fMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P _)
    return r
  }

  def fromMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS(o: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS(o)
    return w.result
  }

  def toMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS(data: ISZ[U8]): Either[Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS, MessagePack.ErrorMsg] = {
    def fMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS(reader: Reader): Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS = {
      val r = reader.readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS()
      return r
    }
    val r = to(data, fMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS _)
    return r
  }

  def fromMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container(o: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container(o)
    return w.result
  }

  def toMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container(data: ISZ[U8]): Either[Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container, MessagePack.ErrorMsg] = {
    def fMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container(reader: Reader): Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container = {
      val r = reader.readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container()
      return r
    }
    val r = to(data, fMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container _)
    return r
  }

  def fromMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P(o: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P(o)
    return w.result
  }

  def toMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P(data: ISZ[U8]): Either[Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P, MessagePack.ErrorMsg] = {
    def fMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P(reader: Reader): Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P = {
      val r = reader.readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P()
      return r
    }
    val r = to(data, fMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P _)
    return r
  }

  def fromMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS(o: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS(o)
    return w.result
  }

  def toMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS(data: ISZ[U8]): Either[Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS, MessagePack.ErrorMsg] = {
    def fMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS(reader: Reader): Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS = {
      val r = reader.readMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS()
      return r
    }
    val r = to(data, fMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS _)
    return r
  }

  def fromMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container(o: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container(o)
    return w.result
  }

  def toMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container(data: ISZ[U8]): Either[Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container, MessagePack.ErrorMsg] = {
    def fMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container(reader: Reader): Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container = {
      val r = reader.readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container()
      return r
    }
    val r = to(data, fMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container _)
    return r
  }

  def fromMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P(o: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P(o)
    return w.result
  }

  def toMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P(data: ISZ[U8]): Either[Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P, MessagePack.ErrorMsg] = {
    def fMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P(reader: Reader): Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P = {
      val r = reader.readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P()
      return r
    }
    val r = to(data, fMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P _)
    return r
  }

  def fromMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS(o: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS(o)
    return w.result
  }

  def toMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS(data: ISZ[U8]): Either[Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS, MessagePack.ErrorMsg] = {
    def fMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS(reader: Reader): Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS = {
      val r = reader.readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS()
      return r
    }
    val r = to(data, fMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS _)
    return r
  }

  def fromMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container(o: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container(o)
    return w.result
  }

  def toMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container(data: ISZ[U8]): Either[Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container, MessagePack.ErrorMsg] = {
    def fMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container(reader: Reader): Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container = {
      val r = reader.readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container()
      return r
    }
    val r = to(data, fMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container _)
    return r
  }

  def fromMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P(o: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P(o)
    return w.result
  }

  def toMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P(data: ISZ[U8]): Either[Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P, MessagePack.ErrorMsg] = {
    def fMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P(reader: Reader): Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P = {
      val r = reader.readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P()
      return r
    }
    val r = to(data, fMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P _)
    return r
  }

  def fromMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS(o: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS(o)
    return w.result
  }

  def toMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS(data: ISZ[U8]): Either[Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS, MessagePack.ErrorMsg] = {
    def fMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS(reader: Reader): Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS = {
      val r = reader.readMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS()
      return r
    }
    val r = to(data, fMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS _)
    return r
  }

  def fromMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container(o: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container(o)
    return w.result
  }

  def toMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container(data: ISZ[U8]): Either[Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container, MessagePack.ErrorMsg] = {
    def fMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container(reader: Reader): Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container = {
      val r = reader.readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container()
      return r
    }
    val r = to(data, fMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container _)
    return r
  }

  def fromMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_P(o: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_P, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_P(o)
    return w.result
  }

  def toMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_P(data: ISZ[U8]): Either[Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_P, MessagePack.ErrorMsg] = {
    def fMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_P(reader: Reader): Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_P = {
      val r = reader.readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_P()
      return r
    }
    val r = to(data, fMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_P _)
    return r
  }

  def fromMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PS(o: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PS, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PS(o)
    return w.result
  }

  def toMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PS(data: ISZ[U8]): Either[Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PS, MessagePack.ErrorMsg] = {
    def fMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PS(reader: Reader): Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PS = {
      val r = reader.readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PS()
      return r
    }
    val r = to(data, fMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PS _)
    return r
  }

  def fromMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container(o: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container(o)
    return w.result
  }

  def toMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container(data: ISZ[U8]): Either[Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container, MessagePack.ErrorMsg] = {
    def fMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container(reader: Reader): Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container = {
      val r = reader.readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container()
      return r
    }
    val r = to(data, fMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container _)
    return r
  }

  def fromMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_P(o: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_P, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_P(o)
    return w.result
  }

  def toMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_P(data: ISZ[U8]): Either[Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_P, MessagePack.ErrorMsg] = {
    def fMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_P(reader: Reader): Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_P = {
      val r = reader.readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_P()
      return r
    }
    val r = to(data, fMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_P _)
    return r
  }

  def fromMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PS(o: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PS, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PS(o)
    return w.result
  }

  def toMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PS(data: ISZ[U8]): Either[Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PS, MessagePack.ErrorMsg] = {
    def fMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PS(reader: Reader): Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PS = {
      val r = reader.readMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PS()
      return r
    }
    val r = to(data, fMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PS _)
    return r
  }

  def fromOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container(o: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container(o)
    return w.result
  }

  def toOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container(data: ISZ[U8]): Either[Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container, MessagePack.ErrorMsg] = {
    def fOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container(reader: Reader): Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container = {
      val r = reader.readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container()
      return r
    }
    val r = to(data, fOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container _)
    return r
  }

  def fromOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P(o: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P(o)
    return w.result
  }

  def toOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P(data: ISZ[U8]): Either[Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P, MessagePack.ErrorMsg] = {
    def fOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P(reader: Reader): Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P = {
      val r = reader.readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P()
      return r
    }
    val r = to(data, fOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P _)
    return r
  }

  def fromOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS(o: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS(o)
    return w.result
  }

  def toOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS(data: ISZ[U8]): Either[Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS, MessagePack.ErrorMsg] = {
    def fOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS(reader: Reader): Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS = {
      val r = reader.readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS()
      return r
    }
    val r = to(data, fOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS _)
    return r
  }

  def fromOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container(o: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container(o)
    return w.result
  }

  def toOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container(data: ISZ[U8]): Either[Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container, MessagePack.ErrorMsg] = {
    def fOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container(reader: Reader): Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container = {
      val r = reader.readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container()
      return r
    }
    val r = to(data, fOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container _)
    return r
  }

  def fromOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P(o: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P(o)
    return w.result
  }

  def toOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P(data: ISZ[U8]): Either[Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P, MessagePack.ErrorMsg] = {
    def fOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P(reader: Reader): Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P = {
      val r = reader.readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P()
      return r
    }
    val r = to(data, fOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P _)
    return r
  }

  def fromOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS(o: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS(o)
    return w.result
  }

  def toOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS(data: ISZ[U8]): Either[Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS, MessagePack.ErrorMsg] = {
    def fOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS(reader: Reader): Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS = {
      val r = reader.readOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS()
      return r
    }
    val r = to(data, fOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS _)
    return r
  }

  def fromDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container(o: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container(o)
    return w.result
  }

  def toDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container(data: ISZ[U8]): Either[Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container, MessagePack.ErrorMsg] = {
    def fDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container(reader: Reader): Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container = {
      val r = reader.readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container()
      return r
    }
    val r = to(data, fDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container _)
    return r
  }

  def fromDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_P(o: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_P, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_P(o)
    return w.result
  }

  def toDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_P(data: ISZ[U8]): Either[Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_P, MessagePack.ErrorMsg] = {
    def fDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_P(reader: Reader): Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_P = {
      val r = reader.readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_P()
      return r
    }
    val r = to(data, fDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_P _)
    return r
  }

  def fromDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PS(o: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PS, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PS(o)
    return w.result
  }

  def toDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PS(data: ISZ[U8]): Either[Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PS, MessagePack.ErrorMsg] = {
    def fDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PS(reader: Reader): Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PS = {
      val r = reader.readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PS()
      return r
    }
    val r = to(data, fDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PS _)
    return r
  }

  def fromDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container(o: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container(o)
    return w.result
  }

  def toDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container(data: ISZ[U8]): Either[Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container, MessagePack.ErrorMsg] = {
    def fDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container(reader: Reader): Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container = {
      val r = reader.readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container()
      return r
    }
    val r = to(data, fDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container _)
    return r
  }

  def fromDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_P(o: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_P, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_P(o)
    return w.result
  }

  def toDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_P(data: ISZ[U8]): Either[Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_P, MessagePack.ErrorMsg] = {
    def fDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_P(reader: Reader): Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_P = {
      val r = reader.readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_P()
      return r
    }
    val r = to(data, fDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_P _)
    return r
  }

  def fromDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PS(o: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PS, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PS(o)
    return w.result
  }

  def toDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PS(data: ISZ[U8]): Either[Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PS, MessagePack.ErrorMsg] = {
    def fDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PS(reader: Reader): Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PS = {
      val r = reader.readDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PS()
      return r
    }
    val r = to(data, fDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PS _)
    return r
  }

  def fromDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container(o: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container(o)
    return w.result
  }

  def toDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container(data: ISZ[U8]): Either[Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container, MessagePack.ErrorMsg] = {
    def fDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container(reader: Reader): Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container = {
      val r = reader.readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container()
      return r
    }
    val r = to(data, fDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container _)
    return r
  }

  def fromDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_P(o: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container_P, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_P(o)
    return w.result
  }

  def toDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_P(data: ISZ[U8]): Either[Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container_P, MessagePack.ErrorMsg] = {
    def fDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_P(reader: Reader): Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container_P = {
      val r = reader.readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_P()
      return r
    }
    val r = to(data, fDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_P _)
    return r
  }

  def fromDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PS(o: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PS, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PS(o)
    return w.result
  }

  def toDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PS(data: ISZ[U8]): Either[Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PS, MessagePack.ErrorMsg] = {
    def fDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PS(reader: Reader): Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PS = {
      val r = reader.readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PS()
      return r
    }
    val r = to(data, fDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PS _)
    return r
  }

  def fromDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container(o: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container(o)
    return w.result
  }

  def toDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container(data: ISZ[U8]): Either[Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container, MessagePack.ErrorMsg] = {
    def fDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container(reader: Reader): Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container = {
      val r = reader.readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container()
      return r
    }
    val r = to(data, fDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container _)
    return r
  }

  def fromDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_P(o: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container_P, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_P(o)
    return w.result
  }

  def toDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_P(data: ISZ[U8]): Either[Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container_P, MessagePack.ErrorMsg] = {
    def fDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_P(reader: Reader): Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container_P = {
      val r = reader.readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_P()
      return r
    }
    val r = to(data, fDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_P _)
    return r
  }

  def fromDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PS(o: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PS, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PS(o)
    return w.result
  }

  def toDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PS(data: ISZ[U8]): Either[Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PS, MessagePack.ErrorMsg] = {
    def fDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PS(reader: Reader): Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PS = {
      val r = reader.readDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PS()
      return r
    }
    val r = to(data, fDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PS _)
    return r
  }

  def fromutilContainer(o: util.Container, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeutilContainer(o)
    return w.result
  }

  def toutilContainer(data: ISZ[U8]): Either[util.Container, MessagePack.ErrorMsg] = {
    def futilContainer(reader: Reader): util.Container = {
      val r = reader.readutilContainer()
      return r
    }
    val r = to(data, futilContainer _)
    return r
  }

  def fromutilEmptyContainer(o: util.EmptyContainer, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.writeutilEmptyContainer(o)
    return w.result
  }

  def toutilEmptyContainer(data: ISZ[U8]): Either[util.EmptyContainer, MessagePack.ErrorMsg] = {
    def futilEmptyContainer(reader: Reader): util.EmptyContainer = {
      val r = reader.readutilEmptyContainer()
      return r
    }
    val r = to(data, futilEmptyContainer _)
    return r
  }

  def from_artDataContent(o: art.DataContent, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.write_artDataContent(o)
    return w.result
  }

  def to_artDataContent(data: ISZ[U8]): Either[art.DataContent, MessagePack.ErrorMsg] = {
    def f_artDataContent(reader: Reader): art.DataContent = {
      val r = reader.read_artDataContent()
      return r
    }
    val r = to(data, f_artDataContent _)
    return r
  }

  def from_artEmpty(o: art.Empty, pooling: B): ISZ[U8] = {
    val w = Writer.Default(MessagePack.writer(pooling))
    w.write_artEmpty(o)
    return w.result
  }

  def to_artEmpty(data: ISZ[U8]): Either[art.Empty, MessagePack.ErrorMsg] = {
    def f_artEmpty(reader: Reader): art.Empty = {
      val r = reader.read_artEmpty()
      return r
    }
    val r = to(data, f_artEmpty _)
    return r
  }

}