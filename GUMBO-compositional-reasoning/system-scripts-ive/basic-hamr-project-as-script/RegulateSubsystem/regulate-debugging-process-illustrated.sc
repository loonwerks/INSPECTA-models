// #Sireum -- #Sireum indicates file is in Slang, Logika tells the IVE to apply Logika checking
//
import org.sireum._

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
        lower_desired_temp_MHS_In.value <= upper_desired_temp_MHS_In.value
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
          (heat_control_MHS_Out == On_Off.Onn),
        // case ReqMHS3
        //   Req-MHS-3
        (regulator_mode_MHS_In == Regulator_Mode.Normal_Regulator_Mode &
          current_temp_MHS_In.value > upper_desired_temp_MHS_In.value) -->:
          (heat_control_MHS_Out == On_Off.Off),
        // case ReqMHS4
        //   Req-MHS-4
        (regulator_mode_MHS_In == Regulator_Mode.Normal_Regulator_Mode &
          current_temp_MHS_In.value >= lower_desired_temp_MHS_In.value &
          current_temp_MHS_In.value <= upper_desired_temp_MHS_In.value) -->:
          (heat_control_MHS_Out == In(lastCmd)),
        // case ReqMHS5
        //   Req-MHS-5
        (regulator_mode_MHS_In == Regulator_Mode.Failed_Regulator_Mode) -->:
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
      Modifies(regulator_mode_MRM_Out),  // Debugging
      Ensures(
        // BEGIN_COMPUTE_ENSURES_timeTriggered

        // case REQMRM2
        //   REQ-MRM-2
//        (lastRegulatorMode == Regulator_Mode.Init_Regulator_Mode) -->:
//          ((!(interface_failure_MRM_In.value || internal_failure_MRM_In.value) &&
//            current_tempWstatus_MRM_In.status == ValueStatus.Valid) -->:
//            (regulator_mode_MRM_Out == Regulator_Mode.Normal_Regulator_Mode &&
//              lastRegulatorMode == Regulator_Mode.Normal_Regulator_Mode)),
        // DEBUGGING
        (lastRegulatorMode == Regulator_Mode.Normal_Regulator_Mode) -->: (regulator_mode_MRM_Out == Regulator_Mode.Normal_Regulator_Mode),
        // case REQMRMMaintainNormal
        //   REQ-MRM-Maintain-Normal
//        (lastRegulatorMode == Regulator_Mode.Normal_Regulator_Mode) -->:
//          ((!(interface_failure_MRM_In.value || internal_failure_MRM_In.value) &&
//            current_tempWstatus_MRM_In.status == ValueStatus.Valid) -->:
//            (regulator_mode_MRM_Out == Regulator_Mode.Normal_Regulator_Mode &&
//              lastRegulatorMode == Regulator_Mode.Normal_Regulator_Mode)),
        // case REQMRM3
        //   REQ-MRM-3
//        (lastRegulatorMode == Regulator_Mode.Normal_Regulator_Mode) -->:
//          ((!(!(interface_failure_MRM_In.value || internal_failure_MRM_In.value) &&
//            current_tempWstatus_MRM_In.status == ValueStatus.Valid)) -->:
//            (regulator_mode_MRM_Out == Regulator_Mode.Failed_Regulator_Mode &&
//              lastRegulatorMode == Regulator_Mode.Failed_Regulator_Mode)),
        // case REQMRM4
        //   REQ-MRM-4
//        (lastRegulatorMode == Regulator_Mode.Init_Regulator_Mode) -->:
//          ((!(!(interface_failure_MRM_In.value || internal_failure_MRM_In.value) &&
//            current_tempWstatus_MRM_In.status == ValueStatus.Valid)) -->:
//            (regulator_mode_MRM_Out == Regulator_Mode.Failed_Regulator_Mode &&
//              lastRegulatorMode == Regulator_Mode.Failed_Regulator_Mode)),
        // case REQMRMMaintainFailed
        //   REQ-MRM-Maintain-Failed
//        ((lastRegulatorMode == Regulator_Mode.Failed_Regulator_Mode) -->:
//          (regulator_mode_MRM_Out == Regulator_Mode.Failed_Regulator_Mode &&
//            lastRegulatorMode == Regulator_Mode.Failed_Regulator_Mode))
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
        // assume lower_is_not_higher_than_upper
        lower_desired_tempWstatus_MRI_In.value <= upper_desired_tempWstatus_MRI_In.value
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

  def CONTEXT_to_MRM_current_temp(): Unit = {
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
  Comm.CONTEXT_to_MRM_current_temp()
  Comm.MHS_to_CONTEXT_heat_control()
  Comm.MRI_to_MRM_interface_failure()
  Comm.MRM_to_MRI_regulator_mode()
  Comm.MRM_to_MHS_regulator_mode()
  Comm.CONTEXT_to_MRM_current_temp()
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
  Comm.CONTEXT_to_MRM_current_temp()

  assert(App.current_tempWstatus_MRI_In.value == 97)
  // --------- MRI ---------
  App.timeTriggered_MRI()
  assert(!App.interface_failure_MRI_Out.value)
  assert(!App.internal_failure_MRM_In.value)

  // schedule all outgoing communication from MRI
  Comm.MRI_to_MHS_lower_desired_temp()
  Comm.MRI_to_MHS_upper_desired_temp()
  Comm.MRI_to_MRM_interface_failure()
  Comm.MRI_to_CONTEXT_displayed_temperature()
  Comm.MRI_to_CONTEXT_regulator_status()

  assert(App.regulator_mode_MHS_In == Regulator_Mode.Normal_Regulator_Mode)
  assert(App.lastRegulatorMode == Regulator_Mode.Normal_Regulator_Mode)
  assert(App.current_tempWstatus_MRM_In == TempWstatus_impl(97,ValueStatus.Valid))
  assert(!App.interface_failure_MRM_In.value)
  assert(App.regulator_mode_MRM_Out == Regulator_Mode.Normal_Regulator_Mode)
  //
  // --------- MRM ---------
  App.timeTriggered_MRM()
  assert(App.regulator_mode_MRM_Out == Regulator_Mode.Normal_Regulator_Mode)
  // assert(App.regulator_mode_MRM_Out == Regulator_Mode.Failed_Regulator_Mode)
  // assert(App.regulator_mode_MRM_Out == Regulator_Mode.Init_Regulator_Mode)

  // schedule all outgoing communication from MRM
  Comm.MRM_to_MHS_regulator_mode()
  Comm.MRM_to_MRI_regulator_mode()

  assert(App.current_temp_MHS_In.value == 97)
  assert(App.regulator_mode_MHS_In == Regulator_Mode.Normal_Regulator_Mode)
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
      // needed when setting the regulator mode to NORMAL
      App.regulator_mode_MRM_Out,
      App.lastRegulatorMode,
      App.regulator_mode_MRI_In,
      App.regulator_mode_MHS_In,
      App.internal_failure_MRM_In,
      Context.current_tempWstatus_CONTEXT_In,
      Context.lower_desired_tempWstatus_CONTEXT_In,
      Context.upper_desired_tempWstatus_CONTEXT_In
    ),
    Ensures(
      App.regulator_mode_MRM_Out == Regulator_Mode.Normal_Regulator_Mode,
      App.lastRegulatorMode == Regulator_Mode.Normal_Regulator_Mode,
      App.regulator_mode_MRI_In == Regulator_Mode.Normal_Regulator_Mode,
      App.regulator_mode_MHS_In == Regulator_Mode.Normal_Regulator_Mode,
      App.internal_failure_MRM_In == Failure_Flag_impl(F),
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


