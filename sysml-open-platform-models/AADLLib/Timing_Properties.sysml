/* SysML v2 Domain Library for AADL:
 * references: 
 * SAE AS-5506D AADL appendix A3
 * OMG SysML v2 */

standard library package Timing_Properties {
	
	private import AADL::*; 

    // Activate_Deadline: Time applies to (Thread);
	attribute def Activate_Deadline :> AADL::Property, Time;

    // Activate_Execution_Time: Time_Range applies to (thread);
	attribute def Activate_Execution_Time :> AADL::Property, Time_Range; 

	// Compute_Deadline: Time 
    //    applies to (thread, device, subprogram, subprogram access, event port, event data port);
	attribute def Compute_Deadline :> AADL::Property, Time;

    // Compute_Execution_Time: Time_Range 
    //    applies to (thread, device, subprogram, event port, event data port); 
	attribute def Compute_Execution_Time :> AADL::Property, Time_Range;
	
    // Client_Subprogram_Execution_Time: Time_Range applies to (subprogram); 
	attribute def Client_Subprogram_Execution_Time :> AADL::Property, Time_Range;

    // Deactivate_Deadline: Time applies to (thread); 
	attribute def Deactivate_Deadline :> AADL::Property, Time; 

    // Deactivate_Execution_Time: Time_Range applies to (thread); 
	attribute def Deactivate_Execution_Time :> AADL::Property, Time_Range;

    // Deadline: inherit Time => Period 
    //    applies to (thread, thread group, process, system, device, virtual processor);
	attribute def Deadline :> AADL::Property, Time; 

    // First_Dispatch_Time : inherit Time applies to (thread, thread group);
    attribute def First_Dispatch_Time :> AADL::Property, Time;
 
    // Dispatch_Jitter: inherit Time applies to (thread, thread group); 
    attribute def Dispatch_Jitter :> AADL::Property, Time;

    // Dispatch_Offset: inherit Time applies to (thread); 
    attribute def Dispatch_Offset :> AADL::Property, Time;

    // Execution_Time: Time applies to (virtual processor); 
    attribute def Execution_Time :> AADL::Property, Time;

    // Finalize_Deadline: Time applies to (thread); 
    attribute def Finalize_Deadline :> AADL::Property, Time;
    
    // Finalize_Execution_Time: Time_Range applies to (thread);
    attribute def Finalize_Execution_Time :> AADL::Property, Time_Range;

    // Initialize_Deadline: Time applies to (thread); 
    attribute def Initialize_Deadline :> AADL::Property, Time;

    // Initialize_Execution_Time: Time_Range applies to (thread); 
    attribute def Initialize_Execution_Time :> AADL::Property, Time_Range;
 
    // Load_Deadline: Time applies to (process, system); 
    attribute def Load_Deadline :> AADL::Property, Time;

    // Load_Time: Time_Range applies to (process, system); 
    attribute def Load_Time :> AADL::Property, Time_Range;

    // Period: inherit Time applies to (thread, thread group, process, system, device, 
    //                                  virtual processor, bus, virtual bus); 
	attribute def Period :> AADL::Property, Time;

    // Processor_Capacity: aadlreal Processor_Speed_Units 
    //    applies to (processor, virtual processor, system); 
    attribute def Processor_Capacity :> AADL::Property, Processor_Speed_Units;

    // Recover_Deadline: Time applies to (thread); 
    attribute def Recover_Deadline :> AADL::Property, Time;

    // Recover_Execution_Time: Time_Range applies to (thread);
    attribute def Recover_Execution_Time :> AADL::Property, Time_Range;

    // Reference_Time : inherit reference (device, processor, bus, system, abstract)
    //    applies to (device, processor, bus, system, memory, virtual bus, virtual processor);
	// Note: this property is likely to be supported using SysMLv2 clocks;

    // Startup_Deadline: Time applies to (processor, virtual processor, process, system); 
    attribute def Startup_Deadline :> AADL::Property, Time;

    // Startup_Execution_Time: Time_Range applies to (virtual processor, processor, process, system); 
 	attribute def Startup_Execution_Time :> AADL::Property, Time_Range;

    // Clock_Jitter: Time applies to (processor, system); 
    attribute def Clock_Jitter :> AADL::Property, Time;

    // Clock_Period: Time applies to (processor, system); 
    attribute def Clock_Period :> AADL::Property, Time;

    // Clock_Period_Range: Time_Range applies to (processor, system); 
    attribute def Clock_Period_Range :> AADL::Property, Time_Range;

    // Process_Swap_Execution_Time: Time_Range applies to (processor, virtual processor); 
    attribute def Process_Swap_Execution_Time :> AADL::Property, Time_Range;

	// Reference_Processor: inherit classifier (processor)
	//    applies to (subprogram, subprogram group, thread, thread group, process, device, system);
    //  TBD, not used in AADL

    // Reference_Time: inherit reference (processor, device, bus, system, abstract) 
    //    applies to (processor, device, memory, bus, virtual processor, virtual bus, system); 

    // Scheduler_Quantum : inherit Time applies to (processor);
    attribute def Scheduler_Quantum :> AADL::Property, Time;

    // Thread_Swap_Execution_Time: Time_Range applies to (processor, virtual processor, system); 
  	attribute def Thread_Swap_Execution_Time :> AADL::Property, Time_Range;

    // Frame_Period: Time applies to (processor, virtual processor);
    attribute def Frame_Period :> AADL::Property, Time;

    // Slot_Time: Time applies to (processor, virtual processor); 
    attribute def Slot_Time :> AADL::Property, Time;
  
}