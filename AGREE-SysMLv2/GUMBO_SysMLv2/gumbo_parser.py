#!/usr/bin/env python3
"""Gumbo parser and transformer used by GumboTransformers."""

import os
import re
from typing import List, Tuple

try:
    from lark import Lark, Transformer, Token
except ImportError as exc:
    raise SystemExit(
        "The 'lark' package is required to run this script. "
        "Install it with 'pip install lark' and retry."
    ) from exc

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
        self.state_vars = []
        self.helper_funcs = []
        self.integration_assumes = []
        self.initialize_guarantees = []
        self.compute_cases = []
        self.guarantees = []

        # Context flags
        self.in_integration = False
        self.in_initialize = False
        self.in_case = False
        self.current_case = None

    # —— state_decl
    def state_decl(self, items):
        var_name = str(items[0])
        type_str = items[1]
        self.state_vars.append((var_name, type_str))

    # —— func_decl
    def func_decl(self, items):
        func_name = str(items[0])
        return_type = items[1]
        body_expr = items[2]
        # store for 'calc def' emission rather than 'constraint function'
        self.helper_funcs.append((func_name, return_type, body_expr))

    # —— type_ref
    def type_ref(self, items):
        """Reconstruct a qualified type reference."""
        tokens = [str(tok) for tok in items if str(tok) != "::"]
        return "::".join(tokens)

    # —— integration_section
    def integration_section(self, items):
        old_flag = self.in_integration
        self.in_integration = True
        for item in items:
            if hasattr(item, "__iter__") and not isinstance(item, str):
                for stmt in item:
                    if stmt:
                        self.integration_assumes.append(stmt)
            elif item:
                self.integration_assumes.append(item)
        self.in_integration = old_flag
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

    # —— compute_section
    def compute_section(self, items):
        return items

    # —— case_statement
    def case_statement(self, items):
        case_id = str(items[0])
        case_desc = ""
        body_start = 1

        if len(items) > 1 and isinstance(items[1], str):
            case_desc = items[1].strip('"')
            body_start = 2

        case_data = {
            "id": case_id,
            "description": case_desc,
            "assumes": [],
            "guarantees": [],
        }

        if body_start < len(items) and items[body_start]:
            case_body_result = items[body_start]
            if isinstance(case_body_result, list):
                for stmt_tuple in case_body_result:
                    if stmt_tuple and len(stmt_tuple) >= 4:
                        stmt_type, _, _, expr = stmt_tuple
                        if stmt_type == "assume":
                            case_data["assumes"].append(stmt_tuple)
                        elif stmt_type == "guarantee":
                            case_data["guarantees"].append(stmt_tuple)

        self.compute_cases.append(case_data)
        return case_data

    def case_body(self, items):
        return [item for item in items if item is not None]

    # —— named vs anonymous assume/guarantee
    def named_assume_statement(self, items):
        name, desc, expr = self._unpack_optional_statement(items)
        return ("assume", name, desc, expr)

    def named_guarantee_statement(self, items):
        name, desc, expr = self._unpack_optional_statement(items)
        return ("guarantee", name, desc, expr)

    def anon_assume_statement(self, items):
        expr = items[0]
        return ("assume", "", "", expr)

    def anon_guarantee_statement(self, items):
        expr = items[0]
        return ("guarantee", "", "", expr)

    def _unpack_optional_statement(self, items):
        name = None
        desc = ""
        expr = ""
        if len(items) == 3:
            name, desc, expr = items
        elif len(items) == 2:
            first, second = items
            if isinstance(first, str) and not re.fullmatch(r"[A-Za-z_][A-Za-z0-9_]*", first):
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
        result = tokens[0]
        for i in range(1, len(tokens)):
            prev_part = tokens[i - 1]
            curr_part = tokens[i]
            if curr_part in ["degrees", "status", "flag", "value"]:
                sep = "."
            elif (
                prev_part in ["Isolette_Data_Model", "Base_Types"]
                or prev_part in ["Status", "Monitor_Mode", "ValueStatus", "On_Off"]
                or curr_part in ["Status", "Monitor_Mode", "ValueStatus", "On_Off"]
                or curr_part.endswith("_Status")
                or curr_part.endswith("_Mode")
                or curr_part.endswith("_Off")
            ):
                sep = "::"
            else:
                sep = "."
            result += sep + curr_part
        return result

    def number(self, items):
        tok = items[0]
        return tok.value if hasattr(tok, "value") else str(tok)

    def true_literal(self, items):
        return "true"

    def false_literal(self, items):
        return "false"

    def not_expr(self, items):
        # SysML v2 uses 'not' for logical negation
        return f"(not {items[0]})"

    def implication_expr(self, items):
        if len(items) == 1:
            return items[0]
        result = items[0]
        for nxt in items[1:]:
            result = f"({result} implies {nxt})"
        return result

    def or_expr(self, items):
        # Use 'or' for logical disjunction in SysML v2
        if len(items) == 1:
            return items[0]
        result = items[0]
        for rhs in items[1:]:
            result = f"({result} or {rhs})"
        return result

    def and_expr(self, items):
        # Use 'and' for logical conjunction in SysML v2
        if len(items) == 1:
            return items[0]
        result = items[0]
        for rhs in items[1:]:
            result = f"({result} and {rhs})"
        return result

    def compare_expr(self, items):
        if len(items) == 1:
            return items[0]
        result = items[0]
        i = 1
        while i < len(items):
            op_tok = items[i]
            rhs = items[i + 1]
            op = op_tok.value if hasattr(op_tok, "value") else str(op_tok)
            result = f"({result} {op} {rhs})"
            i += 2
        return result

    def add_expr(self, items):
        if len(items) == 1:
            return items[0]
        result = items[0]
        i = 1
        while i < len(items):
            op_tok = items[i]
            rhs = items[i + 1]
            op = op_tok.value if hasattr(op_tok, "value") else str(op_tok)
            result = f"({result} {op} {rhs})"
            i += 2
        return result

    def mul_expr(self, items):
        if len(items) == 1:
            return items[0]
        result = items[0]
        i = 1
        while i < len(items):
            op_tok = items[i]
            rhs = items[i + 1]
            op = op_tok.value if hasattr(op_tok, "value") else str(op_tok)
            result = f"({result} {op} {rhs})"
            i += 2
        return result

    def paren_expr(self, items):
        return f"({items[0]})"

    def ID(self, token):
        return token.value

    def NUMBER(self, token):
        return token.value

    def STRING(self, token):
        return token.value.strip('"')

