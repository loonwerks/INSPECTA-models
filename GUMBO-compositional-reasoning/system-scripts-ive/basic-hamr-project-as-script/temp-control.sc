// #Sireum #Logika -- #Sireum indicates file is in Slang, #Logika tells the IVE to apply Logika checking
//
import org.sireum._

def skip(): Unit = {
  Contract(
    Modifies()
  )
}

//===============================================
//  D a t a    T y p e s
//
//  To mock up HAMR/Slang system for system analysis,
//  include all data type definitions and their
//  invariants.
//
//  In the complete verification framework, we would
//  import all base types and application defined types
//  directly.
//===============================================

// Mock up base types.  For prototyping, use Z instead of F32
// for temperature values to make the analysis more efficient.
//
type Integer = Z
def Integer_example(): Integer = { return z"0" }

object Inf {
  // create a dummy type to represent HAMR events (for AADL event ports)
  @datatype class Event {}

  val event: Event = Event()
}

// use a simplified temperature type (degrees as Z instead of F32)
@datatype class Temperature_i(
                               degrees : Integer) {
  @spec def temperature_Inv = Invariant(
    degrees >= -459
  )
}

@enum object FanCmd {
  "On"
  "Off"
}

//===============================================
//  E n v i r o n m e n t
//
//   These represent externally readable/writable
//   states that could be
//     - constrained and queried by Logika
//     - interacted with in testing
//
//   In the full verification framework, we should have an
//  approach for specifying modeling environment conditions, probably
//  tied to the notion of extensions.
//
//  For now, I have hacked component contracts to reference
//  environment conditions.
//===============================================

object Env {
  var tempEnv: Z = 72
  var fanEnv: FanCmd.Type = FanCmd.Off
}

object App {
  //===============================================
  //          T e m p   S e n s o r
  //===============================================

  // ---- P o r t s

  // --- Port - current Temp ---

  // declare global variable to model port queue
  // (this can eventually be switched to a spec var)
  var currentTemp_Port_TSOut: Temperature_i = Temperature_i(z"72")

  // Integration constraint representation
  //
  // Mock up representation of GUMBO integration contracts that
  // are generated in a component's bridge/API file
  //
  //   ToDo: Add in full strict pure predicate definitions as in GUMBO-generated files
  @strictpure def currentTemp_Port_TSOut_IC(currentTemp: Temperature_i): B =
    currentTemp.degrees >= -50 & currentTemp.degrees <= 150

  @spec def currentTemp_Port_TSOut_Inv = Invariant(
    currentTemp_Port_TSOut_IC(currentTemp_Port_TSOut)
  )

  // --- Port - tempChanged ---
  var tempChanged_Port_TSOut: Option[Inf.Event] = None()

  //-------------------------------------------------
  //  I n i t i a l i z e    E n t r y    P o i n t
  //-------------------------------------------------
  def initialise_EP_TS(): Unit = {
    Contract(
      // in general, each component with output event ports needs to be
      // able to assume that the application ports are empty at the start of the entry point
      Requires(tempChanged_Port_TSOut.isEmpty), // this should be added automatically by GUMBO translation, not by infrastructure ??
      Modifies(currentTemp_Port_TSOut),
      Ensures(currentTemp_Port_TSOut == Temperature_i(z"72"),
        tempChanged_Port_TSOut.isEmpty
      )
    )
    // contract only
    halt("contract only")
  }

  //-------------------------------------------------
  //  C o m p u t e    E n t r y    P o i n t
  //-------------------------------------------------

  @strictpure def tempBounding(t: Z): Z = if (t > 150) 150 else (if (t < -50) -50 else t)

  def compute_EP_TS(): Unit = {
    Contract(
      Requires(tempChanged_Port_TSOut.isEmpty),
      Modifies(currentTemp_Port_TSOut, tempChanged_Port_TSOut),
      Ensures(currentTemp_Port_TSOut == Temperature_i(tempBounding(Env.tempEnv)),
        tempChanged_Port_TSOut == Some[Inf.Event](Inf.event))
    )
    // contract only
    halt("Contract only")
  }

