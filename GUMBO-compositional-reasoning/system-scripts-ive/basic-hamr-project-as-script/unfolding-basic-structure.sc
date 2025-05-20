// #Sireum   -- #Sireum indicates file is in Slang, Logika tells the IVE to apply Logika checking

//  This example illustrates the basic structure of using Logika unfolding analysis for HAMR system-level
//  verification.

import org.sireum._

//=========================================================================
// U s e r    C o d e   - system behavioral model (auto-generated per system)
// ===========================================================================

//   ..simple component - 1 local variable (c1local), modeled here as a global so that it can be
//   visible to property checking; no inputs/outputs
//
// Verification concept: separately (running Logika in "normal" mode),
// we verify that the code conforms to the contract.
//
// Then, using Logika inter-procedural analysis, we use the component's contract (not it's source code)
// for reasoning about the system-level execution using the HAMR "computation schema".
//
// This is analogous to what happens in full HAMR/GUMBO: we use Logika to verify
// that Slang implementations conform to HAMR/GUMBO-generated in each component.
// Then (this is what we are aiming for in our prototype), we use Logika interprocedural
// analysis on the HAMR "computation schema" for system level reason.

var c1local:Z = 0
def c1_compute(): Unit = {
  Contract(
    Modifies(c1local),
    Ensures(c1local > In(c1local))
    // use this post-condition below (comment out the one above)
    // to illustrate an error in system-level checking.
    // It specifies that c1local is always decreasing, which will
    // cause the invariant c1local >= 0 to be violated.
    // Logika will show the property violation in the
    //  state_check_user_properties method (the assertion will fail)
    // Ensures(c1local < In(c1local))
  )
  c1local = c1local + 1
}

//===================================================================
// A n a l y s i s    C o n f i g u r a t i o n
//   (controllable by the developer)
//===================================================================

// bound on "operational model" time.  Note: a full implementation, this
// would need to be coordinated with other Logika bounds
val opModTimeBound: Z = 10  // bound in system steps in the currently configured time model

//===================================================================
// U s e r    P r o p e r t y    E n c o d i n g s
//
//  These would be generated from higher-level properties.
//
//  To illustrate this, imagine a simple system-level property:
//    an invariant stating that  c1local >= 0
//
//  The envisioned tool would generate Slang helper functions
//  to aid in initializing and checking of the system-level properties.
//  We typically need such functions
//    - to initialize aspects of the property checking at beginning
//      of execution
//    - to check with a property holds at specific points during the execution
//    - to check with a property is true at the end of execution
//
//  Note that our simple invariant example WILL NOT illustrate all
//  of these (it's not complex enough).  However, we mock up
//  the helper functions to show the general anticipated structure.
//===================================================================

//initialization of state needed to manage properties
def init_user_properties() : Unit = {}

// function to check properties after each atomic state change
def state_check_user_properties() : Unit = {
  // check if the system-level invariant property is currently true
  val c1gte0 = (c1local >= 0)
  assert(c1gte0)
}

// function to check state of properties representations when bounded execution is complete
def final_check_user_properties() : B = {
  return true
}


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
// S y s t e m    A n a l y s i s    I n f r u s t r u c t u r e
//
//  Representation of HAMR system execution, with analysis functionality
//  interleaved
//===================================================================

//---------------------------------------
//  HAMR execution schema
//
// The following Slang function abstractly captures the execution of
// the HAMR run-time at a level of abstraction that is sufficient for
// verification of a given system property set.
//
// We imagine different versions of the schema would be generated
// based on the nature of the model, and based on the structure of the
// properties to be checked.
//----------------------------------------

def boundedSystem() : Unit = {
  // initialize the state of user's component local variables
  //  (these would be derived from GUMBO state declarations)
  c1local = 0
  // initialize system time
  opModTime = 0
  // initialize property state
  init_user_properties()

  val opModTimeBoundLocal = 10

  // begin system execution, and execute up to user-provided analysis bounds
  // This type of schema structure would be used for sporadic oriented systems with no static scheduling.
  // In systems where there is a static scheduling, the entire schedule would be unfolded into the while loop below.
  while (opModTime < opModTimeBoundLocal) {
    // debugging
    println(s"System Time: ${opModTime}; System State: ${c1local}")

    // capture the effect of one execution (dispatch) of the component.
    // Logika interprocedural analysis would use the method contract to
    // achieve the logical "state updates" carried out by the component.
    c1_compute()

    // ---- atomic state change property check
    // check user property (auto-generated from user property specification)
    //   - simple global temporal invariant: [](c1local >= 0), i.e., the property always holds
    //     at the beginning and ending of each component execution
    state_check_user_properties()
    // assert(c1local >= 0)

    // ----- advance operational model time
    // advanceOpModTime()
    opModTime = opModTime + 1
  }
}



boundedSystem()

