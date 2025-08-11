# Isolette::Isolette.Single_Sensor

## AADL Architecture
![arch.svg](../../aadl/diagrams/arch.svg)
|System: [Isolette::Isolette.Single_Sensor]()|
|:--|

|Thread: Regulate::Manage_Regulator_Interface.i |
|:--|
|Type: [Manage_Regulator_Interface](../../aadl/aadl/packages/Regulate.aadl#L155-L299)<br>Implementation: [Manage_Regulator_Interface.i](../../aadl/aadl/packages/Regulate.aadl#L300-L302)<br>GUMBO: [Subclause](../../aadl/aadl/packages/Regulate.aadl#L212-L297)|
|Periodic |

|Thread: Regulate::Manage_Heat_Source.i |
|:--|
|Type: [Manage_Heat_Source](../../aadl/aadl/packages/Regulate.aadl#L498-L576)<br>Implementation: [Manage_Heat_Source.i](../../aadl/aadl/packages/Regulate.aadl#L577-L579)<br>GUMBO: [Subclause](../../aadl/aadl/packages/Regulate.aadl#L514-L575)|
|Periodic |

|Thread: Regulate::Manage_Regulator_Mode.i |
|:--|
|Type: [Manage_Regulator_Mode](../../aadl/aadl/packages/Regulate.aadl#L343-L450)<br>Implementation: [Manage_Regulator_Mode.i](../../aadl/aadl/packages/Regulate.aadl#L451-L453)<br>GUMBO: [Subclause](../../aadl/aadl/packages/Regulate.aadl#L365-L448)|
|Periodic |

|Thread: Regulate::Detect_Regulator_Failure.i |
|:--|
|Type: [Detect_Regulator_Failure](../../aadl/aadl/packages/Regulate.aadl#L612-L619)<br>Implementation: [Detect_Regulator_Failure.i](../../aadl/aadl/packages/Regulate.aadl#L620-L622)|
|Periodic |

|Thread: Monitor::Manage_Monitor_Interface.i |
|:--|
|Type: [Manage_Monitor_Interface](../../aadl/aadl/packages/Monitor.aadl#L143-L165)<br>Implementation: [Manage_Monitor_Interface.i](../../aadl/aadl/packages/Monitor.aadl#L166-L259)<br>
GUMBO: [Subclause](../../aadl/aadl/packages/Monitor.aadl#L173-L258)|
|Periodic |

|Thread: Monitor::Manage_Alarm.i |
|:--|
|Type: [Manage_Alarm](../../aadl/aadl/packages/Monitor.aadl#L409-L425)<br>Implementation: [Manage_Alarm.i](../../aadl/aadl/packages/Monitor.aadl#L426-L519)<br>
GUMBO: [Subclause](../../aadl/aadl/packages/Monitor.aadl#L428-L518)|
|Periodic |

|Thread: Monitor::Manage_Monitor_Mode.i |
|:--|
|Type: [Manage_Monitor_Mode](../../aadl/aadl/packages/Monitor.aadl#L301-L315)<br>Implementation: [Manage_Monitor_Mode.i](../../aadl/aadl/packages/Monitor.aadl#L316-L364)<br>
GUMBO: [Subclause](../../aadl/aadl/packages/Monitor.aadl#L318-L363)|
|Periodic |

|Thread: Monitor::Detect_Monitor_Failure.i |
|:--|
|Type: [Detect_Monitor_Failure](../../aadl/aadl/packages/Monitor.aadl#L551-L557)<br>Implementation: [Detect_Monitor_Failure.i](../../aadl/aadl/packages/Monitor.aadl#L558-L560)|
|Periodic |

|Thread: Operator_Interface::Operator_Interface_Thread.i |
|:--|
|Type: [Operator_Interface_Thread](../../aadl/aadl/packages/Operator_Interface.aadl#L93-L119)<br>Implementation: [Operator_Interface_Thread.i](../../aadl/aadl/packages/Operator_Interface.aadl#L120-L122)<br>GUMBO: [Subclause](../../aadl/aadl/packages/Operator_Interface.aadl#L107-L118)|
|Periodic |

|Thread: Devices::Temperature_Sensor.i |
|:--|
|Type: [Temperature_Sensor](../../aadl/aadl/packages/Devices.aadl#L85-L90)<br>Implementation: [Temperature_Sensor.i](../../aadl/aadl/packages/Devices.aadl#L91-L93)|
|Periodic |

|Thread: Devices::Heat_Source.i |
|:--|
|Type: [Heat_Source](../../aadl/aadl/packages/Devices.aadl#L129-L134)<br>Implementation: [Heat_Source.i](../../aadl/aadl/packages/Devices.aadl#L135-L137)|
|Periodic |


## Rust Code


### Behavior Code
#### mri: Regulate::Manage_Regulator_Interface.i

 - **Entry Points**


    Initialize: [Rust](crates/thermostat_rt_mri_mri/src/component/thermostat_rt_mri_mri_app.rs#L26-L54)

    TimeTriggered: [Rust](crates/thermostat_rt_mri_mri/src/component/thermostat_rt_mri_mri_app.rs#L56-L258)


- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Regulate.aadl#L160-L160'>upper_desired_tempWstatus</a></td>
        <td>In</td><td>Data</td>
        <td>Isolette_Data_Model::TempWstatus.i</td><td><a title='Memory Map' href='microkit.system#L68-L72'>Memory Map</a> -> <a title='Rust API' href='crates/thermostat_rt_mri_mri/src/bridge/thermostat_rt_mri_mri_api.rs#L229-L243'>Rust API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Regulate.aadl#L161-L161'>lower_desired_tempWstatus</a></td>
        <td>In</td><td>Data</td>
        <td>Isolette_Data_Model::TempWstatus.i</td><td><a title='Memory Map' href='microkit.system#L63-L67'>Memory Map</a> -> <a title='Rust API' href='crates/thermostat_rt_mri_mri/src/bridge/thermostat_rt_mri_mri_api.rs#L214-L228'>Rust API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Regulate.aadl#L163-L163'>current_tempWstatus</a></td>
        <td>In</td><td>Data</td>
        <td>Isolette_Data_Model::TempWstatus.i</td><td><a title='Memory Map' href='microkit.system#L73-L77'>Memory Map</a> -> <a title='Rust API' href='crates/thermostat_rt_mri_mri/src/bridge/thermostat_rt_mri_mri_api.rs#L244-L258'>Rust API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Regulate.aadl#L165-L165'>regulator_mode</a></td>
        <td>In</td><td>Data</td>
        <td>Isolette_Data_Model::Regulator_Mode</td><td><a title='Memory Map' href='microkit.system#L58-L62'>Memory Map</a> -> <a title='Rust API' href='crates/thermostat_rt_mri_mri/src/bridge/thermostat_rt_mri_mri_api.rs#L199-L213'>Rust API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Regulate.aadl#L169-L169'>upper_desired_temp</a></td>
        <td>Out</td><td>Data</td>
        <td>Isolette_Data_Model::Temp.i</td><td><a title='Rust API' href='crates/thermostat_rt_mri_mri/src/bridge/thermostat_rt_mri_mri_api.rs#L111-L127'>Rust API</a> -> <a title='Memory Map' href='microkit.system#L33-L37'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Regulate.aadl#L170-L170'>lower_desired_temp</a></td>
        <td>Out</td><td>Data</td>
        <td>Isolette_Data_Model::Temp.i</td><td><a title='Rust API' href='crates/thermostat_rt_mri_mri/src/bridge/thermostat_rt_mri_mri_api.rs#L128-L144'>Rust API</a> -> <a title='Memory Map' href='microkit.system#L38-L42'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Regulate.aadl#L172-L172'>displayed_temp</a></td>
        <td>Out</td><td>Data</td>
        <td>Isolette_Data_Model::Temp.i</td><td><a title='Rust API' href='crates/thermostat_rt_mri_mri/src/bridge/thermostat_rt_mri_mri_api.rs#L145-L161'>Rust API</a> -> <a title='Memory Map' href='microkit.system#L43-L47'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Regulate.aadl#L174-L174'>regulator_status</a></td>
        <td>Out</td><td>Data</td>
        <td>Isolette_Data_Model::Status</td><td><a title='Rust API' href='crates/thermostat_rt_mri_mri/src/bridge/thermostat_rt_mri_mri_api.rs#L162-L178'>Rust API</a> -> <a title='Memory Map' href='microkit.system#L48-L52'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Regulate.aadl#L176-L176'>interface_failure</a></td>
        <td>Out</td><td>Data</td>
        <td>Isolette_Data_Model::Failure_Flag.i</td><td><a title='Rust API' href='crates/thermostat_rt_mri_mri/src/bridge/thermostat_rt_mri_mri_api.rs#L179-L195'>Rust API</a> -> <a title='Memory Map' href='microkit.system#L53-L57'>Memory Map</a></td></tr>
    </table>
- **GUMBO**

    <table>
    <tr><th colspan=4>Initialize</th></tr>
    <tr><td>guarantee RegulatorStatusIsInitiallyInit</td>
    <td><a href=../../aadl/aadl/packages/Regulate.aadl#L219-L220>GUMBO</a></td>
    <td><a href=crates/thermostat_rt_mri_mri/src/component/thermostat_rt_mri_mri_app.rs#L31-L33>Verus</a></td>
    <td><a href=crates/thermostat_rt_mri_mri/src/bridge/thermostat_rt_mri_mri_GUMBOX.rs#L27-L30>GUMBOX</a></td>
    </tr></table>
    <table>
    <tr><th colspan=4>Compute</th></tr>
    <tr><td>assume lower_is_not_higher_than_upper</td>
    <td><a href=../../aadl/aadl/packages/Regulate.aadl#L225-L225>GUMBO</a></td>
    <td><a href=crates/thermostat_rt_mri_mri/src/component/thermostat_rt_mri_mri_app.rs#L61-L63>Verus</a></td>
    <td><a href=crates/thermostat_rt_mri_mri/src/bridge/thermostat_rt_mri_mri_GUMBOX.rs#L74-L79>GUMBOX</a></td>
    </tr>
    <tr><td>case REQ_MRI_1</td>
    <td><a href=../../aadl/aadl/packages/Regulate.aadl#L230-L234>GUMBO</a></td>
    <td><a href=crates/thermostat_rt_mri_mri/src/component/thermostat_rt_mri_mri_app.rs#L66-L68>Verus</a></td>
    <td><a href=crates/thermostat_rt_mri_mri/src/bridge/thermostat_rt_mri_mri_GUMBOX.rs#L121-L128>GUMBOX</a></td>
    </tr>
    <tr><td>case REQ_MRI_2</td>
    <td><a href=../../aadl/aadl/packages/Regulate.aadl#L236-L240>GUMBO</a></td>
    <td><a href=crates/thermostat_rt_mri_mri/src/component/thermostat_rt_mri_mri_app.rs#L72-L74>Verus</a></td>
    <td><a href=crates/thermostat_rt_mri_mri/src/bridge/thermostat_rt_mri_mri_GUMBOX.rs#L137-L144>GUMBOX</a></td>
    </tr>
    <tr><td>case REQ_MRI_3</td>
    <td><a href=../../aadl/aadl/packages/Regulate.aadl#L242-L246>GUMBO</a></td>
    <td><a href=crates/thermostat_rt_mri_mri/src/component/thermostat_rt_mri_mri_app.rs#L78-L80>Verus</a></td>
    <td><a href=crates/thermostat_rt_mri_mri/src/bridge/thermostat_rt_mri_mri_GUMBOX.rs#L153-L160>GUMBOX</a></td>
    </tr>
    <tr><td>case REQ_MRI_4</td>
    <td><a href=../../aadl/aadl/packages/Regulate.aadl#L250-L257>GUMBO</a></td>
    <td><a href=crates/thermostat_rt_mri_mri/src/component/thermostat_rt_mri_mri_app.rs#L84-L86>Verus</a></td>
    <td><a href=crates/thermostat_rt_mri_mri/src/bridge/thermostat_rt_mri_mri_GUMBOX.rs#L171-L179>GUMBOX</a></td>
    </tr>
    <tr><td>case REQ_MRI_5</td>
    <td><a href=../../aadl/aadl/packages/Regulate.aadl#L259-L263>GUMBO</a></td>
    <td><a href=crates/thermostat_rt_mri_mri/src/component/thermostat_rt_mri_mri_app.rs#L91-L93>Verus</a></td>
    <td><a href=crates/thermostat_rt_mri_mri/src/bridge/thermostat_rt_mri_mri_GUMBOX.rs#L186-L191>GUMBOX</a></td>
    </tr>
    <tr><td>case REQ_MRI_6</td>
    <td><a href=../../aadl/aadl/packages/Regulate.aadl#L267-L272>GUMBO</a></td>
    <td><a href=crates/thermostat_rt_mri_mri/src/component/thermostat_rt_mri_mri_app.rs#L97-L99>Verus</a></td>
    <td><a href=crates/thermostat_rt_mri_mri/src/bridge/thermostat_rt_mri_mri_GUMBOX.rs#L201-L209>GUMBOX</a></td>
    </tr>
    <tr><td>case REQ_MRI_7</td>
    <td><a href=../../aadl/aadl/packages/Regulate.aadl#L274-L279>GUMBO</a></td>
    <td><a href=crates/thermostat_rt_mri_mri/src/component/thermostat_rt_mri_mri_app.rs#L105-L107>Verus</a></td>
    <td><a href=crates/thermostat_rt_mri_mri/src/bridge/thermostat_rt_mri_mri_GUMBOX.rs#L220-L229>GUMBOX</a></td>
    </tr>
    <tr><td>case REQ_MRI_8</td>
    <td><a href=../../aadl/aadl/packages/Regulate.aadl#L283-L289>GUMBO</a></td>
    <td><a href=crates/thermostat_rt_mri_mri/src/component/thermostat_rt_mri_mri_app.rs#L113-L115>Verus</a></td>
    <td><a href=crates/thermostat_rt_mri_mri/src/bridge/thermostat_rt_mri_mri_GUMBOX.rs#L241-L254>GUMBOX</a></td>
    </tr>
    <tr><td>case REQ_MRI_9</td>
    <td><a href=../../aadl/aadl/packages/Regulate.aadl#L291-L296>GUMBO</a></td>
    <td><a href=crates/thermostat_rt_mri_mri/src/component/thermostat_rt_mri_mri_app.rs#L121-L123>Verus</a></td>
    <td><a href=crates/thermostat_rt_mri_mri/src/bridge/thermostat_rt_mri_mri_GUMBOX.rs#L262-L267>GUMBOX</a></td>
    </tr></table>
    <table>
    <tr><th colspan=4>GUMBO Methods</th></tr>
    <tr><td>ROUND</td>
    <td><a href=../../aadl/aadl/packages/Regulate.aadl#L215-L215>GUMBO</a></td>
    <td><a href=crates/thermostat_rt_mri_mri/src/component/thermostat_rt_mri_mri_app.rs#L274-L277>Verus</a></td>
    <td><a href=crates/thermostat_rt_mri_mri/src/bridge/thermostat_rt_mri_mri_GUMBOX.rs#L17-L20>GUMBOX</a></td>
    </tr></table>


#### mhs: Regulate::Manage_Heat_Source.i

 - **Entry Points**


    Initialize: [Rust](crates/thermostat_rt_mhs_mhs/src/component/thermostat_rt_mhs_mhs_app.rs#L32-L54)

    TimeTriggered: [Rust](crates/thermostat_rt_mhs_mhs/src/component/thermostat_rt_mhs_mhs_app.rs#L56-L150)


- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Regulate.aadl#L503-L503'>current_tempWstatus</a></td>
        <td>In</td><td>Data</td>
        <td>Isolette_Data_Model::TempWstatus.i</td><td><a title='Memory Map' href='microkit.system#L105-L109'>Memory Map</a> -> <a title='Rust API' href='crates/thermostat_rt_mhs_mhs/src/bridge/thermostat_rt_mhs_mhs_api.rs#L124-L134'>Rust API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Regulate.aadl#L505-L505'>lower_desired_temp</a></td>
        <td>In</td><td>Data</td>
        <td>Isolette_Data_Model::Temp.i</td><td><a title='Memory Map' href='microkit.system#L90-L94'>Memory Map</a> -> <a title='Rust API' href='crates/thermostat_rt_mhs_mhs/src/bridge/thermostat_rt_mhs_mhs_api.rs#L102-L112'>Rust API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Regulate.aadl#L506-L506'>upper_desired_temp</a></td>
        <td>In</td><td>Data</td>
        <td>Isolette_Data_Model::Temp.i</td><td><a title='Memory Map' href='microkit.system#L85-L89'>Memory Map</a> -> <a title='Rust API' href='crates/thermostat_rt_mhs_mhs/src/bridge/thermostat_rt_mhs_mhs_api.rs#L91-L101'>Rust API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Regulate.aadl#L508-L508'>regulator_mode</a></td>
        <td>In</td><td>Data</td>
        <td>Isolette_Data_Model::Regulator_Mode</td><td><a title='Memory Map' href='microkit.system#L100-L104'>Memory Map</a> -> <a title='Rust API' href='crates/thermostat_rt_mhs_mhs/src/bridge/thermostat_rt_mhs_mhs_api.rs#L113-L123'>Rust API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Regulate.aadl#L512-L512'>heat_control</a></td>
        <td>Out</td><td>Data</td>
        <td>Isolette_Data_Model::On_Off</td><td><a title='Rust API' href='crates/thermostat_rt_mhs_mhs/src/bridge/thermostat_rt_mhs_mhs_api.rs#L75-L87'>Rust API</a> -> <a title='Memory Map' href='microkit.system#L95-L99'>Memory Map</a></td></tr>
    </table>
- **GUMBO**

    <table>
    <tr><th colspan=3>State Variables</th></tr>
    <tr><td>lastCmd</td>
    <td><a href=../../aadl/aadl/packages/Regulate.aadl#L518-L518>GUMBO</a></td>
    <td><a href=crates/thermostat_rt_mhs_mhs/src/component/thermostat_rt_mhs_mhs_app.rs#L18-L20>Verus</a></td></tr></table>
    <table>
    <tr><th colspan=4>Initialize</th></tr>
    <tr><td>guarantee initlastCmd</td>
    <td><a href=../../aadl/aadl/packages/Regulate.aadl#L522-L523>GUMBO</a></td>
    <td><a href=crates/thermostat_rt_mhs_mhs/src/component/thermostat_rt_mhs_mhs_app.rs#L37-L39>Verus</a></td>
    <td><a href=crates/thermostat_rt_mhs_mhs/src/bridge/thermostat_rt_mhs_mhs_GUMBOX.rs#L22-L25>GUMBOX</a></td>
    </tr>
    <tr><td>guarantee REQ_MHS_1</td>
    <td><a href=../../aadl/aadl/packages/Regulate.aadl#L524-L527>GUMBO</a></td>
    <td><a href=crates/thermostat_rt_mhs_mhs/src/component/thermostat_rt_mhs_mhs_app.rs#L39-L41>Verus</a></td>
    <td><a href=crates/thermostat_rt_mhs_mhs/src/bridge/thermostat_rt_mhs_mhs_GUMBOX.rs#L35-L38>GUMBOX</a></td>
    </tr></table>
    <table>
    <tr><th colspan=4>Compute</th></tr>
    <tr><td>assume lower_is_lower_temp</td>
    <td><a href=../../aadl/aadl/packages/Regulate.aadl#L532-L532>GUMBO</a></td>
    <td><a href=crates/thermostat_rt_mhs_mhs/src/component/thermostat_rt_mhs_mhs_app.rs#L61-L63>Verus</a></td>
    <td><a href=crates/thermostat_rt_mhs_mhs/src/bridge/thermostat_rt_mhs_mhs_GUMBOX.rs#L71-L76>GUMBOX</a></td>
    </tr>
    <tr><td>guarantee lastCmd</td>
    <td><a href=../../aadl/aadl/packages/Regulate.aadl#L535-L536>GUMBO</a></td>
    <td><a href=crates/thermostat_rt_mhs_mhs/src/component/thermostat_rt_mhs_mhs_app.rs#L66-L68>Verus</a></td>
    <td><a href=crates/thermostat_rt_mhs_mhs/src/bridge/thermostat_rt_mhs_mhs_GUMBOX.rs#L120-L125>GUMBOX</a></td>
    </tr>
    <tr><td>case REQ_MHS_1</td>
    <td><a href=../../aadl/aadl/packages/Regulate.aadl#L540-L544>GUMBO</a></td>
    <td><a href=crates/thermostat_rt_mhs_mhs/src/component/thermostat_rt_mhs_mhs_app.rs#L69-L71>Verus</a></td>
    <td><a href=crates/thermostat_rt_mhs_mhs/src/bridge/thermostat_rt_mhs_mhs_GUMBOX.rs#L148-L155>GUMBOX</a></td>
    </tr>
    <tr><td>case REQ_MHS_2</td>
    <td><a href=../../aadl/aadl/packages/Regulate.aadl#L546-L551>GUMBO</a></td>
    <td><a href=crates/thermostat_rt_mhs_mhs/src/component/thermostat_rt_mhs_mhs_app.rs#L75-L77>Verus</a></td>
    <td><a href=crates/thermostat_rt_mhs_mhs/src/bridge/thermostat_rt_mhs_mhs_GUMBOX.rs#L166-L176>GUMBOX</a></td>
    </tr>
    <tr><td>case REQ_MHS_3</td>
    <td><a href=../../aadl/aadl/packages/Regulate.aadl#L553-L558>GUMBO</a></td>
    <td><a href=crates/thermostat_rt_mhs_mhs/src/component/thermostat_rt_mhs_mhs_app.rs#L82-L84>Verus</a></td>
    <td><a href=crates/thermostat_rt_mhs_mhs/src/bridge/thermostat_rt_mhs_mhs_GUMBOX.rs#L187-L197>GUMBOX</a></td>
    </tr>
    <tr><td>case REQ_MHS_4</td>
    <td><a href=../../aadl/aadl/packages/Regulate.aadl#L560-L568>GUMBO</a></td>
    <td><a href=crates/thermostat_rt_mhs_mhs/src/component/thermostat_rt_mhs_mhs_app.rs#L89-L91>Verus</a></td>
    <td><a href=crates/thermostat_rt_mhs_mhs/src/bridge/thermostat_rt_mhs_mhs_GUMBOX.rs#L212-L225>GUMBOX</a></td>
    </tr>
    <tr><td>case REQ_MHS_5</td>
    <td><a href=../../aadl/aadl/packages/Regulate.aadl#L570-L574>GUMBO</a></td>
    <td><a href=crates/thermostat_rt_mhs_mhs/src/component/thermostat_rt_mhs_mhs_app.rs#L99-L101>Verus</a></td>
    <td><a href=crates/thermostat_rt_mhs_mhs/src/bridge/thermostat_rt_mhs_mhs_GUMBOX.rs#L234-L241>GUMBOX</a></td>
    </tr></table>


#### mrm: Regulate::Manage_Regulator_Mode.i

 - **Entry Points**


    Initialize: [Rust](crates/thermostat_rt_mrm_mrm/src/component/thermostat_rt_mrm_mrm_app.rs#L32-L48)

    TimeTriggered: [Rust](crates/thermostat_rt_mrm_mrm/src/component/thermostat_rt_mrm_mrm_app.rs#L50-L162)


- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Regulate.aadl#L348-L348'>current_tempWstatus</a></td>
        <td>In</td><td>Data</td>
        <td>Isolette_Data_Model::TempWstatus.i</td><td><a title='Memory Map' href='microkit.system#L132-L136'>Memory Map</a> -> <a title='Rust API' href='crates/thermostat_rt_mrm_mrm/src/bridge/thermostat_rt_mrm_mrm_api.rs#L99-L108'>Rust API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Regulate.aadl#L350-L350'>interface_failure</a></td>
        <td>In</td><td>Data</td>
        <td>Isolette_Data_Model::Failure_Flag.i</td><td><a title='Memory Map' href='microkit.system#L117-L121'>Memory Map</a> -> <a title='Rust API' href='crates/thermostat_rt_mrm_mrm/src/bridge/thermostat_rt_mrm_mrm_api.rs#L79-L88'>Rust API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Regulate.aadl#L352-L352'>internal_failure</a></td>
        <td>In</td><td>Data</td>
        <td>Isolette_Data_Model::Failure_Flag.i</td><td><a title='Memory Map' href='microkit.system#L127-L131'>Memory Map</a> -> <a title='Rust API' href='crates/thermostat_rt_mrm_mrm/src/bridge/thermostat_rt_mrm_mrm_api.rs#L89-L98'>Rust API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Regulate.aadl#L356-L356'>regulator_mode</a></td>
        <td>Out</td><td>Data</td>
        <td>Isolette_Data_Model::Regulator_Mode</td><td><a title='Rust API' href='crates/thermostat_rt_mrm_mrm/src/bridge/thermostat_rt_mrm_mrm_api.rs#L64-L75'>Rust API</a> -> <a title='Memory Map' href='microkit.system#L122-L126'>Memory Map</a></td></tr>
    </table>
- **GUMBO**

    <table>
    <tr><th colspan=3>State Variables</th></tr>
    <tr><td>lastRegulatorMode</td>
    <td><a href=../../aadl/aadl/packages/Regulate.aadl#L368-L368>GUMBO</a></td>
    <td><a href=crates/thermostat_rt_mrm_mrm/src/component/thermostat_rt_mrm_mrm_app.rs#L18-L20>Verus</a></td></tr></table>
    <table>
    <tr><th colspan=4>Initialize</th></tr>
    <tr><td>guarantee REQ_MRM_1</td>
    <td><a href=../../aadl/aadl/packages/Regulate.aadl#L376-L378>GUMBO</a></td>
    <td><a href=crates/thermostat_rt_mrm_mrm/src/component/thermostat_rt_mrm_mrm_app.rs#L37-L39>Verus</a></td>
    <td><a href=crates/thermostat_rt_mrm_mrm/src/bridge/thermostat_rt_mrm_mrm_GUMBOX.rs#L24-L27>GUMBOX</a></td>
    </tr></table>
    <table>
    <tr><th colspan=4>Compute</th></tr>
    <tr><td>case REQ_MRM_2</td>
    <td><a href=../../aadl/aadl/packages/Regulate.aadl#L384-L395>GUMBO</a></td>
    <td><a href=crates/thermostat_rt_mrm_mrm/src/component/thermostat_rt_mrm_mrm_app.rs#L55-L57>Verus</a></td>
    <td><a href=crates/thermostat_rt_mrm_mrm/src/bridge/thermostat_rt_mrm_mrm_GUMBOX.rs#L66-L80>GUMBOX</a></td>
    </tr>
    <tr><td>case REQ_MRM_Maintain_Normal</td>
    <td><a href=../../aadl/aadl/packages/Regulate.aadl#L397-L411>GUMBO</a></td>
    <td><a href=crates/thermostat_rt_mrm_mrm/src/component/thermostat_rt_mrm_mrm_app.rs#L67-L69>Verus</a></td>
    <td><a href=crates/thermostat_rt_mrm_mrm/src/bridge/thermostat_rt_mrm_mrm_GUMBOX.rs#L98-L112>GUMBOX</a></td>
    </tr>
    <tr><td>case REQ_MRM_3</td>
    <td><a href=../../aadl/aadl/packages/Regulate.aadl#L413-L425>GUMBO</a></td>
    <td><a href=crates/thermostat_rt_mrm_mrm/src/component/thermostat_rt_mrm_mrm_app.rs#L82-L84>Verus</a></td>
    <td><a href=crates/thermostat_rt_mrm_mrm/src/bridge/thermostat_rt_mrm_mrm_GUMBOX.rs#L128-L142>GUMBOX</a></td>
    </tr>
    <tr><td>case REQ_MRM_4</td>
    <td><a href=../../aadl/aadl/packages/Regulate.aadl#L427-L439>GUMBO</a></td>
    <td><a href=crates/thermostat_rt_mrm_mrm/src/component/thermostat_rt_mrm_mrm_app.rs#L95-L97>Verus</a></td>
    <td><a href=crates/thermostat_rt_mrm_mrm/src/bridge/thermostat_rt_mrm_mrm_GUMBOX.rs#L158-L172>GUMBOX</a></td>
    </tr>
    <tr><td>case REQ_MRM_MaintainFailed</td>
    <td><a href=../../aadl/aadl/packages/Regulate.aadl#L441-L447>GUMBO</a></td>
    <td><a href=crates/thermostat_rt_mrm_mrm/src/component/thermostat_rt_mrm_mrm_app.rs#L108-L110>Verus</a></td>
    <td><a href=crates/thermostat_rt_mrm_mrm/src/bridge/thermostat_rt_mrm_mrm_GUMBOX.rs#L182-L190>GUMBOX</a></td>
    </tr></table>


#### drf: Regulate::Detect_Regulator_Failure.i

 - **Entry Points**


    Initialize: [Rust](crates/thermostat_rt_drf_drf/src/component/thermostat_rt_drf_drf_app.rs#L22-L28)

    TimeTriggered: [Rust](crates/thermostat_rt_drf_drf/src/component/thermostat_rt_drf_drf_app.rs#L30-L36)


- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Regulate.aadl#L617-L617'>internal_failure</a></td>
        <td>Out</td><td>Data</td>
        <td>Isolette_Data_Model::Failure_Flag.i</td><td><a title='Rust API' href='crates/thermostat_rt_drf_drf/src/bridge/thermostat_rt_drf_drf_api.rs#L32-L40'>Rust API</a> -> <a title='Memory Map' href='microkit.system#L144-L148'>Memory Map</a></td></tr>
    </table>


#### mmi: Monitor::Manage_Monitor_Interface.i

 - **Entry Points**


    Initialize: [Rust](crates/thermostat_mt_mmi_mmi/src/component/thermostat_mt_mmi_mmi_app.rs#L32-L58)

    TimeTriggered: [Rust](crates/thermostat_mt_mmi_mmi/src/component/thermostat_mt_mmi_mmi_app.rs#L60-L204)


- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Monitor.aadl#L148-L148'>upper_alarm_tempWstatus</a></td>
        <td>In</td><td>Data</td>
        <td>Isolette_Data_Model::TempWstatus.i</td><td><a title='Memory Map' href='microkit.system#L186-L190'>Memory Map</a> -> <a title='Rust API' href='crates/thermostat_mt_mmi_mmi/src/bridge/thermostat_mt_mmi_mmi_api.rs#L202-L220'>Rust API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Monitor.aadl#L150-L150'>lower_alarm_tempWstatus</a></td>
        <td>In</td><td>Data</td>
        <td>Isolette_Data_Model::TempWstatus.i</td><td><a title='Memory Map' href='microkit.system#L181-L185'>Memory Map</a> -> <a title='Rust API' href='crates/thermostat_mt_mmi_mmi/src/bridge/thermostat_mt_mmi_mmi_api.rs#L183-L201'>Rust API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Monitor.aadl#L152-L152'>current_tempWstatus</a></td>
        <td>In</td><td>Data</td>
        <td>Isolette_Data_Model::TempWstatus.i</td><td><a title='Memory Map' href='microkit.system#L191-L195'>Memory Map</a> -> <a title='Rust API' href='crates/thermostat_mt_mmi_mmi/src/bridge/thermostat_mt_mmi_mmi_api.rs#L221-L234'>Rust API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Monitor.aadl#L154-L154'>monitor_mode</a></td>
        <td>In</td><td>Data</td>
        <td>Isolette_Data_Model::Monitor_Mode</td><td><a title='Memory Map' href='microkit.system#L176-L180'>Memory Map</a> -> <a title='Rust API' href='crates/thermostat_mt_mmi_mmi/src/bridge/thermostat_mt_mmi_mmi_api.rs#L169-L182'>Rust API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Monitor.aadl#L158-L158'>upper_alarm_temp</a></td>
        <td>Out</td><td>Data</td>
        <td>Isolette_Data_Model::Temp.i</td><td><a title='Rust API' href='crates/thermostat_mt_mmi_mmi/src/bridge/thermostat_mt_mmi_mmi_api.rs#L102-L117'>Rust API</a> -> <a title='Memory Map' href='microkit.system#L156-L160'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Monitor.aadl#L160-L160'>lower_alarm_temp</a></td>
        <td>Out</td><td>Data</td>
        <td>Isolette_Data_Model::Temp.i</td><td><a title='Rust API' href='crates/thermostat_mt_mmi_mmi/src/bridge/thermostat_mt_mmi_mmi_api.rs#L118-L133'>Rust API</a> -> <a title='Memory Map' href='microkit.system#L161-L165'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Monitor.aadl#L162-L162'>monitor_status</a></td>
        <td>Out</td><td>Data</td>
        <td>Isolette_Data_Model::Status</td><td><a title='Rust API' href='crates/thermostat_mt_mmi_mmi/src/bridge/thermostat_mt_mmi_mmi_api.rs#L134-L149'>Rust API</a> -> <a title='Memory Map' href='microkit.system#L166-L170'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Monitor.aadl#L164-L164'>interface_failure</a></td>
        <td>Out</td><td>Data</td>
        <td>Isolette_Data_Model::Failure_Flag.i</td><td><a title='Rust API' href='crates/thermostat_mt_mmi_mmi/src/bridge/thermostat_mt_mmi_mmi_api.rs#L150-L165'>Rust API</a> -> <a title='Memory Map' href='microkit.system#L171-L175'>Memory Map</a></td></tr>
    </table>
- **GUMBO**

    <table>
    <tr><th colspan=3>State Variables</th></tr>
    <tr><td>lastCmd</td>
    <td><a href=../../aadl/aadl/packages/Monitor.aadl#L175-L175>GUMBO</a></td>
    <td><a href=crates/thermostat_mt_mmi_mmi/src/component/thermostat_mt_mmi_mmi_app.rs#L18-L20>Verus</a></td></tr></table>
    <table>
    <tr><th colspan=4>Integration</th></tr>
    <tr><td>assume Table_A_12_LowerAlarmTemp</td>
    <td><a href=../../aadl/aadl/packages/Monitor.aadl#L183-L185>GUMBO</a></td>
    <td><a href=crates/thermostat_mt_mmi_mmi/src/bridge/thermostat_mt_mmi_mmi_api.rs#L194-L195>Verus</a></td>
    <td><a href=crates/thermostat_mt_mmi_mmi/src/bridge/thermostat_mt_mmi_mmi_GUMBOX.rs#L40-L44>GUMBOX</a></td>
    </tr>
    <tr><td>assume Table_A_12_UpperAlarmTemp</td>
    <td><a href=../../aadl/aadl/packages/Monitor.aadl#L187-L189>GUMBO</a></td>
    <td><a href=crates/thermostat_mt_mmi_mmi/src/bridge/thermostat_mt_mmi_mmi_api.rs#L213-L214>Verus</a></td>
    <td><a href=crates/thermostat_mt_mmi_mmi/src/bridge/thermostat_mt_mmi_mmi_GUMBOX.rs#L28-L32>GUMBOX</a></td>
    </tr></table>
    <table>
    <tr><th colspan=4>Initialize</th></tr>
    <tr><td>guarantee monitorStatusInitiallyInit</td>
    <td><a href=../../aadl/aadl/packages/Monitor.aadl#L193-L194>GUMBO</a></td>
    <td><a href=crates/thermostat_mt_mmi_mmi/src/component/thermostat_mt_mmi_mmi_app.rs#L37-L39>Verus</a></td>
    <td><a href=crates/thermostat_mt_mmi_mmi/src/bridge/thermostat_mt_mmi_mmi_GUMBOX.rs#L51-L54>GUMBOX</a></td>
    </tr></table>
    <table>
    <tr><th colspan=4>Compute</th></tr>
    <tr><td>case REQ_MMI_1</td>
    <td><a href=../../aadl/aadl/packages/Monitor.aadl#L201-L205>GUMBO</a></td>
    <td><a href=crates/thermostat_mt_mmi_mmi/src/component/thermostat_mt_mmi_mmi_app.rs#L65-L67>Verus</a></td>
    <td><a href=crates/thermostat_mt_mmi_mmi/src/bridge/thermostat_mt_mmi_mmi_GUMBOX.rs#L121-L128>GUMBOX</a></td>
    </tr>
    <tr><td>case REQ_MMI_2</td>
    <td><a href=../../aadl/aadl/packages/Monitor.aadl#L207-L211>GUMBO</a></td>
    <td><a href=crates/thermostat_mt_mmi_mmi/src/component/thermostat_mt_mmi_mmi_app.rs#L71-L73>Verus</a></td>
    <td><a href=crates/thermostat_mt_mmi_mmi/src/bridge/thermostat_mt_mmi_mmi_GUMBOX.rs#L137-L144>GUMBOX</a></td>
    </tr>
    <tr><td>case REQ_MMI_3</td>
    <td><a href=../../aadl/aadl/packages/Monitor.aadl#L213-L219>GUMBO</a></td>
    <td><a href=crates/thermostat_mt_mmi_mmi/src/component/thermostat_mt_mmi_mmi_app.rs#L77-L79>Verus</a></td>
    <td><a href=crates/thermostat_mt_mmi_mmi/src/bridge/thermostat_mt_mmi_mmi_GUMBOX.rs#L155-L162>GUMBOX</a></td>
    </tr>
    <tr><td>case REQ_MMI_4</td>
    <td><a href=../../aadl/aadl/packages/Monitor.aadl#L222-L228>GUMBO</a></td>
    <td><a href=crates/thermostat_mt_mmi_mmi/src/component/thermostat_mt_mmi_mmi_app.rs#L85-L87>Verus</a></td>
    <td><a href=crates/thermostat_mt_mmi_mmi/src/bridge/thermostat_mt_mmi_mmi_GUMBOX.rs#L173-L182>GUMBOX</a></td>
    </tr>
    <tr><td>case REQ_MMI_5</td>
    <td><a href=../../aadl/aadl/packages/Monitor.aadl#L230-L237>GUMBO</a></td>
    <td><a href=crates/thermostat_mt_mmi_mmi/src/component/thermostat_mt_mmi_mmi_app.rs#L93-L95>Verus</a></td>
    <td><a href=crates/thermostat_mt_mmi_mmi/src/bridge/thermostat_mt_mmi_mmi_GUMBOX.rs#L193-L202>GUMBOX</a></td>
    </tr>
    <tr><td>case REQ_MMI_6</td>
    <td><a href=../../aadl/aadl/packages/Monitor.aadl#L241-L249>GUMBO</a></td>
    <td><a href=crates/thermostat_mt_mmi_mmi/src/component/thermostat_mt_mmi_mmi_app.rs#L101-L103>Verus</a></td>
    <td><a href=crates/thermostat_mt_mmi_mmi/src/bridge/thermostat_mt_mmi_mmi_GUMBOX.rs#L214-L227>GUMBOX</a></td>
    </tr>
    <tr><td>case REQ_MMI_7</td>
    <td><a href=../../aadl/aadl/packages/Monitor.aadl#L251-L256>GUMBO</a></td>
    <td><a href=crates/thermostat_mt_mmi_mmi/src/component/thermostat_mt_mmi_mmi_app.rs#L109-L111>Verus</a></td>
    <td><a href=crates/thermostat_mt_mmi_mmi/src/bridge/thermostat_mt_mmi_mmi_GUMBOX.rs#L235-L242>GUMBOX</a></td>
    </tr></table>
    <table>
    <tr><th colspan=4>GUMBO Methods</th></tr>
    <tr><td>timeout_condition_satisfied</td>
    <td><a href=../../aadl/aadl/packages/Monitor.aadl#L178-L178>GUMBO</a></td>
    <td><a href=crates/thermostat_mt_mmi_mmi/src/component/thermostat_mt_mmi_mmi_app.rs#L220-L223>Verus</a></td>
    <td><a href=crates/thermostat_mt_mmi_mmi/src/bridge/thermostat_mt_mmi_mmi_GUMBOX.rs#L17-L20>GUMBOX</a></td>
    </tr></table>


#### ma: Monitor::Manage_Alarm.i

 - **Entry Points**


    Initialize: [Rust](crates/thermostat_mt_ma_ma/src/component/thermostat_mt_ma_ma_app.rs#L31-L46)

    TimeTriggered: [Rust](crates/thermostat_mt_ma_ma/src/component/thermostat_mt_ma_ma_app.rs#L48-L126)


- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Monitor.aadl#L414-L414'>current_tempWstatus</a></td>
        <td>In</td><td>Data</td>
        <td>Isolette_Data_Model::TempWstatus.i</td><td><a title='Memory Map' href='microkit.system#L223-L227'>Memory Map</a> -> <a title='Rust API' href='crates/thermostat_mt_ma_ma/src/bridge/thermostat_mt_ma_ma_api.rs#L124-L134'>Rust API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Monitor.aadl#L416-L416'>lower_alarm_temp</a></td>
        <td>In</td><td>Data</td>
        <td>Isolette_Data_Model::Temp.i</td><td><a title='Memory Map' href='microkit.system#L208-L212'>Memory Map</a> -> <a title='Rust API' href='crates/thermostat_mt_ma_ma/src/bridge/thermostat_mt_ma_ma_api.rs#L102-L112'>Rust API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Monitor.aadl#L418-L418'>upper_alarm_temp</a></td>
        <td>In</td><td>Data</td>
        <td>Isolette_Data_Model::Temp.i</td><td><a title='Memory Map' href='microkit.system#L203-L207'>Memory Map</a> -> <a title='Rust API' href='crates/thermostat_mt_ma_ma/src/bridge/thermostat_mt_ma_ma_api.rs#L91-L101'>Rust API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Monitor.aadl#L420-L420'>monitor_mode</a></td>
        <td>In</td><td>Data</td>
        <td>Isolette_Data_Model::Monitor_Mode</td><td><a title='Memory Map' href='microkit.system#L218-L222'>Memory Map</a> -> <a title='Rust API' href='crates/thermostat_mt_ma_ma/src/bridge/thermostat_mt_ma_ma_api.rs#L113-L123'>Rust API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Monitor.aadl#L424-L424'>alarm_control</a></td>
        <td>Out</td><td>Data</td>
        <td>Isolette_Data_Model::On_Off</td><td><a title='Rust API' href='crates/thermostat_mt_ma_ma/src/bridge/thermostat_mt_ma_ma_api.rs#L75-L87'>Rust API</a> -> <a title='Memory Map' href='microkit.system#L213-L217'>Memory Map</a></td></tr>
    </table>
- **GUMBO**

    <table>
    <tr><th colspan=3>State Variables</th></tr>
    <tr><td>lastCmd</td>
    <td><a href=../../aadl/aadl/packages/Monitor.aadl#L430-L430>GUMBO</a></td>
    <td><a href=crates/thermostat_mt_ma_ma/src/component/thermostat_mt_ma_ma_app.rs#L17-L19>Verus</a></td></tr></table>
    <table>
    <tr><th colspan=4>Initialize</th></tr>
    <tr><td>guarantee REQ_MA_1</td>
    <td><a href=../../aadl/aadl/packages/Monitor.aadl#L437-L441>GUMBO</a></td>
    <td><a href=crates/thermostat_mt_ma_ma/src/component/thermostat_mt_ma_ma_app.rs#L36-L38>Verus</a></td>
    <td><a href=crates/thermostat_mt_ma_ma/src/bridge/thermostat_mt_ma_ma_GUMBOX.rs#L31-L37>GUMBOX</a></td>
    </tr></table>
    <table>
    <tr><th colspan=4>Compute</th></tr>
    <tr><td>assume Figure_A_7</td>
    <td><a href=../../aadl/aadl/packages/Monitor.aadl#L446-L450>GUMBO</a></td>
    <td><a href=crates/thermostat_mt_ma_ma/src/component/thermostat_mt_ma_ma_app.rs#L53-L55>Verus</a></td>
    <td><a href=crates/thermostat_mt_ma_ma/src/bridge/thermostat_mt_ma_ma_GUMBOX.rs#L73-L78>GUMBOX</a></td>
    </tr>
    <tr><td>assume Table_A_12_LowerAlarmTemp</td>
    <td><a href=../../aadl/aadl/packages/Monitor.aadl#L452-L454>GUMBO</a></td>
    <td><a href=crates/thermostat_mt_ma_ma/src/component/thermostat_mt_ma_ma_app.rs#L59-L61>Verus</a></td>
    <td><a href=crates/thermostat_mt_ma_ma/src/bridge/thermostat_mt_ma_ma_GUMBOX.rs#L87-L91>GUMBOX</a></td>
    </tr>
    <tr><td>assume Table_A_12_UpperAlarmTemp</td>
    <td><a href=../../aadl/aadl/packages/Monitor.aadl#L456-L458>GUMBO</a></td>
    <td><a href=crates/thermostat_mt_ma_ma/src/component/thermostat_mt_ma_ma_app.rs#L64-L66>Verus</a></td>
    <td><a href=crates/thermostat_mt_ma_ma/src/bridge/thermostat_mt_ma_ma_GUMBOX.rs#L100-L104>GUMBOX</a></td>
    </tr>
    <tr><td>case REQ_MA_1</td>
    <td><a href=../../aadl/aadl/packages/Monitor.aadl#L462-L468>GUMBO</a></td>
    <td><a href=crates/thermostat_mt_ma_ma/src/component/thermostat_mt_ma_ma_app.rs#L72-L74>Verus</a></td>
    <td><a href=crates/thermostat_mt_ma_ma/src/bridge/thermostat_mt_ma_ma_GUMBOX.rs#L151-L160>GUMBOX</a></td>
    </tr>
    <tr><td>case REQ_MA_2</td>
    <td><a href=../../aadl/aadl/packages/Monitor.aadl#L470-L479>GUMBO</a></td>
    <td><a href=crates/thermostat_mt_ma_ma/src/component/thermostat_mt_ma_ma_app.rs#L79-L81>Verus</a></td>
    <td><a href=crates/thermostat_mt_ma_ma/src/bridge/thermostat_mt_ma_ma_GUMBOX.rs#L174-L188>GUMBOX</a></td>
    </tr>
    <tr><td>case REQ_MA_3</td>
    <td><a href=../../aadl/aadl/packages/Monitor.aadl#L481-L496>GUMBO</a></td>
    <td><a href=crates/thermostat_mt_ma_ma/src/component/thermostat_mt_ma_ma_app.rs#L89-L91>Verus</a></td>
    <td><a href=crates/thermostat_mt_ma_ma/src/bridge/thermostat_mt_ma_ma_GUMBOX.rs#L206-L223>GUMBOX</a></td>
    </tr>
    <tr><td>case REQ_MA_4</td>
    <td><a href=../../aadl/aadl/packages/Monitor.aadl#L498-L509>GUMBO</a></td>
    <td><a href=crates/thermostat_mt_ma_ma/src/component/thermostat_mt_ma_ma_app.rs#L104-L106>Verus</a></td>
    <td><a href=crates/thermostat_mt_ma_ma/src/bridge/thermostat_mt_ma_ma_GUMBOX.rs#L238-L252>GUMBOX</a></td>
    </tr>
    <tr><td>case REQ_MA_5</td>
    <td><a href=../../aadl/aadl/packages/Monitor.aadl#L511-L517>GUMBO</a></td>
    <td><a href=crates/thermostat_mt_ma_ma/src/component/thermostat_mt_ma_ma_app.rs#L115-L117>Verus</a></td>
    <td><a href=crates/thermostat_mt_ma_ma/src/bridge/thermostat_mt_ma_ma_GUMBOX.rs#L262-L271>GUMBOX</a></td>
    </tr></table>
    <table>
    <tr><th colspan=4>GUMBO Methods</th></tr>
    <tr><td>timeout_condition_satisfied</td>
    <td><a href=../../aadl/aadl/packages/Monitor.aadl#L433-L433>GUMBO</a></td>
    <td><a href=crates/thermostat_mt_ma_ma/src/component/thermostat_mt_ma_ma_app.rs#L142-L145>Verus</a></td>
    <td><a href=crates/thermostat_mt_ma_ma/src/bridge/thermostat_mt_ma_ma_GUMBOX.rs#L17-L20>GUMBOX</a></td>
    </tr></table>


#### mmm: Monitor::Manage_Monitor_Mode.i

 - **Entry Points**


    Initialize: [Rust](crates/thermostat_mt_mmm_mmm/src/component/thermostat_mt_mmm_mmm_app.rs#L57-L72)

    TimeTriggered: [Rust](crates/thermostat_mt_mmm_mmm/src/component/thermostat_mt_mmm_mmm_app.rs#L74-L159)


- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Monitor.aadl#L306-L306'>current_tempWstatus</a></td>
        <td>In</td><td>Data</td>
        <td>Isolette_Data_Model::TempWstatus.i</td><td><a title='Memory Map' href='microkit.system#L250-L254'>Memory Map</a> -> <a title='Rust API' href='crates/thermostat_mt_mmm_mmm/src/bridge/thermostat_mt_mmm_mmm_api.rs#L99-L108'>Rust API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Monitor.aadl#L308-L308'>interface_failure</a></td>
        <td>In</td><td>Data</td>
        <td>Isolette_Data_Model::Failure_Flag.i</td><td><a title='Memory Map' href='microkit.system#L235-L239'>Memory Map</a> -> <a title='Rust API' href='crates/thermostat_mt_mmm_mmm/src/bridge/thermostat_mt_mmm_mmm_api.rs#L79-L88'>Rust API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Monitor.aadl#L310-L310'>internal_failure</a></td>
        <td>In</td><td>Data</td>
        <td>Isolette_Data_Model::Failure_Flag.i</td><td><a title='Memory Map' href='microkit.system#L245-L249'>Memory Map</a> -> <a title='Rust API' href='crates/thermostat_mt_mmm_mmm/src/bridge/thermostat_mt_mmm_mmm_api.rs#L89-L98'>Rust API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Monitor.aadl#L314-L314'>monitor_mode</a></td>
        <td>Out</td><td>Data</td>
        <td>Isolette_Data_Model::Monitor_Mode</td><td><a title='Rust API' href='crates/thermostat_mt_mmm_mmm/src/bridge/thermostat_mt_mmm_mmm_api.rs#L64-L75'>Rust API</a> -> <a title='Memory Map' href='microkit.system#L240-L244'>Memory Map</a></td></tr>
    </table>
- **GUMBO**

    <table>
    <tr><th colspan=3>State Variables</th></tr>
    <tr><td>lastMonitorMode</td>
    <td><a href=../../aadl/aadl/packages/Monitor.aadl#L320-L320>GUMBO</a></td>
    <td><a href=crates/thermostat_mt_mmm_mmm/src/component/thermostat_mt_mmm_mmm_app.rs#L18-L20>Verus</a></td></tr></table>
    <table>
    <tr><th colspan=4>Initialize</th></tr>
    <tr><td>guarantee REQ_MMM_1</td>
    <td><a href=../../aadl/aadl/packages/Monitor.aadl#L327-L329>GUMBO</a></td>
    <td><a href=crates/thermostat_mt_mmm_mmm/src/component/thermostat_mt_mmm_mmm_app.rs#L62-L64>Verus</a></td>
    <td><a href=crates/thermostat_mt_mmm_mmm/src/bridge/thermostat_mt_mmm_mmm_GUMBOX.rs#L29-L32>GUMBOX</a></td>
    </tr></table>
    <table>
    <tr><th colspan=4>Compute</th></tr>
    <tr><td>case REQ_MMM_2</td>
    <td><a href=../../aadl/aadl/packages/Monitor.aadl#L334-L342>GUMBO</a></td>
    <td><a href=crates/thermostat_mt_mmm_mmm/src/component/thermostat_mt_mmm_mmm_app.rs#L79-L81>Verus</a></td>
    <td><a href=crates/thermostat_mt_mmm_mmm/src/bridge/thermostat_mt_mmm_mmm_GUMBOX.rs#L70-L83>GUMBOX</a></td>
    </tr>
    <tr><td>case REQ_MMM_3</td>
    <td><a href=../../aadl/aadl/packages/Monitor.aadl#L344-L353>GUMBO</a></td>
    <td><a href=crates/thermostat_mt_mmm_mmm/src/component/thermostat_mt_mmm_mmm_app.rs#L89-L91>Verus</a></td>
    <td><a href=crates/thermostat_mt_mmm_mmm/src/bridge/thermostat_mt_mmm_mmm_GUMBOX.rs#L98-L111>GUMBOX</a></td>
    </tr>
    <tr><td>case REQ_MMM_4</td>
    <td><a href=../../aadl/aadl/packages/Monitor.aadl#L355-L362>GUMBO</a></td>
    <td><a href=crates/thermostat_mt_mmm_mmm/src/component/thermostat_mt_mmm_mmm_app.rs#L100-L102>Verus</a></td>
    <td><a href=crates/thermostat_mt_mmm_mmm/src/bridge/thermostat_mt_mmm_mmm_GUMBOX.rs#L122-L129>GUMBOX</a></td>
    </tr></table>
    <table>
    <tr><th colspan=4>GUMBO Methods</th></tr>
    <tr><td>timeout_condition_satisfied</td>
    <td><a href=../../aadl/aadl/packages/Monitor.aadl#L323-L323>GUMBO</a></td>
    <td><a href=crates/thermostat_mt_mmm_mmm/src/component/thermostat_mt_mmm_mmm_app.rs#L175-L178>Verus</a></td>
    <td><a href=crates/thermostat_mt_mmm_mmm/src/bridge/thermostat_mt_mmm_mmm_GUMBOX.rs#L17-L20>GUMBOX</a></td>
    </tr></table>


#### dmf: Monitor::Detect_Monitor_Failure.i

 - **Entry Points**


    Initialize: [Rust](crates/thermostat_mt_dmf_dmf/src/component/thermostat_mt_dmf_dmf_app.rs#L22-L28)

    TimeTriggered: [Rust](crates/thermostat_mt_dmf_dmf/src/component/thermostat_mt_dmf_dmf_app.rs#L30-L36)


- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Monitor.aadl#L556-L556'>internal_failure</a></td>
        <td>Out</td><td>Data</td>
        <td>Isolette_Data_Model::Failure_Flag.i</td><td><a title='Rust API' href='crates/thermostat_mt_dmf_dmf/src/bridge/thermostat_mt_dmf_dmf_api.rs#L32-L40'>Rust API</a> -> <a title='Memory Map' href='microkit.system#L262-L266'>Memory Map</a></td></tr>
    </table>


#### oit: Operator_Interface::Operator_Interface_Thread.i

 - **Entry Points**


    Initialize: [Rust](crates/operator_interface_oip_oit/src/component/operator_interface_oip_oit_app.rs#L25-L31)

    TimeTriggered: [Rust](crates/operator_interface_oip_oit/src/component/operator_interface_oip_oit_app.rs#L33-L39)


- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Operator_Interface.aadl#L97-L97'>regulator_status</a></td>
        <td>In</td><td>Data</td>
        <td>Isolette_Data_Model::Status</td><td><a title='Memory Map' href='microkit.system#L279-L283'>Memory Map</a> -> <a title='Rust API' href='crates/operator_interface_oip_oit/src/bridge/operator_interface_oip_oit_api.rs#L195-L208'>Rust API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Operator_Interface.aadl#L98-L98'>monitor_status</a></td>
        <td>In</td><td>Data</td>
        <td>Isolette_Data_Model::Status</td><td><a title='Memory Map' href='microkit.system#L284-L288'>Memory Map</a> -> <a title='Rust API' href='crates/operator_interface_oip_oit/src/bridge/operator_interface_oip_oit_api.rs#L209-L222'>Rust API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Operator_Interface.aadl#L99-L99'>display_temperature</a></td>
        <td>In</td><td>Data</td>
        <td>Isolette_Data_Model::Temp.i</td><td><a title='Memory Map' href='microkit.system#L274-L278'>Memory Map</a> -> <a title='Rust API' href='crates/operator_interface_oip_oit/src/bridge/operator_interface_oip_oit_api.rs#L181-L194'>Rust API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Operator_Interface.aadl#L100-L100'>alarm_control</a></td>
        <td>In</td><td>Data</td>
        <td>Isolette_Data_Model::On_Off</td><td><a title='Memory Map' href='microkit.system#L289-L293'>Memory Map</a> -> <a title='Rust API' href='crates/operator_interface_oip_oit/src/bridge/operator_interface_oip_oit_api.rs#L223-L236'>Rust API</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Operator_Interface.aadl#L103-L103'>lower_desired_tempWstatus</a></td>
        <td>Out</td><td>Data</td>
        <td>Isolette_Data_Model::TempWstatus.i</td><td><a title='Rust API' href='crates/operator_interface_oip_oit/src/bridge/operator_interface_oip_oit_api.rs#L102-L117'>Rust API</a> -> <a title='Memory Map' href='microkit.system#L294-L298'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Operator_Interface.aadl#L104-L104'>upper_desired_tempWstatus</a></td>
        <td>Out</td><td>Data</td>
        <td>Isolette_Data_Model::TempWstatus.i</td><td><a title='Rust API' href='crates/operator_interface_oip_oit/src/bridge/operator_interface_oip_oit_api.rs#L118-L133'>Rust API</a> -> <a title='Memory Map' href='microkit.system#L299-L303'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Operator_Interface.aadl#L105-L105'>lower_alarm_tempWstatus</a></td>
        <td>Out</td><td>Data</td>
        <td>Isolette_Data_Model::TempWstatus.i</td><td><a title='Rust API' href='crates/operator_interface_oip_oit/src/bridge/operator_interface_oip_oit_api.rs#L134-L155'>Rust API</a> -> <a title='Memory Map' href='microkit.system#L304-L308'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Operator_Interface.aadl#L106-L106'>upper_alarm_tempWstatus</a></td>
        <td>Out</td><td>Data</td>
        <td>Isolette_Data_Model::TempWstatus.i</td><td><a title='Rust API' href='crates/operator_interface_oip_oit/src/bridge/operator_interface_oip_oit_api.rs#L156-L177'>Rust API</a> -> <a title='Memory Map' href='microkit.system#L309-L313'>Memory Map</a></td></tr>
    </table>
- **GUMBO**

    <table>
    <tr><th colspan=4>Integration</th></tr>
    <tr><td>guarantee Table_A_12_LowerAlarmTemp</td>
    <td><a href=../../aadl/aadl/packages/Operator_Interface.aadl#L111-L113>GUMBO</a></td>
    <td><a href=crates/operator_interface_oip_oit/src/bridge/operator_interface_oip_oit_api.rs#L138-L139>Verus</a></td>
    <td><a href=crates/operator_interface_oip_oit/src/bridge/operator_interface_oip_oit_GUMBOX.rs#L23-L27>GUMBOX</a></td>
    </tr>
    <tr><td>guarantee Table_A_12_UpperAlarmTemp</td>
    <td><a href=../../aadl/aadl/packages/Operator_Interface.aadl#L115-L117>GUMBO</a></td>
    <td><a href=crates/operator_interface_oip_oit/src/bridge/operator_interface_oip_oit_api.rs#L160-L161>Verus</a></td>
    <td><a href=crates/operator_interface_oip_oit/src/bridge/operator_interface_oip_oit_GUMBOX.rs#L35-L39>GUMBOX</a></td>
    </tr></table>


#### thermostat: Devices::Temperature_Sensor.i

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Devices.aadl#L88-L88'>air</a></td>
        <td>In</td><td>Data</td>
        <td>Isolette_Data_Model::PhysicalTemp.i</td><td><a title='Memory Map' href='microkit.system#L326-L330'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Devices.aadl#L89-L89'>current_tempWstatus</a></td>
        <td>Out</td><td>Data</td>
        <td>Isolette_Data_Model::TempWstatus.i</td><td><a title='Memory Map' href='microkit.system#L321-L325'>Memory Map</a></td></tr>
    </table>


#### heat_controller: Devices::Heat_Source.i

 - **Entry Points**



- **APIs**

    <table>
    <tr><th>Port Name</th><th>Direction</th><th>Kind</th><th>Payload</th><th>Realizations</th></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Devices.aadl#L132-L132'>heat_control</a></td>
        <td>In</td><td>Data</td>
        <td>Isolette_Data_Model::On_Off</td><td><a title='Memory Map' href='microkit.system#L338-L342'>Memory Map</a></td></tr>
    <tr><td><a title='Model' href='../../aadl/aadl/packages/Devices.aadl#L133-L133'>heat_out</a></td>
        <td>Out</td><td>Data</td>
        <td>Isolette_Environment::Heat</td><td><a title='Memory Map' href='microkit.system#L343-L347'>Memory Map</a></td></tr>
    </table>