  def DIR_compute_EP_TS(): Unit = {
    // no pre-amble (currently) because there are no input ports or clear or setup
    compute_EP_TS()
  }


  //===============================================
  //          T e m p   C o n t r o l
  //===============================================

  // ---- P o r t s

  // --- Port - current Temp ---
  // Integration constraint representation
  //
  //   ToDo: Add in full strict pure predicate definitions as in GUMBO-generated files
  @strictpure def currentTemp_Port_TCIn_IC(currentTemp: Temperature_i): B =
    currentTemp.degrees >= -70 & currentTemp.degrees <= 180

  var currentTemp_Port_TCIn: Temperature_i = Temperature_i(z"72")

  @spec def currentTemp_Port_TCIn_Inv = Invariant(
    currentTemp_Port_TCIn_IC(currentTemp_Port_TSOut)
  )

  // --- Port - tempChanged ---
  var tempChanged_Port_TCIn: Option[Inf.Event] = None()

  // --- Port - fanCmd ---

  var fanCmd_Port_TCOut: Option[FanCmd.Type] = None()

  // ---- L o c a l   S t a t e

  var currentFanState: FanCmd.Type = FanCmd.Off
  var latestTemp: Temperature_i = Temperature_i(z"72")
  // ToDo: John - illustrate local state invariant concept

  //-------------------------------------------------
  //  I n i t i a l i z e    E n t r y    P o i n t
  //-------------------------------------------------
  def initialise_EP_TC(): Unit = {
    Contract(
      Requires(fanCmd_Port_TCOut == None[FanCmd.Type]()),
      Modifies(latestTemp, currentFanState),
      Ensures(
        currentFanState == FanCmd.Off,
        latestTemp == Temperature_i(z"72")
      )
    )
    // contract only
    halt("Contract only")
  }

  //-------------------------------------------------
  //  C o m p u t e    E n t r y    P o i n t
  //-------------------------------------------------

  def handle_tempChanged(): Unit = {
    Contract(
      Requires(fanCmd_Port_TCOut == None[FanCmd.Type]()),
      Modifies(
        currentFanState,
        latestTemp,
        fanCmd_Port_TCOut
      ),
      Ensures(
        // ---- control laws ----
        // currentSetPoint.low.degrees == 70.0f && currentSetPoint.high.degrees == 80.0f
        // ToDo: hard coding setPoint for now
        (latestTemp.degrees < 70) ->: (currentFanState == FanCmd.Off),
        (latestTemp.degrees > 80) ->: (currentFanState == FanCmd.On),
        (latestTemp.degrees >= 70 & latestTemp.degrees <= 80) ->: (currentFanState == In(currentFanState)),
        // (In(currentFanState) != currentFanState) ->: (api.fanCmd.nonEmpty && api.fanCmd.get == currentFanState) && (currentFanState == In(currentFanState)) ->: api.fanCmd.isEmpty && (In(currentFanState) != currentFanState) ->: api.fanCmd.nonEmpty,
        // guarantees tempChanged
        // ---- internal state ----
        latestTemp == currentTemp_Port_TCIn,
        // ---- port output ----
        (In(currentFanState) != currentFanState) ->: (fanCmd_Port_TCOut.nonEmpty && fanCmd_Port_TCOut.get == currentFanState),
        (In(currentFanState) == currentFanState) ->: fanCmd_Port_TCOut.isEmpty
      )
    )
    // contract only
    halt("Contract only")
  }


  //--------------------------------------------
  //
  //  p e r f o r m _ f a n _ c o n t r o l
  //
  //  Helper function to control the fan after
  //    change to current temperature, or
  //    change to set point.
  //
  //--------------------------------------------

