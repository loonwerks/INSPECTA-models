#![allow(non_camel_case_types)]
#![allow(non_upper_case_globals)]
#![allow(non_snake_case)]
#![allow(unused_parens)]

mod data;
mod art;
mod extern_c_api;
mod component;

fn main() {
    todo!()
}

#[cfg(test)]
mod tests {
    use serial_test::serial;
    use crate::{data::*, extern_c_api};
    use crate::component::manage_regulator_interface_api::*;
    use crate::component::manage_regulator_interface_app::*;
    use crate::component::manage_regulator_interface_GUMBOX as GUMBOX;
    
    const failOnUnsatPrecondition: bool = false;

    #[test]
    #[serial]
    fn test_initialization() {

        let api = MRI_Initialization_Api {};
        let mut app_api: Manage_Regulator_Interface_Application_Api<MRI_Initialization_Api> = Manage_Regulator_Interface_Application_Api::new(api);
        let mut mri = Manage_Regulator_Interface {};
        
        mri.initialise(&mut app_api);

        // get values of outgoing ports
        let regulator_status = extern_c_api::OUT_regulator_status.lock().unwrap().expect("Not expecting None");
        let display_temp = extern_c_api::OUT_displayed_temp.lock().unwrap().expect("Not expecting None");
        let upper_desired = extern_c_api::OUT_upper_desired_temp.lock().unwrap().expect("Not expecting none");
        let lower_desired = extern_c_api::OUT_lower_desired_temp.lock().unwrap().expect("Not expecting None");
        let interface_failure = extern_c_api::OUT_interface_failure.lock().unwrap().expect("Not expecting None");

        assert!(GUMBOX::inititialize_IEP_Post(
            display_temp,
            interface_failure,
            lower_desired,
            regulator_status,
            upper_desired));

        assert!(regulator_status == isolette_Isolette_Data_Model_Status_Type::Init_Status);
        assert!(display_temp == isolette_Isolette_Data_Model_Temp_i::default());
        assert!(upper_desired == isolette_Isolette_Data_Model_Temp_i::default());
        assert!(lower_desired == isolette_Isolette_Data_Model_Temp_i::default());
        assert!(interface_failure == isolette_Isolette_Data_Model_Failure_Flag_i::default());
    
    }

    #[test]
    #[serial]
    fn test_compute_normal() {
        // set values of incoming data ports
        let api_current_tempWstatus = isolette_Isolette_Data_Model_TempWstatus_i {degrees: 99, status: isolette_Isolette_Data_Model_ValueStatus_Type::Valid};
        let api_lower_desired_tempWstatus = isolette_Isolette_Data_Model_TempWstatus_i {degrees: 98, status: isolette_Isolette_Data_Model_ValueStatus_Type::Valid};
        let api_regulator_mode = isolette_Isolette_Data_Model_Regulator_Mode_Type::Normal_Regulator_Mode;
        let api_upper_desired_tempWstatus = isolette_Isolette_Data_Model_TempWstatus_i {degrees: 101, status: isolette_Isolette_Data_Model_ValueStatus_Type::Valid};

        *extern_c_api::IN_regulator_mode.lock().unwrap() = Some(api_regulator_mode);
        *extern_c_api::IN_lower_desired_tempWstatus.lock().unwrap() = Some(api_lower_desired_tempWstatus);
        *extern_c_api::IN_upper_desired_tempWstatus.lock().unwrap() = Some(api_upper_desired_tempWstatus);
        *extern_c_api::IN_current_tempWstatus.lock().unwrap() = Some(api_current_tempWstatus);

        let api = MRI_Compute_Api {};
        let mut app_api  = Manage_Regulator_Interface_Application_Api::new(api);
        let mut mri = Manage_Regulator_Interface {};

        if(!GUMBOX::compute_CEP_Pre(
            api_current_tempWstatus, 
            api_lower_desired_tempWstatus, 
            api_regulator_mode, 
            api_upper_desired_tempWstatus)) {

            if failOnUnsatPrecondition {
                assert!(false, "MRI precondition failed");
            }
        } else {

            mri.timeTriggered(&mut app_api);

            // get values of outgoing ports
            let api_regulator_status = extern_c_api::OUT_regulator_status.lock().unwrap().expect("Not expecting None");
            let api_displayed_temp = extern_c_api::OUT_displayed_temp.lock().unwrap().expect("Not expecting None");
            let api_upper_desired_temp = extern_c_api::OUT_upper_desired_temp.lock().unwrap().expect("Not expecting none");
            let api_lower_desired_temp = extern_c_api::OUT_lower_desired_temp.lock().unwrap().expect("Not expecting None");
            let api_interface_failure = extern_c_api::OUT_interface_failure.lock().unwrap().expect("Not expecting None");

            assert!(GUMBOX::compute_CEP_Post(
                api_current_tempWstatus,
                api_lower_desired_tempWstatus, 
                api_regulator_mode, 
                api_upper_desired_tempWstatus,
                api_displayed_temp, 
                api_interface_failure, 
                api_lower_desired_temp,
                api_regulator_status, 
                api_upper_desired_temp));

            assert!(!api_interface_failure.flag);
            assert!(api_regulator_status == isolette_Isolette_Data_Model_Status_Type::On_Status);
            assert!(api_displayed_temp.degrees == api_current_tempWstatus.degrees);
            assert!(api_lower_desired_temp.degrees == api_lower_desired_tempWstatus.degrees);
            assert!(api_upper_desired_temp.degrees == api_upper_desired_tempWstatus.degrees);
        } 
        
    }

