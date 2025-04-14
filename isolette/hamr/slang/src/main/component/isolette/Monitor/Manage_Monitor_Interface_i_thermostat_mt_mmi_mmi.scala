// #Sireum #Logika

package isolette.Monitor

import org.sireum._
import isolette.Isolette_Data_Model.{Failure_Flag_i, Temp_i}
import isolette._
import org.sireum.S32._

// This file will not be overwritten so is safe to edit
object Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi {

  // BEGIN STATE VARS
  var lastCmd: Isolette_Data_Model.On_Off.Type = Isolette_Data_Model.On_Off.byOrdinal(0).get
  // END STATE VARS

  // BEGIN FUNCTIONS
  @strictpure def timeout_condition_satisfied(): Base_Types.Boolean = T
  // END FUNCTIONS

  def initialise(api: Manage_Monitor_Interface_i_Initialization_Api): Unit = {
    Contract(
      Modifies(
        api
      ),
      Ensures(
        // BEGIN INITIALIZES ENSURES
        // guarantee monitorStatusInitiallyInit
        api.monitor_status == Isolette_Data_Model.Status.Init_Status
        // END INITIALIZES ENSURES
      )
    )

    // set initial lower desired temp
    api.put_lower_alarm_temp(
      Temp_i(
        // FIXME:
        s32"97"))
        //InitialValues.DEFAULT_LOWER_ALARM_TEMPERATURE))
    // set initial upper desired temp
    api.put_upper_alarm_temp(
      Temp_i(
        // FIXME
        s32"101"))
        //InitialValues.DEFAULT_UPPER_ALARM_TEMPERATURE))
    // set initial regulator status
    api.put_monitor_status(InitialValues.DEFAULT_MONITOR_STATUS)

    // set initial regulator failure
    api.put_interface_failure(
      Failure_Flag_i(
        InitialValues.DEFAULT_MONITOR_INTERFACE_FAILURE_FLAG))
  }

