package SW {
	public import HAMR::*;
	public import CASE_Scheduling::*;
	public import HAMR_AADL::*;
	
	part def u16Array :> Array {
		:>> Base_Type : Base_Types::Unsigned_16;
		:>> Dimensions = 1;
		:>> Data_Size = 2[byte];
		:>> Array_Size_Kind = Array_Size_Kinds::Fixed;
	}
	
	part def RawEthernetMessage :> Array {
		redefines Base_Type : Base_Types::Unsigned_8;
		:>> Dimensions = 1600;
		:>> Data_Size = 1600 [B];
		:>> Array_Size_Kind = Fixed; 
	}
	
	part def EthIpUdpHeaders :> Array {
		:>> Base_Type : Base_Types::Unsigned_8;
		:>> Dimensions = 42;
		:>> Data_Size = 42 [byte];
		:>> Array_Size_Kind = Fixed;
	}
	
	part def UdpPayload :> Array {
		:>> Base_Type : Base_Types::Unsigned_8;
		:>> Dimensions = 1558;
		:>> Data_Size = 1558[byte];
		:>> Array_Size_Kind = Fixed;
	}
	
	part def UdpFrame :> Data {
		part headers : EthIpUdpHeaders;
		part payload : UdpPayload;
	}
	
	part def SizedEthernetMessage :> Data {
		part amessage : RawEthernetMessage;
		part sz : Base_Types::Unsigned_16;
	}
	
	part def LowLevelEthernetDriver :> Thread {
		port EthernetFramesRx0: EventDataPort {out :> type : RawEthernetMessage; }
		port EthernetFramesRx1: EventDataPort {out :> type : RawEthernetMessage; } 
		port EthernetFramesRx2: EventDataPort {out :> type : RawEthernetMessage; } 
		port EthernetFramesRx3: EventDataPort {out :> type : RawEthernetMessage; }
		port EthernetFramesTx0: EventDataPort {in :> type : SizedEthernetMessage; }
		port EthernetFramesTx1: EventDataPort {in :> type : SizedEthernetMessage; }
		port EthernetFramesTx2: EventDataPort {in :> type : SizedEthernetMessage; }
		port EthernetFramesTx3: EventDataPort {in :> type : SizedEthernetMessage; }

		:>> Dispatch_Protocol = AADL_Project::Periodic;
		:>> Period : Time { ::> num = 1000[ms]; }
		:>> Compute_Execution_Time : Time_Range {
			:>> minimum : Time { :>> num = 300[ms]; }
			:>> maximum : Time { :>> num = 300[ms]; }
		}
		:>> Implementation_Language : HAMR::Implementation_Language = Rust;
		attribute Stack_size = 1048576 [byte];
	}
	
	part def LowLevelEthernetDriver_seL4 :> Process {
		port EthernetFramesRx0: EventDataPort {out :> type : RawEthernetMessage; }
		port EthernetFramesRx1: EventDataPort {out :> type : RawEthernetMessage; } 
		port EthernetFramesRx2: EventDataPort {out :> type : RawEthernetMessage; } 
		port EthernetFramesRx3: EventDataPort {out :> type : RawEthernetMessage; }
		port EthernetFramesTx0: EventDataPort {in :> type : SizedEthernetMessage; }
		port EthernetFramesTx1: EventDataPort {in :> type : SizedEthernetMessage; }
		port EthernetFramesTx2: EventDataPort {in :> type : SizedEthernetMessage; }
		port EthernetFramesTx3: EventDataPort {in :> type : SizedEthernetMessage; }	

		part lowLevelEthernetDriver : LowLevelEthernetDriver; 
		
		connection c1 : PortConnection 
			connect EthernetFramesTx0 to lowLevelEthernetDriver.EthernetFramesTx0;
		connection c2 : PortConnection 
			connect EthernetFramesTx1 to lowLevelEthernetDriver.EthernetFramesTx1;
		connection c3 : PortConnection 
			connect EthernetFramesTx2 to lowLevelEthernetDriver.EthernetFramesTx2;
		connection c4 : PortConnection 
			connect EthernetFramesTx3 to lowLevelEthernetDriver.EthernetFramesTx3;
		connection c5 : PortConnection
			connect lowLevelEthernetDriver.EthernetFramesRx0 to EthernetFramesRx0;
		connection c6 : PortConnection
			connect lowLevelEthernetDriver.EthernetFramesRx1 to EthernetFramesRx1;
		connection c7 : PortConnection
			connect lowLevelEthernetDriver.EthernetFramesRx2 to EthernetFramesRx2;
		connection c8 : PortConnection
			connect lowLevelEthernetDriver.EthernetFramesRx3 to EthernetFramesRx3;
			
		:>> Domain : CASE_Scheduling::Domain = 4; 
		attribute Implementation_Language : HAMR::Implementation_Language = Rust;
	}
	
	part def RxFirewall :> Thread {
		port EthernetFramesRxIn0 : EventDataPort { in :> type : RawEthernetMessage; }
		port EthernetFramesRxIn1 : EventDataPort { in :> type : RawEthernetMessage; }
		port EthernetFramesRxIn2 : EventDataPort { in :> type : RawEthernetMessage; }
		port EthernetFramesRxIn3 : EventDataPort { in :> type : RawEthernetMessage; }
		port VmmOut0 : EventDataPort { out :> type : RawEthernetMessage; }
		port VmmOut1 : EventDataPort { out :> type : RawEthernetMessage; }
		port VmmOut2 : EventDataPort { out :> type : RawEthernetMessage; }
		port VmmOut3 : EventDataPort { out :> type : RawEthernetMessage; }
		port MavlinkOut0 : EventDataPort { out :> type : UdpFrame; }
		port MavlinkOut1 : EventDataPort { out :> type : UdpFrame; }
		port MavlinkOut2 : EventDataPort { out :> type : UdpFrame; }
		port MavlinkOut3 : EventDataPort { out :> type : UdpFrame; }
		:>> Dispatch_Protocol = AADL_Project::Periodic;
		:>> Period : Time { ::> num = 1000[ms]; }
		:>> Compute_Execution_Time : Time_Range {
			:>> minimum : Time { :>> num = 300[ms]; }
			:>> maximum : Time { :>> num = 300[ms]; }
		}
		attribute stack_size : Memory_Properties::Stack_Size = 1048576[byte];
		:>> Implementation_Language = Rust;			
			
		language "GUMBO" /*{ 
			functions
				def TCP_ALLOWED_PORTS(): u16Array := (5760[u16]);
          
          		def UDP_ALLOWED_PORTS(): u16Array := (68[u16]);
          
          
		        def two_bytes_to_u16(byte0: Base_Types::Unsigned_8, byte1: Base_Types::Unsigned_8): Base_Types::Unsigned_16 :=
		            toU16(byte0) * 256[u16] + toU16(byte1); 
		             
		        def frame_is_wellformed_eth2(frame: RawEthernetMessage): Base_Types::Boolean :=
           			valid_frame_ethertype(frame) & valid_frame_dst_addr(frame);

         		def valid_frame_ethertype(frame: RawEthernetMessage): Base_Types::Boolean :=
            		frame_has_ipv4(frame) | frame_has_arp(frame) | frame_has_ipv6(frame);     
		             
		        def valid_frame_dst_addr(frame: RawEthernetMessage): Base_Types::Boolean :=
	              not ((frame[0] == 0[u8]) &
					   (frame[1] == 0[u8]) &
					   (frame[2] == 0[u8]) &
					   (frame[3] == 0[u8]) &
					   (frame[4] == 0[u8]) &
					   (frame[5] == 0[u8]));
	
	            def frame_has_ipv4(frame: RawEthernetMessage): Base_Types::Boolean := 
	                frame[12] == 8[u8] & frame[13] == 0[u8];
	          
	            def frame_has_ipv6(frame: RawEthernetMessage): Base_Types::Boolean :=
	                frame[12] == 134[u8] & frame[13] == 221[u8];
	                    
	          	def frame_has_arp(frame: RawEthernetMessage): Base_Types::Boolean := 
	                frame[12] == 8[u8] & frame[13] == 6[u8];
	
	            def arp_has_ipv4(frame: RawEthernetMessage): Base_Types::Boolean := 
	                frame[16] == 8[u8] & frame[17] == 0[u8];
	          
	            def arp_has_ipv6(frame: RawEthernetMessage): Base_Types::Boolean :=
	                frame[16] == 134[u8] & frame[17] == 221[u8];
	
		        def valid_arp_ptype(frame: RawEthernetMessage): Base_Types::Boolean :=
	   		    	arp_has_ipv4(frame) | arp_has_ipv6(frame);
	
		        def valid_arp_op(frame: RawEthernetMessage): Base_Types::Boolean :=
	  		    	frame[20] == 0[u8] & (frame[21[] == 1[u8] | frame[21] == 2[u8]);
	
				def valid_arp_htype(frame: RawEthernetMessage): Base_Types::Boolean :=
			    	frame[14] == 0[u8] & frame[15] == 1[u8];
	
				def wellformed_arp_frame(frame: RawEthernetMessage): Base_Types::Boolean :=
			    	valid_arp_op(frame) & valid_arp_htype(frame) & valid_arp_ptype(frame);
	
		        def valid_ipv4_length(frame: RawEthernetMessage): Base_Types::Boolean :=
			        two_bytes_to_u16(frame[16],frame[17]) <= 9000[u16];     
		             
		        def valid_ipv4_protocol(frame: RawEthernetMessage): Base_Types::Boolean :=
				    (frame[23] == 0[u8])  |
					(frame[23] == 1[u8])  |
					(frame[23] == 2[u8])  |
					(frame[23] == 6[u8])  |
					(frame[23] == 17[u8]) |
					(frame[23] == 43[u8]) |
					(frame[23] == 44[u8]) |
					(frame[23] == 58[u8]) |
					(frame[23] == 59[u8]) |
					(frame[23] == 60[u8]);

				def valid_ipv4_vers_ihl(frame: RawEthernetMessage): Base_Types::Boolean :=
				    frame[14] == 69[u8];
				
				def wellformed_ipv4_frame(frame: RawEthernetMessage): Base_Types::Boolean :=
				    valid_ipv4_protocol(frame) & valid_ipv4_length(frame) & valid_ipv4_vers_ihl(frame);
				
				def ipv4_is_tcp(frame: RawEthernetMessage): Base_Types::Boolean :=
					frame[23] == 6[u8];
				
				def ipv4_is_udp(frame: RawEthernetMessage): Base_Types::Boolean :=
					frame[23] == 17[u8];
					
				def tcp_is_valid_port(frame: RawEthernetMessage): Base_Types::Boolean :=
					two_bytes_to_u16(frame[36],frame[37]) == TCP_ALLOWED_PORTS()[0];

				def udp_is_valid_port(frame: RawEthernetMessage): Base_Types::Boolean :=
					two_bytes_to_u16(frame[36],frame[37]) == UDP_ALLOWED_PORTS()[0];

  				def udp_is_mavlink_port(frame: RawEthernetMessage): Base_Types::Boolean :=
	  				two_bytes_to_u16(frame[34],frame[35]) == 14550;

  				def frame_has_ipv4_tcp_on_allowed_port_quant(frame: RawEthernetMessage): Base_Types::Boolean :=
    				exists(0 .. size(TCP_ALLOWED_PORTS()), {in i;
        						TCP_ALLOWED_PORTS()[i] == two_bytes_to_u16(frame[36], frame[37])});

  				def frame_has_ipv4_udp_on_allowed_port_quant(frame: RawEthernetMessage): Base_Types::Boolean :=
    				exists(0  .. size(UDP_ALLOWED_PORTS()), {in i;
        					UDP_ALLOWED_PORTS()[i] == two_bytes_to_u16(frame[36], frame[37])});    
       		
	            def valid_arp(frame: RawEthernetMessage): Base_Types::Boolean :=
	          		frame_is_wellformed_eth2(frame) & frame_has_arp(frame) & wellformed_arp_frame(frame);
	            
	            def valid_ipv4_tcp(frame: RawEthernetMessage): Base_Types::Boolean :=
	                frame_is_wellformed_eth2(frame) & frame_has_ipv4(frame) & wellformed_ipv4_frame(frame) & ipv4_is_tcp(frame);
	            
	            def valid_ipv4_udp(frame: RawEthernetMessage): Base_Types::Boolean :=
	            	frame_is_wellformed_eth2(frame) & frame_has_ipv4(frame) & wellformed_ipv4_frame(frame) & ipv4_is_udp(frame);
	
				def valid_ipv4_tcp_port(frame: RawEthernetMessage): Base_Types::Boolean :=
					valid_ipv4_tcp(frame) & frame_has_ipv4_tcp_on_allowed_port_quant(frame);
	
				def valid_ipv4_udp_port(frame: RawEthernetMessage): Base_Types::Boolean :=
	            	valid_ipv4_udp(frame) & frame_has_ipv4_udp_on_allowed_port_quant(frame) & not udp_is_mavlink_port(frame);
	
	          	def valid_ipv4_udp_mavlink(frame: RawEthernetMessage): Base_Types::Boolean :=
	            	valid_ipv4_udp(frame) & udp_is_mavlink_port(frame);
	            
	          	def allow_outbound_frame(frame: RawEthernetMessage): Base_Types::Boolean :=
	            	valid_arp(frame) |
	            	valid_ipv4_udp_mavlink(frame) |
	            	valid_ipv4_udp_port(frame);      
			  
			  	def input_eq_mav_output_headers(frame: RawEthernetMessage, headers: EthIpUdpHeaders): Base_Types::Boolean :=
	            	forAll(0 .. size(headers), {in i => headers[i] == frame[i]});
	
			 	def input_eq_mav_output_payload(frame: RawEthernetMessage, payload: UdpPayload, headers: EthIpUdpHeaders): Base_Types::Boolean :=
					forAll(0 .. size(payload), {in i => frame[i + size(headers)] == payload[i]});
	
			  	def input_eq_mav_output(frame: RawEthernetMessage, output: UdpFrame): Base_Types::Boolean :=
			  		input_eq_mav_output_headers(frame, output.headers) & input_eq_mav_output_payload(frame, output.payload, output.headers);
	            
	      compute
	
		        guarantee hlr_05_rx0_can_send_arp_to_vmm:
		          ((HasEvent(EthernetFramesRxIn0) & valid_arp(EthernetFramesRxIn0)) implies 
		            (HasEvent(VmmOut0) & (EthernetFramesRxIn0 == VmmOut0) & NoSend(MavlinkOut0)));
		        guarantee hlr_18_rx0_can_send_mavlink_udp:
		          ((HasEvent(EthernetFramesRxIn0) & valid_ipv4_udp_mavlink(EthernetFramesRxIn0)) implies 
		            (HasEvent(MavlinkOut0) & input_eq_mav_output(EthernetFramesRxIn0, MavlinkOut0) & NoSend(VmmOut0)));
		        guarantee hlr_13_rx0_can_send_ipv4_udp:
		          ((HasEvent(EthernetFramesRxIn0) & valid_ipv4_udp_port(EthernetFramesRxIn0)) implies 
		            (HasEvent(VmmOut0) & (EthernetFramesRxIn0 == VmmOut0) & NoSend(MavlinkOut0)));
		        guarantee hlr_15_rx0_disallow:
			       (HasEvent(EthernetFramesRxIn0) & not allow_outbound_frame(EthernetFramesRxIn0)) implies 
			         (NoSend(VmmOut0) & NoSend(MavlinkOut0));
		        guarantee hlr_17_rx0_no_input:
		           (not HasEvent(EthernetFramesRxIn0) implies (NoSend(VmmOut0) & NoSend(MavlinkOut0)));
		
		        guarantee hlr_05_rx1_can_send_arp_to_vmm:
		          ((HasEvent(EthernetFramesRxIn1) & valid_arp(EthernetFramesRxIn1)) implies 
		            (HasEvent(VmmOut1) & (EthernetFramesRxIn1 == VmmOut1) & NoSend(MavlinkOut1)));
		        guarantee hlr_18_rx1_can_send_mavlink_udp:
		          ((HasEvent(EthernetFramesRxIn1) & valid_ipv4_udp_mavlink(EthernetFramesRxIn1)) implies 
		            (HasEvent(MavlinkOut1) & input_eq_mav_output(EthernetFramesRxIn1, MavlinkOut1) & NoSend(VmmOut1)));
		        guarantee hlr_13_rx1_can_send_ipv4_udp:
		          ((HasEvent(EthernetFramesRxIn1) & valid_ipv4_udp_port(EthernetFramesRxIn1)) implies 
		            (HasEvent(VmmOut1) & (EthernetFramesRxIn1 == VmmOut1) & NoSend(MavlinkOut1)));
		        guarantee hlr_15_rx1_disallow:
			       (HasEvent(EthernetFramesRxIn1) & not allow_outbound_frame(EthernetFramesRxIn1)) implies 
			         (NoSend(VmmOut1) & NoSend(MavlinkOut1));
		        guarantee hlr_17_rx1_no_input:
		           (not HasEvent(EthernetFramesRxIn1) implies (NoSend(VmmOut1) & NoSend(MavlinkOut1)));
		
		        guarantee hlr_05_rx2_can_send_arp_to_vmm:
		          ((HasEvent(EthernetFramesRxIn2) & valid_arp(EthernetFramesRxIn2)) implies 
		            (HasEvent(VmmOut2) & (EthernetFramesRxIn2 == VmmOut2) & NoSend(MavlinkOut2)));
		        guarantee hlr_18_rx2_can_send_mavlink_udp:
		          ((HasEvent(EthernetFramesRxIn2) & valid_ipv4_udp_mavlink(EthernetFramesRxIn2)) implies 
		            (HasEvent(MavlinkOut2) & input_eq_mav_output(EthernetFramesRxIn2, MavlinkOut2) & NoSend(VmmOut2)));
		        guarantee hlr_13_rx2_can_send_ipv4_udp:
		          ((HasEvent(EthernetFramesRxIn2) & valid_ipv4_udp_port(EthernetFramesRxIn2)) implies 
		            (HasEvent(VmmOut2) & (EthernetFramesRxIn2 == VmmOut2) & NoSend(MavlinkOut2)));
		        guarantee hlr_15_rx2_disallow:
			       (HasEvent(EthernetFramesRxIn2) & not allow_outbound_frame(EthernetFramesRxIn2)) implies 
			         (NoSend(VmmOut2) & NoSend(MavlinkOut2));
		        guarantee hlr_17_rx2_no_input:
		           (not HasEvent(EthernetFramesRxIn2) implies (NoSend(VmmOut2) & NoSend(MavlinkOut2)));
		
		        guarantee hlr_05_rx3_can_send_arp_to_vmm:
		          ((HasEvent(EthernetFramesRxIn3) & valid_arp(EthernetFramesRxIn3)) implies 
		            (HasEvent(VmmOut3) & (EthernetFramesRxIn3 == VmmOut3) & NoSend(MavlinkOut3)));
		        guarantee hlr_18_rx3_can_send_mavlink_udp:
		          ((HasEvent(EthernetFramesRxIn3) & valid_ipv4_udp_mavlink(EthernetFramesRxIn3)) implies 
		            (HasEvent(MavlinkOut3) & input_eq_mav_output(EthernetFramesRxIn3, MavlinkOut3) & NoSend(VmmOut3)));
		        guarantee hlr_13_rx3_can_send_ipv4_udp:
		          ((HasEvent(EthernetFramesRxIn3) & valid_ipv4_udp_port(EthernetFramesRxIn3)) implies 
		            (HasEvent(VmmOut3) & (EthernetFramesRxIn3 == VmmOut3) & NoSend(MavlinkOut3)));
		        guarantee hlr_15_rx3_disallow:
			       (HasEvent(EthernetFramesRxIn3) & not allow_outbound_frame(EthernetFramesRxIn3)) implies 
			         (NoSend(VmmOut3) & NoSend(MavlinkOut3));
		        guarantee hlr_17_rx3_no_input:
		           (not HasEvent(EthernetFramesRxIn3) implies (NoSend(VmmOut3) & NoSend(MavlinkOut3)));
		 }*/
	}
	
	part def RxFirewall_seL4 :> Process {
		port EthernetFramesRxIn0 : EventDataPort { in :> type : RawEthernetMessage; }
		port EthernetFramesRxIn1 : EventDataPort { in :> type : RawEthernetMessage; }
		port EthernetFramesRxIn2 : EventDataPort { in :> type : RawEthernetMessage; }
		port EthernetFramesRxIn3 : EventDataPort { in :> type : RawEthernetMessage; }
		port VmmOut0 : EventDataPort { out :> type : RawEthernetMessage; }
		port VmmOut1 : EventDataPort { out :> type : RawEthernetMessage; }
		port VmmOut2 : EventDataPort { out :> type : RawEthernetMessage; }
		port VmmOut3 : EventDataPort { out :> type : RawEthernetMessage; }
		port MavlinkOut0 : EventDataPort { out :> type : UdpFrame; }
		port MavlinkOut1 : EventDataPort { out :> type : UdpFrame; }
		port MavlinkOut2 : EventDataPort { out :> type : UdpFrame; }
		port MavlinkOut3 : EventDataPort { out :> type : UdpFrame; }

		part firewall : RxFirewall;
		connection c1 : PortConnection 
			connect EthernetFramesRxIn0 to firewall.EthernetFramesRxIn0;
		connection c2 : PortConnection 
			connect EthernetFramesRxIn1 to firewall.EthernetFramesRxIn1;
		connection c3 : PortConnection 
			connect EthernetFramesRxIn2 to firewall.EthernetFramesRxIn2;
		connection c4 : PortConnection 
			connect EthernetFramesRxIn3 to firewall.EthernetFramesRxIn3;	
		connection c5 : PortConnection
			connect firewall.VmmOut0 to VmmOut0;
		connection c6 : PortConnection
			connect firewall.VmmOut1 to VmmOut1;
		connection c7 : PortConnection
			connect firewall.VmmOut2 to VmmOut2;
		connection c8 : PortConnection
			connect firewall.VmmOut3 to VmmOut3;
		connection c9 : PortConnection
			connect firewall.MavlinkOut0 to MavlinkOut0;
		connection c10 : PortConnection
			connect firewall.MavlinkOut1 to MavlinkOut1;
		connection c11 : PortConnection
			connect firewall.MavlinkOut2 to MavlinkOut2;
		connection c12 : PortConnection
			connect firewall.MavlinkOut3 to MavlinkOut3;
			
		:>> Domain : CASE_Scheduling::Domain = 5;	
		attribute Implementation_Language : HAMR::Implementation_Language = Rust;
	}
	
	
	part def TxFirewall :> Thread {
		port EthernetFramesTxIn0 : EventDataPort { in :> type : RawEthernetMessage; }
		port EthernetFramesTxIn1 : EventDataPort { in :> type : RawEthernetMessage; }
		port EthernetFramesTxIn2 : EventDataPort { in :> type : RawEthernetMessage; }
		port EthernetFramesTxIn3 : EventDataPort { in :> type : RawEthernetMessage; }
		port EthernetFramesTxOut0 : EventDataPort { out :> type : SizedEthernetMessage; }
		port EthernetFramesTxOut1 : EventDataPort { out :> type : SizedEthernetMessage; }
		port EthernetFramesTxOut2 : EventDataPort { out :> type : SizedEthernetMessage; }
		port EthernetFramesTxOut3 : EventDataPort { out :> type : SizedEthernetMessage; }
	
		:>> Dispatch_Protocol = AADL_Project::Periodic;
		:>> Period : Time { ::> num = 1000[ms]; }
		:>> Compute_Execution_Time : Time_Range {
			:>> minimum : Time { ::> num = 300[ms]; }
			:>> maximum : Time { ::> num = 300[ms]; }
		}
		attribute stack_size : Memory_Properties::Stack_Size = 1048576[byte];
		:>> Implementation_Language : HAMR::Implementation_Language = Rust;
		
		language "GUMBO" /*{ 
			 functions
	          
	          def two_bytes_to_u16(byte0: Base_Types::Unsigned_8, byte1: Base_Types::Unsigned_8): Base_Types::Unsigned_16 :=
	              toU16(byte0) * 256[u16] + toU16(byte1); 
	          
	          def frame_is_wellformed_eth2(frame: RawEthernetMessage): Base_Types::Boolean :=
	            valid_frame_ethertype(frame) & valid_frame_dst_addr(frame);
	
	          def valid_frame_ethertype(frame: RawEthernetMessage): Base_Types::Boolean :=
	            frame_has_ipv4(frame) | frame_has_arp(frame) | frame_has_ipv6(frame);
	            
	          def valid_frame_dst_addr(frame: RawEthernetMessage): Base_Types::Boolean :=
	              not ((frame[0] == 0[u8]) &
					(frame[1] == 0[u8]) &
					(frame[2] == 0[u8]) &
					(frame[3] == 0[u8]) &
					(frame[4] == 0[u8]) &
					(frame[5] == 0[u8]));
	
	          def frame_has_ipv4(frame: RawEthernetMessage): Base_Types::Boolean := 
	              frame[12] == 8[u8] & frame[13] == 0[u8];
	          
	          def frame_has_ipv6(frame: RawEthernetMessage): Base_Types::Boolean :=
	              frame[12] == 134[u8] & frame[13] == 221[u8];
	                    
	          def frame_has_arp(frame: RawEthernetMessage): Base_Types::Boolean := 
	              frame[12] == 8[u8] & frame[13] == 6[u8];
	
	          def arp_has_ipv4(frame: RawEthernetMessage): Base_Types::Boolean := 
	              frame[16] == 8[u8] & frame[17] == 0[u8];
	          
	          def arp_has_ipv6(frame: RawEthernetMessage): Base_Types::Boolean :=
	              frame[16] == 134[u8] & frame[17] == 221[u8];
	
		      def valid_arp_ptype(frame: RawEthernetMessage): Base_Types::Boolean :=
			      arp_has_ipv4(frame) | arp_has_ipv6(frame);
	
		      def valid_arp_op(frame: RawEthernetMessage): Base_Types::Boolean :=
			      frame[20] == 0[u8] & (frame[21] == 1[u8] | frame[21] == 2[u8]);
	
		      def valid_arp_htype(frame: RawEthernetMessage): Base_Types::Boolean :=
			      frame[14] == 0[u8] & frame[15] == 1[u8];
	
		      def wellformed_arp_frame(frame: RawEthernetMessage): Base_Types::Boolean :=
			      valid_arp_op(frame) & valid_arp_htype(frame) & valid_arp_ptype(frame);
	
		      def ipv4_length(frame: RawEthernetMessage): Base_Types::Unsigned_16 :=
			      two_bytes_to_u16(frame[16],frame[17]);
	
		      def valid_ipv4_length(frame: RawEthernetMessage): Base_Types::Boolean :=
			      ipv4_length(frame) <= 9000[u16];
	
		      def valid_ipv4_protocol(frame: RawEthernetMessage): Base_Types::Boolean :=
			      (frame[23] == 0[u8])  |
				  (frame[23] == 1[u8])  |
				  (frame[23] == 2[u8])  |
				  (frame[23] == 6[u8])  |
				  (frame[23] == 17[u8]) |
				  (frame[23] == 43[u8]) |
				  (frame[23] == 44[u8]) |
				  (frame[23] == 58[u8]) |
				  (frame[23] == 59[u8]) |
				  (frame[23] == 60[u8]);
	
		      def valid_ipv4_vers_ihl(frame: RawEthernetMessage): Base_Types::Boolean :=
			      frame[14] == 69[u8];
	
		      def wellformed_ipv4_frame(frame: RawEthernetMessage): Base_Types::Boolean :=
			      valid_ipv4_protocol(frame) & valid_ipv4_length(frame) & valid_ipv4_vers_ihl(frame);
	            
	          --------------------------------------------------------------------------
	          -- TX Firewall
	          --------------------------------------------------------------------------
	          
	          def valid_ipv6(frame: RawEthernetMessage): Base_Types::Boolean :=
	            frame_is_wellformed_eth2(frame) & frame_has_ipv6(frame);
	            
	          def valid_arp(frame: RawEthernetMessage): Base_Types::Boolean :=
	            frame_is_wellformed_eth2(frame) & frame_has_arp(frame) & wellformed_arp_frame(frame);
	            
	          def valid_ipv4(frame: RawEthernetMessage): Base_Types::Boolean :=
	            frame_is_wellformed_eth2(frame) & frame_has_ipv4(frame) & wellformed_ipv4_frame(frame);
	
	          def valid_output_arp_size(output: SizedEthernetMessage): Base_Types::Boolean :=
	            output.sz == 64[u16];
	
	          def valid_output_ipv4_size(input: RawEthernetMessage, output: SizedEthernetMessage): Base_Types::Boolean :=
	            output.sz == (ipv4_length(input)+14[u16]);
	
	          def allow_outbound_frame(frame: RawEthernetMessage): Base_Types::Boolean :=
	            valid_arp(frame) |
	            valid_ipv4(frame);
	       
	      compute
	
	      	guarantee hlr_07_tx0_can_send_valid_arp:
	          ((HasEvent(EthernetFramesTxIn0) & valid_arp(EthernetFramesTxIn0)) implies 
	            (HasEvent(EthernetFramesTxOut0) & (EthernetFramesTxIn0 == EthernetFramesTxOut0.message) & valid_output_arp_size(EthernetFramesTxOut0)));
	        guarantee hlr_12_tx0_can_send_valid_ipv4:
	          ((HasEvent(EthernetFramesTxIn0) & valid_ipv4(EthernetFramesTxIn0)) implies 
	            (HasEvent(EthernetFramesTxOut0) & (EthernetFramesTxIn0 == EthernetFramesTxOut0.message) & valid_output_ipv4_size(EthernetFramesTxIn0, EthernetFramesTxOut0) ));
	        guarantee hlr_14_tx0_disallow:
		       (HasEvent(EthernetFramesTxIn0) & not allow_outbound_frame(EthernetFramesTxIn0)) implies 
		         (NoSend(EthernetFramesTxOut0));
	        guarantee hlr_16_tx0_no_input:
	           (not HasEvent(EthernetFramesTxIn0) implies NoSend(EthernetFramesTxOut0));
	
	        guarantee hlr_07_tx1_can_send_valid_arp:
	          ((HasEvent(EthernetFramesTxIn1) & valid_arp(EthernetFramesTxIn1)) implies 
	            (HasEvent(EthernetFramesTxOut1) & (EthernetFramesTxIn1 == EthernetFramesTxOut1.message) & valid_output_arp_size(EthernetFramesTxOut1) ));
	        guarantee hlr_12_tx1_can_send_valid_ipv4:
	          ((HasEvent(EthernetFramesTxIn1) & valid_ipv4(EthernetFramesTxIn1)) implies 
	            (HasEvent(EthernetFramesTxOut1) & (EthernetFramesTxIn1 == EthernetFramesTxOut1.message) & valid_output_ipv4_size(EthernetFramesTxIn1, EthernetFramesTxOut1) ));
	        guarantee hlr_14_tx1_disallow:
		       (HasEvent(EthernetFramesTxIn1) & not allow_outbound_frame(EthernetFramesTxIn1)) implies 
		         (NoSend(EthernetFramesTxOut1));
	        guarantee hlr_16_tx1_no_input:
	           (not HasEvent(EthernetFramesTxIn1) implies NoSend(EthernetFramesTxOut1));
	
	        guarantee hlr_07_tx2_can_send_valid_arp:
	          ((HasEvent(EthernetFramesTxIn2) & valid_arp(EthernetFramesTxIn2)) implies 
	            (HasEvent(EthernetFramesTxOut2) & (EthernetFramesTxIn2 == EthernetFramesTxOut2.message) & valid_output_arp_size(EthernetFramesTxOut2) ));
	        guarantee hlr_12_tx2_can_send_valid_ipv4:
	          ((HasEvent(EthernetFramesTxIn2) & valid_ipv4(EthernetFramesTxIn2)) implies 
	            (HasEvent(EthernetFramesTxOut2) & (EthernetFramesTxIn2 == EthernetFramesTxOut2.message) & valid_output_ipv4_size(EthernetFramesTxIn2, EthernetFramesTxOut2) ));
	        guarantee hlr_14_tx2_disallow:
		       (HasEvent(EthernetFramesTxIn2) & not allow_outbound_frame(EthernetFramesTxIn2)) implies 
		         (NoSend(EthernetFramesTxOut2));
	        guarantee hlr_16_tx2_no_input:
	           (not HasEvent(EthernetFramesTxIn2) implies NoSend(EthernetFramesTxOut2));
	
	        guarantee hlr_07_tx3_can_send_valid_arp:
	          ((HasEvent(EthernetFramesTxIn3) & valid_arp(EthernetFramesTxIn3)) implies 
	            (HasEvent(EthernetFramesTxOut3) & (EthernetFramesTxIn3 == EthernetFramesTxOut3.message) & valid_output_arp_size(EthernetFramesTxOut3) ));
	        guarantee hlr_12_tx3_can_send_valid_ipv4:
	          ((HasEvent(EthernetFramesTxIn3) & valid_ipv4(EthernetFramesTxIn3)) implies 
	            (HasEvent(EthernetFramesTxOut3) & (EthernetFramesTxIn3 == EthernetFramesTxOut3.message) & valid_output_ipv4_size(EthernetFramesTxIn3, EthernetFramesTxOut3) ));
	        guarantee hlr_14_tx3_disallow:
		       (HasEvent(EthernetFramesTxIn3) & not allow_outbound_frame(EthernetFramesTxIn3)) implies 
		         (NoSend(EthernetFramesTxOut3));
	        guarantee hlr_16_tx3_no_input:
	           (not HasEvent(EthernetFramesTxIn3) implies NoSend(EthernetFramesTxOut3));
	           	  
		}*/
	}
	
	
	part def TxFirewall_seL4 :> Process {
		port EthernetFramesTxIn0 : EventDataPort { in :> type : RawEthernetMessage; }
		port EthernetFramesTxIn1 : EventDataPort { in :> type : RawEthernetMessage; }
		port EthernetFramesTxIn2 : EventDataPort { in :> type : RawEthernetMessage; }
		port EthernetFramesTxIn3 : EventDataPort { in :> type : RawEthernetMessage; }
		port EthernetFramesTxOut0 : EventDataPort { out :> type : SizedEthernetMessage; }
		port EthernetFramesTxOut1 : EventDataPort { out :> type : SizedEthernetMessage; }
		port EthernetFramesTxOut2 : EventDataPort { out :> type : SizedEthernetMessage; }
		port EthernetFramesTxOut3 : EventDataPort { out :> type : SizedEthernetMessage; }
	
		part firewall : TxFirewall_seL4;
		
		connection c1 : PortConnection 
			connect EthernetFramesTxIn0 to firewall.EthernetFramesTxIn0;
		connection c2 : PortConnection 
			connect EthernetFramesTxIn1 to firewall.EthernetFramesTxIn1;
		connection c3 : PortConnection 
			connect EthernetFramesTxIn2 to firewall.EthernetFramesTxIn2;
		connection c4 : PortConnection 
			connect EthernetFramesTxIn3 to firewall.EthernetFramesTxIn3;	
		connection c5 : PortConnection
			connect firewall.EthernetFramesTxOut0 to EthernetFramesTxOut0;
		connection c6 : PortConnection
			connect firewall.EthernetFramesTxOut1 to EthernetFramesTxOut1;
		connection c7 : PortConnection
			connect firewall.EthernetFramesTxOut2 to EthernetFramesTxOut2;
		connection c8 : PortConnection
			connect firewall.EthernetFramesTxOut3 to EthernetFramesTxOut3;
			
		:>> Domain : CASE_Scheduling::Domain = 3; 
		attribute Implementation_Language : HAMR::Implementation_Language = Rust;
	}
	
	part def MavlinkFirewall :> Thread {
		port In0 : EventDataPort { in :> type : UdpFrame; }
		port In1 : EventDataPort { in :> type : UdpFrame; }
		port In2 : EventDataPort { in :> type : UdpFrame; }
		port In3 : EventDataPort { in :> type : UdpFrame; }
		port Out0 : EventDataPort { out :> type : RawEthernetMessage; }
		port Out1 : EventDataPort { out :> type : RawEthernetMessage; }
		port Out2 : EventDataPort { out :> type : RawEthernetMessage; }
		port Out3 : EventDataPort { out :> type : RawEthernetMessage; }
		
		:>> Dispatch_Protocol = AADL_Project::Periodic;
		:>> Period : Time { ::> num = 1000[ms]; }
		:>> Compute_Execution_Time : Time_Range {
			:>> minimum : Time { :>> num = 300[ms]; }
			:>> maximum : Time { :>> num = 300[ms]; }
		}
		attribute stack_size : Memory_Properties::Stack_Size = 1048576[byte];
		:>> Implementation_Language : HAMR::Implementation_Language = Rust;
		:>> Source_Text  = "src/MavlinkFirewall.c";
		attribute Initialize_Entrypoint_Source_Text = "init";
		attribute Compute_Entrypoint_Source_Text = "compute";
		
		language "GUMBO" /*{
		
			functions
		      def three_bytes_to_u32(byte0: Base_Types::Unsigned_8, byte1: Base_Types::Unsigned_8, byte2: Base_Types::Unsigned_8): Base_Types::Unsigned_32 :=
	            toU32(byte2) * 65536[u32] + toU32(byte1) * 256[u32] + toU32(byte0);
	
	          def two_bytes_to_u16(byte0: Base_Types::Unsigned_8, byte1: Base_Types::Unsigned_8): Base_Types::Unsigned_16 :=
	            toU16(byte1) * 256[u16] + toU16(byte0);
	
			  def msg_v1_is_command_int(msg: UdpPayload): Base_Types::Boolean :=
				msg[5] == 75[u8];
	
			  def command_int_msg_v1_is_bootloader_flash(msg: UdpPayload): Base_Types::Boolean :=
				two_bytes_to_u16(msg[33], msg[34]) == 42650[u16];
	          
			  def msg_v1_is_command_long(msg: UdpPayload): Base_Types::Boolean :=
				msg[5] == 76[u8];
			
			  def command_long_msg_v1_is_bootloader_flash(msg: UdpPayload): Base_Types::Boolean :=
				two_bytes_to_u16(msg[34], msg[35]) == 42650[u16];
	
			  def msg_is_mavlinkv1(msg: UdpPayload): Base_Types::Boolean :=
			    msg[0] == 254[u8]; \\"0xFE"
	
			  def msg_v2_is_command_int(msg: UdpPayload): Base_Types::Boolean :=
				three_bytes_to_u32(msg[7], msg[8], msg[9]) == 75[u32];
	
			  def command_int_msg_v2_is_bootloader_flash(msg: UdpPayload): Base_Types::Boolean :=
				two_bytes_to_u16(msg[37], msg[38]) == 42650[u16];
	          
			  def msg_v2_is_command_long(msg: UdpPayload): Base_Types::Boolean :=
				three_bytes_to_u32(msg[7], msg[8], msg[9]) == 76[u32];
			
			  def command_long_msg_v2_is_bootloader_flash(msg: UdpPayload): Base_Types::Boolean :=
				two_bytes_to_u16(msg[38], msg[39]) == 42650[u16];
	
			  def msg_is_mavlinkv2(msg: UdpPayload): Base_Types::Boolean :=
			    msg[0] == 253[u8]; //"0xFD"
	
	
			  def msg_is_mav_v2_cmd_flash_bootloader(msg: UdpPayload): Base_Types::Boolean :=
				msg_is_mavlinkv2(msg) & ((msg_v2_is_command_int(msg) & command_int_msg_v2_is_bootloader_flash(msg)) |
				(msg_v2_is_command_long(msg) & command_long_msg_v2_is_bootloader_flash(msg)));
	
			  def msg_is_mav_v1_cmd_flash_bootloader(msg: UdpPayload): Base_Types::Boolean :=
				msg_is_mavlinkv1(msg) & ((msg_v1_is_command_int(msg) & command_int_msg_v1_is_bootloader_flash(msg)) |
				(msg_v1_is_command_long(msg) & command_long_msg_v1_is_bootloader_flash(msg)));
	
			  def msg_is_mav_cmd_flash_bootloader(msg: UdpPayload): Base_Types::Boolean :=
				msg_is_mav_v2_cmd_flash_bootloader(msg) | msg_is_mav_v1_cmd_flash_bootloader(msg);
	
			  def mav_input_headers_eq_output(headers: EthIpUdpHeaders, frame: RawEthernetMessage): Base_Types::Boolean :=
	            	(forAll(0 .. size(headers), {in i; headers[i] == frame[i]}));
	
			  def mav_input_payload_eq_output(payload: UdpPayload, headers: EthIpUdpHeaders, frame: RawEthernetMessage): Base_Types::Boolean :=
					(forAll(0 .. size(payload), {in i; frame[i + size(headers)] == payload[i]}));
	
			  def mav_input_eq_output(input: UdpFrame, frame: RawEthernetMessage): Base_Types::Boolean :=
			  		mav_input_headers_eq_output(input.headers, frame) & mav_input_payload_eq_output(input.payload, input.headers, frame);
	
			  def msg_is_blacklisted(msg: UdpPayload): Base_Types::Boolean :=
				  msg_is_mav_cmd_flash_bootloader(msg);
	     
	      compute
	
				guarantee hlr_19_mav0_drop_mav_cmd_flash_bootloader:
				  ((HasEvent(In0) & msg_is_mav_cmd_flash_bootloader(In0.payload)) implies
				    (NoSend(Out0)));
				guarantee hlr_21_mav0_no_input:
				   (not HasEvent(In0) implies (NoSend(Out0)));
				guarantee hlr_22_mav0_allow:
				   (HasEvent(In0) & not msg_is_blacklisted(In0.payload)) ->: 
				     (HasEvent(Out0) & mav_input_eq_output(In0, Out0));
		}*/			
	}
	
	part def MavlinkFirewall_seL4 :> Process {
		port In0 : EventDataPort { in :> type : UdpFrame; }
		port In1 : EventDataPort { in :> type : UdpFrame; }
		port In2 : EventDataPort { in :> type : UdpFrame; }
		port In3 : EventDataPort { in :> type : UdpFrame; }
		port Out0 : EventDataPort { out :> type : RawEthernetMessage; }
		port Out1 : EventDataPort { out :> type : RawEthernetMessage; }
		port Out2 : EventDataPort { out :> type : RawEthernetMessage; }
		port Out3 : EventDataPort { out :> type : RawEthernetMessage; }
		
		part mavlinkFirewall : MavlinkFirewall;
		
		connection c1 : PortConnection
			connect In0 to mavlinkFirewall.In0;
		connection c2 : PortConnection
			connect In1 to mavlinkFirewall.In1;
		connection c3 : PortConnection
			connect In2 to mavlinkFirewall.In2;
		connection c4 : PortConnection
			connect In3 to mavlinkFirewall.In3;
		connection c5 : PortConnection
			connect mavlinkFirewall.Out0 to Out0;
		connection c6 : PortConnection
			connect mavlinkFirewall.Out1 to Out1;
		connection c7 : PortConnection
			connect mavlinkFirewall.Out2 to Out2;
		connection c8 : PortConnection
			connect mavlinkFirewall.Out3 to Out3;
			
		:>> Domain = 6;
		attribute Implementation_Language : HAMR::Implementation_Language = Rust;
	}
	
	part def ArduPilot :> Thread {
		port FirewallRx0 : EventDataPort { in :> type : RawEthernetMessage; }
		port FirewallRx1 : EventDataPort { in :> type : RawEthernetMessage; }
		port FirewallRx2 : EventDataPort { in :> type : RawEthernetMessage; }
		port FirewallRx3 : EventDataPort { in :> type : RawEthernetMessage; }
		port MavlinkRx0 : EventDataPort { out :> type : RawEthernetMessage; }
		port MavlinkRx1 : EventDataPort { out :> type : RawEthernetMessage; }
		port MavlinkRx2 : EventDataPort { out :> type : RawEthernetMessage; }
		port MavlinkRx3 : EventDataPort { out :> type : RawEthernetMessage; }
		port EthernetFramesTx0 : EventDataPort { out :> type : RawEthernetMessage; }
		port EthernetFramesTx1 : EventDataPort { out :> type : RawEthernetMessage; }
		port EthernetFramesTx2 : EventDataPort { out :> type : RawEthernetMessage; }
		port EthernetFramesTx3 : EventDataPort { out :> type : RawEthernetMessage; }
	
		:>> Dispatch_Protocol = AADL_Project::Periodic;
		:>> Period : Time { ::> num = 1000[ms]; }
		:>> Compute_Execution_Time : Time_Range {
			:>> minimum : Time { :>> num = 600[ms]; }
			:>> maximum : Time { :>> num = 600[ms]; }
			}
		attribute stack_size = 1048576[byte];
		:>> Source_Text = "src/Ardupilot.c";
		attribute Initialize_Entrypoint_Source_Text = "init";
		attribute Compute_Entrypoint_Source_Text = "compute";
	}
	
	part def ArduPilot_seL4 :> Process {
		port FirewallRx0 : EventDataPort { in :> type : RawEthernetMessage; }
		port FirewallRx1 : EventDataPort { in :> type : RawEthernetMessage; }
		port FirewallRx2 : EventDataPort { in :> type : RawEthernetMessage; }
		port FirewallRx3 : EventDataPort { in :> type : RawEthernetMessage; }
		port MavlinkRx0 : EventDataPort { out :> type : RawEthernetMessage; }
		port MavlinkRx1 : EventDataPort { out :> type : RawEthernetMessage; }
		port MavlinkRx2 : EventDataPort { out :> type : RawEthernetMessage; }
		port MavlinkRx3 : EventDataPort { out :> type : RawEthernetMessage; }
		port EthernetFramesTx0 : EventDataPort { out :> type : RawEthernetMessage; }
		port EthernetFramesTx1 : EventDataPort { out :> type : RawEthernetMessage; }
		port EthernetFramesTx2 : EventDataPort { out :> type : RawEthernetMessage; }
		port EthernetFramesTx3 : EventDataPort { out :> type : RawEthernetMessage; }
	
		part arduPilot : ArduPilot;
		
		connection c1 : PortConnection 
			connect FirewallRx0 to arduPilot.FirewallRx0;
		connection c2 : PortConnection 
			connect FirewallRx1 to arduPilot.FirewallRx1;
		connection c3 : PortConnection 
			connect FirewallRx2 to arduPilot.FirewallRx2;
		connection c4 : PortConnection 
			connect FirewallRx3 to arduPilot.FirewallRx3;
		connection c5 : PortConnection
			connect MavlinkRx0 to arduPilot.MavlinkRx0;
		connection c6 : PortConnection
			connect MavlinkRx1 to arduPilot.MavlinkRx1;
		connection c7 : PortConnection
			connect MavlinkRx2 to arduPilot.MavlinkRx2;
		connection c8 : PortConnection
			connect MavlinkRx3 to arduPilot.MavlinkRx3;
		connection c9 : PortConnection
			connect arduPilot.EthernetFramesTx0 to EthernetFramesTx0;
		connection c10 : PortConnection
			connect arduPilot.EthernetFramesTx1 to EthernetFramesTx1;
		connection c11 : PortConnection
			connect arduPilot.EthernetFramesTx2 to EthernetFramesTx2;
		connection c12 : PortConnection
			connect arduPilot.EthernetFramesTx3 to EthernetFramesTx3;
			
		:>> Domain  = 2;
		attribute SMC = true;
	}
	
	part def seL4 :> System {
		part arduPilot : ArduPilot_seL4;
		part mavlinkFirewall : MavlinkFirewall_seL4;
		part rxFirewall : RxFirewall_seL4;
		part txFirewall : TxFirewall_seL4;
		part lowLevelEthernetDriver : LowLevelEthernetDriver_seL4;
		
		connection c1 : PortConnection
			connect lowLevelEthernetDriver.EthernetFramesRx0 to rxFirewall.EthernetFramesRxIn0;
		connection c2 : PortConnection
			connect lowLevelEthernetDriver.EthernetFramesRx1 to rxFirewall.EthernetFramesRxIn1;
		connection c3 : PortConnection
			connect lowLevelEthernetDriver.EthernetFramesRx2 to rxFirewall.EthernetFramesRxIn2;
		connection c4 : PortConnection
			connect lowLevelEthernetDriver.EthernetFramesRx3 to rxFirewall.EthernetFramesRxIn3;
		connection c5 : PortConnection
			connect rxFirewall.VmmOut0 to arduPilot.FirewallRx0;
		connection c6 : PortConnection
			connect rxFirewall.VmmOut1 to arduPilot.FirewallRx1;
		connection c7 : PortConnection
			connect rxFirewall.VmmOut2 to arduPilot.FirewallRx2;
		connection c8 : PortConnection
			connect rxFirewall.VmmOut3 to arduPilot.FirewallRx3;
		
		connection c9 : PortConnection
			connect rxFirewall.MavlinkOut0 to mavlinkFirewall.In0;
		connection c10 : PortConnection
			connect rxFirewall.MavlinkOut1 to mavlinkFirewall.In1;
		connection c11 : PortConnection
			connect rxFirewall.MavlinkOut2 to mavlinkFirewall.In2;
		connection c12 : PortConnection
			connect rxFirewall.MavlinkOut3 to mavlinkFirewall.In3;
		connection c13 : PortConnection
			connect mavlinkFirewall.Out0 to arduPilot.MavlinkRx0;
		connection c14 : PortConnection
			connect mavlinkFirewall.Out1 to arduPilot.MavlinkRx1;
		connection c15 : PortConnection
			connect mavlinkFirewall.Out2 to arduPilot.MavlinkRx2;
		connection c16 : PortConnection
			connect mavlinkFirewall.Out3 to arduPilot.MavlinkRx3;
		
		connection c17 : PortConnection
			connect arduPilot.EthernetFramesTx0 to txFirewall.EthernetFramesTxOut0;
		connection c18 : PortConnection
			connect arduPilot.EthernetFramesTx1 to txFirewall.EthernetFramesTxOut1;
		connection c19 : PortConnection
			connect arduPilot.EthernetFramesTx2 to txFirewall.EthernetFramesTxOut2;
		connection c20 : PortConnection
			connect arduPilot.EthernetFramesTx3 to txFirewall.EthernetFramesTxOut3;
		connection c21 : PortConnection
			connect txFirewall.EthernetFramesTxOut0 to lowLevelEthernetDriver.EthernetFramesTx0;
		connection c22 : PortConnection
			connect txFirewall.EthernetFramesTxOut1 to lowLevelEthernetDriver.EthernetFramesTx1;
		connection c23 : PortConnection
			connect txFirewall.EthernetFramesTxOut2 to lowLevelEthernetDriver.EthernetFramesTx2;
		connection c24 : PortConnection
			connect txFirewall.EthernetFramesTxOut3 to lowLevelEthernetDriver.EthernetFramesTx3;
	}	
}