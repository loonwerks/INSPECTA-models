// #Sireum #Logika

package isolette.Regulate

import org.sireum._
import isolette.Isolette_Data_Model.{Failure_Flag_i, Temp_i}
import isolette._
import org.sireum.S32._

// This file will not be overwritten so is safe to edit
object Manage_Regulator_Interface_i_thermostat_rt_mri_mri {

  // BEGIN FUNCTIONS
  @strictpure def ROUND(num: Base_Types.Integer_32): Base_Types.Integer_32 = num
  // END FUNCTIONS

  def initialise(api: Manage_Regulator_Interface_i_Initialization_Api): Unit = {
    Contract(
      Modifies(api),
      Ensures(
        // BEGIN INITIALIZES ENSURES
        // guarantee RegulatorStatusIsInitiallyInit
        api.regulator_status == Isolette_Data_Model.Status.Init_Status
        // END INITIALIZES ENSURES
      )
    )

    // set initial upper desired temp
    api.put_upper_desired_temp(Temp_i(InitialValues.DEFAULT_UPPER_DESIRED_TEMPERATURE))
    // set initial lower desired temp
    api.put_lower_desired_temp(Temp_i(InitialValues.DEFAULT_LOWER_DESIRED_TEMPERATURE))
    // set initial displayed temp
    api.put_displayed_temp(Temp_i(InitialValues.DEFAULT_DISPLAYED_TEMPERATURE))
    // set initial regulator status
    api.put_regulator_status(InitialValues.DEFAULT_REGULATOR_STATUS)
    // set initial regulator interface failure flag
    api.put_interface_failure(Failure_Flag_i(InitialValues.DEFAULT_REGULATOR_INTERFACE_FAILURE_FLAG))
  }

