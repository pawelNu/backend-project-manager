from pathlib import Path
import shutil
import subprocess
import sys

import utils

working_dir = Path.cwd()
print(working_dir)
if working_dir.name != "docs":
    print("This script has to be started from dir 'docs'. Exiting the script...")
    sys.exit(1)
else:
    print("Directory 'docs' was confirmed. Continuing...")

package_name = "com.pawelnu.projectmanager"
artifact_id = "projectmanager-rest-api"
artifact_version = "0.1.0"
directory_path = Path(working_dir) / artifact_id

generate_rest_api_controlers_and_models = [
    "java",
    "-jar",
    Path(utils.openapi_generator_cli_path) / "openapi-generator-cli-7.12.0.jar",
    "generate",
    "-i",
    Path(working_dir) / "apiSpecification" / "openapi.yaml",
    "-g",
    "spring",
    "-o",
    directory_path,
    "--skip-validate-spec",
    "--additional-properties=useSpringBoot3=true",
    f"--additional-properties=apiPackage={package_name}.api",
    f"--additional-properties=modelPackage={package_name}.model",
    f"--additional-properties=groupId={package_name}",
    f"--additional-properties=artifactId={artifact_id}",
    f"--additional-properties=artifactVersion={artifact_version}",
    "--additional-properties=interfaceOnly=true",
    "--additional-properties=skipDefaultInterface=true",
]
subprocess.run(generate_rest_api_controlers_and_models)

generate_jar = ["mvn", "-f", Path(artifact_id, "pom.xml"), "clean", "install"]
subprocess.run(generate_jar)

try:
    shutil.rmtree(directory_path)
    print(f"Katalog {directory_path} oraz jego zawartość zostały usunięte.")
except FileNotFoundError:
    print(f"Błąd: Katalog {directory_path} nie istnieje.")
except Exception as e:
    print(f"Błąd: {e}")
