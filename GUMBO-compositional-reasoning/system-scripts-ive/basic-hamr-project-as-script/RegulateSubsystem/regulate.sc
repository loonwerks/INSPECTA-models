// #Sireum -- #Sireum indicates file is in Slang, Logika tells the IVE to apply Logika checking
//
import org.sireum._

// Types of Properties
//   - scenarios
//   - frame invariants (property preservation)
//   - state invariants (property always holds (no need to assume the post-condition and prove the frame-condition))
//       (e.g., at frame boundary, if mode is failure then heater is OFF
//

//===============================================
//  E n v i r o n m e n t    T y p e s
//
//===============================================

@enum object FanCmd {
  "On"
  "Off"
}


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

//
//@datatype class Regulator_Status_impl(status:Status.Type){
//
//}
//
//@datatype class On_Off_impl (on_off:On_Off.Type){
//  // On_Off implementation
//}
//



//===============================================
//  E n v i r o n m e n t
//
//   These represent externally readable/writable
//   states that could be
//     - constrained and queried by Logika
//     - interacted with in testing
//===============================================

var tempEnv: Z = 72
var fanEnv: FanCmd.Type = FanCmd.Off

//===============================================
//  S y s t e m    C o n t e x t
//
//   These represent port connections from the broader system context
//   (outside of the Isolette Thermostat).
//   These states that could be
//     - constrained and queried by Logika
//     - interacted with in testing
//
//  Refer to the diagram in Figure 8 of
//  FAA-DoT-Requirements-AR-08-32.pdf
//===============================================

object Context {
  // ----- Inputs -------
  var current_tempWstatus_CONTEXT_In: TempWstatus_impl = TempWstatus_impl(z"85", ValueStatus.Valid)

  var lower_desired_tempWstatus_CONTEXT_In: TempWstatus_impl = TempWstatus_impl(z"70", ValueStatus.Valid)
  var upper_desired_tempWstatus_CONTEXT_In: TempWstatus_impl = TempWstatus_impl(z"90", ValueStatus.Valid)

  // Operator settings (desired upper and lower temperature)

  // ------ Outputs ------

  var heat_control_CONTEXT_Out: On_Off.Type = On_Off.Off

  var regulator_Status_CONTEXT_Out: Status.Type = Status.Init_Status

  var displayed_temp_CONTEXT_Out: Temp_impl = Temp_impl(z"0")

}

object App {

  @strictpure def ROUND(num: Z): Z = num
  //===============================================
  //          M a n a g e H e a t S o u r c e
  //===============================================

  // Global Vars

  var lastCmd: On_Off.Type = On_Off.byOrdinal(0).get

  // ---- P o r t s

  // --- Port - upper Temp ---

  // declare global variable to model port queue
  // (this can eventually be switched to a spec var)
  var upper_desired_temp_MHS_In: Temp_impl = Temp_impl(z"90")

  // --- Port - lower Temp ---

  // declare global variable to model port queue
  // (this can eventually be switched to a spec var)
  var lower_desired_temp_MHS_In: Temp_impl = Temp_impl(z"70")

  // --- Port - current Temp ---

  // declare global variable to model port queue
  // (this can eventually be switched to a spec var)
  // TODO: For a consistent naming scheme, should this variable name be switched to "current_tempWstatus_MHS_In"
  var current_temp_MHS_In: TempWstatus_impl = TempWstatus_impl(z"85", ValueStatus.Valid)

  // --- Port - regulator mode ---

  // declare global variable to model port queue
  // (this can eventually be switched to a spec var)
  var regulator_mode_MHS_In: Regulator_Mode.Type = Regulator_Mode.Init_Regulator_Mode

  // --- Port - heat control ---

  // declare global variable to model port queue
  // (this can eventually be switched to a spec var)
  var heat_control_MHS_Out: On_Off.Type = On_Off.Off

  var heat_control_MHS_Out_PRE: On_Off.Type = On_Off.Off   // needed for properties where there is pre/post-state verification
  /*
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
var tempChanged_Port_TSOut: Option[Event] = None()
*/

  //-------------------------------------------------
  //  I n i t i a l i z e    E n t r y    P o i n t
  //-------------------------------------------------
  def initialise_MHS(): Unit = {
    Contract(
      Modifies(lastCmd),
      Ensures(
        // BEGIN INITIALIZES ENSURES
        // guarantee initlastCmd
        lastCmd == On_Off.Off,
        // guarantee initheatcontrol
        heat_control_MHS_Out == On_Off.Off
        // END INITIALIZES ENSURES
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
  }

  def timeTriggered_MHS(): Unit = {
    Contract(
      Requires(
        // BEGIN_COMPUTE_REQUIRES_timeTriggered
        // assume lower_is_lower_temp
        (regulator_mode_MHS_In == Regulator_Mode.Normal_Regulator_Mode) -->:   // ToDo: Decide if this is needed or not
          (lower_desired_temp_MHS_In.value <= upper_desired_temp_MHS_In.value)
        // END_COMPUTE REQUIRES_timeTriggered
      ),
      Modifies(lastCmd, heat_control_MHS_Out),
      Ensures(
        // BEGIN_COMPUTE_ENSURES_timeTriggered
        // guarantee lastCmd
        //   Set lastCmd to value of output Cmd port
        lastCmd == heat_control_MHS_Out,
        // case ReqMHS1
        //   Req-MHS-1
        (regulator_mode_MHS_In == Regulator_Mode.Init_Regulator_Mode) -->:
          (heat_control_MHS_Out == On_Off.Off),
        // case ReqMHS2
        //   Req-MHS-2
        (regulator_mode_MHS_In == Regulator_Mode.Normal_Regulator_Mode &
          current_temp_MHS_In.value < lower_desired_temp_MHS_In.value) -->:
          // (heat_control_MHS_Out == On_Off.Off),  // seeded bug
          (heat_control_MHS_Out == On_Off.Onn),
        // case ReqMHS3
        //   Req-MHS-3
        (regulator_mode_MHS_In == Regulator_Mode.Normal_Regulator_Mode &
          current_temp_MHS_In.value > upper_desired_temp_MHS_In.value) -->:
          // (heat_control_MHS_Out == On_Off.Onn),  // seeded bug
          (heat_control_MHS_Out == On_Off.Off),
          // case ReqMHS4
          //   Req-MHS-4
        (regulator_mode_MHS_In == Regulator_Mode.Normal_Regulator_Mode &
          current_temp_MHS_In.value >= lower_desired_temp_MHS_In.value &
          current_temp_MHS_In.value <= upper_desired_temp_MHS_In.value) -->:
          // (heat_control_MHS_Out == On_Off.Onn),  // seeded bug
          (heat_control_MHS_Out == In(lastCmd)),
        // case ReqMHS5
        //   Req-MHS-5
        (regulator_mode_MHS_In == Regulator_Mode.Failed_Regulator_Mode) -->:
          // (heat_control_MHS_Out == On_Off.Onn)  // seeded bug
          (heat_control_MHS_Out == On_Off.Off)
        // END_COMPUTE ENSURES_timeTriggered
      )
    )
    // contract only
    halt("contract only")
  }

  //==========================================================
  //          M a n a g e R e g u l a t o r M o d e
  //==========================================================

  // Global Vars

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

  //=======================================================================
  //          M a n a g e R e g u l a t o r I n t e r f a c e
  //=======================================================================

  // Global Vars

  // val LOW: TempWstatus_impl = TempWstatus_impl(z"0", ValueStatus.Invalid)
  // val HIGH: TempWstatus_impl = TempWstatus_impl(z"0", ValueStatus.Invalid)

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
        //  the contraints on "downstream components".   It would be good to have a specific name for this.
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
          upper_desired_tempWstatus_MRI_In.status != ValueStatus.Valid) -->:
          (interface_failure_MRI_Out.value),
        // case REQMRI7
        //   REQ-MRI-7
        (T) -->:
          (interface_failure_MRI_Out.value == !(upper_desired_tempWstatus_MRI_In.status == ValueStatus.Valid &
            lower_desired_tempWstatus_MRI_In.status == ValueStatus.Valid)),
        // case REQMRI8
        //   REQ-MRI-8
        (!(interface_failure_MRI_Out.value)) -->:
          (lower_desired_temp_MRI_Out.value == lower_desired_tempWstatus_MRI_In.value &
            upper_desired_temp_MRI_Out.value == upper_desired_tempWstatus_MRI_In.value)
        // END_COMPUTE ENSURES_timeTriggered
      )
    )
    // contract only
    halt("contract only")
  }
}

  //====================================================
  //  C o m m u n i c a t i o n
  //====================================================

object Comm {

  // ------- C o n t e x t   to   M R I
  def CONTEXT_to_MRI_current_tempWstatus(): Unit = {
    App.current_tempWstatus_MRI_In = Context.current_tempWstatus_CONTEXT_In
  }

  def CONTEXT_to_MRI_lower_desired_tempWstatus(): Unit = {
    App.lower_desired_tempWstatus_MRI_In = Context.lower_desired_tempWstatus_CONTEXT_In
  }

