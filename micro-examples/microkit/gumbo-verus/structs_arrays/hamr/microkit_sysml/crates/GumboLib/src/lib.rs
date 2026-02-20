#![cfg_attr(not(test), no_std)]

#![allow(non_camel_case_types)]
#![allow(non_snake_case)]
#![allow(non_upper_case_globals)]

#![allow(dead_code)]
#![allow(static_mut_refs)]
#![allow(unused_imports)]
#![allow(unused_macros)]
#![allow(unused_parens)]
#![allow(unused_unsafe)]
#![allow(unused_variables)]

// This file will not be overwritten if codegen is rerun

use data::*;
use vstd::prelude::*;

macro_rules! implies {
  ($lhs: expr, $rhs: expr) => {
    !$lhs || $rhs
  };
}

macro_rules! impliesL {
  ($lhs: expr, $rhs: expr) => {
    !$lhs | $rhs
  };
}

// BEGIN MARKER GUMBO RUST MARKER
pub fn normalLibraryFunction(a: Gubmo_Structs_Arrays::MyArrayInt32) -> bool
{
  librarySpecFunction_Guarantee(a) & (0..=a.len() - 2).all(|i| a[i] <= a[i + 1])
}

/// GUMBOX wrapper for the GUMBO spec function `test` that delegates to the developer-supplied GUMBOX
/// specification function that must have the following signature:
/// 
///   pub exec fn librarySpecFunction_Assume__developer_gumbox(a: Gubmo_Structs_Arrays::MyArrayInt32) -> (res: bool) { ... }
/// 
/// The semantics of the GUMBO spec function are entirely defined by the developer-supplied implementation.
pub fn librarySpecFunction_Assume(a: Gubmo_Structs_Arrays::MyArrayInt32) -> bool
{
  librarySpecFunction_Assume__developer_gumbox(a)
}

/// GUMBOX wrapper for the GUMBO spec function `test` that delegates to the developer-supplied GUMBOX
/// specification function that must have the following signature:
/// 
///   pub exec fn librarySpecFunction_Guarantee__developer_gumbox(a: Gubmo_Structs_Arrays::MyArrayInt32) -> (res: bool) { ... }
/// 
/// The semantics of the GUMBO spec function are entirely defined by the developer-supplied implementation.
pub fn librarySpecFunction_Guarantee(a: Gubmo_Structs_Arrays::MyArrayInt32) -> bool
{
  librarySpecFunction_Guarantee__developer_gumbox(a)
}
// END MARKER GUMBO RUST MARKER

verus! {
  // BEGIN MARKER GUMBO VERUS MARKER
  pub open spec fn normalLibraryFunction_spec(a: Gubmo_Structs_Arrays::MyArrayInt32) -> bool
  {
    librarySpecFunction_Guarantee_spec(a) && forall|i:int| 0 <= i <= a.len() - 2 ==> #[trigger] a[i] <= a[i + 1]
  }

  /// Verus wrapper for the GUMBO spec function `test` that delegates to the developer-supplied Verus
  /// specification function that must have the following signature:
  /// 
  ///   pub open spec fn librarySpecFunction_Assume__developer_verus(a: Gubmo_Structs_Arrays::MyArrayInt32) -> (res: bool) { ... }
  /// 
  /// The semantics of the GUMBO spec function are entirely defined by the developer-supplied implementation.
  pub open spec fn librarySpecFunction_Assume_spec(a: Gubmo_Structs_Arrays::MyArrayInt32) -> bool
  {
    librarySpecFunction_Assume__developer_verus(a)
  }

  /// Verus wrapper for the GUMBO spec function `test` that delegates to the developer-supplied Verus
  /// specification function that must have the following signature:
  /// 
  ///   pub open spec fn librarySpecFunction_Guarantee__developer_verus(a: Gubmo_Structs_Arrays::MyArrayInt32) -> (res: bool) { ... }
  /// 
  /// The semantics of the GUMBO spec function are entirely defined by the developer-supplied implementation.
  pub open spec fn librarySpecFunction_Guarantee_spec(a: Gubmo_Structs_Arrays::MyArrayInt32) -> bool
  {
    librarySpecFunction_Guarantee__developer_verus(a)
  }
  // END MARKER GUMBO VERUS MARKER

  /// Developer-supplied Verus realization of the GUMBO spec function `test`.
  /// 
  /// This function may be freely refined as long as it remains a pure Verus `spec fn`.
  pub open spec fn librarySpecFunction_Assume__developer_verus(a: Gubmo_Structs_Arrays::MyArrayInt32) -> (res: bool)
  {
    // This default implementation returns `true`, which is safe but weak:
    // * In `assume` contexts, returning `false` may allow Verus to prove `false`.
    // * To obtain meaningful guarantees, developers should strengthen this
    //   specification to reflect the intended semantics of the GUMBO spec function.
    true
  }

  /// Developer-supplied Verus realization of the GUMBO spec function `test`.
  /// 
  /// This function may be freely refined as long as it remains a pure Verus `spec fn`.
  pub open spec fn librarySpecFunction_Guarantee__developer_verus(a: Gubmo_Structs_Arrays::MyArrayInt32) -> (res: bool)
  {
    // This default implementation returns `true`, which is safe but weak:
    // * In `assume` contexts, returning `false` may allow Verus to prove `false`.
    // * To obtain meaningful guarantees, developers should strengthen this
    //   specification to reflect the intended semantics of the GUMBO spec function.
    true
  }

  /// Developer-supplied GUMBOX realization of the GUMBO spec function `test`.
  /// 
  /// This function may be freely refined.
  pub exec fn librarySpecFunction_Assume__developer_gumbox(a: Gubmo_Structs_Arrays::MyArrayInt32) -> (res: bool)
    ensures
      res == librarySpecFunction_Assume__developer_verus(a),
  {
    // This default implementation returns `true`, which is safe but weak:
    // * In `assume` contexts, returning `false` may allow GUMBOX to prove `false`.
    // * To obtain meaningful guarantees, developers should strengthen this
    //   specification to reflect the intended semantics of the GUMBO spec function.
    true
  }

  /// Developer-supplied GUMBOX realization of the GUMBO spec function `test`.
  /// 
  /// This function may be freely refined.
  pub exec fn librarySpecFunction_Guarantee__developer_gumbox(a: Gubmo_Structs_Arrays::MyArrayInt32) -> (res: bool)
    ensures
      res == librarySpecFunction_Guarantee__developer_verus(a),
  {
    // This default implementation returns `true`, which is safe but weak:
    // * In `assume` contexts, returning `false` may allow GUMBOX to prove `false`.
    // * To obtain meaningful guarantees, developers should strengthen this
    //   specification to reflect the intended semantics of the GUMBO spec function.
    true
  }
}
