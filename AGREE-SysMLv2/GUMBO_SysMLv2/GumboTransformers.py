#!/usr/bin/env python3
# ========================================================================================================
# @author: Amer N Tahat
# Collins Aerospace
# Enhanced Gumbo-to-SysML v2 transformer (GUMBY)
#
# Key features:
# - Functions: parse typed parameters; emit `calc def` with SysML-ish parameter typing:
#       calc def F { in x as Type; return EXPR }
#   (No return cast unless needed; we omit `as Type` on the return by default)
# - Comments: preserve `//` blocks and anchor them to sections/cases as `doc /*...*/`
# - Tags: keep numeric tags like `96 [s32]` and render as `96 /* [s32] */`
# - Readability:
#     • add a blank line before `package ... {`
#     • add a blank line after every assume/require
#     • split boolean chains by placing a line-break after `and`/`or`
# - ComputeBase is emitted only if there are top-level compute assumes/guarantees
# - CLI wrapper provided as GUMBY_CLI.py
# ========================================================================================================
import os
import re
import argparse
import subprocess
from typing import Dict, List, Tuple, Optional

from gumbo_parser import parser, GumboTransformer
from sysml_utils import extract_gumbo_blocks, find_enclosing_part_name, replace_blocks

# ----------------------------- small utilities -----------------------------

def _get_block_indent(text: str, start: int) -> str:
    line_start = text.rfind('\n', 0, start) + 1
    m = re.match(r'[ \t]*', text[line_start:start])
    return m.group(0) if m else ''

def _collapse_doc(text: str) -> str:
    """Sanitize for doc blocks."""
    return text.replace("*/", "*\\/")

def _fmt_expr_with_breaks(expr: str, base_indent: str, extra_levels: int = 2) -> str:
    """
    Add a newline and indent after every ' and ' or ' or ' to improve readability.
    """
    indent = base_indent + "  " * extra_levels
    expr = re.sub(r'\s+(and)\s+', f' and\n{indent}', expr)
    expr = re.sub(r'\s+(or)\s+',  f' or\n{indent}',  expr)
    return expr

def collect_line_comment_blocks(gumbo_text: str) -> Dict[str, object]:
    """
    Group consecutive // lines and attach to the very next significant element:
      - 'state', 'functions', 'initialize', 'integration', 'compute', or 'case <ID>'
    Returns:
      {
        "header": str | None,
        "sections": { "state": str?, "functions": str?, "initialize": str?, "integration": str?, "compute": str? },
        "cases": { "<ID>": str, ... }
      }
    """
    lines = gumbo_text.splitlines()
    out = {"header": None, "sections": {}, "cases": {}}
    i = 0
    while i < len(lines):
        ln = lines[i]
        if ln.strip().startswith("//"):
            # collect this block
            block: List[str] = []
            while i < len(lines) and lines[i].strip().startswith("//"):
                block.append(lines[i].split("//", 1)[1].strip())
                i += 1
            comment_text = "\n".join(block).strip()

            # seek the next non-empty, non-comment line
            j = i
            while j < len(lines) and (not lines[j].strip() or lines[j].strip().startswith("//")):
                j += 1
            anchor = lines[j].strip() if j < len(lines) else ""

            m_case = re.match(r"case\s+([A-Za-z_][A-Za-z0-9_]*)\b", anchor)
            if m_case:
                cid = m_case.group(1)
                prev = out["cases"].get(cid, "")
                out["cases"][cid] = (prev + "\n" + comment_text).strip() if prev else comment_text
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

def _emit_named_doc(lines: List[str], name: Optional[str], desc: str, indent: str = "    "):
    """
    Emit a single doc line combining ID and description so it's distinguishable
    from general comments.
    """
    if name or desc:
        s = " ".join(part for part in [f"ID {name}" if name else None, desc if desc else None] if part)
        if s:
            lines.append(f"{indent}doc /*{_collapse_doc(s)}*/")

# ----------------------------- translation -----------------------------

