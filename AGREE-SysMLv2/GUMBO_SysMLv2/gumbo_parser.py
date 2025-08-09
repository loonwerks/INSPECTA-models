#!/usr/bin/env python3
"""
Gumbo parser and transformer.
Parses state/functions/integration/initialize/compute (with cases),
supports:
  • function declarations with typed params,
  • sequent sugar  '->:'(A, B)  =>  (A implies B),
  • typed numeric tags: 96 [s32] => "96 /* [s32] */",
  • T/F shorthands for booleans.
"""

import os
import re
from typing import List, Tuple, Optional, Union

from lark import Lark, Transformer, Token, Tree

# -------------------------------------------------------------------
# Grammar loading
# -------------------------------------------------------------------
GRAMMAR_FILE = os.path.join(os.path.dirname(__file__), "gumbo.lark")
with open(GRAMMAR_FILE, "r", encoding="utf-8") as fh:
    Gumbo_grammar = fh.read()

parser = Lark(Gumbo_grammar, parser="lalr")

# -------------------------------------------------------------------
# Transformer used during translation
# -------------------------------------------------------------------
class GumboTransformer(Transformer):
    def __init__(self):
        super().__init__()
        self.state_vars: List[Tuple[str, str]] = []
        # helper_funcs: (name, return_type, body_expr, params)
        # params is a List[(param_name, type_ref)]
        self.helper_funcs: List[Tuple[str, str, str, List[Tuple[str, str]]]] = []

        self.integration_assumes: List[Tuple[str, Optional[str], str, str]] = []
        self.initialize_guarantees: List[Tuple[str, Optional[str], str, str]] = []
        self.guarantees: List[Tuple[str, Optional[str], str, str]] = []

        self.compute_cases: List[dict] = []

        # storage for top-level compute section assume/guarantee
        self.compute_toplevel_assumes: List[Tuple[str, Optional[str], str, str]] = []
        self.compute_toplevel_guarantees: List[Tuple[str, Optional[str], str, str]] = []

    # —— state_decl
    def state_decl(self, items):
        var_name = str(items[0])
        type_str = items[1]
        self.state_vars.append((var_name, type_str))

    # —— function declarations with typed params
    def func_param(self, items):
        return (str(items[0]), items[1])  # (name, type_ref)

    def func_params(self, items):
        return items

    def func_decl(self, items):
        """
        items:
          with params: [ID, params(list), return_type, expr]
          without    : [ID, return_type, expr]
        """
        func_name = str(items[0])
        if len(items) == 4:
            params: List[Tuple[str, str]] = items[1]
            return_type = items[2]
            body_expr = items[3]
        else:
            params = []
            return_type = items[1]
            body_expr = items[2]
        self.helper_funcs.append((func_name, return_type, body_expr, params))

    # —— type_ref
    def type_ref(self, items):
        tokens = [str(tok) for tok in items if str(tok) != "::"]
        return "::".join(tokens)

    # —— integration_section
    def integration_section(self, items):
        for item in items:
            if isinstance(item, list):
                for stmt in item:
                    if stmt:
                        self.integration_assumes.append(stmt)
            elif item:
                self.integration_assumes.append(item)
        return items

    def integration_block(self, items):
        return items

    # —— initialize_section
    def initialize_section(self, items):
        if items and items[0]:
            for item in items[0]:
                if item:
                    self.initialize_guarantees.append(item)
        return items

    def initialize_block(self, items):
        return items

    # —— compute_section: collect any top-level assume/guarantee before the cases
    def compute_section(self, items):
        for itm in items:
            stmt = itm
            if isinstance(itm, Tree) and itm.data == "top_level_stmt":
                stmt = itm.children[0]
            if isinstance(stmt, tuple):
                kind = stmt[0]
                if kind == "assume":
                    self.compute_toplevel_assumes.append(stmt)
                elif kind == "guarantee":
                    self.compute_toplevel_guarantees.append(stmt)
        return items

    # —— case_statement
    def case_statement(self, items):
        case_id = str(items[0])
        case_desc = ""
        body_start = 1

        if len(items) > 1 and isinstance(items[1], str):
            case_desc = items[1].strip('"')
            body_start = 2

        data = {
            "id": case_id,
            "description": case_desc,
            "assumes": [],
            "guarantees": [],
        }

        if body_start < len(items) and items[body_start]:
            for stmt_tuple in items[body_start]:
                if stmt_tuple and len(stmt_tuple) >= 4:
                    t, n, d, expr = stmt_tuple
                    if t == "assume":
                        data["assumes"].append(stmt_tuple)
                    elif t == "guarantee":
                        data["guarantees"].append(stmt_tuple)

        self.compute_cases.append(data)
        return data

    def case_body(self, items):
        return [i for i in items if i is not None]

    # —— named vs anonymous statements
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
        name: Optional[str] = None
        desc = ""
        expr = ""
        if len(items) == 3:
            name, desc, expr = items
        elif len(items) == 2:
            a, b = items
            if isinstance(a, str) and not re.fullmatch(r"[A-Za-z_][A-Za-z0-9_]*", a):
                desc, expr = items
            else:
                name, expr = items
        elif len(items) == 1:
            expr = items[0]
        if desc:
            desc = re.sub(r"\s*\|\s*", " ", desc.strip())
        return name, desc, expr

    # —— Expression handling
    def var(self, items):
        tokens = [str(it) for it in items if str(it) not in ("::", ".")]
        if len(tokens) == 1:
            return tokens[0]
        res = tokens[0]
        for prev, curr in zip(tokens, tokens[1:]):
            if curr in ["degrees", "status", "flag", "value"]:
                sep = "."
            elif (
                prev in ["Isolette_Data_Model", "Base_Types"]
                or curr in ["Status", "Monitor_Mode", "Regulator_Mode", "ValueStatus", "On_Off"]
                or curr.endswith("_Status")
            ):
                sep = "::"
            else:
                sep = "."
            res += sep + curr
        return res

    def typed_number(self, items):
        num = items[0]
        tag = items[1]
        return f"{num} /* [{tag}] */"

    def number(self, items):
        tok = items[0]
        return tok.value if hasattr(tok, "value") else str(tok)

    def true_literal(self, items):
        return "true"

    def false_literal(self, items):
        return "false"

    def not_expr(self, items):
        return f"(not {items[0]})"

    def or_expr(self, items):
        parts = [it for it in items if not (isinstance(it, Token) and str(it).lower() in ("or", "|"))]
        result = parts[0]
        for r in parts[1:]:
            result = f"({result} or {r})"
        return result

    def and_expr(self, items):
        parts = [it for it in items if not (isinstance(it, Token) and str(it).lower() in ("and", "&"))]
        result = parts[0]
        for r in parts[1:]:
            result = f"({result} and {r})"
        return result

    def implication_expr(self, items):
        result = items[0]
        for nxt in items[1:]:
            result = f"({result} implies {nxt})"
        return result

    def compare_expr(self, items):
        res = items[0]
        i = 1
        while i < len(items):
            op = items[i]
            rhs = items[i + 1]
            op_str = op.value if hasattr(op, "value") else str(op)
            res = f"({res} {op_str} {rhs})"
            i += 2
        return res

    def add_expr(self, items):
        res = items[0]
        i = 1
        while i < len(items):
            op = items[i]
            rhs = items[i + 1]
            op_str = op.value if hasattr(op, "value") else str(op)
            res = f"({res} {op_str} {rhs})"
            i += 2
        return res

    def mul_expr(self, items):
        res = items[0]
        i = 1
        while i < len(items):
            op = items[i]
            rhs = items[i + 1]
            op_str = op.value if hasattr(op, "value") else str(op)
            res = f"({res} {op_str} {rhs})"
            i += 2
        return res

    def paren_expr(self, items):
        return f"({items[0]})"

    # —— Function calls / special sugar
    def call_expr(self, items):
        func = items[0]
        args = items[1:]
        return f"{func}({', '.join(args)})"

    # '->:'(A, B)  =>  (A implies B)
    def sequent_call(self, items):
        # Lark passes tokens (SEQIMPL, '(', ',', ')') among children; keep only expr strings.
        exprs: List[str] = [it for it in items if not isinstance(it, Token) and isinstance(it, (str,))]
        if len(exprs) < 2:
            # Fallback: pick last two non-token items
            non_tok = [it for it in items if not isinstance(it, Token)]
            if len(non_tok) >= 2:
                left, right = non_tok[-2], non_tok[-1]
            else:
                # Defensive default
                return "true"
        else:
            left, right = exprs[0], exprs[1]
        return f"({left} implies {right})"

    # —— Token passthroughs
    def ID(self, token): return token.value
    def NUMBER(self, token): return token.value
    def STRING(self, token): return token.value.strip('"')
    def TYPETAG(self, token): return token.value
