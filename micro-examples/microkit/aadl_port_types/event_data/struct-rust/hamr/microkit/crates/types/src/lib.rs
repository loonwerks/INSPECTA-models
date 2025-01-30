#![no_std]
#![allow(non_camel_case_types)]

pub type microkit_channel = u32;

pub type sb_event_counter_t = cty::uintmax_t;

pub const BASE_EVENT_DATA_2_PROD_2_CONS_ARRAY_OF_INTS_SIZE: usize = 40; 
pub const BASE_EVENT_DATA_2_PROD_2_CONS_ARRAY_OF_INTS_DIM: usize = 10;

pub type base_event_data_2_prod_2_cons_ArrayOfInts = [cty::c_int; BASE_EVENT_DATA_2_PROD_2_CONS_ARRAY_OF_INTS_DIM];

#[repr(C)]
#[derive(Debug)]
pub struct base_event_data_2_prod_2_cons_struct_i {
    pub size: cty::c_int,
    pub elements: base_event_data_2_prod_2_cons_ArrayOfInts
}