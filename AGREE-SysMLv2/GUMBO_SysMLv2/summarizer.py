import json
import os
import re
import sys
from typing import Dict, List

try:
    import openai
except Exception:  # library may not be installed
    openai = None


def extract_rules(grammar_text: str) -> List[str]:
    """Return a list of rule names from a Lark grammar file."""
    rules = []
    rule_pattern = re.compile(r"^\??(\w+):")
    for line in grammar_text.splitlines():
        m = rule_pattern.match(line.strip())
        if m:
            rules.append(m.group(1))
    return rules


MAPPING_INFO = {
    "named_assume_statement": {
        "translation": "Assume requirement",
        "explanation": "Named assume statements map to SysML requirement assumptions",
    },
    "named_guarantee_statement": {
        "translation": "Guarantee requirement",
        "explanation": "Named guarantee statements map to SysML requirement guarantees",
    },
    "anon_assume_statement": {
        "translation": "Assume within compute case",
        "explanation": "Anonymous assumes become part of the antecedent in a constraint",
    },
    "anon_guarantee_statement": {
        "translation": "Guarantee within compute case",
        "explanation": "Anonymous guarantees form the consequent in a constraint",
    },
    "implication_expr": {
        "translation": "implies operator",
        "explanation": "Gumbo 'implies' maps to SysML 'implies' / '->:' operator",
    },
    "or_expr": {
        "translation": "||",
        "explanation": "Logical OR translated to SysML '||'",
    },
    "and_expr": {
        "translation": "&&",
        "explanation": "Logical AND translated to SysML '&&'",
    },
    "not_expr": {
        "translation": "!",
        "explanation": "Logical NOT translated to SysML '!'",
    },
}


def call_gpt(mapping: Dict[str, Dict[str, str]], model: str = "gpt-4o") -> str:
    """Return a GPT-generated summary table for the mapping."""
    if openai is None or not os.getenv("OPENAI_API_KEY"):
        return json.dumps(mapping, indent=2)

    prompt = (
        "Summarize the following Gumbo to SysML mapping as a table with columns"import json
import os
import re
import sys
from typing import Dict, List

try:
    import openai
except Exception:  # library may not be installed
    openai = None


def extract_rules(grammar_text: str) -> List[str]:
    """Return a list of rule names from a Lark grammar file."""
    rules = []
    rule_pattern = re.compile(r"^\??(\w+):")
    for line in grammar_text.splitlines():
        m = rule_pattern.match(line.strip())
        if m:
            rules.append(m.group(1))
    return rules


MAPPING_INFO = {
    "named_assume_statement": {
        "translation": "Assume requirement",
        "explanation": "Named assume statements map to SysML requirement assumptions",
    },
    "named_guarantee_statement": {
        "translation": "Guarantee requirement",
        "explanation": "Named guarantee statements map to SysML requirement guarantees",
    },
    "anon_assume_statement": {
        "translation": "Assume within compute case",
        "explanation": "Anonymous assumes become part of the antecedent in a constraint",
    },
    "anon_guarantee_statement": {
        "translation": "Guarantee within compute case",
        "explanation": "Anonymous guarantees form the consequent in a constraint",
    },
    "implication_expr": {
        "translation": "implies operator",
        "explanation": "Gumbo 'implies' maps to SysML 'implies' / '->:' operator",
    },
    "or_expr": {
        "translation": "||",
        "explanation": "Logical OR translated to SysML '||'",
    },
    "and_expr": {
        "translation": "&&",
        "explanation": "Logical AND translated to SysML '&&'",
    },
    "not_expr": {
        "translation": "!",
        "explanation": "Logical NOT translated to SysML '!'",
    },
}


def call_gpt(mapping: Dict[str, Dict[str, str]], model: str = "gpt-4o") -> str:
    """Return a GPT-generated summary table for the mapping."""
    if openai is None or not os.getenv("OPENAI_API_KEY"):
        return json.dumps(mapping, indent=2)

    prompt = (
        "Summarize the following Gumbo to SysML mapping as a table with columns"
        " 'Gumbo Rule', 'Translation Rule', and 'Explanation'. Return in Markdown.\n"
        + json.dumps(mapping, indent=2)
    )
    try:
        resp = openai.ChatCompletion.create(
            model=model,
            messages=[
                {"role": "system", "content": "You are a helpful assistant."},
                {"role": "user", "content": prompt},
            ],
        )
        return resp.choices[0].message.content.strip()
    except Exception as exc:
        print(f"GPT call failed: {exc}", file=sys.stderr)
        return json.dumps(mapping, indent=2)


def mapping_to_csv(mapping: Dict[str, Dict[str, str]]) -> str:
    """Return the mapping as CSV-formatted text."""
    rows = ["Gumbo Rule,Translation Rule,Explanation"]
    for rule, info in mapping.items():
        translation = info.get("translation", "")
        explanation = info.get("explanation", "")
        # escape quotes
        row = [rule, translation, explanation]
        output = []
        for cell in row:
            if any(c in cell for c in [',', '"', '\n']):
                cell = '"' + cell.replace('"', '""') + '"'
            output.append(cell)
        rows.append(','.join(output))
    return "\n".join(rows)

def generate_mapping(grammar_path: str, translator_path: str) -> Dict[str, Dict[str, str]]:
    """Generate a mapping from Gumbo rules to SysML translations."""
    with open(grammar_path, "r", encoding="utf-8") as fh:
        grammar_text = fh.read()
    with open(translator_path, "r", encoding="utf-8") as fh:
        translator_src = fh.read()

    rules = set(extract_rules(grammar_text)) | set(MAPPING_INFO.keys())
    mapping: Dict[str, Dict[str, str]] = {}
    for rule in rules:
        if rule in MAPPING_INFO and re.search(rf"def\s+{rule}\b", translator_src):
            mapping[rule] = MAPPING_INFO[rule]
    return mapping


if __name__ == "__main__":
  
    import argparse

    parser = argparse.ArgumentParser(description="Summarize Gumbo grammar mapping")
    parser.add_argument("--csv", metavar="FILE", nargs="?", help="write CSV output to FILE")
    args = parser.parse_args()

    mapping = generate_mapping("gumbo.lark", "gumbo_parser.py")
    if args.csv is not None:
        csv_text = mapping_to_csv(mapping)
        if args.csv:
            with open(args.csv, "w", encoding="utf-8") as fh:
                fh.write(csv_text)
        else:
            print(csv_text)
        " 'Gumbo Rule', 'Translation Rule', and 'Explanation'. Return in Markdown.\n"
        + json.dumps(mapping, indent=2)
    )
    try:
        resp = openai.ChatCompletion.create(
            model=model,
            messages=[
                {"role": "system", "content": "You are a powerful assistant to translate Gumbo contracts to SysMl v2 contracts and idoms"},
                {"role": "user", "content": prompt},
            ],
        )
        return resp.choices[0].message.content.strip()
    except Exception as exc:
        print(f"GPT call failed: {exc}", file=sys.stderr)
        return json.dumps(mapping, indent=2)


