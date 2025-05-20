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