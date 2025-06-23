// #Sireum -- #Sireum indicates file is in Slang, Logika tells the IVE to apply Logika checking
//
import org.sireum._

/*
  This Slang script file presents of mock up of HAMR-emitted verification
  conditions for verifying the Isolette Regulate "System Properties" associated
  with the Display Temp function.

  Intuitively, the properties associated with this function are:
    - Req-REG-DisplayTemp-1: If the mode is normal in the subsystem pre-state,
      then the display temp = current temp in the subsystem post-state.
        (in full system, display temp is current temp rounded to the
        closest integer).
    - Req-REG-DisplayTemp-2: If the mode is not normal in the subsystem pre-state,
      then the display temp is unspecified (unconstrained) in the
      subsystem post-state.

  Declared Boundary Ports (associated DisplayTemp function):
    Inputs:
      currentTempWstatus
    Outputs:
      displayTemp

  Subsystem State (associated with DisplayTemp function):
    current_mode: regulator_mode

  Function Declaration
    REG_Display_Temp
      In:
        currentTempWstatus
      Out:
        displayTemp
      Reads:
        lastRegulatorMode

  GUMBO predicates supporting the function specification:
    def DisplayCurrentEqual(displayTemp: Temp_impl,
                            currentTempWstatus: TempWstatus_impl
                            ): B = {
        displayTemp.value == currentTempWstatus.value
      }


  The following subsystem specifications are derived from the architecture:
    Subsystem components: MRI, MRM, MHS
    Scheduling constraints: MRI < MRM < MHS

   Note: this property is actually independent of MRM and MHS so the relative ordering of these
   two does not matter - we only need to know the MRI precedes both.

   //   - Req-REG-DisplayTemp-1: If the mode is normal in the subsystem pre-state,
   //      then the display temp = current temp in the subsystem post-state.
   //        (in full system, display temp is current temp rounded to the
   //        closest integer).
   def Req-REG-DisplayTemp-1(lastRegulatorModePre: Regulator_Mode.Type,
                             currentTempWstatus: TempWStatus_impl,
                             displayTemp: Temp_impl): B = {
          (lastRegulatorModePre == Regulator_Mode.Normal_Regulator_Mode)
            --> DisplayCurrentEqual(displayTemp, currentTempWstatus)
            }
   def Req-REG-DisplayTemp-1(lastRegulatorModePre: Regulator_Mode.Type,
                             currentTempWstatus: TempWStatus_impl,
                             displayTemp: Temp_impl): B = {
          !(lastRegulatorModePre == Regulator_Mode.Normal_Regulator_Mode)
            --> T
            }

   def Req-REG-DisplayTemp(lastRegulatorModePre: Regulator_Mode.Type,
                           currentTempWstatus: TempWStatus_impl,
                           displayTemp: Temp_impl): B = {
           Req-REG-DisplayTemp-1(lastRegulatorModePre, currentTempWstatus, displayTemp)
       /\  Req-REG-DisplayTemp-2(lastRegulatorModePre, currentTempWstatus, displayTemp)
 */

//===============================================
//  D a t a    T y p e s
//
//  To mock up HAMR/Slang system for system analysis,
//  include all data type definitions and their
//  invariants.
//===============================================

// Mock up base types.  For prototyping, use Z instead of F32
// for temperature values to make the analysis more efficient.
//
// Note: These types are the same across all verification files.
//  In a full implementation, all VCs files would import a common
//  set of data type definitions.

type Integer = Z
def Integer_example(): Integer = { return z"0" }

// create a dummy type to represent HAMR events (for AADL event ports)
@datatype class Event {}
val event = Event()

// use a simplified temperature type (degrees as Z instead of F32)
@datatype class Temp_impl(value : Integer) {
}

@enum object On_Off {
  "Onn"
  "Off"
}

//enumeration used in the tempWstatus datatype.
@enum object ValueStatus {
  "Valid"
  "Invalid"
}

@datatype class TempWstatus_impl(value:Integer, status:ValueStatus.Type){
  //the TempWstatus usually uses an F32 value for its "value" variable, however, an integer is used
  // here to simplify proofs.
}

