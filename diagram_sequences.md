# Diagram sequences

## Application diagram sequence

```mermaid
sequenceDiagram
    autonumber
    Admin ->> User: Create user
    User ->> Project: Create project
    User ->> Ticket: Create ticket for project
    Ticket ->> Project: Ticket is assigned to project
    User ->> Task: Create task for ticket
    Task ->> Ticket: Task is assigned to ticket
```
