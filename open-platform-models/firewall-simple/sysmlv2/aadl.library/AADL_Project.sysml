library package AADL_Project {
	
	attribute def Time :> ISQ::DurationValue {
		attribute :>> num: ScalarValues::Integer;
	}
	
	attribute def Time_Range {
		attribute minimum : Time;
		attribute maximum : Time;
		attribute delta : Time;
	}
	
    package Time_Units {
		attribute <ps> picosecond : SI::DurationUnit {
			:>> unitConversion: MeasurementReferences::ConversionByPrefix {
				:>> prefix = SI::pico;
				:>> referenceUnit = SI::s;
			}
		}
		
		attribute <ns> nanosecond : SI::DurationUnit {
			:>> unitConversion: MeasurementReferences::ConversionByPrefix {
				:>> prefix = SI::nano;
				:>> referenceUnit = SI::s;
			}
		}
		
		attribute <us> microsecond : SI::DurationUnit {
			:>> unitConversion: MeasurementReferences::ConversionByPrefix {
				:>> prefix = SI::micro;
				:>> referenceUnit = SI::s;
			}
		}
		
		attribute <ms> millisecond : SI::DurationUnit {
			:>> unitConversion: MeasurementReferences::ConversionByPrefix {
				:>> prefix = SI::milli;
				:>> referenceUnit = SI::s;
			}
		}
	}
	
	// Defines the type for Dispatch_Protocol as an enum with two potential values
	enum def Supported_Dispatch_Protocols {
		doc
		/* The <b>Supported_Dispatch_Protocols</b> property enumeration type specifies the set of thread dispatch protocols
		 * that are supported.<p> 
		 * <b>Periodic</b> represents periodic dispatch of threads with deadlines. <b>Sporadic</b> represents event-triggered
		 * dispatching of threads with soft deadlines. <b>Aperiodic</b> represents event-triggered dispatch of threads with
		 * hard deadlines. <b>Timed</b> represents threads that are dispatched after a given time unless they are dispatched
		 * by arrival of an event or event data. <b>Hybrid</b> represents threads that are dispatched by both an event or
		 * event data arrival and periodically. <b>Background</b> represents threads that are dispatched once and execute 
		 * until completion.  The <b>Period</b> is required for <b>Periodic, Timed, and Hybrid</b> threads.
		 */
		enum Sporadic;
		enum Periodic;
		enum Aperiodic;
		enum Timed;
		enum Hybrid;
		enum Background;
	}
}