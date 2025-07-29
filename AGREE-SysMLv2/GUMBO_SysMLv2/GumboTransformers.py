#!/usr/bin/env python3
# ========================================================================================================
# @author: Amer N Tahat
# Collins Aerospace,
# Enhanced Gumbo-to-SysML v2 transformer:
#
# feat: extend Gumbo parser to support initialize, compute cases, and complex expressions
# - Add support for initialize sections with guarantee statements
# - Add support for compute sections with compute_cases containing anonymous assume/guarantee statements
# - Extend expression grammar to handle |, &, implies, not operators
# - Implement pattern-based qualified name reconstruction for proper :: and . separators
# - Add separate grammar rules for named vs anonymous statements in different contexts
# - Generate separate SysML v2 contracts for initialization, integration, and each compute case
# - Align with pure SysML v2 idioms: requirement def, assume constraint, require constraint
# ========================================================================================================

import re
import argparse
import sys

from gumbo_parser import parser, GumboTransformer
from sysml_utils import (
    extract_gumbo_blocks,
    find_enclosing_part_name,
    replace_blocks,
)

# -------------------------------------------------------------------
# 3) Translation function

def translate_monitor_gumbo(transformer: GumboTransformer, block_name: str) -> str:
    """
    Generate a SysML v2 snippet for the Monitor component, converting embedded Gumbo-style blocks
    into pure SysML v2 constructs. Respects GumboTransformer naming:
      - state_vars
      - helper_funcs
      - integration_assumes
      - guarantees
      - initialize_guarantees
      - compute_cases
    """
    lines = []
    lines.append("package IsoletteContracts {")
    lines.append("  import Isolette_Data_Model::*;")
    lines.append("  import Base_Types::*;")
    lines.append("")

    # ------------------ State variables ------------------
    for var_name, type_str in transformer.state_vars:
        lines.append(f"  attribute {var_name}: {type_str};")
    lines.append("")

    # ------------------ Helper functions ------------------
    for func_name, return_type, body_expr in transformer.helper_funcs:
        # Pure calculation helper
        lines.append(f"  calc def {func_name} {{ return y: {return_type} = {body_expr}; }}")
    lines.append("")

    # ------------------ Initialize contract ------------------
    if transformer.initialize_guarantees:
        exprs = [
            e.replace("==", "=").replace("->:", "->")
            for (_k, _n, _d, e) in transformer.initialize_guarantees
        ]
        expr_combined = " && ".join(exprs)
        lines.append("  requirement def InitializeContract {")
        lines.append(f"    require constraint {{ {expr_combined} }}")
        lines.append("  }")
        lines.append("")

    # ------------------ Integration contract ------------------
    if transformer.integration_assumes or transformer.guarantees:
        assumes = [
            expr.replace("->:", "->")
            for (_k, _n, _d, expr) in transformer.integration_assumes
        ]
        guarantees = [
            expr.replace("==", "=").replace("->:", "->")
            for (_k, _n, _d, expr) in transformer.guarantees
        ]
        assumes_expr = " && ".join(assumes) if assumes else "true"
        guar_expr    = " && ".join(guarantees) if guarantees else "true"
        lines.append("  requirement def MonitorIntegration {")
        lines.append(f"    assume constraint {{ {assumes_expr} }}")
        lines.append(f"    require constraint {{ {guar_expr} }}")
        lines.append("  }")
        lines.append("")

    # ------------------ Compute cases ------------------
    for case in transformer.compute_cases:
        case_id   = case['id']
        case_desc = case.get('description', "")
        assumes   = [
            expr.replace("->:", "->")
            for (_k, _n, _d, expr) in case['assumes']
        ]
        guarantees = [
            expr.replace("==", "=").replace("->:", "->")
            for (_k, _n, _d, expr) in case['guarantees']
        ]
        assumes_expr = " && ".join(assumes) if assumes else "true"
        guar_expr    = " && ".join(guarantees) if guarantees else "true"

        if case_desc:
            lines.append(f"  // {case_desc}")
        lines.append(f"  requirement def {case_id} {{")
        if case_desc:
            lines.append(f"    doc \"{case_desc}\"")
        lines.append(f"    assume constraint {{ {assumes_expr} }}")
        lines.append(f"    require constraint {{ {guar_expr} }}")
        lines.append("  }")
        lines.append("")

    lines.append("}")
    return "\n".join(lines)


def validate_sysml(sysml_text: str, jar_path: str = "MCSysMLv2.jar") -> None: #ToDo: Use SysMl v2 Hammer parser.
    """
    Run the MontiCore SysML v2 parser on the generated text.
    Writes sysml_text to temp file, invokes the jar, prints output.
    """
    import subprocess, tempfile, os
    with tempfile.NamedTemporaryFile("w", suffix=".sysml", delete=False) as fh:
        fh.write(sysml_text)
        fh.flush()
        tmp_name = fh.name
    try:
        result = subprocess.run(
            ["java", "-jar", jar_path, "-i", tmp_name, "-pp"], # Use SysML v2 Hammer Paser here
            capture_output=True, text=True, timeout=40
        )
        if result.returncode == 0:
            print("\n=== SysML v2 Parser Output ===") #ToDo: Use SysMl v2 Hammer parser.
            print(result.stdout.strip())
            print("Validation succeeded")
        else:
            print("\nValidation failed")
            print(result.stderr)
    finally:
        os.unlink(tmp_name)


def process_sysml_file(path: str, *, validate: bool = True, debug: bool = False) -> None:
    """
    Load path, replace Gumbo blocks with translated SysML.
    If validate=True, run parser; if debug=True, print preview.
    """
    with open(path, "r") as fh:
        text = fh.read()

    blocks = extract_gumbo_blocks(text)
    if not blocks:
        print("No GUMBO contracts found")
        return

    replacements = []
    contracts = []
    for start, end, gumbo in blocks:
        gumbo_clean = re.sub(r"\[[^\]]+\]", "", gumbo)
        tree = parser.parse(gumbo_clean.strip())
        transformer = GumboTransformer()
        transformer.transform(tree)
        name = find_enclosing_part_name(text, start)
        sysml_contract = translate_monitor_gumbo(transformer, block_name=name)
        contracts.append(sysml_contract)
        replacements.append((start, end, ""))

    new_text = replace_blocks(text, replacements)
    if contracts:
        new_text += "\n\n" + "\n\n".join(contracts) + "\n"

    out_path = path.rsplit('.', 1)[0] + '.translated.sysml'
    with open(out_path, 'w') as fh:
        fh.write(new_text)
    print(f'Translated file written to {out_path}')

    if debug or not validate:
        print("\n=== Translated SysML v2 (preview) ===")
        print(new_text)
        print()

    if validate:
        validate_sysml(new_text)


def main() -> None:
    """CLI entry point."""
    parser_cli = argparse.ArgumentParser(description="Translate Gumbo to SysML v2")
    parser_cli.add_argument("input", help="SysML file containing Gumbo blocks")
    parser_cli.add_argument(
        "--no-validate",
        action="store_true",
        help="Skip running the MontiCore parser on the translated model",
    )
    parser_cli.add_argument(
        "--show",
        action="store_true",
        help="Print the translated SysML text",
    )
    args = parser_cli.parse_args()

    process_sysml_file(
        args.input,
        validate=not args.no_validate,
        debug=args.show
    )

if __name__ == '__main__':
    main()
