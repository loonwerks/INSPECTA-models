package Platform {
	private import HAMR_AADL::*;
	private import SW::*;
	private import CASE_Scheduling::*;
	
	part def Proc :> Processor {
		:>> Frame_Period = 1850 [ms];
		:>> Clock_Period = 2 [ms];
		attribute Max_Domain = 6;
		attribute Schedule_Source_Text = "domain_schedule.c";
	}
	
	part ZCU102 : System {
		part proc : Proc;
		part seL4 : SW::seL4;
		
		attribute Actual_Processor_Binding; // (reference (proc)) applies to seL4
	}
}