@enum object Regulator_Mode {
  "Init_Regulator_Mode"
  "Normal_Regulator_Mode"
  "Failed_Regulator_Mode"
}

//@datatype class Regulator_Mode_impl(mode:Regulator_Mode.Type){
//  //Regulator Mode Datatype Implementation of enumerated regulator modes
//}

@enum object Status {
  "Init_Status"
  "On_Status"
  "Failed_Status"
}

@datatype class Failure_Flag_impl(value : B) {}

//===============================================================
//  S u b s y s t e m    C o m p o n e n t    A r t i f a c t s
//===============================================================

/*---------------------------------------------------------------
   Representation of (relevant) subsystem boundary variables
 *---------------------------------------------------------------*/

object Subsystem {
  // ----- Inputs (relevant to abstract DisplayTemp function) ------
  var current_tempWstatus_In: TempWstatus_impl = TempWstatus_impl(z"85", ValueStatus.Valid)
  // ------ Outputs (relevant to DisplayTemp function) ------
  var displayed_temp_Out: Temp_impl = Temp_impl(z"0")
  // ------ Subsystem local vars (relevant to DisplayTemp function) ------
  //  Theoretically, this values should be unconstrained
  var mode: Regulator_Mode.Type = Regulator_Mode.Init_Regulator_Mode
}

//===============================================================
//  S u b s y s t e m    C o m p o n e n t
//    B e h a v i o r    S p e c i f i c a t i o n
//===============================================================

//  Specification of abstract contract associated with subsystem.
//  One can imagine an abstract function here (no implementation) with a
//  contract that includes the abstract function specification above.
//

//----------------------------------------
//  Representation of GUMBO declared helper functions for the subsystem
//----------------------------------------

// specifies that the value fields of displayTemp and currentTempWstatus are equal
//  ..indicating the expected situation when the subsystem begins its execution in Normal mode
@strictpure def displayCurrentEqual(
   displayTemp: Temp_impl, currentTempWstatus: TempWstatus_impl): B =
  (displayTemp.value == currentTempWstatus.value)

// - Req-REG-DisplayTemp-1: If the mode is normal in the subsystem pre-state,
// then the display temp = current temp in the subsystem post-state.
// (in full system, display temp is current temp rounded to the
//  closest integer).

@strictpure def Req_REG_DisplayTemp_1(
  lastRegulatorModePre: Regulator_Mode.Type,
  currentTempWstatus: TempWstatus_impl,
  displayTemp: Temp_impl): B = (
  (lastRegulatorModePre == Regulator_Mode.Normal_Regulator_Mode)
  ->: displayCurrentEqual(displayTemp, currentTempWstatus)
  )

//  - Req-REG-DisplayTemp-2: If the mode is not normal in the subsystem pre-state,
// then the display temp is unspecified (unconstrained) in the
// subsystem post-state.

@strictpure def Req_REG_DisplayTemp_2(
  lastRegulatorModePre: Regulator_Mode.Type,
  currentTempWstatus: TempWstatus_impl,
  displayTemp: Temp_impl): B = (
  !(lastRegulatorModePre == Regulator_Mode.Normal_Regulator_Mode)
    ->: T) // no constraints between current and display temp

//  - The specification for the required behavior of the DisplayTemp
//    subsystem function is the conjunction of the two requirements above.
@strictpure def Req_REG_DisplayTemp(lastRegulatorModePre: Regulator_Mode.Type,
                                    currentTempWstatus: TempWstatus_impl,
                                    displayTemp: Temp_impl): B =
  (Req_REG_DisplayTemp_1(lastRegulatorModePre, currentTempWstatus, displayTemp)
  & Req_REG_DisplayTemp_2(lastRegulatorModePre, currentTempWstatus, displayTemp))



// examples

// Example 1: (NORMAL mode example)
//  - NORMAL mode
//  - display temp output = current temp input

