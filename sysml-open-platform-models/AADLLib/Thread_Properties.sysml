/* SysML v2 Domain Library for AADL:
 * references:
 * SAE AS-5506D AADL appendix A2
 * OMG SysML v2 */

 standard library package Thread_Properties {

	private import AADL_Project::*;

    // Dispatch_Protocol: Supported_Dispatch_Protocols applies to (thread, device, virtual processor);
	attribute def Dispatch_Protocol :> AADL::Property, Supported_Dispatch_Protocols;

	// Dispatch_Trigger: list of reference (port) applies to (device, thread);
	// Dispatch_Trigger_Annex: aadlboolean applies to (thread, device);

	// Dispatch_Able: aadlboolean applies to (thread);
	attribute def Dispatch_Able :> AADL::Property, ScalarValues::Boolean; // do we need a specific aadlboolean?

	// POSIX_Scheduling_Policy: enumeration (SCHED_FIFO, SCHED_RR, SCHED_OTHERS) applies to (thread, thread group);
	enum def Supported_POSIX_Scheduling_Policies {
		enum SCHED_FIFO;
		enum SCHED_RR;
		enum SCHED_OTHERS;
	}
	alias SCHED_FIFO for Supported_POSIX_Scheduling_Policies::SCHED_FIFO;
    attribute def POSIX_Scheduling_Policy :> AADL::Property, Supported_POSIX_Scheduling_Policies;

	attribute def Priority :> AADL::Property, ScalarValues::Integer {
		doc /* The Priority property specifies the priority of the thread that is taken into consideration by some
		scheduling protocols in scheduling the execution order of threads. When applied to data or data
		access, it specifies priority for concurrency control protocols that use priority based conflict
		resolution. A larger value represents a higher priority. */
	}

	// Criticality: aadlinteger applies to (thread, thread group);
	attribute def Criticality :> AADL::Property, ScalarValues::Integer;

    // Time_Slot: list of aadlinteger applies to (thread, thread group, process, virtual processor, system);
	attribute def Time_Slot :> AADL::Property, ScalarValues::Integer;
	// XXX will become a list at usage level

	// Concurrency_Control_Protocol: Supported_Concurrency_Control_Protocols applies to (data, data access);
	attribute def Concurrency_Control_Protocol :> AADL::Property, Supported_Concurrency_Control_Protocols;

	// Urgency: aadlinteger 0 .. Max_Urgency applies to (port, subprogram);
	attribute def Urgency :> AADL::Property, ScalarValues::Integer;

	// Dequeue_Protocol: enumeration (OneItem, MultipleItems, AllItems) => OneItem applies to (event port, event data port);
	enum def Supported_Dequeue_Protocols {
		enum OneItem;
		enum MultipleItems;
		enum AllItems;
	}
	alias OneItem for Supported_Dequeue_Protocols::OneItem;
	alias MultipleItems for Supported_Dequeue_Protocols::MultipleItems;
	alias AllItems for Supported_Dequeue_Protocols::AllItems;
	attribute def Dequeue_Protocol :> AADL::Property, Supported_Dequeue_Protocols;

	// Dequeued_Items: aadlinteger applies to (event port, event data port);
	attribute def Dequeued_Items :> AADL::Property, ScalarValues::Integer;

	// Mode_Transition_Response: enumeration (emergency, planned) applies to (mode transition);
	enum def Supported_Mode_Transition_Responses {
		enum Emergency;
		enum Planned;
	}
	alias Emergency for Supported_Mode_Transition_Responses::Emergency;
	alias Planned for Supported_Mode_Transition_Responses::Planned;
	attribute def Mode_Transition_Response :> AADL::Property, Supported_Mode_Transition_Responses;

	// Resumption_Policy: enumeration (restart, resume) applies to (thread, thread group, process, system, device, processor, memory, bus, system, virtual bus, virtual processor, subprogram);
	enum def Supported_Resumption_Policies {
		enum Restart;
		enum Resume;
	}
	alias Restart for Supported_Resumption_Policies::Restart;
	alias Resume for Supported_Resumption_Policies::Resume;
	attribute def Resumption_Policy :> AADL::Property, Supported_Resumption_Policies;

	// Active_Thread_Handling_Protocol: inherit Supported_Active_Thread_Handling_Protocols => abort applies to (thread, thread group, process, system);
    // Note: default value TBD when we address the thread automata */
	attribute def Active_Thread_Handling_Protocol :> AADL::Property, Supported_Active_Thread_Handling_Protocols;

	// Active_Thread_Queue_Handling_Protocol: inherit enumeration (flush, hold) => flush applies to (thread, thread group, process, system);
	enum def Supported_Active_Thread_Queue_Handling_Protocols {
		enum Flush;
		enum Hold;
	}
	alias Flush for Supported_Active_Thread_Queue_Handling_Protocols::Flush;
	alias Hold for Supported_Active_Thread_Queue_Handling_Protocols::Hold;
	attribute def Active_Thread_Queue_Handling_Protocol :> AADL::Property, Supported_Active_Thread_Queue_Handling_Protocols;

    // Deactivation_Policy: enumeration (inactive, unload) => inactive applies to (thread, process, virtual processor, processor);
 	enum def Supported_Deactivation_Policies {
 		enum Inactive;
 		enum Unload;
 	}
 	alias Inactive for Supported_Deactivation_Policies::Inactive;
 	alias Unload for Supported_Deactivation_Policies::Unload;
 	attribute def Deactivation_Policy :> AADL::Property, Supported_Deactivation_Policies;
 	// attribute Deactivation_Policy : Deactivation_Policy_d default inactive;
 	// XXX the line above does not parse

    // Runtime_Protection: inherit aadlboolean => true applies to (process, system, virtual processor);
	attribute def Runtime_Protection :>  AADL::Property, ScalarValues::Boolean;
	// attribute Runtime_Protection : Runtime_Protection_d default true;

	// Subprogram_Call_Type: enumeration (Synchronous, SemiSynchronous) => Synchronous applies to (subprogram);
    enum def Supported_Subprogram_Call_Types {
    	enum Synchronous;
    	enum SemiSynchronous;
    }
    alias Synchronous for Supported_Subprogram_Call_Types::Synchronous;
    alias SemiSynchronous for Supported_Subprogram_Call_Types::SemiSynchronous;
	attribute def Subprogram_Call_Type :> AADL::Property, Supported_Subprogram_Call_Types;

    // Synchronized_Component: inherit aadlboolean => true applies to (thread, thread group, process, system);
	attribute def Synchronized_Component :> AADL::Property, ScalarValues::Boolean;
}