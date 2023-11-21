# Project Manager - Backend

<!-- TOC -->
* [Project Manager - Backend](#project-manager---backend)
* [Technology stack:](#technology-stack)
	* [Main](#main)
	* [DB](#db)
	* [Tools/libs](#toolslibs)
* [Github repositories](#github-repositories)
* [Code standards](#code-standards)
* [Commit prefixes:](#commit-prefixes)
<!-- TOC -->

## Technology stack:

#### Main
- Java 17
- Spring Boot
- Hibernate

#### DB
- H2

#### Tools/libs
- Liquibase
- Lombok
- Spotless

## Github repositories

Backend: https://github.com/pawelNu/backend-project-manager

Frontend: https://github.com/pawelNu/frontend-project-manager

## Code standards

- Always use UUID Generator (https://www.uuidgenerator.net/version4) for UUID generation
- Before merging to `main`: `mvn spotless:apply` in order to maintain code formatting and minimizing merge conflicts

## Commit prefixes:

- feat (issue_no): The new feature you're adding to a particular application
- fix (issue_no): A bug fix
- style (issue_no): Feature and updates related to styling
- refactor (issue_no): Refactoring a specific section of the codebase
- test (issue_no): Everything related to testing
- docs (issue_no): Everything related to documentation
- chore (issue_no): Regular code maintenance. [ You can also use emojis to represent commit types]
