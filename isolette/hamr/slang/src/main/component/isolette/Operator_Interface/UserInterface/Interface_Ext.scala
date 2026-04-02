package isolette.Operator_Interface.UserInterface

import isolette.Isolette_Data_Model.TempWstatus_i
import isolette._
import org.sireum._

import java.awt.GraphicsEnvironment
import java.util.concurrent.atomic.AtomicReference

object Interface_Ext {

  // Alternatively could implement gui directly here rather than
  // doing pass-throughs

  val form: Gui = new Gui()

  var lowerDesiredTempWstatus: AtomicReference[Isolette_Data_Model.TempWstatus_i] = new AtomicReference[TempWstatus_i]()
  var upperDesiredTempWstatus: AtomicReference[Isolette_Data_Model.TempWstatus_i] = new AtomicReference[TempWstatus_i]()
  var lowerAlarmTempWstatus: AtomicReference[Isolette_Data_Model.TempWstatus_i] = new AtomicReference[TempWstatus_i]()
  var upperAlarmTempWstatus: AtomicReference[Isolette_Data_Model.TempWstatus_i] = new AtomicReference[TempWstatus_i]()

  def initialise(_lowerDesiredTempWstatus: Isolette_Data_Model.TempWstatus_i,
                 _upperDesiredTempWstatus: Isolette_Data_Model.TempWstatus_i,
                 _lowerAlarmTempWstatus: Isolette_Data_Model.TempWstatus_i,
                 _upperAlarmTempWstatus: Isolette_Data_Model.TempWstatus_i): Unit = {
    lowerDesiredTempWstatus.set(_lowerDesiredTempWstatus)
    upperDesiredTempWstatus.set(_upperDesiredTempWstatus)
    lowerAlarmTempWstatus.set(_lowerAlarmTempWstatus)
    upperAlarmTempWstatus.set(_upperAlarmTempWstatus)

    form.init(
      lowerDesiredTempWstatus.get(),
      upperDesiredTempWstatus.get(),
      lowerAlarmTempWstatus.get(),
      upperAlarmTempWstatus.get())
  }

  def isHeadless: B = {
    return GraphicsEnvironment.isHeadless
  }

  def finalise(): Unit = {
    form.frame.dispose()
  }

  def setRegulatorStatus(v: Option[Isolette_Data_Model.Status.Type]): Unit = {
    form.setRegulatorStatus(v)
  }

  def setMonitorStatus(v: Option[Isolette_Data_Model.Status.Type]): Unit = {
    form.setMonitorStatus(v)
  }

  def setDispayTemperature(v: Option[Isolette_Data_Model.Temp_i]): Unit = {
    form.setDisplayTemperature(v)
  }

  def setAlarmControl(v: Option[Isolette_Data_Model.On_Off.Type]): Unit = {
    form.setAlarmControl(v)
  }


  def getLowerDesiredTempWstatus(): Isolette_Data_Model.TempWstatus_i = {
    return lowerDesiredTempWstatus.get()
  }

  def getUpperDesiredTempWstatus(): Isolette_Data_Model.TempWstatus_i = {
    return upperDesiredTempWstatus.get()
  }

  def getLowerAlarmTempWstatus(): Isolette_Data_Model.TempWstatus_i = {
    return lowerAlarmTempWstatus.get()
  }

  def getUpperAlarmTempWstatus(): Isolette_Data_Model.TempWstatus_i = {
    return upperAlarmTempWstatus.get()
  }
}
