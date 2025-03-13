#![allow(non_camel_case_types)]
#![allow(non_snake_case)]
#![allow(unused_parens)]

use crate::data::Isolette_Data_Model::*;

// logical implication
fn impliesL(p: bool, q: bool) -> bool {
    return !p | q;
}

// short cicuit implication
fn implies(p: bool, q: bool) -> bool {
    return !p || q;
}

/** Initialize Entrypoint Contract
 *
 * guarantee initlastCmd
 * @param lastCmd post-state state variable
 */
fn initialize_initlastCmd(lastCmd: On_Off) -> bool {
    lastCmd == On_Off::Off
}

/** Initialize Entrypoint Contract
 *
 * guarantee REQ_MHS_1
 *   If the Regulator Mode is INIT, the Heat Control shall be
 *   set to Off
 *   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=110
 * @param api_heat_control outgoing data port
 */
fn initialize_REQ_MHS_1(api_heat_control: On_Off) -> bool {
    api_heat_control == On_Off::Off
}

/** IEP-Guar: Initialize Entrypoint Contracts for mhs
 *
 * @param lastCmd post-state state variable
 * @param api_heat_control outgoing data port
 */
fn initialize_IEP_Guar(lastCmd: On_Off, api_heat_control: On_Off) -> bool {
    initialize_initlastCmd(lastCmd) & initialize_REQ_MHS_1(api_heat_control)
}

/** IEP-Post: Initialize Entrypoint Post-Condition
 *
 * @param lastCmd post-state state variable
 * @param api_heat_control outgoing data port
 */
pub fn inititialize_IEP_Post(lastCmd: On_Off, api_heat_control: On_Off) -> bool {
    (
        // IEP-Guar: Initialize Entrypoint contract for mhs
        initialize_IEP_Guar(lastCmd, api_heat_control)
    )
}

/** Compute Entrypoint Contract
 *
 * assumes lower_is_lower_temp
 * @param api_lower_desired_temp incoming data port
 * @param api_upper_desired_temp incoming data port
 */
fn compute_spec_lower_is_lower_temp_assume(
    api_lower_desired_temp: Temp_i,
    api_upper_desired_temp: Temp_i,
) -> bool {
    api_lower_desired_temp.degrees <= api_upper_desired_temp.degrees
}

/** CEP-T-Assm: Top-level assume contracts for mhs's compute entrypoint
 *
 * @param api_lower_desired_temp incoming data port
 * @param api_upper_desired_temp incoming data port
 */
fn compute_CEP_T_Assm(api_lower_desired_temp: Temp_i, api_upper_desired_temp: Temp_i) -> bool {
    compute_spec_lower_is_lower_temp_assume(api_lower_desired_temp, api_upper_desired_temp)
}

/** CEP-Pre: Compute Entrypoint Pre-Condition for mhs
 *
 * @param In_lastCmd pre-state state variable
 * @param api_current_tempWstatus incoming data port
 * @param api_lower_desired_temp incoming data port
 * @param api_regulator_mode incoming data port
 * @param api_upper_desired_temp incoming data port
 */
pub fn compute_CEP_Pre(
    In_lastCmd: On_Off,
    api_current_tempWstatus: TempWstatus_i,
    api_lower_desired_temp: Temp_i,
    api_regulator_mode: Regulator_Mode,
    api_upper_desired_temp: Temp_i,
) -> bool {
    (
        // CEP-Assm: assume clauses of mhs's compute entrypoint
        compute_CEP_T_Assm(api_lower_desired_temp, api_upper_desired_temp)
    )
}

/** Compute Entrypoint Contract
 *
 * guarantee lastCmd
 *   Set lastCmd to value of output Cmd port
 * @param lastCmd post-state state variable
 * @param api_heat_control outgoing data port
 */
fn compute_spec_lastCmd_guarantee(lastCmd: On_Off, api_heat_control: On_Off) -> bool {
    lastCmd == api_heat_control
}

/** CEP-T-Guar: Top-level guarantee contracts for mhs's compute entrypoint
 *
 * @param lastCmd post-state state variable
 * @param api_heat_control outgoing data port
 */
fn compute_CEP_T_Guar(lastCmd: On_Off, api_heat_control: On_Off) -> bool {
    compute_spec_lastCmd_guarantee(lastCmd, api_heat_control)
}

/** guarantee REQ_MHS_1
 *   If the Regulator Mode is INIT, the Heat Control shall be
 *   set to Off.
 *   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=110
 * @param api_regulator_mode incoming data port
 * @param api_heat_control outgoing data port
 */
fn compute_case_REQ_MHS_1(api_regulator_mode: Regulator_Mode, api_heat_control: On_Off) -> bool {
    implies(
        api_regulator_mode == Regulator_Mode::Init_Regulator_Mode,
        api_heat_control == On_Off::Off,
    )
}

/** guarantee REQ_MHS_2
 *   If the Regulator Mode is NORMAL and the Current Temperature is less than
 *   the Lower Desired Temperature, the Heat Control shall be set to On.
 *   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=110
 * @param api_current_tempWstatus incoming data port
 * @param api_lower_desired_temp incoming data port
 * @param api_regulator_mode incoming data port
 * @param api_heat_control outgoing data port
 */
fn compute_case_REQ_MHS_2(
    api_current_tempWstatus: TempWstatus_i,
    api_lower_desired_temp: Temp_i,
    api_regulator_mode: Regulator_Mode,
    api_heat_control: On_Off,
) -> bool {
    implies(
        (api_regulator_mode == Regulator_Mode::Normal_Regulator_Mode)
            & (api_current_tempWstatus.degrees < api_lower_desired_temp.degrees),
        api_heat_control == On_Off::Onn,
    )
}

