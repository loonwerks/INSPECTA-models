/* SysML v2 Domain Library for AADL:
 * references:
 * SAE AS-5506D AADL appendix A4
 * OMG SysML v2 */

standard library package Communication_Properties {
	
	private import AADL_Project::*;
	
	private import SI::*;
    private import MeasurementReferences::*;
    private import Time::*;

	// Fan_Out_Policy: enumeration (Broadcast, RoundRobin, Selective, OnDemand) applies to (feature); 
    // XXX: This property influences the semantic of communication and should be addressed using a different pattern

	// Data_Rate: aadlinteger units Data_Rate_Units applies to ( feature, connection, bus, virtual bus, system, processor, virtual processor, device, memory ); 
	attribute def Data_Rate :> AADL::Property, Data_Rate_Units;

	// Connection_Pattern: list of list of Supported_Connection_Patterns applies to (connection, flow specification); 
	// Connection_Set: list of Connection_Pair applies to (connection, flow specification); 
	// Connection_Pair: type record ( src: list of aadlinteger; dst: list of aadlinteger;);  
	// XXX what to do with these?

	// Overflow_Handling_Protocol: enumeration (DropOldest, DropNewest, Error) => DropOldest applies to (event port, event data port, subprogram access); 
	// Queue_Processing_Protocol: Supported_Queue_Processing_Protocols => FIFO applies to (event port, event data port, subprogram access); 
    // XXX: these properties influence the semantic of ports and should be addressed separately

	// Queue_Size: aadlinteger 0 .. Max_Queue_Size => 1 applies to (event port, event data port, subprogram access); 
	attribute def Queue_Size :> AADL::Property, ScalarValues::Integer;

	// Required_Connection : aadlboolean => true applies to (feature); 

	// Timing : enumeration (sampled, immediate, delayed) => sampled applies to (port connection); 
	enum def Supported_Timing_Protocols {
		enum Sampled;
		enum Immediate;
		enum Delayed;
	}
    alias Sampled for Supported_Timing_Protocols::Sampled;
    alias Immediate for Supported_Timing_Protocols::Immediate;
    alias Delayed for Supported_Timing_Protocols::Delayed;
	attribute def Timing :> AADL::Property, Supported_Timing_Protocols;

	enum def Supported_Transmission_Types { 
		enum Push;
		enum Pull;
	}
    alias Push for Supported_Transmission_Types::Push;
    alias Pull for Supported_Transmission_Types::Pull;	
	attribute def Transmission_Type :> AADL::Property, Supported_Transmission_Types;

	// Input_Rate: Rate_Spec => [ Value_Range => 1.0 .. 1.0; Rate_Unit => PerDispatch; Rate_Distribution => Fixed; ] applies to (feature); 
 	// Input_Time: list of IO_Time_Spec => ([ Time => Dispatch; Offset => 0.0 ns .. 0.0 ns;]) applies to (feature); 
	// IO_Time_Spec : type record ( Offset : Time_Range; Time : IO_Reference_Time; ); 
	// IO_Reference_Time : type enumeration (Dispatch, Start, Completion, Deadline, NoIO, Dynamic); 
	// Output_Rate: Rate_Spec => [ Value_Range => 1.0 .. 1.0; Rate_Unit => PerDispatch; Rate_Distribution => Fixed; ] applies to (feature); 
	// Output_Time: list of IO_Time_Spec => ([ Time => Completion; Offset => 0.0 ns .. 0.0 ns;]) applies to (feature); 
 	// Rate_Spec : type record ( Value_Range : range of aadlreal; Rate_Unit : enumeration (PerSecond, PerDispatch); Rate_Distribution : Supported_Distributions; ); 
 	// Subprogram_Call_Rate: Rate_Spec => [ Value_Range => 1.0 .. 1.0; Rate_Unit => PerDispatch; Rate_Distribution => Fixed; ] applies to (subprogram access); 
	// Note: these properties act as requirements, how to map them?
	
	// Transmission_Time: record ( Fixed: Time_Range; PerByte: Time_Range; ) applies to (bus); 
 	attribute def Transmission_Time :> AADL::Property {
 		attribute Fixed : Time_Range;
 		attribute PerByte : Time_Range;
 	} 

	// Actual_Latency: Time_Range applies to (flow, connection, bus, virtual bus, device, processor, virtual processor, system, memory, feature); 
 	attribute def Actual_Latency :> AADL::Property, Time_Range;

	// Latency: Time_Range applies to (flow, connection, bus, virtual bus, device, processor, virtual processor, system, memory, feature); 
	attribute def Latency :> AADL::Property, Time_Range;
	
}