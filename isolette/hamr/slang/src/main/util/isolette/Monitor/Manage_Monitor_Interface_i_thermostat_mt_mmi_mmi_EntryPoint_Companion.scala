// #Sireum

package isolette.Monitor

import org.sireum._
import art._
import isolette._

// Do not edit this file as it will be overwritten if HAMR codegen is rerun

object Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_EntryPoint_Companion {

  var last_api_upper_alarm_temp: Option[Isolette_Data_Model.Temp_i] = None()

  /** get the value of outgoing data port upper_alarm_temp.  If a 'fresh' value wasn't sent
    * during the last dispatch then return last_api_upper_alarm_temp.get.
    * Note: this requires outgoing data ports to be initialized during the
    * initialization phase or prior to system testing.
    */
  def get_api_upper_alarm_temp: Isolette_Data_Model.Temp_i = {
    Art.observeOutPortVariable(Arch.Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi.operational_api.upper_alarm_temp_Id) match {
      case Some(Isolette_Data_Model.Temp_i_Payload(value)) =>
        last_api_upper_alarm_temp = Some(value)
        return value
      case _ if last_api_upper_alarm_temp.isEmpty =>
        assert(F, "No value found on outgoing data port upper_alarm_temp.\n                  Note: values placed during the initialization phase will persist across dispatches")
        halt("No value found on outgoing data port upper_alarm_temp.\n                  Note: values placed during the initialization phase will persist across dispatches")
      case _ => return last_api_upper_alarm_temp.get
    }
  }
  var last_api_lower_alarm_temp: Option[Isolette_Data_Model.Temp_i] = None()

  /** get the value of outgoing data port lower_alarm_temp.  If a 'fresh' value wasn't sent
    * during the last dispatch then return last_api_lower_alarm_temp.get.
    * Note: this requires outgoing data ports to be initialized during the
    * initialization phase or prior to system testing.
    */
  def get_api_lower_alarm_temp: Isolette_Data_Model.Temp_i = {
    Art.observeOutPortVariable(Arch.Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi.operational_api.lower_alarm_temp_Id) match {
      case Some(Isolette_Data_Model.Temp_i_Payload(value)) =>
        last_api_lower_alarm_temp = Some(value)
        return value
      case _ if last_api_lower_alarm_temp.isEmpty =>
        assert(F, "No value found on outgoing data port lower_alarm_temp.\n                  Note: values placed during the initialization phase will persist across dispatches")
        halt("No value found on outgoing data port lower_alarm_temp.\n                  Note: values placed during the initialization phase will persist across dispatches")
      case _ => return last_api_lower_alarm_temp.get
    }
  }
  var last_api_monitor_status: Option[Isolette_Data_Model.Status.Type] = None()

  /** get the value of outgoing data port monitor_status.  If a 'fresh' value wasn't sent
    * during the last dispatch then return last_api_monitor_status.get.
    * Note: this requires outgoing data ports to be initialized during the
    * initialization phase or prior to system testing.
    */
  def get_api_monitor_status: Isolette_Data_Model.Status.Type = {
    Art.observeOutPortVariable(Arch.Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi.operational_api.monitor_status_Id) match {
      case Some(Isolette_Data_Model.Status_Payload(value)) =>
        last_api_monitor_status = Some(value)
        return value
      case _ if last_api_monitor_status.isEmpty =>
        assert(F, "No value found on outgoing data port monitor_status.\n                  Note: values placed during the initialization phase will persist across dispatches")
        halt("No value found on outgoing data port monitor_status.\n                  Note: values placed during the initialization phase will persist across dispatches")
      case _ => return last_api_monitor_status.get
    }
  }
  var last_api_interface_failure: Option[Isolette_Data_Model.Failure_Flag_i] = None()

