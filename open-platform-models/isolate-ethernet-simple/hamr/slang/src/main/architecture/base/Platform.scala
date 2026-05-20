// #Sireum

package base

import org.sireum._

// This file will not be overwritten if HAMR codegen is rerun

object Platform {

  def setup(): Unit = {
    // BEGIN MARKER PLATFORM SETUP
    {
      // Contributions from GumboX Plugin
      base.runtimemonitor.RuntimeMonitor.init(base.runtimemonitor.ModelInfo.modelInfo)
    }
    // END MARKER PLATFORM SETUP
  }

  def tearDown(): Unit = {
    // BEGIN MARKER PLATFORM TEARDOWN
    {
      // Contributions from GumboX Plugin
      base.runtimemonitor.RuntimeMonitor.finalise()
    }
    // END MARKER PLATFORM TEARDOWN
  }
}