val ex1_currentTempWstatus: TempWstatus_impl = TempWstatus_impl(z"98", ValueStatus.Valid)
val ex1_lastRegulatorModePre: Regulator_Mode.Type = Regulator_Mode.Normal_Regulator_Mode
val ex1_displayTemp: Temp_impl = Temp_impl(z"98")
val ex1_displayTemp_fail: Temp_impl = Temp_impl(z"99")

// assert that the requirement predicate holds when the currentTemp matches display temp
assert(Req_REG_DisplayTemp(ex1_lastRegulatorModePre,ex1_currentTempWstatus,ex1_displayTemp))
// assert that the requirement predicate fails when the currentTemp DOES NOT MATCH the display temp
assert(!Req_REG_DisplayTemp(ex1_lastRegulatorModePre,ex1_currentTempWstatus,ex1_displayTemp_fail))

// Example 2: (! NORMAL mode example)
//  - init mode (i.e., ! normal mode)
//  - display temp output (97) != current temp input (98)

val ex2_currentTempWstatus: TempWstatus_impl = TempWstatus_impl(z"98", ValueStatus.Valid)
val ex2_lastRegulatorModePre: Regulator_Mode.Type = Regulator_Mode.Init_Regulator_Mode
val ex2_displayTemp: Temp_impl = Temp_impl(z"97")

// assert that the requirement holds (trivially, i.e., agnostic of temps), whenever mode is init
assert(Req_REG_DisplayTemp(ex2_lastRegulatorModePre,ex2_currentTempWstatus,ex2_displayTemp))

//------------------------------------------
//  Properties shared between components (perhaps defined in a GUMBO library)
//------------------------------------------

@strictpure def Lib_LowerUpperDesiredTempWStatus(
      lower_desired_tempWstatus: TempWstatus_impl,
      upper_desired_tempWstatus: TempWstatus_impl): B =
  ((upper_desired_tempWstatus.status == ValueStatus.Valid &
    lower_desired_tempWstatus.status == ValueStatus.Valid) -->:
    (lower_desired_tempWstatus.value <= upper_desired_tempWstatus.value))


// ToDo: define Slang function representing the abstract function computed by the Subsystem
//       with the requirements above representing the function contract.
//       Conceptually, this abstract function will be refined by the subsystem implementation.

object App {


  @strictpure def ROUND(num: Z): Z = num

  //=======================================================================
  //          M a n a g e R e g u l a t o r I n t e r f a c e
  //=======================================================================

  // ---- P o r t s

  // --- Port - lower desired tempWstatus ---

  // declare global variable to model port queue
  // (this can eventually be switched to a spec var)
  var lower_desired_tempWstatus_MRI_In: TempWstatus_impl = TempWstatus_impl(z"70", ValueStatus.Valid)

  // --- Port - upper desired tempWstatus ---

  // declare global variable to model port queue
  // (this can eventually be switched to a spec var)
  var upper_desired_tempWstatus_MRI_In: TempWstatus_impl = TempWstatus_impl(z"90", ValueStatus.Valid)

  // --- Port - current tempWstatus ---

  // declare global variable to model port queue
  // (this can eventually be switched to a spec var)
  var current_tempWstatus_MRI_In: TempWstatus_impl = TempWstatus_impl(z"85", ValueStatus.Valid)

  // --- Port - regulator mode ---

  // declare global variable to model port queue
  // (this can eventually be switched to a spec var)
  var regulator_mode_MRI_In: Regulator_Mode.Type = Regulator_Mode.Init_Regulator_Mode

  // --- Port - regulator status ---

  // declare global variable to model port queue
  // (this can eventually be switched to a spec var)
  var regulator_Status_MRI_Out: Status.Type = Status.Init_Status

  // --- Port - displayed temp ---

  // declare global variable to model port queue
  // (this can eventually be switched to a spec var)
  var displayed_temp_MRI_Out: Temp_impl = Temp_impl(z"0")

  // --- Port - interface failure ---

  // declare global variable to model port queue
  // (this can eventually be switched to a spec var)
  var interface_failure_MRI_Out: Failure_Flag_impl = Failure_Flag_impl(F)

  // --- Port - lower desired tempWstatus Out ---

