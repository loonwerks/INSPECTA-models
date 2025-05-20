Notes to Gage for Helping on HAMR System Contract Verification
================================================================

This file contains suggestions for getting started helping John and Jason on research related to system-level verification of HAMR contracts.

John has sketched out some basic patterns that use Logika's "Interprocedural" symbolic execution.  One of those examples temp-control.sc includes an illustration of checking system-level properties on a simplified version of the Temp Control example.

We would like Gage to follow these patterns to experiment with system-level verification of the Isolette example. Suggested steps are given below.

Creating a Representation of the Model
========================================

The aim is to incrementally create a representation of the Isolette AADL model directly in Slang.  We will likely have several .sc files -- each moving to a more complicated version of the model.

Phase I (Regulate Subsystem Only)
----------

Create a representation of the Isolette Regulate subsystem following the style presented, e.g., in temp-control.sc
We will use Z instead of F32, etc. to make the prototyping more efficient.

Here are the main steps.

**Data Types** 

Create representations of the Regulate subsystem data types following the style in the "Data Types" section of temp-control.sc

**Component Ports** 

Following the organizational style in temp-control.sc (e.g., each component's representation contents grouped together), 
create global variables representing each of the component's ports, e.g., 

```scala worksheet
// --- Port - current Temp ---

// declare global variable to model port queue
// (this can eventually be switched to a spec var)
var currentTemp_Port_TSOut: Temperature_i = Temperature_i(z"72")
```

**Initialize Entry Point** 

Create a method representing the initialize entry point for each component.  Replace occurances of port APIs, e.g., ```get_value```, ```put_value``` in the original code with direct manipulation of the global variables representing ports.  Translate the contract in the original HAMR slang to a contract in the script mock-up.

```scala worksheet
//-------------------------------------------------
//  I n i t i a l i z e    E n t r y    P o i n t
//-------------------------------------------------
def initialise_EP_TS(): Unit = {
  Contract(
    // in general, each component with output event ports needs to be
    // able to assume that the
    Requires(tempChanged_Port_TSOut.isEmpty),
    Modifies(currentTemp_Port_TSOut),
    Ensures(currentTemp_Port_TSOut == Temperature_i(z"72"),
            tempChanged_Port_TSOut.isEmpty
            )
  )
  currentTemp_Port_TSOut = Temperature_i(z"72")   // put RTS
}
```

**Compute Entry Point**

Create a method representing the compute entry point for each component.  Replace occurances of port APIs, e.g., ```get_value```, ```put_value``` in the original code with direct manipulation of the global variables representing ports.  Translate the contract in the original HAMR slang to a contract in the script mock-up.

```scala worksheet
//-------------------------------------------------
//  C o m p u t e    E n t r y    P o i n t
//-------------------------------------------------

@strictpure def tempBounding(t: Z) : Z = if (t > 150) 150 else (if (t < -50) -50 else t)

def compute_EP_TS(): Unit = {
  Contract(
    Modifies(currentTemp_Port_TSOut,tempChanged_Port_TSOut),
    Ensures(currentTemp_Port_TSOut == Temperature_i(tempBounding(tempEnv)))
  )
  // read temperature from HARDWARE temperature sensor,
  // via interface realized via Slang Extension "TempSensorNative"
  var temp: Z = tempBounding(tempEnv) // Z.random; assume(temp > -50 & temp < 120)
  currentTemp_Port_TSOut = Temperature_i(temp)  // put temp value out on currentTemp port
  tempChanged_Port_TSOut = Some(event) // send tempChanged event
}
```

In the above, I have added some extra concepts for reading from the sensor.  In the Isolette example, you can skip stuff like that for now since the Regulate components receive the sensor value from a port.

**Communication Infrastructure**

To model the communication between output and connected input ports in the Regulate subsystem, create methods like the one below for each connection to propagate a value from an output data port to a connected input data port.

```scala worksheet
def TS_to_TC_currentTemp_Comm(): Unit = {
  Contract(
    Modifies(currentTemp_Port_TCIn),
    Ensures(
      currentTemp_Port_TCIn == currentTemp_Port_TSOut      // propagate
    )
  )
  currentTemp_Port_TCIn = currentTemp_Port_TSOut
}
```

System Composition
========================================

**Initial State**

Create a representation of the initial system state.  For the Isolette, this will be all ports set to the default values for their given data types (and if there are any GUMBO declared state variables, those will be set to their default values as well).
```scala worksheet
def initialSystemState() : Unit = {
  ...}
```

**Init Phase**

Create a representation of the semantics of the Initialization Phase of the system in which each component is executed once.

```scala worksheet
def initPhase() : Unit = {
  // === init phase pre-condition (different strategies) ===
  // Give a logical characterization of the initial state
  // [1] "Ensure" the pre-condition in a method contract.
  initialSystemState()
  
  // === run static schedule of system init phase ===
  //     (round-robin of each component initialize entry point)
  
  // ...insert calls to each component's initialize entry point here
  
  // === init phase post-condition (different strategies) ===
  //  [1] "Require" the post-condition in a method contract
  initPhasePostCondition()
  // - you can experiment with this
}
```