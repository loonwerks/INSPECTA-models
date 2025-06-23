// #Sireum -- #Sireum indicates file is in Slang, Logika tells the IVE to apply Logika checking
//
import org.sireum._


type Integer = Z
def Integer_example(): Integer = { return z"0" }

//===============================================================
//  S u b s y s t e m    C o m p o n e n t    A r t i f a c t s
//===============================================================

/*---------------------------------------------------------------
   Representation of (relevant) subsystem channels from
   HAMR Micro Model
 *---------------------------------------------------------------*/

/*
Channel definitions from HAMR Micro 04

definition RegCh :: "ChIds"
  where "RegCh = {''upper_desired_tempWstatus'',
                  ''lower_desired_tempWstatus'',
                  ''current_tempWstatus'',
                  ''upper_desired_temp'',
                  ''lower_desired_temp'',
                  ''heat_control'',
                  ''regulator_mode''}"
 */

object Comm {
  var upper_desired_tempWstatus: Integer = z"0" // Integer_example(); Tipe is telling me I cannot reference this
  var lower_desired_tempWstatus: Integer = z"0";
  var current_tempWstatus: Integer = z"0";
  var upper_desired_temp: Integer = z"0";
  var lower_desired_temp: Integer = z"0";
  var display_temp: Integer = z"0";
  var heat_control: Integer = z"0";
  var regulator_mode: Integer = z"0";
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
   displayTemp: Integer, currentTempWstatus: Integer): B =
  (displayTemp == currentTempWstatus)

// - Req-REG-DisplayTemp-1: If the mode is normal in the subsystem pre-state,
// then the display temp = current temp in the subsystem post-state.
// (in full system, display temp is current temp rounded to the
//  closest integer).

@strictpure def Req_REG_DisplayTemp_1(
  lastRegulatorModePre: Integer,
  currentTempWstatus: Integer,
  displayTemp: Integer): B = (
  (lastRegulatorModePre == 1 /*Normal*/)
  ->: displayCurrentEqual(displayTemp, currentTempWstatus)
  )

//  - Req-REG-DisplayTemp-2: If the mode is not normal in the subsystem pre-state,
// then the display temp is unspecified (unconstrained) in the
// subsystem post-state.

@strictpure def Req_REG_DisplayTemp_2(
  lastRegulatorModePre: Integer,
  currentTempWstatus: Integer,
  displayTemp: Integer): B = (
  !(lastRegulatorModePre == 1 /* Normal */)
    ->: T) // no constraints between current and display temp

//  - The specification for the required behavior of the DisplayTemp
//    subsystem function is the conjunction of the two requirements above.
@strictpure def Req_REG_DisplayTemp(lastRegulatorModePre: Integer,
                                    currentTempWstatus: Integer,
                                    displayTemp: Integer): B =
  (Req_REG_DisplayTemp_1(lastRegulatorModePre, currentTempWstatus, displayTemp)
  & Req_REG_DisplayTemp_2(lastRegulatorModePre, currentTempWstatus, displayTemp))


//-------------------------
// debugging -- examples
//-------------------------

// Example 1: (NORMAL mode example)
//  - NORMAL mode
//  - display temp output = current temp input

val ex1_currentTempWstatus: Integer = z"98";
val ex1_lastRegulatorModePre: Integer = 1 /* Normal */;
val ex1_displayTemp: Integer = z"98";
val ex1_displayTemp_fail: Integer = z"99";

// assert that the requirement predicate holds when the currentTemp matches display temp
assert(Req_REG_DisplayTemp(ex1_lastRegulatorModePre,ex1_currentTempWstatus,ex1_displayTemp))
// assert that the requirement predicate fails when the currentTemp DOES NOT MATCH the display temp
assert(!Req_REG_DisplayTemp(ex1_lastRegulatorModePre,ex1_currentTempWstatus,ex1_displayTemp_fail))

// Example 2: (! NORMAL mode example)
//  - init mode (i.e., ! normal mode)
//  - display temp output (97) != current temp input (98)

val ex2_currentTempWstatus: Integer = z"98"
val ex2_lastRegulatorModePre: Integer = 0 /* Init mode */
val ex2_displayTemp: Integer = z"97"

// assert that the requirement holds (trivially, i.e., agnostic of temps), whenever mode is init
assert(Req_REG_DisplayTemp(ex2_lastRegulatorModePre,ex2_currentTempWstatus,ex2_displayTemp))

