#!/usr/bin/env python3
# ========================================================================================================
# @author: Amer N Tahat
# Collins Aerospace
# Enhanced Gumbo-to-SysML v2 transformer
#
# Features:
# - Support for `state`, `functions`, `integration`, `initialize`, and `compute` sections
# - Top-level compute contracts emitted as `ComputeBase`
# - Individual compute cases emitted as `ComputeCase_<ID>` refining `ComputeBase`
# - Initialization and integration requirements as `InitializeContract` and `MonitorIntegration`
# - Helper functions emitted as `calc def`
# - Full support for basic arithmetic expressions (`+`, `-`, `*`, `/`)
# - Support for function-call syntax in expressions (e.g. `In(lastCmd)`)
# - Use of `and`/`or`/`not` operators verbatim as in SysMLv2.g4
# - Preservation of `==` for equality tests
# - One `assume constraint` or `require constraint` per original clause
# - Prefixes: compute-case IDs (`ComputeCase_`), package names (enclosing part name)
# - Preservation of original GUMBO block indentation
# ========================================================================================================
import os
import re
import argparse
import subprocess

from gumbo_parser import parser, GumboTransformer
from sysml_utils import extract_gumbo_blocks, find_enclosing_part_name, replace_blocks


def _get_block_indent(text: str, start: int) -> str:
    line_start = text.rfind('\n', 0, start) + 1
    m = re.match(r'[ \t]*', text[line_start:start])
    return m.group(0) if m else ''

def translate_monitor_gumbo(transformer: GumboTransformer, part_name: str) -> str:
    """
    Build a SysMLv2 package named <part_name>Contracts.

    The function now
      • emits `refines ComputeBase` only when ComputeBase exists, and
      • keeps the original ‘name’ of every assume / guarantee clause
        by adding   doc /*<name>*/   ahead of the constraint.
    """
    lines: list[str] = []

    # ────────────────────────────────────────────────────────────────────
    # package header + imports
    # ────────────────────────────────────────────────────────────────────
    lines.append(f"package {part_name}Contracts {{")
    lines.append("  import Isolette_Data_Model::*")
    lines.append("  import Base_Types::*")
    lines.append("")

    # ────────────────────────────────────────────────────────────────────
    # state variables
    # ────────────────────────────────────────────────────────────────────
    for var, typ in transformer.state_vars:
        lines.append(f"  attribute {var}: {typ}")
    if transformer.state_vars:
        lines.append("")

    # ────────────────────────────────────────────────────────────────────
    # helper functions  →  calc def
    # ────────────────────────────────────────────────────────────────────
    for fname, _rettype, body in transformer.helper_funcs:
        lines.append(f"  calc def {fname} {{")
        lines.append(f"    return {body}")
        lines.append("  }")
        lines.append("")

    # ────────────────────────────────────────────────────────────────────
    # InitializeContract
    # ────────────────────────────────────────────────────────────────────
    if transformer.initialize_guarantees:
        lines.append("  requirement def InitializeContract {")
        for _k, name, desc, expr in transformer.initialize_guarantees:
            expr = expr.replace("->:", "->")
            if name:
                lines.append(f"    doc /*{name}*/")
            if desc:
                lines.append(f"    doc /*{desc}*/")
            lines.append(f"    require constraint {{ {expr} }}")
        lines.append("  }")
        lines.append("")

    # ────────────────────────────────────────────────────────────────────
    # MonitorIntegration
    # ────────────────────────────────────────────────────────────────────
    if transformer.integration_assumes or transformer.guarantees:
        lines.append("  requirement def MonitorIntegration {")
        for _k, name, desc, expr in transformer.integration_assumes:
            expr = expr.replace("->:", "->")
            if name:
                lines.append(f"    doc /*{name}*/")
            if desc:
                lines.append(f"    doc /*{desc}*/")
            lines.append(f"    assume constraint {{ {expr} }}")

        if transformer.guarantees:
            for _k, name, desc, expr in transformer.guarantees:
                expr = expr.replace("->:", "->")
                if name:
                    lines.append(f"    doc /*{name}*/")
                if desc:
                    lines.append(f"    doc /*{desc}*/")
                lines.append(f"    require constraint {{ {expr} }}")
        else:
            lines.append("    require constraint { true }")
        lines.append("  }")
        lines.append("")

    # ────────────────────────────────────────────────────────────────────
    # ComputeBase–top‑level compute contracts
    # ────────────────────────────────────────────────────────────────────
    compute_base_exists = bool(
        transformer.compute_toplevel_assumes or transformer.compute_toplevel_guarantees
    )

    if compute_base_exists:
        lines.append("  requirement def ComputeBase {")
        for _k, name, desc, expr in transformer.compute_toplevel_assumes:
            expr = expr.replace("->:", "->")
            if name:
                lines.append(f"    doc /*{name}*/")
            if desc:
                lines.append(f"    doc /*{desc}*/")
            lines.append(f"    assume constraint {{ {expr} }}")
        for _k, name, desc, expr in transformer.compute_toplevel_guarantees:
            expr = expr.replace("->:", "->")
            if name:
                lines.append(f"    doc /*{name}*/")
            if desc:
                lines.append(f"    doc /*{desc}*/")
            lines.append(f"    require constraint {{ {expr} }}")
        lines.append("  }")
        lines.append("")

    # ────────────────────────────────────────────────────────────────────
    # Individual compute cases
    # ────────────────────────────────────────────────────────────────────
    for case in transformer.compute_cases:
        cid   = case["id"]
        cdesc = case["description"]

        lines.append(f"  // compute case {cid}")
        lines.append(f"  requirement def ComputeCase_{cid} {{")

        # Only add the refinement when ComputeBase is present
        if compute_base_exists:
            lines.append("    refines ComputeBase")

        if cdesc:
            lines.append(f"    doc /*{cdesc}*/")

        # assumes
        for _k, name, desc, expr in case["assumes"]:
            expr = expr.replace("->:", "->")
            if name:
                lines.append(f"    doc /*{name}*/")
            if desc:
                lines.append(f"    doc /*{desc}*/")
            lines.append(f"    assume constraint {{ {expr} }}")

        # guarantees
        for _k, name, desc, expr in case["guarantees"]:
            expr = expr.replace("->:", "->")
            if name:
                lines.append(f"    doc /*{name}*/")
            if desc:
                lines.append(f"    doc /*{desc}*/")
            lines.append(f"    require constraint {{ {expr} }}")

        lines.append("  }")
        lines.append("")

    lines.append("}")
    return "\n".join(lines)


