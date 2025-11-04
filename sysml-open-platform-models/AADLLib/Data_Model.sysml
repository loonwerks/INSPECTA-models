package Data_Model {
	
	private import SI::*;
    
    attribute def Data_Size :> StorageCapacityUnit;
	
	enum def Number_Representation_enum {
	  enum Signed;
	  enum Unsigned;
    }

	alias Signed for Number_Representation_enum::Signed;
	alias Unsigned for Number_Representation_enum::Unsigned;

    attribute def Number_Representation :> Number_Representation_enum;
	
	enum def IEEE754_Precision {
		enum Simple;
		enum Double;	
	}
	
}