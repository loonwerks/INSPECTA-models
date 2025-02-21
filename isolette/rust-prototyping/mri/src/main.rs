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

    #[test]
    #[serial]
    fn test_initialization() {

        let api = MRI_Initialization_Api {};
        let mut app_api: Manage_Regulator_Interface_Application_Api<MRI_Initialization_Api> = Manage_Regulator_Interface_Application_Api::new(api);
        let mut mri = Manage_Regulator_Interface {};
        
        mri.initialise(&mut app_api);

        // get values of outgoing ports
        let regulator_status = extern_c_api::TEST_out_regulator_status.lock().unwrap().expect("Not expecting None");

        assert!(regulator_status == isolette_Isolette_Data_Model_Status_Type::Init_Status);
    }

    #[test]
    #[serial]
    fn test_compute_normal() {
        // set values of incoming data ports
        *extern_c_api::TEST_in_regulator_mode.lock().unwrap() = Some(isolette_Isolette_Data_Model_Regulator_Mode_Type::Normal_Regulator_Mode);
        *extern_c_api::TEST_in_lower_desired_tempWstatus.lock().unwrap() = Some(isolette_Isolette_Data_Model_TempWstatus_i {degrees: 98, status: isolette_Isolette_Data_Model_ValueStatus_Type::Valid});
        *extern_c_api::TEST_in_upper_desired_tempWstatus.lock().unwrap() = Some(isolette_Isolette_Data_Model_TempWstatus_i {degrees: 101, status: isolette_Isolette_Data_Model_ValueStatus_Type::Valid});
        *extern_c_api::TEST_in_current_tempWstatus.lock().unwrap() = Some(isolette_Isolette_Data_Model_TempWstatus_i {degrees: 99, status: isolette_Isolette_Data_Model_ValueStatus_Type::Valid});

        let api = MRI_Compute_Api {};
        let mut app_api  = Manage_Regulator_Interface_Application_Api::new(api);
        let mut mri = Manage_Regulator_Interface {};
        
        mri.timeTriggered(&mut app_api);

        // get values of outgoing ports
        let regulator_status = extern_c_api::TEST_out_regulator_status.lock().unwrap().expect("Not expecting None");
        let display_temp = extern_c_api::TEST_out_displayed_temp.lock().unwrap().expect("Not expecting None");
        let upper_desired = extern_c_api::TEST_out_upper_desired_temp.lock().unwrap().expect("Not expecting none");
        let lower_desired = extern_c_api::TEST_out_lower_desired_temp.lock().unwrap().expect("Not expecting None");
        let interface_failure = extern_c_api::TEST_out_interface_failure.lock().unwrap().expect("Not expecting None");

        assert!(!interface_failure.flag);
        assert!(regulator_status == isolette_Isolette_Data_Model_Status_Type::On_Status);
        assert!(display_temp.degrees == 99);
        assert!(lower_desired.degrees == 98);
        assert!(upper_desired.degrees == 101);
        
    }

    #[test]
    #[serial]
    fn test_compute_failed() {
        // set values of incoming data ports
        *extern_c_api::TEST_in_regulator_mode.lock().unwrap() = Some(isolette_Isolette_Data_Model_Regulator_Mode_Type::Normal_Regulator_Mode);
        *extern_c_api::TEST_in_lower_desired_tempWstatus.lock().unwrap() = 
          Some(isolette_Isolette_Data_Model_TempWstatus_i {degrees: 98, status: isolette_Isolette_Data_Model_ValueStatus_Type::Invalid});
        *extern_c_api::TEST_in_upper_desired_tempWstatus.lock().unwrap() = 
          Some(isolette_Isolette_Data_Model_TempWstatus_i {degrees: 101, status: isolette_Isolette_Data_Model_ValueStatus_Type::Valid});
        *extern_c_api::TEST_in_current_tempWstatus.lock().unwrap() = Some(isolette_Isolette_Data_Model_TempWstatus_i {degrees: 99, status: isolette_Isolette_Data_Model_ValueStatus_Type::Valid});

        let api = MRI_Compute_Api {};
        let mut app_api  = Manage_Regulator_Interface_Application_Api::new(api);
        let mut mri = Manage_Regulator_Interface {};
        
        mri.timeTriggered(&mut app_api);

        // get values of outgoing ports
        let regulator_status = extern_c_api::TEST_out_regulator_status.lock().unwrap().expect("Not expecting None");
        let display_temp = extern_c_api::TEST_out_displayed_temp.lock().unwrap().expect("Not expecting None");
        let upper_desired = extern_c_api::TEST_out_upper_desired_temp.lock().unwrap().expect("Not expecting None");
        let lower_desired = extern_c_api::TEST_out_lower_desired_temp.lock().unwrap().expect("Not expecting None");
        let interface_failure = extern_c_api::TEST_out_interface_failure.lock().unwrap().expect("Not expecting None");

        assert!(interface_failure.flag);
        assert!(regulator_status == isolette_Isolette_Data_Model_Status_Type::On_Status);
        assert!(display_temp.degrees == 99);
        assert!(lower_desired.degrees == isolette_Isolette_Data_Model_Temp_i::default().degrees);
        assert!(upper_desired.degrees == isolette_Isolette_Data_Model_Temp_i::default().degrees);
        
    }

}