//------------------------------------------
//  AProperties shared between components (perhaps defined in a GUMBO library)
//------------------------------------------

@strictpure def Lib_LowerUpperDesiredTempWStatus(
      lower_desired_tempWstatus: Integer,
      upper_desired_tempWstatus: Integer): B =
   (lower_desired_tempWstatus <= upper_desired_tempWstatus)


// ToDo: define Slang function representing the abstract function computed by the Subsystem
//       with the requirements above representing the function contract.
//       Conceptually, this abstract function will be refined by the subsystem implementation.

object App {

  //=======================================================================
  //          M a n a g e R e g u l a t o r I n t e r f a c e
  //=======================================================================

  //-------------------------------------------------
  //  I n i t i a l i z e    E n t r y    P o i n t
  //-------------------------------------------------
  def initialise_MRI(): Unit = {
    Contract(
      Modifies(
        Comm.lower_desired_temp,
        Comm.upper_desired_temp,
        Comm.display_temp
      ),
      Ensures(
        // BEGIN INITIALIZES ENSURES
        // guarantee RegulatorStatusIsInitiallyInit
        Comm.lower_desired_temp == z"0",
        Comm.upper_desired_temp == z"0",
        Comm.display_temp == z"0"
        // END INITIALIZES ENSURES
      )
    )
    // contract only
  }

  //-------------------------------------------------
  //  C o m p u t e    E n t r y    P o i n t
  //-------------------------------------------------

  def timeTriggered_MRI(): Unit = {
    Contract(
      Requires(
        // BEGIN_COMPUTE_REQUIRES_timeTriggered
        // assume lower_is_not_higher_than_upper, when the status of both values is valid
        // Note: this constraint is not necessary for the proper functioning of this component.
        //  Thus, it would never be detected during unit verification alone.
        //  Rather it is a constraint that is needed during system composition to satisfy
        //  the constraints on "downstream components".   It would be good to have a specific name for this.
        //  Something like a "system-required strengthening of component contract".  In general, if this
        //  component is (re)used in other contexts, we may not want to require this condition.  This is an
        //  argument for keeping it separate from other component level constraints.
          (Comm.lower_desired_tempWstatus <= Comm.upper_desired_tempWstatus)
        // END_COMPUTE REQUIRES_timeTriggered
      ),
      Modifies(
        Comm.upper_desired_temp,
        Comm.lower_desired_temp,
        Comm.display_temp
      ),
      Ensures(
        ((Comm.regulator_mode == z"1") ->:
          (Comm.display_temp == Comm.current_tempWstatus)),
        // case REQMRI7/8
        //   REQ-MRI-7
        (Comm.lower_desired_tempWstatus == Comm.lower_desired_temp)
        // END_COMPUTE ENSURES_timeTriggered
      )
    )
    // contract only
    // halt("contract only")
  }

  // Predicate pepresentation of precondition (this would be automatically
  // extracted from GUMBO requires clauses -- following the same principles as the
  // GUMBOX pre-condition.
  //
  // Technically, the precondition can reference any input ports or any thread local
  // state.  For this reason, we include all such state items as parameters below.

  @strictpure def MRI_Pre(
     upper_desired_tempWstatus_MRI_In: Integer,
     lower_desired_tempWstatus_MRI_In: Integer,
     current_tempWstatus_MRI_In: Integer,
     regulator_mode_MRI_In: Integer,
     last_regulator_mode: Integer): B = (
   lower_desired_tempWstatus_MRI_In <= upper_desired_tempWstatus_MRI_In
  )


  //=======================================================================
  //          M a n a g e R e g u l a t o r M o d e
  //=======================================================================

  // Thread local variables

  var lastRegulatorMode: Integer = z"0"; /* init mode */


  //-------------------------------------------------
  //  I n i t i a l i z e    E n t r y    P o i n t
  //-------------------------------------------------
  def initialise_MRM(): Unit = {
    Contract(
      Modifies(
        lastRegulatorMode,
        Comm.regulator_mode
      ),
      Ensures(
        // BEGIN INITIALIZES ENSURES
        // guarantee RegulatorModeIsInitiallyInit
        lastRegulatorMode == z"1",
        Comm.regulator_mode == z"1" /* INIT mode */
        // END INITIALIZES ENSURES
      )
    )
    // contract only
    // halt("contract only")
  }

