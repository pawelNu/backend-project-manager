/*
 v0.2.0
 */
--
--
create table categories (
    id uuid not null,
    name varchar(255) not null,
    version integer not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    is_deleted boolean not null,
    primary key (id)
);
--
--
create table category_values (
    id uuid not null,
    category_id uuid not null,
    numeric_value numeric(10, 2),
    string_value varchar(255),
    date_value timestamp(6) without time zone,
    version integer not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    is_deleted boolean not null,
    primary key (id)
);
alter table if exists category_values
add constraint fk_category_values_and_categories foreign key (category_id) references categories;
--
--
create table companies (
    id uuid not null,
    name varchar(255) not null,
    nip varchar(10) not null,
    regon varchar(9) not null,
    website varchar(255) not null,
    status_id uuid,
    version integer not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    is_deleted boolean not null,
    primary key (id)
);
alter table if exists companies
add constraint fk_companies_and_category_values foreign key (status_id) references category_values;
--
--
create table company_addresses (
    id uuid not null,
    street varchar(255) not null,
    street_number varchar(255) not null,
    city varchar(255) not null,
    zip_code varchar(255) not null,
    country varchar(255) not null,
    phone_number varchar(255) not null,
    email_address varchar(255) not null,
    address_type varchar(255) not null,
    company_id uuid not null,
    version integer not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    is_deleted boolean not null,
    primary key (id)
);
alter table if exists company_addresses
add constraint fk_company_addresses_and_companies foreign key (company_id) references companies;
--
--
create table employees (
    id uuid not null,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    username varchar(255) not null,
    password varchar(255) not null,
    email varchar(255) not null,
    phone_number varchar(255) not null,
    role_id uuid,
    company_id uuid not null,
    version integer not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    is_deleted boolean not null,
    primary key (id)
);
alter table if exists employees
add constraint fk_employees_and_category_values foreign key (role_id) references category_values;
alter table if exists employees
add constraint fk_employees_and_companies foreign key (company_id) references companies;
--
--
create table authorities (
    id uuid not null,
    name_backend varchar(255) not null,
    name_frontend varchar(255),
    version integer not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    is_deleted boolean not null,
    primary key (id)
);
--
--
create table employee_authorities (
    id uuid not null,
    authority_id uuid,
    employee_id uuid,
    version integer not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    is_deleted boolean not null,
    primary key (id)
);
alter table if exists employee_authorities
add constraint fk_employee_authorities_and_authorities foreign key (authority_id) references authorities;
alter table if exists employee_authorities
add constraint fk_employee_authorities_and_employees foreign key (employee_id) references employees;
--
--
create table projects (
    id uuid not null,
    name varchar(255) not null,
    category_id uuid,
    company_id uuid,
    assigned_employee_id uuid,
    priority_id uuid,
    version integer not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    is_deleted boolean not null,
    primary key (id)
);
alter table if exists projects
add constraint fk_projects_and_category_values_category foreign key (category_id) references category_values;
alter table if exists projects
add constraint fk_projects_and_companies foreign key (company_id) references companies;
alter table if exists projects
add constraint fk_projects_and_employees foreign key (assigned_employee_id) references employees;
alter table if exists projects
add constraint fk_projects_and_category_values_priority foreign key (priority_id) references category_values;
--
--
create table project_steps (
    id uuid not null,
    name varchar(255) not null,
    project_id uuid,
    priority_id uuid,
    deadline timestamp(6) without time zone,
    version integer not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    is_deleted boolean not null,
    primary key (id)
);
alter table if exists project_steps
add constraint fk_project_steps_and_projects foreign key (project_id) references projects;
alter table if exists project_steps
add constraint fk_project_steps_and_category_values foreign key (priority_id) references category_values;
--
--
create table project_step_comments (
    id uuid not null,
    comment varchar(255) not null,
    project_step_id uuid,
    employee_id uuid,
    version integer not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    is_deleted boolean not null,
    primary key (id)
);
alter table if exists project_step_comments
add constraint fk_project_step_comments_and_project_steps foreign key (project_step_id) references project_steps;
alter table if exists project_step_comments
add constraint fk_project_step_comments_and_employees foreign key (employee_id) references employees;
--
--
create table tickets (
    id uuid not null,
    ticket_number varchar(255) not null,
    title varchar(255) not null,
    category_id uuid,
    deadline timestamp(6) without time zone,
    priority_id uuid,
    additional_details varchar(1000) not null,
    project_id uuid,
    project_step_id uuid,
    version integer not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    is_deleted boolean not null,
    primary key (id)
);
alter table if exists tickets
add constraint fk_tickets_and_category_values_category foreign key (category_id) references category_values;
alter table if exists tickets
add constraint fk_tickets_and_category_values_priority foreign key (priority_id) references category_values;
alter table if exists tickets
add constraint fk_tickets_and_projects foreign key (project_id) references projects;
alter table if exists tickets
add constraint fk_tickets_and_project_steps foreign key (project_step_id) references project_steps;
--
--
create table attachments (
    id uuid not null,
    name varchar(255) not null,
    path_to_file text not null,
    project_id uuid,
    ticket_id uuid,
    version integer not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    is_deleted boolean not null,
    primary key (id)
);
alter table if exists attachments
add constraint fk_attachments_and_projects foreign key (project_id) references projects;
alter table if exists attachments
add constraint fk_attachments_and_tickets foreign key (ticket_id) references tickets;
--
--
create table ticket_histories (
    id uuid not null,
    ticket_id uuid,
    from_status_id uuid,
    to_status_id uuid,
    from_employee_id uuid,
    to_employee_id uuid,
    comment text,
    version integer not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    is_deleted boolean not null,
    primary key (id)
);
alter table if exists ticket_histories
add constraint fk_ticket_histories_and_tickets foreign key (ticket_id) references tickets;
alter table if exists ticket_histories
add constraint fk_ticket_histories_and_category_values_from_status foreign key (from_status_id) references category_values;
alter table if exists ticket_histories
add constraint fk_ticket_histories_and_category_values_to_status foreign key (to_status_id) references category_values;
alter table if exists ticket_histories
add constraint fk_ticket_histories_and_employees_from_employee foreign key (from_employee_id) references employees;
alter table if exists ticket_histories
add constraint fk_ticket_histories_and_employees_to_employee foreign key (to_employee_id) references employees;