  def timeTriggered(api: Manage_Monitor_Interface_i_Operational_Api): Unit = {
    Contract(
      Modifies(
        api,
        lastCmd
      ),
      Ensures(
        // BEGIN COMPUTE ENSURES timeTriggered
        // case REQ_MMI_1
        //   If the Manage Monitor Interface mode is INIT,
        //   the Monitor Status shall be set to Init.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 
        (api.monitor_mode == Isolette_Data_Model.Monitor_Mode.Init_Monitor_Mode) ___>: (api.monitor_status == Isolette_Data_Model.Status.Init_Status),
        // case REQ_MMI_2
        //   If the Manage Monitor Interface mode is NORMAL,
        //   the Monitor Status shall be set to On
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 
        (api.monitor_mode == Isolette_Data_Model.Monitor_Mode.Normal_Monitor_Mode) ___>: (api.monitor_status == Isolette_Data_Model.Status.On_Status),
        // case REQ_MMI_3
        //   If the Manage Monitor Interface mode is FAILED,
        //   the Monitor Status shall be set to Failed.
        //   Latency: < Max Operator Response Time
        //   Tolerance: N/A
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 
        (api.monitor_mode == Isolette_Data_Model.Monitor_Mode.Failed_Monitor_Mode) ___>: (api.monitor_status == Isolette_Data_Model.Status.Failed_Status),
        // case REQ_MMI_4
        //   If the Status attribute of the Lower Alarm Temperature
        //   or the Upper Alarm Temperature is Invalid,
        //   the Monitor Interface Failure shall be set to True
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 
        (api.lower_alarm_tempWstatus.status == Isolette_Data_Model.ValueStatus.Invalid |
           api.upper_alarm_tempWstatus.status == Isolette_Data_Model.ValueStatus.Invalid) ___>: (api.interface_failure.flag),
        // case REQ_MMI_5
        //   If the Status attribute of the Lower Alarm Temperature
        //   and the Upper Alarm Temperature is Valid,
        //   the Monitor Interface Failure shall be set to False
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 
        (api.lower_alarm_tempWstatus.status == Isolette_Data_Model.ValueStatus.Valid &
           api.upper_alarm_tempWstatus.status == Isolette_Data_Model.ValueStatus.Valid) ___>: (!(api.interface_failure.flag)),
        // case REQ_MMI_6
        //   If the Monitor Interface Failure is False,
        //   the Alarm Range variable shall be set to the Desired Temperature Range
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 
        (T) ___>: (!(api.interface_failure.flag) ___>:
          api.lower_alarm_temp.degrees == api.lower_alarm_tempWstatus.degrees &
            api.upper_alarm_temp.degrees == api.upper_alarm_tempWstatus.degrees),
        // case REQ_MMI_7
        //   If the Monitor Interface Failure is True,
        //   the Alarm Range variable is UNSPECIFIED
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=113 
        (T) ___>: (api.interface_failure.flag ___>: T)
        // END COMPUTE ENSURES timeTriggered
      )
    )

    val lower: Isolette_Data_Model.TempWstatus_i = api.get_lower_alarm_tempWstatus().get

    val upper: Isolette_Data_Model.TempWstatus_i = api.get_upper_alarm_tempWstatus().get

    val monitor_mode: Isolette_Data_Model.Monitor_Mode.Type = api.get_monitor_mode().get

    // =============================================
    //  Set values for Monitor Status (Table A-6)
    // =============================================

    var monitor_status: Isolette_Data_Model.Status.Type = Isolette_Data_Model.Status.Init_Status

    monitor_mode match {

      // INIT Mode
      case Isolette_Data_Model.Monitor_Mode.Init_Monitor_Mode =>
        //  REQ-MMI-1
        monitor_status = Isolette_Data_Model.Status.Init_Status

      // NORMAL Mode
      case Isolette_Data_Model.Monitor_Mode.Normal_Monitor_Mode =>
        //  REQ-MMI-2
        monitor_status = Isolette_Data_Model.Status.On_Status

      // FAILED Mode
      case Isolette_Data_Model.Monitor_Mode.Failed_Monitor_Mode =>
        //  REQ-MMI-3
        monitor_status = Isolette_Data_Model.Status.Failed_Status
    }
    api.put_monitor_status(monitor_status)

    //api.logInfo(s"Sent on monitor_status: $monitor_status")

    // =============================================
    //  Set values for Monitor Interface Failure internal variable
    // =============================================

    // The interface_failure status defaults to TRUE, which is the safe modality.
    var interface_failure: B = true

    // Extract the value status from both the upper and lower alarm range
    val upper_alarm_status: Isolette_Data_Model.ValueStatus.Type = upper.status
    val lower_alarm_status: Isolette_Data_Model.ValueStatus.Type = lower.status

    // Set the Monitor Interface Failure value based on the status values of the
    //   upper and lower temperature
    if (!(upper_alarm_status == Isolette_Data_Model.ValueStatus.Valid) ||
      !(lower_alarm_status == Isolette_Data_Model.ValueStatus.Valid)) {
      //  REQ-MMI-4
      interface_failure = true
    } else {
      //  REQ-MMI-5
      interface_failure = false
    }

    // create the appropriately typed value to send on the output port and set the port value
    val interface_failure_flag = Isolette_Data_Model.Failure_Flag_i(interface_failure)
    api.put_interface_failure(interface_failure_flag)

    //api.logInfo(s"Sent on interface_failure: $interface_failure_flag")


    // =============================================
    //  Set values for Alarm Range internal variable
    // =============================================

    if (!interface_failure) {
      //  REQ-MMI-6
      api.put_lower_alarm_temp(Isolette_Data_Model.Temp_i(lower.degrees))
      api.put_upper_alarm_temp(Isolette_Data_Model.Temp_i(upper.degrees))
    } else {
      //  REQ-MMI-7
      api.put_lower_alarm_temp(Isolette_Data_Model.Temp_i(lower.degrees))
      api.put_upper_alarm_temp(Isolette_Data_Model.Temp_i(upper.degrees))
    }
  }

  def finalise(api: Manage_Monitor_Interface_i_Operational_Api): Unit = { }
}
