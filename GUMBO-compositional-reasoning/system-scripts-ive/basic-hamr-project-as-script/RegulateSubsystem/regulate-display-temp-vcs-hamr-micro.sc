// #Sireum -- #Sireum indicates file is in Slang, Logika tells the IVE to apply Logika checking
//
import org.sireum._


type Integer = Z
def Integer_example(): Integer = { return z"0" }

//===============================================================
//  S u b s y s t e m    C o m p o n e n t    A r t i f a c t s
//===============================================================

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

  Abstract Function Declaration
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

  In the simplified HAMR Micro 04 setting, this becomes

    def DisplayCurrentEqual(displayTemp: Integer,
                            currentTempWstatus: Integer
                            ): B = {
        displayTemp == currentTempWstatus
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
   def Req-REG-DisplayTemp-1(lastRegulatorModePre: Integer,
                             currentTempWstatus: Integer,
                             displayTemp: Integer): B = {
          (lastRegulatorModePre == z"1")
            --> DisplayCurrentEqual(displayTemp, currentTempWstatus)
            }
   def Req-REG-DisplayTemp-1(lastRegulatorModePre: Integer,
                             currentTempWstatus: Integer,
                             displayTemp: Integer): B = {
          !(lastRegulatorModePre == z"1")
            --> T
            }

   def Req-REG-DisplayTemp(lastRegulatorModePre: Integer,
                           currentTempWstatus: Integer,
                           displayTemp: Integer): B = {
           Req-REG-DisplayTemp-1(lastRegulatorModePre, currentTempWstatus, displayTemp)
       /\  Req-REG-DisplayTemp-2(lastRegulatorModePre, currentTempWstatus, displayTemp)

  Contract for abstract function:

  REG_Display_Temp
      In:
        currentTempWstatus
      Out:
        displayTemp
      Reads:
        lastRegulatorMode
    Contract(
      Requires(),
      Modifies(displayTemp),
      Ensures(Req-REG-DisplayTemp(lastRegulatorMode,currentTempWstatus,displayTemp)
    )
  ===============
  Refinement
  ===============

  The pre and post states of the abstract function above must be associated with
  state elements (ports or thread local variables) of the refining subsystem at
  specified temporal points.

  By default the abstract function has temporal points
     REG_Display_Temp_BEGIN
      - in which values of the pre-state are observed
     REG_Display_Temp_END
      - in which values of the post-state are observed.

  Intuitively, REG_Display_Temp_BEGIN and REG_Display_Temp_END must be associated
  with temporal points in the refining subsystem.

  We will assume the refining subsystem REG has abstract temporal points REG_BEGIN, REG_END.
  So we can associate the temporal aspects of the abstract function to the subsystem as follows:
    Refine(REG_Display_Temp_BEGIN, REG_BEGIN)
    Refine(REG_Display_Temp_END, REG_END)

  Additionally, the state elements need to be refined, ..something like this...
     Refine(REG_Display_Temp.currentTempWstatus, REG.currentTempWstatus)
      ..where REG.currentTempWstatus refers to the AADL boundary port of Reg
     OR
      Refine(REG_Display_Temp.currentTempWstatus, REG.MRI.currentTempWstatus)


  Scheduling Constraints (not fully worked out yet)
   - from the subsystem scheduling constraints (whatever form they take;
     we don't exactly know yet), we assume a relation
       may_immediately_precede(C1,C2)
     which indicates that C1 may immediately precede C2 in the schedule.
     For every Ci such that MIP(Ci,C), we most show that the system assertion
     Ci_end ^ C.app_post -> C_end
     That is the Ci_end system assertion together with C's contract post condition
     (transfer function) implies the C_end system assertion.

subsystem REG
  with functions Display_Temp, Regulator_Status, Heat_Control;

   // REG.begin is an abstract temporal point that is associated with
   // a single identifiable position in the schedule
   REG.begin
     log
       // save/log the inputs necessary for our reasoning.  Unless we assume that an
       // automatic gateway component is "freezes" the input state to the subsystem is implemented,
       // we must indicate which input port (or local variable) of the subsystem components that we log from
       current_tempWstatus[<-MRI.current_tempWstatus], // subsystems inputs with multiple destinations need to be disambiguated
       last_regulator_mode[<-MRM.last_regulator_mode];
     assumes
       (lower_desired_tempWstatus <= upper_desired_tempWstatus)
       & (regulator_mode == last_regulator_mode)
     with Display_Temp
      assumes
        log.REG.begin.current_tempWstatus == MRI.current_tempWstatus
      & log.REG.begin.last_regulator_mode == MRI.regulator_mode
      & (lower_desired_tempWstatus <= upper_desired_tempWstatus)

    MRI
      assert [Display_Temp]
         Req_REG_DisplayTemp(log.REG.begin.last_regulator_mode,
                             log.REG.begin.current_tempWstatus,
                             displayTemp)
        & // other subsystem properties needed to establish preconditions of down-stream components
         (lower_desired_temp <= upper_desired_temp)

    MRM
    assert [Display_Temp]
         Req_REG_DisplayTemp(log.REG.begin.last_regulator_mode,
                             log.REG.begin.current_tempWstatus,
                             displayTemp)
        & // other subsystem properties needed to establish preconditions of down-stream components
         (lower_desired_temp <= upper_desired_temp)
   MHS
    assert [Display_Temp]
         Req_REG_DisplayTemp(log.REG.begin.last_regulator_mode,
                             log.REG.begin.current_tempWstatus,
                             displayTemp)
        & // other subsystem properties needed to establish preconditions of down-stream components
         (lower_desired_temp <= upper_desired_temp)

   REG.End
     log
       display_temp[<-MRI.displayTemp],
     guarantee [DisplayTemp]
       Req_REG_DisplayTemp(log.REG.begin.last_regulator_mode,
                           log.REG.begin.current_tempWstatus,
                           log.displayTemp)

 */

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

object Log {

  // Define logged state values (Owicki Greis "auxiliary variables") that provide snapshots
  // of state associated with abstract function

  var currentTempWstatus: Integer = z"0" /* Default value */
  var displayTemp: Integer = z"0"
  var lastRegulatorMode: Integer = z"0"


  //-------------------------------------------------
  //  Log Display Temp abstract function pre-state
  //
  //  Proposed concept (not worked out in detail):
  //   - after declaring an abstract function
  //-------------------------------------------------

  def Reg_Display_Temp_pre(): Unit = {
    Contract(
      Requires(),
      Modifies(
        Log.currentTempWstatus,
        Log.lastRegulatorMode
      ),
      Ensures(
         Log.currentTempWstatus == Comm.current_tempWstatus,
         // Note: multiple options exist for what to log for the mode:
         //  - either MRM local variable holding the current mode, OR
         //  - mode delivered as input to MRI (we choose the latter for now)
         Log.lastRegulatorMode == Comm.regulator_mode
      )
    )
    // contract only
    // halt("contract only")
  }

  def Reg_Display_Temp_post(): Unit = {
    Contract(
      Requires(),
      Modifies(
        Log.displayTemp
      ),
      Ensures(
        Log.displayTemp == Comm.display_temp
      )
    )
    // contract only
    // halt("contract only")
  }
}

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
        (Comm.lower_desired_tempWstatus == Comm.lower_desired_temp),
        (Comm.upper_desired_tempWstatus == Comm.upper_desired_temp)
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

//==============================================
// System Assertions
//  - system assertions correspond to specs that the developer would write
//    (probably with GUMBO syntax at the model-level),
//    perhaps in the style of the "proof outline" of Owicki-Gries proof outlines
//==============================================
//
//================================================
//  Frame Invariant
//
//  System assertion(s) for properties that must hold true at the boundary
//  of the major frame in the static schedule.   Frame invariants are
//  analogous to loop invariants.
//  Frame invariants would likely consists of multiple parts including...
//   ..administrative properties related to overall consistency of state
//   ..properties the relate values computed in previous frame cycle to
//     upcoming cycle
//  We may allow frame invariant properties to be defined as individual
//  named properties and then conjoined together.
//==================================================

//
// ToDo: Frame invariants omitted for now, until REG subsystem verification
//  is fully prototyped.

//===================================================================
//  Representation of System Assertions for REG subsystem
//
//  The REG subsystem computes multiple functions.
//  We need an assertion REG_begin that specifies facts derived from the
//  frame invariant and the effects of components executed outside
//  the REG subsystem (in our case, temp sensor, operator interface),
//  leading up to the point where the REG subsystem begins in the schedule.
//  Since the REG subsystem includes multiple functions, the REG_begin
//  assertion should include all info needed to establish the preconditions
//  of the abstract functions allocated to the subsystem.
//===================================================================

// Below we define two system assertions:
//  - DisplayTemp_FrameInv is an invariant that specifies "administrative properties"
//    that must hold at the frame boundaries to establish the primary Display Temp property

//  Now we state that the DisplayTemp_FrameInv, intuitively...
//  * Lower Desired <= Upper Desired
//  * last_regulator_mode == regulator_mode_MRI_in
//
// ToDo: what should the parameters to the REG_begin
//   - All state elements visible?
//   - e.g., subsystem inputs and any contract declared thread local state for the subsystem
//   Also, we can't be mislead by the parameterization (it potentially obscures the
//   specific sources of information in the system state)
@strictpure def sysAssert_REG_begin(upper_desired_tempWstatus: Integer,
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
    //
    // This fact is also needed when compose on frame cycle with the next (and would show up
    // in the frame invariant)
    (regulator_mode == last_regulator_mode)
  )

//===================================================================
//  Representation of System Assertions relevant for the DisplayTemp function
//===================================================================
//

// Display_Temp_REG_pre is a system assertion that should hold true at the
// beginning of the scheduling of components of the components implementing
// the display temp function.
// It represents the precondition for the abstract function ??and would be
// a refinement of an abstract precondition??
//
// The precondition can reference all the state visible from the abstract function
// and its refined input state.  This likely included logged versions of state
// at the time the abstract function begins its execution.
//
// It also may include references to other state needed to establish the
// enabledness of components that implement the function (even though those
// pre-conditions are irrelevant for the current function being verified.
// We may eventually be able to address this via some notion of "borrowing"
// properties from other functional reasoning in the subsystem.
@strictpure def sysAssert_Display_Temp_REG_pre(
                                    upper_desired_tempWstatus: Integer, // needed to prove component enabledness
                                    lower_desired_tempWstatus: Integer, // needed to prove component enabledness
                                    current_tempWstatus_MRI_In: Integer,
                                    // logged subsystem input port value for current_temp for
                                    // Display Temp function.  This is somewhat imprecise, but for now we assume
                                    // that we log the current temp input to MRI at the point at which the MRI
                                    // begins its execution.
                                    current_tempWstatus_MRI_In_logged: Integer,
                                    // logged value of MRI regulator mode input
                                    regulator_mode_MRI_In: Integer,
                                    // ??do we need the value below?? and is it included in the logging?
                                    last_regulator_mode_logged: Integer): B = (
  // The Operator Interface (in the enclosing context) needs to establish this constraint
  // before control flows to REG subsystem
       (lower_desired_tempWstatus <= upper_desired_tempWstatus)

    // Need constraint that lastRegulatorMode
    //   (which we refer to in the abstract subsystem specification) is equal to
    //   the MRI mode input port value (which is not visible to the abstract subsystem
    //   (it involves an internal port connection that does not appear on the subsystem interface).
    //
    // This fact is also needed when compose on frame cycle with the next (and would show up
    // in the frame invariant)
    & (regulator_mode_MRI_In == last_regulator_mode_logged)
    & (current_tempWstatus_MRI_In == current_tempWstatus_MRI_In_logged)
  )

//  - sysAssert_DisplayTemp_MRI_End is an assertion that must be true after the execution
//    of MRI.  In effect, it establishes that the desired Display_Temp property holds
//    at the system level, but this property must be propagated through the remaining
//    two components of the subsystem (MRM, MHS).  This propagation should come "for free"
//    (some how, to be determined) because the two components do not modify any state
//    mentioned by the Display Temp property (in particular, they do not modify display
//    temp or current temp.

// Intuitively, the MRI End system assertion says that we are able to
// establish the desired display temp property (derived from the requirement
// of the abstract function) at this point (end of MRI).
@strictpure def sysAssert_DisplayTemp_MRI_End(regulatorMode_logged: Integer,
                                              currentTempWstatus_logged: Integer,
                                              displayTemp_MRI_Out: Integer,
                                              upper_desired_temp_MRI_Out: Integer,
                                              lower_desired_temp_MRI_Out: Integer): B = (
  // Display Temp function property at this point
  Req_REG_DisplayTemp(regulatorMode_logged,currentTempWstatus_logged,displayTemp_MRI_Out)
  & // other subsystem properties needed to establish preconditions of down-stream components
    (lower_desired_temp_MRI_Out <= upper_desired_temp_MRI_Out)
  )

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
@strictpure def sysAssert_DisplayTemp_MRM_End(regulatorMode_logged: Integer,
                                              currentTempWstatus_logged: Integer,
                                              displayTemp_MRI_Out: Integer,
                                              upper_desired_temp_MRI_Out: Integer,
                                              lower_desired_temp_MRI_Out: Integer): B = (
  // Display Temp function property at this point
  Req_REG_DisplayTemp(regulatorMode_logged,currentTempWstatus_logged,displayTemp_MRI_Out)
  & // other subsystem properties needed to establish preconditions of down-stream components
  (lower_desired_temp_MRI_Out <= upper_desired_temp_MRI_Out)
  )

// Intuitively, the MRM End system assertion says that we can still
// establish the DisplayTemp system property after the MHS
//
// (independence concepts are the same as what is stated above).
@strictpure def sysAssert_DisplayTemp_MHS_End(regulatorMode_logged: Integer,
                                              currentTempWstatus_logged: Integer,
                                              displayTemp_MRI_Out: Integer): B = (
  Req_REG_DisplayTemp(regulatorMode_logged,currentTempWstatus_logged,displayTemp_MRI_Out))

@strictpure def sysAssert_DisplayTemp_REG_post(regulatorMode_logged: Integer,
                                               currentTempWstatus_logged: Integer,
                                               displayTemp_logged: Integer): B = (
  Req_REG_DisplayTemp(regulatorMode_logged,currentTempWstatus_logged,displayTemp_logged))

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

// Note: helpers from Frame Inv to the beginning of the abstract function omitted for now


// --------- REG_begin ----------
// ASSUME
def assume_sysAssert_REG_begin(): Unit = {
  assume(
    sysAssert_REG_begin(
      Comm.upper_desired_tempWstatus,
      Comm.lower_desired_tempWstatus,
      Comm.current_tempWstatus,
      Comm.regulator_mode,
      App.lastRegulatorMode)
  )
}

// ------- Display_Temp_REG_pre --------
//  ASSUME
def assume_sysAssert_Display_Temp_REG_pre(): Unit = {
  assume(
    sysAssert_Display_Temp_REG_pre(
      Comm.upper_desired_tempWstatus, // needed to prove component enabledness - NOT present in abstract function
      Comm.lower_desired_tempWstatus, // needed to prove component enabledness - NOT present in abstract function
      Comm.current_tempWstatus,
      // logged subsystem (more specifically, MRI) input port value for current_temp for
      // Display Temp function.  It is currently unclear to me exactly when the logging occurs,
      // put I believe that we should log either at the temporal point at the beginning of the
      // Reg schedule segment or at the beginning of the MRI execution.
      // Currently, the VC structure reflects a logging at the beginning of MRI.
      Log.currentTempWstatus,
      // input value to MRI (needed to establish that function computed by MRI takes a
      // concrete input that is equal to the Logged lastRegulatorMode referenced by the abstract function.
      Comm.regulator_mode,// ToDo: ?? why is this here? It is not visible in the abstract function
      // logged value of the subsystem state referenced in the abstract Display Temp function
      Log.lastRegulatorMode)
  )
}


//  ASSERT
def assert_sysAssert_Display_Temp_REG_pre(): Unit = {
  assert(
    sysAssert_Display_Temp_REG_pre(
      Comm.upper_desired_tempWstatus, // needed to prove component enabledness
      Comm.lower_desired_tempWstatus, // needed to prove component enabledness
      Comm.current_tempWstatus,
      // logged subsystem (more specifically, MRI) input port value for current_temp for
      // Display Temp function.  It is currently unclear to me exactly when the logging occurs,
      // put I believe that we should log either at the temporal point at the beginning of the
      // Reg schedule segment or at the beginning of the MRI execution.
      // Currently, the VC structure reflects a logging at the beginning of MRI.
      Log.currentTempWstatus,
      // input value to MRI (needed to establish that function computed by MRI takes a
      // concrete input that is equal to the Logged lastRegulatorMode referenced by the abstract function.
      Comm.regulator_mode,
      // logged value of the subsystem state referenced in the abstract Display Temp function
      Log.lastRegulatorMode)
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
    Log.lastRegulatorMode,
    Log.currentTempWstatus,
    Comm.display_temp,
    Comm.upper_desired_temp,
    Comm.lower_desired_temp
  ))
}

//  ASSERT
def assert_sysAssert_DisplayTemp_MRI_End(): Unit = {
  assert(sysAssert_DisplayTemp_MRI_End(
    Log.lastRegulatorMode,
    Log.currentTempWstatus,
    Comm.display_temp,
    Comm.upper_desired_temp,
    Comm.lower_desired_temp
  ))
}

// --------- MRM Pre -----------
def assert_MRM_Pre(): Unit = {
  // Note: we don't have a pre-condition declared for the
  // application contract for MRM currently, so the VC below
  // is trivially true (and the choice of parameters to use here
  // may not be meaningful).
  assert(App.MRM_Pre(
    Comm.current_tempWstatus, // input port
    App.lastRegulatorMode // local state variable
  ))
}

// --------- MRM End -----------
//  ASSUME
def assume_sysAssert_DisplayTemp_MRM_End(): Unit = {
  assume(sysAssert_DisplayTemp_MRM_End(
    Log.lastRegulatorMode,
    Log.currentTempWstatus,
    Comm.display_temp,
    Comm.upper_desired_temp,
    Comm.lower_desired_temp
  ))
}

// ASSERT
def assert_sysAssert_DisplayTemp_MRM_End(): Unit = {
  // Note: This system assertion is failing because it references
  //  the mode value on the output port instead of the mode value at
  //  the beginning of the abstract function.
  assert(sysAssert_DisplayTemp_MRM_End(
    Log.lastRegulatorMode,
    Log.currentTempWstatus,
    Comm.display_temp,
    Comm.upper_desired_temp,
    Comm.lower_desired_temp
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
    Log.lastRegulatorMode,
    Log.currentTempWstatus,
    Comm.display_temp
  ))
}

//  ASSERT
def assert_sysAssert_DisplayTemp_MHS_End(): Unit = {
  assert(sysAssert_DisplayTemp_MHS_End(
    Log.lastRegulatorMode,
    Log.currentTempWstatus,
    Comm.display_temp
  ))
}

// ------- Display_Temp_REG_post --------
//  ASSUME
def assume_sysAssert_DisplayTemp_REG_post(): Unit = {
  assume(
    sysAssert_DisplayTemp_REG_post(
      Log.lastRegulatorMode,
      Log.currentTempWstatus,
      Log.displayTemp
    )
  )
}


//  ASSERT
def assert_sysAssert_DisplayTemp_REG_post(): Unit = {
  assert(
    sysAssert_DisplayTemp_REG_post(
      Log.lastRegulatorMode,
      Log.currentTempWstatus,
      Log.displayTemp
    )
  )
}



//----------------------------------
//  Display Temp Pre VC
//----------------------------------

// Shows that the facts available at the start of the REG subsystem in the
// static schedule are sufficient to establish the pre-condition for the
// Display Temp function (which also includes some administrative facts)

// This also reflects aspects of the logging, abstractly done at the
// beginning of the scheduling of components that realize Display Temp
//

def REG_Display_Temp_PreVC() : Unit = {
  assume_sysAssert_REG_begin()
  Log.Reg_Display_Temp_pre()  // implement declared logging for Display_Temp pre-condition
  assert_sysAssert_Display_Temp_REG_pre()
}

//----------------------------------
//  MRI VCs
//----------------------------------


// MRI_PreVC
//
// Must show that all preconditions of the MRI hold.
//
// In general, we follow the pattern that all "End" system assertions from
// components that may precede MRI
// or the subsystem _begin property itself implies the pre-condition of MRI
// Note the relationship to the pre(S) in Owicki & Gries
//
// Technically, we should generate a distinct VC for such preceding component, or
// add in an intervening system assertion that "merges" the facts for all possible
// preceding components (e.g., in a disjunction).
//
// Note: The structure below is probably NOT what we want ultimately.
// The issue is that, in the formulation below, we are relying only on
// logic associated with the abstract function Display Temp to establish *all*
// the pre-condition logic of MRI, which supports other functions as well.
// So we likely need some way of combining the facts from multiple functions or
// for "borrowing" the facts from other functions here to establish the general precondition.
// In the current formulation, we are forced to carry along facts in the Display Temp
// reasoning that don't actually pertain to Display Temp but are necessary to establish
// the general preconditions of all threads to which Display Temp is allocated.
//
def REG_Display_Temp_MRI_PreVC() : Unit = {
  assume_sysAssert_Display_Temp_REG_pre()
  assert_MRI_Pre()
}


// MRI_ExecVC
//
// Technically, we should generate a distinct VC for such preceding component, or
// add in an intervening system assertion that "merges" the facts for all possible
// preceding components (e.g., in a disjunction).
//
def REG_Display_Temp_MRI_ExecVC() : Unit = {
  // Below we would typically assume the invariant for any components that could immediately precede
  // the current one.   Since there are no components that precede MRI in the abstract scheduling
  // constraints, that we assume the major frame invariant.
  //
  // Logging: Since we also refine the abstract function pre-state to REG_Begin
  assume_sysAssert_Display_Temp_REG_pre()
  App.timeTriggered_MRI()
  assert_sysAssert_DisplayTemp_MRI_End()   // assert DisplayTemp system assertion for this temporal point (post MRI)
}

//----------------------------------
//  MRM VCs
//----------------------------------

// Generate a PreVC for MRM for every component that could possibly precede MRM
// in the schedule.  In this case, there is only one such component (MRI)
def REG_Display_Temp_MRM_PreVC_MRI() : Unit = {
  assume_sysAssert_DisplayTemp_MRI_End()
  assert_MRM_Pre()
}

// Generate an ExecVC for MRM for every component that could possibly precede MRM
// in the schedule.  In this case, there is only one such component (MRI)
def REG_Display_Temp_MRM_ExecVC() : Unit = {
  // Assume the _End system assertion for the component that precedes it
  assume_sysAssert_DisplayTemp_MRI_End()
  App.timeTriggered_MRM()
  assert_sysAssert_DisplayTemp_MRM_End()
}

//----------------------------------
//  MHS VCs
//----------------------------------

// Generate a PreVC for MRM for every component that could possibly precede MHS
// in the schedule.  In this case, there is only one such component (MRM)
def REG_Display_Temp_MHS_PreVC_MRM() : Unit = {
  assume_sysAssert_DisplayTemp_MRM_End()
  assert_MHS_Pre()
}


// Generate an ExecVC for MRM for every component that could possibly precede MHS
// in the schedule.  In this case, there is only one such component (MRM)
def REG_Display_Temp_MHS_ExecVC() : Unit = {
  // Assume the _End system assertion for the component that precedes it
  assume_sysAssert_DisplayTemp_MRM_End()
  App.timeTriggered_MHS()
  assert_sysAssert_DisplayTemp_MHS_End()
}

// Generate a VC for function post state that includes logging of its observed post-state
// values.
def REG_Display_Temp_postVC() : Unit = {
  // Assume the _End system assertion for the component that precedes it
  assume_sysAssert_DisplayTemp_MHS_End()
  Log.Reg_Display_Temp_post()
  assert_sysAssert_DisplayTemp_REG_post()
}