  def perform_fan_control(): Unit = {
    Contract(
      // For now we need to manually specify that we assume that output event ports are empty at start of method.
      // This is because if we do not call "put value" on them, we need to be able to conclude that the ports are still empty
      // in the post-condition.
      Requires(fanCmd_Port_TCOut == None[FanCmd.Type]()),
      Modifies(
        currentFanState,
        fanCmd_Port_TCOut),
      Ensures(
        // post-conditions - control laws
        (latestTemp.degrees < 70) ->: (currentFanState == FanCmd.Off),
        (latestTemp.degrees > 80) ->: (currentFanState == FanCmd.On),
        (latestTemp.degrees >= 70 & latestTemp.degrees <= 80) ->: (currentFanState == In(currentFanState)),
        // ---- port output ----
        (In(currentFanState) != currentFanState) ->: (fanCmd_Port_TCOut.nonEmpty && fanCmd_Port_TCOut.get == currentFanState),
        (In(currentFanState) == currentFanState) ->: fanCmd_Port_TCOut.isEmpty
      )
    )
    // contract only
    halt("Contract only")
  }

  //-----------------------------------------------
  //  Set Port State for TC
  //    (auto-generated)
  //-----------------------------------------------

  def set_port_state_TC(): Unit = {
    Contract(
      Modifies(fanCmd_Port_TCOut),
      Ensures(fanCmd_Port_TCOut == None[FanCmd.Type]())
    )
    fanCmd_Port_TCOut = None[FanCmd.Type]()
  }

  //-----------------------------------------------
  //  Compute EP Wrapper (dispatch logic and queuing)
  //    (auto-generated)
  //-----------------------------------------------


  // 0: compute EP should not be executed
  // 1: tempChanged port triggered dispatch
  @strictpure def compute_EP_TC_dispatch_selection(tempChanged_param: Option[Inf.Event]): Z = {
    if (tempChanged_param.nonEmpty) 1 else 0
  }

  def DIR_compute_EP_TC(): Unit = {
    Contract(
      // ToDo: Need logic for clearing ports
      Requires(fanCmd_Port_TCOut == None[FanCmd.Type]()),
      Modifies(
        currentFanState,
        latestTemp,
        fanCmd_Port_TCOut,
        tempChanged_Port_TCIn // this port will potentially be dequeued
      ),
      Ensures(
        // ToDo: Prototyping, for now include the dispatch logic constraints directly in the
        //  contracts.  Later on this should go in model code representing steps in the operational semantics.
        ((compute_EP_TC_dispatch_selection(In(tempChanged_Port_TCIn)) == 1) -->:
          (// ---- control laws ----
            // currentSetPoint.low.degrees == 70.0f && currentSetPoint.high.degrees == 80.0f
            // ToDo: hard coding setPoint for now
            ((latestTemp.degrees < 70) ->: (currentFanState == FanCmd.Off)) &
              ((latestTemp.degrees > 80) ->: (currentFanState == FanCmd.On)) &
              ((latestTemp.degrees >= 70 & latestTemp.degrees <= 80) ->: (currentFanState == In(currentFanState))) &
              // ---- update internal state ----
              latestTemp == currentTemp_Port_TCIn &
              // ---- port output ----
              ((In(currentFanState) != currentFanState) -->: (fanCmd_Port_TCOut.nonEmpty && fanCmd_Port_TCOut.get == currentFanState)) &
              ((In(currentFanState) == currentFanState) -->: fanCmd_Port_TCOut.isEmpty) &
              (tempChanged_Port_TCIn == None[Inf.Event]()) // infrastructure
            )
          ), // ToDo: DEQUEUE - this should go in infrastructure contract
        // ToDo: ** This logic should be reflected in the DIR implementation
        //   This logic handles the case where we did not dispatch due to tempChanged.
        ((compute_EP_TC_dispatch_selection(In(tempChanged_Port_TCIn)) == 0) -->:
          (currentFanState == In(currentFanState) & // local variable doesn't change
            (latestTemp == In(latestTemp)) & // local variable doesn't change
            (fanCmd_Port_TCOut == In(fanCmd_Port_TCOut)) & // output port state doesn't change
            (tempChanged_Port_TCIn == In(tempChanged_Port_TCIn)))) // output port state doesn't change
      )
    )
    //  if (tempChanged_Port_TCIn.nonEmpty) {
    //     handle_tempChanged();
    //     tempChanged_Port_TCIn = None[Event]() // dequeue from dispatch trigger
    //  }
    compute_EP_TC_dispatch_selection(tempChanged_Port_TCIn) match {
      case 0 =>
      case 1 =>
        handle_tempChanged();
        tempChanged_Port_TCIn = None[Inf.Event]() // dequeue from dispatch trigger
    }
  }


