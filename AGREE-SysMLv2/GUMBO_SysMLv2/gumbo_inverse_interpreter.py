#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# ========================================================================================================
# @author: Amer N. Tahat,
# Collins Aerospace,
# August 2025.
# ========================================================================================================
"""
GUMBO Inverse Interpreter
-------------------------
Reconstructs original GUMBO 'language "GUMBO"' blocks from nested
'<Part>_GumboContract' packages and reinserts them in the corresponding
part definitions, removing the packages.

Outputs reconstructed models under a SysML directory (default './SysML')
with filenames like '<input>_reconstructed_with_GUMBO.sysml'.

Optionally runs:
  sireum hamr sysml logika --sourcepath <sysml_dir>

Key behaviors:
- Maps package 'attribute' → GUMBO 'state'.
- Rebuilds 'functions' from 'calc def' blocks:
    def NAME(p1:T1, p2:T2):TRet := <expr>;
  where parameters come from `in [attribute] <name> : <Type>;`
  and return type from `out [attribute] <name> : <Type>;`.
  If return is missing, infer: boolean-like expr → Base_Types::Boolean,
  else first parameter’s type.
- initialize/compute/integration sections produced from requirement blocks.
- Case docs rendered; empty descriptions never produce "".
- Enum values Namespace::EnumType::Value rendered as Namespace::EnumType.Value.
- 'pre_<Name>' tokens rewritten to 'In(Name)'.

Author: (you)
"""

from __future__ import annotations

import argparse
import os
import re
import shutil
import subprocess
from typing import List, Tuple, Optional


# ----------------------------- low level helpers -----------------------------

def read_text(p: str) -> str:
    with open(p, "r", encoding="utf-8") as fh:
        return fh.read()


def write_text(p: str, s: str) -> None:
    os.makedirs(os.path.dirname(p), exist_ok=True)
    with open(p, "w", encoding="utf-8") as fh:
        fh.write(s)


def find_matching_brace(s: str, start_brace_idx: int) -> int:
    """Given s and the index of '{', return index of its matching '}' (inclusive)."""
    depth = 0
    for i in range(start_brace_idx, len(s)):
        if s[i] == '{':
            depth += 1
        elif s[i] == '}':
            depth -= 1
            if depth == 0:
                return i
    return -1


def prev_line_indent(s: str, pos: int) -> str:
    """Return the leading whitespace of the line that contains pos."""
    bol = s.rfind("\n", 0, pos) + 1
    eol = s.find("\n", pos)
    if eol == -1:
        eol = len(s)
    line = s[bol:eol]
    return re.match(r"[ \t]*", line).group(0)


def detab(ws: str, tabsize: int = 4) -> str:
    """Convert tabs in a whitespace prefix to spaces."""
    return ws.replace("\t", " " * tabsize)


# ----------------------------- enum & token fixers -----------------------------

_ENUM_BACK_RE = re.compile(r"(\b[A-Za-z_][A-Za-z0-9_:]*::[A-Za-z_][A-Za-z0-9_]*)::([A-Za-z_][A-Za-z0-9_]*)")
PRE_PREFIX_RE = re.compile(r"\bpre_([A-Za-z_][A-Za-z0-9_]*)\b")

def enums_sysml_to_gumbo(expr: str) -> str:
    """Namespace::EnumType::EnumValue → Namespace::EnumType.EnumValue."""
    return _ENUM_BACK_RE.sub(r"\1.\2", expr)

def rewrite_pre_tokens(expr: str) -> str:
    """Convert tokens like 'pre_lastCmd' to 'In(lastCmd)'."""
    return PRE_PREFIX_RE.sub(r"In(\1)", expr)


# ----------------------------- typed literal re-insertion -----------------------------

