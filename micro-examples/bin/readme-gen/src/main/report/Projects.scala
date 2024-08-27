// #Sireum
package report

import org.sireum._

object Projects {

  val home: Os.Path = Os.path(".").up.up.up

  val jenkinsJobName: String = "0DSC_Unit_Testing_start"

  @datatype class Container(val project: String,
                            val packageName: String,
                            val objectName: String,

                            val prettyName: String,
                            val tag: String
                           ) {
    def dscPrefix: String = {
      return s"$project-$tag"
    }
  }

  @datatype class TContainer(val node: String,
                             val server: String,
                             val basePackage: String,
                             val hamrRoot: Os.Path,
                             val aadlRoot: Os.Path,
                             val timeouts: ISZ[Z],
                             val containers: ISZ[Container])

  val isolette: (String, TContainer) = "isolette" ~> TContainer(
    "mac-mini-m1",
    "e2206hm02.cs.ksu.edu",
    "isolette",
    home / "isolette" / "hamr" / "slang",
    home / "isolette" / "aadl",
    ISZ(1, 5, 30, 60, 360),
    //ISZ(1, 5, 30),
    ISZ(
      Container("isolette", "isolette.Monitor", "Manage_Alarm_impl_thermostat_monitor_temperature_manage_alarm",
        "Manage Alarm", "ma"),
      Container("isolette", "isolette.Monitor", "Manage_Monitor_Interface_impl_thermostat_monitor_temperature_manage_monitor_interface",
        "Manage Monitor Interface", "mmi"),
      Container("isolette", "isolette.Monitor", "Manage_Monitor_Mode_impl_thermostat_monitor_temperature_manage_monitor_mode",
        "Manage Monitor Mode", "mmm"),

      Container("isolette", "isolette.Regulate", "Manage_Heat_Source_impl_thermostat_regulate_temperature_manage_heat_source",
        "Manage Heat Source", "mhs"),
      Container("isolette", "isolette.Regulate", "Manage_Heat_Source_impl_thermostat_regulate_temperature_manage_heat_source",
        "Manage Regulator Interface", "mri"),
      Container("isolette", "isolette.Regulate", "Manage_Regulator_Mode_impl_thermostat_regulate_temperature_manage_regulator_mode",
        "Manage Regulator Mode", "mrm")
    )
  )

  val rts: (String, TContainer) = "rts" ~> TContainer(
    "mac-mini-intel",
    "e2206hm02.cs.ksu.edu",
    "RTS",
    home / "rts" / "hamr" / "slang",
    home / "rts" / "aadl",
    ISZ(1, 5, 30, 60, 360),
    ISZ(
      Container("rts", "RTS.Actuation", "Actuator_i_actuationSubsystem_saturationActuatorUnit_saturationActuator_actuator",
        "Saturation Actuator", "saturationActuator"),

      Container("rts", "RTS.Actuation", "Actuator_i_actuationSubsystem_tempPressureActuatorUnit_tempPressureActuator_actuator",
        "TempPressure Actuator Unit", "tempPressureActuator"),

      Container("rts", "RTS.Actuation", "CoincidenceLogic_i_actuationSubsystem_actuationUnit1_pressureLogic_coincidenceLogic",
        "Actuation Unit 1 Pressure Logic", "au1-pressureLogic"),
      Container("rts", "RTS.Actuation", "CoincidenceLogic_i_actuationSubsystem_actuationUnit1_saturationLogic_coincidenceLogic",
        "Actuation Unit 1 Saturation Logic", "au1-saturationLogic"),
      Container("rts", "RTS.Actuation", "CoincidenceLogic_i_actuationSubsystem_actuationUnit1_temperatureLogic_coincidenceLogic",
        "Actuation Unit 1 Temperature Logic", "au1-temperatureLogic"),


      Container("rts", "RTS.Actuation", "CoincidenceLogic_i_actuationSubsystem_actuationUnit2_pressureLogic_coincidenceLogic",
        "Actuation Unit 2 Pressure Logic", "au2-pressureLogic"),
      Container("rts", "RTS.Actuation", "CoincidenceLogic_i_actuationSubsystem_actuationUnit2_saturationLogic_coincidenceLogic",
        "Actuation Unit 2 Saturation Logic", "au2-saturationLogic"),
      Container("rts", "RTS.Actuation", "CoincidenceLogic_i_actuationSubsystem_actuationUnit2_temperatureLogic_coincidenceLogic",
        "Actuation Unit 2 Temperature Logic", "au2-temperatureLogic"),


      Container("rts", "RTS.Actuation", "OrLogic_i_actuationSubsystem_actuationUnit1_tempPressureTripOut_orLogic",
        "Actuation Unit 1 TempPressureTripOut or Logic", "au1-tempPressureTripOut"),
      Container("rts", "RTS.Actuation", "OrLogic_i_actuationSubsystem_actuationUnit2_tempPressureTripOut_orLogic",
        "Actuation Unit 2 TempPressureTripOut or Logic", "au2-tempPressureTripOut"),

      Container("rts", "RTS.Actuation", "OrLogic_i_actuationSubsystem_saturationActuatorUnit_actuateSaturationActuator_orLogic",
        "Actuate Saturation Actuator", "actuateSaturationActuator"),

      Container("rts", "RTS.Actuation", "OrLogic_i_actuationSubsystem_tempPressureActuatorUnit_actuateTempPressureActuator_orLogic",
        "Actuate TempPressure Actuator", "actuateTempPressureActuator")
    )
  )

  val tc: (String, TContainer) = "tc" ~> TContainer(
    "mac-mini-m1",
    "e2206hm02.cs.ksu.edu",
    "tc",
    home / "temp_control" / "periodic" / "hamr" / "slang",
    home / "temp_control" / "periodic" / "aadl",
    ISZ(1, 5, 30, 60, 360),
    ISZ(
      Container("tc", "tc.CoolingFan", "FanPeriodic_p_tcproc_fan",
        "Cooling Fan", "fan"),

      Container("tc", "tc.TempControlSoftwareSystem", "OperatorInterfacePeriodic_p_tcproc_operatorInterface",
        "Operator Interface", "operator-interface"),

      Container("tc", "tc.TempControlSoftwareSystem", "TempControlPeriodic_p_tcproc_tempControl",
        "Temperature Controller", "temp-control"),

      Container("tc", "tc.TempSensor", "TempSensorPeriodic_p_tcproc_tempSensor",
        "Temperature Sensor", "temp-sensor")
    )
  )


  val projects: Map[String, TContainer] = Map.empty[String, TContainer] ++ ISZ(isolette, rts, tc)

}