def mapping_to_csv(mapping: Dict[str, Dict[str, str]]) -> str:
    """Return the mapping as CSV-formatted text."""
    rows = ["Gumbo Rule,Translation Rule,Explanation"]
    for rule, info in mapping.items():
        translation = info.get("translation", "")
        explanation = info.get("explanation", "")
        # escape quotes
        row = [rule, translation, explanation]
        output = []
        for cell in row:
            if any(c in cell for c in [',', '"', '\n']):
                cell = '"' + cell.replace('"', '""') + '"'
            output.append(cell)
        rows.append(','.join(output))
    return "\n".join(rows)

def generate_mapping(grammar_path: str, translator_path: str) -> Dict[str, Dict[str, str]]:
    """Generate a mapping from Gumbo rules to SysML translations."""
    with open(grammar_path, "r", encoding="utf-8") as fh:
        grammar_text = fh.read()
    with open(translator_path, "r", encoding="utf-8") as fh:
        translator_src = fh.read()

    rules = set(extract_rules(grammar_text)) | set(MAPPING_INFO.keys())
    mapping: Dict[str, Dict[str, str]] = {}
    for rule in rules:
        if rule in MAPPING_INFO and re.search(rf"def\s+{rule}\b", translator_src):
            mapping[rule] = MAPPING_INFO[rule]
    return mapping


if __name__ == "__main__":
  
    import argparse

    parser = argparse.ArgumentParser(description="Summarize Gumbo grammar mapping")
    parser.add_argument("--csv", metavar="FILE", nargs="?", help="write CSV output to FILE")
    args = parser.parse_args()

    mapping = generate_mapping("gumbo.lark", "gumbo_parser.py")
    if args.csv is not None:
        csv_text = mapping_to_csv(mapping)
        if args.csv:
            with open(args.csv, "w", encoding="utf-8") as fh:
                fh.write(csv_text)
        else:
            print(csv_text)
    else:
        summary = call_gpt(mapping)
        print(summary)
