# GumboTransformers.py
#!/usr/bin/env python3
# ========================================================================================================
# Enhanced Gumbo → SysML v2 translator (pure SysML output)
#
# What’s new in this drop:
# - Output contains ONLY SysML v2 (no language "GUMBO" blocks).
# - Per-part contracts packaged under   package <PartName>_Contracts { … }.
# - Root aggregator file:               package GUMBO_Contracts { … }  (imports every per-part package).
# - Model file gets:                    private import GUMBO_Contracts::*;  injected in the root package.
# - Guarantees/Assumes mapping to:      require/assume constraint { … }.
# - Compute cases:                      requirement def ComputeCase_* specializes ComputeBase { … }.
# - calc def:                           last expression is the result (no 'return').
# - Boolean literals:                   true/false only.
# - Typed numerics like '96 [s32]'      appear as comments above constraints (no inline /* */).
# - Balances all braces and semicolons to keep the ANTLR SysMLv2 grammar happy.
# ========================================================================================================
import argparse
import os
import re
import subprocess
import sys
from typing import Dict, List, Tuple, Optional, Iterable

from gumbo_parser import parser, GumboTransformer
from sysml_utils import extract_gumbo_blocks, find_enclosing_part_name, replace_blocks, ensure_root_contract_import

# ----------------------------- helpers -----------------------------

def _collapse_doc(text: str) -> str:
    """Sanitize for doc blocks."""
    return text.replace("*/", "*\\/")

def _fmt_expr_multiline(expr: str, indent: str = "      ") -> str:
    """Break after 'and/or/&/|' for readability."""
    expr = re.sub(r'\s*\band\b\s*', f' and\n{indent}', expr)
    expr = re.sub(r'\s*\bor\b\s*',  f' or\n{indent}',  expr)
    expr = re.sub(r'\s*&\s*',       f' &\n{indent}',   expr)
    expr = re.sub(r'\s*\|\s*',      f' |\n{indent}',   expr)
    return expr

_TAG_MARK = "__TAG__"

def _strip_tag_markers(expr: str) -> Tuple[str, List[str]]:
    """
    Convert "97__TAG__[s32]" to "97" and return list ["97 [s32]", ...] we can print above the constraint.
    """
    tags: List[str] = []
    def repl(m: re.Match) -> str:
        num = m.group(1)
        tag = m.group(2)
        tags.append(f"{num} [{tag}]")
        return num
    clean = re.sub(rf'(\d+(?:\.\d+)?){_TAG_MARK}\[([^\]]+)\]', repl, expr)
    return clean, tags

def _doc_lines(block: Optional[str]) -> Iterable[str]:
    if not block: return []
    for ln in _collapse_doc(block).splitlines():
        ln = ln.strip()
        if ln:
            yield f"  doc /*{ln}*/"

def _name_desc_doc(name: Optional[str], desc: str) -> Iterable[str]:
    tag = None
    if name and desc:
        tag = f"ID {name} {desc}"
    elif name:
        tag = f"ID {name}"
    elif desc:
        tag = f"ID {desc}"
    if tag:
        yield f"    doc /*{_collapse_doc(tag)}*/"

# ----------------------------- translate one GUMBO block into a per-part package -----------------------------

