// #Sireum

package isolette.Regulate

import org.sireum._
import art._
import isolette.SystemTestSuiteSlang.runtimeMonitorStream
import isolette._

// Do not edit this file as it will be overwritten if HAMR codegen is rerun

object Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_SystemTestAPI {
  /** helper method to set the values of all incoming ports and state variables
    * @param In_lastRegulatorMode pre-state state variable
    * @param api_current_tempWstatus incoming data port
    * @param api_interface_failure incoming data port
    * @param api_internal_failure incoming data port
    */
  def put_concrete_inputs(In_lastRegulatorMode: Isolette_Data_Model.Regulator_Mode.Type,
                          api_current_tempWstatus: Isolette_Data_Model.TempWstatus_i,
                          api_interface_failure: Isolette_Data_Model.Failure_Flag_i,
                          api_internal_failure: Isolette_Data_Model.Failure_Flag_i): Unit = {
    put_In_lastRegulatorMode(In_lastRegulatorMode)
    put_current_tempWstatus(api_current_tempWstatus)
    put_interface_failure(api_interface_failure)
    put_internal_failure(api_internal_failure)
  }

  // setter for state variable
  def put_In_lastRegulatorMode(value: Isolette_Data_Model.Regulator_Mode.Type): Unit = {
    Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm.lastRegulatorMode = value
  }

  // setter for incoming data port
  def put_current_tempWstatus(value: Isolette_Data_Model.TempWstatus_i): Unit = {
    Art.insertInInfrastructurePort(Arch.Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm.operational_api.current_tempWstatus_Id, Isolette_Data_Model.TempWstatus_i_Payload(value))
  }

  // setter for incoming data port
  def put_interface_failure(value: Isolette_Data_Model.Failure_Flag_i): Unit = {
    Art.insertInInfrastructurePort(Arch.Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm.operational_api.interface_failure_Id, Isolette_Data_Model.Failure_Flag_i_Payload(value))
  }

  // setter for incoming data port
  def put_internal_failure(value: Isolette_Data_Model.Failure_Flag_i): Unit = {
    Art.insertInInfrastructurePort(Arch.Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm.operational_api.internal_failure_Id, Isolette_Data_Model.Failure_Flag_i_Payload(value))
  }

  def fetchContainer(): isolette.Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS = {
    if (runtimeMonitorStream.contains(Arch.Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm.id)) {
      val (_, postContainer_) = runtimeMonitorStream.get(Arch.Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm.id).get
      return postContainer_.asInstanceOf[isolette.Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS]
    }
    else {
      assert(F, s"No post state recorded for ${Arch.Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm.name}")
      halt(s"No post state recorded for ${Arch.Isolette_Single_Sensor_Instance_thermostat_rt_mrm_mrm.name}")
    }
  }

  def check_concrete_outputs(lastRegulatorMode: Isolette_Data_Model.Regulator_Mode.Type,
                             api_regulator_mode: Isolette_Data_Model.Regulator_Mode.Type): Unit = {
    var failureReasons: ISZ[ST] = ISZ()

    val actual_lastRegulatorMode = get_lastRegulatorMode()
    if (lastRegulatorMode != actual_lastRegulatorMode) {
      failureReasons = failureReasons :+ st"'lastRegulatorMode' did not match expected.  Expected: $lastRegulatorMode, Actual: $actual_lastRegulatorMode"
    }
    val actual_regulator_mode = get_api_regulator_mode()
    if (api_regulator_mode != actual_regulator_mode) {
      failureReasons = failureReasons :+ st"'regulator_mode' did not match expected.  Expected: $api_regulator_mode, Actual: $actual_regulator_mode"
    }

    assert(failureReasons.isEmpty, st"${(failureReasons, "\n")}".render)
  }

  // getter for state variable
  def get_lastRegulatorMode(): Isolette_Data_Model.Regulator_Mode.Type = {
    return Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm.lastRegulatorMode
  }

  def get_api_regulator_mode(): Isolette_Data_Model.Regulator_Mode.Type = {
    return fetchContainer().api_regulator_mode
  }
}