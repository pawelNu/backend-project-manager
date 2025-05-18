/*
 v0.1.0
 */
/*
 execute with postgres user
 */
DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA IF NOT EXISTS public;
--CREATE USER developer WITH PASSWORD 'developer';
GRANT CONNECT ON DATABASE "project-manager-dev" TO developer;
GRANT USAGE ON SCHEMA public TO developer;
GRANT CREATE ON SCHEMA public TO developer;
GRANT SELECT,
    INSERT,
    UPDATE,
    DELETE ON ALL TABLES IN SCHEMA public TO developer;
ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT SELECT,
    INSERT,
    UPDATE,
    DELETE ON TABLES TO developer;
GRANT REFERENCES,
    TRIGGER ON ALL TABLES IN SCHEMA public TO developer;
/*
 v0.2.0
 */
create table companies (
    id uuid not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    version integer not null,
    regon varchar(9) not null,
    nip varchar(10) not null,
    name varchar(255) not null,
    status varchar(255) not null check (status in ('ACTIVE', 'TERMINATED')),
    website varchar(255) not null,
    is_deleted boolean not null,
    primary key (id)
);
/*
 v0.3.0
 */
create table company_addresses (
    id uuid not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    version integer not null,
    address_type varchar(255) not null,
    city varchar(255) not null,
    country varchar(255) not null,
    email_address varchar(255) not null,
    phone_number varchar(255) not null,
    street varchar(255) not null,
    street_number varchar(255) not null,
    zip_code varchar(255) not null,
    company_id uuid not null,
    is_deleted boolean not null,
    primary key (id)
);
alter table if exists company_addresses
add constraint fk_company_addresses_and_companies foreign key (company_id) references companies;
/*
 v0.4.0
 */
create table employees (
    id uuid not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    version integer not null,
    email varchar(255) not null,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    password varchar(255) not null,
    phone_number varchar(255) not null,
    username varchar(255) not null,
    company_id uuid not null,
    is_deleted boolean not null,
    primary key (id)
);
alter table if exists employees
add constraint fk_employees_and_companies foreign key (company_id) references companies;
/*
 v0.5.0
 */
create table authorities (
    id uuid not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    version integer not null,
    name varchar(255) not null,
    is_deleted boolean not null,
    primary key (id)
);
/*
 v0.6.0
 */
create table employee_authorities (
    id uuid not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    version integer not null,
    authority_id uuid,
    employee_id uuid,
    is_deleted boolean not null,
    primary key (id)
);
alter table if exists employee_authorities
add constraint fk_employee_authorities_and_authorities foreign key (authority_id) references authorities;
alter table if exists employee_authorities
add constraint fk_employee_authorities_and_employees foreign key (employee_id) references employees;
/*
 v0.7.0
 */
create table category_types (
    id uuid not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    version integer not null,
    name varchar(255) not null,
    primary key (id)
);
/*
 v0.8.0
 */
create table type_values (
    id uuid not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    version integer not null,
    category_type_id uuid not null,
    numeric_value numeric(10, 2),
    string_value varchar(255),
    date_value timestamp(6) without time zone,
    primary key (id)
);
alter table if exists type_values
add constraint fk_type_values_and_category_types foreign key (category_type_id) references category_types;
/*
 v0.9.0
 */
create table priorities (
    id uuid not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    version integer not null,
    name varchar(255) not null,
    type_value_id uuid not null,
    primary key (id)
);
alter table if exists priorities
add constraint fk_priorities_and_type_values foreign key (type_value_id) references type_values;
/*
 v0.10.0
 */
create table project_steps (
    id uuid not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    version integer not null,
    name varchar(255) not null,
    priority_id uuid,
    deadline timestamp(6) without time zone,
    primary key (id)
);
alter table if exists project_steps
add constraint fk_project_steps_and_priorities foreign key (priority_id) references priorities;
/*
 v0.11.0
 */
create table project_step_comments (
    id uuid not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    version integer not null,
    comment varchar(255) not null,
    project_step_id uuid,
    employee_id uuid,
    primary key (id)
);
alter table if exists project_step_comments
add constraint fk_project_step_comments_and_project_steps foreign key (project_step_id) references project_steps;
alter table if exists project_step_comments
add constraint fk_project_step_comments_and_employees foreign key (employee_id) references employees;
/*
 v0.12.0
 */
create table projects (
    id uuid not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    version integer not null,
    name varchar(255) not null,
    type_value_id uuid,
    company_id uuid,
    assigned_employee_id uuid,
    priority_id uuid,
    primary key (id)
);
alter table if exists projects
add constraint fk_projects_and_type_values foreign key (type_value_id) references type_values;
alter table if exists projects
add constraint fk_projects_and_companies foreign key (company_id) references companies;
alter table if exists projects
add constraint fk_projects_and_employees foreign key (assigned_employee_id) references employees;
alter table if exists projects
add constraint fk_projects_and_priorities foreign key (priority_id) references priorities;
/*
 v0.13.0
 */
create table tickets (
    id uuid not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    version integer not null,
    ticket_number varchar(255) not null,
    title varchar(255) not null,
    type_value_id uuid,
    deadline timestamp(6) without time zone,
    priority_id uuid,
    additionalDetails varchar(1000) not null,
    project_id uuid,
    project_step_id uuid,
    primary key (id)
);
alter table if exists tickets
add constraint fk_tickets_and_type_values foreign key (type_value_id) references type_values;
alter table if exists tickets
add constraint fk_tickets_and_priorities foreign key (priority_id) references priorities;
alter table if exists tickets
add constraint fk_tickets_and_projects foreign key (project_id) references projects;
alter table if exists tickets
add constraint fk_tickets_and_project_steps foreign key (project_step_id) references project_steps;
/*
 v0.14.0
 */
create table attachments (
    id uuid not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    version integer not null,
    name varchar(255) not null,
    path_to_file text not null,
    project_id uuid,
    ticket_id uuid,
    primary key (id)
);
alter table if exists attachments
add constraint fk_attachments_and_projects foreign key (project_id) references projects;
alter table if exists attachments
add constraint fk_attachments_and_tickets foreign key (ticket_id) references tickets;
/*
 v0.15.0
 */
create table ticket_histories (
    id uuid not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    version integer not null,
    name varchar(255) not null,
    ticket_id uuid,
    from_status_id uuid,
    to_status_id uuid,
    from_employee_id uuid,
    to_employee_id uuid,
    comment text,
    primary key (id)
);
alter table if exists ticket_histories
add constraint fk_ticket_histories_and_tickets foreign key (ticket_id) references tickets;
alter table if exists ticket_histories
add constraint fk_ticket_histories_and_type_values_from_status foreign key (from_status_id) references type_values;
alter table if exists ticket_histories
add constraint fk_ticket_histories_and_type_values_to_status foreign key (to_status_id) references type_values;
alter table if exists ticket_histories
add constraint fk_ticket_histories_and_employees_from_employee foreign key (from_employee_id) references employees;
alter table if exists ticket_histories
add constraint fk_ticket_histories_and_employees_to_employee foreign key (to_employee_id) references employees;