/* SysML v2 Domain Library for AADL:
 * references:
 * SAE AS-5506D AADL chapters 4 to 10
 * OMG SysML v2 release 2025-02 */

standard library package AADL {

	// Give visibility to all AADL standard property sets enum values with a single import of AADL::*
	public import AADL_Project::*;
	public import Deployment_Properties::*;
	public import Thread_Properties::*;
	public import Timing_Properties::*;
	public import Communication_Properties::*;
	public import Memory_Properties::*;
	public import Programming_Properties::*;
	public import Data_Model::*;
	public import Behavior_Properties::*;

	// SysMLv2 libraries
	private import Actions::*;
	private import Flows::*;
	private import Interfaces::BinaryInterface;

	enum def ComponentCategory {
		enum Abstract;
		enum Bus;
		enum Data;
		enum Device;
		enum Memory;
		enum Process;
		enum Processor;
		enum System;
		enum Subprogram;
		enum SubprogramGroup;
		enum Thread;
		enum ThreadGroup;
		enum VirtualProcessor;
		enum VirtualBus;
	}

	abstract part def Component {
		attribute category : ComponentCategory;
 	}

	// PDI004 Wonder if the following would avoid copying all attributes again
	// part def Abstract :> Bus, Data, Device, Memory, Process, Processor, System, Subprogram, SubprogramGroup, Thread, ThreadGroup, VirtualProcessor, VirtualBus
	// JH: To be discussed.

	part def Abstract :> Component {
		doc AADL_Doc /* Abstract component, see AADLv2.3 (4.6) */

		attribute :>> category = ComponentCategory::Abstract;

		attribute Compute_Execution_Time : Timing_Properties::Compute_Execution_Time;
		attribute Period : Timing_Properties::Period;
		attribute Dispatch_Protocol : Thread_Properties::Dispatch_Protocol;
		attribute Priority : Thread_Properties::Priority;
	}

	part def Bus :> Component {
		doc AADL_Doc /* Bus component, see AADLv2.3 (6.4) */

		attribute :>> category = ComponentCategory::Bus;

		attribute Access_Right : Memory_Properties::Access_Right;
		attribute Period : Timing_Properties::Period;
		attribute Transmission_Time : Communication_Properties::Transmission_Time;
		attribute Transmission_Type : Communication_Properties::Transmission_Type;
	}

	// Rationale for Data (after discussion with BRL and Pierre)
	// in AADL,
	// 1. a Data has an structural meaning: it uses memory resources, that
	// in turns "cost" CPU time, bandwidth, etc. This is typically inferred from attributes
	// 2. A data also has semantics that can be leveraged for computation, hence an item/attribute
	// Remember that a part usage *is * an item usage, so it is perfectly fine to use Data for data ports.
	part def Data :> Component {
		doc AADL_Doc /* Data component, see AADLv2.3 (5.1) */

		attribute :>> category = ComponentCategory::Data;

		attribute Access_Right : Memory_Properties::Access_Right;
		attribute Base_Address : Memory_Properties::Base_Address;
		attribute Code_Size : Memory_Properties::Code_Size;
		attribute Concurrency_Control_Protocol : Thread_Properties::Concurrency_Control_Protocol;
	    attribute Data_Size : Memory_Properties::Data_Size;
		attribute Priority : Thread_Properties::Priority;
		attribute Period : Timing_Properties::Period;
		attribute Source_Language : Programming_Properties::Source_Language;
		attribute Source_Name : Programming_Properties::Source_Name;
		attribute Source_Text : Programming_Properties::Source_Text;
        attribute Type_Source_Name : Programming_Properties::Type_Source_Name;

		attribute value: Base::DataValue;
	}

	part def Device :> Component {
		doc AADL_Doc /* Device component, see AADLv2.3 (6.6) */

		attribute :>> category = ComponentCategory::Device;

		attribute Compute_Execution_Time : Timing_Properties::Compute_Execution_Time;
		attribute Dispatch_Protocol : Thread_Properties::Dispatch_Protocol;
		attribute Period : Timing_Properties::Period;
	}

	part def Memory :> Component {
		doc AADL_Doc /* Memory component, see AADLv2.3 (6.3) */

		attribute :>> category = ComponentCategory::Memory;

		attribute Base_Address : Memory_Properties::Base_Address;
		attribute Memory_Protocol : Deployment_Properties::Memory_Protocol default Read_Write;
		attribute Memory_Size : Memory_Properties::Memory_Size;
		attribute Word_Size : Memory_Properties::Word_Size;
	}

	part def Process :> Component {
		doc AADL_Doc /* Process component, see AADLv2.3 (5.6) */
		attribute :>> category = ComponentCategory::Process;

		attribute Period : Timing_Properties::Period;
	}

	part def Processor :> Component {
		doc AADL_Doc /* Processor component, see AADLv2.3 (6.1) */
		attribute :>> category = ComponentCategory::Processor;

		attribute Preemptive_Scheduler : Deployment_Properties::Preemptive_Scheduler;
		attribute Scheduler_Quantum : Timing_Properties::Scheduler_Quantum;
		attribute Scheduling_Protocol : Deployment_Properties::Scheduling_Protocol;
		attribute Thread_Limit : Deployment_Properties::Thread_Limit;
	}

	part def System :> Component {
		doc AADL_Doc /* System component, see AADLv2.3 (7.1) */
		attribute :>> category = ComponentCategory::System;

		attribute Period : Timing_Properties::Period;
	}

	part def Subprogram :> Component {
		doc AADL_Doc /* Subprogram component, see AADLv2.3 (5.2) */
		attribute :>> category = ComponentCategory::Subprogram;

		attribute Client_Subprogram_Execution_Time : Timing_Properties::Client_Subprogram_Execution_Time;
		attribute Code_Size : Memory_Properties::Code_Size;
		attribute Compute_Execution_Time : Timing_Properties::Compute_Execution_Time;
		attribute Data_Size : Memory_Properties::Data_Size;
		attribute Heap_Size : Memory_Properties::Heap_Size;
		attribute Source_Language : Programming_Properties::Source_Language;
		attribute Source_Name : Programming_Properties::Source_Name;
		attribute Source_Text : Programming_Properties::Source_Text;
		attribute Stack_Size : Memory_Properties::Stack_Size;
        attribute Type_Source_Name : Programming_Properties::Type_Source_Name;
	}

	part def SubprogramGroup :> Component {
		doc AADL_Doc /* Subprogram Group component, see AADLv2.3 (5.3) */
		attribute :>> category = ComponentCategory::SubprogramGroup;
	}

	part def Thread :> Component {
		doc AADL_Doc /* Thread component, see AADLv2.3 (5.4) */

		attribute :>> category = ComponentCategory::Thread;

        attribute Activate_Deadline : Timing_Properties::Activate_Deadline;
        attribute Activate_Execution_Time : Timing_Properties::Activate_Execution_Time;
		attribute Active_Thread_Handling_Protocol : Thread_Properties::Active_Thread_Handling_Protocol;
		attribute Active_Thread_Queue_Handling_Protocol : Thread_Properties::Active_Thread_Queue_Handling_Protocol;
		attribute Code_Size : Memory_Properties::Code_Size;
        attribute Compute_Deadline : Timing_Properties::Compute_Deadline;
		attribute Compute_Execution_Time : Timing_Properties::Compute_Execution_Time;
		attribute Criticality : Thread_Properties::Criticality;
		attribute Data_Size : Memory_Properties::Data_Size;
		attribute Deactivate_Deadline : Timing_Properties::Deactivate_Deadline;
		attribute Deactivate_Execution_Time : Timing_Properties::Deactivate_Execution_Time;
		attribute Deactivation_Policy : Thread_Properties::Deactivation_Policy default Inactive;
		attribute Deadline : Timing_Properties::Deadline;
		attribute Dequeue_Protocol : Thread_Properties::Dequeue_Protocol;
		attribute Dispatch_Able : Thread_Properties::Dispatch_Able;
		attribute Dispatch_Protocol : Thread_Properties::Dispatch_Protocol;
		attribute Dispatch_Jitter : Timing_Properties::Dispatch_Jitter;
		attribute Dispatch_Offset : Timing_Properties::Dispatch_Offset;
		attribute Execution_Time : Timing_Properties::Execution_Time;
        attribute Finalize_Deadline : Timing_Properties::Finalize_Deadline;
		attribute Finalize_Execution_Time : Timing_Properties::Finalize_Execution_Time;
		attribute First_Dispatch_Time : Timing_Properties::First_Dispatch_Time;
		attribute Heap_Size : Memory_Properties::Heap_Size;
		attribute Initialize_Deadline : Timing_Properties::Initialize_Deadline;
        attribute Initialize_Execution_Time : Timing_Properties::Initialize_Execution_Time;
		attribute Period : Timing_Properties::Period;
		attribute POSIX_Scheduling_Policy : Thread_Properties::POSIX_Scheduling_Policy;
		attribute Priority : Thread_Properties::Priority
		{ doc prout /* lfskhfslafhaslkfh */}


		attribute Recover_Deadline : Timing_Properties::Recover_Deadline;
		attribute Recover_Execution_Time : Timing_Properties::Recover_Execution_Time;
		attribute Resumption_Policy : Thread_Properties::Resumption_Policy;
		attribute Source_Language : Programming_Properties::Source_Language;
		attribute Source_Text : Programming_Properties::Source_Text;
		attribute Stack_Size : Memory_Properties::Stack_Size;
		attribute Synchronized_Component : Thread_Properties::Synchronized_Component default true;
		attribute Time_Slot : Thread_Properties::Time_Slot;

		// PDI005 Dispatch event port for threads, to be used with AADL BA transition condition: On Dispatch
		// it corresponds to the "enabled" condition for clock-triggered threads
		// JH: TBD when we address dispatching conditition. In particular, AADL has default ports we should consider
		port def Dispatch_t;
		port Dispatch : Dispatch_t;
	}

	part def ThreadGroup :> Component {
		doc AADL_Doc /* Thread Group component, see AADLv2.3 (5.5) */
		attribute :>> category = ComponentCategory::ThreadGroup;

		attribute Period : Timing_Properties::Period;
	}

	part def VirtualBus :> Component {
		doc AADL_Doc /* Virtual Bus component, see AADLv2.3 (6.5) */
		attribute :>> category = ComponentCategory::VirtualBus;

		attribute Period : Timing_Properties::Period;
		attribute Transmission_Type : Communication_Properties::Transmission_Type;
	}

	part def VirtualProcessor :> Component {
		doc AADL_Doc /* Virtual Processor, see AADLv2.3 (6.2) */
		attribute :>> category = ComponentCategory::VirtualProcessor;
		attribute Period : Timing_Properties::Period;
		attribute Runtime_Protection : Thread_Properties::Runtime_Protection default true;
	}

	abstract port def Feature {
		attribute Transmission_Type : Communication_Properties::Transmission_Type;
	}

	port def AbstractFeature :> Feature {
		doc AADL_Doc /* Abstract Feature, see AADLv2.3 (8.1) */
		inout item type : Component;
	}

	port def DataPort :> Feature {
		doc AADL_Doc /* Data Port, see AADLv2.3 (8.3) */
		inout part type : Data;

		attribute Base_Address : Memory_Properties::Base_Address;
	}

	port def EventDataPort :> Feature {
		doc AADL_Doc /* Event Data Port, see AADLv2.3 (8.3) */
		inout part type : Data;

		attribute Base_Address : Memory_Properties::Base_Address;
		attribute Queue_Size : Communication_Properties::Queue_Size default 1;
	}

	port def EventPort :> Feature {
		doc AADL_Doc /* Event Port, see AADLv2.3 (8.3) */

		attribute Base_Address : Memory_Properties::Base_Address;
		attribute Queue_Size : Communication_Properties::Queue_Size default 1;
	}

	port def DataAccess :> Feature {
		doc AADL_Doc /* Data Access, see AADLv2.3 (8.6) */
		inout ref type : Component;

		attribute Access_Right : Memory_Properties::Access_Right;
		attribute Base_Address : Memory_Properties::Base_Address;
	}

	port def BusAccess :> Feature {
		doc AADL_Doc /* Bus Access, see AADLv2.3 (8.7) */
		inout ref type : Component;

		attribute Access_Right : Memory_Properties::Access_Right;
	}

	port def VirtualBusAccess :> Feature {
		doc AADL_Doc /* Virtual Bus Access, see AADLv2.3 (8.8) */
		inout ref type : Component;
	}

	port def SubprogramAccess :> Feature {
		doc AADL_Doc /* Subprogram Access, see AADLv2.3 (8.4) */
		inout ref type : Component;

		attribute Queue_Size : Communication_Properties::Queue_Size default 1;
		// attribute Subprogram_Call_Protocol : Behavior_Properties::Subprogram_Call_Protocol default HSER;
		// JH: This is wrong, this would make the property always visible
	}

	port def SubprogramGroupAccess :> Feature {
		doc AADL_Doc /* Subprogram Group Access, see AADLv2.3 (8.4) */
		inout ref type : Component;

		attribute Queue_Size : Communication_Properties::Queue_Size default 1;
	}

	abstract connection def Connection :> Connections::BinaryConnection {
		attribute Transmission_Type : Communication_Properties::Transmission_Type;
		attribute Timing : Communication_Properties::Timing;
	}

	connection def FeatureConnection :> Connection {
		doc AADL_Doc /* Feature connection, see AADLv2.3 (9.1) */
		end source : Feature;
		end target : Feature;
	}

	interface def PortConnection :> Connection, BinaryInterface {
		doc AADL_Doc /* Port connection, see AADLv2.3 (9.2) */
		end port source : Feature;
        end port target : Feature;

       flow move {
        ref :>> payload : Data;

        end occurrence source : Feature :>> source = BinaryInterface::source;
        end occurrence target : Feature :>> target = BinaryInterface::target;

        // Because a flow usage is an action usage, what is the semantics of this?
        // since the syntax seems pretty obvious ..

        action flow_action {
           accept d : Data via source ;
           accept after 100.0 [ s ];
		   send d via target;
		 }

       }

       bind source = move.source;
       bind target = move.target;
    }

	connection def ParameterConnection :> Connection {
		doc AADL_Doc /* Parameter connection, see AADLv2.3 (9.3) */
	}

	connection def AccessConnection :> Connection {
		doc AADL_Doc /* Access connection, see AADLv2.3 (9.4) */
	}

	connection def FeatureGroupConnection :> Connection {
		doc AADL_Doc /* Feature Group connection, see AADLv2.3 (9.5) */

	}

	abstract attribute def Property;

	part def EventDataPort_Queue {
       inout port EDP : EventDataPort; // This is the public side of the Queue, i.e., see by the component
       inout port EDP_private : EventDataPort;

	   action queue_act {
		 // from the port to the connection
		 accept data : Data via EDP ; then
		 action do_something { in data  := data; }
		 send do_something.data via EDP_private;

		// from the port to the connection
		 accept data : Data via EDP_private ; then
		 action do_something2 { in data  := data; }
		 send do_something.data via EDP;
       }
	}

	port def EventDataPort_beh :> EventDataPort {
		ref queue : EventDataPort_Queue;

		bind this = queue.EDP;
	}

	interface def PortConnection_beh :> Connection, BinaryInterface {
	   end port source : Feature;
       end port target : Feature;

	   // The interface provides an instance of the queue for the source and target

	   part source_queue : EventDataPort_Queue;
	   part target_queue : EventDataPort_Queue;

	   bind source_queue.EDP = source;
	   bind target_queue.EDP = target;

	   flow move {
        ref :>> payload : Data;

        end occurrence source : Feature :>> source = BinaryInterface::source;
        end occurrence target : Feature :>> target = BinaryInterface::target;

        // Because a flow usage is an action usage, what is the semantics of this?
        // since the syntax seems pretty obvious ..

        action flow_action {
           accept d : Data via source ;
           accept after 100.0 [ s ];
		   send d via target;
		 }

       }

       bind source_queue.EDP_private = move.source;
       bind target_queue.EDP_private = move.target;

	}

}
