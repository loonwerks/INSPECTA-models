package GUMBOLib {
	private import AADL::*;
	private import SysML;
	private import Metaobjects::SemanticMetadata;
 
 	
	
	metadata def <gumbodefs> GUMBODefinitions {
		:> annotatedElement : SysML::Usage;
		// would like to somehow restrict this to a collection of calc definitions
	}
	
	comment /* Depending on the kinds of things state variables
	 * need to reference, it might make sense to annotate a different
	 * SysMLv2 construct. 
	 */
	metadata def <statevar> GUMBOStateVariable {
		:> annotatedElement : SysML::AttributeUsage;
	}
	metadata def <invariant> GUMBOInvariantBlock {
		:> annotatedElement : SysML::Usage;
	}
	
	metadata def <gumbocompute> GUMBOComputeBlock {
		:> annotatedElement : SysML::Usage;
	}
	
	metadata def <gumbointegration> GUMBOIntegrationBlock {
		:> annotatedElement : SysML::Usage;
	}
	
	metadata def <guarantee> GUMBOGuaranteeConstraint {
		:> annotatedElement: SysML::ConstraintUsage;
	}
	
	comment /* /assume/ is a keyword in SysMLv2. 
	 * Could just use unannotated constraints since we already
	 * have the #compute block to demarcate that these are GUMBO things.
	 * NOTE: assume might only be valid inside a requirement block. 
	 */
	metadata def <gumboassume> GUMBOAssumeConstraint {
		:> annotatedElement: SysML::ConstraintUsage;
	}
	
	metadata def <cases> GUMBOCaseBlock {
		:> annotatedElement : SysML::Usage;
	}
	
	metadata def <gumbostate> GUMBOState;
	
	comment /* Kinda fake. Gives us the ability to add [u16] and [u8]
	 * tags to attributes. Can probably make them more useful. 
	 */
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
	
	
	package Examples {
		#statevar attribute name = 1;
		
		#gumbodefs definition_block_name {
			calc def {}
		}
		
		#gumbocompute compute_block_name {
			#guarantee constraint guarantee_name {}
			#gumboassume constraint assume_name {}
			constraint plain_constraint {}
		}
		
		#gumbointegration integration_block_name {
			
		}
	}
}