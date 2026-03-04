package Aadl_Datatypes {

    private import HAMR::*;
    private import Base_Types::*;

    ///////////////////////////////////////////////////////////////////////
    //
    //   E n u m
    //
    ///////////////////////////////////////////////////////////////////////

    enum def MyEnum { On; Off; }

    ///////////////////////////////////////////////////////////////////////
    //
    //   S t r u c t
    //
    ///////////////////////////////////////////////////////////////////////

    part def MyStruct2_i :> Struct {
        part fieldSChar : Character;
    }

    part def MyStruct_i :> Struct {
        part fieldInt64  : Integer_64;
        part fieldStr    : String;
        attribute fieldEnum  : MyEnum;
        part fieldRec    : MyStruct2_i;
        part fieldArray  : MyArrayOneDim;
    }

    ///////////////////////////////////////////////////////////////////////
    //
    //   A r r a y
    //
    ///////////////////////////////////////////////////////////////////////

    part def MyArrayOneDim :> Array {
        part :>> Base_Type : Integer_32;
        attribute :>> Dimensions = 10;
        attribute :>> Array_Size_Kind = Fixed;
        attribute :>> Data_Size = 40 [byte];
    }
}
