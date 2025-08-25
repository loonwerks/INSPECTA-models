#!/usr/bin/env python3
# ========================================================================================================
# Gumbo → SysML v2 translator (inline, pure SysML output) — formatting-aligned
#
# Updates vs your previous version:
#   • Enum literals:   Namespace::EnumType.EnumValue  →  Namespace::EnumType::EnumValue  (constraints only)
#   • Subjects block:  print 'subject' once per requirement; align following lines without repeating 'subject'
#   • Grouping style:  remove redundant outer parens; pretty-print top-level AND/OR chains
#   • In(...) removal: replace In(x) with x in constraints
#
# Formatting goals (to match your “pattern file”):
#   - Nested per-part:        package <PartName>_GumboContract { … }
#   - Tabs for structure; spaces for column alignment
#   - Docs before constraints/subjects as in examples
#   - Initialize/Integration/ComputeBase/ComputeCase blocks use multi-line constraint braces:
#         require/assume constraint {
#             (term1) and
#             (term2)
#         }
#   - Typed numeric literals collected into comment lines:  // typed literals: 1 [s32], ...
# ========================================================================================================

import argparse
import os
import re
import subprocess
import sys
from typing import Dict, List, Tuple, Optional, Iterable, Set

from gumbo_parser import parser, GumboTransformer
from sysml_utils import extract_gumbo_blocks, find_enclosing_part_name, replace_blocks

# ----------------------------- formatting helpers -----------------------------

INDENT_UNIT = "\t"  # structural indent uses tabs (to mirror your examples)


def _collapse_doc(text: str) -> str:
    return text.replace("*/", "*\\/")


def _normalize_bools(s: str) -> str:
    # GUMBO sometimes emits T/F
    return re.sub(r'\bT\b', 'true', re.sub(r'\bF\b', 'false', s))


def _strip_in_calls(expr: str) -> str:
    # Replace legacy previous-value helper In(x) with x
    return re.sub(r'\bIn\s*\(\s*([^)]+?)\s*\)', r'\1', expr)


def _strip_numeric_type_annotations(expr: str) -> Tuple[str, List[str]]:
    """
    Collect 'typed' numeric literals and remove the inline annotation.
    Handles both '42__TAG__[s32]' and '42 [s32]' shapes.
    Returns (clean_expr, tags_list like ['42 [s32]', '1 [s32]']).
    """
    tags: List[str] = []

    def rep_tag(m: re.Match) -> str:
        tags.append(f"{m.group(1)} [{m.group(2)}]")
        return m.group(1)

    # 1) __TAG__ form
    expr = re.sub(r'(\d+(?:\.\d+)?)__TAG__\[([^\]]+)\]', lambda m: rep_tag(m), expr)

    # 2) Plain trailing [type] form with optional whitespace
    expr = re.sub(r'(\d+(?:\.\d+)?)\s*\[([^\]]+)\]', lambda m: rep_tag(m), expr)

    return expr, tags


_ENUM_FIX_RE = re.compile(
    r'(\b[A-Za-z_][A-Za-z0-9_:]*::[A-Za-z_][A-Za-z0-9_]*)\.([A-Za-z_][A-Za-z0-9_]*)'
)
def _fix_enum_literals(expr: str) -> str:
    """
    Convert Namespace::EnumType.EnumValue → Namespace::EnumType::EnumValue.
    Requires at least one '::' in the LHS to avoid touching variable.property uses like x.degrees.
    """
    return _ENUM_FIX_RE.sub(r'\1::\2', expr)


def _remove_outer_parens(s: str) -> str:
    """
    Remove redundant *outermost* parentheses that enclose the entire expression.
    Repeats until no such outer layer remains.
    """
    s_strip = s.strip()
    if not s_strip:
        return s_strip

    def is_wrapped(ss: str) -> bool:
        if not (ss.startswith("(") and ss.endswith(")")):
            return False
        depth = 0
        for i, ch in enumerate(ss):
            if ch == "(":
                depth += 1
            elif ch == ")":
                depth -= 1
                if depth == 0 and i != len(ss) - 1:
                    return False
        return True

    before = s_strip
    while is_wrapped(before):
        before = before[1:-1].strip()
    return before


