package SW {
	public import AADL::*;
	private import SI::*;
	private import SIPrefixes::*;
	
	private import GUMBOLib::*;
	private import CASEHAMRDefs::*;
	
	part def u16Array :> Data {
		attribute def myArray :> Collections::Array {
			redefines elements : Base_Types::Unsigned_16[0..1600];
			}
		redefines value : myArray; 
	}
	
	part def RawEthernetMessage :> Data {
		redefines Data_Size = 1600 [B];
		redefines value : Collections::Array { :>> elements : Base_Types::Unsigned_8[0..1600]; } // dimensions refers to 
		attribute Array_Size_Kind : CASEHAMRDefs::HAMR::Array_Size_Kind = CASEHAMRDefs::HAMR::Array_Size_Kind::Fixed;
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
			:>> minimum : Time { :>> num = 100[m][s]; }
			:>> maximum : Time { :>> num = 100[m][s]; }
		}
		attribute stack_size : Memory_Properties::Stack_Size = 1048576[B];
		attribute microkit_language : HAMR::Microkit_Language = HAMR::Microkit_Language::Rust;			
			
		comment /* The *_ALLOWED_PORTS definitions show two different ways to return values.
		 * One makes use of SysML data values and the other returns an AADL Data part. 
		 */
		#gumbofunction calc def TCP_ALLOWED_PORTS { return : Collections::Array;
			("5760"[u16])
		}
		
		#gumbofunction  calc def UDP_ALLOWED_PORTS {
			return : u16Array;
			part x : u16Array {
				:>> value = ("68"[u16]);
			}
		}
		
		#gumbofunction calc def two_bytes_to_u16{ in byte0 : Base_Types::Unsigned_8 ; in byte1 : Base_Types::Unsigned_8; return : Base_Types::Unsigned_8; 
			// byte0.toU16 * "256"[u16] + byte1.toU16	
			part : Base_Types::Unsigned_8 {
				redefines value = byte0.value + byte1.value;
			}
				
		}
		
		
		#gumbofunction calc def frame_is_wellformed_eth2 { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			valid_frame_ethertype(aframe) and valid_frame_dst_addr(aframe)
		}
		
		#gumbofunction calc def valid_frame_ethertype {  in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			frame_has_ipv4(aframe) or frame_has_arp(aframe) or frame_has_ipv6(aframe)
		}
		
		#gumbofunction calc def valid_frame_dst_addr {  in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			not (aframe.value#(0) == "0"[u8] and 
				 aframe.value#(2) == "0"[u8] and 
				 aframe.value#(3) == "0"[u8] and 
				 aframe.value#(4) == "0"[u8] and 
				 aframe.value#(5) == "0"[u8])
		}
		
		#gumbofunction calc def frame_has_ipv4 { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			aframe.value#(12) == "8"[u8] and aframe.value#(13) == "0"[u8]
		}
		
		#gumbofunction calc def frame_has_ipv6 { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			aframe.value#(12) == "134"[u8] and aframe.value#(13) == "221"[u8]
		}
		
		#gumbofunction calc def frame_has_arp { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			aframe.value#(12) == "8"[u8] and aframe.value#(13) == "6"[u8]
		}
		
		#gumbofunction calc def arp_has_ipv4 { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			aframe.value#(16) == "8"[u8] and aframe.value#(17) == "0"[u8]
		}
		
		#gumbofunction calc def arp_has_ipv6 { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			aframe.value#(16) == "134"[u8] and aframe.value#(17) == "0"[u8]
		}
		#gumbofunction calc def valid_arp_ptype { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			arp_has_ipv4(aframe) or arp_has_ipv6(aframe)
		}
		
		#gumbofunction calc def valid_arp_op { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			aframe.value#(20) == "0"[u8] and (aframe.value#(21) == "1"[u8] or aframe.value#(21) == "2")
		}
		
		#gumbofunction calc def valid_arp_htype { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			aframe.value#(14) == "0"[u8] and aframe.value#(15) == "1"[u8] 
		}
		
		#gumbofunction #gumbofunction calc def wellformed_arp_frame { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			valid_arp_op(aframe) and valid_arp_htype(aframe) and valid_arp_ptype(aframe)
		}
		
		calc def valid_ipv4_length { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			val = aframe.value;
			two_bytes_to_u16(val#(16), val#(17)).value <= "9000"[u16]
		}
		
		#gumbofunction #gumbofunction calc def valid_ipv4_protocol2 { in aframe : Collections::Array { :>> elements : ScalarValues::Integer[0..1600]; } return : ScalarValues::Boolean; 
			  aframe#(23) == "0"[u8] or 
			  aframe#(23) == "0"[u8] or 
			  aframe#(23) == "2"[u8] or 
			  aframe#(23) == "6"[u8]  
			  
		}
		
		#gumbofunction calc def valid_ipv4_protocol3 { in aframe : Collections::Array { :>> elements : ScalarValues::Integer[0..1600]; } return : ScalarValues::Boolean; 
			  aframe#(23) == "0"[u8] or 
			  aframe#(23) == "0"[u8] or 
			  aframe#(23) == "2"[u8] or 
			  aframe#(23) == "6"[u8]  
			  
		}
		
		#gumbofunction calc def valid_ipv4_protocol { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			 (aframe.value#(23) == "0"[u8] or 
			  aframe.value#(23) == "0"[u8] or 
			  aframe.value#(23) == "2"[u8] or 
			  aframe.value#(23) == "6"[u8] or 
			  aframe.value#(23) == "17"[u8] or
			  aframe.value#(23) == "43"[u8] or
			  aframe.value#(23) == "44"[u8] or
			  aframe.value#(23) == "58"[u8] or
			  aframe.value#(23) == "59"[u8] or
			  aframe.value#(23) == "60"[u8])
		}
		
		#gumbofunction calc def wellformed_ipv4_frame { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			valid_ipv4_protocol(aframe) and valid_ipv4_length(aframe)
		}
		
		#gumbofunction calc def ipv4_is_tcp { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			aframe.value#(23) == "6"[u8]
		}
		
		#gumbofunction calc def ipv4_is_udp { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			aframe.value#(23) == "17"[u8]
		}
		
		comment /* These next two definitions show the difference between returning a part and a data value */
		#gumbofunction calc def tcp_is_valid { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			two_bytes_to_u16(aframe.value#(36), aframe.value#(37)) == TCP_ALLOWED_PORTS()#(0)
		}
		
		#gumbofunction calc def udp_is_valid_port {  in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			two_bytes_to_u16(aframe#(36), aframe.value#(37)) == UDP_ALLOWED_PORTS().value#(0)
		}
		
		comment /* I need to learn the semantic difference between /to/ and /until/ */
		#gumbofunction calc def frame_has_ipv4_udp_on_allowed_port_quant { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			Exists(0, UDP_ALLOWED_PORTS().value.dimensions, 
				{in i; UDP_ALLOWED_PORTS().value#(i) == two_bytes_to_u16(aframe.value#(36), aframe.value#(37))})
		}
		
		#gumbofunction calc def frame_has_ipv4_tcp_on_allowed_port_quant { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			Exists(0, TCP_ALLOWED_PORTS().dimensions, 
				{in i; TCP_ALLOWED_PORTS()#(i) == two_bytes_to_u16(aframe.value#(36), aframe.value#(37))})
		}
		
		#gumbofunction calc def valid_arp { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			frame_is_wellformed_eth2(aframe) and frame_has_arp(aframe) and wellformed_arp_frame(aframe)
		}
		
		#gumbofunction calc def valid_ipv4_tcp { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			frame_is_wellformed_eth2(aframe) and frame_has_ipv4(aframe) and wellformed_ipv4_frame(aframe) and ipv4_is_tcp(aframe)
		}
		
		#gumbofunction calc def valid_ipv4_udp { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			frame_is_wellformed_eth2(aframe) and frame_has_ipv4(aframe) and wellformed_ipv4_frame(aframe) and ipv4_is_udp(aframe)
		}
		
		#gumbofunction calc def valid_ipv4_tcp_port { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			valid_ipv4_tcp(aframe) and frame_has_ipv4_tcp_on_allowed_port_quant(aframe)
		}
		
		#gumbofunction calc def valid_ipv4_udp_port { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			valid_ipv4_udp(aframe) and frame_has_ipv4_udp_on_allowed_port_quant(aframe)
		}
		
		#gumbofunction calc def allow_outbound_frame { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			valid_arp(aframe) or valid_ipv4_tcp_port(aframe) or valid_ipv4_udp_port(aframe)
		}		
		
		
		#compute #guarantee constraint hlr_05_rx0_can_send_arp {
				(HasEvent(ethernetFramesRxIn0) and valid_arp(ethernetFramesRxIn0.type)) implies
					(HasEvent(ethernetFramesRxOut0) and ethernetFramesRxIn0 == ethernetFramesRxOut0) 
					// what does this mean in GUMBO? I'm assuming that the data on the ports should be equivalent
			}
			
		#compute #guarantee constraint hlr_06_rx0_can_send_ipv4_tcp {
				((HasEvent(ethernetFramesRxIn0) and valid_ipv4_tcp_port(ethernetFramesRxIn0.type)) implies
            		(HasEvent(ethernetFramesRxOut0) and (ethernetFramesRxIn0 == ethernetFramesRxOut0)))
			}
			
		#compute #guarantee constraint hlr_13_rx0_can_send_ipv4_udp {
			(HasEvent(ethernetFramesRxIn0) and valid_ipv4_udp_port(ethernetFramesRxIn0.type)) implies 
        		(HasEvent(ethernetFramesRxOut0) and (ethernetFramesRxIn0 == ethernetFramesRxOut0))
		}
		
		#compute #guarantee constraint hlr_15_rx0_disallow {
       		(HasEvent(ethernetFramesRxIn0) and not allow_outbound_frame(ethernetFramesRxIn0.type)) implies
         		NoSend(ethernetFramesRxOut0)
        }
        
        #compute #compute #guarantee constraint hlr_17_rx0_no_input {
       		(not HasEvent(ethernetFramesRxIn0)) implies NoSend(ethernetFramesRxOut0)
        }
        
        #guarantee constraint hlr_05_rx1_can_send_arp {
			(HasEvent(ethernetFramesRxIn1) and valid_arp(ethernetFramesRxIn1.type)) implies
				(HasEvent(ethernetFramesRxOut1) and ethernetFramesRxIn1 == ethernetFramesRxOut1) 
		}
		
		#compute #guarantee constraint hlr_06_rx1_can_send_ipv4_tcp {
			((HasEvent(ethernetFramesRxIn1) and valid_ipv4_tcp_port(ethernetFramesRxIn1.type)) implies
        		(HasEvent(ethernetFramesRxOut1) and (ethernetFramesRxIn1 == ethernetFramesRxOut1)))
		}
		
		#compute #guarantee constraint hlr_13_rx1_can_send_ipv4_udp {
			(HasEvent(ethernetFramesRxIn1) and valid_ipv4_udp_port(ethernetFramesRxIn1.type)) implies 
        		(HasEvent(ethernetFramesRxOut1) and (ethernetFramesRxIn1 == ethernetFramesRxOut1))
		}
		
		#compute #guarantee constraint hlr_15_rx1_disallow {
       		(HasEvent(ethernetFramesRxIn1) and not allow_outbound_frame(ethernetFramesRxIn1.type)) implies
         		NoSend(ethernetFramesRxOut1)
        }
        
        #compute #guarantee constraint hlr_17_rx1_no_input {
       		(not HasEvent(ethernetFramesRxIn1)) implies NoSend(ethernetFramesRxOut1)
        }
        
        #compute #guarantee constraint hlr_05_rx2_can_send_arp {
			(HasEvent(ethernetFramesRxIn2) and valid_arp(ethernetFramesRxIn2.type)) implies
				(HasEvent(ethernetFramesRxOut2) and ethernetFramesRxIn0 == ethernetFramesRxOut2) 
				// what does this mean in GUMBO? I'm assuming that the data on the ports should be equivalent
		}
		
		#compute #guarantee constraint hlr_06_rx2_can_send_ipv4_tcp {
			((HasEvent(ethernetFramesRxIn2) and valid_ipv4_tcp_port(ethernetFramesRxIn2.type)) implies
        		(HasEvent(ethernetFramesRxOut2) and (ethernetFramesRxIn0 == ethernetFramesRxOut2)))
		}
		
		#compute #guarantee constraint hlr_13_rx2_can_send_ipv4_udp {
			(HasEvent(ethernetFramesRxIn2) and valid_ipv4_udp_port(ethernetFramesRxIn2.type)) implies 
        		(HasEvent(ethernetFramesRxOut2) and (ethernetFramesRxIn2 == ethernetFramesRxOut2))
		}
		
		#compute #guarantee constraint hlr_15_rx2_disallow {
       		(HasEvent(ethernetFramesRxIn2) and not allow_outbound_frame(ethernetFramesRxIn2.type)) implies
         		NoSend(ethernetFramesRxOut2)
        }
        
        #compute #guarantee constraint hlr_17_rx2_no_input {
       		(not HasEvent(ethernetFramesRxIn2)) implies NoSend(ethernetFramesRxOut2)
        }
        
        #compute #guarantee constraint hlr_05_rx3_can_send_arp {
			(HasEvent(ethernetFramesRxIn3) and valid_arp(ethernetFramesRxIn3.type)) implies
				(HasEvent(ethernetFramesRxOut3) and ethernetFramesRxIn3 == ethernetFramesRxOut3) 
				// what does this mean in GUMBO? I'm assuming that the data on the ports should be equivalent
		}
		
		#compute #guarantee constraint hlr_06_rx3_can_send_ipv4_tcp {
			((HasEvent(ethernetFramesRxIn3) and valid_ipv4_tcp_port(ethernetFramesRxIn3.type)) implies
        		(HasEvent(ethernetFramesRxOut3) and (ethernetFramesRxIn0 == ethernetFramesRxOut3)))
		}
		
		#compute #guarantee constraint hlr_13_rx3_can_send_ipv4_udp {
			(HasEvent(ethernetFramesRxIn3) and valid_ipv4_udp_port(ethernetFramesRxIn3.type)) implies 
        		(HasEvent(ethernetFramesRxOut3) and (ethernetFramesRxIn3 == ethernetFramesRxOut3))
		}
		
		#compute #guarantee constraint hlr_15_rx3_disallow {
       		(HasEvent(ethernetFramesRxIn3) and not allow_outbound_frame(ethernetFramesRxIn3.type)) implies
         		NoSend(ethernetFramesRxOut3)
        }
        
        #compute #guarantee constraint hlr_17_rx3_no_input {
       		(not HasEvent(ethernetFramesRxIn3)) implies NoSend(ethernetFramesRxOut3)
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
			
		#gumbofunction calc def TCP_ALLOWED_PORTS { return : u16Array::myArray;
			("5760"[u16])
		}
		
		#gumbofunction calc def UDP_ALLOWED_PORTS { //return : Collections::Array; 
			//("68"[u16]) 
			return : u16Array;
			part x : u16Array {
				:>> value = ("68"[u16]);
			}
		}
		
		#gumbofunction calc def two_bytes_to_u16{ in byte0 : Base_Types::Unsigned_8 ; in byte1 : Base_Types::Unsigned_8; return : Base_Types::Unsigned_16; 
			// byte0.toU16 * "256"[u16] + byte1.toU16	
				
		}
		
		#gumbofunction calc def frame_is_wellformed_eth2 { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			valid_frame_ethertype(aframe) and valid_frame_dst_addr(aframe)
		}
		
		#gumbofunction calc def valid_frame_ethertype {  in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			frame_has_ipv4(aframe) or frame_has_arp(aframe) or frame_has_ipv6(aframe)
		}
		
		#gumbofunction calc def valid_frame_dst_addr {  in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			not (aframe.value#(0) == "0"[u8] and 
				 aframe.value#(2) == "0"[u8] and 
				 aframe.value#(3) == "0"[u8] and 
				 aframe.value#(4) == "0"[u8] and 
				 aframe.value#(5) == "0"[u8])
		}
		
		#gumbofunction calc def frame_has_ipv4 { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			aframe.value#(12) == "8"[u8] and aframe.value#(13) == "0"[u8]
		}
		
		#gumbofunction calc def frame_has_ipv6 { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			aframe.value#(12) == "134"[u8] and aframe.value#(13) == "221"[u8]
		}
		
		#gumbofunction #gumbofunction calc def frame_has_arp { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			aframe.value#(12) == "8"[u8] and aframe.value#(13) == "6"[u8]
		}
		
		#gumbofunction calc def arp_has_ipv4 { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			aframe.value#(16) == "8"[u8] and aframe.value#(17) == "0"[u8]
		}
		
		#gumbofunction calc def arp_has_ipv6 { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			aframe.value#(16) == "134"[u8] and aframe.value#(17) == "0"[u8]
		}
		
		#gumbofunction calc def valid_arp_ptype { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			arp_has_ipv4(aframe) or arp_has_ipv6(aframe)
		}
		
		#gumbofunction calc def valid_arp_op { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			aframe.value#(20) == "0"[u8] and (aframe.value#(21) == "1"[u8] or aframe.value#(21) == "2")
		}
		
		#gumbofunction calc def valid_arp_htype { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			aframe.value#(14) == "0"[u8] and aframe.value#(15) == "1"[u8] 
		}
		
		#gumbofunction calc def wellformed_arp_frame { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			valid_arp_op(aframe) and valid_arp_htype(aframe) and valid_arp_ptype(aframe)
		}
		
		#gumbofunction calc def valid_ipv4_length { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			val = aframe.value;
			two_bytes_to_u16(val#(16), val#(17)).value <= "9000"[u16]
		}
		
		#gumbofunction calc def valid_ipv4_protocol { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			 (aframe.value#(23) == "0"[u8] or 
			  aframe.value#(23) == "0"[u8] or 
			  aframe.value#(23) == "2"[u8] or 
			  aframe.value#(23) == "6"[u8] or 
			  aframe.value#(23) == "17"[u8] or
			  aframe.value#(23) == "43"[u8] or
			  aframe.value#(23) == "44"[u8] or
			  aframe.value#(23) == "58"[u8] or
			  aframe.value#(23) == "59"[u8] or
			  aframe.value#(23) == "60"[u8])
		}
		
		#gumbofunction calc def wellformed_ipv4_frame { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			valid_ipv4_protocol(aframe) and valid_ipv4_length(aframe)
		}
		
		#gumbofunction calc def ipv4_is_tcp { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			aframe.value#(23) == "6"[u8]
		}
		
		#gumbofunction calc def ipv4_is_udp { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			aframe.value#(23) == "17"[u8]
		}
		
		comment /* These next two definitions show the difference between returning a part and a data value */
		#gumbofunction calc def tcp_is_valid { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			two_bytes_to_u16(aframe.value#(36), aframe.value#(37)) == TCP_ALLOWED_PORTS()#(0)
		}
		
		#gumbofunction calc def udp_is_valid_port {  in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			two_bytes_to_u16(aframe.value#(36), aframe.value#(37)) == UDP_ALLOWED_PORTS().value#(0)
		}
		
		comment /* I need to learn the semantic difference between /to/ and /until/ */
		#gumbofunction calc def frame_has_ipv4_udp_on_allowed_port_quant { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			Exists(0, UDP_ALLOWED_PORTS().value.dimensions, 
				{in i; UDP_ALLOWED_PORTS().value#(i) == two_bytes_to_u16(aframe.value#(36), aframe.value#(37))})
		}
		
		#gumbofunction calc def frame_has_ipv4_tcp_on_allowed_port_quant { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			Exists(0, TCP_ALLOWED_PORTS().dimensions, 
				{in i; TCP_ALLOWED_PORTS()#(i) == two_bytes_to_u16(aframe.value#(36), aframe.value#(37))})
		}
		
		#gumbofunction calc def valid_arp { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			frame_is_wellformed_eth2(aframe) and frame_has_arp(aframe) and wellformed_arp_frame(aframe)
		}
		
		#gumbofunction calc def valid_ipv4_tcp { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			frame_is_wellformed_eth2(aframe) and frame_has_ipv4(aframe) and wellformed_ipv4_frame(aframe) and ipv4_is_tcp(aframe)
		}
		
		#gumbofunction calc def valid_ipv4_udp { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			frame_is_wellformed_eth2(aframe) and frame_has_ipv4(aframe) and wellformed_ipv4_frame(aframe) and ipv4_is_udp(aframe)
		}
		
		#gumbofunction calc def valid_ipv4_tcp_port { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			valid_ipv4_tcp(aframe) and frame_has_ipv4_tcp_on_allowed_port_quant(aframe)
		}
		
		calc def valid_ipv4_udp_port { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			valid_ipv4_udp(aframe) and frame_has_ipv4_udp_on_allowed_port_quant(aframe)
		}
		
		#gumbofunction calc def allow_outbound_frame { in aframe : RawEthernetMessage; return : ScalarValues::Boolean; 
			valid_arp(aframe) or valid_ipv4_tcp_port(aframe) or valid_ipv4_udp_port(aframe)
		}		
		
		#compute #guarantee constraint hlr_05_tx0_can_send_arp {
			(HasEvent(ethernetFramesTxIn0) and valid_arp(ethernetFramesTxIn0.type)) implies
				(HasEvent(ethernetFramesTxOut0) and ethernetFramesTxIn0 == ethernetFramesTxOut0) 
				// what does this mean in GUMBO? I'm assuming that the data on the ports should be equivalent
		}
		
		#compute #guarantee constraint hlr_06_tx0_can_send_ipv4_tcp {
			((HasEvent(ethernetFramesTxIn0) and valid_ipv4_tcp_port(ethernetFramesTxIn0.type)) implies
        		(HasEvent(ethernetFramesTxOut0) and (ethernetFramesTxIn0 == ethernetFramesTxOut0)))
		}
		
		#compute #guarantee constraint hlr_13_rx0_can_send_ipv4_udp {
			(HasEvent(ethernetFramesTxIn0) and valid_ipv4_udp_port(ethernetFramesTxIn0.type)) implies 
        		(HasEvent(ethernetFramesTxOut0) and (ethernetFramesTxIn0 == ethernetFramesTxOut0))
		}
		
		#compute #guarantee constraint hlr_15_rx0_disallow {
       		(HasEvent(ethernetFramesTxIn0) and not allow_outbound_frame(ethernetFramesTxIn0.type)) implies
         		NoSend(ethernetFramesTxOut0)
        }
        
        #compute #guarantee constraint hlr_17_tx0_no_input {
       		(not HasEvent(ethernetFramesTxIn0)) implies NoSend(ethernetFramesTxOut0)
        }
        
        #compute #guarantee constraint hlr_05_tx1_can_send_arp {
			(HasEvent(ethernetFramesTxIn1) and valid_arp(ethernetFramesTxIn1.type)) implies
				(HasEvent(ethernetFramesTxOut1) and ethernetFramesTxIn1 == ethernetFramesTxOut1) 
		}
		
		#compute #guarantee constraint hlr_06_tx1_can_send_ipv4_tcp {
			((HasEvent(ethernetFramesTxIn1) and valid_ipv4_tcp_port(ethernetFramesTxIn1.type)) implies
        		(HasEvent(ethernetFramesTxOut1) and (ethernetFramesTxIn1 == ethernetFramesTxOut1)))
		}
		
		#compute #guarantee constraint hlr_13_tx1_can_send_ipv4_udp {
			(HasEvent(ethernetFramesTxIn1) and valid_ipv4_udp_port(ethernetFramesTxIn1.type)) implies 
        		(HasEvent(ethernetFramesTxOut1) and (ethernetFramesTxIn1 == ethernetFramesTxOut1))
		}
		
		#compute #guarantee constraint hlr_15_tx1_disallow {
       		(HasEvent(ethernetFramesTxIn1) and not allow_outbound_frame(ethernetFramesTxIn1.type)) implies
         		NoSend(ethernetFramesTxOut1)
        }
        
        #compute #guarantee constraint hlr_17_tx1_no_input {
       		(not HasEvent(ethernetFramesTxIn1)) implies NoSend(ethernetFramesTxOut1)
        }
        
        #compute #guarantee constraint hlr_05_tx2_can_send_arp {
			(HasEvent(ethernetFramesTxIn2) and valid_arp(ethernetFramesTxIn2.type)) implies
				(HasEvent(ethernetFramesTxOut2) and ethernetFramesTxIn0 == ethernetFramesTxOut2) 
				// what does this mean in GUMBO? I'm assuming that the data on the ports should be equivalent
		}
		
		#compute #guarantee constraint hlr_06_rx2_can_send_ipv4_tcp {
			((HasEvent(ethernetFramesTxIn2) and valid_ipv4_tcp_port(ethernetFramesTxIn2.type)) implies
        		(HasEvent(ethernetFramesTxOut2) and (ethernetFramesTxIn0 == ethernetFramesTxOut2)))
		}
		
		#compute #guarantee constraint hlr_13_tx2_can_send_ipv4_udp {
			(HasEvent(ethernetFramesTxIn2) and valid_ipv4_udp_port(ethernetFramesTxIn2.type)) implies 
        		(HasEvent(ethernetFramesTxOut2) and (ethernetFramesTxIn2 == ethernetFramesTxOut2))
		}
		
		#compute #guarantee constraint hlr_15_tx2_disallow {
       		(HasEvent(ethernetFramesTxIn2) and not allow_outbound_frame(ethernetFramesTxIn2.type)) implies
         		NoSend(ethernetFramesTxOut2)
        }
        
        #compute #guarantee constraint hlr_17_tx2_no_input {
       		(not HasEvent(ethernetFramesTxIn2)) implies NoSend(ethernetFramesTxOut2)
        }
        
        #compute #guarantee constraint hlr_05_tx3_can_send_arp {
			(HasEvent(ethernetFramesTxIn3) and valid_arp(ethernetFramesTxIn3.type)) implies
				(HasEvent(ethernetFramesTxOut3) and ethernetFramesTxIn3 == ethernetFramesTxOut3) 
				// what does this mean in GUMBO? I'm assuming that the data on the ports should be equivalent
		}
		
		#compute #guarantee constraint hlr_06_tx3_can_send_ipv4_tcp {
			((HasEvent(ethernetFramesTxIn3) and valid_ipv4_tcp_port(ethernetFramesTxIn3.type)) implies
        		(HasEvent(ethernetFramesTxOut3) and (ethernetFramesTxIn0 == ethernetFramesTxOut3)))
		}
		
		#compute #guarantee constraint hlr_13_tx3_can_send_ipv4_udp {
			(HasEvent(ethernetFramesTxIn3) and valid_ipv4_udp_port(ethernetFramesTxIn3.type)) implies 
        		(HasEvent(ethernetFramesTxOut3) and (ethernetFramesTxIn3 == ethernetFramesTxOut3))
		}
		
		#compute #guarantee constraint hlr_15_tx3_disallow {
       		(HasEvent(ethernetFramesTxIn3) and not allow_outbound_frame(ethernetFramesTxIn3.type)) implies
         		NoSend(ethernetFramesTxOut3)
        }
        
        #compute #guarantee constraint hlr_17_tx3_no_input {
       		(not HasEvent(ethernetFramesTxIn3)) implies NoSend(ethernetFramesTxOut3)
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