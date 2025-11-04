/* SysML v2 Domain Library for AADL:
 * references:
 * SAE AS-5506D AADL appendix A1
 * OMG SysML v2 */

 standard library package Deployment_Properties {

	private import AADL_Project::*;

	/*
 	 * General considerations: allowed_*, required_*, provided_* properties should be requirements. 
 	 * Do we want to create specific requirement definitions for these?
 	 */

    // Allowed_Processor_Binding_Class: inherit list of classifier (processor, virtual processor, system, device, abstract) 
    //    applies to (thread, thread group, process, system, virtual processor, device);

    // Allowed_Processor_Binding: inherit list of reference (processor, virtual processor, system, device, abstract) 
    //    applies to (thread, thread group, process, system, virtual processor, device);

    // Actual_Processor_Binding: inherit list of reference (processor, virtual processor, system, device, abstract) 
    //    applies to (thread, thread group, process, system, virtual processor, device);
	allocation def Actual_Processor_Binding {
		end part sw : AADL::Component; // XXX refine to concrete categories?
		end part hw : AADL::Component;
		}

    // Allowed_Memory_Binding_Class: inherit list of classifier (memory, system, processor, virtual processor, abstract) 
    //    applies to (thread, thread group, process, system, device, data, data port, event data port, 
    //	               subprogram, subprogram group, processor, virtual processor);

    // Allowed_Memory_Binding: inherit list of reference (memory, system, processor, virtual processor, abstract)
    //    applies to (memory, thread, thread group, process, system, device, data, data port, event data port,
    //                 subprogram, subprogram group, processor, virtual processor);

    //  Actual_Memory_Binding: inherit list of reference (memory, system, processor, virtual processor, abstract)
    //     applies to (memory, thread, thread group, process, system, processor, virtual processor, device, data,
    //                  data port, event data port, subprogram, subprogram group);
	allocation def Actual_Memory_Binding {
		end part sw : AADL::Component;
		end part hw : AADL::Component;
	}

    // Allowed_Connection_Binding_Class: inherit list of classifier 
    //    (processor, virtual processor, bus, virtual bus, device, memory, system, abstract) 
    //    applies to (feature, connection, thread, thread group, process, system, virtual bus);

    // Allowed_Connection_Binding: inherit list of reference 
    //    (processor, virtual processor, bus, virtual bus, device, memory, system, abstract) 
    //    applies to (feature, connection, thread, thread group, process, system, virtual bus);

    // Actual_Connection_Binding: inherit list of reference 
    //    (processor, virtual processor, bus, virtual bus, device, memory, system, abstract) 
    //    applies to (feature, connection, thread, thread group, process, system, virtual bus);
	allocation def Actual_Connection_Binding {
		end part cnx : AADL::Component;
		end part bus : AADL::Component;
		}

    // Actual_Function_Binding: inherit list of reference 
    //    (processor, virtual processor, bus, virtual bus, device, memory, system, process, thread, feature, abstract) 
    //    applies to (subprogram, thread, thread group, process, system, abstract, feature);
 	// AADLv2 Actual_Function_Binding is a native SysMLv2 allocate

	// Allowed_Subprogram_Call: list of reference (subprogram) applies to (subprogram access);

    // Actual_Subprogram_Call: reference (subprogram) applies to (subprogram access);

    // Allowed_Subprogram_Call_Binding: list of reference (bus, processor, device) 
    //    applies to (subprogram, thread, thread group, process, system);

    // Actual_Subprogram_Call_Binding: list of reference (bus, processor, memory, device) applies to (subprogram);

    // Provided_Virtual_Bus_Class: inherit list of classifier (virtual bus) 
    //    applies to (bus, virtual bus, processor, virtual processor, device, memory, system);

    // Required_Virtual_Bus_Class: inherit list of classifier (virtual bus) 
    //    applies to (virtual bus, connection, port, thread, thread group, process, system, device);

    // Provided_Connection_Quality_Of_Service: inherit list of Supported_Connection_QoS 
    //    applies to (bus, virtual bus, processor, virtual processor, system, device, memory);

    // Required_Connection_Quality_Of_Service: inherit list of Supported_Connection_QoS 
    //    applies to (port, connection, virtual bus, thread, thread group, process, system, device);

    // Not_Collocated: record (
    //    Targets: list of reference (data, thread, process, system, connection);
    //    Location: classifier (processor, memory, bus, system);) applies to (process, system);
    // Collocated: record (
    //    Targets: list of reference (data, thread, process, system, connection);
    //    Location: classifier (processor, memory, bus, system);) applies to (process, system);
    // XXX do we need those? their use case is for automating the allocation of elements to some locations

    // Allowed_Connection_Type: list of enumeration (Sampled_Data_Connection, Immediate_Data_Connection,
    //    Delayed_Data_Connection, Port_Connection, Data_Access_Connection, Subprogram_Access_Connection) 
    //    applies to (bus, device);

    // Allowed_Dispatch_Protocol: list of Supported_Dispatch_Protocols applies to (processor, virtual processor);

    // Allowed_Period: list of Time_Range applies to (processor, system, virtual processor);

    // Allowed_Physical_Access_Class: list of classifier (device, processor, memory, bus) applies to (bus);

    // Allowed_Physical_Access: list of reference (device, processor, memory, bus) applies to (bus);

    // Memory_Protocol: enumeration (execute_only, read_only, write_only, read_write) => read_write applies to (memory);
	enum def Supported_Memory_Protocols {
	    enum Execute_Only; 
	    enum Read_Only; 
	    enum Write_Only; 
	    enum Read_Write;
	 }
	alias Execute_Only for Supported_Memory_Protocols::Execute_Only;
	alias Read_Only for Supported_Memory_Protocols::Read_Only;
	alias Write_Only for Supported_Memory_Protocols::Write_Only;
	alias Read_Write for Supported_Memory_Protocols::Read_Write;
	attribute def Memory_Protocol :> AADL::Property, Supported_Memory_Protocols;
    
    // Runtime_Protection_Support: aadlboolean applies to (processor, virtual processor);
	attribute def Runtime_Protection_Support :> AADL::Property, ScalarValues::Boolean;

    // Scheduling_Protocol: inherit list of Supported_Scheduling_Protocols 
    //    applies to (virtual processor, processor, system);
	attribute def Scheduling_Protocol :> AADL::Property, Supported_Scheduling_Protocols;

    // Preemptive_Scheduler: aadlboolean applies to (processor);
	attribute def Preemptive_Scheduler :> AADL::Property, ScalarValues::Boolean; 

    // Thread_Limit: aadlinteger 0 .. Max_Thread_Limit applies to (processor, virtual processor);
    attribute def Thread_Limit :> AADL::Property, ScalarValues::Positive;
    // Note: this definition seems enough, no need for a project-wide limit

    // Priority_Map: list of Priority_Mapping applies to (processor);

    // Priority_Mapping: type record (
    //    Aadl_Priority: aadlinteger;
    //    RTOS_Priority: aadlinteger;);

    // Priority_Range: range of aadlinteger applies to (processor, virtual processor);
	// XXX should be a constraint, TBD
	
}