  //===============================================
  //          F a n
  //===============================================

  // ---- P o r t s

  // --- Port - fanCmd ---
  var fanCmd_Port_FIn: Option[FanCmd.Type] = None[FanCmd.Type]()

  //-------------------------------------------------
  //  I n i t i a l i z e    E n t r y    P o i n t
  //-------------------------------------------------
  def initialise_EP_F(): Unit = {
    Contract(
      Modifies(Env.fanEnv),
      Ensures(
        Env.fanEnv == FanCmd.Off
      )
    )
    // contract only
    halt("Contract only")
  }

  //-------------------------------------------------
  //  C o m p u t e    E n t r y    P o i n t
  //-------------------------------------------------

  def handle_fanCmd(value: FanCmd.Type): Unit = {
    Contract(
      Modifies(Env.fanEnv),
      Ensures(Env.fanEnv == value)
    )
    // contract only
    halt("Contract only")
  }

  //-----------------------------------------------
  //  Set Port State for TC
  //    (auto-generated)
  //-----------------------------------------------

  def set_port_state_F(): Unit = {
    Contract(
      Modifies()
    )
  }

  //-----------------------------------------------
  //  Compute EP Wrapper (dispatch logic and queuing)
  //    (auto-generated)
  //-----------------------------------------------

  //@datatype class dispatch_status_F()
  //@datatype class not_enabled_F() extends dispatch_status_F
  //@datatype class event_triggered_F(value: FanCmd.Type) extends dispatch_status_F

  // 0: compute EP should not be executed
  // 1: tempChanged port triggered dispatch
  @strictpure def compute_EP_F_dispatch_selection(fanCmd_param: Option[FanCmd.Type]): Z = {
    if (fanCmd_param.nonEmpty) 1 else 0
  }

  def DIR_compute_EP_F(): Unit = {
    Contract(
      Requires(),
      Modifies(
        Env.fanEnv,
        fanCmd_Port_FIn // this port will potentially be dequeued
      ),
      Ensures(
        ((compute_EP_F_dispatch_selection(In(fanCmd_Port_FIn)) == 1) -->:
          // ToDo: parameter extraction for handler
          (Env.fanEnv == In(fanCmd_Port_FIn).get)),
        // ToDo: this logic should go in the DIR/infrastructure code
        ((compute_EP_F_dispatch_selection(In(fanCmd_Port_FIn)) == 0) -->:
          (Env.fanEnv == In(Env.fanEnv) &
            fanCmd_Port_FIn == In(fanCmd_Port_FIn)))
      )
    )
    compute_EP_F_dispatch_selection(fanCmd_Port_FIn) match {
      case 0 =>
      case 1 =>
        handle_fanCmd(fanCmd_Port_FIn.get);
        fanCmd_Port_FIn = None[FanCmd.Type]() // dequeue from dispatch trigger
    }
    //  if (fanCmd_Port_FIn.nonEmpty) {
    //    handle_fanCmd(fanCmd_Port_FIn.get);
    //    fanCmd_Port_FIn = None[FanCmd.Type]() // dequeue from dispatch trigger
    //  } else {
    //    skip()
    //  }
  }
}

//=========================================================================================
//-----------------------------------------------------------------------------------------
//  I n t e g r a t i o n     L o g i c
//-----------------------------------------------------------------------------------------
//=========================================================================================

//======== Integration Constraint Compatibility =========

@pure def currentTemp_Port_TSOut_I_currentTemp_Port_TCIn(v: Temperature_i): Unit = {
  Deduce(
    // for all values v flowing across the connection from TempSensor.currentTemp to TempControl.currentTemp,
    //  if v satisfies the ensures port integration constraints in TempSensor,
    //  then v satisfies the requires port integration constraints in TempControl
    // TempSensor_i_Api.currentTemp_PAPred(v) |- TempControl_i_Api.currentTemp_PAPred(v)
    App.currentTemp_Port_TSOut_IC(v) |- App.currentTemp_Port_TCIn_IC(v)
  )
}

