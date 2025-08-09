#!/usr/bin/env python3
# ========================================================================================================
# @author: Amer N Tahat
# Collins Aerospace
# Enhanced Gumbo-to-SysML v2 transformer
#
# Changes in this drop:
# - Functions: parse typed parameters; emit `calc def` with a signature doc line.
# - Comments: preserve `//` blocks and anchor them to their *next* statement or section/case.
# - Statement IDs: if a statement is named, emit a single doc line like:  /* [ASSUME REQ_X] text... */
# - Tags: keep numeric tags like `96 [s32]` and render as `96 /* [s32] */`.
# - Readability: add a blank line before `package ... {`; add a blank line after every assume/require;
#   and wrap long boolean expressions by inserting a newline after every `and` / `or`.
# - Sequent sugar `'->:'(A,B)` supported in parser and translated to `(A implies B)`.
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

def _wrap_and_or(expr: str, indent_cols: int = 14) -> str:
    """
    Insert a newline after each `and` / `or` to mimic the original style for readability.
    """
    indent = " " * indent_cols
    # surround with single spaces so we don't split identifiers
    expr = re.sub(r'\s+and\s+', f' and\n{indent}', expr)
    expr = re.sub(r'\s+or\s+',  f' or\n{indent}',  expr)
    return expr

def _doc_for_named(kind: str, name: Optional[str], desc: str) -> Optional[str]:
    """
    Build a concise doc for named assumptions/guarantees.
    kind ∈ {"assume","guarantee"}.
    """
    if not name and not desc:
        return None
    tag = f"[{kind.upper()} {name}]" if name else f"[{kind.upper()}]"
    text = f"{tag} {desc}".strip()
    return text

def collect_line_comment_blocks(gumbo_text: str) -> Dict[str, object]:
    """
    Walk raw GUMBO text and group consecutive // lines as a block.
    Anchor each block to the *next* significant token:
      - section headers: state, functions, initialize, integration, compute, compute_cases
      - case lines:      case <ID>
      - statements:      assume..., guarantee...
    We also track context to attach statement-comments to the right area:
      initialize / integration / compute top-level / compute cases (by id).
    """
    lines = gumbo_text.splitlines()
    out = {
        "header": None,
        "sections": {},       # e.g., { "state": "...", "functions": "..." }
        "cases": {},          # case-level doc by id
        "stmt_comments": {    # queued comments to prepend to the next stmt in order
            "integration": [],
            "initialize": [],
            "compute_toplevel": [],
            "cases": {}       # id -> [ comment, ... ]
        }
    }

    section = None  # "state" | "functions" | "initialize" | "integration" | "compute"
    in_compute_cases = False
    current_case: Optional[str] = None

    i = 0
    while i < len(lines):
        ln = lines[i]
        stripped = ln.strip()

        # Update section/case tracking on non-comment lines
        if stripped and not stripped.startswith("//"):
            if stripped.startswith("state"):
                section, in_compute_cases, current_case = "state", False, None
            elif stripped.startswith("functions"):
                section, in_compute_cases, current_case = "functions", False, None
            elif stripped.startswith("initialize"):
                section, in_compute_cases, current_case = "initialize", False, None
            elif stripped.startswith("integration"):
                section, in_compute_cases, current_case = "integration", False, None
            elif stripped.startswith("compute_cases"):
                section, in_compute_cases = "compute", True
                current_case = None
            elif stripped.startswith("compute"):
                section, in_compute_cases, current_case = "compute", False, None
            elif stripped.startswith("case "):
                m = re.match(r"case\s+([A-Za-z_][A-Za-z0-9_]*)\b", stripped)
                if m:
                    current_case = m.group(1)

        if stripped.startswith("//"):
            # collect this entire comment block
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

            # routing
            m_case = re.match(r"case\s+([A-Za-z_][A-Za-z0-9_]*)\b", anchor)
            if m_case:
                cid = m_case.group(1)
                prev = out["cases"].get(cid)
                out["cases"][cid] = (prev + "\n" if prev else "") + comment_text
            elif anchor.startswith("assume") or anchor.startswith("guarantee"):
                # Statement-level comments: append to next statement in the right bucket
                if section == "initialize":
                    out["stmt_comments"]["initialize"].append(comment_text)
                elif section == "integration":
                    out["stmt_comments"]["integration"].append(comment_text)
                elif section == "compute":
                    if in_compute_cases and current_case:
                        out["stmt_comments"]["cases"].setdefault(current_case, []).append(comment_text)
                    else:
                        out["stmt_comments"]["compute_toplevel"].append(comment_text)
                else:
                    # treat as header if we can't classify confidently
                    out["header"] = (out["header"] + "\n" if out["header"] else "") + comment_text
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
                out["header"] = (out["header"] + "\n" if out["header"] else "") + comment_text
            continue  # already advanced i

        i += 1

    return out