def _split_top_level(expr: str) -> Tuple[List[str], List[str]]:
    """
    Split expression into top-level operands and operators (and/or).
    Returns (operands, ops) where len(ops) == len(operands) - 1.
    Only splits on top-level 'and'/'or' (not inside parentheses).
    """
    tokens: List[str] = []
    ops: List[str] = []

    depth = 0
    i = 0
    last = 0

    def is_word_at(pos: int, word: str) -> bool:
        return expr[pos:pos+len(word)] == word and \
               (pos == 0 or not expr[pos-1].isalnum()) and \
               (pos+len(word) == len(expr) or not expr[pos+len(word)].isalnum())

    while i < len(expr):
        ch = expr[i]
        if ch == '(':
            depth += 1
            i += 1
            continue
        if ch == ')':
            depth = max(0, depth - 1)
            i += 1
            continue

        if depth == 0:
            if is_word_at(i, "and"):
                tokens.append(expr[last:i].strip())
                ops.append("and")
                i += 3
                last = i
                continue
            if is_word_at(i, "or"):
                tokens.append(expr[last:i].strip())
                ops.append("or")
                i += 2
                last = i
                continue
        i += 1

    tokens.append(expr[last:].strip())
    return tokens, ops


def _parenthesize_if_needed(t: str) -> str:
    t = t.strip()
    if not t:
        return t
    if t.startswith("(") and t.endswith(")"):
        # Quick check that it's at least balanced; if not, wrap it.
        depth = 0
        for i, ch in enumerate(t):
            if ch == "(":
                depth += 1
            elif ch == ")":
                depth -= 1
                if depth == 0 and i != len(t) - 1:
                    # Outermost paren closed before end — not a single wrapper
                    return f"({t})"
        return t
    return f"({t})"


def _pretty_bool_expr(expr: str, indent: str) -> List[str]:
    """
    Produce a list of lines for a constraint block, with:
      - redundant outer parens removed
      - top-level 'and/or' chains split one per line
      - each operand wrapped in parentheses
    """
    e = _remove_outer_parens(expr)
    operands, ops = _split_top_level(e)

    # If no top-level boolean ops, just return one line (parentheses optional here).
    if len(operands) == 1:
        return [operands[0]]

    lines: List[str] = []
    for idx, term in enumerate(operands):
        lhs = _parenthesize_if_needed(term)
        if idx < len(ops):
            lines.append(f"{lhs} {ops[idx]}")
        else:
            lines.append(lhs)
    return lines


def _doc_lines_block(text: Optional[str], indent: str) -> List[str]:
    if not text:
        return []
    out: List[str] = []
    for ln in _collapse_doc(text).splitlines():
        ln = ln.strip()
        if ln:
            out.append(f"{indent}doc /*{ln}*/")
    return out


def _name_desc_docs(name: Optional[str], desc: str, indent: str) -> List[str]:
    """
    Render a compact one-line doc similar to: doc /*ID <name> <desc>*/.
    """
    tag = None
    n = (name or "").strip()
    d = (desc or "").strip()
    if n and d:
        tag = f"ID {n} {d}"
    elif n:
        tag = f"ID {n}"
    elif d:
        tag = f"ID {d}"
    return [f"{indent}doc /*{_collapse_doc(tag)}*/"] if tag else []


def _indent_block(s: str, prefix: str) -> str:
    return "\n".join((prefix + ln) if ln else ln for ln in s.splitlines())


# ----------------------------- part/port introspection -----------------------------

def _find_part_body_span(text: str, part_name: str) -> Optional[Tuple[int, int]]:
    m = re.search(rf'\bpart\s+def\s+{re.escape(part_name)}\b', text)
    if not m:
        return None
    i = text.find('{', m.end())
    if i == -1:
        return None
    depth = 0
    for j in range(i, len(text)):
        c = text[j]
        if c == '{':
            depth += 1
        elif c == '}':
            depth -= 1
            if depth == 0:
                return (i, j)
    return None


_PORT_TYPE_RE = re.compile(
    r'\b(?:in|out)\s+port\s+([A-Za-z_][A-Za-z0-9_]*)\s*:\s*DataPort\s*\{[^{}]*?type\s*:\s*'
    r'([A-Za-z_][A-Za-z0-9_]*(?:::[A-Za-z_][A-Za-z0-9_]*)*)\s*;\s*\}',
    re.DOTALL
)

def _get_part_ports(text: str, part_name: str) -> Dict[str, str]:
    span = _find_part_body_span(text, part_name)
    if not span:
        return {}
    body = text[span[0]:span[1]+1]
    out: Dict[str, str] = {}
    for m in _PORT_TYPE_RE.finditer(body):
        name, typ = m.group(1), m.group(2)
        out[name] = typ
    return out


# ----------------------------- subjects (ports + state) -----------------------------

