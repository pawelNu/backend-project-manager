# Project manager spring boot react

- [Project manager spring boot react](#project-manager-spring-boot-react)
  - [Github repositories](#github-repositories)
  - [Features](#features)
  - [Application diagram sequence](#application-diagram-sequence)
  - [Application entity diagram](#application-entity-diagram)
  - [Create Spring Boot project](#create-spring-boot-project)
    - [Configure Swagger](#configure-swagger)
    - [Random data generator Mockaroo](#random-data-generator-mockaroo)
  - [Install TypeScript](#install-typescript)
  - [Create React application](#create-react-application)

## Github repositories

Backend: https://github.com/pawelNu/backend-project-manager

Frontend: https://github.com/pawelNu/frontend-project-manager

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

### Configure Swagger

1. https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui
2. Add to `pom.xml`
3. In controller class add `@Tag`
4. After running application, go to: http://localhost:8080/swagger-ui/index.html

### Random data generator Mockaroo

https://www.mockaroo.com/

## Install TypeScript

1. Install globally `npm install -g typescript`
2. Check version `tsc --version`

## Create React application

1. In terminal 
    ```shell
    npx create-react-app frontend-project-manager --template typescript
    ```
2. Add Bootstrap: https://getbootstrap.com/docs/5.2/getting-started/introduction/#quick-start
3. file `public/index.html`
   Add CSS
    ```html
        <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
    ```
    Add JavaScript
    ```html
     <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4" crossorigin="anonymous"></script>
    ```