def translate_block_to_package(part_name: str,
                               tf: GumboTransformer,
                               comments: Dict[str, object] | None = None) -> str:
    """
    Emit a *per-part* contracts package:  package <part>_Contracts { … }.
    This is valid SysML v2 per your ANTLR grammar.
    """
    comments = comments or {"header": None, "sections": {}, "cases": {}}

    lines: List[str] = []
    lines.append(f"package {part_name}_Contracts {{")
    lines.append("  private import Isolette_Data_Model::*;")
    lines.append("  private import Base_Types::*;")
    lines.append("")

    # State
    if tf.state_vars:
        for var, typ in tf.state_vars:
            lines.append(f"  attribute {var}: {typ};")
        lines.append("")

    # Functions -> calc def with 'in attribute' params; body is final expression (no 'return')
    for (fname, rettype, body, params) in tf.helper_funcs:
        body_clean, body_tags = _strip_tag_markers(body)
        lines.append(f"  calc def {fname} {{")
        for p, t in params:
            lines.append(f"    in attribute {p}: {t};")
        if body_tags:
            lines.append(f"    // typed literals: {', '.join(body_tags)}")
        lines.append(f"    {_fmt_expr_multiline(body_clean, '    ')}")
        lines.append("  }")
        lines.append("")

    # InitializeContract
    if tf.initialize_guarantees:
        lines.append("  requirement def InitializeContract {")
        for _k, name, desc, expr in tf.initialize_guarantees:
            expr_clean, expr_tags = _strip_tag_markers(expr)
            for dl in _name_desc_doc(name, desc): lines.append(dl)
            if expr_tags:
                lines.append(f"    // typed literals: {', '.join(expr_tags)}")
            lines.append(f"    require constraint {{ {_fmt_expr_multiline(expr_clean)} }}")
            lines.append("")
        lines.append("  }")
        lines.append("")

    # IntegrationContract (assumes + requires)
    if tf.integration_assumes or tf.integration_requires:
        lines.append("  requirement def IntegrationContract {")
        for _k, name, desc, expr in tf.integration_assumes:
            expr_clean, expr_tags = _strip_tag_markers(expr)
            for dl in _name_desc_doc(name, desc): lines.append(dl)
            if expr_tags:
                lines.append(f"    // typed literals: {', '.join(expr_tags)}")
            lines.append(f"    assume constraint {{ {_fmt_expr_multiline(expr_clean)} }}")
            lines.append("")
        for _k, name, desc, expr in tf.integration_requires:
            expr_clean, expr_tags = _strip_tag_markers(expr)
            for dl in _name_desc_doc(name, desc): lines.append(dl)
            if expr_tags:
                lines.append(f"    // typed literals: {', '.join(expr_tags)}")
            lines.append(f"    require constraint {{ {_fmt_expr_multiline(expr_clean)} }}")
            lines.append("")
        lines.append("  }")
        lines.append("")

    # ComputeBase (top-level compute)
    compute_base_exists = bool(tf.compute_toplevel_assumes or tf.compute_toplevel_guarantees)
    if compute_base_exists:
        lines.append("  doc /*======  C o m p u t e     C o n s t r a i n t s =====*/")
        lines.append("  requirement def ComputeBase {")
        for _k, name, desc, expr in tf.compute_toplevel_assumes:
            expr_clean, expr_tags = _strip_tag_markers(expr)
            for dl in _name_desc_doc(name, desc): lines.append(dl)
            if expr_tags:
                lines.append(f"    // typed literals: {', '.join(expr_tags)}")
            lines.append(f"    assume constraint {{ {_fmt_expr_multiline(expr_clean)} }}")
            lines.append("")
        for _k, name, desc, expr in tf.compute_toplevel_guarantees:
            expr_clean, expr_tags = _strip_tag_markers(expr)
            for dl in _name_desc_doc(name, desc): lines.append(dl)
            if expr_tags:
                lines.append(f"    // typed literals: {', '.join(expr_tags)}")
            lines.append(f"    require constraint {{ {_fmt_expr_multiline(expr_clean)} }}")
            lines.append("")
        lines.append("  }")
        lines.append("")

    # Compute cases
    for case in tf.compute_cases:
        cid = case["id"]; cdesc = case["description"]
        # requirement header w/ specializes
        if compute_base_exists:
            lines.append(f"  requirement def ComputeCase_{cid} specializes ComputeBase {{")
        else:
            lines.append(f"  requirement def ComputeCase_{cid} {{")
        if cdesc:
            lines.append(f"    doc /*{_collapse_doc(cdesc)}*/")
        for _k, name, desc, expr in case["assumes"]:
            expr_clean, expr_tags = _strip_tag_markers(expr)
            for dl in _name_desc_doc(name, desc): lines.append(dl)
            if expr_tags:
                lines.append(f"    // typed literals: {', '.join(expr_tags)}")
            lines.append(f"    assume constraint {{ {_fmt_expr_multiline(expr_clean)} }}")
            lines.append("")
        for _k, name, desc, expr in case["guarantees"]:
            expr_clean, expr_tags = _strip_tag_markers(expr)
            for dl in _name_desc_doc(name, desc): lines.append(dl)
            if expr_tags:
                lines.append(f"    // typed literals: {', '.join(expr_tags)}")
            lines.append(f"    require constraint {{ {_fmt_expr_multiline(expr_clean)} }}")
            lines.append("")
        lines.append("  }")
        lines.append("")

    lines.append("}")  # end part contracts package
    return "\n".join(lines)