//====================================================
//  C o m m u n i c a t i o n
//====================================================

object Comm {
  @strictpure def emptyInputEventPortsProp(p_tempChanged_Port_TCIn: Option[Inf.Event],
                                           p_fanCmd_Port_Fin: Option[FanCmd.Type]): B = (
    p_tempChanged_Port_TCIn == None[Inf.Event]()
      & p_fanCmd_Port_Fin == None[FanCmd.Type]()
    )

  @strictpure def emptyOutputEventPortsProp(p_tempChanged_Port_TSOut: Option[Inf.Event],
                                            p_fanCmd_Port_TCOut: Option[FanCmd.Type]): B = (
    p_tempChanged_Port_TSOut == None[Inf.Event]()
      & p_fanCmd_Port_TCOut == None[FanCmd.Type]())


  def TS_to_TC_tempChanged(): Unit = {
    //  Contract(
    //    Modifies(tempChanged_Port_TSOut, tempChanged_Port_TCIn),
    //    Ensures(
    //      tempChanged_Port_TCIn == In(tempChanged_Port_TSOut),  // enqueue tempChanged at TC
    //      tempChanged_Port_TSOut == None[Event]()              // dequeue tempChanged from TS
    //    )
    //  )
    // Note: in general, for this equality to be sound, we would need to prove the absence of a fan in
    // structure for this port,
    //   which we can prove with static analysis of model
    App.tempChanged_Port_TCIn = App.tempChanged_Port_TSOut;
    App.tempChanged_Port_TSOut = None[Inf.Event]();
  }

  def TS_to_TC_currentTemp(): Unit = {
    //  Contract(
    //    Modifies(currentTemp_Port_TCIn),
    //    Ensures(
    //      currentTemp_Port_TCIn == currentTemp_Port_TSOut      // propagate
    //    )
    //  )
    App.currentTemp_Port_TCIn = App.currentTemp_Port_TSOut
  }

  def TC_to_Fan_fanCmd(): Unit = {
    //  Contract(
    //    Modifies(fanCmd_Port_FIn, fanCmd_Port_TCOut),
    //    Ensures(
    //      fanCmd_Port_FIn == In(fanCmd_Port_TCOut),      // propagate
    //      fanCmd_Port_TCOut == None[FanCmd.Type]()
    //    )
    //  )
    App.fanCmd_Port_FIn = App.fanCmd_Port_TCOut;
    App.fanCmd_Port_TCOut = None[FanCmd.Type]();
  }
}

//=================================================================================
//  D e v e l o p e r  -   D e f i n e d      P r o p e r t i e s
//=================================================================================

//------ initialization phase post-condition stated in various ways -------

// Inituitively, this property should constrain each output port and each thread local variable
@strictpure def initPhasePostConditionProp(p_currentTemp_Port_TSOut: Temperature_i,
                                           p_tempChanged_Port_TSOut: Option[Inf.Event],
                                           //
                                           p_currentTemp_Port_TCIn: Temperature_i,
                                           p_tempChanged_Port_TCIn: Option[Inf.Event],
                                           p_fanCmd_Port_TCOut: Option[FanCmd.Type],
                                           p_currentFanState: FanCmd.Type,
                                           p_latestTemp: Temperature_i,
                                           //
                                           p_fanCmd_Port_FIn: Option[FanCmd.Type],
                                           p_fanEnv: FanCmd.Type   // ToDo: is this environment variable appropriate here?
                                          ): B = (
  // Temp Sensor Constraints
  p_currentTemp_Port_TSOut == Temperature_i(72)
    & p_tempChanged_Port_TSOut == None[Inf.Event]()       // infrastructure
    // Temp Control Constraints
    & p_currentTemp_Port_TCIn == Temperature_i(72)
    & p_tempChanged_Port_TCIn == None[Inf.Event]()       // infrastructure
    & p_fanCmd_Port_TCOut == None[FanCmd.Type]()     // infrastructure
    & p_currentFanState == FanCmd.Off
    & p_latestTemp == Temperature_i(z"72")
    // Fan component
    & p_fanCmd_Port_FIn == None[FanCmd.Type]()       // infrastructure
    & p_fanEnv == FanCmd.Off)

