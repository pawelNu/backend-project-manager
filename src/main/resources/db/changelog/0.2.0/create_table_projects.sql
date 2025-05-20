create table projects (
    id uuid not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    version integer not null,
    name varchar(255) not null,
    category_value_id uuid,
    company_id uuid,
    assigned_employee_id uuid,
    priority_id uuid,
    primary key (id)
);
alter table if exists projects
add constraint fk_projects_and_category_values foreign key (category_value_id) references category_values;
alter table if exists projects
add constraint fk_projects_and_companies foreign key (company_id) references companies;
alter table if exists projects
add constraint fk_projects_and_employees foreign key (assigned_employee_id) references employees;
alter table if exists projects
add constraint fk_projects_and_priorities foreign key (priority_id) references priorities;