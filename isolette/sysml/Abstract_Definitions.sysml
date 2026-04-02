// Abstract_Definitions.sysml
package Abstract_Definitions {

  private import HAMR::*;

  // Abstract periodic thread serving as the base type for all application threads.
  // Corresponds to AADL Abstract_Definitions::Periodic_Thread, which all thread
  // types in this model extend.  Period matches Isolette_Properties::ThreadPeriod (1000ms).
  abstract part def Periodic_Thread :> Thread {
    attribute :>> Dispatch_Protocol = Periodic;

    attribute :>> Period = Isolette_Properties::ThreadPeriod;

    attribute :>> Compute_Execution_Time = Isolette_Properties::Default_Compute_Execution_Time;
  }

  abstract part def Periodic_Rust_Thread :> Periodic_Thread {
    attribute :>> Implementation_Language = Rust;
  }
}
