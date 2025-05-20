// #Sireum   -- #Sireum indicates file is in Slang, #Logika tells the IVE to apply Logika checking
//
// The code in this file provides an abstraction of the basic structures in a HAMR project, along with an illustration
// of the basic deductive principles for reasoning about an AADL / HAMR system.  These deductive principles reflection
// reasoning structure both at the AADL model and in the generated Slang code the conforms to the model.
//
// The purpose of this code is to illustrate that HAMR abstractions of components (threads), communication,
// and scheduling before going on to a full Slang project.
//

import org.sireum._

/*********************************
  Architecture summary (system layout)

      Producer  (thread component)
        pout    (port)
        fin     (port)
      Filter    (thread component)
        fout
        cin
      Consumer  (thread component)
 *********************************/

// declare global variables to represent the abstract state of each of the threads

var plocal: Z = 0  // local state of Producer
var clocal: Z = 0  // local state of Consumer

// declare global variables to represent the abstract state of the ports of each of the components.

var pout: Z = 0    // pout port of Producer
var fin: Z = 0     // fin port of Filter
var fout: Z = 0    // fout port of Filter
var cin: Z = 0     // cin port of Consumer

//================================================
//  Component Entry Points
//================================================

//----------------  P r o d u c e r  -------------

// producer Initialize Entry Point
def producer_init(): Unit = {
  Contract(
    Modifies(plocal,pout),
    // contract to specify values of local state and output port after initialization
    Ensures(plocal == 0 & pout == 0)
  )
  plocal = 0
  pout = 0
}

// producer Compute Entry Point
def producer_compute(): Unit = {
  Contract(
    Modifies(plocal,pout),
    // specify functional behavior of the Producer component
    Ensures(plocal == In(plocal) + 1,
            pout == plocal)
  )
  plocal = plocal + 1 // increment local state
  pout = plocal       // put value on output port
}

//----------------  F i l t e r   -------------

// filter Initialize Entry Point
def filter_init(): Unit = {
  Contract(
    Modifies(fout),
    // specify initial value of output port
    Ensures(fout == 0)
  )
  fout = 0 // put initial value on output port
}

// filter Compute Entry Point
def filter_compute(): Unit = {
  Contract(
    Modifies(fin, fout),
    // specify functional behavior of filter
    Ensures(fin == In(fin),  // input port is not modified by component
            (In(fin) < 10) ->: (fout == In(fin)), // if input value is < 10, then pass the value through
            !(In(fin) < 10) ->: (fout == 0))      // otherwise, send out a 0
  )
  if (fin < 10) {
    fout = fin
  } else {
    fout = 0
  }
}

//----------------  C o n s u m e r   -------------

// Consumer Initialize Entry Point
def consumer_init(): Unit = {
  Contract(
    Modifies(clocal),
    Ensures(clocal == 0)
  )
  clocal = 0
}

// Consumer Compute Entry Point
def consumer_compute(): Unit = {
  Contract(
    Modifies(clocal, cin),
    // add incoming values onto an accumulator
    Ensures(clocal == In(clocal) + In(cin),
            cin == In(cin))
  )
  clocal = clocal + cin
}

//================================================
//  Communication Steps
//
//  The following functions model direct communication (no model of communication substrate state)
//  from a sender to a receiver for AADL data ports.
//================================================

def p2fcomm(): Unit = {
  Contract(
    Modifies(fin,pout),
    Ensures(pout == In(pout), // pout is not changed
            fin == In(pout))  // value of pout is propagated to fin
  )
  fin = pout
}

def f2ccomm(): Unit = {
  Contract(
    Modifies(cin,fout),
    Ensures(cin == In(fout),  // value of fout is propagated to cin
            fout == In(fout))
  )
  cin = fout
}

//================================================
//  Scheduling
//================================================

//=========== initialization phase reasoning

// "Helper predicate" that describes the local state of components and their ports after the
//  AADL initialize phase is completed.
//
// generalize in the future to deal with families of initial states
@strictpure def initPost(poutp: Z, plocalp: Z, foutp: Z, clocalp: Z): B =
  (  poutp == 0
   & plocalp == 0
   & foutp == 0
   & clocalp == 0
  )

// Abstract representation of the AADL Initialize Phase
//  -- each component Initialize Entry Point is executed
def initPhase() : Unit = {
  Contract(
    Modifies(plocal,pout,fout,clocal),
    Ensures(// init phase post-condition
      initPost(pout, plocal, fout, clocal)
    )
  )
  producer_init()
  filter_init()
  consumer_init()
}

// Note: the following only makes complete sense for data ports in which we have no
//  "fan in"
def initCommunicationStep(): Unit = {
  Contract(
    Modifies(pout,fin,fout,cin),
    Ensures(pout == In(pout),
            fin == In(pout),
            fout == In(fout),
            cin == In(fout)
           )
  )
  // propagate values from Producer to Filter
  p2fcomm()
  // propagate values from Filter to Consumer
  f2ccomm()
}

//=========== compute phase reasoning

