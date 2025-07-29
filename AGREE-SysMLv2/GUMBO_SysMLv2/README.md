# GumboTransformers_Copilot

This repository contains an example Gumbo to SysML transformer.  It relies on
the `lark` parsing library.  If you wish to execute the script, install the
dependency first.  A simple `requirements.txt` is provided for convenience or
you can install the package directly:

```bash
pip install -r requirements.txt  # or `pip install lark`
```

After installing the dependency you can run the demo translator.  The tool
replaces any `language "GUMBO"` blocks in the given SysML file and writes a new
`*.translated.sysml` file.  By default the generated model is to be validated with the
bundled customized parser.

```bash
python3 GumboTransformers.py Monitor.sysml
```

The script will parse a small Gumbo contract, translate it to SysML v2 and then
invoke the bundled `MCSysMLv2.jar` to validate that the generated model is
syntactically correct.  Pass `--show` to print the translated text or
`--no-validate` to skip parser validation.
