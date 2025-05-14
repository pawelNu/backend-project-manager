# Project Manager - Backend

<!-- TOC -->
* [Project Manager - Backend](#project-manager---backend)
* [Technology stack:](#technology-stack)
	* [Main](#main)
	* [DB](#db)
	* [Tools/libs](#toolslibs)
* [Github repositories](#github-repositories)
* [Postman collection](#postman-collection)
* [Code standards](#code-standards)
* [Commit prefixes:](#commit-prefixes)
<!-- TOC -->

## Technology stack:

#### Main
- Java 17
- Spring Boot
- Hibernate

#### DB
- Postgres

#### Tools/libs
- Liquibase
- Lombok
- Spotless

## Github repositories

Backend: https://github.com/pawelNu/backend-project-manager

Frontend: https://github.com/pawelNu/frontend-project-manager

## Postman collection

`docs/postman/ProjectManager.postman_collection.json`

## Code standards

- Always use UUID Generator (https://www.uuidgenerator.net/version4) for UUID generation
- Before merging to `main`: `mvn spotless:apply` in order to maintain code formatting and minimizing merge conflicts

## Commit prefixes:

- feat: The new feature you're adding to a particular application
- fix: A bug fix
- style: Feature and updates related to styling
- refactor: Refactoring a specific section of the codebase
- test: Everything related to testing
- docs: Everything related to documentation
- chore: Regular code maintenance. [ You can also use emojis to represent commit types]
