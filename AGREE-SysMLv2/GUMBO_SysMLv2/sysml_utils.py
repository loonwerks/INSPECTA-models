# sysml_utils.py
import re
from typing import List, Tuple

GUMBO_OPEN = re.compile(r'language\s+"GUMBO"\s*/\*\{', re.IGNORECASE)
GUMBO_BLOCK = re.compile(
    r'language\s+"GUMBO"\s*/\*\{(.*?)\}\*/',
    re.IGNORECASE | re.DOTALL
)

def extract_gumbo_blocks(text: str) -> List[Tuple[int, int, str]]:
    """Return list of (start_idx, end_idx, gumbo_text) for each GUMBO block."""
    blocks: List[Tuple[int, int, str]] = []
    for m in GUMBO_BLOCK.finditer(text):
        blocks.append((m.start(), m.end(), m.group(1)))
    return blocks

def replace_blocks(text: str, replacements: List[Tuple[int, int, str]]) -> str:
    """Replace spans [(start, end, new_text)] in reverse order to keep offsets valid."""
    out = text
    for start, end, rep in sorted(replacements, key=lambda t: t[0], reverse=True):
        out = out[:start] + rep + out[end:]
    return out

def find_enclosing_part_name(text: str, idx: int) -> str:
    """
    Find the nearest preceding 'part def NAME' or 'part NAME' to prefix the contracts package.
    Fallback to 'Top' if not found.
    """
    search_span = text[:idx]
    # Prefer 'part def <Name>' first
    m = None
    for pat in (
        r'\bpart\s+def\s+([A-Za-z_][A-Za-z0-9_]*)\b',
        r'\bpart\s+([A-Za-z_][A-Za-z0-9_]*)\b',
    ):
        m = list(re.finditer(pat, search_span))
        if m:
            return m[-1].group(1)
    return "Top"

def ensure_root_contract_import(sysml_text: str) -> str:
    """
    Ensure the root package includes:  'private import GUMBO_Contracts::*;'
    Only adds it once, right after the package header line.
    """
    if "GUMBO_Contracts" in sysml_text and "private import GUMBO_Contracts::*;" in sysml_text:
        return sysml_text

    # Find the first 'package <Name> {' opening
    pkg_open = re.search(r'\bpackage\b\s+[A-Za-z_][A-Za-z0-9_]*\s*\{', sysml_text)
    if not pkg_open:
        return sysml_text  # do nothing if we can't find the root package

    insert_pos = pkg_open.end()
    inject = "\n\tprivate import GUMBO_Contracts::*;\n"
    return sysml_text[:insert_pos] + inject + sysml_text[insert_pos:]