def initPhasePostCondition(): Unit = {
  Contract(
    Requires(
      // TS component
      App.currentTemp_Port_TSOut == Temperature_i(72),
      App.tempChanged_Port_TSOut == None[Inf.Event](),
      // TC component
      App.currentTemp_Port_TCIn == Temperature_i(72),
      App.fanCmd_Port_TCOut == None[FanCmd.Type](),
      App.currentFanState == FanCmd.Off,
      App.latestTemp == Temperature_i(z"72"),
      // Fan component
      App.fanCmd_Port_FIn == None[FanCmd.Type](),
      Env.fanEnv == FanCmd.Off
    )
  )
}




//==================================================================================
//  S y s t e m     C o m p o s i t i o n
//==================================================================================

// Logical specification of the initial system state.  Generated automatically
// from system description.
// One of the main points here is that all event ports are empty
def initialSystemState() : Unit = {
  Contract(
    Modifies(
      App.currentTemp_Port_TSOut, App.tempChanged_Port_TSOut,
      App.currentTemp_Port_TCIn, App.tempChanged_Port_TCIn, App.fanCmd_Port_TCOut,
      App.currentFanState, App.latestTemp,
      App.fanCmd_Port_FIn
    ),
    Ensures(
      // This post-condition will have the following effect
      //  --- Temp Sensor ---
      // currentTemp_Port_TSOut == Random(Temperature_i)
      App.tempChanged_Port_TSOut == None[Inf.Event](),
      //  --- Temp Control ---
      // currentTemp_Port_TCIn == Random(Temperature_i)
      App.tempChanged_Port_TCIn == None[Inf.Event](),
      App.fanCmd_Port_TCOut == None[FanCmd.Type](),
      // currentFanState == Random(FanCmd.Type)
      // latestTemp == Random(Temperature_i)
      //  --- Fan ---
      App.fanCmd_Port_FIn == None[FanCmd.Type]()
    )
  )
  halt("Contract only")
}

// ---------------------------------
//   I n i t i a l i z a t i o n    P h a s e     V e r i f i c a t i o n
// ---------------------------------

// The method below, which provides a "script" characterizing the semantics of the initialization phase,
// would be automatically generated by the framework.
// The user would not modify it.  Instead, the user would tweak
// the AADL-level contracts (primarily the initialize entry point contracts) and initialization phase
// post-condition until the method below verifies correctly under interprocedural verification.


def initPhase() : Unit = {
  // === init phase pre-condition (different strategies) ===
  // Give a logical characterization of the initial state using one of the two following equivalent strategies
  // [1] "Ensure" the pre-condition in a method contract
  //     (from the calling context perspective, this has the effect of assuming the post-condition)
  initialSystemState()
  // [2] "Assume" the pre-condition
  // assume(emptyInputEventPortsProp(tempChanged_Port_TCIn,fanCmd_Port_FIn))
  // assume(emptyOutputEventPortsProp(tempChanged_Port_TSOut,fanCmd_Port_TCOut))
  // === run static schedule of system init phase ===
  //     (round-robin of each component entry point)
  App.initialise_EP_TS()
  App.initialise_EP_TC()
  App.initialise_EP_F()
  // === communication ===
  Comm.TS_to_TC_tempChanged()
  Comm.TS_to_TC_currentTemp() // Demo: comment out this line to seed error (input port of TC not initialized)
  Comm.TC_to_Fan_fanCmd()
  // === init phase post-condition (different strategies) ===
  //  [1] "Require" the post-condition in a method contract
  initPhasePostCondition()
  // [2] "Assert" the post-condition
  //  assert(initPhasePostConditionProp(
  //    currentTemp_Port_TSOut, tempChanged_Port_TSOut,
  //    currentTemp_Port_TCIn, tempChanged_Port_TCIn, fanCmd_Port_FIn, currentFanState, latestTemp,
  //    fanCmd_Port_FIn, fanEnv))
}

