#![allow(dead_code)]

extern "C" {
    pub fn read_port1_is_empty() -> bool;
    pub fn get_read_port1_poll(numDropped: *mut types::sb_event_counter_t, data: *mut types::base_event_data_2_prod_2_cons_struct_i) -> bool;
    pub fn get_read_port1(data: *mut types::base_event_data_2_prod_2_cons_struct_i) -> bool;

    pub fn read_port2_is_empty() -> bool;
    pub fn get_read_port2_poll(numDropped: *mut types::sb_event_counter_t, data: *mut types::base_event_data_2_prod_2_cons_struct_i) -> bool;
    pub fn get_read_port2(data: *mut types::base_event_data_2_prod_2_cons_struct_i) -> bool;
}

pub fn c_read_port1_is_empty() -> bool {
    return unsafe { read_port1_is_empty() };
}

pub fn c_get_read_port1_poll(num_dropped: &mut types::sb_event_counter_t, data: &mut types::base_event_data_2_prod_2_cons_struct_i) -> bool {
    return unsafe { get_read_port1_poll(num_dropped, data) };
}

pub fn c_get_read_port1(data: &mut types::base_event_data_2_prod_2_cons_struct_i) -> bool {
    return unsafe { get_read_port1(data) };
}

pub fn c_read_port2_is_empty() -> bool {
    return unsafe { read_port2_is_empty() };
}

pub fn c_get_read_port2_poll(num_dropped: &mut types::sb_event_counter_t, data: &mut types::base_event_data_2_prod_2_cons_struct_i) -> bool {
    return unsafe { get_read_port2_poll(num_dropped, data) };
}

pub fn c_get_read_port2(data: &mut types::base_event_data_2_prod_2_cons_struct_i) -> bool {
    return unsafe { get_read_port2(data) };
}
