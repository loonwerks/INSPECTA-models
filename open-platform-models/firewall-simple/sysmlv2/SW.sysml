package SW {
	
	public import HAMR::*;
	public import ArduPilot_Data_Model::*;
	
	part def LowLevelEthernetDriver :> Thread {
		in port EthernetFramesTx0 : EventDataPort {in :>> type : ArduPilot_Data_Model::SizedEthernetMessage;}
		in port EthernetFramesTx1 : EventDataPort {in :>> type : ArduPilot_Data_Model::SizedEthernetMessage;}
		in port EthernetFramesTx2 : EventDataPort {in :>> type : ArduPilot_Data_Model::SizedEthernetMessage;}
		in port EthernetFramesTx3 : EventDataPort {in :>> type : ArduPilot_Data_Model::SizedEthernetMessage;}
		out port EthernetFramesRx0 : EventDataPort {out :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		out port EthernetFramesRx1 : EventDataPort {out :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		out port EthernetFramesRx2 : EventDataPort {out :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		out port EthernetFramesRx3 : EventDataPort {out :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
	}
	
	part def LowLevelEthernetDriver_seL4 :> Process {
		in port EthernetFramesTx0 : EventDataPort {in :>> type : ArduPilot_Data_Model::SizedEthernetMessage;}
		in port EthernetFramesTx1 : EventDataPort {in :>> type : ArduPilot_Data_Model::SizedEthernetMessage;}
		in port EthernetFramesTx2 : EventDataPort {in :>> type : ArduPilot_Data_Model::SizedEthernetMessage;}
		in port EthernetFramesTx3 : EventDataPort {in :>> type : ArduPilot_Data_Model::SizedEthernetMessage;}
		out port EthernetFramesRx0 : EventDataPort {out :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		out port EthernetFramesRx1 : EventDataPort {out :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		out port EthernetFramesRx2 : EventDataPort {out :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		out port EthernetFramesRx3 : EventDataPort {out :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		
		part LowLevelEthernetDriver : SW::LowLevelEthernetDriver;
		
		connection c0 : PortConnection connect EthernetFramesTx0 to LowLevelEthernetDriver.EthernetFramesTx0;
		connection c1 : PortConnection connect EthernetFramesTx1 to LowLevelEthernetDriver.EthernetFramesTx1;
		connection c2 : PortConnection connect EthernetFramesTx2 to LowLevelEthernetDriver.EthernetFramesTx2;
		connection c3 : PortConnection connect EthernetFramesTx3 to LowLevelEthernetDriver.EthernetFramesTx3;
		connection c4 : PortConnection connect LowLevelEthernetDriver.EthernetFramesRx0 to EthernetFramesRx0;
		connection c5 : PortConnection connect LowLevelEthernetDriver.EthernetFramesRx1 to EthernetFramesRx1;
		connection c6 : PortConnection connect LowLevelEthernetDriver.EthernetFramesRx2 to EthernetFramesRx2;
		connection c7 : PortConnection connect LowLevelEthernetDriver.EthernetFramesRx3 to EthernetFramesRx3;
	}
	
	
	part def RxFirewall :> Thread {
		in port EthernetFramesRxIn0 : EventDataPort {in :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		in port EthernetFramesRxIn1 : EventDataPort {in :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		in port EthernetFramesRxIn2 : EventDataPort {in :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		in port EthernetFramesRxIn3 : EventDataPort {in :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		out port EthernetFramesRxOut0 : EventDataPort {out :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		out port EthernetFramesRxOut1 : EventDataPort {out :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		out port EthernetFramesRxOut2 : EventDataPort {out :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		out port EthernetFramesRxOut3 : EventDataPort {out :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
	}
	
	part def RxFirewall_seL4 :> Process {
		in port EthernetFramesRxIn0 : EventDataPort {in :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		in port EthernetFramesRxIn1 : EventDataPort {in :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		in port EthernetFramesRxIn2 : EventDataPort {in :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		in port EthernetFramesRxIn3 : EventDataPort {in :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		out port EthernetFramesRxOut0 : EventDataPort {out :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		out port EthernetFramesRxOut1 : EventDataPort {out :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		out port EthernetFramesRxOut2 : EventDataPort {out :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		out port EthernetFramesRxOut3 : EventDataPort {out :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		
		part RxFirewall : SW::RxFirewall;
		
		connection c0 : PortConnection connect EthernetFramesRxIn0 to RxFirewall.EthernetFramesRxIn0;
		connection c1 : PortConnection connect EthernetFramesRxIn1 to RxFirewall.EthernetFramesRxIn0;
		connection c2 : PortConnection connect EthernetFramesRxIn2 to RxFirewall.EthernetFramesRxIn0;
		connection c3 : PortConnection connect EthernetFramesRxIn3 to RxFirewall.EthernetFramesRxIn0;
		connection c4 : PortConnection connect EthernetFramesRxOut0 to EthernetFramesRxOut0;
		connection c5 : PortConnection connect EthernetFramesRxOut1 to EthernetFramesRxOut1;
		connection c6 : PortConnection connect EthernetFramesRxOut2 to EthernetFramesRxOut2;
		connection c7 : PortConnection connect EthernetFramesRxOut3 to EthernetFramesRxOut3;
	}
	
	
	part def TxFirewall :> Thread {
		in port EthernetFramesTxIn0 : EventDataPort {in :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		in port EthernetFramesTxIn1 : EventDataPort {in :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		in port EthernetFramesTxIn2 : EventDataPort {in :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		in port EthernetFramesTxIn3 : EventDataPort {in :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		out port EthernetFramesTxOut0 : EventDataPort {out :>> type : ArduPilot_Data_Model::SizedEthernetMessage;}
		out port EthernetFramesTxOut1 : EventDataPort {out :>> type : ArduPilot_Data_Model::SizedEthernetMessage;}
		out port EthernetFramesTxOut2 : EventDataPort {out :>> type : ArduPilot_Data_Model::SizedEthernetMessage;}
		out port EthernetFramesTxOut3 : EventDataPort {out :>> type : ArduPilot_Data_Model::SizedEthernetMessage;}
	}
	
	part def TxFirewall_seL4 :> Process {
		in port EthernetFramesTxIn0 : EventDataPort {in :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		in port EthernetFramesTxIn1 : EventDataPort {in :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		in port EthernetFramesTxIn2 : EventDataPort {in :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		in port EthernetFramesTxIn3 : EventDataPort {in :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		out port EthernetFramesTxOut0 : EventDataPort {out :>> type : ArduPilot_Data_Model::SizedEthernetMessage;}
		out port EthernetFramesTxOut1 : EventDataPort {out :>> type : ArduPilot_Data_Model::SizedEthernetMessage;}
		out port EthernetFramesTxOut2 : EventDataPort {out :>> type : ArduPilot_Data_Model::SizedEthernetMessage;}
		out port EthernetFramesTxOut3 : EventDataPort {out :>> type : ArduPilot_Data_Model::SizedEthernetMessage;}
		
		part TxFirewall : SW::TxFirewall;
		
		connection c0 : PortConnection connect EthernetFramesTxIn0 to TxFirewall.EthernetFramesTxIn0;
		connection c1 : PortConnection connect EthernetFramesTxIn1 to TxFirewall.EthernetFramesTxIn1;
		connection c2 : PortConnection connect EthernetFramesTxIn2 to TxFirewall.EthernetFramesTxIn2;
		connection c3 : PortConnection connect EthernetFramesTxIn3 to TxFirewall.EthernetFramesTxIn3;
		connection c4 : PortConnection connect TxFirewall.EthernetFramesTxOut0 to EthernetFramesTxOut0;
		connection c5 : PortConnection connect TxFirewall.EthernetFramesTxOut1 to EthernetFramesTxOut1;
		connection c6 : PortConnection connect TxFirewall.EthernetFramesTxOut2 to EthernetFramesTxOut2;
		connection c7 : PortConnection connect TxFirewall.EthernetFramesTxOut3 to EthernetFramesTxOut3;
	}
	
	
	part def ArduPilot :> Thread {
		in port EthernetFramesRx0 : EventDataPort {in :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		in port EthernetFramesRx1 : EventDataPort {in :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		in port EthernetFramesRx2 : EventDataPort {in :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		in port EthernetFramesRx3 : EventDataPort {in :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		out port EthernetFramesTx0 : EventDataPort {out :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		out port EthernetFramesTx1 : EventDataPort {out :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		out port EthernetFramesTx2 : EventDataPort {out :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		out port EthernetFramesTx3 : EventDataPort {out :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
	}
	
	part def ArduPilot_seL4 :> Process {
		in port EthernetFramesRx0 : EventDataPort {in :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		in port EthernetFramesRx1 : EventDataPort {in :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		in port EthernetFramesRx2 : EventDataPort {in :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		in port EthernetFramesRx3 : EventDataPort {in :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		out port EthernetFramesTx0 : EventDataPort {out :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		out port EthernetFramesTx1 : EventDataPort {out :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		out port EthernetFramesTx2 : EventDataPort {out :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		out port EthernetFramesTx3 : EventDataPort {out :>> type : ArduPilot_Data_Model::RawEthernetMessage;}
		
		part ArduPilot : SW::ArduPilot;
		
		connection c0 : PortConnection connect EthernetFramesRx0 to ArduPilot.EthernetFramesRx0;
		connection c1 : PortConnection connect EthernetFramesRx1 to ArduPilot.EthernetFramesRx1;
		connection c2 : PortConnection connect EthernetFramesRx2 to ArduPilot.EthernetFramesRx2;
		connection c3 : PortConnection connect EthernetFramesRx3 to ArduPilot.EthernetFramesRx3;
		connection c4 : PortConnection connect ArduPilot.EthernetFramesTx0 to EthernetFramesTx0;
		connection c5 : PortConnection connect ArduPilot.EthernetFramesTx1 to EthernetFramesTx1;
		connection c6 : PortConnection connect ArduPilot.EthernetFramesTx2 to EthernetFramesTx2;
		connection c7 : PortConnection connect ArduPilot.EthernetFramesTx3 to EthernetFramesTx3;
	}
	
	
	part def seL4 :> System {
		part ArduPilot : SW::ArduPilot_seL4;
		part RxFirewall : SW::RxFirewall_seL4;
		part TxFirewall : SW::TxFirewall_seL4;
		part LowLevelEthernetDriver : SW::LowLevelEthernetDriver_seL4;
		
		// Incoming traffic
		connection c0 : PortConnection connect LowLevelEthernetDriver.EthernetFramesRx0 to RxFirewall.EthernetFramesRxIn0;
		connection c1 : PortConnection connect LowLevelEthernetDriver.EthernetFramesRx1 to RxFirewall.EthernetFramesRxIn1;
		connection c2 : PortConnection connect LowLevelEthernetDriver.EthernetFramesRx2 to RxFirewall.EthernetFramesRxIn2;
		connection c3 : PortConnection connect LowLevelEthernetDriver.EthernetFramesRx3 to RxFirewall.EthernetFramesRxIn3;
		connection c4 : PortConnection connect RxFirewall.EthernetFramesRxOut0 to ArduPilot.EthernetFramesRx0;
		connection c5 : PortConnection connect RxFirewall.EthernetFramesRxOut1 to ArduPilot.EthernetFramesRx1;
		connection c6 : PortConnection connect RxFirewall.EthernetFramesRxOut2 to ArduPilot.EthernetFramesRx2;
		connection c7 : PortConnection connect RxFirewall.EthernetFramesRxOut3 to ArduPilot.EthernetFramesRx3;
		
		// Outgoing traffic
		connection c8 : PortConnection connect ArduPilot.EthernetFramesTx0 to TxFirewall.EthernetFramesTxIn0;
		connection c9 : PortConnection connect ArduPilot.EthernetFramesTx1 to TxFirewall.EthernetFramesTxIn1;
		connection c10 : PortConnection connect ArduPilot.EthernetFramesTx2 to TxFirewall.EthernetFramesTxIn2;
		connection c11 : PortConnection connect ArduPilot.EthernetFramesTx3 to TxFirewall.EthernetFramesTxIn3;
		connection c12 : PortConnection connect TxFirewall.EthernetFramesTxOut0 to LowLevelEthernetDriver.EthernetFramesTx0;
		connection c13 : PortConnection connect TxFirewall.EthernetFramesTxOut1 to LowLevelEthernetDriver.EthernetFramesTx1;
		connection c14 : PortConnection connect TxFirewall.EthernetFramesTxOut2 to LowLevelEthernetDriver.EthernetFramesTx2;
		connection c15 : PortConnection connect TxFirewall.EthernetFramesTxOut3 to LowLevelEthernetDriver.EthernetFramesTx3;
	}
	
}