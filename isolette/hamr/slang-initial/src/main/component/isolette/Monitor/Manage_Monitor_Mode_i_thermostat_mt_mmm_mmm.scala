// #Sireum

package isolette.Monitor

import org.sireum._
import isolette._

// This file will not be overwritten so is safe to edit
object Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm {

  // BEGIN STATE VARS
  var lastMonitorMode: Isolette_Data_Model.Monitor_Mode.Type = Isolette_Data_Model.Monitor_Mode.byOrdinal(0).get
  // END STATE VARS

  // BEGIN FUNCTIONS
  @strictpure def timeout_condition_satisfied(): Base_Types.Boolean = F
  // END FUNCTIONS

  def initialise(api: Manage_Monitor_Mode_i_Initialization_Api): Unit = {
    Contract(
      Ensures(
        // BEGIN INITIALIZES ENSURES
        // guarantee REQ_MMM_1
        //   Upon the first dispatch of the thread, the monitor mode is Init.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=114 
        api.monitor_mode == Isolette_Data_Model.Monitor_Mode.Init_Monitor_Mode
        // END INITIALIZES ENSURES
      )
    )
    // example api usage

    api.logInfo("Example info logging")
    api.logDebug("Example debug logging")
    api.logError("Example error logging")

    api.put_monitor_mode(Isolette_Data_Model.Monitor_Mode.byOrdinal(0).get)
  }

  def timeTriggered(api: Manage_Monitor_Mode_i_Operational_Api): Unit = {
    Contract(
      Ensures(
        // BEGIN COMPUTE ENSURES timeTriggered
        // case REQ_MMM_2
        //   If the current mode is Init, then
        //   the mode is set to NORMAL iff the monitor status is true (valid) (see Table A-15), i.e.,
        //   if  NOT (Monitor Interface Failure OR Monitor Internal Failure)
        //   AND Current Temperature.Status = Valid
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=114 
        (In(lastMonitorMode) == Isolette_Data_Model.Monitor_Mode.Init_Monitor_Mode) -->: ((!(api.interface_failure.flag || api.internal_failure.flag) &&
           api.current_tempWstatus.status == Isolette_Data_Model.ValueStatus.Valid) -->:
          (api.monitor_mode == Isolette_Data_Model.Monitor_Mode.Normal_Monitor_Mode)),
        // case REQ_MMM_3
        //   If the current Monitor mode is Normal, then
        //   the Monitor mode is set to Failed iff
        //   the Monitor status is false, i.e.,
        //   if  (Monitor Interface Failure OR Monitor Internal Failure)
        //   OR NOT(Current Temperature.Status = Valid)
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=114 
        (In(lastMonitorMode) == Isolette_Data_Model.Monitor_Mode.Normal_Monitor_Mode) -->: ((api.interface_failure.flag || api.internal_failure.flag ||
           api.current_tempWstatus.status != Isolette_Data_Model.ValueStatus.Valid) -->:
          (api.monitor_mode == Isolette_Data_Model.Monitor_Mode.Failed_Monitor_Mode)),
        // case REQ_MMM_4
        //   If the current mode is Init, then
        //   the mode is set to Failed iff the time during
        //   which the thread has been in Init mode exceeds the
        //   Monitor Init Timeout value.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=114 
        (In(lastMonitorMode) == Isolette_Data_Model.Monitor_Mode.Init_Monitor_Mode) -->: (Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm.timeout_condition_satisfied() == (api.monitor_mode == Isolette_Data_Model.Monitor_Mode.Failed_Monitor_Mode))
        // END COMPUTE ENSURES timeTriggered
      )
    )
    // example api usage

    val apiUsage_current_tempWstatus: Option[Isolette_Data_Model.TempWstatus_i] = api.get_current_tempWstatus()
    api.logInfo(s"Received on data port current_tempWstatus: ${apiUsage_current_tempWstatus}")
    val apiUsage_interface_failure: Option[Isolette_Data_Model.Failure_Flag_i] = api.get_interface_failure()
    api.logInfo(s"Received on data port interface_failure: ${apiUsage_interface_failure}")
    val apiUsage_internal_failure: Option[Isolette_Data_Model.Failure_Flag_i] = api.get_internal_failure()
    api.logInfo(s"Received on data port internal_failure: ${apiUsage_internal_failure}")
  }

  def finalise(api: Manage_Monitor_Mode_i_Operational_Api): Unit = { }
}