  // declare global variable to model port queue
  // (this can eventually be switched to a spec var)
  var lower_desired_temp_MRI_Out: Temp_impl = Temp_impl(z"0")

  // --- Port - upper desired tempWstatus Out ---

  // declare global variable to model port queue
  // (this can eventually be switched to a spec var)
  var upper_desired_temp_MRI_Out: Temp_impl = Temp_impl(z"0")

  //-------------------------------------------------
  //  I n i t i a l i z e    E n t r y    P o i n t
  //-------------------------------------------------
  def initialise_MRI(): Unit = {
    Contract(
      Modifies(
        lower_desired_temp_MRI_Out,
        upper_desired_temp_MRI_Out,
        displayed_temp_MRI_Out,
        regulator_Status_MRI_Out,
        interface_failure_MRI_Out
      ),
      Ensures(
        // BEGIN INITIALIZES ENSURES
        // guarantee RegulatorStatusIsInitiallyInit
        regulator_Status_MRI_Out == Status.Init_Status
        // END INITIALIZES ENSURES
      )
    )
    // contract only
    halt("contract only")
  }

  //-------------------------------------------------
  //  C o m p u t e    E n t r y    P o i n t
  //-------------------------------------------------

  def timeTriggered_MRI(): Unit = {
    Contract(
      Requires(
        // BEGIN_COMPUTE_REQUIRES_timeTriggered
        // assume lower_is_not_higher_than_upper, when the status of both values is valid
        // Note: this contraint is not necessary for the proper functioning of this component.
        //  Thus, it would never be detected during unit verification alone.
        //  Rather it is a constraint that is needed during system composition to satisfy
        //  the constraints on "downstream components".   It would be good to have a specific name for this.
        //  Something like a "system-required strengthening of component contract".  In general, if this
        //  component is (re)used in other contexts, we may not want to require this condition.  This is an
        //  argument for keeping it separate from other component level constraints.
        (upper_desired_tempWstatus_MRI_In.status == ValueStatus.Valid &
          lower_desired_tempWstatus_MRI_In.status == ValueStatus.Valid) -->:
          (lower_desired_tempWstatus_MRI_In.value <= upper_desired_tempWstatus_MRI_In.value)
        // END_COMPUTE REQUIRES_timeTriggered
      ),
      Modifies(
        lower_desired_temp_MRI_Out,
        upper_desired_temp_MRI_Out,
        displayed_temp_MRI_Out,
        regulator_Status_MRI_Out,
        interface_failure_MRI_Out
      ),
      Ensures(
        // BEGIN_COMPUTE_ENSURES_timeTriggered
        // case REQMRI1
        //   REQ-MRI-1
        (regulator_mode_MRI_In == Regulator_Mode.Init_Regulator_Mode) -->:
          (regulator_Status_MRI_Out == Status.Init_Status),
        // case REQMRI2
        //   REQ-MRI-2
        (regulator_mode_MRI_In == Regulator_Mode.Normal_Regulator_Mode) -->:
          (regulator_Status_MRI_Out == Status.On_Status),
        // case REQMRI3
        //   REQ-MRI-3
        (regulator_mode_MRI_In == Regulator_Mode.Failed_Regulator_Mode) -->:
          (regulator_Status_MRI_Out == Status.Failed_Status),
        // case REQMRI4
        //   REQ-MRI-4
        (regulator_mode_MRI_In == Regulator_Mode.Normal_Regulator_Mode) -->:
          (displayed_temp_MRI_Out.value == ROUND(current_tempWstatus_MRI_In.value)), //Manage_Regulator_Interface_impl_thermostat_regulate_temperature_manage_regulator_interface.ROUND(api.current_tempWstatus.value)),
        // case REQMRI6
        //   REQ-MRI-6
        (upper_desired_tempWstatus_MRI_In.status != ValueStatus.Valid |
          upper_desired_tempWstatus_MRI_In.status != ValueStatus.Valid) ->:
          (interface_failure_MRI_Out.value),
        // case REQMRI7
        //   REQ-MRI-7
        (T) ->:
          (interface_failure_MRI_Out.value == !(upper_desired_tempWstatus_MRI_In.status == ValueStatus.Valid &
            lower_desired_tempWstatus_MRI_In.status == ValueStatus.Valid)),
        // case REQMRI8
        //   REQ-MRI-8
        (!(interface_failure_MRI_Out.value)) ->:
          (lower_desired_temp_MRI_Out.value == lower_desired_tempWstatus_MRI_In.value &
            upper_desired_temp_MRI_Out.value == upper_desired_tempWstatus_MRI_In.value)
        // END_COMPUTE ENSURES_timeTriggered
      )
    )
    // contract only
    halt("contract only")
  }

