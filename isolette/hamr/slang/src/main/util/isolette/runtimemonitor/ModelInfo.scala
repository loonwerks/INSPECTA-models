// #Sireum
package isolette.runtimemonitor

import org.sireum._
import isolette._

// Do not edit this file as it will be overwritten if HAMR codegen is rerun

object ModelInfo {
  val Manage_Regulator_Interface_i_thermostat_rt_mri_mri_MI : Component =
    Component(
      name = "Manage_Regulator_Interface_i_thermostat_rt_mri_mri",
      id = Arch.Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri.id.toZ,
      dispatchProtocol = iDispatchProtocol.Periodic,
      state = ISZ(
        Port(
          name = "upper_desired_tempWstatus",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri.upper_desired_tempWstatus.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.In,
          slangType = ""),
        Port(
          name = "lower_desired_tempWstatus",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri.lower_desired_tempWstatus.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.In,
          slangType = ""),
        Port(
          name = "current_tempWstatus",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri.current_tempWstatus.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.In,
          slangType = ""),
        Port(
          name = "regulator_mode",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri.regulator_mode.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.In,
          slangType = ""),
        Port(
          name = "upper_desired_temp",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri.upper_desired_temp.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.Out,
          slangType = ""),
        Port(
          name = "lower_desired_temp",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri.lower_desired_temp.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.Out,
          slangType = ""),
        Port(
          name = "displayed_temp",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri.displayed_temp.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.Out,
          slangType = ""),
        Port(
          name = "regulator_status",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri.regulator_status.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.Out,
          slangType = ""),
        Port(
          name = "interface_failure",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_rt_mri_mri.interface_failure.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.Out,
          slangType = "")))

  val Manage_Heat_Source_i_thermostat_rt_mhs_mhs_MI : Component =
    Component(
      name = "Manage_Heat_Source_i_thermostat_rt_mhs_mhs",
      id = Arch.Isolette_Single_Sensor_Instance_thermostat_rt_mhs_mhs.id.toZ,
      dispatchProtocol = iDispatchProtocol.Periodic,
      state = ISZ(
        StateVariable(
          name = "In_lastCmd",
          id = 0,
          direction = StateDirection.In,
          slangType = "Isolette_Data_Model.On_Off.Type"),
        StateVariable(
          name = "lastCmd",
          id = 0,
          direction = StateDirection.Out,
          slangType = "Isolette_Data_Model.On_Off.Type"),
        Port(
          name = "current_tempWstatus",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_rt_mhs_mhs.current_tempWstatus.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.In,
          slangType = ""),
        Port(
          name = "lower_desired_temp",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_rt_mhs_mhs.lower_desired_temp.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.In,
          slangType = ""),
        Port(
          name = "upper_desired_temp",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_rt_mhs_mhs.upper_desired_temp.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.In,
          slangType = ""),
        Port(
          name = "regulator_mode",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_rt_mhs_mhs.regulator_mode.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.In,
          slangType = ""),
        Port(
          name = "heat_control",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_rt_mhs_mhs.heat_control.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.Out,
          slangType = "")))

  val Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_MI : Component =
    Component(
      name = "Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm",
      id = Arch.Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm.id.toZ,
      dispatchProtocol = iDispatchProtocol.Periodic,
      state = ISZ(
        StateVariable(
          name = "In_lastRegulatorMode",
          id = 0,
          direction = StateDirection.In,
          slangType = "Isolette_Data_Model.Regulator_Mode.Type"),
        StateVariable(
          name = "lastRegulatorMode",
          id = 0,
          direction = StateDirection.Out,
          slangType = "Isolette_Data_Model.Regulator_Mode.Type"),
        Port(
          name = "current_tempWstatus",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm.current_tempWstatus.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.In,
          slangType = ""),
        Port(
          name = "interface_failure",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm.interface_failure.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.In,
          slangType = ""),
        Port(
          name = "internal_failure",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm.internal_failure.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.In,
          slangType = ""),
        Port(
          name = "regulator_mode",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm.regulator_mode.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.Out,
          slangType = "")))

  val Detect_Regulator_Failure_i_thermostat_rt_drf_drf_MI : Component =
    Component(
      name = "Detect_Regulator_Failure_i_thermostat_rt_drf_drf",
      id = Arch.Isolette_Single_Sensor_Instance_thermostat_rt_drf_drf.id.toZ,
      dispatchProtocol = iDispatchProtocol.Periodic,
      state = ISZ(
        Port(
          name = "internal_failure",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_rt_drf_drf.internal_failure.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.Out,
          slangType = "")))

