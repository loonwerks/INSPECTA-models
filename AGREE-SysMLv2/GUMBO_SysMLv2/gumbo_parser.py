# gumbo_parser.py
#!/usr/bin/env python3
import os
import re
from typing import List, Tuple, Optional, Dict, Any

from lark import Lark, Transformer, Token, Tree

# -------------------------------------------------------------------
# Grammar load
# -------------------------------------------------------------------
GRAMMAR_FILE = os.path.join(os.path.dirname(__file__), "gumbo.lark")
with open(GRAMMAR_FILE, "r", encoding="utf-8") as fh:
    Gumbo_grammar = fh.read()

parser = Lark(Gumbo_grammar, parser="lalr")

# -------------------------------------------------------------------
# Transformer
# -------------------------------------------------------------------
_TAG_MARK = "__TAG__"

class GumboTransformer(Transformer):
    """
    Produces:
      - state_vars:         [(name, type)]
      - helper_funcs:       [(name, return_type, body_expr, params=[(pname, ptype), ...])]
      - integration_assumes: [("assume", name?, desc, expr)]
      - integration_requires:[("guarantee", name?, desc, expr)]  # mapped to 'require'
      - initialize_guarantees: same tuple shape as above
      - compute_toplevel_assumes / compute_toplevel_guarantees
      - compute_cases: [ {id, description, assumes=[...], guarantees=[...]} ]
    Expressions may contain inline markers like:  96__TAG__[s32]
    """
    def __init__(self):
        super().__init__()
        self.state_vars: List[Tuple[str, str]] = []
        self.helper_funcs: List[Tuple[str, str, str, List[Tuple[str, str]]]] = []

        self.integration_assumes: List[Tuple[str, Optional[str], str, str]] = []
        self.integration_requires: List[Tuple[str, Optional[str], str, str]] = []

        self.initialize_guarantees: List[Tuple[str, Optional[str], str, str]] = []

        self.compute_cases: List[dict] = []

        self.compute_toplevel_assumes: List[Tuple[str, Optional[str], str, str]] = []
        self.compute_toplevel_guarantees: List[Tuple[str, Optional[str], str, str]] = []

    # —— state_decl
    def state_decl(self, items):
        var_name = str(items[0])
        typ = items[1]
        self.state_vars.append((var_name, typ))

    # —— functions
    def func_param(self, items):
        return (str(items[0]), items[1])

    def func_params(self, items):
        return items

    def func_decl(self, items):
        # with params: [ID, params(list), return_type, expr]
        # without    : [ID, return_type, expr]
        fname = str(items[0])
        if len(items) == 4:
            params = items[1]
            rettype = items[2]
            body = items[3]
        else:
            params = []
            rettype = items[1]
            body = items[2]
        self.helper_funcs.append((fname, rettype, body, params))

    def type_ref(self, items):
        return "::".join([str(x) for x in items if str(x) != "::"])

    # —— integration (assume + guarantee)
    def integration_section(self, items):
        flat: List[Any] = []
        for it in items:
            if isinstance(it, list):
                flat += it
            else:
                flat.append(it)
        for stmt in flat:
            if not stmt:
                continue
            if stmt[0] == "assume":
                self.integration_assumes.append(stmt)
            elif stmt[0] == "guarantee":
                self.integration_requires.append(stmt)
        return items

    def integration_block(self, items): return items

    # —— initialize
    def initialize_section(self, items):
        if items and items[0]:
            for s in items[0]:
                if s: self.initialize_guarantees.append(s)
        return items

    def initialize_block(self, items): return items

    # —— compute: collect top-level assume/guarantee before cases
    def compute_section(self, items):
        for itm in items:
            stmt = itm
            if isinstance(itm, Tree) and itm.data == "top_level_stmt":
                stmt = itm.children[0]
            if isinstance(stmt, tuple):
                k = stmt[0]
                if k == "assume":
                    self.compute_toplevel_assumes.append(stmt)
                elif k == "guarantee":
                    self.compute_toplevel_guarantees.append(stmt)
        return items

    # —— case
    def case_statement(self, items):
        case_id = str(items[0])
        case_desc = ""
        body_idx = 1
        if len(items) > 1 and isinstance(items[1], str):
            case_desc = items[1].strip('"')
            body_idx = 2

        data = {"id": case_id, "description": case_desc, "assumes": [], "guarantees": []}

        if body_idx < len(items) and items[body_idx]:
            for tup in items[body_idx]:
                if not tup: continue
                if tup[0] == "assume":
                    data["assumes"].append(tup)
                elif tup[0] == "guarantee":
                    data["guarantees"].append(tup)
        self.compute_cases.append(data)
        return data

    def case_body(self, items): return [i for i in items if i is not None]

    # —— statements
    def named_assume_statement(self, items):
        name, desc, expr = self._unpack_optional_statement(items)
        return ("assume", name, desc, expr)

    def named_guarantee_statement(self, items):
        name, desc, expr = self._unpack_optional_statement(items)
        return ("guarantee", name, desc, expr)

    def anon_assume_statement(self, items):
        return ("assume", "", "", items[0])

    def anon_guarantee_statement(self, items):
        return ("guarantee", "", "", items[0])

    def _unpack_optional_statement(self, items):
        # returns (name (or None), desc, expr)
        name: Optional[str] = None
        desc = ""
        expr = ""
        if len(items) == 3:
            name, desc, expr = items
        elif len(items) == 2:
            a, b = items
            # could be (name, expr) or (desc, expr)
            if isinstance(a, str) and not re.fullmatch(r"[A-Za-z_][A-Za-z0-9_]*", a):
                # 'a' looks like description (quoted)
                desc, expr = items
            else:
                name, expr = items
        elif len(items) == 1:
            expr = items[0]
        if desc:
            desc = re.sub(r"\s*\|\s*", " ", desc.strip())
        return name, desc, expr

    # —— expr atoms
    def var(self, items):
        tokens = [str(it) for it in items if str(it) not in ("::", ".")]
        if len(tokens) == 1:
            return tokens[0]
        # heuristic separator (kept from your previous code)
        res = tokens[0]
        for prev, curr in zip(tokens, tokens[1:]):
            if curr in ["degrees", "status", "flag", "value"]:
                sep = "."
            elif (prev in ["Isolette_Data_Model", "Base_Types"]
                  or curr in ["Status", "Monitor_Mode", "Regulator_Mode", "ValueStatus", "On_Off"]
                  or curr.endswith("_Status")):
                sep = "::"
            else:
                sep = "."
            res += sep + curr
        return res

    # typed numeric → mark with __TAG__ (we will strip to comment later)
    def typed_number(self, items):
        num = items[0]
        tag = items[1]
        return f"{num}__TAG__[{tag}]"

    def number(self, items):
        tok = items[0]
        return tok.value if hasattr(tok, "value") else str(tok)

    def true_literal(self, _): return "true"
    def false_literal(self, _): return "false"

    def not_expr(self, items): return f"(not {items[0]})"

    def or_expr(self, items):
        parts = [it for it in items if not (isinstance(it, Token) and str(it).lower() in ("or", "|"))]
        out = parts[0]
        for r in parts[1:]:
            out = f"({out} or {r})"
        return out

    def and_expr(self, items):
        parts = [it for it in items if not (isinstance(it, Token) and str(it).lower() in ("and", "&"))]
        out = parts[0]
        for r in parts[1:]:
            out = f"({out} and {r})"
        return out

    def implication_expr(self, items):
        out = items[0]
        for nxt in items[1:]:
            out = f"({out} implies {nxt})"
        return out

    def compare_expr(self, items):
        res = items[0]; i = 1
        while i < len(items):
            op = items[i]; rhs = items[i+1]
            op_s = op.value if hasattr(op, "value") else str(op)
            res = f"({res} {op_s} {rhs})"
            i += 2
        return res

    def add_expr(self, items):
        res = items[0]; i = 1
        while i < len(items):
            op = items[i]; rhs = items[i+1]
            op_s = op.value if hasattr(op, "value") else str(op)
            res = f"({res} {op_s} {rhs})"
            i += 2
        return res

    def mul_expr(self, items):
        res = items[0]; i = 1
        while i < len(items):
            op = items[i]; rhs = items[i+1]
            op_s = op.value if hasattr(op, "value") else str(op)
            res = f"({res} {op_s} {rhs})"
            i += 2
        return res

    def paren_expr(self, items): return f"({items[0]})"

    def call_expr(self, items):
        func = items[0]; args = items[1:]
        return f"{func}({', '.join(args)})"

    def sequent_call(self, items):
        exprs = [x for x in items if not isinstance(x, Token)]
        left = exprs[0] if exprs else items[0]
        right = exprs[1] if len(exprs) > 1 else items[-1]
        return f"({left} implies {right})"

    # passthroughs
    def ID(self, t): return t.value
    def NUMBER(self, t): return t.value
    def STRING(self, t): return t.value.strip('"')
    def TYPETAG(self, t): return t.value
