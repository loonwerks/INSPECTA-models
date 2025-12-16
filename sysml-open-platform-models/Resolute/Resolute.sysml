package Resolute {
	private import SysML;
	
	private import AADL::*;
	private import Base_Types::*;
	
	alias Boolean for ScalarValues::Boolean;
	// Resolute Concepts from reference.md
	// claim functions (functions that end in bool?)
	// computational functions
	// global constants
		
	metadata def <goal> ResoluteGoal {
		attribute text : ScalarValues::String;
		attribute strategy : ScalarValues::String;
		:> annotatedElement : SysML::ConstraintDefinition;
		
	}
	metadata def <argue> ResoluteArgue;
	metadata def <resdef> ResoluteDef;
	
	metadata def <goalreq> ResoluteGoalreq {
		attribute text : ScalarValues::String;
		attribute strategy : ScalarValues::String;
		:> annotatedElement : SysML::RequirementDefinition;
	}
	
			
	calc def forall {
		in coll : Collections::Collection; 
		in calc predicate { 
			in arg;
			return : Boolean;
		}
		return : Boolean;
	}
	
	calc def exists {
		in coll : Component[0..*]; 
		in calc predicate {
			in arg :> coll; // arg is subset of coll
			// in arg : Component;
			return : Data_Boolean;
		}
		return : Data_Boolean;
	}
		
	calc def connections {
		in component : Component;
		return : Connection[*]; // Do we want the return type
	}
	
	calc def has_property {
		in component : Component;
		in property : Property;
		return : Data_Boolean;
	}

	// can fail and is overloaded :*(
	calc def property {
		in component : Component[0..1];
		in property : AADL::Property[0..1];
		in defaultAnswer : Data_String[0..1];
		return : Data_String ;
	}
	
	
	//? diff between this and has_property?
	calc def property_member {
		in component : Component;
		in property : AADL::Property;
		return : Data_Boolean;
	}
	
	calc def name {
		in component : Component;
		return : Data_String;
	}
	
	calc def has_type {
		in component : Component;
		return : Data_Boolean;
	}
	
	calc def parent {
		in component : Component;
		return : Component;
	}
	
	calc def source_component {
		in conn : Connection;
		return : Component;
	}
	
	calc def destination_component {
		in conn : Connection;
		return : Component;
	}
	
	calc def component_equality {
		in c1 : Component;
		in c2 : Component;
		return : Data_Boolean;
	}
	
	calc def property_equality {
		in p1 : Property;
		in p2 : Property;
		return : Data_Boolean;
	}
	
	calc def list_filter {
		in list : Component[*];
		in calc pred { in arg : Component; return : Data_Boolean;} // wait, why is arg : Component fine here?
		return : Component[*]; //should be a list but dont have rep yet
	}
	
	calc def intersect {
		in c1 : Collections::Set;
		in c2 : Collections::Set;
		return : Collections::Set;
	}

}