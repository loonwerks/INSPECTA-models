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
  with the Heat Control function.  The Heat Control function also
  saves the value of the last heat control command.  So it can be viewed
  as having two outputs (or at least, an output and a side-effect).

  Note:  In some sense, the beginning to end of frame aspect of the
    heat control function is a composition with the mode computing
    function.  If we consider the heat control function end to end
    with the start of the frame, then the heat control function
    must take into account the error conditions on inputs, and
    the even the possibility of internal error.  To me, this seems
    overly complex.   The heat control function can be viewed as a
    property that holds at the end of the frame -- except for the
    fact that we want to constrain the latency between the time of
    the sensed temperature value (and maybe even the operator inputs,
    for the set points) and the time of the actuation action.

   Note: This abstract modeling, derived from HAMR micro, does not take
    into account status of values, etc.
    Ultimately, we would likely want to have "derived requirements"
    such as "if status = INVALID on the upper desired, lower desired,
    or current temp, then heat control is OFF".  Such properties
    would be a consequence of the mode function and the heat control
    function (which is an interesting technical concept).  But we don't
    have enough precision in the model, nor the techical framework to
    support this reasoning at the moment.

  Intuitively, the properties associated with this function are:
    - Init-Off: If the calculated mode (the post-state mode) is INIT
      then the heat command is OFF
    - Normal-High: If the post-state mode is NORMAL and the current temp is
      greater than the upper desired temp, then the heat command is OFF
    - Normal-High: If the post-state mode is NORMAL and the current temp is
      lower than the lower desired temp, then the heat command is ON
    - Normal-Mid: If the post-state mode is NORMAL and the current is
      less than or equal upper desired or greater than or equal the lower
      desired, then the heat command is set to the last (previous) command
    - Failed-Off: If the calculated mode (the post-state mode) is INIT
      then the heat command is OFF

  Declared Boundary Ports (associated with the Heat Control function):
    Inputs:
      upperDesired_tempWstatus
      lowerDesired_tempWstatus
      currentTempWstatus
    Outputs:
      heat control

  Subsystem State (associated with heat control function):
    current_mode: regulator_mode (post state mode)
    lastCmd: the previous heat control command sent.

  Abstract Function Declaration
    REG_Display_Temp
      In:
        upperDesired_tempWstatus (temp portion)
        lowerDesired_tempWstatus (temp portion)
        currentTempWstatus (temp portion)
      Out:
        heat control
        lastCmd
      Reads:
        lastRegulatorMode (post state)
        lastCmd
       (also note that the mode calculation depends on
          internal error, previous mode, status values of all temperature inputs)

  GUMBO predicates supporting the function specification:
    This helper function ensures that the sent heat control value and the
    saved heat control value (lastCmd) are equivalent.

    def HeatControlSetting(heat_control_value: On_Off,
                           heat_control: On_Off,
                           last_heat_control: On_Off
                           ): B = {
           heat_control == heat_control_value
        &  last_heat_control == heat_control_value
      }


  The following subsystem specifications are derived from the architecture:
    Subsystem components: MRI, MRM, MHS
    Scheduling constraints: MRI < MRM < MHS

     Req-REG-Heat-Control-1
       If the Regulator Subsystem mode is Init (at the end of the frame)
       then the heat control is Off (at the end of the frame)
     ToDo: the "at the end of the frame" should be removed and these
      concepts should be captured by abstract function inputs and outputs,
      and then refined to concrete temporal points.

     Req-REG-Heat-Control-2
       If the Regulator Subsystem mode is Normal
       and the current_temp is less than the lower desired temp,
       then the heat control is On

     Req-REG-Heat-Control-3
       If the Regulator Subsystem mode is Normal
       and the current_temp is greater than the upper desired temp,
       then the heat control is Off

     Req-REG-Heat-Control-4
       If the Regulator Subsystem mode is Normal
       and the current_temp is less than or equal to upper desired temp,
       and the current_temp is greater than or equal to the lower desired temp,
       then the heat control is unchanged (set to the value of lastCmd)

     Req-REG-Heat-Control-5
       If the Regulator Subsystem mode is Failed (at the end of the frame)
       then the heat control is Off (at the end of the frame)

   def Req-REG-Heat-Control-1(upperDesiredTempWstatus: Integer,
                             lowerDesiredTempWstatus: Integer,
                             currentTempWstatus: Integer,
                             lastRegulatorModePost: Integer,
                             heat_command: Integer,
                             last_heat_command: Integer): B = {
          (lastRegulatorModePost == z"0") // init mode
            --> HeatControlSetting(z"0", heat_command, last_heat_command)
            }

   def Req-REG-Heat-Control-2(upperDesiredTempWstatus: Integer,
                             lowerDesiredTempWstatus: Integer,
                             currentTempWstatus: Integer,
                             lastRegulatorModePost: Integer,
                             heat_command: Integer,
                             last_heat_command: Integer): B = {
          (lastRegulatorModePost == z"1") // normal mode
           & currentTempWstatus.value < lowerDesiredTempWstatus.value
            --> HeatControlSetting(z"0", heat_command, last_heat_command) // heat off
            }
   /* ... others omitted */
   def Req-REG-Heat-Control-5(upperDesiredTempWstatus: Integer,
                             lowerDesiredTempWstatus: Integer,
                             currentTempWstatus: Integer,
                             lastRegulatorModePost: Integer,
                             heat_command: Integer,
                             last_heat_command: Integer): B = {
          (lastRegulatorModePost == z"2") // failed mode
            --> HeatControlSetting(z"0", heat_command, last_heat_command)
   }

   def Req-REG-Heat-Control(upperDesiredTempWstatus: Integer,
                           lowerDesiredTempWstatus: Integer,
                           currentTempWstatus: Integer,
                           lastRegulatorModePost: Integer,
                           heat_command: Integer,
                           last_heat_command: Integer): B = {
           Req-REG-Heat-Control-1(...)
       /\  Req-REG-Heat-Control-2(...)
       /\  Req-REG-Heat-Control-3(...)
       /\  Req-REG-Heat-Control-4(...)
       /\  Req-REG-Heat-Control-5(...))



  Contract for abstract function:
  REG_Heat_Control
      In:
       upper_desired_tempWstatus
       lower_desired_tempWstatus
       // conceptual problem: currentTempWstatus is not available to read in the pre-state
       // It is like there are multiple pre-states (or at least multiple states where the input becomes available
       currentTempWstatus
      Out:
        heat_control
      Reads:
        lastRegulatorMode (**post-state, which may be updated as this function is computing)
        lastCmd
      Writes:
        lastCmd
    Contract(
      Requires(lower_desired_tempWstatus.value <= upper_desired_tempWstatus.value),
      Modifies(heat_control,lastCmd),
      Ensures(Req-REG-Heat-Control(upperDesiredTempWstatus,
                           lowerDesiredTempWstatus,
                           currentTempWstatus,
                           lastRegulatorMode,
                           In(lastCmd),  // lastCmdPre - Note: must connect with logged value
                           heat_control,
                           lastCmd)      // lastCmdPost
    )

    //===============================================================
    //  M o d e     T r a n s i t i o n  (Next Mode)
    //===============================================================

  Declared Boundary Ports (associated with the Next Mode function):
    Inputs:
      upperDesired_tempWstatus (status value)
      lowerDesired_tempWstatus (status value)
      currentTempWstatus (status value)
    Read (local state)
      regulator_mode
    Outputs
      (none) (there are only internal ports, and internal state that are outputs)
    Write (local state)
      regulator_mode

  Abstract Function Declaration
    REG_Next_Mode
      In:
        upperDesired_tempWstatus (status value)
        lowerDesired_tempWstatus (status value)
        currentTempWstatus (status value)
      Reads:
        lastRegulatorMode (pre state)
        internal error
      Write (local state)
        lastRegulatorMode (post state)


  Contract for abstract function:
  REG_Heat_Control
      In:
       upper_desired_tempWstatus
       lower_desired_tempWstatus
       // conceptual problem: currentTempWstatus is not available to read in the pre-state
       // It is like there are multiple pre-states (or at least multiple states where the input becomes available
       currentTempWstatus
      Out:
        heat_control
      Reads:
        lastRegulatorMode (**post-state, which may be updated as this function is computing)
        lastCmd
      Writes:
        lastCmd
    Contract(
      Requires(lower_desired_tempWstatus.value <= upper_desired_tempWstatus.value),
      Modifies(heat_control,lastCmd),
      Ensures(Req-REG-Heat-Control(upperDesiredTempWstatus,
                           lowerDesiredTempWstatus,
                           currentTempWstatus,
                           lastRegulatorMode,
                           In(lastCmd),  // lastCmdPre - Note: must connect with logged value
                           heat_control,
                           lastCmd)      // lastCmdPost
    )

  ===============
  Refinement
  ===============

  The pre and post states of the abstract function above must be associated with
  state elements (ports or thread local variables) of the refining subsystem at
  specified temporal points.


subsystem REG
  with functions Display_Temp, Regulator_Status, Heat_Control;

   // REG.begin is an abstract temporal point that is associated with
   // a single identifiable position in the schedule
   REG.begin
     log
       // save/log the inputs necessary for our reasoning.  Unless we assume that an
       // automatic gateway component "freezes" the input state to the subsystem is implemented,
       // we must indicate which input port (or local variable) of the subsystem components that
       // we log from
       //
       // **NOTE**: The declarations for the logged values below
       // were the inputs to the Display Temp function,
       // but they don't apply here, because we depend on the current temp value and mode values
       // being used by MHS.
       // It is likely that during the composition of the functions, the potential inconsistencies
       // between the different mode values and current temp values will be exposed.
       //
       // current_tempWstatus[<-MRI.current_tempWstatus], // subsystems inputs with multiple destinations need to be disambiguated
       // last_regulator_mode[<-MRM.last_regulator_mode];
       upper_desired_tempWstatus[<- MRI.upper_desired_tempWstatus];
       lower_desired_tempWstatus[<- MRI.lower_desired_tempWstatus];
       lastCmd[<- MHS.lastCmd]; // the PRE state value of lastCmd which is used in MHS for Req-4
       // We can't log current_temp here NOR mode, because they're values are not guaranteed
       // to arrive (current_temp) or not computed (mode) until part way through
       // the schedule.  What are the implications of this for composition??
     assumes
       // Note: Our HAMR micro model doesn't include the notion of value status, so we need to
       // ignore the antecedent in the clause below for now.
       (lower_desired_tempWstatus.status == Valid & upper_desired_TempWstatus.status == Valid)
        --> (lower_desired_tempWstatus.value <= upper_desired_tempWstatus.value))
     with Heat_Control
      assumes
      // ToDo: DO I need all of these?? Some of these are just cut and paste from DISPLAY Temp
      // Reply: It seems like assertions like this are necessary to establish that there are no
      //  new/updated inputs between REG.begin and MRI.begin.  Is this necessary, or should we
      //  just state the abstract function in terms of MRI.begin for upper/lower_desired inputs?
      //  This probably comes into place when we try to compose the Operator_Interface function
      //  with the Heat_Control function (e.g., at least the operator interface should ensure
      //  that lower <= upper.  We also want to prove that the values read in from the environment
      //  are propagated unchanged to the REG functions (which begin with MRI).
      //  ToDo: It is not clear what MRI.upper_desired_tempWstatus means below.
      //   What we need ultimate is a relationship between the inputs to the abstract function
      //   and the concrete inputs to the MRI component.
      //   Is it the value of upper_desired_tempWstatus when the MRI begins to execute?
      //   or is it the value at Reg.begin?
      (the following might be considered to be implicitly defined due to the definition of logging)
      In any case, these constraints seem to represent a refinement relationship between the
      logged values that the abstract function processes and the concrete values actually flowing
      through the system.
        log.REG.begin.upper_desired_tempWstatus == MRI.upper_desired_tempWstatus
      & log.REG.begin.lower_desired_tempWstatus == MRI.lower_desired_tempWstatus
      & log.REG.begin.lastCmd == MHS.lastCmd  // specify relationship between logged value and state (which will eventually become input (pre-state) to MHS)
      (this could be considered to be the primary constraint declared - it is the pre-condition
      of the abstract function)
      & (log.REG.begin.lower_desired_tempWstatus <= log.REG.begin.upper_desired_tempWstatus)

    MRI
      // Note: in the full model, we will also have to consider "valid"/"invalid" conditions
      // on the upper/lower desired temp values.
      end.assert [Heat_Command]
      // constraints arising from the output of the current component (MRI)
      // The three below could be theorectically be derived from the MRI app contract
           log.REG.begin.lower_desired_tempWstatus.value == MRI.lower_desired_temp (output)
        &  log.REG.begin.upper_desired_tempWstatus.value == MRI.upper_desired_temp
        & (MRI.lower_desired_temp <= MRI.upper_desired_temp)
       // This constraint can be propagated formed from the pre-state (via the frame condition
       //  on MRI (if schedule constraints show that MRM cannot be schedule before MRI executes)
        & log.REG.begin.lastCmd == MHS.lastCmd


  [Note: that after the initial component that uses the logged input from communication,
    it seems that the constraint between the log value and the communication channel
    doesn't need to be kept around any more.
    However, we typically do need to keep the constraint between the log value and the
    component local state (to ensure that when we start the component, the pre-state
    value of the local state matches the logged state.]

    MRM
     end.log
      lastRegulatorMode[<- MRM.lastRegulatorMode];
      assert [Heat_Command]
           // This is a situation where a system assertion constraints the output and input of other
           // component that were not immediately executed previously.
           // These constraints could be automatically propagated forward due to the fact that
           // MRM's frame condition indicates that the upper/lower desired temp values are not
           // modified.
           //  i.e., no component writes to ZZZ_desired_temp comm channels in the interim (again),
           //  this could be established by considering the scheduling constraints.
           log.REG.begin.lower_desired_tempWstatus.value == lower_desired_temp
        &  log.REG.begin.upper_desired_tempWstatus.value == upper_desired_temp
        & log.REG.begin.lastCmd == MHS.lastCmd
        & (lower_desired_temp <= upper_desired_temp)
           // logging/refinement constraint (snapshot of local variable)
        & log.lastRegulatorMode == MRM.lastRegulatorMode
           // constraint on current state of communication substrate after execution of MRM
           //  (relationship between output and saved value of output)
        & log.lastRegulatorMode == Comm.regulator_mode

   MHS
    begin.log
       current_tempWstatus[<- MHS.current_tempWstatus];
    // log.end??? Should we log heat_control and lastCmd here??
    assert [Heat_Command]
       Req-REG-Heat-Control(log.Reg.begin.upperDesiredTempWstatus,
                            log.Reg.begin.lowerDesiredTempWstatus,
                            log.MHS.begin.currentTempWstatus,
                            log.MRM.end.lastRegulatorMode,
                            log.Reg.begin.lastCmd,
                            heat_control,
                            lastCmd)

   REG.End
     log
       heat_control[<-MHS.heat_control],
       heat_control[<-MHS.lastCmd]
     guarantee [Heat_Command]
       Req-REG-Heat-Control(log.Reg.begin.upperDesiredTempWstatus,
                            log.Reg.begin.lowerDesiredTempWstatus,
                            log.MHS.begin.currentTempWstatus,
                            log.MRM.end.lastRegulatorMode,
                            log.Reg.begin.lastCmd,
                            log.Reg.end.heat_control,
                            log.Reg.end.lastCmd)

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

// specifies that the state holding the heater commands are equal
@strictpure def HeatControlSetting(
                  heat_control_value: Integer, heat_control: Integer, lastCmd: Integer): B =
  (heat_control == heat_control_value & lastCmd == heat_control_value)

// Req-REG-Heat-Control-1
// If the Regulator Subsystem mode is Init (at the end of the frame)
//  then the heat control is Off (at the end of the frame)
@strictpure def Req_REG_Heat_Control_1(
                                        upperDesiredTempWstatus: Integer,
                                        lowerDesiredTempWstatus: Integer,
                                        currentTempWstatus: Integer,
                                        lastRegulatorModePost: Integer,
                                        heat_command: Integer,
                                        lastCmdPre: Integer,
                                        lastCmdPost: Integer): B = (
  (lastRegulatorModePost == z"0") // init mode
  ->: HeatControlSetting(z"0", heat_command, lastCmdPost) // heat Off
  )

// Req-REG-Heat-Control-2
// If the Regulator Subsystem mode is Normal
// and the current_temp is less than the lower desired temp,
// then the heat control is On
@strictpure def Req_REG_Heat_Control_2(
                                        upperDesiredTempWstatus: Integer,
                                        lowerDesiredTempWstatus: Integer,
                                        currentTempWstatus: Integer,
                                        lastRegulatorModePost: Integer,
                                        heat_command: Integer,
                                        lastCmdPre: Integer,
                                        lastCmdPost: Integer): B = (
  ((lastRegulatorModePost == z"1") // normal
    & (currentTempWstatus < lowerDesiredTempWstatus)) // current < lower
    ->: HeatControlSetting(z"1", heat_command, lastCmdPost) // heat On
  )

// Req-REG-Heat-Control-3
//  If the Regulator Subsystem mode is Normal
//  and the current_temp is greater than the upper desired temp,
//  then the heat control is Off
@strictpure def Req_REG_Heat_Control_3(
                                        upperDesiredTempWstatus: Integer,
                                        lowerDesiredTempWstatus: Integer,
                                        currentTempWstatus: Integer,
                                        lastRegulatorModePost: Integer,
                                        heat_command: Integer,
                                        lastCmdPre: Integer,
                                        lastCmdPost: Integer): B = (
  ((lastRegulatorModePost == z"1") // normal
    & (upperDesiredTempWstatus < currentTempWstatus)) // upper < current
    ->: HeatControlSetting(z"0", heat_command, lastCmdPost) // heat Off
  )

// Req-REG-Heat-Control-4
//   If the Regulator Subsystem mode is Normal
//   and the current_temp is less than or equal to upper desired temp,
//   and the current_temp is greater than or equal to the lower desired temp,
//   then the heat control is unchanged (set to the value of lastCmd)
@strictpure def Req_REG_Heat_Control_4(
                                        upperDesiredTempWstatus: Integer,
                                        lowerDesiredTempWstatus: Integer,
                                        currentTempWstatus: Integer,
                                        lastRegulatorModePost: Integer,
                                        heat_command: Integer,
                                        lastCmdPre: Integer,
                                        lastCmdPost: Integer): B = (
  ((lastRegulatorModePost == z"1") // normal
    & (lowerDesiredTempWstatus <= currentTempWstatus)  // lower <= current
    & (currentTempWstatus <= upperDesiredTempWstatus)) // current <= upper
    ->: HeatControlSetting(lastCmdPre, heat_command, lastCmdPost) // heat is set to lastCmd
  )

// Req-REG-Heat-Control-5
// If the Regulator Subsystem mode is Failed (at the end of the frame)
//  then the heat control is Off (at the end of the frame)
@strictpure def Req_REG_Heat_Control_5(
                                        upperDesiredTempWstatus: Integer,
                                        lowerDesiredTempWstatus: Integer,
                                        currentTempWstatus: Integer,
                                        lastRegulatorModePost: Integer,
                                        heat_command: Integer,
                                        lastCmdPre: Integer,
                                        lastCmdPost: Integer): B = (
  (lastRegulatorModePost == z"2") // failed mode
    ->: HeatControlSetting(z"0", heat_command, lastCmdPost) // heat Off
  )


//  - The specification for the required behavior of the DisplayTemp
//    subsystem function is the conjunction of the two requirements above.
@strictpure def Req_REG_Heat_Control(upperDesiredTempWstatus: Integer,
                                     lowerDesiredTempWstatus: Integer,
                                     currentTempWstatus: Integer,
                                     lastRegulatorModePost: Integer,
                                     heat_command: Integer,
                                     lastCmdPre: Integer,         // ToDo: Reorder these for a more logical progression
                                     lastCmdPost: Integer): B =
   (Req_REG_Heat_Control_1(upperDesiredTempWstatus, lowerDesiredTempWstatus, currentTempWstatus, lastRegulatorModePost, heat_command, lastCmdPre, lastCmdPost)
  & Req_REG_Heat_Control_2(upperDesiredTempWstatus, lowerDesiredTempWstatus, currentTempWstatus, lastRegulatorModePost, heat_command, lastCmdPre, lastCmdPost)
  & Req_REG_Heat_Control_3(upperDesiredTempWstatus, lowerDesiredTempWstatus, currentTempWstatus, lastRegulatorModePost, heat_command, lastCmdPre, lastCmdPost)
  & Req_REG_Heat_Control_4(upperDesiredTempWstatus, lowerDesiredTempWstatus, currentTempWstatus, lastRegulatorModePost, heat_command, lastCmdPre, lastCmdPost)
  & Req_REG_Heat_Control_5(upperDesiredTempWstatus, lowerDesiredTempWstatus, currentTempWstatus, lastRegulatorModePost, heat_command, lastCmdPre, lastCmdPost))


//-------------------------
// debugging -- examples
//-------------------------

def foo(): Unit = {
  // heat on when temp is too low
  assert(Req_REG_Heat_Control(
    upperDesiredTempWstatus = z"103",
    lowerDesiredTempWstatus = z"98",
    currentTempWstatus = z"96", // current > upper
    lastRegulatorModePost = z"1", // normal
    heat_command = z"1", // heat off
    lastCmdPre = z"0",
    lastCmdPost = z"1"))

  // heat off when temp is too high
  assert(Req_REG_Heat_Control(
    upperDesiredTempWstatus = z"103",
    lowerDesiredTempWstatus = z"98",
    currentTempWstatus = z"104", // current > upper
    lastRegulatorModePost = z"1",
    heat_command = z"0", // heat off
    lastCmdPre = z"0",
    lastCmdPost = z"0"))

  // heat maintained when temp is medium
  assert(Req_REG_Heat_Control(
    upperDesiredTempWstatus = z"103",
    lowerDesiredTempWstatus = z"98",
    currentTempWstatus = z"99", // medium
    lastRegulatorModePost = z"1",
    heat_command = z"1", // heat maintained
    lastCmdPre = z"1",
    lastCmdPost = z"1"))

  //-------------------------
  // debugging -- failures
  //-------------------------


  // violations of property
  assert(!Req_REG_Heat_Control(
    upperDesiredTempWstatus = z"103",
    lowerDesiredTempWstatus = z"98",
    currentTempWstatus = z"96",
    lastRegulatorModePost = z"1",
    heat_command = z"0", // error, should be On
    lastCmdPre = z"0",
    lastCmdPost = z"0"))

  // heat not maintained when temp is medium
  assert(!Req_REG_Heat_Control(
    upperDesiredTempWstatus = z"103",
    lowerDesiredTempWstatus = z"98",
    currentTempWstatus = z"99", // in between
    lastRegulatorModePost = z"1",
    heat_command = z"1", // heat on
    lastCmdPre = z"0", // previous heat off -- so heat command is not maintained
    lastCmdPost = z"1"))

}

//=================================================================
//  N e x t    M o d e    Requirements (formalized as strict pure functions)
//=================================================================

// Helper function to compute the combined status of Upper Desired and Lower Desired temps

@strictpure def REG_Desired_Temps_Valid(
  upperDesiredTempWstatus: Integer,
  lowerDesiredTempWstatus: Integer): B = (
   (upperDesiredTempWstatus == 1 & lowerDesiredTempWstatus == 1)
  )

// Helper function to compute the combined status of All temps
@strictpure def REG_All_Temps_Valid(
       upperDesiredTempWstatus: Integer,
       lowerDesiredTempWstatus: Integer,
       currentTempWstatus: Integer): B = (
  REG_Desired_Temps_Valid(upperDesiredTempWstatus,lowerDesiredTempWstatus) & currentTempWstatus == 1
  )


//==================================================================
//  L o g g i n g
//
//==================================================================

object Log {

  // Define logged state values (Owicki Greis "auxiliary variables") that provide snapshots
  // of state associated with abstract function

  var upper_desired_tempWstatus: Integer = z"0"
  /* Default value */
  var lower_desired_tempWstatus: Integer = z"0"
  /* Default value */
  var current_tempWstatus: Integer = z"0"
  /* Default value */
  var lastRegulatorMode: Integer = z"0"
  var heat_command: Integer = z"0"
  var lastCmd: Integer = z"0"


  //-------------------------------------------------
  //  Log Heat Control abstract function pre-state
  //    - logs state of specified channels and thread local variables
  //      at temporal point REG_Heat_Control_Pre (or alternatively REG_begin
  //       -- start of schedule for the subsystem)
  //
  //       upper_desired_tempWstatus[<- MRI.upper_desired_tempWstatus]; (infrastructure port for MRI)
  //       lower_desired_tempWstatus[<- MRI.lower_desired_tempWstatus]; (infrastructure port for MRI)
  //       lastCmd[<- MHS.lastCmd];  (last command local variable for MHS)
  //-------------------------------------------------

  def Reg_Heat_Control_PreState(): Unit = {
    Contract(
      Requires(),
      Modifies(
        Log.upper_desired_tempWstatus,
        Log.lower_desired_tempWstatus,
        Log.lastCmd
      ),
      Ensures(
        Log.upper_desired_tempWstatus == Comm.upper_desired_tempWstatus,
        Log.lower_desired_tempWstatus == Comm.lower_desired_tempWstatus,
        Log.lastCmd == App.lastCmd)
    )
  }

  def Reg_Heat_Control_MRM_PostState(): Unit = {
    Contract(
      Requires(),
      Modifies(
        Log.lastRegulatorMode
      ),
      Ensures(
        Log.lastRegulatorMode == App.lastRegulatorMode
    ))
  }

  def Reg_Heat_Control_MHS_PreState(): Unit = {
    Contract(
      Requires(),
      Modifies(
        Log.current_tempWstatus
      ),
      Ensures(
        Log.current_tempWstatus == Comm.current_tempWstatus
      ))
  }

  def Reg_Heat_Control_PostState(): Unit = {
    Contract(
      Requires(),
      Modifies(
        Log.heat_command,
        Log.lastCmd
      ),
      Ensures(
        Log.heat_command == Comm.heat_control,
        Log.lastCmd == App.lastCmd)
    )
  }

  // MRM
  //  end.log
  //   lastRegulatorMode[<- MRM.lastRegulatorMode];

  // MHS
  //  begin.log
  //    heat_control[<- Comm.heat_control];
  //    lastCmd[<- MHS.lastCmd];
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
        // case ReqMHS1
        //   Req-MHS-1
        ((Comm.regulator_mode == z"0") ->: // init mode
          (Comm.heat_control == z"0")),  // heat off
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
          (Comm.heat_control == In(lastCmd))),
        // case ReqMHS5
        //   Req-MHS-5
        ((Comm.regulator_mode == z"2") ->: // failed mode
          (Comm.heat_control == z"0"))  // heat off
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
//
//  All contract declared state associated with functions allocated to the subsystem
//  are visible to this property.
//===================================================================


@strictpure def sysAssert_REG_begin(upperDesiredTempWstatus: Integer, // input
                                    lowerDesiredTempWstatus: Integer, // input
                                    currentTempWstatus: Integer, // input
                                    regulator_mode: Integer, // input (internal channel)
                                    lastRegulatorMode: Integer, // MRM local state
                                    heat_command: Integer, // output
                                    lastCmd: Integer // MHS local state
                                   ): B = (
  // -- Environment Constraints -- (constraints on values flowing through ports on subsystem boundary)
  // The Operator Interface (in the enclosing context) needs to establish this constraint
  // before control flows to REG subsystem
    (lowerDesiredTempWstatus <= upperDesiredTempWstatus)
  // -- Subsystem Invariants -- (constraints on spec-declared state and port variables)
  & (regulator_mode == lastRegulatorMode)
  // --... relationship between subsystem output port and local state
  & (heat_command == lastCmd)
  )

// Heat_Control_REG_pre is a property that should hold true at the
// beginning of the scheduling of components of the components implementing
// the heat control function.
// It represents the precondition for the abstract function ??and would be
// a refinement of an abstract precondition??
//
// The precondition can reference all the state visible from the abstract function
// and its refined *input* state.  This likely includes logged versions of state
// at the time the abstract function begins its execution.

@strictpure def Fun_Heat_Control_REG_pre(
                                                 Log_upperDesiredTempWstatus: Integer,
                                                 Log_lowerDesiredTempWstatus: Integer
                                                 ): B = (
  // The Operator Interface (in the enclosing context) needs to establish this constraint
  // before control flows to REG subsystem
  (Log_lowerDesiredTempWstatus <= Log_upperDesiredTempWstatus)
  )

// ToDo:
//   Clarify the conceptual purpose of this constraint.
//   Is it the "post-condition" for the logging action???
@strictpure def Fun_Heat_Control_REG_pre_LoggingRefinement(
                                          Log_upperDesiredTempWstatus: Integer,
                                          Log_lowerDesiredTempWstatus: Integer,
                                          Log_lastCmd: Integer,
                                          MRI_In_upperDesiredTempWstatus: Integer,
                                          MRI_In_lowerDesiredTempWstatus: Integer,
                                          MHS_lastCmd_pre: Integer
                                        ): B = (
  // The Operator Interface (in the enclosing context) needs to establish this constraint
  // before control flows to REG subsystem
     (Log_upperDesiredTempWstatus == MRI_In_upperDesiredTempWstatus
    & Log_lowerDesiredTempWstatus == MRI_In_lowerDesiredTempWstatus
    & Log_lastCmd == MHS_lastCmd_pre
    )
  )

 // System Assert - MRI End
 //   These two clauses are important because they establish the relationship between
 //   the logged values (inputs to the abstract function) and outputs of the MRI component
 //   -- capturing the intermediate abstract-input to concrete-outputs "progress" of the function
 //   up to this point.

 //   Note: in the full model, we will also have to consider "valid"/"invalid" conditions
 //   on the upper/lower desired temp values.
 //   end.assert [Heat_Command]
 //
 //
 //   In the full framework, there would be additional properties capturing the relationship
 //   between the infrastrastructure port state and application port state.
 //
 //     log.REG.begin.lower_desired_tempWstatus.value == MRI.lower_desired_temp
 //  &  log.REG.begin.upper_desired_tempWstatus.value == MRI.upper_desired_temp
 //  & (MRI.lower_desired_temp <= MRI.upper_desired_temp)
 //


@strictpure def sysAssert_Heat_Control_MRI_End(Log_upper_desired_tempWstatus: Integer,
                                               Log_lower_desired_tempWstatus: Integer,
                                               Log_lastCmd: Integer,
                                               upper_desired_temp_MRI_Out: Integer,
                                               lower_desired_temp_MRI_Out: Integer,
                                               MHS_lastCmd_pre:Integer): B = (
      // Relate logged values captured earlier in the computation to current component outputs
       Log_upper_desired_tempWstatus == upper_desired_temp_MRI_Out
    &  Log_lower_desired_tempWstatus == lower_desired_temp_MRI_Out
    &  Log_lastCmd == MHS_lastCmd_pre
    &  (lower_desired_temp_MRI_Out <= upper_desired_temp_MRI_Out)
  )

// System Assert - MRM End
//     end.log
//      lastRegulatorMode[<- MRM.lastRegulatorMode];
//      assert [Heat_Command]
//           log.REG.begin.lower_desired_tempWstatus.value == lower_desired_temp
//        &  log.REG.begin.upper_desired_tempWstatus.value == upper_desired_temp
//        & (lower_desired_temp <= upper_desired_temp)
//        & log.lastRegulatorMode == regulator_mode

@strictpure def sysAssert_Heat_Control_MRM_End(Log_upper_desired_tempWstatus: Integer,
                                               Log_lower_desired_tempWstatus: Integer,
                                               Log_lastCmd: Integer,
                                               upper_desired_temp_MRI_Out: Integer,
                                               lower_desired_temp_MRI_Out: Integer,
                                               App_lastCmd: Integer,
                                               Log_lastRegulatorMode: Integer,
                                               App_lastRegulatorMode: Integer,
                                               regulatorMode: Integer
): B = (
    // Relate logged values captured earlier in the computation to current component outputs
       Log_upper_desired_tempWstatus == upper_desired_temp_MRI_Out
    &  Log_lower_desired_tempWstatus == lower_desired_temp_MRI_Out
    &  Log_lastCmd == App_lastCmd
    // constraint on the current state of the communication substrate
    &  (lower_desired_temp_MRI_Out <= upper_desired_temp_MRI_Out
    // logging capture of component local variable
    & Log_lastRegulatorMode == App_lastRegulatorMode
    // Output of component matches stored mode
    & App_lastRegulatorMode == regulatorMode)
)

//  System Assert - MHS End
//    begin.log
//       current_tempWstatus[<- MHS.current_tempWstatus];
//    // log.end??? Should we log heat_control and lastCmd here??
//    assert [Heat_Command]
//       Req-REG-Heat-Control(log.Reg.begin.upperDesiredTempWstatus,
//                            log.Reg.begin.lowerDesiredTempWstatus,
//                            log.MHS.begin.currentTempWstatus,
//                            log.MRM.end.lastRegulatorMode,
//                            heat_control,
//                            lastCmd)

@strictpure def sysAssert_Heat_Control_MHS_End(Log_upper_desired_tempWstatus: Integer,
                                               Log_lower_desired_tempWstatus: Integer,
                                               Log_lastRegulatorMode: Integer,
                                               Log_current_tempWstatus: Integer,
                                               Log_lastCmdPre: Integer,
                                               heat_control: Integer,
                                               lastCmd: Integer
                                              ): B = (
  // Relate logged values captured earlier in the computation to current component outputs
  Req_REG_Heat_Control(Log_upper_desired_tempWstatus,Log_lower_desired_tempWstatus,
                       Log_current_tempWstatus,Log_lastRegulatorMode,
                       heat_control,Log_lastCmdPre,lastCmd)
  )

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
      App.lastRegulatorMode,
      Comm.heat_control,
      App.lastCmd)
  )
}