  // Predicate pepresentation of precondition (this would be automatically
  // extracted from GUMBO requires clauses -- following the same principles as the
  // GUMBOX pre-condition.

  @strictpure def MRI_Pre(
     upper_desired_tempWstatus_MRI_In: TempWstatus_impl,
     lower_desired_tempWstatus_MRI_In: TempWstatus_impl,
     current_tempWstatus_MRI_In: TempWstatus_impl,
     regulator_mode_MRI_In: Regulator_Mode.Type,
     last_regulator_mode_MRM: Regulator_Mode.Type): B = (
    (upper_desired_tempWstatus_MRI_In.status == ValueStatus.Valid &
     lower_desired_tempWstatus_MRI_In.status == ValueStatus.Valid) -->:
      (lower_desired_tempWstatus_MRI_In.value <= upper_desired_tempWstatus_MRI_In.value)
  )


  //=======================================================================
  //          M a n a g e R e g u l a t o r M o d e
  //=======================================================================

  // Thread local variables

  var lastRegulatorMode: Regulator_Mode.Type = Regulator_Mode.byOrdinal(0).get
  var isFirstInvocation: B = true

  // ---- P o r t s

  // --- Port - interface failure ---

  // declare global variable to model port queue
  // (this can eventually be switched to a spec var)
  var interface_failure_MRM_In: Failure_Flag_impl = Failure_Flag_impl(F)

  // --- Port - internal failure ---

  // declare global variable to model port queue
  // (this can eventually be switched to a spec var)
  var internal_failure_MRM_In: Failure_Flag_impl = Failure_Flag_impl(F)

  // --- Port - Current Temp W Status ---

  // declare global variable to model port queue
  // (this can eventually be switched to a spec var)
  var current_tempWstatus_MRM_In: TempWstatus_impl = TempWstatus_impl(z"85", ValueStatus.Valid)

  // --- Port - regulator mode ---

  // declare global variable to model port queue
  // (this can eventually be switched to a spec var)
  var regulator_mode_MRM_Out: Regulator_Mode.Type = Regulator_Mode.Init_Regulator_Mode

  //-------------------------------------------------
  //  I n i t i a l i z e    E n t r y    P o i n t
  //-------------------------------------------------
  def initialise_MRM(): Unit = {
    Contract(
      Modifies(
        lastRegulatorMode,
        isFirstInvocation,
        regulator_mode_MRM_Out
      ),
      Ensures(
        // BEGIN INITIALIZES ENSURES
        // guarantee RegulatorModeIsInitiallyInit
        regulator_mode_MRM_Out == Regulator_Mode.Init_Regulator_Mode
        // END INITIALIZES ENSURES
      )
    )
    // contract only
    halt("contract only")
  }

  //-------------------------------------------------
  //  C o m p u t e    E n t r y    P o i n t
  //-------------------------------------------------

