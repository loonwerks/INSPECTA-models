/* SysML v2 Domain Library for AADL:
 * references:
 * SAE AS-5506D AADL appendix A8
 * OMG SysML v2 */

standard library package AADL_Project {

	public import SI::*; // Propagate definition of unit types, e.g. [s], to users of this package
    private import ScalarValues::Real;
    private import Quantities::*;
    private import MeasurementReferences::*;
    private import ISQBase::*;

	// Supported_Active_Thread_Handling_Protocols: type enumeration (abort, <project-specified>);
	enum def Supported_Active_Thread_Handling_Protocols {
		enum Abort;
	}

	// Supported_Connection_Patterns: type enumeration
	//    (One_To_One, All_To_All, One_To_All, All_To_One, Next, Previous, Cyclic_Next, Cyclic_Previous);
	enum def Supported_Connection_Patterns {
		enum One_To_One; enum All_To_All; enum One_To_All; enum All_To_One; enum Next;
		enum Previous; enum Cyclic_Next; enum Cyclic_Previous; enum Odd_To_Odd;
		enum Even_To_Even; enum Next_Next; enum Cyclic_Next_Next; enum Previous_Previous;
		enum Cyclic_Previous_Previous;
	}

    // Supported_Concurrency_Control_Protocols: type enumeration
	//    (None_Specified, Interrupt_Masking, Maximum_Priority, Priority_Inheritance,
    //     Priority_Ceiling, Spin_Lock, Semaphore,  <project-specified>);
	enum def Supported_Concurrency_Control_Protocols {
		enum None_Specified;
		enum Interrupt_Masking;
        enum Maximum_Priority;
        enum Priority_Inheritance;
        enum Priority_Ceiling;
        enum Spin_Lock;
        enum Semaphore;
        enum Protected_Access;
	}
	alias None_Specified for Supported_Concurrency_Control_Protocols::None_Specified;
	alias Interrupt_Masking for Supported_Concurrency_Control_Protocols::Interrupt_Masking;
	alias Maximum_Priority for Supported_Concurrency_Control_Protocols::Maximum_Priority;
	alias Priority_Inheritance for Supported_Concurrency_Control_Protocols::Priority_Inheritance;
	alias Priority_Inheritance_Protocol for Supported_Concurrency_Control_Protocols::Priority_Inheritance;
	alias PIP for Supported_Concurrency_Control_Protocols::Priority_Inheritance;
	alias Priority_Ceiling for Supported_Concurrency_Control_Protocols::Priority_Ceiling;
	alias Priority_Ceiling_Protocol for Supported_Concurrency_Control_Protocols::Priority_Ceiling;
	alias PCP for Supported_Concurrency_Control_Protocols::Priority_Ceiling;
	alias Spin_Lock for Supported_Concurrency_Control_Protocols::Spin_Lock;
	alias Semaphore for Supported_Concurrency_Control_Protocols::Semaphore;
	alias Protected_Access for Supported_Concurrency_Control_Protocols::Protected_Access;

    // Supported_Dispatch_Protocols: type enumeration
    //    (Periodic, Sporadic, Aperiodic, Timed, Hybrid, Background, <project-specified>);
	enum def Supported_Dispatch_Protocols {
		enum Periodic;
		enum Sporadic;
		enum Aperiodic;
		enum Timed;
		enum Hybrid;
		enum Background;
	}
	alias Periodic for Supported_Dispatch_Protocols::Periodic;
	alias Sporadic for Supported_Dispatch_Protocols::Sporadic;
	alias Aperiodic for Supported_Dispatch_Protocols::Aperiodic;
	alias Timed for Supported_Dispatch_Protocols::Timed;
	alias Hybrid for Supported_Dispatch_Protocols::Hybrid;
	alias Background for Supported_Dispatch_Protocols::Background;

    // Supported_Queue_Processing_Protocols: type enumeration (Fifo,  <project-specified>);
	enum def Supported_Queue_Processing_Protocols {
		enum FIFO;
	}

    // Supported_Hardware_Source_Languages: type enumeration (VHDL, <project-specified>);
    enum def Supported_Hardware_Source_Languages {
		enum VHDL;
	}

	// Supported_Connection_QoS : type enumeration
	//    (GuaranteedDelivery, OrderedDelivery, SecureDelivery, <project specific>);
 	enum def Supported_Connection_QoS {
 		enum GuaranteedDelivery;
		enum OrderedDelivery;
		enum SecureDelivery;
 	}

    // Supported_Scheduling_Protocols: type enumeration
	//    (FixedTimeline, Cooperative, RMS, EDF, SporadicServer, SlackServer, ARINC653, <project-specified>);
	enum def Supported_Scheduling_Protocols {
		enum No_Scheduling_Protocol;
		enum Round_Robin_Protocol;
		enum Rate_Monotonic_Protocol;
		enum Deadline_Monotonic_Protocol;
		enum Earliest_Deadline_First_Protocol;
		enum Least_Laxity_First_Protocol;
		enum POSIX_1003_Highest_Priority_First_Protocol;
		enum ARINC653;
	}
	alias No_Scheduling_Protocol for Supported_Scheduling_Protocols::No_Scheduling_Protocol;
	alias None for Supported_Scheduling_Protocols::No_Scheduling_Protocol;
	alias Round_Robin_Protocol for Supported_Scheduling_Protocols::Round_Robin_Protocol;
	alias RR for Supported_Scheduling_Protocols::Round_Robin_Protocol;
	alias Rate_Monotonic_Protocol for Supported_Scheduling_Protocols::Rate_Monotonic_Protocol;
	alias RM for Supported_Scheduling_Protocols::Rate_Monotonic_Protocol;
	alias RMS for Supported_Scheduling_Protocols::Rate_Monotonic_Protocol;
	alias Deadline_Monotonic_Protocol for Supported_Scheduling_Protocols::Deadline_Monotonic_Protocol;
	alias DM for Supported_Scheduling_Protocols::Deadline_Monotonic_Protocol;
	alias Earliest_Deadline_First_Protocol for Supported_Scheduling_Protocols::Earliest_Deadline_First_Protocol;
	alias EDF for Supported_Scheduling_Protocols::Earliest_Deadline_First_Protocol;
	alias Least_Laxity_First_Protocol for Supported_Scheduling_Protocols::Least_Laxity_First_Protocol;
	alias LLF for Supported_Scheduling_Protocols::Least_Laxity_First_Protocol;
	alias POSIX_1003_Highest_Priority_First_Protocol for Supported_Scheduling_Protocols::POSIX_1003_Highest_Priority_First_Protocol;
	alias POSIX_1003_HIGHEST_PRIORITY_FIRST_PROTOCOL for Supported_Scheduling_Protocols::POSIX_1003_Highest_Priority_First_Protocol;
	alias HPF for Supported_Scheduling_Protocols::POSIX_1003_Highest_Priority_First_Protocol;
	alias ARINC653 for Supported_Scheduling_Protocols::ARINC653;

    // Supported_Source_Languages: type enumeration (Ada95, Ada2005, C, Simulink_6_5, <project-specified>);
 	enum def Supported_Source_Languages {
 		enum Ada95;
		enum Ada2005;
		enum C;
		enum Java;
		enum Simulink_6_5;
		enum SCADE;
	}

    // Supported_Distributions: type enumeration (Fixed, Poisson, <project-specified>);
	enum def Supported_Distributions {
		enum Fixed;
		enum Poisson;
	}

	/*
 	 Note: The following properties are not translated. They adjust AADL typing and cannot be supported as-is in SysMLv2

 	Supported_Classifier_Substitutions: type enumeration (Classifier_Match, Type_Extension, Signature_Match);

    Supported_Classifier_Equivalence_Matches: constant list of list of classifier (data, bus, subprogram,
        subprogram group, feature group) => ();

    Supported_Classifier_Subset_Matches: constant list of list of classifier (data, bus, subprogram,
        subprogram group) => ();

    Supported_Type_Conversions: constant list of list of classifier (data, bus, subprogram, subprogram group) => ();

    Supported_Classifier_Complement_Matches: constant list of list of classifier (feature group) => ();

 	*/

	attribute def Time :> ISQ::DurationValue {
		attribute :>> num: ScalarValues::Integer;
		//  Many properties assume a positive value, e.g. Period
	}

	attribute def Time_Range { // XXX native SysMLv2 constructs for range types?
		attribute minimum : Time;
		attribute maximum : Time;
		attribute delta : Time;
		// add constraints that minimum <= maximum
	}

	attribute def Size :> StorageCapacityValue;

	// Size_Range: type range of Size;

	attribute def Data_Rate_Units :> BinaryDigitRateValue ;
	// Note: the original AADL definition uses bits per second as base unit

	// XXX Could be part of a generic library
	attribute def ProcessorInstructionstRate :> ScalarQuantityValue {
        attribute :>> num: Real;
        attribute :>> mRef: ProcessorInstructions[1];
    }

    attribute def ProcessorInstructions :> DerivedUnit {
        private attribute durationPF: QuantityPowerFactor[1] { :>> quantity = isq.T; :>> exponent = -1; }
        attribute :>> quantityDimension { :>> quantityPowerFactors = durationPF; }
    }

	attribute def Processor_Speed_Units :> ProcessorInstructionstRate;
	 
	package CASE_Scheduling {
		// is a warning in SW.sysml if specializes Number.. Why?
		attribute def Domain :> AADL::Property, ScalarValues::Natural; 
	}
	
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
}