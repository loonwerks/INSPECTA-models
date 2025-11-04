/* SysML v2 Domain Library for AADL:
 * references:
 * SAE AS-5506D AADL appendix A5
 * OMG SysML v2 */

standard library package Memory_Properties {

	private import ScalarValues::*;
	private import AADL_Project::*;

	// Access_Right: Access_Rights => read_write applies to (data, bus, data access, bus access);
 	enum def Access_Rights {
 		Read_Only;
 		Write_Only;
 		Read_Write;
 		By_Method;
 	}
 	alias Read_Only for Access_Right::Read_Only;
 	alias Write_Only for Access_Right::Write_Only;
 	alias Read_Write for Access_Right::Read_Write;
 	attribute def Access_Right :> AADL::Property, Access_Rights;

    //  Access_Time: record (
    //    First: IO_Time_Spec;
    //    Last: IO_Time_Spec;) => [First => [Time => Start; Offset => 0ns .. 0ns;];
    //    Last => [Time => Completion; Offset => 0ns .. 0ns;];] applies to (data access, data);

    // Allowed_Message_Size: Size_Range applies to (bus);

    // Assign_Time: record (
    //    Fixed: Time_Range;
    //    PerByte: Time_Range;) applies to (processor);
 	attribute def Assign_Time :> AADL::Property {
  		attribute Fixed : Time_Range;
  		attribute PerByte : Time_Range;
  	}

  	// Base_Address: aadlinteger 0 .. Max_Base_Address applies to (memory, data, data access, port);
	attribute def Base_Address :> Integer; // TBD range 0 .. Max_Base_Address

    // Device_Register_Address: aadlinteger applies to (port, feature group);
	attribute def Device_Register_Address :> Integer;

    //	Read_Time: record (
    //    Fixed: Time_Range;
    //    PerByte: Time_Range;) applies to (memory);
    attribute def Read_Time :> AADL::Property {
  		attribute Fixed : Time_Range;
  		attribute PerByte : Time_Range;
  	}

	// Code_Size: Size applies to (data, thread, thread group, process, system, subprogram, processor, virtual processor,
    //                             virtual bus, device);
	attribute def Code_Size :> AADL::Property, Size;

	// Data_Size: Size applies to (data, feature, subprogram, thread, thread group, process, system, processor,
  	//                             virtual processor, virtual bus, bus, device);
	attribute def Data_Size :> AADL::Property, Size;

    // Heap_Size: Size applies to (thread, subprogram, thread group, process, system, processor, virtual processor,
    //                             device);
	attribute def Heap_Size :> AADL::Property, Size;

    // Stack_Size: Size applies to (thread, subprogram, processor, virtual processor, device);
	attribute def Stack_Size :> AADL::Property, Size;

	// Memory_Size: Size applies to (memory, system, processor, virtual processor);
	attribute def Memory_Size :> AADL::Property, Size;

   	// Word_Size: Size => 8 bits applies to (memory);
	attribute def Word_Size :> AADL::Property, Size;

    // The Word_Space specifies the interval between successive addresses used for successive words of memory.
    // A value greater than 1 means the addresses used to access words are not contiguous integers.
    // The default value is 1.
    // Word_Space: aadlinteger 1 .. Max_Word_Space => 1 applies to (memory);

    // The Write_Time property specifies a time unit value used in a linear estimation of the execution time
    // required to write a block of bytes to memory. The time required is (Number_of_Bytes * PerByte) + Fixed
    // Write_Time: record (
    //    Fixed: Time_Range;
    //    PerByte: Time_Range;) applies to (memory);

}
