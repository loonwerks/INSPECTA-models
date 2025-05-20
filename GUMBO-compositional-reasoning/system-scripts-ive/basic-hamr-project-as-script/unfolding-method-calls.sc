// #Sireum   -- removed Logika tag to focus on Logika unfolding checking

// This example illustrates how we need to represents method calls in situations
// where we want to apply Logika interprocedural analysis, but yet do NOT want to unfold
// some methods, i.e., we want to use the input/output relation associated with their
// contract in the analysis instead of the method implementations.
// This is what we want to do with GUMBO contracts for
// AADL components -- we need the overall analysis to be an interprocedural analysis, but we want
// the analysis to use the **contracts** for each user component as the basis for the system analysis.
// This will give us a system analysis in which, for each component, the input/output behavior on
// the component's ports are realized by the contract -- not the component's implementation.
//

import org.sireum._

// mock up structure for a component with two input ports, two output ports, and
// one local variable

var inPort1: Z = 0
var inPort2: Z = 0
var outPort1: Z = 0
var outPort2: Z = 0
var clocal: Z = 0

def c1() : Unit = {
 Contract(
   Requires(inPort1 >= 0 & inPort2 >= 0),
   Modifies(clocal, outPort1, outPort2),
   Ensures(outPort1 == (In(clocal) + inPort1 + inPort2),
           outPort2 == inPort2 * 2,
           clocal == In(clocal) + 1)
 )
  outPort1 = clocal + inPort1 + inPort2
  outPort2 = inPort2 * 2
  clocal = clocal + 1
}

def analysisTopLevel():Unit = {

  // set initial state of component
  inPort1 = 0
  inPort2 = 0
  outPort1 = 0
  outPort2 = 0
  clocal = 0

  // with the "use contracts in interprocedural analysis Logika option", the
  // the c1 method contract will be used in the verification
  c1()

  // observe the current state of c1, based on its contract behavior, as a global property might do
  assert(clocal == 1)
  assert(outPort1 == 0)
  assert(outPort2 == 0) // inPort1 is initialized to 0, so this causes outPort2 to stay at 0


  // simulate a receipt of value 3 on inPort1 (e.g., due to effects of other component behaviors).
  inPort1 = 3
  // simulate a receipt of some value, where we only know constraint, not actual value
  inPort2 = randomInt()
  assume(inPort2 >= 5)

  // simulate the effects of the component a second time
  c1()

  // assert the effect of c1's contract behavior after message arrival
  assert(clocal == 2) // clocal incremented once for each dispatch
  assert(outPort1 >= 9) // In(clocal) [1] + inPort1 [3] + inPort2 [5]
  assert(outPort2 >= 10)  // inPort2 [>= 5] * 2
}


