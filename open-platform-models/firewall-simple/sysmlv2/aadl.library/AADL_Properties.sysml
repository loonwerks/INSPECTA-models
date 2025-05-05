//Stores definitions of AADL properties
library package AADL_Properties {

	public import ISQ::*;
	public import SIPrefixes::*;
	public import AADL_Data_Model::*;
	public import AADL_Memory_Properties::*;
	public import AADL_Thread_Properties::*;
	public import AADL_Timing_Properties::*;


	// Attach the "Properties" metadata to a part to assign AADL-like properties
	metadata def Properties :> AADL_Thread_Properties, 
	                           AADL_Timing_Properties,
	                           AADL_Memory_Properties,
	                           Data_Model {}
	

	// These aliases allow the enumerated values to be referenced more easily
	alias Sporadic for Supported_Dispatch_Protocols::Sporadic;
	alias Periodic for Supported_Dispatch_Protocols::Periodic;

}