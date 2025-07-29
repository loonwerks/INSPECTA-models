import re
from typing import List, Tuple


def extract_gumbo_blocks(text: str) -> List[Tuple[int, int, str]]:
    """Find all ``language \"GUMBO\" /*{ ... }*/`` blocks in ``text``.

    Returns a list of tuples ``(start, end, content)`` where ``start`` and ``end``
    are character indices covering the full block (including the surrounding
    ``language"GUMBO"`` prefix and comment delimiters) and ``content`` is the
    inner Gumbo source without the ``/*{`` and ``}*/`` markers.
    """
    # Some models omit the closing '}' before the end of the comment. Allow it
    # to be optional so the pattern still matches.
    pattern = re.compile(r'language\s+"GUMBO"\s*/\*\{(.*?)\}?\s*\*/', re.DOTALL)
    blocks = []
    for m in pattern.finditer(text):
        blocks.append((m.start(), m.end(), m.group(1)))
    return blocks


def find_enclosing_part_name(text: str, idx: int) -> str:
    """Return the part/thread name preceding ``idx`` if found."""
    part_pattern = re.compile(r'part\s+def\s+(\w+)')
    part_name = "GumboBlock"
    for m in part_pattern.finditer(text, 0, idx):
        part_name = m.group(1)
    return part_name


def replace_blocks(text: str, replacements: List[Tuple[int, int, str]]) -> str:
    """Replace slices in ``text`` with new strings.

    ``replacements`` is a list of ``(start, end, new_text)``.  The list must be
    sorted by ``start``.  Replacement happens from the end towards the beginning
    to avoid shifting indices.
    """
    replacements = sorted(replacements, key=lambda x: x[0], reverse=True)
    for start, end, new in replacements:
        text = text[:start] + new + text[end:]
    return text
