# INSPECTA Micro-Examples — Microkit Target

This directory contains a set of focused micro-examples that illustrate how
HAMR translates AADL and SysML models into seL4 Microkit C/Rust code.  Each
micro-example is self-contained: it includes both an AADL model and a
corresponding SysML model, HAMR-generated source code for two scheduling
variants (seL4 domain scheduling and MCS user-land scheduling), and
documentation explaining the relevant modelling concepts and generated API.

The examples are organized into three categories:

| Category | Description |
|---|---|
| [aadl\_datatypes](microkit/aadl_datatypes/) | Reference for all AADL data types supported by HAMR for the Microkit target — base types, enums, structs, and fixed-size arrays |
| [aadl\_port\_types](microkit/aadl_port_types/) | One micro-example per AADL port family (data, event, event data), each shown with three payload varieties (base type, array, struct) |
| [port\_queues](microkit/port_queues/) | Micro-examples showing how to specify different event data port queue sizes in AADL and SysML, and how HAMR realizes those sizes in the generated C API |
