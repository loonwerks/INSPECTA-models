// #Sireum
package isolette.Monitor

import org.sireum._

// Do not edit this file as it will be overwritten if HAMR codegen is rerun

@msig trait Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_Injection_Provider {
  def pre_receiveInput(): Unit
}

object Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_Injection_Service {

  var providers: MSZ[Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_Injection_Provider] = MSZ()

  def register(provider: Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_Injection_Provider): Unit = {
    providers = providers :+ provider
  }

  def pre_receiveInput(): Unit = {
    for (provider <- providers) {
      provider.pre_receiveInput()
    }
  }
}