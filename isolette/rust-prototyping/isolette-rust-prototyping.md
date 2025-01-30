# Rust / Verus Prototyping for Isolette 

This directory contains a progression of Rust examples for the Isolette (primarily the Manage Heat Source (MHS) thread) that are being used to prototype HAMR code generation for Rust component application code and with associated Verus contracts and executable contracts used for testing.

The reference target for these prototypes is the Slang implementation with associated Logika contracts for the MHS thread in the files below:
- [AADL Thread Type](https://github.com/loonwerks/INSPECTA-models/blob/f74552f2b47ee9ff47cc0258888f1c7a9b4bea2f/isolette/aadl/aadl/packages/Regulate.aadl#L493-L576)
- [Slang Application Component Code (initial skeleton code)](https://github.com/loonwerks/INSPECTA-models/blob/main/isolette/hamr/slang-initial/src/main/component/isolette/Regulate/Manage_Heat_Source_i_thermostat_rt_mhs_mhs.scala)
- [Slang Application Component Code (completed)](https://github.com/loonwerks/INSPECTA-models/blob/main/isolette/hamr/slang/src/main/component/isolette/Regulate/Manage_Heat_Source_i_thermostat_rt_mhs_mhs.scala)
- [Slang port API declaration code](https://github.com/loonwerks/INSPECTA-models/blob/main/isolette/hamr/slang/src/main/bridge/isolette/Regulate/Manage_Heat_Source_i_Api.scala)
- [Slang bridge code (thread infrastructure code)](https://github.com/loonwerks/INSPECTA-models/blob/main/isolette/hamr/slang/src/main/bridge/isolette/Regulate/Manage_Heat_Source_i_thermostat_rt_mhs_mhs_Bridge.scala)
- [Slang GUMBOX executable representations of contracts](https://github.com/loonwerks/INSPECTA-models/blob/main/isolette/hamr/slang/src/main/bridge/isolette/Regulate/Manage_Heat_Source_i_thermostat_rt_mhs_mhs_GumboX.scala)


## Rust Prototyping Files
- [Manage Heat Source - Junaid (v0)](MHS-junaid.rs)

## Comments on Junaid (v0)

### Use of "old" (references to pre) in Pre-condition

Does Verus require the use of "old" to reference the pre-state, even in the pre-condition?  In Logika, and in most other contract languages, references to non-local variables is implicitly referencing the pre-state (i.e., old is not required to be explicitly stated)
```
requires
   old(self).api.lower_desired_temp <= old(self).api.upper_desired_temp
```
Next step: consult Verus documentation for this.

### Contract Specification/Interpretation of Input Ports as Read Only 

Input ports are read-only.  This means that the pre and post-states for the application port states for these should always be equal (i.e., they give rise to a frame condition).  In a contract language, this would typically be treated by annotating these variables as "non modified".   A more brute force approach is to have an explicit frame condition clause (in the post-condition) specifying that the pre-state is equal to the post-state.   

Unfortunately, even in Slang, we do not have an elegant solution for this because we cannot use Modifies directly elements of a API structure.  (So how is the clause below working because it is simply referring to the post-state regulator mode? It must be catching that regulator mode is never updated)

```
(api.regulator_mode == Isolette_Data_Model.Regulator_Mode.Init_Regulator_Mode) -->: (api.heat_control == Isolette_Data_Model.On_Off.Off),
```

The proposed Verus contracts are written as follows...
```
((old(self).api.regulator_mode == RegulatorMode::INIT)
            ==> (self.api.heat_control == HeatControl::OFF))
```
The above is semantically correct, but it refers to the post-state version of mode.

**Next step**: 
 - Figure out how to clarify this issue in the GUMBO documentation (i.e., technically there are no pre/post-state versions of input port because they are read-only)
 - Decide how we want to document/realize this in Slang
 - Investigate how to best specify frame conditions in Verus
 - Determine how we want to treat the above issue in Versus

### Implication in Verus -- is it interpreted as a "short circuit" operation

Is the `==>` of Verus interpreted as a short-circuit operation so that we can appropriately use as guards in contracts?

### Contract Specification of "Keep Current Heater setting"

Right now, the Rust code doesn't have a `currentCmd` local variable as found in the Slang code; the `last_command` (thread local state variable, contract visible) has its value set by reading from the output port variable `self.api.heat_control`.   While this "works" in the current Rust code where we simplify things by modeling the ports as global variables, we'll need to find an alternate approach that is more like the Slang code, because by principle (e.g., to enforce appropriate information flow control), *output ports are write only*.  

Related to this, consider the MHS requirement below
```
 // If the Regulator Mode is NORMAL and the Current
 //   Temperature is greater than or equal to the Lower Desired Temperature
 //   and less than or equal to the Upper Desired Temperature, the value of
 //   the Heat Control shall not be changed.
```
When needs to be enforced here is that the output heat command on the *current* dispatch is the same as the output command on the *previous* dispatch.   For this, we need contract below to refer to OLD version of last command, not the current post-state version.
So the contract concept below needs to be updated
```
((old(self).api.regulator_mode == RegulatorMode::NORMAL && 
 (old(self).api.lower_desired_temp <= old(self).api.current_temp <= old(self).api.upper_desired_temp))
     ==> (self.api.heat_control == self.last_command))
```     

Then, we need to add a contract clause to the post-condition to match the analogous Slang contract clause
```
lastCmd == api.heat_control
```
to establish that the lastCmd local state variable is properly maintained, i.e., its value in the post-state should always match the value in the api.heat_control output port.



### Port Representation

HAMR single item queue storage is represented using Slang option types, as follows..
- empty queue - option NONE
- full queue - option Some(v)

Even though the Isolette example only uses data ports (non-queued, no queueing visible to application ports), we should investigate representing the application port state using option types.