  def CONTEXT_to_MRI_upper_desired_tempWstatus(): Unit = {
    App.upper_desired_tempWstatus_MRI_In = Context.upper_desired_tempWstatus_CONTEXT_In
  }

  // ------- M R I  to C o n t e x t

  def MRI_to_CONTEXT_displayed_temperature(): Unit = {
    Context.displayed_temp_CONTEXT_Out = App.displayed_temp_MRI_Out
  }

  def MRI_to_CONTEXT_regulator_status(): Unit = {
    Context.regulator_Status_CONTEXT_Out = App.regulator_Status_MRI_Out
  }

  // ------- M R I  to   M H S

  def MRI_to_MHS_lower_desired_temp(): Unit = {
    App.lower_desired_temp_MHS_In = App.lower_desired_temp_MRI_Out
  }

  def MRI_to_MHS_upper_desired_temp(): Unit = {
    App.upper_desired_temp_MHS_In = App.upper_desired_temp_MRI_Out
  }

  // ------- C o n t e x t  to   M H S

  def CONTEXT_to_MHS_current_tempWstatus(): Unit = {
    App.current_temp_MHS_In = Context.current_tempWstatus_CONTEXT_In
  }

  // ------- M H S  to  C o n t e x t

  def MHS_to_CONTEXT_heat_control(): Unit = {
    Context.heat_control_CONTEXT_Out = App.heat_control_MHS_Out
  }

  // ------- M R I  to   M R M

  def MRI_to_MRM_interface_failure(): Unit = {
    App.interface_failure_MRM_In = App.interface_failure_MRI_Out
  }

  // ------- M R M  to   M R I

  def MRM_to_MRI_regulator_mode(): Unit = {
    App.regulator_mode_MRI_In = App.regulator_mode_MRM_Out
  }

  // ------- M R M  to   M H S

  def MRM_to_MHS_regulator_mode(): Unit = {
    App.regulator_mode_MHS_In = App.regulator_mode_MRM_Out
  }

  // ------- C o n t e x t  to   M R M

  def CONTEXT_to_MRM_current_tempWstatus(): Unit = {
    App.current_tempWstatus_MRM_In = Context.current_tempWstatus_CONTEXT_In
  }

 // TODO:  Dealing with interface failure ??

}

//=================================================================================
//  D e v e l o p e r  -   D e f i n e d      P r o p e r t i e s
//=================================================================================

// The developer specifies a post condition for the initialization phase to capture important
// properties of the state:
//   - at the end of the initialization phase
//   - at the beginning of the compute phase
//
// Often, this property should constrain each input port, output port
// and each thread local variable.
// In some cases, where one does not aim to prove complete correctness,
// it may be possible that only a limited number of ports/variables are constrained.
//

@strictpure def initPhasePostConditionProp(
                   // MRI
                   p_lower_desired_tempWstatus_MRI_In: TempWstatus_impl,
                   p_upper_desired_tempWstatus_MRI_In: TempWstatus_impl,
                   p_current_tempWstatus_MRI_In: TempWstatus_impl,
                   p_regulator_mode_MRI_In: Regulator_Mode.Type,
                   p_regulator_Status_MRI_Out: Status.Type,
                   p_displayed_temp_MRI_Out: Temp_impl,
                   p_interface_failure_MRI_Out: Failure_Flag_impl,
                   p_lower_desired_temp_MRI_Out: Temp_impl,
                   p_upper_desired_temp_MRI_Out: Temp_impl,
                   // MRM
                   p_lastRegulatorMode: Regulator_Mode.Type,
                   p_isFirstInvocation: B,
                   p_interface_failure_MRM_In: Failure_Flag_impl,
                   p_internal_failure_MRM_In: Failure_Flag_impl,
                   p_current_tempWstatus_MRM_In: TempWstatus_impl,
                   p_regulator_mode_MRM_Out: Regulator_Mode.Type,
                   // MHS
                   p_lastCmd: On_Off.Type,
                   p_upper_desired_temp_MHS_In: Temp_impl,
                   p_lower_desired_temp_MHS_In: Temp_impl,
                   p_current_temp_MHS_In: TempWstatus_impl,
                   p_regulator_mode_MHS_In: Regulator_Mode.Type,
                   p_heat_control_MHS_Out: On_Off.Type): B = (
   // --------------- MRI -----------------
   // p_lower_desired_tempWstatus_MRI_In == ?
   // p_upper_desired_tempWstatus_MRI_In == ?
   // p_current_tempWstatus_MRI_In == ?
   // p_regulator_mode_MRI_In == ?
   p_regulator_Status_MRI_Out == Status.Init_Status &
   // p_displayed_temp_MRI_Out == ?
   // p_interface_failure_MRI_Out == ?
   // p_lower_desired_temp_MRI_Out == ?
   // p_upper_desired_temp_MRI_Out == ?
  // ---------- MRM -----------
  // ToDo: Gage, you likely need to add some guarantee clauses to the initialize entry point
  //  to establish the values of the lastRegulatorMode and isFirstInvocation vars, since the
  //  behavior of the compute entrypoint depends on those
  // p_lastRegulatorMode == ?
  // p_isFirstInvocation == ?
  // p_interface_failure_MRM_In == ?
  // p_internal_failure_MRM_In == ?
  // p_current_tempWstatus_MRM_In == ?
  p_regulator_mode_MRM_Out == Regulator_Mode.Init_Regulator_Mode &
  // ------------ MHS ----------
  p_lastCmd == On_Off.Off &
  // ToDo: Gage, consider if you need to establish any properties of the
  //  these inputs before the Compute phase starts.  For example, if the compute entry point has
  //  preconditions on each of these values, you will likely need to initialization phase to
  //  get the ports in a state that satisfies those pre-conditions.
  //  For instance, we likely need that "lower" < "upper", below.
  // p_upper_desired_temp_MHS_In == ?
  // p_lower_desired_temp_MHS_In == ?
  // p_current_temp_MHS_In == ?
  // p_regulator_mode_MHS_In == ?
  p_heat_control_MHS_Out == On_Off.Off  // seeded error: On_Off.Onn
  )

//==================================================================================
//  S y s t e m     C o m p o s i t i o n
//==================================================================================

// Logical specification of the initial system state.  Generated automatically
// from AADL system description.
//
// Each port and component local variable should be given an initial value.
// The specific initial value SHOULD NOT be important, as each component
// should be responsible for initializing its own values/ports in the initialize
// entry points.
//
// However, to obtain an appropriate trace semantics, we need some initial state.
// The initial values for each type should be configured/determined by the framework
// somewhere, and the same values used across the Slang executable semantics,
// Isabelle semantics, and the Logika system checking framework.
//

