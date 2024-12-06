package isolette

import org.sireum._
import art.Art
import art.scheduling.static._

// This file will not be overwritten so is safe to edit

class SystemTests extends SystemTestSuite {

  // note: this is overriding SystemTestSuite's 'def scheduler: Scheduler'
  //       abstract method
  var scheduler: StaticScheduler = Schedulers.getStaticSchedulerH(MNone())

  def compute(isz: ISZ[Command]): Unit = {
    scheduler = scheduler(commandProvider = ISZCommandProvider(isz :+ Stop()))

    Art.computePhase(scheduler)
  }

  override def beforeEach(): Unit = {
    // uncomment the following to disable the various guis
    //System.setProperty("java.awt.headless", "true")

    // uncomment the following to suppress (or potentially redirect) ART's log stream
    //art.ArtNative_Ext.logStream = new java.io.PrintStream(new java.io.OutputStream {
    //  override def write(b: Int): Unit = {}
    //})

    // uncomment the following to suppress (or potentially redirect) the static scheduler's log stream
    //art.scheduling.static.StaticSchedulerIO_Ext.logStream = new java.io.PrintStream(new java.io.OutputStream {
    //  override def write(b: Int): Unit = {}
    //})

    super.beforeEach()
  }

  // Suggestion: add the following import renamings of the components' SystemTestAPIs,
  //             replacing nickname with shortened versions that are easier to reference
  // import isolette.Regulate.{Manage_Regulator_Interface_i_thermostat_rt_mri_mri_SystemTestAPI => nickname}
  // import isolette.Regulate.{Manage_Heat_Source_i_thermostat_rt_mhs_mhs_SystemTestAPI => nickname}
  // import isolette.Regulate.{Manage_Regulator_Mode_i_thermostat_rt_mrm_mrm_SystemTestAPI => nickname}
  // import isolette.Regulate.{Detect_Regulator_Failure_i_thermostat_rt_drf_drf_SystemTestAPI => nickname}
  // import isolette.Monitor.{Manage_Monitor_Interface_i_thermostat_mt_mmi_mmi_SystemTestAPI => nickname}
  // import isolette.Monitor.{Manage_Alarm_i_thermostat_mt_ma_ma_SystemTestAPI => nickname}
  // import isolette.Monitor.{Manage_Monitor_Mode_i_thermostat_mt_mmm_mmm_SystemTestAPI => nickname}
  // import isolette.Monitor.{Detect_Monitor_Failure_i_thermostat_mt_dmf_dmf_SystemTestAPI => nickname}
  // import isolette.Operator_Interface.{Operator_Interface_Thread_i_operator_interface_oip_oit_SystemTestAPI => nickname}
  // import isolette.Devices.{Temperature_Sensor_i_temperature_sensor_cpi_thermostat_SystemTestAPI => nickname}
  // import isolette.Devices.{Heat_Source_i_heat_source_cpi_heat_controller_SystemTestAPI => nickname}

  test("Example system test") {
    // run the initialization phase
    Art.initializePhase(scheduler)

    // run components' compute entrypoints through one hyper-period
    compute(ISZ(Hstep(1)))

    // use the component SystemTestAPIs' put methods to change the prestate values for components
    // TODO

    // run another hyper-period
    compute(ISZ(Hstep(1)))

    // use the component SystemTestAPIs' check or get methods to check the poststate values for components
    // TODO
  }
}
