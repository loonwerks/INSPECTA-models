# !/usr/bin/env python3
# ========================================================================================================
# @author: Amer N Tahat,
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
# ========================================================================================================

import re
from lark import Lark, Transformer, Token

# -------------------------------------------------------------------
# 1) Fixed grammar with proper separation of named vs anonymous statements
# -------------------------------------------------------------------
Gumbo_grammar = r"""
start: spec_statement+

spec_statement: state_section
              | function_section
              | integration_section
              | initialize_section
              | compute_section

// ——————————————————————————————————————————————————————————
// 1) state section
state_section: "state" WS_INLINE? state_decl+
state_decl   : ID ":" type_ref ";"

// ——————————————————————————————————————————————————————————
// 2) functions section
function_section: "functions" WS_INLINE? func_decl+
func_decl       : "def" ID "(" ")" ":" type_ref ":=" atom ";"

// ——————————————————————————————————————————————————————————
// 3) integration section - uses NAMED statements
integration_section: "integration" WS_INLINE? integration_block
integration_block  : ( named_assume_statement )+

// ——————————————————————————————————————————————————————————
// 4) initialize section - uses NAMED statements  
initialize_section: "initialize" WS_INLINE? initialize_block
initialize_block  : ( named_guarantee_statement )+

// ——————————————————————————————————————————————————————————
// 5) compute section with compute_cases - uses ANONYMOUS statements
compute_section: "compute" WS_INLINE? "compute_cases" WS_INLINE? case_statement+

case_statement: "case" ID STRING? ":" case_body

case_body: ( anon_assume_statement | anon_guarantee_statement )+

// ——————————————————————————————————————————————————————————
// NAMED statements for integration/initialize
named_assume_statement: "assume" ID STRING? ":" expr ";"
named_guarantee_statement: "guarantee" ID STRING? ":" expr ";"

// ANONYMOUS statements for case bodies
anon_assume_statement: "assume" expr ";"
anon_guarantee_statement: "guarantee" expr ";"

// ——————————————————————————————————————————————————————————
// Enhanced expressions
?expr: implication_expr
?implication_expr: or_expr ( "implies" or_expr )*     -> implication_expr
?or_expr: and_expr ( (OR | "|") and_expr )*          -> or_expr
?and_expr: compare_expr ( (AND | "&") compare_expr )* -> and_expr
?compare_expr: add_expr ( COMPOP add_expr )*         -> compare_expr
?add_expr: mul_expr ( ADD mul_expr )*                -> add_expr
?mul_expr: unary_expr ( MUL unary_expr )*            -> mul_expr
?unary_expr: "not" unary_expr                        -> not_expr
           | atom

?atom: "(" expr ")"                                  -> paren_expr
     | operand

?operand: ID (("::" | ".") ID)*   -> var
        | NUMBER                  -> number
        | "T"                     -> true_literal
        | "true"                  -> true_literal
        | "false"                 -> false_literal

// Logic / arithmetic tokens
OR     : "or"
AND    : "and"
COMPOP : "<=" | ">=" | "<" | ">" | "==" | "!="
ADD    : "+" | "-"
MUL    : "*" | "/"

type_ref: ID ("::" ID)*

%import common.CNAME         -> ID
%import common.SIGNED_NUMBER -> NUMBER
%import common.ESCAPED_STRING-> STRING
%import common.WS_INLINE
%ignore WS_INLINE
%ignore /\s+/
"""

parser = Lark(Gumbo_grammar, parser='lalr')


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
        return "::".join(str(tok) for tok in items)

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
        # items is a list of strings like ['lower_alarm_tempWstatus', 'degrees']
        # We need to figure out what separators should be based on patterns

        if len(items) == 1:
            return items[0]

        # Reconstruct the proper qualified name with appropriate separators
        result = items[0]

        for i in range(1, len(items)):
            prev_part = items[i - 1]
            curr_part = items[i]

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
        # items should be [operand, operator, operand, operator, ...]
        result = str(items[0])
        i = 1
        while i < len(items):
            if i + 1 < len(items):
                # Skip the operator token, get the operand
                result = f"({result} or {items[i + 1]})"
                i += 2
            else:
                break
        return result

    def and_expr(self, items):
        if len(items) == 1:
            return items[0]
        # items should be [operand, operator, operand, operator, ...]
        result = str(items[0])
        i = 1
        while i < len(items):
            if i + 1 < len(items):
                # Skip the operator token, get the operand
                result = f"({result} and {items[i + 1]})"
                i += 2
            else:
                break
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