def parse_typed_literals_comment(line: str) -> List[Tuple[str, str]]:
    """
    Parse a comment line like: // typed literals: 96 [s32], 101 [s32]
    -> [('96','s32'), ('101','s32')]
    """
    m = re.search(r"typed\s+lit(?:eral)?s?:\s*(.*)$", line.strip(), re.IGNORECASE)
    if not m:
        return []
    payload = m.group(1)
    out: List[Tuple[str, str]] = []
    for piece in re.split(r"\s*,\s*", payload):
        piece = piece.strip()
        mm = re.match(r"(\d+(?:\.\d+)?)\s*\[([^\]]+)\]", piece)
        if mm:
            out.append((mm.group(1), mm.group(2)))
    return out


def reinsert_typed_literals(expr: str, tags: List[Tuple[str, str]]) -> str:
    """
    Given an expression and a list of (number, type) pairs, insert ' [type]' after
    the first occurrence of each number in order, using whole-number matching.
    """
    result = expr
    for num, ty in tags:
        pat = re.compile(rf"(?<![\w.])({re.escape(num)})(?![\w.])")
        result, _ = pat.subn(rf"\1 [{ty}]", result, count=1)
    return result


# ----------------------------- package / requirement parsing -----------------------------

REQ_BLOCK_RE = re.compile(r"\brequirement\s+def\s+([A-Za-z_][A-Za-z0-9_]*)\b[^{]*\{", re.MULTILINE)
PKG_RE = re.compile(r"\bpackage\s+([A-Za-z_][A-Za-z0-9_]*)\s*\{", re.MULTILINE)
CALC_RE = re.compile(r"\bcalc\s+def\s+([A-Za-z_][A-Za-z0-9_]*)\s*\{", re.MULTILINE)
ATTR_RE = re.compile(r"\battribute\s+([A-Za-z_][A-Za-z0-9_]*)\s*:\s*([^;]+)\s*;", re.MULTILINE)
DOC_RE = re.compile(r"^\s*doc\s*/\*(.*?)\*/\s*$", re.MULTILINE | re.DOTALL)

# Declarations inside calc-body
IN_DECL_NAME_TYPE_RE  = re.compile(r"^\s*in\s+(?:attribute\s+)?([A-Za-z_][A-Za-z0-9_]*)\s*:\s*([^;]+)\s*;\s*$", re.MULTILINE)
OUT_DECL_TYPE_RE      = re.compile(r"^\s*out\s+(?:attribute\s+)?[A-Za-z_][A-Za-z0-9_]*\s*:\s*([^;]+)\s*;\s*$", re.MULTILINE)
DECL_LINE_RE          = re.compile(r"^\s*(?:in|out)\s+(?:attribute\s+)?[A-Za-z_][A-Za-z0-9_]*\s*:\s*[^;]+;\s*$", re.MULTILINE)

TYPED_LIT_LINE_RE = re.compile(r"^\s*//\s*typed\s+lit", re.IGNORECASE)
ASSUME_OPEN_RE = re.compile(r"\bassume\s+constraint\s*\{", re.IGNORECASE)
REQUIRE_OPEN_RE = re.compile(r"\brequire\s+constraint\s*\{", re.IGNORECASE)


class ReqItem:
    def __init__(self, kind: str, expr: str, typed_tags: List[Tuple[str, str]]):
        self.kind = kind  # 'assume' or 'require'
        self.expr = expr
        self.typed_tags = typed_tags


class Requirement:
    def __init__(self, name: str, body: str):
        self.name = name
        self.body = body
        self.docs: List[str] = []
        self.items: List[ReqItem] = []

    def parse(self):
        # Collect non-empty docs
        for m in DOC_RE.finditer(self.body):
            d = " ".join(ln.strip() for ln in m.group(1).splitlines())
            if d:
                self.docs.append(d)

        # Bind typed-literal comment tags to the NEXT assume/require
        i = 0
        tags_queue: List[Tuple[str, str]] = []
        b = self.body
        while i < len(b):
            if b.startswith("//", i):
                eol = b.find("\n", i)
                if eol == -1:
                    eol = len(b)
                line = b[i:eol]
                ttags = parse_typed_literals_comment(line)
                if ttags:
                    tags_queue.extend(ttags)
                i = eol + 1
                continue

            ma = ASSUME_OPEN_RE.search(b, i)
            mr = REQUIRE_OPEN_RE.search(b, i)
            cand = [(ma, "assume"), (mr, "require")]
            cand = [(m, k) for (m, k) in cand if m]
            if not cand:
                break
            m, kind = min(cand, key=lambda mk: mk[0].start())

            open_brace = b.find("{", m.start())
            close = find_matching_brace(b, open_brace)
            if open_brace == -1 or close == -1:
                break
            inner = b[open_brace + 1:close].strip()
            i = close + 1

            self.items.append(ReqItem(kind, inner, tags_queue))
            tags_queue = []

        return self


