# Isolette — Infant Incubator Thermostat

The Isolette is a case study in high-assurance embedded system design, originally derived from the
[FAA Requirements Engineering Management Handbook (AR-08-32)](https://www.faa.gov/sites/faa.gov/files/aircraft/air_cert/design_approvals/air_software/AR-08-32.pdf#page=92).
The system models a neonatal intensive-care infant incubator that regulates and monitors air
temperature to keep premature infants within a safe thermal range.

The design follows the dual-channel safety architecture prescribed in the FAA document: an
independent **Regulate** subsystem controls the heat source to maintain the desired temperature,
while an independent **Monitor** subsystem activates the alarm if the temperature drifts outside
the operator-specified alarm range.  Both subsystems detect and report internal failures
independently.

GUMBO behavioral contracts are attached to the Monitor and Regulate threads, specifying integration constraints
and per-component assume/guarantee pairs that target Logika and Verus proof generation.

Two model representations are provided — an AADL model and a SysML v2 model converted from the
AADL model — and both models generate code for two targets: a JVM-based simulation target and a
[seL4 Microkit](https://github.com/seL4/microkit) embedded target.

 Table of Contents
  * [Models](#models)
    * [AADL Model](#aadl-model)
    * [SysML Model](#sysml-model)
  * [System Architecture](#system-architecture)
    * [Regulate Temperature Subsystem](#regulate-temperature-subsystem)
    * [Monitor Temperature Subsystem](#monitor-temperature-subsystem)
  * [GUMBO Contracts](#gumbo-contracts)

---

## Models

### Arch
![Arch](aadl/diagrams/arch.svg)

---

### AADL Model

The primary model is written in AADL and lives under [`aadl/`](aadl/).  The top-level system is
in [`aadl/aadl/packages/Isolette.aadl`](aadl/aadl/packages/Isolette.aadl), with the thermostat
structure split across
[`Thermostat.aadl`](aadl/aadl/packages/Thermostat.aadl),
[`Regulate.aadl`](aadl/aadl/packages/Regulate.aadl), and
[`Monitor.aadl`](aadl/aadl/packages/Monitor.aadl).
GUMBO behavioral contracts are written in `annex GUMBO {** ... **}` syntax and attached to the
Monitor and Regulate threads. 

HAMR codegen from the AADL model targeting the **JVM** produces Slang code in
[`hamr/slang/`](hamr/slang/).  Codegen targeting **Microkit** produces Rust crates in
[`hamr/microkit/`](hamr/microkit/).

### SysML Model

The SysML model in [`sysml/`](sysml/) was **derived/converted from the AADL model**.  The
structure (components, port types, and connections) is identical to the AADL model, but expressed
in SysML v2 syntax using the
[santoslab AADL SysML libraries](https://github.com/santoslab/sysml-aadl-libraries).
GUMBO contracts use the `language "GUMBO" /*{ ... }*/` annotation syntax.

HAMR codegen from the SysML model targeting the **JVM** produces Slang code in
[`hamr/slang/`](hamr/slang/).  Codegen targeting **Microkit** produces Rust crates in
[`hamr/microkit/`](hamr/microkit/).

For installation, codegen, and simulation instructions see:
- [aadl_readme.md](aadl_readme.md) — AADL model, OSATE/FMIDE codegen, seL4 domain scheduling
- [sysml_readme.md](sysml_readme.md) — SysML model, Sireum codegen, seL4 domain scheduling

---

## System Architecture

The Isolette system is composed of five top-level subsystems:

- **Temperature Sensor** (`temperature_sensor`) — reads the physical air temperature and reports
  the current temperature with a validity status flag to the thermostat.
- **Operator Interface** (`operator_interface`) — accepts the nurse's input (desired temperature
  range and alarm range) and displays system status (regulator status, monitor status, current
  temperature, alarm state).
- **Thermostat** (`thermostat`) — the main control system, composed of two independent subsystems:
  - **Regulate Temperature** (`rt`) — maintains air temperature within the operator-specified
    desired range by commanding the heat source.
  - **Monitor Temperature** (`mt`) — independently monitors air temperature against the
    operator-specified alarm range and activates the alarm when the temperature is out of range.
- **Heat Source** (`heat_source`) — the heater actuator, turned on or off by the thermostat's
  Regulate subsystem.

### Regulate Temperature Subsystem

The Regulate subsystem (`Regulate_Temperature.i`) contains four threads:

| Thread | Short name | Function |
|---|---|---|
| Manage Regulator Interface | MRI | Validates the desired temperature range from the operator interface; provides the display temperature |
| Manage Heat Source | MHS | Computes the heat control command (On/Off) based on the current temperature vs. the desired range |
| Manage Regulator Mode | MRM | Tracks the regulator operating mode (Init → Normal or Failed) based on temperature status and failure flags |
| Detect Regulator Failure | DRF | Detects internal regulator failures and sets a failure flag |

### Monitor Temperature Subsystem

The Monitor subsystem (`Monitor_Temperature.i`) contains four threads:

| Thread | Short name | Function |
|---|---|---|
| Manage Monitor Interface | MMI | Validates the alarm temperature range from the operator interface |
| Manage Alarm | MA | Computes the alarm control command (On/Off) based on the current temperature, alarm range, and monitor mode |
| Manage Monitor Mode | MMM | Tracks the monitor operating mode (Init → Normal or Failed) based on temperature status and failure flags |
| Detect Monitor Failure | DMF | Detects internal monitor failures and sets a failure flag |

---

## GUMBO Contracts

GUMBO (Grand Unified Modeling of Behavioral Operators) contracts are attached to the
Monitor and Regulate threads.  Each contract follows an assume/guarantee pattern tied to the FAA requirements:

- **Integration constraints** (`integration`) — specify valid ranges for port values.  For
  example, `Manage_Monitor_Interface` requires that the lower and upper alarm temperatures each
  carry a valid `TempWstatus` status and that the lower bound is strictly less than the upper
  bound.

- **Behavioral contracts** (`compute`) — enumerate cases per FAA requirement, specifying what the
  thread guarantees given its assumptions.  For example, `Manage_Alarm` specifies under
  `REQ_MA_1`–`REQ_MA_5` exactly when the alarm control output is `On` or `Off`.

The AADL GUMBO contracts use the `annex GUMBO {** ... **}` syntax in the thread type declaration.
The SysML GUMBO contracts use the `language "GUMBO" /*{ ... }*/` annotation syntax attached to
the corresponding `part def`. 
