// #Sireum

package base.Gumbo_Structs_Arrays

import org.sireum._
import base._

// This file will not be overwritten so is safe to edit
object ProducerThr_i_producer_producer {

  def initialise(api: ProducerThr_i_Initialization_Api): Unit = {
    // example api usage

    api.logInfo("Example info logging")
    api.logDebug("Example debug logging")
    api.logError("Example error logging")

    api.put_myStructArray(Gumbo_Structs_Arrays.MyStructArray_i.example())
    api.put_MyArrayStruct(Gumbo_Structs_Arrays.MyArrayStruct.example())
  }

  def timeTriggered(api: ProducerThr_i_Operational_Api): Unit = {
    // example api usage


  }

  def finalise(api: ProducerThr_i_Operational_Api): Unit = { }
}
