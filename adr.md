# Project Manager

## Architecture Decision Record

### 2025-04-19

| Decision                                             | Reason                                       |
|------------------------------------------------------|----------------------------------------------|
| Spring Boot chosen for backend stack.                | Most popular web framework                   |
| Data base hosting https://remotemysql.com/ rejected. | Users report problems with the site          |
| Postgres chosen for database.                        | One of the most popular relational databases |
| React chosen for frontend                            | Popular frontend library. Good documentation |
| Render chosen for hosting                            | Free options, alternative for Heroku         |
| Liquibase chosen for database management             | Good documentation. Already had experience   |

### 2025-04-21

| Decision                                                | Reason                                                                      |
|---------------------------------------------------------|-----------------------------------------------------------------------------|
| Abandoning api and models generation from specification | Unnecessary additional work and no ability to customize validation messages |

### 2025-04-27

| Decision                | Reason                      |
|-------------------------|-----------------------------|
| Abandoning erm diagrams | Unnecessary additional work |

### Example table

| Decision | Reason |
|----------|--------|
| x        | x      |
