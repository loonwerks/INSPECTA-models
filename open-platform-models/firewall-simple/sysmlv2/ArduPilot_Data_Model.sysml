package ArduPilot_Data_Model {
	
	public import AADL::*;
	public import Base_Types::*;
	public import ScalarValues::*;
	
	part def SizedEthernetMessage :> Data {
		part payload : ArduPilot_Data_Model::RawEthernetMessage;
		part size : Base_Types::Unsigned_16;
	}
	
	part def RawEthernetMessage :> Data {
		
	}
	
}