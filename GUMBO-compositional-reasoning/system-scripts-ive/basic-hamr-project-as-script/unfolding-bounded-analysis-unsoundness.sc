// #Sireum   -- remove Logika tag to focus on Logika unfolding checking

// This example illustrates the potential unsoundess of Logika unbounded
// verification.  If the loop bound is set too low, Logika may not exhaustively explore
// the program state space and thus miss detecting an error (e.g., an assertion violation).

import org.sireum._

var appVar:Z = 0
var appSteps:Z = 0

// To observe analysis soundness
// set stepBound to greater than 15 (e.g., 16) and
// set the loop unrolling bound in Logika preferences to something greater 16 (e.g., 20).
// To observe analysis unsoundness,
// set stepBound to greater than 15 (e.g., 16) and
// set the loop unrolling bound in Logika preferences to something less than 15 (e.g., 14).

val stepBound:Z = 16
def boundedSystem() : Unit = {
  // initial user var state
  appVar = 0
  // initial number of steps
  appSteps = 0

  while (appSteps < stepBound) {

    // appVar is normally incremented, EXCEPT when it reaches a value of 15
    // This is used to illustrate that that the assertion appVar >= 0 will
    // hold up to the point where appVar is 15, but then fail.
    if (appVar != 15) {
      appVar = appVar + 1
    } else {
      appVar = -1
    }
    // user-specified system property
    assert(appVar >= 0)

    // advance step count
    appSteps = appSteps + 1
  }
  // assert(false)
}