// Abstractly:
//   1 hyper-period, with each component/thread (periodic) executing once,
//   with AADL's immediate communication option
def computeOneHPimmediateComm() : Unit = {
  Contract(
    Modifies(plocal,pout,fin,fout,cin,clocal),
    // Note: It is possible that we would NOT want to state a precise contract that summarizes the end-to-end
    // behavior on ports and local state variables for each hyper-period.  Instead, we would want to state
    // some key properties (invariants, or properties that should hold at HP boundaries), and then prove that
    // those hold for a bounded unrolling of the system
    Ensures(plocal == In(plocal) + 1,
            pout == plocal,
            fin == pout,
            (fin < 10) ->: (fout == fin),
            !(fin < 10) ->: (fout == 0),
            cin == fout,
            clocal == In(clocal) + cin
    )
  )
  // producer
  producer_compute()
  // producer -> filter
  //   Note: we can also imagine that some steps, like the communication steps are simple equalities that we don't
  //   need both a contract and an implementation for.
  p2fcomm()
    // filter
  filter_compute()
  // filter -> consumer
  f2ccomm()
  // consumer
  consumer_compute()
}

// @strictpure def hpInvariant( ): B =
//  (
//
//  )

// Useful for debugging / testing
def logSysState(description: String): Unit = {
  println("----------------")
  println(description)
  println("----------------")
  println(s"plocal = ${plocal}")
  println(s"pout   = ${pout}")
  println(s"fin    = ${fin}")
  println(s"fout   = ${fout}")
  println(s"cin    = ${cin}")
  println(s"clocal = ${clocal}")
}

// define a "helper predicate" that abstractly represents the end-to-end function of the system at the hyper-period boundary
//  (from producer local state to consumer local state)
@strictpure def absInputOutput(plocalp: Z): Z = {
  if (plocalp == 0) {
    0
  } else if (plocalp < 10) {
    // summation function
    (plocalp * (plocalp + 1)) / 2
  } else {
    // stable state of the system after 10 iterations
    45
  }
}

// The following function represents an abstraction of the system when
//  initializing, then executing a single hyper-period
def sysTrace1HP() : Unit = {
  Contract(
    Modifies(plocal,pout,fin,fout,cin,clocal)
  )
  initPhase()
  logSysState("After Init")
  Deduce(
    |- (initPost(pout, plocal, fout, clocal))
  )
  initCommunicationStep()
  // computeInv hold
  computeOneHPimmediateComm()
  Deduce(
    |- (plocal == 1)
  )
  logSysState("After 1 HP")
}

// The following function represents an abstraction of the system when
//  initializing, then executing a 12 hyper-periods

def sysTrace12HP() : Unit = {
  Contract(
    Modifies(plocal,pout,fin,fout,cin,clocal)
  )
  // Initialization Phase of system
  initPhase()
  logSysState("After Init")
  Deduce(
    |- (initPost(pout, plocal, fout, clocal))
  )
  // communication after Initialization Phase
  initCommunicationStep()
  // declare var to count number of hyper-periods in a bounded execution of the system
  var i: Z = 0
  // configure number of hyper-periods to be run in the bounded execution (this could be passed as a parameter)
  val numHPs: Z = 12
  // Compute Phase of the system
  //  (for the configured # of hyper-periods)
  while (i < numHPs) {
    Invariant(
      Modifies(plocal,pout,fin,fout,cin,clocal),
      clocal == absInputOutput(plocal)
    )
    computeOneHPimmediateComm()
    logSysState(s"After Iteration: ${i}")
    i = i + 1
    // println(s"Spec: plocal = ${plocal} expected clocal = ${clocal}")
  }
}

// call the simulated execution (executed if this file is run as a Slang script)
sysTrace12HP()

def sysTraceUnrolling3HP() : Unit = {
  Contract(
    Modifies(plocal,pout,fin,fout,cin,clocal)
  )
  // Initialization Phase of system
  initPhase()
  logSysState("After Init")
  Deduce(
    |- (initPost(pout, plocal, fout, clocal))
  )
  // communication after Initialization Phase
  initCommunicationStep()
  // declare var to count number of hyper-periods in a bounded execution of the system
  var i: Z = 0
  // configure number of hyper-periods to be run in the bounded execution (this could be passed as a parameter)
  val numHPs: Z = 3
  // Compute Phase of the system
  //  (for the configured # of hyper-periods)
  while (i < numHPs) {
    Invariant(
      Modifies(plocal,pout,fin,fout,cin,clocal),
      clocal == absInputOutput(plocal)
    )
    computeOneHPimmediateComm()
    logSysState(s"After Iteration: ${i}")
    i = i + 1
    // println(s"Spec: plocal = ${plocal} expected clocal = ${clocal}")
  }
}

def unrollingTest() : Unit = {
  Contract(
    Modifies(plocal,pout,fin,fout,cin,clocal)
  )
  // Initialization Phase of system
  initPhase()
  logSysState("After Init")
  // communication after Initialization Phase
  initCommunicationStep()
  // declare var to count number of hyper-periods in a bounded execution of the system
  var i: Z = 0
  // configure number of hyper-periods to be run in the bounded execution (this could be passed as a parameter)
  val numHPs: Z = 3
  // Compute Phase of the system
  //  (for the configured # of hyper-periods)
  assert(plocal == 0)
}
