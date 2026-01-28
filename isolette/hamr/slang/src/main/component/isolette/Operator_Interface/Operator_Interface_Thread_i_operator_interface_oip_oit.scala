// #Sireum

package isolette.Operator_Interface

import org.sireum._
import isolette._
import isolette.Operator_Interface.UserInterface
import isolette.Operator_Interface.UserInterface.Interface

// This file will not be overwritten so is safe to edit
object Operator_Interface_Thread_i_operator_interface_oip_oit {

  // BEGIN FUNCTIONS
  @strictpure def Allowed_UpperAlarmTempWStatus(upper: Isolette_Data_Model.TempWstatus_i): Base_Types.Boolean = GUMBO_Library.GUMBO__Library.Allowed_UpperAlarmTempWStatus(upper)
  // END FUNCTIONS

  // Define initial values here because they are used in both
  val initLowerDesiredTempWstatus: Isolette_Data_Model.TempWstatus_i = Isolette_Data_Model.TempWstatus_i(InitialValues.DEFAULT_LOWER_DESIRED_TEMPERATURE, Isolette_Data_Model.ValueStatus.Valid)
  val initUpperDesiredTempWstatus: Isolette_Data_Model.TempWstatus_i = Isolette_Data_Model.TempWstatus_i(InitialValues.DEFAULT_UPPER_DESIRED_TEMPERATURE, Isolette_Data_Model.ValueStatus.Valid)
  val initLowerAlarmTempWstatus: Isolette_Data_Model.TempWstatus_i = Isolette_Data_Model.TempWstatus_i(InitialValues.DEFAULT_LOWER_ALARM_TEMPERATURE, Isolette_Data_Model.ValueStatus.Valid)
  val initUpperAlarmTempWstatus: Isolette_Data_Model.TempWstatus_i = Isolette_Data_Model.TempWstatus_i(InitialValues.DEFAULT_UPPER_ALARM_TEMPERATURE, Isolette_Data_Model.ValueStatus.Valid)

  // Set values for "previous state" based on default values in InitialValues system configuration file
  var lastLowerDesiredTempWstatus: Isolette_Data_Model.TempWstatus_i = Isolette_Data_Model.TempWstatus_i(InitialValues.DEFAULT_LOWER_DESIRED_TEMPERATURE, Isolette_Data_Model.ValueStatus.Valid)
  var lastUpperDesiredTempWstatus: Isolette_Data_Model.TempWstatus_i = Isolette_Data_Model.TempWstatus_i(InitialValues.DEFAULT_UPPER_DESIRED_TEMPERATURE, Isolette_Data_Model.ValueStatus.Valid)
  var lastLowerAlarmTempWstatus: Isolette_Data_Model.TempWstatus_i = Isolette_Data_Model.TempWstatus_i(InitialValues.DEFAULT_LOWER_ALARM_TEMPERATURE, Isolette_Data_Model.ValueStatus.Valid)
  var lastUpperAlarmTempWstatus: Isolette_Data_Model.TempWstatus_i = Isolette_Data_Model.TempWstatus_i(InitialValues.DEFAULT_UPPER_ALARM_TEMPERATURE, Isolette_Data_Model.ValueStatus.Valid)

  var firstInvocation: B = T

  def initialise(api: Operator_Interface_Thread_i_Initialization_Api): Unit = {
    api.put_lower_desired_tempWstatus(initLowerDesiredTempWstatus)
    api.put_upper_desired_tempWstatus(initUpperDesiredTempWstatus)
    api.put_lower_alarm_tempWstatus(initLowerAlarmTempWstatus)
    api.put_upper_alarm_tempWstatus(initUpperAlarmTempWstatus)
  }

  def timeTriggered(api: Operator_Interface_Thread_i_Operational_Api): Unit = {
    Contract(
      Ensures(
        // BEGIN COMPUTE ENSURES timeTriggered
        // guarantee Allowed_AlarmTempWStatus_Ranges
        //   An integration constraint can only refer to a single port, so need a general requires
        //   clause to relate the lower and upper temps
        GUMBO_Library.GUMBO__Library.Allowed_AlarmTempWStatus_Ranges(api.lower_alarm_tempWstatus, api.upper_alarm_tempWstatus)
        // END COMPUTE ENSURES timeTriggered
      )
    )
    if (firstInvocation) {
      Interface.initialise(
        lastLowerDesiredTempWstatus, lastUpperDesiredTempWstatus,
        lastLowerAlarmTempWstatus, lastUpperAlarmTempWstatus)
      firstInvocation = F
    }

    // send to interface
    Interface.setRegulatorStatus(api.get_regulator_status())

    Interface.setMonitorStatus(api.get_monitor_status())

    Interface.setDispayTemperature(api.get_display_temperature())

    Interface.setAlarmControl(api.get_alarm_control())


    // fetch from interface
    val ldt = Interface.getLowerDesiredTempWstatus()
    if (ldt != lastLowerDesiredTempWstatus) {
      api.put_lower_desired_tempWstatus(ldt)
      lastLowerDesiredTempWstatus = ldt
      api.logInfo(s"Sent lower desired temp: ${lastLowerDesiredTempWstatus}")
    }

    val udt = Interface.getUpperDesiredTempWstatus()
    if (udt != lastUpperDesiredTempWstatus) {
      api.put_upper_desired_tempWstatus(udt)
      lastUpperDesiredTempWstatus = udt
      api.logInfo(s"Sent upper desired temp: ${lastUpperDesiredTempWstatus}")
    }

    val lat = Interface.getLowerAlarmTempWstatus()
    if (lat != lastLowerAlarmTempWstatus) {
      api.put_lower_alarm_tempWstatus(lat)
      lastLowerAlarmTempWstatus = lat
      api.logInfo(s"Sent lower alarm temp: ${lastLowerAlarmTempWstatus}")
    }

    val uat = Interface.getUpperAlarmTempWstatus()
    if (uat != lastUpperAlarmTempWstatus) {
      api.put_upper_alarm_tempWstatus(uat)
      lastUpperAlarmTempWstatus = uat
      api.logInfo(s"Sent upper alarm temp: ${lastUpperAlarmTempWstatus}")
    }
  }

  def finalise(api: Operator_Interface_Thread_i_Operational_Api): Unit = { }
}
