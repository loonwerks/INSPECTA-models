package Base_Types {
	
	/* This package shows how to emulate the content of the Base_Types package 
	 * from the Data Modeling Annex.
	 */
	
	private import ScalarValues::*;
	private import SI::*;
	
	private import AADL::* ;
	private import Data_Model::* ;
	 
	/* 
	 data Boolean
     properties
       Data_Model::Data_Representation => Boolean;
     end Boolean;
    */

    part def Data_Boolean :> Data {
	   :>  value : ScalarValues::Boolean;
    }

    /* 
     data Integer
     properties
        Data_Model::Data_Representation => Integer;
     end Integer;

     -- Signed integer of various byte sizes

     data Integer_8 extends Integer
     properties
       Data_Model::Number_Representation => Signed;
       Source_Data_Size => 1 Bytes;
     end Integer_8;
 
     -- Integer_16, Integer_32, Integer_64, ..
    */
    
    part def Integer :> Data {
	   :>  value : ScalarValues::Integer;
       attribute Number_Representation = Signed;
    }

    part def Integer_8 :> Integer {
	   :> Data_Size = 1 [byte];
    }

    part def Integer_16 :> Integer {
	   :> Data_Size = 2 [byte];
    }

    part def Integer_32 :> Integer {
	   :> Data_Size = 4 [byte];
    }

    part def Integer_64 :> Integer {
	   :> Data_Size = 8 [byte];
    }

	/*
     data Natural extends Integer
     properties 
       Data_Model::Integer_Range => 0 .. Max_Target_Integer;
     end Natural;
    */

	part def Natural :> Data {
	   :>  value : ScalarValues::Integer;
       attribute Number_Representation = Signed;
     }

    /*  
     data Unsigned_8 extends Integer
     properties
       Data_Model::Number_Representation => Unsigned;
       Source_Data_Size => 1 Bytes;
     end Unsigned_8; 
     */

    part def Unsigned_8 :> Data {
	   :> Data_Size = 1 [byte];
	   attribute Number_Representation = Unsigned;
    }

    part def Unsigned_16 :> Data {
	   :> Data_Size = 2 [byte];
	   attribute Number_Representation = Unsigned;
    }
    
    part def Unsigned_32 :> Data {
	   :> Data_Size = 4 [byte];
	   attribute Number_Representation = Unsigned;
    }
    
    part def Unsigned_64 :> Data {
	   :> Data_Size = 8 [byte];
	   attribute Number_Representation = Unsigned;
    }

   /*
    data Float
    properties
       Data_Model::Data_Representation => Float;
    end Float;

    data Float_32 extends Float
    properties
       Data_Model::IEEE754_Precision => Simple;
       Source_Data_Size => 4 Bytes;
    end Float_32;
    */
	
	part def Data_Float :> Data {
	   :>  value : ScalarValues::Real;	
	}

	part def Data_Float_32 :> Data_Float {
		:> Data_Size = 4 [byte];
	    attribute IEEE754_Precisiion = IEEE754_Precision::Simple;
	}

	part def Data_Float_64 :> Data_Float {
		:> Data_Size = 48 [byte];
	    attribute IEEE754_Precisiion = IEEE754_Precision::Double;
	}
	
	/* 
	 data Character
     properties
        Data_Model::Data_Representation => Character;
     end Character;

     data String
     properties
        Data_Model::Data_Representation => String;
     end String;
     */	
	
	part def Data_String :> Data {
	   :>  value : ScalarValues::String;	
		
	}
		
}