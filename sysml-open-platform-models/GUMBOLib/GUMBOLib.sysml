package GUMBOLib {
	private import AADL::*;
	private import SysML;
	private import Metaobjects::SemanticMetadata;
 	
 	//tag-per-definition style
	metadata def <gumbofunction> GUMBOFunctionDefinition {
		:> annotatedElement : SysML::CalculationDefinition;
	}
	
	metadata def <statevar> GUMBOStateVariable {
		:> annotatedElement : SysML::Usage;
	}
	
	metadata def <compute> GUMBOComputeContract {
		:> annotatedElement : SysML::ConstraintUsage;
	}
	
	comment /* 'case' is a keyword in SysMLv2. */
	metadata def <cases> GUMBOCaseContract {
		:> annotatedElement : SysML::ConstraintUsage;
	}
	
	metadata def <guarantee> GUMBOGuaranteeConstraint {
		:> annotatedElement: SysML::ConstraintUsage;
	}
	
	comment /* 'assume' is a keyword in SysMLv2. */
	metadata def <assumption> GUMBOAssumeConstraint {
		:> annotatedElement: SysML::ConstraintUsage;
	}
	
	metadata def <initialize> GUMBOInitializationConstraint {
		:> annotatedElement: SysML::ConstraintUsage;
	}
	
	metadata def <GUMBOLib> gumbofunctioninitionLibrary {
		:> annotatedElement: SysML::Package;
	}
	
	metadata def <invariant> GUMBOInvariant {
		:> annotatedElement: SysML::ConstraintUsage;
	}
	
	metadata def <integration> GUMBOIntegrationConstraint {
		:> annotatedElement: SysML::ConstraintUsage;
	}
	
	comment /* Kinda fake. Gives us the ability to add [u16] and [u8]
	 * tags to attributes. Can probably make them more useful. 
	 */
	attribute <u16>  : StorageCapacityUnit;
	attribute <u8>  : StorageCapacityUnit;	
	attribute <s32> : StorageCapacityUnit;
	
	// GUMBO built-ins
	// TODO: evaluate if KerML expression provided quantifiers work in place of built in quantifiers
	calc def HasEvent {
		in portName : AADL::Feature;
		return : ScalarValues::Boolean;
	}
	
	calc def NoSend {
		in portName : AADL::Feature;
		return : ScalarValues::Boolean;
	}
	
	calc def In {
		in portName : AADL::Feature;
		return : ScalarValues::Boolean;
	}
	
	// difference between this and a for loop? Probably stops on first true
	constraint def Exists {
		in lo;
		in hi;
		in predicate;
	}
	
	constraint def ExistsIn {
		in coll;
		in predicate;
		return : ScalarValues::Boolean;
	}
	
	constraint def All {
		in lo;
		in hi;
		in predicate;
	}
	
	
	package Examples {
		#statevar attribute name = 1;
		
		#gumbofunction calc def func_name { in param; return : Base_Types::Data_Boolean;}
		
		// syntactically valid but not correct gumbo
		#compute constraint compute_block_name {
			#guarantee constraint guarantee_name {}
			#assumption constraint assume_name {}
			constraint plain_constraint {}
		}
		
		#integration constraint integration_block_name {
			
		}
	}
}