// #Sireum
package base.SW

import org.sireum._

// Do not edit this file as it will be overwritten if HAMR codegen is rerun

@msig trait ArduPilot_Impl_seL4_ArduPilot_ArduPilot_Injection_Provider {
  def pre_receiveInput(): Unit
}

object ArduPilot_Impl_seL4_ArduPilot_ArduPilot_Injection_Service {

  var providers: MSZ[ArduPilot_Impl_seL4_ArduPilot_ArduPilot_Injection_Provider] = MSZ()

  def register(provider: ArduPilot_Impl_seL4_ArduPilot_ArduPilot_Injection_Provider): Unit = {
    providers = providers :+ provider
  }

  def pre_receiveInput(): Unit = {
    for (provider <- providers) {
      provider.pre_receiveInput()
    }
  }
}