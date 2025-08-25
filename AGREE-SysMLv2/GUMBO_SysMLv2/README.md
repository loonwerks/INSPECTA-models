# GumboTransformers Copilot

This repository provides a tool to translate embedded **GUMBO** contracts into **SysML v2** models and validate them using the ANTLR4 TestRig parser and SysIDE Linker.

[![Watch the video](https://img.youtube.com/vi/_c0D2oJS-ME/0.jpg)](https://youtu.be/_c0D2oJS-ME)

---
> **Note:** 
Before opening the project in SysIDE (VS Code/VSCodium), complete the following steps:

- Clone this repo and change into sysml. 
- Clone the SysML v2 AADL libraries into the sysml/ directory of this repo:

```
cd sysml
git clone https://github.com/santoslab/sysml-aadl-libraries.git
```
 
## Requirements

| Requirement                 | Version    | Notes                                   |
|-----------------------------|------------|-----------------------------------------|
| Python                      | `3.10+`    |                                         |
| Java                        | `17`       | For running the ANTLR4 TestRig          |
| Lark                        | `>=0.7.8`  | Python parsing library                  |
| antlr4-python3-runtime      | `>=4.13.2` | Python runtime for ANTLR4               |
| ANTLR4 runtime JAR          | `4.13.2`   | Download [here](https://www.antlr.org/download.html) |

Install Python dependencies via `requirements.txt`:

```bash
pip install -r requirements.txt
```

> **Note:** After installing the dependency you can run the demo translator.  The tool
replaces any `language "GUMBO"` blocks in the given SysMLv2 file and writes a new
`*.translated.sysml` file.  By default the generated model is to be validated with the
bundled customized parser (such as validating the result using ANTLR4’s TestRig).

## Usage

Translate embedded GUMBO contracts in a SysML file and validate with ANTLR4 (default):
```bash
python3 GumboTransformers.py [options] <input-file>.sysml
```
## Options

 Flag           . | Description                          .               |
|-----------------|------------------------------------------------------|
| `--show`        | Print the translated SysML v2 to stdout              |
| `--no-validate` | Skip all validation steps (not recommended)          |
| `--no-antlr`    | Skip ANTLR4 TestRig validation (not recommended)     |


### Examples

> - 1) Translate + validate & print output
python3 GumboTransformers.py --show Monitor.sysml

> - 2) Translate only, skip validation entirely
python3 GumboTransformers.py --no-validate Monitor.sysml


