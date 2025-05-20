// #Sireum #Logika  -- #Sireum indicates file is in Slang, #Logika tells the IVE to apply Logika checking
import org.sireum._

/*********************************
  Architecture summary

      Producer  (component)
        pout    (port)
        p2fconn (communication channel)
        fin     (port)
      Filter    (component)
        fout
        f2cconn
        cin
      Consumer
 *********************************/

var plocal: Z = 0
var pout: Z = 0
var p2fconn: Z = 0
var fin: Z = 0
var fout: Z = 0
var f2cconn: Z = 0
var cin: Z = 0
var clocal: Z = 0

//================================================
//  Component Entry Points
//================================================

//----------------  P r o d u c e r  -------------

def producer_init(): Unit = {
  Contract(
    Modifies(plocal,pout),
    Ensures(plocal == 0 & pout == 0)
  )
  plocal = 0
  pout = 0
}

def producer_compute(): Unit = {
  Contract(
    Modifies(plocal,pout),
    Ensures(plocal == In(plocal) + 1,
            pout == plocal)
  )
  plocal = plocal + 1
  pout = plocal
}

//----------------  F i l t e r   -------------

def filter_init(): Unit = {
  Contract(
    Modifies(fout),
    Ensures(fout == 0)
  )
  fout = 0
}

def filter_compute(): Unit = {
  Contract(
    Modifies(fin, fout),
    Ensures(fin == In(fin),
            (In(fin) < 10) ->: (fout == In(fin)),
            !(In(fin) < 10) ->: (fout == 0))
  )
  if (fin < 10) {
    fout = fin
  } else {
    fout = 0
  }
}

//----------------  C o n s u m e r   -------------

def consumer_init(): Unit = {
  Contract(
    Modifies(clocal),
    Ensures(clocal == 0)
  )
  clocal = 0
}

def consumer_compute(): Unit = {
  Contract(
    Modifies(clocal, cin),
    Ensures(clocal == In(clocal) + In(cin),
            cin == In(cin))
  )
  clocal = clocal + cin
}

//================================================
//  Communication Steps
//================================================

def p2fcommSend(): Unit = {
  Contract(
    Modifies(p2fconn,pout),
    Ensures(p2fconn == In(pout),
           pout == In(pout))
  )
  p2fconn = pout
}

def p2fcommReceive(): Unit = {
  Contract(
    Modifies(p2fconn,fin),
    Ensures(fin == In(p2fconn),
            p2fconn == In(p2fconn))

  )
  fin = p2fconn
}

def f2ccommSend(): Unit = {
  Contract(
    Modifies(f2cconn,fout),
    Ensures(f2cconn == In(fout),
            fout == In(fout))
  )
  f2cconn = fout
}

def f2ccommReceive(): Unit = {
  Contract(
    Modifies(cin,f2cconn),
    Ensures(cin == In(f2cconn),
            f2cconn == In(f2cconn))
  )
  cin = f2cconn
}

//================================================
//  Scheduling
//================================================

//=========== initialization phase reasoning

// generalize in the future to deal with families of initial states
@strictpure def initPost(poutp: Z, plocalp: Z, foutp: Z, clocalp: Z): B =
  (  poutp == 0
   & plocalp == 0
   & foutp == 0
   & clocalp == 0
  )

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
    Modifies(pout,p2fconn,fin,fout,f2cconn,cin),
    Ensures(pout == In(pout),
            fin == In(pout),
            p2fconn == In(pout),
            fout == In(fout),
            f2cconn == In(fout),
            cin == In(fout)
           )
  )
  p2fcommSend()
//  Deduce(
//    |- (p2fconn == pout)
//  )
  p2fcommReceive()
//  Deduce(
//    |- (fin == p2fconn)
//  )
  f2ccommSend()
  f2ccommReceive()
}

//=========== compute phase reasoning

// Abstractly:
//   1 hyper-period
//   immediate communication
def computeOneHPimmediateComm() : Unit = {
  Contract(
    Modifies(plocal,pout,p2fconn,fin,fout,f2cconn,cin,clocal),
    Ensures(plocal == In(plocal) + 1,
            pout == plocal,
            p2fconn == pout,
            fin == pout,
            (fin < 10) ->: (fout == fin),
            !(fin < 10) ->: (fout == 0),
            f2cconn == fout,
            cin == f2cconn,
            clocal == In(clocal) + cin
    )
  )
  // producer
  producer_compute()
  // producer -> filter
  p2fcommSend()
  p2fcommReceive()
  // filter
  filter_compute()
  // filter -> consumer
  f2ccommSend()
  f2ccommReceive()
  // consumer
  consumer_compute()
}

// @strictpure def hpInvariant( ): B =
//  (
//
//  )

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

@strictpure def absInputOutput(plocalp: Z): Z = {
  if (plocalp == 0) {
    0
  } else if (plocalp < 10) {
    (plocalp * (plocalp + 1)) / 2
  } else {
    45
  }
}

def sysTrace1HP() : Unit = {
  Contract(
    Modifies(plocal,pout,p2fconn,fin,fout,f2cconn,cin,clocal)
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

// sysTrace1HP()

def sysTrace12HP() : Unit = {
  Contract(
    Modifies(plocal,pout,p2fconn,fin,fout,f2cconn,cin,clocal)
  )
  initPhase()
  logSysState("After Init")
  Deduce(
    |- (initPost(pout, plocal, fout, clocal))
  )
  initCommunicationStep()
  var i: Z = 0
  while (i < 12) {
    Invariant(
      Modifies(plocal,pout,p2fconn,fin,fout,f2cconn,cin,clocal),
      clocal == absInputOutput(plocal)
    )
    computeOneHPimmediateComm()
    logSysState(s"After Iteration: ${i}")
    i = i + 1
    // println(s"Spec: plocal = ${plocal} expected clocal = ${clocal}")
  }
}

sysTrace12HP()


