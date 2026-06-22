// #Sireum #Logika

package isolette.Monitor

import org.sireum._
import isolette._

// This file will not be overwritten so is safe to edit
object Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm {

  // BEGIN STATE VARS
  var lastMonitorMode: Isolette_Data_Model.Monitor_Mode.Type = Isolette_Data_Model.Monitor_Mode.Init_Monitor_Mode
  // END STATE VARS

  // BEGIN FUNCTIONS
  @strictpure def monitor_status(interface_failure: Isolette_Data_Model.Failure_Flag_i, internal_failure: Isolette_Data_Model.Failure_Flag_i, current_tempWstatus: Isolette_Data_Model.TempWstatus_i): Base_Types.Boolean = !(interface_failure.flag | internal_failure.flag) &
    current_tempWstatus.status == Isolette_Data_Model.ValueStatus.Valid

  @strictpure def timeout_condition_satisfied(): Base_Types.Boolean = F
  // END FUNCTIONS

  def initialise(api: Manage_Monitor_Mode_i_Initialization_Api): Unit = {
    Contract(
      Modifies(
        api,
        lastMonitorMode
      ),
      Ensures(
        // BEGIN INITIALIZES ENSURES
        // guarantee REQ_MMM_1
        //   Upon the first dispatch of the thread, the monitor mode is Init.
        //   https://www.faa.gov/sites/faa.gov/files/aircraft/air_cert/design_approvals/air_software/AR-08-32.pdf#page=114 
        api.monitor_mode == Isolette_Data_Model.Monitor_Mode.Init_Monitor_Mode,
        // guarantee update_lastMonitorMode
        lastMonitorMode == api.monitor_mode
        // END INITIALIZES ENSURES
      )
    )
    Manage_Monitor_Mode__InjectionProvider.init()

    lastMonitorMode = Isolette_Data_Model.Monitor_Mode.Init_Monitor_Mode

    api.put_monitor_mode(lastMonitorMode)
  }

