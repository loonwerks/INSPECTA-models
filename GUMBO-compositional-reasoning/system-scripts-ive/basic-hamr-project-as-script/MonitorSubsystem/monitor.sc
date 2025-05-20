// #Sireum
// -- #Sireum indicates file is in Slang, Logika tells the IVE to apply Logika checking
import org.sireum._

def skip(): Unit = {
  Contract(
    Modifies()
  )
}

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

@enum object Monitor_Mode {
  "Init_Monitor_Mode"
  "Normal_Monitor_Mode"
  "Failed_Monitor_Mode"
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

@strictpure def ROUND(num: Z): Z = num

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

  var lower_alarm_tempWstatus_CONTEXT_In: TempWstatus_impl = TempWstatus_impl(z"70", ValueStatus.Valid)
  var upper_alarm_tempWstatus_CONTEXT_In: TempWstatus_impl = TempWstatus_impl(z"90", ValueStatus.Valid)

  // Operator settings (alarm upper and lower temperature)

  // ------ Outputs ------

  var alarm_control_CONTEXT_Out: On_Off.Type = On_Off.Off

  var monitor_status_CONTEXT_Out: Status.Type = Status.Init_Status

}

object App {
  //===============================================
  //          M a n a g e A l a r m
  //===============================================

  //Global Vars

  var firstInvocationFlag_MA: B = F

  var lastCmd_MA: On_Off.Type = On_Off.byOrdinal(0).get

  // ---- P o r t s

  // --- Port - upper alarm Temp ---

  // declare global variable to model port queue
  // (this can eventually be switched to a spec var)
  var upper_alarm_temp_MA_In: TempWstatus_impl = TempWstatus_impl(z"90", ValueStatus.Valid)

  // --- Port - lower alarm Temp ---

  // declare global variable to model port queue
  // (this can eventually be switched to a spec var)
  var lower_alarm_temp_MA_In: TempWstatus_impl = TempWstatus_impl(z"70", ValueStatus.Valid)

  // --- Port - current Temp ---

  // declare global variable to model port queue
  // (this can eventually be switched to a spec var)
  var current_tempWstatus_MA_In: TempWstatus_impl = TempWstatus_impl(z"85", ValueStatus.Valid)

  // --- Port - monitor mode ---

  // declare global variable to model port queue
  // (this can eventually be switched to a spec var)
  var monitor_mode_MA_In: Monitor_Mode.Type = Monitor_Mode.Init_Monitor_Mode

  // --- Port - alarm control ---

  // declare global variable to model port queue
  // (this can eventually be switched to a spec var)
  var alarm_control_MA_Out: On_Off.Type = On_Off.Off

  //-------------------------------------------------
  //  I n i t i a l i z e    E n t r y    P o i n t
  //-------------------------------------------------