# ----------------------------- formatting helpers -----------------------------

BOOL_OPS_RE = re.compile(r"\b(not|and|or|implies|True|False)\b|==|!=|<=|>=|<|>")

def looks_boolean(expr: str) -> bool:
    return BOOL_OPS_RE.search(expr) is not None

def infer_return_type(param_types: List[str], explicit_ret: Optional[str], expr: str) -> str:
    """
    If 'explicit_ret' is provided, use it. Otherwise:
      - If expr looks boolean → Base_Types::Boolean
      - Else if there is at least one parameter → first parameter's type
      - Else → Base_Types::Boolean (fallback)
    """
    if explicit_ret and explicit_ret.strip():
        return explicit_ret.strip()
    if looks_boolean(expr):
        return "Base_Types::Boolean"
    if param_types:
        return param_types[0].strip()
    return "Base_Types::Boolean"

def _split_doc_id(doc: str) -> Tuple[str, str]:
    """
    Parse 'ID REQ_1 some text |http://...' -> ('REQ_1', 'some text | http://...').
    If no leading 'ID', returns ('', doc). Empty docs -> ('','').
    """
    d = doc.strip()
    if not d:
        return "", ""
    m = re.match(r"ID\s+([^\s]+)\s*(.*)$", d, flags=re.IGNORECASE)
    if not m:
        return "", d
    name = m.group(1).strip()
    rest = re.sub(r"\s*\|\s*", " | ", m.group(2).strip())
    return name, rest

def _normalize_expr(expr: str, typed_tags: List[Tuple[str,str]]) -> str:
    expr = re.sub(r"\s+", " ", expr.strip())
    if typed_tags:
        expr = reinsert_typed_literals(expr, typed_tags)
    expr = enums_sysml_to_gumbo(expr)
    expr = rewrite_pre_tokens(expr)
    return expr

def _emit_with_optional_desc(indent: str, kind: str, label: str, desc: str, expr: str) -> str:
    """
    If desc is empty, do not print "".
    """
    if label:
        if desc:
            return f'{indent}{kind} {label} "{desc}": {expr};'
        else:
            return f"{indent}{kind} {label}: {expr};"
    else:
        return f"{indent}{kind} {expr};"


# ----------------------------- core model -----------------------------

