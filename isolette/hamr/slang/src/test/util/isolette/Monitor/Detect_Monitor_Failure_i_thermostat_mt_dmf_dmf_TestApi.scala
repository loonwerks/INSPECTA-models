// #Sireum

package isolette.Monitor

import org.sireum._
import art.Art
import isolette._

// Do not edit this file as it will be overwritten if HAMR codegen is rerun
@msig trait Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_TestApi {

  def BeforeEntrypoint(): Unit = {
    Art.initTest(Arch.Isolette_Single_Sensor_Instance_thermostat_mt_dmf_dmf)
  }

  def AfterEntrypoint(): Unit = {
    Art.finalizeTest(Arch.Isolette_Single_Sensor_Instance_thermostat_mt_dmf_dmf)
  }

  def testCompute(): Unit = {
    Art.manuallyClearOutput()
    Art.testCompute(Arch.Isolette_Single_Sensor_Instance_thermostat_mt_dmf_dmf)
  }

  def testInitialise(): Unit = {
    Art.manuallyClearOutput()
    Art.testInitialise(Arch.Isolette_Single_Sensor_Instance_thermostat_mt_dmf_dmf)
  }

  /** helper function to check Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf's
   * output ports.  Use named arguments to check subsets of the output ports.
   * @param internal_failure method that will be called with the value of the outgoing data
   *        port 'internal_failure'.
   */
  def check_concrete_output(internal_failure: Isolette_Data_Model.Failure_Flag_i => B): Unit = {
    var testFailures: ISZ[ST] = ISZ()

    val internal_failureValue: Isolette_Data_Model.Failure_Flag_i = get_internal_failure().get
    if(!internal_failure(internal_failureValue)) {
      testFailures = testFailures :+ st"'internal_failure' did not match expected: value of the outgoing data port is ${internal_failureValue}"
    }

    assert(testFailures.isEmpty, st"${(testFailures, "\n")}".render)
  }


  // getter for out DataPort
  def get_internal_failure(): Option[Isolette_Data_Model.Failure_Flag_i] = {
    val value: Option[Isolette_Data_Model.Failure_Flag_i] = get_internal_failure_payload() match {
      case Some(Isolette_Data_Model.Failure_Flag_i_Payload(v)) => Some(v)
      case Some(v) => halt(s"Unexpected payload on port internal_failure.  Expecting 'Isolette_Data_Model.Failure_Flag_i_Payload' but received ${v}")
      case _ => None[Isolette_Data_Model.Failure_Flag_i]()
    }
    return value
  }

  // payload getter for out DataPort
  def get_internal_failure_payload(): Option[Isolette_Data_Model.Failure_Flag_i_Payload] = {
    return Art.observeOutInfrastructurePort(Arch.Isolette_Single_Sensor_Instance_thermostat_mt_dmf_dmf.initialization_api.internal_failure_Id).asInstanceOf[Option[Isolette_Data_Model.Failure_Flag_i_Payload]]
  }

}