/** guarantee REQ_MHS_3
 *   If the Regulator Mode is NORMAL and the Current Temperature is greater than
 *   the Upper Desired Temperature, the Heat Control shall be set to Off.
 *   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=110
 * @param api_current_tempWstatus incoming data port
 * @param api_regulator_mode incoming data port
 * @param api_upper_desired_temp incoming data port
 * @param api_heat_control outgoing data port
 */
fn compute_case_REQ_MHS_3(
    api_current_tempWstatus: TempWstatus_i,
    api_regulator_mode: Regulator_Mode,
    api_upper_desired_temp: Temp_i,
    api_heat_control: On_Off,
) -> bool {
    implies(
        (api_regulator_mode == Regulator_Mode::Normal_Regulator_Mode)
            & (api_current_tempWstatus.degrees > api_upper_desired_temp.degrees),
        api_heat_control == On_Off::Off,
    )
}

/** guarantee REQ_MHS_4
 *   If the Regulator Mode is NORMAL and the Current
 *   Temperature is greater than or equal to the Lower Desired Temperature
 *   and less than or equal to the Upper Desired Temperature, the value of
 *   the Heat Control shall not be changed.
 *   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=110
 * @param In_lastCmd pre-state state variable
 * @param api_current_tempWstatus incoming data port
 * @param api_lower_desired_temp incoming data port
 * @param api_regulator_mode incoming data port
 * @param api_upper_desired_temp incoming data port
 * @param api_heat_control outgoing data port
 */
fn compute_case_REQ_MHS_4(
    In_lastCmd: On_Off,
    api_current_tempWstatus: TempWstatus_i,
    api_lower_desired_temp: Temp_i,
    api_regulator_mode: Regulator_Mode,
    api_upper_desired_temp: Temp_i,
    api_heat_control: On_Off,
) -> bool {
    implies(
        (api_regulator_mode == Regulator_Mode::Normal_Regulator_Mode)
            & ((api_current_tempWstatus.degrees >= api_lower_desired_temp.degrees)
                & (api_current_tempWstatus.degrees <= api_upper_desired_temp.degrees)),
        api_heat_control == In_lastCmd,
    )
}

/** guarantee REQ_MHS_5
 *   If the Regulator Mode is FAILED, the Heat Control shall be
 *   set to Off.
 *   http://pub.santoslab.org/high-assurance/module-requirements/reading/FAA-DoT-Requirements-AR-08-32.pdf#page=111
 * @param api_regulator_mode incoming data port
 * @param api_heat_control outgoing data port
 */
fn compute_case_REQ_MHS_5(api_regulator_mode: Regulator_Mode, api_heat_control: On_Off) -> bool {
    implies(
        api_regulator_mode == Regulator_Mode::Failed_Regulator_Mode,
        api_heat_control == On_Off::Off,
    )
}

/** CEP-T-Case: Top-Level case contracts for mhs's compute entrypoint
 *
 * @param In_lastCmd pre-state state variable
 * @param api_current_tempWstatus incoming data port
 * @param api_lower_desired_temp incoming data port
 * @param api_regulator_mode incoming data port
 * @param api_upper_desired_temp incoming data port
 * @param api_heat_control outgoing data port
 */
fn compute_CEP_T_Case(
    In_lastCmd: On_Off,
    api_current_tempWstatus: TempWstatus_i,
    api_lower_desired_temp: Temp_i,
    api_regulator_mode: Regulator_Mode,
    api_upper_desired_temp: Temp_i,
    api_heat_control: On_Off,
) -> bool {
    compute_case_REQ_MHS_1(api_regulator_mode, api_heat_control)
        & compute_case_REQ_MHS_2(
            api_current_tempWstatus,
            api_lower_desired_temp,
            api_regulator_mode,
            api_heat_control,
        )
        & compute_case_REQ_MHS_3(
            api_current_tempWstatus,
            api_regulator_mode,
            api_upper_desired_temp,
            api_heat_control,
        )
        & compute_case_REQ_MHS_4(
            In_lastCmd,
            api_current_tempWstatus,
            api_lower_desired_temp,
            api_regulator_mode,
            api_upper_desired_temp,
            api_heat_control,
        )
        & compute_case_REQ_MHS_5(api_regulator_mode, api_heat_control)
}

/** CEP-Post: Compute Entrypoint Post-Condition for mhs
 *
 * @param In_lastCmd pre-state state variable
 * @param lastCmd post-state state variable
 * @param api_current_tempWstatus incoming data port
 * @param api_lower_desired_temp incoming data port
 * @param api_regulator_mode incoming data port
 * @param api_upper_desired_temp incoming data port
 * @param api_heat_control outgoing data port
 */
pub fn compute_CEP_Post(
    In_lastCmd: On_Off,
    lastCmd: On_Off,
    api_current_tempWstatus: TempWstatus_i,
    api_lower_desired_temp: Temp_i,
    api_regulator_mode: Regulator_Mode,
    api_upper_desired_temp: Temp_i,
    api_heat_control: On_Off,
) -> bool {
    (
        // CEP-Guar: guarantee clauses of mhs's compute entrypoint
        compute_CEP_T_Guar (lastCmd, api_heat_control) & 
  
       // CEP-T-Case: case clauses of mhs's compute entrypoint
       compute_CEP_T_Case (In_lastCmd, api_current_tempWstatus, api_lower_desired_temp, api_regulator_mode, api_upper_desired_temp, api_heat_control)
    )
}