# ----------------------------- translation -----------------------------

def translate_monitor_gumbo(transformer: GumboTransformer,
                            part_name: str,
                            comments: Optional[Dict[str, object]] = None) -> str:
    """
    Build a SysMLv2 package named <part_name>_GUMBO_Contracts.

    - Emits `calc def` for helper functions with a doc signature.
    - Places section-comments and statement-comments near their targets.
    - For named statements, emits a combined doc: /* [ASSUME REQ_X] description... */
    - Wraps long boolean expressions by inserting a newline after 'and'/'or'.
    """
    comments = comments or {"header": None, "sections": {}, "cases": {}, "stmt_comments": {}}
    stmtq_integ: List[str] = list(comments.get("stmt_comments", {}).get("integration", []))
    stmtq_init:  List[str] = list(comments.get("stmt_comments", {}).get("initialize", []))
    stmtq_cbase: List[str] = list(comments.get("stmt_comments", {}).get("compute_toplevel", []))
    stmtq_cases: Dict[str, List[str]] = dict(comments.get("stmt_comments", {}).get("cases", {}))

    lines: List[str] = []

    # ──────────────────────────────────────────────────────────────
    # package header + imports
    # ──────────────────────────────────────────────────────────────
    lines.append(f"\npackage {part_name}_GUMBO_Contracts {{")
    lines.append("  import Isolette_Data_Model::*")
    lines.append("  import Base_Types::*")
    lines.append("")

    # header comments (if any)
    if comments.get("header"):
        for para in _collapse_doc(comments["header"]).splitlines():
            if para.strip():
                lines.append(f"  doc /*{para}*/")
        lines.append("")

    # ──────────────────────────────────────────────────────────────
    # state variables
    # ──────────────────────────────────────────────────────────────
    if comments["sections"].get("state"):
        for para in _collapse_doc(comments["sections"]["state"]).splitlines():
            lines.append(f"  doc /*{para}*/")
    for var, typ in transformer.state_vars:
        lines.append(f"  attribute {var}: {typ}")
    if transformer.state_vars:
        lines.append("")

    # ──────────────────────────────────────────────────────────────
    # helper functions  →  calc def
    # ──────────────────────────────────────────────────────────────
    if comments["sections"].get("functions"):
        for para in _collapse_doc(comments["sections"]["functions"]).splitlines():
            lines.append(f"  doc /*{para}*/")
    for func in transformer.helper_funcs:
        # func tuple: (name, return_type, body, params)
        if len(func) == 4:
            fname, rettype, body, params = func
        else:
            fname, rettype, body = func
            params = []
        lines.append(f"  calc def {fname} {{")
        if params:
            sig = f"{fname}(" + ", ".join(f"{p}:{t}" for p, t in params) + f"): {rettype}"
        else:
            sig = f"{fname}(): {rettype}"
        lines.append(f"    doc /*signature: {sig}*/")
        lines.append(f"    return {body}")
        lines.append("  }")
        lines.append("")

    # ──────────────────────────────────────────────────────────────
    # InitializeContract
    # ──────────────────────────────────────────────────────────────
    if transformer.initialize_guarantees:
        if comments["sections"].get("initialize"):
            for para in _collapse_doc(comments["sections"]["initialize"]).splitlines():
                lines.append(f"  doc /*{para}*/")
        lines.append("  requirement def InitializeContract {")
        for _k, name, desc, expr in transformer.initialize_guarantees:
            expr = expr.replace("->:", "->")
            docline = _doc_for_named("guarantee", name, desc)
            if docline:
                lines.append(f"    doc /*{_collapse_doc(docline)}*/")
            if stmtq_init:
                # attach statement-level comments immediately above this clause
                for para in _collapse_doc(stmtq_init.pop(0)).splitlines():
                    lines.append(f"    doc /*{para}*/")
            lines.append(f"    require constraint {{ {_wrap_and_or(expr)} }}")
            lines.append("")
        lines.append("  }")
        lines.append("")

    # ──────────────────────────────────────────────────────────────
    # MonitorIntegration
    # ──────────────────────────────────────────────────────────────
    if transformer.integration_assumes or transformer.guarantees:
        lines.append("  requirement def MonitorIntegration {")
        for _k, name, desc, expr in transformer.integration_assumes:
            expr = expr.replace("->:", "->")
            docline = _doc_for_named("assume", name, desc)
            if docline:
                lines.append(f"    doc /*{_collapse_doc(docline)}*/")
            if stmtq_integ:
                for para in _collapse_doc(stmtq_integ.pop(0)).splitlines():
                    lines.append(f"    doc /*{para}*/")
            lines.append(f"    assume constraint {{ {_wrap_and_or(expr)} }}")
            lines.append("")
        if transformer.guarantees:
            for _k, name, desc, expr in transformer.guarantees:
                expr = expr.replace("->:", "->")
                docline = _doc_for_named("guarantee", name, desc)
                if docline:
                    lines.append(f"    doc /*{_collapse_doc(docline)}*/")
                lines.append(f"    require constraint {{ {_wrap_and_or(expr)} }}")
                lines.append("")
        else:
            lines.append("    require constraint { true }")
            lines.append("")
        lines.append("  }")
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
                lines.append(f"  doc /*{para}*/")
        lines.append("  requirement def ComputeBase {")
        for _k, name, desc, expr in transformer.compute_toplevel_assumes:
            expr = expr.replace("->:", "->")
            docline = _doc_for_named("assume", name, desc)
            if docline:
                lines.append(f"    doc /*{_collapse_doc(docline)}*/")
            if stmtq_cbase:
                for para in _collapse_doc(stmtq_cbase.pop(0)).splitlines():
                    lines.append(f"    doc /*{para}*/")
            lines.append(f"    assume constraint {{ {_wrap_and_or(expr)} }}")
            lines.append("")
        for _k, name, desc, expr in transformer.compute_toplevel_guarantees:
            expr = expr.replace("->:", "->")
            docline = _doc_for_named("guarantee", name, desc)
            if docline:
                lines.append(f"    doc /*{_collapse_doc(docline)}*/")
            lines.append(f"    require constraint {{ {_wrap_and_or(expr)} }}")
            lines.append("")
        lines.append("  }")
        lines.append("")

    # ──────────────────────────────────────────────────────────────
    # Individual compute cases
    # ──────────────────────────────────────────────────────────────
    for case in transformer.compute_cases:
        cid   = case["id"]
        cdesc = case["description"]

        lines.append(f"  // compute case {cid}")
        lines.append(f"  requirement def ComputeCase_{cid} {{")
        if compute_base_exists:
            lines.append("    refines ComputeBase")
        # case-level comments (anchored to the 'case' line)
        if comments["cases"].get(cid):
            for para in _collapse_doc(comments['cases'][cid]).splitlines():
                lines.append(f"    doc /*{para}*/")
        if cdesc:
            lines.append(f"    doc /*{_collapse_doc(cdesc)}*/")

        # local queue of stmt comments to prepend in order
        case_queue = list(stmtq_cases.get(cid, []))

        # assumes
        for _k, name, desc, expr in case["assumes"]:
            expr = expr.replace("->:", "->")
            docline = _doc_for_named("assume", name, desc)
            if docline:
                lines.append(f"    doc /*{_collapse_doc(docline)}*/")
            if case_queue:
                for para in _collapse_doc(case_queue.pop(0)).splitlines():
                    lines.append(f"    doc /*{para}*/")
            lines.append(f"    assume constraint {{ {_wrap_and_or(expr)} }}")
            lines.append("")

        # guarantees
        for _k, name, desc, expr in case["guarantees"]:
            expr = expr.replace("->:", "->")
            docline = _doc_for_named("guarantee", name, desc)
            if docline:
                lines.append(f"    doc /*{_collapse_doc(docline)}*/")
            if case_queue:
                for para in _collapse_doc(case_queue.pop(0)).splitlines():
                    lines.append(f"    doc /*{para}*/")
            lines.append(f"    require constraint {{ {_wrap_and_or(expr)} }}")
            lines.append("")

        lines.append("  }")
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

# ----------------------------- CLI driver -----------------------------

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
        # Keep tags and comments; just trim surrounding whitespace
        clean = gumbo.strip()

        # collect comments for anchoring
        comment_map = collect_line_comment_blocks(clean)

        # parse
        tree = parser.parse(clean)
        tf = GumboTransformer()
        tf.transform(tree)

        # enclosing SysML part to prefix the contract package
        part = find_enclosing_part_name(text, start)

        sysml = translate_monitor_gumbo(tf, part, comments=comment_map)
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
    cli = argparse.ArgumentParser(description="Translate Gumbo to SysML v2")
    cli.add_argument("input", help="SysML file containing Gumbo blocks")
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

# Alias requested earlier
def GUMBY_CLI():
    main()

if __name__ == "__main__":
    main()
