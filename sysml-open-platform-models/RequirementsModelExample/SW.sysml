package SW {
	public import AADL::*;
	private import SI::*;
	private import SIPrefixes::*;
	
	private import GUMBOLib::*;
	private import CASEHAMRDefs::*;
	
	
	part def u16Array :> Data {		
		::> Data_Size = 2 [B];
		::> value : Collections::Array { ::> dimensions = 1; } // untyped should be an unsigned 16
		attribute Array_Size_Kind : CASEHAMRDefs::HAMR::Array_Size_Kind = CASEHAMRDefs::HAMR::Array_Size_Kind::Fixed;
	}
	
	part def RawEthernetMessage :> Data {
		::> Data_Size = 1600 [B];
		attribute Array_Size_Kind : CASEHAMRDefs::HAMR::Array_Size_Kind = CASEHAMRDefs::HAMR::Array_Size_Kind::Fixed;
		:>> value : Collections::Array { ::> dimensions = 1600; } // untyped should be an unsigned 8
	}
	
	part def SizedEthernetMessage :> Data {
		// My version of the AADL Libraries do not have much in the way Data Representation constructs
		// attribute Data_Model::Data_Representation => Struct;  
	}

	part def SizedEthernetMessageImpl :> SizedEthernetMessage {
		part amessage : RawEthernetMessage;
		part sz : Base_Types::Unsigned_16;
	}
	
	part def LowLevelEthernetDriver :> Thread {
		out port ethernetFramesRx0: EventDataPort {out :> type : RawEthernetMessage; }
		out port ethernetFramesRx1: EventDataPort {out :> type : RawEthernetMessage; } 
		out port ethernetFramesRx2: EventDataPort {out :> type : RawEthernetMessage; } 
		out port ethernetFramesRx3: EventDataPort {out :> type : RawEthernetMessage; }
		in port ethernetFramesTx0: EventDataPort {in :> type : SizedEthernetMessageImpl; }
		in port ethernetFramesTx1: EventDataPort {in :> type : SizedEthernetMessageImpl; }
		in port ethernetFramesTx2: EventDataPort {in :> type : SizedEthernetMessageImpl; }
		in port ethernetFramesTx3: EventDataPort {in :> type : SizedEthernetMessageImpl; }
	}
	
	comment /* unsure of where to put attributes for this. Gonna follow AADL lead for now */
	
	part def LowLevelEthernetDriverImpl :> LowLevelEthernetDriver {
		:>> Dispatch_Protocol = AADL_Project::Periodic;
		:>> Period : Time { ::> num = 1000[m][s]; }
		:>> Compute_Execution_Time : Time_Range {
			:>> minimum : Time { :>> num = 100[m][s]; }
			:>> maximum : Time { :>> num = 100[m][s]; }
		}
		attribute microkit_language : CASEHAMRDefs::HAMR::Microkit_Language = CASEHAMRDefs::HAMR::Microkit_Language::Rust;
		attribute Stack_size = 1048576 [B];
	}
	
	part def LowLevelEthernetDriver_seL4 :> Process {
		out port ethernetFramesRx0: EventDataPort {out :> type : RawEthernetMessage; }
		out port ethernetFramesRx1: EventDataPort {out :> type : RawEthernetMessage; } 
		out port ethernetFramesRx2: EventDataPort {out :> type : RawEthernetMessage; } 
		out port ethernetFramesRx3: EventDataPort {out :> type : RawEthernetMessage; }
		in port ethernetFramesTx0: EventDataPort {in :> type : SizedEthernetMessageImpl; }
		in port ethernetFramesTx1: EventDataPort {in :> type : SizedEthernetMessageImpl; }
		in port ethernetFramesTx2: EventDataPort {in :> type : SizedEthernetMessageImpl; }
		in port ethernetFramesTx3: EventDataPort {in :> type : SizedEthernetMessageImpl; }	
	}
	
	part def LowLevelEthernetDriver_seL4Impl :> LowLevelEthernetDriver_seL4 {
		part lowLevelEthernetDriver : LowLevelEthernetDriver; 
		
		connection c1 : PortConnection 
			connect ethernetFramesTx0 to lowLevelEthernetDriver.ethernetFramesTx0;
		connection c2 : PortConnection 
			connect ethernetFramesTx1 to lowLevelEthernetDriver.ethernetFramesTx1;
		connection c3 : PortConnection 
			connect ethernetFramesTx2 to lowLevelEthernetDriver.ethernetFramesTx2;
		connection c4 : PortConnection 
			connect ethernetFramesTx3 to lowLevelEthernetDriver.ethernetFramesTx3;
		connection c5 : PortConnection
			connect lowLevelEthernetDriver.ethernetFramesRx0 to ethernetFramesRx0;
		connection c6 : PortConnection
			connect lowLevelEthernetDriver.ethernetFramesRx1 to ethernetFramesRx1;
		connection c7 : PortConnection
			connect lowLevelEthernetDriver.ethernetFramesRx2 to ethernetFramesRx2;
		connection c8 : PortConnection
			connect lowLevelEthernetDriver.ethernetFramesRx3 to ethernetFramesRx3;
			
		attribute domain : CASE_Scheduling::Domain = 2; //: Case_Scheduling::Domain = 2;
		attribute microkit_language : HAMR::Microkit_Language = HAMR::Microkit_Language::Rust;
	}
	
	part def RxFirewall :> Thread {
		in port ethernetFramesRxIn0 : EventDataPort { in :> type : RawEthernetMessage; }
		in port ethernetFramesRxIn1 : EventDataPort { in :> type : RawEthernetMessage; }
		in port ethernetFramesRxIn2 : EventDataPort { in :> type : RawEthernetMessage; }
		in port ethernetFramesRxIn3 : EventDataPort { in :> type : RawEthernetMessage; }
		out port ethernetFramesRxOut0 : EventDataPort { out :> type : RawEthernetMessage; }
		out port ethernetFramesRxOut1 : EventDataPort { out :> type : RawEthernetMessage; }
		out port ethernetFramesRxOut2 : EventDataPort { out :> type : RawEthernetMessage; }
		out port ethernetFramesRxOut3 : EventDataPort { out :> type : RawEthernetMessage; }
	}
	
	part def RxFirewallImpl :> RxFirewall {
		:>> Dispatch_Protocol = AADL_Project::Periodic;
		:>> Period : Time { ::> num = 1000[m][s]; }
		:>> Compute_Execution_Time : Time_Range {
			:>> minimum : Time { ::> num = 100[m][s]; }
			:>> maximum : Time { ::> num = 100[m][s]; }
		}
		attribute stack_size : Memory_Properties::Stack_Size = 1048576[B];
		attribute microkit_language : HAMR::Microkit_Language = HAMR::Microkit_Language::Rust;
		
		#gumbodefs {
			calc def TCP_ALLOWED_PORTS { return : Collections::Array;
				("5760"[u16])
			}
			
			calc def UDP_ALLOWED_PORTS { return : Collections::Array;
				("68"[u16])
			}
			
			calc def frame_is_wellformed { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
				valid_frame_ethertype(aframe) and valid_frame_dst_addr(aframe)
			}
			
			calc def valid_frame_ethertype {  in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
				frame_has_ivp4(aframe) or frame_has_arp(aframe) or frame_has_ivp6(aframe)
			}
			
			calc def valid_frame_dst_addr {  in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
				not (aframe.value#(0) == "0"[u8] and 
					 aframe.value#(2) == "0"[u8] and 
					 aframe.value#(3) == "0"[u8] and 
					 aframe.value#(4) == "0"[u8] and 
					 aframe.value#(5) == "0"[u8])
			}
			
			calc def frame_has_ivp4 { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
				aframe.value#(12) == "8"[u8] and aframe.value#(13) == "0"[u8]
			}
			
			calc def frame_has_ivp6 { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
				aframe.value#(12) == "134"[u8] and aframe.value#(13) == "221"[u8]
			}
			
			calc def frame_has_arp { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
				aframe.value#(12) == "8"[u8] and aframe.value#(13) == "6"[u8]
			}
			
			calc def arp_has_ivp4 { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
				aframe.value#(16) == "8"[u8] and aframe.value#(17) == "0"[u8]
			}
				
		}
		
		#gumbocompute {
			#guarantee constraint rx {
				HasEvent(ethernetFramesRxIn0).value
			}
		}
	}
	
	part def RxFirewall_seL4 :> Process {
		in port ethernetFramesRxIn0 : EventDataPort { in :> type : RawEthernetMessage; }
		in port ethernetFramesRxIn1 : EventDataPort { in :> type : RawEthernetMessage; }
		in port ethernetFramesRxIn2 : EventDataPort { in :> type : RawEthernetMessage; }
		in port ethernetFramesRxIn3 : EventDataPort { in :> type : RawEthernetMessage; }
		out port ethernetFramesRxOut0 : EventDataPort { out :> type : RawEthernetMessage; }
		out port ethernetFramesRxOut1 : EventDataPort { out :> type : RawEthernetMessage; }
		out port ethernetFramesRxOut2 : EventDataPort { out :> type : RawEthernetMessage; }
		out port ethernetFramesRxOut3 : EventDataPort { out :> type : RawEthernetMessage; }
	}
	
	part def RxFirewall_seL4Impl :> RxFirewall_seL4 {
		part firewall : RxFirewallImpl;
		connection c1 : PortConnection 
			connect ethernetFramesRxIn0 to firewall.ethernetFramesRxIn0;
		connection c2 : PortConnection 
			connect ethernetFramesRxIn1 to firewall.ethernetFramesRxIn1;
		connection c3 : PortConnection 
			connect ethernetFramesRxIn2 to firewall.ethernetFramesRxIn2;
		connection c4 : PortConnection 
			connect ethernetFramesRxIn3 to firewall.ethernetFramesRxIn3;	
		connection c5 : PortConnection
			connect firewall.ethernetFramesRxOut0 to ethernetFramesRxOut0;
		connection c6 : PortConnection
			connect firewall.ethernetFramesRxOut1 to ethernetFramesRxOut1;
		connection c7 : PortConnection
			connect firewall.ethernetFramesRxOut2 to ethernetFramesRxOut2;
		connection c8 : PortConnection
			connect firewall.ethernetFramesRxOut3 to ethernetFramesRxOut3;
		attribute domain : CASE_Scheduling::Domain = 3;	
		attribute microkit_language : HAMR::Microkit_Language = HAMR::Microkit_Language::Rust;
	}
	
	part def TxFirewall :> Thread {
		in port ethernetFramesTxIn0 : EventDataPort { in :> type : RawEthernetMessage; }
		in port ethernetFramesTxIn1 : EventDataPort { in :> type : RawEthernetMessage; }
		in port ethernetFramesTxIn2 : EventDataPort { in :> type : RawEthernetMessage; }
		in port ethernetFramesTxIn3 : EventDataPort { in :> type : RawEthernetMessage; }
		out port ethernetFramesTxOut0 : EventDataPort { out :> type : SizedEthernetMessageImpl; }
		out port ethernetFramesTxOut1 : EventDataPort { out :> type : SizedEthernetMessageImpl; }
		out port ethernetFramesTxOut2 : EventDataPort { out :> type : SizedEthernetMessageImpl; }
		out port ethernetFramesTxOut3 : EventDataPort { out :> type : SizedEthernetMessageImpl; }
	}
	
	part def TxFirewallImpl :> TxFirewall {
		:>> Dispatch_Protocol = AADL_Project::Periodic;
		:>> Period : Time { ::> num = 1000[m][s]; }
		:>> Compute_Execution_Time : Time_Range {
			:>> minimum : Time { ::> num = 100[m][s]; }
			:>> maximum : Time { ::> num = 100[m][s]; }
		}
		attribute stack_size : Memory_Properties::Stack_Size = 1048576[B];
		attribute microkit_language : HAMR::Microkit_Language = HAMR::Microkit_Language::Rust;
		
		#gumbodefs {
			
		}
		
		#gumbocompute {
			
		}
	}
	
	part def TxFirewall_seL4 :> Process {
		in port ethernetFramesTxIn0 : EventDataPort { in :> type : RawEthernetMessage; }
		in port ethernetFramesTxIn1 : EventDataPort { in :> type : RawEthernetMessage; }
		in port ethernetFramesTxIn2 : EventDataPort { in :> type : RawEthernetMessage; }
		in port ethernetFramesTxIn3 : EventDataPort { in :> type : RawEthernetMessage; }
		out port ethernetFramesTxOut0 : EventDataPort { out :> type : SizedEthernetMessageImpl; }
		out port ethernetFramesTxOut1 : EventDataPort { out :> type : SizedEthernetMessageImpl; }
		out port ethernetFramesTxOut2 : EventDataPort { out :> type : SizedEthernetMessageImpl; }
		out port ethernetFramesTxOut3 : EventDataPort { out :> type : SizedEthernetMessageImpl; }
	}
	
	part def TxFirewall_seL4Impl :> TxFirewall_seL4 {
		part firewall : TxFirewall_seL4;
		
		connection c1 : PortConnection 
			connect ethernetFramesTxIn0 to firewall.ethernetFramesTxIn0;
		connection c2 : PortConnection 
			connect ethernetFramesTxIn1 to firewall.ethernetFramesTxIn1;
		connection c3 : PortConnection 
			connect ethernetFramesTxIn2 to firewall.ethernetFramesTxIn2;
		connection c4 : PortConnection 
			connect ethernetFramesTxIn3 to firewall.ethernetFramesTxIn3;	
		connection c5 : PortConnection
			connect firewall.ethernetFramesTxOut0 to ethernetFramesTxOut0;
		connection c6 : PortConnection
			connect firewall.ethernetFramesTxOut1 to ethernetFramesTxOut1;
		connection c7 : PortConnection
			connect firewall.ethernetFramesTxOut2 to ethernetFramesTxOut2;
		connection c8 : PortConnection
			connect firewall.ethernetFramesTxOut3 to ethernetFramesTxOut3;
			
		attribute domain : CASE_Scheduling::Domain = 5; //: Case_Scheduling::Domain = 2;
		attribute microkit_language : HAMR::Microkit_Language = HAMR::Microkit_Language::Rust;
	}
	
	part def ArduPilot :> Thread {
		in port ethernetFramesRx0 : EventDataPort { in :> type : RawEthernetMessage; }
		in port ethernetFramesRx1 : EventDataPort { in :> type : RawEthernetMessage; }
		in port ethernetFramesRx2 : EventDataPort { in :> type : RawEthernetMessage; }
		in port ethernetFramesRx3 : EventDataPort { in :> type : RawEthernetMessage; }
		out port ethernetFramesTx0 : EventDataPort { out :> type : RawEthernetMessage; }
		out port ethernetFramesTx1 : EventDataPort { out :> type : RawEthernetMessage; }
		out port ethernetFramesTx2 : EventDataPort { out :> type : RawEthernetMessage; }
		out port ethernetFramesTx3 : EventDataPort { out :> type : RawEthernetMessage; }
	}
	
	part def ArduPilotImpl :> ArduPilot {
		:>> Dispatch_Protocol = AADL_Project::Periodic;
		:>> Period : Time { ::> num = 1000[m][s]; }
		:>> Compute_Execution_Time : Time_Range {
			:>> minimum : Time { :>> num = 100[m][s]; }
			:>> maximum : Time { :>> num = 100[m][s]; }
			}
		attribute stack_size = 1048576[B];
		attribute source_text = "src/Ardupilot.c";
		attribute initialize_entrypoint_source_text = "init";
		attribute compute_entrypoint_source_text = "compute";
	}
	
	part def ArduPilot_seL4 :> Process {
		in port ethernetFramesRx0 : EventDataPort { in :> type : RawEthernetMessage; }
		in port ethernetFramesRx1 : EventDataPort { in :> type : RawEthernetMessage; }
		in port ethernetFramesRx2 : EventDataPort { in :> type : RawEthernetMessage; }
		in port ethernetFramesRx3 : EventDataPort { in :> type : RawEthernetMessage; }
		out port ethernetFramesTx0 : EventDataPort { out :> type : RawEthernetMessage; }
		out port ethernetFramesTx1 : EventDataPort { out :> type : RawEthernetMessage; }
		out port ethernetFramesTx2 : EventDataPort { out :> type : RawEthernetMessage; }
		out port ethernetFramesTx3 : EventDataPort { out :> type : RawEthernetMessage; }
	}
	
	part def ArduPilot_seL4Impl :> ArduPilot_seL4 {
		part arduPilot : ArduPilotImpl;
		
		connection c1 : PortConnection 
			connect ethernetFramesRx0 to arduPilot.ethernetFramesRx0;
		connection c2 : PortConnection 
			connect ethernetFramesRx1 to arduPilot.ethernetFramesRx1;
		connection c3 : PortConnection 
			connect ethernetFramesRx2 to arduPilot.ethernetFramesRx2;
		connection c4 : PortConnection 
			connect ethernetFramesRx3 to arduPilot.ethernetFramesRx3;
		connection c5 : PortConnection
			connect arduPilot.ethernetFramesTx0 to ethernetFramesTx0;
		connection c6 : PortConnection
			connect arduPilot.ethernetFramesTx1 to ethernetFramesTx1;
		connection c7 : PortConnection
			connect arduPilot.ethernetFramesTx2 to ethernetFramesTx2;
		connection c8 : PortConnection
			connect arduPilot.ethernetFramesTx3 to ethernetFramesTx3;
			
		attribute domain : CASE_Scheduling::Domain = 4;
	}
	
	part def seL4 :> System;
	
	part def seL4Impl :> seL4 {
		part arduPilot : ArduPilot_seL4Impl;
		part rxfirewall : RxFirewall_seL4Impl;
		part txfirewall : TxFirewall_seL4Impl;
		part lowLevelEthernetDriver : LowLevelEthernetDriver_seL4Impl;
		
		connection c1 : PortConnection
			connect lowLevelEthernetDriver.ethernetFramesRx0 to rxfirewall.ethernetFramesRxIn0;
		connection c2 : PortConnection
			connect lowLevelEthernetDriver.ethernetFramesRx1 to rxfirewall.ethernetFramesRxIn1;
		connection c3 : PortConnection
			connect lowLevelEthernetDriver.ethernetFramesRx2 to rxfirewall.ethernetFramesRxIn2;
		connection c4 : PortConnection
			connect lowLevelEthernetDriver.ethernetFramesRx3 to rxfirewall.ethernetFramesRxIn3;
		connection c5 : PortConnection
			connect rxfirewall.ethernetFramesRxOut0 to arduPilot.ethernetFramesRx0;
		connection c6 : PortConnection
			connect rxfirewall.ethernetFramesRxOut1 to arduPilot.ethernetFramesRx1;
		connection c7 : PortConnection
			connect rxfirewall.ethernetFramesRxOut2 to arduPilot.ethernetFramesRx2;
		connection c8 : PortConnection
			connect rxfirewall.ethernetFramesRxOut3 to arduPilot.ethernetFramesRx3;
		
		connection c9 : PortConnection
			connect arduPilot.ethernetFramesTx0 to txfirewall.ethernetFramesTxOut0;
		connection c10 : PortConnection
			connect arduPilot.ethernetFramesTx1 to txfirewall.ethernetFramesTxOut1;
		connection c11 : PortConnection
			connect arduPilot.ethernetFramesTx2 to txfirewall.ethernetFramesTxOut2;
		connection c12 : PortConnection
			connect arduPilot.ethernetFramesTx3 to txfirewall.ethernetFramesTxOut3;
		connection c13 : PortConnection
			connect txfirewall.ethernetFramesTxOut0 to lowLevelEthernetDriver.ethernetFramesTx0;
		connection c14 : PortConnection
			connect txfirewall.ethernetFramesTxOut1 to lowLevelEthernetDriver.ethernetFramesTx1;
		connection c15 : PortConnection
			connect txfirewall.ethernetFramesTxOut2 to lowLevelEthernetDriver.ethernetFramesTx2;
		connection c16 : PortConnection
			connect txfirewall.ethernetFramesTxOut3 to lowLevelEthernetDriver.ethernetFramesTx3;
	}
}