create table tickets (
    id uuid not null,
    created timestamp(6) without time zone not null,
    last_modified timestamp(6) without time zone not null,
    version integer not null,
    ticket_number varchar(255) not null,
    title varchar(255) not null,
    category_value_id uuid,
    deadline timestamp(6) without time zone,
    priority_id uuid,
    additionalDetails varchar(1000) not null,
    project_id uuid,
    project_step_id uuid,
    primary key (id)
);
alter table if exists tickets
add constraint fk_tickets_and_category_values foreign key (category_value_id) references category_values;
alter table if exists tickets
add constraint fk_tickets_and_priorities foreign key (priority_id) references priorities;
alter table if exists tickets
add constraint fk_tickets_and_projects foreign key (project_id) references projects;
alter table if exists tickets
add constraint fk_tickets_and_project_steps foreign key (project_step_id) references project_steps;