  def timeTriggered_MRM(): Unit = {
    Contract(
      // Requires(In(lastRegulatorMode) == Regulator_Mode.Normal_Regulator_Mode),
      // Modifies(lastRegulatorMode, isFirstInvocation, regulator_mode_MRM_Out),
      Modifies(lastRegulatorMode, regulator_mode_MRM_Out),  // Debugging
      Ensures(
        // BEGIN_COMPUTE_ENSURES_timeTriggered

        // case REQMRM2
        //   REQ-MRM-2
        (In(lastRegulatorMode) == Regulator_Mode.Init_Regulator_Mode) -->:
          ((!(interface_failure_MRM_In.value || internal_failure_MRM_In.value) &&
            current_tempWstatus_MRM_In.status == ValueStatus.Valid) -->:
            (regulator_mode_MRM_Out == Regulator_Mode.Normal_Regulator_Mode &&
              lastRegulatorMode == Regulator_Mode.Normal_Regulator_Mode)),
        // case REQMRMMaintainNormal
        //   REQ-MRM-Maintain-Normal
        (In(lastRegulatorMode) == Regulator_Mode.Normal_Regulator_Mode) -->:
          ((!(interface_failure_MRM_In.value || internal_failure_MRM_In.value) &&
            current_tempWstatus_MRM_In.status == ValueStatus.Valid) -->:
            (regulator_mode_MRM_Out == Regulator_Mode.Normal_Regulator_Mode &&
              lastRegulatorMode == Regulator_Mode.Normal_Regulator_Mode)),
        // case REQMRM3
        //   REQ-MRM-3
        (In(lastRegulatorMode) == Regulator_Mode.Normal_Regulator_Mode) -->:
          ((!(!(interface_failure_MRM_In.value || internal_failure_MRM_In.value) &&
            current_tempWstatus_MRM_In.status == ValueStatus.Valid)) -->:
            (regulator_mode_MRM_Out == Regulator_Mode.Failed_Regulator_Mode &&
              lastRegulatorMode == Regulator_Mode.Failed_Regulator_Mode)),
        // case REQMRM4
        //   REQ-MRM-4
        (In(lastRegulatorMode) == Regulator_Mode.Init_Regulator_Mode) -->:
          ((!(!(interface_failure_MRM_In.value || internal_failure_MRM_In.value) &&
            current_tempWstatus_MRM_In.status == ValueStatus.Valid)) -->:
            (regulator_mode_MRM_Out == Regulator_Mode.Failed_Regulator_Mode &&
              lastRegulatorMode == Regulator_Mode.Failed_Regulator_Mode)),
        // case REQMRMMaintainFailed
        //   REQ-MRM-Maintain-Failed
        ((In(lastRegulatorMode) == Regulator_Mode.Failed_Regulator_Mode) -->:
          (regulator_mode_MRM_Out == Regulator_Mode.Failed_Regulator_Mode &&
            lastRegulatorMode == Regulator_Mode.Failed_Regulator_Mode))
        // END_COMPUTE ENSURES_timeTriggered
      )
    )
    // contract only
    halt("contract only")
  }

  @strictpure def MRM_Pre(last_regulator_mode_MRM_Local: Regulator_Mode.Type,
                          isFirstInvocation_MRM_Local: B,
                          interface_failure_MRM_In: Failure_Flag_impl,
                          internal_failure_MRM_In: Failure_Flag_impl,
                          current_tempWstatus_MRM_In: TempWstatus_impl
                           ): B = (
    T
    )
}

//===================================================================
//  Representation of System Assertions relevant for the DisplayTemp function
//===================================================================
//
//  * Lower Desired <= Upper Desired when value status is valid
//  * last_regulator_mode == regulator_mode_MRI_in
//
// ToDo: what should the parameters to the FrameInv be?
//   - All state elements visible?
//   - Only state elements relevant to the DisplayTemp abstraction function?
//     Or probably the user needs to declare what is appropriate to observe?
//   Also, we can't be mislead by the parameterization (it potentially obscures the
//   specific sources of information)
@strictpure def sysAssert_DisplayTemp_FrameInv(upper_desired_tempWstatus_MRI_In: TempWstatus_impl,
                                               lower_desired_tempWstatus_MRI_In: TempWstatus_impl,
                                               current_tempWstatus_MRI_In: TempWstatus_impl,
                                               regulator_mode_MRI_In: Regulator_Mode.Type,
                                               last_regulator_mode_MRM: Regulator_Mode.Type): B = (
  // The Operator Interface (in the enclosing context) needs to establish this constraint
  // before control flows to REG subsystem
  ((upper_desired_tempWstatus_MRI_In.status == ValueStatus.Valid &
    lower_desired_tempWstatus_MRI_In.status == ValueStatus.Valid) -->:
    (lower_desired_tempWstatus_MRI_In.value <= upper_desired_tempWstatus_MRI_In.value))
  &
  // Need constraint that lastRegulatorMode
  //   (which we refer to in the abstract subsystem specification) is equal to
  //   the MRI mode input port value (which is not visible to the abstract subsystem
  //   (it involves an internal port connection that does not appear on the subsystem interface).
  (regulator_mode_MRI_In == last_regulator_mode_MRM)
  )

