// #Sireum

package firewall.runtimemonitor

import org.sireum._
import art.scheduling.static.CommandProvider

// Do not edit this file as it will be overwritten if HAMR codegen is rerun

object HamrVision {

  var cp: MOption[CommandProvider] = MNone()

  def getCommandProvider(): CommandProvider = {
    cp = MSome(HamrVisionRuntimeMonitorI.getCommandProvider())
    return cp.get
  }
}

@ext object HamrVisionRuntimeMonitorI {
  def getCommandProvider(): CommandProvider = $
}