  //-------------------------------------------------
  //  C o m p u t e    E n t r y    P o i n t
  //-------------------------------------------------

  def timeTriggered_MRM(): Unit = {
    Contract(
      Modifies(
        lastRegulatorMode,
        Comm.regulator_mode
      ),
      Ensures(
        /* Simplification: Always stay in NORMAL mode */
        lastRegulatorMode == z"1",
        Comm.regulator_mode == z"1"
      )
    )
    // contract only
    // halt("contract only")
  }

  @strictpure def MRM_Pre(
                         current_tempWstatus: Integer, // input port
                         lastRegulatorMode: Integer // state local variable
                         ): B = (
    T /* we currently do not have a precondition for MRM */
    )


//===============================================
//          M a n a g e H e a t S o u r c e
//===============================================

// Thread local variable

var lastCmd: Integer = z"0" /* Off */

//-------------------------------------------------
//  I n i t i a l i z e    E n t r y    P o i n t
//-------------------------------------------------
def initialise_MHS(): Unit = {
  Contract(
    Modifies(lastCmd),
    Ensures(
      // BEGIN INITIALIZES ENSURES
      // guarantee initlastCmd
      lastCmd == z"0", /* Off */
      // guarantee initheatcontrol
      Comm.heat_control == z"0" /* Off */
      // END INITIALIZES ENSURES
    )
  )
  // contract only
  // halt("contract only")
}

//-------------------------------------------------
//  C o m p u t e    E n t r y    P o i n t
//-------------------------------------------------

def timeTriggered_MHS(): Unit = {
  Contract(
    Requires(
      // BEGIN_COMPUTE_REQUIRES_timeTriggered
      // assume lower_is_lower_temp
      Comm.lower_desired_temp <= Comm.upper_desired_temp
      // END_COMPUTE REQUIRES_timeTriggered
    ),
    Modifies(lastCmd, Comm.heat_control),
    Ensures(
      // BEGIN_COMPUTE_ENSURES_timeTriggered
      // guarantee lastCmd
      //   Set lastCmd to value of output Cmd port
      lastCmd == Comm.heat_control,
      // case ReqMHS2
      //   Req-MHS-2
      ((Comm.regulator_mode == z"1" &
        Comm.current_tempWstatus < Comm.lower_desired_temp) ->:
        // (heat_control_MHS_Out == On_Off.Off),  // seeded bug
        (Comm.heat_control == z"1")),
      // case ReqMHS3
      //   Req-MHS-3
      ((Comm.regulator_mode == z"1" &
        Comm.current_tempWstatus > Comm.lower_desired_temp) ->:
        (Comm.heat_control == z"0")),
      // case ReqMHS4
      //   Req-MHS-4
      ((Comm.regulator_mode == z"1" &
        Comm.current_tempWstatus >= Comm.lower_desired_temp &
        Comm.current_tempWstatus <= Comm.upper_desired_temp) ->:
        (Comm.heat_control == In(lastCmd)))
    )
  )
  // contract only
  // halt("contract only")
}

// Predicate representation of precondition (this would be automatically
// extracted from GUMBO requires clauses -- following the same principles as the
// GUMBOX pre-condition.
//
// Technically, the precondition can reference any input ports or any thread local
// state.  For this reason, we include all such state items as parameters below.

@strictpure def MHS_Pre(upper_desired_temp_MHS_In: Integer,
                        lower_desired_temp_MHS_In: Integer,
                        current_tempWstatus_MHS_In: Integer,
                        regulator_mode_MHS_In: Integer,
                        lastCmd: Integer): B = (
  lower_desired_temp_MHS_In <= upper_desired_temp_MHS_In
  )
}

//===================================================================
//  S y s t e m    R e a s o n i n g
//===================================================================

//===================================================================
//  Representation of System Assertions relevant for the DisplayTemp function
//===================================================================
//
// Below we define two system assertions:
//  - DisplayTemp_FrameInv is an invariant that specifies "administrative properties"
//    that must hold at the frame boundaries to establish the primary Display Temp property
//  - sysAssert_DisplayTemp_MRI_End is an assertion that must be true after the execution
//    of MRI.  In effect, it establishes that the desired Display_Temp property holds
//    at the system level, but this property must be propagated through the remaining
//    two components of the subsystem (MRM, MHS).  This propagation should come "for free"
//    some how because the two components do not modify any state mentioned by the
//    Display Temp property (in particular, they do not modify display temp or current temp.

