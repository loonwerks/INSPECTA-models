## Summary of Properties

Normal Operation

* [Heat control on] When the mode is normal,
  if the current temperature is less than the lower desired temperature,
  then the heat control shall be on.
* [Heat control off] When the mode is normal,
  if the current temperature is greater than the upper desired temperature,
  then the heat control shall be off.
* [Heat control unchanged] When the mode is normal,
  if the current temperature is greater than or equal to the lower desired temperature,
  and the current temperature is less than or equal to the lower desired temperature,
  then the heater state is unchanged.

Different notions of Failure  
* [Heat control is off in FAILED state] When the subsystem mode is FAILED,
then heater state is OFF.
* [Invalid temperature inputs lead to mode FAILED:] When either the current temperature status or lower desired temperature status, or upper desired temperature status is invalid, then subsystem will be in FAILED mode.

## Normal mode - control law - heat must be ON

**High-level statement:** 
When the mode is normal, 
if the current temperature is less than the lower desired temperature, 
then the heat control shall be on.

Assessment - How to turn the system features mentioned in natural language statement above into formal observations of the system : 
  * The current temperature and desired temperature values are values that are flowing into the subsystem from the context, and they can be observed at the subsystem boundary (i.e., the input application port state of a port that receives its value from a source outside of the subsystem).  
  * The mode is a state/value internal to the subsystem -- it can't be observed at the system boundary in our current technical set up.  Therefore we probably need to decide upon some canonical place where it is observed, e.g., at the source (point of generation), or at the sink (point of consumption).

These points of observation need to be explicit in the property statement (or at least there is some tool support for maintaining consistency of the observations).
For boundary observations, you would like those to be able to be referenced/composed with the context.  

Further assumptions: 
  - the input temperature values (current temperature, upper desired temperature) are valid sensor readings (this may not always hold, and we want to be able to handle the fault situation).
    Note that this will be related to the mode: the mode will only be normal if the temperature inputs are valid.  So we may need to decide if we want to capture the "valid temperature" notions only in terms of the mode being normal, or whether we want to make explict constraints related to "valid" temperatures.
  - the temperature readings (specifically the desired temperature values) satisfy environmental assumptions (i.e., low set point is less than high)

  Note that in our overall methodology, we may choose to monitor the environmental or boundary assumptions by run-time monitoring

