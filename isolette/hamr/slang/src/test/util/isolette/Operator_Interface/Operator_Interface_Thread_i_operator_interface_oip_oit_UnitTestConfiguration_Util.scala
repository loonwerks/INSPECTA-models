// #Sireum

package isolette.Operator_Interface

// Do not edit this file as it will be overwritten if HAMR codegen is rerun

import org.sireum._
import isolette.GumboXUtil.GumboXResult
import isolette.util.{Container, Profile, UnitTestConfigurationBatch}
import isolette.RandomLib
import org.sireum.Random.Impl.Xoshiro256

object Operator_Interface_Thread_i_operator_interface_oip_oit_UnitTestConfiguration_Util {

  def freshRandomLib: RandomLib = {
    return RandomLib(Random.Gen64Impl(Xoshiro256.create))
  }

  val tq: String = "\"\"\""

  type DefaultInitializeProfile = Operator_Interface_Thread_i_operator_interface_oip_oit_Profile

  def defaultInitializeConfig: Operator_Interface_Thread_i_operator_interface_oip_oit_Initialize_UnitTestConfiguration = {
    return (Operator_Interface_Thread_i_operator_interface_oip_oit_Initialize_UnitTestConfiguration (
      verbose = F,
      name = "Default_Initialize_Config",
      description = "Default Initialize Configuration",
      numTests = 100,
      numTestVectorGenRetries = 100,
      failOnUnsatPreconditions = F,
      profile = Operator_Interface_Thread_i_operator_interface_oip_oit_Profile (
        name = "Initialize_Default_Profile",
      ),
      genReplay = (c: Container, testName: String, r: GumboXResult.Type) => None())
    )
  }

  type DefaultComputeProfile = Operator_Interface_Thread_i_operator_interface_oip_oit_Profile_P

  def defaultComputeConfig: Operator_Interface_Thread_i_operator_interface_oip_oit_Compute_UnitTestConfiguration = {
    return (Operator_Interface_Thread_i_operator_interface_oip_oit_Compute_UnitTestConfiguration (
      verbose = F,
      name = "Default_Compute_Config",
      description = "Default Compute Configuration",
      numTests = 100,
      numTestVectorGenRetries = 100,
      failOnUnsatPreconditions = F,
      profile = Operator_Interface_Thread_i_operator_interface_oip_oit_Profile_P (
        name = "Compute_Default_Profile",
        api_alarm_control = freshRandomLib,
        api_display_temperature = freshRandomLib,
        api_monitor_status = freshRandomLib,
        api_regulator_status = freshRandomLib
      ),
      genReplay = (c: Container, testName: String, r: GumboXResult.Type) => Some(
       st"""Replay Unit Test:
            |  test("Replay: $testName") {
            |    val results = isolette.GumboXUtil.GumboXResult.$r
            |    val json = st${tq}${isolette.JSON.fromutilContainer(c, T)}${tq}.render
            |    val testVector = isolette.JSON.toOperator_InterfaceOperator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container_P(json).left
            |    assert (testComputeCBV(testVector) == results)
            |  }""".render))
    )
  }
}

@record class Operator_Interface_Thread_i_operator_interface_oip_oit_Initialize_UnitTestConfiguration (
  var verbose: B,
  var name: String,
  var description: String,
  var numTests: Z,
  var numTestVectorGenRetries: Z,
  var failOnUnsatPreconditions: B,
  var profile: Operator_Interface_Thread_i_operator_interface_oip_oit_Profile_Trait,
  var genReplay: (Container, String, GumboXResult.Type) => Option[String])
  extends UnitTestConfigurationBatch with Operator_Interface_Thread_i_operator_interface_oip_oit_GumboX_TestHarness {

  override def test(c: Container): GumboXResult.Type = {
    return testInitialiseCB()
  }
}

@record class Operator_Interface_Thread_i_operator_interface_oip_oit_Compute_UnitTestConfiguration (
  var verbose: B,
  var name: String,
  var description: String,
  var numTests: Z,
  var numTestVectorGenRetries: Z,
  var failOnUnsatPreconditions: B,
  var profile: Operator_Interface_Thread_i_operator_interface_oip_oit_Profile_P_Trait,
  var genReplay: (Container, String, GumboXResult.Type) => Option[String])
  extends UnitTestConfigurationBatch with Operator_Interface_Thread_i_operator_interface_oip_oit_GumboX_TestHarness {

  override def test(c: Container): GumboXResult.Type = {
    return testComputeCBV(c.asInstanceOf[Operator_Interface_Thread_i_operator_interface_oip_oit_PreState_Container])
  }
}