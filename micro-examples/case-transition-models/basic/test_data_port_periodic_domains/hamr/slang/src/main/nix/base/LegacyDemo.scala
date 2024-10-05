// #Sireum

package base

import org.sireum._
import art._

// Do not edit this file as it will be overwritten if HAMR codegen is rerun

object LegacyDemo extends App {
  def main(args: ISZ[String]): Z = {

    val seed: Z = if (args.size == z"1") {
      val n = Z(args(0)).get
      if (n <= z"0") 1 else n
    } else {
      1
    }

    PlatformComm.initialise(seed, None())

    val empty = art.Empty()

    PlatformComm.sendAsync(IPCPorts.producer_thread_i_producer_producer_App, IPCPorts.producer_thread_i_producer_producer_App, empty)
    PlatformComm.sendAsync(IPCPorts.consumer_thread_i_consumer_consumer_App, IPCPorts.consumer_thread_i_consumer_consumer_App, empty)

    PlatformComm.finalise()
    return 0
  }
}