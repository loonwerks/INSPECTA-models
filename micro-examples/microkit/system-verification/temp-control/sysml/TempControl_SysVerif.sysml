package TempControl_SysVerif {

  private import HAMR::*;
  
  part def TempControlSystem :> System {
    part tsp: TempSensorProcess;
    part tcp: TempControlProcess;
    part fp: FanProcess;

    part proc: TempControlProcessor;

    connection ctTStoTC : PortConnection connect tsp.currentTemp to tcp.currentTemp;
    connection fcTCtoF : PortConnection connect tcp.fanCmd to fp.fanCmd;
    connection faFtoTC : PortConnection connect fp.fanAck to tcp.fanAck;

    allocation b0 : Deployment_Properties::Actual_Processor_Binding allocate tsp to proc;
    allocation b1 : Deployment_Properties::Actual_Processor_Binding allocate tcp to proc;
    allocation b2 : Deployment_Properties::Actual_Processor_Binding allocate fp to proc;

    language "GUMBO" /*{
      composition nominal {

        components
          sensor  = tsp.tst;
          control = tcp.tct;
          fan     = fp.ft;

        ports
          sensedTemp = sensor.currentTemp; // outgoing event-data port

        state
          sv_fanError        = control.fanError;
          sv_currentSetPoint = control.currentSetPoint;
          sv_currentFanState = control.currentFanState;
          sv_latestTemp      = control.latestTemp;

        schema {
          sensor;
          control;
          fan
        }

        // Loop invariant carrying TempControl's compute assumes (a1/a2) over the frame.
        // a1/a2 relate only TempControl's persisted state vars and are the same predicates
        // as the TC_Req_01/02 compute guarantees, so they hold at init (72 in [70,90] ->
        // antecedents false), are framed across sensor/fan, re-established by control, and
        // close END |- START. Bound in an abstract base so every property covering control
        // inherits it and can discharge control's Pre-Assert.
        abstract property Control_Invariant_Base
          "loop invariant: TempControl's a1/a2 over the carried control state" {

          at START "loop entry":
            '->:'(not sv_fanError & sv_latestTemp.degrees < sv_currentSetPoint.low.degrees, sv_currentFanState == FanCmd.Off) &
            '->:'(not sv_fanError & sv_latestTemp.degrees > sv_currentSetPoint.high.degrees, sv_currentFanState == FanCmd.On);

          after sensor "carried across sensor (frame)":
            '->:'(not sv_fanError & sv_latestTemp.degrees < sv_currentSetPoint.low.degrees, sv_currentFanState == FanCmd.Off) &
            '->:'(not sv_fanError & sv_latestTemp.degrees > sv_currentSetPoint.high.degrees, sv_currentFanState == FanCmd.On);

          after control "re-established by control's TC_Req guarantees":
            '->:'(not sv_fanError & sv_latestTemp.degrees < sv_currentSetPoint.low.degrees, sv_currentFanState == FanCmd.Off) &
            '->:'(not sv_fanError & sv_latestTemp.degrees > sv_currentSetPoint.high.degrees, sv_currentFanState == FanCmd.On);

          after fan "carried across fan (frame)":
            '->:'(not sv_fanError & sv_latestTemp.degrees < sv_currentSetPoint.low.degrees, sv_currentFanState == FanCmd.Off) &
            '->:'(not sv_fanError & sv_latestTemp.degrees > sv_currentSetPoint.high.degrees, sv_currentFanState == FanCmd.On);

          at END "loop closes: END |- START":
            '->:'(not sv_fanError & sv_latestTemp.degrees < sv_currentSetPoint.low.degrees, sv_currentFanState == FanCmd.Off) &
            '->:'(not sv_fanError & sv_latestTemp.degrees > sv_currentSetPoint.high.degrees, sv_currentFanState == FanCmd.On);
        }

        property TC_Req_01 :> Control_Invariant_Base
          "System-level TC_Req_01: if the temperature produced by the temp sensor is below the
          |set point's low, the fan state is Off (stated over the sensor's outgoing currentTemp
          |port rather than TempControl's latestTemp state var)" {

          after control "post-control: the fan state reflects the just-sensed temperature":
            '->:'(HasEvent(sensedTemp) and (not sv_fanError & sensedTemp.degrees < sv_currentSetPoint.low.degrees),
                  sv_currentFanState == FanCmd.Off);
        }

        property TC_Req_02 :> Control_Invariant_Base
          "System-level TC_Req_02: if the temperature produced by the temp sensor is above the
          |set point's high, the fan state is On (stated over the sensor's outgoing currentTemp
          |port rather than TempControl's latestTemp state var)" {

          after control "post-control: the fan state reflects the just-sensed temperature":
            '->:'(HasEvent(sensedTemp) and (not sv_fanError & sensedTemp.degrees > sv_currentSetPoint.high.degrees),
                  sv_currentFanState == FanCmd.On);
        }

        // TC_Req_03 (fan state maintained when temp in range) is a pre/post equality that
        // needs In(), which is not yet supported for system properties. Restating it as an
        // End->Start loop property would require a latch state var (prior fan state) in
        // TempControl -- deferred for now.
      }
    }*/
  }

  part def TempControlProcessor :> Processor {
    attribute :>> Frame_Period = 1000 [ms];
    attribute :>> Clock_Period = 1 [ms];
  }

  part def TempControlProcess :> Process {
    part tct : TempControl;

    port currentTemp : EventDataPort { in :>> type : Temperature; }
    port fanAck : EventDataPort { in :>> type : FanAck; }
    port fanCmd : EventDataPort { out :>> type : FanCmd; }

    connection ctTCPtoTC : PortConnection connect currentTemp to tct.currentTemp;
    connection faTCPtoTC : PortConnection connect fanAck to tct.fanAck;
    connection fcTCtoTCP : PortConnection connect tct.fanCmd to fanCmd;

    // domains follow the composition schema order (sensor; control; fan)
    attribute :>> Domain = 3;
  }

  part def TempSensorProcess :> Process {
    part tst : TempSensor;

    port currentTemp : EventDataPort { out :>> type : Temperature; }

    connection ctTStoTSP : PortConnection connect tst.currentTemp to currentTemp;

    attribute :>> Domain = 2;
  }

  part def FanProcess :> Process {
    part ft : Fan;

    port fanCmd : EventDataPort { in :>> type : FanCmd; }
    port fanAck : EventDataPort { out :>> type : FanAck; }

    connection fcFtoFP : PortConnection connect fanCmd to ft.fanCmd;
    connection faFPtoF : PortConnection connect ft.fanAck to fanAck;

    attribute :>> Domain = 4;
  }

  part def PeriodicThread :> Thread {
    attribute :>> Dispatch_Protocol = Supported_Dispatch_Protocols::Periodic;
    attribute :>> Period = 1000 [ms];
  }

  part def PeriodicRustThread :> PeriodicThread {
    attribute :>> Implementation_Language = Rust;
  }

  part def TempSensor :> PeriodicThread {
    port currentTemp : EventDataPort { out :>> type : Temperature; }

    language "GUMBO" /*{

      integration
        guarantee Sensor_Temperature_Range:
          currentTemp.degrees >= -40 [i32] & currentTemp.degrees <= 122 [i32];

      initialize
        guarantee initializes:
          currentTemp.degrees == 72 [i32];
    }*/
  }

  part def TempControl :> PeriodicRustThread {
    port currentTemp : EventDataPort { in :>> type : Temperature; }
    port fanAck : EventDataPort { in :>> type : FanAck; }
    port setPoint : EventDataPort { in :>> type : SetPoint; }
    port fanCmd : EventDataPort { out :>> type : FanCmd; }
    
    language "GUMBO" /*{
      state
        currentSetPoint: SetPoint;
        currentFanState: FanCmd;
        latestTemp: Temperature;
        fanError: Base_Types::Boolean;

      functions
        def validRange(temp: Base_Types::Integer_32): Base_Types::Boolean :=
          temp >= -129 [i32] & temp <= 134[i32];

      integration
        assume currentTempRange:
          validRange(if true ? currentTemp.degrees + 0 [i32] else currentTemp.degrees);

      initialize
        modifies currentSetPoint, currentFanState, latestTemp;

        guarantee defaultSetPoint:
          (currentSetPoint.low.degrees == 70 [i32]) & (currentSetPoint.high.degrees == 90 [i32]);

        guarantee defaultFanStates:
          currentFanState == FanCmd.Off;

        guarantee defaultLatestTemp:
          latestTemp.degrees == 72 [i32];

      compute
        modifies currentSetPoint, currentFanState, latestTemp, fanError;

        assume
          a1 "If the previously received currentTemp was less than the previously
             |received setPoint then the last fan command must have been Off":
            '->:'(not fanError & latestTemp.degrees < currentSetPoint.low.degrees,
                  currentFanState == FanCmd.Off);

        assume
          a2 "If the previously received currentTemp was more than the previously
             |received setPoint then the last fan command must have been On":
            '->:'(not fanError & latestTemp.degrees > currentSetPoint.high.degrees,
                  currentFanState == FanCmd.On);
        
        guarantee errorState "If the fan sent FanAck.Error then continue sending the
                              |last fan command until the fan sends FanAck.Ok":
          '->:'(fanError,
                In(currentFanState) == currentFanState & MustSend(fanCmd, currentFanState));

        //    ---------------------------
        //     Compute Entry Point General Clauses
        //       The following contracts state properties reflecting control laws that should always
        //       hold no matter what incoming event triggers dispatch.   Therefore, they are stated
        //       as independent (not associated with a handler) guarantee clauses.
        //    ---------------------------
        guarantee  TC_Req_01 "If the current temperature is less than the set point, then the fan state shall be Off.":
          '->:'(not fanError & latestTemp.degrees < currentSetPoint.low.degrees,
                currentFanState == FanCmd.Off);

        guarantee TC_Req_02 "If the current temperature is greater than the set point,
                             |then the fan state shall be On.":
          '->:' (not fanError & latestTemp.degrees > currentSetPoint.high.degrees,
                 currentFanState == FanCmd.On);

        guarantee TC_Req_03 "If the current temperature is greater than or equal to the
                            |current low set point and less than or equal to the current high set point,
                            |then the current fan state is maintained.":
          '->:'(not fanError & latestTemp.degrees >= currentSetPoint.low.degrees & latestTemp.degrees <= currentSetPoint.high.degrees,
                // if we are still in set point range, don't change the fan state
                currentFanState == In(currentFanState));

        guarantee mustSendFanCmd "If the local record of the fan state was updated,
                                 |then send a fan command event with this updated value.":
          '->:'(not fanError & In(currentFanState) != currentFanState,
                 MustSend(fanCmd, currentFanState)) &

          '->:' (not fanError & currentFanState == In(currentFanState),
                NoSend(fanCmd));

        //    ---------------------------
        //     Formerly Handler Contracts (now periodic)
        //       With periodic dispatch there is a single compute entrypoint and zero or
        //       more incoming events may be present per period, so each former handler's
        //       clauses are guarded by HasEvent(<port>) implies (...).  The per-handler
        //       frame conditions become event-guarded frame conditions.
        //    ---------------------------

        guarantee setPointChanged:
          HasEvent(setPoint) implies (currentSetPoint == setPoint);

        guarantee fanAck_setPointNotModified:
          '->:'(HasEvent(fanAck) & not HasEvent(setPoint), currentSetPoint == In(currentSetPoint));

        guarantee currentTemp_setPointNotModified:
          '->:'(HasEvent(currentTemp) & not HasEvent(setPoint), currentSetPoint == In(currentSetPoint));


        guarantee tempChanged:
          HasEvent(currentTemp) implies (latestTemp == currentTemp);

        guarantee setPoint_latestTempNotModified:
          '->:'(HasEvent(setPoint) & not HasEvent(currentTemp), latestTemp == In(latestTemp));

        guarantee fanAck_latestTempNotModified:
          '->:'(HasEvent(fanAck) & not HasEvent(currentTemp), latestTemp == In(latestTemp));


        guarantee manageErrorState:
          HasEvent(fanAck) implies (
            '->:'(fanAck == FanAck.Ok, not fanError) &
            '->:'(fanAck == FanAck.Error, fanError));
    }*/
  }

  part def Fan :> PeriodicRustThread {
    port fanCmd : EventDataPort { in :>> type : FanCmd; }
    port fanAck : EventDataPort { out :>> type : FanAck; }
  }

  part def Temperature :> Data {

    part degrees : Base_Types::Integer_32;
    attribute unit : TempUnit;

    language "GUMBO" /*{
      invariants
        inv AbsZero:
          degrees > -460 [i32];
    }*/
  }
      
  enum def TempUnit {
    enum Fahrenheit;
    //enum Celsius;
    //enum Kelvin;
  }

  part def SetPoint :> Data {
    part low : Temperature;
    part high : Temperature;

    language "GUMBO" /*{
      invariants
        inv SetPoint_Data_Invariant:
          (low.degrees >= 50 [i32]) &
          (high.degrees <= 110 [i32]) &
          (low.degrees <= high.degrees);
    }*/
  }

  enum def FanCmd {
    enum On;
    enum Off;
  }
    
  enum def FanAck {
    Ok;
    Error;
  }
}