@strictpure def sysAssert_DisplayTemp_MRI_End(regulatorModePre: Regulator_Mode.Type,
                                              currentTempWstatus: TempWstatus_impl,
                                              displayTemp: Temp_impl): B = (
  Req_REG_DisplayTemp(regulatorModePre,currentTempWstatus,displayTemp))

//===================================================================
//  Representation of VCs for DisplayTemp function
//===================================================================

// ToDo:
//  - here we most likely will encounter the need to represent any relationships
//    on state that span frame boundaries
//  - Assess what pieces of the system state are evaluated in the assertion.
//    The methodology to be followed for this is not yet clear.
//    For example, where is the impact of the communications step captured?

//----------------------------------
//  Helper methods for stating VCs
//----------------------------------

def assume_sysAssert_DisplayTemp_FrameInv(): Unit = {
  assume(
    sysAssert_DisplayTemp_FrameInv(
      App.upper_desired_tempWstatus_MRI_In,
      App.lower_desired_tempWstatus_MRI_In,
      App.current_tempWstatus_MRI_In,
      App.regulator_mode_MRI_In,
      App.lastRegulatorMode)
  )
}

def assume_sysAssert_DisplayTemp_MRI_End(): Unit = {
  assume(sysAssert_DisplayTemp_MRI_End(
    App.regulator_mode_MRI_In,
    App.current_tempWstatus_MRI_In,
    App.displayed_temp_MRI_Out
  ))
}

def assert_sysAssert_DisplayTemp_MRI_End(): Unit = {
  assert(sysAssert_DisplayTemp_MRI_End(
    App.regulator_mode_MRI_In,
    App.current_tempWstatus_MRI_In,
    App.displayed_temp_MRI_Out
  ))
}

def assert_MRI_Pre(): Unit = {
  assert(App.MRI_Pre(
    App.upper_desired_tempWstatus_MRI_In,
    App.lower_desired_tempWstatus_MRI_In,
    App.current_tempWstatus_MRI_In,
    App.regulator_mode_MRI_In,
    App.lastRegulatorMode
  ))
}

def assert_MRM_Pre(): Unit = {
  assert(App.MRM_Pre(
    App.lastRegulatorMode,
    App.isFirstInvocation,
    App.interface_failure_MRM_In,
    App.internal_failure_MRM_In,
    App.current_tempWstatus_MRM_In
  ))
}

//----------------------------------
//  MRI VCs
//----------------------------------

def MRI_PreVC() : Unit = {
  assume_sysAssert_DisplayTemp_FrameInv()
  assert_MRI_Pre()
}

def MRI_ExecVC() : Unit = {
  // Below we would typically assume the invariant for any components that could immediately precede
  // the current one.   Since there are no components that precede MRI in the abstract scheduling
  // constraints, that we assume the major frame invariant.
  assume_sysAssert_DisplayTemp_FrameInv()
  App.timeTriggered_MRI()
  assert_sysAssert_DisplayTemp_MRI_End()   // assert DisplayTemp system assertion for this temporal point (post MRI)
}

//----------------------------------
//  MRM VCs
//----------------------------------

def MRM_PreVC() : Unit = {
  assume_sysAssert_DisplayTemp_MRI_End()
  assert_MRM_Pre()
}

// ToDo
//   def MRI_ExecVC() : Unit

//----------------------------------
//  MHS VCs
//----------------------------------

// ToDo
//   def MHS_PreVC() : Unit

// ToDo
//   def MHS_ExecVC() : Unit




