// #Sireum

package isolette.Monitor

// Do not edit this file as it will be overwritten if HAMR codegen is rerun

import org.sireum._
import isolette.GumboXUtil.GumboXResult
import isolette.util.{Container, Profile, UnitTestConfigurationBatch}
import isolette.RandomLib
import org.sireum.Random.Impl.Xoshiro256

object Manage_Alarm_i_thermostat_mt_ma_ma_UnitTestConfiguration_Util {

  def freshRandomLib: RandomLib = {
    return RandomLib(Random.Gen64Impl(Xoshiro256.create))
  }

  val tq: String = "\"\"\""

  type DefaultInitializeProfile = Manage_Alarm_i_thermostat_mt_ma_ma_Profile

  def defaultInitializeConfig: Manage_Alarm_i_thermostat_mt_ma_ma_Initialize_UnitTestConfiguration = {
    return (Manage_Alarm_i_thermostat_mt_ma_ma_Initialize_UnitTestConfiguration (
      verbose = F,
      name = "Default_Initialize_Config",
      description = "Default Initialize Configuration",
      numTests = 100,
      numTestVectorGenRetries = 100,
      failOnUnsatPreconditions = F,
      profile = Manage_Alarm_i_thermostat_mt_ma_ma_Profile (
        name = "Initialize_Default_Profile",
      ),
      genReplay = (c: Container, testName: String, r: GumboXResult.Type) => None())
    )
  }

  type DefaultComputeProfile = Manage_Alarm_i_thermostat_mt_ma_ma_Profile_P

  def defaultComputeConfig: Manage_Alarm_i_thermostat_mt_ma_ma_Compute_UnitTestConfiguration = {
    return (Manage_Alarm_i_thermostat_mt_ma_ma_Compute_UnitTestConfiguration (
      verbose = F,
      name = "Default_Compute_Config",
      description = "Default Compute Configuration",
      numTests = 100,
      numTestVectorGenRetries = 100,
      failOnUnsatPreconditions = F,
      profile = Manage_Alarm_i_thermostat_mt_ma_ma_Profile_P (
        name = "Compute_Default_Profile",
        api_current_tempWstatus = freshRandomLib,
        api_lower_alarm_temp = freshRandomLib,
        api_monitor_mode = freshRandomLib,
        api_upper_alarm_temp = freshRandomLib
      ),
      genReplay = (c: Container, testName: String, r: GumboXResult.Type) => Some(
       st"""Replay Unit Test:
            |  test("Replay: $testName") {
            |    val results = isolette.GumboXUtil.GumboXResult.$r
            |    val json = st${tq}${isolette.JSON.fromutilContainer(c, T)}${tq}.render
            |    val testVector = isolette.JSON.toMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_P(json).left
            |    assert (testComputeCBV(testVector) == results)
            |  }""".render))
    )
  }

  type DefaultComputewLProfile = Manage_Alarm_i_thermostat_mt_ma_ma_Profile_PS

  def defaultComputewLConfig: Manage_Alarm_i_thermostat_mt_ma_ma_ComputewL_UnitTestConfiguration = {
    return (Manage_Alarm_i_thermostat_mt_ma_ma_ComputewL_UnitTestConfiguration (
      verbose = F,
      name = "Default_ComputewL_Config",
      description = "Default ComputewL Configuration",
      numTests = 100,
      numTestVectorGenRetries = 100,
      failOnUnsatPreconditions = F,
      profile = Manage_Alarm_i_thermostat_mt_ma_ma_Profile_PS (
        name = "ComputewL_Default_Profile",
        In_lastCmd = freshRandomLib,
        api_current_tempWstatus = freshRandomLib,
        api_lower_alarm_temp = freshRandomLib,
        api_monitor_mode = freshRandomLib,
        api_upper_alarm_temp = freshRandomLib
      ),
      genReplay = (c: Container, testName: String, r: GumboXResult.Type) => Some(
       st"""Replay Unit Test:
            |  test("Replay: $testName") {
            |    val results = isolette.GumboXUtil.GumboXResult.$r
            |    val json = st${tq}${isolette.JSON.fromutilContainer(c, T)}${tq}.render
            |    val testVector = isolette.JSON.toMonitorManage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS(json).left
            |    assert (testComputeCBwLV(testVector) == results)
            |  }""".render))
    )
  }
}

@record class Manage_Alarm_i_thermostat_mt_ma_ma_Initialize_UnitTestConfiguration (
  var verbose: B,
  var name: String,
  var description: String,
  var numTests: Z,
  var numTestVectorGenRetries: Z,
  var failOnUnsatPreconditions: B,
  var profile: Manage_Alarm_i_thermostat_mt_ma_ma_Profile_Trait,
  var genReplay: (Container, String, GumboXResult.Type) => Option[String])
  extends UnitTestConfigurationBatch with Manage_Alarm_i_thermostat_mt_ma_ma_GumboX_TestHarness {

  override def test(c: Container): GumboXResult.Type = {
    return testInitialiseCB()
  }
}

@record class Manage_Alarm_i_thermostat_mt_ma_ma_Compute_UnitTestConfiguration (
  var verbose: B,
  var name: String,
  var description: String,
  var numTests: Z,
  var numTestVectorGenRetries: Z,
  var failOnUnsatPreconditions: B,
  var profile: Manage_Alarm_i_thermostat_mt_ma_ma_Profile_P_Trait,
  var genReplay: (Container, String, GumboXResult.Type) => Option[String])
  extends UnitTestConfigurationBatch with Manage_Alarm_i_thermostat_mt_ma_ma_GumboX_TestHarness {

  override def test(c: Container): GumboXResult.Type = {
    return testComputeCBV(c.asInstanceOf[Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container])
  }
}

@record class Manage_Alarm_i_thermostat_mt_ma_ma_ComputewL_UnitTestConfiguration (
  var verbose: B,
  var name: String,
  var description: String,
  var numTests: Z,
  var numTestVectorGenRetries: Z,
  var failOnUnsatPreconditions: B,
  var profile: Manage_Alarm_i_thermostat_mt_ma_ma_Profile_PS_Trait,
  var genReplay: (Container, String, GumboXResult.Type) => Option[String])
  extends UnitTestConfigurationBatch with Manage_Alarm_i_thermostat_mt_ma_ma_GumboX_TestHarness {

  override def test(c: Container): GumboXResult.Type = {
    return testComputeCBwLV(c.asInstanceOf[Manage_Alarm_i_thermostat_mt_ma_ma_PreState_Container_PS])
  }
}