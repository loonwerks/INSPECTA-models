// #Sireum

package base.runtimemonitor

import org.sireum._
import art.Art.BridgeId

// Do not edit this file as it will be overwritten if HAMR codegen is rerun

@msig trait RuntimeMonitorListener {
  def init(modelInfo: ModelInfo): Unit

  def finalise(): Unit

  /**
    * Called before the initialise entrypoint calls sendOutput
    */
  def observeInitialisePostState(bridgeId: BridgeId, observationKind: ObservationKind.Type, post: art.DataContent): Unit

  /**
    * Called after the compute entrypoint calls receiveInput and before it
    * invokes the behavior code
    */
  def observeComputePreState(bridgeId: BridgeId, observationKind: ObservationKind.Type, pre: Option[art.DataContent]): Unit

  /**
    * Called after the compute entrypoint calls receiveInput and before it
    * invokes the behavior code
    */
  def observeComputePrePostState(bridgeId: BridgeId, observationKind: ObservationKind.Type, pre: Option[art.DataContent], post: art.DataContent): Unit
}

@ext object RuntimeMonitor {

  def registerListener(listener: RuntimeMonitorListener): Unit = $

  def init(modelInfo: ModelInfo): Unit = $

  def finalise(): Unit = $

  def observeInitialisePostState(bridgeId: BridgeId, observationKind: ObservationKind.Type, post: art.DataContent): Unit = $

  def observeComputePreState(bridgeId: BridgeId, observationKind: ObservationKind.Type, pre: Option[art.DataContent]): Unit = $

  def observeComputePrePostState(bridgeId: BridgeId, observationKind: ObservationKind.Type, pre: Option[art.DataContent], post: art.DataContent): Unit = $
}