// ------- Heat_Control abstract function precondition --------
//  ASSUME
def assume_Fun_Heat_Control_REG_pre(): Unit = {
  assume(
    Fun_Heat_Control_REG_pre(
      Log.upper_desired_tempWstatus, // input
      Log.lower_desired_tempWstatus // input
    ))
}

// ASSERT
def assert_Fun_Heat_Control_REG_pre(): Unit = {
  assert(Fun_Heat_Control_REG_pre(
    Log.upper_desired_tempWstatus, // input
    Log.lower_desired_tempWstatus // input
  ))
}

// -------- Fun_Heat_Control_REG_pre_LoggingRefinement --------
//  ASSUME
def assume_Fun_Heat_Control_REG_pre_LoggingRefinement(): Unit = {
  assume(
    Fun_Heat_Control_REG_pre_LoggingRefinement(
      Log.upper_desired_tempWstatus, // input
      Log.lower_desired_tempWstatus, // input
      Log.lastCmd,
      Comm.upper_desired_tempWstatus,
      Comm.lower_desired_tempWstatus,
      App.lastCmd
    ))
}

//  ASSERT
def assert_Fun_Heat_Control_REG_pre_LoggingRefinement(): Unit = {
  assert(
    Fun_Heat_Control_REG_pre_LoggingRefinement(
      Log.upper_desired_tempWstatus, // input
      Log.lower_desired_tempWstatus, // input
      Log.lastCmd,
      Comm.upper_desired_tempWstatus,
      Comm.lower_desired_tempWstatus,
      App.lastCmd
    ))
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
def assume_sysAssert_Heat_Control_MRI_End(): Unit = {
  assume(sysAssert_Heat_Control_MRI_End(
     Log.upper_desired_tempWstatus,
     Log.lower_desired_tempWstatus,
     Log.lastCmd,
     Comm.upper_desired_temp,
     Comm.lower_desired_temp,
     App.lastCmd
  ))
}

//  ASSERT
def assert_sysAssert_Heat_Control_MRI_End(): Unit = {
  assert(sysAssert_Heat_Control_MRI_End(
    Log.upper_desired_tempWstatus,
    Log.lower_desired_tempWstatus,
    Log.lastCmd,
    Comm.upper_desired_temp,
    Comm.lower_desired_temp,
    App.lastCmd)
  )
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
def assume_sysAssert_Heat_Control_MRM_End(): Unit = {
  assume(sysAssert_Heat_Control_MRM_End(
    Log.upper_desired_tempWstatus,
    Log.lower_desired_tempWstatus,
    Log.lastCmd,
    Comm.upper_desired_temp,
    Comm.lower_desired_temp,
    App.lastCmd,
    Log.lastRegulatorMode,
    App.lastRegulatorMode,
    Comm.regulator_mode)
  )
}

//  ASSERT
def assert_sysAssert_Heat_Control_MRM_End(): Unit = {
  assert(sysAssert_Heat_Control_MRM_End(
    Log.upper_desired_tempWstatus,
    Log.lower_desired_tempWstatus,
    Log.lastCmd,
    Comm.upper_desired_temp,
    Comm.lower_desired_temp,
    App.lastCmd,
    Log.lastRegulatorMode,
    App.lastRegulatorMode,
    Comm.regulator_mode
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
def assume_sysAssert_Heat_Control_MHS_End(): Unit = {
  assume(sysAssert_Heat_Control_MHS_End(
    Log.upper_desired_tempWstatus,
    Log.lower_desired_tempWstatus,
    Log.lastRegulatorMode,
    Log.current_tempWstatus,
    Log.lastCmd,                // ToDo: re-order these parameters
    Comm.heat_control,
    App.lastCmd)
  )
}

//  ASSERT

def assert_sysAssert_Heat_Control_MHS_End(): Unit = {
  assert(sysAssert_Heat_Control_MHS_End(
    Log.upper_desired_tempWstatus,
    Log.lower_desired_tempWstatus,
    Log.lastRegulatorMode,
    Log.current_tempWstatus,
    Log.lastCmd,     // beginning of frame
    Comm.heat_control,
    App.lastCmd)
  )
  assert(Req_REG_Heat_Control_1(
    Log.upper_desired_tempWstatus,
    Log.lower_desired_tempWstatus,
    Log.current_tempWstatus,
    Log.lastRegulatorMode,
    Comm.heat_control,
    Log.lastCmd,
    App.lastCmd))
  assert(Req_REG_Heat_Control_2(
    Log.upper_desired_tempWstatus,
    Log.lower_desired_tempWstatus,
    Log.current_tempWstatus,
    Log.lastRegulatorMode,
    Comm.heat_control,
    Log.lastCmd,
    App.lastCmd))
  assert(Req_REG_Heat_Control_3(
    Log.upper_desired_tempWstatus,
    Log.lower_desired_tempWstatus,
    Log.current_tempWstatus,
    Log.lastRegulatorMode,
    Comm.heat_control,
    Log.lastCmd,
    App.lastCmd))
  assert(Req_REG_Heat_Control_4(
    Log.upper_desired_tempWstatus,
    Log.lower_desired_tempWstatus,
    Log.current_tempWstatus,
    Log.lastRegulatorMode,
    Comm.heat_control,
    Log.lastCmd,
    App.lastCmd))
  assert(Req_REG_Heat_Control_5(
    Log.upper_desired_tempWstatus,
    Log.lower_desired_tempWstatus,
    Log.current_tempWstatus,
    Log.lastRegulatorMode,
    Comm.heat_control,
    Log.lastCmd,
    App.lastCmd))
}

//----------------------------------
//  Heat Control Pre VC
//----------------------------------

// Shows that the facts available at the start of the REG subsystem in the
// static schedule are sufficient to establish the pre-condition for the
// Heat Control function

// This also reflects aspects of the logging, abstractly done at the
// beginning of the scheduling of components that realize Heat Control function
//

def REG_Display_Temp_PreVC() : Unit = {
  // assumptions about values at the REG subsystem interface and its schedule boundary
  assume_sysAssert_REG_begin()
  // implement declared logging to capture Heat_Control pre-state
  //  I believe that this also is implicitly capturing a state refinement, with
  //  constraints that relate the concrete state of the implementation to the state processed by the
  //  Heat_Control abstract function
  Log.Reg_Heat_Control_PreState()  // execute logging actions for Heat Control Pre-state
  // assert that the logging/refinement relationship holds
  assert_Fun_Heat_Control_REG_pre_LoggingRefinement()
  // assert that the precondition for the abstract function holds
  assert_Fun_Heat_Control_REG_pre()
}


def REG_Display_Temp_MRI_PreVC() : Unit = {
  assume_sysAssert_REG_begin()
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
  // constraints, that we assume the subsystem "begin" assertion REG_begin.
  assume_sysAssert_REG_begin()
  // include Logging/Refinement constraints (to relate MRI concrete input port values to logged values)
  // We have already proved that this follows from the REG_begin and the logging action
  assume_Fun_Heat_Control_REG_pre_LoggingRefinement()
  // we have proved that this follows from the REG pre and logging action - so not sure this
  //  is strictly needed here
  assume_Fun_Heat_Control_REG_pre()
  // execute the MRI application contract
  App.timeTriggered_MRI()
  // assert Heat_Control properties for End of MRI
  assert_sysAssert_Heat_Control_MRI_End()
}

// MRM_ExecVC
//
//

//----------------------------------
//  MRM VCs
//----------------------------------

// Generate a PreVC for MRM for every component that could possibly precede MRM
// in the schedule.  In this case, there is only one such component (MRI)
def REG_Heat_Control_MRM_PreVC_MRI() : Unit = {
  assume_sysAssert_Heat_Control_MRI_End()
  assert_MRM_Pre()
}

// Generate an ExecVC for MRM for every component that could possibly precede MRM
// in the schedule.  In this case, there is only one such component (MRI)
def REG_Display_Temp_MRM_ExecVC() : Unit = {
  // Assume the _End system assertion for the component that precedes it
  assume_sysAssert_Heat_Control_MRI_End()
  App.timeTriggered_MRM()
  Log.Reg_Heat_Control_MRM_PostState()
  assert_sysAssert_Heat_Control_MRM_End()
}

//----------------------------------
//  MHS VCs
//----------------------------------

// Generate a PreVC for MRM for every component that could possibly precede MRM
// in the schedule.  In this case, there is only one such component (MRI)
def REG_Heat_Control_MHS_PreVC_MRM() : Unit = {
  assume_sysAssert_Heat_Control_MRM_End()
  assert_MHS_Pre()
}

// Generate an ExecVC for MRM for every component that could possibly precede MHS
// in the schedule.  In this case, there is only one such component (MRM)
def REG_Display_Temp_MHS_ExecVC() : Unit = {
  // Assume the _End system assertion for the component that precedes it
  assume_sysAssert_Heat_Control_MRM_End()
  // Need log against for current Temp here
  Log.Reg_Heat_Control_MHS_PreState()
  App.timeTriggered_MHS()
  // Log.Reg_Heat_Control_MRM_Post()
  assert_sysAssert_Heat_Control_MHS_End()
}



