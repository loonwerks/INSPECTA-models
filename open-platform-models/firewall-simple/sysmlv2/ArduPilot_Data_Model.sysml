package ArduPilot_Data_Model {
	
	public import HAMR::*;

	part def SizedEthernetMessage :> Data {
		part payload : ArduPilot_Data_Model::RawEthernetMessage;
		part size : Base_Types::Unsigned_16;
	}
	
	part def RawEthernetMessage :> Data {
		
	}
	
}