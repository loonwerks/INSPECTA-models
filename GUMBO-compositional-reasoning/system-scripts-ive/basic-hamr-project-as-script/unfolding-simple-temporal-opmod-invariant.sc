// #Sireum   -- #Sireum indicates file is in Slang, Logika tells the IVE to apply Logika checking

//  This example illustrates the concept of checking a time-triggered invariant using Logika bounded checking.
//  In full GUMBO, we will have properties that should be true at certain "interesting" points in "operational time",
//  e.g., beginning of a hyper-period, end of a hyper-period, before/after the dispatch of a particular component.
//  Such properties may be viewed as being "triggered" at certain points in time.
//  Alternatively, the may be viewed as invariants with guards that are true at certain points in time.
//

import org.sireum._

//=========================================================================
// U s e r    C o d e   - system behavioral model (auto-generated per system)
// ===========================================================================

//   ..simple component - 1 local variable (c1local), modeled here as a global so that it can be
//   visible to property checking; no inputs/outputs
//
// Verification concept: separately, we verify that the code conforms to the contract.
// Then we use the contract for reasoning about the system-level execution
var c1local:Z = 0
def c1_compute(): Unit = {
  Contract(
    Modifies(c1local),
    Ensures(c1local == In(c1local) + 1)
  )
  c1local = c1local + 1
}

//===================================================================
// A n a l y s i s    C o n f i g u r a t i o n
//   (controllable by the developer)
//===================================================================

// bound on "operational model" time.  Note: a full implementation, this
// would need to be coordinated with other Logika bounds
val opModTimeBound: Z = 10  // bound in terms of system steps in the currently configured time model

//===================================================================
// A n a l y s i s     I n f r a s t r u c t u r e
//
//  Representations of time and other aspects of system execution state
//===================================================================

// analysis infrastructure
//   (auto-generated based on system model characteristics and user-specification of type of analysis)

var opModTime: Z = 0  // current "time" - in the framework's "operational model" of time
// in the simplest case, 1 tick = 1 dispatch of a model component

// analysis function that advances the notion of time by one increment
def advanceOpModTime() : Unit = {
  opModTime = opModTime + 1
}

//===================================================================
// U s e r    P r o p e r t y    E n c o d i n g s
//
//  These would be generated from higher-level properties
//===================================================================

//initialization of state needed to manage properties
def init_user_properties() : Unit = {
}

// In general, we should be able to select various "interesting" temporal points,
// e.g., beginning of hyper-period, end of hyper-period, and check aspects of properties
// at those points.
// These are most easily understood with bounded unfoldings, but we can also imagine various
// forms of inductive schemes for unbounded verification.

// the following predicate indicates if we are at a point of time at which our property should be checked
def isInvariantCheckState(): B = {
  return ((opModTime % 6) == 0)   // set up for an invariant that will be checked at 0, 6, 12, ... etc.
}

// the following predicate encodes the property that should be checked at the specified time point
def temporalInvariantProperty(): B = {
  return (0 == (c1local % 2))  // is c1local even?
}

// function to check properties after each atomic state change
def state_check_user_properties() : Unit = {
  if (isInvariantCheckState()) {   // are we at a time point relevant to our property
    // println(temporalInvariantProperty())
    assert(temporalInvariantProperty())  
  }
}

// function to check state of properties representations when bounded execution is complete
def final_check_user_properties() : B = {
  return true
}




//===================================================================
// S y s t e m    A n a l y s i s    I n f r u s t r u c t u r e
//
//  Representation of HAMR system execution, with analysis functionality
//  interleaved
//===================================================================

// analysis specification (auto-generated)

def boundedSystem() : Unit = {
  // initial user state
  c1local = 0
  // initial system time
  opModTime = 0
  // initialize property state
  init_user_properties()

  // begin system execution, and execute up to user-provided analysis bounds
  // This type of structure would be used for sporadic oriented systems with no static scheduling.
  // In systems where there is a static scheduling, the entire schedule would be unfolded into the while look below.
  while (opModTime < 10) {
    // debugging
    println(s"System Time: ${opModTime}; Component State: ${c1local}")

    // ---- atomic state change property check
    // check user property (auto-generated from user property specification)
    state_check_user_properties()

    c1_compute()

    // ----- advance operational model time
    // advanceOpModTime()
    opModTime = opModTime + 1
  }
}



boundedSystem()

