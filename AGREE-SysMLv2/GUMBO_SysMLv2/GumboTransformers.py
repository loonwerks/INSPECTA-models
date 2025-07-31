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
# - Auto-detect ANTLR parser location under ~/hamr-sysml-parser or fallback to script directory
# ========================================================================================================

import os
import re
import argparse
import subprocess
import tempfile
import shutil

from gumbo_parser import parser, GumboTransformer
from sysml_utils import extract_gumbo_blocks, find_enclosing_part_name, replace_blocks

# -------------------------------------------------------------------
# 1) Translation of a single Gumbo block into a SysML v2 contract
# -------------------------------------------------------------------

def translate_monitor_gumbo(transformer: GumboTransformer, block_name: str) -> str:
    lines = []
    lines.append("package IsoletteContracts {")
    lines.append("  import Isolette_Data_Model::*")
    lines.append("  import Base_Types::*")
    lines.append("")

    # State variables
    if transformer.state_vars:
        for var, typ in transformer.state_vars:
            lines.append(f"  attribute {var}: {typ}")
        lines.append("")

    # Helper functions → 'calc def'
    if transformer.helper_funcs:
        for fname, rettype, body in transformer.helper_funcs:
            lines.append(f"  calc def {fname} {{")
            lines.append(f"    return {body}")
            lines.append("  }")
            lines.append("")

    # Initialize contract
    if transformer.initialize_guarantees:
        exprs = [
            e.replace("->:", "->")
            for (_k, _n, _d, e) in transformer.initialize_guarantees
        ]
        expr_combined = " and ".join(exprs)
        lines.append("  requirement def InitializeContract {")
        lines.append(f"    require constraint {{ {expr_combined} }}")
        lines.append("  }")
        lines.append("")

    # Integration contract
    if transformer.integration_assumes or transformer.guarantees:
        assumes = [
            expr.replace("->:", "->")
            for (_k, _n, _d, expr) in transformer.integration_assumes
        ]
        guarantees = [
            expr.replace("->:", "->")
            for (_k, _n, _d, expr) in transformer.guarantees
        ]
        assumes_expr = " and ".join(assumes) if assumes else "true"
        guar_expr   = " and ".join(guarantees) if guarantees else "true"

        lines.append("  requirement def MonitorIntegration {")
        lines.append(f"    assume constraint {{ {assumes_expr} }}")
        lines.append(f"    require constraint {{ {guar_expr} }}")
        lines.append("  }")
        lines.append("")

    # Compute cases
    for case in transformer.compute_cases:
        case_id   = case["id"]
        case_desc = case["description"]
        assumes   = [
            expr.replace("->:", "->")
            for (_k, _n, _d, expr) in case["assumes"]
        ]
        guarantees = [
            expr.replace("->:", "->")
            for (_k, _n, _d, expr) in case["guarantees"]
        ]
        assumes_expr = " and ".join(assumes) if assumes else "true"
        guar_expr    = " and ".join(guarantees) if guarantees else "true"

        if case_desc:
            lines.append(f"  // {case_desc}")
        lines.append(f"  requirement def {case_id} {{")
        if case_desc:
            lines.append(f"    doc /* {case_desc} */")
        lines.append(f"    assume constraint {{ {assumes_expr} }}")
        lines.append(f"    require constraint {{ {guar_expr} }}")
        lines.append("  }")
        lines.append("")

    lines.append("}")
    return "\n".join(lines)


# -------------------------------------------------------------------
# 2) Validation via ANTLR4's TestRig, auto-detecting parser location
# -------------------------------------------------------------------

def validate_sysml_antlr(sysml_path: str) -> None:
    """
    Validate SysML v2 by invoking ANTLR4's TestRig on the compiled parser.
    It first looks for '~/hamr-sysml-parser' containing:
      - out/ (compiled .class files)
      - antlr-4.13.2-complete.jar
    If not found, it falls back to './out' and './antlr-4.13.2-complete.jar'
    next to this script.
    """
    # Try home directory
    home = os.path.expanduser("~")
    hamr_dir = os.path.join(home, "hamr-sysml-parser")
    if os.path.isdir(hamr_dir):
        parser_out = os.path.join(hamr_dir, "out")
        antlr_jar  = os.path.join(hamr_dir, "antlr-4.13.2-complete.jar")
    else:
        # fallback to script directory
        script_dir = os.path.dirname(__file__)
        parser_out = os.path.join(script_dir, "out")
        antlr_jar  = os.path.join(script_dir, "antlr-4.13.2-complete.jar")

    classpath = f"{parser_out}:{antlr_jar}"

    cmd = [
        "java",
        "-cp", classpath,
        "org.antlr.v4.gui.TestRig",
        "SysMLv2",
        "ruleRootNamespace",
        sysml_path,
    ]

    try:
        proc = subprocess.run(cmd, capture_output=True, text=True, timeout=60)
    except FileNotFoundError:
        print("Java or ANTLR TestRig not found; ensure Java is installed and the ANTLR JAR is present.")
        return
    except subprocess.TimeoutExpired:
        print("ANTLR validation timed out.")
        return

    if proc.returncode == 0:
        print("\n=== ANTLR4 TestRig Parse Succeeded ===")
    else:
        print("\n=== ANTLR4 TestRig Parse Failed ===")
        print(proc.stderr.strip() or proc.stdout.strip())


# -------------------------------------------------------------------
# 3) Main processing and CLI
# -------------------------------------------------------------------

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
    with open(path, "r") as fh:
        text = fh.read()

    blocks = extract_gumbo_blocks(text)
    if not blocks:
        print("No GUMBO contracts found")
        return

    replacements = []
    contracts = []
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
    cli = argparse.ArgumentParser(description="Translate GUMBO to SysML v2")
    cli.add_argument("input", help="SysML file containing GUMBO blocks")
    cli.add_argument("--no-validate", action="store_true", help="Skip validation")
    cli.add_argument("--show", action="store_true", help="Print translated SysML")
    cli.add_argument("--no-antlr", action="store_true", help="Skip ANTLR validation")
    cli.add_argument("--sireum-validate", action="store_true", help="Use Sireum IVE parser")
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
