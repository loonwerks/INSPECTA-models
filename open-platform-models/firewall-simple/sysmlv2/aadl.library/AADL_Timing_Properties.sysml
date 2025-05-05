library package AADL_Timing_Properties {
	
	metadata def AADL_Timing_Properties {
		attribute def Compute_Execution_Time :> AADL::Property, AADL_Project::Time_Range;
		attribute def Period :> AADL::Property, AADL_Project::Time;
	}
}