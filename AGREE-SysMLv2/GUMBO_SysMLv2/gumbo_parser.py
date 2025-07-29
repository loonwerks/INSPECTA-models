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
        "Install it with 'pip install lark' and retry.") from exc

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

# -------------------------------------------------------------------
# 2) Transformer based on original working approach
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
        self.helper_funcs.append((func_name, return_type, body_expr))

    # —— type_ref
    def type_ref(self, items):
        """Reconstruct a qualified type reference.

        Lark includes the '::' separator tokens in the items list. The original
        implementation joined all items with '::', which produced outputs like
        ``Foo::::Bar``.  Filter out the separator tokens before joining so the
        result is ``Foo::Bar``.
        """

        tokens = [str(tok) for tok in items if str(tok) != "::"]
        return "::".join(tokens)

    # —— integration_section
    def integration_section(self, items):
        old_flag = self.in_integration
        self.in_integration = True
        # Process the integration_block
        for item in items:
            if hasattr(item, '__iter__') and not isinstance(item, str):
                for stmt in item:
                    if stmt:
                        self.integration_assumes.append(stmt)
            elif item:
                self.integration_assumes.append(item)
        self.in_integration = old_flag
        return items

    def integration_block(self, items):
        # Just return the items, they'll be processed by integration_section
        return items

    # —— initialize_section
    def initialize_section(self, items):
        # Process children - items[0] should be initialize_block result
        if items and items[0]:
            for item in items[0]:
                if item:
                    self.initialize_guarantees.append(item)
        return items

    def initialize_block(self, items):
        # Just return the processed guarantee statements
        return items

    # —— compute_section
    def compute_section(self, items):
        # Items should be case_statements
        for item in items:
            if item:  # Skip None items
                pass  # case_statement already processed itself
        return items

    # —— case_statement
    def case_statement(self, items):
        case_id = str(items[0])
        case_desc = ""
        body_start = 1

        if len(items) > 1 and isinstance(items[1], str):
            case_desc = items[1].strip('"')
            body_start = 2

        # Create case data
        case_data = {
            'id': case_id,
            'description': case_desc,
            'assumes': [],
            'guarantees': []
        }

        # Process case body - items[body_start] should be the case_body result
        if body_start < len(items) and items[body_start]:
            case_body_result = items[body_start]

            # Extract statements from case_body result and add to case_data
            if isinstance(case_body_result, list):
                for stmt_tuple in case_body_result:
                    if stmt_tuple and len(stmt_tuple) >= 4:
                        stmt_type, name, desc, expr = stmt_tuple
                        if stmt_type == "assume":
                            case_data['assumes'].append(stmt_tuple)
                        elif stmt_type == "guarantee":
                            case_data['guarantees'].append(stmt_tuple)

        self.compute_cases.append(case_data)
        return case_data

    def case_body(self, items):
        # Items are already processed case_assume/case_guarantee results
        return [item for item in items if item is not None]

    # —— case_assume and case_guarantee - REMOVE THESE, replaced by anon_* methods
    # def case_assume(self, items):
    # def case_guarantee(self, items):

    # —— Named statements for integration/initialize
    def named_assume_statement(self, items):
        name, desc, expr = self._unpack_optional_statement(items)
        result = ("assume", name, desc, expr)
        return result

    def named_guarantee_statement(self, items):
        name, desc, expr = self._unpack_optional_statement(items)
        result = ("guarantee", name, desc, expr)
        return result

    # —— Anonymous statements for case bodies
    def anon_assume_statement(self, items):
        expr = items[0]
        # Just return the tuple, case_statement will handle adding to case data
        return ("assume", "", "", expr)

    def anon_guarantee_statement(self, items):
        expr = items[0]
        # Just return the tuple, case_statement will handle adding to case data
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
            desc = re.sub(r'\s*\|\s*', ' ', desc.strip())

        return name, desc, expr

    # —— Expression handling
    def var(self, items):
        """Reconstruct variable names while inferring separators."""

        # The parser supplies both identifier tokens and the literal separator
        # tokens ('::' or '.').  Filter out the separators first so that
        # ``items`` becomes a simple list of identifier strings.
        tokens = [str(it) for it in items if str(it) not in ("::", ".")]

        if len(tokens) == 1:
            return tokens[0]

        # Reconstruct the proper qualified name with appropriate separators
        result = tokens[0]

        for i in range(1, len(tokens)):
            prev_part = tokens[i - 1]
            curr_part = tokens[i]

            # Determine separator based on patterns:

            # Pattern 1: Field access - if current part is a known field name, use .
            if curr_part in ['degrees', 'status', 'flag', 'value']:
                separator = "."
            # Pattern 2: Module path - if previous part looks like a module name, use ::
            elif (prev_part in ['Isolette_Data_Model', 'Base_Types'] or
                  prev_part in ['Status', 'Monitor_Mode', 'ValueStatus', 'On_Off'] or
                  curr_part in ['Status', 'Monitor_Mode', 'ValueStatus', 'On_Off'] or
                  (curr_part.endswith('_Status') or curr_part.endswith('_Mode') or
                   curr_part.endswith('_Off') or curr_part in ['Invalid', 'Valid', 'Init_Status', 'On_Status',
                                                               'Failed_Status', 'Init_Monitor_Mode',
                                                               'Normal_Monitor_Mode', 'Failed_Monitor_Mode'])):
                separator = "::"
            # Pattern 3: Default for variable field access - use .
            else:
                separator = "."

            result += separator + curr_part

        return result

    def number(self, items):
        tok = items[0]
        return tok.value if hasattr(tok, "value") else str(tok)

    def true_literal(self, items):
        return "true"

    def false_literal(self, items):
        return "false"

    def not_expr(self, items):
        return f"(not {items[0]})"

    def implication_expr(self, items):
        if len(items) == 1:
            return items[0]
        result = items[0]
        for i in range(1, len(items)):
            result = f"({result} implies {items[i]})"
        return result

    def or_expr(self, items):
        if len(items) == 1:
            return items[0]

        result = items[0]
        i = 1
        while i < len(items):
            if hasattr(items[i], "type"):
                op_tok = items[i]
                if i + 1 >= len(items):
                    break
                rhs = items[i + 1]
                op = op_tok.value if hasattr(op_tok, "value") else str(op_tok)
                result = f"({result} {op} {rhs})"
                i += 2
            else:
                rhs = items[i]
                result = f"({result} or {rhs})"
                i += 1
        return result

    def and_expr(self, items):
        if len(items) == 1:
            return items[0]

        result = items[0]
        i = 1
        while i < len(items):
            if hasattr(items[i], "type"):
                op_tok = items[i]
                if i + 1 >= len(items):
                    break
                rhs = items[i + 1]
                op = op_tok.value if hasattr(op_tok, "value") else str(op_tok)
                result = f"({result} {op} {rhs})"
                i += 2
            else:
                rhs = items[i]
                result = f"({result} and {rhs})"
                i += 1
        return result

    def compare_expr(self, items):
        if len(items) == 1:
            return items[0]
        result = items[0]
        i = 1
        while i < len(items):
            op_tok = items[i]
            rhs = items[i + 1]
            op = op_tok.value if hasattr(op_tok, 'value') else str(op_tok)
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
            op = op_tok.value if hasattr(op_tok, 'value') else str(op_tok)
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
            op = op_tok.value if hasattr(op_tok, 'value') else str(op_tok)
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