  /** get the value of outgoing data port interface_failure.  If a 'fresh' value wasn't sent
    * during the last dispatch then return last_api_interface_failure.get.
    * Note: this requires outgoing data ports to be initialized during the
    * initialization phase or prior to system testing.
    */
  def get_api_interface_failure: Isolette_Data_Model.Failure_Flag_i = {
    Art.observeOutPortVariable(Arch.Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi.operational_api.interface_failure_Id) match {
      case Some(Isolette_Data_Model.Failure_Flag_i_Payload(value)) =>
        last_api_interface_failure = Some(value)
        return value
      case _ if last_api_interface_failure.isEmpty =>
        assert(F, "No value found on outgoing data port interface_failure.\n                  Note: values placed during the initialization phase will persist across dispatches")
        halt("No value found on outgoing data port interface_failure.\n                  Note: values placed during the initialization phase will persist across dispatches")
      case _ => return last_api_interface_failure.get
    }
  }
  var preStateContainer_wL: Option[Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS] = None()

  def pre_initialise(): Unit = {
    // assume/require contracts cannot refer to incoming ports or
    // state variables so nothing to do here
  }

  def post_initialise(): Unit = {
    // block the component while its post-state values are retrieved
    val postStateContainer_wL =
      Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS(
        lastCmd = isolette.Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi.lastCmd,
        api_interface_failure = get_api_interface_failure,
        api_lower_alarm_temp = get_api_lower_alarm_temp,
        api_monitor_status = get_api_monitor_status,
        api_upper_alarm_temp = get_api_upper_alarm_temp)

    // the rest can now be performed via a different thread
    isolette.runtimemonitor.RuntimeMonitor.observeInitialisePostState(Arch.Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi.id, isolette.runtimemonitor.ObservationKind.Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_postInit, postStateContainer_wL)
  }

  def pre_compute(): Unit = {
    // block the component while its pre-state values are retrieved
    preStateContainer_wL = Some(
      Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS(
        In_lastCmd = isolette.Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi.lastCmd, 
        api_current_tempWstatus = Art.observeInPortVariable(Arch.Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi.operational_api.current_tempWstatus_Id).get.asInstanceOf[Isolette_Data_Model.TempWstatus_i_Payload].value, 
        api_lower_alarm_tempWstatus = Art.observeInPortVariable(Arch.Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi.operational_api.lower_alarm_tempWstatus_Id).get.asInstanceOf[Isolette_Data_Model.TempWstatus_i_Payload].value, 
        api_monitor_mode = Art.observeInPortVariable(Arch.Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi.operational_api.monitor_mode_Id).get.asInstanceOf[Isolette_Data_Model.Monitor_Mode_Payload].value, 
        api_upper_alarm_tempWstatus = Art.observeInPortVariable(Arch.Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi.operational_api.upper_alarm_tempWstatus_Id).get.asInstanceOf[Isolette_Data_Model.TempWstatus_i_Payload].value))

    // the rest can now be performed via a different thread
    isolette.runtimemonitor.RuntimeMonitor.observeComputePreState(Arch.Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi.id, isolette.runtimemonitor.ObservationKind.Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_preCompute, preStateContainer_wL.asInstanceOf[Option[art.DataContent]])
  }

  def post_compute(): Unit = {
    // block the component while its post-state values are retrieved
    val postStateContainer_wL =
      Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS(
        lastCmd = isolette.Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi.lastCmd,
        api_interface_failure = get_api_interface_failure,
        api_lower_alarm_temp = get_api_lower_alarm_temp,
        api_monitor_status = get_api_monitor_status,
        api_upper_alarm_temp = get_api_upper_alarm_temp)

    // the rest can now be performed via a different thread
    isolette.runtimemonitor.RuntimeMonitor.observeComputePrePostState(Arch.Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi.id, isolette.runtimemonitor.ObservationKind.Isolette_Single_Sensor_Instance_thermostat_mt_mmi_mmi_postCompute, preStateContainer_wL.asInstanceOf[Option[art.DataContent]], postStateContainer_wL)
  }
}