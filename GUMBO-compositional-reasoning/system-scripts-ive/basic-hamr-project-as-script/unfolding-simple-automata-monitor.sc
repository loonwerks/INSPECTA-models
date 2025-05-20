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
// Verification concept: separately, we verify that the code conforms to the contract.
// Then we use the contract for reasoning about the system-level execution
var c1local:Z = 0
var c1OutPort:Z = 0
def c1_compute(): Unit = {
  Contract(
    Modifies(c1local, c1OutPort),
    Ensures((In(c1local) <= 3) ->: (c1OutPort == 0),
            ((In(c1local)) >= 4 & (In(c1local) <= 6)) ->: (c1OutPort == 1),
            ((In(c1local)) >= 7 & (In(c1local) <= 8)) ->: (c1OutPort == 2),
            ((In(c1local)) >= 9) ->: (c1OutPort == 0),
            (c1local == In(c1local) + 1))
  )
  if (c1local <= 3) {
    c1OutPort = 0
  } else if (c1local <= 6) {
    c1OutPort = 1
  } else if (c1local <= 8) {
    c1OutPort = 2
  } else {
    c1OutPort = 0
  }
  c1local = c1local + 1
}

var c2InPort:Z = 0
var c2Mode:Z = 0
def c2_compute(): Unit = {
  Contract(
    Requires(c2InPort >= 0 & c2InPort <= 3),
    Modifies(c2Mode),
    Ensures(
      (c2InPort == 0) ->: (c2Mode == In(c2Mode)),
      (c2InPort == 1) ->: (c2Mode == 1),
      (c2InPort == 2) ->: (c2Mode == 2),
      (c2InPort == 3) ->: (c2Mode == 3))
  )
  if (c2InPort == 1) {
    c2Mode = 1
  } else if (c2InPort == 2) {
    c2Mode = 2
  } else if (c2InPort == 3) {
    c2Mode = 3
  }
}

//===================================================================
// A n a l y s i s    C o n f i g u r a t i o n
//   (controllable by the developer)
//===================================================================

// bound on "operational model" time.  Note: a full implementation, this
// would need to be coordinated with other Logika bounds
val opModTimeBound: Z = 10  // bound system steps in the currently configured time model

//===================================================================
// U s e r    P r o p e r t y    E n c o d i n g s
//
//  These would be generated from higher-level properties
//===================================================================

var propAutomataState:Z = 0 // state of property automata
//initialization of state needed to manage properties
def init_user_properties() : Unit = {
  propAutomataState = 0
}

// function to check properties after each atomic state change
def state_check_user_properties() : Unit = {

  // property automata state transition
  if (propAutomataState == 0 & c2Mode == 1) {
    propAutomataState = 1
  }
  if (propAutomataState == 1 & c2Mode == 2) {
    propAutomataState = 2
  }

  // enforce desired monitored property in each automata state
  if (propAutomataState == 0) {
    assert(c2Mode == 0)
  }
  if (propAutomataState == 1) {
    assert(c2Mode == 1)
  }
  if (propAutomataState == 2) {
    assert(c2Mode == 2)
  }
  assert(c2Mode != 3)
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

// analysis specification (auto-generated)

def boundedSystem() : Unit = {
  // initial user state
  // c1
  c1local = 0
  c1OutPort = 0
  // c2
  c2Mode = 0
  c2InPort = 0
  // initial system time
  opModTime = 0
  // initialize property state
  init_user_properties()

  // begin system execution, and execute up to user-provided analysis bounds
  // This type of structure would be used for sporadic oriented systems with no static scheduling.
  // In systems where there is a static scheduling, the entire schedule would be unfolded into the while look below.
  while (opModTime < 10) {
    // debugging
    // println(s"System Time: ${opModTime}; System State: ${c1local}")

    // ----- begin Component c1 relational behavior ---
    //  For now, we need to simulate the encounter with a method contract in Logika's implementation
    //  of unfolding symbolic execution.  One a mix of unfolding and contract implementation is implemented,
    //  we may desire to have a direct reference to method/contract here.
    //
    // Modifies(c1local, c1OutPort)
    //
    // Ensures((In(c1local) <= 3) ->: (c1OutPort == 0),
    //  ((In(c1local)) >= 4 & (In(c1local) <= 6)) ->: (c1OutPort == 1),
    //  ((In(c1local)) >= 7 & (In(c1local) <= 8)) ->: (c1OutPort == 2),
    //  ((In(c1local)) >= 9) ->: (c1OutPort == 0),
    //  (c1local == In(c1local) + 1))
    //   (pre-condition is empty, so nothing to assert)
    //  --- assert pre-condition
    //   save c1 pre-state so that it could be referred to in post-state via "In" construct
    val c1local_in = c1local
    //   --- simulate c1 state changes my havoc-ing all variables that the component modifies
    c1local = randomInt() // havoc c1local
    c1OutPort = randomInt()
    //  ------ enforce c1 post-condition ------
    assume((c1local_in <= 3) ->: (c1OutPort == 0))
    assume((c1local_in >= 4 & (c1local_in <= 6)) ->: (c1OutPort == 1))
    assume((c1local_in >= 7 & (c1local_in <= 8)) ->: (c1OutPort == 2))
    assume((c1local_in >= 9) ->: (c1OutPort == 0))
    assume(c1local == c1local_in + 1)
    // ------ end c1 relational behavior ------

    // communication
    c2InPort = c1OutPort
    // ---- begin c2 relational behavior ----
    val c2Mode_in = c2Mode
    assert(c2InPort >= 0 & c2InPort <= 3)
    c2Mode = randomInt() // havoc modified variable
    assume((c2InPort == 0) ->: (c2Mode == c2Mode_in))
    assume((c2InPort == 1) ->: (c2Mode == 1))
    assume((c2InPort == 2) ->: (c2Mode == 2))
    assume((c2InPort == 3) ->: (c2Mode == 3))
    // ---- end c2 relational behavior ---


    // ---- atomic state change property check
    // check user property (auto-generated from user property specification)
    //   - simple global temporal invariant: [](c1local >= 0), i.e., the property always holds
    //     must be checked after each state change
    state_check_user_properties()

    // ----- advance operational model time
    // advanceOpModTime()
    opModTime = opModTime + 1
  }
  assert(c2Mode == 2)
  // assert(false)
}



boundedSystem()

