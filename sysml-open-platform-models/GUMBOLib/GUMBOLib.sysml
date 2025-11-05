package GUMBOLib {
	private import AADL::*;
	private import SysML;
	private import Metaobjects::SemanticMetadata;
 
 	
	
	metadata def <gumbodefs> GUMBODefinitions {
		:> annotatedElement : SysML::Usage;
		// would like to somehow restrict this to a collection of calc definitions
	}
	
	metadata def <invariant> GUMBOInvariantBlock {
		:> annotatedElement : SysML::Usage;
	}
	
	metadata def <gumbocompute> GUMBOComputeBlock;
	
	metadata def <guarantee> GUMBOGuaranteeConstraint {
		:> annotatedElement: SysML::ConstraintUsage;
	}
	
	metadata def <gumbostate> GUMBOState;
	
	attribute <u16>  : StorageCapacityUnit;
	attribute <u8>  : StorageCapacityUnit;	
	
	calc def HasEvent {
		in portName : Feature;
		return : ScalarValues::Boolean;
	}
	
	calc def NoSend {
		in portName : Feature;
		return : ScalarValues::Boolean;
	}
	
	// difference between this and a for loop? Probably stops on first true
	constraint def Exists {
		in lo;
		in hi;
		in f;
	}
}