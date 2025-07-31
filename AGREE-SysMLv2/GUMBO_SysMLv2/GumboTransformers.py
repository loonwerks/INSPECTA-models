#!/usr/bin/env python3
# ========================================================================================================
# @author: Amer N Tahat,
# Collins Aerospace,
# Enhanced Gumbo-to-SysML v2 transformer:
#
# feat: support initialize sections, compute cases, and precise SysML v2 grammar conformance
# - Emit helper functions as `calc def`
# - Use `and`/`or`/`not` operators exactly as in SysMLv2.g4
# - Preserve `==` for equality
# - Emit one `assume constraint` per original assume clause
# - Prefix compute‐case IDs with `ComputeCase_`
# - Prefix package name with the enclosing part name
# ========================================================================================================

import os
import re
import argparse
import subprocess

from gumbo_parser import parser, GumboTransformer
from sysml_utils import extract_gumbo_blocks, find_enclosing_part_name, replace_blocks

def translate_monitor_gumbo(transformer: GumboTransformer, part_name: str) -> str:
    """
    Build a SysML v2 package named <part_name>Contracts containing:
      – attributes for each state var
      – calc def for helper functions
      – one InitializeContract
      – one MonitorIntegration with two separate assume constraint {…} lines
      – each compute case named `ComputeCase_<ID>`
    """
    lines = []
    # prefix package with the part name
    lines.append(f"package {part_name}Contracts {{")
    lines.append("  import Isolette_Data_Model::*")
    lines.append("  import Base_Types::*")
    lines.append("")

    # state variables
    if transformer.state_vars:
        for var, typ in transformer.state_vars:
            lines.append(f"  attribute {var}: {typ}")
        lines.append("")

    # helper functions → calc def
    if transformer.helper_funcs:
        for fname, rettype, body in transformer.helper_funcs:
            lines.append(f"  calc def {fname} {{")
            lines.append(f"    return {body}")
            lines.append("  }")
            lines.append("")

    # InitializeContract
    if transformer.initialize_guarantees:
        lines.append("  requirement def InitializeContract {")
        for _k, _n, _d, expr in transformer.initialize_guarantees:
            expr = expr.replace("->:", "->")
            lines.append(f"    require constraint {{ {expr} }}")
        lines.append("  }")
        lines.append("")

    # MonitorIntegration: emit two separate assume constraint lines
    if transformer.integration_assumes or transformer.guarantees:
        lines.append("  requirement def MonitorIntegration {")
        # two assume clauses
        for _k, _n, _d, expr in transformer.integration_assumes:
            expr = expr.replace("->:", "->")
            lines.append(f"    assume constraint {{ {expr} }}")
        # default require true if no guarantees
        if transformer.guarantees:
            for _k, _n, _d, expr in transformer.guarantees:
                expr = expr.replace("->:", "->")
                lines.append(f"    require constraint {{ {expr} }}")
        else:
            lines.append("    require constraint { true }")
        lines.append("  }")
        lines.append("")

    # compute cases, prefix IDs with ComputeCase_
    for case in transformer.compute_cases:
        cid = case["id"]
        desc = case["description"]
        lines.append(f"  // compute case {cid}: {desc}" if desc else f"  // compute case {cid}")
        lines.append(f"  requirement def ComputeCase_{cid} {{")
        if desc:
            # use block‐comment for doc
            lines.append(f"    doc /* {desc} */")
        # assume clauses
        for _k, _n, _d, expr in case["assumes"]:
            expr = expr.replace("->:", "->")
            lines.append(f"    assume constraint {{ {expr} }}")
        # require clauses
        for _k, _n, _d, expr in case["guarantees"]:
            expr = expr.replace("->:", "->")
            lines.append(f"    require constraint {{ {expr} }}")
        lines.append("  }")
        lines.append("")

    lines.append("}")
    return "\n".join(lines)