# ----------------------------- collect line comments nearby (optional) -----------------------------

def collect_line_comment_blocks(gumbo_text: str) -> Dict[str, object]:
    """
    Group consecutive // lines and anchor them to sections/cases.
    """
    lines = gumbo_text.splitlines()
    out = {"header": None, "sections": {}, "cases": {}}
    i = 0
    while i < len(lines):
        ln = lines[i]
        if ln.strip().startswith("//"):
            block: List[str] = []
            while i < len(lines) and lines[i].strip().startswith("//"):
                block.append(lines[i].split("//", 1)[1].strip())
                i += 1
            comment_text = "\n".join(block).strip()
            # find anchor
            j = i
            while j < len(lines) and (not lines[j].strip() or lines[j].strip().startswith("//")):
                j += 1
            anchor = lines[j].strip() if j < len(lines) else ""
            m_case = re.match(r"case\s+([A-Za-z_][A-Za-z0-9_]*)\b", anchor)
            if m_case:
                cid = m_case.group(1)
                prev = out["cases"].get(cid, "")
                out["cases"][cid] = (prev + ("\n" if prev and comment_text else "") + comment_text).strip()
            elif anchor.startswith("state"):
                out["sections"]["state"] = comment_text
            elif anchor.startswith("functions"):
                out["sections"]["functions"] = comment_text
            elif anchor.startswith("initialize"):
                out["sections"]["initialize"] = comment_text
            elif anchor.startswith("integration"):
                out["sections"]["integration"] = comment_text
            elif anchor.startswith("compute"):
                out["sections"]["compute"] = comment_text
            else:
                out["header"] = (out["header"] + "\n" + comment_text).strip() if out["header"] else comment_text
        else:
            i += 1
    return out

# ----------------------------- ANTLR validation -----------------------------

def validate_sysml_antlr(sysml_path: str, verbose: bool = False) -> None:
    home = os.path.expanduser("~")
    candidate = os.path.join(home, "hamr-sysml-parser")

    if os.path.isdir(candidate):
        parser_out = os.path.join(candidate, "out")
        antlr_jar = os.path.join(candidate, "antlr-4.13.2-complete.jar")
        parser_jar = os.path.join(candidate, "hamr-sysml-parser.jar")
    else:
        here = os.path.dirname(__file__)
        parser_out = os.path.join(here, "out")
        antlr_jar = os.path.join(here, "antlr-4.13.2-complete.jar")
        parser_jar = os.path.join(here, "hamr-sysml-parser.jar")

    grammar_name = "org.sireum.hamr.sysml.parser.SysMLv2"
    cp = os.pathsep.join([parser_out, parser_jar, antlr_jar])

    cmd = [
        "java", "-cp", cp,
        "org.antlr.v4.gui.TestRig",
        grammar_name, "ruleRootNamespace",
        "-SLL","-diagnostics",
        sysml_path,
    ]

    if verbose:
        print("Running:", " ".join(cmd))

    try:
        proc = subprocess.run(cmd, capture_output=True, text=True, timeout=120)
    except FileNotFoundError:
        print("Java or ANTLR TestRig not found.")
        return
    except subprocess.TimeoutExpired:
        print("ANTLR validation timed out.")
        return

    stdout, stderr = proc.stdout.strip(), proc.stderr.strip()
    if verbose and stdout:
        print("\n--- TestRig stdout ---\n" + stdout)
    if verbose and stderr:
        print("\n--- TestRig stderr ---\n" + stderr)

    err_text = (stdout + "\n" + stderr).lower()
    suspicious = any(kw in err_text for kw in (
        "mismatched input", "no viable alternative", "extraneous input", "token recognition error"
    ))

    if proc.returncode == 0 and not suspicious:
        print("\n=== ANTLR4 TestRig Parse Succeeded ===")
    else:
        print("\n=== ANTLR4 TestRig Parse Failed ===")
        if not verbose:
            print(stderr or stdout or "(no output)")

