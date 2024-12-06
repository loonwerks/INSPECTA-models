// #Sireum
package isolette.Regulate

import org.sireum._

// Do not edit this file as it will be overwritten if HAMR codegen is rerun

@msig trait Manage_Heat_Source_i_thermostat_rt_mhs_mhs_Injection_Provider {
  def pre_receiveInput(): Unit
}

object Manage_Heat_Source_i_thermostat_rt_mhs_mhs_Injection_Service {

  var providers: MSZ[Manage_Heat_Source_i_thermostat_rt_mhs_mhs_Injection_Provider] = MSZ()

  def register(provider: Manage_Heat_Source_i_thermostat_rt_mhs_mhs_Injection_Provider): Unit = {
    providers = providers :+ provider
  }

  def pre_receiveInput(): Unit = {
    for (provider <- providers) {
      provider.pre_receiveInput()
    }
  }
}