def validate_sysml_antlr(sysml_path: str) -> None:
    home = os.path.expanduser("~")
    candidate = os.path.join(home, "hamr-sysml-parser")
    if os.path.isdir(candidate):
        parser_out = os.path.join(candidate, "out")
        antlr_jar = os.path.join(candidate, "antlr-4.13.2-complete.jar")
    else:
        here = os.path.dirname(__file__)
        parser_out = os.path.join(here, "out")
        antlr_jar = os.path.join(here, "antlr-4.13.2-complete.jar")

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
        print("\n=== ANTLR4 TestRig Parse Succeeded ===")
    else:
        print("\n=== ANTLR4 TestRig Parse Failed ===")
        print(proc.stderr.strip() or proc.stdout.strip())


def process_sysml_file(path: str, *, validate: bool = True, debug: bool = False, antlr: bool = True,
                       sireum_validate: bool = False, sireum_cmd: str | None = None,
                       sireum_grammar: str | None = None) -> None:
    text = open(path).read()
    blocks = extract_gumbo_blocks(text)
    if not blocks:
        print("No GUMBO contracts found")
        return

    replacements = []
    for start, end, gumbo in blocks:
        clean = re.sub(r"\[[^\]]+\]", "", gumbo).strip()
        tree = parser.parse(clean)
        tf = GumboTransformer()
        tf.transform(tree)
        part = find_enclosing_part_name(text, start)

        sysml = translate_monitor_gumbo(tf, part)
        indent = _get_block_indent(text, start)
        sysml = "\n".join((indent + line if line.strip() else "") for line in sysml.splitlines())
        replacements.append((start, end, sysml))

    new_text = replace_blocks(text, replacements)
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
    cli = argparse.ArgumentParser(description="Translate Gumbo to SysML v2")
    cli.add_argument("input", help="SysML file containing Gumbo blocks")
    cli.add_argument("--no-validate", action="store_true", help="Skip validation")
    cli.add_argument("--show", action="store_true", help="Print translated SysML")
    cli.add_argument("--no-antlr", action="store_true", help="Skip ANTLR validation")
    cli.add_argument("--sireum-validate", action="store_true", help="Also run Sireum IVE parser")
    cli.add_argument("--sireum-cmd", metavar="PATH", help="Path to sireum executable")
    cli.add_argument("--sireum-grammar", metavar="FILE", help="SysML v2 grammar for Sireum")
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