class GumboPackage:
    def __init__(self, part_name: str):
        self.part_name = part_name
        self.state_attrs: List[Tuple[str, str]] = []  # [(name, type)]
        # functions: (name, [(param_name, param_type), ...], explicit_return_type, expr)
        self.functions: List[Tuple[str, List[Tuple[str, str]], Optional[str], str]] = []
        self.init_req: Optional[Requirement] = None
        self.integration_req: Optional[Requirement] = None
        self.compute_base: Optional[Requirement] = None
        self.compute_cases: List[Requirement] = []

    def to_gumbo(self, indent: str = "        ") -> str:
        """
        Build the GUMBO block text using 4-space indentation.
        'indent' is the left margin for the 'language "GUMBO"' line.
        """
        IND = "    "  # 4 spaces
        L0 = indent
        L1 = L0 + IND
        L2 = L1 + IND
        L3 = L2 + IND
        L4 = L3 + IND

        lines: List[str] = []
        lines.append(f'{L0}language "GUMBO" /*{{')

        # STATE
        if self.state_attrs:
            lines.append("")
            lines.append(f"{L1}state")
            for n, t in self.state_attrs:
                lines.append(f"{L2}{n}: {t};")

        # FUNCTIONS
        if self.functions:
            lines.append("")
            lines.append(f"{L1}functions")
            for fname, params, ret_ty_explicit, expr in self.functions:
                expr = enums_sysml_to_gumbo(expr.strip())
                expr = rewrite_pre_tokens(expr)
                # Build "name:type" pairs exactly (no spaces around ':', comma+space between params)
                arg_pairs = [f"{n.strip()}:{t.strip()}" for (n, t) in params]
                args_str = ", ".join(arg_pairs)
                # Infer return type if necessary
                ret_ty = infer_return_type([t for (_, t) in params], ret_ty_explicit, expr)
                # Requested spacing: no space before ':ReturnType', but spaces around ':='
                lines.append(f"{L2}def {fname}({args_str}):{ret_ty} := {expr};")

        # INITIALIZE
        if self.init_req:
            lines.append("")
            lines.append(f"{L1}initialize")
            doc_iter = iter(self.init_req.docs)
            for item in self.init_req.items:
                doc = next(doc_iter, "")
                label, desc = _split_doc_id(doc)
                expr = _normalize_expr(item.expr, item.typed_tags)
                lines.append(_emit_with_optional_desc(L2, "assume" if item.kind == "assume" else "guarantee",
                                                     label, desc, expr))

        # INTEGRATION
        if self.integration_req:
            lines.append("")
            lines.append(f"{L1}integration")
            doc_iter = iter(self.integration_req.docs)
            for item in self.integration_req.items:
                doc = next(doc_iter, "")
                label, desc = _split_doc_id(doc)
                expr = _normalize_expr(item.expr, item.typed_tags)
                lines.append(_emit_with_optional_desc(L2, "guarantee", label, desc, expr))

        # COMPUTE (base + cases)
        if self.compute_base or self.compute_cases:
            lines.append("")
            lines.append(f"{L1}compute")

            if self.compute_base:
                doc_iter = iter(self.compute_base.docs)
                for item in self.compute_base.items:
                    doc = next(doc_iter, "")
                    label, desc = _split_doc_id(doc)
                    expr = _normalize_expr(item.expr, item.typed_tags)
                    lines.append(_emit_with_optional_desc(L2, "assume" if item.kind == "assume" else "guarantee",
                                                         label, desc, expr))

            if self.compute_cases:
                lines.append("")
                lines.append(f"{L2}compute_cases")
                for case in self.compute_cases:
                    cid = case.name
                    if cid.startswith("ComputeCase_"):
                        cid = cid[len("ComputeCase_"):]
                    case_desc = case.docs[0] if case.docs else ""
                    if case_desc:
                        lines.append(f'{L3}case {cid} "{case_desc}" :')
                    else:
                        lines.append(f"{L3}case {cid} :")
                    for it in case.items:
                        expr = _normalize_expr(it.expr, it.typed_tags)
                        if it.kind == "assume":
                            lines.append(f"{L4}assume {expr};")
                        else:
                            lines.append(f"{L4}guarantee {expr};")

        lines.append(f"{L0}}}*/")
        return "\n".join(lines)


# ----------------------------- parsing & assembly -----------------------------

