# Docs README

- [Docs README](#docs-readme)
- [Rest api docs](#rest-api-docs)
- [Generation jar with models and api](#generation-jar-with-models-and-api)

# Rest api docs

File [apiSpecification/openapi.yaml](apiSpecification/openapi.yaml)
contains rest api models and Specification.

Errors will be added by annotations inside application code.

# Generation jar with models and api

File [scripts/generate_controllers_and_models_from_api_specification.py](scripts/generate_controllers_and_models_from_api_specification.py)
contains Python script which generates and adds jar file with models and api to local repository.

`utils.openapi_generator_cli_path` contains path to directory with `openapi-generator-cli-7.12.0.jar`

Download from here https://mvnrepository.com/artifact/org.openapitools/openapi-generator-cli/7.12.0

Script has to running from docs directory.