def translate_monitor_gumbo(transformer: GumboTransformer,
                            part_name: str,
                            comments: Optional[Dict[str, object]] = None,
                            base_indent: str = "  ") -> str:
    """
    Build a SysMLv2 package named <part_name>_GUMBO_Contracts.

    - emits `refines ComputeBase` only when ComputeBase exists
    - preserves statement IDs/descriptions with a single combined doc line
    - places line comments (//) as doc blocks anchored to: initialize, compute/ComputeBase, and each compute case
    - ensures readability newlines and line-breaks after 'and'/'or'
    """
    comments = comments or {"header": None, "sections": {}, "cases": {}}
    lines: List[str] = []

    # ──────────────────────────────────────────────────────────────
    # package header + imports
    # ──────────────────────────────────────────────────────────────
    lines.append(f"\npackage {part_name}_GUMBO_Contracts {{")
    lines.append(f"{base_indent}import Isolette_Data_Model::*")
    lines.append(f"{base_indent}import Base_Types::*")
    lines.append("")

    # header comments (if any)
    if comments.get("header"):
        for para in _collapse_doc(comments["header"]).splitlines():
            if para.strip():
                lines.append(f"{base_indent}doc /*{para}*/")
        lines.append("")

    # ──────────────────────────────────────────────────────────────
    # state variables
    # ──────────────────────────────────────────────────────────────
    if comments["sections"].get("state"):
        for para in _collapse_doc(comments["sections"]["state"]).splitlines():
            lines.append(f"{base_indent}doc /*{para}*/")
    for var, typ in transformer.state_vars:
        lines.append(f"{base_indent}attribute {var}: {typ}")
    if transformer.state_vars:
        lines.append("")

    # ──────────────────────────────────────────────────────────────
    # helper functions  →  calc def + parameter typing via `in <p> as <Type>`
    # ──────────────────────────────────────────────────────────────
    if comments["sections"].get("functions"):
        for para in _collapse_doc(comments["sections"]["functions"]).splitlines():
            lines.append(f"{base_indent}doc /*{para}*/")
    for func in transformer.helper_funcs:
        # (name, return_type, body, params)
        if len(func) == 4:
            fname, rettype, body, params = func
        else:
            fname, rettype, body = func
            params = []
        lines.append(f"{base_indent}calc def {fname} {{")
        for p, t in params:
            lines.append(f"{base_indent}  in {p} as {t}")
        # Default: omit the return cast, unless you later decide to enforce narrowing
        lines.append(f"{base_indent}  return {_fmt_expr_with_breaks(body, base_indent, extra_levels=2)}")
        lines.append(f"{base_indent}}}")
        lines.append("")

    # ──────────────────────────────────────────────────────────────
    # InitializeContract
    # ──────────────────────────────────────────────────────────────
    if transformer.initialize_guarantees:
        if comments["sections"].get("initialize"):
            for para in _collapse_doc(comments["sections"]["initialize"]).splitlines():
                lines.append(f"{base_indent}doc /*{para}*/")
        lines.append(f"{base_indent}requirement def InitializeContract {{")
        for _k, name, desc, expr in transformer.initialize_guarantees:
            _emit_named_doc(lines, name, desc, indent=f"{base_indent}  ")
            expr_out = _fmt_expr_with_breaks(expr, base_indent, extra_levels=2)
            lines.append(f"{base_indent}  require constraint {{ {expr_out} }}")
            lines.append("")
        lines.append(f"{base_indent}}}")
        lines.append("")

    # ──────────────────────────────────────────────────────────────
    # MonitorIntegration
    # ──────────────────────────────────────────────────────────────
    if transformer.integration_assumes or transformer.guarantees:
        if comments["sections"].get("integration"):
            for para in _collapse_doc(comments["sections"]["integration"]).splitlines():
                lines.append(f"{base_indent}doc /*{para}*/")
        lines.append(f"{base_indent}requirement def MonitorIntegration {{")
        for _k, name, desc, expr in transformer.integration_assumes:
            _emit_named_doc(lines, name, desc, indent=f"{base_indent}  ")
            expr_out = _fmt_expr_with_breaks(expr, base_indent, extra_levels=2)
            lines.append(f"{base_indent}  assume constraint {{ {expr_out} }}")
            lines.append("")
        if transformer.guarantees:
            for _k, name, desc, expr in transformer.guarantees:
                _emit_named_doc(lines, name, desc, indent=f"{base_indent}  ")
                expr_out = _fmt_expr_with_breaks(expr, base_indent, extra_levels=2)
                lines.append(f"{base_indent}  require constraint {{ {expr_out} }}")
                lines.append("")
        else:
            lines.append(f"{base_indent}  require constraint {{ true }}")
            lines.append("")
        lines.append(f"{base_indent}}}")
        lines.append("")

    # ──────────────────────────────────────────────────────────────
    # ComputeBase–top‑level compute contracts
    # ──────────────────────────────────────────────────────────────
    compute_base_exists = bool(
        transformer.compute_toplevel_assumes or transformer.compute_toplevel_guarantees
    )
    if compute_base_exists:
        if comments["sections"].get("compute"):
            for para in _collapse_doc(comments["sections"]["compute"]).splitlines():
                lines.append(f"{base_indent}doc /*{para}*/")
        lines.append(f"{base_indent}requirement def ComputeBase {{")
        for _k, name, desc, expr in transformer.compute_toplevel_assumes:
            _emit_named_doc(lines, name, desc, indent=f"{base_indent}  ")
            expr_out = _fmt_expr_with_breaks(expr, base_indent, extra_levels=2)
            lines.append(f"{base_indent}  assume constraint {{ {expr_out} }}")
            lines.append("")
        for _k, name, desc, expr in transformer.compute_toplevel_guarantees:
            _emit_named_doc(lines, name, desc, indent=f"{base_indent}  ")
            expr_out = _fmt_expr_with_breaks(expr, base_indent, extra_levels=2)
            lines.append(f"{base_indent}  require constraint {{ {expr_out} }}")
            lines.append("")
        lines.append(f"{base_indent}}}")
        lines.append("")

    # ──────────────────────────────────────────────────────────────
    # Individual compute cases
    # ──────────────────────────────────────────────────────────────
    for case in transformer.compute_cases:
        cid   = case["id"]
        cdesc = case["description"]

        lines.append(f"{base_indent}// compute case {cid}")
        lines.append(f"{base_indent}requirement def ComputeCase_{cid} {{")
        if compute_base_exists:
            lines.append(f"{base_indent}  refines ComputeBase")
        if comments["cases"].get(cid):
            for para in _collapse_doc(comments['cases'][cid]).splitlines():
                lines.append(f"{base_indent}  doc /*{para}*/")
        if cdesc:
            lines.append(f"{base_indent}  doc /*{cdesc}*/")

        # assumes
        for _k, name, desc, expr in case["assumes"]:
            _emit_named_doc(lines, name, desc, indent=f"{base_indent}  ")
            expr_out = _fmt_expr_with_breaks(expr, base_indent, extra_levels=3)
            lines.append(f"{base_indent}  assume constraint {{ {expr_out} }}")
            lines.append("")

        # guarantees
        for _k, name, desc, expr in case["guarantees"]:
            _emit_named_doc(lines, name, desc, indent=f"{base_indent}  ")
            expr_out = _fmt_expr_with_breaks(expr, base_indent, extra_levels=3)
            lines.append(f"{base_indent}  require constraint {{ {expr_out} }}")
            lines.append("")

        lines.append(f"{base_indent}}}")
        lines.append("")

    lines.append("}")
    return "\n".join(lines)