//  Now we state that the DisplayTemp_FrameInv, intuitively...
//  * Lower Desired <= Upper Desired
//  * last_regulator_mode == regulator_mode_MRI_in
//
// ToDo: what should the parameters to the FrameInv be?
//   - All state elements visible?
//   - Only state elements relevant to the DisplayTemp abstraction function?
//     Or probably the user needs to declare what is appropriate to observe?
//   Also, we can't be mislead by the parameterization (it potentially obscures the
//   specific sources of information in the system state)
@strictpure def sysAssert_DisplayTemp_FrameInv(upper_desired_tempWstatus: Integer,
                                               lower_desired_tempWstatus: Integer,
                                               current_tempWstatus: Integer,
                                               regulator_mode: Integer,
                                               last_regulator_mode: Integer): B = (
  // The Operator Interface (in the enclosing context) needs to establish this constraint
  // before control flows to REG subsystem
  (lower_desired_tempWstatus <= upper_desired_tempWstatus)
  &
  // Need constraint that lastRegulatorMode
  //   (which we refer to in the abstract subsystem specification) is equal to
  //   the MRI mode input port value (which is not visible to the abstract subsystem
  //   (it involves an internal port connection that does not appear on the subsystem interface).
  (regulator_mode == last_regulator_mode)
  )

// Intuitively, the MRI End system assertion says that we are able to
// establish the desired display temp property (derived from the requirement
// of the abstract function) at this point (end of MRI).
@strictpure def sysAssert_DisplayTemp_MRI_End(regulatorModePre: Integer,
                                              currentTempWstatus: Integer,
                                              displayTemp: Integer): B = (
  Req_REG_DisplayTemp(regulatorModePre,currentTempWstatus,displayTemp))

// Intuitively, the MRM End system assertion says that we can still
// establish the DisplayTemp system property after the MRM.
//
// Note regarding choice of parameters: When generating from GUMBO
// should the strictpure function take all the observable state elements
// as parameters, or only those relevant to the assertion?? (probably the latter).
//
// Note: we should be able to establish this for free (as well as
// auto-generating the system assertion in some way without the developer
// having to write it), because it preserved by MRM due to the fact that
// MRM is independent on the Req_REG_DisplayTemp functional property
@strictpure def sysAssert_DisplayTemp_MRM_End(regulatorModePre: Integer,
                                              currentTempWstatus: Integer,
                                              displayTemp: Integer): B = (
  Req_REG_DisplayTemp(regulatorModePre,currentTempWstatus,displayTemp))

// Intuitively, the MRM End system assertion says that we can still
// establish the DisplayTemp system property after the MHS
//
// (independence concepts are the same as what is stated above).
@strictpure def sysAssert_DisplayTemp_MHS_End(regulatorModePre: Integer,
                                              currentTempWstatus: Integer,
                                              displayTemp: Integer): B = (
  Req_REG_DisplayTemp(regulatorModePre,currentTempWstatus,displayTemp))


//===================================================================
//  Representation of VCs for DisplayTemp function
//===================================================================

// Below we define a bunch of helpers, primarily a lot of helpers used to
// assume and assert properties defined above.  These are not strictly needed,
// as they assumes and asserts can be stated directly.
// They also play the role of establish which concrete pieces of the state
// are passed as parameters to the functions/predicates defined above.
//
// In addition, we try to mock up the exact structure of the VCs as they would be
// auto-generated from HAMR.
//
// ToDo:
//  - here we most likely will encounter the need to represent any relationships
//    on state that span frame boundaries
//  - Assess what pieces of the system state are evaluated in the assertion.
//    The methodology to be followed for this is not yet clear.
//    For example, where is the impact of the communications step captured?

//----------------------------------
//  Helper methods for stating VCs
//----------------------------------

// ASSUME frame inv
def assume_sysAssert_DisplayTemp_FrameInv(): Unit = {
  assume(
    sysAssert_DisplayTemp_FrameInv(
      Comm.upper_desired_tempWstatus,
      Comm.lower_desired_tempWstatus,
      Comm.current_tempWstatus,
      Comm.regulator_mode,
      App.lastRegulatorMode)
  )
}