/* Reflect the semantics of a single hyperperiod.
   NOTE: This method is not meant to be called directly, but instead is referenced from a
   "Scenario" method, in which the state of the system BEFORE the hyper-period is executed is set.
 */
def hyperperiodImmediateComm(): Unit = {
  // ======= Unfold system execution according to the static schedule ==========
  // --------- Temp Sensor ---------
  App.DIR_compute_EP_TS()
  // Debugging:
  // assert(!tempChanged_Port_TSOut.isEmpty)
  // TS -> TC communication
  Comm.TS_to_TC_tempChanged()
  Comm.TS_to_TC_currentTemp()
  //
  // Debugging: tempEnv value flows to TC currentTemp input port
  //  assert(currentTemp_Port_TCIn.degrees > 81 & currentTemp_Port_TCIn.degrees < 100)
  //  assert(tempChanged_Port_TCIn.nonEmpty)  // tempChanged event has been set

  // --------- Temp Control ---------
  //  set_port_state_TC() // should correspond to a set in the operational semantics rules
  App.DIR_compute_EP_TC()
  //
  // Debugging: in this scenario, a fan should have been set to turn the fan On
  // assert(fanCmd_Port_TCOut.nonEmpty && fanCmd_Port_TCOut.get == FanCmd.On)
  //
  Comm.TC_to_Fan_fanCmd()
  //
  // Debugging: following on the above, the command to turn the fan on should have been delivered
  // assert(fanCmd_Port_FIn.nonEmpty && fanCmd_Port_FIn.get == FanCmd.On)

  // --------- Fan ---------
  //  set_port_state_F()
  App.DIR_compute_EP_F()
  //
  // ============= end unfolding ===============
}



//===========================================================================================
//  Symbolic Test Scenarios
//
//===========================================================================================

// ----------------- Scenario 1 --------------------
//  PRE:
//   temp in (81,...,100)
//   fan is OFF
//  POST:
//   fan is ON
def env_scenario01pre(): Unit = {
  Contract(
    Modifies(Env.tempEnv,Env.fanEnv),
    Ensures(Env.tempEnv > 81 & Env.tempEnv < 100,
      Env.fanEnv == FanCmd.Off)
  )
  // contract only
  skip()
  // abstractly
  //   tempEnv = Z.random; assume(tempEnv > 81 & tempEnv < 100)
  //   fanEnv = FanCmd.Off
}

def env_scenario01post(): Unit = {
  Contract(
    // Requires(Env.fanEnv == FanCmd.Off)
    Requires(Env.fanEnv == FanCmd.On)
  )
  skip()
}

def scenario01_FanOffToFanOn() : Unit = {
  //  Contract(
  //    Modifies(
  //      Env.tempEnv,
  //      currentTemp_Port_TSOut,tempChanged_Port_TSOut,
  //      currentTemp_Port_TCIn,tempChanged_Port_TCIn, fanCmd_Port_TCOut,
  //      currentFanState, latestTemp,
  //      fanCmd_Port_FIn,
  //      Env.fanEnv)
  //  )
  // ========= inital state for system slice ==========
  // In this case, our slice starts with the beginning of the compute phase, and so we begin with the
  //  state characterized by the initialization phase post-condition
  //
  // Assume the initialize phase post-condition (this characterizes the initial state for the compute phase)
  //   This step should address all port state and component local variables by
  //    - giving them unconstrained (or otherwise appropropriate initial values)
  //    - constraining the variables above according to the user supplied initPhasePostCondition
  assume(initPhasePostConditionProp(
    // TempSensor port states
    App.currentTemp_Port_TSOut, App.tempChanged_Port_TSOut,
    // TempControl port states
    App.currentTemp_Port_TCIn, App.tempChanged_Port_TCIn, App.fanCmd_Port_TCOut,
    //    component local variables
    App.currentFanState, App.latestTemp,
    // Fan port states
    App.fanCmd_Port_FIn, Env.fanEnv))

  // --------- Scenario inputs (assumptions) -----------
  // Within a system slice, we may want to break things up in cases, to verify key system properties of interest.
  // For each such "scenario" we indicate desired constraints that characterize the inputs (scenario pre-conditions)
  //  and the verify that constraints satisfying the output (scenario post-conditions) are satisfied.
  //
  // Note that in the envisioned verification framework, this should not be accomplished by "assuming" the scenario
  // pre-condition, because we would have already generated assumptions about the initial/default values.
  // Rather, we would "override" with abstract values (ToDo: further develop this idea when it applies to environment model variables).
  //
  // Establish the input test conditions (logical characterization of "test" values)
  // establish constraints on system inputs
  //  tempEnv = Abs[Z](a => a > 81 & a < 100)
  env_scenario01pre()

  hyperperiodImmediateComm()

  //  check system test post-condition
  env_scenario01post()
}

