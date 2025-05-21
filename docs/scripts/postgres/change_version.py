import re

def renumerate_versions(filename):
    pattern = re.compile(r'v\d+\.\d+\.\d+')
    counter = 1

    def replacer(_):
        nonlocal counter
        new_version = f'v0.{counter}.0'
        counter += 1
        return new_version

    with open(filename, 'r', encoding='utf-8') as f:
        content = f.read()

    new_content = pattern.sub(replacer, content)

    with open(filename, 'w', encoding='utf-8') as f:
        f.write(new_content)

    print(f"Zaktualizowano {counter - 1} wersji w pliku {filename}")

# Użycie:
renumerate_versions('schema_plan.sql')
