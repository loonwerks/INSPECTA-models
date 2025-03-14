// #Sireum

package isolette.Regulate

import org.sireum._
import isolette._
import isolette.util.Container

// Do not edit this file as it will be overwritten if HAMR codegen is rerun

// containers for the pre and post state values of ports and state variables

@sig trait Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container extends Container {
}

// container for incoming ports
@datatype class Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_P (
  ) extends Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container

// container for incoming ports and state variables
@datatype class Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container_PS (
  ) extends Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PreState_Container

@sig trait Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container extends Container {
  def api_internal_failure: Isolette_Data_Model.Failure_Flag_i
}

// container for outgoing ports
@datatype class Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_P (
  val api_internal_failure: Isolette_Data_Model.Failure_Flag_i) extends Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container

// container for outgoing ports and state variables
@datatype class Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container_PS (
  val api_internal_failure: Isolette_Data_Model.Failure_Flag_i) extends Detect_Regulator_Failure_i_thermostat_rt_drf_drf_PostState_Container
