 /* SysML v2 Domain Library for AADL:
 * references:
 * SAE AS-5506D AADL appendix A6
 * OMG SysML v2 */
 
 standard library package Programming_Properties {
 	
 	private import AADL_Project::*;
 	
 	doc /*
 	     * Note: Entrypoints properties are action definitions, see `Threads.sysml` for details
 	     */
 	 
 	// Source_Language: inherit list of Supported_Source_Languages 
 	//     applies to (subprogram, data, thread, thread group, process, system, bus, device,
    //                 processor, virtual processor, virtual bus); 
	attribute def Source_Language :> AADL::Property, Supported_Source_Languages;

    // Source_Name: aadlstring applies to (data, port, subprogram, parameter, virtual bus, virtual processor);
	attribute def Source_Name :> AADL::Property, ScalarValues::String;

	// Source_Text: inherit list of aadlstring 
	//    applies to (data, port, subprogram, thread, thread group, process, system, virtual bus, virtual processor,
    //                memory, bus, device, processor, parameter, feature group, aadl package); */
  	attribute def Source_Text :> AADL::Property, ScalarValues::String;
  
    // Supported_Source_Language: list of Supported_Source_Languages applies to (processor, virtual processor, system);
    attribute def Supported_Source_Language :> AADL::Property, Supported_Source_Languages;
 
    // Type_Source_Name: aadlstring applies to (data, port, subprogram);
	attribute def Type_Source_Name :> AADL::Property, ScalarValues::String;

    // Hardware_Description_Source_Text: inherit list of aadlstring applies to (memory, bus, device, processor, system);
	attribute def Hardware_Description_Source_Text :> AADL::Property, ScalarValues::String;

    // Hardware_Source_Language: Supported_Hardware_Source_Languages applies to (memory, bus, device, processor, system);
	attribute def Hardware_Source_Language :> AADL::Property, Supported_Hardware_Source_Languages;

    // Device_Driver: classifier (abstract implementation) applies to (device);
 	
 }