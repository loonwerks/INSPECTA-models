// #Sireum

package isolette.Operator_Interface.UserInterface

import org.sireum._
import isolette._

@ext object Interface {
  def isHeadless: B = $

  def initialise(_lowerDesiredTempWstatus: Isolette_Data_Model.TempWstatus_i,
                 _upperDesiredTempWstatus: Isolette_Data_Model.TempWstatus_i,
                 _lowerAlarmTempWstatus: Isolette_Data_Model.TempWstatus_i,
                 _upperAlarmTempWstatus: Isolette_Data_Model.TempWstatus_i): Unit = $

  def finalise(): Unit = $

  def setRegulatorStatus(v: Option[Isolette_Data_Model.Status.Type]): Unit = $
  def setMonitorStatus(v: Option[Isolette_Data_Model.Status.Type]): Unit = $
  def setDispayTemperature(v: Option[Isolette_Data_Model.Temp_i]): Unit = $
  def setAlarmControl(v: Option[Isolette_Data_Model.On_Off.Type]): Unit = $

  def getLowerDesiredTempWstatus(): Isolette_Data_Model.TempWstatus_i = $
  def getUpperDesiredTempWstatus(): Isolette_Data_Model.TempWstatus_i = $
  def getLowerAlarmTempWstatus(): Isolette_Data_Model.TempWstatus_i = $
  def getUpperAlarmTempWstatus(): Isolette_Data_Model.TempWstatus_i = $
}