def parse_calc_defs(pkg_text: str) -> List[Tuple[str, List[Tuple[str, str]], Optional[str], str]]:
    """
    Parse 'calc def <name> { ... }' blocks and return:
        (name, [(param_name, param_type), ...], return_type_or_None, expr)
    """
    out: List[Tuple[str, List[Tuple[str, str]], Optional[str], str]] = []
    for m in CALC_RE.finditer(pkg_text):
        name = m.group(1)
        start = m.end() - 1  # at '{'
        close = find_matching_brace(pkg_text, start)
        if close == -1:
            continue
        inner = pkg_text[m.end():close]

        # parameter (name,type) pairs and return type
        params = [(n.strip(), t.strip()) for (n, t) in IN_DECL_NAME_TYPE_RE.findall(inner)]
        ret_type_m = OUT_DECL_TYPE_RE.search(inner)
        ret_type = ret_type_m.group(1).strip() if ret_type_m else None

        # expression body (remove in/out decls and typed-literal comments)
        expr_lines: List[str] = []
        for ln in inner.splitlines():
            s = ln.strip()
            if not s:
                continue
            if DECL_LINE_RE.match(ln) or TYPED_LIT_LINE_RE.match(ln):
                continue
            expr_lines.append(s)

        expr = " ".join(expr_lines).strip()
        expr = re.sub(r";\s*$", "", expr)  # drop trailing semicolon if present

        out.append((name, params, ret_type, expr))
    return out


def parse_requirements(pkg_text: str) -> List[Requirement]:
    out: List[Requirement] = []
    for m in REQ_BLOCK_RE.finditer(pkg_text):
        name = m.group(1)
        start = m.end() - 1  # at '{'
        close = find_matching_brace(pkg_text, start)
        if close == -1:
            continue
        body = pkg_text[m.end():close]
        req = Requirement(name, body)
        req.parse()
        out.append(req)
    return out


def parse_gumbo_package(part_pkg_name: str, pkg_text: str) -> GumboPackage:
    """
    part_pkg_name: e.g., 'Operator_Interface_Thread_i_GumboContract'
    pkg_text: full package text (from 'package ... {' to matching '}')
    """
    # derive part name
    part_name = part_pkg_name[:-len("_GumboContract")] if part_pkg_name.endswith("_GumboContract") else part_pkg_name
    g = GumboPackage(part_name=part_name)

    # state attributes
    for m in ATTR_RE.finditer(pkg_text):
        g.state_attrs.append((m.group(1).strip(), m.group(2).strip()))

    # functions (calc def)
    g.functions = parse_calc_defs(pkg_text)

    # requirements
    for req in parse_requirements(pkg_text):
        if req.name == "InitializeContract":
            g.init_req = req
        elif req.name == "IntegrationContract":
            g.integration_req = req
        elif req.name == "ComputeBase":
            g.compute_base = req
        elif req.name.startswith("ComputeCase_"):
            g.compute_cases.append(req)
        else:
            g.compute_cases.append(req)

    return g


# ----------------------------- model rewriting -----------------------------

def find_part_body_span(text: str, part_name: str) -> Optional[Tuple[int, int]]:
    """Return (start_brace_idx, end_brace_idx) of the 'part def <part_name> { ... }' body."""
    m = re.search(rf"\bpart\s+def\s+{re.escape(part_name)}\b[^\{{]*\{{", text)
    if not m:
        return None
    start_brace = text.find("{", m.end() - 1)
    if start_brace == -1:
        return None
    end_brace = find_matching_brace(text, start_brace)
    if end_brace == -1:
        return None
    return (start_brace, end_brace)


def find_existing_gumbo_block(text: str, body_start: int, body_end: int) -> Optional[Tuple[int, int]]:
    """Return (start, end) of an existing language "GUMBO" /*{...}*/ inside the part body."""
    segment = text[body_start:body_end+1]
    mm = re.search(r'language\s+"GUMBO"\s*/\*\{', segment)
    if not mm:
        return None
    open_idx = body_start + mm.end() - 2  # position at '{'
    # Scan for '}' followed by '*/'
    i = open_idx + 1
    while i < len(text):
        if text[i] == '}' and i + 2 <= len(text) and text[i+1:i+3] == '*/':
            return (mm.start() + body_start, i + 3)
        i += 1
    return None


