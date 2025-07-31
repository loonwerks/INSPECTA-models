# GumboTransformers_Copilot
This repository contains an example Gumbo to SysML transformer.  It relies on
the `lark` parsing library.  If you wish to execute the script, install the
dependency first.  A simple `requirements.txt` is provided for convenience or
you can install the package directly:

```bash
pip install -r requirements.txt  # or `pip install lark`
```

After installing the dependency you can run the demo translator.  The tool
replaces any `language "GUMBO"` blocks in the given SysMLv2 file and writes a new
`*.translated.sysml` file.  By default the generated model is to be validated with the
bundled customized parser (such as validating the result using ANTLR4’s TestRig).

#Usage

Translate embedded GUMBO contracts in a SysML file and validate with ANTLR4 (default):
```bash
python3 GumboTransformers.py [options] <input-file>.sysml
```
#Options
Flag	Description
```bash
--show	Print the translated SysML v2 to stdout
--no-validate	Skip all validation steps (not recommended)
--no-antlr	Skip ANTLR4 TestRig validation (not recommended)
```
## Default behavior:

Translate GUMBO → SysML v2

Invoke ANTLR4 TestRig (ruleRootNamespace) to parse/validate the output

#Examples

# 1) Translate + validate & print output
python3 GumboTransformers.py --show Monitor.sysml

# 2) Translate only, skip validation entirely
python3 GumboTransformers.py --no-validate Monitor.sysml

# 3) Translate + print, but skip ANTLR4 parse step
python3 GumboTransformers.py --show --no-antlr Monitor.sysml