  val Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_MI : Component =
    Component(
      name = "Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi",
      id = Arch.Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi.id.toZ,
      dispatchProtocol = iDispatchProtocol.Periodic,
      state = ISZ(
        StateVariable(
          name = "In_lastCmd",
          id = 0,
          direction = StateDirection.In,
          slangType = "Isolette_Data_Model.On_Off.Type"),
        StateVariable(
          name = "lastCmd",
          id = 0,
          direction = StateDirection.Out,
          slangType = "Isolette_Data_Model.On_Off.Type"),
        Port(
          name = "upper_alarm_tempWstatus",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi.upper_alarm_tempWstatus.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.In,
          slangType = ""),
        Port(
          name = "lower_alarm_tempWstatus",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi.lower_alarm_tempWstatus.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.In,
          slangType = ""),
        Port(
          name = "current_tempWstatus",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi.current_tempWstatus.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.In,
          slangType = ""),
        Port(
          name = "monitor_mode",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi.monitor_mode.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.In,
          slangType = ""),
        Port(
          name = "upper_alarm_temp",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi.upper_alarm_temp.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.Out,
          slangType = ""),
        Port(
          name = "lower_alarm_temp",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi.lower_alarm_temp.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.Out,
          slangType = ""),
        Port(
          name = "monitor_status",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi.monitor_status.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.Out,
          slangType = ""),
        Port(
          name = "interface_failure",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi.interface_failure.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.Out,
          slangType = "")))

  val Manage_Alarm_i_thermostat_mt_ma_ma_MI : Component =
    Component(
      name = "Manage_Alarm_i_thermostat_mt_ma_ma",
      id = Arch.Isolette_Single_Sensor_Instance_thermostat_mt_ma_ma.id.toZ,
      dispatchProtocol = iDispatchProtocol.Periodic,
      state = ISZ(
        StateVariable(
          name = "In_lastCmd",
          id = 0,
          direction = StateDirection.In,
          slangType = "Isolette_Data_Model.On_Off.Type"),
        StateVariable(
          name = "lastCmd",
          id = 0,
          direction = StateDirection.Out,
          slangType = "Isolette_Data_Model.On_Off.Type"),
        Port(
          name = "current_tempWstatus",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_mt_ma_ma.current_tempWstatus.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.In,
          slangType = ""),
        Port(
          name = "lower_alarm_temp",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_mt_ma_ma.lower_alarm_temp.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.In,
          slangType = ""),
        Port(
          name = "upper_alarm_temp",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_mt_ma_ma.upper_alarm_temp.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.In,
          slangType = ""),
        Port(
          name = "monitor_mode",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_mt_ma_ma.monitor_mode.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.In,
          slangType = ""),
        Port(
          name = "alarm_control",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_mt_ma_ma.alarm_control.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.Out,
          slangType = "")))

  val Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_MI : Component =
    Component(
      name = "Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm",
      id = Arch.Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm.id.toZ,
      dispatchProtocol = iDispatchProtocol.Periodic,
      state = ISZ(
        StateVariable(
          name = "In_lastMonitorMode",
          id = 0,
          direction = StateDirection.In,
          slangType = "Isolette_Data_Model.Monitor_Mode.Type"),
        StateVariable(
          name = "lastMonitorMode",
          id = 0,
          direction = StateDirection.Out,
          slangType = "Isolette_Data_Model.Monitor_Mode.Type"),
        Port(
          name = "current_tempWstatus",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm.current_tempWstatus.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.In,
          slangType = ""),
        Port(
          name = "interface_failure",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm.interface_failure.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.In,
          slangType = ""),
        Port(
          name = "internal_failure",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm.internal_failure.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.In,
          slangType = ""),
        Port(
          name = "monitor_mode",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm.monitor_mode.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.Out,
          slangType = "")))

  val Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_MI : Component =
    Component(
      name = "Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf",
      id = Arch.Isolette_Single_Sensor_Instance_thermostat_mt_dmf_dmf.id.toZ,
      dispatchProtocol = iDispatchProtocol.Periodic,
      state = ISZ(
        Port(
          name = "internal_failure",
          id = Arch.Isolette_Single_Sensor_Instance_thermostat_mt_dmf_dmf.internal_failure.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.Out,
          slangType = "")))