_SKIP: Set[str] = {
    'true','false','and','or','xor','not','implies',
    'In',  # legacy helper, handled separately
    'Isolette_Data_Model','Base_Types','AADL','AADL_Project','HAMR',
    'Supported_Dispatch_Protocols','CASE_Scheduling'
}
_ID_RE = re.compile(r'\b([A-Za-z_][A-Za-z0-9_]*)\b')

def _collect_identifiers(exprs: Iterable[str]) -> List[str]:
    seen: Dict[str, None] = {}
    for e in exprs:
        for t in _ID_RE.findall(e or ""):
            if t in _SKIP:      # ignore keywords/namespaces/helpers
                continue
            if re.fullmatch(r'[A-Z][A-Z0-9_]*', t):
                continue
            if t not in seen:
                seen[t] = None
    return list(seen.keys())


def _collect_subject_bindings(
    exprs: Iterable[str],
    part_ports: Dict[str, str],
    state_types: Dict[str, str]
) -> List[Tuple[str, str]]:
    names = _collect_identifiers(exprs)
    binds: List[Tuple[str,str]] = []
    for n in names:
        ty = part_ports.get(n) or state_types.get(n)
        if ty:
            binds.append((n, ty))
    return binds


def _emit_subjects_group(lines: List[str], indent: str, bindings: List[Tuple[str,str]]) -> None:
    """
    Emit a 'subject' block with the keyword only once, and aligned continuations:
        subject a : TypeA;
                bb: TypeB;
                 c: TypeC;
    """
    if not bindings:
        return
    maxn = max(len(n) for n,_ in bindings)
    subject_prefix = "subject "
    for idx, (n, t) in enumerate(bindings):
        pad = " " * (maxn - len(n) + 1)  # at least one space before colon
        if idx == 0:
            lines.append(f"{indent}{subject_prefix}{n}{pad}: {t};")
        else:
            lines.append(f"{indent}{' ' * len(subject_prefix)}{n}{pad}: {t};")
    lines.append("")  # blank line after subjects group


# ----------------------------- comment harvesting -----------------------------

