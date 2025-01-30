use vstd::prelude::*;

verus! {

#[derive(Debug, PartialEq, Eq, Copy, Clone)]
enum HeatControl
{
    ON,
    OFF
}

#[derive(Debug, PartialEq, Eq)]
enum RegulatorMode
{
    INIT,
    NORMAL,
    FAILED
}

// defined in API header?
struct Api
{
    current_temp: i32,
    lower_desired_temp: i32,
    upper_desired_temp: i32,
    regulator_mode: RegulatorMode, 
    heat_control: HeatControl,
}

impl Api
{
    // Spec? Contracts?

    fn get(&self) {
        // read data from input port
    }

    fn put(&self) {
        // write data to output port
    }

    fn set_heat_control(&mut self, hc: HeatControl)
    ensures
        self.heat_control == hc,
    {
        self.heat_control = hc;
    }

    fn set(&mut self, 
        current_temp: i32,
        lower_desired_temp: i32,
        upper_desired_temp: i32,
        regulator_mode: RegulatorMode, 
        heat_control: HeatControl)
    ensures
        self.current_temp == current_temp
        && self.lower_desired_temp == lower_desired_temp
        && self.upper_desired_temp == upper_desired_temp
        && self.regulator_mode == regulator_mode
        && self.heat_control == heat_control,
    {
        self.lower_desired_temp = lower_desired_temp;
        self.upper_desired_temp = upper_desired_temp;
        self.current_temp = current_temp;
        self.regulator_mode = regulator_mode;
        self.heat_control = heat_control;
    }
}

// example persistent data
struct ManageHeatSource
{
    last_command: HeatControl,
    api: Api
}

impl ManageHeatSource
{
    fn build() -> (mhs: ManageHeatSource)
    ensures
        mhs.api.current_temp == 0
        && mhs.api.lower_desired_temp == 0
        && mhs.api.upper_desired_temp == 0
        && mhs.api.regulator_mode == RegulatorMode::INIT
        && mhs.api.heat_control == HeatControl::OFF
        && mhs.last_command == HeatControl::OFF,
    {
        ManageHeatSource {
            last_command: HeatControl::OFF,
            api: Api {
                current_temp: 0,
                lower_desired_temp: 0,
                upper_desired_temp: 0,
                regulator_mode: RegulatorMode::INIT, 
                heat_control: HeatControl::OFF
            }
        }
    }

    fn set(&mut self,
        current_temp: i32,
        lower_desired_temp: i32,
        upper_desired_temp: i32,
        regulator_mode: RegulatorMode, 
        heat_control: HeatControl)
    ensures
        self.api.current_temp == current_temp
        && self.api.lower_desired_temp == lower_desired_temp
        && self.api.upper_desired_temp == upper_desired_temp
        && self.api.regulator_mode == regulator_mode
        && self.api.heat_control == heat_control
        && self.last_command == heat_control,
    {
        self.api.set(current_temp, lower_desired_temp, upper_desired_temp, regulator_mode, heat_control);
        self.last_command = heat_control;
    }

    fn init_entry_point(&mut self)
    ensures
        self.api.heat_control == HeatControl::OFF
        ,
    {
        self.api.set_heat_control(HeatControl::OFF);
        self.last_command = self.api.heat_control;
        self.api.put();
    }

    fn time_triggered(&mut self)
    // verus contract
    requires
        old(self).api.lower_desired_temp <= old(self).api.upper_desired_temp
        ,
    ensures
        ((old(self).api.regulator_mode == RegulatorMode::INIT)
            ==> (self.api.heat_control == HeatControl::OFF))
        && 
        ((old(self).api.regulator_mode == RegulatorMode::FAILED)
            ==> (self.api.heat_control == HeatControl::OFF))
        &&
        ((old(self).api.regulator_mode == RegulatorMode::NORMAL && (old(self).api.current_temp < old(self).api.lower_desired_temp))
            ==> (self.api.heat_control == HeatControl::ON))
        &&
        ((old(self).api.regulator_mode == RegulatorMode::NORMAL && (old(self).api.current_temp > old(self).api.upper_desired_temp))
            ==> (self.api.heat_control == HeatControl::OFF))
        &&
        ((old(self).api.regulator_mode == RegulatorMode::NORMAL && (old(self).api.lower_desired_temp <= old(self).api.current_temp <= old(self).api.upper_desired_temp))
            ==> (self.api.heat_control == self.last_command))
        ,
    {
        // rust implementation
        self.api.heat_control = match self.api.regulator_mode {
            RegulatorMode::INIT => HeatControl::OFF,
            RegulatorMode::NORMAL =>
                if self.api.current_temp < self.api.lower_desired_temp {
                    HeatControl::ON
                } else if self.api.current_temp > self.api.upper_desired_temp {
                    HeatControl::OFF
                } else {
                    self.last_command
                }
            _ => HeatControl::OFF     // regulator_mode == RegulatorMode::FAIL
        };
        self.last_command = self.api.heat_control;
    }
}


fn main() {
    // example calls
    let mut manage_heat_source = ManageHeatSource::build();
    manage_heat_source.init_entry_point();
    manage_heat_source.set(82, 80, 85, RegulatorMode::NORMAL, HeatControl::ON);
    manage_heat_source.time_triggered();
}

} // verus!