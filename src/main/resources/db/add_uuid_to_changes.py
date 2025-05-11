import os
import re

def replace_all_ids_with_fresh_sequence(root_dir):
  current_id = 1
  yaml_file_paths = []

  # Zbieramy ścieżki do wszystkich .yaml
  for dirpath, _, filenames in os.walk(root_dir):
    for filename in filenames:
      if filename.endswith(".yaml"):
        yaml_file_paths.append(os.path.join(dirpath, filename))

  # Iterujemy po plikach w stabilnej kolejności (dla powtarzalności)
  for filepath in sorted(yaml_file_paths):
    with open(filepath, "r", encoding="utf-8") as file:
      lines = file.readlines()

    new_lines = []
    modified = False

    for line in lines:
      match = re.match(r'^(\s*)id:\s*.*', line)
      if match:
        indent = match.group(1)
        new_lines.append(f"{indent}id: {current_id}\n")
        current_id += 1
        modified = True
      else:
        new_lines.append(line)

    if modified:
      with open(filepath, "w", encoding="utf-8") as file:
        file.writelines(new_lines)
      print(f"ID-y zaktualizowane w: {filepath}")

replace_all_ids_with_fresh_sequence("changelog")