    #[test]
    #[serial]
    fn test_compute_failed() {
        // set values of incoming data ports
        let api_current_tempWstatus = isolette_Isolette_Data_Model_TempWstatus_i {degrees: 99, status: isolette_Isolette_Data_Model_ValueStatus_Type::Valid};
        let api_lower_desired_tempWstatus = 
            isolette_Isolette_Data_Model_TempWstatus_i {degrees: 98, status: isolette_Isolette_Data_Model_ValueStatus_Type::Invalid};
        let api_regulator_mode = isolette_Isolette_Data_Model_Regulator_Mode_Type::Normal_Regulator_Mode;
        let api_upper_desired_tempWstatus = isolette_Isolette_Data_Model_TempWstatus_i {degrees: 101, status: isolette_Isolette_Data_Model_ValueStatus_Type::Valid};

        *extern_c_api::IN_regulator_mode.lock().unwrap() = Some(api_regulator_mode);
        *extern_c_api::IN_lower_desired_tempWstatus.lock().unwrap() = Some(api_lower_desired_tempWstatus);
        *extern_c_api::IN_upper_desired_tempWstatus.lock().unwrap() = Some(api_upper_desired_tempWstatus);
        *extern_c_api::IN_current_tempWstatus.lock().unwrap() = Some(api_current_tempWstatus);

        let api = MRI_Compute_Api {};
        let mut app_api  = Manage_Regulator_Interface_Application_Api::new(api);
        let mut mri = Manage_Regulator_Interface {};
        
        if(!GUMBOX::compute_CEP_Pre(
            api_current_tempWstatus, 
            api_lower_desired_tempWstatus, 
            api_regulator_mode, 
            api_upper_desired_tempWstatus)) {

            if failOnUnsatPrecondition {
                assert!(false, "MRI precondition failed");
            }
        } else {

            mri.timeTriggered(&mut app_api);

            // get values of outgoing ports
            let api_regulator_status = extern_c_api::OUT_regulator_status.lock().unwrap().expect("Not expecting None");
            let api_displayed_temp = extern_c_api::OUT_displayed_temp.lock().unwrap().expect("Not expecting None");
            let api_upper_desired_temp = extern_c_api::OUT_upper_desired_temp.lock().unwrap().expect("Not expecting none");
            let api_lower_desired_temp = extern_c_api::OUT_lower_desired_temp.lock().unwrap().expect("Not expecting None");
            let api_interface_failure = extern_c_api::OUT_interface_failure.lock().unwrap().expect("Not expecting None");

            assert!(GUMBOX::compute_CEP_Post(
                api_current_tempWstatus,
                api_lower_desired_tempWstatus, 
                api_regulator_mode, 
                api_upper_desired_tempWstatus,
                api_displayed_temp, 
                api_interface_failure, 
                api_lower_desired_temp,
                api_regulator_status, 
                api_upper_desired_temp));

            assert!(api_interface_failure.flag);
            assert!(api_regulator_status == isolette_Isolette_Data_Model_Status_Type::On_Status);
            assert!(api_displayed_temp.degrees == 99);
            assert!(api_lower_desired_temp.degrees == isolette_Isolette_Data_Model_Temp_i::default().degrees);
            assert!(api_upper_desired_temp.degrees == isolette_Isolette_Data_Model_Temp_i::default().degrees);
        }  
    }

}