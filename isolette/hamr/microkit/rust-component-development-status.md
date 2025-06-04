# Status of Development of HAMR Rust Isolette Implementation

Next steps:
- clean up manual tests on MRM and MMM.  Replace helper macros with
  helper functions
- implement complete tests for MRI
- clean up manual tests for MHS  
- Implement app code and tests for MMI and MA
- Implement prop tests for all



## Regulate Subsystem

### Manage Heat Source 

- *Component Implementation* (done)
- *Manual Testing* (done - 1 test for each requirement - coverage not checked)
- *PropTest Testing* - in separate folder; needs to be brought over to
  this folder

### Manage Regulator Interface

- *Component Implementation* (done)
- *Manual Testing* (a few tests -- need to complete all tests with
  helper functions)

### Manage Regulator Mode

- *Component Implementation* (done)
- *Manual Testing* (done - comprehensive, as supported by Grok.  Need
  to convert helper macros to helper functions)

## Monitor Subsystem

## Manage Alarm

- *Component Implementation* -- TBD
- *Manual Testing* -- TBD

## Manage Monitor Interface

- *Component Implementation* -- Done and Verus verified
- *Manual Testing* -- TBD

## Manage Monitor Mode

- *Component Implementation* (done)
- *Manual Testing* (done - tests adapted from Grok tests in MRM.  Need
  to double check.  As with MRM, need to convert helper macros to
  helper functions)


## Desired shortcuts / coding helps

- hints to list component input, output ports, along with internal
  declared state; dictation command to read all ports or read a
  particular port 

## Isolette Document / Code Errors

- current_temp input port is not used in MMI (not referenced by
  component requirements), even though it is depicted as input in
  Figure A-5.  It might be the case that its status value is supposed
  to be checked, but that is missing from the component requirements
  (and implementation).
- Slang code has comments for MMI e.g., 
  "Set values for Alarm Range internal variable" that inappropriately
  focus on the "internal variable" rather than the output.





