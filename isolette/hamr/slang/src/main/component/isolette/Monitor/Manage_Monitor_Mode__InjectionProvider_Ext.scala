package isolette.Monitor

import org.sireum
import org.sireum.$internal.MutableMarker
import isolette.Isolette_Data_Model.{Failure_Flag_i, Failure_Flag_i_Payload}

object Manage_Monitor_Mode__InjectionProvider_Ext {

  def init(): Unit = {
    Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_Injection_Service.register(
      new Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_Injection_Provider {
        override def pre_receiveInput(): Unit = {
          // simulate the arrival of data on unconnected internal_failure incoming data port
          val internal_failure_id = isolette.Arch.Isolette_Single_Sensor_Instance_thermostat_mt_mmm_mmm.initialization_api.internal_failure_Id
          art.Art.insertInInfrastructurePort(internal_failure_id, Failure_Flag_i_Payload(Failure_Flag_i(false)))
        }

        override def string: sireum.String = toString

        override def $clonable: Boolean = false

        override def $clonable_=(b: Boolean): MutableMarker = this

        override def $owned: Boolean = false

        override def $owned_=(b: Boolean): MutableMarker = this

        override def $clone: MutableMarker = this
      }
    )
  }
}