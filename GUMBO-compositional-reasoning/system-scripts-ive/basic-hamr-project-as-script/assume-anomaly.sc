// #Sireum   -- removed Logika tag to focus on Logika unfolding checking

import org.sireum._

var inPort1: Z = 0
var clocal: Z = 0

def analysisTopLevel():Unit = {
  inPort1 = 6
  clocal = 0

  assume(inPort1 >= 5)
  assert(clocal == 2)
}
