# Event Data Port Queue Sizes

These micro-examples show how **queue size** is specified on AADL event data
ports and how HAMR codegen realizes those sizes in the generated C API.  Each
example uses the same topology — one periodic producer and three sporadic
consumers with queue sizes 1, 2, and 5 — but with a different payload type.
Each micro-example contains both an AADL model and a SysML model, with
generated C code for two scheduling variants.

> **Note:** HAMR currently only supports queue sizes of 1 for Rust components
> that contain GUMBO contracts.  All components in these examples are
> implemented in C, which supports any queue size.

| Micro-example | Description |
|---|---|
| [event\_data\_array](event_data_array/) | Queue sizes on event data ports carrying an array-of-struct payload (`ArrayOfStruct`) |
| [event\_data\_base\_types](event_data_base_types/) | Queue sizes on event data ports carrying a `Base_Types::Integer_8` payload |
| [event\_data\_struct](event_data_struct/) | Queue sizes on event data ports carrying a user-defined struct payload (`struct.i`) |
