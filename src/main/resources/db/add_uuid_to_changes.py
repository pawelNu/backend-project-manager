import os
import uuid
import re

def is_uuid(s):
  """Sprawdza, czy dany string to UUID"""
  try:
    val = uuid.UUID(s)
    return str(val) == s
  except ValueError:
    return False

def replace_id_with_uuid(root_dir):
  for dirpath, _, filenames in os.walk(root_dir):
    for filename in filenames:
      if filename.endswith(".yaml"):
        filepath = os.path.join(dirpath, filename)
        with open(filepath, "r", encoding="utf-8") as file:
          lines = file.readlines()

        modified = False
        new_lines = []

        for line in lines:
          match = re.match(r'^(\s*)id:\s*(.+)', line)
          if match:
            indent, value = match.groups()
            value = value.strip()
            if not is_uuid(value):
              new_uuid = str(uuid.uuid4())
              new_line = f"{indent}id: {new_uuid}\n"
              new_lines.append(new_line)
              modified = True
            else:
              new_lines.append(line)
          else:
            new_lines.append(line)

        if modified:
          with open(filepath, "w", encoding="utf-8") as file:
            file.writelines(new_lines)
          print(f"Zmieniono ID w pliku: {filepath}")

replace_id_with_uuid("changelog")