**Refinement of the high-level statement to a Low-level statement:** 
  [Property clause type (snapshot -- it doesn't look at old and new values of the same observation point)]
  [Property observation point - frame boundary]

*Under environmental assumptions* (reusable across all system properties)
[If the set point temperatures are valid, then low set point is less than high]
   
*When [Mode Condition]*  
- The subsystem mode manager has determined the subsystem to be in NORMAL mode
  (in the frame post-state, as set in the output of Mode Manager component during the frame)

*Then* (intuition: the desired control law holds (observation between boundary inputs and outputs
       at stability points)
if the current temperature (in the frame post-state, as read during the frame) is
less than the lower desired temperature (in the frame post-state, as read during the frame),
then the heat control (in the frame post-state, as set during the frame)
shall be ON

**Logika Encoding**
```
 ((App.regulator_mode_MRM_Out == Regulator_Mode.Normal_Regulator_Mode) &
          (App.current_tempWstatus_MRI_In.value < App.lower_desired_tempWstatus_MRI_In.value))
            ->: (App.heat_control_MHS_Out == On_Off.Onn)
```

**Notes**

Observables: The statement of the property observes the application port state of components at a particular point in time.   The property observes multiple components (MHS, MRI). The property will not hold at all points in the system execution.  The intuition for this is that, as components work together, there are points at which their function/state are in the process of stablizing, and then there are points where their function/state has stablized in support of the overall system goal.  For the property 


We are referencing ports at the boundaries for temperature and control values, and 
internal ports that are value sources for producing values for the mode 
(App.heat_control_MHS_Out == On_Off.Onn)

Note: the above style of specification is feasible because of the way that HAMR port states and variable states work: we can always observe the values of application port states until they are reset.  Therefore, at the end of a frame AND assume that each component was only executed once, we can observe the input/output functionality of all of the component across the hyperperiod.

Verification outcomes:
  * property checks (when inlined)
  * property fails when there is a seeded bug (when inlined)

Supporting properties:
  * We might want to have a supporting property to justify our statement of the property above that says that if the mode is determined to be normal then the 
    temperature inputs at the boundary had a valid status.


Real-time Issues:
 - as stated the property would not be all that we want.  Ultimately, we need to relate inputs to outputs under some latency bound.  So at the least, we need to complement the above with a property about *when* the input values are read and *when* the control to the fan is made.
 - how often is the air temperature sampled (determined by the rate of the sensor)
 - what is the latency on the control signal


Reasoning framework
 - There should be some rules that lift the component application logic properties up to system level reasoning
 - For example, observations about the application port state of a component "lift" to the system state. Application port states are updated atomically and we are going to adopt the view that application port states are not cleared until immediately before the component is executed.  This doesn't quite work, because there will be a period of time after which a component has been dispatched (so app input ports have their values), but output application ports do not have their values yet.  It would be helpful if this corresponds to the "running" state of a component, so that we could say that one cannot make observations of the component's input/output relation when the component is in "running".  So the principle might be something like the contract relationship holds for a component's application port states at the system level as long as the component is not in running mode, and the component has executed at least one time (this is almost like "priming" the component's behavior; there will likely be a system level concept where we only are able to reasoning about some system invariants once the system as stablized).  For example, we wouldn't expect the compute entry point contract of a component to hold (even when it is not running), it it hasn't executed the compute entry point at least once. 

## Normal mode - control law - heat must be OFF

**High-level statement:** 
When the mode is normal, 
if the current temperature is greater than the upper desired temperature, 
then the heat control shall be off.

**Refinement of the high-level statement to a Low-level statement:**
  * [Property clause type (snapshot)]
  * [Property observation point - frame boundary]

*Under environmental assumptions (reusable across all system properties)*
    [If the set point temperatures are valid, then low set point is less than high]
   
*When [Mode Condition]* 
- The subsystem mode manager has determined the subsystem to be in NORMAL mode
  (in the frame post-state, as set in the output of Mode Manager component during the frame)

*Then* (intuition: the desired control law holds (observation between boundary inputs and outputs
        as viewed in the frame post-state)):
  if the current temperature (in the frame post-state, as read during the frame) is
  greater than the upper desired temperature (in the frame post-state, as read during the frame),
  then the heat control (in the frame post-state, as set during the frame)
  shall be OFF

Verification outcomes:
  * property checks (when inlined)
  * property fails when there is a seeded bug (when inlined)

**Logika Encoding**
```
  (App.regulator_mode_MRM_Out == Regulator_Mode.Normal_Regulator_Mode)
         ->: ((App.current_tempWstatus_MRI_In.value > App.upper_desired_tempWstatus_MRI_In.value)
              ->: (App.heat_control_MHS_Out == On_Off.Off))
        
```

## Normal mode - control law - heat control is UNCHANGED

**High-level statement**: When the mode is normal, 
 if the current temperature is greater than or equal to the lower desired temperature, 
 and the current temperature is less than or equal to the lower desired temperature,
 then the heater state is unchanged.

**Refinement of the high-level statement to a Low-level statement:**
 * [Property clause type (frame pre-state/post-state relation)]
 * [Property observation point - frame boundaries]

*Under environmental assumptions* (reusable across all system properties)
[If the set point temperatures are valid, then low set point is less than high]

*When [Mode Condition]*
- The subsystem mode manager has determined the subsystem to be in NORMAL mode
  (observation of output of Mode Manager component)

*Then* (intuition: the desired control law holds (observation between boundary inputs and outputs
at the frame pre-state and post-state)): 
- if the current temperature (in the frame post-state, as read during the frame) is 
      greater than or equal to the lower desired temperature (in the frame post-state, as read during the frame), 
  and less than or equal to the upper desired temperature (in the frame post-state, as read during the frame),
  then the heat control (in the frame post-state, as set during the frame)
  shall be equal to the previous heat control setting (in the frame pre-state)


**Logika Encoding**
```
  (App.regulator_mode_MRM_Out == Regulator_Mode.Normal_Regulator_Mode)
      ->: (((App.current_tempWstatus_MRI_In.value >= App.lower_desired_tempWstatus_MRI_In.value) &
            (App.current_tempWstatus_MRI_In.value <= App.upper_desired_tempWstatus_MRI_In.value))
            ->: (App.heat_control_MHS_Out == App.heat_control_MHS_Out_PRE))
```

## Failed mode - control law - heat control is OFF

**High-level statement**: When the subsystem mode is FAILED,
then heater state is OFF

**Refinement of the high-level statement to a Low-level statement:**
* [Property clause type (snapshot)]
* [Property observation point - frame boundaries]

*Under environmental assumptions* (reusable across all system properties)
(no environment assumptions are needed for this property)

*When [Mode Condition]*
- The subsystem mode manager has determined the subsystem to be in FAILED mode
  (observation of output of Mode Manager component)

*Then* (intuition: the desired control law holds (observation of heat control output in frame post-state):
the heat control (in the frame post-state, as set during the frame) shall be OFF

**Logika Encoding**
```
 ((App.regulator_mode_MRM_Out == Regulator_Mode.Failed_Regulator_Mode)
    ->: (App.heat_control_MHS_Out == On_Off.Off))
```

## Mode Transition (from INIT or NORMAL) to FAILED

**High-level statement**: When either the current temperature status
or lower desired temperature status, or upper desired temperature status is invalid,
then subsystem will be in FAILED mode

**Refinement of the high-level statement to a Low-level statement:**
* [Property clause type (snapshot)]
* [Property observation point - frame boundaries]

*Under environmental assumptions* (reusable across all system properties)
(no environment assumptions are needed for this property)

*When [Mode Condition]*
- (no mode conditioning necessary in this property)

*Then* (intuition: the desired control law holds (observation of heat control output in frame post-state):
 if the current temperature status (in frame post-state, as read during the frame) is INVALID
  or the lower desired temperature status (in frame post-state, as read during the frame) is INVALID
  or the upper desired temperature status (in frame post-state, as read during the frame) is INVALID
 then the mode shall be (observation of output of Mode Manager component in the post-state) FAILED

**Logika Encoding**
```
  ((  (App.current_tempWstatus_MRI_In.status == ValueStatus.Invalid)
    | (App.lower_desired_tempWstatus_MRI_In.status == ValueStatus.Invalid)
    | (App.upper_desired_tempWstatus_MRI_In.status == ValueStatus.Invalid))
    ->: (App.regulator_mode_MRM_Out == Regulator_Mode.Failed_Regulator_Mode))
```