def initialSystemState() : Unit = {
  Contract(
    Modifies(
      // context ports
      Context.current_tempWstatus_CONTEXT_In,
      Context.lower_desired_tempWstatus_CONTEXT_In,
      Context.upper_desired_tempWstatus_CONTEXT_In,
      Context.heat_control_CONTEXT_Out,
      Context.regulator_Status_CONTEXT_Out,
      Context.displayed_temp_CONTEXT_Out,
      // MRI
      App.lower_desired_tempWstatus_MRI_In,
      App.upper_desired_tempWstatus_MRI_In,
      App.current_tempWstatus_MRI_In,
      App.regulator_mode_MRI_In,
      App.regulator_Status_MRI_Out,
      App.displayed_temp_MRI_Out,
      App.interface_failure_MRI_Out,
      App.lower_desired_temp_MRI_Out,
      App.upper_desired_temp_MRI_Out,
      // MRM
      App.lastRegulatorMode,
      App.isFirstInvocation,
      App.interface_failure_MRM_In,
      App.internal_failure_MRM_In,
      App.current_tempWstatus_MRM_In,
      App.regulator_mode_MRM_Out,
        // MHS
      App.lastCmd,
      App.upper_desired_temp_MHS_In,
      App.lower_desired_temp_MHS_In,
      App.current_temp_MHS_In,
      App.regulator_mode_MHS_In,
      App.heat_control_MHS_Out
    ),
    Ensures(
      // context ports
      Context.current_tempWstatus_CONTEXT_In == TempWstatus_impl(z"0", ValueStatus.Invalid),
      Context.lower_desired_tempWstatus_CONTEXT_In == TempWstatus_impl(z"0", ValueStatus.Invalid),
      Context.upper_desired_tempWstatus_CONTEXT_In == TempWstatus_impl(z"0", ValueStatus.Invalid),
      Context.heat_control_CONTEXT_Out == On_Off.Off,
      Context.regulator_Status_CONTEXT_Out == Status.Init_Status,
      Context.displayed_temp_CONTEXT_Out == Temp_impl(z"0"),
      // MRI
      App.lower_desired_tempWstatus_MRI_In == TempWstatus_impl(z"0", ValueStatus.Invalid),
      App.upper_desired_tempWstatus_MRI_In == TempWstatus_impl(z"0", ValueStatus.Invalid),
      App.current_tempWstatus_MRI_In == TempWstatus_impl(z"0", ValueStatus.Invalid),
      App.regulator_mode_MRI_In == Regulator_Mode.Init_Regulator_Mode,
      App.regulator_Status_MRI_Out ==Status.Init_Status,
      App.displayed_temp_MRI_Out == Temp_impl(z"0"),
      App.interface_failure_MRI_Out == Failure_Flag_impl(F),
      App.lower_desired_temp_MRI_Out == Temp_impl(z"0"),
      App.upper_desired_temp_MRI_Out == Temp_impl(z"0"),
      // MRM
      App.lastRegulatorMode == Regulator_Mode.Init_Regulator_Mode,
      App.isFirstInvocation == true,
      App.interface_failure_MRM_In == Failure_Flag_impl(F),
      App.internal_failure_MRM_In == Failure_Flag_impl(F),
      App.current_tempWstatus_MRM_In == TempWstatus_impl(z"0", ValueStatus.Invalid),
      App.regulator_mode_MRM_Out == Regulator_Mode.Init_Regulator_Mode,
      // MHS
      App.lastCmd == On_Off.Off,
      App.upper_desired_temp_MHS_In == Temp_impl(z"0"),
      App.lower_desired_temp_MHS_In == Temp_impl(z"0"),
      App.current_temp_MHS_In == TempWstatus_impl(z"0", ValueStatus.Invalid),
      App.regulator_mode_MHS_In == Regulator_Mode.Init_Regulator_Mode,
      App.heat_control_MHS_Out == On_Off.Off
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
  // [2] "Assume" the pre-condition defined in a strict pure method (not shown -- see Temp Control example)

  // === run static schedule of system init phase ===
  //  This particular ordering of components is auto-generated and determined by the user-configured
  //  static scheduling framework
  //     (round-robin of each component initialize entry point)
  App.initialise_MRI()
  App.initialise_MRM()
  App.initialise_MHS()
  // === communication ===
  //  In general, the ordering of the communication in the initialization phase is not important
  //  since no input ports can be read.
  Comm.CONTEXT_to_MRI_current_tempWstatus()
  Comm.CONTEXT_to_MRI_lower_desired_tempWstatus()
  Comm.CONTEXT_to_MRI_upper_desired_tempWstatus()
  Comm.MRI_to_CONTEXT_displayed_temperature()
  Comm.MRI_to_CONTEXT_regulator_status()
  Comm.MRI_to_MHS_lower_desired_temp()
  Comm.MRI_to_MHS_upper_desired_temp()
  Comm.CONTEXT_to_MRM_current_tempWstatus()
  Comm.MHS_to_CONTEXT_heat_control()
  Comm.MRI_to_MRM_interface_failure()
  Comm.MRM_to_MRI_regulator_mode()
  Comm.MRM_to_MHS_regulator_mode()
  Comm.CONTEXT_to_MRM_current_tempWstatus()
  // === init phase post-condition (different strategies) ===
  assert(initPhasePostConditionProp(
    // MRI
    App.lower_desired_tempWstatus_MRI_In,
    App.upper_desired_tempWstatus_MRI_In,
    App.current_tempWstatus_MRI_In,
    App.regulator_mode_MRI_In,
    App.regulator_Status_MRI_Out,
    App.displayed_temp_MRI_Out,
    App.interface_failure_MRI_Out,
    App.lower_desired_temp_MRI_Out,
    App. upper_desired_temp_MRI_Out,
    // MRM
    App.lastRegulatorMode,
    App.isFirstInvocation,
    App.interface_failure_MRM_In,
    App.internal_failure_MRM_In,
    App.current_tempWstatus_MRM_In,
    App.regulator_mode_MRM_Out,
    // MHS
    App.lastCmd,
    App.upper_desired_temp_MHS_In,
    App.lower_desired_temp_MHS_In,
    App.current_temp_MHS_In,
    App.regulator_mode_MHS_In,
    App.heat_control_MHS_Out
  ))
}

// Framework "helper method" to assume the initialization phase post-condition.
// Putting this assume statement in this method (which will be unfolded)
// simplifies the text/code in the application verification scenarios.

def assumeInitPhasePostCondition(): Unit = {
  // Assume the initialize phase post-condition (this characterizes the initial state for the compute phase)
  //   This step should address all port state and component local variables by
  //    - giving them unconstrained (or otherwise appropropriate initial values)
  //    - constraining the variables above according to the user supplied initPhasePostCondition
  assume(initPhasePostConditionProp(
    // MRI
    App.lower_desired_tempWstatus_MRI_In,
    App.upper_desired_tempWstatus_MRI_In,
    App.current_tempWstatus_MRI_In,
    App.regulator_mode_MRI_In,
    App.regulator_Status_MRI_Out,
    App.displayed_temp_MRI_Out,
    App.interface_failure_MRI_Out,
    App.lower_desired_temp_MRI_Out,
    App.upper_desired_temp_MRI_Out,
    // MRM
    App.lastRegulatorMode,
    App.isFirstInvocation,
    App.interface_failure_MRM_In,
    App.internal_failure_MRM_In,
    App.current_tempWstatus_MRM_In,
    App.regulator_mode_MRM_Out,
    // MHS
    App.lastCmd,
    App.upper_desired_temp_MHS_In,
    App.lower_desired_temp_MHS_In,
    App.current_temp_MHS_In,
    App.regulator_mode_MHS_In,
    App.heat_control_MHS_Out
  ))
}

// ---------------------------------
//   C o m p u t e    P h a s e     V e r i f i c a t i o n
//   I n f r a s t r u c t u r e
// ---------------------------------

// The method below, which provides a "script" characterizing the semantics of one hyper-period
// of the compute phase, would be automatically generated by the framework.
// The user would not modify it.  Instead, the user would tweak
// the AADL-level contracts (primarily the compute entry point contracts) and initialization phase
// post-condition, and scenario specifications (see below)
//
// NOTE: This method is not meant to be called directly, but instead is referenced from a
//  "Scenario" method, in which the state of the system BEFORE the hyper-period is executed is set.

def hyperperiodImmediateComm(): Unit = {
  // ======= Unfold system execution according to the static schedule ==========

  // The communication steps below reflect all in-coming connections from the context.
  //
  // Discussion: The positioning of the communication from the context below MAY or MAY NOT be the right
  // way to model things.  In general, we are making assumptions about the scheduling of the
  // threads that are the sources for these connections as well as the ordering on the communication.
  // It is likely that those assumptions should be part of the AADL system-level specification framework
  // for compositional reasoning.
  //
  Comm.CONTEXT_to_MRI_current_tempWstatus()
  Comm.CONTEXT_to_MRI_lower_desired_tempWstatus()
  Comm.CONTEXT_to_MRI_upper_desired_tempWstatus()
  Comm.CONTEXT_to_MHS_current_tempWstatus()
  Comm.CONTEXT_to_MRM_current_tempWstatus()

  // --------- MRI ---------
  App.timeTriggered_MRI()

  // schedule all outgoing communication from MRI
  Comm.MRI_to_MHS_lower_desired_temp()
  Comm.MRI_to_MHS_upper_desired_temp()
  Comm.MRI_to_MRM_interface_failure()
  Comm.MRI_to_CONTEXT_displayed_temperature()
  Comm.MRI_to_CONTEXT_regulator_status()

  //
  // --------- MRM ---------
  App.timeTriggered_MRM()

  // schedule all outgoing communication from MRM
  Comm.MRM_to_MHS_regulator_mode()
  Comm.MRM_to_MRI_regulator_mode()

  // --------- MHS ----------
  App.timeTriggered_MHS()

  // schedule all outgoing communication from MHS
  Comm.MHS_to_CONTEXT_heat_control()

  //
  // ============= end unfolding ===============
}

//===========================================================================================
//  Symbolic Test Scenarios
//
//===========================================================================================


// ----------------- Scenario 1 --------------------
// This scenario fails to verify (which is the correct outcome,
//  but the reasons why are a bit tricky).
//
// Rough notes: consider the following scenario
//   - desired temperature range is [98..100]
//   - actual temperature is 97
// We might expect the heat source to be turned on (to move the 97 temp within the target range).
// However, the cannot be verified to happen because the mode may not be normal.
// If we assume that the system is in normal mode, then...

def scenario01pre(): Unit = {
  Contract(
    Modifies(
      Context.current_tempWstatus_CONTEXT_In,
      Context.lower_desired_tempWstatus_CONTEXT_In,
      Context.upper_desired_tempWstatus_CONTEXT_In
    ),
    Ensures(
      Context.current_tempWstatus_CONTEXT_In == TempWstatus_impl(97,ValueStatus.Valid),
      Context.lower_desired_tempWstatus_CONTEXT_In == TempWstatus_impl(98,ValueStatus.Valid),
      Context.upper_desired_tempWstatus_CONTEXT_In == TempWstatus_impl(100,ValueStatus.Valid)
  ))
  halt("Contract only")
}

def scenario01post(): Unit = {
  Contract(
    Requires(
      Context.heat_control_CONTEXT_Out == On_Off.Onn,
      Context.displayed_temp_CONTEXT_Out == Temp_impl(97),
      Context.regulator_Status_CONTEXT_Out == Status.On_Status
    )
  )
  halt("Contract Only")
}

def scenario01() : Unit = {
  // ========= inital state for system slice ==========
  // In this case, our slice starts with the beginning of the compute phase, and so we begin with the
  //  state characterized by the initialization phase post-condition
  //

  assumeInitPhasePostCondition()

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

  scenario01pre()

  hyperperiodImmediateComm()

  scenario01post()
}


// ----------------- Scenario 2 --------------------
// This scenario represents the "correction" of the scenario above:
//  - additional assumptions are made about
//      internal failure (false)
//      mode of the subsystem on previous iteration (normal)
//
// under these assumptions, the property verifies.

def scenario02pre(): Unit = {
  Contract(
    Modifies(
      // needed when setting the regulator mode to NORMAL
      App.lastRegulatorMode,
      App.regulator_mode_MRI_In,
      App.internal_failure_MRM_In,
      Context.current_tempWstatus_CONTEXT_In,
      Context.lower_desired_tempWstatus_CONTEXT_In,
      Context.upper_desired_tempWstatus_CONTEXT_In
    ),
    Ensures(
      App.lastRegulatorMode == Regulator_Mode.Normal_Regulator_Mode,
      App.regulator_mode_MRI_In == Regulator_Mode.Normal_Regulator_Mode,
      App.internal_failure_MRM_In == Failure_Flag_impl(F),
      Context.current_tempWstatus_CONTEXT_In == TempWstatus_impl(97,ValueStatus.Valid),
      Context.lower_desired_tempWstatus_CONTEXT_In == TempWstatus_impl(98,ValueStatus.Valid),
      Context.upper_desired_tempWstatus_CONTEXT_In == TempWstatus_impl(100,ValueStatus.Valid)
    ))
  halt("Contract only")
}

def scenario02post(): Unit = {
  Contract(
    Requires(
      Context.heat_control_CONTEXT_Out == On_Off.Onn,
      Context.displayed_temp_CONTEXT_Out == Temp_impl(97),
      Context.regulator_Status_CONTEXT_Out == Status.On_Status
    )
  )
  halt("Contract Only")
}

def scenario02() : Unit = {

  assumeInitPhasePostCondition()

  scenario02pre()

  hyperperiodImmediateComm()

  scenario02post()
}

// ----------------- Scenario 3 --------------------
// Same concept as above, but turn the heater off
// (case where current temperature exceeds the target range)
//

def scenario03pre(): Unit = {
  Contract(
    Modifies(
      // needed when setting the regulator mode to NORMAL
      App.lastRegulatorMode,
      App.regulator_mode_MRI_In,
      App.internal_failure_MRM_In,
      Context.current_tempWstatus_CONTEXT_In,
      Context.lower_desired_tempWstatus_CONTEXT_In,
      Context.upper_desired_tempWstatus_CONTEXT_In
    ),
    Ensures(
      App.lastRegulatorMode == Regulator_Mode.Normal_Regulator_Mode,
      App.regulator_mode_MRI_In == Regulator_Mode.Normal_Regulator_Mode,
      App.internal_failure_MRM_In == Failure_Flag_impl(F),
      Context.current_tempWstatus_CONTEXT_In == TempWstatus_impl(101,ValueStatus.Valid),
      Context.lower_desired_tempWstatus_CONTEXT_In == TempWstatus_impl(98,ValueStatus.Valid),
      Context.upper_desired_tempWstatus_CONTEXT_In == TempWstatus_impl(100,ValueStatus.Valid)
    ))
  halt("Contract only")
}

def scenario03post(): Unit = {
  Contract(
    Requires(
      Context.heat_control_CONTEXT_Out == On_Off.Off,
      Context.displayed_temp_CONTEXT_Out == Temp_impl(101),
      Context.regulator_Status_CONTEXT_Out == Status.On_Status
    )
  )
  halt("Contract Only")
}

def scenario03() : Unit = {

  assumeInitPhasePostCondition()

  scenario03pre()

  hyperperiodImmediateComm()

  scenario03post()
}

// ----------------- Scenario 4 --------------------
// Invalid status on a single desired temperature range
// leads to
//   - subsystem status FAILURE (as reported by the MRI to the context)
//   - heater OFF (as commanded by the MSH to the context)
//  in one hyper-period
//
//  Note (specification/requirements BUG): This scenario uncovered a flaw in the specification structure.
//  In general, we require a range constraint that the lower desired temperature is less
//  than or equal to the upper desired temperature.
//  Specifically, we need this in the MHS component.
//
//  However, the contract of MRI only guarantees that this holds for the
//  output if the temperature fields are valid (specifically, in terms of MRI outputs,
//  if there is no interface failue).  Indeed, if a field has an
//  invalid status value, then the value is untrustworthy, so it would not make
//  sense to insist on the range constraint.
//
//  Note: It would be interesting if system testing with GUMBOX contracts could uncover this issue.
//
//  Therefore, the contract relating low and high for the MHS are
//  changed so that they only apply when the mode is NORMAL
//
//  Note: (improper assumptions regarding timing bug)
//   This issue is related to "it takes the system time to stablize"
//    (in this case, once an improper value is detected, given the current static schedule,
//    it takes the system two HP for a FAILURE status to be propagated by to the user interface).
//   Details -- when MRI is executing, it detects the invalid value status and communicates
//    an interface failure status to MRM.  It also does NOT guarantee that the set point values
//    communicated to the MHS satisfy the low < high invariant.   However, it does not report
//    a failed status to the operator interface, because that only takes place based on a FAILED
//    mode.  In the first HP, the mode is not set to FAILED until the MRM executes (which is after
//    the MRI).  So at the conclusion of MRI, an interface failure has been sent to MRM, but
//    e.g., an NORMAL or INIT status has been sent to the operator interface.
//
//    When MRM executes, the interface failure input causes it to output a FAILED mode
//    to MHS (which will be consumed on the current HP), and the MRI (which will be consumed
//    on the next HP).
//
//    In MHS, the revised precondition states that we only insist on setpoint low < high when
//    the mode is NORMAL (in this case, the mode is failed), so the command to the heat source
//    is set to OFF.
//
//    Now on the next HP, when the MRI executes it, from the MRM it receives a FAILED mode,
//    so it will set subsystem status to FAILED.
//
//    In the MRM, becuase the mode was previously failed, the mode continues to be failed.
//    In the MHS, because the mode is FAILED, the command to the heat source continues to be OFF.
//

def scenario04pre(): Unit = {
  Contract(
    Modifies(
      // needed when setting the regulator mode to NORMAL
      App.lastRegulatorMode,
      App.regulator_mode_MRI_In,
      App.internal_failure_MRM_In,
      Context.current_tempWstatus_CONTEXT_In,
      Context.lower_desired_tempWstatus_CONTEXT_In,
      Context.upper_desired_tempWstatus_CONTEXT_In
    ),
    Ensures(
      App.lastRegulatorMode == Regulator_Mode.Normal_Regulator_Mode,
      App.regulator_mode_MRI_In == Regulator_Mode.Normal_Regulator_Mode,
      App.internal_failure_MRM_In == Failure_Flag_impl(F),
      Context.current_tempWstatus_CONTEXT_In == TempWstatus_impl(101,ValueStatus.Valid),
      Context.lower_desired_tempWstatus_CONTEXT_In == TempWstatus_impl(98,ValueStatus.Invalid),  // Assume lower desired temperature is invalid
      Context.upper_desired_tempWstatus_CONTEXT_In == TempWstatus_impl(100,ValueStatus.Valid)
    ))
  halt("Contract only")
}

def scenario04post(): Unit = {
  Contract(
    Requires(
      Context.heat_control_CONTEXT_Out == On_Off.Off,
      // Context.displayed_temp_CONTEXT_Out == Temp_impl(101),
      Context.regulator_Status_CONTEXT_Out == Status.Failed_Status
    )
  )
  halt("Contract Only")
}

def scenario04_1HP() : Unit = {

  assumeInitPhasePostCondition()

  scenario04pre()

  hyperperiodImmediateComm()

  scenario04post()
}

def scenario04_2HP() : Unit = {

  assumeInitPhasePostCondition()

  scenario04pre()

  hyperperiodImmediateComm()
  hyperperiodImmediateComm()

  scenario04post()
}


def scenario05pre(): Unit = {
  Contract(
    Modifies(
      // needed when setting the regulator mode to FAILED
      App.lastRegulatorMode,
      // needed performing logical actions that havoc all context values
      Context.current_tempWstatus_CONTEXT_In,
      Context.lower_desired_tempWstatus_CONTEXT_In,
      Context.upper_desired_tempWstatus_CONTEXT_In
    ),
    Ensures(
      App.lastRegulatorMode == Regulator_Mode.Failed_Regulator_Mode, // NOTE: when this is phrased an invariant, it should be conditioned failure for the last hyper-period
      // the below represents an invariant on the context condition that must always hold
      (Context.lower_desired_tempWstatus_CONTEXT_In.status == ValueStatus.Valid &
        Context.upper_desired_tempWstatus_CONTEXT_In.status == ValueStatus.Valid) -->:
        (Context.lower_desired_tempWstatus_CONTEXT_In.value <= Context.upper_desired_tempWstatus_CONTEXT_In.value)
    ))
  halt("Contract only")
}

def scenario05post(): Unit = {
  Contract(
    Requires(
      App.lastRegulatorMode == Regulator_Mode.Failed_Regulator_Mode
      // Context.heat_control_CONTEXT_Out == On_Off.Off,
    )
  )
  halt("Contract Only")
}

def scenario05_1HP() : Unit = {

  assumeInitPhasePostCondition()
  scenario05pre()

  hyperperiodImmediateComm()

  scenario05post()
}


//=====================================================================================
//  I n v a r i a n t  -  B a s e d     V e r i f i c a t i o n
//=====================================================================================

// Establish that basic control properties hold in NORMAL mode.
// =============================================================
// This requires a bit of care as we must have appropriate relationships
//   - on the setpoints (status is valid and low is less than high)
//   - we are starting in NORMAL mode
//       (actually no!  We are not saying that we are starting in NORMAL mode.
//       We are actually saying that by the time we execute the control laws in MHS,
//       the mode value feeding into MHS is normal)
//   - the input temperature value is valid
//       (actually no!  we are not assuming this.  We are instead conditioned
//        on the mode being normal at the MHS.  If the input temperature is invalid,
//        then the mode will not be normal.)
//
//  Given these assumptions, we want to show that
//    current temp < low set point  causes fan to be ON
//    current temp > high set point causes fan to be OFF
//    current temp >= low set point AND <= high set point causes fan to be the same as previous value

//  Notes:
//  - In the current specification, we are not requiring the input temperature to be valid,
//    so I am not sure how the property is verifying.
//  - an important point to understand
//      - for the properties above (like "in NORMAL mode" and "temperature is valid",
//        we are referencing those values at specific places in the dataflow of the system
//        (at particular port locations) and at particular points in time.
//  - the way that the current scripts are set up, here are ways that we are referencing things:
//      set points - these are contrained on the context values, which flow directly to the MRI
//          Comm.CONTEXT_to_MRI_lower_desired_tempWstatus()
//          Comm.CONTEXT_to_MRI_upper_desired_tempWstatus()
//      current temperature - constrained on the context values, which flow to MHS, MRM, and MRI
//          Comm.CONTEXT_to_MHS_current_tempWstatus()
//          Comm.CONTEXT_to_MRM_current_tempWstatus()
//          Comm.CONTEXT_to_MRI_current_tempWstatus()
//   - when we have an invariant property, it will have a
//      precondition - which is referencing the state of the application ports at the end of the
//          previous frame
//      postcondition - which is referencing the state of the application ports at the end of
//          current frame




// ----------------- Invariant Low Temp --------------------

// Context assumptions
//  - if the setpoints are valid, then the lower < upper constraint holds
@strictpure def contextDesiredTempInvProp(
                               // Context In
                               p_current_tempWstatus_CONTEXT_In: TempWstatus_impl,
                               p_lower_desired_tempWstatus_CONTEXT_In: TempWstatus_impl,
                               p_upper_desired_tempWstatus_CONTEXT_In: TempWstatus_impl,
                               // Context Out
                               p_heat_control_CONTEXT_Out: On_Off.Type,
                               p_regulator_Status_CONTEXT_Out: Status.Type,
                               p_displayed_temp_CONTEXT_Out: Temp_impl): B = (
  // ToDo: Enhance this to implement directly the environment assumptions from the REMH
  // p_current_tempWstatus_CONTEXT_In ==
  // p_lower_desired_tempWstatus_CONTEXT_In
  // p_upper_desired_tempWstatus_CONTEXT_In
    (p_lower_desired_tempWstatus_CONTEXT_In.status == ValueStatus.Valid &
      p_upper_desired_tempWstatus_CONTEXT_In.status == ValueStatus.Valid) ->:
      (p_lower_desired_tempWstatus_CONTEXT_In.value < p_upper_desired_tempWstatus_CONTEXT_In.value)
  // Context Out
  // p_heat_control_CONTEXT_Out ==
  // p_regulator_Status_CONTEXT_Out ==
  // p_displayed_temp_CONTEXT_Out ==
)

// assume the invariant for the context values
def assumeContextDesiredTempInv(): Unit = {
  assume(contextDesiredTempInvProp(
    Context.current_tempWstatus_CONTEXT_In,
    Context.lower_desired_tempWstatus_CONTEXT_In,
    Context.upper_desired_tempWstatus_CONTEXT_In,
    Context.heat_control_CONTEXT_Out,
    Context.regulator_Status_CONTEXT_Out,
    Context.displayed_temp_CONTEXT_Out))
}

// Invariant Property:
//  - when the mode is normal (at the point where it is flowing into MHS),
//    if the current temperature is less that the low set point (at the point where values are flowing into the MRI),
//    then the heat control is On (at the point where the value is flowing out of the MHS)
//
//  ToDo: Consider when this invariant first gets estalished.
//        In particular, consider the transition from Init mode to Normal mode.
@strictpure def heatControlManagementLowTempInvProp( // antecendents
                                                     i_lower_desired_tempWstatus_MRI_In: TempWstatus_impl,
                                                     i_upper_desired_tempWstatus_MRI_In: TempWstatus_impl,
                                                     i_current_tempWstatus_MRI_In: TempWstatus_impl,
                                                     i_regulator_mode_MRM_Out: Regulator_Mode.Type, //  i_regulator_mode_MHS_In: Regulator_Mode.Type,
                                                     // consequent
                                                     i_heat_control_MHS_Out: On_Off.Type
                                            ): B = (
  // mode is normal
  ((i_regulator_mode_MRM_Out == Regulator_Mode.Normal_Regulator_Mode)
    ->: ((i_current_tempWstatus_MRI_In.value < i_lower_desired_tempWstatus_MRI_In.value)
          ->: (i_heat_control_MHS_Out == On_Off.Onn)))
)


@strictpure def invLowTempProp(
                                            // MRI
                                            p_lower_desired_tempWstatus_MRI_In: TempWstatus_impl,
                                            p_upper_desired_tempWstatus_MRI_In: TempWstatus_impl,
                                            p_current_tempWstatus_MRI_In: TempWstatus_impl,
                                            p_regulator_mode_MRI_In: Regulator_Mode.Type,
                                            p_regulator_Status_MRI_Out: Status.Type,
                                            p_displayed_temp_MRI_Out: Temp_impl,
                                            p_interface_failure_MRI_Out: Failure_Flag_impl,
                                            p_lower_desired_temp_MRI_Out: Temp_impl,
                                            p_upper_desired_temp_MRI_Out: Temp_impl,
                                            // MRM
                                            p_lastRegulatorMode: Regulator_Mode.Type,
                                            p_isFirstInvocation: B,
                                            p_interface_failure_MRM_In: Failure_Flag_impl,
                                            p_internal_failure_MRM_In: Failure_Flag_impl,
                                            p_current_tempWstatus_MRM_In: TempWstatus_impl,
                                            p_regulator_mode_MRM_Out: Regulator_Mode.Type,
                                            // MHS
                                            p_lastCmd: On_Off.Type,
                                            p_upper_desired_temp_MHS_In: Temp_impl,
                                            p_lower_desired_temp_MHS_In: Temp_impl,
                                            p_current_temp_MHS_In: TempWstatus_impl,
                                            p_regulator_mode_MHS_In: Regulator_Mode.Type,
                                            p_heat_control_MHS_Out: On_Off.Type): B = (
  // heat control management invariants
  heatControlManagementLowTempInvProp(p_lower_desired_tempWstatus_MRI_In,
                               p_upper_desired_tempWstatus_MRI_In,
                               p_current_tempWstatus_MRI_In,
                               p_regulator_mode_MRM_Out, // p_regulator_mode_MHS_In,
                               p_heat_control_MHS_Out)
  )

def assumeInvLowTemp(): Unit = {
  // Assume the initialize phase post-condition (this characterizes the initial state for the compute phase)
  //   This step should address all port state and component local variables by
  //    - giving them unconstrained (or otherwise appropropriate initial values)
  //    - constraining the variables above according to the user supplied initPhasePostCondition
  assume(invLowTempProp(
    // MRI
    App.lower_desired_tempWstatus_MRI_In,
    App.upper_desired_tempWstatus_MRI_In,
    App.current_tempWstatus_MRI_In,
    App.regulator_mode_MRI_In,
    App.regulator_Status_MRI_Out,
    App.displayed_temp_MRI_Out,
    App.interface_failure_MRI_Out,
    App.lower_desired_temp_MRI_Out,
    App.upper_desired_temp_MRI_Out,
    // MRM
    App.lastRegulatorMode,
    App.isFirstInvocation,
    App.interface_failure_MRM_In,
    App.internal_failure_MRM_In,
    App.current_tempWstatus_MRM_In,
    App.regulator_mode_MRM_Out,
    // MHS
    App.lastCmd,
    App.upper_desired_temp_MHS_In,
    App.lower_desired_temp_MHS_In,
    App.current_temp_MHS_In,
    App.regulator_mode_MHS_In,
    App.heat_control_MHS_Out
  ))
}

def assertInvLowTemp(): Unit = {
  //  Debugging: I found that the assertion was able to be (correctly) violated here
  // Assume the initialize phase post-condition (this characterizes the initial state for the compute phase)
  //   This step should address all port state and component local variables by
  //    - giving them unconstrained (or otherwise appropropriate initial values)
  //    - constraining the variables above according to the user supplied initPhasePostCondition
  assert(invLowTempProp(
    // MRI
    App.lower_desired_tempWstatus_MRI_In,
    App.upper_desired_tempWstatus_MRI_In,
    App.current_tempWstatus_MRI_In,
    App.regulator_mode_MRI_In,
    App.regulator_Status_MRI_Out,
    App.displayed_temp_MRI_Out,
    App.interface_failure_MRI_Out,
    App.lower_desired_temp_MRI_Out,
    App.upper_desired_temp_MRI_Out,
    // MRM
    App.lastRegulatorMode,
    App.isFirstInvocation,
    App.interface_failure_MRM_In,
    App.internal_failure_MRM_In,
    App.current_tempWstatus_MRM_In,
    App.regulator_mode_MRM_Out,
    // MHS
    App.lastCmd,
    App.upper_desired_temp_MHS_In,
    App.lower_desired_temp_MHS_In,
    App.current_temp_MHS_In,
    App.regulator_mode_MHS_In,
    App.heat_control_MHS_Out
  ))
}

def inv01_debugging() : Unit = {
  assumeInvLowTemp()
  assumeContextDesiredTempInv()
//  assert(!(Context.lower_desired_tempWstatus_CONTEXT_In.status == ValueStatus.Valid &
//           Context.upper_desired_tempWstatus_CONTEXT_In.status == ValueStatus.Valid))
  hyperperiodImmediateComm()
  // assert(!(App.regulator_mode_MRM_Out == Regulator_Mode.Normal_Regulator_Mode))
  // assert(!(Context.lower_desired_tempWstatus_CONTEXT_In.status == ValueStatus.Valid &
  //         Context.upper_desired_tempWstatus_CONTEXT_In.status == ValueStatus.Valid))
  // assert(!(App.current_tempWstatus_MRI_In.value < App.lower_desired_tempWstatus_MRI_In.value))
  // assert(!((App.regulator_mode_MRM_Out == Regulator_Mode.Normal_Regulator_Mode)
  //          & (App.current_tempWstatus_MRI_In.value < App.lower_desired_tempWstatus_MRI_In.value)))
  assert(((App.regulator_mode_MRM_Out == Regulator_Mode.Normal_Regulator_Mode) &
          (App.current_tempWstatus_MRI_In.value < App.lower_desired_tempWstatus_MRI_In.value))
            ->: (App.heat_control_MHS_Out == On_Off.Onn))
// assertInvLowTemp()
}

def inv01_inlined() : Unit = {
  assume(((App.regulator_mode_MRM_Out == Regulator_Mode.Normal_Regulator_Mode) &
    (App.current_tempWstatus_MRI_In.value < App.lower_desired_tempWstatus_MRI_In.value))
    ->: (App.heat_control_MHS_Out == On_Off.Onn))
  assumeContextDesiredTempInv()
  //  assert(!(Context.lower_desired_tempWstatus_CONTEXT_In.status == ValueStatus.Valid &
  //           Context.upper_desired_tempWstatus_CONTEXT_In.status == ValueStatus.Valid))
  hyperperiodImmediateComm()
  // assert(!(App.regulator_mode_MRM_Out == Regulator_Mode.Normal_Regulator_Mode))
  // assert(!(Context.lower_desired_tempWstatus_CONTEXT_In.status == ValueStatus.Valid &
  //         Context.upper_desired_tempWstatus_CONTEXT_In.status == ValueStatus.Valid))
  // assert(!(App.current_tempWstatus_MRI_In.value < App.lower_desired_tempWstatus_MRI_In.value))
  // assert(!((App.regulator_mode_MRM_Out == Regulator_Mode.Normal_Regulator_Mode)
  //          & (App.current_tempWstatus_MRI_In.value < App.lower_desired_tempWstatus_MRI_In.value)))
  assert(((App.regulator_mode_MRM_Out == Regulator_Mode.Normal_Regulator_Mode) &
    (App.current_tempWstatus_MRI_In.value < App.lower_desired_tempWstatus_MRI_In.value))
    ->: (App.heat_control_MHS_Out == On_Off.Onn))
  // assertInvLowTemp()
}

def inv01_1HP() : Unit = {
  assumeInvLowTemp()
  assumeContextDesiredTempInv()
  hyperperiodImmediateComm()
  assertInvLowTemp()
}

def supportingProperty_NormalImpliesValidSetPoints_inlined() : Unit = {
  assumeContextDesiredTempInv()
  //  assert(!(Context.lower_desired_tempWstatus_CONTEXT_In.status == ValueStatus.Valid &
  //           Context.upper_desired_tempWstatus_CONTEXT_In.status == ValueStatus.Valid))
  hyperperiodImmediateComm()
  // assert(!(App.regulator_mode_MRM_Out == Regulator_Mode.Normal_Regulator_Mode))
  // assert(!(Context.lower_desired_tempWstatus_CONTEXT_In.status == ValueStatus.Valid &
  //         Context.upper_desired_tempWstatus_CONTEXT_In.status == ValueStatus.Valid))
  // assert(!(App.current_tempWstatus_MRI_In.value < App.lower_desired_tempWstatus_MRI_In.value))
  // assert(!((App.regulator_mode_MRM_Out == Regulator_Mode.Normal_Regulator_Mode)
  //          & (App.current_tempWstatus_MRI_In.value < App.lower_desired_tempWstatus_MRI_In.value)))
  assert((App.regulator_mode_MRM_Out == Regulator_Mode.Normal_Regulator_Mode) ->: (App.current_tempWstatus_MRI_In.status == ValueStatus.Valid))
  assert((App.regulator_mode_MRM_Out == Regulator_Mode.Normal_Regulator_Mode) ->: (App.lower_desired_tempWstatus_MRI_In.status == ValueStatus.Valid))
  assert((App.regulator_mode_MRM_Out == Regulator_Mode.Normal_Regulator_Mode) ->: (App.upper_desired_tempWstatus_MRI_In.status == ValueStatus.Valid))
}


// ----------------- Inv 02 --------------------
//  currentTemp greater than high set point implies fan is commanded OFF
//
// Context assumptions
//  - if the setpoints are valid, then the lower < upper constraint holds

// Invariant Property:
//  - when the mode is normal (at the point where it is flowing into MHS),
//    if the current temperature is greater than the high set point (at the point where values are flowing into the MRI),
//    then the heat control is Off (at the point where the value is flowing out of the MHS)
//
//  ToDo: Consider when this invariant first gets estalished.
//        In particular, consider the transition from Init mode to Normal mode.
@strictpure def heatControlManagementHighTempInvProp( // antecendents
                                                     i_lower_desired_tempWstatus_MRI_In: TempWstatus_impl,
                                                     i_upper_desired_tempWstatus_MRI_In: TempWstatus_impl,
                                                     i_current_tempWstatus_MRI_In: TempWstatus_impl,
                                                     i_regulator_mode_MRM_Out: Regulator_Mode.Type,
                                                     // consequent
                                                     i_heat_control_MHS_Out: On_Off.Type
                                                   ): B = (
  // mode is normal
  ((i_regulator_mode_MRM_Out == Regulator_Mode.Normal_Regulator_Mode)
    ->: ((i_current_tempWstatus_MRI_In.value > i_upper_desired_tempWstatus_MRI_In.value)
    ->: (i_heat_control_MHS_Out == On_Off.Off)))
  )


@strictpure def invHighTempProp(
                                // MRI
                                p_lower_desired_tempWstatus_MRI_In: TempWstatus_impl,
                                p_upper_desired_tempWstatus_MRI_In: TempWstatus_impl,
                                p_current_tempWstatus_MRI_In: TempWstatus_impl,
                                p_regulator_mode_MRI_In: Regulator_Mode.Type,
                                p_regulator_Status_MRI_Out: Status.Type,
                                p_displayed_temp_MRI_Out: Temp_impl,
                                p_interface_failure_MRI_Out: Failure_Flag_impl,
                                p_lower_desired_temp_MRI_Out: Temp_impl,
                                p_upper_desired_temp_MRI_Out: Temp_impl,
                                // MRM
                                p_lastRegulatorMode: Regulator_Mode.Type,
                                p_isFirstInvocation: B,
                                p_interface_failure_MRM_In: Failure_Flag_impl,
                                p_internal_failure_MRM_In: Failure_Flag_impl,
                                p_current_tempWstatus_MRM_In: TempWstatus_impl,
                                p_regulator_mode_MRM_Out: Regulator_Mode.Type,
                                // MHS
                                p_lastCmd: On_Off.Type,
                                p_upper_desired_temp_MHS_In: Temp_impl,
                                p_lower_desired_temp_MHS_In: Temp_impl,
                                p_current_temp_MHS_In: TempWstatus_impl,
                                p_regulator_mode_MHS_In: Regulator_Mode.Type,
                                p_heat_control_MHS_Out: On_Off.Type): B = (
  // heat control management invariants
  heatControlManagementHighTempInvProp(p_lower_desired_tempWstatus_MRI_In,
    p_upper_desired_tempWstatus_MRI_In,
    p_current_tempWstatus_MRI_In,
    p_regulator_mode_MRM_Out,
    p_heat_control_MHS_Out)
  )

def assumeInvHighTemp(): Unit = {
  // Assume the initialize phase post-condition (this characterizes the initial state for the compute phase)
  //   This step should address all port state and component local variables by
  //    - giving them unconstrained (or otherwise appropropriate initial values)
  //    - constraining the variables above according to the user supplied initPhasePostCondition
  assume(invHighTempProp(
    // MRI
    App.lower_desired_tempWstatus_MRI_In,
    App.upper_desired_tempWstatus_MRI_In,
    App.current_tempWstatus_MRI_In,
    App.regulator_mode_MRI_In,
    App.regulator_Status_MRI_Out,
    App.displayed_temp_MRI_Out,
    App.interface_failure_MRI_Out,
    App.lower_desired_temp_MRI_Out,
    App.upper_desired_temp_MRI_Out,
    // MRM
    App.lastRegulatorMode,
    App.isFirstInvocation,
    App.interface_failure_MRM_In,
    App.internal_failure_MRM_In,
    App.current_tempWstatus_MRM_In,
    App.regulator_mode_MRM_Out,
    // MHS
    App.lastCmd,
    App.upper_desired_temp_MHS_In,
    App.lower_desired_temp_MHS_In,
    App.current_temp_MHS_In,
    App.regulator_mode_MHS_In,
    App.heat_control_MHS_Out
  ))
}

def assertInvHighTemp(): Unit = {
  // Assume the initialize phase post-condition (this characterizes the initial state for the compute phase)
  //   This step should address all port state and component local variables by
  //    - giving them unconstrained (or otherwise appropropriate initial values)
  //    - constraining the variables above according to the user supplied initPhasePostCondition
  assert(invHighTempProp(
    // MRI
    App.lower_desired_tempWstatus_MRI_In,
    App.upper_desired_tempWstatus_MRI_In,
    App.current_tempWstatus_MRI_In,
    App.regulator_mode_MRI_In,
    App.regulator_Status_MRI_Out,
    App.displayed_temp_MRI_Out,
    App.interface_failure_MRI_Out,
    App.lower_desired_temp_MRI_Out,
    App.upper_desired_temp_MRI_Out,
    // MRM
    App.lastRegulatorMode,
    App.isFirstInvocation,
    App.interface_failure_MRM_In,
    App.internal_failure_MRM_In,
    App.current_tempWstatus_MRM_In,
    App.regulator_mode_MRM_Out,
    // MHS
    App.lastCmd,
    App.upper_desired_temp_MHS_In,
    App.lower_desired_temp_MHS_In,
    App.current_temp_MHS_In,
    App.regulator_mode_MHS_In,
    App.heat_control_MHS_Out
  ))
}


def inv02_1HP() : Unit = {
  assumeInvHighTemp()
  assumeContextDesiredTempInv()
  hyperperiodImmediateComm()
  assertInvHighTemp()
}

def inv02_inlined() : Unit = {
  assumeContextDesiredTempInv()
  hyperperiodImmediateComm()
  assert( (App.regulator_mode_MRM_Out == Regulator_Mode.Normal_Regulator_Mode)
         ->: ((App.current_tempWstatus_MRI_In.value > App.upper_desired_tempWstatus_MRI_In.value)
              ->: (App.heat_control_MHS_Out == On_Off.Off))
        )
}

// ----------------- Verification 03 --------------------
//  currentTemp greater or equal to low set point and less than or equal to high set point
//    implies that the fan remains in the same state
//
// Context assumptions
//  - if the setpoints are valid, then the lower < upper constraint holds

// Invariant Property:
//  - when the mode is normal (at the point where it is flowing out of the mode manager),
//    if the current temperature is greater than the high set point (at the point where values are flowing into the MRI),
//    then the heat control setting has the same value as it did in the previous state at this temporal observation point,
//    (i.e., at the previous frame boundary).
//
//  ToDo: Consider when this invariant first gets estalished.
//        In particular, consider the transition from Init mode to Normal mode.
@strictpure def heatControlManagementBetweenPropPost( // antecendents
                                                      i_lower_desired_tempWstatus_MRI_In: TempWstatus_impl,
                                                      i_upper_desired_tempWstatus_MRI_In: TempWstatus_impl,
                                                      i_current_tempWstatus_MRI_In: TempWstatus_impl,
                                                      i_regulator_mode_MRM_Out: Regulator_Mode.Type,
                                                      // consequent
                                                      i_heat_control_MHS_Out: On_Off.Type,
                                                      i_heat_control_MHS_Out_PRE: On_Off.Type
                                                    ): B = (
  // mode is normal
  ((i_regulator_mode_MRM_Out == Regulator_Mode.Normal_Regulator_Mode)
    ->: (((i_current_tempWstatus_MRI_In.value >= i_lower_desired_tempWstatus_MRI_In.value) &
         (i_current_tempWstatus_MRI_In.value <= i_upper_desired_tempWstatus_MRI_In.value))
    ->: (i_heat_control_MHS_Out == i_heat_control_MHS_Out_PRE)))
  )


@strictpure def heatControlManagementBetweenPropPostState(
                                 // MRI
                                 p_lower_desired_tempWstatus_MRI_In: TempWstatus_impl,
                                 p_upper_desired_tempWstatus_MRI_In: TempWstatus_impl,
                                 p_current_tempWstatus_MRI_In: TempWstatus_impl,
                                 p_regulator_mode_MRI_In: Regulator_Mode.Type,
                                 p_regulator_Status_MRI_Out: Status.Type,
                                 p_displayed_temp_MRI_Out: Temp_impl,
                                 p_interface_failure_MRI_Out: Failure_Flag_impl,
                                 p_lower_desired_temp_MRI_Out: Temp_impl,
                                 p_upper_desired_temp_MRI_Out: Temp_impl,
                                 // MRM
                                 p_lastRegulatorMode: Regulator_Mode.Type,
                                 p_isFirstInvocation: B,
                                 p_interface_failure_MRM_In: Failure_Flag_impl,
                                 p_internal_failure_MRM_In: Failure_Flag_impl,
                                 p_current_tempWstatus_MRM_In: TempWstatus_impl,
                                 p_regulator_mode_MRM_Out: Regulator_Mode.Type,
                                 // MHS
                                 p_lastCmd: On_Off.Type,
                                 p_upper_desired_temp_MHS_In: Temp_impl,
                                 p_lower_desired_temp_MHS_In: Temp_impl,
                                 p_current_temp_MHS_In: TempWstatus_impl,
                                 p_regulator_mode_MHS_In: Regulator_Mode.Type,
                                 p_heat_control_MHS_Out: On_Off.Type,
                                 p_heat_control_MHS_Out_PRE: On_Off.Type): B = (
  // heat control management invariants
  heatControlManagementBetweenPropPost(p_lower_desired_tempWstatus_MRI_In,
    p_upper_desired_tempWstatus_MRI_In,
    p_current_tempWstatus_MRI_In,
    p_regulator_mode_MRM_Out,
    p_heat_control_MHS_Out,
    p_heat_control_MHS_Out_PRE
  )
  )

//*************** To Do:  Need to set this property to the post-condition ******************************
def assume_heatControlManagementBetweenPropPre(): Unit = {
  // Assume the initialize phase post-condition (this characterizes the initial state for the compute phase)
  //   This step should address all port state and component local variables by
  //    - giving them unconstrained (or otherwise appropropriate initial values)
  //    - constraining the variables above according to the user supplied initPhasePostCondition
  assume(heatControlManagementBetweenPropPostState(
    // MRI
    App.lower_desired_tempWstatus_MRI_In,
    App.upper_desired_tempWstatus_MRI_In,
    App.current_tempWstatus_MRI_In,
    App.regulator_mode_MRI_In,
    App.regulator_Status_MRI_Out,
    App.displayed_temp_MRI_Out,
    App.interface_failure_MRI_Out,
    App.lower_desired_temp_MRI_Out,
    App.upper_desired_temp_MRI_Out,
    // MRM
    App.lastRegulatorMode,
    App.isFirstInvocation,
    App.interface_failure_MRM_In,
    App.internal_failure_MRM_In,
    App.current_tempWstatus_MRM_In,
    App.regulator_mode_MRM_Out,
    // MHS
    App.lastCmd,
    App.upper_desired_temp_MHS_In,
    App.lower_desired_temp_MHS_In,
    App.current_temp_MHS_In,
    App.regulator_mode_MHS_In,
    App.heat_control_MHS_Out,
    App.heat_control_MHS_Out_PRE
  ))
}

def assert_heatControlManagementBetweenPropPost(): Unit = {
  // Assume the initialize phase post-condition (this characterizes the initial state for the compute phase)
  //   This step should address all port state and component local variables by
  //    - giving them unconstrained (or otherwise appropropriate initial values)
  //    - constraining the variables above according to the user supplied initPhasePostCondition
  assert(heatControlManagementBetweenPropPostState(
    // MRI
    App.lower_desired_tempWstatus_MRI_In,
    App.upper_desired_tempWstatus_MRI_In,
    App.current_tempWstatus_MRI_In,
    App.regulator_mode_MRI_In,
    App.regulator_Status_MRI_Out,
    App.displayed_temp_MRI_Out,
    App.interface_failure_MRI_Out,
    App.lower_desired_temp_MRI_Out,
    App.upper_desired_temp_MRI_Out,
    // MRM
    App.lastRegulatorMode,
    App.isFirstInvocation,
    App.interface_failure_MRM_In,
    App.internal_failure_MRM_In,
    App.current_tempWstatus_MRM_In,
    App.regulator_mode_MRM_Out,
    // MHS
    App.lastCmd,
    App.upper_desired_temp_MHS_In,
    App.lower_desired_temp_MHS_In,
    App.current_temp_MHS_In,
    App.regulator_mode_MHS_In,
    App.heat_control_MHS_Out,
    App.heat_control_MHS_Out_PRE
  ))
}

def heatControlManagementBetweenProp_1HP() : Unit = {
  assume_heatControlManagementBetweenPropPre()
  assumeContextDesiredTempInv()
  App.heat_control_MHS_Out_PRE = App.heat_control_MHS_Out  // capture needed pre-state element
  hyperperiodImmediateComm()
  assert_heatControlManagementBetweenPropPost()
}

def heatControlManagementBetweenProp_inlined() : Unit = {
  assume( // small chunk of application logic invariant needed for property
    (App.heat_control_MHS_Out == App.lastCmd)
  )
  assumeContextDesiredTempInv()
  App.heat_control_MHS_Out_PRE = App.heat_control_MHS_Out  // capture needed pre-state element
  hyperperiodImmediateComm()
  assert(
    (App.regulator_mode_MRM_Out == Regulator_Mode.Normal_Regulator_Mode)
      ->: (((App.current_tempWstatus_MRI_In.value >= App.lower_desired_tempWstatus_MRI_In.value) &
            (App.current_tempWstatus_MRI_In.value <= App.upper_desired_tempWstatus_MRI_In.value))
            ->: (App.heat_control_MHS_Out == App.heat_control_MHS_Out_PRE))
  )
}

// ----------------- Invariant: If we end a frame in failure mode, the heater will be off --------------------

// Invariant Property:
//  - when the mode is normal (at the point where it is flowing into MHS),
//    if the current temperature is less that the low set point (at the point where values are flowing into the MRI),
//    then the heat control is On (at the point where the value is flowing out of the MHS)
//

// property stated directly in terms of relevant state variables
@strictpure def heatControlManagementFailedModeHeatOff( // antecendent
                                                        i_regulator_mode_MRM_Out: Regulator_Mode.Type,
                                                        // consequent
                                                        i_heat_control_MHS_Out: On_Off.Type
                                                    ): B = (
  // mode is normal
  ((i_regulator_mode_MRM_Out == Regulator_Mode.Failed_Regulator_Mode)
    ->: (i_heat_control_MHS_Out == On_Off.Off))
  )

// property lifted to entire state
@strictpure def invFailedModeHeatOff(
                                 // MRI
                                 p_lower_desired_tempWstatus_MRI_In: TempWstatus_impl,
                                 p_upper_desired_tempWstatus_MRI_In: TempWstatus_impl,
                                 p_current_tempWstatus_MRI_In: TempWstatus_impl,
                                 p_regulator_mode_MRI_In: Regulator_Mode.Type,
                                 p_regulator_Status_MRI_Out: Status.Type,
                                 p_displayed_temp_MRI_Out: Temp_impl,
                                 p_interface_failure_MRI_Out: Failure_Flag_impl,
                                 p_lower_desired_temp_MRI_Out: Temp_impl,
                                 p_upper_desired_temp_MRI_Out: Temp_impl,
                                 // MRM
                                 p_lastRegulatorMode: Regulator_Mode.Type,
                                 p_isFirstInvocation: B,
                                 p_interface_failure_MRM_In: Failure_Flag_impl,
                                 p_internal_failure_MRM_In: Failure_Flag_impl,
                                 p_current_tempWstatus_MRM_In: TempWstatus_impl,
                                 p_regulator_mode_MRM_Out: Regulator_Mode.Type,
                                 // MHS
                                 p_lastCmd: On_Off.Type,
                                 p_upper_desired_temp_MHS_In: Temp_impl,
                                 p_lower_desired_temp_MHS_In: Temp_impl,
                                 p_current_temp_MHS_In: TempWstatus_impl,
                                 p_regulator_mode_MHS_In: Regulator_Mode.Type,
                                 p_heat_control_MHS_Out: On_Off.Type): B = (
  // heat control management invariants
  heatControlManagementFailedModeHeatOff(
    p_regulator_mode_MRM_Out,
    p_heat_control_MHS_Out)
  )


def assertInvFailedHeatOff(): Unit = {
  // Assume the initialize phase post-condition (this characterizes the initial state for the compute phase)
  //   This step should address all port state and component local variables by
  //    - giving them unconstrained (or otherwise appropropriate initial values)
  //    - constraining the variables above according to the user supplied initPhasePostCondition
  assert(invFailedModeHeatOff(
    // MRI
    App.lower_desired_tempWstatus_MRI_In,
    App.upper_desired_tempWstatus_MRI_In,
    App.current_tempWstatus_MRI_In,
    App.regulator_mode_MRI_In,
    App.regulator_Status_MRI_Out,
    App.displayed_temp_MRI_Out,
    App.interface_failure_MRI_Out,
    App.lower_desired_temp_MRI_Out,
    App.upper_desired_temp_MRI_Out,
    // MRM
    App.lastRegulatorMode,
    App.isFirstInvocation,
    App.interface_failure_MRM_In,
    App.internal_failure_MRM_In,
    App.current_tempWstatus_MRM_In,
    App.regulator_mode_MRM_Out,
    // MHS
    App.lastCmd,
    App.upper_desired_temp_MHS_In,
    App.lower_desired_temp_MHS_In,
    App.current_temp_MHS_In,
    App.regulator_mode_MHS_In,
    App.heat_control_MHS_Out
  ))
}

def verifyInvFailedModeHeatOff() : Unit = {
  // This property is a state invariant property, not a frame preservation property, so we don't need this assumption
  // assumeInvFailureHeatOff()
  assumeContextDesiredTempInv()
  hyperperiodImmediateComm()
  assertInvFailedHeatOff()
}

def verifyInvFailedModeHeatOff_inlined() : Unit = {
  // This property is a state invariant property, not a frame preservation property, so we don't need this assumption
  // assumeInvFailureHeatOff()
  assumeContextDesiredTempInv()
  hyperperiodImmediateComm()
  assert(
    ((App.regulator_mode_MRM_Out == Regulator_Mode.Failed_Regulator_Mode)
      ->: (App.heat_control_MHS_Out == On_Off.Off))
  )
}

// ----------------- Invariant: If we end a frame where one of the temperature inputs has an invalid status,
//   the mode will be Failed (note: regardless of the starting mode)
//   Note: we might want to phrase this as predicated on starting in Init mode or Normal mode --------------

def verifyInvalidTempFailedMode_inlined() : Unit = {
  // This property is a state invariant property, not a frame preservation property, so we don't need this assumption
  // assumeInvFailureHeatOff()
  assumeContextDesiredTempInv()
  hyperperiodImmediateComm()
  assert(
    ((  (App.current_tempWstatus_MRI_In.status == ValueStatus.Invalid)
      | (App.lower_desired_tempWstatus_MRI_In.status == ValueStatus.Invalid)
      | (App.upper_desired_tempWstatus_MRI_In.status == ValueStatus.Invalid))
      ->: (App.regulator_mode_MRM_Out == Regulator_Mode.Failed_Regulator_Mode))
  )
}


//assert((App.regulator_mode_MRM_Out == Regulator_Mode.Normal_Regulator_Mode) ->: (App.current_tempWstatus_MRI_In.status == ValueStatus.Valid))
//assert((App.regulator_mode_MRM_Out == Regulator_Mode.Normal_Regulator_Mode) ->: (App.lower_desired_tempWstatus_MRI_In.status == ValueStatus.Valid))
//assert((App.regulator_mode_MRM_Out == Regulator_Mode.Normal_Regulator_Mode) ->: (App.upper_desired_tempWstatus_MRI_In.status == ValueStatus.Valid))

//  ToDo: Consider when this invariant first gets estalished.
//        In particular, consider the transition from Init mode to Normal mode.