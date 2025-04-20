// #Sireum

package isolette

import org.sireum._
import org.sireum.Random.Gen64

/*
GENERATED FROM

ValueStatus.scala

TempWstatus_i.scala

Regulator_Mode.scala

Temp_i.scala

Status.scala

Failure_Flag_i.scala

On_Off.scala

Monitor_Mode.scala

Interface_Interaction.scala

PhysicalTemp_i.scala

Heat.scala

Base_Types.scala

Manage_Regulator_Interface_i_thermostat_rt_mri_mri_Containers.scala

Manage_Heat_Source_i_thermostat_rt_mhs_mhs_Containers.scala

Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_Containers.scala

Detect_Regulator_Failure_i_thermostat_rt_drf_drf_Containers.scala

Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_Containers.scala

Manage_Alarm_i_thermostat_mt_ma_ma_Containers.scala

Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_Containers.scala

Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_Containers.scala

Operator_Interface_Thread_i_operator_interface_oip_oit_Containers.scala

Temperature_Sensor_i_temperature_sensor_cpi_thermostat_Containers.scala

Heat_Source_i_heat_source_cpi_heat_controller_Containers.scala

ObservationKind.scala

Container.scala

DataContent.scala

Aux_Types.scala

*/

@record class Gen_String(param: RandomLibI) extends MJen[String] {
  override def generate(f: String => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextString())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Z(param: RandomLibI) extends MJen[Z] {
  override def generate(f: Z => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextZ())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_B(param: RandomLibI) extends MJen[B] {
  override def generate(f: B => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextB())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_C(param: RandomLibI) extends MJen[C] {
  override def generate(f: C => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextC())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_R(param: RandomLibI) extends MJen[R] {
  override def generate(f: R => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextR())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_F32(param: RandomLibI) extends MJen[F32] {
  override def generate(f: F32 => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextF32())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_F64(param: RandomLibI) extends MJen[F64] {
  override def generate(f: F64 => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextF64())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_S8(param: RandomLibI) extends MJen[S8] {
  override def generate(f: S8 => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextS8())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_S16(param: RandomLibI) extends MJen[S16] {
  override def generate(f: S16 => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextS16())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_S32(param: RandomLibI) extends MJen[S32] {
  override def generate(f: S32 => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextS32())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_S64(param: RandomLibI) extends MJen[S64] {
  override def generate(f: S64 => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextS64())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_U8(param: RandomLibI) extends MJen[U8] {
  override def generate(f: U8 => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextU8())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_U16(param: RandomLibI) extends MJen[U16] {
  override def generate(f: U16 => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextU16())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_U32(param: RandomLibI) extends MJen[U32] {
  override def generate(f: U32 => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextU32())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_U64(param: RandomLibI) extends MJen[U64] {
  override def generate(f: U64 => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextU64())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}


@record class Gen__artDataContent(param: RandomLibI) extends MJen[art.DataContent] {
  override def generate(f: art.DataContent => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.next_artDataContent())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen__artEmpty(param: RandomLibI) extends MJen[art.Empty] {
  override def generate(f: art.Empty => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.next_artEmpty())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Base_TypesBoolean_Payload(param: RandomLibI) extends MJen[Base_Types.Boolean_Payload] {
  override def generate(f: Base_Types.Boolean_Payload => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextBase_TypesBoolean_Payload())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Base_TypesInteger_Payload(param: RandomLibI) extends MJen[Base_Types.Integer_Payload] {
  override def generate(f: Base_Types.Integer_Payload => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextBase_TypesInteger_Payload())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Base_TypesInteger_8_Payload(param: RandomLibI) extends MJen[Base_Types.Integer_8_Payload] {
  override def generate(f: Base_Types.Integer_8_Payload => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextBase_TypesInteger_8_Payload())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Base_TypesInteger_16_Payload(param: RandomLibI) extends MJen[Base_Types.Integer_16_Payload] {
  override def generate(f: Base_Types.Integer_16_Payload => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextBase_TypesInteger_16_Payload())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Base_TypesInteger_32_Payload(param: RandomLibI) extends MJen[Base_Types.Integer_32_Payload] {
  override def generate(f: Base_Types.Integer_32_Payload => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextBase_TypesInteger_32_Payload())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Base_TypesInteger_64_Payload(param: RandomLibI) extends MJen[Base_Types.Integer_64_Payload] {
  override def generate(f: Base_Types.Integer_64_Payload => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextBase_TypesInteger_64_Payload())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Base_TypesUnsigned_8_Payload(param: RandomLibI) extends MJen[Base_Types.Unsigned_8_Payload] {
  override def generate(f: Base_Types.Unsigned_8_Payload => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextBase_TypesUnsigned_8_Payload())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Base_TypesUnsigned_16_Payload(param: RandomLibI) extends MJen[Base_Types.Unsigned_16_Payload] {
  override def generate(f: Base_Types.Unsigned_16_Payload => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextBase_TypesUnsigned_16_Payload())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Base_TypesUnsigned_32_Payload(param: RandomLibI) extends MJen[Base_Types.Unsigned_32_Payload] {
  override def generate(f: Base_Types.Unsigned_32_Payload => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextBase_TypesUnsigned_32_Payload())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Base_TypesUnsigned_64_Payload(param: RandomLibI) extends MJen[Base_Types.Unsigned_64_Payload] {
  override def generate(f: Base_Types.Unsigned_64_Payload => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextBase_TypesUnsigned_64_Payload())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Base_TypesFloat_Payload(param: RandomLibI) extends MJen[Base_Types.Float_Payload] {
  override def generate(f: Base_Types.Float_Payload => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextBase_TypesFloat_Payload())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Base_TypesFloat_32_Payload(param: RandomLibI) extends MJen[Base_Types.Float_32_Payload] {
  override def generate(f: Base_Types.Float_32_Payload => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextBase_TypesFloat_32_Payload())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Base_TypesFloat_64_Payload(param: RandomLibI) extends MJen[Base_Types.Float_64_Payload] {
  override def generate(f: Base_Types.Float_64_Payload => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextBase_TypesFloat_64_Payload())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Base_TypesCharacter_Payload(param: RandomLibI) extends MJen[Base_Types.Character_Payload] {
  override def generate(f: Base_Types.Character_Payload => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextBase_TypesCharacter_Payload())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Base_TypesString_Payload(param: RandomLibI) extends MJen[Base_Types.String_Payload] {
  override def generate(f: Base_Types.String_Payload => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextBase_TypesString_Payload())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_ISZB(param: RandomLibI) extends MJen[ISZ[B]] {
  override def generate(f: ISZ[B] => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextISZB())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Base_TypesBits_Payload(param: RandomLibI) extends MJen[Base_Types.Bits_Payload] {
  override def generate(f: Base_Types.Bits_Payload => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextBase_TypesBits_Payload())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_DevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container(param: RandomLibI) extends MJen[Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container] {
  override def generate(f: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_DevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_P(param: RandomLibI) extends MJen[Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container_P] {
  override def generate(f: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container_P => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_P())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_DevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PS(param: RandomLibI) extends MJen[Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PS] {
  override def generate(f: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PS => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextDevicesHeat_Source_i_heat_source_cpi_heat_controller_PreState_Container_PS())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_DevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container(param: RandomLibI) extends MJen[Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container] {
  override def generate(f: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_DevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_P(param: RandomLibI) extends MJen[Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container_P] {
  override def generate(f: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container_P => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_P())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_DevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PS(param: RandomLibI) extends MJen[Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PS] {
  override def generate(f: Devices.Heat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PS => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextDevicesHeat_Source_i_heat_source_cpi_heat_controller_PostState_Container_PS())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container(param: RandomLibI) extends MJen[Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container] {
  override def generate(f: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_P(param: RandomLibI) extends MJen[Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_P] {
  override def generate(f: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_P => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_P())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PS(param: RandomLibI) extends MJen[Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PS] {
  override def generate(f: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PS => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PreState_Container_PS())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container(param: RandomLibI) extends MJen[Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container] {
  override def generate(f: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_P(param: RandomLibI) extends MJen[Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_P] {
  override def generate(f: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_P => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_P())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_DevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PS(param: RandomLibI) extends MJen[Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PS] {
  override def generate(f: Devices.Temperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PS => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextDevicesTemperature_Sensor_i_temperature_sensor_cpi_thermostat_PostState_Container_PS())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Isolette_Data_ModelFailure_Flag_i(param: RandomLibI) extends MJen[Isolette_Data_Model.Failure_Flag_i] {
  override def generate(f: Isolette_Data_Model.Failure_Flag_i => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextIsolette_Data_ModelFailure_Flag_i())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Isolette_Data_ModelFailure_Flag_i_Payload(param: RandomLibI) extends MJen[Isolette_Data_Model.Failure_Flag_i_Payload] {
  override def generate(f: Isolette_Data_Model.Failure_Flag_i_Payload => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextIsolette_Data_ModelFailure_Flag_i_Payload())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Isolette_Data_ModelMonitor_ModeType(param: RandomLibI) extends MJen[Isolette_Data_Model.Monitor_Mode.Type] {
  override def generate(f: Isolette_Data_Model.Monitor_Mode.Type => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextIsolette_Data_ModelMonitor_ModeType())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Isolette_Data_ModelMonitor_Mode_Payload(param: RandomLibI) extends MJen[Isolette_Data_Model.Monitor_Mode_Payload] {
  override def generate(f: Isolette_Data_Model.Monitor_Mode_Payload => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextIsolette_Data_ModelMonitor_Mode_Payload())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Isolette_Data_ModelOn_OffType(param: RandomLibI) extends MJen[Isolette_Data_Model.On_Off.Type] {
  override def generate(f: Isolette_Data_Model.On_Off.Type => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextIsolette_Data_ModelOn_OffType())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Isolette_Data_ModelOn_Off_Payload(param: RandomLibI) extends MJen[Isolette_Data_Model.On_Off_Payload] {
  override def generate(f: Isolette_Data_Model.On_Off_Payload => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextIsolette_Data_ModelOn_Off_Payload())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Isolette_Data_ModelPhysicalTemp_i(param: RandomLibI) extends MJen[Isolette_Data_Model.PhysicalTemp_i] {
  override def generate(f: Isolette_Data_Model.PhysicalTemp_i => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextIsolette_Data_ModelPhysicalTemp_i())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Isolette_Data_ModelPhysicalTemp_i_Payload(param: RandomLibI) extends MJen[Isolette_Data_Model.PhysicalTemp_i_Payload] {
  override def generate(f: Isolette_Data_Model.PhysicalTemp_i_Payload => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextIsolette_Data_ModelPhysicalTemp_i_Payload())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Isolette_Data_ModelRegulator_ModeType(param: RandomLibI) extends MJen[Isolette_Data_Model.Regulator_Mode.Type] {
  override def generate(f: Isolette_Data_Model.Regulator_Mode.Type => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextIsolette_Data_ModelRegulator_ModeType())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Isolette_Data_ModelRegulator_Mode_Payload(param: RandomLibI) extends MJen[Isolette_Data_Model.Regulator_Mode_Payload] {
  override def generate(f: Isolette_Data_Model.Regulator_Mode_Payload => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextIsolette_Data_ModelRegulator_Mode_Payload())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Isolette_Data_ModelStatusType(param: RandomLibI) extends MJen[Isolette_Data_Model.Status.Type] {
  override def generate(f: Isolette_Data_Model.Status.Type => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextIsolette_Data_ModelStatusType())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Isolette_Data_ModelStatus_Payload(param: RandomLibI) extends MJen[Isolette_Data_Model.Status_Payload] {
  override def generate(f: Isolette_Data_Model.Status_Payload => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextIsolette_Data_ModelStatus_Payload())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Isolette_Data_ModelTempWstatus_i(param: RandomLibI) extends MJen[Isolette_Data_Model.TempWstatus_i] {
  override def generate(f: Isolette_Data_Model.TempWstatus_i => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextIsolette_Data_ModelTempWstatus_i())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Isolette_Data_ModelTempWstatus_i_Payload(param: RandomLibI) extends MJen[Isolette_Data_Model.TempWstatus_i_Payload] {
  override def generate(f: Isolette_Data_Model.TempWstatus_i_Payload => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextIsolette_Data_ModelTempWstatus_i_Payload())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Isolette_Data_ModelTemp_i(param: RandomLibI) extends MJen[Isolette_Data_Model.Temp_i] {
  override def generate(f: Isolette_Data_Model.Temp_i => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextIsolette_Data_ModelTemp_i())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Isolette_Data_ModelTemp_i_Payload(param: RandomLibI) extends MJen[Isolette_Data_Model.Temp_i_Payload] {
  override def generate(f: Isolette_Data_Model.Temp_i_Payload => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextIsolette_Data_ModelTemp_i_Payload())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Isolette_Data_ModelValueStatusType(param: RandomLibI) extends MJen[Isolette_Data_Model.ValueStatus.Type] {
  override def generate(f: Isolette_Data_Model.ValueStatus.Type => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextIsolette_Data_ModelValueStatusType())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Isolette_Data_ModelValueStatus_Payload(param: RandomLibI) extends MJen[Isolette_Data_Model.ValueStatus_Payload] {
  override def generate(f: Isolette_Data_Model.ValueStatus_Payload => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextIsolette_Data_ModelValueStatus_Payload())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Isolette_EnvironmentHeatType(param: RandomLibI) extends MJen[Isolette_Environment.Heat.Type] {
  override def generate(f: Isolette_Environment.Heat.Type => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextIsolette_EnvironmentHeatType())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Isolette_EnvironmentHeat_Payload(param: RandomLibI) extends MJen[Isolette_Environment.Heat_Payload] {
  override def generate(f: Isolette_Environment.Heat_Payload => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextIsolette_EnvironmentHeat_Payload())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Isolette_EnvironmentInterface_InteractionType(param: RandomLibI) extends MJen[Isolette_Environment.Interface_Interaction.Type] {
  override def generate(f: Isolette_Environment.Interface_Interaction.Type => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextIsolette_EnvironmentInterface_InteractionType())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Isolette_EnvironmentInterface_Interaction_Payload(param: RandomLibI) extends MJen[Isolette_Environment.Interface_Interaction_Payload] {
  override def generate(f: Isolette_Environment.Interface_Interaction_Payload => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextIsolette_EnvironmentInterface_Interaction_Payload())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container(param: RandomLibI) extends MJen[Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container] {
  override def generate(f: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_P(param: RandomLibI) extends MJen[Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_P] {
  override def generate(f: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_P => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_P())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PS(param: RandomLibI) extends MJen[Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PS] {
  override def generate(f: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PS => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PreState_Container_PS())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container(param: RandomLibI) extends MJen[Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container] {
  override def generate(f: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_P(param: RandomLibI) extends MJen[Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_P] {
  override def generate(f: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_P => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_P())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_MonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PS(param: RandomLibI) extends MJen[Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PS] {
  override def generate(f: Monitor.Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PS => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextMonitorDetect_Monitor_Failure_i_thermostat_mt_dmf_dmf_PostState_Container_PS())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_MonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container(param: RandomLibI) extends MJen[Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container] {
  override def generate(f: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_MonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P(param: RandomLibI) extends MJen[Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P] {
  override def generate(f: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_MonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS(param: RandomLibI) extends MJen[Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS] {
  override def generate(f: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_MonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container(param: RandomLibI) extends MJen[Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container] {
  override def generate(f: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_MonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P(param: RandomLibI) extends MJen[Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P] {
  override def generate(f: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_P())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_MonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS(param: RandomLibI) extends MJen[Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS] {
  override def generate(f: Monitor.Manage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextMonitorManage_Alarm_i_thermostat_mt_ma_ma_PostState_Container_PS())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container(param: RandomLibI) extends MJen[Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container] {
  override def generate(f: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P(param: RandomLibI) extends MJen[Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P] {
  override def generate(f: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_P())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS(param: RandomLibI) extends MJen[Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS] {
  override def generate(f: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PreState_Container_PS())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container(param: RandomLibI) extends MJen[Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container] {
  override def generate(f: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P(param: RandomLibI) extends MJen[Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P] {
  override def generate(f: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_P())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_MonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS(param: RandomLibI) extends MJen[Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS] {
  override def generate(f: Monitor.Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextMonitorManage_Monitor_Interface_i_thermostat_mt_mmi_mmi_PostState_Container_PS())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container(param: RandomLibI) extends MJen[Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container] {
  override def generate(f: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P(param: RandomLibI) extends MJen[Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P] {
  override def generate(f: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_P())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS(param: RandomLibI) extends MJen[Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS] {
  override def generate(f: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PreState_Container_PS())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container(param: RandomLibI) extends MJen[Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container] {
  override def generate(f: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P(param: RandomLibI) extends MJen[Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P] {
  override def generate(f: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_P())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_MonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS(param: RandomLibI) extends MJen[Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS] {
  override def generate(f: Monitor.Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextMonitorManage_Monitor_Mode_i_thermostat_mt_mmm_mmm_PostState_Container_PS())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container(param: RandomLibI) extends MJen[Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container] {
  override def generate(f: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P(param: RandomLibI) extends MJen[Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P] {
  override def generate(f: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS(param: RandomLibI) extends MJen[Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS] {
  override def generate(f: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_PS())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container(param: RandomLibI) extends MJen[Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container] {
  override def generate(f: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P(param: RandomLibI) extends MJen[Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P] {
  override def generate(f: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_P())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_Operator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS(param: RandomLibI) extends MJen[Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS] {
  override def generate(f: Operator_Interface.Operator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PostState_Container_PS())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container(param: RandomLibI) extends MJen[Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container] {
  override def generate(f: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P(param: RandomLibI) extends MJen[Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P] {
  override def generate(f: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS(param: RandomLibI) extends MJen[Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS] {
  override def generate(f: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container(param: RandomLibI) extends MJen[Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container] {
  override def generate(f: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P(param: RandomLibI) extends MJen[Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P] {
  override def generate(f: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_RegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS(param: RandomLibI) extends MJen[Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS] {
  override def generate(f: Regulate.Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container(param: RandomLibI) extends MJen[Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container] {
  override def generate(f: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P(param: RandomLibI) extends MJen[Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P] {
  override def generate(f: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_P())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS(param: RandomLibI) extends MJen[Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS] {
  override def generate(f: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PreState_Container_PS())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container(param: RandomLibI) extends MJen[Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container] {
  override def generate(f: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P(param: RandomLibI) extends MJen[Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P] {
  override def generate(f: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_P())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_RegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS(param: RandomLibI) extends MJen[Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS] {
  override def generate(f: Regulate.Manage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextRegulateManage_Heat_Source_i_thermostat_rt_mhs_mhs_PostState_Container_PS())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container(param: RandomLibI) extends MJen[Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container] {
  override def generate(f: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P(param: RandomLibI) extends MJen[Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P] {
  override def generate(f: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_P())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS(param: RandomLibI) extends MJen[Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS] {
  override def generate(f: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PreState_Container_PS())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container(param: RandomLibI) extends MJen[Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container] {
  override def generate(f: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P(param: RandomLibI) extends MJen[Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P] {
  override def generate(f: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_P())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_RegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS(param: RandomLibI) extends MJen[Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS] {
  override def generate(f: Regulate.Manage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextRegulateManage_Regulator_Interface_i_thermostat_rt_mri_mri_PostState_Container_PS())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container(param: RandomLibI) extends MJen[Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container] {
  override def generate(f: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P(param: RandomLibI) extends MJen[Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P] {
  override def generate(f: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_P())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS(param: RandomLibI) extends MJen[Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS] {
  override def generate(f: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PreState_Container_PS())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container(param: RandomLibI) extends MJen[Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container] {
  override def generate(f: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P(param: RandomLibI) extends MJen[Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P] {
  override def generate(f: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_P())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_RegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS(param: RandomLibI) extends MJen[Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS] {
  override def generate(f: Regulate.Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextRegulateManage_Regulator_Mode_i_thermostat_rt_mrm_mrm_PostState_Container_PS())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_utilContainer(param: RandomLibI) extends MJen[util.Container] {
  override def generate(f: util.Container => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextutilContainer())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_utilEmptyContainer(param: RandomLibI) extends MJen[util.EmptyContainer] {
  override def generate(f: util.EmptyContainer => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextutilEmptyContainer())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}

@record class Gen_runtimemonitorObservationKindType(param: RandomLibI) extends MJen[runtimemonitor.ObservationKind.Type] {
  override def generate(f: runtimemonitor.ObservationKind.Type => Jen.Action): Jen.Action = {
    var continue = Jen.Continue
    while (T) {

      continue = f(param.nextruntimemonitorObservationKindType())

      if (!continue) {
        return Jen.End
      }
    }
    return continue
  }

  override def string: String = {
    return s""
  }
}