def remove_gumbo_contract_packages(text: str) -> Tuple[str, List[Tuple[str, str]]]:
    """
    Remove all 'package <Part>_GumboContract { ... }' blocks.
    Returns (new_text, packages) where packages is a list of (PartPkgName, PkgBodyText).
    """
    packages: List[Tuple[str, str]] = []
    out = text
    spans: List[Tuple[int, int, str, str]] = []
    for m in PKG_RE.finditer(text):
        pkg_name = m.group(1)
        if not pkg_name.endswith("_GumboContract"):
            continue
        start = m.end() - 1  # at '{'
        close = find_matching_brace(text, start)
        if close == -1:
            continue
        spans.append((m.start(), close + 1, pkg_name, text[m.start():close+1]))

    for (s, e, pkg_name, pkg_text) in sorted(spans, key=lambda t: t[0], reverse=True):
        packages.append((pkg_name, pkg_text))
        out = out[:s] + out[e:]
    packages.reverse()
    return out, packages


def _find_last_top_level_out_end(text: str, body_start: int, body_end: int) -> Optional[Tuple[int, str]]:
    """
    Inside the part body [body_start+1 : body_end], find the LAST declaration that
    starts with 'out ' (but not 'out :>') and return (absolute_end_index, leading_ws).
    The 'end' is positioned right AFTER the declaration (after ';' or after '}' of its block).
    """
    segment = text[body_start + 1:body_end]
    last_m = None
    for m in re.finditer(r"^[ \t]*out\s+(?!:>)[^\n]*", segment, re.MULTILINE):
        last_m = m
    if not last_m:
        return None

    abs_line_start = body_start + 1 + last_m.start()
    abs_scan_from = abs_line_start
    # Whether this 'out …' ends with ';' or has a '{…}' block
    brace_idx = text.find("{", abs_scan_from, body_end)
    semi_idx = text.find(";", abs_scan_from, body_end)

    if brace_idx != -1 and (semi_idx == -1 or brace_idx < semi_idx):
        close = find_matching_brace(text, brace_idx)
        if close == -1:
            return None
        end_idx = close + 1
    else:
        end_idx = (semi_idx + 1) if semi_idx != -1 else abs_line_start + len(last_m.group(0))

    ws = re.match(r"[ \t]*", last_m.group(0)).group(0)
    return end_idx, ws


def insert_gumbo_into_part(model_text: str, part_name: str, gumbo_block_text: str) -> str:
    """
    Insert the 'language "GUMBO"' block inside part 'part_name'.
    - If a block exists already, remove it.
    - Insert the new block **immediately after the last top-level line that starts with 'out'**.
    - Re-indent the GUMBO block so its 'language' line aligns with that 'out' line.
    """
    span = find_part_body_span(model_text, part_name)
    if not span:
        return model_text
    body_start, body_end = span

    existing = find_existing_gumbo_block(model_text, body_start, body_end)
    if existing:
        s, e = existing
        model_text = model_text[:s] + model_text[e:]
        # recompute span after removal
        span = find_part_body_span(model_text, part_name)
        if not span:
            return model_text
        body_start, body_end = span

    last_out = _find_last_top_level_out_end(model_text, body_start, body_end)

    if last_out:
        insert_at, out_ws = last_out
        first_ws = re.match(r"[ \t]*", gumbo_block_text).group(0)
        block = re.sub(rf"(?m)^{re.escape(first_ws)}", out_ws, gumbo_block_text)
        before = "" if (insert_at > 0 and model_text[insert_at-1] == "\n") else "\n"
        after = "\n"
        return model_text[:insert_at] + before + block + after + model_text[insert_at:]
    else:
        # Fallback: place right before the closing brace, indented 8 spaces from the 'part' line
        base_ws = detab(prev_line_indent(model_text, body_start))
        desired_ws = base_ws + " " * 8
        first_ws = re.match(r"[ \t]*", gumbo_block_text).group(0)
        block = re.sub(rf"(?m)^{re.escape(first_ws)}", desired_ws, gumbo_block_text)
        return model_text[:body_end] + "\n" + block + "\n" + model_text[body_end:]


# ----------------------------- driver -----------------------------