  def initialise_MA(): Unit = {
    Contract(
      Modifies(lastCmd_MA),
      Ensures(
        // BEGIN INITIALIZES ENSURES
        // guarantee initlastCmd
        lastCmd_MA == On_Off.Off,
        // guarantee initheatcontrol
        alarm_control_MA_Out == On_Off.Off
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

  def compute_EP_TS_MA(): Unit = {
  }

  def compute_EP_timeTriggered_MA(): Unit = {
    Contract(
      Requires(
        (monitor_mode_MA_In == Monitor_Mode.Normal_Monitor_Mode) -->: //NO PROMISE OF ASSUMPTION WHEN FAILED
          (upper_alarm_temp_MA_In.value - lower_alarm_temp_MA_In.value >= 1 && //DIFFERENCE
        upper_alarm_temp_MA_In.value >= 99 & upper_alarm_temp_MA_In.value <= 103 && //UPPER REQUIREMENTS
        lower_alarm_temp_MA_In.value >= 93 & lower_alarm_temp_MA_In.value <= 98) //LOWER REQUIREMENTS
        // Let, mode = init, temps not valid
      ),
      Modifies(
        lastCmd_MA,
        alarm_control_MA_Out
      ),
      Ensures(
        // BEGIN_COMPUTE_ENSURES_timeTriggered
        // case REQMRM1
        //   REQ-MA-1
        (monitor_mode_MA_In == Monitor_Mode.Init_Monitor_Mode) -->: (alarm_control_MA_Out == On_Off.Off & lastCmd_MA == On_Off.Off),
        // case REQMRM2
        //   REQ-MA-2
        (monitor_mode_MA_In == Monitor_Mode.Normal_Monitor_Mode &
          (current_tempWstatus_MA_In.value < lower_alarm_temp_MA_In.value || current_tempWstatus_MA_In.value > upper_alarm_temp_MA_In.value)) -->:
          (alarm_control_MA_Out == On_Off.Onn & lastCmd_MA == On_Off.Onn),
        // case REQMRM3
        //   REQ-MA-3
        (monitor_mode_MA_In == Monitor_Mode.Normal_Monitor_Mode &
          ((current_tempWstatus_MA_In.value >= lower_alarm_temp_MA_In.value
            && current_tempWstatus_MA_In.value < lower_alarm_temp_MA_In.value + 1)
            || (current_tempWstatus_MA_In.value > upper_alarm_temp_MA_In.value - 1
            && current_tempWstatus_MA_In.value <= upper_alarm_temp_MA_In.value))) -->:
          (alarm_control_MA_Out == In(lastCmd_MA) &
            lastCmd_MA == In(lastCmd_MA)),
        // case REQMRM4
        //   REQ-MRM-4
        (monitor_mode_MA_In == Monitor_Mode.Normal_Monitor_Mode &
          current_tempWstatus_MA_In.value >= lower_alarm_temp_MA_In.value + 1 && current_tempWstatus_MA_In.value <= upper_alarm_temp_MA_In.value - 1) -->:
          (alarm_control_MA_Out == On_Off.Off & lastCmd_MA == On_Off.Off),
        // case REQMRM5
        //   REQ-MRM-5
        (monitor_mode_MA_In == Monitor_Mode.Failed_Monitor_Mode) -->:
          (alarm_control_MA_Out == On_Off.Onn & lastCmd_MA == On_Off.Onn)
        // END_COMPUTE ENSURES_timeTriggered
      )
    )
    // contract only
    halt("contract only")
  }

  //==========================================================
  //          M a n a g e M o n i t o r M o d e
  //==========================================================


  var lastMonitorMode_MMM: Monitor_Mode.Type = Monitor_Mode.Init_Monitor_Mode

  var firstInvocationFlag_MMM: B = F

  // ---- P o r t s

  // --- Port - current Temp ---

  // declare global variable to model port queue
  // (this can eventually be switched to a spec var)
  var current_tempWstatus_MMM_In: TempWstatus_impl = TempWstatus_impl(z"85", ValueStatus.Valid)

  // --- Port - monitor internal failure

  var internal_failure_MMM_In: Failure_Flag_impl = Failure_Flag_impl(F)

  // --- Port - monitor interface failure

  var interface_failure_MMM_In: Failure_Flag_impl = Failure_Flag_impl(F)

  // --- Port - monitor mode ---

  // declare global variable to model port queue
  // (this can eventually be switched to a spec var)
  var monitor_mode_MMM_Out: Monitor_Mode.Type = Monitor_Mode.Init_Monitor_Mode

  // timeout changed to var in this abstract to demonstrate the functionality of a timeout in Scenarios.
  //var hasTimeout: B = F
  var timeout_condition_satisfied_MMM: B = F


  //-------------------------------------------------
  //  I n i t i a l i z e    E n t r y    P o i n t
  //-------------------------------------------------

  def initialise_MMM(): Unit = {
    Contract(
      Modifies(
        monitor_mode_MMM_Out,
        firstInvocationFlag_MMM
      ),
      Ensures(
        // BEGIN INITIALIZES ENSURES
        // guarantee MonitorModeIsInitiallyInit
        monitor_mode_MMM_Out == Monitor_Mode.Init_Monitor_Mode,
        // guarantee firstInvocationFlagInitiallyTrue
        firstInvocationFlag_MMM == T
        // END INITIALIZES ENSURES
      )
    )
    // contract only
    halt("contract only")
  }

  //-------------------------------------------------
  //  C o m p u t e    E n t r y    P o i n t
  //-------------------------------------------------

  //@strictpure def tempBounding(t: Z): Z = if (t > 150) 150 else (if (t < -50) -50 else t)

  def compute_MMM(): Unit = {
  }

  //This is the old implementation of the compute phase for MMM
  // If you would like to know why it needed to be amended, see Scenario 7.
  // To show that this solution provides a determined result the old one
  // does not, please consult Scenario 8.
  def compute_EP_timeTriggered_MMM_Revised(): Unit = {
    Contract(
      Requires(
        //(monitor_mode_MMM_Out) == (lastMonitorMode_MMM)
      ),
      Modifies(
        monitor_mode_MMM_Out,
        lastMonitorMode_MMM,
        firstInvocationFlag_MMM
      ),
      Ensures(
        monitor_mode_MMM_Out == lastMonitorMode_MMM,
        // BEGIN_COMPUTE_ENSURES_timeTriggered
        // case REQMRM2
        //   REQ-MMM-2
        (In(monitor_mode_MMM_Out) == Monitor_Mode.Init_Monitor_Mode) -->:
          ((!(interface_failure_MMM_In.value ||
            internal_failure_MMM_In.value) &&
            current_tempWstatus_MMM_In.status == ValueStatus.Valid &&
            !timeout_condition_satisfied_MMM) ==
            (monitor_mode_MMM_Out == Monitor_Mode.Normal_Monitor_Mode)),
        // case REQMRM3
        //   REQ-MMM-3
        // Normal to Fail (REQ-MMM-3)
        (In(monitor_mode_MMM_Out) == Monitor_Mode.Normal_Monitor_Mode) -->:
          ((interface_failure_MMM_In.value ||
            internal_failure_MMM_In.value ||
            current_tempWstatus_MMM_In.status != ValueStatus.Valid) ==
            (monitor_mode_MMM_Out == Monitor_Mode.Failed_Monitor_Mode)),
        // Maintain Normal (REQ-MMM-3)
        (In(monitor_mode_MMM_Out) == Monitor_Mode.Normal_Monitor_Mode) -->:
          (!(interface_failure_MMM_In.value ||
            internal_failure_MMM_In.value ||
            current_tempWstatus_MMM_In.status != ValueStatus.Valid) ==
            (monitor_mode_MMM_Out == Monitor_Mode.Normal_Monitor_Mode)),

        // case REQMRM4
        //   REQ-MMM-4
        (In(monitor_mode_MMM_Out) == Monitor_Mode.Init_Monitor_Mode) -->:
          (timeout_condition_satisfied_MMM ==
            (monitor_mode_MMM_Out == Monitor_Mode.Failed_Monitor_Mode))
        // END_COMPUTE ENSURES_timeTriggered
      )
    )
    // contract only
    halt("contract only")
  }

  //This is the old implementation of the compute phase for MMM
  // If you would like to know why it needed to be amended, see Scenario 7.
  def compute_EP_timeTriggered_MMM_Old(): Unit = {
    Contract(
      Requires(
        //(monitor_mode_MMM_Out) == (lastMonitorMode_MMM)
      ),
      Modifies(
        monitor_mode_MMM_Out,
        lastMonitorMode_MMM,
        firstInvocationFlag_MMM
      ),
      Ensures(
        monitor_mode_MMM_Out == lastMonitorMode_MMM,
        // BEGIN_COMPUTE_ENSURES_timeTriggered
        // case REQMRM2
        //   REQ-MMM-2
        (In(monitor_mode_MMM_Out) == Monitor_Mode.Init_Monitor_Mode) -->:
          ((!(interface_failure_MMM_In.value || internal_failure_MMM_In.value) && current_tempWstatus_MMM_In.status == ValueStatus.Valid) ==
            (monitor_mode_MMM_Out == Monitor_Mode.Normal_Monitor_Mode)),
        // case REQMRM3
        //   REQ-MMM-3
        // Normal to Fail (REQ-MMM-3)
        (In(monitor_mode_MMM_Out) == Monitor_Mode.Normal_Monitor_Mode) -->:
          ((interface_failure_MMM_In.value ||
            internal_failure_MMM_In.value ||
            current_tempWstatus_MMM_In.status != ValueStatus.Valid) ==
            (monitor_mode_MMM_Out == Monitor_Mode.Failed_Monitor_Mode)),
        // Maintain Normal (REQ-MMM-3)
        (In(monitor_mode_MMM_Out) == Monitor_Mode.Normal_Monitor_Mode) -->:
          (!(interface_failure_MMM_In.value ||
            internal_failure_MMM_In.value ||
            current_tempWstatus_MMM_In.status != ValueStatus.Valid) ==
            (monitor_mode_MMM_Out == Monitor_Mode.Normal_Monitor_Mode)),

        // case REQMRM4
        //   REQ-MMM-4
        (In(monitor_mode_MMM_Out) == Monitor_Mode.Init_Monitor_Mode) -->:
          (timeout_condition_satisfied_MMM ==
            (monitor_mode_MMM_Out == Monitor_Mode.Failed_Monitor_Mode))
        // END_COMPUTE ENSURES_timeTriggered
      )
    )
    // contract only
    halt("contract only")
  }


  //==========================================================
  //          M a n a g e M o n i t o r I n t e r f a c e
  //==========================================================


  var lastMonitorMode_MMI: Monitor_Mode.Type = Monitor_Mode.Init_Monitor_Mode

  var firstInvocationFlag_MMI: B = F

  // ---- P o r t s

  // --- Port - current Temp ---

  // declare global variable to model port queue
  // (this can eventually be switched to a spec var)
  var current_tempWstatus_MMI_In: TempWstatus_impl = TempWstatus_impl(z"85", ValueStatus.Valid)

  // --- Port - monitor mode ---

  // declare global variable to model port queue
  // (this can eventually be switched to a spec var)
  var monitor_mode_MMI_In: Monitor_Mode.Type = Monitor_Mode.Init_Monitor_Mode

  // --- Port - monitor interface failure

  var interface_failure_MMI_Out: Failure_Flag_impl = Failure_Flag_impl(F)

  // --- Port - upper alarm Temp ---

  // declare global variable to model port queue
  // (this can eventually be switched to a spec var)
  var upper_alarm_temp_MMI_In: TempWstatus_impl = TempWstatus_impl(z"90", ValueStatus.Valid)

  // --- Port - lower alarm Temp ---

  // declare global variable to model port queue
  // (this can eventually be switched to a spec var)
  var lower_alarm_temp_MMI_In: TempWstatus_impl = TempWstatus_impl(z"70", ValueStatus.Valid)

  // --- Port - upper alarm Temp ---

  // declare global variable to model port queue
  // (this can eventually be switched to a spec var)
  var upper_alarm_temp_MMI_Out: TempWstatus_impl = TempWstatus_impl(z"90", ValueStatus.Valid)

  // --- Port - lower alarm Temp ---

  // declare global variable to model port queue
  // (this can eventually be switched to a spec var)
  var lower_alarm_temp_MMI_Out: TempWstatus_impl = TempWstatus_impl(z"70", ValueStatus.Valid)

  // --- Port - monitor status

  var monitor_status_MMI_Out: Status.Type = Status.Init_Status

  var timeout_condition_satisfied_MMI: B = T


  //-------------------------------------------------
  //  I n i t i a l i z e    E n t r y    P o i n t
  //-------------------------------------------------

  def initialise_MMI(): Unit = {
    Contract(
      Modifies(
        monitor_status_MMI_Out
      ),
      Ensures(
        // BEGIN INITIALIZES ENSURES
        // guarantee monitorStatusInitiallyInit
        monitor_status_MMI_Out == Status.Init_Status
        // END INITIALIZES ENSURES
      )
    )
    // contract only
    halt("contract only")
  }
  //-------------------------------------------------
  //  C o m p u t e    E n t r y    P o i n t
  //-------------------------------------------------

  //@strictpure def tempBounding(t: Z): Z = if (t > 150) 150 else (if (t < -50) -50 else t)

  def compute_MMI(): Unit = {
  }

  def compute_EP_timeTriggered_MMI(): Unit = {
    Contract(
      Requires(
        //ENVIRONMENTAL ASSUMPTION
        // UPPER AND LOWER VALS IN RANGE
      ),
      Modifies(
        monitor_status_MMI_Out,
        interface_failure_MMI_Out
      ),
      Ensures(
        // BEGIN_COMPUTE_ENSURES_timeTriggered
        // case REQMMI1
        //   REQ-MMI-1
        (monitor_mode_MMI_In == Monitor_Mode.Init_Monitor_Mode) -->: (monitor_status_MMI_Out == Status.Init_Status),
        // case REQMMI2
        //   REQ-MMI-2
        (monitor_mode_MMI_In == Monitor_Mode.Normal_Monitor_Mode) -->: (monitor_status_MMI_Out == Status.On_Status),
        // case REQMMI3
        //   REQ-MMI-3
        (monitor_mode_MMI_In == Monitor_Mode.Failed_Monitor_Mode) -->: (monitor_status_MMI_Out == Status.Failed_Status),
        // case REQMMI4
        //   REQ-MMI-4
        (lower_alarm_temp_MMI_In.status == ValueStatus.Invalid | upper_alarm_temp_MMI_In.status == ValueStatus.Invalid) -->:
          (interface_failure_MMI_Out.value),
        // case REQMMI5
        //   REQ-MMI-5
        (lower_alarm_temp_MMI_In.status == ValueStatus.Valid & upper_alarm_temp_MMI_In.status == ValueStatus.Valid) -->:
          ((interface_failure_MMI_Out == Failure_Flag_impl(F))),
        // case REQMMI6
        //   REQ-MMI-6
        (!(interface_failure_MMI_Out.value)) -->:
          (lower_alarm_temp_MMI_Out.value == lower_alarm_temp_MMI_In.value &
            upper_alarm_temp_MMI_Out.value == upper_alarm_temp_MMI_In.value),
        // case REQMMI7
        //   REQ-MMI-7
        (interface_failure_MMI_Out.value) -->: (T)
        // END_COMPUTE ENSURES_timeTriggered
      )
    )
    // contract only
    halt("contract only")
  }
}

object Comm {

  // ------- C o n t e x t   to   M M I
  def CONTEXT_to_MMI_current_tempWstatus(): Unit = {
    App.current_tempWstatus_MMI_In = Context.current_tempWstatus_CONTEXT_In
  }

  def CONTEXT_to_MMI_lower_desired_tempWstatus(): Unit = {
    App.lower_alarm_temp_MMI_In = Context.lower_alarm_tempWstatus_CONTEXT_In //TODO
  }

  def CONTEXT_to_MMI_upper_desired_tempWstatus(): Unit = {
    App.upper_alarm_temp_MMI_In = Context.upper_alarm_tempWstatus_CONTEXT_In
  }

  // ------- M M I  to C o n t e x t

  def MMI_to_CONTEXT_monitor_status(): Unit = {
    Context.monitor_status_CONTEXT_Out = App.monitor_status_MMI_Out
  }

  // ------- M M I  to   M A

  def MMI_to_MA_lower_desired_temp(): Unit = {
    App.lower_alarm_temp_MA_In = App.lower_alarm_temp_MMI_Out
  }

  def MMI_to_MA_upper_desired_temp(): Unit = {
    App.upper_alarm_temp_MA_In = App.upper_alarm_temp_MMI_Out
  }

  // ------- M M I to M M M

  def MMI_to_MMM_interface_failure(): Unit = {
    App.interface_failure_MMM_In = App.interface_failure_MMI_Out
  }

  // ------- C o n t e x t  to   M M M

  def CONTEXT_to_MMM_current_tempWstatus(): Unit = {
    App.current_tempWstatus_MMM_In = Context.current_tempWstatus_CONTEXT_In
  }

  def CONTEXT_to_MMM_monitor_failure(): Unit = {
    //TODO Detect Monitor Failure
  }

  // ------- M M M  to  M M I

  def MMM_to_MMI_monitor_mode(): Unit = {
    App.monitor_mode_MMI_In = App.monitor_mode_MMM_Out
  }

  // ------- M M M  to  M A

  def MMM_to_MA_monitor_mode(): Unit = {
    App.monitor_mode_MA_In = App.monitor_mode_MMM_Out
  }

  // ------- C o n t e x t  to   M A

  def CONTEXT_to_MA_current_tempWstatus(): Unit = {
    App.current_tempWstatus_MA_In = Context.current_tempWstatus_CONTEXT_In
  }

  // ------- M A  to   C o n t e x t

  def MA_to_CONTEXT_alarm_control(): Unit = {
    Context.alarm_control_CONTEXT_Out = App.alarm_control_MA_Out
  }

  // TODO:  Dealing with internal failure (MMM):: (Regulator has interface failure missing, this the other, probably need comm, between two.)
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
                                            // MA
                                            p_lastCmd_MA: On_Off.Type,
                                            p_current_tempWstatus_MA_In: TempWstatus_impl,
                                            p_upper_alarm_tempWstatus_MA_In: TempWstatus_impl,
                                            p_lower_alarm_tempWstatus_MA_In: TempWstatus_impl,
                                            p_monitor_mode_MA_In: Monitor_Mode.Type,
                                            p_alarm_control: On_Off.Type,
                                            // MMI
                                            p_timeout_condition_satisfied_MMI: B,
                                            p_lastMonitorMode_MMI: Monitor_Mode.Type,
                                            p_upper_alarm_tempWstatus_MMI_In: TempWstatus_impl,
                                            p_lower_alarm_tempWstatus_MMI_In: TempWstatus_impl,
                                            p_current_tempWstatus_MMI_In: TempWstatus_impl,
                                            p_monitor_mode_MMI_In: Monitor_Mode.Type,
                                            p_upper_alarm_tempWstatus_MMI_Out: TempWstatus_impl,
                                            p_lower_alarm_tempWstatus_MMI_Out: TempWstatus_impl,
                                            p_monitor_status_MMI_Out: Status.Type,
                                            p_interface_failure_MMI_Out: Failure_Flag_impl,
                                            // MMM
                                            p_timeout_condition_satisfied_MMM: B,
                                            p_lastMonitorMode_MMM: Monitor_Mode.Type,
                                            p_interface_failure_MMM_In: Failure_Flag_impl,
                                            p_current_tempWstatus_MMM_In: TempWstatus_impl,
                                            p_internal_failure_MMM_In: Failure_Flag_impl,
                                            p_monitor_mode_MMM_Out: Monitor_Mode.Type
                                          ): B = (
  //TODO FOR BELOW:: The MA MMI and MMM Compute sections might have requirements that must be checked before moving to compute.
  // Currently, there are just the ensures of the init guarantees.
  // --------------- MA -----------------
  p_lastCmd_MA == On_Off.Off &
    // p_current_tempWstatus_MA_In == ?
    // p_upper_alarm_tempWstatus_MA_In == ?
    // p_lower_alarm_tempWstatus_MA_In == ?
    // p_monitor_mode_MA_In == ?
    p_alarm_control == On_Off.Off &
    // ---------- MMI -----------
    p_timeout_condition_satisfied_MMI == F &
    p_lastMonitorMode_MMI == Monitor_Mode.Init_Monitor_Mode &
    // p_upper_alarm_tempWstatus_MMI_In == ?
    // p_lower_alarm_tempWstatus_MMI_In == ?
    // p_current_tempWstatus_MMI_In == ?
     p_monitor_mode_MMI_In == Monitor_Mode.Init_Monitor_Mode & //Added to be able to prove any Monitor Status
    // p_upper_alarm_tempWstatus_MMI_Out == ?
    // p_lower_alarm_tempWstatus_MMI_Out == ?
    p_monitor_status_MMI_Out == Status.Init_Status &
    // p_interface_failure_MMI_Out == ?
    // ------------ MMM ----------
    p_timeout_condition_satisfied_MMM == F &
    // p_interface_failure_MMM_In == ?
    // p_current_tempWstatus_MMM_In == ?
    p_internal_failure_MMM_In == Failure_Flag_impl(F) &
    p_monitor_mode_MMM_Out == Monitor_Mode.Init_Monitor_Mode &
    p_lastMonitorMode_MMM == Monitor_Mode.Init_Monitor_Mode
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
      Context.upper_alarm_tempWstatus_CONTEXT_In,
      Context.lower_alarm_tempWstatus_CONTEXT_In,
      Context.alarm_control_CONTEXT_Out,
      Context.monitor_status_CONTEXT_Out,
      // MA
      App.firstInvocationFlag_MA,
      App.lastCmd_MA,
      App.current_tempWstatus_MA_In,
      App.monitor_mode_MA_In,
      App.upper_alarm_temp_MA_In,
      App.lower_alarm_temp_MA_In,
      App.alarm_control_MA_Out,
      // MMM
      App.lastMonitorMode_MMM,
      App.firstInvocationFlag_MMM,
      App.monitor_mode_MA_In,
      App.internal_failure_MMM_In,
      App.current_tempWstatus_MMM_In,
      App.monitor_mode_MMM_Out,
      // MMI
      App.lastMonitorMode_MMI,
      App.firstInvocationFlag_MMI,
      App.monitor_mode_MMI_In,
      App.current_tempWstatus_MMI_In,
      App.interface_failure_MMI_Out,
      App.upper_alarm_temp_MMI_Out,
      App.lower_alarm_temp_MMI_Out,
      App.monitor_status_MMI_Out
    ),
    Ensures(
      // context ports
      Context.current_tempWstatus_CONTEXT_In == TempWstatus_impl(z"0",ValueStatus.Invalid),
      Context.upper_alarm_tempWstatus_CONTEXT_In == TempWstatus_impl(z"0",ValueStatus.Invalid),
      Context.lower_alarm_tempWstatus_CONTEXT_In == TempWstatus_impl(z"0",ValueStatus.Invalid),
      Context.alarm_control_CONTEXT_Out == On_Off.Off,
      Context.monitor_status_CONTEXT_Out == Status.Init_Status,
      // MA
      App.firstInvocationFlag_MA == true,
      App.lastCmd_MA == On_Off.Off,
      App.current_tempWstatus_MA_In == TempWstatus_impl(z"0",ValueStatus.Invalid),
      App.monitor_mode_MA_In == Monitor_Mode.Init_Monitor_Mode,
      App.upper_alarm_temp_MA_In == TempWstatus_impl(z"0",ValueStatus.Invalid),
      App.lower_alarm_temp_MA_In == TempWstatus_impl(z"0",ValueStatus.Invalid),
      App.alarm_control_MA_Out == On_Off.Off,
      // MMM
      App.lastMonitorMode_MMM == Monitor_Mode.Init_Monitor_Mode,
      App.firstInvocationFlag_MMM == true,
      App.monitor_mode_MA_In == Monitor_Mode.Init_Monitor_Mode,
      App.internal_failure_MMM_In == Failure_Flag_impl(F),
      App.current_tempWstatus_MMM_In == TempWstatus_impl(z"0",ValueStatus.Invalid),
      App.monitor_mode_MMM_Out == Monitor_Mode.Init_Monitor_Mode,
      // MMI
      App.lastMonitorMode_MMI == Monitor_Mode.Init_Monitor_Mode,
      App.firstInvocationFlag_MMI == true,
      App.monitor_mode_MMI_In == Monitor_Mode.Init_Monitor_Mode,
      App.current_tempWstatus_MMI_In == TempWstatus_impl(z"0",ValueStatus.Invalid),
      App.interface_failure_MMI_Out == Failure_Flag_impl(F),
      App.upper_alarm_temp_MMI_Out == TempWstatus_impl(z"0",ValueStatus.Invalid),
      App.lower_alarm_temp_MMI_Out == TempWstatus_impl(z"0",ValueStatus.Invalid),
      App.monitor_status_MMI_Out == Status.Init_Status
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
  App.initialise_MA()
  App.initialise_MMM()
  App.initialise_MMI()
  // === communication ===
  //  In general, the ordering of the communication in the initialization phase is not important
  //  since no input ports can be read.
  Comm.CONTEXT_to_MMI_lower_desired_tempWstatus()
  Comm.CONTEXT_to_MMI_upper_desired_tempWstatus()
  Comm.CONTEXT_to_MMI_current_tempWstatus()
  Comm.CONTEXT_to_MMM_current_tempWstatus()
  Comm.CONTEXT_to_MA_current_tempWstatus()
  Comm.CONTEXT_to_MMM_monitor_failure()
  Comm.MA_to_CONTEXT_alarm_control()
  Comm.MMI_to_MA_upper_desired_temp()
  Comm.MMI_to_MA_lower_desired_temp()
  Comm.MMI_to_MMM_interface_failure()
  Comm.MMI_to_CONTEXT_monitor_status()
  Comm.MMM_to_MA_monitor_mode()
  Comm.MMM_to_MMI_monitor_mode()

  // === init phase post-condition (different strategies) ===
  assert(initPhasePostConditionProp(
    //MA
    App.lastCmd_MA,
    App.current_tempWstatus_MA_In,
    App.upper_alarm_temp_MA_In,
    App.lower_alarm_temp_MA_In,
    App.monitor_mode_MA_In,
    App.alarm_control_MA_Out,
    //MMI
    App.timeout_condition_satisfied_MMI,
    App.lastMonitorMode_MMI,
    App.upper_alarm_temp_MMI_In,
    App.lower_alarm_temp_MMI_In,
    App.current_tempWstatus_MMI_In,
    App.monitor_mode_MMI_In,
    App.upper_alarm_temp_MMI_Out,
    App.lower_alarm_temp_MMI_Out,
    App.monitor_status_MMI_Out,
    App.interface_failure_MMI_Out,
    //MMM
    App.timeout_condition_satisfied_MMM,
    App.lastMonitorMode_MMM,
    App.interface_failure_MMM_In,
    App.current_tempWstatus_MMM_In,
    App.internal_failure_MMM_In,
    App.monitor_mode_MMM_Out
  )
  )
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
    //MA
    App.lastCmd_MA,
    App.current_tempWstatus_MA_In,
    App.upper_alarm_temp_MA_In,
    App.lower_alarm_temp_MA_In,
    App.monitor_mode_MA_In,
    App.alarm_control_MA_Out,
    //MMI
    App.timeout_condition_satisfied_MMI,
    App.lastMonitorMode_MMI,
    App.upper_alarm_temp_MMI_In,
    App.lower_alarm_temp_MMI_In,
    App.current_tempWstatus_MMI_In,
    App.monitor_mode_MMI_In,
    App.upper_alarm_temp_MMI_Out,
    App.lower_alarm_temp_MMI_Out,
    App.monitor_status_MMI_Out,
    App.interface_failure_MMI_Out,
    //MMM
    App.timeout_condition_satisfied_MMM,
    App.lastMonitorMode_MMM,
    App.interface_failure_MMM_In,
    App.current_tempWstatus_MMM_In,
    App.internal_failure_MMM_In,
    App.monitor_mode_MMM_Out
    //SYSTEM WIDE (CONTEXT)
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
  Comm.CONTEXT_to_MA_current_tempWstatus()
  Comm.CONTEXT_to_MMI_current_tempWstatus()
  Comm.CONTEXT_to_MMI_upper_desired_tempWstatus()
  Comm.CONTEXT_to_MMI_lower_desired_tempWstatus()
  Comm.CONTEXT_to_MMM_current_tempWstatus()
  Comm.CONTEXT_to_MMM_monitor_failure()

  // --------- MMI ---------
  App.compute_EP_timeTriggered_MMI()

  // schedule all outgoing communication from MMI
  Comm.MMI_to_CONTEXT_monitor_status()
  Comm.MMI_to_MA_upper_desired_temp()
  Comm.MMI_to_MA_lower_desired_temp()
  Comm.MMI_to_MMM_interface_failure()
  //

  // ASSERTS FOR PROOF CHECKING (POST MMI COMPUTE AND COMM)
  /*
  assert(App.lower_alarm_temp_MMI_In.value >= 93 &&
    App.lower_alarm_temp_MMI_In.value <= 98 &&
    App.upper_alarm_temp_MMI_In.value >= 99 &&
    App.upper_alarm_temp_MMI_In.value <= 103)
  assert(!(App.lower_alarm_temp_MMI_In.status == ValueStatus.Valid &&
    App.lower_alarm_temp_MMI_In.value >= 93 &&
    App.lower_alarm_temp_MMI_In.value <= 98 &&
    App.upper_alarm_temp_MMI_In.status == ValueStatus.Valid &&
    App.upper_alarm_temp_MMI_In.value >= 99 &&
    App.upper_alarm_temp_MMI_In.value <= 103) -->:
    (App.interface_failure_MMI_Out == Failure_Flag_impl(T))
  )
  */
  //

  // --------- MMM ---------
  App.compute_EP_timeTriggered_MMM_Revised()

  // schedule all outgoing communication from MMM
  Comm.MMM_to_MA_monitor_mode()
  Comm.MMM_to_MMI_monitor_mode()

  // --------- MA ----------
  App.compute_EP_timeTriggered_MA()

  // schedule all outgoing communication from MA
  Comm.MA_to_CONTEXT_alarm_control()

  //
  // ============= end unfolding ===============
}

def hyperperiodImmediateComm_Old(): Unit = {
  // ======= Unfold system execution according to the static schedule ==========

  // The communication steps below reflect all in-coming connections from the context.
  //
  // Discussion: The positioning of the communication from the context below MAY or MAY NOT be the right
  // way to model things.  In general, we are making assumptions about the scheduling of the
  // threads that are the sources for these connections as well as the ordering on the communication.
  // It is likely that those assumptions should be part of the AADL system-level specification framework
  // for compositional reasoning.
  //
  Comm.CONTEXT_to_MA_current_tempWstatus()
  Comm.CONTEXT_to_MMI_current_tempWstatus()
  Comm.CONTEXT_to_MMI_upper_desired_tempWstatus()
  Comm.CONTEXT_to_MMI_lower_desired_tempWstatus()
  Comm.CONTEXT_to_MMM_current_tempWstatus()
  Comm.CONTEXT_to_MMM_monitor_failure()

  // --------- MMI ---------
  App.compute_EP_timeTriggered_MMI()

  // schedule all outgoing communication from MMI
  Comm.MMI_to_CONTEXT_monitor_status()
  Comm.MMI_to_MA_upper_desired_temp()
  Comm.MMI_to_MA_lower_desired_temp()
  Comm.MMI_to_MMM_interface_failure()
  //

  // ASSERTS FOR PROOF CHECKING (POST MMI COMPUTE AND COMM)
  /*
  assert(App.lower_alarm_temp_MMI_In.value >= 93 &&
    App.lower_alarm_temp_MMI_In.value <= 98 &&
    App.upper_alarm_temp_MMI_In.value >= 99 &&
    App.upper_alarm_temp_MMI_In.value <= 103)
  assert(!(App.lower_alarm_temp_MMI_In.status == ValueStatus.Valid &&
    App.lower_alarm_temp_MMI_In.value >= 93 &&
    App.lower_alarm_temp_MMI_In.value <= 98 &&
    App.upper_alarm_temp_MMI_In.status == ValueStatus.Valid &&
    App.upper_alarm_temp_MMI_In.value >= 99 &&
    App.upper_alarm_temp_MMI_In.value <= 103) -->:
    (App.interface_failure_MMI_Out == Failure_Flag_impl(T))
  )
  */
  //

  // --------- MMM ---------
  App.compute_EP_timeTriggered_MMM_Old()

  // schedule all outgoing communication from MMM
  Comm.MMM_to_MA_monitor_mode()
  Comm.MMM_to_MMI_monitor_mode()

  // --------- MA ----------
  App.compute_EP_timeTriggered_MA()

  // schedule all outgoing communication from MA
  Comm.MA_to_CONTEXT_alarm_control()

  //
  // ============= end unfolding ===============
}

//===========================================================================================
//  Symbolic Test Scenarios
//
//===========================================================================================


// ----------------- Scenario 1 --------------------
// This scenario fails to verify, which it should. Provided a valid current temperature, but
// a lower alarm range that is invalid handed to the system from the context.

// Rough notes: consider the following scenario
//   - Let currentTemp be valid (98)
//   - Let the upper alarm be valid (106)
//   - Let the lower alarm be invalid (90)
//
//   then we should expect:
//   - Monitor Status to be failed
//   - Alarm Control to be On

def scenario01pre(): Unit = {
  Contract(
    Modifies(
      Context.current_tempWstatus_CONTEXT_In,
      Context.upper_alarm_tempWstatus_CONTEXT_In,
      Context.lower_alarm_tempWstatus_CONTEXT_In
    ),
    Ensures(
      Context.current_tempWstatus_CONTEXT_In == TempWstatus_impl(98,ValueStatus.Valid),
      Context.upper_alarm_tempWstatus_CONTEXT_In == TempWstatus_impl(103,ValueStatus.Valid),
      Context.lower_alarm_tempWstatus_CONTEXT_In == TempWstatus_impl(93,ValueStatus.Invalid)
    ))
  halt("Contract only")
}

def scenario01post(): Unit = {
  Contract(
    Requires(
      Context.monitor_status_CONTEXT_Out == Status.Init_Status,
      Context.alarm_control_CONTEXT_Out == On_Off.Off
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
// Moving from Init to Normal. Now verifies. At first it was failing to determine the outcome of
// Manage Monitor Mode's Monitor Mode, but this was due to a promise needing to be made about wh-
// ether or not the internal failure boolean that we are handed from detect internal failure was
// true or false. As we are not implementing the component, the init state assumings that this
// value is set to false and left alone. Under these conditions, we move to normal mode as in-
// tended.

// Scenario 2:: Init to Normal Transition (Single Hyperperiod)
//
//  Precondition:
//    Current = 98, Valid
//    Upper = 103, Valid
//    Lower = 93, Valid
//    ** Values that should transition system from init to normal in a single hyperperiod **
//
//  Process:
//    Single Hyperperiod
//
//  Postcondition:
//    Status = Init (because its one hyperperiod behind)
//    Alarm = Off
//    Monitor Mode = Normal

def scenario02pre(): Unit = {
  Contract(
    Modifies(
      Context.current_tempWstatus_CONTEXT_In,
      Context.upper_alarm_tempWstatus_CONTEXT_In,
      Context.lower_alarm_tempWstatus_CONTEXT_In
    ),
    Ensures(
      Context.current_tempWstatus_CONTEXT_In == TempWstatus_impl(98,ValueStatus.Valid),
      Context.upper_alarm_tempWstatus_CONTEXT_In == TempWstatus_impl(103,ValueStatus.Valid),
      Context.lower_alarm_tempWstatus_CONTEXT_In == TempWstatus_impl(93,ValueStatus.Valid)
    ))
  halt("Contract only")
}

def scenario02post(): Unit = {
  Contract(
    Requires(
      Context.monitor_status_CONTEXT_Out == Status.Init_Status,
      Context.alarm_control_CONTEXT_Out == On_Off.Off,
      App.monitor_mode_MMM_Out == Monitor_Mode.Normal_Monitor_Mode,
      App.monitor_mode_MMI_In == Monitor_Mode.Normal_Monitor_Mode,
      App.monitor_mode_MA_In == Monitor_Mode.Normal_Monitor_Mode
    )
  )
  halt("Contract Only")
}

def scenario02() : Unit = {
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

  scenario02pre()

  hyperperiodImmediateComm()
  //assert(App.interface_failure_MMI_Out == Failure_Flag_impl(F))
  //assert(App.interface_failure_MMM_In == Failure_Flag_impl(F))
  //assert(App.monitor_mode_MMM_Out == Monitor_Mode.Normal_Monitor_Mode)
  //assert(App.monitor_mode_MA_In == Monitor_Mode.Normal_Monitor_Mode)
  //assert(App.alarm_control_MA_Out == On_Off.Off)
  scenario02post()
}

// --- Input and Outputs for Monitor Subsystem ---
//
// INPUTS:
//
// Alarm Range
//    - Lower
//    - Upper
//
// Current Temp
//
// OUTPUTS:
//
// Monitor Status
// Alarm Control

// Let's consider the different possible states of the system in regards to its inputs and
// outputs when considering it only at periods proceeding a given hyperperiod. That is, not
// a state that exists for a time within the process of a given hyperperiod, but instead
// at the completion of hyperperiod.

// if(Current >= Lower &&
//    Current <= Upper &&
//    Upper is in range &&
//    Lower is in range &&
//

// ----------------- Scenario 3 --------------------


def scenario03pre(Current: TempWstatus_impl, Upper: TempWstatus_impl, Lower: TempWstatus_impl): Unit = {
  Contract(
    Modifies(
      Context.current_tempWstatus_CONTEXT_In,
      Context.upper_alarm_tempWstatus_CONTEXT_In,
      Context.lower_alarm_tempWstatus_CONTEXT_In
    ),
    Ensures(
      Context.current_tempWstatus_CONTEXT_In == Current,
      Context.upper_alarm_tempWstatus_CONTEXT_In == Upper,
      Context.lower_alarm_tempWstatus_CONTEXT_In == Lower,
      Lower.value >= 93, //This and below are environmental assumptions about CONTEXT
      Lower.value <= 98,
      Upper.value >= 99,
      Upper.value <= 103,
      Upper.value > Lower.value
    ))
  halt("Contract only")
}

def scenario03post(Current: TempWstatus_impl, Upper: TempWstatus_impl, Lower: TempWstatus_impl): Unit = {
  Contract(
    Requires(
      //Context.monitor_status_CONTEXT_Out == Status.On_Status,
      (Upper.status == ValueStatus.Valid &&
        Lower.status == ValueStatus.Valid &&
        Current.status == ValueStatus.Valid &&
        Current.value >= Lower.value &&
        Current.value <= Upper.value) -->:
        ((Context.alarm_control_CONTEXT_Out == On_Off.Off)),

        (App.monitor_status_MMI_Out == Status.Init_Status),

      (Upper.status == ValueStatus.Valid && Lower.status == ValueStatus.Valid) ==
        (App.interface_failure_MMI_Out == Failure_Flag_impl(F)),

      (Current.status == ValueStatus.Valid && App.interface_failure_MMI_Out == Failure_Flag_impl(F)) ==
        (App.monitor_mode_MMM_Out == Monitor_Mode.Normal_Monitor_Mode),

      (App.monitor_mode_MMM_Out == Monitor_Mode.Init_Monitor_Mode || App.monitor_mode_MMM_Out == Monitor_Mode.Normal_Monitor_Mode),

      (!(Current.status == ValueStatus.Valid && Upper.status == ValueStatus.Valid && Lower.status == ValueStatus.Valid)) ==
        (App.monitor_mode_MMM_Out == Monitor_Mode.Init_Monitor_Mode && App.alarm_control_MA_Out == On_Off.Off),

      ((Current.status == ValueStatus.Invalid) ||
        (Upper.status == ValueStatus.Invalid) ||
        (Lower.status == ValueStatus.Invalid) ||
        (Current.value >= Lower.value && Current.value <= Upper.value)) ==
        (App.alarm_control_MA_Out == On_Off.Off)



      //((Current.value > Lower.value || Current.value < Upper.value) && App.monitor_mode_MMM_Out == Monitor_Mode.Normal_Monitor_Mode) ==
        //(Context.alarm_control_CONTEXT_Out == On_Off.Onn)


    )
  )
  halt("Contract Only")
}

def test03(Current: TempWstatus_impl, Upper: TempWstatus_impl, Lower: TempWstatus_impl) : Unit = {
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
  scenario03pre(Current,Upper,Lower)
  assert(App.monitor_mode_MMI_In == Monitor_Mode.Init_Monitor_Mode)
  assert(App.lastCmd_MA == On_Off.Off)
  Context.current_tempWstatus_CONTEXT_In = Current
  Context.upper_alarm_tempWstatus_CONTEXT_In = Upper
  Context.lower_alarm_tempWstatus_CONTEXT_In = Lower
  Comm.CONTEXT_to_MA_current_tempWstatus()
  Comm.CONTEXT_to_MMI_current_tempWstatus()
  Comm.CONTEXT_to_MMI_upper_desired_tempWstatus()
  Comm.CONTEXT_to_MMI_lower_desired_tempWstatus()
  Comm.CONTEXT_to_MMM_current_tempWstatus()
  Comm.CONTEXT_to_MMM_monitor_failure()

  // --------- MMI ---------

  assert(App.timeout_condition_satisfied_MMI == F)
  App.compute_EP_timeTriggered_MMI()

  // schedule all outgoing communication from MMI
  Comm.MMI_to_CONTEXT_monitor_status()
  Comm.MMI_to_MA_upper_desired_temp()
  Comm.MMI_to_MA_lower_desired_temp()
  Comm.MMI_to_MMM_interface_failure()
  //

  // --------- MMM ---------
  App.compute_EP_timeTriggered_MMM_Revised()

  // schedule all outgoing communication from MMM
  Comm.MMM_to_MA_monitor_mode()
  Comm.MMM_to_MMI_monitor_mode()

  // --------- MA ----------
  assert(App.lastCmd_MA == On_Off.Off)
  assert(App.alarm_control_MA_Out == On_Off.Off)
  App.compute_EP_timeTriggered_MA()

  // schedule all outgoing communication from MA
  Comm.MA_to_CONTEXT_alarm_control()

  assert(!(App.internal_failure_MMM_In.value))
  assert(!(App.timeout_condition_satisfied_MMI))
  assert(App.monitor_mode_MMM_Out == Monitor_Mode.Normal_Monitor_Mode)
  scenario03post(Current,Upper,Lower)

}

def scenario03(): Unit = {
  test03(TempWstatus_impl(98,ValueStatus.Valid),TempWstatus_impl(103,ValueStatus.Valid),TempWstatus_impl(93,ValueStatus.Valid))
}

// ----------------- Scenario 4 --------------------
//

def scenario04pre(): Unit = {
  Contract(
    Modifies(
      Context.current_tempWstatus_CONTEXT_In,
      Context.upper_alarm_tempWstatus_CONTEXT_In,
      Context.lower_alarm_tempWstatus_CONTEXT_In
    ),
    Ensures(
      Context.current_tempWstatus_CONTEXT_In == TempWstatus_impl(91,ValueStatus.Valid),
      Context.upper_alarm_tempWstatus_CONTEXT_In == TempWstatus_impl(103,ValueStatus.Valid),
      Context.lower_alarm_tempWstatus_CONTEXT_In == TempWstatus_impl(93,ValueStatus.Valid)
    ))
  halt("Contract only")
}

def scenario04post(): Unit = {
  Contract(
    Requires(
      //Context.monitor_status_CONTEXT_Out == Status.On_Status,
      Context.alarm_control_CONTEXT_Out == On_Off.Onn
    )
  )
  halt("Contract Only")
}

def scenario04() : Unit = {
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

  scenario04pre()

  hyperperiodImmediateComm()

  scenario04post()
}

// ----------------- Scenario 5 --------------------
//

def scenario05pre(): Unit = {
  Contract(
    Modifies(
      Context.current_tempWstatus_CONTEXT_In,
      Context.upper_alarm_tempWstatus_CONTEXT_In,
      Context.lower_alarm_tempWstatus_CONTEXT_In
    ),
    Ensures(
      Context.current_tempWstatus_CONTEXT_In == TempWstatus_impl(104,ValueStatus.Valid),
      Context.upper_alarm_tempWstatus_CONTEXT_In == TempWstatus_impl(103,ValueStatus.Valid),
      Context.lower_alarm_tempWstatus_CONTEXT_In == TempWstatus_impl(93,ValueStatus.Valid)
    ))
  halt("Contract only")
}

def scenario05post(): Unit = {
  Contract(
    Requires(
      Context.monitor_status_CONTEXT_Out == Status.Init_Status,
      Context.alarm_control_CONTEXT_Out == On_Off.Onn
    )
  )
  halt("Contract Only")
}

def scenario05() : Unit = {
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

  scenario05pre()

  hyperperiodImmediateComm()

  scenario05post()

}

// ----------------- Scenario 6 --------------------
//

def scenario06pre(): Unit = {
  Contract(
    Modifies(
      Context.current_tempWstatus_CONTEXT_In,
      Context.upper_alarm_tempWstatus_CONTEXT_In,
      Context.lower_alarm_tempWstatus_CONTEXT_In
    ),
    Ensures(
      Context.current_tempWstatus_CONTEXT_In == TempWstatus_impl(104,ValueStatus.Valid),
      Context.upper_alarm_tempWstatus_CONTEXT_In == TempWstatus_impl(103,ValueStatus.Valid),
      Context.lower_alarm_tempWstatus_CONTEXT_In == TempWstatus_impl(93,ValueStatus.Valid)
    ))
  halt("Contract only")
}

def scenario06post(): Unit = {
  Contract(
    Requires(
      Context.monitor_status_CONTEXT_Out == Status.On_Status,
      Context.alarm_control_CONTEXT_Out == On_Off.Onn
    )
  )
  halt("Contract Only")
}

def scenario06() : Unit = {
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

  scenario06pre()
  assert(App.monitor_mode_MMI_In == Monitor_Mode.Init_Monitor_Mode)
  assert(Context.current_tempWstatus_CONTEXT_In.value > Context.upper_alarm_tempWstatus_CONTEXT_In.value)
  //assert(App.current_tempWstatus_MMI_In.value > App.upper_alarm_temp_MMI_In.value)
  //assert(Context.alarm_control_CONTEXT_Out == On_Off.Off)
  hyperperiodImmediateComm()
  hyperperiodImmediateComm()

  scenario06post()
}

// ----------------- Scenario 7 --------------------
//
// The system is able to enter a Failed state in a few different ways:
//
// 1. Current Temp is out of the range of the alarm range.
//
// 2. The status value of the Current Temp is invalid.
//
// 3. The status value the Upper or Lower Alarm Temps are invalid.
//
// 4. There is an internal failure.

// These different points of error are handled at different places in the
// subsystem. For (1), any sort of response will begin at Manage Alarm, meaning that
// (since Manage Alarm provides no information back to other parts of the system) Manage
// Monitor Mode does not reach a Failed state. This is because, while the temperature is
// still high or low, the system is operating as intended. As such, only the alarm will go off.
// In essence, just because the alarm is going off, does not mean that the system is in a Failed mode.
// However, if we are in a Failed mode, then the alarm will be going off.
//
// In terms of logic, this can be written as:
//
// (Monitor_Mode == Failed) -->: (Alarm_Control == Onn) ** SCENARIO 12 **
//
// However, there is also the matter of the initialization state where the Failed mode of
// a system can not be reached without first going to Normal if timeout has not been reached.
// A timeout will trigger Failed regardless of values. TODO Devise a invariant for this.
// It is important therefore, to demonstrate that conditions under which a Failed mode would be
// triggered in a normal operating mode.


// To Begin, we need to resolve an issue with have multiple valid outcomes of Monitor Mode in the
// event that we are in Initialization Mode, however, this is the hyperperiod at which both
// a timeout happens and we are givin valid values. To demonstrate, scenario07 will show how we
// can enter into a contradiction in this situation.


def scenario07pre(): Unit = {
  Contract(
    Modifies(
      Context.current_tempWstatus_CONTEXT_In,
      Context.upper_alarm_tempWstatus_CONTEXT_In,
      Context.lower_alarm_tempWstatus_CONTEXT_In
    ),
    Ensures(
      Context.current_tempWstatus_CONTEXT_In == TempWstatus_impl(100,ValueStatus.Valid),
      Context.upper_alarm_tempWstatus_CONTEXT_In == TempWstatus_impl(103,ValueStatus.Invalid),
      Context.lower_alarm_tempWstatus_CONTEXT_In == TempWstatus_impl(93,ValueStatus.Valid)
    ))
  halt("Contract only")
}

def scenario07initStateEnsures(): Unit = {
  Contract(
    Requires(
      Context.monitor_status_CONTEXT_Out == Status.Init_Status,
      Context.alarm_control_CONTEXT_Out == On_Off.Off,
      App.monitor_mode_MA_In == Monitor_Mode.Init_Monitor_Mode,
      App.monitor_mode_MMI_In == Monitor_Mode.Init_Monitor_Mode,
      App.monitor_mode_MMM_Out == Monitor_Mode.Init_Monitor_Mode,
      App.interface_failure_MMM_In == Failure_Flag_impl(T),
      App.interface_failure_MMI_Out == Failure_Flag_impl(T),
      App.internal_failure_MMM_In == Failure_Flag_impl(F)
    )
  )
  halt("Contract Only")
}

def causeTimeout(): Unit = {
  App.timeout_condition_satisfied_MMM = T
  App.timeout_condition_satisfied_MMI = T
}

def scenario07() : Unit = {
  // ========= inital state for system slice ==========
  // In this case, our slice starts with the beginning of the compute phase, and so we begin with the
  //  state characterized by the initialization phase post-condition
  //

  assumeInitPhasePostCondition()

  scenario07pre()
    // Let
      // Current - 100 and Valid
      // Upper - 103 and Invalid
      // Lower - 93 and Valid

  //The number values are alright for this system as they are in the acceptable range,
  // however, the Valid status of at least one end of the alarm range is invalid, so
  // this should prevent us from moving out of initial.

  //Perform a HyperPeriod
  hyperperiodImmediateComm_Old()

  scenario07initStateEnsures()
    // Monitor Status should be init
    // Alarm should be off
    // Monitor Mode should be init
    // Interface Failure is false
    // Internal Failure is false

  //Perform another Hyperperiod
  hyperperiodImmediateComm_Old()

  scenario07initStateEnsures()
    // The Following should continue to hold:
      // Monitor Status should be init
      // Alarm should be off
      // Monitor Mode should be init
      // Interface Failure is false
      // Internal Failure is false

  // Consider that both enough time has passed to timeout and we just now are handed
  // a valid upper temp.

  //Change the Upper Temp
  Context.upper_alarm_tempWstatus_CONTEXT_In = TempWstatus_impl(103,ValueStatus.Valid)
    // Current - 100 and Valid
    // Upper - 103 and Valid
    // Lower - 103 and Valid

  // These values are able to meet the requirements for the implication in REQ-MMM-2:
    /*
      (In(monitor_mode_MMM_Out) == Monitor_Mode.Init_Monitor_Mode) -->:
      ((!(interface_failure_MMM_In.value || internal_failure_MMM_In.value) &&
       current_tempWstatus_MMM_In.status == ValueStatus.Valid) ==
        (monitor_mode_MMM_Out == Monitor_Mode.Normal_Monitor_Mode))
     */

  //Now cause a timeout
  causeTimeout()

  // Now, another requirement is met at the same time. REQ-MMM-4:
    /*
      (In(monitor_mode_MMM_Out) == Monitor_Mode.Init_Monitor_Mode) -->:
        (timeout_condition_satisfied_MMM ==
          (monitor_mode_MMM_Out == Monitor_Mode.Failed_Monitor_Mode))
    */

  //Now, when we run another hyperperiod, in this situation, we are promising that
  // the Monitor Mode should be both Normal and Failed.
  // This is a contradiction.
  hyperperiodImmediateComm_Old()

  //Thus, anything is true.
  assert(T == F)
}

// Now, building off of Scenario 7, if we are able to adjust the promises made,
// then we can provide a clear outcome for the system that does not cause a
// contradiction. When there is both a timeout and a set of valid values,
// it should either fail or continue on. In this case, I have opted to allow
// the system to move to a Failed state as if the timeout has happened, then
// we should be out of time and fail regardless in order to promise a start time
// within 1 second (time measurement to be handled)
//
// This was accomplished via modifying REQ-MMM-2:
/*
    //   REQ-MMM-2
      (In(monitor_mode_MMM_Out) == Monitor_Mode.Init_Monitor_Mode) -->:
        ((!(interface_failure_MMM_In.value ||
          internal_failure_MMM_In.value) &&
          current_tempWstatus_MMM_In.status == ValueStatus.Valid &&
          !timeout_condition_satisfied_MMM) ==
        (monitor_mode_MMM_Out == Monitor_Mode.Normal_Monitor_Mode)),
*/
//
// This solves the contradiction by requiring the timeout condition having not
// been met.

def scenario08NormalStateEnsures(): Unit = {
  Contract(
    Requires(
      Context.monitor_status_CONTEXT_Out == Status.Init_Status,
      Context.alarm_control_CONTEXT_Out == On_Off.Onn,
      App.monitor_mode_MA_In == Monitor_Mode.Failed_Monitor_Mode,
      App.monitor_mode_MMI_In == Monitor_Mode.Failed_Monitor_Mode,
      App.monitor_mode_MMM_Out == Monitor_Mode.Failed_Monitor_Mode,
      App.interface_failure_MMM_In == Failure_Flag_impl(F),
      App.interface_failure_MMI_Out == Failure_Flag_impl(F),
      App.internal_failure_MMM_In == Failure_Flag_impl(F)
    )
  )
  halt("Contract Only")
}

def scenario08() : Unit = {
  // ========= inital state for system slice ==========
  // In this case, our slice starts with the beginning of the compute phase, and so we begin with the
  //  state characterized by the initialization phase post-condition
  //

  assumeInitPhasePostCondition()

  scenario07pre()
  // Let
  // Current - 100 and Valid
  // Upper - 103 and Invalid
  // Lower - 93 and Valid

  //The number values are alright for this system as they are in the acceptable range,
  // however, the Valid status of at least one end of the alarm range is invalid, so
  // this should prevent us from moving out of initial.

  //Perform a HyperPeriod
  hyperperiodImmediateComm()

  scenario07initStateEnsures()
  // Monitor Status should be init
  // Alarm should be off
  // Monitor Mode should be init
  // Interface Failure is false
  // Internal Failure is false

  //Perform another Hyperperiod
  hyperperiodImmediateComm()

  scenario07initStateEnsures()
  // The Following should continue to hold:
  // Monitor Status should be init
  // Alarm should be off
  // Monitor Mode should be init
  // Interface Failure is false
  // Internal Failure is false

  // Consider that both enough time has passed to timeout and we just now are handed
  // a valid upper temp.

  //Change the Upper Temp
  Context.upper_alarm_tempWstatus_CONTEXT_In = TempWstatus_impl(103,ValueStatus.Valid)
  // Current - 100 and Valid
  // Upper - 103 and Valid
  // Lower - 103 and Valid

  // These values are able to meet the requirements for the implication in REQ-MMM-2:
  /*
      //   REQ-MMM-2
        (In(monitor_mode_MMM_Out) == Monitor_Mode.Init_Monitor_Mode) -->:
          ((!(interface_failure_MMM_In.value ||
            internal_failure_MMM_In.value) &&
            current_tempWstatus_MMM_In.status == ValueStatus.Valid &&
            !timeout_condition_satisfied_MMM) ==
          (monitor_mode_MMM_Out == Monitor_Mode.Normal_Monitor_Mode)),
  */

  //Now cause a timeout
  causeTimeout()

  // Now, another requirement is met at the same time. REQ-MMM-4:
  /*
    (In(monitor_mode_MMM_Out) == Monitor_Mode.Init_Monitor_Mode) -->:
      (timeout_condition_satisfied_MMM ==
        (monitor_mode_MMM_Out == Monitor_Mode.Failed_Monitor_Mode))
  */
  //
  // However, REQ-MMM-2 is now no longer met, meaning there is not a contradiction
  // in this situation.

  //Now, when we run another hyperperiod, in this situation, we are promising that
  // the Monitor Mode should be both Normal.
  hyperperiodImmediateComm()

  //Thus, we are in normal mode:
  scenario08NormalStateEnsures()
}

// ----------------- Scenario 9 --------------------
//

def scenario09pre(): Unit = {
  Contract(
    Modifies(
      Context.current_tempWstatus_CONTEXT_In,
      Context.upper_alarm_tempWstatus_CONTEXT_In,
      Context.lower_alarm_tempWstatus_CONTEXT_In
    ),
    Ensures(
      Context.current_tempWstatus_CONTEXT_In == TempWstatus_impl(104,ValueStatus.Valid),
      Context.upper_alarm_tempWstatus_CONTEXT_In == TempWstatus_impl(103,ValueStatus.Invalid),
      Context.lower_alarm_tempWstatus_CONTEXT_In == TempWstatus_impl(93,ValueStatus.Valid)
    ))
  halt("Contract only")
}

def scenario09post(): Unit = {
  Contract(
    Requires(
      Context.monitor_status_CONTEXT_Out == Status.Init_Status,
      Context.alarm_control_CONTEXT_Out == On_Off.Onn
    )
  )
  halt("Contract Only")
}

def scenario09(iterationsTillTimeout:Z) : Unit = {
  // IterationsTillTimeout is a local value that we are going to use
  // to simulate that we do not know how many times the system will get
  // to run until
  assume(iterationsTillTimeout >= 0)

  assumeInitPhasePostCondition()

  scenario09pre()
  var counter: Z = 0
  while(counter < iterationsTillTimeout)
    {
      Invariant(
        counter <= iterationsTillTimeout,
        counter >= 0
      )
      hyperperiodImmediateComm()
      assert(Context.monitor_status_CONTEXT_Out == Status.Init_Status)
      assert(Context.alarm_control_CONTEXT_Out == On_Off.Off)
      counter = counter + 1
    }
  causeTimeout()
  Context.upper_alarm_tempWstatus_CONTEXT_In = TempWstatus_impl(100,ValueStatus.Valid)
  hyperperiodImmediateComm()
  assert(Context.monitor_status_CONTEXT_Out == Status.Init_Status)
  assert(Context.alarm_control_CONTEXT_Out == On_Off.Onn)
  scenario09post()
}

// ----------------- Scenario 10 --------------------
//

def scenario10pre(): Unit = {
  Contract(
    Modifies(
      Context.current_tempWstatus_CONTEXT_In,
      Context.upper_alarm_tempWstatus_CONTEXT_In,
      Context.lower_alarm_tempWstatus_CONTEXT_In
    ),
    Ensures(
      Context.current_tempWstatus_CONTEXT_In == TempWstatus_impl(104,ValueStatus.Valid),
      Context.upper_alarm_tempWstatus_CONTEXT_In == TempWstatus_impl(103,ValueStatus.Invalid),
      Context.lower_alarm_tempWstatus_CONTEXT_In == TempWstatus_impl(93,ValueStatus.Valid)
    ))
  halt("Contract only")
}

def scenario10post(): Unit = {
  Contract(
    Requires(
      Context.monitor_status_CONTEXT_Out == Status.Init_Status,
      Context.alarm_control_CONTEXT_Out == On_Off.Onn
    )
  )
  halt("Contract Only")
}

def scenario10(iterationsTillTimeout:Z) : Unit = {
  // IterationsTillTimeout is a local value that we are going to use
  // to simulate that we do not know how many times the system will get
  // to run until
  assume(iterationsTillTimeout >= 0)

  assumeInitPhasePostCondition()

  scenario10pre()
  var counter: Z = 0
  while(counter < iterationsTillTimeout)
  {
    Invariant(
      counter <= iterationsTillTimeout,
      counter >= 0
    )
    hyperperiodImmediateComm()
    assert(Context.monitor_status_CONTEXT_Out == Status.Init_Status)
    assert(Context.alarm_control_CONTEXT_Out == On_Off.Off)
    counter = counter + 1
  }
  //Like Scenario 9, but there is only a timeout and no change in state.
  causeTimeout()
  hyperperiodImmediateComm()
  assert(Context.monitor_status_CONTEXT_Out == Status.Init_Status)
  assert(Context.alarm_control_CONTEXT_Out == On_Off.Onn)
  scenario10post()
}

// ----------------- Scenario 11 --------------------
//

def scenario11pre(): Unit = {
  Contract(
    Modifies(
      Context.current_tempWstatus_CONTEXT_In,
      Context.upper_alarm_tempWstatus_CONTEXT_In,
      Context.lower_alarm_tempWstatus_CONTEXT_In
    ),
    Ensures(
      Context.current_tempWstatus_CONTEXT_In == TempWstatus_impl(104,ValueStatus.Valid),
      Context.upper_alarm_tempWstatus_CONTEXT_In == TempWstatus_impl(103,ValueStatus.Invalid),
      Context.lower_alarm_tempWstatus_CONTEXT_In == TempWstatus_impl(93,ValueStatus.Valid)
    ))
  halt("Contract only")
}

def scenario11post(): Unit = {
  Contract(
    Requires(
      Context.monitor_status_CONTEXT_Out == Status.On_Status,
      Context.alarm_control_CONTEXT_Out == On_Off.Onn
    )
  )
  halt("Contract Only")
}

def scenario11(iterationsTillTimeout:Z) : Unit = {
  // IterationsTillTimeout is a local value that we are going to use
  // to simulate that we do not know how many times the system will get
  // to run until
  assume(iterationsTillTimeout >= 0)

  assumeInitPhasePostCondition()

  scenario11pre()
  var counter: Z = 0
  while(counter < iterationsTillTimeout)
  {
    Invariant(
      counter <= iterationsTillTimeout,
      counter >= 0
    )
    hyperperiodImmediateComm()
    assert(Context.monitor_status_CONTEXT_Out == Status.Init_Status)
    assert(Context.alarm_control_CONTEXT_Out == On_Off.Off)
    counter = counter + 1
  }
  //Like scenario 9, however, the only change is that this time, we are not counting till a
  // tmeout, but till a valid answer.
  Context.upper_alarm_tempWstatus_CONTEXT_In = TempWstatus_impl(100,ValueStatus.Valid)
  hyperperiodImmediateComm()
  assert(Context.monitor_status_CONTEXT_Out == Status.Init_Status)
  assert(Context.alarm_control_CONTEXT_Out == On_Off.Onn)
  hyperperiodImmediateComm()
  assert(Context.monitor_status_CONTEXT_Out == Status.On_Status)
  assert(Context.alarm_control_CONTEXT_Out == On_Off.Onn)
  scenario11post()
}

// ----------------- Scenario 12 --------------------
//

// In this Scenario,
/*
def scenario11pre(): Unit = {
  Contract(
    Modifies(
      Context.current_tempWstatus_CONTEXT_In,
      Context.upper_alarm_tempWstatus_CONTEXT_In,
      Context.lower_alarm_tempWstatus_CONTEXT_In
    ),
    Ensures(
      Context.current_tempWstatus_CONTEXT_In == TempWstatus_impl(104,ValueStatus.Valid),
      Context.upper_alarm_tempWstatus_CONTEXT_In == TempWstatus_impl(103,ValueStatus.Invalid),
      Context.lower_alarm_tempWstatus_CONTEXT_In == TempWstatus_impl(93,ValueStatus.Valid)
    ))
  halt("Contract only")
}

def scenario11post(): Unit = {
  Contract(
    Requires(
      Context.monitor_status_CONTEXT_Out == Status.On_Status,
      Context.alarm_control_CONTEXT_Out == On_Off.Onn
    )
  )
  halt("Contract Only")
}

def scenario11(iterationsTillTimeout:Z) : Unit = {
  // IterationsTillTimeout is a local value that we are going to use
  // to simulate that we do not know how many times the system will get
  // to run until
  assume(iterationsTillTimeout >= 0)

  assumeInitPhasePostCondition()

  scenario11pre()
  var counter: Z = 0
  while(counter < iterationsTillTimeout)
  {
    Invariant(
      counter <= iterationsTillTimeout,
      counter >= 0
    )
    hyperperiodImmediateComm()
    assert(Context.monitor_status_CONTEXT_Out == Status.Init_Status)
    assert(Context.alarm_control_CONTEXT_Out == On_Off.Off)
    counter = counter + 1
  }
  //Like scenario 9, however, the only change is that this time, we are not counting till a
  // tmeout, but till a valid answer.
  Context.upper_alarm_tempWstatus_CONTEXT_In = TempWstatus_impl(100,ValueStatus.Valid)
  hyperperiodImmediateComm()
  assert(Context.monitor_status_CONTEXT_Out == Status.Init_Status)
  assert(Context.alarm_control_CONTEXT_Out == On_Off.Onn)
  hyperperiodImmediateComm()
  assert(Context.monitor_status_CONTEXT_Out == Status.On_Status)
  assert(Context.alarm_control_CONTEXT_Out == On_Off.Onn)
  scenario11post()
}
*/

//Invariant Work

// Method (All Lists, All Must be Size of Iterations Ran):
/*
  In order to begin to prove an invariant or to begin to say things about a situation where there are multiple
  iterations of the hyperperiod with variable inputs, there must be a representation of these inputs along with some Context based
  assumptions of these variables.

  INPUTS:

  Internal Failure : Failure_Flag_impl(B)

  Timeout Condition : B
    *NOTE* The timeout condition will have a special property that once a value is true, the rest in the list are true.

  Current Temp : TempWstatus_impl(ValueStatus,Z)

  Upper Temp : TempWstatus_impl(ValueStatus,Z)

  Lower Temp : TempWstatus_impl(ValueStatus,Z)
*/
/*
def InvarScen(InternalFailure:ISZ[Failure_Flag_impl], TimeoutCondition:ISZ[B], Current:ISZ[TempWstatus_impl], Upper:ISZ[TempWstatus_impl], Lower:ISZ[TempWstatus_impl]): Unit = {
  Contract(
    Requires(
      10 == InternalFailure.length == TimeoutCondition.length == Current.length == Upper.length == Lower.length
    )
  )
}
*/