# -------------------------------------------------------------------
# 3) Translation function
# -------------------------------------------------------------------
def translate_monitor_gumbo(transformer: GumboTransformer, block_name: str):
    lines = []
    lines.append(f"package IsoletteContracts {{")
    lines.append(f"  import Isolette_Data_Model::*;")
    lines.append(f"  import Base_Types::*;")
    lines.append(f"")
    lines.append(f"  block {block_name} {{")

    # State vars
    if transformer.state_vars:
        for var, typ in transformer.state_vars:
            lines.append(f"    attribute {var}: {typ};")
        lines.append(f"")

    # Helper functions
    if transformer.helper_funcs:
        for fname, rettype, body in transformer.helper_funcs:
            lines.append(f"    constraint function {fname}(): {rettype} {{")
            lines.append(f"      return {body};")
            lines.append(f"    }}")
            lines.append(f"")

    # Initialize contract
    if transformer.initialize_guarantees:
        lines.append(f"    contract InitializeContract {{")
        for (_kind, name, desc, expr) in transformer.initialize_guarantees:
            ident = f" {name}" if name else ""
            desc_str = f' "{desc}"' if desc else ""
            # Keep == as is - SysML v2 supports == for comparisons
            lines.append(f"      require{ident}{desc_str} {{")
            lines.append(f"        {expr};")
            lines.append(f"      }}")
            lines.append(f"")
        lines.append(f"    }}")
        lines.append(f"")

    # Integration contract
    if transformer.integration_assumes or transformer.guarantees:
        lines.append(f"    contract MonitorIntegration {{")

        for (_kind, name, desc, expr) in transformer.integration_assumes:
            ident = f" {name}" if name else ""
            desc_str = f' "{desc}"' if desc else ""
            lines.append(f"      assume{ident}{desc_str} {{")
            lines.append(f"        {expr};")
            lines.append(f"      }}")
            lines.append(f"")

        for (_kind, name, desc, expr) in transformer.guarantees:
            ident = f" {name}" if name else ""
            desc_str = f' "{desc}"' if desc else ""
            # Keep == as is - SysML v2 supports == for comparisons
            lines.append(f"      require{ident}{desc_str} {{")
            lines.append(f"        {expr};")
            lines.append(f"      }}")
            lines.append(f"")

        lines.append(f"    }}")
        lines.append(f"")

    # Compute cases
    for case in transformer.compute_cases:
        case_id = case['id']
        case_desc = case['description']
        lines.append(f"    contract {case_id} {{")

        if case_desc:
            lines.append(f"      // {case_desc}")

        for (_kind, name, desc, expr) in case['assumes']:
            ident = f" {name}" if name else ""
            desc_str = f' "{desc}"' if desc else ""
            lines.append(f"      assume{ident}{desc_str} {{")
            lines.append(f"        {expr};")
            lines.append(f"      }}")
            lines.append(f"")

        for (_kind, name, desc, expr) in case['guarantees']:
            ident = f" {name}" if name else ""
            desc_str = f' "{desc}"' if desc else ""
            # Keep == as is - SysML v2 supports == for comparisons
            lines.append(f"      require{ident}{desc_str} {{")
            lines.append(f"        {expr};")
            lines.append(f"      }}")
            lines.append(f"")

        lines.append(f"    }}")
        lines.append(f"")

    lines.append(f"  }}")
    lines.append(f"}}")

    return "\n".join(lines)


# -------------------------------------------------------------------
# 4) Main function
# -------------------------------------------------------------------
def main():
    gumbo_content = """
state
    lastCmd: Isolette_Data_Model::On_Off;

functions
    def timeout_condition_satisfied():Base_Types::Boolean := T;

integration
    assume Table_A_12_LowerAlarmTemp "Range [96..101]" :
        96 <= lower_alarm_tempWstatus.degrees and lower_alarm_tempWstatus.degrees <= 101;

    assume Table_A_12_UpperAlarmTemp "Range [97..102]" :
        97 <= upper_alarm_tempWstatus.degrees and upper_alarm_tempWstatus.degrees <= 102;

initialize
    guarantee monitorStatusInitiallyInit:
        monitor_status == Isolette_Data_Model::Status.Init_Status;

compute
    compute_cases
        case REQ_MMI_1 "If the Manage Monitor Interface mode is INIT, the Monitor Status shall be set to Init." :
            assume monitor_mode == Isolette_Data_Model::Monitor_Mode.Init_Monitor_Mode;
            guarantee monitor_status == Isolette_Data_Model::Status.Init_Status;

        case REQ_MMI_2 "If the Manage Monitor Interface mode is NORMAL, the Monitor Status shall be set to On" :
            assume monitor_mode == Isolette_Data_Model::Monitor_Mode.Normal_Monitor_Mode;
            guarantee monitor_status == Isolette_Data_Model::Status.On_Status;

        case REQ_MMI_4 "If the Status attribute is Invalid, the Monitor Interface Failure shall be set to True" :
            assume lower_alarm_tempWstatus.status == Isolette_Data_Model::ValueStatus.Invalid | upper_alarm_tempWstatus.status == Isolette_Data_Model::ValueStatus.Invalid;
            guarantee interface_failure.flag;

        case REQ_MMI_5 "If the Status attribute is Valid, the Monitor Interface Failure shall be set to False" :
            assume lower_alarm_tempWstatus.status == Isolette_Data_Model::ValueStatus.Valid & upper_alarm_tempWstatus.status == Isolette_Data_Model::ValueStatus.Valid;
            guarantee not interface_failure.flag;

        case REQ_MMI_6 "If the Monitor Interface Failure is False, the Alarm Range variable shall be set to the Desired Temperature Range" :
            assume true;
            guarantee not interface_failure.flag implies lower_alarm_temp.degrees == lower_alarm_tempWstatus.degrees & upper_alarm_temp.degrees == upper_alarm_tempWstatus.degrees;

        case REQ_MMI_7 "If the Monitor Interface Failure is True, the Alarm Range variable is UNSPECIFIED" :
            assume true;
            guarantee interface_failure.flag implies true;
"""

    print("=== Original GUMBO Content ===")
    print(gumbo_content.strip())
    print()

    try:
        parse_tree = parser.parse(gumbo_content.strip())
        transformer = GumboTransformer()
        transformer.transform(parse_tree)

        sysml_output = translate_monitor_gumbo(transformer, block_name="Manage_Monitor_Interface_i")
        print("=== Translated SysML v2 ===")
        print(sysml_output)

    except Exception as e:
        print(f"Error parsing/transforming: {e}")
        import traceback
        traceback.print_exc()


if __name__ == "__main__":
    main()