def validate_sysml_antlr(sysml_path: str) -> None:
    """
    Validate SysML v2 by invoking ANTLR4's TestRig. It auto‐detects
    ~/hamr-sysml-parser or falls back to ./out and ./antlr-4.13.2-complete.jar.
    """
    home = os.path.expanduser("~")
    candidate = os.path.join(home, "hamr-sysml-parser")
    if os.path.isdir(candidate):
        parser_out = os.path.join(candidate, "out")
        antlr_jar  = os.path.join(candidate, "antlr-4.13.2-complete.jar")
    else:
        here       = os.path.dirname(__file__)
        parser_out = os.path.join(here, "out")
        antlr_jar  = os.path.join(here, "antlr-4.13.2-complete.jar")

    classpath = f"{parser_out}:{antlr_jar}"
    cmd = [
        "java", "-cp", classpath,
        "org.antlr.v4.gui.TestRig",
        "SysMLv2", "ruleRootNamespace",
        sysml_path,
    ]
    try:
        proc = subprocess.run(cmd, capture_output=True, text=True, timeout=60)
    except FileNotFoundError:
        print("Java or ANTLR TestRig not found.")
        return
    except subprocess.TimeoutExpired:
        print("ANTLR validation timed out.")
        return

    if proc.returncode == 0:
        print(f"\n=== ANTLR4 TestRig Parse Succeeded ===")
    else:
        print(f"\n=== ANTLR4 TestRig Parse Failed ===")
        print(proc.stderr.strip() or proc.stdout.strip())


def process_sysml_file(
    path: str,
    *,
    validate: bool = True,
    debug: bool = False,
    antlr: bool = True,
    sireum_validate: bool = False,
    sireum_cmd: str | None = None,
    sireum_grammar: str | None = None,
) -> None:
    """Translate GUMBO blocks in `path` and optionally validate."""
    text = open(path).read()
    blocks = extract_gumbo_blocks(text)
    if not blocks:
        print("No GUMBO contracts found")
        return

    replacements, contracts = [], []
    for start, end, gumbo in blocks:
        clean = re.sub(r"\[[^\]]+\]", "", gumbo).strip()
        tree  = parser.parse(clean)
        tf    = GumboTransformer()
        tf.transform(tree)
        part  = find_enclosing_part_name(text, start)
        sysml = translate_monitor_gumbo(tf, part)
        contracts.append(sysml)
        replacements.append((start, end, ""))

    new_text = replace_blocks(text, replacements)
    if contracts:
        new_text += "\n\n" + "\n\n".join(contracts) + "\n"

    out_path = path.rsplit(".", 1)[0] + ".translated.sysml"
    with open(out_path, "w") as fh:
        fh.write(new_text)
    print(f"Translated file written to {out_path}")

    if debug or not validate:
        print("\n=== Translated SysML v2 (preview) ===\n")
        print(new_text)

    if validate and antlr:
        validate_sysml_antlr(out_path)


def main() -> None:
    """CLI entry point."""
    cli = argparse.ArgumentParser(description="Translate Gumbo to SysML v2")
    cli.add_argument("input", help="SysML file containing Gumbo blocks")
    cli.add_argument("--no-validate", action="store_true", help="Skip validation")
    cli.add_argument("--show",      action="store_true", help="Print translated SysML")
    cli.add_argument("--no-antlr",  action="store_true", help="Skip ANTLR validation")
    cli.add_argument("--sireum-validate", action="store_true",
                     help="Also run Sireum IVE parser")
    cli.add_argument("--sireum-cmd", metavar="PATH",
                     help="Path to sireum executable")
    cli.add_argument("--sireum-grammar", metavar="FILE",
                     help="SysML v2 grammar for Sireum")
    args = cli.parse_args()

    process_sysml_file(
        args.input,
        validate=not args.no_validate,
        debug=args.show,
        antlr=not args.no_antlr,
        sireum_validate=args.sireum_validate,
        sireum_cmd=args.sireum_cmd,
        sireum_grammar=args.sireum_grammar,
    )


if __name__ == "__main__":
    main()