def collect_line_comment_blocks(gumbo_text: str) -> Dict[str, object]:
    """
    Group consecutive '//' comments and anchor them to sections/cases,
    preserving your preferred banner docs.
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
            # find the anchor line
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


# ----------------------------- Indenter -----------------------------

class Indent:
    def __init__(self, base: str = ""):
        self.base = base
        self.level = 0
        self.lines: List[str] = []

    def push(self):
        self.level += 1

    def pop(self):
        self.level = max(0, self.level - 1)

    def emit(self, s: str = ""):
        if s:
            self.lines.append(self.base + (INDENT_UNIT * self.level) + s)
        else:
            self.lines.append(s)

    def text(self) -> str:
        return "\n".join(self.lines)


# ----------------------------- core emitter -----------------------------

def _prep_expr(expr: str) -> Tuple[str, List[str]]:
    """
    Normalize one expression: booleans, In(...), enum dots, strip typed tags.
    Returns (clean_expr, typed_tag_list).
    """
    e = _normalize_bools(expr or "")
    e = _strip_in_calls(e)
    e = _fix_enum_literals(e)
    e, tags = _strip_numeric_type_annotations(e)
    return e, tags


def _emit_constraint(ind: Indent, kind: str, expr: str) -> None:
    """
    kind: 'assume' or 'require'
    """
    ind.emit(f"{kind} constraint {{")
    ind.push()
    for ln in _pretty_bool_expr(expr, INDENT_UNIT * ind.level):
        ind.emit(ln)
    ind.pop()
    ind.emit("}")
    ind.emit("")


def translate_block_to_package(part_name: str,
                               tf: GumboTransformer,
                               part_ports: Dict[str, str],
                               comments: Dict[str, object] | None = None) -> str:
    comments = comments or {"header": None, "sections": {}, "cases": {}}
    state_types: Dict[str, str] = {n: t for (n, t) in (tf.state_vars or [])}

    ind = Indent()

    # package open
    ind.emit(f"package {part_name}_GumboContract {{")
    ind.push()

    # package-section docs (functions/init/compute) before attributes/calc/reqs
    for sec in ("functions", "initialize", "compute"):
        if comments["sections"].get(sec):
            for dl in _doc_lines_block(comments["sections"][sec], ind.base + INDENT_UNIT * ind.level):
                ind.emit(dl[len(ind.base + INDENT_UNIT * ind.level):])

    # state as attributes
    if tf.state_vars:
        for var, typ in tf.state_vars:
            ind.emit(f"attribute {var}: {typ};")
        ind.emit("")

    # functions as calc defs
    for (fname, _rettype, body, params) in tf.helper_funcs:
        body_norm = _normalize_bools(body or "true")
        body_norm = _fix_enum_literals(_strip_in_calls(body_norm))
        body_norm, tags = _strip_numeric_type_annotations(body_norm)

        ind.emit(f"calc def {fname} {{")
        ind.push()
        for p, t in params:
            ind.emit(f"in attribute {p}: {t};")
        if tags:
            ind.emit(f"// typed literals: {', '.join(tags)}")
        # calc body is a single expression; keep it in one line if small
        for ln in _pretty_bool_expr(body_norm, INDENT_UNIT * ind.level):
            ind.emit(ln)
        ind.pop()
        ind.emit("}")
        ind.emit("")

    # ------- InitializeContract -------
    if tf.initialize_guarantees:
        init_exprs = [e for (_k,_n,_d,e) in tf.initialize_guarantees]
        bindings = _collect_subject_bindings(init_exprs, part_ports, state_types)

        ind.emit("requirement def InitializeContract {")
        ind.push()

        # subjects first
        _emit_subjects_group(ind.lines, ind.base + INDENT_UNIT * ind.level, bindings)

        # each guarantee: doc + (typed literals comment) + require block
        for _k, name, desc, expr in tf.initialize_guarantees:
            for dl in _name_desc_docs(name, desc, ind.base + INDENT_UNIT * ind.level):
                ind.emit(dl[len(ind.base + INDENT_UNIT * ind.level):])
            ec, tags = _prep_expr(expr)
            if tags:
                ind.emit(f"// typed literals: {', '.join(tags)}")
            _emit_constraint(ind, "require", ec)

        ind.pop()
        ind.emit("")

        ind.emit("}")

        ind.emit("")

    # ------- IntegrationContract -------
    if tf.integration_assumes or tf.integration_requires:
        exprs = [e for (_k,_n,_d,e) in tf.integration_assumes] + \
                [e for (_k,_n,_d,e) in tf.integration_requires]
        bindings = _collect_subject_bindings(exprs, part_ports, state_types)

        ind.emit("requirement def IntegrationContract {")
        ind.push()

        # Docs BEFORE subjects (as in your pattern)
        for src in (tf.integration_assumes, tf.integration_requires):
            for _k, name, desc, _ in src:
                for dl in _name_desc_docs(name, desc, ind.base + INDENT_UNIT * ind.level):
                    ind.emit(dl[len(ind.base + INDENT_UNIT * ind.level):])

        ind.emit("")
        _emit_subjects_group(ind.lines, ind.base + INDENT_UNIT * ind.level, bindings)

        for _k, name, desc, expr in tf.integration_assumes:
            ec, tags = _prep_expr(expr)
            if tags:
                ind.emit(f"// typed literals: {', '.join(tags)}")
            _emit_constraint(ind, "require", ec)

        for _k, name, desc, expr in tf.integration_requires:
            ec, tags = _prep_expr(expr)
            if tags:
                ind.emit(f"// typed literals: {', '.join(tags)}")
            _emit_constraint(ind, "require", ec)

        ind.pop()
        ind.emit("}")
        ind.emit("")

    # ------- ComputeBase (top-level compute assumes/guarantees) -------
    compute_base_exists = bool(tf.compute_toplevel_assumes or tf.compute_toplevel_guarantees)
    if compute_base_exists:
        exprs = [e for (_k,_n,_d,e) in tf.compute_toplevel_assumes] + \
                [e for (_k,_n,_d,e) in tf.compute_toplevel_guarantees]
        bindings = _collect_subject_bindings(exprs, part_ports, state_types)

        ind.emit("doc /*======  C o m p u t e     C o n s t r a i n t s =====*/")
        ind.emit("requirement def ComputeBase {")
        ind.push()

        _emit_subjects_group(ind.lines, ind.base + INDENT_UNIT * ind.level, bindings)

        for _k, name, desc, expr in tf.compute_toplevel_assumes:
            for dl in _name_desc_docs(name, desc, ind.base + INDENT_UNIT * ind.level):
                ind.emit(dl[len(ind.base + INDENT_UNIT * ind.level):])
            ec, tags = _prep_expr(expr)
            if tags:
                ind.emit(f"// typed literals: {', '.join(tags)}")
            _emit_constraint(ind, "assume", ec)

        for _k, name, desc, expr in tf.compute_toplevel_guarantees:
            for dl in _name_desc_docs(name, desc, ind.base + INDENT_UNIT * ind.level):
                ind.emit(dl[len(ind.base + INDENT_UNIT * ind.level):])
            ec, tags = _prep_expr(expr)
            if tags:
                ind.emit(f"// typed literals: {', '.join(tags)}")
            _emit_constraint(ind, "require", ec)

        ind.pop()
        ind.emit("}")
        ind.emit("")

    # ------- Compute cases -------
    for case in tf.compute_cases:
        cid = case["id"]; cdesc = case["description"]
        exprs = [e for (_k,_n,_d,e) in case["assumes"]] + \
                [e for (_k,_n,_d,e) in case["guarantees"]]
        bindings = _collect_subject_bindings(exprs, part_ports, state_types)

        if compute_base_exists:
            ind.emit(f"requirement def ComputeCase_{cid} specializes ComputeBase {{")
        else:
            ind.emit(f"requirement def ComputeCase_{cid} {{")
        ind.push()

        # case description first
        if cdesc:
            ind.emit(f"doc /*{_collapse_doc(cdesc)}*/")

        _emit_subjects_group(ind.lines, ind.base + INDENT_UNIT * ind.level, bindings)

        # assumptions first
        for _k, name, desc, expr in case["assumes"]:
            for dl in _name_desc_docs(name, desc, ind.base + INDENT_UNIT * ind.level):
                ind.emit(dl[len(ind.base + INDENT_UNIT * ind.level):])
            ec, tags = _prep_expr(expr)
            if tags:
                ind.emit(f"// typed literals: {', '.join(tags)}")
            _emit_constraint(ind, "assume", ec)

        # guarantees
        for _k, name, desc, expr in case["guarantees"]:
            for dl in _name_desc_docs(name, desc, ind.base + INDENT_UNIT * ind.level):
                ind.emit(dl[len(ind.base + INDENT_UNIT * ind.level):])
            ec, tags = _prep_expr(expr)
            if tags:
                ind.emit(f"// typed literals: {', '.join(tags)}")
            _emit_constraint(ind, "require", ec)

        ind.pop()
        ind.emit("}")
        ind.emit("")

    # package close
    ind.pop()
    ind.emit("}")

    return ind.text()


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


# ----------------------------- main driver -----------------------------

def process_sysml_file(path: str,
                       *,
                       show: bool = False,
                       no_model_validate: bool = False) -> None:
    text = open(path, encoding="utf-8").read()
    blocks = extract_gumbo_blocks(text)
    if not blocks:
        print("No GUMBO contracts found.")
        return

    replacements: List[Tuple[int, int, str]] = []

    for start, end, gumbo in blocks:
        clean = gumbo.strip()

        # attach nearby '//' comments by section/case
        comment_map = collect_line_comment_blocks(clean)

        # parse GUMBO
        tree = parser.parse(clean)
        tf = GumboTransformer()
        tf.transform(tree)

        # locate enclosing part + port types for subjects
        part = find_enclosing_part_name(text, start)
        part_ports = _get_part_ports(text, part)

        # emit the nested contracts package
        pkg = translate_block_to_package(part, tf, part_ports, comments=comment_map)

        # preserve the original leading whitespace where the GUMBO block started
        indent_match = re.match(r'[ \t]*', text[text.rfind('\n', 0, start) + 1:start])
        left = indent_match.group(0) if indent_match else ""
        pkg_indented = _indent_block(pkg, left)

        # replace the GUMBO block
        replacements.append((start, end, pkg_indented + "\n"))

    # write the translated model
    new_text = replace_blocks(text, replacements)
    out_path = path.rsplit(".", 1)[0] + ".translated.sysml"
    with open(out_path, "w", encoding="utf-8") as fh:
        fh.write(new_text)
    print(f"Translated file written to {out_path}")

    print("\n=== Translated SysML v2 (inline contracts) ===\n")
    if show:
        print(new_text)

    if not no_model_validate:
        print("\n=== ANTLR validate: model ===")
        validate_sysml_antlr(out_path, verbose=True)


def main() -> None:
    cli = argparse.ArgumentParser(
        prog="GUMBY_CLI",
        description="Gumbo → SysML v2 translator (inline, pure SysML output, alignment-accurate)"
    )
    cli.add_argument("input", help="SysML file containing GUMBO blocks")
    cli.add_argument("--show", action="store_true", help="Print translated model")
    cli.add_argument("--no-model-validate", action="store_true", help="Skip ANTLR validation of the .translated.sysml")
    args = cli.parse_args()

    process_sysml_file(
        args.input,
        show=args.show,
        no_model_validate=args.no_model_validate
    )

if __name__ == "__main__":
    main()