def gather_sysml_files(inputs: List[str]) -> List[str]:
    files: List[str] = []
    for p in inputs:
        if os.path.isdir(p):
            for root, _dirs, fnames in os.walk(p):
                for fn in fnames:
                    if fn.lower().endswith(".sysml"):
                        files.append(os.path.join(root, fn))
        else:
            if p.lower().endswith(".sysml") and os.path.isfile(p):
                files.append(p)
    return sorted(files)


def reconstruct_file(path: str, out_dir: str) -> Optional[str]:
    """
    Process a single .sysml file: extract all *_GumboContract packages, reconstruct
    language "GUMBO" blocks into their corresponding parts, remove packages, and write output.
    Returns the output path if any change was made; otherwise None.
    """
    src = read_text(path)
    src_wo_pkgs, pkgs = remove_gumbo_contract_packages(src)
    if not pkgs:
        base = os.path.basename(path)
        out_path = os.path.join(out_dir, base.replace(".sysml", "_reconstructed_with_GUMBO.sysml"))
        write_text(out_path, src)
        return out_path

    new_text = src_wo_pkgs
    for pkg_name, pkg_text in pkgs:
        brace_idx = pkg_text.find("{")
        end_idx = find_matching_brace(pkg_text, brace_idx)
        if brace_idx == -1 or end_idx == -1:
            continue
        pkg_body = pkg_text[brace_idx+1:end_idx]

        gp = parse_gumbo_package(pkg_name, pkg_body)

        # Build the GUMBO block with a neutral left margin; we'll re-indent at insertion.
        gumbo_block = gp.to_gumbo(indent=" " * 8)

        new_text = insert_gumbo_into_part(new_text, gp.part_name, gumbo_block)

    base = os.path.basename(path)
    out_path = os.path.join(out_dir, base.replace(".sysml", "_reconstructed_with_GUMBO.sysml"))
    write_text(out_path, new_text)
    return out_path


def run_sireum_if_requested(run: bool, sireum_path: Optional[str], sourcepath: str) -> int:
    if not run:
        return 0
    cmd = [sireum_path or shutil.which("sireum") or "sireum", "hamr", "sysml", "logika", "--sourcepath", sourcepath]
    try:
        proc = subprocess.run(cmd, text=True)
        return proc.returncode
    except FileNotFoundError:
        print("[ERROR] 'sireum' not found. Provide --sireum or add it to your PATH.")
        return 127


def main() -> None:
    ap = argparse.ArgumentParser(
        prog="GUMBO_Inverse_Interpreter",
        description="Rebuild GUMBO language blocks from nested *_GumboContract packages and (optionally) run Sireum."
    )
    ap.add_argument("inputs", nargs="+", help="Input .sysml files or directories containing .sysml files.")
    ap.add_argument("-o", "--out-dir", default="SysML", help="Output directory for reconstructed models (default: ./SysML).")
    ap.add_argument("--run-sireum", action="store_true", help="If set, run 'sireum hamr sysml logika --sourcepath <out-dir>'.")
    ap.add_argument("--sireum", default=None, help="Path to 'sireum' launcher (default: use PATH).")
    ap.add_argument("--sourcepath", default=None, help="Override sourcepath passed to Sireum (default: --out-dir).")

    args = ap.parse_args()

    sysml_files = gather_sysml_files(args.inputs)
    if not sysml_files:
        print("No .sysml files found.")
        return

    os.makedirs(args.out_dir, exist_ok=True)

    produced: List[str] = []
    for f in sysml_files:
        outp = reconstruct_file(f, args.out_dir)
        if outp:
            produced.append(outp)

    print(f"\nReconstructed {len(produced)} file(s) into: {os.path.abspath(args.out_dir)}")
    for p in produced:
        print("  -", os.path.basename(p))

    if args.run_sireum:
        sp = args.sourcepath or args.out_dir
        print(f"\nRunning Sireum: sireum hamr sysml logika --sourcepath {sp}")
        rc = run_sireum_if_requested(True, args.sireum, sp)
        if rc != 0:
            print(f"Sireum exited with code {rc}")


if __name__ == "__main__":
    main()