# ----------------------------- Main translation driver -----------------------------

def process_sysml_file(path: str,
                       *,
                       show: bool = False,
                       no_model_validate: bool = False,
                       validate_contracts: bool = True) -> None:
    text = open(path, encoding="utf-8").read()
    blocks = extract_gumbo_blocks(text)
    if not blocks:
        print("No GUMBO contracts found.")
        return

    per_part_packages: List[str] = []
    replacements: List[Tuple[int, int, str]] = []

    for start, end, gumbo in blocks:
        clean = gumbo.strip()

        # comments near anchors
        comment_map = collect_line_comment_blocks(clean)

        # parse
        tree = parser.parse(clean)
        tf = GumboTransformer()
        tf.transform(tree)

        # generate per-part contracts package text
        part = find_enclosing_part_name(text, start)
        pkg_text = translate_block_to_package(part, tf, comments=comment_map)
        per_part_packages.append(pkg_text)

        # replace original GUMBO block with a one-liner note
        indent_match = re.match(r'[ \t]*', text[text.rfind('\n', 0, start) + 1:start])
        indent = indent_match.group(0) if indent_match else ""
        placeholder = indent + f"// GUMBO contracts moved to GUMBO_Contracts::{part}_Contracts\n"
        replacements.append((start, end, placeholder))

    # write the updated model file
    new_text = replace_blocks(text, replacements)
    new_text = ensure_root_contract_import(new_text)  # add root import once near the top
    translated_path = path.rsplit(".", 1)[0] + ".translated.sysml"
    with open(translated_path, "w", encoding="utf-8") as fh:
        fh.write(new_text)
    print(f"Translated file written to {translated_path}")

    print("\n=== Translated SysML v2 (model) ===\n")
    if show:
        print(new_text)

    # write root aggregator
    contracts_path = path.rsplit(".", 1)[0] + ".gumbo.contracts.sysml"
    agg_lines: List[str] = []
    agg_lines.append("package GUMBO_Contracts {")
    agg_lines.append("  private import Isolette_Data_Model::*;")
    agg_lines.append("  private import Base_Types::*;")
    agg_lines.append("")

    # Paste each per-part package (already valid)
    for pkg in per_part_packages:
        # indent nicely inside aggregator
        for ln in pkg.splitlines():
            agg_lines.append("  " + ln)
        agg_lines.append("")

    agg_lines.append("}")  # end GUMBO_Contracts
    contracts_text = "\n".join(agg_lines)

    with open(contracts_path, "w", encoding="utf-8") as fh:
        fh.write(contracts_text)
    print(f"Contracts file written to {contracts_path}")

    print("\n=== GUMBO Contracts (aggregator) ===\n")
    if show:
        print(contracts_text)

    # validate both
    if not no_model_validate:
        print("\n=== ANTLR validate: model ===")
        validate_sysml_antlr(translated_path, verbose=True)
    if validate_contracts:
        print("\n=== ANTLR validate: contracts ===")
        validate_sysml_antlr(contracts_path, verbose=True)

def main() -> None:
    cli = argparse.ArgumentParser(
        prog="GUMBY_CLI",
        description="Gumbo → SysML v2 translator (pure SysML output)"
    )
    cli.add_argument("input", help="SysML file containing GUMBO blocks")
    cli.add_argument("--show", action="store_true", help="Print translated model + aggregator")
    cli.add_argument("--no-model-validate", action="store_true", help="Skip validation of the .translated.sysml")
    cli.add_argument("--no-contracts-validate", action="store_true", help="Skip validation of the contracts file")
    args = cli.parse_args()

    process_sysml_file(
        args.input,
        show=args.show,
        no_model_validate=args.no_model_validate,
        validate_contracts=not args.no_contracts_validate
    )

if __name__ == "__main__":
    main()
