package CASEHAMRDefs {
	private import AADL;
	package HAMR {
		enum def Array_Size_Kind {
			Fixed;
			Dynamic;
		}
		
		enum def Microkit_Language {
			Rust;
			Slang;
			C;
		}
	}
	
	package CASE_Scheduling {
		// is a warning in SW.sysml if specializes Number.. Why?
		attribute def Domain :> AADL::Property, ScalarValues::Natural; 
	}
}