  def timeTriggered(api: Manage_Regulator_Interface_i_Operational_Api): Unit = {
    Contract(
      Requires(
        // BEGIN COMPUTE REQUIRES timeTriggered
        // assume lower_is_not_higher_than_upper
        api.lower_desired_tempWstatus.degrees <= api.upper_desired_tempWstatus.degrees
        // END COMPUTE REQUIRES timeTriggered
      ),
      Modifies(api),
      Ensures(
        // BEGIN COMPUTE ENSURES timeTriggered
        // case REQ_MRI_1
        //   If the Regulator Mode is INIT,
        //   the Regulator Status shall be set to Init.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=107 
        (api.regulator_mode == Isolette_Data_Model.Regulator_Mode.Init_Regulator_Mode) ___>: (api.regulator_status == Isolette_Data_Model.Status.Init_Status),
        // case REQ_MRI_2
        //   If the Regulator Mode is NORMAL,
        //   the Regulator Status shall be set to On
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=107 
        (api.regulator_mode == Isolette_Data_Model.Regulator_Mode.Normal_Regulator_Mode) ___>: (api.regulator_status == Isolette_Data_Model.Status.On_Status),
        // case REQ_MRI_3
        //   If the Regulator Mode is FAILED,
        //   the Regulator Status shall be set to Failed.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=107 
        (api.regulator_mode == Isolette_Data_Model.Regulator_Mode.Failed_Regulator_Mode) ___>: (api.regulator_status == Isolette_Data_Model.Status.Failed_Status),
        // case REQ_MRI_4
        //   If the Regulator Mode is NORMAL, the
        //   Display Temperature shall be set to the value of the
        //   Current Temperature rounded to the nearest integer.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 
        (api.regulator_mode == Isolette_Data_Model.Regulator_Mode.Normal_Regulator_Mode) ___>: (api.displayed_temp.degrees == api.current_tempWstatus.degrees),
        // case REQ_MRI_5
        //   If the Regulator Mode is not NORMAL,
        //   the value of the Display Temperature is UNSPECIFIED.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 
        (T) ___>: (T),
        // case REQ_MRI_6
        //   If the Status attribute of the Lower Desired Temperature
        //   or the Upper Desired Temperature is Invalid,
        //   the Regulator Interface Failure shall be set to True.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 
        (api.upper_desired_tempWstatus.status != Isolette_Data_Model.ValueStatus.Valid |
          api.upper_desired_tempWstatus.status != Isolette_Data_Model.ValueStatus.Valid) ___>: (api.interface_failure.flag),
        // case REQ_MRI_7
        //   If the Status attribute of the Lower Desired Temperature
        //   and the Upper Desired Temperature is Valid,
        //   the Regulator Interface Failure shall be set to False.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 
        (T) ___>: (api.interface_failure.flag == !(api.upper_desired_tempWstatus.status == Isolette_Data_Model.ValueStatus.Valid &
          api.lower_desired_tempWstatus.status == Isolette_Data_Model.ValueStatus.Valid)),
        // case REQ_MRI_8
        //   If the Regulator Interface Failure is False,
        //   the Desired Range shall be set to the Desired Temperature Range.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 
        (T) ___>: (!(api.interface_failure.flag) __>:
          api.lower_desired_temp.degrees == api.lower_desired_tempWstatus.degrees &
            api.upper_desired_temp.degrees == api.upper_desired_tempWstatus.degrees),
        // case REQ_MRI_9
        //   If the Regulator Interface Failure is True,
        //   the Desired Range is UNSPECIFIED.
        //   the Desired Range shall be set to the Desired Temperature Range.
        //   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=108 
        (T) ___>: (T)
        // END COMPUTE ENSURES timeTriggered
      )
    )

    // -------------- Get values of input ports ------------------

    val lower: Isolette_Data_Model.TempWstatus_i = api.get_lower_desired_tempWstatus().get

    val upper: Isolette_Data_Model.TempWstatus_i = api.get_upper_desired_tempWstatus().get

    val regulator_mode: Isolette_Data_Model.Regulator_Mode.Type = api.get_regulator_mode().get

    val currentTemp: Isolette_Data_Model.TempWstatus_i = api.get_current_tempWstatus().get

    // =============================================
    //  Set values for Regulator Status (Table A-6)
    //  http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=101
    // =============================================

    var regulator_status: Isolette_Data_Model.Status.Type = Isolette_Data_Model.Status.Init_Status

    regulator_mode match {

      // =============================================
      //  Set values for Regulator Status
      // =============================================

      // Note that this code computes a function that is independent of the other
      // aspects of this thread.  This should be reflected in flow declarations.

      // INIT Mode
      case Isolette_Data_Model.Regulator_Mode.Init_Regulator_Mode =>
        // REQ-MRI-1
        regulator_status = Isolette_Data_Model.Status.Init_Status

      // NORMAL Mode
      case Isolette_Data_Model.Regulator_Mode.Normal_Regulator_Mode =>
        // REQ-MRI-2
        regulator_status = Isolette_Data_Model.Status.On_Status

      // FAILED Mode
      case Isolette_Data_Model.Regulator_Mode.Failed_Regulator_Mode =>
        // REQ-MRI-3
        regulator_status = Isolette_Data_Model.Status.Failed_Status
    }

    api.put_regulator_status(regulator_status)

    //api.logInfo(s"Sent onf regulator_status: $regulator_status")

    // =============================================
    //  Set values for Display Temperature (Table A-6)
    // =============================================

    // Latency: < Max Operator Response Time
    // Tolerance: +/- 0.6 degrees F
    //

    var display_temperature = Isolette_Data_Model.Temp_i.example()

    regulator_mode match {
      // NORMAL Mode
      case Isolette_Data_Model.Regulator_Mode.Normal_Regulator_Mode =>
        // REQ-MRI-4
        display_temperature = Isolette_Data_Model.Temp_i(currentTemp.degrees)

      // INIT, FAILED Modes

      // REQ-MRI
      //
      // leave the current temperature set to the default temperature value above.
      // This fulfills the requirement since the value when the Regulator Mode is not NORMAL
      // is unspecified.

      case Isolette_Data_Model.Regulator_Mode.Init_Regulator_Mode => // Do nothing
      case Isolette_Data_Model.Regulator_Mode.Failed_Regulator_Mode => // Do nothing
    }

    api.put_displayed_temp(display_temperature)

    //api.logInfo(s"Sent on displayed_temp: $display_temperature")

    // =============================================
    //  Set values for Regulator Interface Failure internal variable
    // =============================================

    // The interface_failure status defaults to TRUE (i.e., failing), which is the safe modality.
    var interface_failure: B = true

    // Extract the value status from both the upper and lower alarm range
    val upper_desired_temp_status: Isolette_Data_Model.ValueStatus.Type = upper.status
    val lower_desired_temp_status: Isolette_Data_Model.ValueStatus.Type = lower.status

    // Set the Monitor Interface Failure value based on the status values of the
    //   upper and lower temperature
    if (!(upper_desired_temp_status == Isolette_Data_Model.ValueStatus.Valid) ||
      !(lower_desired_temp_status == Isolette_Data_Model.ValueStatus.Valid)) {
      // REQ-MRI-6
      interface_failure = true
    } else {
      // REQ-MRI-7
      interface_failure = false
    }

    // create the appropriately typed value to send on the output port and set the port value
    val interface_failure_flag = Isolette_Data_Model.Failure_Flag_i(interface_failure)
    api.put_interface_failure(interface_failure_flag)

    //api.logInfo(s"Sent on interface_failure: $interface_failure_flag")

    // =============================================
    //  Set values for Desired Range internal variable
    // =============================================

    if (!interface_failure) {
      // REQ-MRI-8
      api.put_lower_desired_temp(Isolette_Data_Model.Temp_i(lower.degrees))
      api.put_upper_desired_temp(Isolette_Data_Model.Temp_i(upper.degrees))

      //api.logInfo(s"Sent on lower_desired_temp: ${Isolette_Data_Model.Temp_impl(lower.value)}")
      //api.logInfo(s"Sent on upper_desired_temp: ${Isolette_Data_Model.Temp_impl(upper.value)}")
    } else {
      // REQ-MRI-9
      api.put_lower_desired_temp(Isolette_Data_Model.Temp_i.example())
      api.put_upper_desired_temp(Isolette_Data_Model.Temp_i.example())

      //api.logInfo(s"Sent on lower_desired_temp: ${Isolette_Data_Model.Temp_impl.example()}")
      //api.logInfo(s"Sent on upper_desired_temp: ${Isolette_Data_Model.Temp_impl.example()}")
    }
  }

  def finalise(api: Manage_Regulator_Interface_i_Operational_Api): Unit = { }
}
