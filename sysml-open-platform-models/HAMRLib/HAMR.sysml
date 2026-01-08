library package HAMR {

  public import HAMR_Time_Units::*;
  public import HAMR_AADL::*;
  public import AADL::*;

  attribute def Array_Size_Kind :> AADL::Property, Array_Size_Kinds {
    doc AADL_Doc 
    /* 
     * Specifies the kind of array size semantics for an array data type.
     * 
     * Fixed: The array always has exactly the number of elements specified in
     *   the Data_Model::Dimension property. Both memory allocation and logical
     *   content size are fixed.
     *
     * Bounded: The array may contain fewer elements than specified by
     *   Data_Model::Dimension, but never more. The full size may be allocated in
     *   memory, but only part of it is considered logically valid.
     *
     * Unbounded: The array does not have a statically known size limit.
     *   If Data_Model::Dimension is provided then all dimensions must be equal to 0. 
     *   The array may grow arbitrarily, and size checks must be enforced at runtime.
     */
  }

  enum def Array_Size_Kinds {
    enum Fixed;
    enum Bound;
    enum Unbounded;
  }

  alias Fixed for Array_Size_Kinds::Fixed;
  alias Bound for Array_Size_Kinds::Bound;
  alias Unbounded for Array_Size_Kinds::Unbounded;


  attribute def Implementation_Language :> AADL::Property, Implementation_Languages;

  enum def Implementation_Languages {
    enum Slang;
    enum C;
    enum Rust;
  }

  alias Slang for Implementation_Languages::Slang;
  alias C for Implementation_Languages::C;
  alias Rust for Implementation_Languages::Rust;
}