# ----------------------------- validation -----------------------------

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

# ----------------------------- CLI driver (for direct invocation) -----------------------------

def process_sysml_file(path: str, *, validate: bool = True, debug: bool = False, antlr: bool = True,
                       sireum_validate: bool = False, sireum_cmd: str | None = None,
                       sireum_grammar: str | None = None) -> None:
    text = open(path, encoding="utf-8").read()
    blocks = extract_gumbo_blocks(text)
    if not blocks:
        print("No GUMBO contracts found")
        return

    replacements = []
    for start, end, gumbo in blocks:
        clean = gumbo.strip()

        # collect comments for anchoring
        comment_map = collect_line_comment_blocks(clean)

        # parse
        tree = parser.parse(clean)
        tf = GumboTransformer()
        tf.transform(tree)

        # enclosing SysML part to prefix the contract package
        part = find_enclosing_part_name(text, start)

        sysml = translate_monitor_gumbo(tf, part, comments=comment_map, base_indent="  ")
        indent = _get_block_indent(text, start)
        sysml = "\n".join((indent + line if line.strip() else "") for line in sysml.splitlines())
        replacements.append((start, end, sysml))

    new_text = replace_blocks(text, replacements)
    out_path = path.rsplit(".", 1)[0] + ".translated.sysml"
    with open(out_path, "w", encoding="utf-8") as fh:
        fh.write(new_text)
    print(f"Translated file written to {out_path}")

    if debug:
        print("\n=== Translated SysML v2 (preview) ===\n")
        print(new_text)
    if validate and antlr:
        validate_sysml_antlr(out_path)

def main() -> None:
    # Kept for backward compatibility if someone calls this module directly
    cli = argparse.ArgumentParser(description="Translate Gumbo to SysML v2 (legacy entrypoint)")
    cli.add_argument("input", help="SysML file containing GUMBO blocks")
    cli.add_argument("--no-validate", action="store_true", help="Skip SysML validation")
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
