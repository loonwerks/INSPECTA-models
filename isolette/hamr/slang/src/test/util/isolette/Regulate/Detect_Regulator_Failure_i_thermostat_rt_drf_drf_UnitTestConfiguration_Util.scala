// #Sireum

package isolette.Regulate

// Do not edit this file as it will be overwritten if HAMR codegen is rerun

import org.sireum._
import isolette.GumboXUtil.GumboXResult
import isolette.util.{Container, Profile, UnitTestConfigurationBatch}
import isolette.RandomLib
import org.sireum.Random.Impl.Xoshiro256

object Detect_Regulator_Failure_i_thermostat_rt_drf_drf_UnitTestConfiguration_Util {

  def freshRandomLib: RandomLib = {
    return RandomLib(Random.Gen64Impl(Xoshiro256.create))
  }

  val tq: String = "\"\"\""

  type DefaultComputeProfile = Detect_Regulator_Failure_i_thermostat_rt_drf_drf_Profile_P

  def defaultComputeConfig: Detect_Regulator_Failure_i_thermostat_rt_drf_drf_Compute_UnitTestConfiguration = {
    return (Detect_Regulator_Failure_i_thermostat_rt_drf_drf_Compute_UnitTestConfiguration (
      verbose = F,
      name = "Default_Compute_Config",
      description = "Default Compute Configuration",
      numTests = 100,
      numTestVectorGenRetries = 100,
      failOnUnsatPreconditions = F,
      profile = Detect_Regulator_Failure_i_thermostat_rt_drf_drf_Profile_P (
        name = "Compute_Default_Profile",
      ),
      genReplay = (c: Container, testName: String, r: GumboXResult.Type) => Some(
       st"""Replay Unit Test:
            |  test("Replay: $testName") {
            |    val results = isolette.GumboXUtil.GumboXResult.$r
            |    val json = st${tq}${isolette.JSON.fromutilContainer(c, T)}${tq}.render
            |    val testVector = isolette.JSON.toRegulateDetect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P(json).left
            |    assert (testComputeCBV(testVector) == results)
            |  }""".render))
    )
  }
}

@record class Detect_Regulator_Failure_i_thermostat_rt_drf_drf_Compute_UnitTestConfiguration (
  var verbose: B,
  var name: String,
  var description: String,
  var numTests: Z,
  var numTestVectorGenRetries: Z,
  var failOnUnsatPreconditions: B,
  var profile: Detect_Regulator_Failure_i_thermostat_rt_drf_drf_Profile_P_Trait,
  var genReplay: (Container, String, GumboXResult.Type) => Option[String])
  extends UnitTestConfigurationBatch with Detect_Regulator_Failure_i_thermostat_rt_drf_drf_GumboX_TestHarness {

  override def test(c: Container): GumboXResult.Type = {
    return testComputeCBV(c.asInstanceOf[Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container])
  }
}