// ----------------- Scenario 2 ------------------
//  PRE:
//   temp in (60,...,69)
//   fan is ON
//  POST:
//   fan is OFF

def env_scenario02pre(): Unit = {
  Contract(
    Modifies(Env.tempEnv,Env.fanEnv, App.currentFanState),
    Ensures(Env.tempEnv > 60 & Env.tempEnv < 69,
      Env.fanEnv == FanCmd.On,
      App.currentFanState == FanCmd.On)
  )
  // contract only
  skip()
}

def env_scenario02post(): Unit = {
  Contract(
    // Requires(fanEnv == FanCmd.On)
    Requires(Env.fanEnv == FanCmd.Off)
  )
  skip()
}

def scenario02_FanOnToFanOff() : Unit = {
  Contract(
    Modifies(
      Env.tempEnv,
      App.currentTemp_Port_TSOut, App.tempChanged_Port_TSOut,
      App.currentTemp_Port_TCIn, App.tempChanged_Port_TCIn, App.fanCmd_Port_TCOut,
      App.currentFanState, App.latestTemp,
      App.fanCmd_Port_FIn,
      Env.fanEnv)
  )

  // assume base state (state after initialization phase)
  assume(initPhasePostConditionProp(
    // TempSensor port states
    App.currentTemp_Port_TSOut, App.tempChanged_Port_TSOut,
    // TempControl port states
    App.currentTemp_Port_TCIn, App.tempChanged_Port_TCIn, App.fanCmd_Port_FIn,
    //    component local variables
    App.currentFanState, App.latestTemp,
    // Fan port states
    App.fanCmd_Port_FIn, Env.fanEnv))

  // evolve state with additional constraints representing inputs of test scenario
  env_scenario02pre()

  // symbolically simulate one hyper-period
  hyperperiodImmediateComm()

  //  check system test post-condition
  env_scenario02post()
}


//def initPhaseCommunication(): Unit = {
//  Contract(
//    Modifies(currentTemp_Port_TCIn,
//             tempChanged_Port_TSOut, tempChanged_Port_TCIn,
//             tempChanged_Port_TSOut, tempChanged_Port_TCIn,
//             fanCmd_Port_TCOut, fanCmd_Port_FIn
//    ),
//    Ensures(
//      // TS to TC
//      //   - current temp
//      currentTemp_Port_TCIn == currentTemp_Port_TSOut, // propagate value
//      //   - tempChanged
//      tempChanged_Port_TCIn == In(tempChanged_Port_TSOut), // enqueue tempChanged at TC
//      tempChanged_Port_TSOut == None[Event](), // dequeue tempChanged from TS
//      // TC to Fan
//      //   - fanCmd
//      fanCmd_Port_FIn == In(fanCmd_Port_TCOut), // propagate
//      fanCmd_Port_TCOut == None[FanCmd.Type]()
//    )
//  )
//  TS_to_TC_tempChanged_Comm()
//  TS_to_TC_currentTemp_Comm()
//  TC_to_Fan_fanCmd_Comm()
//}
