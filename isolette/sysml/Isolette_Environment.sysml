// Isolette_Environment.sysml
//
// =====================================================================================================================
//
//   I s o l e t t e     E n v i r o n m e n t     P a c k a g e 
// 
//
//   Modeling of environment for Isolette
//
//   Note:  These models contribute nothing to the Isolette implementation.
//          They are included here to support model-based hazard analysis activities 
//          as well support Concept of Operations and Use Case modeling 
//
// =====================================================================================================================

package Isolette_Environment {
	
    private import AADL::*;
    private import AADL_Project::*;
    private import AADL_Project::Time_Units::*;
	
	// air inside room (make air connections flows?)
	part def Air :> System {
		
		// Ports
		
		// warming of air by heat source
		in port heat_in : DataPort { in :> type : Heat; }
		// heat-loss of incubator air to room
		out port heat_out : DataPort { out :> type : Heat; }
		// air contact with infant
		// TODO: This is currently modeled as an IN/OUT port, which might cause problems
		// with the EMv2/hazard analysis
		inout port infant_interaction : DataPort { inout :> type : Air_Interaction; }
		//actual air temperature that the sensor measures
		out port air_temperature : DataPort { out :> type : Isolette_Data_Model::PhysicalTemp_i; }
	}
	
	// nurse controlling room
	part def Nurse :> Abstract {
		
		// Ports
		
		// models (abstractly) visual information presented to the operator on Isolette operator interface
		in port operator_visual_information : DataPort { in :> type : Interface_Interaction; }
		// models (abstraction) audio information presented to the operator on Isolette operator interface
		in port operator_alarm : DataPort { in :> type : Interface_Interaction; }
		// models (abstractly) commands given by the operator to Isolette operator interface
		out port operator_commands : DataPort { out :> type : Interface_Interaction; }
	}
	
	// infant in room
	part def Infant :> Abstract {
		inout port air_interaction : DataPort { inout :> type : Air_Interaction; }
	}
	
	// heat emitted by source to warm air
	enum def Heat {
	  enum Dummy_Head_Enum;
	}
	
	// data that flows between operator and control panel
	enum def Interface_Interaction {
	  enum Dummy_Interface_Interaction_Enum;
    }
	
	// warming of air by heat source, or of infant by air
	enum def Air_Interaction {
	  enum Dummy_Air_Interaction_Enum;
    }
		
	// implementation of Room that contains incubator, infant, nurse, and air
	part def Isolette_Environment_Normal_Operation_Normal_Operation :> System {
		
		// Subcomponents
		part anIncubator : Isolette::Isolette_Single_Sensor;
		part theAir : Air;
		part aNurse : Nurse;
		part anInfant : Infant;

		// models heat-loss of incubator to (presumably) cooler room
		out port heat_loss : DataPort { out :> type : Heat;}


		// Connections
		// air with infant interaction
		connection a2i : PortConnection
			connect theAir.infant_interaction to anInfant.air_interaction;
		// heating source warming air interaction
		connection hs2a : PortConnection
			connect anIncubator.heat_out to theAir.heat_in;
		connection tmp : PortConnection
			connect theAir.air_temperature to anIncubator.air_temperature;
		// heat-loss
		connection hl : PortConnection
			connect  theAir.heat_out to heat_loss;
		// nurse with control-panel interaction
		connection n2oivi : PortConnection
			connect anIncubator.operator_visual_information to aNurse.operator_visual_information;
		connection n2ioa : PortConnection
			connect anIncubator.operator_alarm to aNurse.operator_alarm;
		connection n2ioc : PortConnection
			connect aNurse.operator_commands to anIncubator.operator_commands;

	}
	
}