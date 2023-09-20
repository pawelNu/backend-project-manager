# Project manager spring boot react

## Github repositories

Backend: https://github.com/pawelNu/backend-project-manager

Frontend: TODO add link to github

## Features

TODO add information about application features

## Application diagram sequence

[Application diagram sequence](diagram_sequences.md#application-diagram-sequence)

## Application entity diagram

[Application entity diagram](entities_diagram.md#application-entity-diagram)

## Create Spring Boot project

1. https://start.spring.io/
2. Project: Maven
3. Language: Java
4. Spring Boot: 3.1.3
5. Project Metadata
6. Group: com.pawelnu
7. Artifact: backend-project-manager
8. Name: backend-project-manager
9.  Description: Project Manager - backend
10. Package name: com.pawelnu.backend-project-manager
11. Packaging: Jar
12. Java: 17
13. Dependencies: 
    - Spring Web WEB
    - Spring Data JPA SQL
    - H2 Database SQL
    - Spring Boot DevTools DEVELOPER TOOLS
    - Lombok DEVELOPER TOOLS

## Configure Swagger

1. https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui
2. Add to `pom.xml`
3. In controller class add `@Tag`
4. After running application, go to: http://localhost:8080/swagger-ui/index.html