// --------- MRI Pre -----------
def assert_MRI_Pre(): Unit = {
  assert(App.MRI_Pre(
    Comm.upper_desired_tempWstatus,
    Comm.lower_desired_tempWstatus,
    Comm.current_tempWstatus,
    Comm.regulator_mode,
    App.lastRegulatorMode
  ))
}

// --------- MRI End -----------
//  ASSUME
def assume_sysAssert_DisplayTemp_MRI_End(): Unit = {
  assume(sysAssert_DisplayTemp_MRI_End(
    Comm.regulator_mode,
    Comm.current_tempWstatus,
    Comm.display_temp
  ))
}

//  ASSERT
def assert_sysAssert_DisplayTemp_MRI_End(): Unit = {
  assert(sysAssert_DisplayTemp_MRI_End(
    Comm.regulator_mode,
    Comm.current_tempWstatus,
    Comm.display_temp
  ))
}

// --------- MRM Pre -----------
def assert_MRM_Pre(): Unit = {
  assert(App.MRM_Pre(
    Comm.current_tempWstatus, // input port
    App.lastRegulatorMode // local state variable
  ))
}

// --------- MRM End -----------
//  ASSUME
def assume_sysAssert_DisplayTemp_MRM_End(): Unit = {
  assume(sysAssert_DisplayTemp_MRM_End(
    Comm.regulator_mode,
    Comm.current_tempWstatus,
    Comm.display_temp
  ))
}

// ASSERT
def assert_sysAssert_DisplayTemp_MRM_End(): Unit = {
  // Note: This system assertion is failing because it references
  //  the mode value on the output port instead of the mode value at
  //  the beginning of the abstract function.
  assert(sysAssert_DisplayTemp_MRM_End(
    Comm.regulator_mode,
    Comm.current_tempWstatus,
    Comm.display_temp
  ))
}

// --------- MHS Pre -----------

def assert_MHS_Pre(): Unit = {
  assert(App.MHS_Pre(
    Comm.upper_desired_temp,
    Comm.lower_desired_temp,
    Comm.current_tempWstatus,
    Comm.regulator_mode,
    App.lastCmd // local state variable
  ))
}

// --------- MHS End -----------
//  ASSUME
def assume_sysAssert_DisplayTemp_MHS_End(): Unit = {
  assume(sysAssert_DisplayTemp_MHS_End(
    Comm.regulator_mode,
    Comm.current_tempWstatus,
    Comm.display_temp
  ))
}

//  ASSERT
def assert_sysAssert_DisplayTemp_MHS_End(): Unit = {
  assert(sysAssert_DisplayTemp_MHS_End(
    Comm.regulator_mode,
    Comm.current_tempWstatus,
    Comm.display_temp
  ))
}

//----------------------------------
//  MRI VCs
//----------------------------------

// MRI_PreVC
//
// Show that all "End" system assertions from components that may precede MRI
// or the frame assertion itself implies the pre-condition of MRI
// Note the relationship to the pre(S) in Owicki & Gries
//
// Technically, we should generate a distinct VC for such preceding component, or
// add in an intervening system assertion that "merges" the facts for all possible
// preceding components (e.g., in a disjunction).
//
def MRI_PreVC() : Unit = {
  assume_sysAssert_DisplayTemp_FrameInv()
  assert_MRI_Pre()
}


// MRI_ExecVC
//
// Technically, we should generate a distinct VC for such preceding component, or
// add in an intervening system assertion that "merges" the facts for all possible
// preceding components (e.g., in a disjunction).
//
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

// Generate a PreVC for MRM for every component that could possibly precede MRM
// in the schedule.  In this case, there is only one such component (MRI)
def MRM_PreVC_MRI() : Unit = {
  assume_sysAssert_DisplayTemp_MRI_End()
  assert_MRM_Pre()
}

// Generate an ExecVC for MRM for every component that could possibly precede MRM
// in the schedule.  In this case, there is only one such component (MRI)
def MRM_ExecVC() : Unit = {
  // Assume the _End system assertion for the component that precedes it
  assume_sysAssert_DisplayTemp_MRI_End()
  App.timeTriggered_MRM()
  assert_sysAssert_DisplayTemp_MRM_End()
}

//----------------------------------
//  MHS VCs
//----------------------------------

// ToDo
//   def MHS_PreVC() : Unit

// ToDo
//   def MHS_ExecVC() : Unit




