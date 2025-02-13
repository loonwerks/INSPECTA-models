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
- [Manage Heat Source - Junaid (v0)](https://github.com/loonwerks/INSPECTA-models/blob/df78cfb4690c5d84159fe3aa2355d2a14abb520f/isolette/rust-prototyping/MHS-junaid.rs)
- [Manage Heat Source - Junaid (v1)](MHS-junaid.rs)
   - Verus contract updated for `time_triggered(..)`:
      - When `regulator_mode` is `NORMAL` and `lower_desired_temp <= current_temp <= upper_desired_temp`, `heat_control == old(last_command)`
      - `last_command == api.heat_control`

## Comments on Junaid (v0)

### Use of "old" (references to pre) in Pre-condition

Does Verus require the use of "old" to reference the pre-state, even in the pre-condition?  In Logika, and in most other contract languages, references to non-local variables is implicitly referencing the pre-state (i.e., old is not required to be explicitly stated)
```
requires
   old(self).api.lower_desired_temp <= old(self).api.upper_desired_temp
```
Next step: consult Verus documentation for this.

Example from Verus documentation referring to the use of old for initial and final values of variables in functions: (https://verus-lang.github.io/verus/guide/induction.html#towards-an-imperative-implementation-mutation-and-tail-recursion)

Compiling the above Verus contract without old produces the following error:
```
$ verus src/manage_heat_source.rs 
error: in requires, use `old(self)` to refer to the pre-state of an &mut variable
   --> src/manage_heat_source.rs:132:9
    |
132 |         self.api.lower_desired_temp <= self.api.upper_desired_temp
    |         ^^^^
```
As indicated in the error, "self" is a mutable variable in this function and thus requires the use of old(self) in the pre-condition.

This is confirmed by compiling the following:
```
fn foo(&self)
requires
   self.api.lower_desired_temp <= self.api.upper_desired_temp
   ,    
{ }
```
No compile errors. Please note that "self" is not mutable in this function.

Can old be used within requires clauses for non-mutable variables? Compiling with the requires clause above modified with old.
```
$ verus src/manage_heat_source.rs 
error: a mutable reference is expected here
   --> src/manage_heat_source.rs:130:9
    |
130 |     old(self).api.lower_desired_temp <= old(self).api.upper_desired_temp
    |         ^^^^
```
Apparently not.

Junaid's observations:
- Use old with mutable variables in Verus contracts for referring to initial and final states of variables in functions.
- Do no use old with non-mutable variables.

### Contract Specification/Interpretation of Input Ports as Read Only 

Input ports are read-only.  This means that the pre and post-states for the application port states for these should always be equal (i.e., they give rise to a frame condition).  In a contract language, this would typically be treated by annotating these variables as "non modified".   A more brute force approach is to have an explicit frame condition clause (in the post-condition) specifying that the pre-state is equal to the post-state.   

Unfortunately, even in Slang, we do not have an elegant solution for this because we cannot use Modifies directly elements of a API structure.  (So how is the clause below working because it is simply referring to the post-state regulator mode? It must be catching that regulator mode is never updated)

```
(api.regulator_mode == Isolette_Data_Model.Regulator_Mode.Init_Regulator_Mode) -->: (api.heat_control == Isolette_Data_Model.On_Off.Off),
```
The above is semantically correct, but it refers to the post-state version of mode.

The proposed Verus contracts are written as follows...
```
((old(self).api.regulator_mode == RegulatorMode::INIT)
            ==> (self.api.heat_control == HeatControl::OFF))
```

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

## Comments on SHA 2025-02-06

The following is a discussion of some bits of the ghost/spec variable strategy for abstracting the state of component APIs.

### Input Ports / Get APIs

Consider the component API for a data input port
```
// Logika spec var representing port state for incoming data port
  @spec var current_tempWstatus: Isolette_Data_Model.TempWstatus_impl = $

  def get_current_tempWstatus() : Option[Isolette_Data_Model.TempWstatus_impl] = {
    Contract(
      Ensures(
        Res == Some(current_tempWstatus)
      )
    )
    val value : Option[Isolette_Data_Model.TempWstatus_impl] = Art.getValue(current_tempWstatus_Id) match {
      case Some(Isolette_Data_Model.TempWstatus_impl_Payload(v)) => Some(v)
      case Some(v) =>
        Art.logError(id, s"Unexpected payload on port current_tempWstatus.  Expecting 'Isolette_Data_Model.TempWstatus_impl_Payload' but received ${v}")
        None[Isolette_Data_Model.TempWstatus_impl]()
      case _ => None[Isolette_Data_Model.TempWstatus_impl]()
    }
    return value
  }
```

In this we see that the `@spec` variable `current_tempWstatus` is introduced to abstract the value in the "application input port" (following the terminology introduced in the HAMR semantics).  

In the definition of the developer-facing API method `get_current_tempWstatus` for fetching the input port value into the user code:
- the executable code in the body of the function ensures that value returned is retrieved from the ART middleware (communication infrastructure)
- the contract specification ensures that the value returned for deductive reasoning is the ghost variable and any of its associated contraints

**Note**: The implementations of input port API methods such as the `get_current_tempWStatus` are *not* verified by Logika
to conform to the contract (i.e., the equality of the ghost variable to the return value in the post-condition).  Logika is not run on the API file.  Instead, the obligation that the actual return value is abstracted by the ghost variable value is discharged by the compositional reasoning aspects of framework.

The strategy above is key to achieving compositional reasoning for the component application code: verification of the application code does not depend on the actual value(s) being retrieved from the middleware via the input port.  Instead it depends on the abstraction of the value provided by the ghost variable and any assumptions (constraints) about it.   These assumptions can arise from
- data invariants declared for the data type
- GUMBO integration constraints for the port
- GUMBO entry point preconditions

GUMBO compositional reasoning principles (not yet fully implemented in the tool framework) generate verification conditions to guarantee that the assumptions on the ghost variables of input ports are sound abstractions of all values flowing into the input ports during actual execution.

Now consider the current Rust/Verus version of the API.
```
impl<API: Manage_Heat_Source_i_Get_Api> Manage_Heat_Source_i_Application_Api<API> {
  pub fn get_current_tempWstatus(&mut self) -> (res: data::TempWstatus_i)
      ensures res == self.current_tempWstatus
      && old(self).heat_control == self.heat_control
      && old(self).lower_desired_temp == self.lower_desired_temp
      && old(self).upper_desired_temp == self.upper_desired_temp
      && old(self).regulator_mode == self.regulator_mode
  {
      let data = self.api.get_current_tempWstatus_unverified();
      self.current_tempWstatus = data;
      data
  }
```

I don't understand the details of how Verus works, but I have the following comments (based on just trying to compare to what happens in the Slang code).

- Just like the Slang API function is not verified by Logika, I believe we want to mark the function above    with the Verus `#[verifier::external_body]` annotation to indicate that it should not be verified by Verus.
- Since we are modeling a read of the input application port `current_tempWstatus`, I believe
  we also need a frame condition that the ghost variable current_tempWstatus is not modified
  (otherwise we will lose all of the constraints on this ghost variable as a result of the `get_` method call).
- I believe we DO NOT want the `self.current_tempWstatus = data` statement in Rust that assigns 
  the concrete infrastructure value to the ghost variable since
  the ghost variable serves as an *abstraction* of the actual value coming from the infrastructure.
  Moreover, the body of the function itself is not to be verified to conform to the contract (since we follow the principle that
  showing that the concrete infrastructure value conforms to the ghost variable abstraction is an verification obligation of the (as yet unimplemented) compositional reasoning infrastructure).

The frame conditions such as `old(self).regulator_mode == self.regulator_mode` look fine (except that we need to add one for current_tempWstatus as well).  They do not appear in Slang, but it seems necessary to keep them in the Rust/Verus since the ghost variable are held in the composition API structure in Rust/Verus, while each ghost variable is a distinct global entity in Slang/Logika.

Consider the following proposed trait API declaration from https://github.com/loonwerks/INSPECTA-models/blob/f3be061a81f12665c9a7694c2c00e680f3301c15/isolette/rust-prototyping/SHA-src-2025-02-06/component/manage_heat_source_api.rs#L27C2-L31C6

```
 fn get_lower_desired_temp_unverified(&self, upper: &Ghost<data::Temp_i>) -> (res: data::Temp_i)
      ensures res.degrees <= upper@.degrees
  {
      super::extern_api::unsafe_get_lower_desired_temp()
}
```
Note that the constraint involving `upper@.degrees` comes from the compute entry point pre-condition.
Although it seems possible to correctly deal with the constraint using the approach above, this strategy has the following disadvantages...
- the compute pre-condition no longer appears explicitly in the user code (i.e., the time-triggered method)
- the representation of the pre-condition becomes obscured and scattered throughout the different get API methods

Is there something about how Verus works that makes it impossible to treat the ghost variables and pre-condition following the approach illustrated with Slang/Logika?

### Output Ports / Put APIs

Now consider an example of a Slang API method for an output port (`put_heat_control`) and the manner in which the appropriate abstraction is established between the port's ghost variable and actual value flowing into the output port communication infrastructure.

```
 // Logika spec var representing port state for outgoing data port
  @spec var heat_control: Isolette_Data_Model.On_Off.Type = $

  def put_heat_control(value : Isolette_Data_Model.On_Off.Type) : Unit = {
    Contract(
      Modifies(heat_control),
      Ensures(
        heat_control == value
      )
    )
    Spec {
      heat_control = value
    }

    Art.putValue(heat_control_Id, Isolette_Data_Model.On_Off_Payload(value))
  }
```

In the above, the effect of the method (as observed by the calling application code) is that the ghost variable for the output port is set to the value to be placed on the actual physical infrastructure output port (implemented by the `Art.putValue` method).  This association is further emphasized by the `Spec {..}` statement.  Since Logika is not actually applied to verified to body of the method above against its contract, the `Spec {..}` statement is technically not needed.  As with the `get_...` API methods, the `put_...` method serves as an abstraction of the interaction with the middleware: its execution actually places the value on the middleware, but its use in the deductive verification framework is to form an equality constraint associating the ghost variable `heat_control` for the output port with the value placed in the communication infrastructure.
When verifying the application code, this equality constraint persists on the ghost variable (technically, unless the `put` method is called again) all the way to the end of the application code entry point method where the post-condition of the entry point is checked by the verification framework.  The constraint has the effect of applying the post-condition to the value placed on the output port.

Now consider the current Rust/Verus version of the API.  The semantics of Rust/Verus is essentially identical to the that of the Slang/Logika version (both execution semantics and deductive semantics).  The only (superficial) difference is that the post-condition includes frameconditions for the other port ghost variables.
```
impl<API: Manage_Heat_Source_i_Put_Api> Manage_Heat_Source_i_Application_Api<API> {
    pub fn put_heat_control(&mut self, value : data::OnOff)
        ensures self.heat_control == value
        && old(self).current_tempWstatus == self.current_tempWstatus
        && old(self).lower_desired_temp == self.lower_desired_temp
        && old(self).upper_desired_temp == self.upper_desired_temp
        && old(self).regulator_mode == self.regulator_mode
    {
        self.api.put_heat_control_unverified(value);
        self.heat_control = value
    }
}
```
Note: we can also consider if we want to use a `#[verifier::external_body]` annotation since the verification of this method's body against its contract is not strictly needed in the HAMR verification framework.




