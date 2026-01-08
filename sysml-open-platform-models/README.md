# Port of open-platform-model to SysMLv2
The projects are currently structured to be imported into an instance of the [SysMLv2-Pilot-Implementation][https://github.com/Systems-Modeling/SysML-v2-Pilot-Implementation/tree/master] in Eclipse.
They could likely be used in any SysMLv2 implementation that includes access to the SysMLv2 model library, but this is unverified.
The open-platform/SW.sysml model is based on the model found in `INSPECTA-models/open-platform-models/open-platform/aadl/SW.aadl` on the `main` branch.
The `SW.sysml` model has GUMBO contracts written using language comments. 
`AADLLib`, `HAMRLib`, `Resolute`, and `GUMBOLib` are user provided SysMLv2 libraries. `SW.sysml` only depends on `AADLLib` and `HAMRLib`.