  val Operator_Interface_Thread_i_operator_interface_oip_oit_MI : Component =
    Component(
      name = "Operator_Interface_Thread_i_operator_interface_oip_oit",
      id = Arch.Isolette_Single_Sensor_Instance_operator_interface_oip_oit.id.toZ,
      dispatchProtocol = iDispatchProtocol.Periodic,
      state = ISZ(
        Port(
          name = "regulator_status",
          id = Arch.Isolette_Single_Sensor_Instance_operator_interface_oip_oit.regulator_status.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.In,
          slangType = ""),
        Port(
          name = "monitor_status",
          id = Arch.Isolette_Single_Sensor_Instance_operator_interface_oip_oit.monitor_status.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.In,
          slangType = ""),
        Port(
          name = "display_temperature",
          id = Arch.Isolette_Single_Sensor_Instance_operator_interface_oip_oit.display_temperature.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.In,
          slangType = ""),
        Port(
          name = "alarm_control",
          id = Arch.Isolette_Single_Sensor_Instance_operator_interface_oip_oit.alarm_control.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.In,
          slangType = ""),
        Port(
          name = "lower_desired_tempWstatus",
          id = Arch.Isolette_Single_Sensor_Instance_operator_interface_oip_oit.lower_desired_tempWstatus.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.Out,
          slangType = ""),
        Port(
          name = "upper_desired_tempWstatus",
          id = Arch.Isolette_Single_Sensor_Instance_operator_interface_oip_oit.upper_desired_tempWstatus.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.Out,
          slangType = ""),
        Port(
          name = "lower_alarm_tempWstatus",
          id = Arch.Isolette_Single_Sensor_Instance_operator_interface_oip_oit.lower_alarm_tempWstatus.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.Out,
          slangType = ""),
        Port(
          name = "upper_alarm_tempWstatus",
          id = Arch.Isolette_Single_Sensor_Instance_operator_interface_oip_oit.upper_alarm_tempWstatus.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.Out,
          slangType = "")))

  val Temperature_Sensor_i_temperature_sensor_cpi_thermostat_MI : Component =
    Component(
      name = "Temperature_Sensor_i_temperature_sensor_cpi_thermostat",
      id = Arch.Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat.id.toZ,
      dispatchProtocol = iDispatchProtocol.Periodic,
      state = ISZ(
        Port(
          name = "air",
          id = Arch.Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat.air.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.In,
          slangType = ""),
        Port(
          name = "current_tempWstatus",
          id = Arch.Isolette_Single_Sensor_Instance_temperature_sensor_cpi_thermostat.current_tempWstatus.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.Out,
          slangType = "")))

  val Heat_Source_i_heat_source_cpi_heat_controller_MI : Component =
    Component(
      name = "Heat_Source_i_heat_source_cpi_heat_controller",
      id = Arch.Isolette_Single_Sensor_Instance_heat_source_cpi_heat_controller.id.toZ,
      dispatchProtocol = iDispatchProtocol.Periodic,
      state = ISZ(
        Port(
          name = "heat_control",
          id = Arch.Isolette_Single_Sensor_Instance_heat_source_cpi_heat_controller.heat_control.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.In,
          slangType = ""),
        Port(
          name = "heat_out",
          id = Arch.Isolette_Single_Sensor_Instance_heat_source_cpi_heat_controller.heat_out.id.toZ,
          kind = PortKind.Data,
          direction = StateDirection.Out,
          slangType = "")))

  val modelInfo: ModelInfo =
    ModelInfo(ISZ(
     Manage_Regulator_Interface_i_thermostat_rt_mri_mri_MI,
     Manage_Heat_Source_i_thermostat_rt_mhs_mhs_MI,
     Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_MI,
     Detect_Regulator_Failure_i_thermostat_rt_drf_drf_MI,
     Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_MI,
     Manage_Alarm_i_thermostat_mt_ma_ma_MI,
     Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_MI,
     Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_MI,
     Operator_Interface_Thread_i_operator_interface_oip_oit_MI,
     Temperature_Sensor_i_temperature_sensor_cpi_thermostat_MI,
     Heat_Source_i_heat_source_cpi_heat_controller_MI))
}

@datatype class ModelInfo(val components: ISZ[Component])

@datatype class Component(val name: String,
                          val id: Z,
                          val dispatchProtocol: iDispatchProtocol.Type,
                          val state: ISZ[StateElement])

@enum object iDispatchProtocol {
  "Sporadic"
  "Periodic"
}

@enum object StateDirection {
  "In"
  "Out"
}

@sig trait StateElement {
  def name: String

  def id: Z

  def slangType: String

  def direction: StateDirection.Type
}

@enum object PortKind {
  "Data"
  "Event"
  "EventData"
}

@datatype class Port(val name: String,
                     val id: Z,
                     val kind: PortKind.Type,
                     val direction: StateDirection.Type,
                     val slangType: String) extends StateElement

@datatype class StateVariable(val name: String,
                              val id: Z,
                              val direction: StateDirection.Type,
                              val slangType: String) extends StateElement