  def timeTriggered(api: Manage_Monitor_Mode_i_Operational_Api): Unit = {
    Contract(
      Modifies(
        api,
        lastMonitorMode
      ),
      Ensures(
        // BEGIN COMPUTE ENSURES timeTriggered
        // case REQ_MMM_2
        //   If the current mode is Init, then
        //   the mode is set to NORMAL iff the monitor status is true (valid) (see Table A-15), i.e.,
        //   if  NOT (Monitor Interface Failure OR Monitor Internal Failure)
        //   AND Current Temperature.Status = Valid.
        //   Formalized as an iff per the requirement text (with the Init timeout
        //   transition of REQ-MMM-4 excluded so the two cases cannot conflict);
        //   the converse direction is needed at the system level to conclude
        //   that NORMAL mode implies no monitor interface failure.
        //   https://www.faa.gov/sites/faa.gov/files/aircraft/air_cert/design_approvals/air_software/AR-08-32.pdf#page=114 
        (In(lastMonitorMode) == Isolette_Data_Model.Monitor_Mode.Init_Monitor_Mode) ___>: ((Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm.monitor_status(api.interface_failure, api.internal_failure, api.current_tempWstatus) & !(Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm.timeout_condition_satisfied())) == (api.monitor_mode == Isolette_Data_Model.Monitor_Mode.Normal_Monitor_Mode)),
        // case REQ_MMM_Maintain_Normal
        //   'maintaining NORMAL, NORMAL to NORMAL'
        //   If the current monitor mode is Normal and the monitor status is true
        //   (no interface/internal failure and the current temperature is valid),
        //   the monitor mode stays Normal.
        //   https://www.faa.gov/sites/faa.gov/files/aircraft/air_cert/design_approvals/air_software/AR-08-32.pdf#page=114 
        (In(lastMonitorMode) == Isolette_Data_Model.Monitor_Mode.Normal_Monitor_Mode &
          Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm.monitor_status(api.interface_failure, api.internal_failure, api.current_tempWstatus)) ___>: (api.monitor_mode == Isolette_Data_Model.Monitor_Mode.Normal_Monitor_Mode),
        // case REQ_MMM_3
        //   If the current Monitor mode is Normal, then
        //   the Monitor mode is set to Failed iff
        //   the Monitor status is false, i.e.,
        //   if  (Monitor Interface Failure OR Monitor Internal Failure)
        //   OR NOT(Current Temperature.Status = Valid)
        //   https://www.faa.gov/sites/faa.gov/files/aircraft/air_cert/design_approvals/air_software/AR-08-32.pdf#page=114 
        (In(lastMonitorMode) == Isolette_Data_Model.Monitor_Mode.Normal_Monitor_Mode) ___>: (!(Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm.monitor_status(api.interface_failure, api.internal_failure, api.current_tempWstatus)) == (api.monitor_mode == Isolette_Data_Model.Monitor_Mode.Failed_Monitor_Mode)),
        // case REQ_MMM_4
        //   If the current mode is Init, then
        //   the mode is set to Failed iff the time during
        //   which the thread has been in Init mode exceeds the
        //   Monitor Init Timeout value.
        //   https://www.faa.gov/sites/faa.gov/files/aircraft/air_cert/design_approvals/air_software/AR-08-32.pdf#page=114 
        (In(lastMonitorMode) == Isolette_Data_Model.Monitor_Mode.Init_Monitor_Mode) ___>: (Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm.timeout_condition_satisfied() == (api.monitor_mode == Isolette_Data_Model.Monitor_Mode.Failed_Monitor_Mode)),
        // case Failed_Mode_Absorbing
        //   If the current mode is Failed, the mode remains Failed.
        //   Derived requirement -- not numbered in the requirements spec, but
        //   implied by Figure A-6: the monitor mode state machine has no
        //   transitions out of the Failed mode.  The diagram's implicit frame
        //   (no arc drawn means no transition) must be stated explicitly here:
        //   a contract that is silent for lastMonitorMode == Failed allows any
        //   mode value (e.g., Failed to Normal while the interface is still
        //   failing), which would break the system-level property that NORMAL
        //   mode implies no monitor interface failure.
        //   https://www.faa.gov/sites/faa.gov/files/aircraft/air_cert/design_approvals/air_software/AR-08-32.pdf#page=114 
        (In(lastMonitorMode) == Isolette_Data_Model.Monitor_Mode.Failed_Monitor_Mode) ___>: (api.monitor_mode == Isolette_Data_Model.Monitor_Mode.Failed_Monitor_Mode)
        // END COMPUTE ENSURES timeTriggered
      )
    )
    // -------------- Get values of input ports ------------------

    val current_tempWstatus: Isolette_Data_Model.TempWstatus_i = api.get_current_tempWstatus().get
    val interface_failure: Isolette_Data_Model.Failure_Flag_i = api.get_interface_failure().get
    val internal_failure: Isolette_Data_Model.Failure_Flag_i = api.get_internal_failure().get

    //==============================================================================


    // determine monitor status as specified in FAA REMH Table A-15
    //  monitor_status = NOT (Monitor Interface Failure OR Monitor Internal Failure)
    //                          AND Current Temperature.Status = Valid
    val monitor_status: B = {
      (!(interface_failure.flag || internal_failure.flag) && current_tempWstatus.status == Isolette_Data_Model.ValueStatus.Valid)
    }

    lastMonitorMode match {

      // Transitions from INIT Mode
      case Isolette_Data_Model.Monitor_Mode.Init_Monitor_Mode =>
        // REQ-MMM-2
        if (monitor_status) {
          lastMonitorMode = Isolette_Data_Model.Monitor_Mode.Normal_Monitor_Mode
        }

        else if (timeout_condition_satisfied()) {
          // REQ-MMM-4
          lastMonitorMode = Isolette_Data_Model.Monitor_Mode.Failed_Monitor_Mode
        } else {
          // otherwise we stay in Init mode
        }

      // Transitions from NORMAL Mode
      case Isolette_Data_Model.Monitor_Mode.Normal_Monitor_Mode =>
        if (!monitor_status) {
          // REQ-MMM-3
          lastMonitorMode = Isolette_Data_Model.Monitor_Mode.Failed_Monitor_Mode
        }

      // Transitions from FAILED Mode (do nothing -- system must be rebooted)
      case Isolette_Data_Model.Monitor_Mode.Failed_Monitor_Mode =>
      // do nothing
    }

    api.put_monitor_mode(lastMonitorMode)

    //api.logInfo(s"Sent on monitor_mode: $lastMonitorMode")
  }

  def finalise(api: Manage_Monitor_Mode_i_Operational_Api): Unit = { }
}

@ext object Manage_Monitor_Mode__InjectionProvider {
  def init(): Unit = $
}